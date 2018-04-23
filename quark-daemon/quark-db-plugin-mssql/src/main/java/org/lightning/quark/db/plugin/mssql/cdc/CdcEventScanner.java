package org.lightning.quark.db.plugin.mssql.cdc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.lightning.quark.core.model.db.RowDataInfo;
import org.lightning.quark.core.model.metadata.MetaColumn;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.row.CdcInfo;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.core.utils.Q;
import org.lightning.quark.db.dispatcher.RowChangeDispatcher;
import org.lightning.quark.core.utils.CdcUtils;
import org.lightning.quark.db.utils.DsUtils;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.db.meta.MetadataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 考虑cdc时间, 使用last-scan-time,
 * 带缓存, 缓存已处理过的lsn
 */
public class CdcEventScanner {

    private static final Logger logger = LoggerFactory.getLogger(CdcEventScanner.class);

    private static final String CDC_SQL = "DECLARE @bglsn VARBINARY(max) = sys.fn_cdc_map_time_to_lsn('smallest greater than or equal', ?);\n" +
            "        DECLARE @edlsn VARBINARY(max) = sys.fn_cdc_map_time_to_lsn('largest less than or equal', ?);\n" +
            "        select [__$start_lsn] as startLsn, [__$seqval] as seqval, sys.fn_cdc_map_lsn_to_time(__$start_lsn) as updateTime,\n" +
            "        {pkColumns}\n" +
            "        from cdc.dbo_{tableName}_CT WITH (NOLOCK)\n" +
            "        where [__$start_lsn] >= @bglsn\n" +
            "        and [__$start_lsn] <= @edlsn ";

    private static final AtomicLong counter = new AtomicLong();

    private RowChangeDispatcher dispatcher;
    private MetadataManager metadataManager;
    private DbManager dbManager;

    private List<String> cdcTables = Lists.newArrayList();

    private Map<String, Date> lastScanLsns = Maps.newHashMap();

    /**
     * 扫描间隔
     */
    @Getter
    @Setter
    private long scanRoundIntervel = 5 * 1000L;


    /**
     * start_lsn -- seqval
     */
    private Cache<Long, List<Long>> cachedLsn = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    public CdcEventScanner(RowChangeDispatcher dispatcher, MetadataManager metadataManager, DbManager dbManager) {
        this.dispatcher = dispatcher;
        this.metadataManager = metadataManager;
        this.dbManager = dbManager;
    }

    public void addScanTable(String tableName) {
        cdcTables.add(tableName);
    }

    /**
     *
     */
    public void startScanJob() {
        while (true) {
            startScanAndDispatch();
            Q.sleep(scanRoundIntervel);
        }
    }

    /**
     * // TODO: 目前使用并行，后续改为akka
     */
    public void startScanAndDispatch() {
        logger.info("one round start");

        Map<String, List<CdcInfo>> cdcMap = fetchCdcs();

        cdcMap.forEach((tbl, cdcs) -> {
            if (CollectionUtils.isEmpty(cdcs)) {
                return;
            }

            Lists.partition(cdcs, 50).forEach(cdcInfos -> {
                RowChangeEvent changeEvent = new RowChangeEvent();
                changeEvent.setTableName(tbl);
                changeEvent.setDbName(DsUtils.getDbName(metadataManager.getDataSource()));
                changeEvent.setChanges(cdcInfos.stream().map(x -> {
                    RowChange rowChange = new RowChange();
                    rowChange.setCurrentRow(new RowDataInfo(x.getPk()));
                    return rowChange;
                }).collect(Collectors.toList()));

                dispatcher.dispatch(changeEvent);
            });

        });

        logger.info("one round done");

    }

    public Map<String, List<CdcInfo>> fetchCdcs() {
        Map<String, List<CdcInfo>> cdcMap = Maps.newConcurrentMap();

        cdcTables.parallelStream().forEach(tbl -> {
            Date startTime = lastScanLsns.getOrDefault(tbl, new Date(System.currentTimeMillis() - 15 * 60 * 1000));
            Date endTime = new Date(System.currentTimeMillis() + 10 * 1000);

            try {
                List<Map<String, Object>> changeIds = queryChanges(tbl, startTime, endTime);

                MetaTable metaTable = metadataManager.getTable(tbl);
                List<CdcInfo> cdcs = changeIds.stream()
                        .map(r -> convertToCdcInfo(metaTable, r))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                cdcMap.put(tbl, cdcs);
            } catch (Exception e) {
                logger.error("fetch cdcs error", e);
            }
        });

        return cdcMap;
    }

    private CdcInfo convertToCdcInfo(MetaTable metaTable, Map<String, Object> r) {
        byte[] startLsn = (byte[]) r.get("startLsn");
        byte[] seqval = (byte[]) r.get("seqval");
        if (isLsnCached(startLsn, seqval)) {
            logger.info("scanned but already processed, startLsn is {}, seqval is {}", CdcUtils.bytesToLong(startLsn), CdcUtils.bytesToLong(seqval));
            return null;
        } else {
            cacheLsn(startLsn, seqval);
        }

        logger.info("scanned startLsn is {}, seqval is {}", CdcUtils.bytesToLong(startLsn), CdcUtils.bytesToLong(seqval));
        RowDataInfo info = metaTable.convertRow(r);

        CdcInfo cdc = new CdcInfo();
        cdc.setStartLsn(startLsn);
        cdc.setSeqval(seqval);
        cdc.setPk(info.getPk());
        return cdc;
    }

    private void cacheLsn(byte[] startLsn, byte[] seqval) {
        long lsn = CdcUtils.bytesToLong(startLsn);
        List<Long> seqs = cachedLsn.getIfPresent(lsn);
        if (seqs == null) {
            seqs = Lists.newArrayList();
            cachedLsn.put(lsn, seqs);
        }
        seqs.add(CdcUtils.bytesToLong(seqval));
    }

    private boolean isLsnCached(byte[] startLsn, byte[] seqval) {
        List<Long> seqs = cachedLsn.getIfPresent(CdcUtils.bytesToLong(startLsn));
        if (CollectionUtils.isNotEmpty(seqs) && seqs.contains(CdcUtils.bytesToLong(seqval))) {
            return true;// 已处理过
        }
        return false;
    }

    private List<Map<String, Object>> queryChanges(String tableName, Date startTime, Date endTime) {
        MetaTable metaTable = metadataManager.getTable(tableName);
        String pkColumnNames = metaTable.getPrimaryKey().getColumns().stream()
                .map(MetaColumn::getName)
                .collect(Collectors.joining(","));
        String sql = CDC_SQL.replaceAll("\\{tableName}", tableName)
                .replaceAll("\\{pkColumns}", pkColumnNames);

        return dbManager.queryAsMap(sql, startTime, endTime);
    }

}
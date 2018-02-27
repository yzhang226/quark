package org.lightning.quark.db.copy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.db.RowDataInfo;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.db.sql.SqlProvider;
import org.lightning.quark.db.sql.SqlProviderFactory;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/2/26
 */
public class DataRowManager {

    private MetaTable table;

    private DataSource dataSource;

    private SqlProvider sqlProvider;

    private DbManager dbManager;

    public DataRowManager(MetaTable table, DataSource dataSource) {
        this.table = table;
        this.dataSource = dataSource;
        this.dbManager = new DbManager(dataSource);
        this.sqlProvider = SqlProviderFactory.createProvider(table.getCatalog().getDatabase().getVendor(), table);
    }

    public int processByStep(PKData startPk, int pageSize) {
        // 1. 获取源 - 步长内数据
        List<Map<String, Object>> sourceRows = fetchRowsByStep(startPk, pageSize);
        if (CollectionUtils.isEmpty(sourceRows)) {
            return 0;
        }

        // 2. 获取目标 - [startPk, endPk] 区间的数据
        PKData endPk = getLastRowPk(sourceRows);
        List<Map<String, Object>> targetRows = fetchRowsByRangeClosed(startPk, endPk);

        // 3. 比对 两批 步长数据

        // 4. 执行 insert/update/delete

        return 0;
    }

    private Map<PKData, RowDataInfo> convertRow(List<Map<String, Object>> rows) {
        if (CollectionUtils.isEmpty(rows)) {
            return Collections.emptyMap();
        }

        Map<PKData, RowDataInfo> pkRows = Maps.newHashMap();
        for (Map<String, Object> row : rows) {
            PKData pk = new PKData();
            table.getPrimaryKey().getColumns().forEach(col -> {
                pk.addOnePk(col.getName(), row.get(col.getName()));
            });

            RowDataInfo rowDataInfo = new RowDataInfo();
            rowDataInfo.setPk(pk);
            rowDataInfo.setRow(row);
            pkRows.put(pk, rowDataInfo);
        }

        return pkRows;
    }

    private PKData getLastRowPk(List<Map<String, Object>> sourceRows) {
        Map<String, Object> row = sourceRows.get(sourceRows.size() - 1);

        PKData endPk = new PKData();

        row.forEach((name, value) -> {
            if (table.isPrimaryKey(name)) {
                endPk.addOnePk(name, value);
            }
        });

        return endPk;
    }

    /**
     * 按步长 取数据
     * @param startPk
     * @param pageSize
     * @return
     */
    public List<Map<String, Object>> fetchRowsByStep(PKData startPk, int pageSize) {
        String sql = sqlProvider.prepareQueryRowByStep(startPk, pageSize);
        List<Object> params = startPk.getValues();
        params.add(pageSize);
        return dbManager.queryAsMap(sql, params.toArray());
    }

    /**
     * 获取 [startPk, endPk] 区间的数据
     * @param startPk
     * @param endPk
     * @return
     */
    public List<Map<String, Object>> fetchRowsByRangeClosed(PKData startPk, PKData endPk) {
        String sql = sqlProvider.prepareQueryRowByRangeClosed(startPk, endPk);
        List<Object> startParams = startPk.getValues();
        List<Object> endParams = endPk.getValues();
        startParams.addAll(endParams);
        return dbManager.queryAsMap(sql, startParams.toArray());
    }



    public List<Map<String, Object>> insertRows(List<Map<String, Object>> rows) {

        return null;
    }

}

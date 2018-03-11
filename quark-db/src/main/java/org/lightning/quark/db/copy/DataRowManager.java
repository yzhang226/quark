package org.lightning.quark.db.copy;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.lightning.quark.core.diff.RowDifference;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.row.TableColumnMapping;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.db.sql.SqlProvider;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/2/26
 */
public class DataRowManager {

    private MetaTable table;

    private DataSource dataSource;

    private SqlProvider sqlProvider;

    private DbManager dbManager;

    private TableColumnMapping columnMapping;

    public DataRowManager(MetaTable table, DataSource dataSource, SqlProvider sqlProvider,
                          TableColumnMapping columnMapping) {
        this.table = table;
        this.dataSource = dataSource;
        this.dbManager = new DbManager(dataSource);
        this.sqlProvider = sqlProvider;
        this.columnMapping = columnMapping;
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

    /**
     *
     * @param pks
     * @return
     */
    public List<Map<String, Object>> fetchRowsByPks(Collection<PKData> pks) {
        String sql = sqlProvider.prepareQueryRowByPks(pks);
        List<Object> params = Lists.newArrayList();
        pks.forEach(pk -> {
            params.addAll(pk.getValues());
        });

        return dbManager.queryAsMap(sql, params.toArray());
    }

    public int insertRows(List<RowDifference> rows) {
        if (CollectionUtils.isEmpty(rows)) {
            return 0;
        }

        String sql = sqlProvider.prepareInsertRow();
        List<List<Object>> paramsList = rows.stream()
                                        .map(RowDifference::getLeft)
                                        .map(row -> table.getColumns().stream()
                                                .map(col -> row.getRow().get(columnMapping.getLeftColumnName(col.getName())))
                                                .collect(Collectors.toList()))
                                        .collect(Collectors.toList());

        List<Map<String, Object>> results = dbManager.insertBatch(sql, paramsList);
        return results.size();
    }

    public int updateRows(List<RowDifference> differences) {
        if (CollectionUtils.isEmpty(differences)) {
            return 0;
        }

        final int[] updated = {0};
        differences.forEach(diff -> {
            String sql = sqlProvider.prepareUpdateRow4OneRow(diff.getResult(), diff.getLeft().getPk());
            List<Object> params = new ArrayList<>(diff.getResult().values());
            params.addAll(diff.getLeft().getPk().getValues());
            updated[0] += dbManager.update(sql, params);
        });

        return updated[0];
    }

    public int deleteRows(List<RowDifference> differences) {
        if (CollectionUtils.isEmpty(differences)) {
            return 0;
        }

        final int[] deleted = {0};

        List<PKData> pks = differences.stream()
                .map(diff -> diff.getRight().getPk())
                .collect(Collectors.toList());

        String sql = sqlProvider.prepareDeleteRowByPks(pks);

        List<Object> params = pks.stream()
                .map(PKData::getValues)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        deleted[0] += dbManager.delete(sql, params);
        return deleted[0];
    }

    public MetaTable getTable() {
        return table;
    }
}

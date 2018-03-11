package org.lightning.quark.core.model.metadata;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.db.RowDataInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by cook on 2018/2/23
 */
@Getter
@Setter
public class MetaTable {

    private MetaCatalog catalog;

    private String name;

    private String dbName;

    /**
     * catalog[.schema].tableName
     */
    private String fullName;

    /**
     *
     */
    private MetaPrimaryKey primaryKey;

    /**
     * all columns of this table
     */
    private List<MetaColumn> columns;


    //
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<String> pkNames;


    public boolean isPrimaryKey(String columnName) {
        if (pkNames != null) {
            return pkNames.contains(columnName);
        }

        synchronized (this) {
            if (pkNames == null) {
                pkNames = primaryKey.getColumns().stream()
                        .map(MetaColumn::getName)
                        .collect(Collectors.toList());
            }
        }
        return pkNames.contains(columnName);
    }


    /**
     *
     * @param rows
     * @return
     */
    public PKData getLastRowPk(List<Map<String, Object>> rows) {
        Map<String, Object> row = rows.get(rows.size() - 1);

        PKData endPk = new PKData();

        row.forEach((name, value) -> {
            if (isPrimaryKey(name)) {
                endPk.addOnePk(name, value);
            }
        });

        return endPk;
    }

    /**
     * 转换多行数据
     * @param rows
     * @return
     */
    public Map<PKData, RowDataInfo> convertRow(List<Map<String, Object>> rows) {
        if (CollectionUtils.isEmpty(rows)) {
            return Collections.emptyMap();
        }

        Map<PKData, RowDataInfo> pkRows = Maps.newHashMap();
        for (Map<String, Object> row : rows) {
            RowDataInfo rowDataInfo = convertRow(row);
            pkRows.put(rowDataInfo.getPk(), rowDataInfo);
        }

        return pkRows;
    }

    /**
     * 转换一行数据
     * @param row
     * @return
     */
    public RowDataInfo convertRow(Map<String, Object> row) {
        if (MapUtils.isEmpty(row)) {
            return null;
        }

        PKData pk = new PKData();
        getPrimaryKey().getColumns().forEach(col -> {
            pk.addOnePk(col.getName(), row.get(col.getName()));
        });

        RowDataInfo rowDataInfo = new RowDataInfo();
        rowDataInfo.setPk(pk);
        rowDataInfo.setRow(row);

        return rowDataInfo;
    }

    /**
     * 转换一行数据 - 原始行, 下标为数字(表示 - 第几列)
     * @param rawRow
     * @return
     */
    public RowDataInfo convertRawRow(Map<Integer, Object> rawRow) {
        if (MapUtils.isEmpty(rawRow)) {
            return null;
        }
        Map<String, Object> row = new HashMap<>(rawRow.size());
        rawRow.forEach((ordinal, value) -> row.put(getColumns().get(ordinal.intValue()).getName(), value));
        return convertRow(row);
    }

}

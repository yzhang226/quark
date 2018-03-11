package org.lightning.quark.core.row;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.model.metadata.MetaTable;

import java.util.Map;

/**
 * Created by cook on 2018/3/1
 */
@Getter
@Setter
public class TableColumnMapping {

    private String leftDbName;
    private String rightDbName;

    private String leftTableName;
    private String rightTableName;

    /**
     * 左 - 右列名映射
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String, String> leftRightMapping = Maps.newHashMap();

    /**
     * 右 - 左列名映射
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String, String> rightLeftMapping = Maps.newHashMap();

    public TableColumnMapping() {
    }

    public TableColumnMapping(String leftDbName, String rightDbName, String leftTableName, String rightTableName) {
        this.leftDbName = leftDbName;
        this.rightDbName = rightDbName;
        this.leftTableName = leftTableName;
        this.rightTableName = rightTableName;
    }

    /**
     *
     * @param leftTable
     * @return
     */
    public static TableColumnMapping from(MetaTable leftTable) {
        TableColumnMapping mapping = new TableColumnMapping();
        mapping.setLeftDbName(leftTable.getDbName());
        mapping.setLeftTableName(leftTable.getName());

        return mapping;
    }

    /**
     *
     * @param leftTable
     * @param rightTable
     * @return
     */
    public static TableColumnMapping from(MetaTable leftTable, MetaTable rightTable) {
        TableColumnMapping mapping = new TableColumnMapping();
        mapping.setLeftDbName(leftTable.getDbName());
        mapping.setLeftTableName(leftTable.getName());
        mapping.setRightDbName(leftTable.getDbName());
        mapping.setRightTableName(rightTable.getName());
        return mapping;
    }

    public void addMapping(String leftColumnName, String rightColumnName) {
        leftRightMapping.put(leftColumnName, rightColumnName);
        rightLeftMapping.put(rightColumnName, leftColumnName);
    }

    /**
     * 获取 左侧列名 对应的 右侧列名
     * @param leftColumnName
     * @return
     */
    public String getRightColumnName(String leftColumnName) {
        if (MapUtils.isEmpty(leftRightMapping) || !leftRightMapping.containsKey(leftColumnName)) {
            return leftColumnName;
        }
        String rightColumnName = leftRightMapping.get(leftColumnName);
        return StringUtils.isEmpty(rightColumnName) ? leftColumnName : rightColumnName;
    }

    /**
     * 获取 右侧列名 对应的 左侧列名
     * @param rightColumnName
     * @return
     */
    public String getLeftColumnName(String rightColumnName) {
        if (MapUtils.isEmpty(rightLeftMapping) || !rightLeftMapping.containsKey(rightColumnName)) {
            return rightColumnName;
        }
        String leftColumnName = rightLeftMapping.get(rightColumnName);
        return StringUtils.isEmpty(leftColumnName) ? rightColumnName : leftColumnName;
    }

    public String getTableFullName() {
        return leftDbName + "." + leftTableName;
    }

}

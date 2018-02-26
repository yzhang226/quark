package org.lightning.quark.db.sql;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.metadata.MetaColumn;
import org.lightning.quark.core.model.metadata.MetaTable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by cook on 2017/12/12
 */
public abstract class BaseSqlProvider implements SqlExecuteProvider {

    protected static final String WHERE = " WHERE ";
    protected static final String AND = " AND ";
    protected static final String OR = " OR ";
    protected static final String NOLOCK = " WITH (NOLOCK) ";
    protected static final String DESC = " DESC ";
    protected static final String ORDER_BY = " ORDER BY ";

    protected MetaTable table;

    public BaseSqlProvider(MetaTable tableDef) {
        this.table = tableDef;
    }

    protected String getColumnsText(List<MetaColumn> columns) {
        return getColumnsText(columns, null);
    }

    protected String getColumnsText(List<MetaColumn> columns, String columnSuffix) {
        return columns.stream()
                .map(x -> wrapName(x.getName()) + StringUtils.trimToEmpty(columnSuffix))
                .collect(Collectors.joining(","));
    }

    protected String getColumnNames() {
        return getColumnsText(table.getColumns());
    }

    protected String getPkNames() {
        return getPkNames(null);
    }

    protected String getPkNames(String pkSuffix) {
        return getColumnsText(getPkColumns(), pkSuffix);
    }

    protected String orderByPkNames(String ordered) {
        if (CollectionUtils.isNotEmpty(getPkColumns())) {
            return "";
        }
        return ORDER_BY + getColumnsText(getPkColumns(), ordered);
    }

    protected List<MetaColumn> getAllColumns() {
        return table.getColumns();
    }

    protected List<MetaColumn> getPkColumns() {
        return table.getPrimaryKey().getColumns();
    }

    protected String preparePkCond(PKData pk, String operator, String connector) {
        if (pk == null || CollectionUtils.isEmpty(pk.getPkValues())) {
            return "";
        }
        return connector + pk.getPkValues().stream()
                .map(pair -> wrapName(pair.getKey()) + " " + operator + " ? ")
                .collect(Collectors.joining(AND));
    }

    protected String prepareEndPkCond(PKData endPk) {
        return preparePkCond(endPk, "<", WHERE);
    }

    protected String prepareStartPkCond(PKData endPk) {
        return preparePkCond(endPk, ">=", WHERE);
    }

    protected String preparePkCond(PKData startPk, PKData endPk) {
        String cond = "";
        if (startPk != null && CollectionUtils.isNotEmpty(startPk.getPkValues())) {
            cond = preparePkCond(startPk, ">=", WHERE);
        }

        if (endPk != null && CollectionUtils.isNotEmpty(endPk.getPkValues())) {
            cond += preparePkCond(endPk, "<", StringUtils.isEmpty(cond) ? WHERE : AND);
        }

        return cond;
    }

    protected String wrappedTableName() {
        return wrapName(table.getName());
    }

    protected String wrapName(String name) {
        return getKeywordEscapeStart() + name + getKeywordEscapeEnd();
    }

    protected String preparePlaceholder(int size) {
        return IntStream.range(0, size).mapToObj(i -> "?").collect(Collectors.joining(","));
    }

    /**
     * 闭合关键字开始  [ `
     * @return
     */
    protected abstract String getKeywordEscapeStart();

    /**
     * 闭合关键字结束  ] `
     * @return
     */
    protected abstract String getKeywordEscapeEnd();

}

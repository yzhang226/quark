package org.lightning.quark.db.plugin.mysql.provider;

import org.lightning.quark.core.model.db.DbVendor;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.sql.BaseSqlProvider;
import org.lightning.quark.db.sql.SqlProviderFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/2/25
 */
public class MySQLSqlProvider extends BaseSqlProvider {

    static {
        doRegister();
    }

    public static void doRegister() {
        SqlProviderFactory.registerProvider(DbVendor.MYSQL, MySQLSqlProvider.class);
    }

    public MySQLSqlProvider(MetaTable tableDef) {
        super(tableDef);
    }

    @Override
    protected String getKeywordEscapeStart() {
        return "`";
    }

    @Override
    protected String getKeywordEscapeEnd() {
        return "`";
    }

    @Override
    public String prepareQueryRowByRange(PKData startPk, PKData endPk) {
        String sql = "SELECT {columns} FROM {tableName} {pkCond}";
        return sql.replace("{columns}", getColumnNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", preparePkCond(startPk, endPk))
                ;
    }

    @Override
    public String prepareQueryRowByRangeClosed(PKData startPk, PKData endPk) {
        String sql = "SELECT {columns} FROM {tableName} {pkCond}";
        return sql.replace("{columns}", getColumnNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", preparePkCond(startPk, endPk, ">=", "<="))
                ;
    }

    @Override
    public String prepareQueryRowByPage(int pageNo, int pageSize, PKData maxPk) {
        long offset = (pageNo - 1 ) * ((long) pageSize);
        return prepareQueryRowByOffset(offset, pageSize, maxPk);
    }

    @Override
    public String prepareQueryRowByOffset(long offset, int pageSize, PKData maxPk) {
        String sql = "SELECT {columns} FROM {tableName} {pkCond} " +
                " ORDER BY {pkName} " +
                " LIMIT {offset}, {pageSize}";
        return sql.replace("{columns}", getColumnNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", prepareEndPkCond(maxPk))
                .replace("{pkName}", getPkNames())
                .replace("{offset}", "" + offset)
                .replace("{pageSize}", "" + pageSize)
                ;
    }

    @Override
    public String prepareQueryRowByStep(PKData minPk, int pageSize) {
        String sql = "SELECT {columns} FROM {tableName} {pkCond} " +
                " ORDER BY {pkName} " +
                " limit {pageSize}";
        return sql.replace("{columns}", getColumnNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", prepareStartPkCond(minPk))
                .replace("{pkName}", getPkNames())
                .replace("{pageSize}", "?")
                ;
    }

    @Override
    public String prepareQueryRowByPks(List<PKData> pks) {
        String sql =  "SELECT {columns} FROM {tableName} {pkCond} " +
                " ORDER BY {pkName} " ;

        String pkCod = BaseSqlProvider.WHERE + pks.stream()
                .map(pk -> "( " + preparePkCond(pk, "=", "") + " )")
                .collect(Collectors.joining(BaseSqlProvider.OR));

        return sql.replace("{columns}", getColumnNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", pkCod)
                .replace("{pkName}", getPkNames())
                ;
    }

    @Override
    public String prepareQueryRowByRowNo(long startRowNo, long endRowNo) {
        return prepareQueryRowByOffset(startRowNo, (int) (endRowNo - startRowNo), null);
    }

    @Override
    public String prepareQueryMaxPkByMaxPk(PKData maxPk) {
        String sql = "select {columns} from {tableName} {pkCond} " +
                " ORDER BY {pkName} " +
                " limit 1" ;
        return sql.replace("{columns}", getPkNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", prepareEndPkCond(maxPk))
                .replace("{pkName}", getPkNames(BaseSqlProvider.DESC))
                ;
    }

    @Override
    public String prepareQueryMinPkByMaxPk(PKData maxPk) {
        String sql = "select {columns} from {tableName} {pkCond} " +
                " ORDER BY {pkName} " +
                " limit 1" ;
        return sql.replace("{columns}", getPkNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", prepareEndPkCond(maxPk))
                .replace("{pkName}", getPkNames())
                ;
    }

    @Override
    public String prepareQueryMaxPkByStep(PKData startPk, int pageSize) {
        String sql = "select {columns} from {tableName} {pkCond} " +
                " ORDER BY {pkName} " +
                " limit {pageSize}" ;
        sql = "select * from (" + sql + ") as temp limit 1" ;

        return sql.replace("{columns}", getPkNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", prepareStartPkCond(startPk))
                .replace("{pkName}", getPkNames(BaseSqlProvider.DESC))
                .replace("{pageSize}", ""+pageSize)
                ;
    }

    @Override
    public String prepareInsertRow() {
        String sql =  "insert into {tableName} ( {columns} ) values ( {placeholder} )";
        return sql.replace("{columns}", getColumnNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{placeholder}", preparePlaceholder(table.getColumns().size()))
                ;
    }

    @Override
    public String prepareDeleteRowByPks(List<PKData> pks) {
        String sql =  "DELETE FROM {tableName} {pkCond} ";

        String pkCod = BaseSqlProvider.WHERE + pks.stream()
                .map(pk -> "( " + preparePkCond(pk, "=", "") + " )")
                .collect(Collectors.joining(BaseSqlProvider.OR));

        return sql
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", pkCod)
                ;
    }

    @Override
    public String prepareUpdateRow4OneRow(Map<String, Object> oneRow, PKData pk) {
        String sql = "UPDATE {tableName} {setCond} {pkCond} ";

        String setCond = " SET " + oneRow.keySet().stream()
                .map(column -> "" + column + "=?")
                .collect(Collectors.joining(", "));

        String pkCod = BaseSqlProvider.WHERE + preparePkCond(pk, "=", "");

        return sql
                .replace("{tableName}", wrappedTableName())
                .replace("{setCond}", setCond)
                .replace("{pkCond}", pkCod)
                ;
    }

    @Override
    public String prepareCount() {
        return prepareCount(null, null);
    }

    @Override
    public String prepareCount(PKData endPk) {
        return prepareCount(null, endPk);
    }

    @Override
    public String prepareCount(PKData startPk, PKData endPk) {
        String sql = "select count(1) as cnt from {tableName} {pkCond} " ;
        return sql
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", preparePkCond(startPk, endPk))
                ;
    }

    @Override
    public DbVendor getVender() {
        return DbVendor.MYSQL;
    }
}

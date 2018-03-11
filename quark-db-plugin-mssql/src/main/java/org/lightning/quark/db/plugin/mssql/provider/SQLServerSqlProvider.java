package org.lightning.quark.db.plugin.mssql.provider;

import org.lightning.quark.core.model.db.DbVendor;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.sql.BaseSqlProvider;
import org.lightning.quark.db.sql.SqlProviderFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/2/27
 */
public class SQLServerSqlProvider extends BaseSqlProvider {

    static {
        doRegister();
    }

    public static void doRegister() {
        SqlProviderFactory.registerProvider(DbVendor.MSSQL, SQLServerSqlProvider.class);
    }

    public SQLServerSqlProvider(MetaTable tableDef) {
        super(tableDef);
    }

    @Override
    protected String getKeywordEscapeStart() {
        return "[";
    }

    @Override
    protected String getKeywordEscapeEnd() {
        return "]";
    }

    @Override
    protected String wrappedTableName() {
        return super.wrappedTableName() + " " + NOLOCK ;
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

    /**
     * TODO: refactor limit
     * @param offset
     * @param pageSize
     * @param maxPk
     * @return
     */
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
        String sql = "SELECT top {pageSize} {columns} FROM {tableName} {pkCond} " +
                " ORDER BY {pkName} " ;
        return sql.replace("{columns}", getColumnNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", prepareStartPkCond(minPk))
                .replace("{pkName}", getPkNames())
                .replace("{pageSize}", "?")
                ;
    }

    @Override
    public String prepareQueryRowByPks(Collection<PKData> pks) {
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
        String sql = "select TOP 1 {columns} from {tableName} {pkCond} " +
                " ORDER BY {pkName} " ;
        return sql.replace("{columns}", getPkNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", prepareEndPkCond(maxPk))
                .replace("{pkName}", getPkNames(BaseSqlProvider.DESC))
                ;
    }

    @Override
    public String prepareQueryMinPkByMaxPk(PKData maxPk) {
        String sql = "select TOP 1 {columns} from {tableName} {pkCond} " +
                " ORDER BY {pkName} " ;
        return sql.replace("{columns}", getPkNames())
                .replace("{tableName}", wrappedTableName())
                .replace("{pkCond}", prepareEndPkCond(maxPk))
                .replace("{pkName}", getPkNames())
                ;
    }

    @Override
    public String prepareQueryMaxPkByStep(PKData startPk, int pageSize) {
        String sql = "select TOP {pageSize} {columns} from {tableName} {pkCond} " +
                " ORDER BY {pkName} " ;
        sql = "select TOP 1 * from (" + sql + ") as temp " ;

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
        String insertSql = sql.replace("{columns}", getColumnNames())
                .replace("{tableName}", super.wrappedTableName())
                .replace("{placeholder}", preparePlaceholder(table.getColumns().size()))
                ;

//        String resultSql =  " BEGIN TRY  " +
//                                " set IDENTITY_INSERT " + super.wrappedTableName() + " on; " +
//                            " END TRY  " +
//                            " BEGIN CATCH  " +
//                            " END CATCH;  "
//                + insertSql;

        String resultSql =  " SET IDENTITY_INSERT " + super.wrappedTableName() + " ON; " +  insertSql;

        return resultSql;
    }

    @Override
    public String prepareDeleteRowByPks(List<PKData> pks) {
        String sql =  "DELETE FROM {tableName} {pkCond} ";

        String pkCod = BaseSqlProvider.WHERE + pks.stream()
                .map(pk -> "( " + preparePkCond(pk, "=", "") + " )")
                .collect(Collectors.joining(BaseSqlProvider.OR));

        return sql
                .replace("{tableName}", super.wrappedTableName())
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
                .replace("{tableName}", super.wrappedTableName())
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
    public DbVendor getVendor() {
        return DbVendor.MSSQL;
    }
}

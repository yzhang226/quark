package org.lightning.quark.db.plugin.mssql.provider;

import org.lightning.quark.core.model.db.DbVendor;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.sql.BaseSqlProvider;
import org.lightning.quark.db.sql.SqlProviderFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/2/27
 */
public class SQLServerSqlProvider extends BaseSqlProvider {

    static {
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
    public String prepareQueryRowByRange(PKData startPk, PKData endPk) {
        return null;
    }

    @Override
    public String prepareQueryRowByRangeClosed(PKData startPk, PKData endPk) {
        return null;
    }

    @Override
    public String prepareQueryRowByPage(int pageNo, int pageSize, PKData maxPk) {
        return null;
    }

    @Override
    public String prepareQueryRowByOffset(long offset, int pageSize, PKData maxPk) {
        return null;
    }

    @Override
    public String prepareQueryRowByStep(PKData minPk, int pageSize) {
        return null;
    }

    @Override
    public String prepareQueryRowByPks(List<PKData> pks) {
        return null;
    }

    @Override
    public String prepareQueryRowByRowNo(long startRowNo, long endRowNo) {
        return null;
    }

    @Override
    public String prepareQueryMaxPkByMaxPk(PKData maxPk) {
        return null;
    }

    @Override
    public String prepareQueryMinPkByMaxPk(PKData maxPk) {
        return null;
    }

    @Override
    public String prepareQueryMaxPkByStep(PKData startPk, int pageSize) {
        return null;
    }

    @Override
    public String prepareInsertRow() {
        return null;
    }

    @Override
    public String prepareDeleteRowByPks(List<PKData> pks) {
        return null;
    }

    @Override
    public String prepareUpdateRow4OneRow(Map<String, Object> oneRow, PKData pk) {
        return null;
    }

    @Override
    public String prepareCount() {
        return null;
    }

    @Override
    public String prepareCount(PKData endPk) {
        return null;
    }

    @Override
    public String prepareCount(PKData startPk, PKData endPk) {
        return null;
    }

    @Override
    public DbVendor getVender() {
        return DbVendor.MSSQL;
    }
}

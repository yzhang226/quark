package org.lightning.quark.db.sql;

import org.lightning.quark.core.model.db.DbVendor;
import org.lightning.quark.core.model.metadata.MetaTable;

/**
 * Created by cook on 2017/12/12
 */
public abstract class SqlProviderFactory {

    /**
     *
     * @param type
     * @param table
     * @return
     */
    public static SqlProvider createProvider(DbVendor type, MetaTable table) {
//        if (type == DbVendorEnum.MSSQL) {
//            return new SQLServerSqlProvider(table);
//        } else
        if (type == DbVendor.MYSQL) {
            return new MySQLSqlProvider(table);
        }
        throw new RuntimeException("unknown type#" + type);
    }

}

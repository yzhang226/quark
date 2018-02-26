package org.lightning.quark.db.sql;

import org.lightning.quark.core.model.db.DbVendorEnum;
import org.lightning.quark.core.model.metadata.MetaTable;

/**
 * Created by cook on 2017/12/12
 */
public abstract class SqlProviderFactory {

    /**
     *
     * @param type
     * @param tableDef
     * @return
     */
    public static SqlExecuteProvider createProvider(DbVendorEnum type, MetaTable tableDef) {
//        if (type == DbVendorEnum.MSSQL) {
//            return new SQLServerSqlProvider(tableDef);
//        } else if (type == DbVendorEnum.MYSQL) {
//            return new MySQLSqlProvider(tableDef);
//        }
        throw new RuntimeException("unknown type#" + type);
    }

}

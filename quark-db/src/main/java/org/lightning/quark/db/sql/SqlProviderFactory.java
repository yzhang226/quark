package org.lightning.quark.db.sql;

import com.google.common.collect.Maps;
import org.apache.commons.beanutils.ConstructorUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.db.DbVendor;
import org.lightning.quark.core.model.metadata.MetaTable;

import java.util.Map;

/**
 * Created by cook on 2017/12/12
 */
public abstract class SqlProviderFactory {

    private static final Map<DbVendor, Class<? extends BaseSqlProvider>> registered = Maps.newHashMap();

    public static void registerProvider(DbVendor vendor, Class<? extends BaseSqlProvider> sqlProviderClass) {
        registered.put(vendor, sqlProviderClass);
    }

    /**
     *
     * @param table
     * @return
     */
    public static SqlProvider createProvider( MetaTable table) {
        return createProvider(table.getCatalog().getDatabase().getVendor(), table);
    }

    /**
     *
     * @param type
     * @param table
     * @return
     */
    public static SqlProvider createProvider(DbVendor type, MetaTable table) {
        if (registered.containsKey(type)) {
            Class<? extends BaseSqlProvider> clazz = registered.get(type);
            try {
                return ConstructorUtils.invokeConstructor(clazz, table);
            } catch (Exception e) {
                throw new QuarkExecuteException("create instance of provider#{}" + type + " error", e);
            }
        }
        throw new RuntimeException("unknown type#" + type);
    }

}

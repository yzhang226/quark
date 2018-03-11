package org.lightning.quark.core.utils;

/**
 * Created by cook on 2018/3/11
 */
public abstract class Q {

    /**
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public static String getFullName(String dbName, String tableName) {
        return dbName + "." + tableName;
    }

}

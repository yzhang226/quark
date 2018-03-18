package org.lightning.quark.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cook on 2018/3/11
 */
public abstract class Q {

    private static final Logger logger = LoggerFactory.getLogger(Q.class);

    /**
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public static String getFullName(String dbName, String tableName) {
        return dbName + "." + tableName;
    }

    /**
     *
     * @param mills
     */
    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (Exception e) {
            logger.error("sleep error", e);
        }
    }

}

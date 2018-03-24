package org.lightning.quark.db.utils;

import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.db.DataSourceParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by cook on 2018/2/11
 */
public abstract class DsUtils {

    /**
     * 转化连接参数 为 Properties
     * @param param
     * @return
     */
    public static Properties convertParamToProperties(DataSourceParam param) {
        Properties info = new Properties();

        info.put("driverClassName", param.getDriverClassName());
        info.put("url", param.getUrl());
        info.put("user", param.getUsername());
        info.put("password", param.getPassword());
        if (param.getConnectionProperties() != null ) {
            info.put("connectionProperties", param.getConnectionProperties());
        }
        if (param.getInitialSize() != null) {
            info.put("initialSize", param.getInitialSize());
        }
        return info;
    }

    public static void populatePoolConfig(DataSourceParam param, GenericObjectPool<PoolableConnection> pool) {
        if (param.getMaxTotal() != null) {
            pool.setMaxTotal(param.getMaxTotal());
        }
        if (param.getMaxIdle() != null) {
            pool.setMaxIdle(param.getMaxIdle());
        }
        if (param.getMinIdle() != null) {
            pool.setMinIdle(param.getMinIdle());
        }
        if (param.getMaxWaitMillis() != null) {
            pool.setMaxWaitMillis(param.getMaxWaitMillis());
        }
        // TODO: change to config
        pool.setTimeBetweenEvictionRunsMillis(60 * 1000);
        pool.setMinEvictableIdleTimeMillis(120 * 1000);
        pool.setBlockWhenExhausted(false);
        AbandonedConfig abandonedConfig = new AbandonedConfig();
        abandonedConfig.setLogAbandoned(true);
        abandonedConfig.setRemoveAbandonedOnBorrow(true);
        abandonedConfig.setRemoveAbandonedTimeout(120);
        pool.setAbandonedConfig(abandonedConfig);
    }

    /**
     * Catalog as databaseName
     *
     * @param ds
     */
    public static String getDbName(DataSource ds) {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            return getDbName(conn);
        } catch (SQLException e) {
            throw new QuarkExecuteException("getCatalog error", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public static String getDbName(Connection conn) {
        String databaseName = null;
        try {
            databaseName = conn.getCatalog();
            return databaseName;
        } catch (Exception e) {
            throw new QuarkExecuteException("getCatalog error", e);
        }
    }

    public static String getSchema(DataSource ds) {
        String schema = null;
        Connection conn = null;
        try {
            conn = ds.getConnection();
            schema = conn.getSchema();
            return schema;
        } catch (Exception e) {
            throw new QuarkExecuteException("getSchema error", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

}

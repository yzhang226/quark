package org.lightning.quark.db.datasource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/2/25
 */
public class DbManager {

    private static final Logger logger = LoggerFactory.getLogger(DbManager.class);

    private DataSource dataSource;
    private DsRunner runner;

    public DbManager(DataSource dataSource) {
        this.dataSource = dataSource;
        runner = new DsRunner(dataSource);
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryAsMap(String sql, Object... params) {
        MapListHandler h = new MapListHandler();
        List<Map<String, Object>> res = null;
        try {
            res = runner.query(sql, h, params);
            return res;
        } catch (Exception e) {
            throw new QuarkExecuteException("queryAsMap error", e);
        }
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    public Map<String, Object> insertOne(String sql, List<Object> params) {
        MapHandler mapHandler = new MapHandler();
        Map<String, Object> res = null;
        try {
            if (CollectionUtils.isNotEmpty(params)) {
                res = runner.insert(sql, mapHandler, params.toArray());
            } else {
                res = runner.insert(sql, mapHandler);
            }
            return res;
        } catch (Exception e) {
            throw new QuarkExecuteException("insertOne error", e);
        }
    }

    /**
     *
     * @param sql
     * @param paramsList
     * @return
     */
    public List<Map<String, Object>> insertBatch(String sql, List<List<Object>> paramsList) {
        return insertBatch(null, sql, null, dataSource, paramsList);
    }

    public List<Map<String, Object>> insertBatch(String beforeSql, String sql, String afterSql,
                                                 List<List<Object>> paramsList) {
        return insertBatch(beforeSql, sql, afterSql, dataSource, paramsList);
    }
    /**
     *
     * @param sql
     * @param paramsList
     * @return
     */
    public List<Map<String, Object>> insertBatch(String beforeSql, String sql, String afterSql,
                                                 DataSource dataSource, List<List<Object>> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            logger.error("paramsList is empty");
            return null;
        }
        Object[][] arrs = new Object[paramsList.size()][];
        for (int i=0; i<paramsList.size(); i++) {
            List<Object> objs = paramsList.get(i);
            Object[] arr = objs.toArray(new Object[]{});
            arrs[i] = arr;
        }
        MapListHandler mapHandler = new MapListHandler();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            if (StringUtils.isNotBlank(beforeSql)) {
                execute(connection, beforeSql);
                logger.debug("execute before sql: {}", beforeSql);
            }
            List<Map<String, Object>> res = runner.insertBatch(connection, sql, mapHandler, arrs);

            if (StringUtils.isNotBlank(afterSql)) {
                execute(connection, beforeSql);
                logger.debug("execute before sql: {}", beforeSql);
            }
            return res == null ? Collections.emptyList() : res;
        } catch (Exception e) {
            throw new QuarkExecuteException("insertBatch error:" + sql, e);
        } finally {
//            DbUtils.closeQuietly(connection);
        }
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    public int update(String sql, List<Object> params) {
        Object[] arr = new Object[]{};
        if (CollectionUtils.isNotEmpty(params)) {
            arr = params.toArray();
        }
        try {
            return runner.update(sql, arr);
        } catch (Exception e) {
            throw new QuarkExecuteException("update error", e);
        }
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    public int delete(String sql, List<Object> params) {
        try {
            if (CollectionUtils.isNotEmpty(params)) {
                return runner.execute(sql, params.toArray());
            } else {
                return runner.execute(sql);
            }
        } catch (Exception e) {
            throw new QuarkExecuteException("update error", e);
        }
    }

    public int execute(String sql) {
        try {
            return execute(dataSource.getConnection(), sql);
        } catch (Exception e) {
            throw new QuarkExecuteException("execute error", e);
        }
    }

    public int execute(Connection conn, String sql) {
        Statement stmt = null;
        int rows = 0;
        try {
            stmt = conn.createStatement();
            rows = stmt.executeUpdate(sql);
            return rows;
        } catch (Exception e) {
            throw new QuarkExecuteException("execute error", e);
        } finally {
            DbUtils.closeQuietly(stmt);
        }
    }



}

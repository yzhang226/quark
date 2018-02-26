package org.lightning.quark.db.datasource;

import com.google.common.collect.Maps;
import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.lightning.quark.core.model.db.DataSourceParam;
import org.lightning.quark.core.utils.DsUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by cook on 2018/2/11
 */
public abstract class DSFactory {

    private static final Map<DataSourceParam, ObjectPool<PoolableConnection>> pools = Maps.newHashMap();

    /**
     * 创建数据源-Pool
     * @param param
     * @return
     */
    public static ObjectPool<PoolableConnection> getPool(DataSourceParam param) {
        ObjectPool<PoolableConnection> connectionPool = pools.get(param);
        if (connectionPool != null) {
            return connectionPool;
        }

        ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(param.getUrl(), DsUtils.convertParamToProperties(param));

        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, null);

        connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        poolableConnectionFactory.setPool(connectionPool);

        pools.put(param, connectionPool);

        return connectionPool;
    }

    /**
     * 创建Pooled的DS
     * @param param
     * @return
     */
    public static DataSource createDataSource(DataSourceParam param) {
        ObjectPool<PoolableConnection> connectionPool = getPool(param);
        PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);
        return dataSource;
    }

}

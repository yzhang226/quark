package org.lightning.quark.core.utils;

import org.lightning.quark.core.model.db.DataSourceParam;

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
        if (param.getMaxTotal() != null) {
            info.put("maxTotal", param.getMaxTotal());
        }
        if (param.getMaxIdle() != null) {
            info.put("maxIdle", param.getMaxIdle());
        }
        if (param.getMinIdle() != null) {
            info.put("minIdle", param.getMinIdle());
        }
        if (param.getMaxWaitMillis() != null) {
            info.put("maxWaitMillis", param.getMaxWaitMillis());
        }

        return info;
    }

}

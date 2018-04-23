package org.lightning.quark.test;

import org.h2.jdbcx.JdbcDataSource;
import org.lightning.quark.core.model.db.DataSourceParam;
import org.lightning.quark.db.datasource.DSFactory;

import javax.sql.DataSource;

/**
 * Created by cook on 2018/2/24
 */
public abstract class DbTestUtils {

    public static DataSourceParam createMySQLParam4Monitor() {
        return createMySQLParam("monitor");
    }

    public static DataSourceParam createMySQLParam(String dbName) {
        DataSourceParam param = new DataSourceParam();
        param.setDriverClassName("com.mysql.jdbc.Driver");
        param.setUrl("jdbc:mysql://127.0.0.1:3306/" + dbName + "?characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&nullNamePatternMatchesAll=true");
        param.setUsername("root");
        param.setPassword("6567zhyf");
        param.setMaxTotal(16);
        param.setMaxWaitMillis(2 * 1000L);
        return param;
    }

    public static DataSource createDemoMySQLDS4Monitor() {
        return DSFactory.createDataSource(createMySQLParam4Monitor());
    }

    public static DataSource createDemoMySQLDS4MyDb() {
        return DSFactory.createDataSource(createMySQLParam("mydb"));
    }

    public static DataSource createDemoSQLServerDataSource() {
        DataSourceParam param = new DataSourceParam();
        param.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        param.setUrl("jdbc:sqlserver://192.168.36.16:1433;DatabaseName=HJ_CRM");
        param.setUsername("class_coder");
        param.setPassword("A791#B578D6B64E31");
        param.setMaxTotal(16);
        param.setMaxWaitMillis(2 * 1000L);
        return DSFactory.createDataSource(param);
    }

    public static DataSource createDemoSQLServerDataSourceInDev() {
        return DSFactory.createDataSource(createDemoSQLServerDS4Dev());
    }

    /**
     * jdbc.crm_ms.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
     jdbc.crm_ms.url=jdbc:sqlserver://192.168.36.16:1433;DatabaseName=HJ_CRM
     jdbc.crm_ms.username=class_coder
     jdbc.crm_ms.password=jNVrGShhM+AOjKxzL0kiD9wiwniLy1NmDRx/owsAiRXI+TUvb4KI1nUQI0WgrIQz7MNRtoDNyB02O6wHRaLmwQ==
     jdbc.crm_ms.initialSize=2
     jdbc.crm_ms.minIdle=3
     jdbc.crm_ms.maxActive=5
     jdbc.crm_ms.maxWait=60000
     jdbc.crm_ms.timeBetweenEvictionRunsMillis=60000
     jdbc.crm_ms.minEvictableIdleTimeMillis=300000
     jdbc.crm_ms.maxPoolPreparedStatementPerConnectionSize=20
     * @return
     */

    public static DataSourceParam createDemoSQLServerDS4Dev() {
        DataSourceParam param = new DataSourceParam();
        param.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        param.setUrl("jdbc:sqlserver://192.168.21.28:2433;DatabaseName=HJ_CRM");
        param.setUsername("class_coder");
        param.setPassword("D64D@6B2A");
        param.setMaxTotal(16);
        param.setMaxWaitMillis(2 * 1000L);
        return param;
    }

    public static DataSourceParam createDemoSQLServerDS4QA() {
        DataSourceParam param = new DataSourceParam();
        param.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        param.setUrl("jdbc:sqlserver://192.168.36.16:1433;DatabaseName=HJ_CRM");
        param.setUsername("class_coder");
        param.setPassword("A791#B578D6B64E31");
        param.setMaxTotal(16);
        param.setMaxWaitMillis(2 * 1000L);
//        param.set
//        param.setMaxIdle();
        return param;
    }

    public static DataSource createDemoSQLServerDataSourceInQA() {
        return DSFactory.createDataSource(createDemoSQLServerDS4QA());
    }

    public static DataSource createDemoH2DataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:sample;DATABASE_TO_UPPER=false;INIT=RUNSCRIPT FROM 'classpath:/scripts/h2/create.sql'");
        ds.setUser("sa");
        ds.setPassword("sa");

        return ds;
    }

    /*
    jdbc.crm_ms.driverClassName=
jdbc.crm_ms.url=
jdbc.crm_ms.username=
jdbc.crm_ms.password=jNVrGShhM+AOjKxzL0kiD9wiwniLy1NmDRx/owsAiRXI+TUvb4KI1nUQI0WgrIQz7MNRtoDNyB02O6wHRaLmwQ==
jdbc.crm_ms.initialSize=2
jdbc.crm_ms.minIdle=3
jdbc.crm_ms.maxActive=5
jdbc.crm_ms.maxWait=60000
jdbc.crm_ms.timeBetweenEvictionRunsMillis=60000
jdbc.crm_ms.minEvictableIdleTimeMillis=300000
jdbc.crm_ms.maxPoolPreparedStatementPerConnectionSize=20
     */



}

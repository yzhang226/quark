package org.lightning.quark.test;

import org.junit.Test;
import org.lightning.quark.chase.subscribe.RowChangeSubscriber;
import org.lightning.quark.core.row.TableColumnMapping;
import org.lightning.quark.core.row.TableColumnMappings;
import org.lightning.quark.core.subscribe.RowChangeDispatcher;
import org.lightning.quark.db.plugin.mssql.provider.SQLServerSqlProvider;
import org.lightning.quark.db.plugin.mysql.provider.MySQLSqlProvider;
import org.lightning.quark.db.plugin.mysql.server.BinLogSalveServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;

public class MySQLToSQLServerTest extends BaseMySQLTestCase {

    private static final Logger logger = LoggerFactory.getLogger(MySQLToSQLServerTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MySQLSqlProvider.doRegister();
        SQLServerSqlProvider.doRegister();


        DataSource leftDataSource = dataSource;
        DataSource rightDataSource = DbTestUtils.createDemoSQLServerDataSourceInDev();
        logger.info("setup rightDataSource {}", rightDataSource);

        String leftDbName = "monitor";
        String rightDbName = "HJ_CRM";

        String leftTableName = "alarm_user";
        String rightTableName = "AlarmUser";

        TableColumnMapping mapping = new TableColumnMapping(leftDbName, rightDbName, leftTableName, rightTableName);
        mapping.addMapping("id", "ID");
        mapping.addMapping("user_name", "UserName");
        mapping.addMapping("email", "Email");
        mapping.addMapping("telephone", "Telephone");
        mapping.addMapping("weixin_account", "WeixinAccount");

        TableColumnMappings.register(mapping);

        RowChangeSubscriber subscriber = new RowChangeSubscriber(leftDataSource, rightDataSource);
        RowChangeDispatcher.addSubscriber(subscriber);

        RowChangeDispatcher.startListen();
    }

    @Test
    public void testBinLogParse() throws IOException, InterruptedException {
        BinLogSalveServer binLogSalveServer = new BinLogSalveServer(param);

        binLogSalveServer.start();

        Thread.sleep(30 * 60 * 1000);
    }

}

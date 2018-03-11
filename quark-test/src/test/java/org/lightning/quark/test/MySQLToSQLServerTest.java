package org.lightning.quark.test;

import org.junit.Test;
import org.lightning.quark.chase.subscribe.RowChangeSubscriber;
import org.lightning.quark.core.row.TableColumnMapping;
import org.lightning.quark.core.row.TableColumnMappings;
import org.lightning.quark.db.plugin.mysql.server.BinLogSalveServer;
import org.lightning.quark.test.base.BaseMySQLTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class MySQLToSQLServerTest extends BaseMySQLTestCase {

    private static final Logger logger = LoggerFactory.getLogger(MySQLToSQLServerTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();


        DataSource leftDataSource = dataSource;
        DataSource rightDataSource = DbTestUtils.createDemoSQLServerDataSourceInDev();
        logger.info("setup rightDataSource {}", rightDataSource);

        String leftDbName = "monitor";
        String leftTableName = "alarm_user";

        String rightDbName = "HJ_CRM";
        String rightTableName = "AlarmUser";

        //        String rightTableName = "user_ext1";

        TableColumnMapping mapping = new TableColumnMapping(leftDbName, rightDbName, leftTableName, rightTableName);
        mapping.addMapping("id", "ID");
        mapping.addMapping("user_name", "UserName");
        mapping.addMapping("email", "Email");
        mapping.addMapping("telephone", "Telephone");
        mapping.addMapping("weixin_account", "WeixinAccount");

//        mapping.addMapping("id", "UserID");
//        mapping.addMapping("telephone", "Cellphone");

        TableColumnMappings.register(mapping);

        RowChangeSubscriber subscriber = new RowChangeSubscriber(leftDataSource, rightDataSource);
        dispatcher.addSubscriber(subscriber);
        dispatcher.startListen();
    }

    @Test
    public void testBinLogParse() throws Exception {
        BinLogSalveServer binLogSalveServer = new BinLogSalveServer(param, dispatcher);

        binLogSalveServer.start();

        Thread.sleep(30 * 60 * 1000);
    }

}

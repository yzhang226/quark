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

public class MySQLToMySQLTest extends BaseMySQLTestCase {

    private static final Logger logger = LoggerFactory.getLogger(MySQLToMySQLTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DataSource leftDataSource = dataSource;
        DataSource rightDataSource = DbTestUtils.createDemoMySQLDS4MyDb();
        logger.info("rightDataSource is {}", rightDataSource);

        String leftDbName = "monitor";
        String rightDbName = "monitor";

        String leftTableName = "alarm_user";
        String rightTableName = "alarm_user3";

        TableColumnMapping mapping = new TableColumnMapping(leftDbName, rightDbName, leftTableName, rightTableName);
        mapping.addMapping("user_name", "user_name2");
        mapping.addMapping("email", "email3");
        mapping.addMapping("telephone", "telephone4");
        mapping.addMapping("weixin_account", "weixin_account5");

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

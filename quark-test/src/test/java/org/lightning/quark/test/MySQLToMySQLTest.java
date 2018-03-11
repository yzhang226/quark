package org.lightning.quark.test;

import org.junit.Test;
import org.lightning.quark.chase.subscribe.RowChangeSubscriber;
import org.lightning.quark.core.row.TableColumnMapping;
import org.lightning.quark.core.row.TableColumnMappings;
import org.lightning.quark.core.subscribe.RowChangeDispatcher;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.plugin.mysql.provider.MySQLSqlProvider;
import org.lightning.quark.db.plugin.mysql.server.BinLogSalveServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;

public class MySQLToMySQLTest extends BaseMySQLTestCase {

    private static final Logger logger = LoggerFactory.getLogger(MySQLToMySQLTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MySQLSqlProvider.doRegister();


        DataSource leftDataSource = dataSource;
        DataSource rightDataSource = dataSource;

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

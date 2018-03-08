package org.lightning.quark.test;

import org.junit.Test;
import org.lightning.quark.chase.subscribe.RowChangeSubscriber;
import org.lightning.quark.core.subscribe.RowChangeDispatcher;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.plugin.mysql.provider.MySQLSqlProvider;
import org.lightning.quark.db.plugin.mysql.server.BinLogSalveServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BinLogParseTest extends BaseMySQLTestCase {

    private static final Logger logger = LoggerFactory.getLogger(BinLogParseTest.class);

    private DataRowManager leftManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MySQLSqlProvider.doRegister();
        RowChangeDispatcher.startListen();

        // TODO: 构建订阅器
//        RowChangeSubscriber subscriber = new RowChangeSubscriber();
//        RowChangeDispatcher.addSubscriber(subscriber);

    }

    @Test
    public void testBinLogParse() throws IOException, InterruptedException {
        BinLogSalveServer binLogSalveServer = new BinLogSalveServer(param);

        binLogSalveServer.start();

        Thread.sleep(30 * 60 * 1000);
    }

}

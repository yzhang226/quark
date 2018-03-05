package org.lightning.quark.db.plugin.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import org.junit.Test;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.test.BaseMySQLTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BinLogParseTest extends BaseMySQLTestCase {

    private static final Logger logger = LoggerFactory.getLogger(BinLogParseTest.class);

    private DataRowManager leftManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

//        leftManager = new DataRowManager(dataSource);

    }

    @Test
    public void testBinLogParse() throws IOException {

        BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "6567zhyf");



        client.registerEventListener(event -> logger.info("event is {}", event));

        client.connect();



    }

}

package org.lightning.quark.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.lightning.quark.chase.subscribe.RowChangeSubscriber;
import org.lightning.quark.core.model.column.TableColumnMapping;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.db.meta.MetadataManager;
import org.lightning.quark.db.plugin.mysql.server.BinLogSalveServer;
import org.lightning.quark.test.base.BaseMySQLTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class MySQLToMySQLTest extends BaseMySQLTestCase {

    private static final Logger logger = LoggerFactory.getLogger(MySQLToMySQLTest.class);

    private MetadataManager rightManager;
    private DbManager rightDbManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DataSource leftDataSource = dataSource;
        DataSource rightDataSource = DbTestUtils.createDemoMySQLDS4MyDb();
        rightManager = new MetadataManager(rightDataSource);
        rightDbManager = new DbManager(rightDataSource);
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

        metadataManager.registerColumnMapping(mapping);

        RowChangeSubscriber subscriber = new RowChangeSubscriber(metadataManager, rightManager);
        dispatcher.addSubscriber(subscriber);
        dispatcher.startListen();
    }

    @Test
    public void testBinLogParse() throws Exception {
        BinLogSalveServer binLogSalveServer = new BinLogSalveServer(param, dispatcher, metadataManager);
        binLogSalveServer.start();

        String telephone = RandomStringUtils.randomAlphabetic(15);
        logger.info("telephone is {}", telephone);

        dbManager.update("update alarm_user set telephone = ? where id = 15", Lists.newArrayList(telephone));

        Thread.sleep(2 * 1000);

        List<Map<String, Object>> tar = rightDbManager.queryAsMap("select telephone4 from alarm_user3 where id = 15");
        String tarTel = (String) tar.get(0).get("telephone4");
        logger.info("tar telephone is {}", tarTel);

        Assert.assertTrue(telephone.equals(tarTel));


        Thread.sleep(30 * 60 * 1000);
    }

}

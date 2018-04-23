package org.lightning.quark.test;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.test.base.BaseMySQLTestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/2/25
 */
public class DbManagerTest extends BaseMySQLTestCase {

    private DbManager dbManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dbManager = new DbManager(dataSource);
    }

    public void testQuery() {
        List<Map<String, Object>> res = dbManager.queryAsMap("select * from misc_table limit 10");
        res.forEach(row -> {
            logger.info("currentRow is {}", row);
        });
    }

    public void testInsertOne() {
        String sql = "insert into misc_table (name, dou) values (?, ?) ";
        Map<String, Object> res = dbManager.insertOne(sql,
                Arrays.asList(RandomStringUtils.randomAlphabetic(25), RandomUtils.nextDouble(3, 8)));
        logger.info("res is {}", res);
    }

    public void testInsertBatch() {
        String sql = "insert into misc_table (name, dou) values (?, ?) ";
        List<List<Object>> paramsList = Lists.newArrayList();
        paramsList.add(Arrays.asList(RandomStringUtils.randomAlphabetic(25), RandomUtils.nextDouble(3, 8)));
        paramsList.add(Arrays.asList(RandomStringUtils.randomAlphabetic(25), RandomUtils.nextDouble(3, 8)));
        List<Map<String, Object>> res = dbManager.insertBatch(sql, paramsList);
        res.forEach(re -> {
            logger.info("re is {}", re);
        });
    }


}

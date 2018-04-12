package org.lightning.quark.test;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.lightning.quark.core.utils.Q;
import org.lightning.quark.test.base.BaseMySQLTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/2/11
 */
public class SimpleDbTest extends BaseMySQLTestCase {

    private static final Logger logger = LoggerFactory.getLogger(SimpleDbTest.class);


    public void testQueryMap() throws SQLException {
//        QueryRunner run = new QueryRunner(dataSource);
//        MapListHandler h = new MapListHandler();
//        List<Map<String, Object>> persons = run.query("SELECT * FROM alarm_user limit 10", h);
//        persons.forEach(p -> {
//            System.out.println("p " + p);
//        });

        MDC.clear();
        MDC.put("test1", "xxxx");
        MDC.put("test2", "1234");

        logger.info("test test ");

        Q.sleep(20 * 1000);

    }

    public void testInsertMap() throws SQLException {
        QueryRunner run = new QueryRunner(dataSource);
        MapHandler mapHandler = new MapHandler();

        Map<String, Object> res = run.insert("insert into misc_table (name, date) values (?, ?) ",
                mapHandler, "test1", LocalDate.now());

        logger.info("res is {}", res);
    }

    public void testBatchInsertMap() throws SQLException {
        QueryRunner run = new QueryRunner(dataSource);
        MapHandler mapHandler = new MapHandler();

        Map<String, Object> res = run.insert("insert into misc_table (name, date) values (?, ?) ",
                mapHandler, "test1", LocalDate.now());

        logger.info("res is {}", res);
    }

}

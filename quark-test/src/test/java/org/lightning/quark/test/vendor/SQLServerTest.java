package org.lightning.quark.test.vendor;

import org.assertj.core.util.Lists;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.test.base.BaseSQLServerTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/3/12
 */
public class SQLServerTest extends BaseSQLServerTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSetIdentify() {
        String sql = "SET IDENTITY_INSERT AlarmUser ON";
        int eff = dbManager.execute(sql);

        logger.info("eff is {}", eff);
    }

    public void testInsertWithIdentify() {
        /**
         * String sql =  "insert into {tableName} ( {columns} ) values ( {placeholder} )";
         String insertSql = sql.replace("{columns}", getColumnNames())
         .replace("{tableName}", super.wrappedTableName())
         .replace("{placeholder}", preparePlaceholder(table.getColumns().size()))
         ;

         -- auto-generated definition
         CREATE TABLE AlarmUser
         (
         ID            INT IDENTITY
         PRIMARY KEY,
         UserName      NVARCHAR(100),
         Email         NVARCHAR(100),
         Telephone     NVARCHAR(100),
         WeixinAccount NVARCHAR(100)
         )
         GO

         */
        // String beforeSql, String sql, List<List<Object>> paramsList
        String beforeSql = "SET IDENTITY_INSERT AlarmUser ON";
        String sql = "insert into AlarmUser (ID, UserName, Email) values (?, ?, ?)";
        List<List<Object>> params = Lists.newArrayList();
        params.add(Arrays.asList(5, "23", "111"));
        List<Map<String, Object>> eff = dbManager.insertBatch(beforeSql, sql, "", params);

        logger.info("eff is {}", eff);
    }

}

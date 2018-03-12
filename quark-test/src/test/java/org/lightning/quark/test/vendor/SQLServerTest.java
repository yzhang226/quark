package org.lightning.quark.test.vendor;

import org.assertj.core.util.Lists;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.test.base.BaseSQLServerTestCase;

import java.time.LocalDateTime;
import java.util.*;

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
        // String beforeSql, String sql, List<List<Object>> paramsList
        String beforeSql = "SET IDENTITY_INSERT AlarmUser ON";
        String sql = "insert into AlarmUser (ID, UserName, Email) values (?, ?, ?)";
        List<List<Object>> params = Lists.newArrayList();
        params.add(Arrays.asList(5, "23", "111"));
        List<Map<String, Object>> eff = dbManager.insertBatch(beforeSql, sql, "", params);

        logger.info("eff is {}", eff);
    }

    public void testCdcQuery() {
//        String sql
        String sql = "DECLARE @bglsn VARBINARY(max) = sys.fn_cdc_map_time_to_lsn('smallest greater than or equal', '2018-03-13 00:01:00');\n" +
                "        DECLARE @edlsn VARBINARY(max) = sys.fn_cdc_map_time_to_lsn('largest less than or equal', '2018-03-13 00:02:00');\n" +
                "        select [__$start_lsn] as startLsn, [__$seqval] as seqval, sys.fn_cdc_map_lsn_to_time(__$start_lsn) as updateTime,\n" +
                "        EventId as pkValue\n" +
                "        from cdc.dbo_evt_MarketingEvents_CT WITH (NOLOCK)\n" +
                "        where [__$start_lsn] &gt;= @bglsn\n" +
                "        and [__$start_lsn] &lt;= @edlsn ";
        Date start = new Date(System.currentTimeMillis() - 60 * 60 * 1000);
        Date end = new Date();
        List<Map<String, Object>> rows = dbManager.queryAsMap(sql);// , start, end

        logger.info("rows is {}", rows);


    }

}

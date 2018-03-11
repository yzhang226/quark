package org.lightning.quark.test.base;

import junit.framework.TestCase;
import org.lightning.quark.core.model.db.DataSourceParam;
import org.lightning.quark.core.subscribe.RowChangeDispatcher;
import org.lightning.quark.db.meta.MetadataManager;
import org.lightning.quark.db.plugin.mssql.provider.SQLServerSqlProvider;
import org.lightning.quark.db.plugin.mysql.binlog.EventParserFactory;
import org.lightning.quark.db.plugin.mysql.binlog.parser.DeleteEventParser;
import org.lightning.quark.db.plugin.mysql.binlog.parser.InsertEventParser;
import org.lightning.quark.db.plugin.mysql.binlog.parser.UpdateEventParser;
import org.lightning.quark.db.plugin.mysql.provider.MySQLSqlProvider;
import org.lightning.quark.test.DbTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by cook on 2018/2/25
 */
public abstract class BaseMySQLTestCase extends TestCase {

    protected static final Logger logger = LoggerFactory.getLogger(BaseMySQLTestCase.class);

    protected DataSource dataSource;
    protected DataSourceParam param;
    protected RowChangeDispatcher dispatcher;

    @Override
    protected void setUp() throws Exception {
        param = DbTestUtils.createMySQLParam4Monitor();
        dataSource = DbTestUtils.createDemoMySQLDS4Monitor();
        logger.info("setup dataSource {}", dataSource);

        MetadataManager metadataManager = new MetadataManager(dataSource);

        InsertEventParser insertEventParser = new InsertEventParser(metadataManager);
        DeleteEventParser deleteEventParser = new DeleteEventParser(metadataManager);
        UpdateEventParser updateEventParser = new UpdateEventParser(metadataManager);
        EventParserFactory.register(insertEventParser);
        EventParserFactory.register(deleteEventParser);
        EventParserFactory.register(updateEventParser);

        dispatcher = new RowChangeDispatcher();

        MySQLSqlProvider.doRegister();
        SQLServerSqlProvider.doRegister();

    }

    @Override
    protected void tearDown() throws Exception {
        dataSource.getConnection().close();
        logger.info("tearDown dataSource {}", dataSource);
    }

}

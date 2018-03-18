package org.lightning.quark.test.base;

import junit.framework.TestCase;
import org.lightning.quark.core.model.db.DataSourceParam;
import org.lightning.quark.db.dispatcher.RowChangeDispatcher;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.db.meta.MetadataManager;
import org.lightning.quark.db.plugin.mssql.provider.SQLServerSqlProvider;
import org.lightning.quark.db.plugin.mysql.provider.MySQLSqlProvider;
import org.lightning.quark.test.DbTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by cook on 2018/2/25
 */
public abstract class BaseSQLServerTestCase extends TestCase {

    protected static final Logger logger = LoggerFactory.getLogger(BaseSQLServerTestCase.class);

    protected DataSource dataSource;
    protected DataSourceParam param;
    protected RowChangeDispatcher dispatcher;
    protected MetadataManager metadataManager;
    protected DbManager dbManager;

    @Override
    protected void setUp() throws Exception {
        param = DbTestUtils.createDemoSQLServerDS4QA();
        dataSource = DbTestUtils.createDemoSQLServerDataSourceInQA();
        logger.info("setup dataSource {}", dataSource);

        dbManager = new DbManager(dataSource);

        metadataManager = new MetadataManager(dataSource);

        dispatcher = new RowChangeDispatcher(metadataManager);

        MySQLSqlProvider.doRegister();
        SQLServerSqlProvider.doRegister();

    }

    @Override
    protected void tearDown() throws Exception {
        dataSource.getConnection().close();
        logger.info("tearDown dataSource {}", dataSource);
    }

}

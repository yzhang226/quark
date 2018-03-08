package org.lightning.quark.test;

import junit.framework.TestCase;
import org.lightning.quark.core.model.db.DataSourceParam;
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

    @Override
    protected void setUp() throws Exception {
        param = DbTestUtils.createMySQLParam();
        dataSource = DbTestUtils.createDemoMySQLDataSource();
        logger.info("setup dataSource {}", dataSource);
    }

    @Override
    protected void tearDown() throws Exception {
        dataSource.getConnection().close();
        logger.info("tearDown dataSource {}", dataSource);
    }

}

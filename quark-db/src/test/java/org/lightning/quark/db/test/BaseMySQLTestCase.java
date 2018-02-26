package org.lightning.quark.db.test;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by cook on 2018/2/25
 */
public abstract class BaseMySQLTestCase extends TestCase {

    protected static final Logger logger = LoggerFactory.getLogger(BaseMySQLTestCase.class);

    protected DataSource dataSource;

    @Override
    protected void setUp() throws Exception {
        dataSource = DbTestUtils.createDemoMySQLDataSource();
        logger.info("setup dataSource {}", dataSource);
    }

    @Override
    protected void tearDown() throws Exception {
        dataSource.getConnection().close();
        logger.info("tearDown dataSource {}", dataSource);
    }
}

package org.lightning.quark.db.test.metadata;

import org.apache.commons.collections4.CollectionUtils;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.crawler.CrawlerUtils;
import org.lightning.quark.db.crawler.TableMetadataFetcher;
import org.lightning.quark.db.test.DbTestUtils;
import schemacrawler.schema.*;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.utility.SchemaCrawlerUtility;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by cook on 2018/2/24
 */
public class MetadataAcquireTest {



    @Test
    public void tableMySQLMetadataTest() throws SQLException, SchemaCrawlerException {
        DataSource dataSource = DbTestUtils.createDemoMySQLDataSource();
        Connection connection = dataSource.getConnection();
        String testTableName = "misc_table";

        printTableColumnInfo2(connection, testTableName);

        connection.close();
    }

    @Test
    public void tableH2MetadataTest() throws SQLException, SchemaCrawlerException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:sample;DATABASE_TO_UPPER=false;INIT=RUNSCRIPT FROM 'classpath:/scripts/h2/create.sql'");
        ds.setUser("sa");
        ds.setPassword("sa");
        Connection conn = ds.getConnection();

        String testTableName = "%";

        printTableColumnInfo2(conn, testTableName);

        conn.close();
    }

    @Test
    public void tableMSSQLMetadataTest() throws SQLException, SchemaCrawlerException {
        DataSource dataSource = DbTestUtils.createDemoSQLServerDataSource();
        Connection connection = dataSource.getConnection();
        String testTableName = "app_User";

        printTableColumnInfo(connection, testTableName);

        connection.close();
    }

    private void printTableColumnInfo2(Connection connection, String testTableName) throws SchemaCrawlerException, SQLException {
        TableMetadataFetcher fetcher = new TableMetadataFetcher(connection);
        List<MetaTable> metaTables = fetcher.fetchMetaTables(testTableName);

        metaTables.forEach(metaTable -> {
            System.out.println("table - " + metaTable.getName());
            metaTable.getPrimaryKey().getColumns().forEach(metaColumn -> {
                System.out.println("\t pk column - " + metaColumn.getName() + ", " + metaColumn.getJdbcType());
            });
            metaTable.getColumns().forEach(metaColumn -> {
                System.out.println("\t column - " + metaColumn.getName() + ", " + metaColumn.getJdbcType());
            });
        });
    }

    private void printTableColumnInfo(Connection connection, String testTableName) throws SchemaCrawlerException, SQLException {
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInfoLevel(CrawlerUtils.createLevel4generate());
        options.setTableNamePattern(testTableName);
        String databaseName = connection.getCatalog();
        RegularExpressionInclusionRule inclusionRule = new RegularExpressionInclusionRule(databaseName+"\\..*" + "|" + databaseName);
        options.setSchemaInclusionRule(inclusionRule);

        final Catalog catalog = SchemaCrawlerUtility.getCatalog(connection, options);

        DatabaseInfo db = catalog.getDatabaseInfo();
        JdbcDriverInfo jdbc = catalog.getJdbcDriverInfo();

        System.out.println("DB: " + db.getProductVersion() + ", " + db.getProductName() + ", " + db.getProperties());
        System.out.println("JDBC: " + jdbc.getDriverName() + ", " + jdbc.getDriverVersion() );

        System.out.println("catalog.getTables size is " + (CollectionUtils.isEmpty(catalog.getTables()) ? 0 : catalog.getTables().size()));

        for (final Table table : catalog.getTables()) {
            System.out.print("table --> " + table.getName());
            if (table instanceof View) {
                System.out.println(" (VIEW)");
            } else {
                System.out.println();
            }

            for (final Column column : table.getColumns()) {
                String extra = column.getAttribute("EXTRA");
                System.out.println("     o--> " + column + " ( " + column.getType() + "(" + column.getSize() + ") " + ") (" + extra + ")");
            }
        }
    }

}

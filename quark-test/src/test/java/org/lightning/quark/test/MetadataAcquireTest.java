package org.lightning.quark.test;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.utils.DsUtils;
import org.lightning.quark.db.crawler.CrawlerUtils;
import org.lightning.quark.db.crawler.TableMetadataFetcher;
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
        DataSource dataSource = DbTestUtils.createDemoMySQLDS4Monitor();
        Connection connection = dataSource.getConnection();
        String testTableName = "alarm_user3";

        printTableColumnInfo(connection, testTableName);

        connection.close();
    }

    @Test
    public void tableH2MetadataTest() throws SQLException, SchemaCrawlerException {
        DataSource dataSource = DbTestUtils.createDemoH2DataSource();
        Connection conn = dataSource.getConnection();

        String testTableName = "%";

        printTableColumnInfo(conn, testTableName);

        conn.close();
    }

    @Test
    public void tableMSSQLMetadataTest() throws SQLException, SchemaCrawlerException {
        DataSource dataSource = DbTestUtils.createDemoSQLServerDataSourceInDev();
        Connection connection = dataSource.getConnection();
        String testTableName = "AlarmUser";// AlarmUser  app_User

        printTableColumnInfo(connection, testTableName);

        connection.close();
    }

    private void printTableColumnInfo2(DataSource dataSource, String testTableName) throws SchemaCrawlerException, SQLException {
        TableMetadataFetcher fetcher = new TableMetadataFetcher(dataSource);
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
        String databaseName = DsUtils.getDbName(connection);

        String schema = connection.getSchema();
        String dbPattern = null;
        if (StringUtils.isNotBlank(schema)) {
            dbPattern = databaseName + "." + schema + "\\..*" + "|" + databaseName + "." + schema;
        } else {
            dbPattern = databaseName+"\\..*" + "|" + databaseName;
        }

        RegularExpressionInclusionRule inclusionRule = new RegularExpressionInclusionRule(dbPattern);
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

package org.lightning.quark.db.crawler;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.metadata.MetaCatalog;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.utils.DsUtils;
import org.lightning.quark.db.utils.MetadataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemacrawler.schema.Catalog;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.utility.SchemaCrawlerUtility;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * Created by cook on 2018/2/25
 */
public class TableMetadataFetcher {

    private static final Logger logger = LoggerFactory.getLogger(TableMetadataFetcher.class);

    private DataSource dataSource;
    private String databaseName;

    public TableMetadataFetcher(DataSource dataSource) {
        this.dataSource = dataSource;
        this.databaseName = getDefaultCatalog();
    }

    public TableMetadataFetcher(DataSource dataSource, String databaseName) {
        this.dataSource = dataSource;
        this.databaseName = databaseName;
    }

    private SchemaCrawlerOptions createSchemaCrawlerOptions(String tablePattern, String databaseName) {
        SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInfoLevel(CrawlerUtils.createLevel4Data());
        options.setTableNamePattern(tablePattern);
        if (StringUtils.isNotEmpty(databaseName)) {
            String schema = DsUtils.getSchema(dataSource);
            String dbPattern = null;
            if (StringUtils.isNotBlank(schema)) {
                dbPattern = databaseName + "." + schema + "\\..*" + "|" + databaseName + "." + schema;
            } else {
                dbPattern = databaseName+"\\..*" + "|" + databaseName;
            }
            RegularExpressionInclusionRule inclusionRule = new RegularExpressionInclusionRule(dbPattern);
            options.setSchemaInclusionRule(inclusionRule);
        }
        return options;
    }

    /**
     *
     * @param tablePattern
     * @return
     */
    public List<MetaTable> fetchMetaTables(String tablePattern) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            SchemaCrawlerOptions options = createSchemaCrawlerOptions(tablePattern, databaseName);

            Catalog catalog = SchemaCrawlerUtility.getCatalog(conn, options);
            MetaCatalog metaCatalog = CrawlerUtils.createCatalogInfo(catalog);

            if (CollectionUtils.isNotEmpty(catalog.getTables())) {
                List<MetaTable> metaTables = MetadataConverter.convert(catalog.getTables());
                metaTables.forEach(table -> {
                    table.setCatalog(metaCatalog);
                    if (StringUtils.isEmpty(table.getDbName())) {
                        table.setDbName(databaseName);
                    }
                });
                return metaTables;
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable e) {
            throw new QuarkExecuteException("fetchTables error", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * as databaseName
     * @return
     */
    public String getDefaultCatalog() {
        return DsUtils.getDbName(dataSource);
    }

}

package org.lightning.quark.db.crawler;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.utils.MetadataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.utility.SchemaCrawlerUtility;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * Created by cook on 2018/2/25
 */
public class TableMetadataFetcher {

    private static final Logger logger = LoggerFactory.getLogger(TableMetadataFetcher.class);

    private Connection connection;
    private String databaseName;

    public TableMetadataFetcher(Connection connection) {
        this.connection = connection;
        this.databaseName = getDefaultCatalog();
    }

    public TableMetadataFetcher(Connection connection, String databaseName) {
        this.connection = connection;
        this.databaseName = databaseName;
    }

    public List<Table> fetchTables(String tablePattern) {
        try {
            SchemaCrawlerOptions options = new SchemaCrawlerOptions();
            options.setSchemaInfoLevel(CrawlerUtils.createLevel4Data());
            options.setTableNamePattern(tablePattern);
            if (StringUtils.isNotEmpty(databaseName)) {
                RegularExpressionInclusionRule inclusionRule = new RegularExpressionInclusionRule(databaseName+"\\..*" + "|" + databaseName);
                options.setSchemaInclusionRule(inclusionRule);
            }

            Catalog catalog = SchemaCrawlerUtility.getCatalog(connection, options);

            return CollectionUtils.isNotEmpty(catalog.getTables()) ? Lists.newArrayList(catalog.getTables()) : Collections.emptyList();
        } catch (Exception e) {
            throw new QuarkExecuteException("fetchTables error", e);
        }
    }

    /**
     *
     * @param tablePattern
     * @return
     */
    public List<MetaTable> fetchMetaTables(String tablePattern) {
        return MetadataConverter.convert(fetchTables(tablePattern));
    }

    /**
     * as databaseName
     * @return
     */
    public String getDefaultCatalog() {
        String databaseName = null;
        try {
            databaseName = connection.getCatalog();
            return databaseName;
        } catch (Exception e) {
            throw new QuarkExecuteException("getCatalog error", e);
        }
    }

}

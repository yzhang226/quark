package org.lightning.quark.db.crawler;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.metadata.MetaCatalog;
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

    private SchemaCrawlerOptions createSchemaCrawlerOptions(String tablePattern, String databaseName) {
        SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInfoLevel(CrawlerUtils.createLevel4Data());
        options.setTableNamePattern(tablePattern);
        if (StringUtils.isNotEmpty(databaseName)) {
            RegularExpressionInclusionRule inclusionRule =
                    new RegularExpressionInclusionRule(databaseName+"\\..*" + "|" + databaseName);
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
        try {
            SchemaCrawlerOptions options = createSchemaCrawlerOptions(tablePattern, databaseName);

            Catalog catalog = SchemaCrawlerUtility.getCatalog(connection, options);
            MetaCatalog metaCatalog = CrawlerUtils.createCatalogInfo(catalog);

            if (CollectionUtils.isNotEmpty(catalog.getTables())) {
                List<MetaTable> metaTables = MetadataConverter.convert(catalog.getTables());
                metaTables.forEach(table -> table.setCatalog(metaCatalog));
                return metaTables;
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new QuarkExecuteException("fetchTables error", e);
        }
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

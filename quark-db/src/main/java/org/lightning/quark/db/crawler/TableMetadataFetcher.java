package org.lightning.quark.db.crawler;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.metadata.MetaCatalog;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.utils.DsUtils;
import org.lightning.quark.core.utils.QuarkAssertor;
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
import java.util.Map;

/**
 * Created by cook on 2018/2/25
 */
public class TableMetadataFetcher {

    private static final Logger logger = LoggerFactory.getLogger(TableMetadataFetcher.class);

    private Map<String, MetaTable> tableCache = Maps.newHashMap();

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
        try {
            SchemaCrawlerOptions options = createSchemaCrawlerOptions(tablePattern, databaseName);

            Catalog catalog = SchemaCrawlerUtility.getCatalog(dataSource.getConnection(), options);
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
        } catch (Exception e) {
            throw new QuarkExecuteException("fetchTables error", e);
        }
    }

    /**
     * with cache
     * @param tableName
     * @return
     */
    public MetaTable fetchMetaTableInCache(String tableName) {
        String fullTableName = databaseName + "." + tableName;
        MetaTable table = tableCache.get(fullTableName);
        if (table != null) {
            return table;
        }
        List<MetaTable> ts = fetchMetaTables(tableName);
        QuarkAssertor.isTrue(CollectionUtils.isNotEmpty(ts) && ts.size() == 1, "table#%s do not exist or has two same name table", tableName);
        tableCache.put(fullTableName, ts.get(0));

        return tableCache.get(fullTableName);
    }

    /**
     * as databaseName
     * @return
     */
    public String getDefaultCatalog() {
        String databaseName = null;
        try {
            databaseName = dataSource.getConnection().getCatalog();
            return databaseName;
        } catch (Exception e) {
            throw new QuarkExecuteException("getCatalog error", e);
        }
    }

}

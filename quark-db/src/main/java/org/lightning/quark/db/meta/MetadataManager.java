package org.lightning.quark.db.meta;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.column.TableColumnMappings;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.model.column.TableColumnMapping;
import org.lightning.quark.core.model.metadata.TableIdData;
import org.lightning.quark.core.utils.DsUtils;
import org.lightning.quark.core.utils.Q;
import org.lightning.quark.core.utils.QuarkAssertor;
import org.lightning.quark.db.crawler.TableMetadataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 元数据管理器 - 表-元数据, 表-列映射
 * Created by cook on 2018/3/11
 */
public class MetadataManager {

    private static final Logger logger = LoggerFactory.getLogger(MetadataManager.class);

    /**
     * db.tableName --> MetaTable
     */
    private Map<String, MetaTable> tableCache = Maps.newHashMap();

    /**
     * db.tableName --> column-mapping
     */
    private Map<String, TableColumnMapping> mappings = Maps.newHashMap();

    /**
     * tableId --> TableIdData
     */
    private static final Map<Long, TableIdData> tableIdCaches = Maps.newHashMap();

    private DataSource dataSource;

    public MetadataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *
     * @param tableName
     * @return
     */
    public MetaTable getTable(String tableName) {
        try {
            String fullTableName = DsUtils.getDbName(dataSource) + "." + tableName;
            MetaTable table = tableCache.get(fullTableName);
            if (table != null) {
                return table;
            }

            // do not get, fetch meta from db
            TableMetadataFetcher fetcher = new TableMetadataFetcher(dataSource);
            List<MetaTable> ts = fetcher.fetchMetaTables(tableName);
            QuarkAssertor.isTrue(CollectionUtils.isNotEmpty(ts) && ts.size() == 1, "table#%s do not exist or has two same name table", tableName);

            putTable(fullTableName, ts.get(0));

            return tableCache.get(fullTableName);
        } catch (Exception e) {
            throw new QuarkExecuteException("get table#" + tableName + " meta error", e);
        }
    }

    public void putTable(String fullTableName, MetaTable metaTable) {
        tableCache.put(fullTableName, metaTable);
    }

    public TableColumnMapping getColumnMapping(String dbName, String tableName) {
        return getColumnMapping(Q.getFullName(dbName, tableName));
    }

    public boolean containsColumnMapping(String dbName, String tableName) {
        return mappings.containsKey(Q.getFullName(dbName, tableName));
    }

    public TableColumnMapping getColumnMapping(String tableFullName) {
        return mappings.get(tableFullName);
    }

    public void registerColumnMapping(TableColumnMapping mapping) {
        String fullName = mapping.getTableFullName();
        if (mappings.containsKey(fullName)) {
            logger.info("{} exist, try to replace with new mapping", fullName);
        }
        mappings.put(fullName, mapping);
    }

    public void putTableIdData(Long tableId, TableIdData data) {
        if (tableIdCaches.containsKey(tableId)) {
            return;
        }
        tableIdCaches.put(tableId, data);
        logger.info("put tableId#{} with info#{}", tableId, data);
    }

    public TableIdData getTableIdData(Long tableId) {
        return tableIdCaches.get(tableId);
    }

    /**
     *
     * @param tableId
     * @return
     */
    public boolean containsColumnMapping4TableId(Long tableId) {
        TableIdData data = getTableIdData(tableId);
        return getColumnMapping(data.getDatabase(), data.getTable()) != null;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}

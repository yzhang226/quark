package org.lightning.quark.db.meta;

import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.crawler.TableMetadataFetcher;

import javax.sql.DataSource;

/**
 * Created by cook on 2018/3/11
 */
public class MetadataManager {

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
            TableMetadataFetcher fetcher = new TableMetadataFetcher(dataSource);
            return fetcher.fetchMetaTableInCache(tableName);
        } catch (Exception e) {
            throw new QuarkExecuteException("get table#" + tableName + " meta error", e);
        }
    }

}

package org.lightning.quark.db.plugin.mysql.binlog;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.crawler.TableMetadataFetcher;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by cook on 2018/3/5
 */
public abstract class BaseEventParser {

    private static final Map<Long, MetaTable> caches = new HashMap<>();

    private static final Interner<Long> lock = Interners.newWeakInterner();

    protected TableMetadataFetcher metadataFetcher;
    protected DataSource dataSource;

    public BaseEventParser(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    protected List<Map<Integer, Object>> toRow(BitSet columnBits, List<Serializable[]> rawRowValues) {
        List<Map<Integer, Object>> rows = new ArrayList<>(rawRowValues.size());
        rawRowValues.forEach(rowValue -> {
            Map<Integer, Object> row = new HashMap<>(rowValue.length);
            columnBits.stream().forEach(i -> {
                row.put(i, rowValue[i]);
            });
            rows.add(row);
        });
        return rows;
    }

    protected MetaTable getMetaTable(Long tableId) {
        if (caches.containsKey(tableId)) {
            return caches.get(tableId);
        }

        try {
            synchronized (lock.intern(tableId)) {
                if (caches.containsKey(tableId)) {
                    return caches.get(tableId);
                }

                String tableName = TableIdMapping.get(tableId).getTable();
                String databaseName = TableIdMapping.get(tableId).getDatabase();

                TableMetadataFetcher fetcher = new TableMetadataFetcher(dataSource.getConnection(), databaseName);
                List<MetaTable> metaTables = fetcher.fetchMetaTables(tableName);
                MetaTable leftTable = metaTables.get(0);

                caches.put(tableId, leftTable);

                return leftTable;
            }
        } catch (SQLException e) {
            throw new QuarkExecuteException("", e);
        }
    }

}

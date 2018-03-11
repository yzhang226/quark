package org.lightning.quark.db.plugin.mysql.binlog;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.db.crawler.TableMetadataFetcher;
import org.lightning.quark.db.meta.MetadataManager;

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

    protected MetadataManager metadataManager;

    public BaseEventParser(MetadataManager metadataManager) {
        this.metadataManager = metadataManager;
    }

    /**
     * 解析
     * @param eventWrapper
     * @return
     */
    public RowChangeEvent parse(EventWrapper eventWrapper) {
        return parseInner(eventWrapper.getEvent().getData());
    }

    public abstract RowChangeEvent parseInner(EventData eventData);

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

                MetaTable leftTable = metadataManager.getTable(tableName);

                caches.put(tableId, leftTable);

                return leftTable;
            }
        } catch (Exception e) {
            throw new QuarkExecuteException("", e);
        }
    }

    protected RowChangeEvent toChangeEvent(Long tableId, List<RowChange> changes) {
        MetaTable metaTable = getMetaTable(tableId);

        RowChangeEvent changeEvent = new RowChangeEvent();
        changeEvent.setChanges(changes);
        changeEvent.setDbName(metaTable.getDbName());
        changeEvent.setTableName(metaTable.getName());

        return changeEvent;
    }

    public abstract List<EventType> getNeedProcessTypes();

}

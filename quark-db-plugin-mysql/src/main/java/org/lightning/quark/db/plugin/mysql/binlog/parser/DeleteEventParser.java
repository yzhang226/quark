package org.lightning.quark.db.plugin.mysql.binlog.parser;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.google.common.collect.Lists;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.core.row.RowChangeType;
import org.lightning.quark.db.meta.MetadataManager;
import org.lightning.quark.db.plugin.mysql.binlog.BaseEventParser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.shyiko.mysql.binlog.event.EventType.*;

/**
 * Created by cook on 2018/3/8
 */
public class DeleteEventParser extends BaseEventParser {

    private static final List<EventType> types = Lists.newArrayList(PRE_GA_DELETE_ROWS, DELETE_ROWS, EXT_DELETE_ROWS);

    public DeleteEventParser(MetadataManager metadataManager) {
        super(metadataManager);
    }

    public RowChangeEvent parseInner(EventData event) {
        DeleteRowsEventData data = (DeleteRowsEventData) event;

        List<Map<Integer, Object>> rows = toRow(data.getIncludedColumns(), data.getRows());

        List<RowChange> changes = rows.stream().map(row -> {
            RowChange change = new RowChange();
            change.setEventType(RowChangeType.DELETE);
            change.setCurrentRow(getMetaTable(data.getTableId()).convertRawRow(row));
            return change;
        }).collect(Collectors.toList());

        return toChangeEvent(data.getTableId(), changes);
    }

    @Override
    public List<EventType> getNeedProcessTypes() {
        return types;
    }

}

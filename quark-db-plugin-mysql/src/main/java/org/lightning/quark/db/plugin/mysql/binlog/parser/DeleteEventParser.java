package org.lightning.quark.db.plugin.mysql.binlog.parser;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.core.row.RowChangeType;
import org.lightning.quark.db.plugin.mysql.binlog.BaseEventParser;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/3/8
 */
public class DeleteEventParser extends BaseEventParser {

    public DeleteEventParser(DataSource dataSource) {
        super(dataSource);
    }

    public RowChangeEvent parse(DeleteRowsEventData data) {
        List<Map<Integer, Object>> rows = toRow(data.getIncludedColumns(), data.getRows());

        List<RowChange> changes = rows.stream().map(row -> {
            RowChange change = new RowChange();
            change.setEventType(RowChangeType.DELETE);
            change.setCurrentRow(getMetaTable(data.getTableId()).convertRawRow(row));
            return change;
        }).collect(Collectors.toList());

        return toChangeEvent(data.getTableId(), changes);
    }


}

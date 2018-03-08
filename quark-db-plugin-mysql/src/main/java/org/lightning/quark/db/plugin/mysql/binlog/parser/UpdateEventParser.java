package org.lightning.quark.db.plugin.mysql.binlog.parser;

import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeType;
import org.lightning.quark.db.plugin.mysql.binlog.BaseEventParser;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/3/8
 */
public class UpdateEventParser extends BaseEventParser {

    public UpdateEventParser(DataSource dataSource) {
        super(dataSource);
    }

    public List<RowChange> parse(UpdateRowsEventData data) {
        List<Serializable[]> previousRowValues = data.getRows().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        List<Serializable[]> currentRowValues = data.getRows().stream().map(Map.Entry::getValue).collect(Collectors.toList());

        List<Map<Integer, Object>> previousValues = toRow(data.getIncludedColumnsBeforeUpdate(), previousRowValues);
        List<Map<Integer, Object>> currentValues = toRow(data.getIncludedColumns(), currentRowValues);

        List<RowChange> changes = new ArrayList<>(previousRowValues.size());
        for (int i=0; i<previousValues.size(); i++) {
            RowChange change = new RowChange();
            change.setEventType(RowChangeType.UPDATE);
            change.setCurrentRow(getMetaTable(data.getTableId()).convertRawRow(currentValues.get(i)));
            change.setPreviousRow(getMetaTable(data.getTableId()).convertRawRow(previousValues.get(i)));
            changes.add(change);
        }
        return changes;
    }

}

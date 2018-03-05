package org.lightning.quark.db.plugin.mysql.binlog;

import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeType;
import org.lightning.quark.db.copy.DataRowManager;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/3/5
 */
public class InsertRowEventProcessor {

    protected DataRowManager leftManager;

    public InsertRowEventProcessor(DataRowManager leftManager) {
        this.leftManager = leftManager;
    }

    public RowChange process(WriteRowsEventData data) {
        RowChange change = new RowChange();
        change.setEventType(RowChangeType.INSERT);

        change.setRow(leftManager.convertRawRow(toRow(data.getIncludedColumns(), data.getRows())));
        return change;
    }

    protected Map<Integer, Object> toRow(BitSet columnBits, List<Serializable[]> rawRowValues) {
        Map<Integer, Object> row = new HashMap<>(rawRowValues.size());

        columnBits.stream().forEach(i -> {
            row.put(i, String.valueOf(rawRowValues.get(i)));
        });

        return row;
    }

}

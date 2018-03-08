package org.lightning.quark.db.plugin.mysql.binlog.parser;

import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeType;
import org.lightning.quark.db.plugin.mysql.binlog.BaseEventParser;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/3/5
 */
public class InsertEventParser extends BaseEventParser {

    public InsertEventParser(DataSource dataSource) {
        super(dataSource);
    }

    public List<RowChange> parse(WriteRowsEventData data) {
        List<Map<Integer, Object>> rows = toRow(data.getIncludedColumns(), data.getRows());

        return rows.stream().map(row -> {
            RowChange change = new RowChange();
            change.setEventType(RowChangeType.INSERT);
            change.setCurrentRow(getMetaTable(data.getTableId()).convertRawRow(row));
            return change;
        }).collect(Collectors.toList());
    }


}

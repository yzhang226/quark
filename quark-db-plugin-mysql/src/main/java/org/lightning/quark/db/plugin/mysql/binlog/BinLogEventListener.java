package org.lightning.quark.db.plugin.mysql.binlog;


import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Maps;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.plugin.mysql.binlog.parser.DeleteEventParser;
import org.lightning.quark.db.plugin.mysql.binlog.parser.InsertEventParser;
import org.lightning.quark.db.plugin.mysql.binlog.parser.UpdateEventParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * Created by cook on 2018/3/5
 */
public class BinLogEventListener implements BinaryLogClient.EventListener {

    private static final Logger logger = LoggerFactory.getLogger(BinLogEventListener.class);

    private DataRowManager leftManager;

    private InsertEventParser insertEventParser;
    private DeleteEventParser deleteEventParser;
    private UpdateEventParser updateEventParser;

    public BinLogEventListener(DataSource dataSource) {
        insertEventParser = new InsertEventParser(dataSource);
        deleteEventParser = new DeleteEventParser(dataSource);
        updateEventParser = new UpdateEventParser(dataSource);
    }

    @Override
    public void onEvent(Event event) {
        logger.debug("event is {}", event);

        List<RowChange> changes = null;
        if (EventType.isRowMutation(event.getHeader().getEventType())) {
            if (event.getData() instanceof WriteRowsEventData) {
                WriteRowsEventData data = event.getData();
                changes = insertEventParser.parse(data);
            } else if (event.getData() instanceof DeleteRowsEventData) {
                DeleteRowsEventData data = event.getData();
                changes = deleteEventParser.parse(data);
            } else if (event.getData() instanceof UpdateRowsEventData) {
                UpdateRowsEventData data = event.getData();
                changes = updateEventParser.parse(data);
            }

            logger.info("changes is {}", changes);
        } else if (Objects.equals(event.getHeader().getEventType(), EventType.TABLE_MAP)) {
            TableMapEventData data = event.getData();
            TableIdMapping.put(data.getTableId(), data);
        }

    }



}

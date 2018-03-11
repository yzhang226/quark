package org.lightning.quark.db.plugin.mysql.binlog;


import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Maps;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.core.subscribe.RowChangeDispatcher;
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

        try {
            if (EventType.isRowMutation(event.getHeader().getEventType())) {
                Long tableId = getTableId(event);
                if (tableId == null || !TableIdMapping.containsMapping(getTableId(event))) {
                    logger.warn("tableId[{}] do not contains column-mapping", tableId);
                    return;
                }

                RowChangeEvent changeEvent = null;
                if (event.getData() instanceof WriteRowsEventData) {
                    WriteRowsEventData data = event.getData();
                    changeEvent = insertEventParser.parse(data);
                } else if (event.getData() instanceof DeleteRowsEventData) {
                    DeleteRowsEventData data = event.getData();
                    changeEvent = deleteEventParser.parse(data);
                } else if (event.getData() instanceof UpdateRowsEventData) {
                    UpdateRowsEventData data = event.getData();
                    changeEvent = updateEventParser.parse(data);
                } else {
                    logger.error("EventType#{} do not support", event.getHeader().getEventType());
                }

                logger.debug("changes is {}", changeEvent);

                RowChangeDispatcher.pushChanges(changeEvent);

            } else if (Objects.equals(event.getHeader().getEventType(), EventType.TABLE_MAP)) {
                TableMapEventData data = event.getData();
                TableIdMapping.put(data.getTableId(), data);
            }
        } catch (Exception e) {
            logger.error("parse event error: " + event, e);
        }

    }

    private Long getTableId(Event event) {
        if (event.getData() instanceof WriteRowsEventData) {
            WriteRowsEventData data = event.getData();
            return data.getTableId();
        } else if (event.getData() instanceof DeleteRowsEventData) {
            DeleteRowsEventData data = event.getData();
            return data.getTableId();
        } else if (event.getData() instanceof UpdateRowsEventData) {
            UpdateRowsEventData data = event.getData();
            return data.getTableId();
        } else {
            return null;
        }
    }

}

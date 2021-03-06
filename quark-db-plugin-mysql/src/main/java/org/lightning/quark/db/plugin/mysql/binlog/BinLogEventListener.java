package org.lightning.quark.db.plugin.mysql.binlog;


import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import org.lightning.quark.core.model.metadata.TableIdData;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.db.dispatcher.RowChangeDispatcher;
import org.lightning.quark.db.meta.MetadataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 *
 * Created by cook on 2018/3/5
 */
public class BinLogEventListener implements BinaryLogClient.EventListener {

    private static final Logger logger = LoggerFactory.getLogger(BinLogEventListener.class);

    private RowChangeDispatcher dispatcher;
    private MetadataManager metadataManager;

    public BinLogEventListener(RowChangeDispatcher dispatcher, MetadataManager metadataManager) {
        this.dispatcher = dispatcher;
        this.metadataManager = metadataManager;
    }

    @Override
    public void onEvent(Event event) {
        logger.debug("event is {}", event);

        try {
            EventType eventType = event.getHeader().getEventType();
            if (EventType.isRowMutation(eventType)) {
                Long tableId = getTableId(event);
                if (tableId == null || !metadataManager.containsColumnMapping4TableId(getTableId(event))) {
                    logger.warn("tableId[{}] do not contains column-mapping", tableId);
                    return;
                }

                BaseEventParser eventParser = EventParserFactory.getParser(eventType);

                if (eventParser != null) {
                    EventWrapper wrapper = new EventWrapper();
                    wrapper.setEvent(event);
                    RowChangeEvent changeEvent = eventParser.parse(wrapper);

                    logger.debug("changes is {}", changeEvent);
                    dispatcher.dispatch(changeEvent);
                } else {
                    logger.error("EventType#{} do not support", eventType);
                }

            } else if (Objects.equals(eventType, EventType.TABLE_MAP)) {
                TableMapEventData data = event.getData();
                TableIdData data1 = new TableIdData();
                data1.setDatabase(data.getDatabase());
                data1.setTable(data.getTable());
                data1.setTableId(data.getTableId());
                metadataManager.putTableIdData(data.getTableId(), data1);
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

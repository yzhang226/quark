package org.lightning.quark.db.plugin.mysql.binlog;


import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import org.lightning.quark.db.copy.DataRowManager;

/**
 * Created by cook on 2018/3/5
 */
public class BinLogEventListener implements BinaryLogClient.EventListener {

    private DataRowManager leftManager;

    private InsertRowEventProcessor insertRowEventProcessor;

    public BinLogEventListener(DataRowManager leftManager) {
        insertRowEventProcessor = new InsertRowEventProcessor(leftManager);
    }

    @Override
    public void onEvent(Event event) {

        if (EventType.isRowMutation(event.getHeader().getEventType())) {
            if (event.getData() instanceof WriteRowsEventData) {
                WriteRowsEventData data = event.getData();
                insertRowEventProcessor.process(data);
            } else if (event.getData() instanceof DeleteRowsEventData) {
                DeleteRowsEventData data = event.getData();

            } else if (event.getData() instanceof UpdateRowsEventData) {
                UpdateRowsEventData data = event.getData();

                data.getIncludedColumns();
                data.getTableId();
                data.getRows();
                data.getIncludedColumnsBeforeUpdate();

            }

        }

    }

}

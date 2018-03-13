package org.lightning.quark.db.dispatcher;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.core.model.column.TableColumnMappings;
import org.lightning.quark.core.subscribe.RowChangeProcessor;
import org.lightning.quark.db.meta.MetadataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 用简单阻塞队列实现,
 * TODO: 后续用akka实现
 *
 * Created by cook on 2018/3/9
 */
public class RowChangeDispatcher {
    
    private static final Logger logger = LoggerFactory.getLogger(RowChangeDispatcher.class);

    private static final BlockingQueue<RowChangeEvent> queue = new ArrayBlockingQueue<>(10000);

    private static final List<RowChangeProcessor> processors = Lists.newArrayList();

    private static boolean isStarted = false;

    private MetadataManager metadataManager;

    public RowChangeDispatcher(MetadataManager metadataManager) {
        this.metadataManager = metadataManager;
    }

    public void addSubscriber(RowChangeProcessor rowChangeProcessor) {
        if (rowChangeProcessor == null) {
            return;
        }
        processors.add(rowChangeProcessor);
    }

    public void dispatch(RowChangeEvent changeEvent) {
        if (changeEvent != null && CollectionUtils.isEmpty(changeEvent.getChanges())) {
            return;
        }
        
        queue.add(changeEvent);
    }

    public synchronized void startListen() {
        if (isStarted) {
            logger.warn("change listen already start");
            return;
        }

        Thread thread = new Thread(() -> {
            logger.info("start listen changes, processors size is {}", processors.size());
            while (true) {
                try {
                    RowChangeEvent event = queue.take();
                    processors.forEach(processor -> {
                        try {
                            logger.debug("row-change-event is {}, processor is {}", event, processor);
                            if (metadataManager.containsColumnMapping(event.getDbName(), event.getTableName())) {
                                processor.process(event);
                            } else {
                                logger.warn("{}.{} do not exist mapping", event.getDbName(), event.getTableName());
                            }
                        } catch (Exception e) {
                            logger.error("process change-event#" + event + " by processor#" + processor + " error", e);
                        }
                    });
                } catch (Exception e) {
                    logger.error("process error", e);
                }
            }
        });
        thread.setName("RowChangeDispatcher");
        thread.setDaemon(true);

        thread.start();

        isStarted = true;
    }


}

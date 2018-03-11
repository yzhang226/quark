package org.lightning.quark.core.subscribe;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.core.row.TableColumnMappings;
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

    public static void addSubscriber(RowChangeProcessor rowChangeProcessor) {
        if (rowChangeProcessor == null) {
            return;
        }
        processors.add(rowChangeProcessor);
    }

    public static void pushChanges(RowChangeEvent changeEvent) {
        if (changeEvent != null && CollectionUtils.isEmpty(changeEvent.getChanges())) {
            return;
        }
        
        queue.add(changeEvent);
    }

    public synchronized static void startListen() {
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
                        if (TableColumnMappings.contains(event.getDbName(), event.getTableName())) {
                            processor.process(event);
                        } else {
                            logger.warn("{}.{} do not exist mapping", event.getDbName(), event.getTableName());
                        }
                    });
                } catch (Exception e) {
                    logger.error("process error", e);
                }
            }
        });
        thread.setName("RowDispatcher");
        thread.setDaemon(true);

        thread.start();

    }


}

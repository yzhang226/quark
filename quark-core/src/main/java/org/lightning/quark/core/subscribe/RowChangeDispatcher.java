package org.lightning.quark.core.subscribe;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.lightning.quark.core.row.RowChange;
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
public abstract class RowChangeDispatcher {
    
    private static final Logger logger = LoggerFactory.getLogger(RowChangeDispatcher.class);

    private static final BlockingQueue<List<RowChange>> queue = new ArrayBlockingQueue<>(10000);

    private static final List<RowChangeProcessor> processors = Lists.newArrayList();

    private static boolean isStarted = false;

    public static void addSubscriber(RowChangeProcessor rowChangeProcessor) {
        if (rowChangeProcessor == null) {
            return;
        }
        processors.add(rowChangeProcessor);
    }

    public static void pushChanges(List<RowChange> changes) {
        if (CollectionUtils.isEmpty(changes)) {
            return;
        }
        
        queue.add(changes);
    }

    public synchronized static void startListen() {
        if (isStarted) {
            logger.warn("change listen already start");
            return;
        }

        List<RowChange> changes = null;
        logger.info("start listen changes, processors size is {}", processors.size());
        while (true) {
            try {
                changes = queue.take();
                List<RowChange> finalChanges = changes;
                processors.forEach(processor -> {
                    processor.process(finalChanges);
                });
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
    }


}

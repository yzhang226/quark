package org.lightning.quark.db.plugin.mysql.binlog;

import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by cook on 2018/3/8
 */
public abstract class TableIdMapping {

    private static final Logger logger = LoggerFactory.getLogger(TableIdMapping.class);

    private static final Map<Long, TableMapEventData> tableIdCaches = Maps.newHashMap();

    public static void put(Long tableId, TableMapEventData data) {
        if (tableIdCaches.containsKey(tableId)) {
            return;
        }
        tableIdCaches.put(tableId, data);

        logger.info("put tableId#{} with info#{}", tableId, data);
    }

    public static TableMapEventData get(Long tableId) {
        return tableIdCaches.get(tableId);
    }

}

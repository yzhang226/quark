package org.lightning.quark.core.row;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.utils.Q;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by cook on 2018/3/1
 */
public abstract class TableColumnMappings {

    private static final Logger logger = LoggerFactory.getLogger(TableColumnMappings.class);

    private static final Map<String, TableColumnMapping> mappings = Maps.newHashMap();

    public static boolean contains(String dbName, String tableName) {
        return mappings.containsKey(Q.getFullName(dbName, tableName));
    }

    public static TableColumnMapping getMapping(String dbName, String tableName) {
        return getMapping(Q.getFullName(dbName, tableName));
    }

    public static TableColumnMapping getMapping(String tableFullName) {
        return mappings.get(tableFullName);
    }

    public static void register(TableColumnMapping mapping) {
        String fullName = mapping.getTableFullName();
        if (mappings.containsKey(fullName)) {
            logger.info("{} exist, try to replace with new mapping", fullName);
        }
        mappings.put(fullName, mapping);
    }

}

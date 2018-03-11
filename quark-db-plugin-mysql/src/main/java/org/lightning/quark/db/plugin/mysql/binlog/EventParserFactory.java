package org.lightning.quark.db.plugin.mysql.binlog;


import com.github.shyiko.mysql.binlog.event.EventType;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by cook on 2018/3/11
 */
public abstract class EventParserFactory {

    private static final Logger logger = LoggerFactory.getLogger(EventParserFactory.class);

    private static final Map<EventType, BaseEventParser> parsers = Maps.newHashMap();

    public static BaseEventParser getParser(EventType type) {
        return parsers.get(type);
    }

    public static void register(BaseEventParser parser) {
        parser.getNeedProcessTypes().forEach(t -> {
            register(t, parser);
        });
    }

    public static void register(EventType type, BaseEventParser parser) {
        parsers.put(type, parser);
        logger.info("register type#{} with parser#{}", type, parser);
    }


}

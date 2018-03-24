package org.lightning.quark.db.plugin.mysql.binlog;

import com.github.shyiko.mysql.binlog.event.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by cook on 2018/3/11
 */
@Getter
@Setter
public class EventWrapper {

    private Event event;


}

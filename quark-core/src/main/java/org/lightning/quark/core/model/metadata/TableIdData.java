package org.lightning.quark.core.model.metadata;

import lombok.Getter;
import lombok.Setter;

/**
 * basic info for table
 * Created by cook on 2018/3/13
 */
@Getter
@Setter
public class TableIdData {

    private long tableId;
    private String database;
    private String table;

}

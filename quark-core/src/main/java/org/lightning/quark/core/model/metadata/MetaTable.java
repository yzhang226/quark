package org.lightning.quark.core.model.metadata;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * Created by cook on 2018/2/23
 */
@Getter
@Setter
public class MetaTable {

    private String name;

    /**
     *
     */
    private MetaPrimaryKey primaryKey;

    /**
     * all columns of this table
     */
    private List<MetaColumn> columns;

}

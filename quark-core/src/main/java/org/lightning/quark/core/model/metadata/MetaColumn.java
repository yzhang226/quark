package org.lightning.quark.core.model.metadata;

import lombok.Getter;
import lombok.Setter;

import java.sql.JDBCType;

/**
 * Created by cook on 2018/2/25
 */
@Getter
@Setter
public class MetaColumn {

    /**
     * name of this column
     */
    private String name;

    /**
     * type of column (int)
     */
    private int type;

    /**
     * type of column (enum)
     */
    private JDBCType jdbcType;



}

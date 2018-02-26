package org.lightning.quark.core.model.metadata;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by cook on 2018/2/25
 */
@Getter
@Setter
public class MetaDatabase {

    /**
     * all tables(s) for this database
     */
    private List<MetaTable> tables;

}

package org.lightning.quark.core.model.metadata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cook on 2018/2/27
 */
@Getter
@Setter
public class MetaCatalog {

    private MetaDatabase database;

    private MetaJdbcDriver jdbcDriver;

}

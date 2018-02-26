package org.lightning.quark.core.model.metadata;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by cook on 2018/2/23
 */
@Getter
@Setter
public class MetaPrimaryKey {

    private String name;

    private List<MetaColumn> columns;


}

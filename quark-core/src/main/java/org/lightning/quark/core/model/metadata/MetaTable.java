package org.lightning.quark.core.model.metadata;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by cook on 2018/2/23
 */
@Getter
@Setter
public class MetaTable {

    private MetaCatalog catalog;

    private String name;

    /**
     *
     */
    private MetaPrimaryKey primaryKey;

    /**
     * all columns of this table
     */
    private List<MetaColumn> columns;


    //
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<String> pkNames;


    public boolean isPrimaryKey(String columnName) {
        if (pkNames != null) {
            return pkNames.contains(columnName);
        }

        synchronized (this) {
            if (pkNames == null) {
                pkNames = primaryKey.getColumns().stream()
                        .map(MetaColumn::getName)
                        .collect(Collectors.toList());
            }
        }
        return pkNames.contains(columnName);
    }

}

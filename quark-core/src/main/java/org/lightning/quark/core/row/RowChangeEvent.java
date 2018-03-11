package org.lightning.quark.core.row;

import lombok.Getter;
import lombok.Setter;
import org.lightning.quark.core.model.db.RowDataInfo;

import java.util.List;

/**
 * Created by cook on 2018/3/5
 */
@Getter
@Setter
public class RowChangeEvent {

    /**
     * 发生变更的数据库名
     */
    private String dbName;

    /**
     * 发生变更的表名
     */
    private String tableName;

    /**
     * 发生具体变更(s)
     */
    private List<RowChange> changes;


}

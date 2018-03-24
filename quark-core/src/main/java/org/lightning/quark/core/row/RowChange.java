package org.lightning.quark.core.row;

import lombok.Getter;
import lombok.Setter;
import org.lightning.quark.core.model.db.RowDataInfo;

/**
 * Created by cook on 2018/3/5
 */
@Getter
@Setter
public class RowChange {

    private RowChangeType eventType;

    /**
     * insert, update, delete, 此记录当前值
     */
    private RowDataInfo currentRow;

    /**
     * 更新时, 此为旧值
     */
    private RowDataInfo previousRow;

}

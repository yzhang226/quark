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

    private RowDataInfo currentRow;

    private RowDataInfo previousRow;

}

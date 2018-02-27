package org.lightning.quark.core.diff;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.lightning.quark.core.model.db.RowDataInfo;

/**
 * Created by cook on 2018/2/27
 */
@Getter
@Setter
public class RowDifference {

    private RowDataInfo sourceRow;
    private RowDataInfo targetRow;

    @Setter(AccessLevel.NONE)
    private DifferenceType result;


    public void executeDiff() {

    }


}

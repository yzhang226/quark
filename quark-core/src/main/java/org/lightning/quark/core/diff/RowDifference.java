package org.lightning.quark.core.diff;

import lombok.Getter;
import lombok.Setter;
import org.lightning.quark.core.model.db.RowDataInfo;

import java.util.Map;

/**
 * Created by cook on 2018/2/27
 */
@Getter
public class RowDifference {

    private final RowDataInfo left;
    private final RowDataInfo right;

    @Setter
    private DifferenceType diffType;

    /**
     * 比较差异 - 结果
     */
    @Setter
    private Map<String, Object> result;

    public RowDifference(RowDataInfo left, RowDataInfo right) {
        this.left = left;
        this.right = right;
    }

}

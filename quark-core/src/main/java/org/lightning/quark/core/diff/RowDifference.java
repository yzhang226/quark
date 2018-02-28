package org.lightning.quark.core.diff;

import lombok.Getter;
import org.apache.commons.collections4.MapUtils;
import org.lightning.quark.core.model.db.RowDataInfo;
import org.lightning.quark.core.utils.RowDiffUtils;

import java.util.Map;

/**
 * Created by cook on 2018/2/27
 */
@Getter
public class RowDifference {

    private final RowDataInfo left;
    private final RowDataInfo right;

    private DifferenceType diffType;

    /**
     * 比较差异 - 结果 
     */
    private Map<String, Object> result;

    public RowDifference(RowDataInfo left, RowDataInfo right) {
        this.left = left;
        this.right = right;
    }

    /**
     * 计算
     */
    public void calcDiff() {
        if (RowDiffUtils.isNotEmpty(left) && RowDiffUtils.isEmpty(right)) {
            diffType = DifferenceType.ONLY_IN_LEFT;
        } else if (RowDiffUtils.isEmpty(left) && RowDiffUtils.isNotEmpty(right)) {
            diffType = DifferenceType.ONLY_IN_RIGHT;
        } else {
            result = RowDiffUtils.diffRow(left, right);
            if (MapUtils.isEmpty(result)) {
                diffType = DifferenceType.NONE;
            } else {
                diffType = DifferenceType.NOT_EQUALS;
            }
        }
    }


}

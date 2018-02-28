package org.lightning.quark.core.diff;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.db.RowDataInfo;
import org.lightning.quark.core.utils.QuarkAssertor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cook on 2018/2/28
 */
public class DifferenceManager {

    /**
     * 计算批量行 - 差异
     * @param lefts
     * @param rights
     * @return
     */
    public Map<DifferenceType, List<RowDifference>> calcBatchRowDiffs(Map<PKData, RowDataInfo> lefts,
                                                                      Map<PKData, RowDataInfo> rights) {
        Map<DifferenceType, List<RowDifference>> diffs = Maps.newHashMap();
        for (PKData leftPk : lefts.keySet()) {
            RowDataInfo left = lefts.get(leftPk);
            RowDataInfo right = rights.get(leftPk);

            RowDifference difference = calcRowDiff(left, right);
            diffs.computeIfAbsent(difference.getDiffType(), k -> Lists.newArrayList()).add(difference);
        }

        return diffs;
    }

    /**
     * 计算一行 - 差异, 左右两侧的主键须一致
     * @param left
     * @param right
     * @return
     */
    public RowDifference calcRowDiff(RowDataInfo left, RowDataInfo right) {
        if (left != null && right != null) {
            QuarkAssertor.isTrue(Objects.equals(left.getPk(), right.getPk()), "左右两侧的主键不一致");
        }
        RowDifference difference = new RowDifference(left, right);
        difference.calcDiff();
        return difference;
    }

}

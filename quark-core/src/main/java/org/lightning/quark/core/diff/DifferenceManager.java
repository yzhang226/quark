package org.lightning.quark.core.diff;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.db.RowDataInfo;
import org.lightning.quark.core.row.TableColumnMapping;
import org.lightning.quark.core.utils.QuarkAssertor;
import org.lightning.quark.core.utils.RowDiffUtils;

import java.util.*;

/**
 * Created by cook on 2018/2/28
 */
public class DifferenceManager {

    private TableColumnMapping columnMapping;

    public DifferenceManager(TableColumnMapping columnMapping) {
        this.columnMapping = columnMapping;
    }

    /**
     * 计算
     */
    public RowDifference calcDiff(RowDataInfo left, RowDataInfo right) {
        DifferenceType diffType = null;
        Map<String, Object> result = null;
        if (RowDiffUtils.isNotEmpty(left) && RowDiffUtils.isEmpty(right)) {
            diffType = DifferenceType.ONLY_IN_LEFT;
        } else if (RowDiffUtils.isEmpty(left) && RowDiffUtils.isNotEmpty(right)) {
            diffType = DifferenceType.ONLY_IN_RIGHT;
        } else {
            result = diffRow(left, right);
            if (MapUtils.isEmpty(result)) {
                diffType = DifferenceType.NONE;
            } else {
                diffType = DifferenceType.NOT_EQUALS;
            }
        }
        RowDifference difference = new RowDifference(left, right);
        difference.setDiffType(diffType);
        difference.setResult(result);
        return difference;
    }

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
        return calcDiff(left, right);
    }

    // ---
    public Map<String, Object> diffRow(Map<String, Object> left, Map<String, Object> right) {
        Map<String, Object> diffed = Maps.newHashMap();
        Set<String> leftColumnNames = left.keySet();
        for (String leftColumnName : leftColumnNames) {
            String rightColumnName = columnMapping.getRightColumnName(leftColumnName);
            Object leftValue = left.get(leftColumnName);
            Object rightValue = right.get(rightColumnName);
            if (leftValue instanceof Date && rightValue instanceof Date) {
                if (Math.abs(((Date) leftValue).getTime() - ((Date) rightValue).getTime()) > 1000) {
                    // mysql datetime(0) 不存储毫秒部分, datetime(3) 会存储毫秒
                    diffed.put(rightColumnName, leftValue);
                }
            } else if (leftValue instanceof Number && rightValue instanceof Number) {
                if (Math.abs((((Number) leftValue).doubleValue() - ((Number) rightValue).doubleValue())) > 0.0000001d) {
                    diffed.put(rightColumnName, leftValue);
                }
            } else if (leftValue instanceof String && rightValue instanceof String) {
                if (!StringUtils.equals(StringUtils.trimToNull(((String) leftValue)), StringUtils.trimToNull(((String) rightValue)))) {
                    diffed.put(rightColumnName, leftValue);
                }
            } else {
                if (!Objects.equals(leftValue, rightValue)) {
                    diffed.put(rightColumnName, leftValue);
                }
            }
        }

        return diffed;
    }

    /**
     * 左右两行数据是否一致
     * @param left
     * @param right
     * @return
     */
    public boolean isEquals(RowDataInfo left, RowDataInfo right) {
        return MapUtils.isNotEmpty(diffRow(left, right));
    }

    /**
     *
     * @param left
     * @param right
     * @return
     */
    public Map<String, Object> diffRow(RowDataInfo left, RowDataInfo right) {
        return diffRow(left.getRow(), right.getRow());
    }

    /**
     * 设置时间类型 值 的差异,
     * @return
     */
    public int getDateTimeDiffMills() {
        return 1000;
    }

}

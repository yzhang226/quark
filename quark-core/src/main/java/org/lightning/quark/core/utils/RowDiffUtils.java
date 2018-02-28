package org.lightning.quark.core.utils;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.model.db.RowDataInfo;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cook on 2018/2/27
 */
public abstract class RowDiffUtils {

    public static boolean isEmpty(RowDataInfo info) {
        return info == null || info.isEmpty();
    }

    public static boolean isNotEmpty(RowDataInfo info) {
        return !isEmpty(info);
    }

    public static Map<String, Object> diffRow(Map<String, Object> left, Map<String, Object> right) {
        Map<String, Object> diffed = Maps.newHashMap();
        left.keySet().forEach(col -> {
            Object srcObj = left.get(col);
            Object tarObj = right.get(col);
            if (srcObj instanceof Date && tarObj instanceof Date) {
                if (Math.abs(((Date) srcObj).getTime() - ((Date) tarObj).getTime()) > 1000) {// mysql datetime 不存储毫秒部分
                    diffed.put(col, srcObj);
                }
            } else if (srcObj instanceof Number && tarObj instanceof Number) {
                if (Math.abs((((Number) srcObj).doubleValue() - ((Number) tarObj).doubleValue())) > 0.0000001d) {
                    diffed.put(col, srcObj);
                }
            } else if (srcObj instanceof String && tarObj instanceof String) {
                if (!StringUtils.equals(StringUtils.trimToNull(((String) srcObj)), StringUtils.trimToNull(((String) tarObj)))) {
                    diffed.put(col, srcObj);
                }
            } else {
                if (!Objects.equals(srcObj, tarObj)) {
                    diffed.put(col, srcObj);
                }
            }
        });

        return diffed;
    }

    /**
     * 左右两行数据是否一致
     * @param left
     * @param right
     * @return
     */
    public static boolean isEquals(RowDataInfo left, RowDataInfo right) {
        return MapUtils.isNotEmpty(diffRow(left, right));
    }

    /**
     *
     * @param left
     * @param right
     * @return
     */
    public static Map<String, Object> diffRow(RowDataInfo left, RowDataInfo right) {
        return diffRow(left.getRow(), right.getRow());
    }

}

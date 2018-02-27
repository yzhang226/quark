package org.lightning.quark.core.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by cook on 2018/2/27
 */
public abstract class RowDiffUtils {



    public static Map<String, Object> diffRow(Map<String, Object> src, Map<String, Object> tar) {
        Map<String, Object> diffed = Maps.newHashMap();
        src.keySet().forEach(col -> {
            Object srcObj = src.get(col);
            Object tarObj = tar.get(col);
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

}

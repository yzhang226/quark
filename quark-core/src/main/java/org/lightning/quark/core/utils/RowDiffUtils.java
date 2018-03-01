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



}

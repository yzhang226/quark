package org.lightning.quark.core.utils;

import com.google.common.collect.Lists;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cook on 2018/3/11
 */
public abstract class Q {

    private static final Logger logger = LoggerFactory.getLogger(Q.class);

    /**
     *
     * @param dbName
     * @param tableName
     * @return
     */
    public static String getFullName(String dbName, String tableName) {
        return dbName + "." + tableName;
    }

    /**
     *
     * @param mills
     */
    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (Exception e) {
            logger.error("sleep error", e);
        }
    }


    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T target = newInstance(clazz);
        BeanCopier copier = BeanCopier.create(source.getClass(), clazz, false);
        copier.copy(source, target, null);
        return target;
    }

    public static <T> List<T> copyList(List<?> list, Class<T> clazz) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<T> copied = Lists.newArrayList();
        for (Object data : list) {
            copied.add(copy(data, clazz));
        }
        return copied;
    }

    public static <T> T newInstance(Class<T> clazz) {
        if (clazz == null) {
            throw new QuarkExecuteException("clazz必须不为空");
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new QuarkExecuteException("", e);
        }

    }

}

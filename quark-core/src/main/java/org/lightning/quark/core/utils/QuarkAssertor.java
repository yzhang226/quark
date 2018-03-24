package org.lightning.quark.core.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.lightning.quark.core.exception.QuarkAssertException;

/**
 * Created by cook on 2018/2/28
 */
public abstract class QuarkAssertor {

    public static void isTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            if (ArrayUtils.isNotEmpty(args)) {
                message = String.format(message, args);
            }
            throw new QuarkAssertException(message);
        }
    }

}

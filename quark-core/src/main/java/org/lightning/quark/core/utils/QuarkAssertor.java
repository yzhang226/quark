package org.lightning.quark.core.utils;

import org.lightning.quark.core.exception.QuarkAssertException;

/**
 * Created by cook on 2018/2/28
 */
public abstract class QuarkAssertor {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new QuarkAssertException(message);
        }
    }

}

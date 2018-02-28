package org.lightning.quark.core.exception;

/**
 * Created by cook on 2018/2/25
 */
public class QuarkAssertException extends RuntimeException {

    public QuarkAssertException(String message) {
        super(message);
    }

    public QuarkAssertException(String message, Throwable cause) {
        super(message, cause);
    }

}

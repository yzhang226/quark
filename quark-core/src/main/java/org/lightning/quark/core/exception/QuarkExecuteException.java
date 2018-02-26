package org.lightning.quark.core.exception;

/**
 * Created by cook on 2018/2/25
 */
public class QuarkExecuteException extends RuntimeException {

    public QuarkExecuteException(String message) {
        super(message);
    }

    public QuarkExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

}

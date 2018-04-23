package org.lightning.quark.core.exception;

/**
 * json解析运行是异常
 * Created by cook on 2017/7/1.
 */
public class JsonParseRuntimeException extends RuntimeException {

    public JsonParseRuntimeException(String message) {
        super(message);
    }

    public JsonParseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}

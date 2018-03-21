package org.lightning.quark.core.message;

import lombok.Getter;
import lombok.Setter;

/**
 * Action处理结果
 * Created by cook on 2017/7/17.
 */
@Getter
@Setter
public class ProcessResult<T> {

    private final int status;
    private final String msg;
    private final T data;

    private ProcessResult(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public boolean isOk() {
        return status == 0;
    }

    public boolean isFail() {
        return !isOk();
    }

    //
    public static ProcessResult ok() {
        return ok("ok", null);
    }

    public static <T> ProcessResult ok(T data) {
        return ok("ok", data);
    }

    public static <T> ProcessResult ok(String msg, T data) {
        return new ProcessResult<>(0, msg, data);
    }

    public static ProcessResult fail() {
        return fail("fail");
    }

    public static ProcessResult fail(String msg) {
        return fail(msg, new Object());
    }

    public static <T> ProcessResult fail(String msg, T data) {
        return new ProcessResult<>(500, msg, data);
    }

}

package org.lightning.quark.core.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by cook on 2018/4/23
 */
@Getter
@Setter
public class DataResult<T> {


    private final int status;
    private final String msg;
    private final T data;

    private DataResult(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isOk() {
        return status == 0;
    }

    @JsonIgnore
    public boolean isFail() {
        return !isOk();
    }

    //
    public static DataResult ok() {
        return ok("ok", null);
    }

    public static <T> DataResult<T> ok(T data) {
        return ok("ok", data);
    }

    public static <T> DataResult<T> ok(String msg, T data) {
        return new DataResult<>(0, msg, data);
    }

    public static DataResult fail() {
        return fail("fail", null);
    }

    public static DataResult fail(String msg) {
        return fail(msg, null);
    }

    public static <T> DataResult<T> fail(String msg, T data) {
        return new DataResult<>(1000, msg, data);
    }


}

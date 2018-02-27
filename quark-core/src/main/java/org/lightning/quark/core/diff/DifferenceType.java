package org.lightning.quark.core.diff;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by cook on 2018/2/27
 */
@Getter
@AllArgsConstructor
public enum DifferenceType {

    ONLY_IN_SOURCE(1, "源存在, 目标不存在"),
    NOT_EQUALS(2, "都存在, 有差异"),
    ONLY_IN_TARGET(3, "源不存在, 目标存在"),
    NONE(4, "完成一致, 无差异");

    private int code;

    private String memo;

}

package org.lightning.quark.core.row;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by cook on 2018/3/5
 */
@Getter
@AllArgsConstructor
public enum RowChangeType {

    INSERT(1, "新增"),

    DELETE(2, "删除"),

    UPDATE(3, "更新");

    private int code;
    private String description;

}

package org.lightning.quark.core.model.db;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 原始行信息
 * Created by cook on 2018/2/27
 */
@Getter
@Setter
public class RowDataInfo {

    private PKData pk;
    private Map<String, Object> row;

    public RowDataInfo() {
    }

    public RowDataInfo(PKData pk) {
        this.pk = pk;
    }

    public boolean isEmpty() {
        return row == null || row.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

}

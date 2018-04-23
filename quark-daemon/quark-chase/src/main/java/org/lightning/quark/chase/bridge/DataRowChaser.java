package org.lightning.quark.chase.bridge;

import org.lightning.quark.core.model.db.RowDataInfo;

import java.util.List;

/**
 * 数据行 追溯
 */
public interface DataRowChaser {

    /**
     * 追溯一行数据变更 值 目标源
     * @param row
     * @return
     */
    int chaseORowData(RowDataInfo row);

    /**
     * 追溯多行行数据变更 值 目标源
     * @param rows
     * @return
     */
    int chaseRowData(List<RowDataInfo> rows);

}

package org.lightning.quark.chase.bridge;

import org.lightning.quark.core.model.db.RowDataInfo;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.copy.DataRowManager;

import java.util.List;

/**
 * Created by cook on 2018/3/5
 */
public abstract class BaseDataRowChaser implements DataRowChaser {

    protected DataRowManager leftManager;

    protected DataRowManager rightManager;

    protected MetaTable table;



}

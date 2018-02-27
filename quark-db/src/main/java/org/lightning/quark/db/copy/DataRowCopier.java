package org.lightning.quark.db.copy;

import org.lightning.quark.core.model.db.PKData;

/**
 * Created by cook on 2018/2/26
 */
public class DataRowCopier {

    private DataRowManager sourceManager;
    private DataRowManager targetManager;

    public DataRowCopier(DataRowManager sourceManager, DataRowManager targetManager) {
        this.sourceManager = sourceManager;
        this.targetManager = targetManager;
    }

    public void copyByStep(PKData startPk, int pageSize) {
//        sourceManager.
    }

}

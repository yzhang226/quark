package org.lightning.quark.chase.subscribe;

import org.lightning.quark.chase.copy.DataRowCopier;
import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.subscribe.RowChangeProcessor;

import java.util.List;

/**
 * Created by cook on 2018/3/8
 */
public class RowChangeSubscriber implements RowChangeProcessor {

    private DataRowCopier dataRowCopier;

    public RowChangeSubscriber(DataRowCopier dataRowCopier) {
        this.dataRowCopier = dataRowCopier;
    }

    @Override
    public CopyResult process(List<RowChange> changes) {
        return dataRowCopier.copyByChanges(changes);
    }

}

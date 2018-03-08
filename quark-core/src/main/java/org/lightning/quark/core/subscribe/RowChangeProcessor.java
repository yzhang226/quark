package org.lightning.quark.core.subscribe;

import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.row.RowChange;

import java.util.List;

/**
 * Created by cook on 2018/3/9
 */
public interface RowChangeProcessor {

    /**
     *
     * @param changes
     * @return
     */
    CopyResult process(List<RowChange> changes);

}

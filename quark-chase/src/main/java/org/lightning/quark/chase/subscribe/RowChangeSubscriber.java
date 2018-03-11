package org.lightning.quark.chase.subscribe;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import org.lightning.quark.chase.copy.DataRowCopier;
import org.lightning.quark.chase.utils.DataCopyUtils;
import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.core.subscribe.RowChangeProcessor;
import org.lightning.quark.core.utils.Q;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by cook on 2018/3/8
 */
public class RowChangeSubscriber implements RowChangeProcessor {

    private static final Map<String, DataRowCopier> cache = Maps.newHashMap();

    private static final Interner<String> lock = Interners.newWeakInterner();

    private DataSource leftDataSource;
    private DataSource rightDataSource;

    public RowChangeSubscriber(DataSource leftDataSource, DataSource rightDataSource) {
        this.leftDataSource = leftDataSource;
        this.rightDataSource = rightDataSource;
    }

    @Override
    public CopyResult process(RowChangeEvent changeEvent) {
        DataRowCopier dataRowCopier = getDataCopier(changeEvent.getDbName(), changeEvent.getTableName());
        return dataRowCopier.copyChanges(changeEvent.getChanges());
    }

    private DataRowCopier getDataCopier(String dbName, String tableName) {
        String fullName = Q.getFullName(dbName, tableName);
        DataRowCopier copier = cache.get(fullName);
        if (copier != null) {
            return copier;
        }

        synchronized (lock.intern(fullName)) {
            if (!cache.containsKey(fullName)) {
                copier = DataCopyUtils.createCopier(leftDataSource, rightDataSource, dbName, tableName);
                cache.put(fullName, copier);
            }
        }

        return cache.get(fullName);
    }

}

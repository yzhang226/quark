package org.lightning.quark.chase.subscribe;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import org.lightning.quark.chase.copy.DataRowCopier;
import org.lightning.quark.core.diff.DifferenceManager;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.column.TableColumnMapping;
import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.row.RowChangeEvent;
import org.lightning.quark.core.subscribe.RowChangeProcessor;
import org.lightning.quark.core.utils.Q;
import org.lightning.quark.core.utils.QuarkAssertor;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.meta.MetadataManager;
import org.lightning.quark.db.sql.SqlProvider;
import org.lightning.quark.db.sql.SqlProviderFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by cook on 2018/3/8
 */
public class RowChangeSubscriber implements RowChangeProcessor {

    private static final Map<String, DataRowCopier> cache = Maps.newHashMap();

    private static final Interner<String> lock = Interners.newWeakInterner();

    private MetadataManager leftManager;
    private MetadataManager rightManager;

    public RowChangeSubscriber(MetadataManager leftManager, MetadataManager rightManager) {
        this.leftManager = leftManager;
        this.rightManager = rightManager;
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
                copier = createCopier(leftManager.getDataSource(), rightManager.getDataSource(), dbName, tableName);
                cache.put(fullName, copier);
            }
        }

        return cache.get(fullName);
    }

    /**
     *
     * @param leftDataSource
     * @param rightDataSource
     * @param leftDbName
     * @param leftTableName
     * @return
     * @throws SQLException
     */
    public DataRowCopier createCopier(DataSource leftDataSource, DataSource rightDataSource,
                                      String leftDbName, String leftTableName) {

        try {
            TableColumnMapping mapping = leftManager.getColumnMapping(leftDbName, leftTableName);
            QuarkAssertor.isTrue(mapping != null, "mapping not exist for db[%s].table[%s]", leftDbName, leftTableName);

            MetaTable leftTable = leftManager.getTable(leftTableName);
            MetaTable rightTable = rightManager.getTable(mapping.getRightTableName());

            SqlProvider leftSqlProvider = SqlProviderFactory.createProvider(leftTable);
            SqlProvider rightSqlProvider = SqlProviderFactory.createProvider(rightTable);

            DataRowManager sourceManager = new DataRowManager(leftTable, leftDataSource, leftSqlProvider, mapping);
            DataRowManager targetManager = new DataRowManager(rightTable, rightDataSource, rightSqlProvider, mapping);

            DifferenceManager differenceManager = new DifferenceManager(mapping);

            return new DataRowCopier(sourceManager, targetManager, differenceManager, mapping);
        } catch (Exception e) {
            throw new QuarkExecuteException("createCopier for " + leftDbName + "." + leftTableName + " error", e);
        }
    }

}

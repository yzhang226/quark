package org.lightning.quark.chase.utils;

import org.lightning.quark.chase.copy.DataRowCopier;
import org.lightning.quark.core.diff.DifferenceManager;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.row.TableColumnMapping;
import org.lightning.quark.core.row.TableColumnMappings;
import org.lightning.quark.core.utils.QuarkAssertor;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.crawler.TableMetadataFetcher;
import org.lightning.quark.db.sql.SqlProvider;
import org.lightning.quark.db.sql.SqlProviderFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by cook on 2018/3/11
 */
public abstract class DataCopyUtils {

    /**
     *
     * @param leftDataSource
     * @param rightDataSource
     * @param leftDbName
     * @param leftTableName
     * @return
     * @throws SQLException
     */
    public static DataRowCopier createCopier(DataSource leftDataSource, DataSource rightDataSource,
                                             String leftDbName, String leftTableName) {

        try {
            TableColumnMapping mapping = TableColumnMappings.getMapping(leftDbName, leftTableName);
            QuarkAssertor.isTrue(mapping != null, "mapping not exist for db[%s].table[%s]", leftDbName, leftTableName);

            TableMetadataFetcher leftFetcher = new TableMetadataFetcher(leftDataSource);
            MetaTable leftTable = leftFetcher.fetchMetaTableInCache(leftTableName);

            TableMetadataFetcher rightFetcher = new TableMetadataFetcher(rightDataSource);
            MetaTable rightTable = rightFetcher.fetchMetaTableInCache(mapping.getRightTableName());

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

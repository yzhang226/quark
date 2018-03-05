package org.lightning.quark.chase;

import org.lightning.quark.chase.copy.DataRowCopier;
import org.lightning.quark.core.diff.DifferenceManager;
import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.row.TableColumnMapping;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.crawler.TableMetadataFetcher;
import org.lightning.quark.db.sql.SqlProvider;
import org.lightning.quark.db.sql.SqlProviderFactory;
import org.lightning.quark.db.test.BaseMySQLTestCase;

import java.util.List;

/**
 * Created by cook on 2018/3/1
 */
public class DbCopierTest extends BaseMySQLTestCase {

    private DataRowCopier dataRowCopier;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String tableNames = "alarm_user";
        TableMetadataFetcher fetcher = new TableMetadataFetcher(dataSource.getConnection());
        List<MetaTable> metaTables = fetcher.fetchMetaTables(tableNames);
        MetaTable leftTable = metaTables.get(0);

        String tableNames2 = "alarm_user3";
        List<MetaTable> metaTables2 = fetcher.fetchMetaTables(tableNames2);
        MetaTable rightTable = metaTables2.get(0);

        TableColumnMapping columnMapping = new TableColumnMapping(leftTable);
        columnMapping.addMapping("user_name", "user_name2");
        columnMapping.addMapping("email", "email3");
        columnMapping.addMapping("telephone", "telephone4");
        columnMapping.addMapping("weixin_account", "weixin_account5");

        SqlProvider leftSqlProvider = SqlProviderFactory.createProvider(leftTable);
        SqlProvider rightSqlProvider = SqlProviderFactory.createProvider(rightTable);

        DataRowManager sourceManager = new DataRowManager(leftTable, dataSource, leftSqlProvider, columnMapping);
        DataRowManager targetManager = new DataRowManager(rightTable, dataSource, rightSqlProvider, columnMapping);

        DifferenceManager differenceManager = new DifferenceManager(columnMapping);

        dataRowCopier = new DataRowCopier(sourceManager, targetManager, differenceManager, columnMapping);

    }

    public void testOneBatch() {
        PKData startPk = new PKData();
        startPk.addOnePk("id", 2);
        CopyResult cr = dataRowCopier.copyByStep(startPk, 4);

        System.out.println(cr.toText());

    }



}

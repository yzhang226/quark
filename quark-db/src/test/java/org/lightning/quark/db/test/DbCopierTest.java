package org.lightning.quark.db.test;

import org.lightning.quark.core.diff.DifferenceManager;
import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.copy.DataRowCopier;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.crawler.TableMetadataFetcher;

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

        String tableNames2 = "alarm_user2";
        List<MetaTable> metaTables2 = fetcher.fetchMetaTables(tableNames2);
        MetaTable rightTable = metaTables2.get(0);

        DataRowManager sourceManager = new DataRowManager(leftTable, dataSource);
        DataRowManager targetManager = new DataRowManager(rightTable, dataSource);
        DifferenceManager differenceManager = new DifferenceManager();

        dataRowCopier = new DataRowCopier(sourceManager, targetManager, differenceManager);

    }

    public void testOneBatch() {
        PKData startPk = new PKData();
        startPk.addOnePk("id", 2);
        CopyResult cr = dataRowCopier.copyByStep(startPk, 4);

        System.out.println(cr.toText());

    }



}

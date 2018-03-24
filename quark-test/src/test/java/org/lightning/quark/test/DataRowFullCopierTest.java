package org.lightning.quark.test;

import org.lightning.quark.chase.copy.DataRowCopier;
import org.lightning.quark.chase.copy.DataRowFullCopier;
import org.lightning.quark.chase.processor.StepRowCopyProcessor;
import org.lightning.quark.chase.subscribe.RowChangeSubscriber;
import org.lightning.quark.core.diff.DifferenceManager;
import org.lightning.quark.core.dispatch.ActionMessageDispatcher;
import org.lightning.quark.core.model.column.TableColumnMapping;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.core.utils.Q;
import org.lightning.quark.db.copy.DataRowManager;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.db.meta.MetadataManager;
import org.lightning.quark.db.plugin.mssql.cdc.CdcEventScanner;
import org.lightning.quark.db.sql.SqlProvider;
import org.lightning.quark.db.sql.SqlProviderFactory;
import org.lightning.quark.test.base.BaseSQLServerTestCase;

import javax.sql.DataSource;

/**
 * Created by cook on 2018/3/22
 */
public class DataRowFullCopierTest extends BaseSQLServerTestCase {

    private CdcEventScanner cdcEventScanner;
    private MetadataManager rightManager;
    private DbManager rightDbManager;

    private DataRowFullCopier fullCopier;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DataSource rightDataSource = DbTestUtils.createDemoMySQLDS4Monitor();
        rightManager = new MetadataManager(rightDataSource);
        rightDbManager = new DbManager(rightDataSource);

        RowChangeSubscriber subscriber = new RowChangeSubscriber(metadataManager, rightManager);
        dispatcher.addSubscriber(subscriber);
        dispatcher.startListen();


        String leftDbName = "HJ_CRM";
        String leftTableName = "evt_MarketingEvents";

        String rightDbName = "monitor";
        String rightTableName = "evt_MarketingEvents";

        TableColumnMapping mapping = new TableColumnMapping(leftDbName, rightDbName, leftTableName, rightTableName);
        mapping.addMapping("EventID","EventID");
        mapping.addMapping("ProfileID","ProfileID");
        mapping.addMapping("MarketingType","MarketingType");
        mapping.addMapping("Memo","Memo");
        mapping.addMapping("EventTime","EventTime");
        mapping.addMapping("MarketingResult","MarketingResult");
        mapping.addMapping("CreateUser","CreateUser");
        mapping.addMapping("CreateTime","CreateTime");
        mapping.addMapping("HandledBy","HandledBy");
        mapping.addMapping("ReferenceCustomerEvent","ReferenceCustomerEvent");
        mapping.addMapping("BusinessProject","BusinessProject");
        mapping.addMapping("IsBackFlow","IsBackFlow");
        mapping.addMapping("IntentionProject","IntentionProject");
        mapping.addMapping("BackFlowCause","BackFlowCause");
        mapping.addMapping("LastMarketingStatus","LastMarketingStatus");
        mapping.addMapping("IntentionLevel","IntentionLevel");
        mapping.addMapping("LastVisitMemo","LastVisitMemo");
        mapping.addMapping("LastVisitUserId","LastVisitUserId");
        mapping.addMapping("LastVisitUserName","LastVisitUserName");
        mapping.addMapping("LastVisitTime","LastVisitTime");
        mapping.addMapping("IsTransfer","IsTransfer");
        mapping.addMapping("IsClaim","IsClaim");
        mapping.addMapping("AssignTime","AssignTime");
        mapping.addMapping("ReleaseTime","ReleaseTime");
        mapping.addMapping("IsAttendAudition","IsAttendAudition");
        mapping.addMapping("IsInviteAudition","IsInviteAudition");
        mapping.addMapping("IsRemindAudition","IsRemindAudition");
        mapping.addMapping("IsSatisfactionAudition","IsSatisfactionAudition");
        mapping.addMapping("AuditionTime","AuditionTime");
        mapping.addMapping("NotAttendAuditionCause","NotAttendAuditionCause");
        mapping.addMapping("DissatisfactionCause","DissatisfactionCause");
        mapping.addMapping("LastListenNeed","LastListenNeed");
        mapping.addMapping("IsStar","IsStar");
        mapping.addMapping("NotIntensionReason","NotIntensionReason");
        mapping.addMapping("AgeGroupType","AgeGroupType");

        metadataManager.registerColumnMapping(mapping);

        cdcEventScanner = new CdcEventScanner(dispatcher, metadataManager, dbManager);
        cdcEventScanner.addScanTable("evt_MarketingEvents");

        ActionMessageDispatcher dispatcher = new ActionMessageDispatcher(true, 8);

        DifferenceManager differenceManager = new DifferenceManager(mapping);

        MetaTable leftTable = metadataManager.getTable(leftTableName);
        MetaTable rightTable = rightManager.getTable(rightTableName);

        SqlProvider leftSqlProvider = SqlProviderFactory.createProvider(leftTable);
        SqlProvider rightSqlProvider = SqlProviderFactory.createProvider(rightTable);

        DataRowManager sourceManager = new DataRowManager(leftTable, dataSource, leftSqlProvider, mapping);
        DataRowManager targetManager = new DataRowManager(rightTable, rightDataSource, rightSqlProvider, mapping);


        fullCopier = new DataRowFullCopier(sourceManager, targetManager, differenceManager, mapping, dispatcher);

        DataRowCopier dataRowCopier = new DataRowCopier(sourceManager, targetManager, differenceManager, mapping);

        new StepRowCopyProcessor(dataRowCopier);

    }

    public void testFullCopy() {
        fullCopier.copyFullTable();

        logger.info("test full copy done");

        Q.sleep(20 * 60 * 1000);
    }


}

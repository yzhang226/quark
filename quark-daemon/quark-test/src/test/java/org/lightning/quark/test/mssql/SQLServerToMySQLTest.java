package org.lightning.quark.test.mssql;

import org.lightning.quark.chase.copy.DataRowFullCopier;
import org.lightning.quark.chase.subscribe.RowChangeSubscriber;
import org.lightning.quark.core.model.column.TableColumnMapping;
import org.lightning.quark.core.utils.Q;
import org.lightning.quark.db.datasource.DbManager;
import org.lightning.quark.db.meta.MetadataManager;
import org.lightning.quark.db.plugin.mssql.cdc.CdcEventScanner;
import org.lightning.quark.test.DbTestUtils;
import org.lightning.quark.test.base.BaseSQLServerTestCase;

import javax.sql.DataSource;

/**
 * Created by cook on 2018/3/14
 */
public class SQLServerToMySQLTest extends BaseSQLServerTestCase {

    private CdcEventScanner cdcEventScanner;
    private MetadataManager rightManager;
    private DbManager rightDbManager;

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



    }

    public void testCdcToMysql() {

        cdcEventScanner.startScanJob();

//        Q.sleep(30 * 60 * 1000);
    }



}

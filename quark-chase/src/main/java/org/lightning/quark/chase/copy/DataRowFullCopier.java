package org.lightning.quark.chase.copy;

import org.lightning.quark.core.diff.DifferenceManager;
import org.lightning.quark.core.dispatch.ActionMessageDispatcher;
import org.lightning.quark.core.feature.RowFeature;
import org.lightning.quark.core.model.column.TableColumnMapping;
import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.message.ActionMessage;
import org.lightning.quark.core.row.StepRowCopyRequest;
import org.lightning.quark.db.copy.DataRowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cook on 2018/3/21
 */
public class DataRowFullCopier {

    private static final Logger logger = LoggerFactory.getLogger(DataRowFullCopier.class);

    private DataRowManager leftManager;
    private DataRowManager rightManager;
    private DifferenceManager differenceManager;
    private TableColumnMapping tableColumnMapping;

    private ActionMessageDispatcher dispatcher;
    private DataRowCopier dataRowCopier;

    private int pageSize = 200;


    public DataRowFullCopier(DataRowManager leftManager, DataRowManager rightManager,
                             DifferenceManager differenceManager, TableColumnMapping tableColumnMapping,
                             ActionMessageDispatcher dispatcher) {
        this.leftManager = leftManager;
        this.rightManager = rightManager;
        this.differenceManager = differenceManager;
        this.tableColumnMapping = tableColumnMapping;
        this.dispatcher = dispatcher;
        dataRowCopier = new DataRowCopier(leftManager, rightManager, differenceManager, tableColumnMapping);
    }

    /**
     * 拷贝全表
     * @return
     */
    public CopyResult copyFullTable() {
        PKData startPk = leftManager.fetchMinPk(null);
        logger.info("table#{} startPk is {}", leftManager.getTable().getName(), startPk);
        do {
            if (startPk == null) {
                break;
            }

            StepRowCopyRequest request = new StepRowCopyRequest();
            request.setStartPk(startPk);
            request.setPageSize(pageSize);
            ActionMessage<StepRowCopyRequest> message = new ActionMessage<>();
            message.setFeature(RowFeature.V1.STEP_ROW_COPY);
            message.setResource(request);
            dispatcher.dispatch(message);

            // plus one
            PKData nextStartPk = leftManager.fetchMaxPk(startPk, pageSize+1);

            if (nextStartPk == null || nextStartPk.equals(startPk)) {
                logger.info("table#{} copy dispatch done, end pk is {}", leftManager.getTable().getName(), nextStartPk);
                break;
            } else {
                startPk = nextStartPk;
                logger.info("table#{} copy, next pk is {}", leftManager.getTable().getName(), nextStartPk);
            }

        } while (true);

        return null;
    }

}

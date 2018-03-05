package org.lightning.quark.chase.copy;

import org.apache.commons.collections4.CollectionUtils;
import org.lightning.quark.core.diff.DifferenceManager;
import org.lightning.quark.core.diff.DifferenceType;
import org.lightning.quark.core.diff.RowDifference;
import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.db.RowDataInfo;
import org.lightning.quark.core.row.TableColumnMapping;
import org.lightning.quark.db.copy.DataRowManager;

import java.util.List;
import java.util.Map;

/**
 * 数据行 之间 拷贝
 * Created by cook on 2018/2/26
 */
public class DataRowCopier {

    private DataRowManager leftManager;
    private DataRowManager rightManager;
    private DifferenceManager differenceManager;
    private TableColumnMapping tableColumnMapping;

    public DataRowCopier(DataRowManager leftManager, DataRowManager rightManager,
                         DifferenceManager differenceManager, TableColumnMapping tableColumnMapping) {
        this.leftManager = leftManager;
        this.rightManager = rightManager;
        this.differenceManager = differenceManager;
        this.tableColumnMapping = tableColumnMapping;
    }

    public CopyResult copyByStep(PKData startPk, int pageSize) {
        // 1. 获取源 - 步长内数据
        List<Map<String, Object>> leftRows = leftManager.fetchRowsByStep(startPk, pageSize);
        if (CollectionUtils.isEmpty(leftRows)) {
            return null;
        }

        // 2. 获取目标 - [startPk, endPk] 区间的数据
        PKData endPk = leftManager.getLastRowPk(leftRows);
        List<Map<String, Object>> rightRows = rightManager.fetchRowsByRangeClosed(startPk, endPk);

        // 3. 比对 两批 步长数据
        Map<PKData, RowDataInfo> lefts = leftManager.convertRow(leftRows);
        Map<PKData, RowDataInfo> rights = rightManager.convertRow(rightRows);
        Map<DifferenceType, List<RowDifference>> diffMap = differenceManager.calcBatchRowDiffs(lefts, rights);

        // 4. 执行 insert/update/delete
        CopyResult copyResult = new CopyResult();
        List<RowDifference> inserts = diffMap.get(DifferenceType.ONLY_IN_LEFT);
        int insertNum = rightManager.insertRows(inserts);
        copyResult.add(DifferenceType.ONLY_IN_LEFT, insertNum);

        List<RowDifference> updates = diffMap.get(DifferenceType.NOT_EQUALS);
        int updateNum = rightManager.updateRows(updates);
        copyResult.add(DifferenceType.NOT_EQUALS, updateNum);

        List<RowDifference> deletes = diffMap.get(DifferenceType.ONLY_IN_RIGHT);
        int deleteNum = rightManager.deleteRows(deletes);
        copyResult.add(DifferenceType.ONLY_IN_RIGHT, deleteNum);

        return copyResult;
    }

}

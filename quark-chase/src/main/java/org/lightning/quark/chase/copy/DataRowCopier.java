package org.lightning.quark.chase.copy;

import org.apache.commons.collections4.CollectionUtils;
import org.lightning.quark.core.diff.DifferenceManager;
import org.lightning.quark.core.diff.DifferenceType;
import org.lightning.quark.core.diff.RowDifference;
import org.lightning.quark.core.model.db.CopyResult;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.db.RowDataInfo;
import org.lightning.quark.core.row.RowChange;
import org.lightning.quark.core.model.column.TableColumnMapping;
import org.lightning.quark.db.copy.DataRowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据行 之间 拷贝
 * Created by cook on 2018/2/26
 */
public class DataRowCopier {

    private static final Logger logger = LoggerFactory.getLogger(DataRowCopier.class);

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
        PKData endPk = leftManager.getTable().getLastRowPk(leftRows);
        List<Map<String, Object>> rightRows = rightManager.fetchRowsByRangeClosed(startPk, endPk);


        return compareAndCopy(leftRows, rightRows);
    }

    public CopyResult compareAndCopy(List<Map<String, Object>> leftRows, List<Map<String, Object>> rightRows) {
        // 3. 比对 两批 步长数据
        Map<PKData, RowDataInfo> lefts = leftManager.getTable().convertRow(leftRows);
        Map<PKData, RowDataInfo> rights = rightManager.getTable().convertRow(rightRows);
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

        logger.info("table#{}, copy result is insert {} rows, update {} rows, delete {} rows",
                leftManager.getTable().getName(), CollectionUtils.size(inserts), CollectionUtils.size(updates),
                CollectionUtils.size(deletes));

        return copyResult;
    }

    /**
     * 同步 变更(s) 至 目标表
     * @param changes
     * @return
     */
    public CopyResult copyChanges(List<RowChange> changes) {
        List<PKData> leftPks = changes.stream()
                .filter(Objects::nonNull)
                .map(x -> x.getCurrentRow().getPk())
                .collect(Collectors.toList());

        // 1. 获取源 - in (pks) 的数据
        List<Map<String, Object>> leftRows = leftManager.fetchRowsByPks(leftPks);

        // 2. 获取目标 - in (pks) 的数据
        List<Map<String, Object>> rightRows = rightManager.fetchRowsByPks(toRight(leftPks));

        return compareAndCopy(leftRows, rightRows);
    }

    private List<PKData> toRight(List<PKData> leftPks) {
        return leftPks.stream().map(left -> {
            PKData da = new PKData();
            left.getPkValues().forEach(val -> {
                da.addOnePk(tableColumnMapping.getRightColumnName(val.getKey()), val.getValue());
            });
            return da;
        }).collect(Collectors.toList());
    }

}

package org.lightning.quark.core.row;

import lombok.Getter;
import lombok.Setter;
import org.lightning.quark.core.model.db.PKData;

import java.util.Date;
import java.util.Objects;

/**
 * SQL Server - model for Change Capture Data
 * Created by cook on 2018/2/27
 */
@Getter
@Setter
public class CdcInfo {

    /** 更改提交的LSN。在同一事务中提交的更改将共享同一个提交 LSN 值  */
    private byte[] startLsn;

    /**  一个事务内可能有多个更改发生，这个值用于对它们进行排序 */
    private byte[] seqval;

    /**
     * 一次cdc的主键(s)
     */
    private PKData pk;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CdcInfo cdcInfo = (CdcInfo) o;
        return Objects.equals(pk, cdcInfo.pk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk);
    }

}

package org.lightning.quark.core.common.paging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by cook on 2018/4/14
 */
@Getter
@Setter
public class Pagination {

    /**
     *
     */
    private int totalCount;

    /**
     *
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int totalPageNo;

    /**
     * 当前页码 - 1-based
     */
    private int currentPageNo;

    /**
     *
     */
    private int pageSize;

    /**
     * 排序
     */
    private SortEnum sort;

    /**
     * 排序列名
     */
    private String sortColumnName;

    /**
     *
     * @return
     */
    public int getTotalPageNo() {
        if (totalPageNo > 0) {
            return totalPageNo;
        }
        int tot = totalCount / pageSize;
        if (totalCount % pageSize != 0) {
            tot++;
        }
        return tot;
    }



}

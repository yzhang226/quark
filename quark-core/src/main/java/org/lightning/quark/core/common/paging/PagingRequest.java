package org.lightning.quark.core.common.paging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by cook on 2018/4/23
 */
@Getter
@Setter
@NoArgsConstructor
public class PagingRequest<T> {

    /**
     * 1-based
     */
    @Setter(AccessLevel.NONE)
    private int pageNo = 1;

    /**
     *
     */
    @Setter(AccessLevel.NONE)
    private int pageSize = 20;

    /**
     * 查询条件
     */
    private T criteria;

    public PagingRequest(int pageNo, int pageSize) {
        setPageNo(pageNo);
        setPageSize(pageSize);
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo <= 0 ? 1 : pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? 20 : pageSize;
    }

}
package org.lightning.quark.core.common.paging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by cook on 2018/4/23
 */
@Getter
@Setter
@NoArgsConstructor
public class PageRequest<T> {

    private int pageNum = 1;
    private int pageSize = 20;
    private T paramData;

    public PageRequest(int pageNum, int pageSize) {
        int pageNumTemp = pageNum <= 0 ? 1 : pageNum;
        int pageSizeTemp = pageSize <= 0 ? 1 : pageSize;
        this.pageNum = pageNumTemp;
        this.pageSize = pageSizeTemp;
    }

    public void setPageNum(int pageNum) {
        int pageNumTemp = pageNum <= 0 ? 1 : pageNum;
        this.pageNum = pageNumTemp;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        int pageSizeTemp = pageSize <= 0 ? 1 : pageSize;
        this.pageSize = pageSizeTemp;
    }

}
package org.lightning.quark.core.common.paging;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by cook on 2018/4/14
 */
@Getter
@Setter
public class PagingResponse<T> {

    private Pagination pagination;

    private List<T> data;

}

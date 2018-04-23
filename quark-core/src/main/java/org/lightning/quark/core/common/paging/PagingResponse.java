package org.lightning.quark.core.common.paging;

import lombok.Getter;
import lombok.Setter;
import org.lightning.quark.core.utils.Q;

import java.util.List;

/**
 * Created by cook on 2018/4/14
 */
@Getter
@Setter
public class PagingResponse<T> {

    private Pagination pagination;

    private List<T> data;


    public <M> PagingResponse<M> convert(Class<M> modelClass) {
        PagingResponse<M> o = new PagingResponse<>();
        o.setData(Q.copyList(data, modelClass));
        o.setPagination(this.getPagination());
        return o;
    }

}

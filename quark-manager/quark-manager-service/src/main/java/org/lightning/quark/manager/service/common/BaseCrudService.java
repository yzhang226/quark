package org.lightning.quark.manager.service.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.lightning.quark.core.common.paging.Pagination;
import org.lightning.quark.core.common.paging.PagingRequest;
import org.lightning.quark.core.common.paging.PagingResponse;
import org.lightning.quark.core.utils.ClassUtil;
import org.lightning.quark.core.utils.Q;
import org.lightning.quark.manager.dao.common.CrudDao;

import java.util.List;

/**
 * Created by cook on 2018/4/23
 */
public abstract class BaseCrudService<PK, E> {

    protected Class<E> entityClass;

    public BaseCrudService() {
        entityClass = ClassUtil.getSuperClassGenericType(getClass(), 1);
    }

    /***
     * 通过<code>主键</code>获取数据Bean
     * @param id
     * @return
     */
    public E getBeanById(PK id) {
        return getCrudDao().getById(id);
    }

    /**
     * 保存数据Bean
     *
     * @param bean
     * @return 返回影响数据表的行数
     */
    public int saveBean(E bean) {
        return getCrudDao().save(bean);
    }

    /**
     * 更新数据Bean
     *
     * @param bean
     * @return 返回影响数据表的行数
     */
    public int updateBean(E bean) {
        return getCrudDao().update(bean);
    }

    /**
     * 根据<code>criteria</code>来查询Beans
     * 有可能查询出很多，所以做了限制，最多查询出10w行数据
     *
     * @param criteria
     * @return
     */
    public List<E> findBeans(E criteria) {
        PagingRequest<E> req = new PagingRequest<>();
        req.setPageNo(1);
        req.setPageSize(maxRowSize());
        req.setCriteria(criteria);
        List<E> result = findBeans(req, false).getData();
        return result;
    }

    protected int maxRowSize() {
        return 100000;
    }

    /**
     * 根据条件, 仅查询出一个Bean
     *
     * @param criteria
     * @return
     */
    public E findOneBean(E criteria) {
        PagingRequest<E> req = new PagingRequest<>();
        req.setPageNo(1);
        req.setPageSize(1);
        req.setCriteria(criteria);
        List<E> one = findBeans(req, false).getData();
        return CollectionUtils.isNotEmpty(one) ? one.get(0) : null;
    }

    /**
     * 带count的分页查询
     * @param req
     * @return
     */
    public PagingResponse<E> findBeans(PagingRequest req) {
        return findBeans(req, true);
    }

    /**
     * 分页查询
     *
     * @param req
     * @return
     */
    public PagingResponse<E> findBeans(PagingRequest req, boolean countEnable) {
        E criteria = Q.copy(req.getCriteria(), entityClass);

        PageHelper.startPage(req.getPageNo(), req.getPageSize(), countEnable);
        List<E> list = getCrudDao().query(criteria);
        PageInfo<E> info = new PageInfo<>(list);

        PagingResponse<E> resp = new PagingResponse<>();

        Pagination pagination = new Pagination();
        pagination.setCurrentPageNo(info.getPageNum());
        pagination.setPageSize(info.getPageSize());
        pagination.setTotalCount((int) info.getTotal());

        resp.setPagination(pagination);
        resp.setData(info.getList());

        return resp;
    }

    /**
     * 该service对应的dao
     *
     * @return
     */
    protected abstract CrudDao<PK, E> getCrudDao();

}
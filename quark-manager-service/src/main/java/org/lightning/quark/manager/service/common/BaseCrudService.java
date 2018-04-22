package org.lightning.quark.manager.service.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.lightning.quark.core.utils.ClassUtil;
import org.lightning.quark.manager.dao.common.CrudDao;
import org.lightning.quark.core.common.paging.PageRequest;
import org.lightning.quark.core.common.paging.PagingResponse;

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
     *
     * @param criteria
     * @return
     */
    public List<E> findBeans(E criteria) {
        return getCrudDao().query(criteria, RowBounds.DEFAULT);
    }

    /**
     * 根据<code>criteria</code>和<code>offset, limit</code>来查询Beans
     *
     * @param criteria
     * @return
     */
    public List<E> findBeans(E criteria, int offset, int limit) {
        return getCrudDao().query(criteria, new RowBounds(offset, limit));
    }

    /**
     * 根据条件, 仅查询出一个Bean
     *
     * @param criteria
     * @return
     */
    public E findOneBean(E criteria) {
        List<E> beans = findBeans(criteria, 0, 1);
        return CollectionUtils.isEmpty(beans) ? null : beans.get(0);
    }

    /**
     * 分页查询
     *
     * @param req
     * @return
     */
    public PagingResponse<E> findBeans(PageRequest req) {
//        E criteria = BeanUtil.copy(req.getParamData(), entityClass);
//        int offset = req.getPageNum() > 0 ? (req.getPageNum() - 1) * req.getPageSize() : 0;
//        Page<E> page = getCrudDao().query(criteria, new RowBounds(offset, req.getPageSize()));
//
//        PageResponse<E> response = new PageResponse<>();
//        response.getPagination().setTotalCount(page.getTotal());
//        response.getPagination().setCurrentPageIndex(req.getPageNum());
//        response.getPagination().setPageSize(req.getPageSize());
//        response.setResultData(page.getResult());
//        return response;
        return null;
    }

    /**
     * 该service对应的dao
     *
     * @return
     */
    protected abstract CrudDao<PK, E> getCrudDao();

}
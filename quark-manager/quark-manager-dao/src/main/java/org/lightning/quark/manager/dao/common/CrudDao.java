package org.lightning.quark.manager.dao.common;

import com.github.pagehelper.Page;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by cook on 2018/4/23
 */
public interface CrudDao<PK, E> {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    E getById(PK id);

    /**
     *
     * @param entity
     * @return
     */
    int save(E entity);

    /**
     *
     * @param entity
     * @return
     */
    int update(E entity);

    /**
     * 分页查询
     * @param criteria 实体(Bean)即为查询的条件
     * @param offsetLimit 分页限制
     * @return
     */
    Page<E> queryForPaging(E criteria, RowBounds offsetLimit);

    /**
     * 分页查询
     * @param criteria 实体(Bean)即为查询的条件
     * @return
     */
    Page<E> query(E criteria);

    /**
     * 批量保存
     * @param entities
     * @return
     */
    int batchSave(List<E> entities);

    /**
     *
     * @param criteria
     * @return
     */
    long count(E criteria);

//    int delete(String id);
//    int deleteBatch(List<User> users);

}

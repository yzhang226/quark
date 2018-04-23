package org.lightning.quark.manager.dao.daos.quark;

import org.apache.ibatis.annotations.Mapper;
import org.lightning.quark.manager.dao.common.CrudDao;
import org.lightning.quark.manager.support.model.po.quark.DataSourceBean;

/**
* dao for DataSource <br>
* Created by cook on 2018-04-23.
*/
@Mapper
public interface DataSourceDao extends CrudDao<Integer, DataSourceBean> {



}

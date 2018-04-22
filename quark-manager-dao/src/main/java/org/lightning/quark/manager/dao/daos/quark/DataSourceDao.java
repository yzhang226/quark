package org.lightning.quark.manager.dao.daos.quark;

import org.lightning.quark.manager.dao.common.CrudDao;
import org.lightning.quark.manager.support.model.po.quark.DataSourceBean;
import org.lightning.quark.manager.dao.common.DaoComponent;
import org.springframework.stereotype.Component;

/**
* dao for DataSource <br>
* Created by cook on 2018-04-23.
*/
@DaoComponent
@Component
public interface DataSourceDao extends CrudDao<Integer, DataSourceBean> {


}

package org.lightning.quark.manager.service.services.quark;

import org.lightning.quark.manager.service.quark.IDataSourceService;
import org.lightning.quark.manager.dao.daos.quark.DataSourceDao;
import org.lightning.quark.manager.model.dto.quark.DataSourceRequest;
import org.lightning.quark.manager.model.dto.quark.DataSourceResponse;
import org.lightning.quark.manager.support.model.po.quark.DataSourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.lightning.quark.manager.service.common.BaseCrudService;

/**
 * service for DataSource <br>
 * Created by cook on 2018-04-23.
 */
@Service
public class DataSourceService extends BaseCrudService<Integer, DataSourceBean>
        implements IDataSourceService {

    @Autowired
    private DataSourceDao dataSourceDao;

    /**
     * 保存<code>request</code>
     * @param request
     * @return 新增的数据的行主键(如果主键是自增)
     */
    public Integer save(DataSourceBean bean) {
        saveBean(bean);
        bean.setId(bean.getId());
        return bean.getId();
    }

    protected DataSourceDao getCrudDao() {
        return dataSourceDao;
    }

}

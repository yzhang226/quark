package org.lightning.quark.manager.api.controller;

import io.swagger.annotations.ApiOperation;
import org.lightning.quark.core.common.DataResult;
import org.lightning.quark.core.common.paging.PagingRequest;
import org.lightning.quark.core.common.paging.PagingResponse;
import org.lightning.quark.core.utils.Q;
import org.lightning.quark.manager.model.dto.quark.DataSourceModel;
import org.lightning.quark.manager.service.services.quark.DataSourceService;
import org.lightning.quark.manager.support.model.po.quark.DataSourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by cook on 2018/4/23
 */
@Validated
@RestController
@RequestMapping(value="/v1/data_source")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;


    /**
     * 可以设置基础约束
     * @param id
     * @return
     */
    @ApiOperation(value = "GetDataSourceById", httpMethod = "GET", response = DataResult.class, notes = "通过id获取DataSource")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DataResult<DataSourceModel> getById(@NotNull @Min(1) @PathVariable Integer id) {
        DataSourceBean bean = dataSourceService.getBeanById(id);
        DataSourceModel model = Q.copy(bean, DataSourceModel.class);
        return DataResult.ok(model);
    }

    /**
     * 新增
     * @param dataSource
     * @return 新增的数据的行主键(如果自增主键)
     */
    @ApiOperation(value = "AddDataSource", httpMethod = "POST", response = DataResult.class, notes = "新增DataSource")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public DataResult<Integer> add(@RequestBody DataSourceModel dataSource) {
        return DataResult.ok(dataSourceService.save(Q.copy(dataSource, DataSourceBean.class)));
    }

    /**
     * 更新
     * @param dataSource
     * @return 影响数据表的行数
     */
    @ApiOperation(value = "UpdateDataSource", httpMethod = "POST", response = DataResult.class, notes = "更新DataSource")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public DataResult<Integer> update(@RequestBody DataSourceModel dataSource) {
        return DataResult.ok(dataSourceService.updateBean(Q.copy(dataSource, DataSourceBean.class)));
    }

    /**
     * 按条件, 分页查询
     * @param page
     * @return 分页结果集
     */
    @ApiOperation(value = "FindDataSourceByPage", httpMethod = "POST", response = DataResult.class, notes = "分页查询")
    @RequestMapping(value = "/page", method = {RequestMethod.POST})
    public DataResult<PagingResponse<DataSourceModel>> findByPage(@RequestBody PagingRequest<DataSourceModel> page) {
        PagingResponse<DataSourceBean> resp = dataSourceService.findBeans(page);
        return DataResult.ok(resp.convert(DataSourceModel.class));
    }

}

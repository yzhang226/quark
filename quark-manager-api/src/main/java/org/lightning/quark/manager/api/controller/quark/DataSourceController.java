package org.lightning.quark.manager.api.controller.quark;

import org.lightning.quark.manager.service.services.quark.DataSourceService;
import org.lightning.quark.manager.model.dto.quark.DataSourceRequest;
import org.lightning.quark.manager.model.dto.quark.DataSourceResponse;
import org.lightning.quark.manager.model.common.PageRequest;
import org.lightning.quark.manager.model.common.paging.PagingResponse;
import org.lightning.quark.manager.model.common.DataResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * controller for DataSource <br>
 * Created by cook on 2018-04-23.
 */
@Validated
@RestController
@RequestMapping(value="/v1/k_data_source")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * 可以设置基础约束
     * @param id
     * @return
     */
    @ApiOperation(value = "GetDataSourceById", httpMethod = "GET", response = DataResult.class, notes = "通过id获取DataSource")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public DataResult<DataSourceResponse> getById(@NotNull @Min(1) @PathVariable Integer id) {
        return DataResult.ok(dataSourceService.getById(id));
    }

    /**
     * 新增
     * @param dataSource
     * @return 新增的数据的行主键(如果自增主键)
     */
    @ApiOperation(value = "AddDataSource", httpMethod = "POST", response = DataResult.class, notes = "新增DataSource")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public DataResult<Integer> add(@RequestBody DataSourceRequest dataSource) {
        return DataResult.ok(dataSourceService.save(dataSource));
    }

    /**
     * 更新
     * @param dataSource
     * @return 影响数据表的行数
     */
    @ApiOperation(value = "UpdateDataSource", httpMethod = "POST", response = DataResult.class, notes = "更新DataSource")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public DataResult<Integer> update(@RequestBody DataSourceRequest dataSource) {
        return DataResult.ok(dataSourceService.update(dataSource));
    }

    /**
     * 按条件, 分页查询
     * @param page
     * @return 分页结果集
     */
    @ApiOperation(value = "FindDataSourceByPage", httpMethod = "POST", response = DataResult.class, notes = "分页查询")
    @RequestMapping(value = "/find_by_page", method = RequestMethod.POST)
    public DataResult<PageResponse<DataSourceResponse>> findByPage(@RequestBody PageRequest<DataSourceRequest> page) {
        return DataResult.ok(dataSourceService.find(page));
    }


}
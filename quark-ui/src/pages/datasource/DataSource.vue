<template>


  <k-content title="数据源" pageName="数据源">

    <div class="row">
      <div class="col-md-12 col-sm-6 col-xs-12">

        <k-form title="数据源">
          <div class="row">
            <k-input v-model="queryModel.userName" type="text" placeholder="用户名" name="userName" label="用户名:" labelSm="3"></k-input>
            <k-select v-model="queryModel.dataSourceType" name="dataSourceType" label="数据源类型"
                      :selectData="dataSourceTypes" labelSm="3"></k-select>

          </div>

          <div class="box-footer">
            <span>name: {{queryModel.userName}}</span>
            <span>type: {{queryModel.dataSourceType}}</span>
            <button type="button" class="btn btn-primary" v-on:click="openAddPage">新增</button>
            <button type="button" class="btn btn-primary" v-on:click="doQuery">查询</button>
          </div>
        </k-form>
      </div>
      <div class="col-md-12 col-sm-6 col-xs-12">
        <k-ajax-table title="数据源列表" :remoteUrl="remoteUrl" :header="header" :queryJson="queryJson">
        </k-ajax-table>
      </div>
      <!-- /.col -->
    </div>

    <!-- TODO: 应该有更优雅的实现 -->
    <k-model v-model="addPageVisible" modalSize="large" title="新增数据源" confirm-text="保存">
      <div class="row">
        <k-input v-model="addModel.userName" type="text" placeholder="用户名" name="userName" label="用户名:" labelSm="3"></k-input>
        <k-select v-model="addModel.dataSourceType" name="dataSourceType" label="数据源类型"
                  :selectData="dataSourceTypes" labelSm="3"></k-select>
      </div>

    </k-model>

  </k-content>

</template>

<script>
  export default {
    name: "DataSource",

    data: function() {
      return {
        addPageVisible: false,
        addModel: {
          userName: null,
          dataSourceType: 10
        },
        queryModel: {
          userName: null,
          dataSourceType: 10
        },
        dataSourceTypes: [
          {value: 10, text: 'MySQL'},
          {value: 20, text: 'SQLServer'}
          // , selected: true
        ],
        header: {
          columns: [{name: 'dataSourceType', displayName: "类型"}, {name: "userName", displayName: "用户名"}, {name: "createTime", displayName: "创建日期"},
            {name:"操作", isOperate: true, html: " <a type='button' class='btn btn-block btn-primary btn-sm' onclick='alert(new Date());'>编辑</a>"}]
        },
        remoteUrl: '/api/v1/data_source/page'
      }
    },
    watch: {

    },
    computed: {
      queryJson: function () {
        return {
          userName: this.queryModel.userName,
          dataSourceType: this.queryModel.dataSourceType
        }
      }
    },
    methods: {
      openAddPage: function() {
        this.addPageVisible = true;
      },
      getQueryUrl: function() {
        let u = this.remoteUrl;
        return u + "?_=" + new Date().getTime() + "&userName=" + this.queryModel.userName;
      },
      doQuery: function () {
        this.remoteUrl = this.getQueryUrl();
      }
    }
  }
</script>

<style scoped>

</style>

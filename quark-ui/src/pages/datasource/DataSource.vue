<template>


  <k-content title="数据源" pageName="数据源">

    <div class="row">
      <div class="col-md-12 col-sm-6 col-xs-12">

        <k-panel title="数据源">
          <k-form title="">
            <div class="row">
              <k-input v-model="queryModel.userName" type="text" placeholder="用户名" name="userName" label="用户名:"
                       labelSm="3"></k-input>
              <k-select v-model="queryModel.dataSourceType" name="dataSourceType" label="数据源类型"
                        :selectData="dataSourceTypes" labelSm="3"></k-select>

            </div>
            <div class="row">
              <k-checkbox v-model="myBoxs" name="test" value="test1" text="测试1" ></k-checkbox>
              <k-checkbox v-model="myBoxs2" name="test" value="test2" text="测试2" ></k-checkbox>
            </div>
          </k-form>
          <div class="row" slot="footer">
            <span>checkedIds: {{checkedIds}}</span>
            <span>myBoxs: {{myBoxs}} {{myBoxs2}}</span>
            <span>name: {{queryModel.userName}}</span>
            <span>type: {{queryModel.dataSourceType}}</span>
            <button type="button" class="btn btn-primary" @click="openAddPage">新增</button>
            <button type="button" class="btn btn-primary" @click="doQuery">查询</button>
          </div>
        </k-panel>
      </div>
      <div class="col-md-12 col-sm-6 col-xs-12">
        <k-ajax-table title="数据源列表" :remoteUrl="remoteUrl" :header="header" :queryJson="queryJson" ref="aTable"
                    :checkedValues.sync="checkedIds">
        </k-ajax-table>
      </div>
      <!-- /.col -->
    </div>


    <k-model v-model="addPageVisible" modalSize="large" title="新增数据源" confirm-text="保存"
             :confirmCallback="doSaveDataSource">
      <k-form id="addForm" ref="addFormRef">

        <div class="row">
          <k-select v-model="addModel.dataSourceType" id="dataSourceType1" label="数据源类型"
                    :selectData="dataSourceTypes" labelSm="3"></k-select>
          <k-input v-model="addModel.driverClass" type="text" placeholder="驱动类: com.mysql.jdbc.Driver" id="driverClass1"
                   label="驱动类:" labelSm="3"></k-input>
        </div>

        <div class="row">
          <k-input v-model="addModel.userName" type="text" placeholder="用户名" id="userName1" label="用户名:"
                   labelSm="3"></k-input>
          <k-input v-model="addModel.password" type="password" placeholder="密码" id="password1" label="密码:"
                   labelSm="3"></k-input>
        </div>

        <div class="row">
          <k-textarea v-model="addModel.connectString" type="text" placeholder="连接URL, JSON格式" id="connectString1"
                      label="连接URL:" labelSm="3"></k-textarea>
          <k-textarea v-model="addModel.poolProperties" type="text" placeholder="连接池配置, JSON格式" id="poolProperties1"
                      label="连接池配置:" labelSm="3"></k-textarea>
        </div>
      </k-form>
    </k-model>

    <k-model v-model="editPageVisible" modalSize="large" title="新增数据源" confirm-text="保存"
             :confirmCallback="doEditDataSource">
      <k-form ref="editFormRef">

        <div class="row">
          <k-select v-model="editModel.dataSourceType"  label="数据源类型"
                    :selectData="dataSourceTypes" labelSm="3"></k-select>
          <k-input v-model="editModel.driverClass" type="text" placeholder="驱动类: com.mysql.jdbc.Driver"
                   label="驱动类:" labelSm="3"></k-input>
        </div>

        <div class="row">
          <k-input v-model="editModel.userName" type="text" placeholder="用户名" label="用户名:"
                   labelSm="3"></k-input>
          <k-input v-model="editModel.password" type="password" placeholder="密码" label="密码:"
                   labelSm="3"></k-input>
        </div>

        <div class="row">
          <k-textarea v-model="editModel.connectString" type="text" placeholder="连接URL, JSON格式"
                      label="连接URL:" labelSm="3"></k-textarea>
          <k-textarea v-model="editModel.poolProperties" type="text" placeholder="连接池配置, JSON格式"
                      label="连接池配置:" labelSm="3"></k-textarea>
        </div>
      </k-form>
    </k-model>

  </k-content>

</template>

<script>
  export default {
    name: "DataSource",

    data: function () {
      return {
        myBoxs: null,
        myBoxs2: null,
        editPageVisible: false,
        addPageVisible: false,
        editModel: {
          userName: null,
          dataSourceType: 10,
          connectString: null,
          poolProperties: null,
          driverClass: null
        },
        addModel: {
          userName: null,
          dataSourceType: 10,
          connectString: null,
          poolProperties: null,
          driverClass: null
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
          columns: [
            {name: 'id', operateType: 'checkbox', checkedName: 'id'},
            {name: 'id', isHidden: true, isPrimaryKey: true},
            {name: 'dataSourceType', displayName: "类型"},
            {name: "driverClass", displayName: "驱动"},
            {name: "userName", displayName: "用户名"},

            {name: "createTime", displayName: "创建日期"},

            // {
            //   name: "操作",
            //   operateType: 'html',
            //
            //   html: "<a type='button' class='btn btn-block btn-primary btn-sm' onclick='openEditPage();'>编辑</a>"
            // }
            ]
        },
        checkedIds: [],
        remoteUrl: '/api/v1/data_source/page'
      }
    },
    watch: {},
    computed: {
      queryJson: function () {
        return {
          userName: this.queryModel.userName,
          dataSourceType: this.queryModel.dataSourceType
        }
      }
    },
    methods: {
      //
      getCheckedValues: function () {
        return this.$refs.aTable.getCheckedValues();
      },
      doEditDataSource: function () {

      },
      doSaveDataSource: function () {
        let that = this;

        let url = "/api/v1/data_source";
        let data = this.addModel;
        axios.put(url, data)
          .then(function (response) {
            that.$refs.addFormRef.reset();
            that.$refs.addFormRef.close();
            Swal('Good job!', '保存成功!', 'success');
          })
          .catch(function (error) {
            console.log("error is ", error);
            Swal('Oops...', 'Something went wrong!', 'error');
          });
      },
      openAddPage: function () {
        this.addPageVisible = true;
      },
      openEditPage: function () {
        let that = this;

        let url = "/api/v1/data_source/";
        let data = this.addModel;
        axios.put(url, data)
          .then(function (response) {
            that.$refs.addFormRef.reset();
            that.$refs.addFormRef.close();
            Swal('Good job!', '保存成功!', 'success');
          })
          .catch(function (error) {
            console.log("error is ", error);
            Swal('Oops...', 'Something went wrong!', 'error');
          });

        this.editPageVisible = true;
      },
      getQueryUrl: function () {
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

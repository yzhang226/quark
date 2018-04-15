<template>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        {{msg}}
        <small>{{msg}}</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
        <li class="active">{{msg}}</li>
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <!-- Info boxes -->
      <div class="row">
        <div class="col-md-12 col-sm-6 col-xs-12">

          <Form :formTitle="testFormTitle">
            <div class="row">
              <VInput blockClass="col-md-12" type="email" placeholder="Email" name="test" eleClass="form-control" id="test-id" label="My Input"></VInput>
              <VInput blockClass="col-md-12" type="email" placeholder="Email" name="test" eleClass="form-control" id="test-id" label="My Input"></VInput>
            </div>

            <div class="row">
              <VInput type="email" placeholder="Email" name="test" eleClass="form-control" id="test-id" label="My Input"></VInput>
              <VInput type="email" placeholder="Email" name="test" eleClass="form-control" id="test-id" label="My Input"></VInput>
            </div>

            <div class="row">
              <VInput blockClass="col-md-3" type="email" placeholder="1" name="test" eleClass="form-control" id="test-id" label="1"></VInput>
              <VInput blockClass="col-md-3" type="email" placeholder="2" name="test" eleClass="form-control" id="test-id" label="2"></VInput>
              <VInput blockClass="col-md-3" type="email" placeholder="3" name="test" eleClass="form-control" id="test-id" label="3"></VInput>
              <VInput blockClass="col-md-3" type="email" placeholder="4" name="test" eleClass="form-control" id="test-id" label="4"></VInput>
            </div>

            <div class="row">
              <VInput blockClass="col-md-12" v-model="queryModel.userName" type="text" placeholder="User Name" name="userName" eleClass="form-control" id="userName" label="User Name"></VInput>
              <span>{{queryModel.userName}}</span>
            </div>

            <div class="box-footer">
              <button type="button" class="btn btn-primary" v-on:click="doQuery">查询</button>
            </div>
          </Form>
        </div>
        <div class="col-md-12 col-sm-6 col-xs-12">
          <AjaxPagingTable :remoteUrl="remoteUrl" :header="header" :tableTitle="tableTitle" ref="dTable" :queryString="getQueryString">
          </AjaxPagingTable>
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

</template>

<script>

  import DataTable from './table/DataTable'
  import AjaxPagingTable from './table/AjaxPagingTable'
  import Form from './form/Form'
  import VInput from './input/Input'
  // rowData: [[{value: 'test1'}], [{value: 'test2'}]],
  export default {
    name: 'HelloWorld',
    components: {DataTable, AjaxPagingTable, Form, VInput},

    data: function() {
      return {
        msg: 'Hello',
        testFormTitle: "Test Form",
        queryModel: {
          userName: null
        },
        header: {
          columns: [{name: 'title', displayName: "标题"}, {name: "author_name", displayName: "作者"}, {name: "date", displayName: "日期"}]
        },
        tableTitle: "test-title",
        _remoteUri: '/news/list'
      }
    },
    watch: {
      'queryModel.userName': function (val) {
        debugger;
        // console.log("val is ", val, ", no is ", this.queryModel.userName)
        // let pNo = this.queryModel.userName || 1;
        return '/news/list';
      }
    },
    computed: {
      remoteUrl: function () {
        console.log("this._remoteUri is ", this._remoteUri);
        return this._remoteUri ? this._remoteUri : '/news/list';
      },
      getQueryString: function() {
        return 'userName=' + this.queryModel.userName;
      },
    },

    methods: {

      doQuery: function () {
        console.log("doQuery...");
        this._remoteUri = '/news/list2';
        this.$refs.dTable.setRowData();
        console.log("doQuery done");
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

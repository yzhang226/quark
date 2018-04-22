<template>

  <k-content title="DEMO2" pageName="DEMO2">

    <div class="row">
      <div class="col-md-12 col-sm-6 col-xs-12">

        <k-form :formTitle="testFormTitle">
          <div class="row">
            <k-input blockClass="col-md-12" type="email" placeholder="Email" name="test" eleClass="form-control" id="test-id" label="My Input"></k-input>
            <k-input blockClass="col-md-12" type="email" placeholder="Email" name="test" eleClass="form-control" id="test-id" label="My Input"></k-input>
          </div>

          <div class="row">
            <k-input type="email" placeholder="Email" name="test" eleClass="form-control" id="test-id" label="My Input"></k-input>
            <k-input type="email" placeholder="Email" name="test" eleClass="form-control" id="test-id" label="My Input"></k-input>
          </div>

          <div class="row">
            <k-input blockClass="col-md-3" type="email" placeholder="1" name="test" eleClass="form-control" id="test-id" label="1"></k-input>
            <k-input blockClass="col-md-3" type="email" placeholder="2" name="test" eleClass="form-control" id="test-id" label="2"></k-input>
            <k-input blockClass="col-md-3" type="email" placeholder="3" name="test" eleClass="form-control" id="test-id" label="3"></k-input>
            <k-input blockClass="col-md-3" type="email" placeholder="4" name="test" eleClass="form-control" id="test-id" label="4"></k-input>
          </div>

          <div class="row">
            <k-input blockClass="col-md-12" v-model="queryModel.userName" type="text" placeholder="User Name" name="userName" eleClass="form-control" id="userName" label="User Name"></k-input>
            <span>{{queryModel.userName}}</span>
          </div>

          <div class="box-footer">
            <button type="button" class="btn btn-primary" v-on:click="doQuery">查询</button>
          </div>
        </k-form>
      </div>
      <div class="col-md-12 col-sm-6 col-xs-12">
        <k-ajax-table title="DEMO2测试数据" :remoteUrl="remoteUrl" :header="header" ref="dTable" >
        </k-ajax-table>
      </div>
      <!-- /.col -->
    </div>

    <div class="row">
      <div class="col-md-12 col-sm-6 col-xs-12">
        <k-panel title="测试面板v2">
          <div class="row">
            <div >
              <h1>body</h1>
            </div>
          </div>
        </k-panel>
      </div>

    </div>

  </k-content>

</template>

<script>

  // components: {DataTable, AjaxPagingTable, Form, VInput, Panel},

  export default {
    name: 'HelloWorld',


    data: function() {
      return {
        msg: 'Hello',
        testFormTitle: "Test Form",
        queryModel: {
          userName: null
        },
        header: {
          columns: [{name: 'title', displayName: "标题"}, {name: "author_name", displayName: "作者"}, {name: "date", displayName: "日期"},
            {name:"操作", isOperate: true, html: " <a type='button' class='btn btn-block btn-primary btn-sm' onclick='alert(new Date());'>编辑</a>"}]
        },
        tableTitle: "test-title",
        remoteUrl: '/news/list'
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
      getQueryString: function() {
        return 'userName=' + this.queryModel.userName;
      },
    },

    methods: {
      getQueryUrl: function() {
        var u = '/news/list2';
        var mills = new Date().getTime();
        return u + "?_=" + mills + "&userName=" + this.queryModel.userName;
      },
      doQuery: function () {
        // debugger;
        console.log("doQuery start, previous is ", this.$refs.dTable.remoteUrl);
        // this._remoteUri = '/news/list2';
        // this.$refs.dTable.remoteUrl = this.getQueryUrl();
        // this.$refs.dTable.fetchAndRender();
        this.remoteUrl = this.getQueryUrl();
        console.log("doQuery done. after is ", this.$refs.dTable.remoteUrl);
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

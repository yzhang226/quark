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

        </div>
        <div class="col-md-12 col-sm-6 col-xs-12">
          <AjaxDataTable :remoteUrl="remoteUrl" :header="header" :tableTitle="tableTitle">
          </AjaxDataTable>
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
  import AjaxDataTable from './table/AjaxDataTable'
  // rowData: [[{value: 'test1'}], [{value: 'test2'}]],
  export default {
    name: 'HelloWorld',
    components: {DataTable, AjaxDataTable},

    data: function() {
      return {
        msg: 'Hello',

        header: {
          columns: [{name: 'test-col'}, {name: "t2"}]
        },
        tableTitle: "test-title",
        remoteUrl: '/news/list'
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
</style>

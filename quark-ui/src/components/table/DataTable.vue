<template>
  <div class="box">
    <div class="box-header">
      <h3 class="box-title">{{tableTitle}}</h3>
    </div>
    <!-- /.box-header -->
    <div class="box-body">

      <div id="example1_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
        <div class="row">
          <div class="col-sm-12">
            <table class="table table-bordered table-striped dataTable" role="grid" aria-describedby="example1_info">
              <thead>
              <tr>
                <th v-for="h in header.columns">{{displayHeader(h)}}</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="row in rowData">
                <td v-for="r in row">{{r.value}}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>

        <slot name="paging"></slot>

      </div>


    </div>
    <!-- /.box-body -->
  </div>
</template>

<script>
  export default {
    name: "DataTable",
    props: ['rowData', 'header', 'tableTitle'],
    methods: {
      displayHeader: function (h) {
        if ('displayName' in h) {
          return h.displayName;
        }
        if ('name' in h) {
          return h.name;
        }
        return 'N/A';
      }
    }
  }
</script>

<style scoped>

</style>

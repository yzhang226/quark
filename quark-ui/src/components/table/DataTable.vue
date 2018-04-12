<template>
  <div class="box">
    <div class="box-header">
      <h3 class="box-title">{{tableTitle}}</h3>
    </div>
    <!-- /.box-header -->
    <div class="box-body">
      <table id="table01" class="table table-bordered table-striped">
        <thead>
        <tr>
          <th v-for="h in header.columns">{{h.name}}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="row in rowData">
          <td v-for="r in row">{{r.value}}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <!-- /.box-body -->
  </div>
</template>

<script>
  export default {
    name: "DataTable",
    props: ['rowData', 'header', 'tableTitle']
  }
</script>

<style scoped>

</style>

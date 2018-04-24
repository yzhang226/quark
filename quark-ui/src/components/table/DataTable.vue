<template>
  <div class="dataTables_wrapper form-inline dt-bootstrap">

    <div class="row">
      <div class="col-sm-12">
        <table class="table table-bordered table-striped dataTable" role="grid" >
          <thead>
          <tr>
            <template v-for="h in header.columns" >
              <th >{{displayHeader(h)}}</th>
            </template>
          </tr>
          </thead>
          <tbody>
          <tr v-for="row in _rowData">
            <template v-for="r in row">
              <td ><span v-html="r.value"></span></td>
            </template>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</template>

<script>
  export default {
    name: "k-table",
    props: ['rowData', 'header'],
    computed: {
      _rowData: function () {
        debugger;
        let destData = [];
        let rData = this.rowData;
        let rHeader = this.header.columns;
        for (let i in rData) {
          let row = rData[i];
          let r = [];
          debugger;
          for (let j in rHeader) {
            let cell = {};
            let rh = rHeader[j];
            if (rh.isOperate) {
              cell.value = rh.html;
            } else {
              cell.value = row[rh.name];
            }
            r.push(cell);
          }
          destData.push(r);
        }
        return destData;
      }
    },
    methods: {
      displayHeader: function (h) {
        if ('displayName' in h) {
          return h.displayName;
        }
        if ('name' in h) {
          return h.name;
        }
        return 'N/A';
      },
      isOperateCell: function (h) {
        return h.isOperate === true;
      }
    }
  }
</script>

<style scoped>

</style>

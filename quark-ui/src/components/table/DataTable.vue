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
          <tr v-for="r in rowData">
            <!--<template v-for="r in row">-->

              <template v-for="h in header.columns" >
                <td >
                  <template v-if="getOperateType(h) === 'checkbox'">
                    <k-checkbox v-model="checkedBoxs" :value="getCellValue(h, r)" text=""  ></k-checkbox>
                  </template>
                  <template v-else>
                    <span v-html="getCellValue(h, r)"></span>
                  </template>
                </td>
              </template>

            <!--</template>-->
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
    props: ['rowData', 'header', 'checkedValues'],
    data: function () {
      return {
        checkedBoxs: [],
        rawRowData: this.rowData
      }
    },
    computed: {
      _rowData: function () {
        // debugger;
        let destData = [];
        let rData = this.rowData;
        let rHeader = this.header.columns;
        for (let i in rData) {
          let row = rData[i];
          let r = [];
          // debugger;
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
    watch: {
      checkedBoxs: function (value) {
        // console.log("checkedBoxs is ", this.checkedBoxs);
        this.$emit('update:checkedValues', value)
      }
    },
    methods: {
      getCheckedValues: function () {
        return this.checkedBoxs;
      },
      getCellValue: function (header, row) {
        if (this.getOperateType(header) == 'html') {
          return header.html;
        } else {
          return row[header.name];
        }
      },
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
      },
      getOperateType: function (h) {
        if (h.hasOwnProperty('operateType') && h.operateType !== undefined) {
          return h.operateType;
        }
        return 'raw';
      }
    }
  }
</script>

<style scoped>

</style>

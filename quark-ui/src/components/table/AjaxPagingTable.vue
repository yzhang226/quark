<template>
  <div>
    <DataTable :rowData="rowDatax" :header="header" :tableTitle="tableTitle">

      <template slot="paging">
        <VPagination :totalCount="pagination.totalCount" :pageSize="pagination.pageSize"
                     :currentPageNo="pagination.currentPageNo"
                     v-on:changePageSize="changePageSize">
        </VPagination>
      </template>

    </DataTable>

  </div>
</template>

<script>
  import DataTable from './DataTable'
  import VPagination from "./Pagination"

  export default {
    name: "AjaxPagingTable",
    components: {DataTable, VPagination},
    props: ['remoteUrl', 'header', 'tableTitle', 'queryString'],
    data: function() {
      return {
        rowDatax: this.rowDatax,
        pagination: {
          totalCount: 0,
          pageSize: 20,
          currentPageNo: 0
        }
      }
    },
    mounted: function () {
      console.log('a mounted');
    },
    created: function () {
      // `this` 指向 vm 实例
      console.log('AjaxPagingTable created');

      this.setRowData();

    },
    methods: {
      changePageSize: function(selectedPageSize) {
        console.log("my changePageSize event selectedPageSize is ", selectedPageSize);
        this.pagination.currentPageNo = selectedPageSize;
        this.setRowData();
      },
      calcRemoteUrl: function() {
        let _remote = this.remoteUrl;
        if (_remote.indexOf('?') < 0) {
          _remote += "?"
        }

        if (this.queryString) {
          _remote += this.queryString;
        }

        _remote += "&currentPageNo=" + this.pagination.currentPageNo;

        return _remote;
      },
      setRowData: function () {
        let that = this;
        let url = this.calcRemoteUrl();

        console.log("paging ajax url is " + url);

        axios.get(url)
          .then(function (response) {
            let res = response.data;
            let resultData = that.extractData(res);
            let headerColumns = that.extractHeaderColumns(res);

            let datax = [];
            for (let i in resultData) {
              let d = resultData[i];
              let r = [];
              for (let j in headerColumns) {
                r.push({value: d[headerColumns[j].name]});
              }
              datax.push(r);
            }
            console.log("datax first is " + JSON.stringify(datax[0]))
            that.rowDatax = datax;
            let temp = that.extractPagination(res);
            if (that.pagination.currentPageNo > 0) {
              temp.currentPageNo = that.pagination.currentPageNo;
            }

            that.pagination = temp;
          })
          .catch(function (error) {
            console.log("error {}", error)
          });

      },
      extractData: function (result) {
        return result.data;
      },
      extractHeaderColumns: function (result) {
        return this.header.columns;
      },
      extractPagination: function (result) {
        return result.pagination;
      }
    }
  }
</script>

<style scoped>

</style>

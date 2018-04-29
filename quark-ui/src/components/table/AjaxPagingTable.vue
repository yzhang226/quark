<template>

  <k-panel :title="title" headerBorderEnable="false" footerEnable="false">

    <k-table :rowData="rowData" :header="header" ref="kTable" :checkedValues.sync="selectedValues">
    </k-table>
    <k-paging :totalCount="pagination.totalCount" :pageSize="pagination.pageSize"
              :currentPageNo="pagination.currentPageNo"
              v-on:changePageSize="changePageSize">
    </k-paging>

  </k-panel>

</template>

<script>
  // import p from '../'
  // import DataTable from './DataTable'
  // import VPagination from "./Pagination"

  export default {
    name: "k-ajax-table",
    props: {
      checkedValues: Array,
      remoteUrl: String,
      header: Object,
      title: String,
      queryJson: Object,
      actionMethod: {
        type: String,
        default: 'POST'
      }
    },
    data: function() {
      return {
        // _checkedValues: this.checkedValues,
        selectedValues: null,
        remoteAjaxUrl: this.remoteUrl,
        rowData: this.rowData,
        pagination: {
          totalCount: 0,
          pageSize: 20,
          currentPageNo: 0
        }
      }
    },
    mounted: function () {
      // console.log('AjaxPagingTable mounted');
      if (!this.remoteAjaxUrl && this.remoteUrl) {
        this.remoteAjaxUrl = this.remoteUrl;
        console.log('set this._remoteUrl is ', this.remoteAjaxUrl);
      }
      this.fetchAndRender();
    },
    beforeMount: function () {
      // console.log('AjaxPagingTable beforeMount');

    },
    created: function () {
      // console.log('AjaxPagingTable created');
    },
    updated: function () {
      // console.log('AjaxPagingTable updated');
      // this.fetchAndRender();
    },
    computed: {

    },
    watch: {
      selectedValues: function (value) {
        // console.log("this.$emit('update:checkedValues', value)", value);
        this.$emit('update:checkedValues', value);
      },
      remoteUrl: function (value) {
        // console.log('watch remote url start, this._remoteUrl is ', this._remoteUrl);
        this.remoteAjaxUrl = value;
        this.fetchAndRender();
        // console.log('watch remote url end, this._remoteUrl is ', this._remoteUrl);
        return value;
      }
    },
    methods: {
      getCheckedValues: function () {
        return this.$refs.kTable.checkedBoxs;
      },
      changePageSize: function(selectedPageSize) {
        // console.log("changePageSize event selectedPageSize is ", selectedPageSize);
        this.pagination.currentPageNo = selectedPageSize;
        this.fetchAndRender();
      },
      pagingRequestData: function () {
        return {
          pageNo: this.pagination.currentPageNo,
          pageSize: this.pagination.pageSize,
          criteria: this.queryJson
        };
      },
      calcRemoteUrl: function() {
        let _remote = this.remoteAjaxUrl;
        if (_remote.indexOf('?') < 0) {
          _remote += "?"
        }

        if (this.queryJson) {
          var q = this.queryJson;
          if (q) {
            var queryString = '';
            for (var key in q) {
              queryString += key + '=' + q[key];
            }
            _remote += queryString;
          }
        }

        if (_remote.indexOf('=') > -1 && !_remote.endsWith('&')) {
          _remote += '&';
        }

        _remote += "currentPageNo=" + this.pagination.currentPageNo;

        return _remote;
      },
      fetchAndRender: function () {
        if (!this.remoteAjaxUrl) {
          console.warn('_remoteUrl is not prepare')
          return;
        }
        let that = this;
        let url = this.calcRemoteUrl();

        console.log("paging ajax url is " + url);

        if (this.actionMethod == 'POST') {
          // window.axios.ajax()
          var data = {};
          // window.axios.post();
          axios.post(url, this.pagingRequestData())
            .then(function (response) {
              that.ajaxFinish(that, response);
            })
            .catch(function (error) {
              that.ajaxError(error);
            });
        } else {
          axios.get(url)
            .then(function (response) {
              that.ajaxFinish(that, response);
            })
            .catch(function (error) {
              that.ajaxError(error);
            });
        }

      },
      ajaxError: function (error) {
        console.log("error {}", error)
      },
      ajaxFinish: function (that, response) {
        // debugger;
        let res = response.data.data;
        let resultData = that.extractData(res);
        if (resultData && resultData.length > 0) {
          // console.log("datax first is " + JSON.stringify(resultData[0]));
        }

        that.rowData = resultData;
        let temp = that.extractPagination(res);
        if (that.pagination.currentPageNo > 0) {
          temp.currentPageNo = that.pagination.currentPageNo;
        }

        that.pagination = temp;
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

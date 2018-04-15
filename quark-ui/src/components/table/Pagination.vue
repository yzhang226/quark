<template>

  <div class="row">
    <div class="col-sm-5">
      <div class="dataTables_info" role="status" aria-live="polite">
        {{entriesMessage}}
      </div>
    </div>
    <div class="col-sm-7">
      <div class="dataTables_paginate paging_simple_numbers">
        <ul class="pagination">
          <template v-for="i in range(1, totalPageNo)">
            <li v-if="i === 1" :class="currentPageNo === i ? 'paginate_button previous disabled' : 'paginate_button previous'">
              <a href="javascript:void(0);" v-on:click="clickPageSize('pre')" aria-controls="example1" :data-dt-idx="-1" tabindex="0">上一页</a>
            </li>

            <li :class="pageBlockClass(i)">
              <a href="javascript:void(0);" v-on:click="clickPageSize(i)" aria-controls="example1" :data-dt-idx="i" tabindex="0">{{i === -1 ? '...' : i}}</a>
            </li>

            <li v-if="i === (totalPageNo)" :class="currentPageNo === (totalPageNo) ? 'paginate_button next disabled' : 'paginate_button next'" >
              <a href="javascript:void(0);" v-on:click="clickPageSize('next')" aria-controls="example1" :data-dt-idx="-1" tabindex="0">下一页</a>
            </li>

          </template>
        </ul>
      </div>
    </div>
  </div>

</template>

<script>
  export default {
    name: "VPagination",
    props: ['totalCount', 'currentPageNo', 'pageSize'],
    computed: {

      entriesMessage: function () {
        if (this.totalCount <= 0) {
          return "无记录";
        }
        return "当前 " + this.currentPageNo + " 页，共 " + this.totalPageNo + " 页";
      },
      totalPageNo: function () {
        if (this.totalCount <= 0) {
          return 0;
        }
        var totPage = this.totalCount / this.pageSize;
        if (this.totalCount % this.pageSize !== 0) {
          totPage++;
        }
        console.log("totPage is ", totPage);
        return totPage;
      },
      _currentPageNo: function () {
        if (this.currentPageNo <= 0) {
          return 1;
        }
        if (this.currentPageNo > (this.totalPageNo)) {
          return this.totalPageNo;
        }
        return this.currentPageNo;
      }
    },
    methods: {
      clickPageSize: function (pageNo) {
        if (pageNo === -1) {
          return;
        }
        let current = this._currentPageNo;
        var nxt = pageNo;
        if (pageNo === 'pre') {
          if (current <= 1) {
            return;
          }
          nxt = current - 1;
        }
        if (pageNo === 'next') {
          if (current >= this.totalPageNo) {
            return;
          }
          nxt = current + 1;
        }
        this.$emit('changePageSize', nxt)
      },
      pageBlockClass: function(pageNo) {
        if (pageNo === this._currentPageNo) {
          return 'paginate_button active';
        }
        if (pageNo === -1) {
          return 'paginate_button disabled';
        }

        return 'paginate_button';
      },
      range : function (start, end) {
        let current = this._currentPageNo;
        return this.calcPageIndex(current, this.totalPageNo, 8);
        // return Array(end - start + 1).fill().map((_, idx) => start + idx);
      },
      calcPageIndex: function (current, length, displayLength) {
        displayLength = displayLength - 2;
        let indexes = [1];
        let start = Math.round(current - displayLength / 2);
        let end = Math.round(current + displayLength / 2);
        if (start <= 1) {
          start = 2;
          end = start + displayLength - 1;
          if (end >= length - 1) {
            end = length - 1;
          }
        }
        if (end >= length - 1) {
          end = length - 1;
          start = end - displayLength + 1;
          if (start <= 1) {
            start = 2;
          }
        }
        if (start !== 2) {
          indexes.push(-1);
        }
        for (var i = start; i <= end; i++) {
          indexes.push(i);
        }
        if (end !== length - 1) {
          indexes.push(-1);
        }
        indexes.push(length);
        return indexes;
      }
    }
  }
</script>

<style scoped>

</style>

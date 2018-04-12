<template>
  <div>
    <DataTable :rowData="rowDatax" :header="header" :tableTitle="tableTitle">
    </DataTable>
  </div>
</template>

<script>
  import DataTable from './DataTable'

  export default {
    name: "AjaxDataTable",
    components: {DataTable},
    props: ['remoteUrl', 'header', 'tableTitle'],
    data: function() {
      return {
        rowDatax: this.rowDatax
      }
    },
    mounted: function () {
      console.log('a mounted');
    },
    created: function () {
      // `this` 指向 vm 实例
      // console.log('a is: ' + this.a);
      console.log('a created');

      this.setRowData();

    },
    methods: {
      setRowData: function () {
        var that = this;
        var url = this.remoteUrl;
        console.log("url is " + url);

        jQuery.ajax({
          url: url,
        }).done(function(data, status, xhr){
          // console.log("data is " + data);
          var res = JSON.parse(data);
          var datax = [];
          // console.log("res.articles is " + JSON.stringify(res.articles[0]))
          for (var i in res.articles) {
            var d = res.articles[i];
            var r = [];
            // console.log("d is " + JSON.stringify(d))
            // r.push({value: d.title});
            r.push({value: d.title});
            r.push({value: d.author_name});
            // console.log("r is " + JSON.stringify(r));
            datax.push(r);
          }
          console.log("datax first is " + JSON.stringify(datax[0]))
          that.rowDatax = datax;
        });
      }
    }
  }
</script>

<style scoped>

</style>

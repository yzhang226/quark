<template>
  <!-- Content Header (Page header) -->
  <section class="content-header">
    <h1>
      {{title}}
      <small>{{littleTitle}}</small>
    </h1>
    <ol class="breadcrumb">
      <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
      <li class="active">{{pageName}}</li>
    </ol>
  </section>
</template>

<script>
  export default {
    name: "k-content-header",
    props: ['title', 'titleSmall', 'pageName'],
    computed: {
      littleTitle: function () {
        return this.titleSmall != undefined ? this.titleSmall : this.title;
      }
    }
  }
</script>

<style scoped>

</style>

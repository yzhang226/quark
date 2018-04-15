<template>
  <div>
    <div class="box box-primary">
      <div class="box-header with-border">
        <h3 class="box-title">{{formTitle}}</h3>
      </div>
      <!-- /.box-header -->
      <!-- form start -->
      <form role="form" class="form-horizontal">
        <div class="box-body">
        <slot></slot>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Form",
    props: ['formTitle']

  }
</script>

<style scoped>

</style>

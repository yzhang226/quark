<template>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <slot name="header">
      <k-content-header :title="title" :pageName="pageName">
      </k-content-header>
    </slot>

    <!-- Main content -->
    <section class="content">
      <!-- Info boxes name="body" -->
      <slot >

      </slot>
      <!-- /.row -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

</template>

<script>
  export default {
    name: "k-content",
    props: ['title', 'pageName']
  }
</script>

<style scoped>

</style>

<template>

  <form role="form" :id="id" ref="_innerForm" :class="(eleClass || 'form-horizontal')">
    <slot></slot>
  </form>

  <!--<div>-->
    <!--&lt;!&ndash; box-primary  &ndash;&gt;-->
    <!--<div :class="[(boxClass || 'box box-primary')]">-->
      <!--<div class="box-header with-border">-->
        <!--<h3 class="box-title">{{title || ''}}</h3>-->
      <!--</div>-->
      <!--&lt;!&ndash; /.box-header &ndash;&gt;-->
      <!--&lt;!&ndash; form start &ndash;&gt;-->
      <!---->
    <!--</div>-->
  <!--</div>-->
</template>

<script>
  // this.$refs.h1ele.style.color = 'red';// 修改html样式
  export default {
    name: "k-form",
    props: ['title', 'eleClass', 'id'],
    computed: {

    },
    methods: {
      reset: function () {
        this.$refs._innerForm.reset();
      }
    }
  }
</script>

<style scoped>

</style>

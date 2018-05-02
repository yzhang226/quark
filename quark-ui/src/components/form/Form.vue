<template>

  <form role="form" :id="id" ref="_innerForm" :class="(eleClass || 'form-horizontal')">
    <slot></slot>
  </form>

</template>

<script>
  // import Vuelidate from 'vuelidate'
  // this.$refs.h1ele.style.color = 'red';// 修改html样式
  // mixins: [Vuelidate.validationMixin],
  export default {

    name: "k-form",
    props: ['title', 'eleClass', 'id', 'validations'],
    beforeCreate: function () {
      // this.validations =
      debugger;
      console.log("test ");
    },
    computed: {

    },
    // validations: this.validations,
    methods: {
      reset: function () {
        this.$refs._innerForm.reset();
      },
      validate: function () {
        //
      }
    }
  }
</script>

<style scoped>

</style>

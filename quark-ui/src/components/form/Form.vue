<template>
  <!-- :enableValidate="enableValidate" -->
  <form role="form" :id="id" ref="_innerForm" :class="(eleClass || 'form-horizontal')" >
    <slot :validations="validations"></slot>
  </form>

</template>

<script>
  // import Vuelidate from 'vuelidate'
  // this.$refs.h1ele.style.color = 'red';// 修改html样式
  // mixins: [Vuelidate.validationMixin],
  export default {

    name: "k-form",
    props: ['title', 'eleClass', 'id', 'validations', 'enableValidate', 'action', 'method'],
    data: function () {
      return {
        needValidateComponents: []
      }
    },
    beforeCreate: function () {

    },
    mounted: function () {
      // this.$nextTick(function () {
      //   // Code that will run only after the
      //   // entire view has been rendered
      //
      // });

      // console.log("form created $children is ", this.$children);

      if (Q.isNotEmpty(this.enableValidate) && this.enableValidate === true) {
        let vas = this.findAllValidatableComponents(this);
        for (let i=0; i<vas.length; i++) {
          let comp = vas[i];
          let modelExpression = comp.$vnode.data.model.expression;
          let constraints = Q.resolveValidateConstraint(this.validations, modelExpression);
          if (Q.isNotEmpty(constraints)) {
            this.needValidateComponents.push(comp);
          }
        }
      }


    },
    computed: {

    },
    // validations: this.validations,
    methods: {
      reset: function () {
        this.$refs._innerForm.reset();
        for (let i=0; i<this.needValidateComponents.length; i++) {
          let comp = this.needValidateComponents[i];
          comp.resetValidate();
        }
      },
      validate: function () {
        //
        let result = [];

        for (let i=0; i<this.needValidateComponents.length; i++) {
          let comp = this.needValidateComponents[i];
          let res = comp.validate();
          result.push(res);
        }

        return result;
      },

      findAllValidatableComponents: function (comp) {
        if (Q.isEmpty(comp) || Q.isArrayEmpty(comp.$children)) {
            return [];
        }

        let children = comp.$children;

        let vas = [];
        for(let i=0;i<children.length;i++){
          let ch = children[i];
          if (ch.supportValidate === true) {
            vas.push(ch);
          }
          let vas2 = this.findAllValidatableComponents(ch);
          if (Q.isArrayNotEmpty(vas2)) {
            vas.concat(vas2);
          }
        }

        return vas;
      }
    }
  }
</script>

<style scoped>

</style>

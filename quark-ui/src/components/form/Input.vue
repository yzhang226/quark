<template>

  <div :class='blockClass || "col-md-6"'>
    <div class="form-group" >
      <label :for="id" :class="['control-label', labelPlaceClass]">{{label}}</label>
      <div :class="placeClass">
        <input :type="type" :class='eleClass || "form-control"' :name="name" :id="id" :placeholder="placeholder"
                @input="emitChange($event.target.value)" :value="inputValue">
        
      </div>
    </div>
  </div>

  /*
  <div class="form-group has-success">
    <label class="control-label" for="inputSuccess"><i class="fa fa-check"></i> Input with success</label>
    <input type="text" class="form-control" id="inputSuccess" placeholder="Enter ...">
    <span class="help-block">Help block with success</span>
  </div>
  */

</template>

<script>
  export default {
    name: "k-input",
    model: {
      prop: 'inputValue',
      event: 'change'
    },
    props: ['type', 'eleClass', 'id', 'name', 'placeholder', 'label', 'blockClass',
            'inputValue', 'labelSm', 'readonly', 'validate'],
    computed: {
      labelPlaceClass: function () {
        if (this.labelSm == undefined || this.labelSm < 1) {
          return "col-sm-2";
        }
        return "col-sm-" + this.labelSm;
      },
      placeClass: function () {
        if (this.labelSm == undefined || this.labelSm < 1) {
          return "col-sm-10";
        }
        return "col-sm-" + (12 - this.labelSm);
      }
    },
    methods: {
      emitChange: function (val) {
        this.$emit('change', val)
      },
      doValidate: function () {
        
      }
    }
  }
</script>

<style scoped>

</style>

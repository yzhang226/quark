<template>

  <div :class='blockClass || "col-md-6"'>
    <div class="form-group">
      <label :for="id" :class="['control-label', labelPlaceClass]">{{label}}</label>
      <div :class="inputPlaceClass">
        <input :type="type" :class='inputClass || "form-control"' :name="name" :id="id" :placeholder="placeholder"
                @input="emitChange($event.target.value)" :value="inputValue">
      </div>
    </div>
  </div>

</template>

<script>
  export default {
    name: "k-input",
    model: {
      prop: 'inputValue',
      event: 'change'
    },
    props: ['type', 'inputClass', 'id', 'name', 'placeholder', 'label', 'blockClass', 'inputValue', 'labelSm'],
    computed: {
      labelPlaceClass: function () {
        if (this.labelSm == undefined || this.labelSm < 1) {
          return "col-sm-2";
        }
        return "col-sm-" + this.labelSm;
      },
      inputPlaceClass: function () {
        if (this.labelSm == undefined || this.labelSm < 1) {
          return "col-sm-10";
        }
        return "col-sm-" + (12 - this.labelSm);
      }
    },
    methods: {
      emitChange: function (val) {
        this.$emit('change', val)
      }
    }
  }
</script>

<style scoped>

</style>

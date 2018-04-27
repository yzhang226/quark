<template>

  <div :class='blockClass || "col-md-6"'>
    <div class="form-group">
      <label :for="id" :class="['control-label', labelPlaceClass]">{{label}}</label>
      <div :class="placeClass">
        <textarea :type="type" :class='eleClass || "form-control"' :name="name" :id="id" :placeholder="placeholder"
                  @input="emitChange($event.target.value)" :value="inputValue"></textarea>
      </div>
    </div>
  </div>

</template>

<script>
  export default {
    name: "k-textarea",
    model: {
      prop: 'inputValue',
      event: 'change'
    },
    props: ['type', 'eleClass', 'id', 'name', 'placeholder', 'label', 'blockClass', 'inputValue', 'labelSm', 'readonly'],
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
      }
    }
  }
</script>

<style scoped>

</style>

<template>

  <div :class='blockClass || "col-md-6"'>
    <div class="form-group">
      <label :for="id" class="col-sm-2 control-label">{{label}}</label>
      <div class="col-sm-10">
        <input :type="type" :class='inputClass || "form-control"' :name="name" :id="id" :placeholder="placeholder"
                @input="emitChange($event.target.value)" :value="inputValue">
      </div>
    </div>
  </div>

</template>

<script>
  export default {
    name: "VInput",
    model: {
      prop: 'inputValue',
      event: 'change'
    },
    props: ['type', 'inputClass', 'id', 'name', 'placeholder', 'label', 'blockClass', "inputValue"],
    computed: {

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

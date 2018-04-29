<template>
  <div :class="(blockClass || 'checkbox')">
    <label><input type="checkbox" :name="name" :value="value" :checked="isChecked" :disabled="readonly"
                  @change="emitChange($event)">{{comText}}</label>
  </div>
</template>

<script>
  export default {
    name: "k-checkbox",
    model: {
      prop: 'checkedValue',
      event: 'change'
    },
    props: {
      name: String,
      checkedValue: null,
      // 这样就允许拿 `value` 这个 prop 做其它事了
      value: null,
      text: String,
      readonly: Boolean,
      blockClass: String,
      trueValue: {
        default: true
      },
      falseValue: {
        default: false
      }
    },
    computed: {
      isChecked: function () {
        if (this.checkedValue instanceof Array) {
          return this.checkedValue.includes(this.value)
        }
        return this.checkedValue !== undefined && this.checkedValue != null;
      },
      comText: function () {
        if (this.text !== undefined) {
          return this.text;
        }
        return this.value;
      }
    },
    methods: {
      emitChange: function (event) {
        debugger;
        let isChecked = event.target.checked;
        if (this.checkedValue instanceof Array) {
          let newValue = [...this.checkedValue];
          if (isChecked) {
            newValue.push(this.value);
          } else {
            newValue.splice(newValue.indexOf(this.value), 1);
          }
          this.$emit('change', newValue);
        }
        else {
          // this.$emit('change', isChecked ? this.trueValue : this.falseValue);
          this.$emit('change', isChecked ? this.value : null);
        }
        // this.$emit('change', val)
      }
    }
  }
</script>

<style scoped>

</style>

<template>
  <div :class='blockClass || "col-md-6"'>
    <div class="form-group">
      <label :class="['control-label', labelPlaceClass]">{{label}}</label>
      <div :class="(selectPlaceClass)">
        <!--   -->
        <select :value="selected" :class="['form-control', selectClass]" :name="name" :id="id" :disabled="readonly"
                @change="emitChange($event.target.value)">
          <template v-for="o in selectData">
            <option :value="o.value" :selected="calcSelected(o)">{{o.text}}</option>
            <!--  -->
          </template>
        </select>
      </div>

    </div>
  </div>
</template>

<script>
  export default {
    name: "k-select",
    model: {
      prop: 'selected',
      event: 'change'
    },
    props:
      {
        selectData: Array,
        readonly: {
          type: Boolean,
          default: false
        },
        blockClass: String,
        label: String,
        name: String,
        id: String,
        selectClass: String,
        labelSm: {
          type: [String, Number]
        },
        selected: {
          type: [String,Number]
        },
        value: String
      },

    // ['blockClass', 'label', 'name', 'id', 'optionData', 'selectClass', 'labelSm'],

    computed: {
      getSelected: function () {

      },
      labelPlaceClass: function () {
        if (this.labelSm == undefined || this.labelSm < 1) {
          return "col-sm-2";
        }
        return "col-sm-" + this.labelSm;
      },
      selectPlaceClass: function () {
        if (this.labelSm == undefined || this.labelSm < 1) {
          return "col-sm-10";
        }
        return "col-sm-" + (12 - this.labelSm);
      }
    },
    methods: {
      calcSelected: function (o) {
        if (o && o.selected) {
          return true;
        }
        return o.value == this.value;
      },
      emitChange: function (val) {
        this.$emit('change', val)
      }
    }
  }
</script>

<style scoped>

</style>

<template>
  <div :class='blockClass || "col-md-6"'>
    <div class="form-group">
      <label :class="['control-label', labelPlaceClass]">{{label}}</label>
      <div :class="(selectPlaceClass)">
        <div :class="validatedCss">

          <select :class="['form-control', selectClass]" :name="name" :id="id" :disabled="readonly"
                  @change="emitChange($event.target.value)">
            <template v-for="o in selectData">
              <option :value="o.value" :selected="calcSelected(o)">{{o.text}}</option>
              <!--  -->
            </template>
          </select>

          <template v-if="isValidateEnable">
            <template v-if="invalidated">
              <label class="control-label"><i class="fa fa-times-circle-o"></i> {{invalidatedMessage}}</label>
            </template>
            <template v-else>
              <label class="control-label"><i class="fa fa-check"></i></label>
            </template>
          </template>

        </div>

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
        selected: {
          type: [String, Number]
        },
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
        enableValidate: Boolean,
        constraints: Object
      },
    data: function () {
      return {
        supportValidate: true,
        validateResult: null,
        validateEnable: this.enableValidate,
      }
    },

    watch: {
      selected: function (value) {
        debugger;
        console.log("select watch value is ", value, ", ", (typeof value));
      }
    },

    // ['blockClass', 'label', 'name', 'id', 'optionData', 'selectClass', 'labelSm'],

    computed: {
      isValidateEnable: function () {
        return Q.isNotEmpty(this.validateEnable) && this.validateEnable === true;
      },
      invalidated: function () {
        return Q.isNotEmpty(this.validateResult) && this.validateResult.isError();
      },
      invalidatedMessage: function() {
        return Q.isNotEmpty(this.validateResult) ? this.validateResult.message : '';
      },
      validatedCss: function () {
        let c = [];// ['form-group'];
        if (!this.doesValidateEnable()) {
          return c;
        }
        if (Q.isNotEmpty(this.validateResult) && this.validateResult.isError()) {
          c.push('has-error');
        } else {
          c.push('has-success');
        }
        return c;
      },


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
      doesValidateEnable: function () {
        return this.validateEnable === true;
      },
      calcSelected: function (o) {
        debugger;
        if (o && o.selected) {
          return true;
        }
        if (Q.isEmpty(o.value) && Q.isEmpty(this.value)) {
          return true;
        }
        return ("" + o.value) == ("" + this.value);
      },
      emitChange: function (val) {
        this.$emit('change', val);
        this.validate(val);
      },
      getConstraints: function () {
        return this.constraints;
      },
      validate: function (val) {
        // debugger;
        val = Q.isNotEmpty(val) ? val : this.selected;
        let constraints = this.getConstraints();
        if (Q.isEmpty(constraints)) {
          return null;
        }

        let res = validator.validate(constraints, val);

        let err = Q.filterOneErrorValidate(res);

        console.log("validate select this.selected is ", this.selected, ", res is ", res);

        this.setValidateResult(err);

        return err;
      },
      setValidateResult: function (res) {
        this.validateEnable = true;
        this.validateResult = res;
      },
      resetValidate: function () {
        this.validateEnable = false;
        this.validateResult = null;
      }
    }
  }
</script>

<style scoped>

</style>

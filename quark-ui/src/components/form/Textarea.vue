<template>

  <div :class='blockClass || "col-md-6"'>
    <div class="form-group">
      <label :for="id" :class="['control-label', labelPlaceClass]">{{label}}</label>
      <div :class="placeClass">
        <div :class="validatedCss">
        <textarea :type="type" :class='eleClass || "form-control"' :name="name" :id="id" :placeholder="placeholder"
                  @input="emitChange($event.target.value)" :value="inputValue"></textarea>

        <template v-if="isValidateEnable">
          <template v-if="invalidated">
            <label class="control-label"><i class="fa fa-times-circle-o"></i> {{invalidatedMessage}}</label>
          </template>
          <template v-else>
            <label class="control-label" ><i class="fa fa-check"></i></label>
          </template>
        </template>

        </div>

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
    props: ['type', 'eleClass', 'id', 'name', 'placeholder', 'label', 'blockClass', 'inputValue',
      'labelSm', 'readonly', 'enableValidate', 'constraints'],
    data: function () {
      return {
        supportValidate: true,
        validateResult: null,
        validateEnable: this.enableValidate,
      }
    },
    computed: {
      isValidateEnable: function () {
        return Q.isNotEmpty(this.validateEnable) && this.validateEnable === true;
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
      invalidated: function () {
        return Q.isNotEmpty(this.validateResult) && this.validateResult.isError();
      },
      invalidatedMessage: function() {
        return Q.isNotEmpty(this.validateResult) ? this.validateResult.message : '';
      },
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
      doesValidateEnable: function() {
        return this.validateEnable === true;
      },
      emitChange: function (val) {
        debugger;
        this.$emit('change', val);
        this.validate(val);
      },
      getConstraints: function () {
        return this.constraints;
      },
      validate: function (val) {
        val = Q.isNotEmpty(val) ? val : this.inputValue;
        let constraints = this.getConstraints();
        if (Q.isEmpty(constraints)) {
          return null;
        }

        let res = validator.validate(constraints, val);
        console.log("this.text-area res is ", res);

        let err = Q.filterOneErrorValidate(res);
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

<template>

  <div class="modal fade in" :style="visibleClass">
    <div :class="['modal-dialog', modelSizeClass]">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close" @click="close">
            <span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title"><span v-html="(title || '框')"></span></h4>
        </div>
        <div class="modal-body">
          <slot></slot>
        </div>
        <slot name="footer">
          <div class="modal-footer">
            <button type="button" class="btn btn-default pull-left" @click="cancel" data-dismiss="modal">{{closeText||'取消'}}</button>
            <button type="button" class="btn btn-primary" @click="confirm">{{confirmText||'确定'}}</button>
          </div>
        </slot>
      </div>
      <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
  </div>

</template>

<script>
  // TODO: 应该有更优雅的实现, 目前使用v-model实现双向绑定

  let __visibleHide = "display: none;";
  let __visibleShow = "display: block; padding-right: 15px;";

  export default {
    name: "k-modal",
    props: {
      title: String,
      closeText: String,
      confirmText: String,
      confirmCallback: Function,
      cancelCallback: Function,
      closeCallback: Function,
      visible: {
        type: Boolean,
        default: false
      },
      modalSize: {
        type: String,
        default: 'default'
      }
    },
    model: {
      prop: 'visible',
      event: 'change'
    },
    data: function () {
      return {
        _visible: false,
        visibleClass: __visibleHide
      }
    },
    watch: {
      visible: function (value) {
        this.setCalcVisibleClass(value);
        return value;
      }
    },
    computed: {
      modelSizeClass: function () {
        if (this.modalSize == "large") {
          return "modal-lg";
        } else if (this.modalSize == "small") {
          return "modal-sm";
        } else {
          return '';
        }
      }
    },
    methods: {
      setCalcVisibleClass: function (value) {
        this._visible = value;
        if (this._visible === true) {
          this.visibleClass = __visibleShow;
        } else {
          this.visibleClass = __visibleHide;
        }
      },
      _doClose: function () {
        this.setCalcVisibleClass(false);
        this.emitChange(false);
      },
      close: function () {
        if (this.closeCallback) {
          this.closeCallback();
        }
        this._doClose();
      },
      cancel: function () {
        if (this.cancelCallback) {
          this.cancelCallback();
        }
        this._doClose();
      },
      confirm: function () {
        let res = true;
        if (this.confirmCallback) {
          res = this.confirmCallback();
        }
        if (res !== false) {
          this._doClose();
        }
      },
      emitChange: function (val) {
        // console.log("emit change val is " + val + (typeof val));
        this.$emit('change', val)
      }
    }
  }
</script>

<style scoped>

</style>

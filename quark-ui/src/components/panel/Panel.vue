<template>

  <div :class="['box', (boxClass||'box-default')]">
    <div :class="headerClass">
      <slot name="header">
        <h3 class="box-title">{{title}}</h3>

        <div class="box-tools pull-right">
          <button v-if="collapseEnable" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
          <button v-if="removeEnable" type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-remove"></i></button>
        </div>
      </slot>
    </div>

    <div class="box-body">
      <!-- name="body" -->
      <slot ></slot>
    </div>


    <div class="box-footer" v-if="!(footerEnable == 'false')">
      <slot name="footer"></slot>
    </div>

  </div>

</template>

<script>
export default {
  name: 'k-panel',
  props: ['title', 'collapseEnable', 'removeEnable', 'boxClass', 'headerBorderEnable', 'footerEnable'],
  computed: {
    headerClass: function () {
      var clazz = ['box-header'];
      if (this.headerBorderEnable == "true") {
        clazz.push('with-border')
      }
      return clazz;
    }
  }
}
</script>

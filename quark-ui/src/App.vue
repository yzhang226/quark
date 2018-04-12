<template>
  <div id="app" class="wrapper">
    <top-header></top-header>
    <sidebar></sidebar>

    <!-- 路由出口 -->
    <!-- 路由匹配到的组件将渲染在这里 -->
    <router-view></router-view>

    <bottom-footer></bottom-footer>
    <off-sidebar></off-sidebar>
    <!-- /.control-sidebar -->
    <!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
    <div class="control-sidebar-bg"></div>
  </div>
</template>

<script>
// import Layout from './components/layout/Layout'
import TopHeader from './components/layout/TopHeader'
import Sidebar from './components/layout/Sidebar'
import BottomFooter from './components/layout/BottomFooter'
import OffSidebar from './components/layout/OffSidebar'

export default {
  beforeCreate: function() {
    document.getElementsByTagName("body")[0].className = "hold-transition skin-blue sidebar-mini";
    console.log('document.getElementsByTagName("body")[0].className is ' + document.getElementsByTagName("body")[0].className);
  },
  name: 'App',
  components: {
    TopHeader, Sidebar, OffSidebar, BottomFooter
  }
}
</script>

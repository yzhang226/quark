<template>
  <!-- vue2一个template仅允许一个根元素 -->
  <div>
    <header></header>
    <siderbar></siderbar>
    <off-siderbar></off-siderbar>
    <div class="content-wrapper">
      <!-- Content Header (Page header) -->
      <section class="content-header">
        <h1>
          Dashboard
          <small>Version 2.0</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
          <li class="active">Dashboard</li>
        </ol>
      </section>

      <!-- Main content -->
      <section class="content">
        <p>
          <!-- 使用 router-link 组件来导航. -->
          <!-- 通过传入 `to` 属性指定链接. -->
          <!-- <router-link> 默认会被渲染成一个 `<a>` 标签 -->
          <router-link to="/foo">Go to MyFoo</router-link>
          <router-link to="/bar">Go to MyBar</router-link>
        </p>
        <!-- 路由出口 -->
        <!-- 路由匹配到的组件将渲染在这里 -->
        <router-view></router-view>
      </section>
    </div>
  </div>

</template>

<script>
import Header from './TopHeader'
import Siderbar from './Sidebar'
import OffSiderbar from './OffSidebar'

export default {
  name: 'layout',

  components: {
    Header,
    Siderbar,
    OffSiderbar
  }
}
</script>

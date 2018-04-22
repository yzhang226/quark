
import Vue from 'vue'
import Router from 'vue-router'

// 组件
import DataTable from '../components/table/DataTable'
import AjaxPagingTable from '../components/table/AjaxPagingTable'
import Form from '../components/form/Form'
import VInput from '../components/input/Input'
import Panel from '../components/panel/Panel'
import Pagination from '../components/table/Pagination'
import Content from '../components/content/Content'
import ContentHeader from '../components/content/ContentHeader'

Vue.component("k-table", DataTable);
Vue.component("k-ajax-table", AjaxPagingTable);
Vue.component("k-form", Form);
Vue.component("k-input", VInput);
Vue.component("k-panel", Panel);
Vue.component("k-paging", Pagination);

Vue.component("k-content", Content);
Vue.component("k-content-header", ContentHeader);

import HelloWorld from '../pages/hello/Index'
import Bar from '../pages/Bar'
import Foo from '../pages/Foo'
import Dashboard from '../components/Dashboard'


Vue.use(Router)

// 0. 如果使用模块化机制编程，导入Vue和VueRouter，要调用 Vue.use(VueRouter)

// 1. 定义（路由）组件。
// 可以从其他文件 import 进来
// const Foo = { template: '<div>foo</div>' }
// const Bar = { template: '<div>bar</div>' }

// 2. 定义路由
// 每个路由应该映射一个组件。 其中"component" 可以是
// 通过 Vue.extend() 创建的组件构造器，
// 或者，只是一个组件配置对象。
// 我们晚点再讨论嵌套路由。
const routes = [
  { path: '/foo', name: 'my-foo', component: Foo },
  { path: '/bar', name: 'my-bar', component: Bar },
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/hello',
    name: 'HelloWorld',
    component: HelloWorld
  }
]

// 3. 创建 router 实例，然后传 `routes` 配置
// 你还可以传别的配置参数, 不过先这么简单着吧。
const router = new Router({
  routes: routes // （缩写）相当于 routes: routes
})

export default router

// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'

// import Vuelidate from 'vuelidate'
// Vue.use(Vuelidate);

// import {required, minLength, sameAs} from 'vuelidate/lib/validators'
// window.required = required;
// window.minLength = minLength;
// window.sameAs = sameAs;


import App from './App'
import router from './router'
let $ = require('jquery/dist/jquery.js');

window.$ = $;
window.jQuery = $;

Vue.config.productionTip = false;



import axios from 'axios';
window.axios = axios;
Vue.prototype.$http = axios;

import Swal from 'sweetalert2'
window.Swal = Swal;

require('datatables.net-bs/css/dataTables.bootstrap.css');

require('bootstrap/dist/css/bootstrap.css');
require('font-awesome/css/font-awesome.css');
require('ionicons/dist/css/ionicons.css');

require('jvectormap/jquery-jvectormap.css');
require('admin-lte/dist/css/AdminLTE.css');
require('admin-lte/dist/css/skins/_all-skins.css');

window.$j = $.noConflict();

require('bootstrap/dist/js/bootstrap.js');
require('fastclick/lib/fastclick.js');
require('admin-lte/dist/js/adminlte.js');
require('jquery-sparkline/jquery.sparkline.js');

require('jvectormap/lib/jvectormap');
require('jvectormap/lib/world-map');
require('jvectormap/lib/vector-canvas');
require('jvectormap/lib/abstract-element');
require('jvectormap/lib/abstract-canvas-element');
require('jvectormap/lib/svg-element');
require('jvectormap/lib/svg-canvas-element');
require('jvectormap/lib/abstract-shape-element.js');
require('jvectormap/lib/numeric-scale.js');
require('jvectormap/lib/color-scale.js');
require('jvectormap/lib/data-series.js');
require('jvectormap/lib/ordinal-scale.js');
require('jvectormap/lib/proj.js');
require('jvectormap/lib/simple-scale.js');
require('jvectormap/lib/svg-shape-element.js');
require('jvectormap/lib/svg-group-element.js');
require('jvectormap/lib/svg-path-element.js');
require('jvectormap/lib/svg-circle-element.js');
require('jvectormap/lib/vml-element.js');
require('jvectormap/lib/vml-shape-element.js');
require('jvectormap/lib/vml-group-element.js');
require('jvectormap/lib/vml-path-element.js');
require('jvectormap/lib/vml-canvas-element.js');
require('jvectormap/lib/vml-circle-element.js');
require('jvectormap/jquery-jvectormap.js');
require('jvectormap/jquery-mousewheel');

require('./plugins/jvectormap/jquery-jvectormap-world-mill-en');
require('jquery-slimscroll/jquery.slimscroll');
require('chart.js/Chart.js');
// require('admin-lte/dist/js/pages/dashboard2.js')
// require('admin-lte/dist/js/demo.js')
require('./plugins/adminlte/demo');

import Q from './components/utils/core'
import validator from './components/validator/va'

window.Q = Q;
window.validator = validator;


require('./mock/mock-data');

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});




import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import ELEMENT from 'element-ui'
Vue.use(ELEMENT)
import axios from 'Axios'
axios.defaults.withCredentials = true; 
Vue.prototype.$axios= axios
import $ from 'jQuery'
let Base64 = require('js-base64').Base64;
Vue.config.productionTip = false
// console.log(process.env);


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

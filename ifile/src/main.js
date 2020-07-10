import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import axios from 'axios'
import 'element-ui/lib/theme-chalk/index.css';
axios.defaults.withCredentials = true
Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.prototype.$axios = axios

new Vue({
  render: h => h(App),
}).$mount('#app')

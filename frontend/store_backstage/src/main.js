import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from '@/store';
import md5 from 'js-md5';

// 引入项目图标库
import './assets/style/fbookicon.css';
// 引入项目主题色样式
import './assets/style/themes.css';

Vue.config.productionTip = false;
// 将md5绑定到vue上
Vue.prototype.$md5 = md5;
// 设置项目静态资源
Vue.prototype.$staticUrl = 'http://localhost:8080';

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

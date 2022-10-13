import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from '@/store';

// 引入项目图标库
import './assets/style/fbookicon.css';
// 引入项目主题色样式
import './assets/style/themes.css';

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

import Vue from 'vue';
import App from './App.vue';
import router from './router';
// 引入项目图标库
import './assets/css/fbookicon.css';
// 引入项目主题色样式
import './assets/css/themes.css';

Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App)
}).$mount('#app');

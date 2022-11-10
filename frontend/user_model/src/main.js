import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from '@/store';
import md5 from 'js-md5';

// 导入通用样式
import '@/assets/style/theme.css';
import '@/assets/fonts/font.css';
import '@/assets/style/common.less';

import banner from '@/assets/images/banner.jpg';
import noCover from '@/assets/images/no-cover.jpg';
import noAvatar from '@/assets/images/no-avatar.jpg';

Vue.config.productionTip = false;
Vue.prototype.$bannerUrl = banner;
Vue.prototype.$noCoverUrl = noCover;
Vue.prototype.$noAvatarUrl = noAvatar;
/** 封装一些工具到Vue上 */
// 将md5绑定到vue上
Vue.prototype.$md5 = md5;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

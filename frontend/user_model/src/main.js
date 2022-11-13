import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from '@/store';
import md5 from 'js-md5';

import { Message } from 'element-ui';

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

/** 封装使用率高的工具函数 */
/**
   * 封装$message
   * @param {String} msg 消息体
   * @param {String} type 消息类型 success/warning/info/error
   * @param {Boolean} isClose 是否可以关闭
   * @param {Function} closeFunc 关闭后执行函数
   * @param {Number} duration 存在时间
   */
Vue.prototype.$showMsg = function(msg, { type = 'info', isClose = true, closeFunc = () => {}, duration = 800 } = {}) {
  Message({
    message: msg,
    type: type,
    showClose: isClose,
    onClose: closeFunc,
    duration: duration
  });
}
/**
 * 判断传入对象是否为空
 * @param {Object} obj 一个对象
 * @returns 布尔结果
 */
Vue.prototype.$isEmpty = function(obj) {
  let str = JSON.stringify(obj);
  let flag = false;
  switch(str) {
    case '""':
      flag = true;
      break;
    case '[]':
      flag = true;
      break;
    case '{}':
      flag = true;
      break;
    case 'null':
      flag = true;
      break;
    case undefined:
      flag = true;
      break;
  }
  return flag;
}

/** 加载插件 */
// const VueMasonryPlugin = window['vue-masonry-plugin'].VueMasonryPlugin;
// Vue.use(VueMasonryPlugin);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

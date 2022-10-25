import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from '@/store';
import md5 from 'js-md5';
import { Message } from 'element-ui';

// 引入项目图标库
import './assets/style/fbookicon.css';
// 引入项目主题色样式
import './assets/style/themes.css';

Vue.config.productionTip = false;
// 将md5绑定到vue上
Vue.prototype.$md5 = md5;
// 设置项目静态资源
Vue.prototype.$staticUrl = 'http://localhost:8080';

/**
   * 封装$message
   * @param {String} msg 消息体
   * @param {String} type 消息类型 success/warning/info/error
   * @param {Boolean} isClose 是否可以关闭
   * @param {Function} closeFunc 关闭后执行函数
   * @param {Number} duration 存在时间
   */
Vue.prototype.$showMsgs = function(msg, { type = 'info', isClose = true, closeFunc = () => {}, duration = 1200 } = {}) {
  Message({
    message: msg,
    type: type,
    showClose: isClose,
    onClose: closeFunc,
    duration: duration
  });
}

const Root = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

export default Root;

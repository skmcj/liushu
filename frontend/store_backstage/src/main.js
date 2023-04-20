import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from '@/store';
import md5 from 'js-md5';
// TIM
import tim from '@/packages/tim-sdk/tim';
import TIM from '@/packages/tim-sdk/tim-js-friendship';

import { Message } from 'element-ui';

// 引入项目图标库
import './assets/style/fbookicon.css';
// 引入项目主题色样式
import './assets/style/themes.css';

Vue.config.productionTip = false;
// 将md5绑定到vue上
Vue.prototype.$md5 = md5;
// 设置项目静态资源
// 开发环境
Vue.prototype.$staticUrl = 'http://localhost:8080';

/**
 * 封装$message
 * @param {String} msg 消息体
 * @param {String} type 消息类型 success/warning/info/error
 * @param {Boolean} isClose 是否可以关闭
 * @param {Function} closeFunc 关闭后执行函数
 * @param {Number} duration 存在时间
 */
Vue.prototype.$showMsgs = function (
  msg,
  { type = 'info', isClose = true, closeFunc = () => {}, duration = 1200 } = {}
) {
  Message({
    message: msg,
    type: type,
    showClose: isClose,
    onClose: closeFunc,
    duration: duration
  });
};

/**
 * 判断传入对象是否为空
 * @param {Object} obj 一个对象
 * @returns 布尔结果
 */
Vue.prototype.$isEmpty = function (obj) {
  let str = JSON.stringify(obj);
  let flag = false;
  switch (str) {
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
};

/**
 * 保留两位小数
 * @param {Number} num
 */
Vue.prototype.$keepTwoNum = function (num) {
  // 四舍五入
  let mNum = Math.round(num * 100) / 100;
  let numStr = mNum.toString();
  let dotIndex = numStr.indexOf('.');
  // 当整数时，pos_decimal=-1 自动补0
  if (dotIndex < 0) {
    dotIndex = numStr.length;
    numStr += '.';
  }

  // 当数字的长度< 小数点索引+2时，补0
  while (numStr.length <= dotIndex + 2) {
    numStr += '0';
  }
  return numStr;
};

/**
 * TIM 服务
 */
window.tim = tim;
window.TIM = TIM;

Vue.prototype.tim = tim;
Vue.prototype.TIM = TIM;
Vue.prototype.$bus = new Vue();
// 图床域名
// 开发环境
Vue.prototype.$ossPath = 'http://localhost:8080/api/img/';

const Root = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

export default Root;

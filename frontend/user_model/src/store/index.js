// 引入 vue
import Vue from 'vue';
// 引入 Vuex
import Vuex from 'vuex';

import conversation from './modules/conversation';
import group from './modules/group';
import user from './modules/user';
import video from './modules/video';
import friend from './modules/friend';
import blacklist from './modules/blacklist';
import { Message } from 'element-ui';

// Vue.use(Vuex) 必须在 store 创建前执行
Vue.use(Vuex);

// 准备actions -> 用于响应组件中的动作
const actions = {
  // 发出设置settlementObj的动作
  setSettlementObj(context, value) {
    // 'setSettlementObj' 是mutations中相应操作的名称
    context.commit('setSettlementObj', value);
  },
  // 发出设置checkedAddress的动作
  setCheckedAddress(context, value) {
    // 'setCheckedAddress' 是mutations中相应操作的名称
    context.commit('setCheckedAddress', value);
  },
  // 发出设置userinfo的动作
  setUserInfo(context, value) {
    // 'setUserInfo' 是mutations中相应操作的名称
    context.commit('setUserInfo', value);
  },
  // 发出设置loginFromPath的动作
  setLoginFromPath(context, value) {
    // 'setLoginFromPath' 是mutations中相应操作的名称
    context.commit('setLoginFromPath', value);
  },
  // 发出设置loginFlag的动作
  setLoginFlag(context, value) {
    // 'setLoginFlag' 是mutations中相应操作的名称
    context.commit('setLoginFlag', value);
  },
  // 发出设置token的动作
  setToken(context, value) {
    // 'setToken' 是mutations中相应操作的名称
    context.commit('setToken', value);
  },
  // keyword
  setKeyword(context, value) {
    context.commit('setKeyword', value);
  }
};
// 准备mutations -> 用于操作数据(state)
const mutations = {
  // 设置 settlementObj 值
  setSettlementObj(state, value) {
    sessionStorage.setItem('settlementObj', JSON.stringify(value));
    state.settlementObj = value;
  },
  // 设置 checkedAddress 值
  setCheckedAddress(state, value) {
    sessionStorage.setItem('checkedAddress', JSON.stringify(value));
    state.checkedAddress = value;
  },
  // 设置 userInfo 值
  setUserInfo(state, value) {
    localStorage.setItem('userInfo', JSON.stringify(value));
    state.userInfo = value;
  },
  // 设置logon的from路径
  setLoginFromPath(state, value) {
    sessionStorage.setItem('loginFromPath', JSON.stringify(value));
    state.loginFromPath = value;
  },
  // 设置 loginFlag 的值
  setLoginFlag(state, value) {
    sessionStorage.setItem('loginFlag', JSON.stringify(value));
    state.loginFlag = value;
  },
  // 设置 token 的值
  setToken(state, value) {
    localStorage.setItem('userToken', JSON.stringify(value));
    state.token = value;
  },
  // TIM 相关
  startComputeCurrent(state) {
    state.intervalID = setInterval(() => {
      state.current = Date.now();
    }, 500);
  },
  stopComputeCurrent(state) {
    clearInterval(state.intervalID);
    state.intervalID = 0;
  },
  showMessage(state, options) {
    if (state.message) {
      state.message.close();
    }
    state.message = Message({
      message: options.message,
      type: options.type || 'success',
      duration: options.duration || 2000,
      offset: 40
    });
  },
  // 设置全局搜索关键词
  setKeyword(state, value) {
    sessionStorage.setItem('keyword', JSON.stringify(value));
    state.keyword = value;
  }
};
// 准备state -> 用于存储数据
// sessionStorage 将数据持久存储于会话中
const state = {
  // 待结算商品
  settlementObj: JSON.parse(sessionStorage.getItem('settlementObj')) || {},
  // 选中地址
  checkedAddress: JSON.parse(sessionStorage.getItem('checkedAddress')) || {},
  // 用户信息
  userInfo: JSON.parse(localStorage.getItem('userInfo')) || {},
  // 用户登录的from路径
  loginFromPath: JSON.parse(sessionStorage.getItem('loginFromPath')) || '/',
  // 用户是否登录
  loginFlag: JSON.parse(sessionStorage.getItem('loginFlag')) || false,
  // 用户token
  token: JSON.parse(localStorage.getItem('userToken')) || '',
  // TIM 相关
  current: Date.now(), // 当前时间
  intervalID: 0,
  message: undefined,
  // 全局搜索
  keyword: ''
};

const getters = {
  hidden(state) {
    // eslint-disable-next-line no-unused-vars
    const temp = state.current;
    if (typeof document.hasFocus !== 'function') {
      return document.hidden;
    }
    return !document.hasFocus();
  }
};
const modules = {
  conversation,
  group,
  friend,
  blacklist,
  user,
  video
};

// 待优化
/**
const storeMaker = (state) => {
  // 初始化
  Object.keys(state).map(key => {
    // 判断类型获取本地存储数据
    if (typeof state[key] === 'object') {
      if (sessionStorage.getItem(key) && JSON.parse(sessionStorage.getItem(key))) {
        state[key] = JSON.parse(sessionStorage.getItem(key));
      }
    } else if (typeof state[key] === 'number') {
      if (sessionStorage.getItem(key) && parseInt(sessionStorage.getItem(key))) {
        state[key] = parseInt(sessionStorage.getItem(key));
      }
    } else {
      if (sessionStorage.getItem(key)) {
        state[key] = sessionStorage.getItem(key);
      }
    }
    return true;
  });

  // 重写set处理
  return new Proxy(state, {
    set: function(target, key, value) {
      let temp = value;
      if (typeof temp === 'object') {
        temp = JSON.stringify(temp);
      }

      sessionStorage.setItem(key, temp);
      return Reflect.set(target, key, value);
    }
  });
}
* 定义一个 storeMaker 函数，实现
* 1、对传入的state初始值判断类型，并尝试从 sessionStorage 中读取数据替换默认值
* 2、通过 Proxy 代理 state 的 set 逻辑，添加同步保存到 sessionStorage 的逻辑
* 使用：
* 在定义 state 时，使用 storeMaker({}) 包装定义
* 如：state = storeMaker({ settlementObj: {} })
*/

// 创建store
export default new Vuex.Store({
  actions,
  mutations,
  state,
  getters,
  modules
});

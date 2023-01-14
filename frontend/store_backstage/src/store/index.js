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
  // 发出设置employeeInfo的值的动作
  setEmployeeInfo(context, value) {
    // 'setBusinessInfo' 是mutations中相应操作的名称
    context.commit('setEmployeeInfo', value);
  },
  // 发出设置businessInfo的值的动作
  setBusinessInfo(context, value) {
    // 'setBusinessInfo' 是mutations中相应操作的名称
    context.commit('setBusinessInfo', value);
  },
  // 发出设置businessDetail的值的动作
  setBusinessDetail(context, value) {
    context.commit('setBusinessDetail', value);
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
  }
};
// 准备mutations -> 用于操作数据(state)
const mutations = {
  // 设置employeeInfo值
  setEmployeeInfo(state, value) {
    localStorage.setItem('employeeInfo', JSON.stringify(value));
    state.employeeInfo = value;
  },
  // 设置businessInfo值
  setBusinessInfo(state, value) {
    sessionStorage.setItem('businessInfo', JSON.stringify(value));
    state.businessInfo = value;
  },
  // 设置businessDetail的值
  setBusinessDetail(state, value) {
    sessionStorage.setItem('businessDetail', JSON.stringify(value));
    state.businessDetail = value;
  },
  // 设置 loginFlag 的值
  setLoginFlag(state, value) {
    sessionStorage.setItem('eLoginFlag', JSON.stringify(value));
    state.loginFlag = value;
  },
  // 设置 token 的值
  setToken(state, value) {
    localStorage.setItem('employeeToken', JSON.stringify(value));
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
  }
};

// 准备state -> 用于存储数据
const state = {
  employeeInfo: JSON.parse(localStorage.getItem('employeeInfo')) || {},
  businessInfo: JSON.parse(sessionStorage.getItem('businessInfo')) || {},
  businessDetail: JSON.parse(sessionStorage.getItem('businessDetail')) || {},
  // 用户是否登录
  loginFlag: JSON.parse(sessionStorage.getItem('eLoginFlag')) || false,
  // 用户token
  token: JSON.parse(localStorage.getItem('employeeToken')) || '',
  // TIM 相关
  current: Date.now(), // 当前时间
  intervalID: 0,
  message: undefined
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

// 创建store
export default new Vuex.Store({
  actions,
  mutations,
  state,
  getters,
  modules
});

// 引入 vue
import Vue from 'vue';
// 引入 Vuex
import Vuex from 'vuex';

// Vue.use(Vuex) 必须在 store 创建前执行
Vue.use(Vuex);

// 准备actions -> 用于响应组件中的动作
const actions = {
  // 发出设置businessInfo的值的动作
  setBusinessInfo(context, value) {
    // 'setBusinessInfo' 是mutations中相应操作的名称
    context.commit('setBusinessInfo', value);
  },
  // 发出设置businessDetail的值的动作
  setBusinessDetail(context, value) {
    context.commit('setBusinessDetail', value);
  }
};
// 准备mutations -> 用于操作数据(state)
const mutations = {
  // 设置businessInfo值
  setBusinessInfo(state, value) {
    state.businessInfo = value;
  },
  // 设置businessDetail的值
  setBusinessDetail(state, value) {
    state.businessInfo = value;
  }
};
// 准备state -> 用于存储数据
const state = {
  businessInfo: {},
  businessDetail: {}
};

// 创建store
export default new Vuex.Store({
  actions,
  mutations,
  state
});

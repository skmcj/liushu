// 引入 vue
import Vue from 'vue';
// 引入 Vuex
import Vuex from 'vuex';

// Vue.use(Vuex) 必须在 store 创建前执行
Vue.use(Vuex);

// 准备actions -> 用于响应组件中的动作
const actions = {};
// 准备mutations -> 用于操作数据(state)
const mutations = {};
// 准备state -> 用于存储数据
const state = {};

// 创建store
export default new Vuex.Store({
  actions,
  mutations,
  state
});

// 引入 vue
import Vue from 'vue';
// 引入 Vuex
import Vuex from 'vuex';

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
  }
};
// 准备state -> 用于存储数据
// sessionStorage 将数据持久存储于会话中
const state = {
  // 待结算商品
  settlementObj: JSON.parse(sessionStorage.getItem('settlementObj')) || {},
  // 选中地址
  checkedAddress: JSON.parse(sessionStorage.getItem('checkedAddress')) || {}
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
  state
});

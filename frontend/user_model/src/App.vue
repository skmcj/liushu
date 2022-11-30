<template>
  <div id="app">
    <transition name="fade">
      <router-view name="index" />
    </transition>
  </div>
</template>

<script>
import { validateTokenApi, getUserInfoApi } from '@/api/userApi';

export default {
  created() {
    // 应用初始化时
    this.initUserInfo();
  },
  methods: {
    /**
     * 初始化用户登录信息
     */
    initUserInfo() {
      // console.log('app init');
      const userInfo = this.$store.state.userInfo;
      // 查看本地是否有用户信息
      // 无，直接返回
      if (!userInfo) return;
      // 有，则发送请求，验证token是否有效
      const token = this.$store.state.token;
      validateTokenApi(token).then(res => {
        if (res.data.flag) {
          // 有效，设置会话默认值
          // console.log('token => ', res.data);
          this.getUserInfo();
          this.$store.dispatch('setLoginFlag', true);
        }
      });
    },
    /**
     * 获取用户信息
     */
    getUserInfo() {
      getUserInfoApi(this.$store.state.userInfo.id).then(res => {
        if (res.data.flag) {
          // 获取成功
          this.$store.dispatch('setUserInfo', res.data.data);
        }
      });
    }
  }
};
</script>

<style lang="less">
html,
body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  font-family: AlibabaPuHuiTi-2-55-Regular, 'Noto Sans';
  color: var(--primary-text);
  background-color: var(--default-bg);
  box-sizing: border-box;
}
// 选中时的背景色
::selection {
  color: #fff;
  background: var(--primary);
}
::-moz-selection {
  color: #fff;
  background-color: var(--primary);
}
#app {
  width: 100%;
  height: 100%;
}
.container {
  width: 100%;
  height: 100%;
  min-width: 1200px;
  position: relative;
}
.main-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  width: 100%;
  padding-bottom: 180px;
}
[class*=' ic-'],
[class^='ic-'] {
  font-family: 'fbookfont' !important;
  font-size: 14px;
  font-style: normal;
  speak: none;
  font-weight: 400;
  font-variant: normal;
  text-transform: none;
  line-height: 1;
  vertical-align: baseline;
  display: inline-block;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

// 路由过渡
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

// 动画帧
@keyframes fade-in {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
</style>

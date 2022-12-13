<template>
  <!-- 我的-消息组件 -->
  <div class="mine mine-mess">
    <div class="title-box">
      <span class="icon ic-bell"></span>
      <span class="text">我的消息</span>
      <div class="hr"></div>
    </div>
    <!-- 导航 -->
    <div class="nav-box">
      <div class="nav" :class="{ 'is-checked': navCheck === 'sys' }" @click.stop="handleNav('sys')">
        <span class="text">系统消息</span>
      </div>
      <div class="nav" :class="{ 'is-checked': navCheck === 'personal' }" @click.stop="handleNav('personal')">
        <span class="text">私信</span>
      </div>
    </div>
    <!-- 显示列表 -->
    <transition name="fade">
      <router-view />
    </transition>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // nav的选中项 sys | personal
      navCheck: 'sys'
    };
  },
  methods: {
    /**
     * nav的点击事件
     */
    handleNav(text) {
      this.navCheck = text;
      this.$router.push(`/mine/mess/${text}`);
    }
  },
  watch: {
    // 监听当前路由信息
    $route: {
      immediate: true,
      handler(val) {
        // console.log('route =>', val);
        this.navCheck = val.meta.nav;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.nav-box {
  width: 100%;
  height: 32px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  padding: 0 24px;
  margin-top: 24px;
  .nav {
    position: relative;
    cursor: pointer;
    user-select: none;
    height: 32px;
    line-height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    font-size: 14px;
    color: var(--primary-text);
    padding: 0 3px;
    transition: all 0.5s ease-in-out;
    & + .nav {
      margin-left: 24px;
    }
    &::after {
      transition: all 0.5s ease-in-out;
      content: '';
      display: block;
      width: 0%;
      height: 3px;
      border-radius: 1.5px;
      position: absolute;
      bottom: 0;
      background-color: var(--primary);
    }
    &.is-checked {
      color: var(--primary);
      &::after {
        width: 100%;
      }
    }
  }
}
</style>

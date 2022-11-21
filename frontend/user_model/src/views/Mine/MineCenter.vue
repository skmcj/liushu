<template>
  <!-- 我的-个人中心 -->
  <div class="mine mine-center">
    <div class="title-box">
      <span class="icon ic-mine-2"></span>
      <span class="text">个人中心</span>
      <div class="hr"></div>
    </div>
    <!-- 导航 -->
    <div class="nav-box">
      <div
        class="nav"
        v-for="(item, i) in navList"
        :key="'nav-item-' + i"
        :class="{ 'is-checked': navCheck === item.value }"
        @click.stop="handleNav(item)">
        <span class="text">{{ item.label }}</span>
      </div>
    </div>
    <div class="mine-center-main-box">
      <transition name="fade">
        <router-view />
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      navList: [
        { label: '个人信息', value: 'personal' },
        { label: '账号安全', value: 'account' },
        { label: '支付设置', value: 'pay' }
      ],
      navCheck: 'personal'
    };
  },
  methods: {
    handleNav(item) {
      this.navCheck = item.value;
      this.$router.push(`/mine/center/${item.value}`);
      // 获取指定订单
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

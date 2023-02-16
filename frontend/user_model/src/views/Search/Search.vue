<template>
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box">
      <div class="content">
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
        <div class="main-content">
          <transition name="fade">
            <router-view />
          </transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Wave from '@/components/Common/Wave';

export default {
  components: {
    Wave
  },
  data() {
    return {
      // all 综合 | book 图书 | shop 商家
      navCheck: 'book',
      navList: [
        { label: '图书', value: 'book' },
        { label: '商家', value: 'shop' }
      ]
    };
  },
  methods: {
    handleNav(item) {
      this.navCheck = item.value;
      this.$router.push(`/search/${item.value}`).catch(err => {});
    }
  }
};
</script>

<style lang="less" scoped>
.nav-box {
  height: 32px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  padding: 0 18px;
  margin: 0 12px;
  margin-top: 24px;
  padding-bottom: 5px;
  border-bottom: 1px solid #f1f2f3;
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
    font-size: 18px;
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
.content {
  border-radius: 12px;
  background-color: #fff;
  padding-bottom: 120px;
}
</style>

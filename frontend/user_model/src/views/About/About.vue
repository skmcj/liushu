<template>
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box">
      <div class="content">
        <div class="aside">
          <div class="item" :class="{ 'is-active': activeAside === 'profile' }" @click.stop="handleAside('profile')">
            本站简介
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'problem' }" @click.stop="handleAside('problem')">
            常见问题
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'our' }" @click.stop="handleAside('our')">
            联系我们
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'link' }" @click.stop="handleAside('link')">
            友情链接
          </div>
        </div>
        <div class="main-content">
          <div class="title-box">
            <span class="icon"></span>
            <span class="text">{{ title }}</span>
            <div class="hr"></div>
          </div>
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
      activeAside: 'profile',
      title: '本站简介'
    };
  },
  methods: {
    /**
     * 处理侧边栏
     */
    handleAside(text) {
      this.activeAside = text;
      this.$router.push(`/about/${text}`);
      switch (text) {
        case 'problem':
          this.title = '常见问题';
          break;
        case 'our':
          this.title = '联系我们';
          break;
        case 'link':
          this.title = '友情链接';
          break;
        default:
          this.title = '本站简介';
          break;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.content {
  display: flex;
  justify-content: space-between;
  .aside {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 18%;
    height: 394px;
    box-sizing: border-box;
    padding: 42px 24px 0 24px;
    border-radius: 12px;
    background-color: var(--white);
    .item {
      cursor: pointer;
      user-select: none;
      width: 100%;
      height: 48px;
      line-height: 48px;
      box-sizing: border-box;
      border-radius: 8px;
      font-size: 21px;
      text-align: center;
      color: var(--primary-text);
      transition: background-color 0.5s ease-in-out;
      &.is-active {
        background-color: #e3e5e7;
      }
      & + .item {
        margin-top: 12px;
      }
    }
  }
  .main-content {
    width: 81%;
    display: flex;
    flex-direction: column;
    border-radius: 12px;
    box-sizing: border-box;
    overflow: hidden;
    padding: 0 12px;
    background-color: var(--white);
    .title-box {
      position: relative;
      width: 100%;
      height: 72px;
      margin-top: 15px;
      display: flex;
      align-items: center;
      .icon {
        position: relative;
        width: 6px;
        height: 48px;
        border-radius: 3px;
        background-color: #e7e7e7;
        overflow: hidden;
        &::before {
          display: block;
          content: '';
          width: 100%;
          height: 75%;
          border-radius: 3px;
          background-color: var(--primary);
          position: absolute;
          top: 0;
          left: 0;
        }
      }
      .text {
        font-size: 27px;
        font-weight: bold;
        color: var(--primary-text);
        margin-left: 12px;
      }
      .hr {
        background-color: #dadada;
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        height: 1px;
        box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
      }
    }
  }
}
</style>

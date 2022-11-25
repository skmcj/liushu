<template>
  <div class="container">
    <!-- 导航条 -->
    <div class="head" :class="{ hide: scrollH > 88 }">
      <div class="links">
        <span class="link" @click="gotoStore">我是商家</span>
      </div>
      <Header :is-login="false" />
    </div>
    <!-- 主要显示内容 -->
    <div class="main">
      <transition name="fade-in-out">
        <router-view />
      </transition>
    </div>
    <!-- 页脚 -->
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header/Header';
import Footer from '@/components/Footer/Footer';

export default {
  components: {
    Header,
    Footer
  },
  data() {
    return {
      scrollH: 0
    };
  },
  mounted() {
    // 监听页面高度
    window.addEventListener('scroll', this.handleScroll);
  },
  destroyed() {
    // 销毁监听事件
    window.removeEventListener('scroll', this.handleScroll);
  },
  methods: {
    handleScroll() {
      // 获取当前滚动高度
      this.scrollH = document.body.scrollTop || document.documentElement.scrollTop || window.pageYOffset;
    },
    /**
     * 进入商家入口
     */
    gotoStore() {}
  }
};
</script>

<style lang="less" scoped>
.head {
  z-index: 800;
  width: 100%;
  padding-top: 32px;
  background-color: rgba(51, 51, 51, 0.7);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  transition: all 0.5s ease-in-out;
  &.hide {
    top: -150px;
  }
  .links {
    display: flex;
    align-items: center;
    position: absolute;
    top: 0;
    right: 120px;
    .link {
      opacity: 0.3;
      line-height: 32px;
      color: var(--white);
      font-size: 12px;
      user-select: none;
      cursor: pointer;
      transition: opacity 0.5s ease-in;
      &:hover {
        opacity: 0.7;
      }
      & + .link {
        margin-left: 12px;
      }
    }
  }
}
.main {
  width: 100%;
}
// 路由过渡
.fade-in-out-enter-active,
.fade-in-out-leave-active {
  transition: opacity 0.3s ease;
}

.fade-in-out-enter-from,
.fade-in-out-leave-to {
  opacity: 0;
}
</style>

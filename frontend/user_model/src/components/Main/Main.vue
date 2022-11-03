<template>
  <div class="container">
    <!-- 导航条 -->
    <div class="head" :class="{ hide: scrollH > 88 }">
      <Header />
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
    }
  }
};
</script>

<style lang="less" scoped>
.head {
  width: 100%;
  padding-top: 36px;
  background-color: rgba(51, 51, 51, 0.45);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  transition: all 0.5s ease-in-out;
  &.hide {
    top: -150px;
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

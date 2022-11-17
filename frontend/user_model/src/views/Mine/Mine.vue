<template>
  <!-- 我的组件 -->
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box">
      <div class="content">
        <div class="aside">
          <div class="item" :class="{ 'is-active': activeAside === 'center' }" @click.stop="handleAside('center')">
            个人中心
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'cart' }" @click.stop="handleAside('cart')">
            我的购物车
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'order' }" @click.stop="handleAside('order')">
            我的订单
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'mess' }" @click.stop="handleAside('mess')">
            我的消息
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'address' }" @click.stop="handleAside('address')">
            我的地址
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'coll' }" @click.stop="handleAside('coll')">
            我的收藏
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'comment' }" @click.stop="handleAside('comment')">
            我的评价
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
      activeAside: '',
      activeTitle: ''
    };
  },
  methods: {
    /**
     * 处理侧边栏
     */
    handleAside(text) {
      this.activeAside = text;
      this.$router.push(`/mine/${text}`);
      switch (text) {
        case 'center':
          this.activeTitle = '个人中心';
          break;
        case 'cart':
          this.activeTitle = '我的购物车';
          break;
        case 'order':
          this.activeTitle = '我的订单';
          break;
        case 'mess':
          this.activeTitle = '我的消息';
          break;
        case 'address':
          this.activeTitle = '我的地址';
          break;
        case 'coll':
          this.activeTitle = '我的收藏';
          break;
        case 'comment':
          this.activeTitle = '我的评价';
          break;
        default:
          this.activeTitle = '';
          break;
      }
    }
  },
  watch: {
    // 监听当前路由信息
    $route: {
      immediate: true,
      handler(val) {
        // console.log('route =>', val);
        this.activeAside = val.meta.aside;
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
    height: 516px;
    box-sizing: border-box;
    padding: 42px 24px 64px 24px;
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
  }
}
</style>

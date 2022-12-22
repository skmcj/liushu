<template>
  <div class="container">
    <!-- 导航条 -->
    <div class="head" :class="{ hide: scrollH > 88 }">
      <div class="links">
        <span class="link" @click="gotoStore">我是商家</span>
        <span v-if="$store.state.loginFlag" class="link" @click="logout">退出登录</span>
      </div>
      <Header :is-login="$store.state.loginFlag" :avatar-url="$store.state.userInfo.avatarUrl" />
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
import { mapState } from 'vuex';
import Header from '@/components/Header/Header';
import Footer from '@/components/Footer/Footer';
import { validateTokenApi, getUserInfoApi, getTIMUserSigApi } from '@/api/userApi';

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
  created() {
    this.initUserInfo();
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
    gotoStore() {},
    /**
     * 退出登录
     */
    logout() {
      this.$confirm('您是否确认要退出登录', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        confirmButtonClass: 'danger',
        type: 'error'
      })
        .then(() => {
          // // 清除store里的信息
          // this.$store.dispatch('setLoginFlag', false);
          // this.$store.dispatch('setUserInfo', {});
          // // 清除会话存储
          // window.sessionStorage.removeItem('userInfo');
          // window.sessionStorage.removeItem('loginFlag');
          // // 清除本地存储
          // window.localStorage.removeItem('userInfo');
          this.$clearLoginInfo();
          this.$showMsg('退出成功', {
            type: 'success',
            closeFunc: () => {
              if (this.$route.meta.rootLink === '/mine' || this.$route.name === 'settlement') {
                this.$router.replace('/');
              }
            }
          });
        })
        .catch(() => {});
    },
    /**
     * 初始化用户登录信息
     */
    initUserInfo() {
      // console.log('app init');
      const userInfo = this.$store.state.userInfo;
      // 查看本地是否有用户信息
      // 无，直接返回
      if (this.$isEmpty(userInfo)) return;
      // 有，则发送请求，验证token是否有效
      const token = this.$store.state.token;
      validateTokenApi(token).then(res => {
        if (res.data.flag) {
          // 有效，设置会话默认值
          // console.log('token => ', res.data);
          this.getUserInfo();
          this.getTIMUserSig();
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
    },
    /**
     * 获取TIM的UserSig
     */
    getTIMUserSig() {
      getTIMUserSigApi(this.$store.state.userInfo.id).then(res => {
        if (res.data.flag) {
          // userSig 获取成功
          let userSig = res.data.data;
          this.timLogin(this.$store.state.userInfo.id, userSig);
        }
      });
    },
    /**
     * 登录TIM
     */
    timLogin(userId, userSig) {
      this.tim
        .login({
          userID: userId,
          userSig: userSig
        })
        .then(() => {
          this.loading = false;
          this.$store.commit('toggleIsLogin', true);
          this.$store.commit('startComputeCurrent');
          // this.$store.commit('showMessage', { type: 'success', message: '登录成功' });
          this.$store.commit({
            type: 'GET_USER_INFO',
            userID: userId,
            userSig: userSig,
            sdkAppID: 1400779173
          });
          // this.$store.commit('showMessage', {
          //   type: 'success',
          //   message: '登录成功'
          // });
        })
        .catch(error => {
          this.$store.commit('showMessage', {
            message: 'TIM登录失败：' + error.message,
            type: 'error'
          });
        });
    }
  },
  computed: {
    ...mapState({
      currentUserProfile: state => state.user.currentUserProfile,
      currentConversation: state => state.conversation.currentConversation,
      isLogin: state => state.user.isLogin,
      isSDKReady: state => state.user.isSDKReady,
      isBusy: state => state.video.isBusy,
      userID: state => state.user.userID,
      userSig: state => state.user.userSig,
      sdkAppID: state => state.user.sdkAppID
    }),
    // 是否显示 Loading 状态
    showLoading() {
      return !this.isSDKReady;
    }
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
    flex-direction: row-reverse;
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
        margin-right: 12px;
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

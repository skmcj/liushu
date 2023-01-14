<template>
  <div class="container">
    <Aside
      width="196px"
      :cover-url="businessInfo.cover ? businessInfo.coverUrl : baseUrl + '/api/img/com-no-cover.jpg'"
      :title="businessInfo.storeName"
      :pathIndex="pathName" />
    <div class="right">
      <Header :returnFlag="blackFlag" :headTitle="title" :name="nickname" />
      <Main />
    </div>
  </div>
</template>

<script>
import Aside from '@/components/Aside/Aside.vue';
import Header from '@/components/Header/Header.vue';
import Main from '@/components/Main/Main.vue';
import { mapState } from 'vuex';
import { getStoreNameApi, validateEmployeeTokenApi, getStoreSigApi } from '@/api/shopApi';

export default {
  components: {
    Aside,
    Header,
    Main
  },
  data() {
    return {
      baseUrl: this.$staticUrl,
      coverUrl: '',
      storeName: '某某书店',
      blackFlag: false,
      title: '员工管理',
      pathName: '/employee',
      nickname: '**'
    };
  },
  created() {
    // 显示登录员工的姓名
    this.initBusinessInfo();
  },
  methods: {
    /**
     * 初始化商家信息
     */
    initBusinessInfo() {
      const employeeInfo = this.$store.state.employeeInfo;
      // 查看本地是否有员工信息
      // 无，直接返回
      if (this.$isEmpty(employeeInfo)) return;
      // 有，则发送请求，验证token
      const token = this.$store.state.token;
      validateEmployeeTokenApi(token).then(res => {
        if (res.data.flag) {
          // 有效，设置商家默认信息
          this.nickname = employeeInfo.name;
          this.getStoreMess(employeeInfo.storeId);
          this.getTIMStoreSig(employeeInfo.storeId);
          this.$store.dispatch('setLoginFlag', true);
        } else {
          // token已失效，返回登录页
          this.$showMsgs('Token已过期，请重新登录', {
            type: 'warning',
            closeFunc: () => {
              this.$router.replace('/login');
            }
          });
        }
      });
    },
    /**
     * 获取商家门脸图及店名
     */
    getStoreMess(storeId) {
      getStoreNameApi(storeId).then(
        res => {
          if (res.data.flag) {
            this.$store.dispatch('setBusinessInfo', res.data.data);
          } else {
            this.$showMsgs(res.data.msg, { type: 'error' });
          }
        },
        err => {
          console.log(err);
        }
      );
    },
    /**
     * 获取TIM的StoreSig
     */
    getTIMStoreSig(storeId) {
      getStoreSigApi(storeId).then(res => {
        if (res.data.flag) {
          // userSig 获取成功
          let storeSig = res.data.data;
          this.timLogin(storeId, storeSig);
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
      sdkAppID: state => state.user.sdkAppID,
      businessInfo: state => state.businessInfo,
      loginFlag: state => state.loginFlag
    }),
    // 是否显示 Loading 状态
    showLoading() {
      return !this.isSDKReady;
    }
  },
  watch: {
    // 监听当前路由信息
    $route: {
      immediate: true,
      handler(val) {
        this.title = val.meta.head;
        this.pathName = val.meta.asideItemIndex;
        this.blackFlag = val.meta.returnFlag;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.container {
  display: flex;
  width: 100%;
  height: 100%;
}

.right {
  flex: 1;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>

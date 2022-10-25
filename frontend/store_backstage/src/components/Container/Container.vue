<template>
  <div class="container">
    <Aside
      width="196px"
      :cover-url="
        $store.state.businessInfo.cover ? $store.state.businessInfo.cover : baseUrl + '/api/img/com-no-cover.jpg'
      "
      :title="$store.state.businessInfo.storeName"
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
import { getStoreNameApi } from '@/api/shopApi';

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
    const businessInfo = JSON.parse(window.localStorage.getItem('employeeInfo'));
    if (businessInfo) {
      this.nickname = businessInfo.name;
    }
    this.getStoreMess();
  },
  methods: {
    /**
     * 获取商家门脸图及店名
     */
    getStoreMess() {
      const businessInfo = JSON.parse(window.localStorage.getItem('employeeInfo'));
      getStoreNameApi(businessInfo.storeId).then(
        res => {
          if (res.data.flag) {
            this.$store.dispatch('setBusinessInfo', res.data.data);
          } else {
            this.$message({
              showClose: true,
              message: res.data.msg,
              type: 'error'
            });
          }
        },
        err => {
          console.log(err);
        }
      );
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

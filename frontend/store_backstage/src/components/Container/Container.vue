<template>
  <div class="container">
    <Aside width="196px" cover-url="https://s1.328888.xyz/2022/09/22/IwkOh.jpg" :pathIndex="pathName" />
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

export default {
  components: {
    Aside,
    Header,
    Main
  },
  data() {
    return {
      blackFlag: false,
      title: '员工管理',
      pathName: '/employee',
      nickname: '张三'
    };
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

<template>
  <div class="container">
    <Aside width="196px" />
    <div class="right">
      <Header :returnFlag="true" />
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

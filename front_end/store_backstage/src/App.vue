<template>
  <div id="app">
    <Container />
  </div>
</template>

<script>
import Container from '@/components/Container/Container.vue';

export default {
  components: {
    Container
  },
  data() {
    return {
      visible: false
    };
  }
};
</script>

<style lang="less">
html,
body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  background-color: var(--default-bg);
}
// 选中时的背景色
::selection {
  background: var(--primary);
}
::-moz-selection {
  background-color: var(--primary);
}
#app {
  width: 100%;
  height: 100%;
}
</style>

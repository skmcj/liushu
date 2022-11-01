<template>
  <div id="app">
    <nav></nav>
    <!-- <router-view/> -->
  </div>
</template>

<style lang="less">
html,
body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  // background-color: var(--default-bg);
}
// 选中时的背景色
::selection {
  color: #fff;
  background: var(--primary);
}
::-moz-selection {
  color: #fff;
  background-color: var(--primary);
}
#app {
  width: 100%;
  height: 100%;
}
// 路由过渡
// .fade-enter-active,
// .fade-leave-active {
//   transition: opacity 0.5s ease;
// }

// .fade-enter-from,
// .fade-leave-to {
//   opacity: 0;
// }
</style>

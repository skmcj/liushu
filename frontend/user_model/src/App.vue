<template>
  <div id="app">
    <transition name="fade">
      <router-view name="index" />
    </transition>
  </div>
</template>

<style lang="less">
@font-face {
  font-family: AlibabaPuHuiTi-2-55-Regular;
  src: url(https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-55-Regular/AlibabaPuHuiTi-2-55-Regular.eot)
      format('embedded-opentype'),
    url(https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-55-Regular/AlibabaPuHuiTi-2-55-Regular.otf)
      format('opentype'),
    url(https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-55-Regular/AlibabaPuHuiTi-2-55-Regular.ttf)
      format('TrueType'),
    url(https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-55-Regular/AlibabaPuHuiTi-2-55-Regular.woff)
      format('woff'),
    url(https://puhuiti.oss-cn-hangzhou.aliyuncs.com/AlibabaPuHuiTi-2/AlibabaPuHuiTi-2-55-Regular/AlibabaPuHuiTi-2-55-Regular.woff2)
      format('woff2');
}
html,
body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  font-family: AlibabaPuHuiTi-2-55-Regular, 'Noto Sans';
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
.container {
  width: 100%;
  height: 100%;
  min-width: 1200px;
  position: relative;
}
.main-box {
  width: 100%;
  padding-bottom: 180px;
}
[class*=' ic-'],
[class^='ic-'] {
  font-family: 'fbookfont' !important;
  font-size: 14px;
  font-style: normal;
  speak: none;
  font-weight: 400;
  font-variant: normal;
  text-transform: none;
  line-height: 1;
  vertical-align: baseline;
  display: inline-block;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

// 路由过渡
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

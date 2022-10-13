<template>
  <div class="tool-box">
    <div tabindex="-1" class="tool-item refresh" v-if="refresh" @click="refresh" title="刷新">
      <i class="ic-change"></i>
    </div>
    <slot></slot>
  </div>
</template>

<script>
export default {
  props: {
    refresh: Function
  }
};
</script>

<style lang="less" scoped>
.tool-box {
  box-sizing: border-box;
  padding: 16px 18px;
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
  border-radius: 5px 0 0 5px;
  opacity: 0;
  transition: all 500ms ease-in-out;
  [class^='ic-'] {
    font-family: 'fbookfont' !important;
    font-size: 16px;
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
  .tool-item {
    color: var(--secondary-font);
    &.refresh {
      &:hover {
        color: var(--primary);
      }
      &:focus {
        animation: rotate-once 500ms ease;
      }
      &:active {
        animation: none;
      }
    }
  }
  &:hover {
    opacity: 1;
  }
}
@keyframes rotate-once {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>

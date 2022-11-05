<template>
  <div class="waves" :style="varStyle">
    <svg
      class="waves-content"
      xmlns="http://www.w3.org/2000/svg"
      xmlns:xlink="http://www.w3.org/1999/xlink"
      viewBox="0 24 150 28"
      preserveAspectRatio="none"
      shape-rendering="auto">
      <defs>
        <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"></path>
      </defs>
      <g class="parallax">
        <template v-if="num >= 0 && num < 6">
          <use v-for="index in num" :key="index" xlink:href="#gentle-wave" x="48" :y="(5 - index) * 2"></use>
        </template>
      </g>
    </svg>
  </div>
</template>

<script>
export default {
  props: {
    // 波颜色
    colorHex: {
      type: String,
      default: '#fff'
    },
    // 波动数量
    num: {
      type: Number,
      default: 3
    },
    // 波高度
    height: {
      type: Number,
      default: 108
    }
  },
  computed: {
    varStyle() {
      let style = {};
      style['--sk-wave-color'] = this.colorHex;
      style['--sk-wave-height'] = this.height + 'px';
      return style;
    }
  }
};
</script>

<style lang="less" scoped>
.waves {
  width: 100%;
  position: absolute;
  bottom: 0;
  -webkit-tap-highlight-color: transparent;
  .waves-content {
    width: 100%;
    height: var(--sk-wave-height);
    margin-bottom: -0.6875rem;
    position: relative;
    .parallax {
      & > use {
        -webkit-animation: wave 25s cubic-bezier(0.55, 0.5, 0.45, 0.5) infinite;
        animation: wave 25s cubic-bezier(0.55, 0.5, 0.45, 0.5) infinite;
        &:nth-child(1) {
          -webkit-animation-delay: -2s;
          animation-delay: -2s;
          -webkit-animation-duration: 7s;
          animation-duration: 7s;
          fill: var(--sk-wave-color);
        }
        &:nth-child(2) {
          -webkit-animation-delay: -3s;
          animation-delay: -3s;
          -webkit-animation-duration: 10s;
          animation-duration: 10s;
          fill: var(--sk-wave-color);
          opacity: 0.7;
        }
        &:nth-child(3) {
          -webkit-animation-delay: -4s;
          animation-delay: -4s;
          -webkit-animation-duration: 13s;
          animation-duration: 13s;
          fill: var(--sk-wave-color);
          opacity: 0.5;
        }
        &:nth-child(4) {
          -webkit-animation-delay: -5s;
          animation-delay: -5s;
          -webkit-animation-duration: 16s;
          animation-duration: 16s;
          fill: var(--sk-wave-color);
          opacity: 0.3;
        }
        &:nth-child(5) {
          -webkit-animation-delay: -6s;
          animation-delay: -6s;
          -webkit-animation-duration: 24s;
          animation-duration: 24s;
          fill: var(--sk-wave-color);
          opacity: 0.1;
        }
      }
    }
  }
}
@keyframes wave {
  0% {
    transform: translate3d(-90px, 0, 0);
  }

  100% {
    transform: translate3d(85px, 0, 0);
  }
}
</style>

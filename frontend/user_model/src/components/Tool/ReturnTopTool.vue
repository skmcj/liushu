<template>
  <div class="sk-tool-return-top" v-if="visible" @click.stop="clickTop">
    <i class="sk-tool-icon ic-more-1"></i>
    <span class="sk-tool-text">顶部</span>
  </div>
</template>

<script>
import { throttle } from '@/utils/throttle-debounce';

const cubic = value => Math.pow(value, 3);
const easeInOutCubic = value => (value < 0.5 ? cubic(value * 2) / 2 : 1 - cubic((1 - value) * 2) / 2);

export default {
  props: {
    handleClick: Function,
    visibilityHeight: {
      type: Number,
      default: 300
    },
    target: [String]
  },
  data() {
    return {
      el: null,
      container: null,
      visible: false
    };
  },
  mounted() {
    this.init();
    this.throttledScrollHandler = throttle(300, this.onScroll);
    this.container.addEventListener('scroll', this.throttledScrollHandler);
  },
  methods: {
    init() {
      this.container = document;
      this.el = document.documentElement;
      if (this.target) {
        this.el = document.querySelector(this.target);
        if (!this.el) {
          throw new Error(`target is not existed: ${this.target}`);
        }
        this.container = this.el;
      }
    },
    clickTop() {
      if (this.handleClick) {
        this.handleClick();
        return;
      }
      this.scrollToTop();
    },
    onScroll() {
      const scrollTop = this.el.scrollTop;
      this.visible = scrollTop >= this.visibilityHeight;
    },
    scrollToTop() {
      const el = this.el;
      const beginTime = Date.now();
      const beginValue = el.scrollTop;
      const rAF = window.requestAnimationFrame || (func => setTimeout(func, 16));
      const frameFunc = () => {
        const progress = (Date.now() - beginTime) / 500;
        if (progress < 1) {
          el.scrollTop = beginValue * (1 - easeInOutCubic(progress));
          rAF(frameFunc);
        } else {
          el.scrollTop = 0;
        }
      };
      rAF(frameFunc);
    }
  },
  beforeDestroy() {
    this.container.removeEventListener('scroll', this.throttledScrollHandler);
  }
};
</script>

<style lang="less" scoped>
.sk-tool-return-top {
  animation: skReturnTopAppear 0.8s ease-in-out;
  transition: all 0.8s ease-in-out;
  cursor: pointer;
  user-select: none;
  width: var(--sk-tool-width);
  height: var(--sk-tool-width);
  border-radius: 5px;
  border: 1px solid #cbcbcb;
  background-color: #fff;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  .sk-tool-icon {
    transform: rotate(-90deg);
  }
  .sk-tool-text {
    font-size: 12px;
    color: #3d3d3d;
    margin-top: 3px;
  }
}
@keyframes skReturnTopAppear {
  0% {
    transform: translateX(100%);
    opacity: 0;
  }
  100% {
    transform: translateX(0);
    opacity: 1;
  }
}
@keyframes skReturnTopDisappear {
  0% {
    transform: translateX(100%);
    opacity: 0;
  }
  100% {
    transform: translateX(0);
    opacity: 1;
  }
}
</style>

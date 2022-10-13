<template>
  <div class="sk-loading-book-box" :class="'tip-' + direction" :style="style">
    <div class="sk-loading-book">
      <div class="sk-loading-book__inner">
        <div class="sk-loading-book__left"></div>
        <div class="sk-loading-book__middle"></div>
        <div class="sk-loading-book__right"></div>
      </div>
      <ul class="sk-loading-pages">
        <li class="sk-loading-page" v-for="index of 18" :key="'sk-loading-book-page-' + (index + 1)"></li>
      </ul>
    </div>
    <div class="sk-loading-book-tip">
      <slot name="tip">{{ tip }}</slot>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // 书本的厚度
    thickness: {
      type: Number,
      default: 32
    },
    // 书本的颜色
    color: String,
    // 纸张厚度
    paperThickness: {
      type: Number,
      default: 4
    },
    // 动画时长
    duration: {
      type: Number,
      default: 6.8
    },
    // tip方向
    direction: {
      type: String,
      default: 'bottom'
    },
    tip: String
  },
  computed: {
    style() {
      let style = {};
      style['--sk-thickness'] = this.thickness + 'px';
      style['--sk-book-color'] = this.color;
      style['--sk-paper-thickness'] = this.paperThickness + 'px';
      style['--sk-book-duration'] = this.duration + 's';
      return style;
    }
  }
};
</script>

<style lang="less" scoped>
.sk-loading-book-box {
  --margin-l: calc(var(--sk-thickness) * 2.8125);
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  &.tip-left {
    flex-direction: row-reverse;
    .sk-loading-book-tip {
      margin-right: calc(var(--margin-l) + 12px);
    }
  }
  &.tip-right {
    flex-direction: row;
    .sk-loading-book-tip {
      margin-left: calc(var(--margin-l) + 12px);
    }
  }
  &.tip-top {
    flex-direction: column-reverse;
    .sk-loading-book-tip {
      margin-bottom: calc((var(--sk-thickness) * 1.6875 - var(--sk-paper-thickness) * 2.5) * 1.5 + 12px);
    }
  }
  &.tip-bottom {
    flex-direction: column;
    .sk-loading-book-tip {
      margin-top: 14px;
    }
  }
  .sk-loading-book-tip {
    font-size: 16px;
    color: #999999;
  }
}
.sk-loading-book {
  --sk-book-width: var(--sk-thickness);
  --sk-book-height: calc(var(--sk-thickness) * 0.375);
  --sk-bk-lo: calc(var(--sk-paper-thickness) / 2);
  --sk-bk-ro: calc(var(--sk-thickness) - (var(--sk-paper-thickness) / 2));
  --sk-book-page-length: calc(var(--sk-book-width) * 1.875);
  --sk-book-inside-page-width: calc(var(--sk-book-width) * 1.5);
  --sk-book-page-step-w: calc(var(--sk-book-width) * 0.6875 - var(--sk-paper-thickness));
  width: var(--sk-book-width);
  height: var(--sk-book-height);
  margin-top: var(--sk-book-width);
  box-sizing: border-box;
  position: relative;
  zoom: 1.5;
  .sk-loading-book__inner {
    width: var(--sk-book-width);
    height: var(--sk-book-height);
    box-sizing: inherit;
    position: relative;
    transform-origin: var(--sk-bk-lo) var(--sk-bk-lo);
    transform: rotateZ(-90deg);
    -webkit-animation: book var(--sk-book-duration) ease infinite;
    animation: book var(--sk-book-duration) ease infinite;
    .sk-loading-book__left,
    .sk-loading-book__right {
      box-sizing: inherit;
      width: var(--sk-book-page-length);
      height: var(--sk-paper-thickness);
      top: 0;
      border-radius: var(--sk-bk-lo);
      background: var(--sk-book-color);
      position: absolute;
      &::before {
        content: '';
        box-sizing: inherit;
        width: var(--sk-book-inside-page-width);
        height: var(--sk-paper-thickness);
        border-radius: var(--sk-bk-lo);
        background: inherit;
        position: absolute;
        top: calc((var(--sk-book-width) - var(--sk-paper-thickness) * 3) / -2);
        left: calc((var(--sk-book-page-length) - var(--sk-book-inside-page-width)) / 2);
      }
    }
    .sk-loading-book__left {
      right: calc((var(--sk-book-page-length) - var(--sk-paper-thickness)) / 2);
      transform-origin: calc(var(--sk-book-page-length) - (var(--sk-paper-thickness) / 2)) var(--sk-bk-lo);
      transform: rotateZ(90deg);
      -webkit-animation: left var(--sk-book-duration) ease infinite;
      animation: left var(--sk-book-duration) ease infinite;
    }
    .sk-loading-book__right {
      left: calc((var(--sk-book-page-length) - var(--sk-paper-thickness)) / 2);
      transform-origin: var(--sk-bk-lo) var(--sk-bk-lo);
      transform: rotateZ(-90deg);
      -webkit-animation: right var(--sk-book-duration) ease infinite;
      animation: right var(--sk-book-duration) ease infinite;
    }
    .sk-loading-book__middle {
      width: var(--sk-book-width);
      height: var(--sk-book-height);
      box-sizing: inherit;
      border: var(--sk-paper-thickness) solid var(--sk-book-color);
      border-top: 0;
      border-radius: 0 0 calc(var(--sk-book-height) * 0.75) calc(var(--sk-book-height) * 0.75);
      transform: translateY(var(--sk-bk-lo));
    }
  }
  .sk-loading-pages {
    margin: 0;
    padding: 0;
    list-style: none;
    position: absolute;
    left: 50%;
    top: 0;
    .sk-loading-page {
      width: var(--sk-book-inside-page-width);
      height: var(--sk-paper-thickness);
      border-radius: var(--sk-bk-lo);
      transform-origin: 100% var(--sk-bk-lo);
      right: 0;
      top: calc((var(--sk-book-width) - var(--sk-paper-thickness) * 3) / -2);
      position: absolute;
      background-color: var(--sk-book-color);
      transform: rotateZ(0deg) translateX(calc(var(--sk-book-page-step-w) * -1));
      animation-duration: var(--sk-book-duration);
      animation-timing-function: ease;
      animation-iteration-count: infinite;
    }
  }
  // 整本书的动画
  @keyframes book {
    4% {
      transform: rotateZ(-90deg);
    }
    10%,
    40% {
      transform: rotateZ(0deg);
      transform-origin: var(--sk-bk-lo) var(--sk-bk-lo);
    }
    40.01%,
    59.99% {
      transform-origin: var(--sk-bk-ro) var(--sk-bk-lo);
    }
    46%,
    54% {
      transform: rotateZ(90deg);
    }
    60%,
    90% {
      transform: rotateZ(0deg);
      transform-origin: var(--sk-bk-lo) var(--sk-bk-lo);
    }
    96% {
      transform: rotateZ(-90deg);
    }
  }
  // 左边书页的动画
  @keyframes left {
    4% {
      transform: rotateZ(90deg);
    }
    10%,
    40% {
      transform: rotateZ(0deg);
    }
    46%,
    54% {
      transform: rotateZ(90deg);
    }
    60%,
    90% {
      transform: rotateZ(0deg);
    }
    96% {
      transform: rotateZ(90deg);
    }
  }
  // 右边书页的动画
  @keyframes right {
    4% {
      transform: rotateZ(-90deg);
    }
    10%,
    40% {
      transform: rotateZ(0deg);
    }
    46%,
    54% {
      transform: rotateZ(-90deg);
    }
    60%,
    90% {
      transform: rotateZ(0deg);
    }
    96% {
      transform: rotateZ(-90deg);
    }
  }
  // 书页的动画
  .pageframe(@name, @i) {
    @delay: @i * 1.86;
    @delayAfter: @i * 1.74;
    @p1: 4 + @delay;
    @p2: 13 + @delayAfter;
    @p3: 54 + @delay;
    @p4: 63 + @delayAfter;
    @ps1: e('@{p1}%');
    @ps2: e('@{p2}%');
    @ps3: e('@{p3}%');
    @ps4: e('@{p4}%');
    @keyframes @name {
      @{ps1} {
        transform: rotateZ(0deg) translateX(calc(var(--sk-book-page-step-w) * -1));
      }
      @{ps2},
      @{ps3} {
        transform: rotateZ(180deg) translateX(calc(var(--sk-book-page-step-w) * -1));
      }
      @{ps4} {
        transform: rotateZ(0deg) translateX(calc(var(--sk-book-page-step-w) * -1));
      }
    }
  }
  .pagesframes(@n, @i: 1) when (@i =< @n) {
    .sk-loading-page:nth-child(@{i}) {
      .pageframe(~'frame-page-@{i}', @i);
      -webkit-animation-name: ~'frame-page-@{i}';
      animation-name: ~'frame-page-@{i}';
    }
    .pagesframes(@n, (@i + 1));
  }
  .pagesframes(18);
}
</style>

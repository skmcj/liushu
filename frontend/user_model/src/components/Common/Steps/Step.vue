<template>
  <div class="sk-step" :class="`is-${currentStatus}`">
    <div v-if="titleDirection === 'top'" :class="'sk-step-head ' + titleDirection" :style="headStyle">
      <div class="sk-step-title">
        <slot name="title">{{ title }}</slot>
      </div>
      <div class="sk-step-desc-content">
        <slot name="desc">{{ desc }}</slot>
      </div>
    </div>
    <div class="sk-step-main" :class="{ 'is-solid': internalSolid }">
      <div :class="'sk-step-icon ' + titleDirection">
        <template v-if="!icon">
          <div v-if="internalMode === 'normal'" class="sk-step-num">{{ index + 1 }}</div>
          <div v-if="internalMode === 'simple'" class="sk-step-circle"></div>
          <div v-if="internalMode === 'elegant'" class="sk-step-num">
            <template v-if="currentStatus !== 'success' && currentStatus !== 'fail'">{{ index + 1 }}</template>
            <template v-else>
              <i :class="currentStatus === 'fail' ? 'ic-close' : 'ic-select-1'"></i>
            </template>
          </div>
        </template>
        <template v-else>
          <div class="sk-step-icon__inner">
            <i :class="icon"></i>
          </div>
        </template>
        <div v-if="titleDirection !== 'top' && titleDirection !== 'bottom'" class="sk-step-title">
          <slot name="title">{{ title }}</slot>
        </div>
      </div>
      <div class="sk-step-line">
        <i class="sk-step-line-inner"></i>
      </div>
    </div>
    <div v-if="titleDirection !== 'top' && titleDirection !== 'bottom'" class="sk-step-head">
      <div class="sk-step-desc-content">
        <slot name="desc">{{ desc }}</slot>
      </div>
    </div>
    <div v-if="titleDirection === 'bottom'" :class="'sk-step-head ' + titleDirection">
      <div class="sk-step-title">
        <slot name="title">{{ title }}</slot>
      </div>
      <div class="sk-step-desc-content">
        <slot name="desc">{{ desc }}</slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // finish: Boolean,
    title: String,
    titleAlgin: String,
    icon: String,
    desc: String,
    // wait / process / finish / fail / success
    status: String,
    // simple | normal | elegant
    mode: String,
    // 图标类型是否实心
    solid: Boolean
  },
  data() {
    return {
      index: -1,
      titleAlginV: '',
      internalStatus: '',
      headStyle: {}
    };
  },
  beforeCreate() {
    this.$parent.steps.push(this);
  },
  created() {
    this.initProps();
  },
  beforeDestroy() {
    const steps = this.$parent.steps;
    const index = steps.indexOf(this);
    if (index >= 0) {
      steps.splice(index, 1);
    }
  },
  methods: {
    initProps() {
      this.titleAlginV = this.titleAlgin ? this.titleAlgin : this.$parent.titleAlgin;
    },
    updateStatus(val) {
      // const prevChild = this.$parent.$children[this.index - 1];
      if (val > this.index) {
        this.internalStatus = this.$parent.finishStatus;
      } else if (val === this.index && this.prevStatus !== 'error') {
        this.internalStatus = this.$parent.processStatus;
      } else {
        this.internalStatus = 'wait';
      }
      // if (prevChild) prevChild.calcProgress(this.internalStatus);
    },
    getHeadStyle() {
      let style = {};
      let sonIndex = this.titleDirection === 'top' ? 1 : 0;
      const parent = this.$parent;
      const firstChild = parent.$children[0];
      const mainIcon = firstChild.$el.children[sonIndex].firstChild;
      let headWidth = (parent.$el.offsetWidth - mainIcon.offsetWidth - 20) / (parent.steps.length - 1);
      style.width = `${headWidth}px`;
      style.transform = `translateX(${mainIcon.offsetWidth / 2 + 10}px)`;
      if (this.isLast) {
        style.transform = `translateX(-${headWidth / 2 - mainIcon.offsetWidth - 20}px)`;
      }
      return style;
    }
  },
  computed: {
    internalMode() {
      return this.mode || this.$parent.mode;
    },
    internalSolid() {
      return this.solid || this.$parent.solid;
    },
    currentStatus() {
      return this.status || this.internalStatus;
    },
    prevStatus() {
      const prevStep = this.$parent.steps[this.index - 1];
      return prevStep ? prevStep.currentStatus : 'wait';
    },
    titleDirection() {
      let cn = '';
      switch (this.titleAlginV) {
        case 'left':
          cn = 'left';
          break;
        case 'right':
          cn = 'right';
          break;
        case 'top':
          cn = 'top';
          break;
        case 'bottom':
          cn = 'bottom';
          break;
        default:
          cn = 'bottom';
          break;
      }
      return cn;
    },
    space() {
      return this.$parent.space;
    },
    isLast() {
      const parent = this.$parent;
      return parent.steps[parent.steps.length - 1] === this;
    },
    // style() {
    //   const style = {};
    //   const parent = this.$parent;
    //   const len = parent.steps.length;
    //   const space =
    //     typeof this.space === 'number' ? this.space + 'px' : this.space ? this.space : 100 / (len - 1) + '%';
    //   // style.flexBasis = space;
    //   style.flexGrow = 1;
    //   if (this.isLast) {
    //     // style.maxWidth = 100 / len + '%';
    //     style.flexGrow = 0;
    //   }
    //   return style;
    // },
    isFinish() {
      const parent = this.$parent;
      let cn = parent.active > this.index ? 'is-finish' : '';
      return this.finish ? 'is-finish' : cn;
    }
  },
  mounted() {
    const unwatch = this.$watch('index', val => {
      this.$watch('$parent.active', this.updateStatus, { immediate: true });
      this.$watch(
        '$parent.processStatus',
        () => {
          const activeIndex = this.$parent.active;
          this.updateStatus(activeIndex);
        },
        { immediate: true }
      );
      unwatch();
    });
    // 获取计算headStyle的样式
    this.headStyle = this.getHeadStyle();
  }
};
</script>

<style lang="less" scoped>
.sk-step {
  position: relative;
  user-select: none;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  &.is-wait {
    color: #99a2aa;
    .sk-step-main {
      color: #99a2aa;
      .sk-step-icon {
        .sk-step-num {
          color: #99a2aa;
          border-color: #99a2aa;
        }
      }
      &.is-solid {
        .sk-step-icon {
          .sk-step-num {
            color: #ffffff;
            border-color: #99a2aa;
            background-color: #99a2aa;
          }
        }
      }
    }
  }
  &.is-process {
    color: #474a4d;
    .sk-step-main {
      color: #474a4d;
      .sk-step-icon {
        .sk-step-num {
          color: #474a4d;
          border-color: #474a4d;
        }
      }
      &.is-solid {
        .sk-step-icon {
          .sk-step-num {
            color: #ffffff;
            border-color: #474a4d;
            background-color: #474a4d;
          }
        }
      }
    }
  }
  &.is-finish {
    color: #83ccd2;
    .sk-step-main {
      color: #83ccd2;
      .sk-step-icon {
        .sk-step-num {
          color: #83ccd2;
          border-color: #83ccd2;
        }
      }
      &.is-solid {
        .sk-step-icon {
          .sk-step-num {
            color: #ffffff;
            border-color: #83ccd2;
            background-color: #83ccd2;
          }
        }
      }
      .sk-step-line {
        .sk-step-line-inner {
          &::before {
            width: 100%;
          }
        }
      }
    }
  }
  &.is-success {
    color: #98d98e;
    .sk-step-main {
      color: #98d98e;
      .sk-step-icon {
        .sk-step-num {
          color: #98d98e;
          border-color: #98d98e;
        }
      }
      &.is-solid {
        .sk-step-icon {
          .sk-step-num {
            color: #98d98e;
            border-color: #98d98e;
          }
        }
      }
      .sk-step-line {
        .sk-step-line-inner {
          &::before {
            background-color: #98d98e;
            width: 100%;
          }
        }
      }
    }
  }
  &.is-fail {
    color: #f56c6c;
    .sk-step-main {
      color: #f56c6c;
      .sk-step-icon {
        .sk-step-num {
          color: #f56c6c;
          border-color: #f56c6c;
        }
      }
      &.is-solid {
        .sk-step-icon {
          .sk-step-num {
            color: #ffffff;
            border-color: #f56c6c;
            background-color: #f56c6c;
          }
        }
      }
    }
  }
  .sk-step-head {
    text-align: center;
    min-width: 100%;
    position: absolute;
    bottom: 100%;
    left: -50%;
    transform: translateX(22.5px);
    &.top {
      flex-direction: column-reverse;
    }
    &.bottom {
      flex-direction: column;
    }
    .sk-step-title {
      font-size: 16px;
      line-height: 25px;
    }
    .sk-step-desc-content {
      font-size: 12px;
      line-height: 20px;
    }
  }
  .sk-step-main {
    width: 100%;
    display: flex;
    color: #99a2aa;
    .sk-step-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 10px;
      &.left {
        flex-direction: row-reverse;
        .sk-step-title {
          margin-right: 10px;
        }
      }
      &.right {
        flex-direction: row;
        .sk-step-title {
          margin-left: 10px;
        }
      }
      &.top {
        flex-direction: column-reverse;
      }
      &.bottom {
        flex-direction: column;
      }
      .sk-step-num,
      .sk-step-circle {
        display: flex;
        align-items: center;
        justify-content: center;
        border: solid 2px #99a2aa;
        border-radius: 50%;
        box-sizing: border-box;
      }
      .sk-step-num {
        width: 25px;
        height: 25px;
        [class^='ic-'] {
          font-size: 12px;
        }
      }
      .sk-step-icon__inner {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 25px;
        height: 25px;
        font-size: 25px;
      }
      .sk-step-circle {
        width: 18px;
        height: 18px;
      }
      &.is-solid {
        .sk-step-num,
        .sk-step-circle {
          color: #ffffff;
          background-color: #99a2aa;
        }
        .sk-step-icon__inner {
          color: #ffffff;
        }
      }
      .sk-step-title {
        font-size: 16px;
        line-height: 25px;
      }
      .sk-step-desc-content {
        font-size: 12px;
        line-height: 20px;
      }
    }
    .sk-step-line {
      flex-grow: 1;
      height: 25px;
      display: flex;
      align-items: center;
      .sk-step-line-inner {
        position: relative;
        overflow: hidden;
        width: 100%;
        height: 2px;
        border-radius: 1px;
        background-color: #e5e9ef;
        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          width: 0%;
          height: 100%;
          transition: all 300ms linear;
          background-color: #83ccd2;
        }
      }
    }
  }
  .sk-step-desc {
    display: flex;
    flex-direction: column;
  }
  &:last-child {
    flex-grow: 0;
    .sk-step-line {
      display: none;
    }
  }
}
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
</style>

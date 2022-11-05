<template>
  <div class="sk-content-block">
    <div class="sk-content-block-title-box">
      <div class="sk-content-block__title">{{ title }}</div>
      <div class="sk-content-block-tools">
        <slot name="tools"></slot>
        <div v-if="changeTool" class="sk-content-block__tool" @click="rotate">
          <i class="sk-content-block__icon ic-change" :style="rotateStyle"></i>
          <span class="sk-content-block__text">换一换</span>
        </div>
        <div v-if="moreTool" class="sk-content-block__tool" @click="handleMore">
          <span class="sk-content-block__text">更多</span>
          <i class="sk-content-block__icon ic-more-1"></i>
        </div>
      </div>
    </div>
    <slot></slot>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: null
    },
    // 换一换
    changeTool: {
      type: Boolean,
      default: true
    },
    handleChange: {
      type: Function,
      default: () => {}
    },
    // 更多
    moreTool: {
      type: Boolean,
      default: false
    },
    handleMore: {
      type: Function,
      default: () => {}
    }
  },
  data() {
    return {
      // 换一换按钮的旋转角
      deg: 0
    };
  },
  methods: {
    // 换一换
    rotate() {
      this.deg -= 360;
      this.handleChange();
    }
  },
  computed: {
    rotateStyle() {
      return {
        transform: `rotate(${this.deg}deg)`
      };
    }
  }
};
</script>

<style lang="less" scoped>
.sk-content-block {
  box-sizing: border-box;
  padding: 24px 36px 36px 36px;
  position: relative;
  width: 100%;
  border-radius: 12px;
  background-color: #fff;
  .sk-content-block-title-box {
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
    .sk-content-block__title {
      font-family: 'Source Han Sans';
      font-size: 27px;
      font-weight: bold;
      color: #3d3d3d;
    }
    .sk-content-block-tools {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      margin-right: 15px;
      .sk-content-block__tool {
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        box-sizing: border-box;
        padding: 5px 12px;
        background-color: #fff;
        border: 1px solid #dfdfdf;
        border-radius: 5px;
        box-shadow: 0 0 5px 2px rgba(190, 194, 188, 0.1) inset;
        & + .sk-content-block__tool {
          margin-left: 12px;
        }
        .sk-content-block__icon {
          font-size: 16px;
          color: #3d3d3d;
          transition: transform 0.8s ease;
          & + .sk-content-block__text {
            margin-left: 5px;
          }
          &.ic-more-1 {
            font-size: 12px;
          }
        }
        .sk-content-block__text {
          user-select: none;
          font-size: 12px;
          color: #3d3d3d;
          & + .sk-content-block__icon {
            margin-left: 5px;
          }
        }
        &:active {
          .sk-content-block__text {
            transform: scale(0.95) translateZ(0);
          }
        }
      }
    }
  }
}
</style>

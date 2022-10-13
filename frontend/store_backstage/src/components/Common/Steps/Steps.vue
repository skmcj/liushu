<template>
  <div class="sk-steps">
    <slot></slot>
  </div>
</template>

<script>
export default {
  props: {
    space: [Number, String],
    mode: {
      // simple | normal | elegant
      type: String,
      default: 'normal'
    },
    active: Number,
    titleAlgin: String,
    // 完成的状态
    finishStatus: {
      type: String,
      default: 'finish'
    },
    // 当前的状态
    processStatus: {
      type: String,
      default: 'process'
    },
    // 图标类型是否实心
    solid: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      steps: [],
      stepOffset: 0
    };
  },
  watch: {
    active(newVal, oldVal) {
      this.$emit('change', newVal, oldVal);
    },
    steps(steps) {
      steps.forEach((child, index) => {
        child.index = index;
      });
    }
  }
};
</script>

<style lang="less" scoped>
.sk-steps {
  width: 100%;
  display: flex;
  justify-content: space-around;
}
</style>

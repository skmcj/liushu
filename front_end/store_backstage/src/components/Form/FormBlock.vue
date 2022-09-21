<template>
  <div class="form-block">
    <div class="title">
      <span class="left-hr"></span>
      <div class="text">{{ title }}</div>
      <span class="right-hr"></span>
    </div>
    <slot></slot>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      default: '功能标题',
      type: String
    }
  }
};
</script>

<style lang="less" scoped>
.form-block {
  width: 100%;
  .title {
    width: 100%;
    margin-bottom: 36px;
    display: flex;
    align-items: center;
    .left-hr {
      width: 5%;
      height: 1px;
      background-color: var(--secondary-border);
    }
    .right-hr {
      flex: 1;
      height: 1px;
      background-color: var(--secondary-border);
    }
    .text {
      padding: 3px 8px;
      font-size: 14px;
      color: var(--primary-font);
    }
  }
}
</style>

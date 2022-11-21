<template>
  <!-- 我的-购物车组件 -->
  <div class="mine mine-cart">
    <div class="title-box">
      <span class="icon ic-address-books"></span>
      <span class="text">我的购物车</span>
      <div class="hr"></div>
    </div>
    <div class="mine-cart-main-box">
      <div class="checkbox-all">
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange"
          >全选</el-checkbox
        >
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 全选按钮半选状态
      isIndeterminate: false,
      // 是否全选
      checkAll: false
    };
  },
  methods: {
    /**
     * 全选改变
     */
    handleCheckAllChange(val) {
      console.log('check all =>', val);
    }
  }
};
</script>

<style lang="less" scoped>
.mine-cart-main-box {
  margin-top: 18px;
  width: 100%;
  display: flex;
  flex-direction: column;
  .checkbox-all {
    width: 100%;
    height: 36px;
    box-sizing: border-box;
    padding: 0 12px;
    display: flex;
    align-items: center;
  }
}
</style>

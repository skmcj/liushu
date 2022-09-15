<template>
  <div class="main">
    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="less" scoped>
.main {
  width: 100%;
  flex: 1;
  overflow-y: scroll;
  &::-webkit-scrollbar {
    display: none;
  }
}
.content {
  margin: 42px;
}
</style>

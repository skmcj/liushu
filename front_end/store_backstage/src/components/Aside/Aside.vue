<template>
  <div class="aside">
    <div class="cover">
      <img alt="门脸图片" src="@/assets/images/bookshop_cover.jpg" />
      <p class="title">片刻书店</p>
    </div>
    <div class="menu">
      <el-menu default-active="1" :router="true">
        <el-menu-item index="1" :route="{ path: '/employee' }">
          <i class="icon fbookfont ic-staff"></i>
          <span slot="title">员工管理</span>
        </el-menu-item>
        <el-menu-item index="2" :route="{ path: '/cate' }">
          <i class="icon fbookfont ic-cate-2"></i>
          <span slot="title">分类管理</span>
        </el-menu-item>
        <el-menu-item index="3" :route="{ path: '/books' }">
          <i class="icon fbookfont ic-books"></i>
          <span slot="title">图书管理</span>
        </el-menu-item>
        <el-menu-item index="4" :route="{ path: '/orders' }">
          <i class="icon fbookfont ic-order-2"></i>
          <span slot="title">订单管理</span>
        </el-menu-item>
        <el-menu-item index="5" :route="{ path: '/shop' }">
          <i class="icon fbookfont ic-shop"></i>
          <span slot="title">店铺管理</span>
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="less" scoped>
.aside {
  width: 196px;
  height: 100%;
  background-color: #343744;
  display: flex;
  flex-direction: column;
  user-select: none;
  .cover {
    width: 100%;
    height: 160px;
    margin-bottom: 32px;
    display: flex;
    flex-direction: column;
    align-items: center;
    img {
      display: block;
      width: 128px;
      height: 81px;
      border-radius: 5px;
      overflow: hidden;
      margin-top: 32px;
      object-fit: cover;
    }
    .title {
      margin: 0;
      padding: 0;
      font-size: 24px;
      font-weight: bold;
      margin-top: 6px;
      color: #fff;
    }
  }

  .menu {
    width: 100%;
    .el-menu {
      background: none;
      border: none;
      .el-menu-item {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 164px;
        height: 42px;
        padding: 0 !important;
        margin-bottom: 24px;
        line-height: 42px;
        border-radius: 0 21px 21px 0;
        color: #bfcbd9;
      }
      .el-menu-item.is-active,
      .el-menu-item.is-active:hover {
        color: #333;
        background-color: #83ccd2;
      }
      .el-menu-item:hover {
        color: #fff;
        background: none;
      }
    }
  }
  .icon {
    margin-right: 8px;
    font-size: 20px;
    color: inherit;
  }
}
</style>

<template>
  <div class="footer">
    <!-- 左边链接 -->
    <div class="left">
      <!-- 上方标题 -->
      <div class="title">
        <!-- logo -->
        <i class="logo ic-logo"></i>
        <!-- 网站标题 -->
        <div class="text">
          <span class="pre">流书网</span>
          <span class="suf">图书外卖</span>
        </div>
      </div>
      <!-- 下方站内链接 -->
      <div class="links">
        <!-- 链接组 -->
        <div class="link-group">
          <span class="link" @click="handleLink('/cate')">分类图书</span>
          <span class="link" @click="handleLink('/logon')">商家入驻</span>
        </div>
        <div class="link-group">
          <span class="link" @click="handleLink('/about/intro')">本站简介</span>
          <span class="link" @click="handleLink('/about/')">关于我们</span>
          <span class="link" @click="handleLink('/about/contact')">联系我们</span>
        </div>
        <div class="link-group">
          <span class="link" @click="handleLink('/about/issue')">常见问题</span>
          <span class="link" @click="handleLink('/agreement')">服务协议</span>
          <span class="link" @click="handleLink('/privacy')">隐私政策</span>
        </div>
      </div>
    </div>
    <!-- 右边二维码 -->
    <div class="right">
      <!-- 公众号 -->
      <div class="qr">
        <img class="img" src="@/assets/images/official_accounts.png" />
        <span class="text">流书网公众号</span>
      </div>
      <!-- 微博 -->
      <div class="qr">
        <img class="img" src="@/assets/images/weibo.png" />
        <span class="text">流书网微博</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    // 点击站内链接
    handleLink(link) {
      // this.$router.push(link);
    }
  }
};
</script>

<style lang="less" scoped>
.footer {
  width: 100%;
  height: 325px;
  background-color: var(--primary);
  display: flex;
  justify-content: space-between;
  .left {
    margin-left: 120px;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    .title {
      margin-top: 56px;
      display: flex;
      align-items: flex-end;
      color: var(--primary-text);
      user-select: none;
      .logo {
        font-size: 81px;
        color: #000;
      }
      .text {
        margin-left: 30px;
        font-family: uisdcfont-titleblack;
        .pre {
          font-size: 56px;
          margin-right: 3px;
        }
        .suf {
          font-size: 36px;
        }
      }
    }
    .links {
      margin-top: 42px;
      display: flex;
      justify-content: space-between;
      .link-group {
        margin: 0 27px;
        display: flex;
        flex-direction: column;
        .link {
          user-select: none;
          cursor: pointer;
          font-size: 16px;
          color: var(--fri-text);
          transition: color 0.5s ease;
          &:hover {
            color: var(--grey);
          }
          & + .link {
            margin-top: 8px;
          }
        }
      }
    }
  }
  .right {
    margin-right: 120px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    .qr {
      width: 150px;
      margin: 0 18px;
      display: flex;
      flex-direction: column;
      align-items: center;
      .img {
        width: 150px;
        height: 150px;
        background-color: #ccc;
      }
      .text {
        margin-top: 8px;
        font-size: 14px;
        color: var(--fri-text);
      }
    }
  }
}
</style>

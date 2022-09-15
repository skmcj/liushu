<template>
  <div class="header">
    <!-- 左边标题区域 -->
    <div class="left">
      <span v-if="returnFlag" class="return-btn">
        <i class="icon fbookfont ic-return"></i>
        返回
      </span>
      <!-- 标题 -->
      <span class="title">{{ headTitle }}</span>
    </div>
    <!-- 右边信息区域 -->
    <div class="right">
      <!-- 信息按钮 -->
      <i class="icon fbookfont ic-mess-1"></i>
      <!-- 登录人姓名 -->
      <span class="name">{{ name }}</span>
      <!-- 退出按钮 -->
      <i class="icon fbookfont ic-exit"></i>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // 是否显示返回图标
    returnFlag: {
      type: Boolean,
      default: false
    },
    // 显示标题
    headTitle: {
      type: String,
      default: '功能标题'
    },
    // 当前登录人姓名
    name: {
      type: String,
      default: '张三'
    }
  }
};
</script>

<style lang="less" scoped>
.header {
  width: 100%;
  height: 64px;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  user-select: none;
  box-shadow: 0 0 3px 1px var(--black-3);
  z-index: 99;
  .left {
    margin-left: 24px;
    font-size: 16px;
    font-weight: bold;
    color: var(--primary-font);
    line-height: 64px;
    .return-btn {
      width: 72px;
      height: 22px;
      color: #21242b;
      font-weight: normal;
      border-right: solid 2px #d8dde3;
      margin-right: 15px;
      cursor: pointer;
      .icon {
        font-size: 14px;
        margin-right: 15px;
        font-weight: bold;
        &:hover {
          color: var(--green);
        }
      }
    }
  }
  .right {
    margin-right: 36px;
    line-height: 64px;

    .ic-mess-1 {
      font-size: 14px;
      color: #bbbbbb;
      cursor: pointer;
      &:hover {
        color: var(--primary);
      }
    }
    .ic-exit {
      font-size: 24px;
      color: #9296a2;
      cursor: pointer;
      &:hover {
        color: var(--delete);
      }
    }
    .name {
      font-size: 14px;
      color: var(--primary-font);
      margin-left: 6px;
      margin-right: 12px;
    }
  }
}
</style>

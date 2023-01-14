<template>
  <div class="header">
    <!-- 左边标题区域 -->
    <div class="left">
      <span v-if="returnFlag" class="return-btn" @click="goBack">
        <i class="icon fbookfont ic-return"></i>
        返回
      </span>
      <!-- 标题 -->
      <span class="title">{{ headTitle }}</span>
    </div>
    <!-- 右边信息区域 -->
    <div class="right">
      <!-- 信息按钮 -->
      <i class="icon fbookfont ic-mess-1" @click.stop="handleOpenChat"></i>
      <!-- 登录人姓名 -->
      <span class="name">{{ name }}</span>
      <!-- 退出按钮 -->
      <i class="icon fbookfont ic-exit" @click="exitDialogVisible = true"></i>
    </div>
    <!-- 点击退出的提示框 -->
    <el-dialog title="提示" :visible.sync="exitDialogVisible" width="30%" :modal-append-to-body="false">
      <span>
        <i class="icon fbookfont ic-info"></i>
        您是否确认退出登录？
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="exit">确 认</el-button>
        <el-button @click="exitDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    /**
     * 是否显示返回图标
     */
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
      default: '**'
    }
  },
  data() {
    return {
      exitDialogVisible: false
    };
  },
  created() {},
  methods: {
    /**
     * 返回上一级页面
     */
    goBack() {
      this.$router.back();
    },
    exit() {
      window.localStorage.removeItem('employeeInfo');
      this.exitDialogVisible = false;
      this.$router.push('/login');
    },
    /**
     * 打开即时通信窗口
     */
    handleOpenChat() {
      this.$store.commit('resetCurrentConversation');
      this.$bus.$emit('openChatWindow', 'sys');
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
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-right: 36px;
    .ic-mess-1 {
      font-size: 14px;
      color: #bbbbbb;
      cursor: pointer;
      &:hover {
        color: var(--primary);
      }
    }
    .ic-exit {
      display: inline-block;
      width: 36px;
      height: 36px;
      text-align: center;
      line-height: 36px;
      border-radius: 50%;
      font-size: 24px;
      cursor: pointer;
      color: var(--primary);
      background-color: var(--pale);
      transition: all 0.2s ease-in-out;
      text-shadow: 1px 1px 0 var(--white);
      box-shadow: -5px -5px 12px var(--white), 5px 5px 12px var(--grayish);
      &:hover {
        color: var(--primary);
        box-shadow: -2px -2px 5px var(--white), 2px 2px 5px var(--grayish);
      }
      &:active {
        color: var(--delete);
        box-shadow: inset 1px 1px 2px var(--grayish), inset -1px -1px 2px var(--white);
      }
      &:focus {
        color: var(--delete);
      }
    }
    .name {
      font-size: 14px;
      color: var(--primary-font);
      padding: 5px 12px;
      margin-right: 12px;
    }
  }
  .el-button--primary {
    border-color: var(--delete);
    background-color: var(--delete);
  }
  .el-button--primary:focus,
  .el-button--primary:hover {
    color: var(--white);
    background-color: #f78989;
    border-color: #f78989;
  }
  .ic-info {
    font-size: 18px;
    color: var(--disable);
    margin-right: 5px;
  }
}
</style>

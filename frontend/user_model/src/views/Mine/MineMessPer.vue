<template>
  <!-- 我的-消息-私信组件 -->
  <div class="mine-mess-per">
    <div class="per-mess-list">
      <div class="per-mess-item" @click="handleClickCoversation">
        <div class="mess-cover">
          <img src="https://voidtech.cn/i/2022/11/17/pjyfse.jpg" alt="cover" />
        </div>
        <div class="mess-content">
          <div class="title">
            <div class="text">片刻书店</div>
            <div class="time">2022-12-11 23:24</div>
          </div>
          <div class="tip">你好，请问是订单有什么问题吗</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    handleClickCoversation() {
      this.$bus.$emit('openChatWindow', 'owner');
    }
  }
};
</script>

<style lang="less" scoped>
.mine-mess-per {
  display: flex;
  flex-direction: column;
  margin-top: 48px;
  width: 100%;
  padding: 0 24px;
  padding-bottom: 120px;
  box-sizing: border-box;
  .per-mess-list {
    width: 100%;
    display: flex;
    flex-direction: column;
  }
  .per-mess-item {
    cursor: pointer;
    user-select: none;
    display: flex;
    align-items: center;
    width: 100%;
    height: 128px;
    padding: 16px;
    border-radius: 8px;
    box-sizing: border-box;
    transition: all 0.5s ease-in-out;
    box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.25);
    &:active {
      box-shadow: inset 0px 1px 2px 0px rgba(0, 0, 0, 0.25);
    }
    & + .per-mess-item {
      margin-top: 12px;
    }
    .mess-cover {
      width: 96px;
      height: 96px;
      border-radius: 50%;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .mess-content {
      display: flex;
      flex-direction: column;
      height: 100%;
      flex-grow: 1;
      margin-left: 16px;
      .title {
        width: 100%;
        display: flex;
        justify-content: space-between;
        height: 48px;
        .text {
          height: 48px;
          line-height: 48px;
          font-size: 24px;
          font-weight: bold;
          color: var(--primary-text);
        }
        .time {
          margin-right: 8px;
          font-size: 14px;
          color: var(--placeholder-text);
        }
      }
      .tip {
        display: flex;
        align-items: center;
        width: 96%;
        flex-grow: 1;
        font-size: 16px;
        color: var(--placeholder-text);
      }
    }
  }
}
</style>

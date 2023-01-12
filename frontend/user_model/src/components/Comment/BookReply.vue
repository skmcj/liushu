<template>
  <!-- 图书回复 -->
  <div class="ls-bcm-reply-box" v-if="reply">
    <div
      class="ls-bcm-reply-item"
      :class="{
        'is-user': reply.userId
      }">
      <div class="ls-bcm-reply__title">
        {{ reply.storeId ? '商家回复:' : '用户回复:' }}
      </div>
      <div class="ls-bcm-reply-content">
        <div class="ls-bcm-reply__content">{{ reply.content }}</div>
        <div class="ls-bcm-reply__time">{{ reply.updateTime }}</div>
      </div>
    </div>
    <book-reply :reply="reply.reply" />
  </div>
</template>

<script>
export default {
  name: 'bookReply',
  props: {
    reply: Object
  }
};
</script>

<style lang="less" scoped>
.ls-bcm-reply-box {
  display: flex;
  flex-direction: column;
  .ls-bcm-reply-item {
    width: 100%;
    display: flex;
    flex-direction: column;
    border-radius: 5px;
    background-color: #f9f8f7;
    overflow: hidden;
    box-sizing: border-box;
    padding: 5px 12px;
    margin-bottom: 8px;
    &.is-user {
      background: linear-gradient(90deg, #fff, #f0f9fa);
      border-left: 2px solid #cccccc;
      border-radius: 0 5px 5px 0;
    }
    & + .ls-bcm-reply-item {
      margin-top: 8px;
      border-top: 1px dashed #dddcd6;
    }
  }
  .ls-bcm-reply__title {
    width: 100%;
    font-size: 14px;
    height: 24px;
    line-height: 24px;
    color: #666666;
  }
  .ls-bcm-reply-content {
    padding: 3px 0 5px 0;
    margin-left: 16px;
    display: flex;
    justify-content: space-between;
    .ls-bcm-reply__content {
      flex-grow: 1;
      font-size: 12px;
      color: #999999;
    }
    .ls-bcm-reply__time {
      min-width: 72px;
      font-size: 10px;
      color: #8b968d;
      text-align: right;
    }
  }
}
</style>

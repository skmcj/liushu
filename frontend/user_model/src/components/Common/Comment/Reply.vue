<template>
  <!-- 回复 -->
  <div class="ls-reply-node">
    <div
      class="ls-reply-content"
      :class="{
        'is-user': reply.userId
      }">
      <div class="ls-reply__title">
        {{ reply.storeId ? '商家回复：' : '我的回复：' }}
      </div>
      <div class="ls-reply__content">
        <div class="ls-reply__text">{{ reply.content }}</div>
        <div class="ls-reply__time">{{ reply.updateTime }}</div>
      </div>
    </div>
    <!-- 子回复 -->
    <reply v-if="reply.reply" :reply="reply.reply" />
  </div>
</template>

<script>
export default {
  name: 'reply',
  props: {
    reply: Object
  },
  data() {
    return {};
  }
};
</script>

<style lang="less" scoped>
.ls-reply-node {
  user-select: none;
  display: flex;
  flex-direction: column;
  .ls-reply-content {
    width: 100%;
    display: flex;
    flex-direction: column;
    border-radius: 5px;
    background-color: #f9f8f7;
    overflow: hidden;
    box-sizing: border-box;
    padding: 5px 12px;
    &.is-user {
      background-color: #f8f8f8;
    }
    .ls-bcm-reply__title {
      width: 100%;
      font-size: 14px;
      height: 24px;
      line-height: 24px;
      color: #666666;
    }
    .ls-reply__content {
      padding: 3px 0 5px 0;
      margin-left: 16px;
      display: flex;
      justify-content: space-between;
      .ls-reply__text {
        flex-grow: 1;
        font-size: 12px;
        color: #999999;
      }
      .ls-reply__time {
        min-width: 72px;
        font-size: 10px;
        color: #8b968d;
        text-align: right;
      }
    }
    & + .ls-reply-node {
      margin-top: 8px;
    }
  }
}
</style>

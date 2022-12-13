<template>
  <!-- 我的-消息-私信组件 -->
  <div class="mine-mess-per">
    <div class="per-mess-list">
      <template v-for="item in conversationList">
        <div
          class="per-mess-item"
          :key="item.conversationID"
          v-if="!excludeCoversationList[item.conversationID]"
          @click="handleClickCoversation(item)">
          <div class="mess-cover">
            <Avatar style="width: 96px; height: 96px" :src="avatar(item)" shape="circle" :type="item.type" />
          </div>
          <div class="mess-content">
            <div class="title">
              <div class="text" :title="item.userProfile.nick || item.userProfile.userID">
                {{ item.remark || item.userProfile.nick || item.userProfile.userID }}
              </div>
              <div class="time">{{ date(item) }}</div>
            </div>
            <div class="tip">{{ messageForShow(item) }}</div>
            <!-- 未读数 -->
            <div class="unread-count" v-if="showUnreadCount(item)">
              <span>{{ item.unreadCount > 99 ? '99+' : item.unreadCount }}</span>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import { isToday, getDate, getTime } from '@/utils/im/imHandle';
import Avatar from '@/components/Chat/Message/Avatar';

export default {
  components: {
    Avatar
  },
  methods: {
    handleClickCoversation(conversation) {
      this.$bus.$emit('openChatWindow', 'owner');
      if (conversation.conversationID !== this.currentConversation.conversationID) {
        this.$store.dispatch('checkoutConversation', conversation.conversationID);
      }
    },
    avatar(conversation) {
      switch (conversation.type) {
        case 'GROUP':
          return conversation.groupProfile.avatar;
        case 'C2C':
          return conversation.userProfile.avatar;
        default:
          return '';
      }
    },
    /**
     * 日期
     */
    date(conversation) {
      if (!conversation.lastMessage || !conversation.lastMessage.lastTime) {
        return '';
      }
      const date = new Date(conversation.lastMessage.lastTime * 1000);
      if (isToday(date)) {
        return getTime(date);
      }
      return getDate(date);
    },
    /**
     * 最新消息预览
     */
    messageForShow(conversation) {
      if (conversation.lastMessage.isRevoked) {
        if (conversation.lastMessage.fromAccount === this.currentUserProfile.userID) {
          return '你撤回了一条消息';
        }
        if (conversation.type === this.TIM.TYPES.CONV_C2C) {
          return '对方撤回了一条消息';
        }
        return `${conversation.lastMessage.fromAccount}撤回了一条消息`;
      }
      return conversation.lastMessage.messageForShow;
    },
    /**
     * 未读数量
     */
    showUnreadCount(conversation) {
      if (this.$store.getters.hidden) {
        return conversation.unreadCount > 0;
      }
      // 是否显示未读计数。当前会话和未读计数为0的会话，不显示。
      return this.currentConversation.conversationID !== conversation.conversationID && conversation.unreadCount > 0;
    }
  },
  computed: {
    ...mapState({
      conversationList: state => state.conversation.conversationList,
      currentConversation: state => state.conversation.currentConversation,
      currentUserProfile: state => state.user.currentUserProfile,
      excludeCoversationList: state => state.conversation.excludeCoversationList
    }),
    ...mapGetters(['toAccount'])
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
      position: relative;
    }
    .mess-content {
      position: relative;
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
      .unread-count {
        position: absolute;
        bottom: 0;
        right: 6px;
        padding: 0 6px;
        height: 18px;
        border-radius: 10px;
        background-color: #f35f5f;
        overflow: hidden;
        text-align: center;
        line-height: 18px;
        span {
          color: #fff;
          font-size: 10px;
        }
      }
    }
  }
}
</style>

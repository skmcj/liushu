<template>
  <!-- 我的-消息-系统消息组件 -->
  <div class="mine-mess-sys">
    <div class="sys-mess-list">
      <!-- 客服通知 -->
      <div class="sys-mess-item" @click="handleClickCoversation('customerService')">
        <div class="mess-cover">
          <img :src="customerSA" alt="cover" />
        </div>
        <div class="mess-content">
          <div class="title">
            <div class="text">在线客服</div>
            <div class="time" v-if="customerConversation">{{ date(customerConversation) }}</div>
          </div>
          <div class="tip">
            <span v-if="customerConversation">{{ messageForShow(customerConversation) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import { isToday, getDate, getTime } from '@/utils/im/imHandle';
import customerSA from '@/assets/images/svg/customer-service.svg';

export default {
  data() {
    return {
      customerSA,
      // 主要分为 订单动态 | 客服消息 | 活动通知
      customerConversation: undefined
    };
  },
  created() {
    this.handleCL();
  },
  methods: {
    handleCL() {
      this.conversationList.forEach(item => {
        if (item.conversationID === 'C2Ccustomerservice') {
          this.customerConversation = item;
        }
      });
    },
    handleClickCoversation(text) {
      this.$bus.$emit('openChatWindow', 'sys');
      let conversationID = '';
      switch (text) {
        case 'customerService':
          conversationID = 'C2Ccustomerservice';
          break;
        default:
          break;
      }
      if (conversationID !== this.currentConversation.conversationID) {
        this.$store.dispatch('checkoutConversation', conversationID);
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
    })
  }
};
</script>

<style lang="less" scoped>
.mine-mess-sys {
  display: flex;
  flex-direction: column;
  margin-top: 48px;
  width: 100%;
  padding: 0 24px;
  padding-bottom: 120px;
  box-sizing: border-box;
  .sys-mess-list {
    width: 100%;
    display: flex;
    flex-direction: column;
  }
  .sys-mess-item {
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
    & + .sys-mess-item {
      margin-top: 12px;
    }
    .mess-cover {
      width: 96px;
      height: 96px;
      img,
      .icon {
        width: 100%;
        height: 100%;
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

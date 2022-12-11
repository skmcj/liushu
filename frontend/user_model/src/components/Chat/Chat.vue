<template>
  <div class="chat" v-if="showChat" @scroll="onScroll">
    <div class="coversation-header" :class="{ 'h-center-in': chatClassFlag, 'h-center-out': !chatClassFlag }">
      <span class="name">{{ name }}</span>
    </div>
    <div class="coversation-main">
      <!-- 消息列表 -->
      <div class="message-list" ref="messageList" @scroll="this.onScroll">
        <!-- 顶部tip -->
        <div class="more" v-if="!isCompleted">
          <span @click="handleLoadMore">查看更多</span>
        </div>
        <div class="no-more" v-else>没有更多了</div>
        <!-- 消息项 -->
        <MessageItem v-for="message in currentMessageList" :key="message.ID" :message="message" />
        <!-- TODO：回到最新位置 -->
        <div v-show="isShowScrollButtomTips" class="newMessageTips" @click="scrollMessageListToButtom">
          回到最新位置
        </div>
      </div>
    </div>
    <div
      v-if="showMessageSendBox"
      class="coversation-footer"
      :class="{
        'f-center-in': chatClassFlag,
        'f-center-out': !chatClassFlag
      }">
      <SendBox />
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import SendBox from '@/components/Chat/SendBox';
import MessageItem from '@/components/Chat/Message/MessageItem';

export default {
  components: {
    SendBox,
    MessageItem
  },
  data() {
    return {
      showChat: false,
      // true - 进入的class, false - 隐藏的class
      chatClassFlag: false,
      // 要发送的消息
      messageContent: '',
      // 是否显示回到最新位置按钮
      isShowScrollButtomTips: false,
      preScrollHeight: 0
    };
  },
  mounted() {
    this.$bus.$on('scroll-bottom', this.scrollMessageListToButtom);
  },
  updated() {
    this.keepMessageListOnButtom();
  },
  methods: {
    /**
     * 会话窗口隐藏
     */
    handleHide() {
      this.chatClassFlag = false;
      setTimeout(() => {
        this.showChat = false;
      }, 480);
    },
    /**
     * 监听消息列表滚动
     */
    onScroll({ target: { scrollTop } }) {
      let messageListNode = this.$refs.messageList;
      if (!messageListNode) {
        return;
      }
      if (this.preScrollHeight - messageListNode.clientHeight - scrollTop < 20) {
        this.isShowScrollButtomTips = false;
      }
    },
    // 直接滚到底部
    scrollMessageListToButtom() {
      this.$nextTick(() => {
        let messageListNode = this.$refs.messageList;
        if (!messageListNode) {
          return;
        }
        messageListNode.scrollTop = messageListNode.scrollHeight;
        this.preScrollHeight = messageListNode.scrollHeight;
        this.isShowScrollButtomTips = false;
      });
    },
    // 如果滚到底部就保持在底部，否则提示是否要滚到底部
    keepMessageListOnButtom() {
      let messageListNode = this.$refs.messageList;
      if (!messageListNode) {
        return;
      }
      // 距离底部20px内强制滚到底部,否则提示有新消息
      if (this.preScrollHeight - messageListNode.clientHeight - messageListNode.scrollTop < 20) {
        this.$nextTick(() => {
          messageListNode.scrollTop = messageListNode.scrollHeight;
        });
        this.isShowScrollButtomTips = false;
      } else {
        this.isShowScrollButtomTips = true;
      }
      this.preScrollHeight = messageListNode.scrollHeight;
    },
    /**
     * 加载更多消息
     */
    handleLoadMore() {
      this.$store.dispatch('getMessageList', this.currentConversation.conversationID);
    }
  },
  computed: {
    ...mapState({
      currentConversation: state => state.conversation.currentConversation,
      currentUnreadCount: state => state.conversation.currentConversation.unreadCount,
      currentMessageList: state => state.conversation.currentMessageList,
      isCompleted: state => state.conversation.isCompleted
    }),
    ...mapGetters(['toAccount', 'hidden']),
    // 是否显示当前会话组件
    showCurrentConversation() {
      return !!this.currentConversation.conversationID;
    },
    /**
     * 会话名称
     */
    name() {
      if (this.currentConversation.type === 'C2C') {
        // let name = this.currentConversation.userProfile.nick || this.toAccount
        // let list = this.currentMessageList
        // let len = list.length
        // for (let i = len - 1; i >= 0; i--) {
        //   // C2C 会话对端更新nick时需要更新会话title
        //   if (list[i].flow === 'in' && list[i].nick && name !== list[i].nick) {
        //     name = list[i].nick
        //     break
        //   }
        // }
        return (
          this.currentConversation.remark ||
          this.currentConversation.userProfile.nick ||
          this.currentConversation.userProfile.userID
        );
      } else if (this.currentConversation.type === 'GROUP') {
        return this.currentConversation.groupProfile.name || this.toAccount;
      } else if (this.currentConversation.conversationID === '@TIM#SYSTEM') {
        return '系统通知';
      }
      return this.toAccount;
    },
    /**
     * 是否显示发送组件
     */
    showMessageSendBox() {
      return this.currentConversation.type !== this.TIM.TYPES.CONV_SYSTEM;
    }
  },
  watch: {
    showCurrentConversation: {
      immediate: true,
      handler(val) {
        if (val) {
          this.chatClassFlag = true;
          this.showChat = true;
        } else {
          this.handleHide();
        }
      }
    },
    currentUnreadCount(next) {
      if (!this.hidden && next > 0) {
        this.tim.setMessageRead({ conversationID: this.currentConversation.conversationID });
      }
    },
    hidden(next) {
      if (!next && this.currentUnreadCount > 0) {
        this.tim.setMessageRead({ conversationID: this.currentConversation.conversationID });
      }
    }
  }
};
</script>

<style lang="less" scoped>
.chat {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  .coversation-header {
    display: flex;
    align-items: center;
    flex-shrink: 0;
    width: 100%;
    height: 48px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.16);
    text-align: left;
    .name {
      width: 0;
      flex-grow: 1;
      margin-left: 21px;
      font-size: 18px;
      font-weight: bold;
      color: #3d3d3d;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .coversation-main {
    display: flex;
    width: 100%;
    flex-grow: 1;
    overflow: hidden;
    -webkit-box-flex: 1;
    -ms-flex: 1;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    height: 100%;
    position: relative;
    .message-list {
      width: 100%;
      box-sizing: border-box;
      overflow-y: scroll;
      flex-grow: 1;
      .more {
        font-size: 12px;
        padding: 5px;
        text-align: center;
        color: #999;
        cursor: pointer;
      }
      .no-more {
        display: flex;
        -webkit-box-pack: center;
        justify-content: center;
        color: #a5b5c1;
        font-size: 12px;
        padding: 10px;
      }
      .newMessageTips {
        user-select: none;
        position: absolute;
        cursor: pointer;
        padding: 5px;
        width: 120px;
        margin: auto;
        left: 0;
        right: 0;
        bottom: 5px;
        font-size: 12px;
        text-align: center;
        border-radius: 10px;
        border: #e9eaec 1px solid;
        background-color: #fff;
        color: #83ccd2;
      }
    }
  }
  .coversation-footer {
    display: flex;
    flex-direction: column;
    flex-shrink: 0;
    width: 100%;
    height: 144px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.16);
    box-sizing: border-box;
    padding: 5px 21px 21px 21px;
  }
  .h-center-in {
    animation: h-enter-in 0.5s ease-in-out;
  }
  .h-center-out {
    animation: h-enter-out 0.5s ease-in-out;
  }
  .f-center-in {
    animation: f-enter-in 0.5s ease-in-out;
  }
  .f-center-out {
    animation: f-enter-out 0.5s ease-in-out;
  }
  ::-webkit-scrollbar {
    width: 6px;
    height: 140px;
    background-color: transparent;
  }
  ::-webkit-scrollbar-track {
    border-radius: 10px;
  }
  ::-webkit-scrollbar-thumb {
    border-radius: 10px;
    background-color: #d6d6d6;
  }
}
@keyframes h-enter-in {
  0% {
    transform: translateY(-64px);
    opacity: 0;
  }
  100% {
    transform: translateY(0);
    opacity: 1;
  }
}
@keyframes h-enter-out {
  0% {
    transform: translateY(0);
    opacity: 1;
  }
  100% {
    transform: translateY(-64px);
    opacity: 0;
  }
}
@keyframes f-enter-in {
  0% {
    transform: translateY(156px);
    opacity: 0;
  }
  100% {
    transform: translateY(0);
    opacity: 1;
  }
}
@keyframes f-enter-out {
  0% {
    transform: translateY(0);
    opacity: 1;
  }
  100% {
    transform: translateY(156px);
    opacity: 0;
  }
}
</style>

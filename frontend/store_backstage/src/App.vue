<template>
  <div id="app">
    <transition name="fade" mode="out-in">
      <router-view name="index" />
    </transition>
    <!-- <router-view v-slot="{ Component }" name="index">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view> -->
    <!-- <Container /> -->
  </div>
</template>

<script>
import Container from '@/components/Container/Container.vue';

export default {
  components: {
    Container
  },
  created() {
    // TIM应用初始化时
    this.initListener();
  },
  beforeDestroy() {
    this.tim.logout();
  },
  data() {
    return {
      visible: false
    };
  },
  methods: {
    initListener() {
      // 登录成功后会触发 SDK_READY 事件，该事件触发后，可正常使用 SDK 接口
      this.tim.on(this.TIM.EVENT.SDK_READY, this.onReadyStateUpdate, this);
      // SDK NOT READT, 避免在 SDK_NOT_READY 中调用 login 接口，可能会造成循环互踢的情况。
      this.tim.on(this.TIM.EVENT.SDK_NOT_READY, this.onReadyStateUpdate, this);
      // 用户被踢下线时触发
      this.tim.on(this.TIM.EVENT.KICKED_OUT, this.onKickOut);
      // SDK内部出错
      this.tim.on(this.TIM.EVENT.ERROR, this.onError);
      // 收到新消息
      this.tim.on(this.TIM.EVENT.MESSAGE_RECEIVED, this.onReceiveMessage);
      // 会话列表更新
      this.tim.on(this.TIM.EVENT.CONVERSATION_LIST_UPDATED, this.onUpdateConversationList);
      // 群组列表更新
      // this.tim.on(this.TIM.EVENT.GROUP_LIST_UPDATED, this.onUpdateGroupList);
      // 网络监测
      this.tim.on(this.TIM.EVENT.NET_STATE_CHANGE, this.onNetStateChange);
      // 已读回执
      this.tim.on(this.TIM.EVENT.MESSAGE_READ_BY_PEER, this.onMessageReadByPeer);
      // 黑名单更新
      // this.tim.on(this.TIM.EVENT.FRIEND_LIST_UPDATED, this.onFriendListUpdated);
      // 好友申请列表更新时触发
      // this.tim.on(this.TIM.EVENT.FRIEND_APPLICATION_LIST_UPDATED, this.onFriendApplicationListUpdated);
      // 好友分组列表更新时触发
      // this.tim.on(this.TIM.EVENT.FRIEND_GROUP_LIST_UPDATED, this.onFriendGroupListUpdated);
    },
    /**
     * TIM SDK ready 状态更新
     */
    onReadyStateUpdate({ name }) {
      const isSDKReady = name === this.TIM.EVENT.SDK_READY ? true : false;
      this.$store.commit('toggleIsSDKReady', isSDKReady);

      if (isSDKReady) {
        this.tim
          .getMyProfile()
          .then(({ data }) => {
            this.$store.commit('updateCurrentUserProfile', data);
          })
          .catch(error => {
            this.$store.commit('showMessage', {
              type: 'error',
              message: error.message
            });
          });
        this.$store.dispatch('getBlacklist');
        // 登录trtc calling - 实时音视频功能
        /**
        this.trtcCalling.login({
          sdkAppID: this.sdkAppID,
          userID: this.userID,
          userSig: this.userSig
        });
        */
      }
    },
    /**
     * 用户被踢出
     */
    onKickOut(event) {
      this.$store.commit('showMessage', {
        message: `${this.kickedOutReason(event.data.type)}被踢出，请重新登录。`,
        type: 'error'
      });
      this.$store.commit('toggleIsLogin', false);
      this.$store.commit('reset');
    },
    kickedOutReason(type) {
      switch (type) {
        case this.TIM.TYPES.KICKED_OUT_MULT_ACCOUNT:
          return '由于多实例登录';
        case this.TIM.TYPES.KICKED_OUT_MULT_DEVICE:
          return '由于多设备登录';
        case this.TIM.TYPES.KICKED_OUT_USERSIG_EXPIRED:
          return '由于 userSig 过期';
        default:
          return '';
      }
    },
    /**
     * SDk 出错
     */
    onError({ data }) {
      if (data.message !== 'Network Error') {
        this.$store.commit('showMessage', {
          message: data.message,
          type: 'error'
        });
      }
    },
    /**
     * 收到新消息
     */
    onReceiveMessage({ data: messageList }) {
      // this.handleVideoMessage(messageList);
      // this.handleQuitGroupTip(messageList);
      // this.handleCloseGroupLive(messageList);
      this.$store.commit('pushCurrentMessageList', messageList);
      // this.$store.commit('pushAvChatRoomMessageList', messageList);
    },
    /**
     * 会话列表更新
     */
    onUpdateConversationList(event) {
      this.$store.commit('updateConversationList', event.data);
    },
    /**
     * 网络监测
     */
    onNetStateChange(event) {
      this.$store.commit('showMessage', this.checkoutNetState(event.data.state));
    },
    checkoutNetState(state) {
      switch (state) {
        case this.TIM.TYPES.NET_STATE_CONNECTED:
          return { message: '已接入网络', type: 'success' };
        case this.TIM.TYPES.NET_STATE_CONNECTING:
          return { message: '当前网络不稳定', type: 'warning' };
        case this.TIM.TYPES.NET_STATE_DISCONNECTED:
          return { message: '当前网络不可用', type: 'error' };
        default:
          return '';
      }
    },
    /**
     * 已读回执
     */
    onMessageReadByPeer() {},
    /**
     * 使用 window.Notification 进行全局的系统通知
     * @param {Message} message
     */
    notifyMe(message) {
      // 需检测浏览器支持和用户授权
      if (!('Notification' in window)) {
        return '';
      } else if (window.Notification.permission === 'granted') {
        this.handleNotify(message);
      } else if (window.Notification.permission !== 'denied') {
        window.Notification.requestPermission().then(permission => {
          // 如果用户同意，就可以向他们发送通知
          if (permission === 'granted') {
            this.handleNotify(message);
          }
        });
      }
    },
    handleNotify(message) {
      const notification = new window.Notification('有人提到了你', {
        icon: 'https://web.sdk.qcloud.com/im/assets/images/logo.png',
        body: message.payload.text
      });
      notification.onclick = () => {
        window.focus();
        this.$store.dispatch('checkoutConversation', message.conversationID);
        notification.close();
      };
    }
  }
};
</script>

<style lang="less">
html,
body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  background-color: var(--default-bg);
}

[class*=' ic-'],
[class^='ic-'] {
  font-family: 'fbookfont' !important;
  font-size: 14px;
  font-style: normal;
  speak: none;
  font-weight: 400;
  font-variant: normal;
  text-transform: none;
  line-height: 1;
  vertical-align: baseline;
  display: inline-block;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
// 选中时的背景色
::selection {
  color: #fff;
  background: var(--primary);
}
::-moz-selection {
  color: #fff;
  background-color: var(--primary);
}
#app {
  width: 100%;
  height: 100%;
}
// 路由过渡
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

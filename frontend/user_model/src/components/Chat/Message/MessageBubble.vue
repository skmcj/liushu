<template>
  <div class="message-bubble" @mousedown.stop @contextmenu.prevent>
    <el-dropdown trigger="" ref="dropdown" placement="bottom-start" v-if="!message.isRevoked" @command="handleCommand">
      <!-- 消息体 -->
      <div class="message-bubble-main">
        <!-- 消息读取状态：已读 | 未读 -->
        <div v-if="isMine && messageReadByPeer" class="message-status">
          <span>{{ messageReadByPeer }}</span>
        </div>
        <!-- 消息内容 -->
        <div class="message-content" :class="bubbleStyle">
          <slot></slot>
        </div>
      </div>
      <!-- 下拉操作菜单：撤回、删除、引用 -->
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="revoke" v-if="isMine && !isTimeout">撤回</el-dropdown-item>
        <el-dropdown-item command="delete" v-show="message.status !== 'fail'">删除</el-dropdown-item>
        <el-dropdown-item command="quote" v-show="message.status !== 'fail'">引用</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <!-- 已撤回消息-重新编辑 -->
    <div class="tip-element-wrapper" v-if="message.isRevoked">
      {{ text }}
      <span class="edit-button" v-show="isEdit" @click="reEdit">&nbsp;重新编辑</span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isMine: {
      type: Boolean
    },
    isNew: {
      type: Boolean
    },
    message: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      isTimeout: false,
      showConversationList: false,
      relayMessage: {},
      selectedConversation: [],
      testMergerMessage: {}
    };
  },
  created() {
    this.isTimeoutHandler();
  },
  mounted() {
    if (this.$refs.dropdown && this.$refs.dropdown.$el) {
      this.$refs.dropdown.$el.addEventListener('mousedown', this.handleDropDownMousedown);
    }
  },
  beforeDestroy() {
    if (this.$refs.dropdown && this.$refs.dropdown.$el) {
      this.$refs.dropdown.$el.removeEventListener('mousedown', this.handleDropDownMousedown);
    }
  },
  methods: {
    handleDropDownMousedown(e) {
      if (e.buttons === 2) {
        if (this.$refs.dropdown.visible) {
          this.$refs.dropdown.hide();
        } else {
          this.$refs.dropdown.show();
        }
      }
    },
    /**
     * 处理下拉菜单命令
     */
    handleCommand(command) {
      switch (command) {
        // 撤回
        case 'revoke':
          this.tim
            .revokeMessage(this.message)
            .then(() => {
              this.isTimeoutHandler();
            })
            .catch(err => {
              this.$store.commit('showMessage', {
                message: err,
                type: 'warning'
              });
            });
          break;
        case 'delete':
          // this.showConversationList = true;
          // this.$store.commit('setRelayType', 1);
          // this.$store.commit('showConversationList', true);
          // this.$store.commit('setRelayMessage', this.message);
          break;
        // 引用
        case 'quote':
          this.$emit('quoteSelected', true);
          break;
        default:
          break;
      }
    },
    // 从发送消息时间开始算起，两分钟内可以编辑
    isTimeoutHandler() {
      let now = new Date();
      if (parseInt(now.getTime() / 1000) - this.message.time > 2 * 60) {
        this.isTimeout = true;
        return;
      }
      setTimeout(this.isTimeoutHandler, 1000);
    },
    reEdit() {
      this.$bus.$emit('reEditMessage', this.message.payload.text);
    }
  },
  computed: {
    /**
     * 气泡样式
     */
    bubbleStyle() {
      let classString = '';
      if (this.isMine) {
        // 发送方
        classString += 'message-send';
      } else {
        // 接收方
        classString += 'message-received';
      }
      if (this.isNew) {
        // 新消息
        classString += 'new';
      }
      return classString;
    },
    /**
     * 撤回tip
     */
    text() {
      if (this.message.conversationType === this.TIM.TYPES.CONV_C2C && !this.isMine) {
        return '对方撤回了一条消息';
      }
      if (this.message.conversationType === this.TIM.TYPES.CONV_GROUP && !this.isMine) {
        return `${this.message.from}撤回了一条消息`;
      }
      return '你撤回了一条消息';
    },
    /**
     * 消息读取状态
     */
    messageReadByPeer() {
      if (this.message.status !== 'success') {
        return false;
      }
      if (this.message.conversationType === this.TIM.TYPES.CONV_C2C && this.message.isPeerRead) {
        return '已读';
      }
      if (this.message.conversationType === this.TIM.TYPES.CONV_C2C && !this.message.isPeerRead) {
        return '未读';
      }
      return '';
    },
    isEdit() {
      if (!this.isMine) {
        return false;
      }
      if (this.message.type !== this.TIM.TYPES.MSG_TEXT) {
        return false;
      }
      if (this.isTimeout) {
        return false;
      }
      return true;
    }
  }
};
</script>

<style lang="less" scoped>
.message-bubble {
  .message-bubble-main {
    display: flex;
    .message-status {
      display: flex;
      min-width: 25px;
      margin-right: 10px;
      flex-shrink: 0;
      -webkit-box-pack: center;
      justify-content: center;
      -webkit-box-align: center;
      align-items: center;
      font-size: 12px;
      color: #6e7981;
    }
    .message-content {
      padding: 12px;
      font-size: 14px;
      color: #666;
      letter-spacing: 0;
      text-align: left;
      word-wrap: break-word;
      word-break: break-all;
      background: #fff;
      overflow: hidden;
      border-radius: 0px 10px 10px 10px;
      box-shadow: rgba(0, 0, 0, 0.1) 0px 5px 10px 0px;
    }
  }
  &.image-message,
  &.face-message {
    .message-bubble-main {
      .message-content {
        display: flex;
        padding: 0;
        background: none;
        box-shadow: none;
        border-radius: 0 10px 10px 10px;
      }
    }
  }
  .tip-element-wrapper {
    padding: 4px 15px;
    border-radius: 3px;
    color: #a5b5c1;
    font-size: 12px;
    .edit-button {
      display: inline-block;
      white-space: nowrap;
      height: 20px;
      font-size: 10px;
      line-height: 20px;
      text-align: center;
      cursor: pointer;
      color: #83ccd2;
      box-sizing: border-box;
      &:hover {
        color: #a0dbe0;
      }
    }
  }
}
.pos-right {
  .message-bubble {
    .message-bubble-main {
      .message-content {
        background: rgba(131, 204, 210, 0.5);
        border-radius: 10px 0px 10px 10px;
      }
    }
    &.face-message,
    &.image-message {
      .message-bubble-main {
        .message-content {
          display: flex;
          padding: 0;
          background: none;
          border-radius: none;
          border-radius: 10px 0 10px 10px;
        }
      }
    }
  }
}
</style>

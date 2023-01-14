<template>
  <!-- 不渲染排除的会话项 -->
  <li
    v-if="!excludeCoversationList[conversation.conversationID]"
    class="coversation-list-item"
    @mousedown.stop
    @contextmenu.prevent
    :class="{
      selected: conversation.conversationID === currentConversation.conversationID,
      'is-pinned': conversation.isPinned
    }"
    @click="selectConversation">
    <!-- TODO: 下拉操作菜单 - 置顶、删除 -->
    <el-dropdown trigger="" ref="cdropdown" placement="bottom" @command="handleCommand">
      <div class="coversation-list-item-content">
        <div class="avatar">
          <!-- 头像 -->
          <Avatar style="width: 44px; height: 44px" :src="avatar" shape="circle" :type="conversation.type" />
          <!-- 未读数 -->
          <div class="unread-count" v-if="showUnreadCount">
            <span>{{ conversation.unreadCount > 99 ? '99+' : conversation.unreadCount }}</span>
          </div>
        </div>
        <div class="item-main">
          <div class="top">
            <!-- 会话名 -->
            <div class="text-ellipsis">
              <!-- 用户名 -->
              <span
                class="name"
                :title="conversation.userProfile.nick || conversation.userProfile.userID"
                v-if="conversation.type === TIM.TYPES.CONV_C2C"
                >{{ conversation.remark || conversation.userProfile.nick || conversation.userProfile.userID }}
              </span>
              <span
                class="name"
                :title="conversation.groupProfile.name || conversation.groupProfile.groupID"
                v-else-if="conversation.type === TIM.TYPES.CONV_GROUP"
                >{{ conversation.groupProfile.name || conversation.groupProfile.groupID }}
              </span>
              <span class="name" v-else-if="conversation.type === TIM.TYPES.CONV_SYSTEM">系统通知 </span>
            </div>
            <!-- 上一次会话结束时间 -->
            <span class="date">{{ date }}</span>
          </div>
          <div class="summary">
            <!-- <div v-if="conversation.lastMessage" class="text-ellipsis">
            <span class="remind"  v-if="hasMessageAtMe">{{messageAtMeText}}</span>
            <span class="text" :title="conversation.lastMessage.messageForShow">
              {{messageForShow}}
            </span>
          </div> -->
            <div class="text-ellipsis">
              <!-- 上一次会话简介 -->
              <span class="text" :title="conversation.lastMessage.messageForShow">
                {{ messageForShow }}
              </span>
            </div>
          </div>
        </div>
      </div>
      <!-- 下拉操作菜单：置顶、删除、免打扰 -->
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="pinned" v-if="!conversation.isPinned">置顶会话</el-dropdown-item>
        <el-dropdown-item command="dispinned" v-if="conversation.isPinned">取消置顶</el-dropdown-item>
        <!-- <el-dropdown-item
          command="noDisturb"
          v-if="conversation.messageRemindType === '' || conversation.messageRemindType === 'AcceptAndNotify'"
          >消息免打扰</el-dropdown-item
        >
        <el-dropdown-item command="noDisturb" v-if="conversation.messageRemindType === 'AcceptNotNotify'"
          >取消免打扰</el-dropdown-item
        > -->
        <el-dropdown-item command="delete">删除会话</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </li>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import { isToday, getDate, getTime } from '@/utils/im/imHandle';
import Avatar from '@/components/Chat/Message/Avatar';

export default {
  components: {
    Avatar
  },
  props: {
    conversation: Object
  },
  data() {
    return {};
  },
  mounted() {
    if (this.$refs.cdropdown && this.$refs.cdropdown.$el) {
      this.$refs.cdropdown.$el.addEventListener('mousedown', this.handleDropDownMousedown);
    }
  },
  beforeDestroy() {
    if (this.$refs.cdropdown && this.$refs.cdropdown.$el) {
      this.$refs.cdropdown.$el.removeEventListener('mousedown', this.handleDropDownMousedown);
    }
  },
  methods: {
    handleDropDownMousedown(e) {
      if (e.buttons === 2) {
        if (this.$refs.cdropdown.visible) {
          this.$refs.cdropdown.hide();
        } else {
          this.$refs.cdropdown.show();
        }
      }
    },
    /**
     * 处理下拉菜单命令
     */
    handleCommand(command) {
      switch (command) {
        // 撤回
        case 'pinned':
          // 置顶会话
          this.pinConversation(true);
          break;
        case 'dispinned':
          this.pinConversation(false);
          // 取消置顶
          break;
        case 'delete':
          // 删除会话
          this.deleteConversation();
          break;
        default:
          break;
      }
    },
    /**
     * 选择会话
     */
    selectConversation() {
      if (this.conversation.conversationID !== this.currentConversation.conversationID) {
        this.$store.dispatch('checkoutConversation', this.conversation.conversationID);
      }
    },
    /**
     * 置顶会话
     * @param {Boolean} flag true - 置顶；false - 取消置顶
     */
    pinConversation(flag) {
      let options = {
        conversationID: this.conversation.conversationID,
        isPinned: flag
      };
      this.tim
        .pinConversation(options)
        .then(res => {})
        .catch(err => {});
    },
    /**
     * 删除会话
     */
    deleteConversation() {
      this.tim
        .deleteConversation(this.conversation.conversationID)
        .then(() => {
          this.$store.commit('showMessage', {
            message: `会话【${this.conversation.userProfile.nick}】删除成功!`,
            type: 'success'
          });
          this.popoverVisible = false;
          this.$store.commit('resetCurrentConversation');
        })
        .catch(error => {
          this.$store.commit('showMessage', {
            message: `会话【${this.conversation.userProfile.nick}】删除失败!, error=${error.message}`,
            type: 'error'
          });
          this.popoverVisible = false;
        });
    },
    /**
     * 显示上下文菜单
     */
    showContextMenu() {
      this.popoverVisible = true;
    }
  },
  computed: {
    ...mapState({
      currentConversation: state => state.conversation.currentConversation,
      currentUserProfile: state => state.user.currentUserProfile,
      excludeCoversationList: state => state.conversation.excludeCoversationList
    }),
    ...mapGetters(['toAccount']),
    /**
     * 日期
     */
    date() {
      if (!this.conversation.lastMessage || !this.conversation.lastMessage.lastTime) {
        return '';
      }
      const date = new Date(this.conversation.lastMessage.lastTime * 1000);
      if (isToday(date)) {
        return getTime(date);
      }
      return getDate(date);
    },
    /**
     * 头像
     */
    avatar() {
      switch (this.conversation.type) {
        case 'GROUP':
          return this.conversation.groupProfile.avatar;
        case 'C2C':
          return this.conversation.userProfile.avatar;
        default:
          return '';
      }
    },
    /**
     * 未读数量
     */
    showUnreadCount() {
      if (this.$store.getters.hidden) {
        return this.conversation.unreadCount > 0;
      }
      // 是否显示未读计数。当前会话和未读计数为0的会话，不显示。
      return (
        this.currentConversation.conversationID !== this.conversation.conversationID &&
        this.conversation.unreadCount > 0
      );
    },
    /**
     * 最新消息预览
     */
    messageForShow() {
      if (this.conversation.lastMessage.isRevoked) {
        if (this.conversation.lastMessage.fromAccount === this.currentUserProfile.userID) {
          return '你撤回了一条消息';
        }
        if (this.conversation.type === this.TIM.TYPES.CONV_C2C) {
          return '对方撤回了一条消息';
        }
        return `${this.conversation.lastMessage.fromAccount}撤回了一条消息`;
      }
      return this.conversation.lastMessage.messageForShow;
    }
    /**
     * 有关于我的消息(群组中)
     */
    /**
    hasMessageAtMe() {
      return (
        this.currentConversation.conversationID !== this.conversation.conversationID &&
        this.conversation.groupAtInfoList &&
        this.conversation.groupAtInfoList.length > 0
      );
    },
    */
    /**
     * 关于我的消息文本 - '@我' | '@所有人'
     */
    /**
    messageAtMeText() {
      let text = '';
      if (this.conversation.groupAtInfoList.length > 0) {
        this.conversation.groupAtInfoList.forEach(item => {
          if (item.atTypeArray[0] === this.TIM.TYPES.CONV_AT_ME) {
            text.indexOf('[@所有人]') !== -1 ? (text = '[@所有人][有人@我]') : (text = '[有人@我]');
          }
          if (item.atTypeArray[0] === this.TIM.TYPES.CONV_AT_ALL) {
            text.indexOf('[有人@我]') !== -1 ? (text = '[有人@我][@所有人]') : (text = '[@所有人]');
          }
          if (item.atTypeArray[0] === this.TIM.TYPES.CONV_AT_ALL_AT_ME) {
            text = '[@所有人][有人@我]';
          }
        });
      }
      return text;
    }
    */
  }
};
</script>

<style lang="less" scoped>
.coversation-list-item {
  width: 100%;
  position: relative;
  & + .coversation-list-item {
    margin-top: 8px;
  }
  .el-dropdown {
    width: 100%;
  }
  &.is-pinned {
    .coversation-list-item-content {
      background-color: rgba(239, 239, 239, 0.9);
    }
  }
  &.selected {
    .coversation-list-item-content {
      background-color: rgba(131, 204, 210, 0.2);
    }
  }
  .coversation-list-item-content {
    margin: auto;
    width: 96%;
    height: 64px;
    cursor: pointer;
    display: flex;
    padding: 10px 12px;
    border-radius: 8px;
    box-sizing: border-box;
    align-items: center;
    color: #333;
    transition: all 0.5s ease-in-out;
    background-color: rgba(239, 239, 239, 0.3);
    .avatar {
      position: relative;
      margin-right: 12px;
      text-align: center;
      width: 44px;
      height: 44px;
      flex-shrink: 0;
      img {
        border-radius: 50%;
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
      .unread-count {
        position: absolute;
        top: -6px;
        right: -9px;
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
    .item-main {
      width: 0;
      flex-grow: 1;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      .top {
        position: relative;
        display: flex;
        justify-content: space-between;
        .name {
          text-align: left;
          color: #666;
          font-weight: bold;
          font-size: 14px;
        }
        .date {
          font-size: 12px;
          padding-left: 10px;
          -ms-flex-negative: 0;
          flex-shrink: 0;
          text-align: right;
          color: #76828c;
        }
      }
      .summary {
        width: 100%;
        text-align: left;
        .text {
          font-size: 12px;
          color: #a5b5c1;
        }
      }
      .text-ellipsis {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}
</style>

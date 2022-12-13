<template>
  <div class="message-wrapper" :class="messagePosition" v-if="!message.isDeleted">
    <!-- 单聊模式 -->
    <div class="c2c-layout" :class="messagePosition">
      <Avatar v-if="showAvatar" :src="avatar" :text="message.nick" />
      <div class="message-main-box">
        <!-- 上层消息体 -->
        <div class="message-content-box">
          <MessageStatusIcon :message="message" />
          <TextElement
            v-if="message.type === TIM.TYPES.MSG_TEXT"
            :isMine="isMine"
            :payload="message.payload"
            :message="message" />
          <ImageElement
            v-else-if="message.type === TIM.TYPES.MSG_IMAGE"
            class="image-message"
            :isMine="isMine"
            :payload="message.payload"
            :message="message" />
          <FileElement
            v-else-if="message.type === TIM.TYPES.MSG_FILE"
            :isMine="isMine"
            :payload="message.payload"
            :message="message" />
          <SoundElement
            v-else-if="message.type === TIM.TYPES.MSG_SOUND"
            :isMine="isMine"
            :payload="message.payload"
            :message="message" />
          <FaceElement
            v-else-if="message.type === TIM.TYPES.MSG_FACE"
            class="face-message"
            :isMine="isMine"
            :payload="message.payload"
            :message="message" />
          <VideoElement
            v-else-if="message.type === TIM.TYPES.MSG_VIDEO"
            :isMine="isMine"
            :payload="message.payload"
            :message="message" />
          <span v-else>暂未支持的消息类型：{{ message.type }}</span>
        </div>
        <!-- 下层时间 -->
        <MessageFooter v-if="showMessageHeader" :message="message" />
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import Avatar from '@/components/Chat/Message/Avatar';
import TextElement from '@/components/Chat/Message/Elements/TextElement';
import FaceElement from '@/components/Chat/Message/Elements/FaceElement';
import FileElement from '@/components/Chat/Message/Elements/FileElement';
import ImageElement from '@/components/Chat/Message/Elements/ImageElement';
import SoundElement from '@/components/Chat/Message/Elements/SoundElement';
import VideoElement from '@/components/Chat/Message/Elements/VideoElement';
import MessageStatusIcon from '@/components/Chat/Message/MessageStatusIcon';
import MessageFooter from '@/components/Chat/Message/MessageFooter';

export default {
  components: {
    Avatar,
    TextElement,
    FaceElement,
    FileElement,
    ImageElement,
    SoundElement,
    VideoElement,
    MessageStatusIcon,
    MessageFooter
  },
  props: {
    message: {
      type: Object,
      required: true
    }
  },
  data() {
    return {};
  },
  methods: {},
  computed: {
    ...mapState({
      currentConversation: state => state.conversation.currentConversation,
      currentUserProfile: state => state.user.currentUserProfile
    }),
    /**
     * 消息位置类
     */
    messagePosition() {
      if (['TIMGroupTipElem', 'TIMGroupSystemNoticeElem'].includes(this.message.type)) {
        return 'pos-center';
      }
      if (this.message.isRevoked) {
        // 撤回消息
        return 'pos-center';
      }
      if (this.isMine) {
        return 'pos-right';
      } else {
        return 'pos-left';
      }
    },
    /**
     * 是否是我本人
     */
    isMine() {
      // console.log(this.currentUserProfile, this.currentConversation);
      return this.message.flow === 'out';
    },
    showMessageHeader() {
      if (['TIMGroupTipElem', 'TIMGroupSystemNoticeElem'].includes(this.message.type)) {
        return false;
      }
      if (this.message.isRevoked) {
        // 撤回消息
        return false;
      }
      return true;
    },
    // 是否显示头像，群提示消息不显示头像
    showAvatar() {
      if (this.currentConversation.type === 'C2C' && !this.message.isRevoked) {
        // C2C且没有撤回的消息
        return true;
      } else if (this.currentConversation.type === 'GROUP' && !this.message.isRevoked) {
        // group且没有撤回的消息
        return this.message.type !== this.TIM.TYPES.MSG_GRP_TIP;
      }
      return false;
    },
    avatar() {
      if (this.currentConversation.type === 'C2C') {
        return this.message.avatar;
      } else if (this.currentConversation.type === 'GROUP') {
        return this.isMine ? this.currentUserProfile.avatar : this.message.avatar;
      } else {
        return '';
      }
    },
    currentConversationType() {
      return this.currentConversation.type;
    }
  }
};
</script>

<style lang="less" scoped>
.message-wrapper {
  box-sizing: border-box;
  margin: 20px 0;
  .c2c-layout {
    display: flex;
    // 仿小米logo
    .avatar-box {
      width: 56px;
      height: 56px;
      box-shadow: rgba(0, 0, 0, 0.1) 0px 5px 10px 0px;
      border-radius: 19px;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .message-main-box {
      display: flex;
      flex-direction: column;
      padding: 8px 12px 0 8px;
      .message-content-box {
        display: flex;
        box-sizing: border-box;
        align-items: center;
      }
    }
    &.pos-center {
      justify-content: center;
    }
    &.pos-left {
      .message-main-box {
        align-items: flex-start;
      }
    }
    &.pos-right {
      flex-direction: row-reverse;
      .message-main-box {
        align-items: flex-end;
      }
    }
  }
}
</style>

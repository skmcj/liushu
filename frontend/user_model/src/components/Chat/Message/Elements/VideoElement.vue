<template>
  <MessageBubble :isMine="isMine" :message="message">
    <video :src="payload.videoUrl" controls class="video" @error="videoError"></video>
    <el-progress v-if="showProgressBar" :percentage="percentage" color="#83ccd2" />
  </MessageBubble>
</template>

<script>
import MessageBubble from '@/components/Chat/Message/MessageBubble';

export default {
  name: 'VideoElement',
  components: {
    MessageBubble
  },
  props: {
    payload: {
      type: Object,
      required: true
    },
    message: {
      type: Object,
      required: true
    },
    isMine: {
      type: Boolean
    }
  },

  computed: {
    showProgressBar() {
      return this.message.status === 'unSend';
    },
    percentage() {
      return Math.floor((this.$parent.message.progress || 0) * 100);
    }
  },
  methods: {
    videoError(e) {
      this.$store.commit('showMessage', { type: 'error', message: '视频出错，错误原因：' + e.target.error.message });
    }
  }
};
</script>

<style lang="less" scoped>
.video {
  width: 100%;
  max-height: 300px;
}
</style>

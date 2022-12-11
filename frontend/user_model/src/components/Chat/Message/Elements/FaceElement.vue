<template>
  <MessageBubble :isMine="isMine" :message="message">
    <div class="face-element-wrapper">
      <img :src="url" />
    </div>
  </MessageBubble>
</template>

<script>
import MessageBubble from '@/components/Chat/Message/MessageBubble';

export default {
  name: 'FaceElement',
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
  components: {
    MessageBubble
  },
  computed: {
    url() {
      let name = '';
      if (this.payload.data.indexOf('@2x') > 0) {
        name = this.payload.data;
      } else {
        name = this.payload.data + '@2x';
      }
      return `https://web.sdk.qcloud.com/im/assets/face-elem/${name}.png`;
    }
  }
};
</script>

<style lang="less" scoped>
.face-element-wrapper {
  img {
    max-width: 90px;
  }
}
</style>

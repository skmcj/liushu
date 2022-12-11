<template>
  <MessageBubble :isMine="isMine" :message="message">
    <template v-for="(item, index) in contentList">
      <span class="text-box" :key="index" v-if="item.name === 'text'">{{ item.text }}</span>
      <img class="text-img" v-else-if="item.name === 'img'" :src="item.src" :key="index" />
    </template>
  </MessageBubble>
</template>

<script>
import MessageBubble from '@/components/Chat/Message/MessageBubble';
import { decodeText } from '@/utils/im/imHandle';

export default {
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
    contentList() {
      return decodeText(this.payload);
    }
  }
};
</script>

<style lang="less" scoped>
.text-box {
  white-space: pre-wrap;
  font-size: inherit;
  word-break: break-word;
  line-height: 20px;
}
.text-img {
  border: 0;
  vertical-align: middle;
  width: 20px;
  height: 20px;
}
</style>

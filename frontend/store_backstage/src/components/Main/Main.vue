<template>
  <div class="main">
    <div class="content">
      <router-view></router-view>
    </div>
    <ChatWindow :title="chatTitle" :visible.sync="chatVisible" />
  </div>
</template>

<script>
import ChatWindow from '@/views/Common/ChatWindow';

export default {
  components: {
    ChatWindow
  },
  data() {
    return {
      chatTitle: undefined,
      chatVisible: false
    };
  },
  mounted() {
    this.$bus.$on('openChatWindow', this.openChatWindow);
  },
  methods: {
    /**
     * 打开聊天窗口
     */
    openChatWindow(val) {
      // console.log('chat', val);
      if (val === 'sys' || val === 'owner') {
        this.chatTitle = val;
      } else {
        this.chatTitle = 'sys';
      }
      this.chatVisible = true;
    }
  }
};
</script>

<style lang="less" scoped>
.main {
  width: 100%;
  flex: 1;
  overflow-y: scroll;
  &::-webkit-scrollbar {
    display: none;
  }
}
.content {
  margin: 42px;
}
</style>

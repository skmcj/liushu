<template>
  <ul class="coversation-list">
    <CoversationListItem v-for="item in conversationList" :conversation="item" :key="item.conversationID" />
  </ul>
</template>

<script>
import { mapState } from 'vuex';
import CoversationListItem from '@/components/Coversation/CoversationListItem';

export default {
  components: {
    CoversationListItem
  },
  data() {
    return {
      userID: '',
      isCheckouting: false, // 是否正在切换会话
      timeout: null
    };
  },
  computed: {
    ...mapState({
      conversationList: state => state.conversation.conversationList,
      currentConversation: state => state.conversation.currentConversation,
      excludeCoversationList: state => state.conversation.excludeCoversationList
    })
  },
  mounted() {
    window.addEventListener('keydown', this.handleKeydown);
  },
  destroyed() {
    window.removeEventListener('keydown', this.handleKeydown);
  },
  methods: {
    /**
     * 键盘按下
     */
    handleKeydown(event) {
      if ((event.keyCode !== 38 && event.keyCode !== 40) || this.isCheckouting) {
        return;
      }
      const currentIndex = this.conversationList.findIndex(
        item => item.conversationID === this.currentConversation.conversationID
      );
      if (event.keyCode === 38 && currentIndex - 1 >= 0) {
        this.checkoutPrev(currentIndex);
      }
      if (event.keyCode === 40 && currentIndex + 1 < this.conversationList.length) {
        this.checkoutNext(currentIndex);
      }
    },
    checkoutPrev(currentIndex) {
      this.isCheckouting = true;
      this.$store
        .dispatch('checkoutConversation', this.conversationList[currentIndex - 1].conversationID)
        .then(() => {
          this.isCheckouting = false;
        })
        .catch(() => {
          this.isCheckouting = false;
        });
    },
    checkoutNext(currentIndex) {
      this.isCheckouting = true;
      this.$store
        .dispatch('checkoutConversation', this.conversationList[currentIndex + 1].conversationID)
        .then(() => {
          this.isCheckouting = false;
        })
        .catch(() => {
          this.isCheckouting = false;
        });
    },
    /**
     * 刷新会话列表
     */
    handleRefresh() {
      if (!this.timeout) {
        this.timeout = setTimeout(() => {
          this.timeout = null;
          this.tim.getConversationList().then(() => {
            this.$store.commit('showMessage', {
              message: '刷新成功',
              type: 'success'
            });
          });
        }, 1000);
      }
    }
  }
};
</script>

<style lang="less" scoped>
.coversation-list {
  margin: 0;
  padding: 0;
  margin-top: 12px;
  position: absolute;
  width: 100%;
  list-style: none;
}
</style>

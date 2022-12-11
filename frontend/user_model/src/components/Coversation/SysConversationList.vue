<template>
  <div class="sys-conversation-list">
    <div
      class="sys-conversation-item"
      :class="{
        selected: asideItem === 'customer'
      }"
      @click="selectConversation('customer')">
      <div class="cover">
        <img :src="CustomerServiceSvg" alt="客服" />
      </div>
      <div class="mess">
        <div class="title">在线客服</div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import CustomerServiceSvg from '@/assets/images/svg/customer-service.svg';

export default {
  data() {
    return {
      CustomerServiceSvg,
      asideItem: ''
    };
  },
  methods: {
    /**
     * 选择会话
     */
    selectConversation(text) {
      this.asideItem = text;
      if (text === 'customer' && this.currentConversation.conversationID !== 'customerservice') {
        this.$store.dispatch('checkoutConversation', 'C2Ccustomerservice');
      }
    }
  },
  computed: {
    ...mapState({
      currentConversation: state => state.conversation.currentConversation
    })
  }
};
</script>

<style lang="less" scoped>
.sys-conversation-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin-top: 12px;
  .sys-conversation-item {
    cursor: pointer;
    padding: 10px 12px;
    box-sizing: border-box;
    width: 96%;
    height: 64px;
    display: flex;
    align-items: center;
    border-radius: 8px;
    transition: all 0.5s ease-in-out;
    background-color: rgba(239, 239, 239, 0.3);
    &.selected {
      background-color: rgba(131, 204, 210, 0.2);
    }
    .cover {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      margin-right: 12px;
      img {
        width: 100%;
        height: 100%;
        // border-radius: 50%;
      }
    }
    .mess {
      width: 0;
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      .title {
        text-align: left;
        color: #666;
        font-weight: bold;
        font-size: 16px;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
      }
    }
  }
}
</style>

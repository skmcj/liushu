<template>
  <div class="coversation">
    <!-- 消息类别 -->
    <div class="coversation-header">
      <div class="item" :class="{ active: headerType === 'sys' }" @click="handleHeader('sys')">
        <i class="ic-mess-2"></i>
        <span>系统消息</span>
      </div>
      <div class="item" :class="{ active: headerType === 'owner' }" @click="handleHeader('owner')">
        <i class="ic-mess-1"></i>
        <span>私信</span>
      </div>
    </div>
    <div class="coversation-main">
      <SysConversationList v-if="headerType === 'sys'" />
      <CoversationList v-else-if="headerType === 'owner'" />
    </div>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import CoversationList from '@/components/Coversation/CoversationList';
import SysConversationList from '@/components/Coversation/SysConversationList';

export default {
  props: {
    title: String
  },
  components: {
    CoversationList,
    SysConversationList
  },
  data() {
    return {
      headerType: 'sys',
      activeName: 'conversation-list'
    };
  },
  created() {
    if (this.title) {
      this.headerType = this.title;
    }
  },
  mounted() {
    this.$bus.$on('checkoutConversation', () => {
      this.checkoutActive(this.activeName);
    });
  },
  methods: {
    handleHeader(text) {
      if (text !== this.headerType) {
        this.$store.commit('resetCurrentConversation');
        this.headerType = text;
      } else {
        this.headerType = text;
      }
    }
  },
  computed: {
    ...mapGetters(['totalUnreadCount']),
    ...mapState({
      userID: state => state.user.userID
    })
  }
};
</script>

<style lang="less" scoped>
.coversation {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  .coversation-header {
    position: relative;
    width: 100%;
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    box-shadow: 0px 1px 0px 0px #e6e6e6;
    .item {
      position: relative;
      cursor: pointer;
      user-select: none;
      width: 42%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #666;
      transition: all 0.5s ease-in-out;
      i {
        font-size: 18px;
        &.ic-mess-2 {
          font-size: 24px;
        }
      }
      span {
        margin-left: 8px;
        font-size: 16px;
        font-weight: bold;
      }
      &::after {
        transition: all 0.5s ease-in-out;
        content: '';
        display: block;
        width: 0%;
        height: 4px;
        border-radius: 2px;
        position: absolute;
        bottom: 0;
        background-color: #83ccd2;
      }
      &.active {
        color: #83ccd2;
        &::after {
          width: 100%;
        }
      }
    }
    &::before {
      content: '';
      display: block;
      position: absolute;
      bottom: -12px;
      left: 0;
      width: 100%;
      height: 12px;
      z-index: 999;
      background: linear-gradient(180deg, #e6e6e6, rgba(255, 255, 255, 0));
    }
  }
  .coversation-main {
    position: relative;
    width: 100%;
    flex-grow: 1;
    overflow-y: auto;
    box-sizing: border-box;
    &::-webkit-scrollbar {
      display: none;
    }
  }
}
</style>

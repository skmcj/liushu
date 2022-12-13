<template>
  <div class="chat-window" v-if="!closed" :class="{ 'window-show': !classFlag, 'window-hide': classFlag }">
    <div class="window-main">
      <div class="window-left">
        <Coversation :title="title" />
      </div>
      <div class="window-right">
        <Chat />
      </div>
    </div>
    <SuspPanel class="susp-pos" @handleItem="handleSuspItem" />
    <ImagePreviewer />
  </div>
</template>

<script>
import SuspPanel from '@/views/Common/SuspPanel';
import Chat from '@/components/Chat/Chat';
import Coversation from '@/components/Coversation/Coversation';
import ImagePreviewer from '@/components/Chat/Message/ImagePreviewer';
import domHandle from '@/utils/domHandleUtil';

export default {
  components: {
    SuspPanel,
    Chat,
    Coversation,
    ImagePreviewer
  },
  props: {
    title: String,
    /**
     * 控制窗口的可见性
     */
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // 控制窗口的动画类，true - 显示，false - 隐藏
      classFlag: false,
      // 窗口是否关闭
      closed: true,
      isLockScroll: true,
      withoutHiddenClass: true
    };
  },
  created() {},
  methods: {
    /**
     * 关闭该窗口
     */
    handleClose() {
      this.restoreBodyStyle();
      this.classFlag = true;
      setTimeout(() => {
        this.closed = true;
        this.$emit('update:visible', false);
      }, 480);
    },
    /**
     * 是否锁定滚动
     */
    lockScroll() {
      if (this.isLockScroll) {
        // 如果是的话
        // 这边的话 判断body是不是有 sk-hidden-scroll
        this.withoutHiddenClass = !domHandle.hasClass(document.body, 'sk-hidden-scroll');
        // 没有的话
        if (this.withoutHiddenClass) {
          // 获取到 body的 padding-right
          this.bodyPaddingRight = document.body.style.paddingRight;
          this.computedBodyPaddingRight = parseInt(domHandle.getStyle(document.body, 'paddingRight'), 10);
        }
        // 获取到 滚动条的 宽度
        let scrollBarWidth = domHandle.getScrollBarWidth();
        // 当前body是否是 超出的状态 也就是说有没有滚动条了
        const bodyHasOverflow = document.documentElement.clientHeight < document.body.scrollHeight;
        // 查看body  overflowY 属性
        const bodyOverflowY = domHandle.getStyle(document.body, 'overflowY') || 'scroll';
        // 总的来说这边条件就是说 body边上 有滚动条了 那么就给body加上相应的 padding-right
        // 免得 body 设置上 overflow 为 hidden的时候滚动条消失 页面变宽  发生页面的抖动
        if (scrollBarWidth > 0 && (bodyHasOverflow || bodyOverflowY === 'scroll') && this.withoutHiddenClass) {
          document.body.style.paddingRight = `${this.computedBodyPaddingRight + scrollBarWidth}px`;
        }
        domHandle.addClass(document.body, 'sk-hidden-scroll');
      }
    },
    restoreBodyStyle() {
      if (this.withoutHiddenClass) {
        document.body.style.paddingRight = 0;
        domHandle.removeClass(document.body, 'sk-hidden-scroll');
      }
      this.withoutHiddenClass = true;
    },
    handleSuspItem(val) {
      console.log('susp =>', val);
      switch (val) {
        case 'home':
          this.handleClose();
          this.$router.push('/');
          break;
        case 'cate':
          this.handleClose();
          this.$router.push('/cate');
          break;
        case 'about':
          this.handleClose();
          this.$router.push('/about');
          break;
        case 'mine':
          this.handleClose();
          this.$router.push('/mine');
          break;
        case 'close':
          this.handleClose();
          break;
        default:
          break;
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.classFlag = false;
        this.closed = false;
        this.lockScroll();
      } else {
        this.handleClose();
      }
    }
  }
};
</script>

<style lang="less" scoped>
.chat-window {
  z-index: 999;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.3);
  .window-main {
    position: relative;
    display: flex;
    width: 92vw;
    height: 92vh;
    background-color: #f1f2f3;
    border-radius: 12px;
    overflow: hidden;
    .window-left {
      min-width: 285px;
      flex: 0 0 24%;
      background-color: #fff;
      border-right: 1px solid #f3f1f2;
      display: flex;
      flex-direction: column;
      align-items: center;
    }
    .window-right {
      box-sizing: border-box;
      padding: 8px 18px;
      flex-grow: 1;
      height: 100%;
      position: relative;
    }
  }
}
.susp-pos {
  position: absolute;
  left: 0;
  bottom: 36px;
}
.window-show {
  animation: enlarge-in 0.5s ease-in-out;
}
.window-hide {
  animation: enlarge-out 0.5s ease-in-out;
}
@keyframes enlarge-in {
  0% {
    scale: 0;
    opacity: 0;
  }
  100% {
    scale: 1;
    opacity: 1;
  }
}
@keyframes enlarge-out {
  0% {
    scale: 1;
    opacity: 1;
  }
  100% {
    scale: 0;
    opacity: 0;
  }
}
</style>

<template>
  <div class="sk-tool-cate-list">
    <!-- 工具按钮 -->
    <div class="sk-tool-cate-list-btn" @click.stop="clickShowList">
      <i class="sk-tool-icon ic-cate-1"></i>
      <span class="sk-tool-text">分类</span>
    </div>
    <div v-if="isShowList" class="sk-tool-cate-list-content">
      <div class="sk-tool-cate__list">
        <div class="sk-tool-cate__list_title">
          <i class="sk-tool-cate-title__icon ic-cate-1"></i>
          <span class="sk-tool-cate-title__text">分类</span>
        </div>
        <div class="sk-tool-cate__list_box">
          <div
            class="sk-tool-cate__list_item"
            v-for="(item, index) in cateList"
            :key="'sk-tool-cate-' + index"
            :class="{ 'is-active': isActive === '#' + item.name }"
            @click.stop="handleClickItem('#' + item.name)">
            {{ item.name }}
          </div>
        </div>
      </div>
      <div class="sk-tool-cate-list-modal" @click.stop="isShowList = false"></div>
    </div>
  </div>
</template>

<script>
import domHandle from '@/utils/domHandleUtil';

export default {
  props: {
    cateList: Array,
    handleClick: Function,
    modal: {
      type: Boolean,
      default: true
    },
    isLockScroll: {
      type: Boolean,
      default: true
    },
    isActive: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      isShowList: false,
      withoutHiddenClass: true
    };
  },
  methods: {
    open() {
      this.lockScroll(this.isShowList);
    },
    close() {
      if (this.isLockScroll) {
        this.restoreBodyStyle();
      }
    },
    clickShowList() {
      if (this.handleClick) {
        this.handleClick();
        return;
      }
      this.isShowList = true;
    },
    handleClickItem(hash) {
      this.isShowList = false;
      window.location.hash = hash;
      this.$emit('listenActiveHash', hash);
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
      if (this.modal && this.withoutHiddenClass) {
        document.body.style.paddingRight = 0;
        domHandle.removeClass(document.body, 'sk-hidden-scroll');
      }
      this.withoutHiddenClass = true;
    }
  },
  watch: {
    isShowList: {
      immediate: true,
      handler(val) {
        if (val) {
          this.open();
        } else {
          this.close();
        }
      }
    }
  }
};
</script>

<style lang="less" scoped>
.sk-tool-cate-list {
  .sk-tool-cate-list-btn {
    cursor: pointer;
    user-select: none;
    width: var(--sk-tool-width);
    padding: 8px 0;
    border-radius: 5px;
    border: 1px solid #cbcbcb;
    background-color: #fff;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    .sk-tool-icon {
      font-size: 24px;
    }
    .sk-tool-text {
      font-size: 12px;
      color: #3d3d3d;
      margin-top: 3px;
    }
  }
  .sk-tool-cate-list-content {
    animation: fade-in 0.2s ease-in-out;
    overflow: hidden;
    z-index: 997;
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    .sk-tool-cate-list-modal {
      z-index: 998;
      position: fixed;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      background-color: rgba(0, 0, 0, 0.3);
    }
    .sk-tool-cate__list {
      z-index: 999;
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-50%);
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      padding: 24px 14px 27px 14px;
      background-color: #fff;
      border-radius: 8px 0 0 8px;
      .sk-tool-cate__list_title {
        display: flex;
        align-items: center;
        justify-content: center;
        .sk-tool-cate-title__icon {
          font-size: 18px;
          margin-right: 5px;
        }
        .sk-tool-cate-title__text {
          font-size: 18px;
          color: #3d3d3d;
          font-weight: bold;
        }
      }
      .sk-tool-cate__list_box {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-top: 35px;
        .sk-tool-cate__list_item {
          cursor: pointer;
          user-select: none;
          font-size: 16px;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 8px;
          padding: 7px 32px;
          & + .sk-tool-cate__list_item {
            margin-top: 8px;
          }
          &.is-active {
            background-color: #e3e5e7;
          }
          &:not(.is-active) {
            &:hover {
              background-color: rgba(227, 229, 231, 0.5);
            }
          }
        }
      }
    }
  }
}
@keyframes fade-in {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
</style>

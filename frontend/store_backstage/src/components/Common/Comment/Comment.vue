<template>
  <!-- 评论 -->
  <div class="ls-comment-box">
    <div class="ls-goods-comment-top">
      <!-- 顶部商品信息 -->
      <div class="ls-goods-mess">
        <div class="ls-goods-cover">
          <img :src="comment.bookCover" alt="ls-goods-cover" />
        </div>
        <div class="ls-goods-name">{{ comment.bookName }}</div>
      </div>
      <!-- 评分 -->
      <div class="ls-goods-rate-box">
        <div class="ls-goods-rate-title">评分：</div>
        <div class="ls-goods-rate">
          <el-rate
            :disabled="disabled"
            v-model="score"
            :max="5"
            allow-half
            :icon-classes="['ic-score', 'ic-score', 'ic-score']"
            :colors="['#83ccd2', '#83ccd2', '#83ccd2']"
            void-icon-class="ic-score"
            disabled-void-icon-class="ic-score"
            show-score
            text-color="#83ccd2"
            disabled-void-color="#999999"
            score-template="{value}">
          </el-rate>
        </div>
      </div>
    </div>
    <!-- 评论内容 -->
    <div class="ls-goods-comment-content">
      <el-input
        type="textarea"
        :rows="5"
        placeholder="请输入评价内容"
        :disabled="disabled"
        v-model="content"></el-input>
    </div>
    <!-- 回复内容 -->
    <div class="ls-goods-reply-box" v-if="comment.reply">
      <Reply :reply="comment.reply" />
    </div>
    <!-- 回复工具 -->
    <div
      v-if="isReply"
      class="ls-goods-reply-input"
      :class="{
        out: isOut
      }">
      <el-input type="textarea" :rows="2" placeholder="请输入您的回复内容" v-model="replyContent"></el-input>
    </div>
    <!-- 底下操作工具 -->
    <div class="ls-goods-comment-tools">
      <div class="tool-left">
        <!-- 删除评价 -->
        <div v-if="disabled && isDelete" class="tool-btn tool-item" @click="handleDelete">
          <i class="ic-delete" title="删除评价"></i>
        </div>
      </div>
      <div class="tool-right">
        <!-- 提交按钮 -->
        <div v-if="!disabled" class="tool-item">
          <el-button type="primary" @click="handleConfirm">确认 评价</el-button>
        </div>
        <!-- 回复 -->
        <div v-if="disabled" class="tool-item">
          <el-button type="primary" @click="handleToReply">{{ isReply ? '确认 回复' : '回复' }}</el-button>
        </div>
        <!-- 关闭回复 -->
        <div v-if="isReply" class="ls-goods-reply-return tool-item" @click="handleCloseReply">
          <span class="ls-goods-text-btn">返回</span>
        </div>
        <!-- 是否匿名 -->
        <!-- <div v-if="!isReply" class="ls-goods-comment-anonymity tool-item">
          <div class="text">{{ comment.isAnonymous === 0 ? '未匿名' : '已匿名' }}</div>
          <div class="switch">
            <el-switch
              :disabled="disabled"
              v-model="isAnonymous"
              active-color="#83ccd2"
              :active-value="1"
              :inactive-value="0"></el-switch>
          </div>
        </div> -->
      </div>
    </div>
  </div>
</template>

<script>
import Reply from '@/components/Common/Comment/Reply';

export default {
  components: {
    Reply
  },
  props: {
    itemId: String,
    // 评论
    comment: Object,
    disabled: {
      type: Boolean,
      default: false
    },
    isDelete: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      score: 0,
      content: '',
      isAnonymous: 0,
      // 是否要回复
      isOut: false,
      isReply: false,
      replyContent: ''
    };
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      this.score = this.comment.score;
      this.content = this.comment.content;
      this.isAnonymous = this.comment.isAnonymous;
      this.isOut = false;
      this.isReply = false;
      this.replyContent = '';
    },
    /**
     * 删除评价
     */
    handleDelete() {
      this.$emit('delete', this.comment.id);
    },
    /**
     * 确认评价
     */
    handleConfirm() {
      if (this.$isEmpty(this.content.trim())) {
        this.content = '';
        this.$showMsg('评价内容不可为空');
        return;
      }
      let comment = {
        orderId: this.comment.orderId,
        orderItemId: this.itemId ? this.itemId : this.comment.orderItemId,
        bookId: this.comment.bookId,
        storeId: this.comment.storeId,
        content: this.content,
        score: this.score,
        isAnonymous: this.isAnonymous
      };
      this.$emit('submit', comment);
    },
    /**
     * 回复
     */
    handleToReply() {
      // 去回复，划入输入框
      if (!this.isReply) {
        this.isReply = true;
      } else {
        // 确认回复
        this.handleReply();
      }
    },
    handleCloseReply() {
      // 改变回复框动画 - 划出动画
      this.isOut = true;
      setTimeout(() => {
        this.isReply = false;
        this.isOut = false;
      }, 420);
    },
    /**
     * 确认回复
     */
    handleReply() {
      if (this.$isEmpty(this.replyContent.trim())) {
        this.replyContent = '';
        this.$showMsg('回复内容不可为空');
        return;
      }
      // 计算回复父评论ID
      let commentId = this.getReplyId(this.comment);
      let reply = {
        commentId: commentId,
        content: this.replyContent
      };
      this.$emit('reply', reply);
    },
    /**
     * 递归获取叶子回复Id
     */
    getReplyId(reply) {
      if (reply.reply) return this.getReplyId(reply.reply);
      return reply.id;
    }
  },
  watch: {
    /**
     * 监听传入评价
     * 当评价改变时，初始化数据
     */
    comment(val) {
      // console.log('comment => ', val);
      this.initData();
    }
  }
};
</script>

<style lang="less" scoped>
.ls-comment-box {
  width: 100%;
}
.ls-goods-comment-top {
  width: 100%;
  height: 42px;
  border-radius: 8px;
  background-color: #f6f6f6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .ls-goods-mess {
    margin-left: 12px;
    display: flex;
    align-items: center;
    .ls-goods-cover {
      width: 36px;
      height: 36px;
      border-radius: 3px;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .ls-goods-name {
      margin-left: 8px;
      font-size: 14px;
      font-weight: bold;
      color: var(--primary-text);
    }
  }
  .ls-goods-rate-box {
    margin-right: 18px;
    display: flex;
    align-items: center;
    .ls-goods-rate-title {
      font-size: 14px;
      color: #666666;
    }
    .ls-goods-rate {
      margin-left: 5px;
      :deep(.el-rate) {
        .el-rate__text {
          text-align: center;
          display: inline-block;
          min-width: 20px;
        }
      }
    }
  }
}
.ls-goods-comment-content {
  margin-top: 8px;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f8f8f8;
  :deep(.el-textarea) {
    .el-textarea__inner {
      border: none;
      background-color: transparent;
    }
  }
}
.ls-goods-reply-box {
  margin-top: 8px;
  width: 96%;
  display: flex;
  flex-direction: column;
  margin-left: 4%;
}
.ls-goods-reply-input {
  margin-top: 8px;
  width: 96%;
  margin-left: 4%;
  :deep(.el-textarea) {
    .el-textarea__inner {
      border-radius: 8px;
    }
  }
  animation-name: transfer-in;
  animation-duration: 0.5s;
  animation-timing-function: ease-in-out;
  &.out {
    animation-name: transfer-out;
  }
}
.ls-goods-comment-tools {
  margin-top: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .tool-left,
  .tool-right {
    display: flex;
    align-items: center;
  }
  .tool-left {
    justify-content: flex-start;
    .tool-btn {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 24px;
      height: 24px;
      font-size: 24px;
      .ic-delete {
        color: #999;
        &:hover {
          color: #ee827c;
        }
      }
      i {
        transition: all 0.5s ease-in-out;
        font-size: 18px;
      }
    }
    .tool-item {
      & + .tool-item {
        margin-left: 12px;
      }
    }
  }
  .tool-right {
    flex-direction: row-reverse;
    justify-content: flex-end;
    .tool-item {
      & + .tool-item {
        margin-right: 12px;
      }
    }
  }
  .tool-item {
    & + .tool-item {
      margin-right: 12px;
    }
  }
}
.ls-goods-reply-return {
  min-width: 32px;
  user-select: none;
  cursor: pointer;
  font-size: 14px;
  color: #999;
  transition: all 0.5s ease-in-out;
  text-align: center;
  &:hover {
    color: #83ccd2;
  }
}
.ls-goods-comment-anonymity {
  user-select: none;
  margin-top: 8px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  .text {
    font-size: 12px;
    color: #999;
    margin-right: 6px;
  }
  .switch {
  }
}
@keyframes transfer-in {
  0% {
    opacity: 0;
    transform: translateY(-37px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
@keyframes transfer-out {
  0% {
    opacity: 1;
    transform: translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateY(-37px);
  }
}
</style>

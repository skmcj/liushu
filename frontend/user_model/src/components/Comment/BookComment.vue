<template>
  <div class="ls-bcm">
    <!-- 头像 -->
    <div class="ls-bcm-avatar-box">
      <!-- 匿名 -->
      <template v-if="isAnonymous">
        <div class="ls-bcm-avatar">
          <img :src="$noAvatarUrl" alt="匿名用户" />
        </div>
        <div class="ls-bcm-name">匿名用户</div>
      </template>
      <!-- 不匿名 -->
      <template v-else>
        <div class="ls-bcm-avatar">
          <img :src="avatar ? avatar : $noAvatarUrl" alt="加载失败" />
        </div>
        <div class="ls-bcm-name">{{ name }}</div>
      </template>
    </div>
    <!-- 主要内容 -->
    <div class="ls-bcm-content-box">
      <!-- 评分 -->
      <div class="ls-bcm-score-box">
        <div class="ls-bcm-score">
          <el-rate
            :value="score"
            :max="5"
            :icon-classes="['ic-score', 'ic-score', 'ic-score']"
            :colors="['#83ccd2', '#83ccd2', '#83ccd2']"
            disabled-void-icon-class="ic-score"
            disabled
            show-score
            text-color="#83ccd2"
            disabled-void-color="#efefef"
            score-template="{value}">
          </el-rate>
        </div>
        <div class="ls-bcm-time">{{ dateTime }}</div>
      </div>
      <!-- 评论内容 -->
      <div class="ls-bcm-content">{{ content }}</div>
      <!-- 回复 -->
      <BookReply class="ls-bcm-reply-main" :reply="reply" />
    </div>
  </div>
</template>

<script>
import BookReply from '@/components/Comment/BookReply';

export default {
  components: {
    BookReply
  },
  props: {
    // 是匿名
    isAnonymous: {
      type: Boolean,
      default: false
    },
    avatar: String,
    name: String,
    score: {
      type: Number,
      default: 0
    },
    dateTime: {
      type: String,
      default: ''
    },
    content: String,
    reply: Object
  }
};
</script>

<style lang="less" scoped>
.ls-bcm {
  display: flex;
  width: 100%;
  box-sizing: border-box;
  padding: 12px 12px 24px 12px;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.25);
  .ls-bcm-avatar-box {
    display: flex;
    flex-direction: column;
    align-items: center;
    min-width: 52px;
    max-width: 56px;
    .ls-bcm-avatar {
      width: 45px;
      height: 45px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .ls-bcm-name {
      margin-top: 3px;
      font-size: 12px;
      color: #666666;
      text-align: center;
    }
  }
  .ls-bcm-content-box {
    margin-left: 36px;
    flex-grow: 1;
    .ls-bcm-score-box {
      width: 100%;
      display: flex;
      justify-content: space-between;
      .ls-bcm-score {
        :deep(.el-rate) {
          .ic-score {
            font-size: 16px;
          }
          .el-rate__text {
            margin-left: 5px;
            font-size: 16px;
          }
        }
      }
      .ls-bcm-time {
        font-size: 10px;
        color: #d4dde1;
      }
    }
    .ls-bcm-content {
      width: 100%;
      margin-top: 18px;
      margin-bottom: 16px;
      font-size: 12px;
      color: #999999;
    }
  }
}
</style>

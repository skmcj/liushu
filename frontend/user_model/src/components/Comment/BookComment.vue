<template>
  <div class="sk-bcm">
    <!-- 头像 -->
    <div class="sk-bcm-avatar-box">
      <!-- 匿名 -->
      <template v-if="isAnonymous">
        <div class="sk-bcm-avatar">
          <img :src="$noAvatarUrl" alt="匿名用户" />
        </div>
        <div class="sk-bcm-name">匿名用户</div>
      </template>
      <!-- 不匿名 -->
      <template v-else>
        <div class="sk-bcm-avatar">
          <img :src="avatar ? avatar : $noAvatarUrl" alt="加载失败" />
        </div>
        <div class="sk-bcm-name">{{ name }}</div>
      </template>
    </div>
    <!-- 主要内容 -->
    <div class="sk-bcm-content-box">
      <!-- 评分 -->
      <div class="sk-bcm-score-box">
        <div class="sk-bcm-score">
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
        <div class="sk-bcm-time">{{ dateTime }}</div>
      </div>
      <!-- 评论内容 -->
      <div class="sk-bcm-content">{{ content }}</div>
      <!-- 商家回复 -->
      <div class="sk-bcm-reply-box" v-if="reply.length > 0">
        <div class="sk-bcm-reply__title">商家回复</div>
        <div class="sk-bcm-reply-item" v-for="(item, index) in reply" :key="'sk-bcm-reply-' + index">
          <div class="sk-bcm-reply__content">{{ item.content }}</div>
          <div class="sk-bcm-reply__time">{{ item.updateTime }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // 是否匿名
    isAnonymous: {
      type: Boolean,
      default: true
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
    reply: {
      type: Array,
      default: () => {
        return [];
      }
    }
  }
};
</script>

<style lang="less" scoped>
.sk-bcm {
  display: flex;
  width: 100%;
  box-sizing: border-box;
  padding: 12px 12px 24px 12px;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.25);
  .sk-bcm-avatar-box {
    display: flex;
    flex-direction: column;
    align-items: center;
    min-width: 52px;
    max-width: 56px;
    .sk-bcm-avatar {
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
    .sk-bcm-name {
      margin-top: 3px;
      font-size: 12px;
      color: #666666;
      text-align: center;
    }
  }
  .sk-bcm-content-box {
    margin-left: 36px;
    flex-grow: 1;
    .sk-bcm-score-box {
      width: 100%;
      display: flex;
      justify-content: space-between;
      .sk-bcm-score {
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
      .sk-bcm-time {
        font-size: 10px;
        color: #d4dde1;
      }
    }
    .sk-bcm-content {
      width: 100%;
      margin-top: 18px;
      margin-bottom: 16px;
      font-size: 12px;
      color: #999999;
    }
    .sk-bcm-reply-box {
      width: 90%;
      border-radius: 5px;
      background-color: #f9f8f7;
      display: flex;
      flex-direction: column;
      overflow: hidden;
      box-sizing: border-box;
      padding: 5px 12px;
      .sk-bcm-reply__title {
        width: 100%;
        font-size: 14px;
        height: 24px;
        line-height: 24px;
        color: #666666;
      }
      .sk-bcm-reply-item {
        padding: 3px 0 5px 0;
        margin-left: 16px;
        display: flex;
        justify-content: space-between;
        .sk-bcm-reply__content {
          flex-grow: 1;
          font-size: 12px;
          color: #999999;
        }
        .sk-bcm-reply__time {
          min-width: 72px;
          font-size: 10px;
          color: #8b968d;
          text-align: right;
        }
        & + .sk-bcm-reply-item {
          margin-top: 8px;
          border-top: 1px dashed #dddcd6;
        }
      }
    }
  }
}
</style>

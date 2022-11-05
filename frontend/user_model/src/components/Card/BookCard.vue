<template>
  <div class="sk-book-card" @click.stop="handleBook">
    <div class="sk-book-card-cover">
      <img :src="cover ? cover : $noCoverUrl" />
    </div>
    <div class="sk-book-card-content">
      <div class="sk-book-card__title">{{ title }}</div>
      <div class="sk-book-card__author">{{ author }}</div>
      <div class="sk-book-card__profile">{{ profile }}</div>
      <div class="sk-book-card__store" @click.stop="handleStore">
        <span>{{ storeName }}</span>
        <i class="icon ic-into"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // 图书封面
    cover: {
      type: String,
      default: null
    },
    // 标题
    title: {
      type: String,
      default: null
    },
    // 作者
    author: {
      type: String,
      default: null
    },
    // 图书简介
    profile: {
      type: String,
      default: null
    },
    // 商家名称
    storeName: {
      type: String,
      default: null
    },
    // 处理点击图书卡片
    handleBook: {
      type: Function,
      default: () => {}
    },
    // 处理点击商家名
    handleStore: {
      type: Function,
      default: () => {}
    }
  }
};
</script>

<style lang="less" scoped>
.sk-book-card {
  cursor: pointer;
  width: 300px;
  height: 186px;
  box-sizing: border-box;
  padding: 12px 8px;
  border-radius: 5px;
  display: flex;
  overflow: hidden;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
  background-color: #fff;
  .sk-book-card-cover {
    width: 42%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    img {
      object-fit: cover;
      width: 100%;
      height: 100%;
    }
  }
  .sk-book-card-content {
    user-select: none;
    width: 58%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    margin-left: 5px;
    .sk-book-card__title {
      font-size: 18px;
      font-weight: bold;
      font-family: 'Source Han Sans';
    }
    .sk-book-card__author {
      font-size: 12px;
      margin: 3px 0 5px 3px;
      color: var(--secondary-text);
    }
    .sk-book-card__profile {
      font-size: 12px;
      color: var(--regular-text);
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 5; //行数
      -webkit-box-orient: vertical;
    }
    .sk-book-card__store {
      display: flex;
      align-items: center;
      margin-top: 3px;
      font-size: 12px;
      color: var(--grey-link);
      transition: color 0.5s ease;
      &:hover {
        color: var(--link-text);
      }
      span {
        margin-right: 3px;
      }
      .icon {
        transform: scale(0.5);
      }
    }
  }
}
</style>

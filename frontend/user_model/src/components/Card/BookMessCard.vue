<template>
  <div class="sk-book-mess-card">
    <!-- 图书封面 -->
    <div class="sk-book-mess-card__cover">
      <img :src="cover ? cover : $noCoverUrl" alt="" />
    </div>
    <div class="sk-book-mess-card-content">
      <!-- 图书名称 -->
      <div class="sk-book-mess-card__title">{{ bookName }}</div>
      <!-- 图书基本信息 -->
      <div class="sk-book-mess-card-base">
        <div class="sk-book-mess-card__line">
          <!-- 作者 -->
          <div class="sk-book-mess-card__item">
            <span class="sk-book-mess-card__item_title">作者：</span>
            <span class="sk-book-mess-card__item_link" @click.stop="handleAuthor">{{ author }}</span>
          </div>
        </div>
        <div class="sk-book-mess-card__line">
          <!-- 出版社 -->
          <div class="sk-book-mess-card__item">
            <span class="sk-book-mess-card__item_title">出版社：</span>
            <span class="sk-book-mess-card__item_link" @click.stop="handlePress">{{ press }}</span>
          </div>
          <!-- 出版时间 -->
          <div class="sk-book-mess-card__item">
            <span class="sk-book-mess-card__item_title">出版时间：</span>
            <span class="sk-book-mess-card__item_text">{{ pubDate ? pubDate : '暂无' }}</span>
          </div>
        </div>
        <div class="sk-book-mess-card__line">
          <!-- 开本 -->
          <div class="sk-book-mess-card__item">
            <span class="sk-book-mess-card__item_title">开本：</span>
            <span class="sk-book-mess-card__item_text">{{ size ? size : '暂无' }}</span>
          </div>
          <!-- 页数 -->
          <div class="sk-book-mess-card__item">
            <span class="sk-book-mess-card__item_title">页数：</span>
            <span class="sk-book-mess-card__item_text">{{ page ? page + '页' : '暂无' }}</span>
          </div>
        </div>
        <div class="sk-book-mess-card__line">
          <!-- 类别 -->
          <div class="sk-book-mess-card__item" @click.stop="handleCate">
            <span class="sk-book-mess-card__item_title">类别：</span>
            <span class="sk-book-mess-card__item_link">{{ cateName }}</span>
          </div>
        </div>
      </div>
      <!-- 图书提示 -->
      <div class="sk-book-mess-card__tip">
        <i class="ic-info"></i>
        <span>{{ getTip(freeDay, borrowCost) }}</span>
      </div>
      <!-- 库存 -->
      <div class="sk-book-mess-card__stock">
        <span>库存：{{ inventory }}</span>
      </div>
      <div class="sk-book-mess-card-btns">
        <div
          class="sk-book-mess-card-btn sk-book-mess-card__buy"
          @click.stop="handleBuy"
          :class="{
            'is-disabled': !inventory
          }"
          :title="!inventory ? '库存不足' : ''">
          <span>立即借阅</span>
        </div>
        <div
          class="sk-book-mess-card-btn sk-book-mess-card__add"
          @click.stop="handleAdd"
          :class="{
            'is-disabled': !inventory
          }"
          :title="!inventory ? '库存不足' : ''">
          <span>加入购物车</span>
        </div>
      </div>
    </div>
    <!-- 购物车 -->
    <div class="sk-book-mess-card-car" @click.stop="handleCar">
      <i class="ic-shopping-cart"></i>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // 封面
    cover: String,
    // 书名
    bookName: String,
    // 作者
    author: String,
    // 出版社
    press: String,
    // 出版时间
    pubDate: String,
    // 开本
    size: String,
    // 页数
    page: Number,
    // 类别
    cateName: String,
    // 借阅费
    borrowCost: Number,
    // 免费天数
    freeDay: Number,
    // 库存
    inventory: Number,
    // 点击作者
    handleAuthor: {
      type: Function,
      default: () => {}
    },
    // 点击出版社
    handlePress: {
      type: Function,
      default: () => {}
    },
    /**
     * 点击类别
     */
    handleCate: {
      type: Function,
      default: () => {}
    },
    // 点击立即借阅
    handleBuy: {
      type: Function,
      default: () => {}
    },
    // 点击加入购物车
    handleAdd: {
      type: Function,
      default: () => {}
    },
    // 点击购物车
    handleCar: {
      type: Function,
      default: () => {}
    }
  },
  methods: {
    getTip(num, cost) {
      if (num) {
        return `借阅时间小于等于 ${num} 天的，免借阅费；大于 ${num} 天的，${cost} 元/天`;
      } else {
        return `借阅费为 ${cost} 元/天`;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.sk-book-mess-card {
  position: relative;
  display: flex;
  align-items: center;
  padding: 30px;
  box-sizing: border-box;
  .sk-book-mess-card__cover {
    width: 37.5%;
    height: 100%;
    border-radius: 5px;
    border: 1px solid #999999;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .sk-book-mess-card-content {
    height: 100%;
    display: flex;
    flex-grow: 1;
    flex-direction: column;
    justify-content: space-between;
    margin-left: 12px;
    .sk-book-mess-card__title {
      font-size: 24px;
      font-weight: bold;
      color: #3d3d3d;
    }
    .sk-book-mess-card-base {
      margin-left: 6px;
      display: flex;
      flex-direction: column;
      .sk-book-mess-card__line {
        display: flex;
        align-items: center;
        .sk-book-mess-card__item {
          display: flex;
          align-items: center;
          .sk-book-mess-card__item_title {
            font-size: 16px;
            color: #666666;
          }
          .sk-book-mess-card__item_text {
            font-size: 16px;
            color: #999999;
          }
          .sk-book-mess-card__item_link {
            cursor: pointer;
            font-size: 16px;
            color: #3271ae;
            &:hover {
              text-decoration: underline;
            }
          }
          & + .sk-book-mess-card__item {
            margin-left: 32px;
          }
        }
      }
    }
    .sk-book-mess-card__tip {
      display: flex;
      align-items: center;
      padding: 18px 12px;
      box-sizing: border-box;
      background-color: rgba(243, 166, 148, 0.15);
      i {
        font-size: 18px;
        color: #999999;
        margin-right: 5px;
      }
      span {
        font-size: 12px;
        color: #999999;
      }
    }
    .sk-book-mess-card__stock {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      span {
        font-size: 12px;
        color: #999999;
        margin-right: 5px;
      }
    }
    .sk-book-mess-card-btns {
      display: flex;
      align-items: center;
      .sk-book-mess-card-btn {
        cursor: pointer;
        user-select: none;
        height: 42px;
        display: flex;
        align-items: center;
        justify-content: center;
        box-sizing: border-box;
        padding: 0 40px;
        border-radius: 21px;
        transition: background-color 0.5s ease;
        span {
          font-size: 16px;
          font-weight: bold;
          color: #fff;
        }
        & + .sk-book-mess-card-btn {
          margin-left: 24px;
        }
        &.sk-book-mess-card__buy {
          background-color: #83ccd2;
          &:hover {
            background-color: rgba(131, 204, 210, 0.8);
          }
          &:active {
            background-color: #76b7bd;
          }
        }
        &.sk-book-mess-card__add {
          background-color: #b1d5c8;
          &:hover {
            background-color: rgba(177, 213, 200, 0.8);
          }
          &:active {
            background-color: #9dbdb1;
          }
        }
        &.is-disabled {
          cursor: not-allowed;
          opacity: 0.7;
        }
      }
    }
  }
  .sk-book-mess-card-car {
    cursor: pointer;
    position: absolute;
    top: 18px;
    right: 18px;
    width: 45px;
    height: 45px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
    i {
      font-size: 25px;
      color: #83ccd2;
    }
    &:active {
      box-shadow: inset 1px 1px 2px #babecc, inset -1px -1px 2px #fff;
    }
  }
}
@media (max-width: 1599px) {
  .sk-book-mess-card-car {
    width: 45px;
    height: 45px;
    i {
      font-size: 25px;
    }
  }
}
@media (min-width: 1600px) {
  .sk-book-mess-card-car {
    width: 56px;
    height: 56px;
    i {
      font-size: 32px;
    }
  }
}
</style>

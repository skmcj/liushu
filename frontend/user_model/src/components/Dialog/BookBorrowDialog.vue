<template>
  <!-- 图书借阅/加入购物车弹窗 -->
  <div class="book-borrow-dialog">
    <!-- 图书信息 -->
    <div class="book-borrow-mess">
      <div class="left">
        <img :src="cover ? cover : $noCoverUrl" alt="图书封面" />
      </div>
      <div class="mid">
        <div class="title">{{ bookName }}</div>
        <!-- 作者 -->
        <div class="mess-line">
          <div class="mess-item">
            <span class="label">作者：</span>
            <span class="text">{{ author }}</span>
          </div>
        </div>
        <!-- 出版信息 -->
        <div class="mess-line">
          <div class="mess-item">
            <span class="label">出版社：</span>
            <span class="text">{{ press }}</span>
          </div>
          <div class="mess-item">
            <span class="label">出版时间：</span>
            <span class="text">{{ pubDate ? pubDate : '暂无' }}</span>
          </div>
        </div>
        <!-- 开本信息 -->
        <div class="mess-line">
          <div class="mess-item">
            <span class="label">开本：</span>
            <span class="text">{{ size ? size : '暂无' }}</span>
          </div>
          <div class="mess-item">
            <span class="label">页数：</span>
            <span class="text">{{ pages ? pages + '页' : '暂无' }}</span>
          </div>
        </div>
        <!-- 类别信息 -->
        <div class="mess-line">
          <div class="mess-item">
            <span class="label">类别：</span>
            <span class="text">{{ cateName }}</span>
          </div>
        </div>
      </div>
      <div class="right">
        <el-input-number
          class="double-mini"
          v-model="quantity"
          :min="1"
          :max="inventory"
          @change="handleQuantity"></el-input-number>
      </div>
    </div>
    <!-- 书店Tip -->
    <div class="book-borrow-tip">
      <i class="ic-info"></i>
      <span>{{ getTip(freeDay, borrowCost) }}</span>
    </div>
    <!-- 底部信息 -->
    <div class="book-borrow-bottom">
      <div class="check-box">
        <div class="check-item">
          <div class="label">借阅时长：</div>
          <el-input
            size="mini"
            class="check-input"
            :min="1"
            :max="borrowDay"
            v-model.number="borrowTime"
            @input="handleInput" />
          <span>天</span>
        </div>
      </div>
      <div class="cost-mess-box">
        <div class="label-box">
          <div class="label">借阅费：</div>
          <div class="label">另需押金：</div>
          <div class="label">另需包装费：</div>
          <div class="label">预估配送费：</div>
        </div>
        <div class="text-box">
          <div class="text">{{ rBorrowCost + ' 元' }}</div>
          <div class="text">{{ rDeposit + ' 元' }}</div>
          <div class="text">{{ rPackingCost + ' 元' }}</div>
          <div class="text">{{ rDeliverFee + ' 元' }}</div>
        </div>
      </div>
    </div>
    <!-- 按钮 -->
    <div class="book-borrow-btns">
      <div
        v-if="model === 'cart'"
        class="btn add"
        :class="{
          'is-disabled': !inventory
        }"
        :title="!inventory ? '库存不足' : ''">
        <span>加入购物车</span>
      </div>
      <div
        v-if="model === 'borrow'"
        class="btn buy"
        :class="{
          'is-disabled': !inventory
        }"
        :title="!inventory ? '库存不足' : ''">
        <span>立即借阅</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    model: {
      type: String,
      default: 'cart'
    },
    cover: String,
    bookName: String,
    author: String,
    press: String,
    pubDate: String,
    size: String,
    pages: Number,
    cateName: String,
    freeDay: Number,
    borrowCost: Number,
    packingCost: Number,
    deposit: Number,
    deliverFee: Number,
    // 首次可借阅天数
    borrowDay: Number,
    inventory: {
      type: Number,
      default: 3
    }
  },
  data() {
    return {
      quantity: 1,
      // 借阅费
      rBorrowCost: 0,
      // 包装费
      rPackingCost: 0,
      // 配送费
      rDeliverFee: 0,
      // 押金
      rDeposit: 0,
      // 借阅时长
      borrowTime: ''
    };
  },
  created() {
    this.initDialog();
  },
  methods: {
    initDialog() {
      this.borrowTime = this.borrowDay;
      // 借阅费
      this.rBorrowCost =
        this.borrowTime > this.freeDay ? this.accCom(this.borrowCost * (this.borrowTime - this.freeDay)) : 0;
      // 包装费
      this.rPackingCost = this.packingCost;
      // 配送费
      this.rDeliverFee = this.deliverFee;
      // 押金
      this.rDeposit = this.deposit;
    },
    /**
     * 获取提示
     */
    getTip(num, cost) {
      if (num) {
        return `借阅时间小于等于 ${num} 天的，免借阅费；大于 ${num} 天的，${cost} 元/天`;
      } else {
        return `借阅费为 ${cost} 元/天`;
      }
    },
    /**
     * 增加数量
     */
    handleQuantity(currentVal, oldVal) {
      console.log('currentVal =>', currentVal);
      // 借阅费
      this.rBorrowCost =
        this.borrowTime > this.freeDay ? this.accCom(this.borrowCost * (this.borrowTime - this.freeDay)) : 0;
      // 包装费
      this.rPackingCost = this.accCom(this.packingCost * currentVal);
      // 押金
      this.rDeposit = this.accCom(this.deposit * currentVal);
    },
    handleInput(val) {
      console.log('input =>', val);
      if (val > this.borrowDay) {
        this.borrowTime = this.borrowDay;
        this.$showMsg(`首次借阅时长不可超过${this.borrowDay}`, { type: 'warning' });
      } else {
        if (val > this.freeDay) {
          this.rBorrowCost = (this.borrowTime - this.freeDay) * this.borrowCost;
        }
      }
    },
    accCom(val) {
      return parseFloat(val).toFixed(2);
    }
  }
};
</script>

<style lang="less" scoped>
.book-borrow-dialog {
  width: 100%;
  box-sizing: border-box;
  padding: 0 24px;
  display: flex;
  flex-direction: column;
  .book-borrow-mess {
    width: 100%;
    box-sizing: border-box;
    padding: 10px 12px;
    border-radius: 8px;
    background-color: #f6f6f6;
    display: flex;
    justify-content: space-between;
    .left {
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      width: 160px;
      height: 160px;
      border-radius: 5px;
      overflow: hidden;
      background-color: #fff;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .mid {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      box-sizing: border-box;
      padding: 0 12px;
      .title {
        font-size: 21px;
        font-weight: bold;
        color: #3d3d3d;
        margin-bottom: 3px;
      }
      .mess-line {
        display: flex;
        align-items: center;
        margin-left: 5px;
        .mess-item {
          font-size: 16px;
          .label {
            color: #666666;
          }
          .text {
            color: #999999;
          }
          & + .mess-item {
            margin-left: 32px;
          }
        }
        & + .mess-line {
          margin-top: 3px;
        }
      }
    }
    .right {
      height: 160px;
      box-sizing: border-box;
      padding: 0 6px;
      display: flex;
      flex-direction: column-reverse;
      align-items: center;
      flex-shrink: 0;
    }
  }
  .book-borrow-tip {
    margin-top: 12px;
    width: 100%;
    display: flex;
    align-items: center;
    padding: 8px 12px;
    box-sizing: border-box;
    background-color: rgba(243, 166, 148, 0.15);
    i {
      font-size: 18px;
      color: #999999;
      margin-right: 5px;
    }
    span {
      font-size: 14px;
      color: #999999;
    }
  }
  .book-borrow-bottom {
    margin-top: 12px;
    width: 100%;
    display: flex;
    justify-content: space-between;
    .check-box {
      display: flex;
      flex-direction: column;
      .check-item {
        display: flex;
        align-items: center;
        .check-input {
          margin-left: 8px;
          width: 64px;
          & + span {
            margin-left: 6px;
          }
        }
      }
    }
    .cost-mess-box {
      display: flex;
      margin-right: 32px;
      .label-box {
        display: flex;
        flex-direction: column;
        .label {
          text-align: right;
          color: #666;
          font-size: 12px;
          line-height: 21px;
          & + .label {
            margin-top: 3px;
          }
        }
      }
      .text-box {
        display: flex;
        flex-direction: column;
        margin-left: 8px;
        .text {
          text-align: right;
          line-height: 21px;
          color: #999;
          font-size: 12px;
          & + .text {
            margin-top: 3px;
          }
        }
      }
    }
  }
  .book-borrow-btns {
    margin-top: 24px;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    .btn {
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
      & + .btn {
        margin-left: 24px;
      }
      &.buy {
        background-color: #83ccd2;
        &:hover {
          background-color: rgba(131, 204, 210, 0.8);
        }
        &:active {
          background-color: #76b7bd;
        }
      }
      &.add {
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
</style>

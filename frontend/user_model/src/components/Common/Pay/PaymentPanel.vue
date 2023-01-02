<template>
  <!-- 支付面板 -->
  <div class="pay-panel">
    <!-- 订单列表 -->
    <div class="pay-order-box">
      <div class="pay-order-list">
        <div class="pay-order-item" v-for="(order, key) in payOrderList" :key="'pay-order-' + key">
          <!-- 左边订单信息 -->
          <div class="left">
            <!-- 订单号 -->
            <div class="order-title">
              <span class="label">订单号：</span>
              <span class="text">{{ order.number }}</span>
            </div>
            <!-- 商品列表 -->
            <div class="order-goods-list">
              <div class="order-goods-item" v-for="(item, index) in order.orderItems" :key="'goods-' + key + index">
                <div class="goods-mess">
                  <div class="cover">
                    <img :src="item.bookCover" alt="cover" />
                  </div>
                  <span class="book-name">{{ item.bookName }}</span>
                </div>
                <div class="goods-num">{{ '×' + item.quantity }}</div>
              </div>
            </div>
          </div>
          <!-- 右边店铺名称及订单金额 -->
          <div class="right">
            <div class="title">{{ order.storeName }}</div>
            <div class="amount">
              <span class="label">小计</span>
              <span class="text">{{ '￥ ' + order.amount }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="pay-order-mess">
        <span class="left">{{ '合并 | 共计 ' + orderCount + ' 单' }}</span>
        <span class="right">{{ '￥ ' + $keepTwoNum(amount) }}</span>
      </div>
    </div>
    <!-- 支付方式列表 -->
    <div class="pay-method-box">
      <div class="title">支付方式</div>
      <div class="pay-method-list">
        <!-- 余额支付 -->
        <div
          class="pay-method-item"
          :class="{ 'is-checked': payMethod === 'balance' }"
          @click.stop="handlePayMethod('balance')">
          <i class="icon ic-wallet"></i>
          <div class="method-mess">
            <span class="label">我的钱包</span>
            <span class="text">{{ '￥ ' + $keepTwoNum(money) }}</span>
          </div>
        </div>
        <!-- 微信支付 -->
        <div
          class="pay-method-item"
          :class="{ 'is-checked': payMethod === 'wechatPay' }"
          @click.stop="handlePayMethod('wechatPay')">
          <i class="icon ic-wechat-pay"></i>
          <span class="label">微信支付</span>
        </div>
        <!-- 支付宝支付 -->
        <div
          class="pay-method-item"
          :class="{ 'is-checked': payMethod === 'alipay' }"
          @click.stop="handlePayMethod('alipay')">
          <i class="icon ic-alipay"></i>
          <span class="label">支付宝</span>
        </div>
      </div>
      <div class="pay-method-panel">
        <template v-if="payMethod === 'balance'">
          <div class="title">支付密码：</div>
          <PayInput v-model="payPass" tip="请输入 6 位数字的支付密码" @complete="handlePayComplete" />
        </template>
        <template v-if="payMethod === 'wechatPay'">
          <div class="title">扫码支付</div>
          <div class="qr-code">
            <div class="qr-img">
              <img src="" alt="加载失败" />
            </div>
            <div class="tip">请使用手机微信扫码完成支付</div>
          </div>
        </template>
        <template v-if="payMethod === 'alipay'">
          <div class="title">扫码支付</div>
          <div class="qr-code">
            <div class="qr-img">
              <img src="" alt="加载失败" />
            </div>
            <div class="tip">请使用手机支付宝扫码完成支付</div>
          </div>
        </template>
      </div>
    </div>
    <!-- 底部信息 -->
    <div class="pay-tip-box" v-if="firstOrder.payStatus === 0">
      <div
        class="pay-time-tip"
        :class="{
          'is-error': isOvertime
        }">
        <i class="ic-info"></i>
        <span class="label">{{ timeTip }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import PayInput from '@/components/Common/Input/PayInput';

export default {
  components: {
    PayInput
  },
  props: {
    orderList: {
      type: Array,
      default: () => {
        return [];
      }
    },
    amount: Number,
    money: Number,
    payType: String
  },
  data() {
    return {
      isOvertime: false,
      firstOrder: {},
      payMethod: 'balance',
      payPass: '',
      payOrderList: {},
      // 计时器
      timer: null,
      // 设置计时，单位：s => 15分钟
      counter: 900,
      counters: 900000,
      timeTip: '',
      orderCount: 0
    };
  },
  created() {
    this.payOrderList = this.orderList;
    this.orderCount = this.payOrderList.length;
    // this.computeAmount(this.payOrderList);
  },
  mounted() {
    this.computeTimeCount(this.payOrderList);
  },
  methods: {
    computeTimeCount(obj) {
      this.firstOrder = this.payOrderList[0];
      // this.firstOrder.createTime = new Date();
      // this.firstOrder.payStatus = 0;
      if (this.firstOrder.payStatus === 0) {
        // 未支付
        let createTime = new Date(this.firstOrder.createTime);
        let now = new Date();
        let diff = now - createTime;
        if (diff < this.counters) {
          // 未超时(15分钟)
          // 需计时
          this.counter = parseInt((this.counters - diff) / 1000);
          this.timeTip = `订单已创建，请在 ${parseInt(this.counter / 60)}:${this.counter % 60} 内完成支付`;
          this.timer = setInterval(() => {
            // 替换文本，秒实时改变
            this.timeTip = `订单已创建，请在 ${parseInt(this.counter / 60)}:${this.counter % 60} 内完成支付`;
            this.counter--;
            if (this.counter < 0) {
              this.timeTip = '订单超时未支付，已取消';
              this.handleOrderOvertime();
              // 当计时小于零时，取消该计时器
              clearInterval(this.timer);
              this.resetTimer();
            }
          }, 1000);
        }
      }
    },
    /**
     * 重置计时相关参数
     */
    resetTimer() {
      this.isOvertime = false;
      this.timeTip = `订单已创建，请在 ${parseInt(this.counter / 60)}:${this.counter % 60} 内完成支付`;
      if (this.timer) {
        // 存在计时器对象，则清除
        clearInterval(this.timer);
        // 重置秒数，防止下次混乱
        this.counter = 900;
        // 计时器对象重置为空
        this.timer = null;
      }
    },
    /**
     * 计算订单总价
     */
    computeAmount(orderObj) {
      for (const key in orderObj) {
        orderObj[key].amount = 0;
        orderObj[key].products.forEach(item => {
          orderObj[key].amount += item.amount;
        });
        orderObj[key].amount += orderObj[key].deliveryFee;
      }
    },
    /**
     * 选择支付方式
     */
    handlePayMethod(text) {
      this.payMethod = text;
      switch (text) {
        case 'alipay':
          this.handleAlipay();
          break;
        case 'wechatPay':
          this.handleWechatPay();
          break;
      }
      this.$emit('payMethod', text);
    },
    /**
     * 支付宝支付
     */
    handleAlipay() {
      this.$showMsg('暂不支持，抱歉~');
    },
    /**
     * 微信支付
     */
    handleWechatPay() {
      this.$showMsg('暂不支持，抱歉~');
    },
    /**
     * 支付完成
     */
    handlePayComplete(val) {
      console.log('pay complete =>', val);
      this.$emit('payComplete', val);
    },
    /**
     * 订单超时
     */
    handleOrderOvertime() {
      this.$emit('overtime', true);
    },
    /**
     * 获取对象第一个属性
     */
    getFirstAttr(obj) {
      return obj[Object.keys(obj)[0]];
    },
    /**
     * 计算对象长度
     */
    computeObjCount(obj) {
      let list = Object.getOwnPropertyNames(obj);
      if (list.indexOf('_ob_')) return list.length - 1;
      return list.length;
    }
  },
  beforeDestroy() {
    this.resetTimer();
  }
};
</script>

<style lang="less" scoped>
.pay-panel {
  width: 100%;
  display: flex;
  flex-direction: column;
  .pay-order-box {
    width: 100%;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    border-bottom: 1px solid #e5e5e5;
    .pay-order-list {
      display: flex;
      flex-wrap: wrap;
      gap: 12px 24px;
      .pay-order-item {
        user-select: none;
        display: flex;
        align-items: center;
        box-sizing: border-box;
        border-radius: 8px;
        background-color: #f5f5f5;
        overflow: hidden;
        .left {
          display: flex;
          flex-direction: column;
          box-sizing: border-box;
          padding: 8px 12px;
          .order-title {
            display: flex;
            align-items: center;
            box-sizing: border-box;
            font-size: 14px;
            color: var(--primary-text);
          }
          .order-goods-list {
            margin-top: 6px;
            display: flex;
            flex-wrap: wrap;
            box-sizing: border-box;
            padding: 0 12px;
            gap: 6px 12px;
            .order-goods-item {
              width: 116px;
              height: 116px;
              display: flex;
              align-items: flex-end;
              justify-content: space-between;
              .goods-mess {
                width: 96px;
                height: 116px;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: space-between;
                border-radius: 5px;
                background-color: #fff;
                overflow: hidden;
                .cover {
                  width: 96px;
                  height: 96px;
                  border-radius: 5px;
                  overflow: hidden;
                  img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                  }
                }
                .book-name {
                  font-size: 12px;
                  color: var(--primary-text);
                  text-overflow: ellipsis;
                  overflow: hidden;
                  white-space: nowrap;
                }
              }
              .goods-num {
                font-size: 10px;
                color: #999;
              }
            }
          }
        }
        .right {
          min-width: 96px;
          position: relative;
          height: 100%;
          background-color: #dfd7c2;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: space-between;
          box-sizing: border-box;
          padding: 12px 21px;
          .title {
            font-size: 12px;
            color: var(--primary-text);
          }
          .amount {
            margin-bottom: 24px;
            display: flex;
            align-items: center;
            flex-direction: column;
            font-size: 14px;
            color: #999;
            .text {
              margin-top: 5px;
            }
          }
        }
      }
    }
    .pay-order-mess {
      user-select: none;
      width: 100%;
      margin-top: 12px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-sizing: border-box;
      padding: 12px 18px;
      .left {
        font-size: 14px;
        color: var(--primary-text);
      }
      .right {
        font-size: 16px;
        color: var(--error);
      }
    }
  }
  .pay-method-box {
    width: 100%;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    padding: 12px 0;
    border-bottom: 1px solid #e5e5e5;
    .title {
      font-size: 16px;
      font-weight: bold;
      color: var(--primary-text);
    }
    .pay-method-list {
      display: flex;
      flex-wrap: wrap;
      gap: 12px 24px;
      box-sizing: border-box;
      padding: 24px 18px;
      .pay-method-item {
        cursor: pointer;
        user-select: none;
        position: relative;
        width: 180px;
        height: 72px;
        box-sizing: border-box;
        border-radius: 8px;
        background-color: #f5f5f5;
        overflow: hidden;
        display: flex;
        align-items: center;
        justify-content: center;
        .icon {
          font-size: 42px;
          margin-right: 5px;
          &.ic-wallet {
            font-size: 36px;
            color: var(--primary);
          }
          &.ic-wechat-pay {
            color: #22ac38;
          }
          &.ic-alipay {
            color: #1677ff;
          }
        }
        .label {
          font-size: 16px;
          color: var(--primary-text);
        }
        .method-mess {
          display: flex;
          flex-direction: column;
          .label {
            font-size: 16px;
            color: var(--primary-text);
          }
          .text {
            font-size: 10px;
            color: #999;
          }
        }
        &.is-checked {
          // border: 2px solid var(--primary);
          &::before {
            border-radius: 8px;
            box-sizing: border-box;
            top: 0;
            left: 0;
            position: absolute;
            display: block;
            content: '';
            width: 100%;
            height: 100%;
            border: 2px solid var(--primary);
            animation: fade-in 0.5s ease-in-out;
          }
          &::after {
            font-family: 'fbookfont' !important;
            font-size: 16px;
            font-style: normal;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
            display: inline-block;
            content: '\e628';
            font-size: 24px;
            color: var(--primary);
            position: absolute;
            bottom: 0px;
            right: 0px;
            animation: fade-in 0.5s ease-in-out;
          }
        }
      }
    }
    .pay-method-panel {
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      padding: 24px 18px;
      .title {
        font-size: 14px;
        font-weight: normal;
      }
      :deep(.sk-input-pay) {
        margin-top: 8px;
        margin-left: 5px;
        align-items: flex-start;
        .sk-input-pay-box {
          .sk-input-pay-item {
            width: 27px;
            height: 32px;
          }
        }
      }
      .qr-code {
        margin-top: 8px;
        margin-left: 12px;
        width: 150px;
        align-items: center;
        display: flex;
        flex-direction: column;
        .qr-img {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 128px;
          height: 128px;
          border-radius: 5px;
          overflow: hidden;
          border: 1px dotted #999;
          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }
        .tip {
          text-align: center;
          margin-top: 3px;
          font-size: 10px;
          color: #999;
        }
      }
    }
  }
  .pay-tip-box {
    margin-top: 12px;
    width: 100%;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    padding: 0 18px;
    .pay-time-tip {
      display: flex;
      align-items: center;
      color: #fac03d;
      &.is-error {
        color: #ee827c;
      }
      i {
        font-size: 18px;
        margin-right: 6px;
      }
      span {
        font-size: 14px;
      }
    }
  }
}
</style>

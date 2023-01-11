<template>
  <!-- 结算页面 -->
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box">
      <div class="content">
        <div class="settlement-box">
          <!-- 标题 -->
          <div class="title-box">
            <span class="text">提交订单</span>
          </div>
          <!-- 地址 -->
          <div class="address-box">
            <div class="address-main">
              <!-- 空地址 -->
              <div v-if="$isEmpty(address)" class="no-address" @click.stop="selectAddress">
                <div class="left">
                  <div class="more-btn">
                    <i class="ic-add"></i>
                  </div>
                  <span class="text">请点击选择收货地址</span>
                </div>
                <div class="right">
                  <i class="ic-more-1"></i>
                </div>
              </div>
              <!-- 显示地址 -->
              <div v-else class="show-address">
                <div class="left">
                  <div class="icon">
                    <i class="ic-location"></i>
                  </div>
                  <div class="address-mess">
                    <div class="address-location">
                      <span class="address-text">{{ address.location }}</span>
                      <span class="address-tip">{{ address.detail }}</span>
                    </div>
                    <div class="address-contact">
                      <span class="address-name">{{ address.consignee }}</span>
                      <span class="address-phone">{{ address.phone }}</span>
                    </div>
                  </div>
                </div>
                <div class="right">
                  <i class="ic-more-1" @click.stop="selectAddress"></i>
                </div>
              </div>
            </div>
          </div>
          <!-- 订单列表 -->
          <div class="order-list">
            <div class="order-item" v-for="(obj, key) in orderObj" :key="'order-item-' + key">
              <!-- 标题 -->
              <div class="order-title">
                <span>{{ obj.storeName }}</span>
              </div>
              <!-- 商品列表 -->
              <div class="order-goods-list">
                <div class="goods-item" v-for="(item, index) in obj.products" :key="key + index">
                  <div class="left">
                    <div class="cover">
                      <img :src="item.bookCover" alt="cover" />
                    </div>
                    <div class="mess">
                      <div class="mess-item">
                        <span class="label">图书名称</span>
                        <span class="text">{{ item.bookName }}</span>
                      </div>
                      <div class="mess-item">
                        <span class="label">图书简介</span>
                        <span class="text row">{{ item.bookOutline }}</span>
                      </div>
                      <div class="mess-item">
                        <span class="label">借阅时长</span>
                        <span class="text">{{ item.borrowTime + ' 天' }}</span>
                      </div>
                      <!-- 图书数量 -->
                      <div class="quantity">{{ '× ' + item.quantity }}</div>
                    </div>
                  </div>
                  <div class="right">
                    <div class="cost-item">
                      <span class="label">金额：</span>
                      <span class="text">{{ '￥ ' + item.amount }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 订单备注 -->
              <div class="remark-box">
                <el-input type="textarea" :rows="1" placeholder="订单备注···" v-model="obj.remark"></el-input>
              </div>
            </div>
          </div>
          <!-- 底部信息 -->
          <div class="bottom-box">
            <div class="left">
              <div class="expect-time">
                <el-cascader
                  v-model="expectTime"
                  :options="options"
                  :props="{ expandTrigger: 'hover' }"
                  separator=" "
                  placeholder="请选择期望配送时间"
                  @change="handleExpectTime"></el-cascader>
              </div>
              <div class="mess-box">
                <div class="mess-item">
                  <span class="label">借阅费：</span>
                  <span class="text">{{ '￥ ' + $keepTwoNum(allBorrowCost) }}</span>
                </div>
                <div class="mess-item">
                  <span class="label">另需包装费：</span>
                  <span class="text">{{ '￥ ' + $keepTwoNum(allPackingCost) }}</span>
                </div>
                <div class="mess-item">
                  <span class="label">预估配送费：</span>
                  <span class="text">{{ '￥ ' + $keepTwoNum(alldeliveryFee) }}</span>
                </div>
                <div class="mess-item">
                  <span class="label">押金：</span>
                  <span class="text">{{ '￥ ' + $keepTwoNum(allDeposit) }}</span>
                </div>
                <div class="mess-item all">
                  <span class="label">总金额：</span>
                  <span class="text">{{ '￥ ' + $keepTwoNum(allAmount) }}</span>
                </div>
              </div>
            </div>
            <div class="right">
              <el-button class="btn" type="primary" round @click.stop="handleSettlement">去付款</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 付款dialog -->
    <el-dialog
      title="收银台"
      :visible.sync="payDialogVisible"
      class="pay-dialog"
      :close-on-click-modal="false"
      destroy-on-close
      @close="handlePayPanelClose">
      <div class="order-to-pay">
        <PaymentPanel
          :order-list="orderDtoList"
          :amount="allAmount"
          :money="money"
          :pay-type="payType"
          @overtime="handleOrderOvertime"
          @payMethod="handlePayMethod"
          @payComplete="handlePayComplete" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="isOvertime" @click="handlePay" round>确认 支付</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Wave from '@/components/Common/Wave';
import PaymentPanel from '@/components/Common/Pay/PaymentPanel';
import commonUtil from '@/utils/common';
import { deleteCartApi } from '@/api/cartApi';
import { getDefaultAddressApi } from '@/api/addressApi';
import { generateOrderApi, payOrderByLSApi } from '@/api/orderApi';

export default {
  components: {
    Wave,
    PaymentPanel
  },
  data() {
    return {
      payPass: '',
      payType: 'borrow',
      isOvertime: false,
      payMethod: 'balance',
      money: 0,
      // 收银台
      payDialogVisible: false,
      address: {},
      expectTime: '',
      options: [],
      orderObj: {},
      orderDtoList: [],
      businessHours: '8:30-20:30',
      // 总借阅费
      allBorrowCost: 0,
      // 总包装费
      allPackingCost: 0,
      // 总配送费
      alldeliveryFee: 0,
      // 押金
      allDeposit: 0,
      // 总金额
      allAmount: 0
    };
  },
  created() {
    this.payType = this.$store.state.settlementObj.type ? this.$store.state.settlementObj.type : 'borrow';
    this.orderObj = this.$store.state.settlementObj.data;
    // 总借阅费
    this.allBorrowCost = this.$store.state.settlementObj.allBorrowCost;
    // 总包装费
    this.allPackingCost = this.$store.state.settlementObj.allPackingCost;
    // 总配送费
    this.alldeliveryFee = this.$store.state.settlementObj.alldeliveryFee;
    // 押金
    this.allDeposit = this.$store.state.settlementObj.allDeposit;
    // 总金额
    this.allAmount = this.$store.state.settlementObj.allAmount;
    this.money = this.$store.state.userInfo.money;
    this.options = this.packingOptions(this.businessHours);
    // $store如果地址为空，获取默认地址
    this.getAddress();
  },
  methods: {
    /**
     * 获取地址
     */
    getAddress() {
      // console.log('address =>', this.$store.state.checkedAddress);
      if (this.$isEmpty(this.$store.state.checkedAddress)) {
        this.getDefaultAddress();
      } else {
        this.address = this.$store.state.checkedAddress;
      }
    },
    /**
     * 获取默认地址
     */
    getDefaultAddress() {
      // 发送请求，获取默认地址
      let address = {};
      getDefaultAddressApi()
        .then(res => {
          if (res.data.flag) {
            address = res.data.data;
          }
        })
        .finally(() => {
          this.address = address;
        });
    },
    /**
     * 选择地址
     */
    selectAddress() {
      this.$router.push('/mine/address');
    },
    /**
     * 去付款
     */
    async handleSettlement() {
      if (this.$isEmpty(this.address)) {
        this.$showMsg('请选择配送地址');
        return;
      }
      if (this.$isEmpty(this.expectTime)) {
        this.$showMsg('请选择期望配送时间');
        return;
      }
      // 获取待结算订单信息
      let genOrderList = this.packingOrderList(this.orderObj);
      // console.log('orderList ==>', genOrderList);
      this.orderDtoList = await this.generateOrderOfList(genOrderList);
      if (this.orderDtoList.length > 0) {
        // 订单生成成功
        this.updateAmount(this.orderDtoList);
        this.payDialogVisible = true;
        // 判断类型，borrow | cart
        // 发送请求，删除购物车中选中项
        if (this.payType === 'cart') {
          let ids = this.getCartIds(this.orderObj, this.orderDtoList);
          this.deleteCart(ids);
        }
      }
    },
    /**
     * 账户支付密码
     */
    handlePayComplete(val) {
      this.payPass = val;
      // console.log('pass pay ==>', val);
    },
    /**
     * 确认支付
     */
    handlePay() {
      // console.log('确认支付');
      // this.payDialogVisible = false;
      if (this.payMethod === 'balance') {
        if (this.$isEmpty(this.payPass)) {
          this.$showMsg('请输入支付密码');
          return;
        }
        // 账户余额支付
        if (this.money > this.allAmount) {
          // 进行支付
          this.payOrderOfList(this.orderDtoList);
        } else {
          // 余额不足
          this.$showMsg('账户余额不足，请充值后前往订单页支付', {
            type: 'warning',
            duration: 1200,
            closeFunc: () => {
              this.$router.replace('/mine');
            }
          });
        }
      }
    },
    /**
     * 批量支付订单
     */
    async payOrderOfList(list) {
      let payPass = this.$sha256(this.payPass.substring(0, 6));
      for (const order of list) {
        let res = await payOrderByLSApi(order.id, payPass);
        if (!res.data.flag) {
          this.$showMsg('订单支付失败，请稍后再试', {
            type: 'error',
            duration: 1200,
            closeFunc: () => {
              this.$router.replace('/mine');
            }
          });
          return;
        }
      }
      this.$showMsg('支付成功', {
        type: 'success',
        closeFunc: () => {
          this.$router.replace('/mine/order').catch(err => {});
        }
      });
    },
    /**
     * 支付方式
     */
    handlePayMethod(val) {
      this.payMethod = val;
    },
    /**
     * 批量生成订单
     */
    async generateOrderOfList(list) {
      let orderList = [];
      for (const item of list) {
        let res = await generateOrderApi(item);
        // console.log('gen order ==>', res);
        if (res.data.flag) {
          orderList.push(res.data.data);
        } else {
          this.$showMsg('订单生成失败，请稍后再试', {
            type: 'error',
            duration: 1200
          });
        }
      }
      return orderList;
    },
    /**
     * 期望时间
     */
    handleExpectTime(val) {
      // console.log('select time =>', val);
    },
    /**
     * 订单超时
     */
    handleOrderOvertime(val) {
      // console.log('订单超时 ==> ', val);
      this.isOvertime = true;
    },
    /**
     * 包装期望时间列表
     * @param {String} businessTime 营业时间
     */
    packingOptions(businessTime) {
      let numReg = /\d+/g;
      /**
       * 提取营业时间数组
       * [ 营业开始：时, 营业开始：分, 营业结束：时, 营业结束：分 ]
       */
      let etList = businessTime.match(numReg).map(item => {
        return Number(item);
      });
      let now = new Date();
      let nTime = now.getHours() * 60 + now.getMinutes();
      let sTime = etList[0] * 60 + etList[1];
      let eTime = etList[2] * 60 + etList[3];
      // 初始化options
      let options = [
        {
          label: '今天',
          value: commonUtil.formatDate(now),
          children: []
        },
        {
          label: '明天',
          value: commonUtil.formatDate(new Date(now.getTime() + 24 * 60 * 60 * 1000)),
          children: []
        },
        {
          label: '后天',
          value: commonUtil.formatDate(new Date(now.getTime() + 24 * 60 * 60 * 1000 * 2)),
          children: []
        }
      ];
      for (let i = 0; i < options.length; i++) {
        let h = 0;
        let m = 0;
        let t = sTime + 20;
        while (t < eTime) {
          h = commonUtil.formatUnits(parseInt(t / 60));
          m = commonUtil.formatUnits(t % 60);
          options[i].children.push({
            label: h + ':' + m,
            value: h + ':' + m,
            disabled: i === 0 && t < nTime
          });
          t += 20;
        }
      }
      // 添加立即配送
      if (!this.isExceedBusinessTime(now, etList)) {
        // 当前时间未超过营业时间
        options.unshift({
          label: '立即配送',
          value: commonUtil.formatDate(now, 'YYYY-mm-dd HH:MM')
        });
      }
      return options;
    },
    /**
     * 包装订单数据
     */
    packingOrderList(orderObj) {
      let userInfo = this.$store.state.userInfo;
      let list = [];
      for (const key in orderObj) {
        let item = {};
        item.userId = userInfo.id;
        item.storeId = orderObj[key].storeId;
        item.addressId = this.address.id;
        item.consignee = this.address.consignee;
        item.address = this.address.location + this.address.detail;
        item.borrowTime = orderObj[key].products[0].borrowTime;
        item.expectedTime = this.expectTime.join(' ') + ':00';
        item.remark = orderObj[key].remark ? orderObj[key].remark : null;
        item.phone = this.address.areaCode + ' ' + this.address.phone;
        item.deliveryFee = orderObj[key].deliveryFee;
        item.orderItems = [];
        let orderAmount = 0;
        let discountAmount = 0;
        let amount = 0;
        for (const product of orderObj[key].products) {
          let orderItem = {};
          if (product.amount) {
            orderAmount += product.amount;
          }
          if (product.discountAmount) {
            discountAmount += product.discountAmount;
          }
          orderItem.bookId = product.bookId;
          orderItem.quantity = product.quantity;
          orderItem.borrowCost = product.borrowCost;
          orderItem.packingCost = product.packingCost;
          orderItem.amount = product.amount;
          orderItem.deposit = product.deposit;
          item.orderItems.push(orderItem);
        }
        item.orderAmount = this.accToFixedTwo(orderAmount);
        item.discountAmount = this.accToFixedTwo(discountAmount);
        item.amount = this.accToFixedTwo(orderAmount - discountAmount);
        list.push(item);
      }
      return list;
    },
    /**
     * 是否超过营业时间
     * @param {Date} date
     * @param {Array} etList
     */
    isExceedBusinessTime(date, etList) {
      let h = date.getHours();
      let m = date.getMinutes();
      if (h < etList[0]) return true;
      if (h > etList[2]) return true;
      if (h === etList[0] && m < etList[1]) return true;
      if (h === etList[2] && m > etList[3]) return true;
      return false;
    },
    /**
     * 支付面板关闭
     */
    handlePayPanelClose() {
      // 暂时跳转到首页
      // this.$router.replace('/');
      this.$router.replace('/mine/order');
    },
    /**
     * 删除购物车项
     */
    deleteCart(ids) {
      deleteCartApi(ids).then(res => {
        if (!res.data.flag) {
          console.warn('已结算购物车信息清除失败');
        }
      });
    },
    accToFixedTwo(num) {
      return parseFloat(parseFloat(num).toFixed(2));
    },
    getCartIds(orderObj, orderList) {
      let ids = [];
      for (const item of orderList) {
        let storeId = item.storeId;
        for (const cart of orderObj[storeId].products) {
          if (cart.id) ids.push(cart.id);
        }
      }
      return ids;
    },
    updateAmount(orderDtoList) {
      let amount = 0;
      for (const order of orderDtoList) {
        amount += order.amount;
      }
      this.allAmount = this.accToFixedTwo(amount);
    }
  }
};
</script>

<style lang="less" scoped>
.settlement-box {
  width: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding: 18px 24px;
  border-radius: 12px;
  background-color: #fff;
  .title-box {
    width: 100%;
    display: flex;
    .text {
      user-select: none;
      font-size: 27px;
      font-weight: bold;
      color: var(--primary-text);
    }
  }
  .address-box {
    margin-top: 24px;
    box-sizing: border-box;
    padding: 0 48px;
    .address-main {
      display: flex;
      align-items: center;
      border-radius: 8px;
      background-color: #f5f3f2;
      box-sizing: border-box;
      padding: 12px;
      .no-address {
        cursor: pointer;
        user-select: none;
        width: 100%;
        box-sizing: border-box;
        padding: 36px 12px 36px 52px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .left {
          display: flex;
          align-items: center;
          .more-btn {
            width: 56px;
            height: 56px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: var(--primary);
            i {
              font-size: 36px;
              color: #fff;
            }
          }
          .text {
            margin-left: 12px;
            font-size: 16px;
            color: var(--primary);
          }
        }
        .right {
          i {
            font-size: 24px;
            color: #999;
          }
        }
      }
      .show-address {
        user-select: none;
        width: 100%;
        box-sizing: border-box;
        padding: 24px 12px 24px 24px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .left {
          display: flex;
          .icon {
            margin-top: 6px;
            margin-right: 12px;
            i {
              font-size: 24px;
              color: var(--primary);
            }
          }
          .address-mess {
            display: flex;
            flex-direction: column;
            .address-location {
              display: flex;
              align-items: flex-end;
              .address-text {
                font-size: 24px;
                font-weight: bold;
                color: var(--primary-text);
              }
              .address-tip {
                margin-left: 8px;
                font-size: 18px;
                color: var(--primary-text);
              }
            }
            .address-contact {
              margin-top: 8px;
              display: flex;
              align-items: center;
              .address-name {
                font-size: 16px;
                color: #666;
              }
              .address-phone {
                margin-left: 6px;
                font-size: 16px;
                color: #666;
              }
            }
          }
        }
        .right {
          i {
            cursor: pointer;
            font-size: 24px;
            color: #999;
          }
        }
      }
    }
  }
  .order-list {
    width: 100%;
    display: flex;
    flex-direction: column;
    margin-top: 24px;
    box-sizing: border-box;
    padding: 0 72px;
    padding-bottom: 120px;
    .order-item {
      width: 100%;
      border-radius: 8px;
      background-color: #f5f5f5;
      overflow: hidden;
      .order-title {
        user-select: none;
        width: 100%;
        height: 36px;
        display: flex;
        align-items: center;
        box-sizing: border-box;
        padding: 0 12px;
        border-bottom: 1px solid #e8e8e8;
        span {
          font-size: 16px;
          font-weight: bold;
          color: var(--primary-text);
        }
      }
      .order-goods-list {
        .goods-item {
          width: 100%;
          padding: 12px 24px;
          box-sizing: border-box;
          display: flex;
          align-items: center;
          justify-content: space-between;
          .left {
            width: 70%;
            display: flex;
            align-items: center;
            .cover {
              width: 96px;
              height: 96px;
              border-radius: 5px;
              overflow: hidden;
              flex-shrink: 0;
              display: flex;
              align-items: center;
              justify-content: center;
              img {
                width: 100%;
                height: 100%;
                object-fit: cover;
              }
            }
            .mess {
              position: relative;
              flex-grow: 1;
              padding: 0 48px 0 8px;
              box-sizing: border-box;
              display: flex;
              flex-direction: column;
              .mess-item {
                user-select: none;
                font-size: 12px;
                display: flex;
                .label {
                  display: inline-block;
                  min-width: 64px;
                  color: var(--primary-text);
                }
                .text {
                  margin-left: 5px;
                  color: #6b798e;
                  &.row {
                    display: -webkit-box;
                    overflow: hidden;
                    -webkit-line-clamp: 3;
                    -webkit-box-orient: vertical;
                  }
                }
                & + .mess-item {
                  margin-top: 3px;
                }
              }
              .quantity {
                position: absolute;
                top: 5px;
                right: 6px;
                font-size: 12px;
                color: #6b798e;
              }
            }
          }
          .right {
            display: flex;
            flex-direction: column;
            .cost-item {
              user-select: none;
              display: flex;
              align-items: center;
              .label {
                display: inline-block;
                min-width: 48px;
                font-size: 14px;
                color: var(--primary-text);
              }
              .text {
                display: inline-block;
                min-width: 48px;
                font-size: 14px;
                color: var(--danger);
              }
            }
          }
          & + .goods-item {
            margin-top: 8px;
          }
        }
      }
      .remark-box {
        width: 70%;
        margin-left: 24px;
        margin-bottom: 12px;
        margin-top: 12px;
        border-radius: 15px;
        overflow: hidden;
        .el-textarea {
          :deep(.el-textarea__inner) {
            border: none;
          }
        }
      }
      & + .order-item {
        margin-top: 12px;
      }
    }
  }
  .bottom-box {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 8px 12px;
    border-top: 1px solid #dadada;
    margin-bottom: 12px;
    .left {
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      .expect-time {
        margin-bottom: 12px;
      }
      .mess-box {
        margin-left: 24px;
        display: flex;
        flex-direction: column;
        .mess-item {
          user-select: none;
          display: flex;
          align-items: center;
          font-size: 12px;
          .label {
            min-width: 85px;
            color: #999;
          }
          .text {
            color: var(--primary-text);
          }
          &.all {
            font-size: 14px;
            font-weight: bold;
            .label {
              color: #999;
            }
            .text {
              color: var(--danger);
            }
          }
          & + .mess-item {
            margin-top: 3px;
          }
        }
      }
    }
    .right {
      .btn {
        height: 40px;
        padding: 0 42px;
      }
    }
  }
}
.pay-dialog {
  :deep(.el-dialog) {
    width: 64%;
  }
}
</style>

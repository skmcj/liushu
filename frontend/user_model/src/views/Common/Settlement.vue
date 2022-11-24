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
                  <span class="text">{{ '￥ ' + $keepTwoNum(allDeliverFee) }}</span>
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
        <PaymentPanel :cart-obj="orderObj" :amount="allAmount" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="payDialogVisible = false" round>确认 支付</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Wave from '@/components/Common/Wave';
import PaymentPanel from '@/components/Common/Pay/PaymentPanel';
import commonUtil from '@/utils/common';

export default {
  components: {
    Wave,
    PaymentPanel
  },
  data() {
    return {
      // 收银台
      payDialogVisible: false,
      address: {},
      expectTime: '',
      options: [],
      orderObj: {},
      businessHours: '8:30-20:30',
      // 总借阅费
      allBorrowCost: 0,
      // 总包装费
      allPackingCost: 0,
      // 总配送费
      allDeliverFee: 0,
      // 押金
      allDeposit: 0,
      // 总金额
      allAmount: 0
    };
  },
  created() {
    this.orderObj = this.$store.state.settlementObj.data;
    // 总借阅费
    this.allBorrowCost = this.$store.state.settlementObj.allBorrowCost;
    // 总包装费
    this.allPackingCost = this.$store.state.settlementObj.allPackingCost;
    // 总配送费
    this.allDeliverFee = this.$store.state.settlementObj.allDeliverFee;
    // 押金
    this.allDeposit = this.$store.state.settlementObj.allDeposit;
    // 总金额
    this.allAmount = this.$store.state.settlementObj.allAmount;
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
        this.address = this.getDefaultAddress();
      } else {
        this.address = this.$store.state.checkedAddress;
      }
    },
    /**
     * 获取默认地址
     */
    getDefaultAddress() {
      // 发送请求，获取默认地址
      // 测试
      return {
        id: 123,
        consignee: '张三',
        sex: 0,
        areaCode: '+86',
        phone: '13489568956',
        location: '广东省韶关市韶关大学',
        detail: '西区曼陀罗苑',
        label: '学校',
        isDefault: 0
      };
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
    handleSettlement() {
      // 发送请求，生成订单
      for (const key in this.orderObj) {
        this.orderObj[key].createTime = new Date();
        this.orderObj[key].payStatus = 0;
      }
      console.log('order =>', this.orderObj);
      this.payDialogVisible = true;
    },
    /**
     * 期望时间
     */
    handleExpectTime(val) {
      console.log('select time =>', val);
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
          label: '立即配送',
          value: commonUtil.formatDate(now, 'YYYY-mm-dd HH:MM')
        },
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
      for (let i = 1; i < options.length; i++) {
        let h = 0;
        let m = 0;
        let t = sTime + 20;
        while (t < eTime) {
          h = parseInt(t / 60);
          m = t % 60;
          options[i].children.push({
            label: h + ':' + m,
            value: h + ':' + m,
            disabled: i === 1 && t < nTime
          });
          t += 20;
        }
      }
      return options;
    },
    /**
     * 支付面板关闭
     */
    handlePayPanelClose() {
      // 暂时跳转到首页
      // this.$router.replace('/');
      this.$router.replace('/mine/cart');
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

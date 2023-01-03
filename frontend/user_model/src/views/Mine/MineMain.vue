<template>
  <!-- 我的-首页组件 -->
  <div class="mine-main">
    <!-- 上方信息卡 -->
    <div class="preview">
      <!-- 头像、昵称 -->
      <div class="avatar-box">
        <i class="logo ic-logo"></i>
        <div class="avatar-main-box">
          <div class="avatar">
            <div class="img">
              <img :src="userInfo.avatarUrl ? userInfo.avatarUrl : $noAvatarUrl" alt="user-avatar" />
            </div>
          </div>
          <div class="nickname">{{ userInfo.nickname }}</div>
        </div>
      </div>
      <!-- 钱包 -->
      <div class="wallet-box" :style="walletBoxStyle">
        <div class="title">我的钱包</div>
        <!-- 支持横向滚动 -->
        <div class="box" @mousewheel.stop="handleWalletBoxScroll" ref="scrollXBox">
          <!-- 零钱 -->
          <div class="wallet" @click.stop="handlePrevieward('wallet')">
            <div class="text-box">{{ walletVisible ? $keepTwoNum(userInfo.money) : '***' }}</div>
            <div class="title-box">
              <i class="ic-wallet"></i>
              <span>零钱</span>
            </div>
            <div class="visibility" @click.stop="walletVisible = !walletVisible">
              <i v-if="walletVisible" class="ic-eye-display"></i>
              <i v-else class="ic-eye-hide"></i>
            </div>
          </div>
          <!-- 优惠卷 -->
          <div class="coupon" @click.stop="handlePrevieward('coupon')">
            <div class="text-box">{{ userInfo.coupon ? userInfo.coupon.length : '无' }}</div>
            <div class="title-box">
              <i class="ic-coupon"></i>
              <span>优惠卷</span>
            </div>
          </div>
        </div>
      </div>
      <!-- 购物车 -->
      <div class="shop-cart">
        <div class="title">我的购物车</div>
        <div class="box" @click.stop="handlePrevieward('cart')">
          <i class="ic-shopping-cart"></i>
        </div>
      </div>
    </div>
    <!-- 下方数据卡 -->
    <div class="data-box">
      <!-- 订单 -->
      <div class="left-box">
        <div class="order-box">
          <div class="title">
            <span>我的订单</span>
          </div>
          <div class="order-state-box">
            <div class="order-state">
              <i class="ic-no-payment"></i>
              <span>待付款</span>
            </div>
            <div class="order-state">
              <i class="ic-delivery"></i>
              <span>待配送</span>
            </div>
            <div class="order-state">
              <i class="ic-return-books"></i>
              <span>待归还</span>
            </div>
            <div class="order-state">
              <i class="ic-evaluate-1"></i>
              <span>待评价</span>
            </div>
            <div class="order-state">
              <i class="ic-more-2"></i>
              <span>全部订单</span>
            </div>
          </div>
          <div class="content-block">
            <SvgPage class="no-order" name="no-order" :img-width="240" />
          </div>
        </div>
      </div>
      <!-- 右边工具 -->
      <div class="right-box">
        <div class="mess-box">
          <div class="title">
            <span>我的消息</span>
          </div>
          <div class="mess-type-box">
            <div class="mess-type" @click.stop="changeMessItem('sys')" :class="{ 'is-active': messActive === 'sys' }">
              <i class="ic-mess-1"></i>
              <span>系统消息</span>
            </div>
            <div class="mess-type" @click.stop="changeMessItem('per')" :class="{ 'is-active': messActive === 'per' }">
              <i class="ic-mess-2"></i>
              <span>私信</span>
            </div>
          </div>
          <div class="content-block">
            <SvgPage class="no-mess" name="no-mess" :img-width="156" />
          </div>
        </div>
        <div class="tool-box">
          <div class="title">
            <span>工具入口</span>
          </div>
          <div class="tool-item-box">
            <div class="tool-item">
              <i class="ic-address-books"></i>
              <span>地址簿</span>
            </div>
            <div class="tool-item">
              <i class="ic-star"></i>
              <span>收藏</span>
            </div>
            <div class="tool-item">
              <i class="ic-assess"></i>
              <span>评价</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 弹窗 -->
    <el-dialog
      class="mine-main-dialog"
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      :modal-append-to-body="false">
      <div v-if="dialogTitle === '钱包'" class="wallet-dialog">
        <div class="main-dialog">
          <div class="title">我的零钱(元)</div>
          <div class="money">{{ $keepTwoNum(userInfo.money) }}</div>
          <div class="btns">
            <div class="btn grey">充值</div>
            <div class="btn">提现</div>
          </div>
        </div>
        <div class="record-box">
          <div class="record-tit">近1个月零钱明细</div>
          <div class="record-list-box">
            <SvgPage v-if="moneyRecords.length < 1" name="no-content" :img-width="180" text="暂无零钱记录" />
            <div v-else class="record-list">
              <div class="record-item" v-for="(item, index) in moneyRecords" :key="'money-record-' + index">
                <div class="text">
                  <span class="type">{{ '[' + item.type + ']' }}</span>
                  <span
                    class="change"
                    :class="{
                      add: item.change === 1,
                      reduce: item.change === 0
                    }"
                    >{{ (item.change ? '+' : '-') + $keepTwoNum(item.amount) }}</span
                  >
                </div>
                <span class="time">{{ item.updateTime }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="dialogTitle === '我的优惠卷'" class="coupon-dialog">
        <SvgPage name="no-coupon" :img-width="240" text="暂无优惠卷" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import SvgPage from '@/components/Common/SvgPage';
import { getUserInfoApi } from '@/api/userApi';

export default {
  components: {
    SvgPage
  },
  data() {
    return {
      width: 0,
      userInfo: {
        avatarUrl: '',
        nickname: '',
        money: 0,
        coupon: null
      },
      walletVisible: false,
      // 消息卡选项的选择项 sys | per
      messActive: 'sys',
      // 弹窗控制参数
      dialogVisible: false,
      dialogTitle: '钱包',
      moneyRecords: []
    };
  },
  created() {
    this.moneyRecords = this.getRecordsTest(15);
    this.updateUserInfo();
  },
  mounted() {
    this.width = this.$el.offsetWidth;
    // this.getUserInfo();
  },
  methods: {
    /**
     * 更新用户信息
     */
    updateUserInfo() {
      getUserInfoApi(this.$store.state.userInfo.id).then(res => {
        if (res.data.flag) {
          // 获取成功
          this.$store.dispatch('setUserInfo', res.data.data);
          this.userInfo = this.$store.state.userInfo;
        }
      });
    },
    /**
     * 获取用户信息
     */
    getUserInfo() {
      this.userInfo = this.$store.state.userInfo;
      this.coupon = null;
    },
    /**
     * 横向滚动
     */
    handleWalletBoxScroll(event) {
      // 禁止默认滚动
      event.preventDefault();
      const box = this.$refs.scrollXBox;
      // 获取滚动方向
      const detail = event.wheelDelta || event.detail;
      // 定义滚动方向，其实也可以在赋值的时候写
      const moveForwardStep = 1;
      const moveBackStep = -1;
      // 定义滚动距离
      let step = 0;
      // 滚动幅度
      let speed = 36;
      // 判断滚动方向,这里的100可以改，代表滚动幅度，也就是说滚动幅度是自定义的
      if (detail < 0) {
        step = moveForwardStep * speed;
      } else {
        step = moveBackStep * speed;
      }
      // 对需要滚动的元素进行滚动操作
      box.scrollLeft += step;
    },
    /**
     * 点击消息卡选项
     */
    changeMessItem(text) {
      this.messActive = text;
    },
    /**
     * 点击上方工具卡片
     */
    handlePrevieward(text) {
      if (text === 'wallet') {
        this.dialogVisible = true;
        this.dialogTitle = '钱包';
      } else if (text === 'coupon') {
        this.dialogVisible = true;
        this.dialogTitle = '我的优惠卷';
      } else if (text === 'cart') {
        this.$router.push('/mine/cart');
      }
    },
    getRecordsTest(n) {
      let list = [];
      for (let i = 0; i < n; i++) {
        let record = {
          id: '1',
          type: '充值',
          change: i % 2 ? 1 : 0,
          amount: 8.37,
          updateTime: '2022-11-15 22:23'
        };
        list.push(record);
      }
      return list;
    }
  },
  computed: {
    walletBoxStyle() {
      let style = {};
      style.width = this.width - 540 + 'px';
      return style;
    }
  }
};
</script>

<style lang="less" scoped>
.preview {
  display: flex;
  align-items: center;
  position: relative;
  width: 100%;
  height: 240px;
  background-color: var(--white);
  border-radius: 12px;
  box-sizing: border-box;
  padding: 12px;
  .avatar-box {
    width: 270px;
    height: 270px;
    border-radius: 5px;
    background-color: var(--primary);
    position: absolute;
    top: -15px;
    left: 15px;
    box-shadow: 7px 0 3px -5px rgba(122, 122, 122, 0.5), -7px 0 3px -5px rgba(122, 122, 122, 0.5);
    .logo {
      margin-top: 5px;
      width: 100%;
      font-size: 112px;
      color: var(--white);
      opacity: 0.3;
      text-align: center;
    }
    .avatar-main-box {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      max-width: 180px;
      display: flex;
      align-items: center;
      flex-direction: column;
      .avatar {
        position: relative;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 136px;
        height: 136px;
        border-radius: 50%;
        border: 1px solid var(--white);
        .img {
          width: 120px;
          height: 120px;
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          border-radius: 50%;
          overflow: hidden;
          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }
      }
      .nickname {
        user-select: none;
        text-align: center;
        margin-top: 8px;
        font-size: 18px;
        font-weight: bold;
        color: var(--white);
      }
    }
  }
  .wallet-box,
  .shop-cart {
    display: flex;
    flex-direction: column;
    transition: all 0.5s ease-in-out;
    border-radius: 8px;
    .title {
      user-select: none;
      width: 100%;
      font-size: 18px;
      font-weight: bold;
      color: var(--primary-text);
      box-sizing: border-box;
      padding: 12px;
    }
    .box {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      border-radius: 8px;
      transition: all 0.5s ease-in-out;
    }
  }
  .wallet-box {
    margin-left: 288px;
    // flex-grow: 1;
    height: 100%;
    .box {
      width: 100%;
      overflow-x: auto;
      overflow-y: hidden;
      &::-webkit-scrollbar {
        height: 5px;
        background-color: transparent;
      }
      &::-webkit-scrollbar-thumb {
        width: 25px;
        border-radius: 2.5px;
        background-color: rgba(216, 216, 216, 0);
      }
      &:hover {
        &::-webkit-scrollbar-thumb {
          background-color: rgba(216, 216, 216, 0.5);
        }
      }
      .wallet,
      .coupon {
        flex-shrink: 0;
        margin-left: 12px;
        position: relative;
        width: 156px;
        height: 156px;
        border-radius: 8px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        transition: all 0.5s ease-in-out;
        .text-box {
          user-select: none;
          margin-top: 36px;
          position: relative;
          width: 100%;
          text-align: center;
          font-size: 36px;
          font-weight: bold;
          color: var(--primary-text);
          transition: all 0.5s ease-in-out;
        }
        .title-box {
          cursor: pointer;
          user-select: none;
          margin-bottom: 12px;
          display: flex;
          align-items: center;
          flex-direction: column;
          transition: all 0.5s ease-in-out;
          i {
            transition: all 0.5s ease-in-out;
            font-size: 21px;
            color: #666666;
          }
          span {
            transition: all 0.5s ease-in-out;
            margin-top: 3px;
            font-size: 14px;
            color: #666666;
          }
        }
        &:hover {
          background-color: rgba(218, 218, 218, 0.2);
        }
        &:active {
          box-shadow: inset 2px 2px 5px #babecc, inset -5px -5px 10px #fff;
          .text-box,
          .title-box {
            transform: translate(-2px, -2px);
          }
        }
        .visibility {
          position: absolute;
          top: 8px;
          right: 8px;
          width: 24px;
          height: 24px;
          display: flex;
          align-items: center;
          justify-content: center;
          opacity: 0.8;
          transition: opacity 0.5s ease;
          i {
            cursor: pointer;
            font-size: 12px;
            color: #d8d8d8;
          }
          .ic-eye-hide {
            font-size: 15px;
          }
          &:hover {
            opacity: 1;
          }
        }
      }
    }
  }
  .shop-cart {
    margin-left: 12px;
    width: 216px;
    height: 100%;
    .box {
      cursor: pointer;
      justify-content: center;
      i {
        font-size: 64px;
        color: #666666;
        transition: all 0.5s ease-in-out;
      }
      &:hover {
        background-color: rgba(218, 218, 218, 0.2);
      }
      &:active {
        box-shadow: inset 2px 2px 5px #babecc, inset -5px -5px 10px #fff;
        i {
          transform: translate(-2px, -2px);
          color: var(--primary);
        }
      }
    }
  }
}
.data-box {
  margin-top: 42px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  .left-box,
  .right-box {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .left-box {
    width: 64%;
  }
  .right-box {
    width: 32%;
  }
  .order-box,
  .mess-box,
  .tool-box {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-sizing: border-box;
    padding: 16px 12px;
    border-radius: 12px;
    background-color: var(--white);
    .title {
      user-select: none;
      box-sizing: border-box;
      padding: 0 12px;
      width: 100%;
      height: 32px;
      display: flex;
      align-items: center;
      border-bottom: 1px solid #d8d8d8;
      span {
        font-size: 18px;
        font-weight: bold;
        color: var(--primary-text);
      }
    }
    .content-block {
      display: flex;
      align-items: center;
      flex-direction: column;
    }
  }
  .order-box {
    .order-state-box {
      margin-top: 12px;
      width: 100%;
      height: 96px;
      box-sizing: border-box;
      padding: 0 36px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .order-state {
        cursor: pointer;
        width: 96px;
        height: 96px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        transition: all 0.5s ease-in-out;
        i {
          font-size: 36px;
          color: var(--regular-text);
        }
        span {
          user-select: none;
          margin-top: 5px;
          font-size: 14px;
          color: var(--regular-text);
        }
        &:hover {
          box-shadow: 0px 8px 32px 0px rgba(0, 0, 0, 0.16);
        }
        &:active {
          box-shadow: 0px 3px 12px 0px rgba(0, 0, 0, 0.16);
        }
      }
    }
    .content-block {
      margin-top: 24px;
      .no-order {
        margin: 24px 0;
      }
    }
  }
  .mess-box {
    .mess-type-box {
      width: 100%;
      height: 56px;
      margin-top: 12px;
      display: flex;
      align-items: center;
      justify-content: space-around;
      .mess-type {
        cursor: pointer;
        user-select: none;
        width: 50%;
        height: 56px;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        i {
          transition: color 0.5s ease-in-out;
          font-size: 25px;
          color: var(--regular-text);
        }
        span {
          transition: color 0.5s ease-in-out;
          margin-top: 5px;
          font-size: 13px;
          color: var(--regular-text);
        }
        &.is-active {
          i {
            color: var(--primary);
          }
        }
      }
    }
    .content-block {
      .no-mess {
        margin: 24px 0;
      }
    }
  }
  .tool-box {
    margin-top: 36px;
    .tool-item-box {
      margin-top: 12px;
      width: 100%;
      height: 86px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .tool-item {
        cursor: pointer;
        user-select: none;
        width: 86px;
        height: 86px;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        border-radius: 8px;
        transition: all 0.5s ease-in-out;
        i {
          font-size: 48px;
          color: var(--regular-text);
        }
        span {
          margin-top: 5px;
          font-size: 14px;
          color: var(--regular-text);
        }
        &:hover {
          box-shadow: 0px 6px 24px 0px rgba(0, 0, 0, 0.16);
        }
        &:active {
          box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.16);
        }
      }
    }
  }
}
.mine-main-dialog {
  :deep(.el-dialog) {
    width: 42%;
    .el-dialog__body {
      padding-top: 16px;
    }
  }
  .wallet-dialog {
    display: flex;
    align-items: center;
    flex-direction: column;
    box-sizing: border-box;
    padding: 0 24px;
    .main-dialog {
      user-select: none;
      width: 100%;
      height: 240px;
      display: flex;
      align-items: center;
      flex-direction: column;
      justify-content: space-between;
      box-sizing: border-box;
      padding: 16px 24px;
      border-radius: 12px;
      background-color: #f6f5ec;
      .title {
        font-size: 16px;
        font-weight: bold;
        color: var(--primary-text);
      }
      .money {
        font-size: 64px;
        font-weight: bold;
        color: var(--primary-text);
      }
      .btns {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
        box-sizing: border-box;
        padding: 0 12px;
        .btn {
          cursor: pointer;
          font-size: 24px;
          text-align: center;
          line-height: 56px;
          width: 210px;
          height: 56px;
          box-sizing: border-box;
          border-radius: 28px;
          color: var(--white);
          transition: background-color 0.5s ease-in-out;
          background-color: var(--primary-btn);
          &:hover {
            background-color: var(--primary-btn-h);
          }
          &:active {
            background-color: var(--primary-btn-a);
          }
          &.grey {
            background-color: var(--success-btn);
            &:hover {
              background-color: var(--success-btn-h);
            }
            &:active {
              background-color: var(--success-btn-a);
            }
          }
        }
      }
    }
    .record-box {
      margin-top: 18px;
      width: 100%;
      display: flex;
      align-items: center;
      flex-direction: column;
      .record-tit {
        user-select: none;
        width: 100%;
        height: 36px;
        border-radius: 8px;
        font-size: 16px;
        color: var(--primary-text);
        text-align: center;
        line-height: 36px;
        background-color: #f5f5f5;
      }
      .record-list-box {
        position: relative;
        width: 100%;
        height: 240px;
        margin-top: 12px;
        display: flex;
        align-items: center;
        flex-direction: column;
        overflow: hidden;
        .record-list {
          overflow-y: auto;
          width: 100%;
          display: flex;
          align-items: center;
          flex-direction: column;
          &::-webkit-scrollbar {
            display: none;
          }
          .record-item {
            display: flex;
            flex-shrink: 0;
            align-items: center;
            justify-content: space-between;
            width: 100%;
            height: 42px;
            border-radius: 8px;
            background-color: #f7f7f7;
            box-sizing: border-box;
            padding: 0 12px;
            .text {
              font-size: 16px;
              color: #666666;
              .type {
                color: #999999;
              }
              .change {
                margin-left: 12px;
                &.add {
                  color: var(--success);
                }
                &.reduce {
                  color: var(--danger);
                }
              }
            }
            .time {
              font-size: 14px;
              color: #666666;
            }
            & + .record-item {
              margin-top: 8px;
            }
          }
        }
      }
    }
  }
  .coupon-dialog {
    width: 100%;
    height: 360px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>

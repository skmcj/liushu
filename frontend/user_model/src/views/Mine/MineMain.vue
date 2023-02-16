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
            <div
              class="order-state"
              v-for="(state, index) in orderState"
              :key="'order-state-' + index"
              :class="{
                'is-active': navCheck === state.value
              }"
              @click="handleClickOrderState(state.value)">
              <i :class="state.icon"></i>
              <span>{{ state.name }}</span>
            </div>
          </div>
          <div class="content-block">
            <!-- 订单列表 -->
            <div class="order-list" v-if="!$isEmpty(orderList)">
              <!-- 订单项 -->
              <div
                class="order-item"
                v-for="(order, oi) in orderList"
                :key="'order-item-' + oi"
                @click.stop="handleDetail">
                <!-- 订单主要内容 -->
                <div class="order-content">
                  <!-- 订单基本信息 -->
                  <div class="order-mess-box">
                    <div class="order-mess-item">
                      <span class="label">订单号：</span>
                      <span class="text">{{ order.number }}</span>
                    </div>
                    <div class="order-mess-item">
                      <span class="label">商家名称：</span>
                      <span class="text link">{{ order.storeName }}</span>
                    </div>
                  </div>
                  <!-- 订单商品列表 -->
                  <div class="orderr-goods-box">
                    <div class="order-goods-list">
                      <div
                        class="order-goods-item"
                        v-for="(goods, gi) in order.orderItems"
                        :key="'order-goods-item-' + gi">
                        <div class="order-goods-content">
                          <div class="cover">
                            <img :src="goods.bookCover" alt="goods" />
                          </div>
                          <div class="title">{{ goods.bookName }}</div>
                        </div>
                        <div class="order-goods-amount">
                          <span>{{ '×' + goods.quantity }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 订单类型 -->
                <!-- 待付款 -->
                <div v-if="computeOrderStatus(order) === 'pay'" class="order-other">
                  <!-- 订单类型 -->
                  <!-- 待付款 -->
                  <div class="order-type">
                    <i class="ic-no-payment"></i>
                    <span>待付款</span>
                  </div>
                </div>
                <!-- 待配送 -->
                <div v-if="computeOrderStatus(order) === 'send'" class="order-other">
                  <!-- 待配送 -->
                  <div class="order-type">
                    <i class="ic-delivery"></i>
                    <span>待配送</span>
                  </div>
                </div>
                <!-- 待归还 -->
                <div v-if="computeOrderStatus(order) === 'back'" class="order-other">
                  <!-- 待归还 -->
                  <div class="order-type" v-if="order.status == 2">
                    <i class="ic-return-books"></i>
                    <span>待归还</span>
                  </div>
                  <!-- 待上门 -->
                  <div class="order-type" v-if="order.status == 3">
                    <i class="ic-repay"></i>
                    <span>待上门</span>
                  </div>
                  <!-- 逾期中 -->
                  <div class="order-type" v-if="order.status === 6">
                    <i class="ic-due"></i>
                    <span>逾期中</span>
                  </div>
                </div>
                <!-- 待评价 -->
                <div v-if="computeOrderStatus(order) === 'comment'" class="order-other">
                  <!-- 待评价 -->
                  <div class="order-type" v-if="order.status === 4">
                    <i class="ic-evaluate"></i>
                    <span>待评价</span>
                  </div>
                </div>
                <!-- 已完成 -->
                <div v-if="computeOrderStatus(order) === 'complete'" class="order-other">
                  <!-- 已完成 -->
                  <div class="order-type" v-if="order.status === 5">
                    <i class="ic-complete"></i>
                    <span>已完成</span>
                  </div>
                  <!-- 已逾期 -->
                  <div class="order-type" v-if="order.status === 7">
                    <i class="ic-overdue"></i>
                    <span>已逾期</span>
                  </div>
                </div>
                <!-- 退款/售后 -->
                <div v-if="computeOrderStatus(order) === 'after'" class="order-other">
                  <!-- 退款/售后 -->
                  <div class="order-type" v-if="order.amStatus === 0">
                    <i class="ic-as-audit"></i>
                    <span>售后审核中</span>
                  </div>
                  <div class="order-type" v-if="order.amStatus === 1">
                    <i class="ic-as-progress"></i>
                    <span>售后处理中</span>
                  </div>
                  <div class="order-type" v-if="order.amStatus === 2">
                    <i class="ic-after-sales"></i>
                    <span>售后完成</span>
                  </div>
                  <!-- 退款/售后 -->
                  <div class="order-type" v-if="order.amStatus === 3">
                    <i class="ic-as-fail"></i>
                    <span>售后申请失败</span>
                  </div>
                </div>
                <!-- 已取消订单 -->
                <div v-if="computeOrderStatus(order) === 'cancel'" class="order-other">
                  <!-- 已取消订单 -->
                  <div class="order-type">
                    <i class="ic-cancel-order"></i>
                    <span>订单已取消</span>
                  </div>
                </div>
              </div>
            </div>
            <SvgPage v-if="$isEmpty(orderList)" class="no-order" name="no-order" :img-width="240" />
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
            <template v-if="messActive === 'sys'">
              <div class="conversation-list">
                <div class="conversation-item" @click="handleClickCoversationSys('customerService')">
                  <div class="cover">
                    <img :src="customerSA" alt="客服" />
                  </div>
                  <div class="name">客服消息</div>
                </div>
              </div>
            </template>
            <template v-if="messActive === 'per'">
              <div class="conversation-list" v-if="conversationList.length > 0">
                <template v-for="item in conversationList">
                  <div
                    class="conversation-item"
                    :key="'sm-' + item.conversationID"
                    v-if="!excludeCoversationList[item.conversationID]"
                    @click="handleClickCoversationPer(item)">
                    <div class="cover">
                      <Avatar style="width: 56px; height: 56px" :src="avatar(item)" shape="circle" :type="item.type" />
                    </div>
                    <div class="name" :title="item.userProfile.nick || item.userProfile.userID">
                      {{ item.remark || item.userProfile.nick || item.userProfile.userID }}
                    </div>
                    <!-- 未读数 -->
                    <div class="unread-count" v-if="showUnreadCount(item)">
                      <span>{{ item.unreadCount > 99 ? '99+' : item.unreadCount }}</span>
                    </div>
                  </div>
                </template>
              </div>
              <SvgPage v-if="conversationList.length <= 0" class="no-mess" name="no-mess" :img-width="156" />
            </template>
          </div>
        </div>
        <div class="tool-box">
          <div class="title">
            <span>工具入口</span>
          </div>
          <div class="tool-item-box">
            <div class="tool-item" @click="handleAddress">
              <i class="ic-address-books"></i>
              <span>地址簿</span>
            </div>
            <div class="tool-item" @click="handleColl">
              <i class="ic-star"></i>
              <span>收藏</span>
            </div>
            <div class="tool-item" @click="handleComment">
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
            <div class="btn grey" @click="cardPkgVisible = true">充值</div>
            <div class="btn" @click="$showMsg('暂未实现，敬请期待')">提现</div>
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

    <el-dialog class="card-pkg-dg" title="卡包" :visible.sync="cardPkgVisible" append-to-body :modal="false">
      <div class="card-pkg-dg-main">
        <BankCard
          :card-number="getCardNumber(userInfo.id)"
          end-year="37"
          end-month="06"
          investor="流书网测试账户"
          :owner="userInfo.nickname"
          click
          @onSelect="onSelectCard" />
        <div class="bank-card-form">
          <el-form
            label-position="top"
            label-width="72px"
            :model="bankCardForm"
            :rules="bankCardRules"
            ref="bankCardForm">
            <el-form-item label="卡号" prop="cardNumber">
              <el-input v-model="bankCardForm.cardNumber" placeholder="请输入卡号"></el-input>
            </el-form-item>
            <el-form-item label="充值金额" prop="money">
              <el-input v-model="bankCardForm.money" placeholder="请输入充值金额"></el-input>
            </el-form-item>
            <el-form-item label="支付密码" prop="payPass">
              <el-input
                type="password"
                autocomplete="off"
                v-model="bankCardForm.payPass"
                placeholder="请输入支付密码"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onRecharge">充值</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import SvgPage from '@/components/Common/SvgPage';
import Avatar from '@/components/Chat/Message/Avatar';
import BankCard from '@/components/Common/BankCard/BankCard';
import { mapGetters, mapState } from 'vuex';
import customerSA from '@/assets/images/svg/customer-service.svg';
import { getUserInfoApi, rechargeOfUserApi } from '@/api/userApi';
import { getAllOrderOgPageApi, getOrderByStatusOfPageApi } from '@/api/orderApi';

export default {
  components: {
    SvgPage,
    Avatar,
    BankCard
  },
  data() {
    return {
      customerSA,
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
      moneyRecords: [],
      // 订单相关
      navCheck: 'pay',
      orderState: [
        { icon: 'ic-no-payment', name: '待付款', value: 'pay' },
        { icon: 'ic-delivery', name: '待配送', value: 'send' },
        { icon: 'ic-return-books', name: '待归还', value: 'back' },
        { icon: 'ic-evaluate-1', name: '待评价', value: 'comment' },
        { icon: 'ic-more-2', name: '全部订单', value: 'all' }
      ],
      orderList: [],
      currentPage: 1,
      pageSize: 5,
      // 卡包
      cardPkgVisible: false,
      bankCardForm: {
        cardNumber: '',
        money: '',
        payPass: ''
      },
      bankCardRules: {
        cardNumber: [{ required: true, message: '请输入卡号', trigger: 'blur' }],
        money: [{ required: true, message: '请输入充值金额', trigger: 'blur' }],
        payPass: [{ required: true, message: '请输入支付密码', trigger: 'blur' }]
      }
    };
  },
  created() {
    // this.moneyRecords = this.getRecordsTest(15);
    this.updateUserInfo();
  },
  mounted() {
    this.initOrderList();
    this.width = this.$el.offsetWidth;
    // this.getUserInfo();
  },
  methods: {
    initOrderList() {
      if (this.navCheck === 'all') {
        this.getOrderPage();
      } else if (this.navCheck === 'pay') {
        this.getOrderPageByStatus(1);
      } else if (this.navCheck === 'send') {
        this.getOrderPageByStatus(2);
      } else if (this.navCheck === 'back') {
        this.getOrderPageByStatus(3);
      } else if (this.navCheck === 'comment') {
        this.getOrderPageByStatus(4);
      } else if (this.navCheck === 'complete') {
        this.getOrderPageByStatus(5);
      } else if (this.navCheck === 'after') {
        this.getOrderPageByStatus(6);
      } else {
        this.getOrderPage();
      }
    },
    /**
     * 分页获取订单
     */
    getOrderPage() {
      getAllOrderOgPageApi(this.currentPage, this.pageSize).then(res => {
        if (res.data.flag) {
          this.orderList = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.$showMsg('订单数据获取失败，请稍后再试', { type: 'error' });
        }
      });
    },
    /**
     * 根据状态分页获取订单
     */
    getOrderPageByStatus(status) {
      getOrderByStatusOfPageApi(status, this.currentPage, this.pageSize).then(res => {
        if (res.data.flag) {
          this.orderList = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.$showMsg('订单数据获取失败，请稍后再试', { type: 'error' });
        }
      });
    },
    /**
     * 计算当前订单的状态 待付款 | 待配送 | 待归还 | 待评价
     * status 订单状态，0-待配送；1-待收货；2-待归还；3-待上门；4-待评价；5-已完成；6-逾期中；7-已逾期；8-售后中
     * amStatus 售后状态，0-待售后；1-待退款；2-已退款；3-待退货；4-已退货
     * payStatus 支付状态，0-未支付，1-已支付
     * tradeStatus 交易状态，0-进行中；1-已完成；2-已取消
     */
    computeOrderStatus(item) {
      if (item.tradeStatus === 2) {
        return 'cancel';
      }
      if (item.tradeStatus === 0 && item.payStatus === 0) {
        return 'pay';
      }
      if (item.status === 0 || item.status === 1) {
        return 'send';
      }
      if (item.status === 2 || item.status === 3 || item.status === 6) {
        return 'back';
      }
      if (item.status === 4) {
        return 'comment';
      }
      if (item.status === 5 || item.status === 7) {
        return 'complete';
      }
      if (item.status === 8) {
        return 'after';
      }
    },
    // 工具入口
    handleAddress() {
      this.$router.push('/mine/address');
    },
    handleColl() {
      this.$router.push('/mine/coll');
    },
    handleComment() {
      this.$router.push('/mine/comment');
    },
    /**
     * 点击订单
     */
    handleDetail() {
      this.$showMsg('请转到[我的订单]页面进行查看处理');
    },
    /**
     * 点击订单类别
     */
    handleClickOrderState(value) {
      if (value === 'all') {
        this.$router.push('/mine/order');
      } else {
        this.navCheck = value;
        this.initOrderList();
      }
    },
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
    /**
     * 点击私人消息会话
     */
    handleClickCoversationPer(conversation) {
      this.$bus.$emit('openChatWindow', 'owner');
      if (conversation.conversationID !== this.currentConversation.conversationID) {
        this.$store.dispatch('checkoutConversation', conversation.conversationID);
      }
    },
    /**
     * 点击系统消息会话
     */
    handleClickCoversationSys(cId) {
      this.$bus.$emit('openChatWindow', 'sys');
      let conversationID = '';
      switch (cId) {
        case 'customerService':
          conversationID = 'C2Ccustomerservice';
          break;
        default:
          break;
      }
      if (conversationID !== this.currentConversation.conversationID) {
        this.$store.dispatch('checkoutConversation', conversationID);
      }
    },
    /**
     * 会话头像
     */
    avatar(conversation) {
      switch (conversation.type) {
        case 'GROUP':
          return conversation.groupProfile.avatar;
        case 'C2C':
          return conversation.userProfile.avatar;
        default:
          return '';
      }
    },
    /**
     * 未读数量
     */
    showUnreadCount(conversation) {
      if (this.$store.getters.hidden) {
        return conversation.unreadCount > 0;
      }
      // 是否显示未读计数。当前会话和未读计数为0的会话，不显示。
      return this.currentConversation.conversationID !== conversation.conversationID && conversation.unreadCount > 0;
    },
    /**
     * 充值
     */
    onRecharge() {
      this.$refs.bankCardForm.validate(valid => {
        if (valid) {
          let card = {
            cardNumber: this.bankCardForm.cardNumber,
            money: this.bankCardForm.money,
            payPass: this.$sha256(this.bankCardForm.payPass)
          };
          console.log(card);
          rechargeOfUserApi(card)
            .then(res => {
              if (res.data.flag) {
                this.userInfo.money = res.data.data;
                this.$showMsg('充值成功', { type: 'success' });
                this.cardPkgVisible = false;
              }
            })
            .catch(err => {
              this.$showMsg('网络繁忙，请稍后重试', { type: 'error' });
            });
        }
      });
    },
    onSelectCard(card) {
      this.bankCardForm.cardNumber = card.cardNumber;
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
    },
    getTimeStr(dateStr, text, count = 2) {
      let date = new Date(dateStr);
      if (text === 'year') {
        return date
          .getFullYear()
          .toString()
          .slice(count * -1);
      }
      if (text === 'month') {
        return (date.getMonth() + 1).toString().padStart(count, '0');
      }
      if (text === 'day') {
        return date.getDate().toString().padStart(count, '0');
      }
    },
    getCardNumber(numStr) {
      return String(numStr).substr(1, 16);
    }
  },
  computed: {
    ...mapState({
      conversationList: state => state.conversation.conversationList,
      currentConversation: state => state.conversation.currentConversation,
      currentUserProfile: state => state.user.currentUserProfile,
      excludeCoversationList: state => state.conversation.excludeCoversationList
    }),
    ...mapGetters(['toAccount']),
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
        i,
        span {
          transition: color 0.5s ease-in-out;
        }
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
        &.is-active {
          i,
          span {
            color: var(--primary);
          }
        }
      }
    }
    .content-block {
      width: 100%;
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
      width: 100%;
      max-height: 240px;
      overflow-y: auto;
      &::-webkit-scrollbar {
        transition: all 0.5s ease-in-out;
        /*滚动条整体样式*/
        width: 0px; /*高宽分别对应横竖滚动条的尺寸*/
        height: 1px;
      }
      &::-webkit-scrollbar-thumb {
        /*滚动条里面小方块*/
        border-radius: 2.5px;
        box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.15);
        background: #9aa7b1;
      }
      &::-webkit-scrollbar-track {
        /*滚动条里面轨道*/
        box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.15);
        border-radius: 2.5px;
        background: #f5f3f2;
      }
      &:hover {
        &::-webkit-scrollbar {
          width: 5px;
        }
      }
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
.order-list {
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 100%;
  padding: 12px;
  box-sizing: border-box;
  .order-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    padding: 12px;
    box-sizing: border-box;
    border-radius: 8px;
    background-color: #f3f3f2;
    .order-other {
      height: 100%;
      display: flex;
      align-items: center;
      .order-type {
        user-select: none;
        display: flex;
        align-items: center;
        flex-direction: column;
        padding: 0 32px;
        box-sizing: border-box;
        i {
          font-size: 48px;
          color: #666666;
        }
        span {
          margin-top: 5px;
          font-size: 12px;
          color: #666666;
        }
      }
    }
    & + .order-item {
      margin-top: 12px;
    }
  }
}
.order-content {
  max-width: 75%;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  .order-mess-box {
    width: 100%;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .order-mess-item {
      user-select: none;
      display: flex;
      align-items: center;
      font-size: 12px;
      .label {
        color: var(--primary-text);
      }
      .text {
        color: #6b798e;
        &.link {
          cursor: pointer;
          transition: color 0.5s ease-in-out;
          &:hover {
            color: var(--link-text);
          }
        }
      }
    }
  }
  .orderr-goods-box {
    margin-top: 12px;
    width: 100%;
    border-radius: 8px;
    box-sizing: border-box;
    padding: 8px;
    background-color: #fff;
    overflow: hidden;
    .order-goods-list {
      display: flex;
      align-items: center;
      width: 100%;
      height: 92px;
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
      .order-goods-item {
        user-select: none;
        width: 92px;
        height: 92px;
        border-radius: 5px;
        overflow: hidden;
        display: flex;
        flex-shrink: 0;
        justify-content: space-between;
        align-items: flex-end;
        .order-goods-content {
          display: flex;
          flex-direction: column;
          width: 72px;
          height: 100%;
          align-items: center;
          justify-content: space-between;
          .cover {
            width: 72px;
            height: 72px;
            border-radius: 5px;
            overflow: hidden;
            img {
              width: 100%;
              height: 100%;
              object-fit: cover;
            }
          }
          .title {
            width: 100%;
            font-size: 12px;
            color: #666666;
            text-align: center;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
          }
        }
        .order-goods-amount {
          height: 100%;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: flex-end;
          span {
            font-size: 10px;
            color: #d8d8d8;
          }
        }
        & + .order-goods-item {
          margin-left: 8px;
        }
      }
    }
  }
  .order-tip-box {
    margin-top: 6px;
    padding-left: 18px;
    box-sizing: border-box;
    width: 100%;
    height: 24px;
    font-size: 12px;
    display: flex;
    align-items: center;
    color: #999;
    i {
      color: #f6ad49;
    }
    span {
      margin-left: 12px;
    }
  }
}
.conversation-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding: 5px 0;
  .conversation-item {
    position: relative;
    cursor: pointer;
    width: 100%;
    display: flex;
    align-items: center;
    box-sizing: border-box;
    padding: 8px;
    border-radius: 5px;
    box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.15);
    .cover {
      width: 56px;
      height: 56px;
      border-radius: 5px;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .name {
      font-size: 16px;
      font-weight: bold;
      margin-left: 8px;
    }
    .unread-count {
      position: absolute;
      top: 8px;
      right: 12px;
      padding: 0 6px;
      height: 18px;
      border-radius: 10px;
      background-color: #f35f5f;
      overflow: hidden;
      text-align: center;
      line-height: 18px;
      span {
        color: #fff;
        font-size: 10px;
      }
    }
    &:active {
      box-shadow: inset 0px -1px 0px 0px rgba(0, 0, 0, 0.15);
    }
    & + .conversation-item {
      margin-top: 8px;
    }
  }
}
.card-pkg-dg {
  .card-pkg-dg-main {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 24px 36px;
  }
  .bank-card-form {
    flex-grow: 1;
  }
  @media (max-width: 1280px) {
    .card-pkg-dg-main {
      flex-direction: column;
    }
  }
}
</style>

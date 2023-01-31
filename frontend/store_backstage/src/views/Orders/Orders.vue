<template>
  <div class="main-box orders">
    <div class="bar">
      <div class="left">
        <el-select v-model="selectCate" placeholder="请选择" class="option">
          <el-option v-for="item in searchCate" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <el-select
          v-if="inputFlag"
          v-model="searchContent"
          clearable
          :placeholder="inputTips[selectCate]"
          class="cates input">
          <template slot="prefix">
            <i class="icon fbookfont ic-search" @click="handleSearch"></i>
          </template>
          <el-option v-for="item in cates" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <el-input
          v-else
          v-model="searchContent"
          :placeholder="inputTips[selectCate]"
          style="width: 250px"
          clearable
          @keyup.enter.native="handleSearch"
          class="input">
          <i slot="prefix" class="icon fbookfont ic-search" style="cursor: pointer" @click="handleSearch"></i>
        </el-input>
        <el-button class="search-btn" round icon="icon fbookfont ic-search" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <!-- 订单表格 -->
    <el-table :data="tableData" stripe class="table" empty-text="暂无订单数据">
      <el-table-column width="180" prop="number" label="订单号"></el-table-column>
      <el-table-column prop="status" label="订单状态">
        <template slot-scope="scope">
          {{ getOrderStatusText(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column width="120" label="借阅时长">
        <template slot-scope="scope">
          {{ scope.row.borrowTime + '天' }}
        </template>
      </el-table-column>
      <el-table-column prop="consignee" label="联系人"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="expectedTime" label="期望时间"></el-table-column>
      <el-table-column class-name="order-tools" label="操作" width="160" align="center">
        <template slot-scope="{ row, $index }">
          <el-button type="text" size="small" title="查看详情" class="detail" @click="lookDetail(row, $index)"
            >查看</el-button
          >
          <el-button
            v-if="row.status === 0"
            type="text"
            size="small"
            title="确认配送"
            class="delivery"
            @click="delivery(row, $index)"
            >配送</el-button
          >
          <el-button
            v-if="row.status === 1"
            type="text"
            size="small"
            title="确认收货"
            class="receipt"
            @click="receipt(row, $index)"
            >收货</el-button
          >
          <el-button
            v-if="row.status === 3"
            type="text"
            size="small"
            title="确认回收"
            class="recycle"
            @click="recycle(row, $index)"
            >回收</el-button
          >
          <el-button
            v-if="row.isComment"
            type="text"
            size="small"
            title="回复用户"
            class="reply"
            @click="reply(row, $index)"
            >回复</el-button
          >
          <el-button
            v-if="row.status === 4"
            type="text"
            size="small"
            title="确认完成"
            class="complete"
            @click="complete(row, $index)"
            >完成</el-button
          >
          <el-button
            v-if="row.status === 6"
            type="text"
            size="small"
            title="标记逾期"
            class="overdue"
            @click="makeOverdue(row, $index)"
            >逾期</el-button
          >
          <!-- 售后 -->
          <el-button
            v-if="row.status === 8"
            type="text"
            size="small"
            title="处理售后"
            class="after-sales"
            @click="handleAfterSales(row, $index)"
            >售后</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="page"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="pageSize"
      :current-page="currentPage"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange">
    </el-pagination>
    <!-- 订单详情 -->
    <el-dialog title="订单详情" width="72%" :visible.sync="dgDetailVisible" class="order-data">
      <div class="order-mess">
        <div class="shop-list">
          <!-- 订单商品列表 -->
          <template>
            <div
              class="shop-item"
              v-for="(orderItem, index) in dgDetailOrder.orderItems"
              :key="'order-detail-item-' + index">
              <div class="img">
                <img :src="orderItem.bookCover" alt="" />
              </div>
              <div class="mess">
                <div class="title">{{ orderItem.bookName }}</div>
                <div class="num">{{ '× ' + orderItem.quantity }}</div>
                <div class="cost-item">
                  <span class="tit">借阅费</span>
                  <span class="text">{{ '￥ ' + orderItem.borrowCost }}</span>
                </div>
                <div class="cost-item">
                  <span class="tit">包装费</span>
                  <span class="text">{{ '￥ ' + orderItem.packingCost }}</span>
                </div>
                <div class="cost-item">
                  <span class="tit">押金</span>
                  <span class="text">{{ '￥ ' + orderItem.deposit }}</span>
                </div>
              </div>
            </div>
          </template>
        </div>
        <div class="detail-list">
          <!-- 详细信息 -->
          <div class="detail-item">
            <!-- 配送信息 -->
            <div class="info-item">
              <span class="tit">借阅时长</span>
              <span class="text">{{ dgDetailOrder.borrowTime + ' 天' }}</span>
            </div>
            <div class="info-item" v-if="dgDetailOrder.status === 7">
              <span class="tit">逾期时长</span>
              <span class="text">{{ dgDetailOrder.overdueTime + ' 天' }}</span>
            </div>
            <template v-if="dgDetailOrder.status < 3">
              <div class="info-item">
                <span class="tit">可续借时长</span>
                <span class="text">
                  <el-input-number
                    size="mini"
                    v-model="dgDetailOrder.renewDuration"
                    :min="1"
                    @change="handleRenewDurationChange"></el-input-number
                  >天
                </span>
                <div class="text-btn" v-if="dgChangeRD" @click.stop="handleChangeRD">修改</div>
              </div>
            </template>
            <template v-else>
              <div class="info-item">
                <span class="tit">可续借时长</span>
                <span class="text">{{ dgDetailOrder.renewDuration + ' 天' }} </span>
              </div>
            </template>
            <div class="info-item">
              <span class="tit">期望时间</span>
              <span class="text">{{ dgDetailOrder.expectedTime }}</span>
            </div>
            <div class="info-item">
              <span class="tit">联系信息</span>
              <span class="text">{{ dgDetailOrder.consignee + '\n' + dgDetailOrder.phone }}</span>
            </div>
            <div class="info-item">
              <span class="tit">配送地址</span>
              <span class="text">{{ dgDetailOrder.address }}</span>
            </div>
            <div class="info-item">
              <span class="tit">备注</span>
              <el-popover
                v-if="dgDetailOrder.remark"
                placement="top-start"
                width="360"
                trigger="hover"
                class="text remark"
                :open-delay="800"
                :disabled="dgDetailOrder.remark.length < 22">
                <span>{{ dgDetailOrder.remark }}</span>
                <span slot="reference">{{ comShowDesc(dgDetailOrder.remark, 22) }}</span>
              </el-popover>
              <span v-else class="text remark">无</span>
            </div>
          </div>
          <div class="detail-item">
            <!-- 订单信息 -->
            <div class="info-item">
              <span class="tit">订单号</span>
              <span class="text">{{ dgDetailOrder.number }}</span>
            </div>
            <div class="info-item">
              <span class="tit">订单状态</span>
              <span class="text">{{ getOrderStatusText(dgDetailOrder) }}</span>
            </div>
            <div class="info-item">
              <span class="tit">创建时间</span>
              <span class="text">{{ dgDetailOrder.createTime }}</span>
            </div>
            <div class="info-item">
              <span class="tit">支付时间</span>
              <span class="text">{{ dgDetailOrder.payTime }}</span>
            </div>
            <div class="info-item">
              <span class="tit">支付方式</span>
              <span class="text">在线支付</span>
            </div>
          </div>
          <div class="detail-item">
            <!-- 费用信息 -->
            <div class="info-item cost">
              <span class="tit">押金</span>
              <span class="text">{{ '￥ ' + getOrderAllCost(dgDetailOrder, 'deposit') }}</span>
            </div>
            <div class="info-item cost">
              <span class="tit">借阅费</span>
              <span class="text">{{ '￥ ' + getOrderAllCost(dgDetailOrder, 'borrow') }}</span>
            </div>
            <div class="info-item cost">
              <span class="tit">包装费</span>
              <span class="text">{{ '￥ ' + getOrderAllCost(dgDetailOrder, 'packing') }}</span>
            </div>
            <div class="info-item cost">
              <span class="tit">配送费</span>
              <span class="text">{{ '￥ ' + dgDetailOrder.deliveryFee }}</span>
            </div>
            <div class="info-item cost" v-if="dgDetailOrder.status === 7">
              <span class="tit">逾期费用</span>
              <span class="text">{{ '￥ ' + dgDetailOrder.overdueCost }}</span>
            </div>
            <div class="info-item cost">
              <span class="tit">优惠金额</span>
              <span class="text">{{ '￥ ' + dgDetailOrder.discountAmount }}</span>
            </div>
            <div class="info-item cost">
              <span class="tit">实收金额</span>
              <span class="text red">{{ '￥ ' + dgDetailOrder.amount }}</span>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleContactUser">联系用户</el-button>
      </span>
    </el-dialog>
    <!-- 售后dialog -->
    <el-dialog
      class="after-dialog"
      :title="'售后申请'"
      :visible.sync="afterDgVisable"
      :modal-append-to-body="false"
      :close-on-click-modal="afterBtnDisabled">
      <div class="dialog-main-box">
        <!-- 售后商品 -->
        <div class="order-content">
          <!-- 订单基本信息 -->
          <div class="order-mess-box">
            <div class="order-mess-item">
              <span class="label">订单号：</span>
              <span class="text">{{ afterDgOrder.number }}</span>
            </div>
            <div class="order-mess-item">
              <span class="label">创建时间：</span>
              <span class="text">{{ afterDgOrder.createTime }}</span>
            </div>
            <div class="order-mess-item">
              <span class="label">商家名称：</span>
              <span class="text">{{ afterDgOrder.storeName }}</span>
            </div>
          </div>
          <!-- 订单商品列表 -->
          <div class="orderr-goods-box">
            <div class="order-goods-list">
              <div
                class="order-goods-item"
                v-for="(goods, gi) in afterDgOrder.orderItems"
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
        <!-- 售后表单(类型、原因) -->
        <div class="dialog-form" v-if="!$isEmpty(afterDgForm)">
          <!-- 售后类型 -->
          <div class="dialog-form-item">
            <div class="dialog-form__label">售后类型：</div>
            <div class="dialog-form__content">
              <!-- 售后类型选择器 -->
              <div class="dialog-after-option">
                <span class="label">{{ afterDgOptions[afterDgForm.type].label }}</span>
                <span class="desc">{{ afterDgOptions[afterDgForm.type].desc }}</span>
              </div>
            </div>
          </div>
          <!-- 售后原因 -->
          <div class="dialog-form-item">
            <div class="dialog-form__label">售后原因：</div>
            <div class="dialog-form__content df-card">
              <!-- 如果售后凭证已生成 -->
              {{ afterDgForm.reason }}
            </div>
          </div>
          <!-- 售后补充凭证 -->
          <div class="dialog-form-item" v-if="afterDgForm.proof && !$isEmpty(afterDgForm.proofList)">
            <div class="dialog-form__label">售后补充凭证：</div>
            <div class="dialog-form__content">
              <div class="proof-list">
                <img
                  v-for="(img, ii) in afterDgForm.proofList"
                  :key="'proof-' + ii"
                  :src="afterDgForm.proof"
                  alt="售后凭证" />
              </div>
            </div>
          </div>
          <!-- 售后失败原因 -->
          <div class="dialog-form-item" v-if="afterDgForm.failReason || afterFailResVisible">
            <div class="dialog-form__label">售后失败原因：</div>
            <div class="dialog-form__content">
              <div class="reason disabled">
                <el-input
                  type="textarea"
                  :rows="5"
                  placeholder="请简要描述您拒绝售后的原因"
                  v-model="afterDgForm.failReason"
                  :disabled="!$isEmpty(afterDgForm.failReason) && afterDgForm.status !== 0">
                </el-input>
              </div>
            </div>
          </div>
          <!-- 售后退货模式 -->
          <div class="dialog-form-item" v-if="afterDgForm.status > 0">
            <div class="dialog-form__label">退货模式：</div>
            <div class="dialog-form__content">
              <!-- 售后类型选择器 -->
              <el-select
                popper-class="dialog-form_select"
                :popper-append-to-body="false"
                v-model="afterDgForm.returnMode"
                placeholder="请选择退货类型"
                :disabled="afterDgForm.status !== 1">
                <el-option
                  v-for="item in afterDgRMOptions"
                  :key="'as-dg-op-' + item.value"
                  :label="item.label"
                  :value="item.value">
                  <div class="option-item">
                    <span class="title">{{ item.label }}</span>
                    <span class="desc">{{ item.desc }}</span>
                  </div>
                </el-option>
              </el-select>
            </div>
          </div>
          <!-- 售后退款金额 -->
          <div class="dialog-form-item" v-if="afterDgForm.refundAmount">
            <div class="dialog-form__label">退款金额：</div>
            <div class="dialog-form__content">
              <div class="money-line">
                <span class="unit">￥</span>
                <span class="money">{{ $keepTwoNum(afterDgForm.refundAmount) }}</span>
              </div>
            </div>
          </div>
          <!-- 售后退货预约时间 -->
          <div class="dialog-form-item" v-if="afterDgForm.returnTime || afterDgForm.isReturn">
            <div class="dialog-form__label">预约时间：</div>
            <div class="dialog-form__content">{{ afterDgForm.returnTime }}</div>
          </div>
          <!-- 售后退货回收时间 -->
          <div class="dialog-form-item" v-if="afterDgForm.recycleTime">
            <div class="dialog-form__label">回收时间：</div>
            <div class="dialog-form__content">
              <div class="time-line">
                <span class="time">{{ afterDgForm.recycleTime }}</span>
              </div>
            </div>
          </div>
          <!-- 售后退款时间 -->
          <div class="dialog-form-item" v-if="afterDgForm.refundTime">
            <div class="dialog-form__label">退款时间：</div>
            <div class="dialog-form__content">
              <div class="time-line">
                <span class="time">{{ afterDgForm.refundTime }}</span>
              </div>
            </div>
          </div>
          <!-- 售后状态 -->
          <div class="dialog-form-item" v-if="!$isEmpty(afterDgForm.status)">
            <div class="dialog-form__label">售后状态：</div>
            <div class="dialog-form__content">
              <div class="status-line">
                <span class="status">{{ afterDgStatus[afterDgForm.status] }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <!-- 拒绝售后需填写拒绝原因 -->
        <el-button v-if="afterDgForm.status === 0 && !afterFailResVisible" @click.stop="beforeRefuseAfS" type="primary"
          >拒绝售后</el-button
        >
        <el-button v-if="afterDgForm.status === 0 && afterFailResVisible" @click.stop="refuseAfS" type="primary"
          >拒绝售后</el-button
        >
        <el-button v-if="afterDgForm.status === 1 && !afterFailResVisible" @click.stop="beforeRefuseAfS" type="primary"
          >关闭售后</el-button
        >
        <el-button v-if="afterDgForm.status === 1 && afterFailResVisible" @click.stop="refuseAfs" type="primary"
          >关闭售后</el-button
        >
        <el-button v-if="afterDgForm.status === 0" @click.stop="agreeAfS" type="primary">同意售后</el-button>
        <el-button v-if="afterDgForm.status === 1" @click.stop="updateAfSMode" type="primary">修改退货模式</el-button>
        <el-button v-if="afterDgForm.status === 2" @click.stop="confirmRecycleOfAfS" type="primary">确认回收</el-button>
        <el-button v-if="afterDgForm.status === 3" @click.stop="confirmRefund" type="primary">确认退款</el-button>
        <!-- <el-button v-if="!afterBtnDisabled" type="primary" @click="submitAfterSales">
          {{ afterDgForm.isReturn ? '预约 退货' : '提交 申请' }}
        </el-button> -->
      </span>
    </el-dialog>
    <!-- 评价 -->
    <el-dialog
      class="comment-dialog"
      :title="'订单号：' + commentDgTitle"
      :visible.sync="commentDgVisable"
      :modal-append-to-body="false"
      :close-on-click-modal="false">
      <div class="goods-comment-list">
        <div class="goods-comment-item" v-for="(orderItem, ci) in itemCommentList" :key="'comment-item-' + ci">
          <Comment
            :item-id="orderItem.id"
            :comment="orderItem.comment"
            disabled
            :is-delete="false"
            @reply="handleSubmitReply" />
        </div>
      </div>
      <span slot="footer" class="dialog-footer"></span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import Comment from '@/components/Common/Comment/Comment';
import { replyComentByStoreApi } from '@/api/commentApi';
import {
  getOrdersApi,
  getOrdersTestApi,
  getOrderByIdApi,
  deliveryOfOrderApi,
  confirmOfOrderApi,
  confirmCompleteApi,
  recycleOfOrderApi,
  beOverdueOfOrderApi,
  agreeAfterSalesServiceApi,
  refuseAfterSalesServiceApi,
  updateRModeOfAfterSalesApi,
  confirmReceiptOfASOrderApi,
  updateRenewDurationOfOrderApi,
  refundOfOrderApi
} from '@/api/orderApi';

export default {
  components: {
    Comment
  },
  data() {
    return {
      searchCate: [
        {
          value: 'number',
          label: '订单号'
        },
        {
          value: 'status',
          label: '订单状态'
        },
        {
          value: 'consignee',
          label: '联系人'
        },
        {
          value: 'phone',
          label: '手机号'
        },
        {
          value: 'address',
          label: '地址'
        }
      ],
      selectCate: 'number',
      searchContent: '',
      inputTips: {
        number: '请输入订单号',
        status: '请选择订单状态',
        consignee: '请输入联系人姓名',
        phone: '请输入手机号',
        address: '请输入收货地址'
      },
      stateStr: ['待配送', '待收货', '待归还', '待上门', '待评价', '已完成', '逾期中', '已逾期', '售后'],
      aSStateStr: ['待审核', '待退货', '待回收', '待退款', '已退货', '已退款', '待换货', '售后已结束'],
      amStateStr: ['待售后', '售后中', '已结束', '售后失败'],
      inputFlag: false,
      cates: [
        {
          value: 0,
          label: '待配送'
        },
        {
          value: 1,
          label: '待收货'
        },
        {
          value: 2,
          label: '待归还'
        },
        {
          value: 3,
          label: '待上门'
        },
        {
          value: 4,
          label: '待评价'
        },
        {
          value: 5,
          label: '已完成'
        },
        {
          value: 6,
          label: '逾期中'
        },
        {
          value: 7,
          label: '已逾期'
        },
        {
          value: 8,
          label: '售后中'
        }
      ],
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      total: 100,
      // 详情
      dgDetailVisible: false,
      dgDetailOrder: {},
      dgDetailRenewDuration: 0,
      dgChangeRD: false,
      dgDetailIndex: 0,
      // 测试数据
      renewDuration: 0,
      remark: '尽快配送',
      // 售后
      afterDgVisable: false,
      afterDgOrder: {},
      afterDgIndex: 0,
      afterDgForm: {},
      afterBtnDisabled: false,
      afterFailResVisible: false,
      afterDgStatus: {
        0: '审核中',
        1: '待退货',
        2: '待回收',
        3: '待退款',
        4: '已退货',
        5: '已退款',
        6: '待换货',
        7: '售后已结束'
      },
      afterDgOptions: [
        { label: '退款(无需退货)', desc: '没收到货，或与商家协商同意不用退货只退款' },
        { label: '退货退款', desc: '已收到货，需归还已收到的货物' }
      ],
      afterDgRMOptions: [
        { label: '7天期限', value: 0 },
        { label: '14天期限', value: 1 },
        { label: '无期限', value: 2 }
      ],
      // 评价
      commentDgVisable: false,
      commentDgOrder: {},
      commentDgIndex: 0,
      commentDgTitle: '',
      itemCommentList: []
    };
  },
  created() {
    this.getOrderByPage();
  },
  methods: {
    /**
     * 获取订单状态标签
     */
    getOrderStatusText(order) {
      if (order.status < 8) return this.stateStr[order.status];
      else {
        if (order.afterSales) return this.aSStateStr[order.afterSales.status];
        else {
          return this.amStateStr[order.amStatus];
        }
      }
    },
    /**
     * 获取订单总借阅费
     */
    getOrderAllCost(order, type = 'borrow') {
      if (!order.orderItems) return 0;
      let amount = 0;
      for (let item of order.orderItems) {
        if (type === 'borrow') {
          amount += item.borrowCost;
          continue;
        }
        if (type === 'packing') {
          amount += item.packingCost;
          continue;
        }
        if (type === 'deposit') {
          amount += item.deposit;
          continue;
        }
      }
      return this.accToFixed(amount);
    },
    /**
     * 执行搜索
     */
    handleSearch() {
      this.currentPage = 1;
      this.getOrderByPage();
    },
    /**
     * 查看订单详情
     */
    lookDetail(order, index) {
      this.dgChangeRD = false;
      this.dgDetailVisible = true;
      this.dgDetailOrder = order;
      this.dgDetailIndex = index;
      this.dgDetailRenewDuration = order.renewDuration;
    },
    /**
     * 联系用户
     */
    handleContactUser() {
      if (!this.dgDetailOrder.userId) return;
      this.dgDetailVisible = false;
      this.$bus.$emit('openChatWindow', 'owner');
      let conversationID = `C2C${this.dgDetailOrder.userId}`;
      if (conversationID !== this.currentConversation.conversationID) {
        this.$store.dispatch('checkoutConversation', conversationID);
      }
    },
    /**
     * 配送
     */
    delivery(order, index) {
      console.log(index);
      this.$confirm('是否确认配送完成?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        customClass: 'confirm-box',
        type: 'warning'
      })
        .then(() => {
          // 配送操作
          deliveryOfOrderApi(order.id)
            .then(res => {
              if (res.data.flag) {
                // 配送完成，更新信息
                return getOrderByIdApi(order.id);
              }
            })
            .then(res => {
              if (res.data.flag) {
                this.$set(this.tableData, index, res.data.data);
                // this.tableData[index] = res.data.data;
                // this.$forceUpdate();
              }
            })
            .catch(err => {
              this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
            });
        })
        .catch(() => {});
    },
    /**
     * 收货
     */
    receipt(order, index) {
      this.$confirm('是否为用户确认收货?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        customClass: 'confirm-box',
        type: 'warning'
      })
        .then(() => {
          confirmOfOrderApi(order.id)
            .then(res => {
              if (res.data.flag) {
                return getOrderByIdApi(order.id);
              } else {
                this.$showMsgs(res.data.msg, { type: 'error' });
                return { data: {} };
              }
            })
            .then(res => {
              if (res.data.flag) {
                this.$set(this.tableData, index, res.data.data);
                // this.tableData[index] = res.data.data;
                // this.$forceUpdate();
              }
            })
            .catch(err => {
              this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
            });
        })
        .catch(() => {});
    },
    /**
     * 回收
     */
    recycle(order, index) {
      this.$confirm('是否确认回收完成?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        customClass: 'confirm-box',
        type: 'warning'
      })
        .then(() => {
          recycleOfOrderApi(order.id)
            .then(res => {
              if (res.data.flag) {
                return getOrderByIdApi(order.id);
              }
            })
            .then(res => {
              if (res.data.flag) {
                // this.tableData[index] = res.data.data;
                // console.log(this.tableData[index]);
                this.$set(this.tableData, index, res.data.data);
              }
            })
            .catch(err => {
              this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
            });
        })
        .catch(() => {});
    },
    /**
     * 回复
     */
    reply(order, index) {
      this.itemCommentList = [];
      for (let orderItem of order.orderItems) {
        if (!orderItem.comment) {
          let comment = {
            orderId: orderItem.orderId,
            orderItemId: orderItem.id,
            bookId: orderItem.bookId,
            storeId: order.storeId,
            content: '',
            score: 0,
            isAnonymous: 0,
            bookCover: orderItem.bookCover,
            bookName: orderItem.bookName
          };
          orderItem.comment = comment;
        }
        this.itemCommentList.push(orderItem);
      }
      this.commentDgTitle = order.number;
      this.commentDgVisable = true;
      this.commentDgOrder = order;
      this.commentDgIndex = index;
    },
    /**
     * 提交回复
     */
    handleSubmitReply(reply) {
      // console.log('提交回复 ==>', reply);
      replyComentByStoreApi(reply)
        .then(res => {
          if (res.data.flag) {
            return getOrderByIdApi(this.commentDgOrder.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.$set(this.tableData, this.commentDgIndex, res.data.data);
            this.$showMsgs('回复成功', { type: 'success' });
          }
        })
        .catch(err => {
          this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
        })
        .finally(() => {
          this.commentDgVisable = false;
        });
    },
    /**
     * 完成
     */
    complete(order, index) {
      this.$confirm('是否为用户确认完成?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        customClass: 'confirm-box',
        type: 'warning'
      })
        .then(() => {
          confirmCompleteApi(order.id)
            .then(res => {
              if (res.data.flag) {
                return getOrderByIdApi(order.id);
              } else {
                this.$showMsgs(res.data.msg, { type: 'error' });
                return { data: {} };
              }
            })
            .then(res => {
              if (res.data.flag) {
                // this.tableData[index] = res.data.data;
                // console.log(this.tableData[index]);
                this.$set(this.tableData, index, res.data.data);
              }
            })
            .catch(err => {
              this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
            });
        })
        .catch(() => {});
    },
    /**
     * 标记逾期
     */
    makeOverdue(order, index) {
      this.$confirm('是否确认将该订单标记为逾期?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        customClass: 'confirm-box',
        type: 'warning'
      })
        .then(() => {
          beOverdueOfOrderApi(order.id)
            .then(res => {
              if (res.data.flag) {
                return getOrderByIdApi(order.id);
              } else {
                this.$showMsgs(res.data.msg);
                return { data: {} };
              }
            })
            .then(res => {
              if (res.data.flag) {
                // this.tableData[index] = res.data.data;
                // console.log(this.tableData[index]);
                this.$set(this.tableData, index, res.data.data);
              }
            })
            .catch(err => {
              this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
            });
        })
        .catch(() => {});
    },
    /**
     * 处理售后
     */
    handleAfterSales(order, index) {
      this.afterFailResVisible = false;
      this.afterBtnDisabled = true;
      this.afterDgVisable = true;
      this.afterDgOrder = order;
      this.afterDgForm = order.afterSales;
    },
    /**
     * 准备拒绝售后请求
     */
    beforeRefuseAfS() {
      this.$showMsgs('请填写拒绝原因');
      this.afterFailResVisible = true;
    },
    /**
     * 拒绝售后
     */
    refuseAfS() {
      if (this.$isEmpty(this.afterDgForm.failReason)) {
        this.$showMsgs('拒绝原因不能为空');
        return;
      }
      refuseAfterSalesServiceApi(this.afterDgForm.id, this.afterDgForm.failReason)
        .then(res => {
          if (res.data.flag) {
            return getOrderByIdApi(this.afterDgOrder.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.$set(this.tableData, this.afterDgIndex, res.data.data);
          }
        })
        .catch(err => {
          this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
        })
        .finally(() => {
          this.afterDgVisable = false;
        });
    },
    /**
     * 同意售后
     */
    agreeAfS() {
      agreeAfterSalesServiceApi(this.afterDgForm.id)
        .then(res => {
          if (res.data.flag) {
            return getOrderByIdApi(this.afterDgOrder.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.$set(this.tableData, this.afterDgIndex, res.data.data);
          }
        })
        .catch(err => {
          this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
        })
        .finally(() => {
          this.afterDgVisable = false;
        });
    },
    /**
     * 修改退货模式
     */
    updateAfSMode() {
      // console.log(this.afterDgForm.returnMode);
      updateRModeOfAfterSalesApi(this.afterDgForm.id, this.afterDgForm.returnMode)
        .then(res => {
          if (res.data.flag) {
            return getOrderByIdApi(this.afterDgOrder.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.$set(this.tableData, this.afterDgIndex, res.data.data);
          }
        })
        .catch(err => {
          this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
        });
    },
    /**
     * 确认售后回收
     */
    confirmRecycleOfAfS() {
      confirmReceiptOfASOrderApi(this.afterDgForm.id)
        .then(res => {
          if (res.data.flag) {
            return getOrderByIdApi(this.afterDgOrder.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.$set(this.tableData, this.afterDgIndex, res.data.data);
          }
        })
        .catch(err => {
          this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
        })
        .finally(() => {
          this.afterDgVisable = false;
        });
    },
    /**
     * 确认退款
     */
    confirmRefund() {
      refundOfOrderApi(this.afterDgOrder.id)
        .then(res => {
          if (res.data.flag) {
            this.$showMsgs('退款成功', { type: 'success' });
            return getOrderByIdApi(this.afterDgOrder.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.$set(this.tableData, this.afterDgIndex, res.data.data);
          }
        })
        .catch(err => {
          this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
        })
        .finally(() => {
          this.afterDgVisable = false;
        });
    },
    /**
     * 处理订单可续借时长值改变
     */
    handleRenewDurationChange(currentVal, oldVal) {
      if (currentVal !== this.dgDetailRenewDuration) this.dgChangeRD = true;
      else this.dgChangeRD = false;
    },
    /**
     * 修改订单可借阅时长
     */
    handleChangeRD() {
      // console.log('修改订单可借阅时长', this.dgDetailOrder.renewDuration);
      updateRenewDurationOfOrderApi(this.dgDetailOrder.id, this.dgDetailOrder.renewDuration)
        .then(res => {
          if (res.data.flag) {
            return getOrderByIdApi(this.afterDgOrder.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.$set(this.tableData, this.dgDetailIndex, res.data.data);
          }
        })
        .catch(err => {
          this.$showMsgs('网络繁忙，请稍后重试', { type: 'error' });
        })
        .finally(() => {
          this.dgDetailVisible = false;
        });
    },
    /**
     * 处理图书每页数量
     */
    handleSizeChange(val) {
      this.pageSize = val;
      this.getOrderByPage();
    },
    /**
     * 处理图书当前页信息
     */
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getOrderByPage();
    },
    /**
     * 获取图书信息
     */
    getOrderByPage() {
      let param = {
        page: this.currentPage,
        pageSize: this.pageSize
      };
      param[this.selectCate] = this.searchContent;
      getOrdersApi(param).then(
        res => {
          // console.log('order =>', res);
          if (res.data.flag) {
            this.tableData = res.data.data.records;
            this.total = res.data.data.total;
          } else {
            this.$showMsgs('订单获取失败，请稍后重试', { type: 'error' });
          }
        },
        err => {
          console.log('get order err =>', err);
        }
      );
    },
    /**
     * 计算显示的描述文本
     * @param {string} desc - 完整的描述文本
     * @param {number} num - 显示的字数
     * @return {string} 展示的描述文本
     */
    comShowDesc(desc, num) {
      if (desc.length <= num) {
        return desc;
      } else {
        return desc.substr(0, num - 1) + '···';
      }
    },
    /**
     * 保留2位小数
     */
    accToFixed(num) {
      return parseFloat(parseFloat(num).toFixed(2));
    }
  },
  computed: {
    ...mapState({
      currentConversation: state => state.conversation.currentConversation
    })
  },
  watch: {
    /**
     * 监听selectCate值的变化
     */
    selectCate(val) {
      this.searchContent = '';
      if (val === 'status') {
        this.inputFlag = true;
      } else {
        this.inputFlag = false;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.orders {
  .bar {
    .left {
      display: flex;
      align-items: center;
      .option {
        width: 120px;
        margin-right: 24px;
      }
      .input {
        margin-right: 24px;
      }
      /* 搜索按钮 */
      .search-btn {
        color: var(--white);
        border: none;
        padding: 12px 24px;
        background-color: var(--primary);
        &:focus {
          color: var(--white);
          background-color: var(--primary);
        }
        &:hover {
          color: var(--white);
          background-color: var(--primary-btn-h);
        }
        &:active {
          background-color: var(--primary-btn-a);
        }
        :deep(.icon) {
          margin-right: 8px;
        }
      }
    }
  }
  .order-data {
    :deep(.el-dialog__body) {
      padding: 0;
      margin-top: 36px;
    }
    :deep(.el-dialog__header) {
      .el-dialog__title {
        font-size: 24px;
        font-weight: bold;
      }
    }
    .order-mess {
      width: 100%;
      display: flex;
      flex-direction: column;
    }
    .shop-list {
      box-sizing: border-box;
      padding: 0 30px;
      display: grid;
      position: relative;
      width: 100%;
      gap: 20px 18px;
      .shop-item {
        border-radius: 8px;
        overflow: hidden;
        display: flex;
        align-items: center;
        height: 180px;
        background-color: var(--gray-bg);
        .img {
          margin-left: 10px;
          width: 160px;
          height: 160px;
          border-radius: 5px;
          overflow: hidden;
          img {
            object-fit: cover;
            width: 100%;
            height: 100%;
          }
        }
        .mess {
          height: 160px;
          box-sizing: border-box;
          display: flex;
          flex-direction: column;
          flex-grow: 1;
          padding: 4px 15px 4px 12px;
          .title {
            font-size: 14px;
            color: var(--primary-font);
          }
          .num {
            font-size: 12px;
            color: #b2bfc3;
            margin: 5px 0 0 3px;
            margin-bottom: 12px;
          }
          .cost-item {
            width: 100%;
            box-sizing: border-box;
            padding-left: 3px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            & + .cost-item {
              margin-top: 5px;
            }
            .tit {
              font-size: 12px;
              color: var(--cost-title);
            }
            .text {
              font-size: 12px;
              color: #474a4d;
            }
          }
        }
      }
    }
    @media (min-width: 1400px) and (max-width: 1559.9px) {
      .shop-list {
        grid-template-columns: repeat(3, 1fr);
      }
    }
    @media (min-width: 1600px) {
      .shop-list {
        grid-template-columns: repeat(4, 1fr);
      }
    }
    .detail-list {
      margin-top: 42px;
      margin-bottom: 32px;
      padding: 12px 0;
      width: 100%;
      min-height: 240px;
      display: flex;
      justify-content: space-between;
      .detail-item {
        width: calc(100% / 3);
        // height: 100%;
        padding: 12px 45px;
        box-sizing: border-box;
        & + .detail-item {
          border-left: solid 1px #d3ccd6;
        }
        .info-item {
          width: 100%;
          display: flex;
          align-items: center;
          & + .info-item {
            margin-top: 8px;
          }
          &.cost {
            .text {
              text-align: right;
            }
          }
          .tit {
            width: 35%;
            flex-shrink: 0;
            font-size: 16px;
            color: var(--cost-title);
          }
          .text {
            // width: 65%;
            flex-grow: 1;
            font-size: 16px;
            color: var(--primary-font);
            white-space: pre-wrap;
            // overflow: hidden;
            // text-overflow: ellipsis;
            // white-space: nowrap;
            &.red {
              color: var(--delete);
            }
            &.remark {
              word-break: break-all;
              text-overflow: ellipsis;
              display: -webkit-box;
              white-space: normal;
              -webkit-box-orient: vertical;
              -webkit-line-clamp: 2; /* 这里是超出几行省略 */
            }
            :deep(.el-input-number--mini) {
              margin-right: 5px;
              width: 96px;
              line-height: 23px;
              .el-input-number__decrease,
              .el-input-number__increase {
                width: 24px;
                &:hover {
                  color: var(--primary);
                  &:not(.is-disabled) ~ .el-input {
                    .el-input__inner:not(.is-disabled) {
                      border-color: var(--primary-border-f);
                    }
                  }
                }
                &.is-disabled {
                  color: #c0c4cc;
                }
              }
              .el-input__inner {
                height: 25px;
                line-height: 25px;
                padding: 0 25px;
                &:focus {
                  border-color: var(--primary-border-f);
                }
              }
            }
          }
          .text-btn {
            user-select: none;
            cursor: pointer;
            flex-shrink: 0;
            font-size: 14px;
            box-sizing: border-box;
            padding: 0 5px;
            color: var(--primary);
            &:hover {
              opacity: 0.8;
            }
          }
        }
      }
    }
  }
  :deep(.el-table) {
    .order-tools {
      .cell {
        display: flex;
        flex-wrap: wrap;
        gap: 0 12px;
        justify-content: center;
        .el-button {
          &:not(.disabled) {
            &.detail {
              color: var(--detail-btn);
              &:hover {
                color: var(--detail-btn-h);
              }
              &:active {
                color: var(--detail-btn-a);
              }
            }
            &.delivery {
              color: var(--delivery-btn);
              &:hover {
                color: var(--delivery-btn-h);
              }
              &:active {
                color: var(--delivery-btn-a);
              }
            }
            &.receipt {
              color: var(--receipt-btn);
              &:hover {
                color: var(--receipt-btn-h);
              }
              &:active {
                color: var(--receipt-btn-a);
              }
            }
            &.recycle {
              color: var(--recycle-btn);
              &:hover {
                color: var(--recycle-btn-h);
              }
              &:active {
                color: var(--recycle-btn-a);
              }
            }
            &.reply {
              color: var(--reply-btn);
              &:hover {
                color: var(--reply-btn-h);
              }
              &:active {
                color: var(--reply-btn-a);
              }
            }
            &.complete {
              color: var(--complete-btn);
              &:hover {
                color: var(--complete-btn-h);
              }
              &:active {
                color: var(--complete-btn-a);
              }
            }
            &.overdue {
              color: var(--overdue-btn);
              &:hover {
                color: var(--overdue-btn-h);
              }
              &:active {
                color: var(--overdue-btn-a);
              }
            }
            &.after-sales {
              color: var(--after-sales-btn);
              &:hover {
                color: var(--after-sales-btn-h);
              }
              &:active {
                color: var(--after-sales-btn-a);
              }
            }
          }

          & + .el-button {
            margin: 0;
          }
        }
      }
    }
  }
}
.after-dialog {
  :deep(.el-dialog) {
    .el-button--primary {
      border-radius: 20px;
      padding: 12px 36px;
    }
    .el-dialog__header {
      .el-dialog__title {
        font-weight: 500;
      }
    }
  }
  .dialog-main-box {
    width: 100%;
    display: flex;
    flex-direction: column;
    .order-content {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      border-radius: 12px;
      background-color: #e6e6e6;
      box-sizing: border-box;
      padding: 12px;
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
    }
    .other-mess {
      margin-top: 12px;
      display: flex;
      flex-direction: column;
      .other-mess-item {
        user-select: none;
        display: flex;
        align-items: center;
        .label {
          font-size: 14px;
          color: #666666;
        }
        .text {
          margin-left: 6px;
          font-size: 14px;
          color: #999;
        }
        & + .other-mess-item {
          margin-top: 6px;
        }
      }
    }
    .tip-box {
      margin-top: 12px;
      width: 100%;
      height: 32px;
      display: flex;
      align-items: center;
      box-sizing: border-box;
      padding: 0 12px;
      background-color: rgba(243, 166, 148, 0.15);
      i {
        font-size: 16px;
        color: #999;
      }
      span {
        margin-left: 6px;
        font-size: 14px;
        color: #999;
      }
    }
    .dialog-form {
      width: 100%;
      margin-top: 12px;
      display: flex;
      flex-direction: column;
      .dialog-form-item {
        width: 100%;
        display: flex;
        align-items: center;
        .dialog-form__label {
          min-width: 8em;
          user-select: none;
          font-size: 12px;
          color: #666;
          text-align: right;
          margin-right: 5px;
        }
        .dialog-form__content {
          display: flex;
          align-items: center;
          flex-grow: 1;
          .el-slider {
            margin-left: 12px;
            width: 64%;
          }
          .unit {
            user-select: none;
            font-size: 14px;
            color: #999;
            margin-left: 5px;
          }
          .dialog-form_select {
            .el-select-dropdown__item {
              height: auto;
              line-height: normal;
              & + .el-select-dropdown__item {
                margin-top: 6px;
              }
            }
            .option-item {
              display: flex;
              flex-direction: column;
              .title {
                font-size: 15px;
                font-weight: bold;
              }
              .desc {
                margin-top: 3px;
                font-size: 12px;
                color: var(--placeholder-text);
              }
            }
          }
          .dialog-after-option {
            display: flex;
            flex-direction: column;
            box-sizing: border-box;
            padding: 6px 12px;
            border-radius: 5px;
            background-color: #f5f7fa;
            .label {
              font-size: 15px;
              font-weight: bold;
            }
            .desc {
              margin-top: 3px;
              font-size: 12px;
              color: var(--placeholder-text);
            }
          }
          .proof-list {
            display: flex;
            flex-wrap: wrap;
            gap: 8px 12px;
            img {
              width: 120px;
              height: 120px;
              border-radius: 8px;
            }
          }
          .reason {
            display: flex;
            flex-direction: column;
            flex-grow: 1;
            p {
              text-indent: 2em;
              font-size: 14px;
              color: var(--placeholder-text);
              & + p {
                margin-top: 8px;
              }
            }
            &.disabled {
              user-select: none;
            }
          }
          .money-line {
            width: 96%;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            user-select: none;
            .unit {
              font-size: 16px;
            }
            .money {
              margin-left: 6px;
              font-size: 14px;
              color: var(--delete);
            }
          }
          .time-line {
            width: 96%;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            user-select: none;
            span {
              font-size: 14px;
              color: var(--placeholder-text);
            }
          }
          .status-line {
            width: 96%;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            user-select: none;
            .status {
              font-size: 12px;
              color: var(--placeholder-text);
            }
          }
          &.df-card {
            box-sizing: border-box;
            padding: 6px 12px;
            border-radius: 5px;
            background-color: #f5f7fa;
          }
        }
        & + .dialog-form-item {
          margin-top: 18px;
        }
      }
    }
    .dialog-result-box {
      margin-top: 12px;
      width: 100%;
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      padding: 0 12px;
      .dialog-result-item-box {
        width: 100%;
        display: flex;
        align-items: center;
        flex-direction: row-reverse;
        .dialog-result-item {
          user-select: none;
          display: flex;
          align-items: center;
          .label {
            font-size: 14px;
            color: #666;
          }
          .text {
            margin-left: 5px;
            font-size: 14px;
            color: #999;
          }
        }
      }
    }
  }
}
</style>

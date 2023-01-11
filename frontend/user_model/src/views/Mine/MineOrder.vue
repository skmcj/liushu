<template>
  <div class="mine mine-order">
    <div class="title-box">
      <span class="icon ic-order-1"></span>
      <span class="text">我的订单</span>
      <div class="hr"></div>
    </div>
    <!-- 导航 -->
    <div class="nav-box">
      <div
        class="nav"
        v-for="(item, i) in navList"
        :key="'nav-item-' + i"
        :class="{ 'is-checked': navCheck === item.value }"
        @click.stop="handleNav(item)">
        <span class="text">{{ item.label }}</span>
      </div>
    </div>
    <!-- 订单列表 -->
    <div class="order-list" v-if="!$isEmpty(orderList)">
      <!-- 订单项 -->
      <div
        class="order-item"
        v-for="(order, oi) in orderList"
        :key="'order-item-' + oi"
        @click.stop="handleDetail(order)">
        <!-- 订单主要内容 -->
        <div class="order-content">
          <!-- 订单基本信息 -->
          <div class="order-mess-box">
            <div class="order-mess-item">
              <span class="label">订单号：</span>
              <span class="text">{{ order.number }}</span>
            </div>
            <div class="order-mess-item">
              <span class="label">创建时间：</span>
              <span class="text">{{ order.createTime }}</span>
            </div>
            <div class="order-mess-item">
              <span class="label">商家名称：</span>
              <span class="text link">{{ order.storeName }}</span>
            </div>
          </div>
          <!-- 订单商品列表 -->
          <div class="orderr-goods-box">
            <div class="order-goods-list">
              <div class="order-goods-item" v-for="(goods, gi) in order.orderItems" :key="'order-goods-item-' + gi">
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
          <!-- 订单提示 -->
          <div class="order-tip-box" v-if="order.orderTip">
            <i class="ic-info" v-if="order.status !== 3"></i>
            <i class="ic-time-tip" v-if="order.status === 3"></i>
            <span>{{ order.orderTip }}</span>
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
          <!-- 订单操作 -->
          <div class="order-tools">
            <div class="order-btn pay" @click.stop="handleClickPay(order, oi)">去付款</div>
            <div class="order-btn cancel" @click.stop="handleCancel(order, oi)">取消订单</div>
            <div class="order-btn contact" @click.stop="handleContact(order)">联系商家</div>
          </div>
        </div>
        <!-- 待配送 -->
        <div v-if="computeOrderStatus(order) === 'send'" class="order-other">
          <!-- 待配送 -->
          <div class="order-type">
            <i class="ic-delivery"></i>
            <span>待配送</span>
          </div>
          <!-- 订单操作 -->
          <div class="order-tools">
            <div class="order-btn urge" v-if="order.status === 0" @click.stop="handleUrge(order)">我要催单</div>
            <div class="order-btn" v-if="order.status === 1" @click.stop="handleConfirm(order, oi)">确认收货</div>
            <div class="order-btn asales" @click.stop="handleAfterSales(order, oi)">申请售后</div>
            <div class="order-btn contact" @click.stop="handleContact(order)">联系商家</div>
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
          <!-- 订单操作 -->
          <div class="order-tools">
            <div class="order-btn renew" v-if="order.status === 2" @click.stop="handleRenew(order, oi)">我要续借</div>
            <div
              class="order-btn repay"
              :class="{
                disabled: order.status === 3
              }"
              @click.stop="handleRepay(order, oi)">
              {{ order.status === 3 ? '已预约' : '预约归还' }}
            </div>
            <div class="order-btn asales" @click.stop="handleAfterSales(order, oi)">申请售后</div>
            <div class="order-btn contact" @click.stop="handleContact(order)">联系商家</div>
          </div>
        </div>
        <!-- 待评价 -->
        <div v-if="computeOrderStatus(order) === 'comment'" class="order-other">
          <!-- 待评价 -->
          <div class="order-type" v-if="order.status === 4">
            <i class="ic-evaluate"></i>
            <span>待评价</span>
          </div>
          <!-- 订单操作 -->
          <div class="order-tools">
            <div class="order-btn comment" v-if="!order.isComment" @click.stop="handleComment(order, oi)">去评价</div>
            <div class="order-btn comment" v-if="order.isComment" @click.stop="handleComment(order, oi)">查看评价</div>
            <div class="order-btn complete" @click.stop="handleComplete(order, oi)">确认完成</div>
            <div class="order-btn asales" @click.stop="handleAfterSales(order, oi)">申请售后</div>
            <div class="order-btn contact" @click.stop="handleContact(order)">联系商家</div>
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
          <!-- 订单操作 -->
          <div class="order-tools">
            <div class="order-btn comment" v-if="!order.isComment" @click.stop="handleComment(order, oi)">去评价</div>
            <div class="order-btn comment" v-if="order.isComment" @click.stop="handleComment(order, oi)">查看评价</div>
            <div class="order-btn asales" v-if="order.status !== 7" @click.stop="handleAfterSales(order, oi)">
              申请售后
            </div>
            <div class="order-btn contact" @click.stop="handleContact(order)">联系商家</div>
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
          <!-- 订单操作 -->
          <div class="order-tools">
            <div
              class="order-btn urge"
              v-if="order.afterSales && order.afterSales.status === 1"
              @click.stop="handleRepayOfAS(order, oi)">
              预约退货
            </div>
            <div
              v-if="order.amStatus === 3 && order.afterSales.orderStatus !== 5"
              title="返回申请售后前的订单状态"
              class="order-btn asales"
              @click.stop="handleReturnPreAS(order, oi)">
              还原订单
            </div>
            <div class="order-btn asales" @click.stop="handleOpenAS(order)">售后单据</div>
            <div class="order-btn contact" @click.stop="handleContact(order)">联系商家</div>
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
    <!-- 无订单 -->
    <SvgPage class="no-order" v-if="$isEmpty(orderList)" name="no-order" text="暂无相关订单" :img-width="480" />
    <div class="page-tool">
      <el-pagination
        layout="total, prev, pager, next"
        :total="total"
        :page-count="total / pageSize"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handleCurrentChange"
        hide-on-single-page>
      </el-pagination>
    </div>
    <!-- 付款dialog -->
    <el-dialog
      title="收银台"
      :visible.sync="payDialogVisible"
      class="pay-dialog"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      destroy-on-close
      @close="handlePayPanelClose">
      <div class="order-to-pay">
        <PaymentPanel
          :order-list="payDialogOrder"
          :amount="allAmount"
          :money="userInfo.money"
          :pay-type="payType"
          @overtime="handleOrderOvertime"
          @payMethod="handlePayMethod"
          @payComplete="handlePayComplete" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="isOvertime" @click="handlePay" round>确认 支付</el-button>
      </span>
    </el-dialog>
    <!-- 续借归还付款dialog -->
    <el-dialog
      title="收银台"
      :visible.sync="payOfRepayVisible"
      class="pay-dialog"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      destroy-on-close
      @close="handlePayOfRepayClose">
      <div class="order-to-pay">
        <PaymentPanel
          :order-list="payOfRepayOrder"
          :amount="allAmount"
          :money="userInfo.money"
          :pay-type="payType"
          :timing="false"
          @payMethod="handlePayMethod"
          @payComplete="handlePayComplete" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handlePayOfRepayComplete" round>确认 支付</el-button>
      </span>
    </el-dialog>
    <!-- 评价dialog -->
    <el-dialog
      class="comment-dialog"
      :title="'订单号：' + commentDgTitle"
      :visible.sync="commentDgVisable"
      :modal-append-to-body="false"
      :close-on-click-modal="false">
      <div class="goods-comment-list">
        <div class="goods-comment-item" v-for="(comment, ci) in commentDgList" :key="'comment-item-' + ci">
          <div class="goods-comment-top">
            <!-- 顶部商品信息 -->
            <div class="goods-mess">
              <div class="goods-cover">
                <img :src="comment.bookCover" alt="goods-cover" />
              </div>
              <div class="goods-name">{{ comment.bookName }}</div>
            </div>
            <div class="goods-rate-box">
              <div class="goods-rate-title">评分：</div>
              <div class="goods-rate">
                <el-rate
                  v-model="comment.score"
                  :max="5"
                  allow-half
                  :icon-classes="['ic-score', 'ic-score', 'ic-score']"
                  :colors="['#83ccd2', '#83ccd2', '#83ccd2']"
                  void-icon-class="ic-score"
                  disabled-void-icon-class="ic-score"
                  show-score
                  text-color="#83ccd2"
                  disabled-void-color="#999999"
                  score-template="{value}">
                </el-rate>
              </div>
            </div>
          </div>
          <!-- 评论内容 -->
          <div class="goods-comment-content">
            <el-input type="textarea" :rows="5" placeholder="请输入评价内容" v-model="comment.content"></el-input>
          </div>
          <!-- 是否匿名 -->
          <div class="goods-comment-anonymity">
            <div class="text">{{ comment.isAnonymous === 0 ? '未匿名' : '已匿名' }}</div>
            <div class="switch">
              <el-switch
                v-model="comment.isAnonymous"
                active-color="#83ccd2"
                :active-value="1"
                :inactive-value="0"
                @change="commentSwitchChange(comment)">
              </el-switch>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitComment">提交 评价</el-button>
      </span>
    </el-dialog>
    <!-- 续借dialog -->
    <el-dialog
      class="renew-dialog"
      :title="'续借图书'"
      :visible.sync="renewVisable"
      :modal-append-to-body="false"
      :close-on-click-modal="false">
      <div class="dialog-main-box">
        <div class="order-content">
          <!-- 订单基本信息 -->
          <div class="order-mess-box">
            <div class="order-mess-item">
              <span class="label">订单号：</span>
              <span class="text">{{ dialogOrder.number }}</span>
            </div>
            <div class="order-mess-item">
              <span class="label">创建时间：</span>
              <span class="text">{{ dialogOrder.createTime }}</span>
            </div>
            <div class="order-mess-item">
              <span class="label">商家名称：</span>
              <span class="text">{{ dialogOrder.storeName }}</span>
            </div>
          </div>
          <!-- 订单商品列表 -->
          <div class="orderr-goods-box">
            <!-- <div class="order-goods-list" @mousewheel.stop="handleXScroll"> -->
            <div class="order-goods-list">
              <div
                class="order-goods-item"
                v-for="(goods, gi) in dialogOrder.orderItems"
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
        <div class="other-mess">
          <div class="other-mess-item">
            <span class="label">借阅时长：</span>
            <span class="text">{{ dialogOrder.borrowTime + ' 天' }}</span>
          </div>
          <div class="other-mess-item">
            <span class="label">剩余时长：</span>
            <span class="text">{{ computeLeaveDay(dialogOrder.deliveryTime, dialogOrder.borrowTime) }}</span>
          </div>
        </div>
        <!-- 提示 -->
        <div class="tip-box">
          <i class="ic-info"></i>
          <span>{{ getDialogTip('renew') }}</span>
        </div>
        <!-- 表单 -->
        <div class="dialog-form">
          <div class="dialog-form-item">
            <div class="dialog-form__label">续借时长：</div>
            <div class="dialog-form__content">
              <el-slider v-model="renewValue" :min="0" :max="maxRenewValue" show-input input-size="mini"></el-slider>
              <span class="unit">天</span>
            </div>
          </div>
        </div>
        <!-- 结果 -->
        <div class="dialog-result-box">
          <div class="dialog-result-item-box">
            <div class="dialog-result-item" style="margin-right: 12px">
              <span class="label">借阅费：</span>
              <span class="text">{{ renewCost + ' 元' }}</span>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="gotoPayOfRenew">去付款</el-button>
      </span>
    </el-dialog>
    <!-- 归还dialog -->
    <el-dialog
      class="repay-dialog"
      :title="'预约归还'"
      :visible.sync="repayDgVisable"
      :modal-append-to-body="false"
      :close-on-click-modal="false">
      <div class="dialog-main-box">
        <div class="order-content">
          <!-- 订单基本信息 -->
          <div class="order-mess-box">
            <div class="order-mess-item">
              <span class="label">订单号：</span>
              <span class="text">{{ dialogOrder.number }}</span>
            </div>
            <div class="order-mess-item">
              <span class="label">创建时间：</span>
              <span class="text">{{ dialogOrder.createTime }}</span>
            </div>
            <div class="order-mess-item">
              <span class="label">商家名称：</span>
              <span class="text">{{ dialogOrder.storeName }}</span>
            </div>
          </div>
          <!-- 订单商品列表 -->
          <div class="orderr-goods-box">
            <div class="order-goods-list">
              <div
                class="order-goods-item"
                v-for="(goods, gi) in dialogOrder.orderItems"
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
        <div class="other-mess">
          <div class="other-mess-item">
            <span class="label">借阅时长：</span>
            <span class="text">{{ dialogOrder.borrowTime + ' 天' }}</span>
          </div>
          <div class="other-mess-item">
            <span class="label">剩余时长：</span>
            <span class="text">{{ computeLeaveDay(dialogOrder.deliveryTime, dialogOrder.borrowTime) }}</span>
          </div>
        </div>
        <!-- 提示 -->
        <div class="tip-box">
          <i class="ic-info"></i>
          <span>{{ getDialogTip('repay') }}</span>
        </div>
        <!-- 表单 -->
        <div class="dialog-form">
          <div class="dialog-form-item">
            <div class="dialog-form__label">预约还书时间：</div>
            <div class="dialog-form__content">
              <el-date-picker
                v-model="repayDate"
                type="datetime"
                placeholder="选择日期时间"
                align="right"
                :picker-options="pickerOptions"
                value-format="yyyy-MM-dd HH:mm"
                format="yyyy-MM-dd HH:mm">
              </el-date-picker>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRepayMess">预约 归还</el-button>
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
        <div class="dialog-form">
          <!-- 售后类型 -->
          <div class="dialog-form-item">
            <div class="dialog-form__label">售后类型：</div>
            <div class="dialog-form__content">
              <!-- 售后类型选择器 -->
              <el-select
                popper-class="dialog-form_select"
                :popper-append-to-body="false"
                v-model="afterDgForm.type"
                placeholder="请选择售后类型"
                :disabled="!$isEmpty(afterDgForm.id)">
                <el-option
                  v-for="item in afterDgOptions"
                  :key="'as-dg-op-' + item.value"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.value === 1 && afterDgOrder.status === 5">
                  <div class="option-item">
                    <span class="title">{{ item.label }}</span>
                    <span class="desc">{{ item.desc }}</span>
                  </div>
                </el-option>
              </el-select>
            </div>
          </div>
          <!-- 售后原因 -->
          <div class="dialog-form-item">
            <div class="dialog-form__label">售后原因：</div>
            <div class="dialog-form__content">
              <!-- 如果售后凭证已生成 -->
              <el-input
                type="textarea"
                :rows="5"
                placeholder="请简要描述您申请售后的原因"
                v-model="afterDgForm.reason"
                :disabled="!$isEmpty(afterDgForm.id)">
              </el-input>
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
          <div class="dialog-form-item" v-if="afterDgForm.failReason">
            <div class="dialog-form__label">售后失败原因：</div>
            <div class="dialog-form__content">
              <div class="reason disabled">
                <p>{{ afterDgForm.failReason }}</p>
              </div>
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
            <div class="dialog-form__content">
              <el-date-picker
                v-model="afterDgForm.returnTime"
                type="datetime"
                placeholder="选择日期时间"
                align="right"
                :picker-options="pickerAfterOptions"
                value-format="yyyy-MM-dd HH:mm"
                format="yyyy-MM-dd HH:mm"
                :disabled="afterDgForm.status !== 1">
              </el-date-picker>
            </div>
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
        <!-- 提示：申请售后前，请先与商家沟通清楚 -->
        <div v-if="!afterBtnDisabled" class="tip-box">
          <i class="ic-info"></i>
          <span>申请售后前，请先与商家沟通清楚</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="!afterBtnDisabled" type="primary" @click="submitAfterSales">
          {{ afterDgForm.isReturn ? '预约 退货' : '提交 申请' }}
        </el-button>
      </span>
    </el-dialog>
    <!-- 详情dialog -->
    <el-dialog class="detail-dialog" title="订单详情" :visible.sync="detailDgVisable" :modal-append-to-body="false">
      <div class="dialog-main-box">
        <div class="order-goods-box">
          <div class="order-goods-title-box">{{ dialogOrder.storeName }}</div>
          <div class="order-goods-list">
            <div class="order-goods-item" v-for="(goods, gi) in dialogOrder.orderItems" :key="'order-goods-item-' + gi">
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
        <div class="order-detail-box">
          <!-- 基本信息 -->
          <div class="order-detail-item-box">
            <div class="order-detail-item">
              <span class="label">借阅时长</span>
              <span class="text">{{ dialogOrder.borrowTime + ' 天' }}</span>
            </div>
            <div class="order-detail-item">
              <span class="label">期望时间</span>
              <span class="text">{{ dialogOrder.expectedTime }}</span>
            </div>
            <div class="order-detail-item">
              <span class="label">联系信息</span>
              <span class="text">{{ dialogOrder.consignee + ' ' + dialogOrder.phone }}</span>
            </div>
            <div class="order-detail-item">
              <span class="label">配送地址</span>
              <span class="text">{{ dialogOrder.address }}</span>
            </div>
            <div class="order-detail-item">
              <span class="label">配送服务</span>
              <span class="text">{{ dialogOrder.shippingMethod ? '高质量配送团队提供' : '由 商家 提供' }}</span>
            </div>
          </div>
          <!-- 订单信息 -->
          <div class="order-detail-item-box">
            <div class="order-detail-item">
              <span class="label">订单号</span>
              <span class="text">{{ dialogOrder.number }}</span>
            </div>
            <div class="order-detail-item">
              <span class="label">支付时间</span>
              <span class="text">{{ dialogOrder.payTime }}</span>
            </div>
            <div class="order-detail-item">
              <span class="label">支付方式</span>
              <span class="text">{{ dialogOrder.payMethod === 0 ? '线上支付' : '其它方式' }}</span>
            </div>
          </div>
          <!-- 费用信息 -->
          <div class="order-detail-item-box">
            <div class="order-detail-item between">
              <span class="label">押金</span>
              <span class="text">{{ '￥ ' + computeOrderAmount(dialogOrder.orderItems, (type = 'deposit')) }}</span>
            </div>
            <div class="order-detail-item between">
              <span class="label">借阅费</span>
              <span class="text">{{ '￥ ' + computeOrderAmount(dialogOrder.orderItems, (type = 'borrow')) }}</span>
            </div>
            <div class="order-detail-item between">
              <span class="label">包装费</span>
              <span class="text">{{ '￥ ' + computeOrderAmount(dialogOrder.orderItems, (type = 'packing')) }}</span>
            </div>
            <div class="order-detail-item between">
              <span class="label">配送费</span>
              <span class="text">{{ '￥ ' + dialogOrder.deliveryFee }}</span>
            </div>
            <div class="order-detail-item between" v-if="dialogOrder.overdueCost">
              <span class="label">{{ `逾期费(逾期${dialogOrder.overdueTime}天)` }}</span>
              <span class="text">{{ '￥ ' + dialogOrder.overdueCost }}</span>
            </div>
            <div class="order-detail-item between block">
              <span class="label">优惠金额</span>
              <span class="text">{{ '￥ ' + dialogOrder.discountAmount }}</span>
            </div>
            <div class="all-cost">
              <span class="label">共计</span>
              <span class="text">{{ '￥ ' + dialogOrder.amount }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Vue from 'vue';
import { mapState } from 'vuex';
import SvgPage from '@/components/Common/SvgPage';
import PaymentPanel from '@/components/Common/Pay/PaymentPanel';
import commonUtil from '@/utils/common';
import {
  getAllOrderOgPageApi,
  getOrderByStatusOfPageApi,
  getOrderByIdApi,
  payOrderByLSApi,
  cancelOrderApi,
  confirmReceiptApi,
  confirmCompleteApi,
  renewOrderApi,
  renewOrderToPayOfLSApi,
  repayOrderApi,
  repayOverdueOrderApi,
  repayOverdueOrderToPayOfLSApi,
  applyOrderToASApi,
  revertOrderStatusOfASApi,
  repayOrderOfASApi
} from '@/api/orderApi';

export default {
  components: {
    PaymentPanel,
    SvgPage
  },
  data() {
    return {
      userInfo: {},
      orderList: [],
      navCheck: 'all',
      navList: [
        { label: '全部订单', value: 'all', ct: {} },
        { label: '待付款', value: 'pay', ct: { payStatus: 0 } },
        { label: '待配送', value: 'send', ct: { status: [0, 1] } },
        { label: '待归还', value: 'back', ct: { status: [2, 3, 6] } },
        { label: '待评价', value: 'comment', ct: { status: 4 } },
        { label: '已完成', value: 'complete', ct: { status: 5 } },
        { label: '退款/售后', value: 'after', ct: { status: 8 } }
      ],
      currentPage: 1,
      pageSize: 5,
      total: 0,
      commentDgVisable: false,
      commentDgTitle: '',
      commentDgList: {},
      renewVisable: false,
      repayDgVisable: false,
      dialogOrder: {},
      dialogOrderIndex: 0,
      // 可续借时长
      renewValue: 0,
      maxRenewValue: 30,
      renewCost: 0,
      payOfRepayVisible: false,
      payOfRepayOrder: {},
      // 还书时间
      repayDate: '',
      pickerOptions: {
        // 时间的快捷选项
        shortcuts: [
          {
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date());
            }
          },
          {
            text: '明天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          },
          {
            text: '后天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          },
          {
            text: '一周后',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }
        ],
        // 禁用日期
        disabledDate(date) {
          return this.computeDisabledRepayDate(date);
        }
      },
      // 详情
      detailDgVisable: false,
      // 付款
      payDialogVisible: false,
      payDialogIndex: 0,
      payDialogOrder: [],
      allAmount: 0,
      payMethod: 'balance',
      payType: 'borrow',
      payPass: '',
      isOvertime: false,
      // 售后申请
      afterDgVisable: false,
      afterDgOrder: {},
      afterDgIndex: 0,
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
      pickerAfterOptions: {
        // 时间的快捷选项
        shortcuts: [
          {
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date());
            }
          },
          {
            text: '明天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          },
          {
            text: '后天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          },
          {
            text: '一周后',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }
        ],
        // 禁用日期
        disabledDate(date) {
          return date < Date.now();
        }
      },
      afterDgForm: {
        type: '',
        reason: ''
      },
      afterDgOptions: [
        { value: 0, label: '我要退款(无需退货)', desc: '没收到货，或与商家协商同意不用退货只退款' },
        { value: 1, label: '我要退货退款', desc: '已收到货，需归还已收到的货物' }
      ],
      afterBtnDisabled: false,
      // 计时
      // 设置计时，单位：s => 15分钟
      counter: 900
    };
  },
  created() {
    this.initOrderList();
    this.userInfo = this.$store.state.userInfo;
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
          this.handleOrderOfPreMounted(this.orderList);
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
          this.handleOrderOfPreMounted(this.orderList);
        } else {
          this.$showMsg('订单数据获取失败，请稍后再试', { type: 'error' });
        }
      });
    },
    getDialogTip(text) {
      let tip = '';
      // 当前订单 dialogOrder
      // 获取订单提示
      if (text === 'renew') {
        tip = '借阅时间小于等于 30 天的，免借阅费；大于 30 天的，0.07 元/天';
      }
      if (text === 'repay') {
        tip = '请在借阅时间结束前预约上门收书，商家会在预约时间的后两个小时内上门收书，请确保有足够的时间等待';
      }
      return tip;
    },
    // 页码改变时
    handleCurrentChange(val) {
      this.currentPage = val;
      // 获取图书
      this.initOrderList();
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
    /**
     * 计算订单项费用和
     * @param {Array} orderItems
     * @param {String} type
     * @returns {Number}
     */
    computeOrderAmount(orderItems, type = 'borrow') {
      if (!orderItems) return 0;
      let amount = 0;
      if (type === 'borrow') {
        orderItems.forEach(item => {
          amount += item.borrowCost;
        });
      } else if (type === 'packing') {
        orderItems.forEach(item => {
          amount += item.packingCost;
        });
      } else if (type === 'deposit') {
        orderItems.forEach(item => {
          amount += item.deposit;
        });
      }
      return parseFloat(parseFloat(amount).toFixed(2));
    },
    /**
     * 横向滚动
     */
    handleXScroll(event) {
      // 禁止默认滚动
      event.preventDefault();
      // 获取当前dom
      const box = event.path[4];
      // 获取滚动方向
      const detail = event.wheelDelta || event.detail;
      // 定义滚动方向，其实也可以在赋值的时候写
      const moveForwardStep = 1;
      const moveBackStep = -1;
      // 定义滚动距离
      let step = 0;
      // 滚动幅度
      let speed = 42;
      // 判断滚动方向,这里的100可以改，代表滚动幅度，也就是说滚动幅度是自定义的
      if (detail < 0) {
        step = moveForwardStep * speed;
      } else {
        step = moveBackStep * speed;
      }
      // 对需要滚动的元素进行滚动操作
      box.scrollLeft += step;
    },
    handleNav(item) {
      this.navCheck = item.value;
      // 获取指定订单
      this.initOrderList();
    },
    handleDetail(item) {
      this.dialogOrder = item;
      this.detailDgVisable = true;
    },
    /**
     * 去付款
     */
    handleClickPay(order, index) {
      this.payDialogOrder = [order];
      this.payDialogIndex = index;
      this.allAmount = order.amount;
      this.payDialogVisible = true;
    },
    /**
     * 账户支付密码
     */
    handlePayComplete(val) {
      this.payPass = val;
      // console.log('pass pay ==>', val);
    },
    /**
     * 订单超时
     */
    handleOrderOvertime(val) {
      // console.log('订单超时 ==> ', val);
      this.isOvertime = true;
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
        if (this.userInfo.money > this.allAmount) {
          // 进行支付
          payOrderByLSApi(this.orderList[this.payDialogIndex].id, this.$sha256(this.payPass))
            .then(res => {
              if (res.data.flag) {
                return getOrderByIdApi(this.orderList[this.payDialogIndex].id);
              }
            })
            .then(res => {
              if (res.data.flag) {
                this.orderList[this.payDialogIndex] = res.data.data;
                this.$forceUpdate();
                this.$showMsg('支付成功', { type: 'success' });
              }
            })
            .catch(err => {
              this.$showMsg('支付失败', { type: 'error' });
            })
            .finally(() => {
              this.payDialogVisible = false;
            });
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
     * 支付方式
     */
    handlePayMethod(val) {
      this.payMethod = val;
    },
    /**
     * 支付面板关闭
     */
    handlePayPanelClose() {
      // 暂时跳转到首页
      // this.$router.replace('/');
      this.payDialogVisible = false;
    },
    /**
     * 取消订单
     */
    handleCancel(order, index) {
      cancelOrderApi(order.id)
        .then(res => {
          if (res.data.flag) {
            // 取消成功，更新数据
            this.$showMsg('取消成功', { type: 'success' });
            return getOrderByIdApi(order.id);
            // item = {}
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.orderList[index] = res.data.data;
            this.$forceUpdate();
          }
        })
        .catch(err => {
          this.$showMsg('取消失败，请稍后重试', { type: 'error' });
        });
    },
    /**
     * 联系商家
     */
    handleContact(order) {
      if (!order.storeId) return;
      this.$bus.$emit('openChatWindow', 'owner');
      let conversationID = `C2C${order.storeId}`;
      if (conversationID !== this.currentConversation.conversationID) {
        this.$store.dispatch('checkoutConversation', conversationID);
      }
    },
    /**
     * 催单
     */
    handleUrge(item) {
      // 暂时不实现实际功能
      // 主要可以通过邮件通知商家或即时通信自定义消息通知
      // 以上实现方式适用项目，并且能够实现，但单纯不想实现
      this.$showMsg('已催促商家尽快配送，请耐心等候');
    },
    /**
     * 申请售后
     */
    handleAfterSales(order, index) {
      this.afterBtnDisabled = false;
      this.afterDgForm = {
        type: '',
        reason: ''
      };
      this.afterDgOrder = order;
      this.afterDgIndex = index;
      this.afterDgVisable = true;
    },
    /**
     * 提交售后申请
     */
    submitAfterSales() {
      if (this.afterDgOrder.status !== 8) {
        // 申请售后
        let afterSales = {
          orderId: this.afterDgOrder.id,
          type: this.afterDgForm.type,
          reason: this.afterDgForm.reason
        };
        // console.log('售后 ==> ', afterSales);
        applyOrderToASApi(afterSales)
          .then(res => {
            if (res.data.flag) {
              return getOrderByIdApi(this.afterDgOrder.id);
            } else {
              this.$showMsg(res.data.msg, {
                type: 'error',
                duration: 1200
              });
            }
          })
          .then(res => {
            if (res.data.flag) {
              // console.log('order ==> ', res.data.data);
              this.orderList[this.afterDgIndex] = res.data.data;
              this.$showMsg('售后申请已提交，请耐心等候结果', { type: 'success' });
              this.$forceUpdate();
            }
          })
          .catch(err => {
            this.$showMsg('网络繁忙，请稍后重试', { type: 'error' });
          })
          .finally(() => {
            this.afterDgVisable = false;
          });
      } else {
        // 预约退货
        repayOrderOfASApi(this.afterDgForm.id, this.afterDgForm.returnTime + ':00')
          .then(res => {
            if (res.data.flag) {
              return getOrderByIdApi(this.afterDgOrder.id);
            } else {
              this.$showMsg(res.data.msg, {
                type: 'error',
                duration: 1200
              });
            }
          })
          .then(res => {
            if (res.data.flag) {
              this.computeOrderOfRefundTip(res.data.data);
              this.orderList[this.afterDgIndex] = res.data.data;
              this.$showMsg('预约成功，请耐心等待', { type: 'success' });
              this.$forceUpdate();
            }
          })
          .catch(err => {
            this.$showMsg('网络繁忙，请稍后重试', { type: 'error' });
          })
          .finally(() => {
            this.afterDgVisable = false;
          });
      }
    },
    /**
     * 查看售后单据
     */
    handleOpenAS(order) {
      this.afterBtnDisabled = true;
      this.afterDgOrder = order;
      this.afterDgForm = order.afterSales;
      this.afterDgVisable = true;
    },
    /**
     * 返回售前状态
     */
    handleReturnPreAS(order, index) {
      revertOrderStatusOfASApi(order.id)
        .then(res => {
          if (res.data.flag) {
            return getOrderByIdApi(order.id);
          } else {
            this.$showMsg(res.data.msg, { type: 'error' });
          }
        })
        .then(res => {
          if (res.data.flag) {
            if (res.data.data.status === 3) {
              this.computeOrderOfRepayTip(res.data.data);
            }
            this.orderList[index] = res.data.data;
            this.$forceUpdate();
          }
        })
        .catch(err => {
          this.$showMsg('网络繁忙，请稍后重试', { type: 'error' });
        });
    },
    /**
     * 预约退货
     */
    handleRepayOfAS(order, index) {
      this.afterBtnDisabled = false;
      this.afterDgForm = order.afterSales;
      this.afterDgForm.returnTime = '';
      this.afterDgForm.isReturn = true;
      this.afterDgOrder = order;
      this.afterDgIndex = index;
      this.afterDgVisable = true;
    },
    /**
     * 确认收货
     */
    handleConfirm(order, index) {
      confirmReceiptApi(order.id)
        .then(res => {
          if (res.data.flag) {
            // 确认收货成功
            // 获取订单最新信息
            return getOrderByIdApi(order.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.orderList[index] = res.data.data;
            this.$forceUpdate();
            this.$showMsg('收货确认成功', { type: 'success' });
          }
        })
        .catch(err => {
          this.$showMsg('网络繁忙，请稍后重试', { type: 'warning' });
        });
    },
    /**
     * 确认完成
     */
    handleComplete(order, index) {
      confirmCompleteApi(order.id)
        .then(res => {
          if (res.data.flag) {
            return getOrderByIdApi(order.id);
          }
        })
        .then(res => {
          if (res.data.flag) {
            this.orderList[index] = res.data.data;
            this.$forceUpdate();
            this.$showMsg('确认完成成功', { type: 'success' });
          }
        })
        .catch(err => {
          this.$showMsg('确认完成失败', { type: 'error' });
        });
    },
    /**
     * 续借
     */
    handleRenew(order, index) {
      this.dialogOrder = order;
      this.dialogOrderIndex = index;
      this.maxRenewValue = order.renewDuration;
      this.renewVisable = true;
    },
    // 续借订单去付款
    gotoPayOfRenew() {
      // console.log('续借订单 ==> ', this.dialogOrder.id, this.renewValue);
      renewOrderApi(this.dialogOrder.id, this.renewValue)
        .then(res => {
          if (res.data.flag) {
            this.renewVisable = false;
            this.payOfRepayVisible = true;
            this.payOfRepayOrder = [res.data.data];
            this.allAmount = res.data.data.amount;
            // console.log('renew order ==>', res.data.data);
          }
        })
        .catch(err => {
          this.renewVisable = false;
          this.$showMsg('续借失败, 请稍后再试', { type: 'error' });
        });
    },
    // 续借订单关闭
    handlePayOfRepayClose() {
      // this.$showMsg('续借失败', { type: 'warning' });
    },
    // 续借订单支付完成
    handlePayOfRepayComplete() {
      if (this.payMethod === 'balance') {
        if (this.$isEmpty(this.payPass)) {
          this.$showMsg('请输入支付密码');
          return;
        }
        // 账户余额支付
        if (this.userInfo.money > this.allAmount) {
          // 进行支付
          if (this.dialogOrder.status === 6) {
            // 逾期中订单预约归还支付
            repayOverdueOrderToPayOfLSApi(this.dialogOrder.id, this.$sha256(this.payPass))
              .then(res => {
                if (res.data.flag) {
                  this.payOfRepayVisible = false;
                  this.$showMsg('预约成功', { type: 'success' });
                  return getOrderByIdApi(this.dialogOrder.id);
                }
              })
              .then(res => {
                if (res.data.flag) {
                  this.orderList[this.dialogOrderIndex] = res.data.data;
                  this.$forceUpdate();
                }
              });
          } else {
            renewOrderToPayOfLSApi(this.dialogOrder.id, this.$sha256(this.payPass))
              .then(res => {
                if (res.data.flag) {
                  this.payOfRepayVisible = false;
                  this.$showMsg('续借成功', { type: 'success' });
                  return getOrderByIdApi(this.dialogOrder.id);
                }
              })
              .then(res => {
                if (res.data.flag) {
                  this.orderList[this.dialogOrderIndex] = res.data.data;
                  this.$forceUpdate();
                }
              });
          }
        } else {
          // 余额不足
          this.$showMsg('账户余额不足，请前往充值', {
            type: 'warning',
            duration: 1200
          });
        }
      }
    },
    /**
     * 归还
     */
    handleRepay(order, index) {
      // 订单已预约，待上门
      if (order.status === 3) return;
      this.dialogOrder = order;
      this.dialogOrderIndex = index;
      this.repayDgVisable = true;
    },
    // 预约归还
    submitRepayMess() {
      // console.log('预约归还 ==> ', this.dialogOrder.id, this.repayDate + ':00');
      if (this.dialogOrder.status === 6) {
        // 逾期中订单预约归还
        repayOverdueOrderApi(this.dialogOrder.id, this.repayDate + ':00')
          .then(res => {
            this.payOfRepayVisible = true;
            this.payOfRepayOrder = [res.data.data];
            this.allAmount = res.data.data.amount;
          })
          .catch(err => {
            this.$showMsg('网络繁忙，请稍后重试', { type: 'warning' });
          })
          .finally(() => {
            this.repayDgVisable = false;
            this.repayDate = '';
          });
      } else {
        // 正常预约归还
        repayOrderApi(this.dialogOrder.id, this.repayDate + ':00')
          .then(res => {
            if (res.data.flag) {
              return getOrderByIdApi(this.dialogOrder.id);
            }
          })
          .then(res => {
            if (res.data.flag) {
              this.computeOrderOfRepayTip(res.data.data);
              this.orderList[this.dialogOrderIndex] = res.data.data;
              this.$forceUpdate();
              this.$showMsg('预约成功', { type: 'success' });
            }
          })
          .catch(err => {
            this.$showMsg('网络繁忙，请稍后重试', { type: 'warning' });
          })
          .finally(() => {
            this.repayDgVisable = false;
            this.repayDate = '';
          });
      }
    },
    /**
     * 计算禁用的归还日期
     */
    computeDisabledRepayDate(date) {
      if (this.$isEmpty(this.dialogOrder)) return date.getTime() < Date.now();
      let now = new Date();
      let dueDate = new Date(this.dialogOrder.deliveryTime);
      dueDate.setDate(dueDate.getDate() + this.dialogOrder.borrowTime);
      return date < now || date > dueDate;
    },
    /**
     * 评价
     */
    handleComment(item) {
      this.commentDgList = [];
      for (let i = 0; i < item.orderItems.length; i++) {
        let comment = {
          // 订单id
          orderId: item.id,
          // 订单详情id
          orderItemId: item.orderItems[i].id,
          // 用户id
          userId: item.userId,
          // 图书id
          bookId: item.orderItems[i].bookId,
          // 书店id
          storeId: item.storeId,
          // 图书名称
          bookName: item.orderItems[i].bookName,
          // 图书封面
          bookCover: item.orderItems[i].bookCover,
          // 用户昵称
          nickname: this.userInfo.nickname,
          // 评论内容
          content: '',
          // 评分
          score: 0,
          // 是否匿名，0-不匿；1-匿名
          isAnonymous: 0
        };
        this.commentDgList.push(comment);
      }
      this.commentDgTitle = item.number;
      this.commentDgVisable = true;
    },
    // 评价匿名处理
    commentSwitchChange(item) {
      item.nickname = item.isAnonymous === 0 ? this.userInfo.nickname : '';
    },
    /**
     * 提交评价
     */
    submitComment() {
      this.commentDgVisable = false;
      console.log('comment =>', this.commentDgList);
    },
    /**
     * 计算剩余时长
     */
    computeLeaveDay(startDate, time) {
      let now = new Date();
      let start = new Date(startDate);
      start.setDate(start.getDate() + time);
      let num = commonUtil.getDaysBetween(now, start);
      return num >= 0 ? num + ' 天' : '已逾期 ' + num * -1 + ' 天';
    },
    /**
     * 设置订单提示
     */
    handleOrderOfPreMounted(orderList) {
      for (const order of orderList) {
        // 对未支付订单进行计时
        if (order.tradeStatus === 0 && order.payStatus === 0) {
          this.computeOrderTimeCount(order);
          continue;
        }
        // 逾期中
        if (order.status === 6) {
          this.computeOrderOfDueTip(order);
          continue;
        }
        // 订单已预约
        if (order.status === 3) {
          this.computeOrderOfRepayTip(order);
          continue;
        }
        // 退货订单已预约
        if (order.status === 8 && order.afterSales.status === 2) {
          this.computeOrderOfRefundTip(order);
          continue;
        }
      }
    },
    /**
     * 计算退货预约上门订单的提示
     */
    computeOrderOfRefundTip(order) {
      if (!order.afterSales) return;
      let now = new Date();
      let returnTime = new Date(order.afterSales.returnTime);
      let dValue = commonUtil.getDaysBetween(now, returnTime);
      if (dValue > 0) {
        order.orderTip = `预约上门时间：${order.afterSales.returnTime} (预计${dValue}天后)`;
      } else if (dValue === 0) {
        order.orderTip = `预约上门时间：${order.afterSales.returnTime} (今天)`;
      } else {
        order.orderTip = `预约上门时间：${order.afterSales.returnTime} (已超过${dValue * -1}天)`;
      }
    },
    /**
     * 计算已预约上门订单的提示
     */
    computeOrderOfRepayTip(order) {
      let now = new Date();
      let returnTime = new Date(order.returnTime);
      let dValue = commonUtil.getDaysBetween(now, returnTime);
      if (dValue > 0) {
        order.orderTip = `预约上门时间：${order.returnTime} (预计${dValue}天后)`;
      } else if (dValue === 0) {
        order.orderTip = `预约上门时间：${order.returnTime} (今天)`;
      } else {
        order.orderTip = `预约上门时间：${order.returnTime} (已超过${dValue * -1}天)`;
      }
    },
    /**
     * 计算逾期中订单的提示
     */
    computeOrderOfDueTip(order) {
      let now = new Date();
      let deliveryTime = new Date(order.deliveryTime);
      deliveryTime.setDate(deliveryTime.getDate() + order.borrowTime);
      let dValue = commonUtil.getDaysBetween(deliveryTime, now);
      if (dValue > 0) {
        order.orderTip = `已逾期${dValue}天`;
      }
    },
    /**
     * 计时待付款订单剩余时间
     */
    computeOrderTimeCount(order) {
      // 订单待付款
      let now = new Date();
      let createTime = new Date(order.createTime);
      // 差值
      let dValue = parseInt((now - createTime) / 1000);
      if (dValue < this.counter) {
        // 显示倒计时 Vue.set() 对新增加的属性设置监听
        // 剩余时长
        Vue.set(order, 'counter', this.counter - dValue);
        Vue.set(order, 'orderTip', `订单已创建，请在 ${parseInt(order.counter / 60)}:${order.counter % 60} 内完成支付`);
        // order.counter = this.counter - dValue;
        // order.orderTip = `订单已创建，请在 ${parseInt(order.counter / 60)}:${order.counter % 60} 内完成支付`;
        Vue.set(
          order,
          'timer',
          setInterval(() => {
            // 替换文本，秒实时改变
            order.orderTip = `订单已创建，请在 ${parseInt(order.counter / 60)}:${order.counter % 60} 内完成支付`;
            order.counter--;
            if (order.counter < 0) {
              order.orderTip = '订单已超时，正在取消···';
              order.isOvertime = true;
              // 当计时小于零时，取消该计时器
              clearInterval(order.timer);
              setTimeout(() => {
                order.orderTip = undefined;
                this.initOrderList();
              }, 3000);
              this.resetTimer();
            }
          }, 1000)
        );
        // order.timer = setInterval(() => {
        //   // 替换文本，秒实时改变
        //   order.orderTip = `订单已创建，请在 ${parseInt(order.counter / 60)}:${order.counter % 60} 内完成支付`;
        //   order.counter--;
        //   if (order.counter < 0) {
        //     order.orderTip = undefined;
        //     order.isOvertime = true;
        //     // 当计时小于零时，取消该计时器
        //     clearInterval(order.timer);
        //     this.resetTimer();
        //   }
        // }, 1000);
      }
    },
    /**
     * 重置计时相关参数
     */
    resetTimer(order) {
      // order.timeTip = `订单已创建，请在 ${parseInt(order.counter / 60)}:${order.counter % 60} 内完成支付`;
      if (order.timer) {
        // 存在计时器对象，则清除
        clearInterval(order.timer);
        // 重置秒数，防止下次混乱
        order.counter = 900;
        // 计时器对象重置为空
        order.timer = null;
      }
    },
    /**
     * 安全保留两位小数
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
     * 监听续借费用变化
     */
    renewValue(val) {
      if (this.$isEmpty(this.dialogOrder)) return;
      if (this.$isEmpty(this.dialogOrder.orderItems)) return;
      let amount = 0;
      let borrowTime = this.dialogOrder.borrowTime;
      for (let item of this.dialogOrder.orderItems) {
        // 计算配送费
        if (borrowTime >= item.freeDay) {
          amount += val * item.borrowFee;
        } else {
          let dValue = borrowTime + val;
          if (dValue > item.freeDay) {
            amount += (dValue - item.freeDay) * item.borrowFee;
          }
        }
      }
      this.renewCost = this.accToFixed(amount);
    },
    /**
     * 监听续借，归还操作弹窗数据
     */
    dialogOrder(val) {
      const _this = this;
      if (val.status === 6) {
        let dueDate = new Date(val.deliveryTime);
        dueDate.setDate(dueDate.getDate() + val.borrowTime + _this.$overduePeriod);
        let now = new Date();
        _this.pickerOptions = {
          // 时间的快捷选项
          shortcuts: [
            {
              text: '今天',
              onClick(picker) {
                picker.$emit('pick', new Date());
              }
            },
            {
              text: '明天',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() + 3600 * 1000 * 24);
                picker.$emit('pick', date);
              }
            },
            {
              text: '后天',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() + 3600 * 1000 * 24);
                picker.$emit('pick', date);
              }
            },
            {
              text: '一周后',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
                picker.$emit('pick', date);
              }
            }
          ],
          // 禁用日期
          disabledDate(date) {
            return date < now || date > dueDate;
          }
        };
      } else {
        _this.pickerOptions = {
          // 时间的快捷选项
          shortcuts: [
            {
              text: '今天',
              onClick(picker) {
                picker.$emit('pick', new Date());
              }
            },
            {
              text: '明天',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() + 3600 * 1000 * 24);
                picker.$emit('pick', date);
              }
            },
            {
              text: '后天',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() + 3600 * 1000 * 24);
                picker.$emit('pick', date);
              }
            },
            {
              text: '一周后',
              onClick(picker) {
                const date = new Date();
                date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
                picker.$emit('pick', date);
              }
            }
          ],
          // 禁用日期
          disabledDate(date) {
            return _this.computeDisabledRepayDate(date);
          }
        };
      }
    }
  }
};
</script>

<style lang="less" scoped>
.nav-box {
  width: 100%;
  height: 32px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  padding: 0 24px;
  margin-top: 24px;
  .nav {
    position: relative;
    cursor: pointer;
    user-select: none;
    height: 32px;
    line-height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    font-size: 14px;
    color: var(--primary-text);
    padding: 0 3px;
    transition: all 0.5s ease-in-out;
    & + .nav {
      margin-left: 24px;
    }
    &::after {
      transition: all 0.5s ease-in-out;
      content: '';
      display: block;
      width: 0%;
      height: 3px;
      border-radius: 1.5px;
      position: absolute;
      bottom: 0;
      background-color: var(--primary);
    }
    &.is-checked {
      color: var(--primary);
      &::after {
        width: 100%;
      }
    }
  }
}
.order-content {
  max-width: 68%;
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
.order-list {
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 100%;
  margin-top: 24px;
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
      .order-tools {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        box-sizing: border-box;
        padding: 0 12px;
        .order-btn {
          cursor: pointer;
          user-select: none;
          width: 96px;
          height: 27px;
          border-radius: 5px;
          font-size: 12px;
          line-height: 27px;
          text-align: center;
          background-color: var(--primary-btn);
          color: #ffffff;
          transition: all 0.5s ease-in-out;
          &:hover {
            background-color: var(--primary-btn-h);
          }
          &:active {
            background-color: var(--primary-btn-a);
          }
          & + .order-btn {
            margin-top: 8px;
          }
          /* 付款按钮 */
          &.pay {
            background-color: var(--success-btn);
            &:not(.disabled):hover {
              background-color: var(--success-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--success-btn-a);
            }
          }
          /* 取消按钮 */
          &.cancel {
            background-color: var(--cancel-btn);
            &:not(.disabled):hover {
              background-color: var(--cancel-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--cancel-btn-a);
            }
          }
          /* 联系按钮 */
          &.contact {
            background-color: var(--contact-btn);
            &:not(.disabled):hover {
              background-color: var(--contact-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--contact-btn-a);
            }
          }
          /* 催促按钮 */
          &.urge {
            background-color: var(--urge-btn);
            &:not(.disabled):hover {
              background-color: var(--urge-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--urge-btn-a);
            }
          }
          /* 续借按钮 */
          &.renew {
            background-color: var(--renew-btn);
            &:not(.disabled):hover {
              background-color: var(--renew-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--renew-btn-a);
            }
          }
          /* 归还按钮 */
          &.repay {
            background-color: var(--repay-btn);
            &:not(.disabled):hover {
              background-color: var(--repay-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--repay-btn-a);
            }
          }
          /* 评价按钮 */
          &.comment {
            background-color: var(--comment-btn);
            &:not(.disabled):hover {
              background-color: var(--comment-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--comment-btn-a);
            }
          }
          &.complete {
            background-color: var(--complete-btn);
            &:not(.disabled):hover {
              background-color: var(--complete-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--complete-btn-a);
            }
          }
          &.asales {
            background-color: var(--asales-btn);
            &:not(.disabled):hover {
              background-color: var(--asales-btn-h);
            }
            &:not(.disabled):active {
              background-color: var(--asales-btn-a);
            }
          }
          &.disabled {
            cursor: not-allowed;
            opacity: 0.6;
          }
        }
      }
    }
    & + .order-item {
      margin-top: 12px;
    }
  }
}
.no-order {
  margin: 36px 0;
}
.page-tool {
  margin-top: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding-bottom: 120px;
}
.comment-dialog,
.renew-dialog,
.repay-dialog,
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
              color: var(--danger);
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
.comment-dialog {
  .goods-comment-list {
    width: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    box-sizing: border-box;
    padding: 0 16px;
    .goods-comment-item {
      width: 100%;
      display: flex;
      align-items: center;
      flex-direction: column;
      .goods-comment-top {
        width: 100%;
        height: 42px;
        border-radius: 8px;
        background-color: #f6f6f6;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .goods-mess {
          margin-left: 12px;
          display: flex;
          align-items: center;
          .goods-cover {
            width: 36px;
            height: 36px;
            border-radius: 3px;
            overflow: hidden;
            img {
              width: 100%;
              height: 100%;
              object-fit: cover;
            }
          }
          .goods-name {
            margin-left: 8px;
            font-size: 14px;
            font-weight: bold;
            color: var(--primary-text);
          }
        }
        .goods-rate-box {
          margin-right: 18px;
          display: flex;
          align-items: center;
          .goods-rate-title {
            font-size: 14px;
            color: #666666;
          }
          .goods-rate {
            margin-left: 5px;
            :deep(.el-rate) {
              .el-rate__text {
                text-align: center;
                display: inline-block;
                min-width: 20px;
              }
            }
          }
        }
      }
      .goods-comment-content {
        margin-top: 8px;
        width: 100%;
        border-radius: 8px;
        overflow: hidden;
        background-color: #f8f8f8;
        :deep(.el-textarea) {
          .el-textarea__inner {
            border: none;
            background-color: transparent;
          }
        }
      }
      .goods-comment-anonymity {
        user-select: none;
        margin-top: 8px;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        .text {
          font-size: 12px;
          color: #999;
          margin-right: 6px;
        }
        .switch {
          margin-right: 12px;
        }
      }
      & + .goods-comment-item {
        margin-top: 12px;
      }
    }
  }
}
.renew-dialog,
.repay-dialog,
.after-dialog {
  .order-content {
    max-width: 100%;
    border-radius: 12px;
    background-color: #e6e6e6;
    box-sizing: border-box;
    padding: 12px;
  }
}
.detail-dialog {
  :deep(.el-dialog) {
    width: 64%;
  }
  .order-goods-box {
    width: 100%;
    display: flex;
    flex-direction: column;
    .order-goods-title-box {
      width: 100%;
      height: 36px;
      line-height: 36px;
      box-sizing: border-box;
      padding: 0 12px;
      border-radius: 5px;
      background-color: #efefef;
      font-size: 16px;
      font-weight: bold;
      color: #2c2f3b;
    }
    .order-goods-list {
      margin-top: 12px;
      width: 100%;
      display: flex;
      flex-wrap: wrap;
      gap: 8px 12px;
      box-sizing: border-box;
      padding: 8px 12px;
      border-radius: 5px;
      background-color: #f5f3f2;
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
      }
    }
  }
  .order-detail-box {
    margin-top: 24px;
    display: flex;
    width: 100%;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 12px 8px;
    .order-detail-item-box {
      position: relative;
      width: 33%;
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      box-sizing: border-box;
      padding: 12px;
      .order-detail-item {
        user-select: none;
        width: 100%;
        display: flex;
        align-items: center;
        &.between {
          justify-content: space-between;
        }
        &.block {
          padding-bottom: 48px;
        }
        .label {
          min-width: 72px;
          text-align: right;
          font-size: 16px;
          color: #9fa0a0;
        }
        .text {
          margin-left: 5px;
          font-size: 16px;
          color: var(--primary-text);
        }
        & + .order-detail-item {
          margin-top: 8px;
        }
      }
      & + .order-detail-item-box {
        border-left: 1px solid #d3ccd6;
      }
      .all-cost {
        position: absolute;
        bottom: 8px;
        right: 12px;
        .label {
          font-size: 16px;
          color: var(--primary-text);
        }
        .text {
          margin-left: 12px;
          font-size: 16px;
          color: var(--danger);
        }
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

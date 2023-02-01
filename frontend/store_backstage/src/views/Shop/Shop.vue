<template>
  <div class="main-box shop">
    <!-- 上方消息区域 -->
    <div class="info-box">
      <!-- 左边详情部分 -->
      <div class="detail">
        <div class="left">
          <!-- 封面 -->
          <div class="cover">
            <el-image :src="storeInfo.coverUrl" :preview-src-list="[storeInfo.coverUrl]"></el-image>
          </div>
          <!-- 内容 -->
          <div class="content">
            <div class="top">
              <div class="title">{{ storeInfo.storeName }}</div>
              <div class="mess">
                <div class="score">
                  <i class="icon fbookfont ic-score"></i>
                  <span>{{ storeInfo.score === null ? '暂无' : storeInfo.score }}</span>
                </div>
                <div class="amount">
                  <span class="tit">月借阅量：</span>
                  <span class="num">{{ amount }}</span>
                </div>
              </div>
              <div class="label-box">
                <span v-for="(item, index) in storeInfo.labelList" :key="index" class="label">{{ item }}</span>
              </div>
            </div>
            <div class="notice">
              <div class="tit">公告：</div>
              <p class="text">{{ storeInfo.notice }}</p>
            </div>
          </div>
        </div>
        <div class="right">
          <div class="btns">
            <div class="btn" @click="editBaseInfo">编辑</div>
          </div>
          <div class="mess">
            <div class="item">
              <span class="tit">商家地址</span>
              <span class="text pointer" @click="openMessBox('address')">
                <span>{{ storeInfo.address }}</span>
                <i class="icon fbookfont ic-more-1"></i>
              </span>
            </div>
            <div class="item">
              <span class="tit">营业时间</span>
              <span class="text">
                <span>{{ storeInfo.businessHours }}</span>
              </span>
            </div>
            <div class="item">
              <span class="tit">相关执照</span>
              <span class="text pointer" @click="openMessBox('license')">
                <i class="icon fbookfont ic-more-1"></i>
              </span>
            </div>
            <div class="item">
              <span class="tit">店内环境</span>
              <span class="text pointer" @click="openMessBox('env')">
                <i class="icon fbookfont ic-more-1"></i>
              </span>
            </div>
            <div class="item">
              <span class="tit">配送服务</span>
              <span class="text">
                <span>{{ storeInfo.distributionStr }}</span>
              </span>
            </div>
            <div class="item">
              <span class="tit">商家服务</span>
              <span v-if="isNotNull(storeInfo.storeService)" class="text pointer" @click="openMessBox('service')">
                <span>{{ storeInfo.storeService }}</span>
                <i class="icon fbookfont ic-more-1"></i>
              </span>
              <span v-else class="text">
                <span>无</span>
              </span>
            </div>
          </div>
        </div>
      </div>
      <!-- 右边店铺状态及收入展示 -->
      <div class="control">
        <div class="switch">
          <div class="title">
            <div class="text">店铺状态</div>
            <div class="state">{{ storeInfo.status === 1 ? '营业中' : '休息中' }}</div>
          </div>
          <div class="status-box">
            <div
              class="pilot-lamp"
              :style="'background-color: ' + (storeInfo.status === 1 ? 'var(--primary);' : 'var(--delete);')"></div>
            <el-switch
              v-model="storeInfo.status"
              active-color="#83ccd2"
              inactive-color="#dcdfe6"
              :active-value="1"
              :inactive-value="0"
              @change="changeStoreStatus"></el-switch>
          </div>
        </div>
        <div class="income">
          <div class="title">总收入</div>
          <div class="text">{{ keepTwoNum(storeInfo.income) }}</div>
          <div class="btn" ref="cashOut">
            <i class="icon fbookfont ic-wallet"></i>
            提现
            <div class="dot-box">
              <div class="dot"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 店铺信息弹框 -->
    <el-dialog class="mess-box" :title="messTitle" :visible.sync="messVisible">
      <div v-if="messTitle !== '相关执照' && messTitle !== '店内环境'" class="mess-content">
        {{ messContent }}
      </div>
      <div v-else>
        <el-image
          v-for="(item, index) in messContent"
          :key="'img-' + index"
          class="dia-img"
          :src="item"
          fit="cover"
          :preview-src-list="messContent">
        </el-image>
      </div>
    </el-dialog>
    <!-- 下方图表区域 -->
    <div class="chat-box">
      <div class="chat">
        <BarChart v-if="orderOption" ref-name="orderChart" :option="orderOption" />
        <ToolBox class="tool-pos" :refresh="refreshOrderChart" />
      </div>
      <div class="chat">
        <BarChart v-if="bookRankOption" ref-name="orderChart" :option="bookRankOption" />
        <ToolBox class="tool-pos" :refresh="refreshBookRankChart" />
      </div>
    </div>
  </div>
</template>

<script>
import BarChart from '@/components/Chart/BarChart';
import ToolBox from '@/components/Common/ToolBox';
import { getOrderQuantityApi, getBookRankApi } from '@/api/orderApi';
import { getStoreInfoApi, editStoreStatusApi } from '@/api/shopApi';
import chartHandler from '@/utils/chartDataHandler';

export default {
  components: {
    BarChart,
    ToolBox
  },
  data() {
    return {
      storeId: '',
      storeInfo: {},
      storeDetail: {},
      amount: 186, // 月借阅量
      orderOption: '',
      bookRankOption: '',
      messVisible: false,
      messTitle: '商家地址',
      messContent: ''
    };
  },
  created() {
    this.getShopMess();
    this.getOrderChartOption();
    this.getBookRankChart();
  },
  mounted() {
    // 为提现按钮设置变量
    this.setBtnVars();
  },
  methods: {
    /**
     * 改变营业状态
     */
    changeStoreStatus(val) {
      this.storeInfo.status = val === 1 ? 0 : 1;
      editStoreStatusApi().then(
        res => {
          if (res.data.flag) {
            this.storeInfo.status = val;
            this.$showMsgs(val === 1 ? '已营业' : '已休息', { type: 'success' });
          } else {
            this.$showMsgs(res.data.msg, { type: 'error' });
          }
        },
        err => {
          console.log('edit store status err =>', err);
        }
      );
    },
    /**
     * 打开消息弹框
     */
    openMessBox(type) {
      this.messVisible = true;
      switch (type) {
        case 'address':
          this.messTitle = '商家地址';
          this.messContent = this.storeInfo.address;
          break;
        case 'license':
          this.messTitle = '相关执照';
          this.messContent = [...this.storeDetail.businessLicenseUrl, ...this.storeDetail.licenseImgUrl];
          break;
        case 'env':
          this.messTitle = '店内环境';
          this.messContent = this.storeDetail.envImgsUrl;
          break;
        case 'service':
          this.messTitle = '商家服务';
          this.messContent = this.storeInfo.storeService ? this.storeInfo.storeService : '无';
          break;
        default:
          this.messTitle = '其它信息';
          this.messContent = '无';
          break;
      }
    },
    /**
     * 获取商家信息
     */
    getShopMess() {
      let employeeInfo = JSON.parse(window.localStorage.getItem('employeeInfo'));
      if (employeeInfo) {
        this.storeId = employeeInfo.storeId;
      }
      getStoreInfoApi(this.storeId).then(
        res => {
          if (res.data.flag) {
            // console.log('mess =>', res.data.data);
            this.storeInfo = res.data.data.bookstore;
            this.storeDetail = res.data.data.bookstoreDetail;
            this.$store.dispatch('setBusinessInfo', res.data.data.bookstore);
            this.$store.dispatch('setBusinessDetail', res.data.data.bookstoreDetail);
          } else {
            this.$showMsgs('店铺信息货物失败', { type: 'error' });
          }
        },
        err => {
          console.log('get store err =>', err);
        }
      );
    },
    /**
     * 前往编辑店铺xinx
     */
    editBaseInfo() {
      this.$router.push('/shop/edit/baseinfo');
    },
    /**
     * 判断字符串是否为空
     * @param {String} str
     */
    isNotNull(str) {
      if (typeof str === 'string' && str.length > 0) {
        return true;
      }
      return false;
    },
    /**
     * 为提现按钮动态设置变量
     */
    setBtnVars() {
      // 获取提现按钮的宽度
      let btnWidth = this.$refs.cashOut.offsetWidth;
      // 为提现按钮设置变量
      this.$refs.cashOut.style.setProperty('--btn-w', btnWidth + 'px');
    },
    /**
     * 获取近14天订单图表数据
     */
    getOrderChartOption() {
      getOrderQuantityApi().then(res => {
        if (res.data.flag) {
          this.orderOption = chartHandler.packingOrderData(res.data.data);
        }
      });
    },
    /**
     * 刷新订单图表数据
     */
    refreshOrderChart() {
      getOrderQuantityApi().then(res => {
        if (res.data.flag) {
          this.orderOption.dataset.source = res.data.data;
        }
      });
    },
    /**
     * 获取图书借阅榜信息
     */
    getBookRankChart() {
      getBookRankApi().then(res => {
        if (res.data.flag) {
          this.bookRankOption = chartHandler.packingBookRank(res.data.data);
        }
      });
    },
    /**
     * 刷新图书借阅榜信息
     */
    refreshBookRankChart() {
      // vue的watch似乎无法监听嵌套对象的多维数组元素的值变化
      getBookRankApi().then(res => {
        if (res.data.flag) {
          this.bookRankOption.dataset.source = res.data.data.data;
        }
      });
    },
    /**
     * 保留两位小数
     */
    keepTwoNum(num) {
      // 四舍五入
      let mNum = Math.round(num * 100) / 100;
      let numStr = mNum.toString();
      let dotIndex = numStr.indexOf('.');
      // 当整数时，pos_decimal=-1 自动补0
      if (dotIndex < 0) {
        dotIndex = numStr.length;
        numStr += '.';
      }

      // 当数字的长度< 小数点索引+2时，补0
      while (numStr.length <= dotIndex + 2) {
        numStr += '0';
      }
      return numStr;
    }
  }
};
</script>

<style lang="less" scoped>
.shop {
  display: flex;
  flex-direction: column;
  align-items: center;
  .info-box {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 320px;
    .detail {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 78%;
      height: 100%;
      .left {
        width: 72%;
        display: flex;
        align-items: center;
        .cover {
          display: flex;
          justify-content: center;
          align-content: center;
          margin-left: 20px;
          .el-image {
            width: 360px;
            height: 275px;
            border-radius: 5px;
          }
        }
        .content {
          flex-grow: 1;
          height: 275px;
          margin-left: 12px;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          .title {
            font-size: 24px;
            color: var(--primary-font);
            font-weight: bold;
            margin-top: 6px;
            margin-bottom: 12px;
          }
          .mess {
            display: flex;
            align-items: center;
            height: 24px;
            .score {
              font-size: 16px;
              color: var(--primary);
              margin-left: 6px;
              margin-right: 25px;
              .icon {
                font-size: 18px;
                margin-right: 8px;
              }
            }
            .amount {
              font-size: 16px;
              .tit {
                color: var(--main-font);
              }
              .num {
                color: var(--secondary-font);
              }
            }
          }
          .label-box {
            margin-top: 6px;
            max-width: 240px;
            display: flex;
            flex-wrap: wrap;
            .label {
              user-select: none;
              font-size: 12px;
              padding: 3px 12px;
              color: var(--gray);
              border-radius: 3px;
              box-sizing: border-box;
              border: solid 1px var(--regular-border);
              margin: 3px 5px;
            }
          }
          .notice {
            // width: 300px;
            // flex-grow: 1;
            height: 120px;
            box-sizing: border-box;
            padding: 6px 8px;
            border-radius: 5px;
            background-color: var(--secondary-bg);
            .tit {
              font-size: 14px;
              line-height: 21px;
              color: var(--main-font);
            }
            .text {
              margin: 0;
              display: -webkit-box;
              -webkit-box-orient: vertical;
              -webkit-line-clamp: 4;
              overflow: hidden;
              font-size: 14px;
              line-height: 21px;
              color: var(--secondary-font);
              text-indent: 2em;
            }
          }
        }
      }
      .right {
        margin-right: 18px;
        width: 210px;
        height: 275px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        .btns {
          display: flex;
          justify-content: flex-end;
          .btn {
            user-select: none;
            cursor: pointer;
            padding: 5px 24px;
            box-sizing: border-box;
            font-size: 14px;
            color: var(--secondary-font);
            border-radius: 5px;
            border: solid 1px var(--routine-border);
            &.active,
            &:hover {
              color: var(--primary);
              border-color: #c1eee1;
              background-color: #e6f5f6;
            }
          }
        }
        .mess {
          width: 100%;
          display: flex;
          flex-direction: column;
          align-items: center;
          box-sizing: border-box;
          padding: 12px 0;
          background-color: var(--secondary-bg);
          border-radius: 5px;
          .item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 6px;
            width: 92%;
            height: 27px;
            box-sizing: border-box;
            border-bottom: solid 1px #e6e6e6;
            font-size: 14px;
            line-height: 27px;
            .tit {
              color: #555555;
            }
            .text {
              user-select: none;
              width: 64%;
              display: flex;
              justify-content: flex-end;
              color: var(--secondary-font);
              span {
                font-size: 12px;
                margin-right: 5px;
                flex: 1;
                text-align: right;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap; /*文本不换行*/
              }
              .icon {
                font-size: 12px;
              }
              &.pointer {
                cursor: pointer;
              }
            }
          }
        }
      }
    }
    .control {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      width: 22%;
      height: 100%;
      .switch {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        width: 100%;
        height: 25%;
        border-radius: 12px;
        box-sizing: border-box;
        background-color: var(--secondary-bg);
        padding: 8px 12px;
        .title {
          display: flex;
          justify-content: space-between;
          .text {
            font-size: 14px;
            color: #555555;
          }
          .state {
            font-size: 10px;
            color: var(--secondary-font);
          }
        }
        .status-box {
          display: flex;
          align-items: center;
          justify-content: space-between;
          height: 40px;
          .pilot-lamp {
            margin-left: 12px;
            width: 18px;
            height: 18px;
            border-radius: 50%;
            background-color: var(--delete);
            transition: all 500ms linear;
          }
        }
      }
      .income {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        width: 100%;
        height: 70%;
        border-radius: 12px;
        box-sizing: border-box;
        padding: 12px;
        background-color: var(--secondary-bg);
        .title {
          font-size: 16px;
          color: #555555;
        }
        .text {
          user-select: none;
          font-size: 36px;
          text-align: center;
          color: var(--primary-font);
        }
        .btn {
          // 变量
          --btn-h: 42px;
          --dot-w: 12px;
          --move-l: calc(var(--btn-w) - var(--btn-h));

          position: relative;
          user-select: none;
          cursor: pointer;
          margin: 0 auto;
          width: 82%;
          height: var(--btn-h);
          border-radius: 21px;
          box-sizing: border-box;
          background: rgba(131, 204, 210, 0.3);
          border: solid 1px var(--primary);
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 16px;
          color: var(--main-font);
          .icon {
            font-size: 18px;
            margin-right: 8px;
          }
          .dot-box {
            position: absolute;
            top: -16px;
            left: calc(var(--btn-h) * 0.5);
            overflow: hidden;
            width: 100%;
            height: calc(100% + 16px);
          }
          .dot {
            content: '';
            position: absolute;
            top: 16px;
            left: calc(var(--btn-h) * -0.5);
            width: var(--btn-h);
            height: var(--btn-h);
            transition: all 300ms ease;
            display: none;
            transform: translateX(0);
            &::after {
              font-family: 'fbookfont' !important;
              font-size: 12px;
              color: var(--disable);
              content: '\e66f';
              position: absolute;
              display: block;
              width: var(--dot-w);
              height: var(--dot-w);
              text-align: center;
              line-height: var(--dot-w);
              border-radius: 50%;
              left: 50%;
              top: calc(var(--dot-w) / -2);
              transform: translateX(-50%);
              background-color: var(--primary);
            }
          }
          &:hover {
            .dot {
              display: block;
              animation: atom 3s infinite ease-in;
            }
          }
          &:active {
            .dot {
              animation-play-state: paused;
            }
          }
          @keyframes atom {
            0% {
              transform: translateX(-6px) rotate(0);
            }
            70% {
              transform: translateX(var(--move-l)) rotate(0);
            }
            90% {
              transform: translateX(var(--move-l)) rotate(90deg);
            }
            100% {
              transform: translateX(var(--move-l)) rotate(90deg) translateX(36px);
            }
          }
        }
      }
    }
  }
  .chat-box {
    display: flex;
    justify-content: space-around;
    width: 100%;
    margin-top: 48px;
    .chat {
      overflow: hidden;
      position: relative;
      width: 45%;
      height: 360px;
      padding: 12px;
      background-color: #fff;
      border-radius: 5px;
      box-sizing: border-box;
      box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
      .tool-box {
        position: absolute;
        top: 0;
        right: 0;
      }
    }
  }
  .mess-box {
    .dia-img {
      width: 150px;
      height: 150px;
      border-radius: 5px;
      margin: 6px;
    }
  }
}
</style>

<template>
  <div class="main-box orders">
    <div class="bar">
      <div class="left">
        <el-select v-model="selectCate" placeholder="请选择" class="option">
          <el-option v-for="item in searchCate" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <el-select v-if="inputFlag" v-model="searchContent" :placeholder="inputTips[selectCate]" class="cates input">
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
        <el-button class="search-btn" round icon="icon fbookfont ic-search">搜索</el-button>
      </div>
    </div>
    <el-table :data="tableData" stripe class="table">
      <el-table-column prop="number" label="订单号"></el-table-column>
      <el-table-column prop="status" label="订单状态">
        <template slot-scope="scope">
          {{ stateStr[scope.row.status] }}
        </template>
      </el-table-column>
      <el-table-column label="借阅时长">
        <template slot-scope="scope">
          {{ scope.row.borrow_time + '天' }}
        </template>
      </el-table-column>
      <el-table-column prop="consignee" label="联系人"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="expected_time" label="期望时间"></el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" class="edit-handle" @click="lookDetail(row)">查看</el-button>
          <el-button type="text" size="small" class="status-handle" @click="delivery()">配送</el-button>
          <el-button type="text" size="small" class="delete-handle" @click="complete()">完成</el-button>
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
    <el-dialog title="订单详情" width="72%" :visible.sync="dialogOrderVisible" class="order-data">
      <div class="order-mess">
        <div class="shop-list">
          <!-- 订单商品列表 -->
          <template>
            <div class="shop-item">
              <div class="img">
                <img src="/static/images/数学之美.jpg" alt="" />
              </div>
              <div class="mess">
                <div class="title">数学之美</div>
                <div class="num">× 1</div>
                <div class="cost-item">
                  <span class="tit">借阅费</span>
                  <span class="text">￥ 0</span>
                </div>
                <div class="cost-item">
                  <span class="tit">包装费</span>
                  <span class="text">￥ 0</span>
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
              <span class="text">30 天</span>
            </div>
            <div class="info-item">
              <span class="tit">可借阅时长</span>
              <span class="text"
                ><el-input-number size="mini" v-model="renewDuration" :min="1"></el-input-number>天
              </span>
            </div>
            <div class="info-item">
              <span class="tit">期望时间</span>
              <span class="text">立即送出</span>
            </div>
            <div class="info-item">
              <span class="tit">联系信息</span>
              <span class="text">张三 13456238956</span>
            </div>
            <div class="info-item">
              <span class="tit">配送地址</span>
              <span class="text">韶关学院西区梧桐苑</span>
            </div>
            <div class="info-item">
              <span class="tit">备注</span>
              <el-popover
                placement="top-start"
                width="360"
                trigger="hover"
                class="text remark"
                :open-delay="800"
                :disabled="remark.length < 22">
                <span>{{ remark }}</span>
                <span slot="reference">{{ comShowDesc(remark, 22) }}</span>
              </el-popover>
            </div>
          </div>
          <div class="detail-item">
            <!-- 订单信息 -->
            <div class="info-item">
              <span class="tit">订单号</span>
              <span class="text">L-1645678912345632</span>
            </div>
            <div class="info-item">
              <span class="tit">订单状态</span>
              <span class="text">待配送</span>
            </div>
            <div class="info-item">
              <span class="tit">创建时间</span>
              <span class="text">2022-10-01 16:40:56</span>
            </div>
            <div class="info-item">
              <span class="tit">支付时间</span>
              <span class="text">2022-10-01 16:41:23</span>
            </div>
            <div class="info-item">
              <span class="tit">支付方式</span>
              <span class="text">在线支付</span>
            </div>
          </div>
          <div class="detail-item">
            <!-- 费用信息 -->
            <div class="info-item cost">
              <span class="tit">借阅费</span>
              <span class="text">￥ 0</span>
            </div>
            <div class="info-item cost">
              <span class="tit">包装费</span>
              <span class="text">￥ 0</span>
            </div>
            <div class="info-item cost">
              <span class="tit">配送费</span>
              <span class="text">￥ 2</span>
            </div>
            <div class="info-item cost">
              <span class="tit">实收金额</span>
              <span class="text red">￥ 2</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOrdersApi } from '@/api/orderApi';

export default {
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
      stateStr: ['待配送', '待收货', '待归还', '待上门', '待评价', '已完成', '逾期中', '已逾期', '售后中'],
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
      dialogOrderVisible: false,
      // 测试数据
      renewDuration: 0,
      remark: '尽快配送'
    };
  },
  created() {
    this.getOrderByPage();
  },
  methods: {
    /**
     * 执行搜索
     */
    handleSearch() {},
    /**
     * 查看订单详情
     */
    lookDetail(row) {
      this.dialogOrderVisible = true;
    },
    /**
     * 配送订单
     */
    delivery() {},
    /**
     * 完成订单
     */
    complete() {},
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
      let start = (this.currentPage - 1) * this.pageSize;
      let end = this.currentPage * this.pageSize;
      getOrdersApi().then(
        res => {
          this.tableData = res.data.slice(start, end);
        },
        err => {}
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
    }
  },
  watch: {
    /**
     * 监听selectCate值的变化
     */
    selectCate(val) {
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
      height: 240px;
      display: flex;
      justify-content: space-between;
      .detail-item {
        width: calc(100% / 3);
        height: 100%;
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
            font-size: 16px;
            color: var(--cost-title);
          }
          .text {
            width: 65%;
            font-size: 16px;
            color: var(--primary-font);
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
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
        }
      }
    }
  }
}
</style>

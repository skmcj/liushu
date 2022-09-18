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
        <el-button type="text" size="small" class="edit-handle" @click="lookDetail()">查看</el-button>
        <el-button type="text" size="small" class="status-handle" @click="delivery()">配送</el-button>
        <el-button type="text" size="small" class="delete-handle" @click="complete()">完成</el-button>
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
  </div>
</template>

<script>
import { getOrdersAPI } from '@/api/orderAPI';

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
      stateStr: ['待付款', '待配送', '待归还', '待评价'],
      inputFlag: false,
      cates: [
        {
          value: '123456',
          label: '计算机'
        },
        {
          value: '123457',
          label: '科学'
        }
      ],
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      total: 100
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
    lookDetail() {},
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
      getOrdersAPI().then(
        res => {
          this.tableData = res.data.slice(start, end);
        },
        err => {}
      );
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
}
</style>

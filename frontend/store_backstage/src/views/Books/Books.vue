<template>
  <div class="main-box books">
    <div class="bar">
      <!-- 搜索框 -->
      <div class="left">
        <el-select v-model="selectCate" placeholder="请选择" class="option">
          <el-option v-for="item in searchCate" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <el-select v-if="inputFlag" v-model="searchContent" :placeholder="inputTips[selectCate]" class="bookCate input">
          <template slot="prefix">
            <i class="icon fbookfont ic-search" @click="handleSearch"></i>
          </template>
          <template v-if="selectCate === 'bookCateId'">
            <el-option v-for="item in bookCate" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </template>
          <template v-if="selectCate === 'goodsCateId'">
            <el-option v-for="item in goodsCate" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </template>
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
      <!-- 批量操作按钮 -->
      <div class="right">
        <div class="batch-btns">
          <span class="btn del" @click="deleteMess('batch')">批量删除</span>
          <span class="btn" @click="editStatus('batch', 1)">批量启售</span>
          <span class="btn del" @click="editStatus('batch', 0)">批量禁售</span>
        </div>
        <el-button class="add-btn" round icon="icon fbookfont ic-add" @click="add">添加图书</el-button>
      </div>
    </div>
    <!-- 表格数据 -->
    <el-table
      :data="tableData"
      stripe
      class="table"
      @selection-change="handleSelectionChange"
      empty-text="暂无图书数据">
      <el-table-column type="selection" width="42"></el-table-column>
      <el-table-column prop="cover" label="封面">
        <template slot-scope="scope">
          <el-image
            style="width: 42px; height: 42px; border-radius: 5px; cursor: pointer"
            :src="scope.row.coverUrl"
            :preview-src-list="[scope.row.coverUrl]">
            <div slot="error" class="image-slot">
              <img src="@/assets/images/no-cover.jpg" style="width: auto; height: 40px; border: none" />
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="图书名称"></el-table-column>
      <el-table-column prop="bookCateId" label="图书类别">
        <template slot-scope="{ row }">
          {{ bookCateName[row.bookCateId] }}
        </template>
      </el-table-column>
      <el-table-column prop="goodsCateId" label="店内分类">
        <template slot-scope="{ row }">
          {{ goodsCateName[row.goodsCateId] }}
        </template>
      </el-table-column>
      <el-table-column prop="author" label="作者"></el-table-column>
      <el-table-column label="售卖状态">
        <template slot-scope="scope">
          {{ scope.row.status == '1' ? '正常' : '禁售' }}
        </template>
      </el-table-column>
      <el-table-column prop="inventory" label="库存"></el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small" class="edit-handle" @click="editMess(scope.row.id)">编辑</el-button>
          <el-button type="text" size="small" class="status-handle" @click="editStatus('single', scope.row)">
            {{ scope.row.status == '1' ? '禁售' : '启售' }}
          </el-button>
          <el-button type="text" size="small" class="delete-handle" @click="deleteMess('single', scope.row)"
            >删除</el-button
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
    <!-- 改变图书状态的提示框 -->
    <el-dialog title="提示" :visible.sync="stateDialogVisible" width="30%" :modal-append-to-body="false">
      <span>
        <i class="icon fbookfont ic-info"></i>
        <template v-if="handleType === 'single'">
          您是否确认{{ checkedRow.status == '1' ? '禁售' : '启售' }}该图书？
        </template>
        <template v-else> 您是否确认{{ checkStatus == '0' ? '禁售' : '启售' }}这些图书？ </template>
      </span>
      <span slot="footer" class="dialog-footer state">
        <el-button type="primary" @click="handleStatus">确 认</el-button>
        <el-button @click="stateDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 删除图书的提示框 -->
    <el-dialog title="提示" :visible.sync="delDialogVisible" width="30%" :modal-append-to-body="false">
      <span>
        <i class="icon fbookfont ic-info"></i>
        <template v-if="handleType === 'single'">
          您是否确认删除该图书？<br />&nbsp;&nbsp;&nbsp;&nbsp;(注：该操作不可撤回)
        </template>
        <template v-else> 您是否确认删除这些图书？<br />&nbsp;&nbsp;&nbsp;&nbsp;(注：该操作不可撤回) </template>
      </span>
      <span slot="footer" class="dialog-footer delete">
        <el-button type="primary" @click="handleDelete">确 认</el-button>
        <el-button @click="delDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getBookPageApi, getBookCateApi, saveBookApi, editBookStatusApi, deleteBookApi } from '@/api/bookApi';
import { getAllCateApi } from '@/api/cateApi';

export default {
  data() {
    return {
      stateDialogVisible: false,
      delDialogVisible: false,
      searchCate: [
        {
          value: 'name',
          label: '名称'
        },
        {
          value: 'bookCateId',
          label: '图书类别'
        },
        {
          value: 'goodsCateId',
          label: '店内分类'
        },
        {
          value: 'author',
          label: '作者'
        },
        {
          value: 'press',
          label: '出版社'
        }
      ],
      selectCate: 'name',
      searchContent: '',
      inputTips: {
        name: '请输入图书名称',
        author: '请输入作者名称',
        press: '请输入出版社名称',
        bookCateId: '请选择图书类别',
        goodsCateId: '请选择店内分类'
      },
      inputFlag: false,
      // 图书类别
      bookCate: [],
      bookCateName: {},
      // 店内分类
      goodsCate: [],
      goodsCateName: {},
      // 选择的图书id列表
      checkList: [],
      checkedRow: '',
      checkStatus: 0,
      handleType: 'single',
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      total: 100
    };
  },
  created() {
    this.getBookCate();
    this.getGoodsCate();
    this.getBookByPage();
  },
  methods: {
    /**
     * 表格选择项改变时触发
     */
    handleSelectionChange(val) {
      let checkArr = [];
      val.forEach(item => {
        checkArr.push(item.id);
      });
      this.checkList = checkArr;
    },
    /**
     * 获取图书类别信息
     */
    getBookCate() {
      getBookCateApi().then(
        res => {
          this.bookCate = res.data.data;
          this.bookCateName = this.getCateNameObj(res.data.data);
        },
        err => {
          console.log('getBookCate err => ', err);
        }
      );
    },
    /**
     * 获取店内分类信息
     */
    getGoodsCate() {
      let storeId = this.$store.state.businessInfo.id;
      getAllCateApi(storeId).then(
        res => {
          this.goodsCate = res.data.data;
          this.goodsCateName = this.getCateNameObj(res.data.data);
        },
        err => {
          console.log('getGoodsCate err => ', err);
        }
      );
    },
    /**
     * 将类别对象数组封装为对象
     * @param {Array} obj
     */
    getCateNameObj(obj) {
      let nameObj = {};
      obj.forEach(item => {
        nameObj[item.id] = item.name;
      });
      return nameObj;
    },
    /**
     * 执行搜索
     */
    handleSearch() {
      this.currentPage = 1;
      this.getBookByPage();
    },
    /**
     * 批量启售
     */
    batchStartSell() {},
    /**
     * 批量停售
     */
    batchStopSell() {},
    /**
     * 添加图书
     */
    add() {
      this.$router.push('/books/add');
    },
    /**
     * 编辑图书信息
     */
    editMess(id) {
      this.$router.push({ path: '/books/edit', query: { id: id } });
    },
    /**
     * 修改图书状态
     * @param {String} type single | batch
     */
    editStatus(type, data) {
      this.handleType = type;
      if (type === 'batch') {
        // 批量修改
        if (this.checkList.length === 0) {
          this.$showMsgs('批量操作，请先勾选操作图书', { type: 'warning' });
          return false;
        }
        this.checkStatus = data;
      } else {
        this.checkedRow = data;
      }
      this.stateDialogVisible = true;
    },
    /**
     * 处理状态操作
     */
    handleStatus() {
      this.stateDialogVisible = false;
      let status = 0;
      let ids = [];
      if (this.handleType === 'single') {
        status = this.checkedRow.status ? 0 : 1;
        ids.push(this.checkedRow.id);
      } else {
        status = this.checkStatus;
        ids = this.checkList;
      }
      editBookStatusApi(status, ids).then(
        res => {
          if (res.data.flag) {
            this.$showMsgs('状态修改成功', {
              type: 'success',
              closeFunc: () => {
                this.getBookByPage();
              }
            });
          } else {
            this.$showMsgs('状态修改失败，请稍候再试', { type: 'error' });
          }
        },
        err => {
          console.log('edit book status err => ', err);
        }
      );
    },
    /**
     * 删除图书
     */
    deleteMess(type, row) {
      this.handleType = type;
      if (type === 'batch') {
        // 批量修改
        if (this.checkList.length === 0) {
          this.$showMsgs('批量操作，请先勾选操作图书', { type: 'warning' });
          return false;
        }
      } else {
        this.checkedRow = row;
      }
      this.delDialogVisible = true;
    },
    /**
     * 处理删除操作
     */
    handleDelete() {
      this.delDialogVisible = false;
      let ids = [];
      if (this.handleType === 'single') {
        ids.push(this.checkedRow.id);
      } else {
        ids = this.checkList;
      }
      deleteBookApi(ids).then(
        res => {
          if (res.data.flag) {
            this.$showMsgs('图书删除成功', {
              type: 'success',
              closeFunc: () => {
                this.getBookByPage();
              }
            });
          } else {
            this.$showMsgs('图书删除失败：' + res.data.msg, { type: 'error' });
          }
        },
        err => {
          console.log('delete book status err => ', err);
        }
      );
    },
    /**
     * 处理图书每页数量
     */
    handleSizeChange(val) {
      this.pageSize = val;
      this.getBookByPage();
    },
    /**
     * 处理图书当前页信息
     */
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getBookByPage();
    },
    /**
     * 获取图书信息
     */
    getBookByPage() {
      let param = {
        page: this.currentPage,
        pageSize: this.pageSize
      };
      if (this.searchContent) {
        param[this.selectCate] = this.searchContent;
      }
      getBookPageApi(param).then(
        res => {
          if (res.data.flag) {
            this.tableData = res.data.data.records;
            this.total = res.data.data.total;
          } else {
            this.$showMsgs('图书信息获取失败', { type: 'error' });
          }
        },
        err => {
          console.log('get book page err =>', err);
        }
      );
    }
  },
  watch: {
    /**
     * 监听selectCate值的变化
     */
    selectCate(val) {
      if (val === 'name' || val === 'author' || val === 'press') {
        this.inputFlag = false;
      } else {
        this.inputFlag = true;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.books {
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
    .right {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      .batch-btns {
        margin-right: 12px;
        .btn {
          width: 104px;
          height: 22px;
          line-height: 22px;
          font-size: 14px;
          padding: 0 24px;
          user-select: none;
          cursor: pointer;
          color: var(--edit);
          & + .btn {
            border-left: solid 1px var(--regular-border);
          }
          &.del {
            color: var(--delete);
          }
        }
      }
    }
  }
}
</style>

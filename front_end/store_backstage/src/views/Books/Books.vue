<template>
  <div class="main-box books">
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
      <div class="right">
        <div class="batch-btns">
          <span class="btn del" @click="batchDelete">批量删除</span>
          <span class="btn" @click="batchStartSell">批量启售</span>
          <span class="btn del" @click="batchStopSell">批量停售</span>
        </div>
        <el-button class="add-btn" round icon="icon fbookfont ic-add" @click="add">添加图书</el-button>
      </div>
    </div>
    <el-table :data="tableData" stripe class="table">
      <el-table-column type="selection" width="42"></el-table-column>
      <el-table-column prop="cover" label="封面">
        <template slot-scope="scope">
          <el-image
            style="width: 42px; height: 42px; border-radius: 5px; cursor: pointer"
            :src="scope.row.cover"
            :preview-src-list="['https://s1.328888.xyz/2022/09/16/ol4Dh.jpg']">
            <div slot="error" class="image-slot">
              <img src="@/assets/images/no-cover.jpg" style="width: auto; height: 40px; border: none" />
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="图书名称"></el-table-column>
      <el-table-column prop="book_cate" label="图书类别"></el-table-column>
      <el-table-column prop="goods_cate" label="店内分类"></el-table-column>
      <el-table-column prop="author" label="作者"></el-table-column>
      <el-table-column label="售卖状态">
        <template slot-scope="scope">
          {{ scope.row.status == '0' ? '启售' : '禁售' }}
        </template>
      </el-table-column>
      <el-table-column prop="inventory" label="库存"></el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small" class="edit-handle" @click="editMess()">编辑</el-button>
          <el-button type="text" size="small" class="status-handle" @click="editStatus(scope.row)">
            {{ scope.row.status == '0' ? '禁售' : '启售' }}
          </el-button>
          <el-button type="text" size="small" class="delete-handle" @click="deleteMess(scope.row)">删除</el-button>
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
        您是否确认{{ checkedRow.status == '0' ? '禁售' : '启售' }}该图书？
      </span>
      <span slot="footer" class="dialog-footer state">
        <el-button type="primary" @click="stateDialogVisible = false">确 认</el-button>
        <el-button @click="stateDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 删除图书的提示框 -->
    <el-dialog title="提示" :visible.sync="delDialogVisible" width="30%" :modal-append-to-body="false">
      <span>
        <i class="icon fbookfont ic-info"></i>
        您是否确认删除该图书？<br />&nbsp;&nbsp;&nbsp;&nbsp;(注：该操作不可撤回)
      </span>
      <span slot="footer" class="dialog-footer delete">
        <el-button type="primary" @click="delDialogVisible = false">确 认</el-button>
        <el-button @click="delDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getBooksAPI } from '@/api/bookAPI';

export default {
  data() {
    return {
      stateDialogVisible: false,
      delDialogVisible: false,
      checkedRow: '',
      searchCate: [
        {
          value: 'name',
          label: '名称'
        },
        {
          value: 'book_cate_id',
          label: '图书类别'
        },
        {
          value: 'goods_cate_id',
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
        book_cate_id: '请选择图书类别',
        goods_cate_id: '请选择店内分类'
      },
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
    this.getBookByPage();
  },
  methods: {
    /**
     * 执行搜索
     */
    handleSearch() {},
    /**
     * 批量删除
     */
    batchDelete() {},
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
    editMess() {},
    /**
     * 修改图书状态
     */
    editStatus(row) {
      this.stateDialogVisible = true;
      this.checkedRow = row;
    },
    /**
     * 删除图书
     */
    deleteMess(row) {
      this.delDialogVisible = true;
      this.checkedRow = row;
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
      let start = (this.currentPage - 1) * this.pageSize;
      let end = this.currentPage * this.pageSize;
      getBooksAPI().then(
        res => {
          this.tableData = res.data.slice(start, end);
        },
        err => {}
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

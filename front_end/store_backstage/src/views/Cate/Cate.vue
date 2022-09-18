<template>
  <div class="main-box cate">
    <div class="bar">
      <el-button class="add-btn" round icon="icon fbookfont ic-add" @click="add">添加分类</el-button>
    </div>
    <el-table :data="tableData" stripe class="table">
      <el-table-column prop="name" label="分类名称" min-width="20%"></el-table-column>
      <el-table-column prop="description" label="分类描述" min-width="45%">
        <template slot-scope="scope">
          <el-popover
            placement="top-start"
            title="分类描述"
            width="500"
            trigger="hover"
            :open-delay="800"
            :disabled="scope.row.description.length < 74">
            <div>{{ scope.row.description }}</div>
            <span slot="reference">{{ comShowDesc(scope.row.description, 74) }}</span>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="操作时间" min-width="20%"></el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <el-button type="text" size="small" class="edit-handle" @click="editMess()">编辑</el-button>
        <el-button type="text" size="small" class="delete-handle" @click="deleteMess()"> 删除 </el-button>
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
    <!-- 删除分类的提示框 -->
    <el-dialog title="提示" :visible.sync="delDialogVisible" width="30%" :modal-append-to-body="false">
      <span>
        <i class="icon fbookfont ic-info"></i>
        您是否确认删除该分类？<br />&nbsp;&nbsp;&nbsp;&nbsp;(注：该操作不可撤回，请确保该分类下没有在库书籍)
      </span>
      <span slot="footer" class="dialog-footer delete">
        <el-button type="primary" @click="delDialogVisible = false">确 认</el-button>
        <el-button @click="delDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCatesAPI } from '@/api/cateApi';

export default {
  data() {
    return {
      delDialogVisible: false,
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      total: 36
    };
  },
  created() {
    this.getCateByPage();
  },
  methods: {
    /**
     * 添加分类
     */
    add() {
      this.$router.push('/cate/add');
    },
    /**
     * 编辑分类信息
     */
    editMess() {},
    /**
     * 删除分类信息
     */
    deleteMess() {
      this.delDialogVisible = true;
    },
    /**
     * 处理分类每页数量
     */
    handleSizeChange(val) {
      this.pageSize = val;
      this.getCateByPage();
    },
    /**
     * 处理员工当前页信息
     */
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getCateByPage();
    },
    /**
     * 获取分类信息
     */
    getCateByPage() {
      let start = (this.currentPage - 1) * this.pageSize;
      let end = this.currentPage * this.pageSize;
      getCatesAPI().then(
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
  }
};
</script>

<style lang="less" scoped>
.cate {
  .bar {
    display: flex;
    justify-content: flex-end;
  }
  .table {
    .desc {
      color: #999;
    }
  }
}
</style>

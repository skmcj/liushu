<template>
  <div class="main-box employee">
    <div class="bar">
      <el-input
        v-model="searchContent"
        placeholder="请输入员工姓名"
        style="width: 250px"
        clearable
        @keyup.enter.native="handleSearch">
        <i slot="prefix" class="icon fbookfont ic-search" style="cursor: pointer" @click="handleSearch"></i>
      </el-input>
      <el-button class="add-btn" round icon="icon fbookfont ic-add" @click="add">添加员工</el-button>
    </div>
    <el-table :data="tableData" stripe class="table">
      <el-table-column prop="name" label="员工姓名"></el-table-column>
      <el-table-column prop="username" label="账号"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="competence" label="权限">
        <template slot-scope="scope">
          {{ scope.row.competence == '0' ? '管理员' : '员工' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="账号状态">
        <template slot-scope="scope">
          {{ scope.row.status == '0' ? '正常' : '禁用' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small" class="edit-handle" @click="editMess()">编辑</el-button>
          <el-button type="text" size="small" class="status-handle" @click="editStatus(scope.row)">
            {{ scope.row.status == '0' ? '禁用' : '启用' }}
          </el-button>
          <el-button type="text" size="small" class="delete-handle" @click="deleteMess(scope.row)"> 删除 </el-button>
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
    <!-- 改变员工状态的提示框 -->
    <el-dialog title="提示" :visible.sync="stateDialogVisible" width="30%" :modal-append-to-body="false">
      <span>
        <i class="icon fbookfont ic-info"></i>
        您是否确认{{ checkedRow.status == '0' ? '禁用' : '启用' }}该员工？
      </span>
      <span slot="footer" class="dialog-footer state">
        <el-button type="primary" @click="stateDialogVisible = false">确 认</el-button>
        <el-button @click="stateDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
    <!-- 删除员工的提示框 -->
    <el-dialog title="提示" :visible.sync="delDialogVisible" width="30%" :modal-append-to-body="false">
      <span>
        <i class="icon fbookfont ic-info"></i>
        您是否确认删除该员工？<br />&nbsp;&nbsp;&nbsp;&nbsp;(注：该操作不可撤回)
      </span>
      <span slot="footer" class="dialog-footer delete">
        <el-button type="primary" @click="delDialogVisible = false">确 认</el-button>
        <el-button @click="delDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getEmployeesAPI } from '@/api/employeeAPI.js';

export default {
  data() {
    return {
      stateDialogVisible: false,
      delDialogVisible: false,
      checkedRow: '',
      searchContent: '',
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      total: 100
    };
  },
  created() {
    this.getEmployeeByPage();
  },
  methods: {
    /**
     * 搜索员工姓名
     */
    handleSearch() {},
    /**
     * 添加员工
     */
    add() {
      this.$router.push('/employee/add');
    },
    /**
     * 编辑员工信息
     */
    editMess() {},
    /**
     * 修改员工账号状态
     */
    editStatus(row) {
      this.checkedRow = row;
      this.stateDialogVisible = true;
    },
    /**
     * 删除员工信息
     */
    deleteMess(row) {
      this.checkedRow = row;
      this.delDialogVisible = true;
    },
    /**
     * 处理员工每页数量
     */
    handleSizeChange(val) {
      this.pageSize = val;
      this.getEmployeeByPage();
    },
    /**
     * 处理员工当前页信息
     */
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getEmployeeByPage();
    },
    /**
     * 获取员工信息
     */
    getEmployeeByPage() {
      let start = (this.currentPage - 1) * this.pageSize;
      let end = this.currentPage * this.pageSize;
      getEmployeesAPI().then(
        res => {
          this.tableData = res.data.slice(start, end);
        },
        err => {}
      );
    }
  }
};
</script>

<style lang="less" scoped></style>

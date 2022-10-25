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
      <el-button class="add-btn" v-if="employeeCom < 2" round icon="icon fbookfont ic-add" @click="add"
        >添加员工</el-button
      >
    </div>
    <el-table :data="tableData" stripe class="table" empty-text="暂无员工或您权限不足">
      <el-table-column prop="name" label="员工姓名"></el-table-column>
      <el-table-column prop="username" label="账号"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="competence" label="权限">
        <template slot-scope="scope">
          {{ scope.row.competence == '0' ? '店长' : scope.row.competence == '1' ? '管理员' : '员工' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="账号状态">
        <template slot-scope="scope">
          {{ scope.row.status == '1' ? '正常' : '禁用' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button
            v-if="employeeCom < scope.row.competence || employeeId === scope.row.id"
            type="text"
            size="small"
            class="edit-handle"
            @click="editMess(scope.row.id)"
            >编辑</el-button
          >
          <el-button
            v-if="employeeCom < scope.row.competence"
            type="text"
            size="small"
            class="status-handle"
            @click="editStatus(scope.row)">
            {{ scope.row.status == '1' ? '禁用' : '启用' }}
          </el-button>
          <el-button
            v-if="employeeCom < scope.row.competence"
            type="text"
            size="small"
            class="delete-handle"
            @click="deleteMess(scope.row)">
            删除
          </el-button>
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
        您是否确认{{ checkedRow.status == '1' ? '禁用' : '启用' }}该员工？
      </span>
      <span slot="footer" class="dialog-footer state">
        <el-button type="primary" @click="changeStatus">确 认</el-button>
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
        <el-button type="primary" @click="delEmployee">确 认</el-button>
        <el-button @click="delDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getEmployeePageApi, editEmployeeStatusApi, delEmployeeApi } from '@/api/employeeApi';

export default {
  data() {
    return {
      storeId: '',
      employeeId: '',
      employeeCom: '',
      stateDialogVisible: false,
      delDialogVisible: false,
      checkedRow: '',
      searchContent: '',
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      total: 0
    };
  },
  created() {
    this.getCompetence();
    this.init();
  },
  methods: {
    /**
     * 初始化
     */
    init() {
      const params = {
        page: this.currentPage,
        pageSize: this.pageSize,
        storeId: this.storeId,
        name: this.searchContent ? this.searchContent : undefined
      };
      getEmployeePageApi(params)
        .then(res => {
          if (res.data.flag) {
            this.tableData = res.data.data.records || [];
            this.total = res.data.data.total;
          }
        })
        .catch(err => {
          this.$message.error('请求出错了：' + err);
        });
    },
    /**
     * 获取当前员工权限
     */
    getCompetence() {
      const employeeInfo = JSON.parse(window.localStorage.getItem('employeeInfo'));
      if (employeeInfo) {
        this.employeeCom = employeeInfo.competence;
        this.employeeId = employeeInfo.id;
        this.storeId = employeeInfo.storeId;
      }
    },
    /**
     * 搜索员工姓名
     */
    handleSearch() {
      this.currentPage = 1;
      this.init();
    },
    /**
     * 添加员工
     */
    add() {
      this.$router.push('/employee/add');
    },
    /**
     * 编辑员工信息
     */
    editMess(id) {
      this.$router.push({ path: '/employee/edit', query: { id: id } });
    },
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
      this.init();
    },
    /**
     * 处理员工当前页信息
     */
    handleCurrentChange(val) {
      this.currentPage = val;
      this.init();
    },
    /**
     * 改变员工状态
     */
    changeStatus() {
      this.stateDialogVisible = false;
      editEmployeeStatusApi({
        id: this.checkedRow.id,
        status: !this.checkedRow.status ? 1 : 0
      }).then(
        res => {
          if (res.data.flag) {
            this.$showMsgs('状态修改成功', { type: 'success' });
            this.handleSearch();
          } else {
            this.$showMsgs('状态修改失败', { type: 'error' });
          }
        },
        err => {
          console.log('changeEmployeeStatus err => ', err);
        }
      );
    },
    /**
     * 删除员工
     */
    delEmployee() {
      this.delDialogVisible = false;
      delEmployeeApi(this.checkedRow.id).then(
        res => {
          if (res.data.flag) {
            this.$showMsgs('员工删除成功', { type: 'success' });
            this.handleSearch();
          } else {
            this.$showMsgs('员工删除失败', { type: 'error' });
          }
        },
        err => {
          console.log('deleteEmployee err => ', err);
        }
      );
    }
  }
};
</script>

<style lang="less" scoped></style>

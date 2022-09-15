<template>
  <div class="employee">
    <div class="bar">
      <el-input
        v-model="searchContent"
        placeholder="请输入员工姓名"
        style="width: 250px"
        clearable
        @keyup.enter.native="handleSearch">
        <i slot="prefix" class="icon fbookfont ic-search" style="cursor: pointer" @click="handleSearch"></i>
      </el-input>
      <el-button round icon="icon fbookfont ic-add">添加员工</el-button>
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
          <el-button type="text" size="small" class="status-handle" @click="editStatus()">
            {{ scope.row.status == '0' ? '启用' : '禁用' }}
          </el-button>
          <el-button type="text" size="small" class="delete-handle" @click="deleteMess()"> 删除 </el-button>
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
  </div>
</template>

<script>
import { getEmployeesAPI } from '@/api/employeeAPI.js';

export default {
  data() {
    return {
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
     * 编辑员工信息
     */
    editMess() {},
    /**
     * 修改员工账号状态
     */
    editStatus() {},
    /**
     * 删除员工信息
     */
    deleteMess() {},
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

<style lang="less" scoped>
.employee {
  background-color: var(--white);
  border-radius: 12px;
  padding: 32px 28px;
  .bar {
    display: flex;
    justify-content: space-between;
    height: 42px;
    :deep(.el-input__inner) {
      height: 42px;
      &:focus {
        border-color: var(--primary-border-f);
      }
    }
    :deep(.el-input__prefix) {
      left: 9px;
      .icon {
        height: 42px;
        line-height: 42px;
        width: 25px;
        text-align: center;
      }
    }
    .el-button {
      color: var(--white);
      border: none;
      padding: 12px 40px;
      background-color: var(--primary);
      &:focus,
      &:hover {
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
  .table {
    overflow: hidden;
    margin-top: 24px;
    width: 100%;
    border: solid 2px var(--primary-border);
    border-radius: 8px;
    box-sizing: border-box;
    .edit-handle {
      color: var(--edit);
    }
    .status-handle {
      color: var(--disable);
    }
    .delete-handle {
      color: var(--delete);
    }
  }
  .el-table {
    &::before {
      height: 0;
    }
    :deep(thead) {
      color: var(--primary-font);
    }
    :deep(.cell) {
      padding: 0 12px;
    }
    :deep(.el-table__cell) {
      padding: 11px 0;
    }
  }
  .page {
    text-align: center;
    margin-top: 32px;
  }
}
</style>

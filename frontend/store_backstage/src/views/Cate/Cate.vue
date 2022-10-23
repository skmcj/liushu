<template>
  <div class="main-box cate">
    <div class="bar">
      <el-button class="add-btn" round icon="icon fbookfont ic-add" @click="addCate">添加分类</el-button>
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
        <template slot-scope="scope">
          <el-button type="text" size="small" class="edit-handle" @click="editMess(scope)">编辑</el-button>
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
    <!-- 新增分类弹框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="cateDialogVisible"
      width="30%"
      :modal-append-to-body="false"
      class="add-box blod-title">
      <el-form :model="formData" :rules="rules" ref="form" label-width="108px" class="form">
        <el-form-item label="分类名称：" prop="name">
          <el-input v-model="formData.name" placeholder="请输入分类名称" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="排序：" prop="sort">
          <el-input v-model.number="formData.sort" placeholder="请输入排序" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="分类描述" prop="desc">
          <el-input
            type="textarea"
            v-model="formData.description"
            placeholder="请输入分类描述"
            resize="none"
            style="width: 300px"
            class="h-75"></el-input>
        </el-form-item>
        <div class="btns">
          <el-form-item label-width="0">
            <el-button @click="goBack('form')">取消</el-button>
            <el-button type="primary" @click="submitForm('form', false)">保存</el-button>
            <el-button v-if="actionType == 'add'" type="primary" class="continue" @click="submitForm('form', true)"
              >保存并继续添加</el-button
            >
          </el-form-item>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { getCatesApi } from '@/api/cateApi';

export default {
  data() {
    return {
      id: '',
      dialogTitle: '新增分类',
      actionType: 'add',
      delDialogVisible: false,
      cateDialogVisible: false,
      tableData: [],
      formData: {
        name: '',
        description: '',
        sort: ''
      },
      rules: {
        name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
      },
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
     * 关闭添加/编辑框
     */
    goBack(formName) {
      this.cateDialogVisible = false;
      this.resetForm(formName);
    },
    /**
     * 提交表单
     * @param {String} formName - 表单的ref属性值
     * @param {Boolean} st - 是否保存并继续
     */
    submitForm(formName, st) {
      // 表单验证
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 验证通过
          if (this.actionType === 'add') {
            // 添加操作
            // 添加成功后判断 st - 返回还是继续添加
            this.goBack();
          } else {
            // 修改操作
            // 修改成功后返回 - this.goBack()
            this.goBack();
          }
        } else {
          // 验证失败
        }
      });
    },
    /**
     * 重置表单
     */
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    /**
     * 添加分类
     */
    addCate() {
      this.actionType = 'add';
      this.dialogTitle = '新增分类';
      this.cateDialogVisible = true;
    },
    /**
     * 编辑分类信息
     */
    editMess(id) {
      // this.id = id;
      this.actionType = 'edit';
      this.dialogTitle = '编辑分类';
      this.cateDialogVisible = true;
    },
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
      getCatesApi().then(
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

<template>
  <div class="main-box add-box">
    <el-form :model="formData" :rules="rules" ref="form" label-width="188px" class="form">
      <el-form-item label="账号：" prop="username">
        <div class="username">
          <div class="prefix">{{ $store.state.businessInfo.prefix }}</div>
          <div class="h">-</div>
          <el-input v-model="formData.username" placeholder="请输入员工用户名" style="flex-grow: 1"></el-input>
          <el-popover
            class="username-tip"
            placement="bottom-start"
            width="210"
            trigger="hover"
            :content="'该员工账号为：' + $store.state.businessInfo.prefix + '-' + formData.username">
            <span class="input-tip" slot="reference">
              <i class="icon fbookfont ic-explain"></i>
            </span>
          </el-popover>
        </div>
      </el-form-item>
      <el-form-item label="员工密码：" prop="password">
        <el-input v-model="formData.password" placeholder="请输入员工密码" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="员工姓名：" prop="name">
        <el-input v-model="formData.name" placeholder="请输入员工姓名" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="手机号：" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入员工手机号" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="性别：">
        <el-radio-group v-model="formData.sex">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="身份证号：" prop="idNumber">
        <el-input
          v-model="formData.idNumber"
          placeholder="请输入员工身份证号"
          maxlength="20"
          style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="权限：" prop="competence">
        <el-select v-model="formData.competence" placeholder="请选择权限" :disabled="employeeId === id">
          <template v-for="item in power">
            <el-option
              v-if="item.value > employeeCom || employeeId === id"
              :key="item.value"
              :label="item.label"
              :value="item.value"></el-option>
          </template>
        </el-select>
      </el-form-item>
      <div class="btns">
        <el-form-item label-width="0">
          <el-button @click="goBack()">取消</el-button>
          <el-button type="primary" @click="submitForm('form', false)">保存</el-button>
          <el-button v-if="actionType == 'add'" type="primary" class="continue" @click="submitForm('form', true)"
            >保存并继续添加</el-button
          >
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import rules from '@/utils/validate';
import { addEmployeeApi, getEmployeeByIdApi, editEmployeeApi } from '@/api/employeeApi';

export default {
  data() {
    return {
      id: '',
      actionType: '',
      employeeCom: '',
      employeeId: '',
      storeId: '',
      formData: {
        username: '',
        password: '',
        name: '',
        phone: '',
        sex: 0,
        idNumber: '',
        competence: ''
      },
      rules: {
        username: [{ required: true, validator: rules.checkEmployeeUsername, trigger: 'blur' }],
        password: [{ required: true, message: '请输入员工密码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }],
        phone: [{ required: true, validator: rules.checkPhone, trigger: 'blur' }],
        idNumber: [{ required: true, validator: rules.checkIDNumber, trigger: 'blur' }],
        competence: [{ required: true, message: '请选择员工权限', trigger: 'blur' }]
      },
      power: [
        { label: '店长', value: 0 },
        { label: '管理员', value: 1 },
        { label: '员工', value: 2 }
      ]
    };
  },
  created() {
    // this.id = requestUrlParam('id');
    this.id = this.$route.query.id;
    this.actionType = this.id ? 'edit' : 'add';
    this.getCompetence();
    if (this.id) {
      this.init();
    }
  },
  methods: {
    /**
     * 初始化数据
     */
    init() {
      getEmployeeByIdApi(this.id).then(
        res => {
          if (res.data.flag) {
            // 业务请求成功
            this.formData = res.data.data;
            let username = res.data.data.username;
            this.formData.password = '******';
            this.formData.username = username.substring(username.indexOf('-') + 1);
          } else {
            this.$showMsgs('请求出错');
          }
        },
        err => {
          console.log('get err', err);
        }
      );
    },
    /**
     * 获取当前员工权限
     */
    getCompetence() {
      const employeeInfo = JSON.parse(window.localStorage.getItem('employeeInfo'));
      if (employeeInfo) {
        this.employeeCom = employeeInfo.competence;
        this.storeId = employeeInfo.storeId;
        this.employeeId = employeeInfo.id;
      }
    },
    /**
     * 返回上一级页面
     */
    goBack() {
      this.$router.back();
    },
    /**
     * 提交表单
     * @param {String} formName - form元素的ref属性值
     * @param {Boolean} st - 是否继续添加
     */
    submitForm(formName, st) {
      // 表单验证
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 验证通过
          if (this.actionType === 'add') {
            // 添加操作
            this.addEmployee(st);
            // 添加成功后判断 st - 返回还是继续添加
          } else {
            // 修改操作
            this.editEmployee();
          }
        }
      });
    },
    /**
     * 添加员工
     */
    addEmployee(st) {
      let employee = {};
      employee.storeId = this.storeId;
      employee.username = this.$store.state.businessInfo.prefix + '-' + this.formData.username;
      employee.password = this.$md5(this.formData.password);
      employee.name = this.formData.name;
      employee.phone = this.formData.phone;
      employee.sex = this.formData.sex;
      employee.idNumber = this.formData.idNumber;
      employee.competence = this.formData.competence;
      addEmployeeApi(employee).then(
        res => {
          // console.log(res);
          if (res.data) {
            if (res.data.flag) {
              // 业务成功
              if (!st) {
                // 保存
                this.$showMsg(res.data.msg, {
                  type: 'success',
                  closeFunc: () => {
                    this.goBack();
                  }
                });
              } else {
                // 保存并继续添加
                this.$showMsg(res.data.msg, { type: 'success' });
              }
            } else {
              // 业务出错
              this.$showMsgs(res.data.msg, { type: 'error' });
            }
          }
        },
        err => {
          console.log('addEmployeeApi err => ', err);
        }
      );
    },
    /**
     * 修改员工
     */
    editEmployee() {
      let employee = {};
      employee.competence = this.formData.competence;
      employee.createTime = this.formData.createTime;
      employee.createUser = this.formData.createUser;
      employee.email = this.formData.email;
      employee.id = this.formData.id;
      employee.idNumber = this.formData.idNumber;
      employee.name = this.formData.name;
      employee.phone = this.formData.phone;
      employee.sex = this.formData.sex;
      employee.status = this.formData.status;
      employee.storeId = this.formData.storeId;
      employee.updateTime = this.formData.updateTime;
      employee.updateUser = this.formData.updateUser;
      employee.username = this.$store.state.businessInfo.prefix + '-' + this.formData.username;
      employee.password = this.formData.password === '******' ? null : this.$md5(this.formData.password);
      editEmployeeApi(employee).then(
        res => {
          // console.log(res);
          if (res.data) {
            if (res.data.flag) {
              // 业务成功
              this.$showMsgs(res.data.msg, {
                type: 'success',
                duration: 800,
                closeFunc: () => {
                  this.goBack();
                }
              });
            } else {
              // 业务出错
              this.$showMsgs(res.data.msg, { type: 'error' });
            }
          }
        },
        err => {
          console.log('editEmployeeApi err => ', err);
        }
      );
    },
    /**
     * 封装$message
     * @param {String} msg 消息体
     * @param {String} type 消息类型 success/warning/info/error
     * @param {Boolean} isClose 是否可以关闭
     * @param {Function} closeFunc 关闭后执行函数
     * @param {Number} duration 存在时间
     */
    $showMsg(msg, { type = 'info', isClose = true, closeFunc = () => {}, duration = 1200 } = {}) {
      this.$message({
        message: msg,
        type: type,
        showClose: isClose,
        onClose: closeFunc,
        duration: duration
      });
    }
  }
};
</script>

<style lang="less" scoped>
.add-box {
  .username {
    position: relative;
    width: 300px;
    display: flex;
    align-items: center;
    .prefix {
      padding: 0 15px;
      color: var(--light-gray);
    }
    .h {
      padding: 0 15px 0 0;
      font-size: 18px;
      font-weight: bold;
      color: var(--light-gray);
    }
    .username-tip {
      position: absolute;
      top: 50%;
      right: -8px;
      transform: translate(100%, -50%);
      &:hover {
        color: var(--disable);
      }
    }
  }
}
</style>

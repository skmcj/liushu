<template>
  <div class="main-box add-box">
    <el-form :model="formData" :rules="rules" ref="form" label-width="188px" class="form">
      <el-form-item label="账号：" prop="username">
        <div class="random-username">
          <div class="text">{{ formData.username }}</div>
          <div class="change-one" @click="rotate" ref="changeBtn">
            <i class="icon fbookfont ic-change"></i>
            <span class="tip">换一个</span>
          </div>
        </div>
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
      <el-form-item label="身份证号：" prop="id_number">
        <el-input
          v-model="formData.id_number"
          placeholder="请输入员工身份证号"
          maxlength="20"
          style="width: 300px"></el-input>
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

export default {
  data() {
    return {
      id: '',
      actionType: '',
      formData: {
        username: 'abcdefghijk',
        name: '',
        phone: '',
        sex: 1,
        id_number: '',
        competence: ''
      },
      rules: {
        name: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }],
        phone: [{ required: true, validator: rules.checkPhone, trigger: 'blur' }],
        id_number: [{ required: true, validator: rules.checkIDNumber, trigger: 'blur' }]
      },
      // 旋转角
      deg: 0
    };
  },
  created() {
    // this.id = requestUrlParam('id');
    this.actionType = this.id ? 'edit' : 'add';
  },
  methods: {
    /**
     * 点击换一换按钮，旋转图标
     */
    rotate() {
      this.deg += 360;
      this.$refs.changeBtn.children[0].style.transform = `rotate(${this.deg}deg)`;
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
            // 添加成功后判断 st - 返回还是继续添加
          } else {
            // 修改操作
            // 修改成功后返回 - this.goBack()
          }
        } else {
          // 验证失败
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
.add-box {
  .el-form-item {
    margin-bottom: 24px;
    // 输入框样式
    &.is-error {
      :deep(.el-input) {
        .el-input__inner {
          border-color: var(--delete);
        }
      }
    }
    :deep(.el-form-item__label) {
      line-height: 42px;
    }
    :deep(.el-form-item__content) {
      line-height: 42px;
    }
    :deep(.el-input) {
      .el-input__inner {
        border-color: var(--regular-border);
        &:focus {
          border-color: var(--primary-border-f);
        }
      }
    }
    // 单选框样式
    :deep(.el-radio) {
      color: var(--regular-font);
      &.is-checked {
        .el-radio__label {
          color: var(--primary);
        }
      }
      .el-radio__input {
        width: 18px;
        height: 18px;
        border-radius: 100%;
        &.is-checked {
          .el-radio__inner {
            // background-color: var(--primary);
            background: none;
            &::before {
              width: 18px;
              height: 18px;
              transform: translate(-50%, -50%) scale(1);
            }
          }
        }
        .el-radio__inner {
          border: none;
          width: 18px;
          height: 18px;
          background-color: var(--regular-border);
          &::after {
            width: 10px;
            height: 10px;
            transform: translate(-50%, -50%) scale(1);
          }
          &::before {
            content: '';
            width: 18px;
            height: 18px;
            border-radius: 100%;
            background-color: var(--primary);
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%) scale(0);
            -webkit-transform: translate(-50%, -50%) scale(0);
            -webkit-transition: -webkit-transform 0.15s ease-in;
            transition: transform 0.15s ease-in;
            transition: -webkit-transform 0.15s ease-in;
            transition: transform 0.15s ease-in, -webkit-transform 0.15s ease-in;
          }
        }
      }
    }
  }
  :deep(.el-form-item__label) {
    color: var(--regular-font);
    font-size: 16px;
    padding: 0;
    padding-right: 8px;
  }
  .random-username {
    display: flex;
    align-items: center;
    .text {
      color: var(--light-gray);
      margin-right: 36px;
    }
    .change-one {
      color: var(--secondary-font);
      user-select: none;
      cursor: pointer;
      &:active {
        transform: scale(0.95) translateZ(0);
      }
      .icon {
        display: inline-block;
        margin-right: 5px;
        transition: transform 0.5s ease;
      }
    }
  }
  .btns {
    display: flex;
    justify-content: center;
    border-top: solid 1px var(--primary-border);
    .el-form-item {
      margin-top: 32px;
      .el-button {
        padding: 14px 22px;
        border-radius: 5px;
        & + .el-button {
          margin-left: 12px;
        }
        &:focus,
        &:hover {
          color: var(--primary);
          border-color: #c1eee1;
          background-color: #e6f5f6;
        }
      }
      .el-button--primary {
        background-color: var(--primary-btn);
        border-color: var(--primary-btn);
        &:focus,
        &:hover {
          color: var(--white);
          background-color: var(--primary-btn-h);
          border-color: var(--primary-btn-h);
        }
      }
      .continue {
        color: var(--white);
        background-color: var(--continue-btn);
        border-color: var(--continue-btn);
        &:focus {
          color: var(--white);
          background-color: var(--continue-btn);
          border-color: var(--continue-btn);
        }
        &:hover {
          color: var(--white);
          background-color: var(--continue-btn-h);
          border-color: var(--continue-btn-h);
        }
        &:active {
          background-color: var(--continue-btn-a);
          border-color: var(--continue-btn-a);
        }
      }
    }
  }
}
</style>

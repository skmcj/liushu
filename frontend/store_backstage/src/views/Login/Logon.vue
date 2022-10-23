<template>
  <div class="logon-box">
    <div class="logon-main" key="logon-box">
      <div class="title">
        <span class="text">商家入驻</span>
      </div>
      <div class="logo">
        <i class="icon fbookfont ic-logo"></i>
      </div>
      <el-form ref="logonForm" :model="logonForm" :rules="logonRules" hide-required-asterisk class="logon-form">
        <el-form-item prop="email">
          <el-input
            v-model="logonForm.email"
            prefix-icon="icon mini fbookfont ic-mess-1"
            placeholder="请输入邮箱账号"
            clearable></el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <el-input
            v-model="logonForm.captcha"
            prefix-icon="icon fbookfont ic-captcha"
            placeholder="请输入验证码"
            clearable
            style="width: 56.25%"></el-input>
          <el-button type="primary" class="code-btn" :disabled="isGetCaptcha" @click="getCaptcha">{{
            captchaBtnText
          }}</el-button>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="logonForm.password"
            show-password
            clearable
            prefix-icon="icon fbookfont ic-password"
            placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="repeat_password">
          <el-input
            v-model="logonForm.repeat_password"
            show-password
            clearable
            prefix-icon="icon fbookfont ic-password"
            placeholder="请再次输入密码"></el-input>
        </el-form-item>
        <div class="agreement-content">
          <el-checkbox v-model="isChecked"
            >我已阅读并同意<router-link to="/agreement" target="_blank" class="link">《用户服务协议》</router-link
            >和<router-link to="/privacy_policy" target="_blank" class="link">《隐私政策》</router-link></el-checkbox
          >
        </div>
        <div class="logon-btns">
          <el-button type="primary" :disabled="!isChecked" round class="logon-btn" @click="logon">注册</el-button>
        </div>
      </el-form>
      <div class="tip">
        <span class="text">已有账户，</span>
        <router-link class="link" to="/login">去登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import validater from '@/utils/validate';
import { sendValidateCodeApi } from '@/api/commonApi';
import { businessLogonApi } from '@/api/shopApi';

export default {
  data() {
    // 强密码正则匹配表达式，6个字符以上，且至少包含大写字母、小写字母、数字、特殊字符
    // const REGEX_PASS = /^\S*(?=\S{6,})(?=\S*\d)(?=\S*[A-Z])(?=\S*[a-z])(?=\S*[!@#$%^&*?,.:;`+-])\S*$/;
    // 为了方便测试输入，只用以下弱密码验证，8个及以上字符，包含字母及数字即可
    const REGEX_PASS = /^\S*(?=\S{8,})(?=\S*[a-zA-Z])(?=\S*\d)\S*$/;
    /**
     * 校验验证码
     */
    const validateCaptcha = (rule, value, callback) => {
      if (this.captchaBtnText === '获取验证码') {
        callback(new Error('请先获取验证码'));
      } else if (value === '') {
        callback(new Error('请输入验证码'));
      } else if (value.length !== 6) {
        callback(new Error('请输入6位验证码'));
      } else {
        callback();
      }
    };
    // 用于验证第一次密码输入
    const validatePassFirst = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (!REGEX_PASS.test(value)) {
        callback(new Error('密码必须为8位字符及以上，且需包含字母和数字'));
      } else {
        callback();
      }
    };
    // 用于验证第二次密码输入
    const validatePassSecond = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.logonForm.password) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    return {
      // 注册账户信息
      logonForm: {
        email: '',
        password: '',
        captcha: '',
        repeat_password: ''
      },
      logonRules: {
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        captcha: [{ required: true, validator: validateCaptcha, trigger: 'blur' }],
        password: [{ required: true, validator: validatePassFirst, trigger: 'blur' }],
        repeat_password: [{ required: true, validator: validatePassSecond, trigger: 'blur' }]
      },
      // 是否勾选协议
      isChecked: false,
      // 计时器
      timer: null,
      // 设置计时，单位：s
      counter: 60,
      // 验证码按钮是否不可用
      isGetCaptcha: false,
      captchaBtnText: '获取验证码'
    };
  },
  methods: {
    /**
     * 获取验证码
     */
    async getCaptcha() {
      const emailFlag = await this.validateField('logonForm', 'email');
      if (!emailFlag) {
        // 发送验证码
        let res = await sendValidateCodeApi('slo', this.logonForm.email);
        if (res.data.flag) {
          this.$message({
            showClose: true,
            message: '验证码已发送，请注意查收',
            type: 'success'
          });
          this.isGetCaptcha = true;
          this.authCountDown();
        } else {
          this.$message({
            showClose: true,
            message: '验证码获取失败，请稍候再试',
            type: 'warning'
          });
        }
      }
    },
    /**
     * 验证码发送后计时
     */
    authCountDown() {
      // 将setInterval()方法赋值给前面定义的timer计时器对象，方便后续操作
      this.timer = setInterval(() => {
        // 替换文本，秒实时改变
        this.captchaBtnText = `${this.counter}秒后重新获取`;
        this.counter--;
        if (this.counter < 0) {
          // 当计时小于零时，取消该计时器
          clearInterval(this.timer);
          this.authReset('重新获取');
        }
      }, 1000);
    },
    /**
     * 重置验证码相关参数
     */
    authReset(text = '获取验证码') {
      // 按钮可用
      this.isGetCaptcha = false;
      // 重置文本内容
      this.captchaBtnText = text;
      if (this.timer) {
        // 存在计时器对象，则清除
        clearInterval(this.timer);
        // 重置秒数，防止下次混乱
        this.counter = 60;
        // 计时器对象重置为空
        this.timer = null;
      }
    },
    /**
     * 注册
     */
    async logon() {
      const logonFlag = await this.validate('logonForm');
      if (logonFlag) {
        const logonRes = await businessLogonApi(
          this.logonForm.email,
          this.$md5(this.logonForm.password),
          this.logonForm.captcha
        );
        if (logonRes.data.flag) {
          // 注册成功
          this.$message({
            showClose: true,
            message: '注册成功，准备前往完善审核信息',
            type: 'success',
            onClose: () => {
              this.$router.replace('/auth');
            }
          });
        } else {
          // 注册失败
          if (logonRes.data.code === 21032) {
            // 邮箱已注册
            this.$message({
              showClose: true,
              message: '邮箱已被注册，请直接前往登录',
              type: 'warning'
            });
          } else {
            this.$message({
              showClose: true,
              message: logonRes.data.msg,
              type: 'error'
            });
          }
        }
      }
    },
    /**
     * 检查表单项
     * @param {String} formName - 表单的ref属性值
     * @param {String} filedName - 需检查的表单字段名
     */
    validateField(formName, fieldName) {
      return new Promise(resolve => {
        this.$refs[formName].validateField(fieldName, valid => {
          resolve(valid);
        });
      });
    },
    /**
     * 检查表单
     * @param {String} formName - 表单的ref属性值
     */
    validate(formName) {
      return new Promise(resolve => {
        this.$refs[formName].validate(valid => {
          resolve(valid);
        });
      });
    },
    /**
     * 返回登录
     */
    returnLogin() {
      if (this.isSendAuthSuccess) {
        this.$router.replace('/login');
      } else {
        this.authBoxVisible = true;
        this.active = 0;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.logon-box {
  background-color: #333333;
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  :deep(.el-button) {
    background-color: var(--primary-btn);
    border-color: var(--primary-btn);
    &:hover {
      background-color: var(--primary-btn-h);
      border-color: var(--primary-btn-h);
    }
    &:active {
      background-color: var(--primary-btn-a);
      border-color: var(--primary-btn-a);
    }
    &.is-disabled {
      &,
      &:active,
      &:focus,
      &:hover {
        background-color: var(--primary-btn-d);
        border-color: var(--primary-btn-d);
      }
    }
  }
  .logon-main {
    display: flex;
    flex-direction: column;
    align-items: center;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 32%;
    height: 85%;
    min-width: 520px;
    min-height: 600px;
    background-color: var(--white);
    border-radius: 12px;
    // 标题样式
    .title {
      user-select: none;
      width: 100%;
      box-sizing: border-box;
      padding: 16px 12px;
      .text {
        font-size: 18px;
        font-weight: bold;
        color: var(--primary-font);
      }
    }
    .logo {
      margin-top: 26px;
      margin-bottom: 36px;
      .icon {
        font-size: 75px;
      }
    }
    .logon-form {
      width: 64%;
      .el-form-item {
        margin-bottom: 24px;
        &.is-error {
          :deep(.el-input) {
            .el-input__prefix {
              .icon {
                color: var(--delete);
              }
            }
            .el-input__inner {
              border-color: var(--delete);
              &:hover {
                border-color: var(--delete);
              }
            }
          }
        }
        :deep(.el-input) {
          .el-input__prefix {
            left: 0;
            width: 36px;
            height: 100%;
            .icon {
              &.mini {
                font-size: 14px;
              }
              font-size: 20px;
              color: #bac0cd;
            }
          }
          .el-input__inner {
            padding-left: 36px;
            &:focus {
              border-color: var(--primary);
              & + .el-input__prefix {
                .icon {
                  color: var(--primary);
                }
              }
            }
          }
        }
        .code-btn {
          width: 38.75%;
          height: 40px;
          padding: 0;
          line-height: 40px;
          text-align: center;
          position: absolute;
          top: 0;
          right: 0;
          background-color: var(--primary);
          border-color: var(--primary);
          box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
          &:hover {
            background-color: var(--primary-btn-h);
            border-color: var(--primary-btn-h);
          }
          &:active {
            background-color: var(--primary-btn-a);
            border-color: var(--primary-btn-a);
            box-shadow: none;
          }
          &.is-disabled {
            &,
            &:active,
            &:focus,
            &:hover {
              background-color: var(--primary-btn-d);
              border-color: var(--primary-btn-d);
              box-shadow: none;
            }
          }
        }
      }
      .agreement-content {
        width: 100%;
        height: 24px;
        margin-bottom: 24px;
        :deep(.el-checkbox) {
          .el-checkbox__label {
            font-size: 12px;
            color: var(--main-font);
          }
        }
        :deep(.el-checkbox__input) {
          &.is-checked {
            & + .el-checkbox__label {
              color: var(--main-font);
            }
            .el-checkbox__inner {
              background-color: var(--primary);
              border-color: var(--primary);
            }
          }
        }
        .link {
          color: #53b2f4;
          text-decoration: none;
        }
      }
      .logon-btns {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        .logon-btn {
          width: 66%;
        }
      }
    }
    .tip {
      user-select: none;
      position: absolute;
      bottom: 12px;
      left: 12px;
      font-size: 10px;
      color: var(--main-font);
      .link {
        cursor: pointer;
        color: #6078ea;
        text-decoration: none;
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
}
</style>

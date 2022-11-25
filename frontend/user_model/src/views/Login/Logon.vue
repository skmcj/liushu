<template>
  <div class="logon-box">
    <div class="logon-main">
      <div class="logo">
        <i class="ic-logo"></i>
      </div>
      <!-- 注册表单 -->
      <el-form :model="logonForm" :rules="logonRules" ref="logonForm" class="logon-form">
        <el-form-item prop="username">
          <el-input
            v-model="logonForm.username"
            prefix-icon="ic-mine-2"
            placeholder="请输入用户名(可由 字母、数字、-、_ 组成)"
            @input="changeInputUsername"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="logonForm.email" prefix-icon="ic-mess-1" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="captcha" class="row">
          <el-input
            v-model="logonForm.captcha"
            prefix-icon="ic-captcha"
            placeholder="请输入验证码"
            @input="changeInputCaptcha"></el-input>
          <el-button type="primary" :disabled="captchaBtnFlag" class="captcha" @click.stop="getCaptcha">{{
            captchaBtnText
          }}</el-button>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="logonForm.password" prefix-icon="ic-password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="secondPass">
          <el-input v-model="logonForm.secondPass" prefix-icon="ic-password" placeholder="请再次输入密码"></el-input>
        </el-form-item>
        <el-checkbox v-model="isCheck"
          >注册即代表你同意<span class="link" @click.stop="handleAgreement('agreement')">《用户服务协议》</span>和<span
            class="link"
            @click.stop="handleAgreement('privacyPolicy')"
            >《隐私政策》</span
          ></el-checkbox
        >
        <div class="btns">
          <el-button type="primary" @click.stop="handleLogon">注 册</el-button>
        </div>
        <div class="tips">
          <span>已有账户？</span>
          <span class="link" @click.stop="handleLogin">去登录</span>
        </div>
      </el-form>
    </div>
    <Footer />
  </div>
</template>

<script>
import Footer from '@/components/Footer/Footer';
import { REGEX } from '@/utils/commonEnums';
import { sendValidateCodeApi } from '@/api/commonApi';

export default {
  components: {
    Footer
  },
  data() {
    // 验证规则
    // 校验验证码
    const validateCaptcha = (rule, value, callback) => {
      if (this.captchaBtnText === '获取验证码') {
        callback(new Error('请先获取验证码'));
      } else if (value === '') {
        callback(new Error('请输入验证码'));
      } else if (value.length !== 6) {
        callback(new Error('请输入6位验证码'));
      } else if (this.captchaErrorFlag) {
        callback(new Error(this.captchaErrorTip));
      } else {
        callback();
      }
    };
    // 用户名
    const validateUser = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'));
      } else if (value.length < 6 || value.length > 25) {
        callback(new Error('用户名长度需保持6-25位'));
      } else if (!REGEX.USERNAME.test(value)) {
        callback(new Error('用户名仅能由字母、数字、- 、_ 组成'));
      } else if (this.userErrorFlag) {
        // 其它规则，动态变化
        callback(new Error(this.userErrorTip));
      } else {
        callback();
      }
    };
    // 密码
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (value.length < 8) {
        callback(new Error('密码不能低于8位'));
      } else if (value.length > 25) {
        callback(new Error('密码不能高于25位'));
      } else if (!REGEX.PASS.test(value)) {
        callback(new Error('密码至少需包含一位字母及数字'));
      } else {
        callback();
      }
    };
    // 第二次密码
    const validatePassAgain = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (this.password === this.secondPass) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    return {
      logonForm: {
        username: '',
        email: '',
        password: '',
        secondPass: '',
        captcha: ''
      },
      logonRules: {
        username: [{ required: true, validator: validateUser, trigger: 'blur' }],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }
        ],
        password: [{ required: true, validator: validatePassword, trigger: 'blur' }],
        secondPass: [{ required: true, validator: validatePassAgain, trigger: 'blur' }],
        captcha: [{ required: true, validator: validateCaptcha, trigger: 'blur' }]
      },
      // 是否勾选协议
      isCheck: false,
      /** 验证码相关 */
      captchaBtnText: '获取验证码',
      captchaErrorFlag: false,
      captchaErrorTip: '验证码校验失败',
      // 验证码按钮是否不可用
      captchaBtnFlag: false,
      // 计时器
      timer: null,
      // 设置计时，单位：s
      counter: 60,
      // 用户名输入自定义指示
      userErrorFlag: false,
      userErrorTip: '用户名已存在'
    };
  },
  methods: {
    /**
     * 注册
     */
    handleLogon() {
      if (!this.isCheck) {
        this.$showMsg('请先勾选底下协议', { type: 'warning' });
        return;
      }
      // 校验表单
      this.$refs.logonForm.validate(valid => {
        // 验证用户名是否可用
        // 校验验证码是否正确
        if (valid) {
          // 校验通过，继续
          // 如果都对发起请求注册
        }
      });
      // 验证用户名是否可用
      let userFlag = this.testUsername();
      // 校验验证码是否正确
      let captchaFlag = this.testCaptcha();
      // 如果都对发起请求注册
    },
    /**
     * 登录
     */
    handleLogin() {
      this.$router.replace('/login');
    },
    /**
     * 获取验证码
     */
    async getCaptcha() {
      const emailFlag = await this.validateField('logonForm', 'email');
      if (!emailFlag) {
        // 发送验证码
        // sendValidateCodeApi('slo', this.logonForm.email).then(res => {
        //   if (res.data.flag) {
        //     this.$showMsg('验证码已发送，请注意查收', { type: 'success' });
        //     this.captchaBtnFlag = true;
        //     this.timeCountDown();
        //   } else {
        //     this.$showMsg('验证码获取失败，请稍候再试', { type: 'warning' });
        //   }
        // });
        // 测试
        this.captchaBtnFlag = true;
        this.timeCountDown();
      }
    },
    /**
     * 验证码发送后计时
     */
    timeCountDown() {
      // 将setInterval()方法赋值给前面定义的timer计时器对象，方便后续操作
      this.timer = setInterval(() => {
        // 替换文本，秒实时改变
        this.captchaBtnText = `${this.counter}秒后可重新获取`;
        this.counter--;
        if (this.counter < 0) {
          // 当计时小于零时，取消该计时器
          clearInterval(this.timer);
          this.resetTimer('重新获取');
        }
      }, 1000);
    },
    /**
     * 重置验证码相关参数
     */
    resetTimer(text = '获取验证码') {
      // 按钮可用
      this.captchaBtnFlag = false;
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
     * 校验验证码
     */
    testCaptcha() {
      // 发送请求
      // 测试
      let flag = true;
      if (this.logonForm.captcha !== '123456') {
        this.captchaErrorFlag = true;
        // 触发字段校验
        this.validateField('logonForm', 'captcha');
      }
      return flag;
    },
    /**
     * 校验用户名是否可用
     */
    testUsername() {
      // 发送请求
      // 测试
      let flag = true;
      if (this.logonForm.username === 'zhangsan') {
        // 用户名存在
        this.userErrorFlag = true;
        this.validateField('logonForm', 'username');
      }
      return flag;
    },
    /**
     * 改变用户名输入
     */
    changeInputUsername() {
      if (this.userErrorFlag) {
        this.userErrorFlag = false;
        this.validateField('logonForm', 'username');
      }
    },
    /**
     * 改变验证码输入
     */
    changeInputCaptcha() {
      if (this.captchaErrorFlag) {
        this.captchaErrorFlag = false;
        this.validateField('logonForm', 'captcha');
      }
    },
    /**
     * 点击下方协议
     */
    handleAgreement(text) {
      if (text === 'agreement') {
        console.log('点击用户服务协议');
      } else if (text === 'privacyPolicy') {
        console.log('点击隐私协议');
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
    }
  }
};
</script>

<style lang="less" scoped>
.logon-box {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  .logon-main {
    margin-top: 96px;
    margin-bottom: 180px;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 50%;
    .logo {
      i {
        font-size: 96px;
        color: var(--primary);
      }
    }
    .logon-form {
      margin-top: 42px;
      width: 368px;
      .el-form-item {
        &.is-error {
          :deep(.el-input__inner) {
            & + .el-input__prefix,
            &:focus + .el-input__prefix {
              i {
                color: var(--danger);
              }
            }
          }
        }
        &.row {
          :deep(.el-form-item__content) {
            display: flex;
            align-items: center;
            justify-content: space-between;
            .el-button {
              min-width: 42%;
              margin-left: 24px;
              &.captcha {
                font-size: 12px;
              }
            }
          }
        }
      }
      :deep(.el-input) {
        .el-input__prefix {
          i {
            transition: color 0.5s ease-in-out;
            font-size: 16px;
            &.ic-mess-1 {
              font-size: 10px;
            }
          }
        }
        .el-input__inner {
          &:focus + .el-input__prefix {
            i {
              color: var(--primary);
            }
          }
        }
      }
      .tips {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        margin-bottom: 12px;
        font-size: 14px;
        color: #c3c3c3;
        .link {
          cursor: pointer;
          user-select: none;
          font-size: 14px;
          color: #96a6f1;
        }
      }
      .btns {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 32px;
        .el-button {
          width: 72%;
        }
      }
      :deep(.el-checkbox) {
        margin-bottom: 12px;
        .el-checkbox__label {
          color: #999;
        }
      }
      .link {
        cursor: pointer;
        user-select: none;
        font-size: 14px;
        color: var(--link-text);
      }
    }
  }
}
</style>

<template>
  <div class="login-box">
    <div class="login-main">
      <div class="logo">
        <i class="ic-logo"></i>
      </div>
      <el-form :model="loginForm" :rules="loginRules" ref="loginForm" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="ic-mine-2" placeholder="请输入用户名/邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="ic-password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <div class="tips">
          <span class="link" @click.stop="handleForget">忘记密码？</span>
        </div>
        <!-- 按钮 -->
        <div class="btns">
          <el-button type="primary" @click.stop="handleLogin">登 录</el-button>
          <el-button plain @click.stop="handleLogon">注 册</el-button>
        </div>
        <el-checkbox v-model="isCheck"
          >登录即代表你同意<span class="link" @click.stop="handleAgreement('agreement')">《用户服务协议》</span>和<span
            class="link"
            @click.stop="handleAgreement('privacyPolicy')"
            >《隐私政策》</span
          ></el-checkbox
        >
      </el-form>
    </div>
    <Footer />
  </div>
</template>

<script>
import Footer from '@/components/Footer/Footer';
import { REGEX } from '@/utils/commonEnums';

export default {
  components: {
    Footer
  },
  data() {
    /** 用于验证用户名/邮箱输入
     * @param {String} value
     */
    const validateUser = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名/邮箱'));
      } else if (value.includes('@')) {
        // 想要输入邮箱
        if (!REGEX.EMAIL.test(value)) {
          callback(new Error('请输入正确的邮箱'));
        }
      } else if (!REGEX.USERNAME.test(value)) {
        // 想要输入用户名
        callback(new Error('请输入正确的用户名'));
      } else {
        callback();
      }
    };
    // 用于验证密码输入
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (!REGEX.PASS.test(value)) {
        callback(new Error('请输入正确的密码'));
      } else {
        callback();
      }
    };
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, validator: validateUser, trigger: 'blur' }],
        password: [{ required: true, validator: validatePass, trigger: 'blur' }]
      },
      // 是否勾选协议
      isCheck: false
    };
  },
  methods: {
    /**
     * 登录
     */
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          if (this.isCheck) {
            this.$showMsg('登录成功', { type: 'success' });
          } else {
            this.$showMsg('请先勾选下方协议', { type: 'warning' });
          }
        }
      });
    },
    /**
     * 注册
     */
    handleLogon() {
      this.$router.push('/logon');
    },
    /**
     * 忘记密码
     */
    handleForget() {
      this.$showMsg('后续支持，抱歉~');
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
    }
  }
};
</script>

<style lang="less" scoped>
.login-box {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  .login-main {
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
    .login-form {
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
      }
      :deep(.el-input) {
        .el-input__prefix {
          i {
            transition: color 0.5s ease-in-out;
            font-size: 16px;
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
        .link {
          cursor: pointer;
          user-select: none;
          font-size: 14px;
          color: var(--link-text);
        }
      }
      .btns {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 32px;
        .el-button {
          width: 164px;
        }
      }
      :deep(.el-checkbox) {
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

<template>
  <div class="login-box">
    <div class="login-main">
      <!-- 左边展示图 -->
      <div class="cover">
        <img src="/static/images/backend_login_cover.jpg" alt="cover" />
      </div>
      <!-- 右边主要信息 -->
      <div class="login-content">
        <!-- 主要表单信息 -->
        <div class="form-box">
          <!-- logo -->
          <div class="logo">
            <i class="icon fbookfont ic-logo"></i>
          </div>
          <!-- 登录信息表单 -->
          <el-form ref="form" :model="loginForm" :rules="rules" hide-required-asterisk class="login-form">
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                prefix-icon="icon fbookfont ic-mine-2"
                placeholder="手机号/邮箱号/用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                show-password
                prefix-icon="icon fbookfont ic-password"
                placeholder="密码"></el-input>
            </el-form-item>
            <el-button round class="login-btn" @click="login">登录</el-button>
          </el-form>
        </div>
        <!-- 底下文字链接 -->
        <div class="tip">
          <span class="text">如果您还没有账户，可以点击</span>
          <span class="tip-link" @click="gotoLogon">去开店入驻</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入手机号/邮箱/用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    };
  },
  methods: {
    login() {
      this.$refs.form.validate(valid => {
        if (valid) {
          window.localStorage.setItem('lsBusinessInfo', 'zhangsan');
          this.$router.push('/');
        }
      });
    },
    gotoLogon() {
      this.$router.push('/logon');
    }
  }
};
</script>

<style lang="less" scoped>
.login-box {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: #333333;
  .login-main {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 56%;
    height: 45%;
    min-width: 810px;
    min-height: 420px;
    background-color: #ffffff;
    border-radius: 12px;
    overflow: hidden;
    display: flex;
    align-items: center;
    .cover {
      width: 60%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      img {
        object-fit: cover;
        width: 100%;
        height: 100%;
      }
    }
    .login-content {
      width: 40%;
      height: 100%;
      position: relative;
      display: flex;
      align-items: center;
      justify-content: center;
      .form-box {
        width: 56%;
        display: flex;
        flex-direction: column;
        align-items: center;
        .logo {
          .icon {
            font-size: 81px;
            line-height: 85px;
          }
        }
        .login-form {
          margin-top: 28px;
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
                left: 7px;
                .icon {
                  color: #bac0cd;
                }
              }
              .el-input__inner {
                border: none;
                border-radius: 0;
                border-bottom: solid 1px #e9e9e8;
                &:hover {
                  border-color: #c0c4cc;
                }
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
          }
          .login-btn {
            margin-top: 12px;
            width: 100%;
            background-color: var(--primary);
            border: none;
            color: var(--white);
            &:hover {
              background-color: var(--primary-btn-h);
            }
            &:active {
              background-color: var(--primary-btn-a);
            }
          }
        }
      }
      .tip {
        user-select: none;
        position: absolute;
        bottom: 12px;
        left: 8px;
        font-size: 12px;
        color: #8d8d8d;
        .tip-link {
          cursor: pointer;
          color: #6078ea;
          &:hover {
            text-decoration: underline;
          }
        }
      }
    }
  }
}
</style>

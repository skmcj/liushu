<template>
  <div class="login-box">
    <div class="login-main">
      <!-- 左边展示图 -->
      <div class="cover">
        <img :src="baseUrl + '/api/img/com-backend-page.jpg'" alt="cover" />
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
import { loginEmployeeApi } from '@/api/shopApi';

export default {
  data() {
    return {
      baseUrl: this.$staticUrl,
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
  created() {
    // this.$router.push('/auth');
  },
  methods: {
    async login() {
      const flag = await this.validate('form');
      // 如果以提交审核信息，且审核通过，直接登录，进入后台
      if (flag) {
        // 表单验证通过
        let res = await loginEmployeeApi(this.loginForm.username, this.$md5(this.loginForm.password));
        console.log('res =>', res);
        if (res.status === 200) {
          if (res.data.code === 21041) {
            // 登录成功
            window.localStorage.setItem('businessToken', res.data.data.token);
            this.$showMsg('登录成功', 'success', {
              closeFunc: () => {
                this.$router.replace('/');
              }
            });
          } else if (res.data.code === 21052) {
            // 前往完善审核信息
            this.$showMsg('当前店铺审核资料尚未完善，准备前往完善', 'warning', {
              closeFunc: () => {
                this.$router.push('/auth');
              }
            });
          } else if (res.data.code === 21050) {
            // 审核不通过
            this.$showMsg(res.data.msg, 'error');
          } else {
            // 其它情况
            this.$showMsg(res.data.msg, 'warning');
          }
        } else {
          this.$showMsg('网络繁忙，请稍后再试', 'error');
        }
      }
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
     * 注册
     */
    gotoLogon() {
      this.$router.push('/logon');
    },
    /**
     * 展示信息
     */
    $showMsg(message, type = 'info', { close = true, closeFunc = () => {} } = {}) {
      this.$message({
        showClose: close,
        type: type,
        message: message,
        onClose: closeFunc
      });
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

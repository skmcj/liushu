<template>
  <!-- 我的-个人中心-账号安全 -->
  <div class="mine-center-acc">
    <div class="acc-box">
      <!-- 密码 -->
      <div class="acc-item">
        <div class="acc__label">账户密码：</div>
        <div class="acc__content">
          <div class="acc_text">********</div>
          <div class="acc_link" @click.stop="handleChange('pass')">更换密码</div>
        </div>
      </div>
      <!-- 邮箱 -->
      <div class="acc-item">
        <div class="acc__label">邮箱：</div>
        <div class="acc__content">
          <div class="acc_text">{{ packingEmail(email) }}</div>
          <div class="acc_link" @click.stop="handleChange('email')">换绑邮箱</div>
        </div>
      </div>
    </div>
    <!-- 换绑操作框 -->
    <el-dialog
      class="change-dialog"
      :title="dialogType === 'pass' ? '账号安全 › 更改密码' : '账号安全 › 换绑邮箱'"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      :modal-append-to-body="false">
      <div class="change-main">
        <Steps :active="active" title-algin="right" mode="elegant" style="width: 75%">
          <Step title="验证身份" />
          <Step :title="dialogType === 'pass' ? '输入新密码' : '输入新邮箱'" />
          <Step
            :title="dialogType === 'pass' ? '更改结果' : '换绑结果'"
            :status="active >= 2 ? (isEditSuccess ? 'success' : 'fail') : ''" />
        </Steps>
        <div class="change-content">
          <!-- 验证身份 -->
          <div v-if="active === 0" class="step step-one">
            <div class="now-email">
              <div class="tit">{{ '绑定邮箱' }}</div>
              <div class="text">{{ packingEmail(email) }}</div>
            </div>
            <div class="captcha">
              <el-input
                v-model="captcha"
                placeholder="请输入验证码"
                style="width: 62%"
                @input="resetCodeTip"
                @change="changeInput"
                :class="dialogCodeTip ? 'is-error' : ''"></el-input>
              <el-button class="cap-btn" plain :disabled="captchaBtnFlag" @click="getCaptcha(dialogType)">{{
                captchaBtnText
              }}</el-button>
              <div class="tip" :class="dialogCodeTip ? 'is-error' : ''">{{ dialogCodeTip }}</div>
            </div>
          </div>
          <div v-if="active === 1" class="step step-two">
            <!-- 密码 -->
            <template v-if="dialogType === 'pass'">
              <el-form
                :model="passForm"
                :rules="passRules"
                ref="passForm"
                class="pass-form"
                hide-required-asterisk
                style="width: 420px">
                <el-form-item prop="newPass">
                  <el-input type="password" v-model="passForm.newPass" placeholder="请输入新的密码"></el-input>
                </el-form-item>
                <el-form-item prop="againPass">
                  <el-input type="password" v-model="passForm.againPass" placeholder="请再次输入密码"></el-input>
                </el-form-item>
              </el-form>
            </template>
            <template v-if="dialogType === 'email'">
              <el-form
                :model="emailForm"
                :rules="emailRules"
                ref="emailForm"
                class="email-form"
                hide-required-asterisk
                style="width: 420px">
                <el-form-item prop="newEmail">
                  <el-input
                    v-model="emailForm.newEmail"
                    placeholder="请输入新的邮箱"
                    @change="changeInput('email')"></el-input>
                </el-form-item>
                <el-form-item prop="captcha">
                  <div class="captcha">
                    <el-input
                      v-model="emailForm.captcha"
                      placeholder="请输入验证码"
                      style="width: 62%"
                      @change="changeInput('two')"></el-input>
                    <el-button class="cap-btn" plain :disabled="captchaBtnFlag" @click="getCaptcha('newEmail')">{{
                      captchaBtnText
                    }}</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </template>
          </div>
          <!-- 结果 -->
          <div v-if="active >= 2" class="step step-three">
            <template v-if="isEditSuccess">
              <i class="icon ic-select-1"></i>
              <span>更换成功</span>
              <span>{{
                '现在，您可以使用新的' + (dialogType === 'pass' ? '密码' : '邮箱') + '进行登录了(*･ω&lt; )'
              }}</span>
            </template>
            <template v-else>
              <i class="icon ic-fail"></i>
              <span>更换失败</span>
              <span>{{ editFailText }}</span>
            </template>
          </div>
        </div>
        <div class="btns">
          <div class="btn" @click="nextStep">{{ dialogBtnText }}</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Steps from '@/components/Common/Steps/Steps';
import Step from '@/components/Common/Steps/Step';
import validator from '@/utils/validate';
import { getValidateCodeApi, checkValidateCodeApi, updateUserEmailApi, updateUserPassApi } from '@/api/userApi';

export default {
  components: {
    Steps,
    Step
  },
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
      } else if (this.captchaErrorFlag) {
        callback(new Error(this.captchaErrorTip));
      } else {
        callback();
      }
    };
    // 用于验证第一次密码输入
    const validatePassFirst = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新的密码'));
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
      } else if (value !== this.passForm.newPass) {
        callback(new Error('两次输入密码不一致'));
      } else {
        callback();
      }
    };
    return {
      password: '',
      email: '',
      // 密码表单
      passForm: {
        newPass: '',
        againPass: ''
      },
      passRules: {
        newPass: [{ required: true, validator: validatePassFirst, trigger: 'blur' }],
        againPass: [{ required: true, validator: validatePassSecond, trigger: 'blur' }]
      },
      // 新邮箱表单
      emailForm: {
        newEmail: '',
        captcha: ''
      },
      emailRules: {
        newEmail: [
          { required: true, message: '请输入新的邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }
        ],
        captcha: [{ required: true, validator: validateCaptcha, trigger: 'blur' }]
      },
      // 换绑对话框
      dialogVisible: false,
      // 换绑类型
      dialogType: 'pass',
      // 步骤
      active: 0,
      // 是否换绑成功
      isEditSuccess: false,
      editFailText: '当前网络较为繁忙，请稍后再试',
      // 对话框按钮文本
      dialogBtnText: '下一步',
      // 验证码按钮的文本
      captchaBtnText: '获取验证码',
      // 是否已发送验证码
      captchaBtnFlag: false,
      // 验证码是否正确
      captchaErrorFlag: false,
      captchaErrorTip: '验证码校验失败',
      // 用户输入验证码
      captcha: '',
      // 用户输入验证码提示
      dialogCodeTip: '',
      // 计时器
      timer: null,
      // 设置计时，单位：s
      counter: 60
    };
  },
  created() {
    this.initMess();
  },
  methods: {
    /**
     * 初始化信息
     */
    initMess() {
      this.email = this.$store.state.userInfo.email;
    },
    /**
     * 包装邮箱账号显示
     * @param {String} email
     */
    packingEmail(email) {
      let list = email.split('@');
      let num = list[0].slice(0, 3) + '***' + list[0].slice(-3);
      return num + '@' + list[1];
    },
    /**
     * 点击更换信息
     */
    handleChange(text) {
      this.active = 0;
      this.dialogType = text;
      this.dialogBtnText = '下一步';
      this.dialogVisible = true;
      this.resetTimer();
    },
    /**
     * 下一步
     */
    async nextStep() {
      if (this.active === 0) {
        // 校验验证码
        let cData = await this.testCaptcha(this.captcha);
        if (!cData.flag) {
          // 验证码校验失败
          this.dialogCodeTip = cData.tip;
        } else {
          this.resetTimer();
          this.active += 1;
        }
      } else if (this.active === 1) {
        if (this.dialogType === 'email') {
          let flag1 = await this.validateField('emailForm', 'captcha');
          let flag2 = await this.validateField('emailForm', 'newEmail');
          // console.log('setp 2 => ', flag1, flag2, !flag1, !flag2);
          if (!flag1 && !flag2) {
            // 校验通过
            // 校验验证码
            let cData = await this.testCaptcha(this.emailForm.captcha);
            if (!cData.flag) {
              // 验证码校验失败
              this.captchaErrorFlag = true;
              this.captchaErrorTip = cData.tip;
              this.validateField('emailForm', 'captcha');
            } else {
              // 更改邮箱
              let userInfo = this.$store.state.userInfo;
              let user = {
                id: userInfo.id,
                username: userInfo.username,
                email: this.emailForm.newEmail
              };
              let { data } = await updateUserEmailApi(user);
              if (data.flag) {
                // 更改成功
                this.isEditSuccess = true;
                userInfo.email = this.emailForm.newEmail;
                this.email = this.emailForm.newEmail;
                this.$store.dispatch('setUserInfo', userInfo);
              } else {
                // 更改失败
                this.isEditSuccess = false;
              }
              this.resetTimer();
              this.active += 1;
              this.dialogBtnText = '返回';
            }
          }
        } else if (this.dialogType === 'pass') {
          this.$refs.passForm.validate(valid => {
            if (valid) {
              // 验证无误
              // 修改密码
              let userInfo = this.$store.state.userInfo;
              let user = {
                id: userInfo.id,
                email: userInfo.email,
                username: userInfo.username,
                password: this.$md5(this.passForm.newPass)
              };
              updateUserPassApi(user)
                .then(res => {
                  if (res.data.flag) {
                    // 修改成功
                    this.isEditSuccess = true;
                  } else {
                    this.isEditSuccess = false;
                  }
                })
                .finally(() => {
                  this.resetTimer();
                  this.active += 1;
                  this.dialogBtnText = '返回';
                });
            }
          });
        }
      } else {
        this.dialogVisible = false;
      }
      // this.active = this.active < 3 ? this.active + 1 : 0;
    },
    /**
     * 校验验证码
     */
    async testCaptcha(code) {
      let { data } = await checkValidateCodeApi(code);
      // console.log('test code =>', data);
      if (data.flag) {
        return { flag: true };
      } else {
        if (data.code === 21020) {
          return {
            flag: false,
            tip: '验证码错误'
          };
        }
        if (data.code === 21022) {
          return {
            flag: false,
            tip: '验证码已过期'
          };
        }
        return {
          flag: false,
          tip: '验证码校验失败'
        };
      }
    },
    /**
     * 获取验证码
     */
    async getCaptcha(authType = 'none') {
      if (authType === 'newEmail') {
        // 新邮箱
        let flag = await this.validateField('emailForm', 'newEmail');
        if (!flag) {
          this.sendCaptcha(this.emailForm.newEmail, authType);
        }
      } else {
        // 发送验证码 ···
        this.sendCaptcha(this.email, authType);
      }
    },
    /**
     * 发送验证码
     */
    sendCaptcha(email, type) {
      // 发送验证码
      getValidateCodeApi(email, type).then(res => {
        // console.log('code => ', res);
        if (res.data.flag) {
          this.$showMsg('验证码已发送，请注意查收', { type: 'success' });
          this.captchaBtnFlag = true;
          this.timeCountDown();
        } else {
          if (res.data.code === 52102) {
            this.$showMsg('该邮箱已被绑定，请选择其它邮箱', { type: 'warning' });
          } else {
            this.$showMsg('验证码获取失败，请稍候再试', { type: 'warning' });
          }
        }
      });
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
    resetCodeTip() {
      this.dialogCodeTip = '';
    },
    /**
     * 改变验证码输入
     */
    changeInput(step = 'one') {
      if (step === 'two' && this.captchaErrorFlag) {
        this.captchaErrorFlag = false;
        this.validateField('emailForm', 'captcha');
      } else if (step === 'one') {
        this.dialogCodeTip = '';
      } else if (step === 'email') {
        this.resetTimer();
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
.mine-center-acc {
  width: 100%;
  margin-top: 32px;
  box-sizing: border-box;
  padding: 0 36px;
  padding-bottom: 120px;
  display: flex;
  flex-direction: column;
  .acc-box {
    width: 100%;
    display: flex;
    flex-direction: column;
    .acc-item {
      display: flex;
      align-items: center;
      .acc__label {
        margin-right: 12px;
        width: 128px;
        height: 40px;
        line-height: 40px;
        font-size: 16px;
        color: #717171;
        text-align: right;
      }
      .acc__content {
        display: flex;
        align-items: center;
        .acc_text {
          min-width: 150px;
          user-select: none;
          font-size: 14px;
          color: #898989;
          & + .acc_link {
            margin-left: 12px;
          }
        }
        .acc_link {
          cursor: pointer;
          user-select: none;
          font-size: 12px;
          color: #4994c4;
          transition: all 0.5s ease-in-out;
          &:hover {
            opacity: 0.8;
          }
        }
      }
    }
  }
  .change-dialog {
    .change-main {
      display: flex;
      flex-direction: column;
      align-items: center;
    }
    .change-content {
      .step {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-top: 56px;
        margin-bottom: 36px;
        :deep(.el-input) {
          &.is-error {
            .el-input__inner {
              border-color: var(--error);
            }
          }
        }
        .now-email {
          position: relative;
          user-select: none;
          color: #c0c4cc;
          display: flex;
          align-items: center;
          width: 420px;
          height: 42px;
          margin-bottom: 24px;
          box-sizing: border-box;
          border: solid 1px #d8d8d8;
          border-radius: 5px;
          background-color: #f3f4f7;
          .tit {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100%;
            box-sizing: border-box;
            padding: 0 32px;
            color: #999;
            border-right: solid 1px #d8d8d8;
          }
          .text {
            margin-left: 18px;
          }
        }
        .captcha {
          position: relative;
          display: flex;
          align-items: center;
          justify-content: space-between;
          width: 420px;
          height: 42px;
          .cap-btn {
            //   display: flex;
            //   align-items: center;
            //   justify-content: center;
            //   color: var(--main-font);
            width: 35%;
          }
          .tip {
            &.is-error {
              color: var(--error);
            }
          }
        }
        .new-email {
          position: relative;
          display: flex;
          align-items: center;
          justify-content: center;
          width: 420px;
          height: 42px;
          margin-bottom: 24px;
        }
        &.step-three {
          .icon {
            color: var(--primary);
            font-size: 64px;
            margin-bottom: 8px;
            &.ic-fail {
              color: var(--error);
            }
          }
          & > span {
            color: #a4a4a4;
            font-size: 16px;
            margin-bottom: 12px;
          }
        }
        .tip {
          font-size: 12px;
          position: absolute;
          line-height: 18px;
          height: 18px;
          left: 0;
          bottom: -18px;
          margin-left: 12px;
          color: var(--delete);
        }
      }
    }
    .btns {
      margin-top: 48px;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      .btn {
        cursor: pointer;
        user-select: none;
        box-sizing: border-box;
        padding: 0 48px;
        font-size: 16px;
        color: #fff;
        height: 40px;
        line-height: 40px;
        border-radius: 20px;
        background-color: var(--primary-btn);
        transition: all 0.5s ease-in-out;
        &:hover {
          background-color: var(--primary-btn-h);
        }
        &:active {
          background-color: var(--primary-btn-a);
        }
      }
    }
  }
}
</style>

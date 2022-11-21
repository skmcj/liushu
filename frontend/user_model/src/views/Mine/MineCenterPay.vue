<template>
  <div class="mine-center-pay">
    <div class="pay-list">
      <div class="pay-item" @click.stop="handleDia">
        <span>设置支付密码</span>
        <i class="ic-more-1"></i>
      </div>
    </div>
    <!-- 换绑操作框 -->
    <el-dialog
      class="change-dialog"
      title="设置支付密码"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      :modal-append-to-body="false">
      <div class="change-main">
        <Steps :active="active" title-algin="right" mode="elegant" style="width: 75%">
          <Step title="验证身份" />
          <Step title="设置支付密码" />
          <Step title="设置结果" :status="active >= 2 ? (isEditSuccess ? 'success' : 'fail') : ''" />
        </Steps>
        <div class="change-content">
          <!-- 验证身份 -->
          <div v-if="active === 0" class="step">
            <!-- 如果是第一次设置支付密码，先通过邮箱验证身份 -->
            <template v-if="!isSetPayPass">
              <div class="now-email">
                <div class="tit">{{ '绑定邮箱' }}</div>
                <div class="text">{{ packingEmail(email) }}</div>
              </div>
              <div class="captcha">
                <el-input
                  v-model="authCode"
                  placeholder="请输入验证码"
                  style="width: 62%"
                  @input="resetCodeTip"
                  :class="dialogCodeTip ? 'is-error' : ''"></el-input>
                <el-button class="cap-btn" plain :disabled="isGetCaptcha" @click="getCaptcha()">{{
                  codeBtnText
                }}</el-button>
                <div class="tip">{{ dialogCodeTip }}</div>
              </div>
            </template>
            <!-- 如果设置过支付密码，需输入之前的支付密码验证身份 -->
            <PayInput v-if="isSetPayPass" v-model="payPass" tip="请输入 6 位数字的旧支付密码" />
          </div>
          <div v-if="active === 1" class="step">
            <!-- 密码 -->
            <PayInput v-model="payPass" tip="请输入 6 位数字的新支付密码" />
          </div>
          <!-- 结果 -->
          <div v-if="active >= 2" class="step step-three">
            <template v-if="isEditSuccess">
              <i class="icon ic-select-1"></i>
              <span>更换成功</span>
              <span>现在，您可以使用新的支付密码进行支付了(*･ω&lt; )</span>
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
import PayInput from '@/components/Common/Input/PayInput';

export default {
  components: {
    Steps,
    Step,
    PayInput
  },
  data() {
    return {
      // 是否设置过支付密码
      isSetPayPass: false,
      payPass: '',
      email: '4567891231@qq.com',
      // 换绑对话框
      dialogVisible: false,
      // 步骤
      active: 0,
      // 是否换绑成功
      isEditSuccess: false,
      editFailText: '当前网络较为繁忙，请稍后再试',
      dialogBtnText: '下一步',
      // 验证码按钮的文本
      codeBtnText: '获取验证码',
      // 是否已发送验证码
      isGetCaptcha: false,
      // 用户输入验证码
      authCode: '',
      // 用户输入验证码提示
      dialogCodeTip: '',
      // 计时器
      timer: null,
      // 设置计时，单位：s
      counter: 60
    };
  },
  methods: {
    /**
     * 包装邮箱账号显示
     * @param {String} email
     */
    packingEmail(email) {
      let list = email.split('@');
      let num = list[0].slice(0, 3) + '***' + list[0].slice(-3);
      return num + '@' + list[1];
    },
    handleDia() {
      this.dialogVisible = true;
      this.initChangeMess();
    },
    nextStep() {
      if (this.active === 0) {
        // 验证身份
        // isSetPayPass
        if (this.isSetPayPass) {
          // 验证旧的支付密码是否正确
        } else {
          // 验证邮箱验证码是否正确
        }
        // 都正确进入下一步
        this.active += 1;
      } else if (this.active === 1) {
        // 设置支付密码
        this.active += 1;
        this.dialogBtnText = '返回';
      } else if (this.active >= 2) {
        this.dialogVisible = false;
      }
    },
    /**
     * 初始化信息
     */
    initChangeMess() {
      this.payPass = '';
      this.active = 0;
      this.dialogBtnText = '下一步';
      this.isGetCaptcha = false;
      this.resetChangeMess();
    },
    /**
     * 重置信息
     */
    resetChangeMess() {
      this.authCode = '';
      this.dialogCodeTip = '';
      this.authReset();
    },
    /**
     * 获取验证码
     */
    async getCaptcha() {
      // 发送验证码 ···
      // let res = await sendValidateCodeApi('sce', this.email);
      // if (res.data.flag) {
      //   this.$showMsg('验证码已发送，请注意查收', { type: 'success' });
      //   // 禁用按钮
      //   this.isGetCaptcha = true;
      //   // 开始60s倒计时
      //   this.authCountDown();
      // } else {
      //   this.$showMsg('验证码获取失败，请稍候再试', { type: 'warning' });
      // }
      // 测试
      // 禁用按钮
      this.isGetCaptcha = true;
      // 开始60s倒计时
      this.authCountDown();
    },
    /**
     * 验证码发送后计时
     */
    authCountDown() {
      // console.log('counter');
      // 将setInterval()方法赋值给前面定义的timer计时器对象，方便后续操作
      this.timer = setInterval(() => {
        // 替换文本，秒实时改变
        this.codeBtnText = `${this.counter}秒后可重新获取`;
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
      this.codeBtnText = text;
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
    }
  }
};
</script>

<style lang="less" scoped>
.mine-center-pay {
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding: 0 36px;
  margin-top: 32px;
  padding-bottom: 120px;
  .pay-list {
    width: 64%;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    .pay-item {
      cursor: pointer;
      user-select: none;
      box-sizing: border-box;
      padding: 16px;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 42px;
      border-radius: 8px;
      background-color: #fff;
      transition: all 0.5s ease-in-out;
      box-shadow: 0px 1px 2px 0px rgba(0, 0, 0, 0.25);
      i {
        transition: color 0.5s ease-in-out;
      }
      &:hover {
        i {
          color: var(--primary);
        }
      }
      &:active {
        box-shadow: inset 0px 1px 2px 0px rgba(0, 0, 0, 0.25);
      }
      & + .pay-item {
        margin-top: 12px;
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
      }
      .pay-box {
        display: flex;
        align-items: center;
        .pay-item {
          width: 36px;
          height: 42px;
          border-radius: 5px;
          border: 1px solid #dcdfe6;
          & + .pay-item {
            margin-left: 5px;
          }
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

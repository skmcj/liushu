<template>
  <div class="logon-box">
    <transition-group name="box-fade" mode="out-in">
      <div v-if="!isLogon" class="logon-main" key="logon-box">
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
      <!-- 认证信息 -->
      <div class="auth-box" v-if="authBoxVisible && isLogon" key="auth-box">
        <!-- 步骤条 -->
        <div class="step-box">
          <Steps :active="active" title-algin="right" mode="elegant" style="width: 75%" solid>
            <Step title="店铺信息" />
            <Step title="资质信息" />
            <Step title="法定代表人" />
          </Steps>
        </div>
        <div class="scroll-box">
          <!-- 店铺信息 -->
          <el-form
            v-if="active === 0"
            :model="baseForm"
            :rules="baseRules"
            ref="baseForm"
            label-width="100px"
            class="base-form">
            <!-- 店铺名称 -->
            <el-form-item label="店铺名称：" prop="name" key="base-form-name">
              <el-input v-model="baseForm.name" placeholder="请输入店铺名称"></el-input>
            </el-form-item>
            <!-- 门脸图片 -->
            <el-form-item
              label="门脸图片："
              prop="cover"
              key="base-form-cover"
              class="upload-item"
              style="width: 488px">
              <el-upload
                class="cover-uploader"
                drag
                action="#"
                :disabled="baseForm.cover != ''"
                :show-file-list="false"
                :on-success="(response, file) => uploadImageSuccess(response, file, { formName: 'base' })"
                :on-error="(err, file) => uploadImageError(err, file, { formName: 'base' })"
                :before-upload="beforeImageUpload">
                <div v-if="baseForm.cover" class="cover">
                  <img :src="baseForm.cover" @click.stop="handlePreviewImage(baseForm.cover)" />
                  <span class="hover">
                    <i class="icon fbookfont ic-delete" @click.stop="handelDeleteImage({ formName: 'base' })"></i>
                  </span>
                </div>
                <i v-else class="icon fbookfont ic-add"></i>
              </el-upload>
              <!-- 上传图片右侧提示 -->
              <el-popover
                class="cover-tip"
                placement="right-end"
                width="200"
                trigger="hover"
                content="拖拽或点击左侧方框上传图片">
                <span slot="reference" class="tip">
                  <i class="icon fbookfont ic-explain"></i>
                </span>
              </el-popover>
            </el-form-item>
            <!-- 店铺地址 -->
            <el-form-item label="店铺地址：" prop="address" key="base-form-address">
              <el-input v-model="baseForm.address" placeholder="请输入店铺地址"></el-input>
            </el-form-item>
            <!-- 营业时间 -->
            <el-form-item label="营业时间：" prop="business_hours" key="base-form-business_hours">
              <el-time-picker
                is-range
                v-model="baseForm.business_hours"
                format="HH:mm"
                value-format="HH:mm"
                range-separator="-"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                placeholder="选择时间范围"
                style="width: 100%">
              </el-time-picker>
            </el-form-item>
            <!-- 配送服务 -->
            <el-form-item label="配送服务：" prop="distribution" key="base-form-distribution">
              <el-radio-group v-model="baseForm.distribution">
                <el-radio :label="0">由 商家 配送</el-radio>
              </el-radio-group>
            </el-form-item>
            <!-- 配送费 -->
            <el-form-item label="配送费：" prop="deliver_fee" key="base-form-deliver_fee">
              <el-input-number
                v-model="baseForm.deliver_fee"
                :precision="2"
                :min="0"
                class="deliver-fee"
                style="width: 45%"></el-input-number>
              <span class="unit">元/单</span>
            </el-form-item>
          </el-form>
          <!-- 资质信息 -->
          <el-form
            v-if="active === 1"
            :model="qfForm"
            :rules="qfRules"
            ref="qfForm"
            label-width="150px"
            class="qf-form">
            <el-form-item label="营业执照：" prop="business_license">
              <el-upload
                action="#"
                drag
                list-type="picture-card"
                :file-list="qfForm.business_license"
                :on-success="
                  (response, file) =>
                    uploadImageSuccess(response, file, { formName: 'qf', itemName: 'business_license' })
                "
                :on-error="(err, file) => uploadImageError(err, file, { formName: 'qf', itemName: 'business_license' })"
                :before-upload="beforeImageUpload"
                class="cover-uploader">
                <i slot="default" class="icon fbookfont ic-add"></i>
                <div slot="file" slot-scope="{ file }" class="thumbnail-view">
                  <img class="upload-img-view" :src="file.url" alt="" />
                  <span class="upload-tools">
                    <span class="tool preview-tool" @click="handlePreviewImage(file.url)">
                      <i class="icon fbookfont ic-preview"></i>
                    </span>
                    <span
                      class="tool delete-pop"
                      @click="handelDeleteImage(file, { formName: 'qf', itemName: 'business_license' })">
                      <i class="icon fbookfont ic-delete"></i>
                    </span>
                  </span>
                </div>
              </el-upload>
            </el-form-item>
            <el-form-item label="出版物经营许可证：" prop="license_img">
              <el-upload
                action="#"
                drag
                list-type="picture-card"
                :file-list="qfForm.license_img"
                :on-success="
                  (response, file) => uploadImageSuccess(response, file, { formName: 'qf', itemName: 'license_img' })
                "
                :on-error="(err, file) => uploadImageError(err, file, { formName: 'qf', itemName: 'license_img' })"
                :before-upload="beforeImageUpload"
                class="cover-uploader">
                <i slot="default" class="icon fbookfont ic-add"></i>
                <div slot="file" slot-scope="{ file }" class="thumbnail-view">
                  <img class="upload-img-view" :src="file.url" alt="" />
                  <span class="upload-tools">
                    <span class="tool preview-tool" @click="handlePreviewImage(file.url)">
                      <i class="icon fbookfont ic-preview"></i>
                    </span>
                    <span
                      class="tool delete-pop"
                      @click="handelDeleteImage(file, { formName: 'qf', itemName: 'license_img' })">
                      <i class="icon fbookfont ic-delete"></i>
                    </span>
                  </span>
                </div>
              </el-upload>
            </el-form-item>
            <el-form-item label="店内环境：" prop="env_imgs">
              <el-upload
                action="#"
                drag
                list-type="picture-card"
                :file-list="qfForm.env_imgs"
                :on-success="
                  (response, file) => uploadImageSuccess(response, file, { formName: 'qf', itemName: 'env_imgs' })
                "
                :on-error="(err, file) => uploadImageError(err, file, { formName: 'qf', itemName: 'env_imgs' })"
                :before-upload="beforeImageUpload"
                class="cover-uploader">
                <i slot="default" class="icon fbookfont ic-add"></i>
                <div slot="file" slot-scope="{ file }" class="thumbnail-view">
                  <img class="upload-img-view" :src="file.url" alt="" />
                  <span class="upload-tools">
                    <span class="tool preview-tool" @click="handlePreviewImage(file.url)">
                      <i class="icon fbookfont ic-preview"></i>
                    </span>
                    <span
                      class="tool delete-pop"
                      @click="handelDeleteImage(file, { formName: 'qf', itemName: 'env_imgs' })">
                      <i class="icon fbookfont ic-delete"></i>
                    </span>
                  </span>
                </div>
              </el-upload>
            </el-form-item>
          </el-form>
          <!-- 法人信息 -->
          <el-form
            v-if="active === 2"
            :model="legalForm"
            :rules="legalRules"
            ref="legalForm"
            label-width="150px"
            class="legal-form">
            <el-form-item label="法人姓名：" prop="owner_name">
              <el-input v-model="legalForm.owner_name" placeholder="请输入法人姓名"></el-input>
            </el-form-item>
            <el-form-item label="法人手机号：" prop="phone">
              <el-input v-model="legalForm.phone" placeholder="请输入法人手机号"></el-input>
            </el-form-item>
            <el-form-item label="法人身份证号：" prop="id_number">
              <el-input v-model="legalForm.id_number" placeholder="请输入法人身份证号"></el-input>
            </el-form-item>
          </el-form>
          <Loading v-if="active === 3" tip="提交中，请耐心等待！" />
          <!-- 下一步按钮 -->
          <div class="auth-btns" v-if="active <= 2">
            <el-button v-if="active >= 1 && active <= 2" type="primary" round class="pre-btn" @click="preStep"
              >上一步</el-button
            >
            <el-button v-if="active <= 2" type="primary" round class="next-btn" @click="nextStep">{{
              active === 2 ? '确认提交' : '下一步'
            }}</el-button>
          </div>
        </div>
      </div>
      <!-- 表单提交结果 -->
      <div class="result-box" v-if="!authBoxVisible" key="result-box">
        <div class="result-icon">
          <i class="icon fbookfont ic-select-1" v-if="isSendAuthSuccess"></i>
          <i class="icon fbookfont ic-fail" v-if="!isSendAuthSuccess"></i>
        </div>
        <div class="result-mess" v-if="isSendAuthSuccess">您的店铺资料已全部提交，正在等待平台审核</div>
        <div class="result-mess" v-if="!isSendAuthSuccess">您的店铺资料提交失败，请返回重试</div>
        <div class="result-tip">
          <div class="result-tip-icon">
            <i class="icon fbookfont ic-info"></i>
          </div>
          <div class="result-tip-text" v-if="isSendAuthSuccess">
            审核时间大概为1-3个工作日，结果将会以邮件形式发送到联系人邮箱里，请耐心等待结果
          </div>
          <div class="result-tip-text" v-if="!isSendAuthSuccess">
            当前网络可能较为拥堵，请检查您的网络情况后，返回重试！
          </div>
        </div>
        <el-button type="primary" round class="return-btn" @click="returnLogin">返回</el-button>
      </div>
    </transition-group>
    <!-- 图片放大图 -->
    <el-dialog :visible.sync="previewImageVisible" append-to-body>
      <img width="100%" :src="previewImageUrl" alt="" />
    </el-dialog>
  </div>
</template>

<script>
import Steps from '@/components/Common/Steps/Steps';
import Step from '@/components/Common/Steps/Step';
import Loading from '@/components/Common/Loading/Loading';
import validater from '@/utils/validate';

export default {
  components: {
    Loading,
    Step,
    Steps
  },
  data() {
    // 强密码正则匹配表达式，6个字符以上，且至少包含大写字母、小写字母、数字、特殊字符
    // const REGEX_PASS = /^\S*(?=\S{6,})(?=\S*\d)(?=\S*[A-Z])(?=\S*[a-z])(?=\S*[!@#$%^&*?,.:;`+-])\S*$/;
    // 为了方便测试输入，只用以下弱密码验证，8个及以上字符，包含字母及数字即可
    const REGEX_PASS = /^\S*(?=\S{8,})(?=\S*[a-zA-Z])(?=\S*\d)\S*$/;
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
        password: [{ required: true, validator: validatePassFirst, trigger: 'blur' }],
        repeat_password: [{ required: true, validator: validatePassSecond, trigger: 'blur' }]
      },
      // 店铺基本信息
      baseForm: {
        name: '',
        cover: '',
        address: '',
        business_hours: '',
        distribution: 0,
        deliver_fee: 0
      },
      baseRules: {
        name: [{ required: true, message: '请输入店铺名称', trigger: 'blur' }],
        cover: [{ required: true, message: '请选择门脸图片', trigger: 'blur' }],
        address: [{ required: true, message: '请输入店铺地址', trigger: 'blur' }],
        business_hours: [{ required: true, message: '请输入营业时间', trigger: 'blur' }],
        distribution: [{ required: true, message: '请选择配送服务', trigger: 'blur' }],
        deliver_fee: [{ required: true, message: '请输入配送费', trigger: 'blur' }]
      },
      // 资质信息
      qfForm: {
        business_license: [],
        license_img: [],
        license: [],
        env_imgs: []
      },
      qfRules: {
        business_license: [{ required: true, message: '请上传店铺营业执照', trigger: 'blur' }],
        license_img: [{ required: true, message: '请上传店铺的出版物经营许可证', trigger: 'blur' }],
        env_imgs: [{ required: true, message: '请上传店铺环境图片', trigger: 'blur' }]
      },
      // 法人信息
      legalForm: {
        owner_name: '',
        phone: '',
        id_number: ''
      },
      legalRules: {
        owner_name: [{ required: true, message: '请输入法人姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入法人手机号', trigger: 'blur' }],
        id_number: [{ required: true, message: '请输入法人身份证号', trigger: 'blur' }]
      },
      // 是否注册
      isLogon: false,
      // 是否勾选协议
      isChecked: false,
      // 计时器
      timer: null,
      // 设置计时，单位：s
      counter: 60,
      // 验证码按钮是否不可用
      isGetCaptcha: false,
      captchaBtnText: '获取验证码',
      active: 0,
      // 已上传封面的预览图
      previewImageUrl: '',
      // 是否显示已上传封面的预览图
      previewImageVisible: false,
      authBoxVisible: true,
      isSendAuthSuccess: false
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
        this.isGetCaptcha = true;
        this.authCountDown();
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
    logon() {
      this.isLogon = true;
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
     * 图片上传成功后的操作
     */
    uploadImageSuccess(res, file, formMess) {
      if (formMess.formName === 'base') {
        this.baseForm.cover = URL.createObjectURL(file.raw);
        let flag = this.validateField('baseForm', 'cover');
      }
    },
    uploadImageError(res, file, formMess) {
      this.$message.error('网络繁忙，请稍后再试！');
      // TODO: 后续删除
      if (formMess.formName === 'base') {
        this.baseForm.cover = URL.createObjectURL(file.raw);
        let flag = this.validateField('baseForm', 'cover');
      } else if (formMess.formName === 'qf') {
        this.qfForm[formMess.itemName].push({
          url: URL.createObjectURL(file.raw)
        });
        let flag = this.validateField('qfForm', formMess.itemName);
      }
    },
    /**
     * 图片上传前的判断处理
     */
    beforeImageUpload(file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!isJpgOrPng) {
        this.$message.error('上传封面图片只能是 JPG/PNG 格式!');
      }
      if (!isLt5M) {
        this.$message.error('上传封面图片大小不能超过 5MB!');
      }
      return isJpgOrPng && isLt5M;
    },
    /**
     * 删除图片
     */
    handelDeleteImage(file, formMess) {
      if (formMess.formName === 'base') {
        // 操作表单为baseForm
        this.baseForm.cover = '';
      } else if (formMess.formName === 'qf') {
        // 操作表单qfForm
        this.qfForm[formMess.itemName] = this.qfForm[formMess.itemName].filter(item => {
          return item.uid !== file.uid;
        });
      }
    },
    /**
     * 显示图片
     */
    handlePreviewImage(file) {
      this.previewImageUrl = file;
      this.previewImageVisible = true;
    },
    /**
     * 上一步
     */
    preStep() {
      if (this.active > 0) {
        this.active--;
      }
    },
    /**
     * 下一步
     */
    nextStep() {
      if (this.active === 0) {
        this.$refs.baseForm.validate(valid => {
          if (valid) {
            this.active += 1;
          }
        });
        console.log('baseForm', this.baseForm);
      } else if (this.active === 1) {
        this.$refs.qfForm.validate(valid => {
          if (valid) {
            this.active += 1;
          }
        });
        console.log('qfForm', this.qfForm);
      } else if (this.active === 2) {
        this.$refs.legalForm.validate(valid => {
          if (valid) {
            this.active += 1;
            // 提交表单
            setTimeout(() => {
              this.authBoxVisible = false;
            }, 10000);
          }
        });
        console.log('legalForm', this.legalForm);
      }
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
  .auth-box {
    width: 56%;
    min-width: 840px;
    height: 76%;
    min-height: 512px;
    background-color: #fff;
    border-radius: 12px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-sizing: border-box;
    padding: 0 24px;
    display: flex;
    flex-direction: column;
    align-items: center;
    .step-box {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      height: 85px;
      border-bottom: solid 1px var(--routine-border);
    }
    .scroll-box {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: 72%;
      overflow-y: scroll;
      scrollbar-color: transparent transparent;
      scrollbar-track-color: transparent;
      -ms-scrollbar-track-color: transparent;
      &::-webkit-scrollbar {
        display: none;
      }
    }
    .el-form {
      position: relative;
      .el-form-item {
        margin-bottom: 24px;
        .unit {
          &.en {
            font-family: 'Consolas';
            font-size: 16px;
          }
          user-select: none;
          display: inline-block;
          font-size: 14px;
          color: var(--regular-font);
          padding: 0 8px;
          box-sizing: border-box;
        }
        :deep(.el-input) {
          &.is-active {
            .el-input__inner {
              border-color: var(--primary-border-f);
            }
          }
          .el-input__inner {
            &:focus {
              border-color: var(--primary-border-f);
            }
          }
        }
        .cover-uploader {
          :deep(.el-upload),
          :deep(.el-upload--picture-card) {
            width: 200px;
            height: 160px;
            border: none;
            .el-upload-dragger {
              position: relative;
              display: flex;
              align-items: center;
              justify-content: center;
              width: 200px;
              height: 160px;
              &:hover {
                border-color: var(--primary-border-f);
                .icon {
                  color: var(--primary);
                }
              }
              .icon {
                transition: all 500ms ease-in-out;
                font-size: 48px;
                color: #d9d9d9;
              }
              .cover {
                width: 100%;
                height: 100%;
                img {
                  width: 100%;
                  height: 100%;
                }
                &:hover {
                  .hover {
                    opacity: 1;
                  }
                }
                .hover {
                  position: absolute;
                  opacity: 0;
                  bottom: 0;
                  left: 0;
                  width: 100%;
                  height: 25px;
                  text-align: center;
                  line-height: 25px;
                  background-color: rgba(0, 0, 0, 0.3);
                  cursor: default;
                  transition: opacity 0.3s;
                  .icon {
                    color: #fff;
                    width: 25px;
                    height: 25px;
                    line-height: 25px;
                    font-size: 16px;
                    cursor: pointer;
                  }
                }
              }
            }
          }
        }
        &.upload-item {
          :deep(.el-form-item__content) {
            display: flex;
            .cover-tip {
              margin: 0 12px;
              position: relative;
              .tip {
                font-size: 14px;
                line-height: 16px;
                color: var(--tip-icon);
                position: absolute;
                bottom: 8px;
                left: 0;
                transition: all 500ms ease-in-out;
                &:hover {
                  color: var(--disable);
                }
              }
            }
          }
        }
        :deep(.el-upload-list--picture-card) {
          .el-upload-list__item {
            width: 200px;
            height: 160px;
            .thumbnail-view {
              width: 100%;
              height: 100%;
              position: relative;
              .upload-img-view {
                width: 100%;
                height: 100%;
                object-fit: cover;
              }
              &:hover {
                .upload-tools {
                  opacity: 1;
                }
              }
              .upload-tools {
                opacity: 0;
                transition: opacity 500ms ease-in-out;
                display: flex;
                align-items: center;
                justify-content: center;
                width: 100%;
                height: 25px;
                background-color: rgba(0, 0, 0, 0.45);
                position: absolute;
                bottom: 0;
                left: 0;
                text-align: center;
                .tool {
                  display: inline-block;
                  width: 25px;
                  height: 25px;
                  line-height: 25px;
                  text-align: center;
                  margin: 0 12px;
                  color: var(--white);
                  cursor: pointer;
                  &:hover {
                    color: var(--primary);
                  }
                  &.preview-tool {
                    &:hover {
                      color: #68be8d;
                    }
                  }
                  &.delete-tool {
                    &:hover {
                      color: var(--delete);
                    }
                  }
                }
              }
            }
          }
        }
        :deep(.el-form-item__label) {
          color: #818693;
        }
        :deep(.el-range-editor) {
          &.is-active {
            &,
            &:hover {
              border-color: var(--primary-border-f);
            }
          }
        }
        :deep(.deliver-fee) {
          .el-input-number__decrease,
          .el-input-number__increase {
            display: none;
          }
          .el-input__inner {
            padding: 0 15px;
            text-align: left;
          }
        }
        :deep(.el-radio) {
          display: flex;
          align-items: center;
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
        &.is-error {
          :deep(.el-upload-dragger) {
            border-color: var(--delete);
          }
        }
      }
    }
    .base-form,
    .qf-form,
    .legal-form {
      min-width: 50%;
      max-width: 100%;
      margin-top: 56px;
    }
    .base-form {
      width: 50%;
    }
    .auth-btns {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 50%;
      margin-top: 18px;
      margin-bottom: 12px;
      .next-btn {
        width: 64%;
        margin: 0 auto;
      }
      .pre-btn {
        width: 45%;
        & + .next-btn {
          width: 45%;
          margin: 0;
        }
      }
    }
    .sk-loading {
      :deep(.sk-loading-book-tip) {
        margin-top: 24px;
      }
    }
  }
  .result-box {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 37.5%;
    min-width: 640px;
    height: 38px;
    min-height: 360px;
    border-radius: 12px;
    background-color: #ffffff;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    box-sizing: border-box;
    .result-icon {
      .icon {
        font-size: 72px;
        color: var(--primary);
        &.ic-fail {
          color: var(--delete);
        }
      }
    }
    .result-mess {
      margin-top: 24px;
      margin-bottom: 12px;
      font-size: 24px;
      color: var(--primary-font);
    }
    .result-tip {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 72%;
      .result-tip-icon {
        margin-right: 12px;
        .icon {
          font-size: 18px;
          color: var(--secondary-font);
        }
      }
      .result-tip-text {
        font-size: 14px;
        line-height: 24px;
        color: var(--secondary-font);
      }
    }
    .return-btn {
      width: 180px;
      margin-top: 24px;
    }
  }
  .box-fade-enter-active,
  .box-fade-leave-active {
    transition: opacity 0.8s ease;
  }

  .box-fade-enter,
  .box-fade-leave-to {
    opacity: 0;
  }
}
</style>

<template>
  <div class="main-box add-box">
    <el-form :model="formData" :rules="rules" ref="form" label-width="188px" class="form">
      <div class="block-content">
        <div class="form-line">
          <el-form-item label="法人姓名：" prop="ownerName">
            <div class="mess-label">
              <span class="text">{{ formData.ownerName }}</span>
            </div>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="法人身份证号：" prop="idNumber">
            <div class="mess-label">
              <span class="text">{{ formData.idNumber }}</span>
            </div>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="法人联系电话：" prop="phone">
            <div class="mess-label">
              <span class="text">{{ formData.phone }}</span>
              <span class="btn" @click="initAuthMess('phone')">换绑电话</span>
            </div>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="法人联系邮箱：" prop="email">
            <div class="mess-label">
              <span class="text">{{ formData.email }}</span>
              <span class="btn" @click="initAuthMess('email')">换绑邮箱</span>
            </div>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="营业执照：" prop="businessLicense">
            <el-upload
              action=""
              :http-request="uploadImagePlusApi"
              :data="{ type: 'sta' }"
              drag
              list-type="picture-card"
              :file-list="formData.businessLicense"
              :on-success="(response, file) => uploadImageSuccess(response, file, 'businessLicense')"
              :on-error="(err, file) => uploadImageError(err, file, 'businessLicense')"
              :before-upload="beforeImageUpload">
              <i slot="default" class="icon fbookfont ic-add"></i>
              <div slot="file" slot-scope="{ file }" class="thumbnail-view">
                <img class="upload-img-view" :src="file.url" alt="" />
                <span class="upload-tools">
                  <span class="tool preview-tool" @click="previewImage(file)">
                    <i class="ic-preview"></i>
                  </span>
                  <span class="tool delete-pop" @click="deleteImage(file, 'businessLicense')">
                    <i class="ic-delete"></i>
                  </span>
                </span>
              </div>
            </el-upload>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="出版物经营许可证：" prop="licenseImg">
            <el-upload
              action=""
              :http-request="uploadImagePlusApi"
              :data="{ type: 'sta' }"
              drag
              list-type="picture-card"
              :file-list="formData.licenseImg"
              :on-success="(response, file) => uploadImageSuccess(response, file, 'licenseImg')"
              :on-error="(err, file) => uploadImageError(err, file, 'licenseImg')"
              :before-upload="beforeImageUpload">
              <i slot="default" class="icon fbookfont ic-add"></i>
              <div slot="file" slot-scope="{ file }" class="thumbnail-view">
                <img class="upload-img-view" :src="file.url" alt="" />
                <span class="upload-tools">
                  <span class="tool preview-tool" @click="previewImage(file)">
                    <i class="ic-preview"></i>
                  </span>
                  <span class="tool delete-pop" @click="deleteImage(file, 'licenseImg')">
                    <i class="ic-delete"></i>
                  </span>
                </span>
              </div>
            </el-upload>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="店内环境：" prop="envImgs">
            <el-upload
              action=""
              :http-request="uploadImagePlusApi"
              :data="{ type: 'sta' }"
              drag
              list-type="picture-card"
              :file-list="formData.envImgs"
              :on-success="(response, file) => uploadImageSuccess(response, file, 'envImgs')"
              :on-error="(err, file) => uploadImageError(err, file, 'envImgs')"
              :before-upload="beforeImageUpload">
              <i slot="default" class="icon fbookfont ic-add"></i>
              <div slot="file" slot-scope="{ file }" class="thumbnail-view">
                <img class="upload-img-view" :src="file.url" alt="" />
                <span class="upload-tools">
                  <span class="tool preview-tool" @click="previewImage(file)">
                    <i class="ic-preview"></i>
                  </span>
                  <span class="tool delete-pop" @click="deleteImage(file, 'envImgs')">
                    <i class="ic-delete"></i>
                  </span>
                </span>
              </div>
            </el-upload>
          </el-form-item>
        </div>
        <!-- 换绑操作框 -->
        <el-dialog
          :title="dialogAuthType === 'phone' ? '更换手机' : '更换邮箱'"
          :visible.sync="dialogAuthVisible"
          :close-on-click-modal="false">
          <div class="change-auth">
            <Steps :active="active" title-algin="right" mode="elegant" style="width: 75%">
              <Step title="验证身份" />
              <Step :title="dialogAuthType === 'phone' ? '更换新手机' : '更换新邮箱'" />
              <Step title="更换结果" :status="isEditSuccess ? '' : active >= 2 ? 'fail' : ''" />
            </Steps>
            <div class="change-content">
              <div v-if="active === 0" class="step step-one">
                <div class="now-email">
                  <div class="tit">{{ '绑定' + (dialogAuthType === 'phone' ? '手机' : '邮箱') }}</div>
                  <div class="text">{{ dialogAuthType === 'phone' ? formData.phone : formData.email }}</div>
                </div>
                <div class="captcha">
                  <el-input
                    v-model="authCode"
                    placeholder="请输入验证码"
                    style="width: 62%"
                    @input="resetAuthCodeTip"
                    :class="dialogAuthCodeTip ? 'is-error' : ''"></el-input>
                  <el-button class="cap-btn" plain :disabled="isGetCaptcha" @click="getCaptcha()">{{
                    authCodeBtnText
                  }}</el-button>
                  <div class="tip">{{ dialogAuthCodeTip }}</div>
                </div>
              </div>
              <div v-if="active === 1" class="step step-two">
                <div class="new-email">
                  <el-input
                    v-model="authMessInput"
                    :placeholder="'请输入新的' + (dialogAuthType === 'phone' ? '手机号' : '邮箱')"
                    :class="dialogAuthTip ? 'is-error' : ''"
                    style="width: 100%"
                    @input="resetAuthTip"></el-input>
                  <div class="tip">{{ dialogAuthTip }}</div>
                </div>
                <div class="captcha">
                  <el-input
                    v-model="authCode"
                    placeholder="请输入验证码"
                    @input="resetAuthCodeTip"
                    style="width: 62%"
                    :class="dialogAuthCodeTip ? 'is-error' : ''"></el-input>
                  <el-button class="cap-btn" plain :disabled="isGetCaptcha" @click="getCaptcha(dialogAuthType)">{{
                    authCodeBtnText
                  }}</el-button>
                  <div class="tip">{{ dialogAuthCodeTip }}</div>
                </div>
              </div>
              <div v-if="active >= 2" class="step step-three">
                <template v-if="isEditSuccess">
                  <i class="icon fbookfont ic-select-1"></i>
                  <span>更换成功</span>
                  <span>{{
                    '现在，您可以使用新的' + (dialogAuthType === 'phone' ? '手机号' : '邮箱') + '进行登录了(*･ω&lt; )'
                  }}</span>
                </template>
                <template v-else>
                  <i class="icon fbookfont ic-fail"></i>
                  <span>更换失败</span>
                  <span>{{ editFailText }}</span>
                </template>
              </div>
            </div>
            <div class="btn next-btn" @click="nextStep">{{ dialogAuthBtnText }}</div>
          </div>
        </el-dialog>
        <!-- 图片放大图 -->
        <el-dialog :visible.sync="dialogImageVisible">
          <img width="100%" :src="dialogImageUrl" alt="" />
        </el-dialog>
        <div class="btns">
          <el-form-item label-width="0">
            <el-button type="primary" @click="submitForm('form')">保存</el-button>
            <el-button @click="goBack">取消</el-button>
          </el-form-item>
        </div>
      </div>
    </el-form>
  </div>
</template>

<script>
import Steps from '@/components/Common/Steps/Steps';
import Step from '@/components/Common/Steps/Step';
import validator from '@/utils/validate';
import { getStoreInfoApi, editStoreEmailApi, editStoreAuthApi } from '@/api/shopApi';
import { uploadImagePlusApi, sendValidateCodeApi, checkValidateCodeApi } from '@/api/commonApi';

export default {
  components: {
    Steps,
    Step
  },
  data() {
    return {
      formData: {
        ownerName: '',
        idNumber: '',
        phone: '',
        email: '',
        businessLicense: [],
        licenseImg: [],
        envImgs: []
      },
      rules: {
        businessLicense: [{ required: true, message: '请上传营业执照', trigger: 'blur' }],
        licenseImg: [{ required: true, message: '请上传出版物经营许可证', trigger: 'blur' }],
        envImgs: [{ required: true, message: '请上传相关店内环境图', trigger: 'blur' }]
      },
      dialogImageVisible: false,
      dialogImageUrl: '',
      deletePopVisible: false,
      // 换绑对话框
      dialogAuthVisible: false,
      // 是否换绑成功
      isEditSuccess: false,
      editFailText: '当前网络较为繁忙，请稍后再试',
      // 步骤
      active: 0,
      // 换绑对话框标题
      dialogAuthType: 'phone',
      // 对话框按钮文本
      dialogAuthBtnText: '下一步',
      // 验证码按钮的文本
      authCodeBtnText: '获取验证码',
      // 是否已发送验证码
      isGetCaptcha: false,
      // 用户输入验证码
      authCode: '',
      // 用户输入邮箱/手机号
      authMessInput: '',
      // 后端返回验证码
      // targetAuthCode: '',
      // 用户输入提示
      dialogAuthTip: '',
      // 用户输入验证码提示
      dialogAuthCodeTip: '',
      // 计时器
      timer: null,
      // 设置计时，单位：s
      counter: 60
    };
  },
  created() {
    this.getStoreInfo();
  },
  methods: {
    uploadImagePlusApi,
    /**
     * 获取店铺信息
     */
    getStoreInfo() {
      let employeeInfo = JSON.parse(window.localStorage.getItem('employeeInfo'));
      if (employeeInfo) {
        this.storeId = employeeInfo.storeId;
      }
      getStoreInfoApi(this.storeId).then(
        res => {
          if (res.data.flag) {
            this.formData = res.data.data.bookstoreDetail;
            this.formData.email = res.data.data.bookstore.email;
            this.formData.businessLicense = this.getUploadImgObj(
              this.formData.businessLicenseList,
              this.formData.businessLicenseUrl
            );
            this.formData.licenseImg = this.getUploadImgObj(this.formData.licenseImgList, this.formData.licenseImgUrl);
            this.formData.envImgs = this.getUploadImgObj(this.formData.envImgsList, this.formData.envImgsUrl);
            // console.log('mess =>', this.formData);
          } else {
            this.$showMsgs('店铺认证资料获取失败', { type: 'error' });
          }
        },
        err => {
          console.log('get store err =>', err);
        }
      );
    },
    /**
     * 返回上一级
     */
    goBack() {
      this.$router.back();
    },
    /**
     * 提交表单
     * @param {String} formName - 表单的ref属性值
     */
    submitForm(formName) {
      // console.log(this.formData);
      // 表单验证
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 验证通过
          this.$confirm(
            '修改认证资料后需重新进行审核，审核通过后才可继续正常营业。在此期间，只有店长可以登录系统查看，请问是否要继续操作？',
            {
              title: '提示',
              type: 'warning',
              showClose: false,
              confirmButtonText: '继续',
              customClass: 'confirm-box'
            }
          ).then(
            res => {
              // 确认提交
              // 包装数据
              let data = {};
              data.id = this.formData.id;
              data.ownerName = this.formData.ownerName;
              data.idNumber = this.formData.idNumber;
              data.businessLicense = JSON.stringify(this.getImageUrlList(this.formData.businessLicense));
              data.licenseImg = JSON.stringify(this.getImageUrlList(this.formData.licenseImg));
              data.envImgs = JSON.stringify(this.getImageUrlList(this.formData.envImgs));
              editStoreAuthApi(data).then(
                res => {
                  if (res.data.flag) {
                    this.$showMsgs('修改成功', {
                      type: 'success',
                      closeFunc: () => {
                        this.goBack();
                      }
                    });
                  } else {
                    this.$showMsgs(res.data.msg, { type: 'error' });
                  }
                },
                err => {
                  console.log('edit auth err =>', err);
                }
              );
            },
            err => {
              // console.log('conf err =>', err);
              this.$showMsgs('已取消修改', { type: 'info' });
            }
          );
        }
      });
    },
    /**
     * 图片上传成功后的操作
     */
    uploadImageSuccess(res, file, st) {
      // this.formData.cover = URL.createObjectURL(file.raw);
      if (st === 'businessLicense') {
        this.formData.businessLicense.push(res.data.data);
      } else if (st === 'licenseImg') {
        this.formData.licenseImg.push(res.data.data);
      } else if (st === 'envImgs') {
        this.formData.envImgs.push(res.data.data);
      }
    },
    /**
     * 删除图片失败
     */
    uploadImageError(res, file, st) {
      // console.log('uploadImageError', res, file, st);
      this.$message.error('网络繁忙，请稍后再试！');
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
     * 预览图片
     */
    previewImage(file) {
      this.dialogImageVisible = true;
      this.dialogImageUrl = file.url;
    },
    /**
     * 删除图片
     */
    deleteImage(file, st) {
      if (st === 'businessLicense') {
        this.formData.businessLicense = this.formData.businessLicense.filter(item => {
          return item.uid !== file.uid;
        });
      } else if (st === 'licenseImg') {
        this.formData.licenseImg = this.formData.licenseImg.filter(item => {
          return item.uid !== file.uid;
        });
      } else if (st === 'envImgs') {
        this.formData.envImgs = this.formData.envImgs.filter(item => {
          return item.uid !== file.uid;
        });
      }
    },
    /**
     * 点击更换邮箱或更换电话
     */
    initAuthMess(st) {
      this.dialogAuthVisible = true;
      switch (st) {
        case 'phone':
          this.dialogAuthType = 'phone';
          break;
        case 'email':
          this.dialogAuthType = 'email';
          break;
        default:
          this.dialogAuthType = 'phone';
          break;
      }
      this.active = 0;
      this.dialogAuthBtnText = '下一步';
      this.isGetCaptcha = false;
      this.resetAuthMess();
    },
    /**
     * 点击下一步后重置表单
     */
    resetAuthMess() {
      this.authMessInput = '';
      this.authCode = '';
      this.dialogAuthTip = '';
      this.dialogAuthCodeTip = '';
      this.authReset();
    },
    /**
     * 点击下一步
     */
    async nextStep() {
      let flag = false;
      if (this.active === 0) {
        flag = await this.checkAuthCode();
        if (flag) {
          this.active++;
          this.dialogAuthBtnText = '确认修改';
          this.resetAuthMess();
        }
      } else if (this.active === 1) {
        flag = await this.checkInput();
        console.log('res ', flag);
        if (flag) {
          this.editAuthMess();
        }
      } else {
        this.dialogAuthVisible = false;
        this.dialogAuthBtnText = '下一步';
        this.active = 0;
      }
    },
    /**
     * 获取验证码
     */
    async getCaptcha(authType = 'none') {
      if (authType !== 'none' && !this.authMessInput) {
        // 未输入手机号/邮箱
        this.dialogAuthTip = '请输入' + (this.dialogAuthType === 'phone' ? '手机号' : '邮箱');
      } else if (authType === 'phone' && !validator.isCellPhone(this.authMessInput)) {
        // 手机号格式不对，显示相应提示
        this.dialogAuthTip = '请输入正确的手机号';
      } else if (authType === 'email' && !validator.isEmail(this.authMessInput)) {
        // 邮箱格式不对
        this.dialogAuthTip = '请输入正确的邮箱';
      } else {
        // 去除错误提示
        this.dialogAuthTip = '';
        // 发送验证码 ···
        if (authType === 'email') {
          let res = await sendValidateCodeApi('sce', this.formData.email);
          if (res.data.flag) {
            this.$message({
              showClose: true,
              message: '验证码已发送，请注意查收',
              type: 'success'
            });
            // 禁用按钮
            this.isGetCaptcha = true;
            // 开始60s倒计时
            this.authCountDown();
          } else {
            this.$message({
              showClose: true,
              message: '验证码获取失败，请稍候再试',
              type: 'warning'
            });
          }
        } else {
          // 手机
          this.$showMsgs('由于不可抗力原因，无法发送手机验证码，请见谅', { type: 'warning' });
        }
      }
    },
    /**
     * 验证码发送后计时
     */
    authCountDown() {
      // console.log('counter');
      // 将setInterval()方法赋值给前面定义的timer计时器对象，方便后续操作
      this.timer = setInterval(() => {
        // 替换文本，秒实时改变
        this.authCodeBtnText = `${this.counter}秒后可重新获取`;
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
      this.authCodeBtnText = text;
      if (this.timer) {
        // 存在计时器对象，则清除
        clearInterval(this.timer);
        // 重置秒数，防止下次混乱
        this.counter = 60;
        // 计时器对象重置为空
        this.timer = null;
      }
    },
    // 验证相关
    /**
     * 验证码验证
     */
    async testValidateCode() {
      // 调用后台api，获取验证码验证结果
      // const res = await 验证码验证API
      // return res.data;
      return await checkValidateCodeApi(this.authCode).then(
        res => {
          if (res.data.flag) {
            return true;
          } else {
            this.$showMsgs(res.data.msg, { type: 'warning' });
            return false;
          }
        },
        err => {
          console.log('testValidateCode err =>', err);
          return false;
        }
      );
    },
    /**
     * 检查验证码及邮箱、手机号输入
     */
    async checkInput() {
      let flag1 = await this.checkPhoneOrEmail();
      let flag2 = await this.checkAuthCode();
      return flag1 && flag2;
    },
    /**
     * 检查手机和邮箱
     */
    checkPhoneOrEmail() {
      let flag = false;
      if (!this.authMessInput) {
        this.dialogAuthTip = '请输入' + (this.dialogAuthType === 'phone' ? '手机号' : '邮箱');
      } else if (this.dialogAuthType === 'phone' && !validator.isCellPhone(this.authMessInput)) {
        this.dialogAuthTip = '请输入正确的手机号';
      } else if (this.dialogAuthType === 'email' && !validator.isEmail(this.authMessInput)) {
        this.dialogAuthTip = '请输入正确的邮箱';
      } else {
        flag = true;
      }
      return flag;
    },
    /**
     * 检查验证码输入
     */
    async checkAuthCode() {
      let flag = false;
      if (!this.authCode) {
        this.dialogAuthCodeTip = '请输入验证码';
      } else if (!(await this.testValidateCode())) {
        this.dialogAuthCodeTip = '验证码错误';
      } else {
        flag = true;
      }
      return flag;
    },
    /**
     * 修改手机号或邮箱
     */
    editAuthMess() {
      if (this.dialogAuthType === 'phone') {
        // 修改手机号
        // this.isEditSuccess = await ...
      } else if (this.dialogAuthType === 'email') {
        // 修改邮箱
        let data = {
          email: this.authMessInput
        };
        editStoreEmailApi(data)
          .then(
            res => {
              if (res.data.flag) {
                this.isEditSuccess = true;
                this.$showMsgs('邮箱修改成功，请使用新邮箱进行登录', {
                  type: 'success',
                  closeFunc: () => {
                    window.localStorage.removeItem('employeeInfo');
                    this.$router.replace('/login');
                  }
                });
              } else {
                this.isEditSuccess = false;
              }
            },
            err => {
              console.log('edit eamil err =>', err);
            }
          )
          .finally(() => {
            this.resetAuthMess();
            this.dialogAuthBtnText = '返回';
            this.active += 2;
          });
      }
      // this.isEditSuccess = false;
    },
    /**
     * 重置输入提示
     */
    resetAuthTip() {
      this.dialogAuthTip = '';
    },
    resetAuthCodeTip() {
      this.dialogAuthCodeTip = '';
    },
    /**
     * 将图片列表封装成upload组件列表显示的对象数组
     * @param {Array} uriList
     * @param {Array} urlList
     */
    getUploadImgObj(uriList, urlList) {
      let list = [];
      for (let i = 0; i < uriList.length; i++) {
        list.push({
          uri: uriList[i],
          url: urlList[i]
        });
      }
      return list;
    },
    /**
     * 获取对象数组的属性值，整合成数组
     * @param {Array} urlList
     * @param {String} itemName 属性名
     * @returns 列表
     */
    getImageUrlList(urlList, itemName = 'uri') {
      return urlList.map(item => {
        return item[itemName];
      });
    }
  }
};
</script>

<style lang="less" scoped>
.add-box {
  .form-line {
    .mess-label {
      width: 300px;
      box-sizing: border-box;
      padding: 0 15px;
      display: flex;
      align-items: center;
      font-size: 14px;
      color: var(--secondary-font);
      .text {
        width: 70%;
      }
      .btn {
        user-select: none;
        cursor: pointer;
        font-size: 12px;
        color: var(--primary);
        &:hover {
          text-decoration: underline;
        }
      }
    }
    .el-upload--picture-card {
      width: 200px;
      height: 160px;
    }
    :deep(.el-upload) {
      width: 200px;
      height: 160px;
      line-height: 160px;
      overflow: hidden;
      .el-upload-dragger {
        width: 200px;
        height: 160px;
        line-height: 160px;
        border: none;
      }
      &:focus,
      &:hover {
        border-color: var(--primary-border-f);
        .icon {
          color: var(--primary);
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
      .el-upload-list__item {
        &.is-ready {
          display: none;
        }
        &.is-uploading {
          display: none;
        }
      }
    }
  }
  .change-auth {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
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
              border-color: var(--delete);
            }
          }
        }
        .now-email {
          position: relative;
          user-select: none;
          color: var(--disabled-font);
          display: flex;
          align-items: center;
          width: 420px;
          height: 42px;
          margin-bottom: 24px;
          box-sizing: border-box;
          border: solid 1px var(--secondary-border);
          border-radius: 5px;
          background-color: var(--default-bg);
          .tit {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100%;
            box-sizing: border-box;
            padding: 0 32px;
            color: var(--secondary-font);
            border-right: solid 1px var(--secondary-border);
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
          :deep(.el-input) {
            .el-input__inner {
              &:focus {
                border-color: var(--primary-border-f);
              }
            }
          }
          .el-button {
            &.is-plain {
              &.is-disabled {
                &:focus,
                &:hover,
                &:active {
                  opacity: 1;
                  background-color: #fff;
                  border-color: #ebeef5;
                  color: #c0c4cc;
                }
              }
              &:focus {
                border-color: #dcdfe6;
                color: #606266;
              }
              &:hover {
                border-color: var(--primary-border-f);
                color: var(--primary);
              }
              &:active {
                opacity: 0.8;
              }
            }
          }
          .cap-btn {
            //   display: flex;
            //   align-items: center;
            //   justify-content: center;
            //   color: var(--main-font);
            width: 35%;
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
          :deep(.el-input) {
            .el-input__inner {
              &:focus {
                border-color: var(--primary-border-f);
              }
            }
          }
        }
        &.step-three {
          .icon {
            color: var(--primary);
            font-size: 64px;
            margin-bottom: 8px;
            &.ic-fail {
              color: var(--delete);
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
    .btn {
      user-select: none;
      cursor: pointer;
      width: 270px;
      height: 36px;
      color: var(--white);
      background-color: var(--primary);
      border-radius: 18px;
      text-align: center;
      line-height: 36px;
    }
  }
  [class^='ic-'] {
    font-family: 'fbookfont' !important;
    font-size: 16px;
    font-style: normal;
    speak: none;
    font-weight: 400;
    font-variant: normal;
    text-transform: none;
    line-height: 1;
    vertical-align: baseline;
    display: inline-block;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
}
</style>

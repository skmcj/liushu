<template>
  <div class="auth-box">
    <transition-group name="box-fade" mode="out-in">
      <!-- 认证信息 -->
      <div class="auth-box" v-if="authBoxVisible" key="auth-box">
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
                action=""
                :http-request="uploadImagePlusApi"
                :data="{ type: 'sta' }"
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
                action=""
                :http-request="uploadImagePlusApi"
                :data="{ type: 'sta' }"
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
                action=""
                :http-request="uploadImagePlusApi"
                :data="{ type: 'sta' }"
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
                action=""
                :http-request="uploadImagePlusApi"
                :data="{ type: 'sta' }"
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
import { uploadImagePlusApi } from '@/api/commonApi';
import { businessAuthApi } from '@/api/shopApi';

export default {
  components: {
    Loading,
    Step,
    Steps
  },
  data() {
    return {
      baseURL: 'http://localhost:8080/api',
      // 店铺基本信息
      baseForm: {
        name: '',
        cover: '',
        coverUri: '',
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
     * 自定义上传函数
     */
    uploadImagePlusApi,
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
     * 图片上传成功后的操作
     */
    uploadImageSuccess(res, file, formMess) {
      // console.log('image res =>', res);
      if (res.status === 200) {
        // 图片上传成功
        if (formMess.formName === 'base') {
          // 上传店铺门脸图
          this.baseForm.cover = res.data.data.url;
          this.baseForm.coverUri = res.data.data.uri;
          let flag = this.validateField('baseForm', 'cover');
        } else if (formMess.formName === 'qf') {
          // 上传店铺资质图
          this.qfForm[formMess.itemName].push(res.data.data);
          let flag = this.validateField('qfForm', formMess.itemName);
        }
      } else {
        // 图片上传失败
        this.$message.error('图片上传失败，请稍后重试');
      }
    },
    uploadImageError(res, file, formMess) {
      this.$message.error('图片上传失败，请稍后再试！');
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
    async nextStep() {
      if (this.active === 0) {
        const flag = await this.validate('baseForm');
        if (flag) {
          this.active += 1;
        }
        // console.log('baseForm', this.baseForm);
      } else if (this.active === 1) {
        const flag = await this.validate('qfForm');
        if (flag) {
          this.active += 1;
        }
        // console.log('qfForm', this.qfForm);
      } else if (this.active === 2) {
        const flag = await this.validate('legalForm');
        if (flag) {
          this.active += 1;
          // 提交表单
          // 包装表单
          let authForm = {};
          authForm.storeName = this.baseForm.name;
          authForm.cover = this.baseForm.coverUri;
          authForm.address = this.baseForm.address;
          authForm.businessHours = this.baseForm.business_hours.join('-');
          authForm.distribution = this.baseForm.distribution;
          authForm.deliverFee = this.baseForm.deliver_fee;
          authForm.businessLicense = JSON.stringify(this.getImageUrlList(this.qfForm.business_license));
          authForm.licenseImg = JSON.stringify(this.getImageUrlList(this.qfForm.license_img));
          authForm.envImgs = JSON.stringify(this.getImageUrlList(this.qfForm.env_imgs));
          authForm.ownerName = this.legalForm.owner_name;
          authForm.idNumber = this.legalForm.id_number;
          authForm.phone = this.legalForm.phone;
          console.log('authForm => ', authForm);
          // 表单提交完成后，回调执行this.authBoxVisible = false;
          let authRes = await businessAuthApi(authForm);
          if (authRes.data.flag) {
            this.authBoxVisible = false;
            this.isSendAuthSuccess = true;
          } else {
            this.authBoxVisible = true;
            this.isSendAuthSuccess = false;
          }
          // console.log('authRes => ', authRes);
        }
        // console.log('legalForm', this.legalForm);
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
.auth-box {
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
          :deep(.el-upload-list) {
            .el-upload-list__item {
              &.is-ready {
                display: none;
              }
              &.is-uploading {
                display: none;
              }
            }
            &.el-upload-list--picture-card {
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

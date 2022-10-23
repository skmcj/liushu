<template>
  <div class="main-box add-box">
    <el-form :model="formData" :rules="rules" ref="form" label-width="188px" class="form">
      <div class="edit-auth" @click="editAuthInfo">认证信息编辑</div>
      <div class="block-content">
        <div class="form-line">
          <el-form-item label="店铺名称：" prop="name">
            <el-input v-model="formData.name" placeholder="请输入店铺名称" style="width: 300px"></el-input>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="门脸图片：" prop="cover" class="upload-item" style="width: 488px">
            <el-upload
              class="cover-uploader"
              drag
              action="#"
              :disabled="formData.cover != ''"
              :show-file-list="false"
              :on-success="uploadCoverSuccess"
              :on-error="uploadCoverError"
              :before-upload="beforeCoverUpload">
              <div v-if="formData.cover" class="cover">
                <img :src="formData.cover" @click.stop="handlePreviewCover" />
                <span class="hover">
                  <i class="icon fbookfont ic-delete" @click.stop="handelDeleteCover"></i>
                </span>
              </div>
              <i v-else class="icon fbookfont ic-add"></i>
            </el-upload>
            <el-dialog :visible.sync="previewCoverVisible">
              <img width="100%" :src="previewCoverUrl" alt="" />
            </el-dialog>
            <el-popover placement="right-end" width="200" trigger="hover" content="拖拽或点击左侧方框上传图片">
              <span class="cover-tip" slot="reference">
                <i class="icon fbookfont ic-explain"></i>
              </span>
            </el-popover>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="店铺地址：" prop="address">
            <el-input v-model="formData.address" placeholder="请输入店铺地址" style="width: 300px"></el-input>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="营业时间：" prop="business_hours">
            <el-time-picker
              is-range
              v-model="formData.business_hours"
              format="HH:mm"
              value-format="HH:mm"
              range-separator="-"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              placeholder="选择时间范围"
              style="width: 300px">
            </el-time-picker>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="首次借阅天数：" prop="borrow_day" style="width: 488px">
            <el-input
              v-model.number="formData.borrow_day"
              placeholder="请输入首次可借阅时长"
              style="width: 210px"></el-input>
            <span class="unit">天</span>
            <el-popover
              class="absolute-tip"
              placement="bottom-start"
              width="210"
              trigger="hover"
              content="用户借阅图书时，不可超过这一设置时长">
              <span class="input-tip" slot="reference">
                <i class="icon fbookfont ic-explain"></i>
              </span>
            </el-popover>
          </el-form-item>
          <el-form-item label="默认可续借天数：" prop="renew_day" style="width: 488px">
            <el-input
              v-model.number="formData.renew_day"
              placeholder="请输入默认可续借时长"
              style="width: 210px"></el-input>
            <span class="unit">天</span>
            <el-popover
              class="absolute-tip"
              placement="bottom-start"
              width="210"
              trigger="hover"
              content="用户续借时，默认不可超过这一时长，后续可在相应订单详情页进行修改">
              <span class="input-tip" slot="reference">
                <i class="icon fbookfont ic-explain"></i>
              </span>
            </el-popover>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="配送服务：" prop="distribution" style="width: 488px">
            <el-radio-group v-model="formData.distribution">
              <el-radio :label="0">由 商家 配送</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="配送费：" prop="deliver_fee" style="width: 488px">
            <el-input-number
              v-model="formData.deliver_fee"
              :precision="2"
              :min="0"
              class="deliver-fee"></el-input-number>
            <span class="unit">元/单</span>
          </el-form-item>
        </div>
        <div class="form-line relative-line">
          <el-form-item label="标签：" prop="label">
            <el-select
              v-model="formData.label"
              multiple
              filterable
              allow-create
              default-first-option
              :multiple-limit="5"
              :popper-append-to-body="false"
              placeholder="请选择店铺标签"
              class="form-label">
              <el-option v-for="item in book_cate" :key="item.id" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </el-form-item>
          <el-popover
            class="absolute-tip"
            placement="bottom-start"
            width="210"
            trigger="hover"
            content="点击左侧方框输入自定义标签或选择已有标签(最多可选择5个标签)">
            <span class="input-tip" slot="reference">
              <i class="icon fbookfont ic-explain"></i>
            </span>
          </el-popover>
        </div>
        <div class="form-line">
          <el-form-item label="商家服务：" prop="store_service">
            <el-input
              type="textarea"
              resize="none"
              show-word-limit
              maxlength="200"
              v-model="formData.store_service"
              placeholder="请输入商家将提供的服务"
              style="width: 800px"
              class="h-82"></el-input>
          </el-form-item>
        </div>
        <div class="form-line">
          <el-form-item label="公告：" prop="notice">
            <el-input
              type="textarea"
              resize="none"
              show-word-limit
              maxlength="200"
              v-model="formData.notice"
              placeholder="请输入店铺公告"
              style="width: 800px"
              class="h-82"></el-input>
          </el-form-item>
        </div>
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
import { getBookCateApi } from '@/api/bookApi';

export default {
  data() {
    return {
      formData: {
        name: '片刻书店',
        cover: 'https://s1.328888.xyz/2022/09/22/IwkOh.jpg',
        address: '广东省韶关市浈江区大学路918号',
        business_hours: ['9:00', '20:00'],
        borrow_day: '',
        renew_day: '',
        distribution: 0,
        distributionStr: '由 商家 配送',
        deliver_fee: 3,
        store_service: '',
        notice: '校内借阅30天内免借阅费',
        label: ['哲学', '文学', '小说']
      },
      rules: {
        name: [{ required: true, message: '请输入店铺名称', trigger: 'blur' }],
        cover: [{ required: true, message: '请选择门脸图片', trigger: 'blur' }],
        address: [{ required: true, message: '请输入店铺地址', trigger: 'blur' }],
        business_hours: [{ required: true, message: '请输入营业时间', trigger: 'blur' }],
        borrow_day: [{ required: true, message: '请输入首次可借阅时长', trigger: 'blur' }],
        renew_day: [{ required: true, message: '请输入默认可续借时长', trigger: 'blur' }],
        distribution: [{ required: true, message: '请选择配送服务', trigger: 'blur' }],
        deliver_fee: [{ required: true, message: '请输入配送费', trigger: 'blur' }]
      },
      // 图书类别
      book_cate: [],
      // 已上传封面的预览图
      previewCoverUrl: '',
      // 是否显示已上传封面的预览图
      previewCoverVisible: false
    };
  },
  created() {
    this.getBookCate();
  },
  methods: {
    /**
     * 前往编辑认证信息
     */
    editAuthInfo() {
      this.$router.push('/shop/edit/authinfo');
    },
    /**
     * 获取图书类别信息
     */
    getBookCate() {
      getBookCateApi().then(
        res => {
          this.book_cate = res.data;
        },
        err => {}
      );
    },
    /**
     * 图片上传成功后的操作
     */
    uploadCoverSuccess(res, file) {
      this.formData.cover = URL.createObjectURL(file.raw);
    },
    uploadCoverError(res, file) {
      console.log(res, file);
      this.$message.error('网络繁忙，请稍后再试！');
      // TODO: 后续删除
      this.formData.cover = URL.createObjectURL(file.raw);
    },
    /**
     * 图片上传前的判断处理
     */
    beforeCoverUpload(file) {
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
     * 删除图书封面
     */
    handelDeleteCover() {
      this.formData.cover = '';
    },
    /**
     * 显示图书封面
     */
    handlePreviewCover() {
      this.previewCoverUrl = this.formData.cover;
      this.previewCoverVisible = true;
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
     * @param {Boolean} st - 是否保存并继续
     */
    submitForm(formName) {
      console.log(this.formData);
      // 表单验证
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 验证通过
        } else {
          // 验证失败
        }
      });
    },
    /**
     * 重置表单
     */
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
};
</script>

<style lang="less" scoped>
.add-box {
  .form-line {
    display: flex;
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
    .upload-item {
      :deep(.el-form-item__content) {
        display: flex;
      }
    }
    .input-tip {
      display: inline-block;
      line-height: 42px;
      margin: 0 10px;
      .icon {
        font-size: 16px;
        color: var(--tip-icon);
        &:hover {
          color: var(--disable);
        }
      }
    }
    .cover-tip {
      margin-top: 136px;
      margin-left: 12px;
      display: inline-block;
      width: 24px;
      height: 24px;
      text-align: center;
      line-height: 24px;
      .icon {
        font-size: 18px;
        color: var(--tip-icon);
        &:hover {
          color: var(--disable);
        }
      }
    }
    .cover-uploader {
      display: inline-block;
      width: 200px;
      height: 160px;
      .el-upload {
        .cover {
          width: 200px;
          height: 160px;
          position: relative;
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
    .form-label {
      width: 800px;
      :deep(.el-select__tags) {
        max-width: 800px !important;
        & > span {
          display: flex;
          flex-wrap: wrap;
          position: absolute;
          left: 42%;
          z-index: 99;
        }
      }
      :deep(.el-input) {
        width: 37.5%;
        &.is-focus {
          .el-input__inner {
            border-color: var(--primary-border-f);
          }
        }
      }
      :deep(.el-select-dropdown) {
        min-width: 300px !important;
      }
    }
    &.relative-line {
      position: relative;
      .absolute-tip {
        position: absolute;
        left: 488px;
        z-index: 99;
      }
    }
  }
  .form {
    position: relative;
    .edit-auth {
      user-select: none;
      cursor: pointer;
      position: absolute;
      top: 0;
      right: 0;
      font-size: 14px;
      color: var(--primary);
      &:hover {
        text-decoration: underline;
      }
    }
  }
}
</style>

<template>
  <!-- 我的-个人中心-个人信息 -->
  <div class="mine-center-per">
    <el-form
      :model="perForm"
      :rules="perRules"
      ref="perForm"
      label-width="120px"
      class="per-form"
      hide-required-asterisk>
      <!-- 头像 -->
      <el-form-item label="头像：" prop="avatar" class="avatar-item">
        <div class="per-avatar">
          <img
            class="avatar-url"
            :src="perForm.avatarUrl ? perForm.avatarUrl : $noAvatarUrl"
            alt="avatar"
            @click.stop="handlePreviewCover" />
          <el-upload
            class="avatar-uploader"
            action=""
            :http-request="uploadImagePlusApi"
            :data="{ type: 'user' }"
            :show-file-list="false"
            :on-success="uploadCoverSuccess"
            :on-error="uploadCoverError"
            :before-upload="beforeCoverUpload">
            <span class="upload-box">编辑头像</span>
          </el-upload>
        </div>
      </el-form-item>
      <!-- 昵称 -->
      <el-form-item label="昵称：" prop="nickname">
        <el-input v-model="perForm.nickname" style="width: 240px" placeholder="请输入昵称"></el-input>
      </el-form-item>
      <!-- 用户名 -->
      <el-form-item label="用户名：">
        <div class="username">{{ perForm.username }}</div>
      </el-form-item>
      <!-- 性别 -->
      <el-form-item label="性别：">
        <div class="sex-box">
          <div
            class="sex-item"
            v-for="(item, index) in sexList"
            :key="'sex-item-' + index"
            :class="{ 'is-checked': perForm.sex === item.value }"
            @click.stop="handleSexChange(item.value)">
            {{ item.label }}
          </div>
        </div>
      </el-form-item>
      <!-- 个性签名 -->
      <el-form-item label="个性签名：">
        <el-input
          type="textarea"
          :rows="5"
          placeholder="这个用户很懒，暂时没有个性签名~"
          v-model="perForm.signature"
          style="width: 560px">
        </el-input>
      </el-form-item>
      <!-- 生日 -->
      <el-form-item label="出生日期：">
        <el-date-picker v-model="perForm.birthday" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-form-item>
      <div class="btns">
        <div class="btn" @click.stop="submitForm">保存</div>
      </div>
    </el-form>
    <!-- 头像放大图 -->
    <el-dialog :visible.sync="previewCoverVisible" :modal-append-to-body="false">
      <img width="100%" :src="previewCoverUrl" alt="previewAvatar" />
    </el-dialog>
  </div>
</template>

<script>
import { uploadImagePlusApi } from '@/api/commonApi';

export default {
  data() {
    return {
      // 性别
      sexList: [
        { label: '男', value: 1 },
        { label: '女', value: 2 },
        { label: '保密', value: 0 }
      ],
      // 个人信息
      perForm: {
        username: '',
        avatar: '',
        avatarUrl: '',
        nickname: '',
        sex: 0,
        money: 0,
        coupon: null,
        birthday: '',
        signature: ''
      },
      perRules: {},
      previewCoverUrl: '',
      previewCoverVisible: false
    };
  },
  created() {
    this.initMess();
  },
  methods: {
    uploadImagePlusApi,
    /**
     * 初始化信息
     */
    initMess() {
      this.perForm = this.$store.state.userInfo;
      this.coupon = null;
    },
    /**
     * 改变性别
     */
    handleSexChange(val) {
      this.perForm.sex = val;
    },
    /**
     * 提交表单
     */
    submitForm() {
      // 如果昵称为空，则默认等于用户名
      this.perForm.nickname = this.perForm.nickname === '' ? this.perForm.username : this.perForm.nickname;
    },
    /**
     * 图片上传成功后的操作
     */
    uploadCoverSuccess(res, file) {
      if (res.status === 200) {
        // 图片上传成功
        this.perForm.avatarUrl = res.data.data.url;
        this.perForm.avatar = res.data.data.uri;
      } else {
        // 图片上传失败
        this.$message.error('头像上传失败，请稍后重试');
      }
    },
    uploadCoverError(res, file) {
      // console.log(res, file);
      this.$message.error('网络繁忙，请稍后再试！');
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
      this.perForm.avatarUrl = '';
      this.perForm.avatar = '';
    },
    /**
     * 显示图书封面
     */
    handlePreviewCover() {
      this.previewCoverUrl = this.perForm.avatarUrl;
      this.previewCoverVisible = true;
    }
  }
};
</script>

<style lang="less" scoped>
.mine-center-per {
  box-sizing: border-box;
  padding: 0 36px;
  margin-top: 32px;
  padding-bottom: 120px;
  .per-form {
    .avatar-item {
      :deep(.el-form-item__label) {
        line-height: 85px;
      }
    }
    .username {
      box-sizing: border-box;
      padding: 0 8px;
      height: 40px;
      line-height: 40px;
      font-size: 14px;
      color: #898989;
    }
    .sex-box {
      height: 40px;
      box-sizing: border-box;
      padding: 0 8px;
      display: flex;
      align-items: center;
      .sex-item {
        cursor: pointer;
        user-select: none;
        height: 24px;
        padding: 0 12px;
        box-sizing: border-box;
        border-radius: 5px;
        border: 1px solid #e6e6e6;
        font-size: 14px;
        color: #999;
        line-height: 24px;
        transition: all 0.5s ease-in-out;
        &.is-checked {
          border-color: var(--primary);
          color: var(--primary);
        }
        & + .sex-item {
          margin-left: 12px;
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
        padding: 0 36px;
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
.per-avatar {
  margin-left: 72px;
  position: relative;
  width: 85px;
  height: 85px;
  border-radius: 50%;
  overflow: hidden;
  border: 1px dashed #ccc;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
  .avatar-url {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  &:hover {
    .avatar-uploader {
      bottom: 0;
      opacity: 1;
    }
  }
  .avatar-uploader {
    opacity: 0;
    width: 100%;
    height: 24px;
    position: absolute;
    bottom: -24px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.8s ease-in-out;
    background-color: rgba(0, 0, 0, 0.5);
    .upload-box {
      color: #f5f3f2;
      font-size: 10px;
    }
  }
}
</style>

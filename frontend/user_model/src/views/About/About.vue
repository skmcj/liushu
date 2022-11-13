<template>
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box">
      <div class="content">
        <div class="aside">
          <div class="item" :class="{ 'is-active': activeAside === 'profile' }" @click.stop="handleAside('profile')">
            本站简介
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'problem' }" @click.stop="handleAside('problem')">
            常见问题
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'our' }" @click.stop="handleAside('our')">
            联系我们
          </div>
          <div class="item" :class="{ 'is-active': activeAside === 'link' }" @click.stop="handleAside('link')">
            友情链接
          </div>
        </div>
        <div class="main-content">
          <div class="main-content-box">
            <div class="title-box">
              <span class="icon"></span>
              <span class="text">{{ title }}</span>
              <div class="hr"></div>
            </div>
            <transition name="fade">
              <router-view />
            </transition>
          </div>
          <transition name="fade" v-if="activeAside === 'link'">
            <div class="add-link-box">
              <div class="title-box">
                <span class="icon"></span>
                <span class="text">申请友情链接</span>
                <div class="hr"></div>
              </div>
              <div class="add-link-content">
                <div class="add-link-tips">
                  <div class="title">申请步骤</div>
                  <div class="steps">
                    <div class="step">
                      <span class="num">1.</span>
                      <div class="text-box">
                        <div class="text">请先在贵网站做好流书网的文字友情链接：</div>
                        <div class="text">链接文字：流书网链接地址： www.liushu.com</div>
                      </div>
                    </div>
                    <div class="step">
                      <span class="num">2.</span>
                      <div class="text-box">
                        <div class="text">做好链接后，请在右侧填写申请信息。</div>
                      </div>
                    </div>
                    <div class="step">
                      <span class="num">3.</span>
                      <div class="text-box">
                        <div class="text">
                          已经开通我站友情链接且内容健康，符合本站友情链接要求的网站，经本站管理员审核后，将显示在此友情链接页面。
                        </div>
                      </div>
                    </div>
                    <div class="step">
                      <span class="num">4.</span>
                      <div class="text-box">
                        <div class="text">请通过右侧提交申请。</div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="add-link-form-box">
                  <div class="title">申请信息</div>
                  <el-form
                    :model="linkForm"
                    :rules="linkRule"
                    ref="linkForm"
                    label-width="100px"
                    class="link-form"
                    hide-required-asterisk>
                    <el-form-item label="网站名称：" prop="name">
                      <el-input v-model="linkForm.name" placeholder="请输入网站名称"></el-input>
                    </el-form-item>
                    <el-form-item label="网站链接：" prop="url">
                      <el-input v-model="linkForm.url" placeholder="请输入网站链接"></el-input>
                    </el-form-item>
                    <el-form-item label="网站Logo：" prop="logo">
                      <el-input v-model="linkForm.logo" placeholder="请输入网站Logo"></el-input>
                    </el-form-item>
                    <el-form-item label="联系邮箱：" prop="email">
                      <el-input v-model="linkForm.email" placeholder="请输入联系邮箱"></el-input>
                    </el-form-item>
                    <el-form-item label="链接类型：" prop="type">
                      <el-select v-model="linkForm.type" placeholder="请选择友情链接类型">
                        <el-option label="文本链接" value="text"></el-option>
                        <el-option label="图片链接" value="image"></el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="网站简介：" prop="prefile">
                      <el-input type="textarea" v-model="linkForm.prefile" placeholder="请输入网站简介"></el-input>
                    </el-form-item>
                    <div class="link-form-btns">
                      <div class="btn" @click.stop="submitForm">提交申请</div>
                    </div>
                  </el-form>
                </div>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Wave from '@/components/Common/Wave';

export default {
  components: {
    Wave
  },
  data() {
    return {
      activeAside: 'profile',
      title: '本站简介',
      linkForm: {
        name: '',
        url: '',
        logo: '',
        email: '',
        prefile: '',
        type: ''
      },
      linkRule: {
        name: [{ required: true, message: '请输入网站名称', trigger: 'blur' }],
        url: [{ required: true, message: '请输入网站地址', trigger: 'blur' }],
        logo: [{ required: true, message: '请输入网站Logo', trigger: 'blur' }],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        prefile: [{ required: true, message: '请输入网站简介', trigger: 'blur' }],
        type: [{ required: true, message: '请选择链接类型', trigger: 'blur' }]
      }
    };
  },
  methods: {
    /**
     * 处理侧边栏
     */
    handleAside(text) {
      this.activeAside = text;
      this.$router.push(`/about/${text}`);
      switch (text) {
        case 'problem':
          this.title = '常见问题';
          break;
        case 'our':
          this.title = '联系我们';
          break;
        case 'link':
          this.title = '友情链接';
          break;
        default:
          this.title = '本站简介';
          break;
      }
    },
    /**
     * 提交表单
     */
    submitForm() {
      this.$refs.linkForm.validate(valid => {
        if (valid) {
          console.log('link form =>', this.linkForm);
        }
      });
    }
  },
  watch: {
    // 监听当前路由信息
    $route: {
      immediate: true,
      handler(val) {
        // console.log('route =>', val);
        this.activeAside = val.meta.aside;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.content {
  display: flex;
  justify-content: space-between;
  .aside {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 18%;
    height: 394px;
    box-sizing: border-box;
    padding: 42px 24px 0 24px;
    border-radius: 12px;
    background-color: var(--white);
    .item {
      cursor: pointer;
      user-select: none;
      width: 100%;
      height: 48px;
      line-height: 48px;
      box-sizing: border-box;
      border-radius: 8px;
      font-size: 21px;
      text-align: center;
      color: var(--primary-text);
      transition: background-color 0.5s ease-in-out;
      &.is-active {
        background-color: #e3e5e7;
      }
      & + .item {
        margin-top: 12px;
      }
    }
  }
  .main-content {
    width: 81%;
    display: flex;
    flex-direction: column;
  }
  .main-content-box,
  .add-link-box {
    width: 100%;
    display: flex;
    flex-direction: column;
    border-radius: 12px;
    box-sizing: border-box;
    overflow: hidden;
    padding: 0 12px;
    background-color: var(--white);
    .title-box {
      position: relative;
      width: 100%;
      height: 72px;
      margin-top: 15px;
      display: flex;
      align-items: center;
      .icon {
        position: relative;
        width: 6px;
        height: 48px;
        border-radius: 3px;
        background-color: #e7e7e7;
        overflow: hidden;
        &::before {
          display: block;
          content: '';
          width: 100%;
          height: 75%;
          border-radius: 3px;
          background-color: var(--primary);
          position: absolute;
          top: 0;
          left: 0;
        }
      }
      .text {
        font-size: 27px;
        font-weight: bold;
        color: var(--primary-text);
        margin-left: 12px;
      }
      .hr {
        background-color: #dadada;
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        height: 1px;
        box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
      }
    }
  }
  .add-link-box {
    margin-top: 24px;
    .add-link-content {
      display: flex;
      align-items: center;
      box-sizing: border-box;
      padding: 36px;
      .add-link-tips,
      .add-link-form-box {
        width: 50%;
        height: 100%;
        display: flex;
        flex-direction: column;
        box-sizing: border-box;
        .title {
          font-size: 18px;
          font-weight: bold;
          color: var(--primary-text);
        }
      }
      .add-link-tips {
        border-right: 1px solid #cacaca;
        .steps {
          margin-top: 24px;
          display: flex;
          flex-direction: column;
          width: 100%;
          box-sizing: border-box;
          padding-left: 6px;
          padding-right: 24px;
          .step {
            width: 100%;
            display: flex;
            .num {
              font-size: 14px;
              color: var(--primary-text);
            }
            .text-box {
              flex-grow: 1;
              margin-left: 5px;
              display: flex;
              flex-direction: column;
              .text {
                font-size: 14px;
                color: #666666;
                & + .text {
                  margin-top: 3px;
                }
              }
            }
            & + .step {
              margin-top: 12px;
            }
          }
        }
      }
      .add-link-form-box {
        padding: 0 12px 0 24px;
        .link-form {
          margin-top: 24px;
          :deep(.el-form-item) {
            .el-form-item__label {
              font-size: 14px;
              color: var(--primary-text);
            }
            .el-form-item__content {
              line-height: 36px;
              .el-input {
                &.is-active {
                  border-color: var(--primary);
                }
              }
              .el-input__inner {
                height: 36px;
                line-height: 36px;
                &:focus {
                  border-color: var(--primary);
                }
              }
              .el-select {
                width: 100%;
                .el-input {
                  .el-input__inner {
                    &:focus {
                      border-color: #dcdfe6;
                    }
                  }
                  &.is-focus {
                    .el-input__inner {
                      border-color: var(--primary);
                    }
                  }
                }
              }
              .el-textarea__inner {
                &:focus {
                  border-color: var(--primary);
                }
              }
            }
          }
          .link-form-btns {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 12px;
            .btn {
              cursor: pointer;
              user-select: none;
              padding: 8px 24px;
              border-radius: 6px;
              box-sizing: border-box;
              background-color: var(--primary);
              font-size: 16px;
              color: #fff;
              &:hover {
                background-color: rgba(131, 204, 210, 0.8);
              }
              &:active {
                background-color: #7abfc4;
              }
            }
          }
        }
      }
    }
  }
}
</style>

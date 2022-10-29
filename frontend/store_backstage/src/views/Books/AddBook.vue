<template>
  <div class="main-box add-box">
    <el-form :model="formData" :rules="rules" ref="form" label-width="188px" class="form">
      <FormBlock title="基本信息">
        <div class="block-content">
          <div class="form-line">
            <el-form-item label="图书名称：" prop="book.name">
              <el-input v-model="formData.book.name" placeholder="请输入图书名称" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="图书类别：" prop="book.bookCateId">
              <el-select v-model="formData.book.bookCateId" placeholder="请选择图书类别" style="width: 300px">
                <el-option v-for="item in bookCate" :key="item.id" :label="item.name" :value="item.id"> </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="作者：" prop="book.author">
              <el-input v-model="formData.book.author" placeholder="请输入作者名称" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="店内分类：" prop="book.goodsCateId">
              <el-select v-model="formData.book.goodsCateId" placeholder="请选择店内分类" style="width: 300px">
                <el-option v-for="item in goodsCate" :key="item.id" :label="item.name" :value="item.id"> </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="出版社：" prop="book.press">
              <el-input v-model="formData.book.press" placeholder="请输入出版社名称" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="出版时间：" prop="book.pubDate">
              <el-date-picker
                v-model="formData.book.pubDate"
                type="date"
                placeholder="请选择出版时间"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions"
                prefix-icon="icon fbookfont ic-calendar"
                style="width: 300px">
              </el-date-picker>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="开本：" prop="book.size" style="width: 488px">
              <el-input v-model="formData.book.size" placeholder="请输入图书规格" style="width: 132px"></el-input>
            </el-form-item>
            <el-form-item label="开本：" prop="book.pages" style="width: 488px">
              <el-input
                v-model.number="formData.book.pages"
                placeholder="请输入图书页数"
                style="width: 132px"></el-input>
              <span class="unit">页</span>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="售卖状态：" prop="book.status" style="width: 488px">
              <el-switch
                v-model="formData.book.status"
                active-color="var(--primary)"
                :active-value="1"
                :inactive-value="0"
                :active-text="formData.book.status ? '启售' : '禁售'"></el-switch>
            </el-form-item>
            <el-form-item label="库存：" prop="book.inventory" style="width: 488px">
              <el-input-number size="small" v-model="formData.book.inventory" :min="0"></el-input-number>
              <span class="unit">本</span>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="图书封面：" prop="book.cover" class="upload-item" style="width: 488px">
              <el-upload
                class="cover-uploader"
                drag
                action=""
                :http-request="uploadImagePlusApi"
                :data="{ type: 'bkc' }"
                :disabled="formData.book.coverUrl != ''"
                :show-file-list="false"
                :on-success="uploadCoverSuccess"
                :on-error="uploadCoverError"
                :before-upload="beforeCoverUpload">
                <div v-if="formData.book.coverUrl" class="cover">
                  <img :src="formData.book.coverUrl" @click.stop="handlePreviewCover" />
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
        </div>
      </FormBlock>
      <FormBlock title="版权信息">
        <div class="block-content">
          <div class="form-line">
            <el-form-item label="ISBN：" prop="bookInfo.isbn">
              <el-input
                v-model.trim="formData.bookInfo.isbn"
                placeholder="请输入图书ISBN"
                style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="条形码：" prop="bookInfo.barCode">
              <el-input
                v-model="formData.bookInfo.barCode"
                placeholder="请输入图书条形码"
                style="width: 300px"></el-input>
              <el-popover placement="bottom" width="175" trigger="hover" content="如有多个可用英文逗号隔开">
                <span class="input-tip" slot="reference">
                  <i class="icon fbookfont ic-explain"></i>
                </span>
              </el-popover>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="装帧：" prop="bookInfo.binding">
              <el-input
                v-model="formData.bookInfo.binding"
                placeholder="请输入图书装帧类型"
                style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="版次：" prop="bookInfo.version" style="width: 488px">
              <el-input v-model="formData.bookInfo.version" placeholder="请输入版次" style="width: 132px"></el-input>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="册数：" prop="bookInfo.volume" style="width: 488px">
              <el-input v-model="formData.bookInfo.volume" placeholder="请输入册数" style="width: 132px"></el-input>
            </el-form-item>
            <el-form-item label="重量：" prop="bookInfo.weight" style="width: 488px">
              <el-input v-model="formData.bookInfo.weight" placeholder="请输入图书重量" style="width: 132px"></el-input>
              <span class="unit en">g</span>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="印刷次数：" prop="bookInfo.printTimes" style="width: 488px">
              <el-input
                v-model="formData.bookInfo.printTimes"
                placeholder="请输入印刷次数"
                style="width: 132px"></el-input>
            </el-form-item>
          </div>
        </div>
      </FormBlock>
      <FormBlock title="详细信息">
        <div class="block-content">
          <div class="form-line">
            <el-form-item label="本书特色：" prop="bookDetail.featureDesc">
              <el-input
                type="textarea"
                v-model="formData.bookDetail.featureDesc"
                placeholder="请简短地介绍本书的特色"
                style="width: 800px"
                class="h-82"></el-input>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="图书简介：" prop="bookDetail.outline">
              <el-input
                type="textarea"
                v-model="formData.bookDetail.outline"
                placeholder="请输入图书的内容简介"
                style="width: 800px"
                class="h-82"></el-input>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="作者简介：" prop="bookDetail.authorDesc">
              <el-input
                type="textarea"
                v-model="formData.bookDetail.authorDesc"
                placeholder="请输入图书作者的简介"
                style="width: 800px"
                class="h-82"></el-input>
            </el-form-item>
          </div>
        </div>
      </FormBlock>
      <FormBlock title="费用信息">
        <div class="block-content">
          <div class="form-line">
            <el-form-item label="借阅费：" prop="bookCost.borrowCost" style="width: 488px">
              <el-input-number
                v-model="formData.bookCost.borrowCost"
                :precision="2"
                placeholder="请输入借阅费"
                :min="0"
                class="number"
                style="width: 132px"></el-input-number>
              <span class="unit">元/天</span>
            </el-form-item>
            <el-form-item label="免借阅费天数：" prop="bookCost.freeDay" style="width: 488px">
              <el-input
                v-model.number="formData.bookCost.freeDay"
                placeholder="请输入免费天数"
                style="width: 132px"></el-input>
              <span class="unit">天</span>
              <el-popover
                placement="right"
                width="175"
                trigger="hover"
                content="表示借阅时长在设置天数内免借阅费，超过则超过部分正常收费。">
                <span class="input-tip" slot="reference">
                  <i class="icon fbookfont ic-explain"></i>
                </span>
              </el-popover>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="包装费：" prop="bookCost.packingCost" style="width: 488px">
              <el-input-number
                v-model="formData.bookCost.packingCost"
                :precision="2"
                placeholder="请输入包装费"
                :min="0"
                class="number"
                style="width: 132px"></el-input-number>
              <span class="unit">元</span>
            </el-form-item>
            <el-form-item label="押金：" prop="bookCost.deposit" style="width: 488px">
              <el-input-number
                v-model="formData.bookCost.deposit"
                placeholder="请输入押金"
                :precision="2"
                :min="0"
                class="number"
                style="width: 132px"></el-input-number>
              <span class="unit">元</span>
              <el-popover
                placement="right"
                width="175"
                trigger="hover"
                content="即借阅该图书时需支付的额外费用，仅做抵押用处，将订单完成无误后原封返还">
                <span class="input-tip" slot="reference">
                  <i class="icon fbookfont ic-explain"></i>
                </span>
              </el-popover>
            </el-form-item>
          </div>
        </div>
      </FormBlock>
      <div class="btns">
        <el-form-item label-width="0">
          <el-button @click="goBack">取消</el-button>
          <el-button type="primary" @click="submitForm(false)">保存</el-button>
          <el-button v-if="actionType == 'add'" type="primary" class="continue" @click="submitForm(true)"
            >保存并继续添加</el-button
          >
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import FormBlock from '@/components/Form/FormBlock';
import { getBookCateApi, saveBookApi, editBookApi, getBookByIdApi } from '@/api/bookApi';
import { getAllCateApi } from '@/api/cateApi';
import { uploadImagePlusApi } from '@/api/commonApi';
import rules from '@/utils/validate';

export default {
  components: { FormBlock },
  data() {
    return {
      id: '',
      formData: {
        // 基本信息
        book: {
          name: '', // 图书名称
          author: '', // 图书作者
          bookCateId: '', // 图书类别id
          goodsCateId: '', // 图书所属店内分类id
          press: '', // 出版社名称
          pubDate: '', // 出版时间
          size: '', // 开本
          pages: '', // 页数
          status: 1, // 售卖状态：0-禁售，1-启售
          inventory: 0, // 库存
          cover: '', // 封面
          coverUrl: ''
        },
        // 版权信息
        bookInfo: {
          isbn: '', // ISBN 码
          barCode: '', // 条形码
          binding: '', // 装帧
          version: '', // 版次
          volume: '', // 册数
          weight: '', // 重量
          printTimes: '' // 印刷次数
        },
        // 详细信息
        bookDetail: {
          featureDesc: '', // 本书特色
          outline: '', // 图书简介
          authorDesc: '' // 作者简介
        },
        // 费用信息
        bookCost: {
          borrowCost: 0,
          freeDay: 0,
          packingCost: 0,
          deposit: 0
        }
      },
      // 表单信息所遵循的规则
      rules: {
        book: {
          name: [{ required: true, message: '请输入图书名称', trigger: 'blur' }],
          author: [{ required: true, message: '请输入作者名称', trigger: 'blur' }],
          bookCateId: [{ required: true, message: '请选择图书类别', trigger: 'change' }],
          goodsCateId: [{ required: true, message: '请选择店内分类', trigger: 'change' }],
          press: [{ required: true, message: '请输入出版社名称', trigger: 'blur' }],
          pubDate: [{ required: true, message: '请输入出版时间', trigger: 'blur' }],
          cover: [{ required: true, message: '请输入选择图书封面', trigger: 'blur' }]
        },
        bookInfo: {
          isbn: [{ required: true, validator: rules.checkISBN, trigger: 'blur' }],
          barCode: [{ required: true, message: '请输入图书条形码', trigger: 'blur' }]
        },
        bookCost: {
          borrowCost: [{ required: true, message: '请输入借阅费', trigger: 'blur' }],
          deposit: [{ required: true, message: '请输入押金', trigger: 'blur' }]
        }
      },
      // 图书类别
      bookCate: [],
      // 店内分类
      goodsCate: [],
      // 已上传封面的预览图
      previewCoverUrl: '',
      // 是否显示已上传封面的预览图
      previewCoverVisible: false,
      // 当前窗口类型
      actionType: 'add',
      // 日期选择器的设置
      pickerOptions: {
        // 禁用未来的日期
        disabledDate(time) {
          return time.getTime() > Date.now();
        }
      }
    };
  },
  created() {
    this.id = this.$route.query.id;
    this.actionType = this.id ? 'edit' : 'add';
    this.getBookCate();
    this.getGoodsCate();
    if (this.id) {
      this.getBookMessById(this.id);
    }
  },
  methods: {
    /**
     * 根据id获取图书信息
     */
    getBookMessById(id) {
      getBookByIdApi(id).then(
        res => {
          if (res.data.flag) {
            this.formData = res.data.data;
          } else {
            this.$showMsgs('图书信息获取失败', { type: 'error' });
          }
        },
        err => {
          console.log('getBookById err =>', err);
        }
      );
    },
    /**
     * 自定义上传函数
     */
    uploadImagePlusApi,
    /**
     * 获取图书类别信息
     */
    getBookCate() {
      getBookCateApi().then(
        res => {
          this.bookCate = res.data.data;
        },
        err => {
          console.log('getBookCate err => ', err);
        }
      );
    },
    /**
     * 获取店内分类信息
     */
    getGoodsCate() {
      let storeId = this.$store.state.businessInfo.id;
      getAllCateApi(storeId).then(
        res => {
          this.goodsCate = res.data.data;
        },
        err => {
          console.log('getGoodsCate err => ', err);
        }
      );
    },
    /**
     * 图片上传成功后的操作
     */
    uploadCoverSuccess(res, file) {
      if (res.status === 200) {
        // 图片上传成功
        this.formData.book.coverUrl = res.data.data.url;
        this.formData.book.cover = res.data.data.uri;
      } else {
        // 图片上传失败
        this.$message.error('图片上传失败，请稍后重试');
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
      this.formData.book.coverUrl = '';
      this.formData.book.cover = '';
    },
    /**
     * 显示图书封面
     */
    handlePreviewCover() {
      this.previewCoverUrl = this.formData.book.coverUrl;
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
    submitForm(st) {
      // 表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          // 验证通过
          if (this.actionType === 'add') {
            // 添加操作
            this.addBook(st);
          } else {
            // 修改操作
            // 修改成功后返回 - this.goBack()
            this.editBook();
          }
        } else {
          // 验证失败
        }
      });
    },
    /**
     * 添加图书
     */
    addBook(st) {
      saveBookApi(this.formData).then(
        res => {
          console.log('book res => ', res);
          if (res.data.flag) {
            if (!st) {
              // 保存
              this.$showMsgs('图书添加成功', {
                type: 'success',
                closeFunc: () => {
                  this.goBack();
                }
              });
            } else {
              // 保存并继续
              this.$showMsgs('图书添加成功', {
                type: 'success',
                closeFunc: () => {
                  this.resetForm();
                  this.book.coverUrl = '';
                }
              });
            }
          } else {
            this.$showMsgs('图书添加失败，请稍后重试', { type: 'error' });
          }
        },
        err => {
          console.log('addBook err => ', err);
        }
      );
    },
    /**
     * 修改图书
     */
    editBook() {
      editBookApi(this.formData).then(
        res => {
          if (res.data.flag) {
            this.$showMsgs('图书修改成功', {
              type: 'success',
              closeFunc: () => {
                this.goBack();
              }
            });
          } else {
            this.$showMsgs('图书修改失败，请稍后再试', {
              type: 'error'
            });
          }
        },
        err => {
          console.log('edit book err', err);
        }
      );
    },
    /**
     * 重置表单
     */
    resetForm() {
      this.$refs.form.resetFields();
    }
  }
  // watch: {
  //   formData: {
  //     handler(val) {
  //       // console.log('表单数据 ==>', val);
  //     },
  //     deep: true
  //   }
  // }
};
</script>

<style lang="less" scoped>
.add-box {
  .form-line {
    display: flex;
    :deep(.number) {
      .el-input-number__decrease,
      .el-input-number__increase {
        display: none;
      }
      .el-input__inner {
        padding: 0 15px;
        text-align: left;
      }
    }
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
  }
}
</style>

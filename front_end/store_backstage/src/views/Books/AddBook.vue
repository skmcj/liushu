<template>
  <div class="main-box add-box">
    <el-form :model="formData" :rules="rules" ref="form" label-width="188px" class="form">
      <FormBlock title="基本信息">
        <div class="block-content">
          <div class="form-line">
            <el-form-item label="图书名称：" prop="baseInfo.name">
              <el-input v-model="formData.baseInfo.name" placeholder="请输入图书名称" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="图书类别：" prop="baseInfo.book_cate_id">
              <el-select v-model="formData.baseInfo.book_cate_id" placeholder="请选择图书类别" style="width: 300px">
                <el-option v-for="item in book_cate" :key="item.id" :label="item.name" :value="item.id"> </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="作者：" prop="baseInfo.author">
              <el-input v-model="formData.baseInfo.author" placeholder="请输入作者名称" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="店内分类：" prop="baseInfo.goods_cate_id">
              <el-select v-model="formData.baseInfo.goods_cate_id" placeholder="请选择店内分类" style="width: 300px">
                <el-option v-for="item in goods_cate" :key="item.id" :label="item.name" :value="item.id"> </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="出版社：" prop="baseInfo.press">
              <el-input v-model="formData.baseInfo.press" placeholder="请输入作者名称" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="出版时间：" prop="baseInfo.pub_date">
              <el-date-picker
                v-model="formData.baseInfo.pub_date"
                type="date"
                placeholder="请选择出版时间"
                :picker-options="pickerOptions"
                prefix-icon="icon fbookfont ic-calendar"
                style="width: 300px">
              </el-date-picker>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="开本：" prop="baseInfo.size" style="width: 488px">
              <el-input v-model="formData.baseInfo.size" placeholder="请输入图书规格" style="width: 132px"></el-input>
              <span class="unit">开</span>
            </el-form-item>
            <el-form-item label="开本：" prop="baseInfo.pages" style="width: 488px">
              <el-input
                v-model.number="formData.baseInfo.pages"
                placeholder="请输入图书页数"
                style="width: 132px"></el-input>
              <span class="unit">页</span>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="售卖状态：" prop="baseInfo.status" style="width: 488px">
              <el-switch
                v-model="formData.baseInfo.status"
                active-color="var(--primary)"
                :active-value="1"
                :inactive-value="0"
                :active-text="formData.baseInfo.status ? '启售' : '禁售'"></el-switch>
            </el-form-item>
            <el-form-item label="库存：" prop="baseInfo.inventory" style="width: 488px">
              <el-input-number size="small" v-model="formData.baseInfo.inventory" :min="0"></el-input-number>
              <span class="unit">本</span>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="图书封面：" prop="baseInfo.cover" class="upload-item" style="width: 488px">
              <el-upload
                class="cover-uploader"
                drag
                action="#"
                :disabled="formData.baseInfo.cover != ''"
                :show-file-list="false"
                :on-success="uploadCoverSuccess"
                :on-error="uploadCoverError"
                :before-upload="beforeCoverUpload">
                <div v-if="formData.baseInfo.cover" class="cover">
                  <img :src="formData.baseInfo.cover" @click.stop="handlePreviewCover" />
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
            <el-form-item label="ISBN：" prop="copyrightInfo.isbn">
              <el-input
                v-model.trim="formData.copyrightInfo.isbn"
                placeholder="请输入图书ISBN"
                style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="条形码：" prop="copyrightInfo.bar_code">
              <el-input
                v-model="formData.copyrightInfo.bar_code"
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
            <el-form-item label="装帧：" prop="copyrightInfo.binding">
              <el-input
                v-model="formData.copyrightInfo.binding"
                placeholder="请输入图书装帧类型"
                style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item label="版次：" prop="copyrightInfo.version" style="width: 488px">
              <el-input
                v-model="formData.copyrightInfo.version"
                placeholder="请输入版次"
                style="width: 132px"></el-input>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="册数：" prop="copyrightInfo.volume" style="width: 488px">
              <el-input
                v-model="formData.copyrightInfo.volume"
                placeholder="请输入册数"
                style="width: 132px"></el-input>
            </el-form-item>
            <el-form-item label="重量：" prop="copyrightInfo.weight" style="width: 488px">
              <el-input
                v-model="formData.copyrightInfo.weight"
                placeholder="请输入图书重量"
                style="width: 132px"></el-input>
              <span class="unit en">g</span>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="印刷次数：" prop="copyrightInfo.print_times" style="width: 488px">
              <el-input
                v-model="formData.copyrightInfo.print_times"
                placeholder="请输入印刷次数"
                style="width: 132px"></el-input>
            </el-form-item>
          </div>
        </div>
      </FormBlock>
      <FormBlock title="详细信息">
        <div class="block-content">
          <div class="form-line">
            <el-form-item label="本书特色：" prop="detailedInfo.feature_desc">
              <el-input
                type="textarea"
                v-model="formData.detailedInfo.feature_desc"
                placeholder="请简短地介绍本书的特色"
                style="width: 800px"
                class="h-82"></el-input>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="图书简介：" prop="detailedInfo.outline">
              <el-input
                type="textarea"
                v-model="formData.detailedInfo.outline"
                placeholder="请输入图书的内容简介"
                style="width: 800px"
                class="h-82"></el-input>
            </el-form-item>
          </div>
          <div class="form-line">
            <el-form-item label="作者简介：" prop="detailedInfo.author_desc">
              <el-input
                type="textarea"
                v-model="formData.detailedInfo.author_desc"
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
            <el-form-item label="借阅费：" prop="costInfo.borrow_cost" style="width: 488px">
              <el-input
                v-model="formData.copyrightInfo.borrow_cost"
                placeholder="请输入借阅费"
                style="width: 132px"></el-input>
              <span class="unit">元/天</span>
            </el-form-item>
            <el-form-item label="免借阅费天数：" prop="costInfo.free_days" style="width: 488px">
              <el-input
                v-model="formData.copyrightInfo.free_days"
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
            <el-form-item label="包装费：" prop="costInfo.packing_cost" style="width: 488px">
              <el-input
                v-model="formData.copyrightInfo.packing_cost"
                placeholder="请输入包装费"
                style="width: 132px"></el-input>
              <span class="unit">天</span>
            </el-form-item>
          </div>
        </div>
      </FormBlock>
      <div class="btns">
        <el-form-item label-width="0">
          <el-button @click="goBack">取消</el-button>
          <el-button type="primary" @click="submitForm('form', false)">保存</el-button>
          <el-button v-if="actionType == 'add'" type="primary" class="continue" @click="submitForm('form', true)"
            >保存并继续添加</el-button
          >
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import FormBlock from '@/components/Form/FormBlock';
import { getBookCateAPI } from '@/api/bookAPI';
import { getGoodsCateAPI } from '@/api/shopAPI';
import rules from '@/utils/validate';

export default {
  components: { FormBlock },
  data() {
    return {
      formData: {
        // 基本信息
        baseInfo: {
          name: '', // 图书名称
          author: '', // 图书作者
          book_cate_id: '', // 图书类别id
          goods_cate_id: '', // 图书所属店内分类id
          press: '', // 出版社名称
          pub_date: '', // 出版时间
          size: '', // 开本
          pages: '', // 页数
          status: 1, // 售卖状态：0-禁售，1-启售
          inventory: 0, // 库存
          cover: '' // 封面
        },
        // 版权信息
        copyrightInfo: {
          isbn: '', // ISBN 码
          bar_code: '', // 条形码
          binding: '', // 装帧
          version: '', // 版次
          volume: '', // 册数
          weight: '', // 重量
          print_times: '' // 印刷次数
        },
        // 详细信息
        detailedInfo: {
          feature_desc: '', // 本书特色
          outline: '', // 图书简介
          author_desc: '' // 作者简介
        },
        // 费用信息
        costInfo: {
          borrow_cost: '',
          free_days: 0,
          packing_cost: 0
        }
      },
      // 表单信息所遵循的规则
      rules: {
        baseInfo: {
          name: [{ required: true, message: '请输入图书名称', trigger: 'blur' }],
          author: [{ required: true, message: '请输入作者名称', trigger: 'blur' }],
          book_cate_id: [{ required: true, message: '请选择图书类别', trigger: 'change' }],
          goods_cate_id: [{ required: true, message: '请选择店内分类', trigger: 'change' }],
          press: [{ required: true, message: '请输入出版社名称', trigger: 'blur' }],
          pub_date: [{ required: true, message: '请输入出版时间', trigger: 'blur' }],
          cover: [{ required: true, message: '请输入选择图书封面', trigger: 'blur' }]
        },
        copyrightInfo: {
          isbn: [{ required: true, validator: rules.checkISBN, trigger: 'blur' }],
          bar_code: [{ required: true, message: '请输入图书条形码', trigger: 'blur' }]
        },
        costInfo: {
          borrow_cost: [{ required: true, message: '请输入借阅费', trigger: 'blur' }]
        }
      },
      // 图书类别
      book_cate: [],
      // 店内分类
      goods_cate: [],
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
    this.getBookCate();
    this.getGoodsCate();
  },
  methods: {
    /**
     * 获取图书类别信息
     */
    getBookCate() {
      getBookCateAPI().then(
        res => {
          this.book_cate = res.data;
        },
        err => {}
      );
    },
    /**
     * 获取店内分类信息
     */
    getGoodsCate() {
      getGoodsCateAPI().then(
        res => {
          this.goods_cate = res.data;
        },
        err => {}
      );
    },
    /**
     * 图片上传成功后的操作
     */
    uploadCoverSuccess(res, file) {
      this.formData.baseInfo.cover = URL.createObjectURL(file.raw);
    },
    uploadCoverError(res, file) {
      console.log(res, file);
      this.$message.error('网络繁忙，请稍后再试！');
      // TODO: 后续删除
      this.formData.baseInfo.cover = URL.createObjectURL(file.raw);
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
      this.formData.baseInfo.cover = '';
    },
    /**
     * 显示图书封面
     */
    handlePreviewCover() {
      this.previewCoverUrl = this.formData.baseInfo.cover;
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
    submitForm(formName, st) {
      // 表单验证
      this.$refs[formName].validate(valid => {
        if (valid) {
          // 验证通过
          if (this.actionType === 'add') {
            // 添加操作
            // 添加成功后判断 st - 返回还是继续添加
            this.goBack();
          } else {
            // 修改操作
            // 修改成功后返回 - this.goBack()
            this.goBack();
          }
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
  },
  watch: {
    formData: {
      handler(val) {
        console.log('表单数据 ==>', val);
      },
      deep: true
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
  }
}
</style>

<template>
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box">
      <div class="content">
        <!-- 推荐图书 -->
        <ContentBlock class="block" title="推荐图书" :handleChange="handleChangeBook">
          <div class="card-box">
            <!-- 渲染推荐图书 -->
            <BookCard
              v-for="index of 6"
              :key="index"
              :cover="book.cover"
              :title="book.title"
              :author="book.author"
              :profile="book.profile"
              :store-name="book.storeName" />
          </div>
        </ContentBlock>
        <!-- 推荐商家 -->
        <ContentBlock class="block" title="推荐商家" :handleChange="handleChangeShop">
          <div class="card-box">
            <ShopCard v-for="index of 6" :key="index" :cover="shop.cover" :name="shop.storeName" :score="shop.score" />
          </div>
        </ContentBlock>
        <!-- 分类图书 -->
        <ContentBlock class="block" title="分类图书" :handleChange="handleChangeCate">
          <template #tools>
            <el-select class="tool-select" v-model="selectCate" placeholder="请选择" @change="handleSelectCate">
              <el-option v-for="item in bookCate" :key="item.id" :label="item.name" :value="item.id"> </el-option>
            </el-select>
          </template>
          <div class="card-box">
            <BookCard
              v-for="index of 6"
              :key="index"
              :cover="book.cover"
              :title="book.title"
              :author="book.author"
              :profile="book.profile"
              :store-name="book.storeName" />
          </div>
        </ContentBlock>
      </div>
      <Tool class="tools" :top-visible-height="500" />
    </div>
  </div>
</template>

<script>
import ContentBlock from '@/components/Common/ContentBlock';
import BookCard from '@/components/Card/BookCard';
import ShopCard from '@/components/Card/ShopCard';
import Wave from '@/components/Common/Wave';
import Tool from '@/components/Tool/Tool';

export default {
  components: {
    ContentBlock,
    BookCard,
    ShopCard,
    Wave,
    Tool
  },
  created() {
    this.getRecommendingBook();
  },
  data() {
    return {
      // 推荐图书列表
      books: [],
      // 推荐商家列表
      shops: [],
      // 分类图书列表
      cateBooks: [],
      // 图书类别
      bookCate: [
        {
          id: 1,
          name: '文学',
          sort: 1
        },
        {
          id: 2,
          name: '社科',
          sort: 2
        },
        {
          id: 3,
          name: '少儿',
          sort: 3
        },
        {
          id: 4,
          name: '艺术',
          sort: 4
        },
        {
          id: 5,
          name: '生活',
          sort: 5
        },
        {
          id: 6,
          name: '文教',
          sort: 6
        },
        {
          id: 7,
          name: '经管',
          sort: 7
        },
        {
          id: 8,
          name: '计算机',
          sort: 8
        },
        {
          id: 9,
          name: '科技',
          sort: 9
        }
      ],
      selectCate: 1,
      // 测试图书数据
      book: {
        cover: null,
        title: '数学之美',
        author: '吴军著',
        profile:
          '这是一本备受推崇的经典科普作品，被央视推荐为数学学科的敲门砖，是信息领域大学生的必读好书。 数学既是对于自然界事实的总结和归纳，又是抽象思考的结果。在《数学之美》里，吴军博士集中阐述了他对数学和信息处理这些专业学科的理解，把数学在IT领域，特别是语音识别、自然语言处理和信息搜索等方面的美丽之处予以了精彩表达，这些都是智能时代的热门技术话题。 本书还用了大量篇幅介绍各个领域的典故，是文科生也可以看懂的科普读物。成为一个领域的大师有其偶然性，但更有其必然性。其必然性就是大师们的思维方法。通过本书，可以了解他们的平凡与卓越，理解他们取得成功的原因，感受那些真正懂得数学之美的人们所拥有的美好人生。 本书先后荣获国家图书馆第八届文津图书奖、第五届中华优秀出版物奖图书提名奖、入选广电总局“2014年向全国青少年推荐百种优秀图书书目”、*版曾荣获2012-2013年度全行业畅销书，《数学之美》多次被中央电视台、学习强国平台、新华书店推选为必读书。《数学之美》给广大读者，尤其是在校读大学甚至读高中的年轻人带去了美的数学启示，作者更希望中国做工程的年轻人，能够从《数学之美》中体会到在信息技术行业做事情的正确方法，以便在职业和生活上都获得成功。 第三版增加了三章新内容，分别介绍当今非常热门的三个主题：区块链的数学基础，量子通信的原理，以及人工智能的数学极限。',
        storeName: '片刻书店'
      },
      // 测试商家数据
      shop: {
        cover: null,
        storeName: '片刻书店',
        score: null
      }
    };
  },
  methods: {
    /**
     * 根据当前屏幕大小计算请求数量
     */
    getReqNum() {
      const width = document.body.scrollWidth || document.body.offsetWidth;
      if (width < 1367) {
        return 2;
      }
      if (width >= 1367 && width < 1701) {
        return 3;
      }
      if (width >= 1701 && width < 2096) {
        return 4;
      }
      if (width >= 2096) {
        return 5;
      }
    },
    /**
     * 获取推荐图书
     */
    getRecommendingBook() {
      let num = this.getReqNum();
      // 请求 num * 2 的图书
    },
    /**
     * 处理点击换一换按钮
     */
    handleChangeBook() {
      console.log('change book');
    },
    handleChangeCate() {
      console.log('change cate');
    },
    handleChangeShop() {
      console.log('change shop');
    },
    /**
     * 选择分类
     */
    handleSelectCate() {
      console.log('cate =>', this.selectCate);
    }
  }
};
</script>

<style lang="less" scoped>
.content {
  width: 75%;
  .block {
    & + .block {
      margin-top: 75px;
    }
  }
  .tool-select {
    margin-right: 12px;
    width: 120px;
    :deep(.el-input--suffix) {
      .el-input__inner {
        font-family: AlibabaPuHuiTi-2-55-Regular, 'Noto Sans';
        height: auto;
        line-height: 18px;
        padding: 5px 12px;
        padding-right: 30px;
        border-radius: 5px;
        font-size: 12px;
        color: var(--primary-text);
        background-color: #fff;
        border: 1px solid #dfdfdf;
        box-shadow: 0 0 5px 2px rgba(190, 194, 188, 0.1) inset;
      }
      .el-input__suffix-inner {
        .el-input__icon {
          line-height: normal;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
  }
}
</style>

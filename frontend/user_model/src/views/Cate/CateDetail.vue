<template>
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box">
      <div class="content">
        <div class="cate-list">
          <div
            class="cate-item"
            v-for="(item, index) in bookCate"
            :key="'cate-' + index"
            :class="{ 'is-active': cateId === item.id }"
            @click="handleClickCate(item.id)">
            <span class="cate-name">{{ item.name }}</span>
          </div>
        </div>
        <div class="book-block">
          <div class="book-list">
            <div class="title">{{ cateName }}</div>
            <div class="book-box" ref="bookBox">
              <BookCard
                v-for="(book, id) in bookList"
                :key="'book' + id"
                :cover="book.cover"
                :title="book.title"
                :author="book.author"
                :profile="book.profile"
                :store-name="book.storeName" />
            </div>
            <div class="book-page">
              <el-pagination
                layout="total, prev, pager, next"
                :total="total"
                :page-count="total / pageSize"
                :page-size="pageSize"
                :current-page="currentPage"
                @current-change="handleCurrentChange"
                hide-on-single-page>
              </el-pagination>
            </div>
          </div>
          <div class="book-rank">
            <div class="rank-title">热门</div>
            <div class="rank-content">
              <div class="rank-item" v-for="(item, index) in bookRank" :key="'book-rank-' + index">
                <span class="rank-no" :class="{ 'is-hot': index < 3 }">{{ index + 1 }}</span>
                <span class="rank-book-name">{{ item.title }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BookCard from '@/components/Card/BookCard';
import Wave from '@/components/Common/Wave';

export default {
  components: {
    Wave,
    BookCard
  },
  data() {
    return {
      cateId: 1,
      cateName: '文学',
      cateObj: {},
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
      // 图书列表
      bookList: [],
      // 图书排行榜
      bookRank: [],
      currentPage: 1,
      pageSize: 15,
      total: 100
    };
  },
  created() {
    this.cateId = parseInt(this.$route.query.id);
    if (this.cateId) {
      // 获取相应请求的信息
      this.getCateObj();
      this.getBookPageByCate();
      this.getBookRank();
      this.cateName = this.cateObj[this.cateId];
    }
  },
  methods: {
    /**
     * 根据当前屏幕大小计算请求数量
     */
    getReqNum() {
      const width = document.body.scrollWidth || document.body.offsetWidth;
      if (width < 1417) {
        return 2;
      }
      if (width >= 1417 && width < 1840) {
        return 3;
      }
      if (width >= 1840 && width < 2377) {
        return 4;
      }
      if (width >= 2377) {
        return 5;
      }
    },
    /**
     * 获取分类对象
     */
    getCateObj() {
      this.bookCate.forEach(item => {
        this.cateObj[item.id] = item.name;
      });
    },
    /**
     * 点击分类
     */
    handleClickCate(id) {
      this.cateId = id;
      this.cateName = this.cateObj[this.cateId];
    },
    // 页码改变时
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getBookPageByCate();
    },
    /**
     * 获取相应的图书列表
     */
    getBookPageByCate() {
      let num = this.getReqNum();
      this.pageSize = num * 5;
      this.bookList = this.getBookDataTest(this.pageSize);
    },
    /**
     * 获取排行榜信息
     */
    getBookRank() {
      this.bookRank = this.getBookDataTest(10);
    },
    /**
     * 用于测试
     */
    getBookDataTest(num) {
      let list = [];
      // 测试图书数据
      let book = {
        cover: null,
        title: '数学之美',
        author: '吴军著',
        profile:
          '这是一本备受推崇的经典科普作品，被央视推荐为数学学科的敲门砖，是信息领域大学生的必读好书。 数学既是对于自然界事实的总结和归纳，又是抽象思考的结果。在《数学之美》里，吴军博士集中阐述了他对数学和信息处理这些专业学科的理解，把数学在IT领域，特别是语音识别、自然语言处理和信息搜索等方面的美丽之处予以了精彩表达，这些都是智能时代的热门技术话题。 本书还用了大量篇幅介绍各个领域的典故，是文科生也可以看懂的科普读物。成为一个领域的大师有其偶然性，但更有其必然性。其必然性就是大师们的思维方法。通过本书，可以了解他们的平凡与卓越，理解他们取得成功的原因，感受那些真正懂得数学之美的人们所拥有的美好人生。 本书先后荣获国家图书馆第八届文津图书奖、第五届中华优秀出版物奖图书提名奖、入选广电总局“2014年向全国青少年推荐百种优秀图书书目”、*版曾荣获2012-2013年度全行业畅销书，《数学之美》多次被中央电视台、学习强国平台、新华书店推选为必读书。《数学之美》给广大读者，尤其是在校读大学甚至读高中的年轻人带去了美的数学启示，作者更希望中国做工程的年轻人，能够从《数学之美》中体会到在信息技术行业做事情的正确方法，以便在职业和生活上都获得成功。 第三版增加了三章新内容，分别介绍当今非常热门的三个主题：区块链的数学基础，量子通信的原理，以及人工智能的数学极限。',
        storeName: '片刻书店'
      };
      for (let i = 0; i < num; i++) {
        list.push(book);
      }
      return list;
    }
  }
};
</script>

<style lang="less" scoped>
.content {
  .cate-list {
    width: 100%;
    height: 96px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: var(--white);
    border-radius: 12px;
    .cate-item {
      z-index: 3;
      position: relative;
      cursor: pointer;
      user-select: none;
      overflow: hidden;
      box-sizing: border-box;
      padding: 5px 30px;
      border-radius: 8px;
      background-color: var(--grey-label-bg);
      transition: color 0.8s ease-in-out;
      & + .cate-item {
        margin-left: 25px;
      }
      .cate-name {
        position: relative;
        z-index: 3;
      }
      &::after {
        z-index: 1;
        position: absolute;
        left: 0;
        bottom: -100%;
        content: '';
        display: block;
        width: 100%;
        height: 100%;
        background-color: var(--primary);
        transition: bottom 0.8s ease-in-out;
      }
      &.is-active {
        color: var(--white);
        // background-color: var(--primary);
        &::after {
          bottom: 0;
        }
      }
    }
  }
  .book-block {
    margin-top: 64px;
    padding: 45px 0 120px 0;
    background-color: var(--white);
    border-radius: 12px;
    box-sizing: border-box;
    width: 100%;
    display: flex;
    justify-content: center;
    .book-list {
      width: 70%;
      height: 1080px;
      margin-right: 36px;
      .title {
        font-size: 30px;
        font-weight: bold;
        margin-left: 5px;
      }
      .book-box {
        :deep(.sk-book-card) {
          width: 240px;
          height: 148px;
          .sk-book-card-content {
            .sk-book-card__profile {
              -webkit-line-clamp: 3;
            }
          }
        }
      }
      .book-page {
        margin-top: 64px;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    .book-rank {
      display: flex;
      flex-direction: column;
      width: 21%;
      height: 560px;
      .rank-title {
        font-size: 24px;
      }
      .rank-content {
        margin-top: 24px;
        padding: 12px 14px 36px 14px;
        box-sizing: border-box;
        border-radius: 8px;
        background-color: #f6f6f6;
        .rank-item {
          width: 100%;
          height: 30px;
          display: flex;
          align-items: center;
          justify-content: space-between;
          & + .rank-item {
            margin-top: 8px;
          }
          .rank-no {
            font-size: 10px;
            width: 24px;
            height: 24px;
            border-radius: 5px;
            background-color: var(--grey);
            color: var(--white);
            display: flex;
            align-items: center;
            justify-content: center;
            &.is-hot {
              background-color: var(--primary);
            }
          }
          .rank-book-name {
            cursor: pointer;
            font-size: 12px;
            padding-left: 12px;
            display: flex;
            align-items: center;
            width: 86%;
            height: 100%;
            border-radius: 5px;
            box-sizing: border-box;
            background-color: var(--white);
          }
        }
      }
    }
  }
}
</style>

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
                v-for="book in bookList"
                :key="'book-' + book.id"
                :cover="book.coverUrl"
                :title="book.name"
                :author="book.author"
                :profile="book.outline"
                :store-name="book.storeName"
                :handle-book="() => clickBook(book.id)"
                :handle-store="() => clickStore(book.storeId)" />
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
                <span class="rank-book-name">{{ item.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <Tool class="tools" :top-visible-height="500" />
    </div>
  </div>
</template>

<script>
import BookCard from '@/components/Card/BookCard';
import Wave from '@/components/Common/Wave';
import Tool from '@/components/Tool/Tool';
import { getBookCateApi } from '@/api/cateApi';
import { getBookPageByCateApi, getBookRankByCateApi } from '@/api/bookApi';

export default {
  components: {
    Tool,
    Wave,
    BookCard
  },
  data() {
    return {
      cateId: 1,
      cateName: '文学',
      cateObj: {},
      // 图书类别
      bookCate: [],
      // 图书列表
      bookList: [],
      // 图书排行榜
      bookRank: [],
      currentPage: 1,
      pageSize: 15,
      total: 0
    };
  },
  created() {
    this.cateId = parseInt(this.$route.query.id);
    if (this.cateId) {
      // 获取相应请求的信息
      this.initPageMess();
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
    initPageMess() {
      this.getBookCate();
      this.getBookPageByCate();
      this.getBookRank();
    },
    /**
     * 获取图书分类
     */
    getBookCate() {
      getBookCateApi().then(res => {
        if (res.data.flag) {
          this.bookCate = res.data.data;
          this.getCateObj();
          this.cateName = this.cateObj[this.cateId];
        } else {
          this.$showMsg('分类信息获取失败，请刷新重试', { type: 'warning' });
        }
      });
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
      this.currentPage = 1;
      this.getBookPageByCate();
      this.getBookRank();
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
      getBookPageByCateApi(this.cateId, this.currentPage, this.pageSize).then(res => {
        if (res.data.flag) {
          this.bookList = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.$showMsg('图书获取失败，请稍后再试', { type: 'warning' });
        }
      });
    },
    /**
     * 获取排行榜信息
     */
    getBookRank() {
      getBookRankByCateApi(this.cateId, 10).then(res => {
        if (res.data.flag) {
          this.bookRank = res.data.data;
        } else {
          this.$showMsg('图书榜单获取失败', { type: 'warning' });
        }
      });
    },
    /**
     * 点击图书
     */
    clickBook(id) {
      this.$router.push({ path: '/book', query: { id } });
    },
    /**
     * 点击商家
     */
    clickStore(id) {
      this.$router.push({ path: '/shop', query: { id } });
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
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
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

<template>
  <div class="search-book">
    <template v-if="$isEmpty(books)">
      <SvgPage style="margin-top: 48px" name="no-result" :img-width="480" text="查无相关内容"></SvgPage>
    </template>
    <div class="card-box">
      <BookCard
        v-for="(book, index) in books"
        :key="'book-recommend-' + index"
        :cover="book.coverUrl"
        :title="book.name"
        :author="book.author"
        :profile="book.outline ? book.outline : '暂无'"
        :store-name="book.storeName"
        :handle-book="() => clickBook(book.id)"
        :handle-store="() => clickStore(book.storeId)" />
    </div>
    <el-pagination
      class="page-tool"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[9, 12, 15, 20]"
      :page-size="pageSize"
      hide-on-single-page
      layout="total, sizes, prev, pager, next"
      :total="total">
    </el-pagination>
  </div>
</template>

<script>
import BookCard from '@/components/Card/BookCard';
import SvgPage from '@/components/Common/SvgPage';
import { searchBookApi } from '@/api/searchApi';

export default {
  components: {
    BookCard,
    SvgPage
  },
  data() {
    return {
      books: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      keyword: ''
    };
  },
  created() {
    // this.searchBook();
    this.keyword = this.$store.state.keyword;
    if (this.keyword) {
      this.searchBook();
    }
  },
  mounted() {
    this.$bus.$on('searchBook', this.search);
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
    search(val) {
      // this.keyword = val;
      this.searchBook();
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
    },
    searchBook() {
      if (!this.keyword) return;
      this.pageSize = this.getReqNum() * 4;
      searchBookApi(this.keyword, this.currentPage, this.pageSize).then(res => {
        if (res.data.flag) {
          this.books = res.data.data.records;
          this.total = res.data.data.total;
        }
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.searchBook();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.searchBook();
    }
  }
};
</script>

<style lang="less" scoped>
.card-box {
  box-sizing: border-box;
  padding: 0 24px;
}
.page-tool {
  margin-top: 24px;
}
.search-book {
  display: flex;
  align-items: center;
  flex-direction: column;
}
</style>

<template>
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box">
      <div class="content">
        <template v-for="item in bookCate">
          <ContentBlock
            :id="item.name"
            :key="'cate-' + item.id"
            class="block"
            :more-tool="true"
            :title="item.name"
            :handleChange="() => handleChange(item.id)"
            :handleMore="() => handleMore(item.id)">
            <div class="card-box">
              <!-- 渲染推荐图书 -->
              <BookCard
                v-for="(book, id) in bookList[item.id]"
                :key="'book-' + item.id + '-' + id"
                :cover="book.coverUrl"
                :title="book.name"
                :author="book.author"
                :profile="book.outline"
                :store-name="book.storeName"
                :handle-book="() => clickBook(book.id)"
                :handle-store="() => clickStore(book.storeId)" />
            </div>
          </ContentBlock>
        </template>
      </div>
      <Tool
        class="tools"
        :cate="true"
        :cate-list="bookCate"
        :active-cate="activeCate"
        :listenCateHash="handleListenCateHash"
        :top-visible-height="500" />
    </div>
  </div>
</template>

<script>
import ContentBlock from '@/components/Common/ContentBlock';
import BookCard from '@/components/Card/BookCard';
import Wave from '@/components/Common/Wave';
import Tool from '@/components/Tool/Tool';
import { getBookCateApi } from '@/api/cateApi';
import { getBookByCateApi } from '@/api/bookApi';

export default {
  components: {
    ContentBlock,
    BookCard,
    Wave,
    Tool
  },
  created() {
    this.getBookCate();
  },
  mounted() {
    document.documentElement.style.scrollBehavior = 'smooth';
  },
  beforeDestroy() {
    document.documentElement.style.removeProperty('scroll-behavior');
  },
  data() {
    return {
      // 图书类别
      bookCate: [],
      bookList: {},
      activeCate: '#文学'
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
     * 获取图书类别
     */
    getBookCate() {
      getBookCateApi().then(res => {
        if (res.data.flag) {
          this.bookCate = res.data.data;
          // console.log('bookCate =>', this.bookCate);
          this.bookCate.forEach(item => {
            this.bookList[item.id] = [];
            this.handleChange(item.id);
          });
        } else {
          this.$showMsg('网络繁忙，请稍后再试', { type: 'warning' });
        }
      });
    },
    /**
     * 换一换
     * @param {Number} id 类别id
     */
    handleChange(id) {
      // console.log('id =>', id);
      // 根据id获取该类别的推荐图书
      // 获取每行显示条目
      let num = this.getReqNum();
      // 获取 num * 2 条数据
      getBookByCateApi(num * 2, id)
        .then(res => {
          if (res.data.flag) {
            this.bookList[id] = res.data.data;
            // console.log(id, this.bookList[id]);
          } else {
            this.$showMsg('网络繁忙，请稍后再试', { type: 'warning' });
          }
        })
        .finally(() => {
          this.$forceUpdate();
        });
    },
    /**
     * 更多
     */
    handleMore(id) {
      this.$router.push({ path: '/cate/detail', query: { id } });
    },
    handleListenCateHash(hash) {
      this.activeCate = hash;
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
.tools {
  :deep(.sk-tool-cate-list) {
    &:first-of-type {
      margin-bottom: 53px;
    }
  }
}
.content {
  width: 75%;
  .block {
    & + .block {
      margin-top: 75px;
    }
  }
}
</style>

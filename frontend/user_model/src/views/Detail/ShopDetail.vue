<template>
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box" id="shopContentBox">
      <div class="content" id="shopContent">
        <!-- 店铺详情 -->
        <ShopMessCard
          class="shop-mess"
          :style="cardStyle"
          size="primary"
          :name="shopData.bookstore.storeName"
          :cover="shopData.bookstore.coverUrl"
          :score="shopData.bookstore.score ? shopData.bookstore.score : 0"
          :amount="shopData.bookstore.mba"
          :label="shopData.bookstore.labelList"
          :notice="shopData.bookstore.notice"
          :is-coll="isColl"
          :address="shopData.bookstore.address"
          :business-hours="shopData.bookstore.businessHours"
          :license-imgs="[...shopData.bookstoreDetail.businessLicenseUrl, ...shopData.bookstoreDetail.licenseImgUrl]"
          :env-imgs="shopData.bookstoreDetail.envImgsUrl"
          :distribution="shopData.bookstore.distribution"
          :store-service="shopData.bookstore.storeService"
          @clickCollection="handleCollection" />
        <div class="tab-box" id="shopTabBox">
          <!-- 选项卡 -->
          <div class="tab-tit-panel" :style="navStyle">
            <!-- 左边导航组 -->
            <div class="nav-box">
              <div
                class="nav-item"
                id="detail"
                :class="{ 'is-active': activeTab === 'hot' }"
                @click.stop="handleNav('hot')">
                <span class="nav-context">热门</span>
                <span class="nav-bg"></span>
              </div>
              <div
                class="nav-item"
                id="comment"
                :class="{ 'is-active': activeTab === 'cate' }"
                @click.stop="handleNav('cate')">
                <span class="nav-context">分类</span>
                <span class="nav-bg"></span>
              </div>
            </div>
            <!-- 右边工具组 -->
            <div class="tool-box">
              <div class="search-box">
                <input
                  type="text"
                  v-model="searchContent"
                  placeholder="请输入关键词"
                  class="search-content"
                  @keyup.enter="handleSearch" />
                <i class="ic-search" @click.stop="handleSearch"></i>
              </div>
            </div>
          </div>
          <!-- 占位 -->
          <div class="tab-copy-tit-panel"></div>
          <Transition name="fade">
            <!-- 热门 -->
            <div class="tab-content hot" v-if="activeTab === 'hot'" key="hot-content">
              <div class="aside" id="shopRankBox">
                <div class="aside-title">门店借阅榜</div>
                <div class="aside-content">
                  <div
                    class="aside-item"
                    :class="{ 'is-hot': index < 3 }"
                    v-for="(item, index) in bookRank"
                    :key="'book-rank-' + index"
                    @click.stop="handleBookRank(item.id)">
                    <span class="num">{{ getSerialNo(index + 1) }}</span>
                    <span class="text">{{ item.name }}</span>
                    <i class="icon ic-more-1"></i>
                  </div>
                </div>
              </div>
              <div class="book-page">
                <SvgPage v-if="$isEmpty(bookPage)" name="no-content" text="没有相关内容" />
                <div v-else class="side-table">
                  <BookCard
                    class="card"
                    v-for="(book, index) in bookPage"
                    :key="'book-' + index"
                    :cover="book.coverUrl"
                    :title="book.name"
                    :author="book.author"
                    :profile="book.outline"
                    :store-name="book.storeName"
                    :handle-book="() => openBook(book.id)" />
                </div>
                <div class="page-tool">
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
            </div>
            <!-- 店内分类 -->
            <div class="tab-content cate" v-if="activeTab === 'cate'" key="cate-content">
              <div class="aside" id="shopCateBox">
                <div class="aside-title">分类</div>
                <div class="aside-content">
                  <div
                    class="aside-item mid"
                    :class="{ 'is-active': selectCate === item.id }"
                    v-for="(item, index) in goodsCate"
                    :key="'book-cate-' + index"
                    @click.stop="handleBookCate(item.id)">
                    <span class="text">{{ item.name }}</span>
                  </div>
                </div>
              </div>
              <div class="book-page">
                <SvgPage v-if="$isEmpty(bookPage)" name="no-content" text="没有相关内容" />
                <div v-else class="side-table">
                  <BookCard
                    class="card"
                    v-for="(book, index) in bookPage"
                    :key="'book-' + index"
                    :cover="book.coverUrl"
                    :title="book.name"
                    :author="book.author"
                    :profile="book.outline"
                    :store-name="book.storeName"
                    :handle-book="() => openBook(book.id)" />
                </div>
                <div class="page-tool">
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
            </div>
            <!-- 搜索结果 -->
            <div class="tab-content search" v-if="activeTab === 'search'" key="search-content">
              <!-- <SvgPage v-if="$isEmpty(searchResult)" text="没有相关内容" /> -->
              <div class="mid-table">
                <BookCard
                  class="card"
                  v-for="(book, index) in searchResult"
                  :key="'book-' + index"
                  :cover="book.coverUrl"
                  :title="book.name"
                  :author="book.author"
                  :profile="book.outline"
                  :store-name="book.storeName"
                  :handle-book="() => openBook(book.id)" />
              </div>
              <div class="page-tool">
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
          </Transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Wave from '@/components/Common/Wave';
import ShopMessCard from '@/components/Card/ShopMessCard';
import BookCard from '@/components/Card/BookCard';
import SvgPage from '@/components/Common/SvgPage';
import domHandle from '@/utils/domHandleUtil';
import {
  getShopMessByIdApi,
  getBookRankOfShopApi,
  getBookPageOfShopApi,
  getBookPageByCateOfShopApi,
  getBookPageBySearchOfShopApi
} from '@/api/shopAPi';
import { getGoodsCateApi } from '@/api/cateApi';

export default {
  components: {
    Wave,
    ShopMessCard,
    BookCard,
    SvgPage
  },
  data() {
    return {
      storeId: 0,
      width: 0,
      shopData: {
        bookstore: {},
        bookstoreDetail: {
          businessLicenseUrl: [],
          licenseImgUrl: []
        }
      },
      isColl: false,
      activeTab: 'hot',
      /** @type {HTMLElement} */
      contentBoxDom: {},
      /** @type {HTMLElement} */
      tabBoxDom: {},
      /* 搜索相关 */
      // 搜索关键词
      searchContent: '',
      // 结果列表
      searchResult: [],
      // 销量榜(前十)
      bookRank: [],
      // 热门、分类图书
      bookPage: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      // 分类
      goodsCate: [],
      selectCate: 0
    };
  },
  created() {
    this.storeId = this.$route.query.id;
    if (this.storeId) {
      this.initPageMess();
    }
  },
  mounted() {
    this.width = document.getElementById('shopContent').offsetWidth;
    this.contentBoxDom = document.getElementById('shopContentBox');
    this.tabBoxDom = document.getElementById('shopTabBox');
    // 监听滚动事件
    window.addEventListener('scroll', this.onScroll);
  },
  computed: {
    cardStyle() {
      let style = {};
      style.height = this.width * 0.25 + 'px';
      return style;
    },
    navStyle() {
      let style = {};
      style.width = this.tabBoxDom.clientWidth + 'px';
      return style;
    }
  },
  methods: {
    initPageMess() {
      this.getShopDetail();
      this.getBookRank();
      this.getBookPageByHot();
    },
    /**
     * 监听滚动
     */
    onScroll() {
      // 获取当前文档流的 scrollTop
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
      let tabTop = this.tabBoxDom.offsetTop + this.contentBoxDom.offsetTop;
      // 设置导航面板吸顶
      if (scrollTop > tabTop - 10) {
        domHandle.addClass(this.tabBoxDom, 'is-fixed');
      } else {
        domHandle.removeClass(this.tabBoxDom, 'is-fixed');
      }
    },
    /**
     * 根据当前屏幕大小计算请求数量
     */
    getReqNum() {
      const width = document.body.scrollWidth || document.body.offsetWidth;
      if (width < 1417) {
        return 2;
      }
      if (width >= 1417 && width < 2377) {
        return 3;
      }
      if (width >= 2377) {
        return 4;
      }
    },
    getSerialNo(num) {
      if (num < 10) return '0' + num;
      return num;
    },
    /**
     * 获取商家信息
     */
    getShopDetail() {
      getShopMessByIdApi(this.storeId).then(res => {
        if (res.data.flag) {
          this.shopData = res.data.data;
        }
      });
    },
    /**
     * 处理收藏按钮
     */
    handleCollection(flag) {
      console.log('点击收藏 =>', flag);
    },
    /**
     * 点击搜索
     */
    handleSearch() {
      this.activeTab = 'search';
      this.currentPage = 1;
      // console.log('search =>', this.searchContent);
      this.getBookPageBySearch();
    },
    /**
     * 点击导航
     */
    handleNav(text) {
      this.activeTab = text;
      if (text === 'hot') {
        this.currentPage = 1;
        this.getBookPageByHot();
      } else if (text === 'cate') {
        this.currentPage = 1;
        this.getGoodsCate();
      }
    },
    openBook(id) {
      this.$router.push({ path: '/book', query: { id } });
    },
    /**
     * 点击借阅榜项
     */
    handleBookRank(id) {
      this.openBook(id);
      // console.log('rank item =>', id);
    },
    /**
     * 点击图书分类
     */
    handleBookCate(id) {
      this.selectCate = id;
      this.getBookPageByCate();
      // console.log('cate item =>', id);
    },
    // 页码改变时
    handleCurrentChange(val) {
      this.currentPage = val;
      if (this.activeTab === 'hot') {
        this.getBookPageByHot();
      } else if (this.activeTab === 'cate') {
        this.getBookPageByCate();
      } else if (this.activeTab === 'search') {
        this.getBookPageBySearch();
      }
    },
    /**
     * 获取店内分类
     */
    getGoodsCate() {
      // this.goodsCate = [];
      // this.selectCate = this.goodsCate[0].id;
      getGoodsCateApi(this.storeId).then(res => {
        if (res.data.flag) {
          this.goodsCate = res.data.data;
          if (res.data.data.length > 0) {
            this.handleBookCate(res.data.data[0].id);
          }
        } else {
          this.$showMsg('店内分类获取失败', { type: 'warning' });
        }
      });
    },
    /**
     * 获取相应的图书列表
     */
    getBookPageByCate() {
      // 传入所选分类
      let num = this.getReqNum();
      this.pageSize = num * 5;
      getBookPageByCateOfShopApi(this.storeId, this.selectCate, this.currentPage, this.pageSize).then(res => {
        if (res.data.flag) {
          this.bookPage = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.$showMsg('图书获取失败', { type: 'warning' });
        }
      });
    },
    /**
     * 获取热门图书列表
     */
    getBookPageByHot() {
      let num = this.getReqNum();
      this.pageSize = num * 5;
      getBookPageOfShopApi(this.storeId, this.currentPage, this.pageSize).then(res => {
        if (res.data.flag) {
          this.bookPage = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.$showMsg('图书获取失败', { type: 'warning' });
        }
      });
    },
    /**
     * 搜索图书
     */
    getBookPageBySearch() {
      let num = this.getReqNum();
      this.pageSize = (num + 1) * 5;
      getBookPageBySearchOfShopApi(this.storeId, this.searchContent, this.currentPage, this.pageSize).then(res => {
        if (res.data.flag) {
          this.searchResult = res.data.data.records;
          this.total = res.data.data.total;
        } else {
          this.$showMsg('图书获取失败', { type: 'warning' });
        }
      });
    },
    /**
     * 获取排行榜信息
     */
    getBookRank() {
      getBookRankOfShopApi(this.storeId, 10).then(res => {
        if (res.data.flag) {
          this.bookRank = res.data.data;
        } else {
          this.$showMsg('榜单获取失败', { type: 'warning' });
        }
      });
    }
  },
  destroyed() {
    // 移除监听器，不然当该vue组件被销毁了，监听器还在就会出错
    window.removeEventListener('scroll', this.onScroll);
  }
};
</script>

<style lang="less" scoped>
.content {
  background-color: var(--white);
  border-radius: 12px;
  box-sizing: border-box;
  padding: 30px 72px 120px 72px;
  .shop-mess {
    width: 100%;
  }
  .tab-box {
    margin-top: 36px;
    width: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    box-sizing: border-box;
    &.is-fixed {
      .tab-tit-panel {
        position: fixed;
        top: 0;
      }
      .tab-copy-tit-panel {
        display: block;
      }
    }
    .tab-tit-panel {
      z-index: 560;
      display: flex;
      align-items: center;
      justify-content: space-between;
      // width: 100%;
      height: 64px;
      border-radius: 5px;
      background-color: #fff;
      overflow: hidden;
      box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
      .nav-box {
        height: 100%;
        display: flex;
        align-items: center;
        .nav-item {
          cursor: pointer;
          user-select: none;
          position: relative;
          height: 100%;
          box-sizing: border-box;
          padding: 0 72px;
          display: flex;
          align-items: center;
          & + .nav-item {
            margin-left: 24px;
          }
          .nav-context {
            z-index: 565;
            font-size: 18px;
            color: #474a4d;
            font-weight: bold;
            transition: color 0.3s ease-in-out;
          }
          .nav-bg {
            z-index: 564;
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 8px;
            border-radius: 5px;
            background-color: #4182a4;
            transition: height 0.5s ease-in-out;
          }
          &.is-active {
            .nav-context {
              color: #fff;
            }
            .nav-bg {
              height: 100%;
            }
          }
        }
      }
      .tool-box {
        user-select: none;
        display: flex;
        align-items: center;
        .search-box {
          width: 420px;
          height: 46px;
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-right: 42px;
          border-radius: 23px;
          padding: 0 23px;
          box-sizing: border-box;
          background-color: #f7f7f7;
          .search-content {
            outline: none;
            border: none;
            text-indent: 12px;
            height: 32px;
            margin-left: 12px;
            margin-right: 23px;
            flex-grow: 1;
            border-radius: 5px;
            background-color: rgba(216, 216, 216, 0.3);
            font-size: 14px;
            color: var(--placeholder-text);
          }
          input::-webkit-input-placeholder {
            color: #b5b5b5;
          }
          input::-moz-placeholder {
            /* Mozilla Firefox 19+ */
            color: #b5b5b5;
          }
          input:-moz-placeholder {
            /* Mozilla Firefox 4 to 18 */
            color: #b5b5b5;
          }
          input:-ms-input-placeholder {
            /* Internet Explorer 10-11 */
            color: #b5b5b5;
          }
          i {
            cursor: pointer;
            font-size: 18px;
            color: var(--placeholder-text);
            transition: color 0.5s ease-in;
            &:active {
              color: var(--primary);
            }
          }
        }
      }
    }
    .tab-copy-tit-panel {
      display: none;
      width: 100%;
      height: 64px;
    }
    .tab-content {
      margin-top: 36px;
      width: 100%;
      // 各类别容器
      &.hot,
      &.cate {
        display: flex;
        justify-content: space-between;
      }
      &.search {
        display: flex;
        flex-direction: column;
        align-items: center;
        .page-tool {
          margin-top: 36px;
          width: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
      .aside {
        user-select: none;
        display: flex;
        flex-direction: column;
        width: 23%;
        height: 100%;
        box-sizing: border-box;
        padding: 12px 12px 32px 12px;
        background-color: #efefef;
        border-radius: 5px;
        .aside-title {
          width: 100%;
          font-size: 18px;
          font-weight: bold;
          color: #3d3d3d;
          text-align: center;
          margin-bottom: 18px;
        }
        .aside-content {
          display: flex;
          align-items: center;
          flex-direction: column;
          .aside-item {
            cursor: pointer;
            width: 100%;
            display: flex;
            align-items: center;
            box-sizing: border-box;
            padding: 3px 6px;
            border-radius: 3px;
            background-color: #f5f5f5;
            transition: background-color 0.5s ease-in-out;
            &.mid {
              .text {
                text-align: center;
              }
            }
            &.is-hot {
              .num {
                color: var(--primary);
              }
            }
            &.is-active {
              background-color: rgba(131, 204, 210, 0.5);
              .text {
                color: #fff;
              }
            }
            & + .aside-item {
              margin-top: 8px;
            }
            .num {
              font-size: 18px;
              font-weight: bold;
              color: #a8a8a8;
            }
            .text {
              flex-grow: 1;
              font-size: 16px;
              color: #666666;
              margin-left: 8px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              transition: color 0.5s ease-in-out;
            }
            .icon {
              font-size: 12px;
              color: var(--placeholder-text);
            }
          }
        }
      }
      .book-page {
        width: 75%;
        margin: 0;
        display: flex;
        flex-direction: column;
        .page-tool {
          margin-top: 36px;
          width: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
      .side-table,
      .mid-table {
        width: 100%;
        display: grid;
        row-gap: 24px;
        justify-content: space-between;
      }
      .side-table {
        grid-template-columns: repeat(3, 240px);
      }
      .mid-table {
        grid-template-columns: repeat(4, 240px);
      }
      .card {
        width: 240px;
        height: 148px;
      }
      :deep(.sk-book-card) {
        .sk-book-card-content {
          .sk-book-card__profile {
            -webkit-line-clamp: 4;
            flex-grow: 1;
          }
          .sk-book-card__store {
            display: none;
          }
        }
      }
      @media (min-width: 1500px) and (max-width: 1844.9px) {
        .card {
          width: 240px;
          height: 148px;
        }
        .side-table {
          grid-template-columns: repeat(3, 240px);
        }
        :deep(.sk-book-card) {
          .sk-book-card-content {
            .sk-book-card__profile {
              -webkit-line-clamp: 4;
              flex-grow: 1;
            }
          }
        }
      }
      @media (min-width: 1845px) {
        .card {
          width: 300px;
          height: 186px;
        }
        .side-table {
          grid-template-columns: repeat(3, 300px);
        }
        :deep(.sk-book-card) {
          .sk-book-card-content {
            .sk-book-card__profile {
              -webkit-line-clamp: 6;
              flex-grow: 1;
            }
          }
        }
      }
    }
    .fade-enter-active,
    .fade-leave-active {
      transition: opacity 0.5s ease-in-out;
    }

    .fade-enter-from,
    .fade-leave-to {
      opacity: 0;
    }
  }
}
</style>

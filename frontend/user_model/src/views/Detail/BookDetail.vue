<template>
  <div class="main-box">
    <div class="banner">
      <img class="img" :src="$bannerUrl" />
      <Wave class="wave" colorHex="#f1f2f3" />
    </div>
    <div class="content-box" id="bookContentBox">
      <div class="content" :style="style">
        <div class="top">
          <BookMessCard
            class="book-mess"
            :cover="bookData.book.coverUrl"
            :book-name="bookData.book.name"
            :author="bookData.book.author"
            :press="bookData.book.press"
            :pubDate="bookData.book.pubDate"
            :size="bookData.book.size"
            :page="bookData.book.pages"
            :cate-name="bookData.bookCateName"
            :borrowCost="bookData.bookCost.borrowCost"
            :freeDay="bookData.bookCost.freeDay"
            :inventory="bookData.book.inventory"
            :handle-author="handleAuthor"
            :handle-press="handlePress"
            :handle-cate="handleCate"
            :handle-buy="handleBuy"
            :handle-add="handleAdd"
            :handle-car="handleCar" />
          <ShopMessCard
            class="shop-mess"
            :cover="shopData.coverUrl"
            :name="shopData.storeName"
            :score="shopData.score ? shopData.score : 0"
            :amount="shopData.amount"
            :label="shopData.labelList"
            :rank="bookRank"
            @clickRankItem="handleRankItem"
            @clickStoreName="handleStoreName(shopData.id)" />
        </div>
        <div class="tab-box" id="bookTabBox">
          <!-- 选项卡 -->
          <div class="tab-tit-panel" :style="navStyle">
            <!-- 左边导航组 -->
            <div class="nav-box">
              <div
                class="nav-item"
                id="detail"
                :class="{ 'is-active': activeTab === 'detail' }"
                @click.stop="handleNav('detail')">
                <span class="nav-context">商品详情</span>
                <span class="nav-bg"></span>
              </div>
              <div
                class="nav-item"
                id="comment"
                :class="{ 'is-active': activeTab === 'comment' }"
                @click.stop="handleNav('comment')">
                <span class="nav-context">{{
                  '商品评论(' + (comment.length > 99 ? '99+' : comment.length) + '条)'
                }}</span>
                <span class="nav-bg"></span>
              </div>
            </div>
            <!-- 右边工具组 -->
            <div class="tool-box">
              <div class="car" @click.stop="handleCar">
                <i class="ic-shopping-cart"></i>
              </div>
              <div class="btns">
                <div class="btn btn-buy" @click.stop="handleBuy">立即借阅</div>
                <div class="btn btn-add" @click.stop="handleAdd">加入购物车</div>
              </div>
            </div>
          </div>
          <!-- 占位 -->
          <div class="tab-copy-tit-panel"></div>
          <div class="tab-content">
            <!-- 商品详情 -->
            <div class="book-detail" id="bookDetail">
              <div class="tab-content-tit">
                <span>商品详情</span>
              </div>
              <div class="detail-content">
                <!-- 版权信息 -->
                <div class="detail-block">
                  <div class="detail-tit">版权信息</div>
                  <div class="detail-info">
                    <!-- ISBN -->
                    <div class="detail-info-item">
                      <span class="info-tit">ISBN：</span>
                      <span class="info-text">{{ handleBookInfo(bookData.bookInfo.isbn) }}</span>
                    </div>
                    <!-- 条形码 -->
                    <div class="detail-info-item">
                      <span class="info-tit">条形码：</span>
                      <span class="info-text">{{ handleBookInfo(bookData.bookInfo.barCode) }}</span>
                    </div>
                    <!-- 装帧 -->
                    <div class="detail-info-item">
                      <span class="info-tit">装帧：</span>
                      <span class="info-text">{{ handleBookInfo(bookData.bookInfo.binding) }}</span>
                    </div>
                    <!-- 版次 -->
                    <div class="detail-info-item">
                      <span class="info-tit">版次：</span>
                      <span class="info-text">{{ handleBookInfo(bookData.bookInfo.version) }}</span>
                    </div>
                    <!-- 册数 -->
                    <div class="detail-info-item">
                      <span class="info-tit">册数：</span>
                      <span class="info-text">{{ handleBookInfo(bookData.bookInfo.volume) }}</span>
                    </div>
                    <!-- 质量 -->
                    <div class="detail-info-item">
                      <span class="info-tit">质量：</span>
                      <span class="info-text">{{ handleBookInfo(bookData.bookInfo.weight) }}</span>
                    </div>
                    <!-- 印刷次数 -->
                    <div class="detail-info-item">
                      <span class="info-tit">印刷次数：</span>
                      <span class="info-text">{{ handleBookInfo(bookData.bookInfo.printTimes) }}</span>
                    </div>
                    <!-- 分类 -->
                    <div class="detail-info-item item-cate" @click.stop="handleCate">
                      <span class="info-tit">所属分类：</span>
                      <span class="info-link">{{ bookData.bookCateName }}</span>
                    </div>
                  </div>
                </div>
                <!-- 本书特色 -->
                <div class="detail-block">
                  <div class="detail-tit">本书特色</div>
                  <div class="detail-text">
                    <p
                      v-for="(text, ti) in handleBookText(bookData.bookDetail.featureDesc)"
                      :key="'featureDesc--' + ti">
                      {{ text }}
                    </p>
                  </div>
                </div>
                <!-- 内容简介 -->
                <div class="detail-block">
                  <div class="detail-tit">内容简介</div>
                  <div class="detail-text">
                    <p v-for="(text, ti) in handleBookText(bookData.bookDetail.outline)" :key="'outline-' + ti">
                      {{ text }}
                    </p>
                  </div>
                </div>
                <!-- 作者简介 -->
                <div class="detail-block">
                  <div class="detail-tit">作者简介</div>
                  <div class="detail-text">
                    <p v-for="(text, ti) in handleBookText(bookData.bookDetail.authorDesc)" :key="'authorDesc-' + ti">
                      {{ text }}
                    </p>
                  </div>
                </div>
              </div>
              <!-- 详情右边导航 -->
              <div class="detail-aside" :style="{ top: asideTop + 'px' }" id="detailAside">
                <div class="detail-aside-item" :class="{ 'is-active': activeAside === 0 }" @click.stop="handleAside(0)">
                  <span class="detail-aside__icon"></span>
                  <span class="detail-aside__text">版权信息</span>
                </div>
                <div class="detail-aside-item" :class="{ 'is-active': activeAside === 1 }" @click.stop="handleAside(1)">
                  <span class="detail-aside__icon"></span>
                  <span class="detail-aside__text">本书特色</span>
                </div>
                <div class="detail-aside-item" :class="{ 'is-active': activeAside === 2 }" @click.stop="handleAside(2)">
                  <span class="detail-aside__icon"></span>
                  <span class="detail-aside__text">内容简介</span>
                </div>
                <div class="detail-aside-item" :class="{ 'is-active': activeAside === 3 }" @click.stop="handleAside(3)">
                  <span class="detail-aside__icon"></span>
                  <span class="detail-aside__text">作者简介</span>
                </div>
              </div>
            </div>
            <!-- 商品评论 -->
            <div class="book-comment" id="bookComment">
              <div class="tab-content-tit">
                <span>{{ '商品评论(' + (comment.length > 99 ? '99+' : comment.length) + '条)' }}</span>
              </div>
              <div class="comment-content">
                <BookComment
                  class="comment-item"
                  v-for="(item, index) in comment"
                  :key="'book-comment-' + index"
                  :is-anonymous="item.isAnonymous ? true : false"
                  :name="item.nickname"
                  :avatar="item.userAvatar"
                  :date-time="item.updateTime"
                  :score="item.score"
                  :content="item.content"
                  :reply="item.reply" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Wave from '@/components/Common/Wave';
import BookMessCard from '@/components/Card/BookMessCard';
import ShopMessCard from '@/components/Card/ShopMessCard';
import BookComment from '@/components/Comment/BookComment';
import domHandle from '@/utils/domHandleUtil';
import { getBookDetailByIdApi } from '@/api/bookApi';
import { getBookCateApi } from '@/api/cateApi';
import { getShopByIdApi, getBookRankOfShopApi } from '@/api/shopAPi';
import { getCommentByBookId } from '@/api/commentApi';

export default {
  components: {
    Wave,
    BookMessCard,
    ShopMessCard,
    BookComment
  },
  data() {
    return {
      bookId: '',
      bookData: {
        book: {
          coverUrl: '',
          name: '',
          author: '',
          bookCateId: 0,
          press: '',
          pubDate: '',
          size: '',
          pages: 0,
          inventory: 0
        },
        bookInfo: {},
        bookDetail: {},
        bookCost: {
          borrowCost: 0,
          freeDay: 0,
          packingCost: 0,
          deposit: 0
        }
      },
      shopData: {
        storeName: '',
        coverUrl: '',
        score: '',
        amount: 0,
        labelList: []
      },
      width: 0,
      activeTab: 'detail',
      activeAside: 0,
      bookRank: [],
      asideTop: 120,
      comment: [],
      /** @type {HTMLElement} */
      contentBoxDom: {},
      /** @type {HTMLElement} */
      tabBoxDom: {},
      /** @type {HTMLElement} */
      detailDom: {},
      /** @type {HTMLElement} */
      detailAsideDom: {},
      // 商品详情项DOM
      detailListDom: [],
      /** @type {HTMLElement} */
      commentDom: {}
    };
  },
  created() {
    this.bookId = this.$route.query.id;
    if (this.bookId) {
      // 发起请求
      this.initPageMess();
    }
  },
  mounted() {
    this.width = document.body.offsetWidth;
    this.contentBoxDom = document.getElementById('bookContentBox');
    this.tabBoxDom = document.getElementById('bookTabBox');
    this.detailDom = document.getElementById('bookDetail');
    this.detailAsideDom = document.getElementById('detailAside');
    this.commentDom = document.getElementById('bookComment');
    this.detailListDom = document.querySelectorAll('.detail-block');
    // 监听滚动事件
    window.addEventListener('scroll', this.onScroll);
  },
  methods: {
    initPageMess() {
      this.getBookDetail(this.bookId);
      this.getComment();
    },
    /**
     * 处理图书信息
     */
    handleBookInfo(info) {
      if (info) return info;
      return '暂无';
    },
    handleBookText(text) {
      if (text) return text.split('\n');
      return ['暂无'];
    },
    /**
     * 滚动事件
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
      // 156
      // 设置aside相对固定
      let lt = tabTop + 92;
      let rt = tabTop + 100 + this.detailDom.offsetHeight - this.detailAsideDom.offsetHeight - 120;
      if (scrollTop > lt) {
        if (scrollTop < rt) {
          this.asideTop = 120 + (scrollTop - lt);
        }
      } else {
        this.asideTop = 120;
      }
      let dtTopArr = this.getDomOTList(
        this.detailListDom,
        this.contentBoxDom.offsetTop + this.detailDom.offsetTop - 75
      );
      let nowIndex = 0;
      for (let i = 0; i < dtTopArr.length; i++) {
        // 如果 scrollTop 大于等于第 n 个元素的 offsetTop 则说明 n-1 的内容已经完全不可见
        // 那么此时导航索引就应该是 n 了
        if (scrollTop >= dtTopArr[i]) {
          nowIndex = i;
        }
      }
      this.activeAside = nowIndex;
    },
    /**
     * 获取图书评论
     */
    getComment() {
      getCommentByBookId(1).then(res => {
        if (res.data.flag) {
          this.comment = res.data.data;
        }
      });
    },
    /**
     * 获取图书详情
     */
    getBookDetail(id) {
      getBookDetailByIdApi(id).then(res => {
        // res.data.flag
        if (res.data.flag) {
          // 获取成功
          this.bookData = res.data.data;
          this.getShopMess(res.data.data.book.storeId);
        } else {
          this.$showMsg('网络繁忙，请稍后重试', { type: 'warning' });
        }
      });
    },
    /**
     * 获取商家信息
     */
    getShopMess(id) {
      getShopByIdApi(id).then(res => {
        if (res.data.flag) {
          this.shopData = res.data.data;
        } else {
          this.$showMsg('网络繁忙，请稍后重试', { type: 'warning' });
        }
      });
      getBookRankOfShopApi(id, 3).then(res => {
        if (res.data.flag) {
          this.bookRank = res.data.data;
        } else {
          this.$showMsg('网络繁忙，请稍后重试', { type: 'warning' });
        }
      });
    },
    /**
     * 点击作者
     */
    handleAuthor() {
      console.log('作者');
    },
    /**
     * 点击出版社
     */
    handlePress() {
      console.log('出版社');
    },
    // 点击类别
    handleCate() {
      console.log('类别');
    },
    // 点击借阅
    handleBuy() {
      console.log('借阅');
    },
    // 点击添加购物车
    handleAdd() {
      console.log('加购物车');
    },
    // 点击购物车
    handleCar() {
      console.log('购物车');
    },
    /**
     * 点击借阅榜项
     */
    handleRankItem(id) {
      // console.log('借阅榜项ID =>', id);
      // 捕获路由错误，但不暴露，防止重复路由控制台显示报错
      this.$router.push({ path: '/book', query: { id } }).catch(err => err);
    },
    /**
     * 点击商家名称
     */
    handleStoreName(id) {
      // console.log('商家名');
      this.$router.push({ path: '/shop', query: { id } });
    },
    /**
     * 点击nav
     */
    handleNav(id) {
      this.activeTab = id;
      if (id === 'detail') {
        // this.detailDom.scrollIntoView({ behavior: 'smooth' });
        let offsetPosition = this.getDomScrollTop(this.detailDom) - 88;
        window.scrollTo({
          top: offsetPosition,
          behavior: 'smooth'
        });
      } else if (id === 'comment') {
        // this.commentDom.scrollIntoView({ behavior: 'smooth' });
        let offsetPosition = this.getDomScrollTop(this.commentDom) - 88;
        window.scrollTo({
          top: offsetPosition,
          behavior: 'smooth'
        });
      }
    },
    /**
     * 点击详情aside
     */
    handleAside(index) {
      this.activeAside = index;
      let offsetPosition =
        this.detailListDom[index].offsetTop + this.contentBoxDom.offsetTop + this.detailDom.offsetTop - 70;
      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth'
      });
    },
    /**
     * 获取元素距离文档流顶部的位置
     * @param {HTMLElement} dom
     */
    getDomScrollTop(dom) {
      let actualTop = dom.offsetTop;
      let current = dom.offsetParent;
      while (current !== null) {
        actualTop += current.offsetTop;
        current = current.offsetParent;
      }
      return actualTop;
    },
    /**
     * 获取dom数组中元素的距离文档顶部的距离
     * @param {Array} arr
     */
    getDomOTList(arr, inc = 0) {
      let list = [];
      arr.forEach(item => {
        list.push(item.offsetTop + inc);
      });
      return list;
    }
  },
  computed: {
    style() {
      let style = {};
      style['--book-mess-h'] = this.width * 0.21875 + 'px';
      return style;
    },
    navStyle() {
      let style = {};
      style.width = this.tabBoxDom.clientWidth - 144 + 'px';
      return style;
    }
  },
  watch: {
    $route(to, from) {
      this.bookId = to.query.id;
      this.initPageMess();
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
  padding: 30px 0 120px 0;
  .top {
    display: flex;
    justify-content: space-between;
    .book-mess {
      width: 67%;
      height: var(--book-mess-h);
    }
    .shop-mess {
      width: 27%;
      margin-right: 30px;
    }
  }
  .tab-box {
    margin-top: 36px;
    width: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    box-sizing: border-box;
    padding: 0 72px;
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
          padding: 0 56px;
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
        .car {
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          width: 42px;
          height: 42px;
          border-radius: 50%;
          background-color: #fff;
          box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
          i {
            font-size: 21px;
            color: #83ccd2;
          }
          &:active {
            box-shadow: inset 1px 1px 2px #babecc, inset -1px -1px 2px #fff;
          }
        }
        .btns {
          display: flex;
          align-items: center;
          margin: 0 36px;
          .btn {
            cursor: pointer;
            height: 36px;
            padding: 0 36px;
            border-radius: 18px;
            font-size: 16px;
            font-weight: bold;
            color: #fff;
            line-height: 36px;
            & + .btn {
              margin-left: 32px;
            }
            &.btn-buy {
              background-color: #83ccd2;
              &:hover {
                background-color: rgba(131, 204, 210, 0.8);
              }
              &:active {
                background-color: #76b7bd;
              }
            }
            &.btn-add {
              background-color: #b1d5c8;
              &:hover {
                background-color: rgba(177, 213, 200, 0.8);
              }
              &:active {
                background-color: #9dbdb1;
              }
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
      display: flex;
      flex-direction: column;
      align-items: center;
      .tab-content-tit {
        width: 100%;
        height: 56px;
        border-radius: 8px;
        background-color: #f5f5f5;
        display: flex;
        align-items: center;
        span {
          font-size: 16px;
          font-weight: bold;
          margin-left: 24px;
        }
      }
      .book-detail {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 100%;
        padding-bottom: 36px;
        .detail-content {
          margin-left: 36px;
          margin-top: 8px;
          display: flex;
          flex-direction: column;
          width: 78%;
          .detail-block {
            display: flex;
            flex-direction: column;
            width: 100%;
            .detail-tit {
              user-select: none;
              margin-bottom: 12px;
              font-size: 16px;
              color: var(--primary-text);
            }
            .detail-info {
              margin-left: 24px;
              width: 85%;
              display: grid;
              gap: 8px 24px;
              grid-template-columns: 1fr auto auto;
              .detail-info-item {
                display: flex;
                align-items: center;
                .info-tit {
                  user-select: none;
                  font-size: 14px;
                  color: #666666;
                }
                .info-text {
                  font-size: 14px;
                  color: #777777;
                }
                .info-link {
                  cursor: pointer;
                  font-size: 14px;
                  color: #3271ae;
                }
                &.item-cate {
                  grid-column: 1;
                }
              }
            }
            .detail-text {
              font-size: 15px;
              display: flex;
              flex-direction: column;
              padding-left: 24px;
              width: 100%;
              color: #777777;
              text-indent: 2em;
              line-height: 24px;
              box-sizing: border-box;
              white-space: pre-wrap;
              p {
                margin: 8px 0;
              }
            }
            & + .detail-block {
              margin-top: 12px;
            }
          }
        }
        .detail-aside {
          position: absolute;
          width: 150px;
          top: 120px;
          right: 24px;
          border-radius: 5px;
          background-color: #f1f1f1;
          display: flex;
          flex-direction: column;
          box-sizing: border-box;
          padding: 12px 10px 81px 10px;
          .detail-aside-item {
            cursor: pointer;
            user-select: none;
            position: relative;
            display: flex;
            width: 100%;
            height: 27px;
            align-items: center;
            justify-content: center;
            transition: all 0.5s ease-in-out;
            &.is-active {
              .detail-aside__icon {
                background-color: var(--primary);
              }
              .detail-aside__text {
                color: var(--primary);
              }
            }
            .detail-aside__icon {
              position: absolute;
              top: 0;
              left: 0;
              width: 4px;
              height: 100%;
              border-radius: 2px;
            }
            .detail-aside__text {
              font-size: 14px;
              color: #666666;
            }
            & + .detail-aside-item {
              margin-top: 5px;
            }
          }
        }
      }
      .book-comment {
        margin-top: 24px;
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 1080px;
        .comment-content {
          width: 95%;
          margin-top: 12px;
          display: flex;
          flex-direction: column;
          .comment-item {
            width: 100%;
            & + .comment-item {
              margin-top: 8px;
            }
          }
        }
      }
    }
  }
}
</style>

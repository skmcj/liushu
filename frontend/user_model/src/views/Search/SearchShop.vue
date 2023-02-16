<template>
  <div class="search-shop">
    <template v-if="$isEmpty(shops)">
      <SvgPage style="margin-top: 48px" name="no-result" :img-width="480" text="查无相关内容"></SvgPage>
    </template>
    <div class="card-box">
      <ShopCard
        v-for="(shop, index) in shops"
        :key="'shop-recommend-' + index"
        :cover="shop.coverUrl"
        :name="shop.storeName"
        :score="shop.score"
        :hanle-click="() => clickStore(shop.id)" />
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
import ShopCard from '@/components/Card/ShopCard';
import SvgPage from '@/components/Common/SvgPage';
import { searchShopApi } from '@/api/searchApi';

export default {
  components: {
    ShopCard,
    SvgPage
  },
  data() {
    return {
      shops: [],
      currentPage: 1,
      pageSize: 15,
      total: 0,
      keyword: ''
    };
  },
  created() {
    // this.searchShop();
    this.keyword = this.$store.state.keyword;
    if (this.keyword) {
      this.searchShop();
    }
  },
  mounted() {
    this.$bus.$on('searchShop', this.search);
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
      this.searchShop();
    },
    /**
     * 点击商家
     */
    clickStore(id) {
      this.$router.push({ path: '/shop', query: { id } });
    },
    searchShop() {
      if (!this.keyword) return;
      this.pageSize = this.getReqNum() * 4;
      searchShopApi(this.keyword, this.currentPage, this.pageSize).then(res => {
        if (res.data.flag) {
          this.shops = res.data.data.records;
          this.total = res.data.data.total;
        }
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.searchShop();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.searchShop();
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
.search-shop {
  display: flex;
  align-items: center;
  flex-direction: column;
}
</style>

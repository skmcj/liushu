<template>
  <div class="mine mine-coll">
    <div class="title-box">
      <span class="icon ic-star"></span>
      <span class="text">我的收藏</span>
      <div class="hr"></div>
    </div>
    <div class="mine-coll-box">
      <div
        class="coll-item"
        v-for="(item, index) in collShop"
        :key="'coll-shop-' + index"
        @click.stop="handleColl(item.id)">
        <div class="cover">
          <img :src="$noCoverUrl" alt="cover" />
        </div>
        <div class="mess-box">
          <div class="title">{{ item.storeName }}</div>
          <div class="score">
            <i class="ic-score"></i>
            <span>{{ item.score }}</span>
          </div>
          <div class="amount">
            <span class="tit">月借阅：</span>
            <span>{{ item.amount }}</span>
          </div>
          <div class="lbs" v-if="item.labelList">
            <div class="lb" v-for="(label, index) in item.labelList" :key="'coll-lb-' + index">{{ label }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      collShop: []
    };
  },
  created() {
    this.collShop = this.getShopTest(13);
  },
  methods: {
    getShopTest(n) {
      let list = [];
      let shop = {
        id: 1,
        storeName: '片刻书店',
        score: 4.5,
        amount: 108,
        labelList: ['文学', '小说']
      };
      for (let i = 0; i < n; i++) {
        list.push(shop);
      }
      return list;
    },
    handleColl(id) {
      this.$router.push({ path: '/shop', query: { id } });
    }
  }
};
</script>

<style lang="less" scoped>
.mine-coll {
  .mine-coll-box {
    margin-top: 24px;
    width: 100%;
    box-sizing: border-box;
    padding: 0 18px;
    padding-bottom: 120px;
    display: grid;
    justify-content: space-between;
    row-gap: 16px;
    .coll-item {
      cursor: pointer;
      user-select: none;
      width: 270px;
      height: 148px;
      display: flex;
      border-radius: 8px;
      overflow: hidden;
      background-color: #f7f7f7;
      .cover {
        min-width: 140px;
        width: 52%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
      .mess-box {
        display: flex;
        flex-grow: 1;
        flex-direction: column;
        .title {
          margin-top: 8px;
          margin-left: 8px;
          font-size: 18px;
          font-weight: bold;
          color: var(--primary-text);
          text-overflow: ellipsis;
          overflow: hidden;
          white-space: nowrap;
        }
        .score {
          margin-top: 5px;
          margin-left: 13px;
          font-size: 15px;
          color: var(--primary);
          span {
            margin-left: 5px;
          }
        }
        .amount {
          margin-top: 3px;
          margin-left: 13px;
          font-size: 12px;
          color: #999999;
        }
        .lbs {
          margin-top: 3px;
          margin-left: 13px;
          margin-bottom: 8px;
          display: flex;
          flex-wrap: wrap;
          gap: 5px 8px;
          overflow-y: auto;
          &::-webkit-scrollbar {
            display: none;
          }
          .lb {
            box-sizing: border-box;
            padding: 2px 8px;
            font-size: 8px;
            color: #565656;
            border-radius: 3px;
            background-color: #dfdfdf;
          }
        }
      }
    }
  }
  @media (max-width: 1139.9px) {
    .mine-coll-box {
      grid-template-columns: repeat(2, 270px);
    }
  }
  @media (min-width: 1367px) and (max-width: 1700.9px) {
    .mine-coll-box {
      grid-template-columns: repeat(3, 270px);
    }
  }
  @media (min-width: 1701px) and (max-width: 2199.9px) {
    .mine-coll-box {
      grid-template-columns: repeat(4, 270px);
    }
  }
}
</style>

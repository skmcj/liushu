<template>
  <div class="sk-shop-mess-card">
    <template v-if="size === 'primary'">1</template>
    <template v-else-if="size === 'mini'">
      <div class="sk-shop-mess-card__title">店家信息</div>
      <div class="sk-shop-mess-card-cover" :style="imgStyle">
        <img :src="cover ? cover : $noCoverUrl" />
      </div>
      <div class="sk-shop-mess-card-name" @click.stop="handleName">{{ name }}</div>
      <!-- 商家基本信息 -->
      <div class="sk-shop-mess-card-outline">
        <div class="sk-shop-mess-card__otem">
          <div class="sk-shop-mess-card__ot">评分：</div>
          <!-- 评分图标 -->
          <div class="sk-shop-mess-card__os">
            <el-rate
              :value="score"
              :max="5"
              :icon-classes="['ic-score', 'ic-score', 'ic-score']"
              :colors="['#83ccd2', '#83ccd2', '#83ccd2']"
              disabled-void-icon-class="ic-score"
              disabled
              show-score
              text-color="#83ccd2"
              disabled-void-color="#efefef"
              score-template="{value}">
            </el-rate>
          </div>
        </div>
        <div class="sk-shop-mess-card__otem">
          <div class="sk-shop-mess-card__ot">月借阅量：</div>
          <!-- 内容 -->
          <span class="sk-shop-mess-card__oc">{{ amount ? amount : '暂无' }}</span>
        </div>
        <div class="sk-shop-mess-card__otem">
          <div class="sk-shop-mess-card__ot">标签：</div>
          <!-- 内容 -->
          <div class="sk-shop-mess-card__ols" v-if="label">
            <!-- 具体标签 -->
            <span class="sk-shop-mess-card__ol" v-for="(item, index) in label" :key="'sk-smc-lb-' + index">{{
              item
            }}</span>
          </div>
          <span v-else class="sk-shop-mess-card__oc">暂无</span>
        </div>
      </div>
      <!-- 借阅榜 -->
      <div class="sk-shop-mess-card-rank">
        <div class="sk-shop-mess-card-rank__title">门店借阅榜</div>
        <div class="sk-shop-mess-card-rank-content" v-if="rank">
          <div
            class="sk-shop-mess-card-rank__item"
            v-for="(item, index) in rank"
            :key="'sk-smc-rk-' + index"
            @click.stop="handleRank(item.id)">
            <span>{{ item.name }}</span>
            <i class="ic-more-1"></i>
          </div>
        </div>
        <div class="sk-shop-mess-card-rank-content" v-else>暂无</div>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  props: {
    size: {
      type: String,
      // mini - primary
      default: 'mini'
    },
    score: {
      type: Number,
      default: 0
    },
    name: String,
    cover: String,
    label: {
      type: Array,
      default: null
    },
    amount: Number,
    rank: {
      type: Array,
      default: null
      // 每个item至少需包含id、name两项
    }
  },
  data() {
    return {
      coverWidth: 0
    };
  },
  mounted() {
    this.coverWidth = this.$el.children[1].offsetWidth;
  },
  computed: {
    imgStyle() {
      let style = {};
      style.height = this.coverWidth * 0.584 + 'px';
      return style;
    }
  },
  methods: {
    handleRank(id) {
      // 点击借阅榜项
      this.$emit('clickRankItem', id);
    },
    handleName() {
      // 点击书店名称
      this.$emit('clickStoreName', this.name);
    }
  }
};
</script>

<style lang="less" scoped>
.sk-shop-mess-card {
  margin-right: 30px;
  display: flex;
  align-items: center;
  flex-direction: column;
  box-sizing: border-box;
  border-radius: 8px;
  background-color: #fffffc;
  border: 1px solid #efefef;
  padding: 18px 30px 36px 30px;
  .sk-shop-mess-card__title {
    font-size: 21px;
    font-weight: bold;
    color: #3d3d3d;
  }
  .sk-shop-mess-card-cover {
    width: 100%;
    border-radius: 5px;
    overflow: hidden;
    margin-top: 12px;
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  .sk-shop-mess-card-name {
    cursor: pointer;
    font-size: 18px;
    font-weight: bold;
    width: 100%;
    margin: 12px 0 8px 0;
  }
  .sk-shop-mess-card-outline {
    width: 100%;
    display: flex;
    flex-direction: column;
    margin-left: 5px;
    .sk-shop-mess-card__otem {
      width: 100%;
      display: flex;
      align-items: center;
      & + .sk-shop-mess-card__otem {
        margin-top: 3px;
      }
      .sk-shop-mess-card__ot {
        min-width: 42px;
        font-size: 14px;
        color: #666666;
      }
      .sk-shop-mess-card__os {
        display: flex;
        align-items: center;
      }
      .sk-shop-mess-card__oc {
        font-size: 14px;
        color: #999999;
      }
      .sk-shop-mess-card__ols {
        flex-grow: 1;
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        gap: 3px 8px;
        .sk-shop-mess-card__ol {
          font-size: 8px;
          padding: 0 10px;
          border-radius: 3px;
          color: #565656;
          background-color: #dfdfdf;
          box-sizing: border-box;
        }
      }
    }
  }
  .sk-shop-mess-card-rank {
    width: 100%;
    margin-top: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
    .sk-shop-mess-card-rank__title {
      font-size: 16px;
      color: #3d3d3d;
    }
    .sk-shop-mess-card-rank-content {
      margin-top: 12px;
      width: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      font-size: 14px;
      color: #666666;
      .sk-shop-mess-card-rank__item {
        cursor: pointer;
        user-select: none;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-radius: 5px;
        box-sizing: border-box;
        padding: 6px 12px;
        background-color: #eaeef1;
        transition: background-color 0.5s ease;
        & + .sk-shop-mess-card-rank__item {
          margin-top: 8px;
        }
        &:hover {
          background-color: rgba(234, 238, 241, 0.5);
        }
        span {
          font-size: 14px;
          color: #666666;
        }
        i {
          font-size: 10px;
          color: #666666;
          margin-right: 3px;
        }
      }
    }
  }
}
</style>

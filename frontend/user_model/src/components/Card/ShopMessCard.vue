<template>
  <div
    class="sk-shop-mess-card"
    :class="{ 'sk-shop-mess-card__mini': size === 'mini', 'sk-shop-mess-card__primary': size === 'primary' }">
    <!-- 详细卡 -->
    <template v-if="size === 'primary'">
      <div class="sk-shop-mess-card-info-box">
        <div class="sk-shop-mess-card-cover">
          <img :src="cover ? cover : $noCoverUrl" alt="门脸图片" />
        </div>
        <div class="sk-shop-mess-card-content">
          <div class="sk-shop-mess-card-content__box">
            <div class="sk-shop-mess-card__title">{{ name }}</div>
            <!-- 评分 -->
            <div class="sk-shop-mess-card__tip">
              <div class="sk-shop-mess-card_score">
                <i class="tit ic-score"></i>
                <span class="text">{{ score }}</span>
              </div>
              <div class="sk-shop-mess-card_amount">
                <span class="tit">月借阅量：</span>
                <span class="text">{{ amount }}</span>
              </div>
            </div>
            <div class="sk-shop-mess-card__lbs" v-if="label">
              <span class="sk-shop-mess-card_lb" v-for="(item, index) in label" :key="'sk-smc-lb-' + index">{{
                item
              }}</span>
            </div>
          </div>
          <div class="sk-shop-mess-card-content__notice">
            <div class="title">公告：</div>
            <div class="content">{{ notice ? notice : '暂无' }}</div>
          </div>
        </div>
      </div>
      <div class="sk-shop-mess-card-detail-box">
        <!-- 按钮 -->
        <div class="sk-shop-mess-card_btns">
          <div class="sk-shop-mess-card__btn" :class="{ 'is-active': isColl }" @click.stop="handleCollect">
            {{ isColl ? '已收藏' : '收藏' }}
          </div>
        </div>
        <div class="sk-shop-mess-card_detail">
          <div class="detail-item">
            <div class="tit">商家地址：</div>
            <div class="content pointer" @click.stop="handleItem('address')">
              <span>{{ address }}</span>
              <i class="ic-more-1"></i>
            </div>
          </div>
          <div class="detail-item">
            <div class="tit">营业时间：</div>
            <div class="content">
              <span>{{ businessHours }}</span>
            </div>
          </div>
          <div class="detail-item">
            <div class="tit">相关执照：</div>
            <div class="content pointer" @click.stop="handleItem('license')">
              <i class="ic-more-1"></i>
            </div>
          </div>
          <div class="detail-item">
            <div class="tit">店内环境：</div>
            <div class="content pointer" @click.stop="handleItem('env')">
              <i class="ic-more-1"></i>
            </div>
          </div>
          <div class="detail-item">
            <div class="tit">配送服务：</div>
            <div class="content">
              <span>{{ distribution === 0 ? '由 商家 提供' : '优质配送团队提供' }}</span>
            </div>
          </div>
          <div class="detail-item">
            <div class="tit">商家服务：</div>
            <div class="content pointer" @click.stop="handleItem('storeService')">
              <span>{{ storeService ? storeService : '无' }}</span>
              <i class="ic-more-1" v-if="storeService"></i>
            </div>
          </div>
        </div>
      </div>
      <!-- 店铺信息弹框 -->
      <el-dialog class="mess-box" :title="messTitle" :visible.sync="messVisible" :modal-append-to-body="false">
        <div v-if="messTitle !== '相关执照' && messTitle !== '店内环境'" class="mess-content">
          {{ messContent }}
        </div>
        <div v-else>
          <el-image
            v-for="(item, index) in messContent"
            :key="'img-' + index"
            class="dia-img"
            :src="item"
            fit="cover"
            :preview-src-list="messContent">
          </el-image>
        </div>
      </el-dialog>
    </template>
    <!-- mini卡 -->
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
    // 是否收藏
    isColl: {
      type: Boolean,
      default: false
    },
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
    // 月借阅量
    amount: Number,
    rank: {
      type: Array,
      default: null
      // 每个item至少需包含id、name两项
    },
    notice: String,
    address: String,
    businessHours: String,
    envImgs: {
      type: Array,
      default: () => {
        return [];
      }
    },
    licenseImgs: {
      type: Array,
      default: () => {
        return [];
      }
    },
    storeService: String,
    distribution: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      coverWidth: 0,
      messTitle: '',
      messVisible: false,
      messContent: ''
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
    },
    /**
     * 处理收藏按钮的点击
     */
    handleCollect() {
      this.$emit('clickCollection', this.isColl);
    },
    handleItem(text) {
      switch (text) {
        case 'address':
          this.messTitle = '商家地址';
          this.messContent = this.address;
          break;
        case 'license':
          this.messTitle = '相关执照';
          this.messContent = this.licenseImgs;
          break;
        case 'env':
          this.messTitle = '店内环境';
          this.messContent = this.envImgs;
          break;
        case 'storeService':
          this.messTitle = '商家服务';
          this.messContent = this.storeService;
          break;
      }
      this.messVisible = true;
    }
  }
};
</script>

<style lang="less" scoped>
.sk-shop-mess-card {
  display: flex;
  box-sizing: border-box;
  &.sk-shop-mess-card__mini {
    align-items: center;
    flex-direction: column;
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
  &.sk-shop-mess-card__primary {
    align-items: center;
    justify-content: space-between;
    padding: 20px 24px;
    .sk-shop-mess-card-info-box {
      display: flex;
      align-items: center;
      width: 70%;
      height: 100%;
      .sk-shop-mess-card-cover {
        min-width: 45%;
        width: 48%;
        height: 100%;
        border-radius: 5px;
        overflow: hidden;
        display: flex;
        align-items: center;
        justify-content: center;
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
      .sk-shop-mess-card-content {
        height: 100%;
        margin-left: 12px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        flex-grow: 1;
        padding: 6px 0;
        box-sizing: border-box;
        .sk-shop-mess-card-content__box {
          display: flex;
          flex-direction: column;
          .sk-shop-mess-card__title {
            font-size: 24px;
            font-weight: bold;
            color: #3d3d3d;
          }
          .sk-shop-mess-card__tip {
            user-select: none;
            display: flex;
            align-items: center;
            margin-top: 8px;
            .sk-shop-mess-card_score {
              display: flex;
              align-items: center;
              .tit {
                font-size: 16px;
                color: #83ccd2;
                margin-right: 8px;
              }
              .text {
                font-size: 14px;
                color: #83ccd2;
              }
            }
            .sk-shop-mess-card_amount {
              margin-left: 24px;
              display: flex;
              align-items: center;
              .tit {
                font-size: 14px;
                color: #666666;
                margin-right: 8px;
              }
              .text {
                font-size: 14px;
                color: #999999;
              }
            }
          }
          .sk-shop-mess-card__lbs {
            display: flex;
            flex-wrap: wrap;
            gap: 8px 12px;
            margin-top: 8px;
            .sk-shop-mess-card_lb {
              user-select: none;
              font-size: 10px;
              box-sizing: border-box;
              color: #999999;
              padding: 2px 12px;
              border-radius: 3px;
              border: 1px solid #d8dde3;
            }
          }
        }
        .sk-shop-mess-card-content__notice {
          display: flex;
          flex-direction: column;
          align-items: center;
          border-radius: 5px;
          overflow: hidden;
          background-color: #f6f6f6;
          box-sizing: border-box;
          padding: 5px 8px;
          width: 100%;
          min-height: 72px;
          .title {
            user-select: none;
            font-size: 14px;
            color: #666666;
            width: 100%;
            margin-bottom: 3px;
          }
          .content {
            width: 96%;
            font-size: 12px;
            color: #999999;
            text-indent: 2em;
          }
        }
      }
    }
    .sk-shop-mess-card-detail-box {
      width: 24%;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      justify-content: space-between;
      .sk-shop-mess-card_btns {
        width: 100%;
        display: flex;
        justify-content: flex-end;
        .sk-shop-mess-card__btn {
          cursor: pointer;
          user-select: none;
          padding: 5px 24px;
          font-size: 14px;
          color: #999999;
          border-radius: 5px;
          box-sizing: border-box;
          border: 1px solid #e5e5e5;
          transition: border-color 0.5s ease-in-out, background-color 0.5s ease-in-out;
          &:hover {
            border-color: #83ccd2;
          }
          &:active {
            background-color: rgba(131, 204, 210, 0.1);
          }
          &.is-active {
            background-color: rgba(131, 204, 210, 0.1);
            border-color: #83ccd2;
          }
        }
      }
      .sk-shop-mess-card_detail {
        width: 100%;
        display: flex;
        flex-direction: column;
        box-sizing: border-box;
        overflow: hidden;
        border-radius: 5px;
        background-color: #f7f7f7;
        padding: 12px 15px;
        .detail-item {
          user-select: none;
          width: 100%;
          display: flex;
          align-items: center;
          justify-content: space-between;
          box-sizing: border-box;
          padding: 3px 0;
          border-bottom: 1px solid #e6e6e6;
          .tit {
            min-width: 35%;
            font-size: 14px;
            color: #555555;
          }
          .content {
            max-width: 65%;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            font-size: 12px;
            color: #999999;
            &.pointer {
              cursor: pointer;
            }
            span {
              text-align: right;
              flex-grow: 1;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              margin-right: 5px;
            }
            i {
              font-size: 10px;
            }
          }
          &.detail-item {
            margin-top: 3px;
          }
        }
      }
    }
    .mess-box {
      :deep(.el-dialog) {
        border-radius: 8px;
        .el-dialog__header {
          .el-dialog__headerbtn,
          .el-dialog__close {
            &:hover,
            &:focus {
              color: #83ccd2;
            }
          }
        }
      }
      .dia-img {
        width: 150px;
        height: 150px;
        border-radius: 5px;
        margin: 6px;
      }
    }
  }
}
</style>

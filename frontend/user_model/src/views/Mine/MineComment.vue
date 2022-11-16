<template>
  <div class="mine mine-comment">
    <div class="title-box">
      <span class="icon ic-assess"></span>
      <span class="text">我的评价</span>
      <div class="hr"></div>
    </div>
    <div class="mine-comment-list">
      <!-- 评论项 -->
      <div class="comment-item" v-for="(item, index) in commentList" :key="'comment-item-' + index">
        <div class="left">
          <div class="order-detail">
            <div class="title" @click.stop="handleStoreDetail(item.storeId)">{{ item.storeName }}</div>
            <!-- <div class="detail-box" @mousewheel="handleWalletBoxScroll" ref="scrollXBox"> -->
            <div class="detail-box">
              <div class="goods-list">
                <div class="goods-item">
                  <div class="goods" @click.stop="handleBookDetail(item.bookId)">
                    <div class="cover">
                      <img :src="item.bookCover" alt="goods" />
                    </div>
                    <div class="tit">{{ item.bookName }}</div>
                  </div>
                  <div class="num">
                    <span>{{ '×' + item.quantity }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="right">
          <div class="comment-content">
            <!-- 评论主要内容 -->
            <div class="comment-content-main">
              <div class="score">
                <el-rate
                  :value="item.score"
                  :max="5"
                  :icon-classes="['ic-score', 'ic-score', 'ic-score']"
                  :colors="['#83ccd2', '#83ccd2', '#83ccd2']"
                  disabled-void-icon-class="ic-score"
                  disabled
                  show-score
                  text-color="#83ccd2"
                  disabled-void-color="#999999"
                  score-template="{value}">
                </el-rate>
              </div>
              <div class="text">
                {{ item.content }}
              </div>
            </div>
            <!-- 右边删除按钮 -->
            <div class="other-mess">
              <div class="del-btn">
                <el-tooltip content="删除评论" placement="top" effect="light" :hide-after="1500">
                  <i class="ic-delete" @click.stop="handleDelete(item.id)"></i>
                </el-tooltip>
              </div>
              <div class="time">{{ item.updateTime }}</div>
            </div>
          </div>
          <!-- 商家回复 -->
          <div class="reply-list" v-if="item.reply">
            <div class="reply-item" v-for="(reply, i) in item.reply" :key="'reply-item-' + index + '-' + i">
              <div class="title">
                <span>商家回复</span>
                <span class="time">{{ reply.updateTime }}</span>
              </div>
              <div class="text">{{ reply.content }}</div>
            </div>
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
      commentList: []
    };
  },
  created() {
    this.commentList = this.getCommentTest(5);
  },
  methods: {
    getCommentTest(n) {
      let list = [];
      for (let i = 0; i < n; i++) {
        let comment = {
          id: 1,
          score: 4.5,
          storeId: '2',
          storeName: '片刻书店',
          bookId: '1',
          bookName: '数学之美',
          quantity: i,
          bookCover: 'https://voidtech.cn/i/2022/11/15/115c6tb.jpg',
          content: '这本书挺有意思的，可以学到挺多编程思想；商家服务态度也不错，是一次比较不错的借阅过程',
          createTime: '2022-11-16- 15:13',
          updateTime: '2022-11-16- 15:13',
          reply:
            i % 2
              ? null
              : [
                  {
                    id: 2,
                    content: '多亏了你的支持，我们才有了今天。感谢您的每一个评价，店家会尽力让您满意',
                    createTime: '2022-11-16- 15:13',
                    updateTime: '2022-11-16- 15:13'
                  }
                ]
        };
        list.push(comment);
      }
      return list;
    },
    /**
     * 处理删除事件
     */
    handleDelete(id) {
      console.log('delete comment =>', id);
    },
    /**
     * 点击图书详情
     */
    handleBookDetail(id) {
      console.log('goto book =>', id);
      this.$router.push({ path: '/book', query: { id } });
    },
    /**
     * 点击商家名
     */
    handleStoreDetail(id) {
      console.log('goto store =>', id);
      this.$router.push({ path: '/shop', query: { id } });
    }
  }
};
</script>

<style lang="less" scoped>
.mine-comment-list {
  width: 100%;
  margin-top: 24px;
  display: flex;
  align-items: center;
  flex-direction: column;
  padding: 0 6px;
  padding-bottom: 120px;
  box-sizing: border-box;
  .comment-item {
    width: 100%;
    box-sizing: border-box;
    border-radius: 8px;
    background-color: #f5f5f5;
    padding: 6px 12px;
    display: flex;
    .left,
    .right {
      display: flex;
      flex-direction: column;
    }
    .left {
      min-width: 12%;
      width: 15%;
      .order-detail {
        width: 100%;
        height: 150px;
        border-radius: 5px;
        background-color: var(--white);
        display: flex;
        align-items: center;
        flex-direction: column;
        box-sizing: border-box;
        padding: 5px 6px;
        .title {
          cursor: pointer;
          user-select: none;
          width: 100%;
          box-sizing: border-box;
          padding-bottom: 3px;
          border-bottom: 1px solid #e5e5e5;
          font-size: 14px;
          color: var(--primary-text);
        }
        .detail-box {
          width: 100%;
          flex-grow: 1;
          margin-top: 8px;
          overflow: hidden;
          display: flex;
          align-items: center;
          overflow-x: auto;
          overflow-y: hidden;
          &::-webkit-scrollbar {
            display: none;
          }
          .goods-list {
            display: flex;
            align-items: center;
            .goods-item {
              user-select: none;
              width: 92px;
              height: 100%;
              display: flex;
              flex-shrink: 0;
              justify-content: space-between;
              align-items: flex-end;
              border-radius: 5px;
              .goods {
                cursor: pointer;
                width: 72px;
                height: 100%;
                display: flex;
                align-items: center;
                justify-content: space-between;
                flex-direction: column;
                .cover {
                  width: 72px;
                  height: 80px;
                  img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                  }
                }
                .tit {
                  text-overflow: ellipsis;
                  overflow: hidden;
                  white-space: nowrap;
                  font-size: 12px;
                  color: var(--primary-text);
                }
              }
              .num {
                height: 100%;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: flex-end;
                span {
                  font-size: 10px;
                  color: #d8d8d8;
                }
              }
              & + .goods-item {
                margin-left: 6px;
              }
            }
          }
        }
      }
    }
    .right {
      flex-grow: 1;
      box-sizing: border-box;
      padding-left: 18px;
      .comment-content {
        width: 100%;
        min-height: 150px;
        display: flex;
        justify-content: space-between;
        box-sizing: border-box;
        padding: 6px;
        .comment-content-main {
          display: flex;
          flex-direction: column;
          flex-grow: 1;
          .score {
            user-select: none;
          }
          .text {
            margin-top: 8px;
            width: 100%;
            font-size: 14px;
            color: #666666;
          }
        }
        .other-mess {
          margin-left: 12px;
          display: flex;
          flex-direction: column;
          align-items: flex-end;
          justify-content: space-between;
          .del-btn {
            width: 21px;
            height: 21px;
            display: flex;
            align-items: center;
            justify-content: center;
            i {
              cursor: pointer;
              font-size: 21px;
              color: var(--placeholder-text);
              transition: color 0.5s ease-in-out;
              &:hover {
                color: var(--danger);
              }
            }
          }
          .time {
            user-select: none;
            min-width: 80px;
            max-width: 120px;
            font-size: 14px;
            color: var(--placeholder-text);
            text-align: right;
          }
        }
      }
      .reply-list {
        margin-top: 16px;
        width: 92%;
        display: flex;
        align-items: center;
        flex-direction: column;
        .reply-item {
          width: 100%;
          display: flex;
          flex-direction: column;
          border-radius: 5px;
          box-sizing: border-box;
          padding: 5px 8px;
          background-color: rgba(255, 255, 255, 0.8);
          .title {
            user-select: none;
            width: 100%;
            display: flex;
            justify-content: space-between;
            font-size: 14px;
            span {
              color: #666666;
            }
            .time {
              font-size: 10px;
              color: var(--grey-link);
            }
          }
          .text {
            margin-top: 6px;
            margin-left: 12px;
            font-size: 10px;
            color: var(--placeholder-text);
          }
          & + .reply-item {
            margin-top: 8px;
          }
        }
      }
    }
    & + .comment-item {
      margin-top: 12px;
    }
  }
}
</style>

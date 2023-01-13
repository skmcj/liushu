<template>
  <!-- 我的-评论组件 -->
  <div class="mine mine-comment">
    <div class="title-box">
      <span class="icon ic-assess"></span>
      <span class="text">我的评价</span>
      <div class="hr"></div>
    </div>
    <div class="mine-comment-list" v-if="!$isEmpty(commentList)">
      <!-- 评论项 -->
      <div
        class="comment-item"
        v-for="(comment, index) in commentList"
        :key="'comment-item-' + index"
        @click.stop="handleCommentDetail(comment)">
        <!-- 评论项标题 -->
        <div class="comment-title-box">
          <div class="comment-title-item">
            <span class="comment-title__label">订单号：</span>
            <span class="comment-title__text">{{ comment.orderNumber }}</span>
          </div>
          <div class="comment-title-item">
            <span class="comment-title__label">商家名称：</span>
            <span class="comment-title__text">{{ comment.storeName }}</span>
          </div>
          <div class="comment-title-item">
            <i class="ic-delete" title="删除评价" @click.stop="handleDelete(comment.id)"></i>
          </div>
        </div>
        <!-- 评论信息 -->
        <div class="comment-item-main">
          <!-- 图书信息 -->
          <div class="comment-item-main-book">
            <!-- 图书封面 - bookCover -->
            <div class="comment-item__bc">
              <img :src="comment.bookCover" alt="图书封面" />
            </div>
            <!-- 图书名称 - bookName -->
            <div class="comment-item__bn">{{ comment.bookName }}</div>
          </div>
          <!-- 评论neir -->
          <div class="comment-item-main-content">
            <div class="comment-item__mess">
              <div class="comment-item_mess__score">
                <el-rate
                  disabled
                  v-model="comment.score"
                  :max="5"
                  allow-half
                  :icon-classes="['ic-score', 'ic-score', 'ic-score']"
                  :colors="['#83ccd2', '#83ccd2', '#83ccd2']"
                  void-icon-class="ic-score"
                  disabled-void-icon-class="ic-score"
                  show-score
                  text-color="#83ccd2"
                  disabled-void-color="#999999"
                  score-template="{value}">
                </el-rate>
              </div>
              <div class="comment-item_mess__time">{{ comment.updateTime }}</div>
            </div>
            <div class="comment-item__content">{{ comment.content }}</div>
          </div>
        </div>
      </div>
    </div>
    <SvgPage class="no-comment" v-if="$isEmpty(commentList)" name="no-comment" text="暂无评价" :img-width="480" />
    <el-pagination
      class="page-tool"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-sizes="[5, 10, 15]"
      :page-size="pageSize"
      layout="sizes, prev, pager, next"
      :total="total"
      hide-on-single-page>
    </el-pagination>
    <!-- 评价dialog -->
    <el-dialog
      class="comment-dialog"
      :title="'订单号：' + dgTitle"
      :visible.sync="dgVisible"
      :modal-append-to-body="false"
      :close-on-click-modal="false">
      <div class="dialog-comment-box">
        <Comment
          :comment="dgComment"
          disabled
          @submit="handleSubmitComment"
          @reply="handleSubmitReply"
          @delete="handleDeleteComment" />
      </div>
      <span slot="footer" class="dialog-footer"></span>
    </el-dialog>
  </div>
</template>

<script>
import SvgPage from '@/components/Common/SvgPage';
import Comment from '@/components/Common/Comment/Comment';
import {
  getCommentPageByUserApi,
  addCommentOfOrderItemApi,
  deleteCommentApi,
  replyComentByUserApi
} from '@/api/commentApi';
import { getOrderByIdApi } from '@/api/orderApi';

export default {
  components: {
    Comment,
    SvgPage
  },
  data() {
    return {
      commentList: [],
      currentPage: 1,
      pageSize: 5,
      total: 0,
      dgVisible: false,
      dgComment: {},
      dgIndex: 0,
      dgTitle: ''
    };
  },
  created() {
    this.getCommentPage();
  },
  methods: {
    getCommentPage() {
      getCommentPageByUserApi(this.currentPage, this.pageSize)
        .then(res => {
          if (res.data.flag) {
            this.commentList = res.data.data.records;
            this.total = res.data.data.total;
          }
        })
        .catch(err => {
          this.$showMsg('网络繁忙，请稍后刷新重试', { type: 'error' });
        });
    },
    /**
     * 改变页码
     */
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getCommentPage();
    },
    /**
     * 改变页量
     */
    handleSizeChange(val) {
      this.pageSize = val;
      this.getCommentPage();
    },
    /**
     * 处理删除事件
     */
    handleDelete(id) {
      // console.log('delete comment =>', id);
      this.$confirm('评价删除后将不可恢复, 是否继续?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'danger'
      })
        .then(() => {
          deleteCommentApi(id)
            .then(res => {
              if (res.data.flag) {
                this.$showMsg('删除成功', { type: 'success' });
                this.getCommentPage();
              }
            })
            .catch(err => {
              this.$showMsg('网络繁忙，请稍后重试', { type: 'error' });
            })
            .finally(() => {
              this.dgVisible = false;
            });
        })
        .catch(() => {});
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
    },
    handleCommentDetail(comment) {
      this.dgComment = comment;
      this.dgVisible = true;
      this.dgTitle = comment.orderNumber;
    },
    /**
     * 提交评价
     */
    handleSubmitComment(comment) {
      // this.commentDgVisable = false;
      // console.log('提交评价 =>', comment);
      addCommentOfOrderItemApi(comment)
        .then(res => {
          if (res.data.flag) {
            this.$showMsg('评价成功', { type: 'success' });
            this.getCommentPage();
          }
        })
        .catch(err => {
          this.$showMsg('网络繁忙，请稍后重试', { type: 'error' });
        })
        .finally(() => {
          this.dgVisible = false;
        });
    },
    /**
     * 回复
     */
    handleSubmitReply(reply) {
      // console.log('提交回复 ==>', reply);
      replyComentByUserApi(reply)
        .then(res => {
          if (res.data.flag) {
            this.$showMsg('回复成功', { type: 'success' });
            this.getCommentPage();
          }
        })
        .catch(err => {
          this.$showMsg('网络繁忙，请稍后重试', { type: 'error' });
        })
        .finally(() => {
          this.dgVisible = false;
        });
    },
    /**
     * 删除评价
     */
    handleDeleteComment(commentId) {
      console.log('删除评价 ==>', commentId);
      this.handleDelete(commentId);
    }
  }
};
</script>

<style lang="less" scoped>
.mine-comment {
  padding-bottom: 120px;
}
.mine-comment-list {
  width: 100%;
  margin-top: 24px;
  display: flex;
  align-items: center;
  flex-direction: column;
  padding: 0 6px;
  box-sizing: border-box;
  .comment-item {
    cursor: pointer;
    width: 100%;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    border-radius: 8px;
    background-color: #f5f5f5;
    padding: 6px 12px;
    & + .comment-item {
      margin-top: 12px;
    }
  }
}
.comment-title-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 24px;
  .comment-title-item {
    user-select: none;
    display: flex;
    align-items: center;
    font-size: 12px;
    .comment-title__label {
      color: var(--primary-text);
    }
    .comment-title__text {
      color: #6b798e;
      &.link {
        cursor: pointer;
        transition: color 0.5s ease-in-out;
        &:hover {
          color: var(--link-text);
        }
      }
    }
    .ic-delete {
      cursor: pointer;
      color: #999;
      font-size: 16px;
      margin: 0 12px;
      transition: all 0.5s ease-in-out;
      &:hover {
        color: var(--danger);
      }
    }
  }
}
.comment-item-main {
  margin-top: 12px;
  display: flex;
  .comment-item-main-book {
    width: 95px;
    height: 95px;
    display: flex;
    flex-direction: column;
    align-items: center;
    flex-shrink: 0;
    margin-right: 8px;
    .comment-item__bc {
      width: 75px;
      height: 75px;
      border-radius: 5px;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .comment-item__bn {
      width: 100%;
      font-size: 12px;
      color: #666666;
      text-align: center;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
    }
  }
  .comment-item-main-content {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    .comment-item__mess {
      width: 100%;
      display: flex;
      justify-content: space-between;
      .comment-item_mess__score {
        :deep(.el-rate) {
          .ic-score {
            font-size: 16px;
          }
          .el-rate__text {
            margin-left: 5px;
            font-size: 16px;
          }
        }
      }
      .comment-item_mess__time {
        font-size: 10px;
        color: #d4dde1;
      }
    }
    .comment-item__content {
      width: 100%;
      box-sizing: border-box;
      padding: 12px 8px;
      font-size: 12px;
      color: #999999;
      text-indent: 2em;
    }
  }
}
.page-tool {
  margin: 0 auto;
  margin-top: 24px;
}
.no-comment {
  margin: 36px 0;
}
.comment-dialog {
  :deep(.el-dialog) {
    .el-button--primary {
      border-radius: 20px;
      padding: 12px 36px;
    }
    .el-dialog__header {
      .el-dialog__title {
        font-weight: 500;
      }
    }
  }
  .dialog-comment-box {
    width: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    box-sizing: border-box;
    padding: 0 16px;
  }
}
</style>

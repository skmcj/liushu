<template>
  <div
    class="sk-svg-page"
    :class="{
      top: position === 'top',
      bottom: position === 'bottom',
      left: position === 'left',
      right: position === 'right'
    }">
    <div class="sk-svg-box" :style="imgStyle">
      <img v-if="name === 'no-records'" :src="noRecords" alt="no-records" />
      <img v-if="name === 'no-result'" :src="noResult" alt="no-result" />
      <img v-if="name === 'no-content'" :src="noContent" alt="no-content" />
      <img v-if="name === 'no-order'" :src="noOrder" alt="no-order" />
      <img v-if="name === 'no-mess'" :src="noMess" alt="no-mess" />
      <img v-if="name === 'no-address'" :src="noAddress" alt="no-address" />
      <img v-if="name === 'no-cart'" :src="noCart" alt="no-cart" />
      <img v-if="name === 'no-comment'" :src="noComment" alt="no-comment" />
      <img v-if="name === 'no-coupon'" :src="noCoupon" alt="no-coupon" />
    </div>
    <div
      class="sk-svg-mess"
      :class="{
        center: position === 'center',
        left: position === 'left',
        right: position === 'right'
      }"
      v-if="text">
      <div class="sk-svg-text">{{ text }}</div>
      <div class="sk-svg-tip">{{ tip }}</div>
    </div>
  </div>
</template>

<script>
import noRecords from '@/assets/images/svg/no-records.svg';
import noResult from '@/assets/images/svg/no-result.svg';
import noContent from '@/assets/images/svg/no-content.svg';
import noOrder from '@/assets/images/svg/no-order.svg';
import noMess from '@/assets/images/svg/no-mess.svg';
import noAddress from '@/assets/images/svg/no-address.svg';
import noCart from '@/assets/images/svg/no-cart.svg';
import noComment from '@/assets/images/svg/no-comment.svg';
import noCoupon from '@/assets/images/svg/no-coupon.svg';

export default {
  props: {
    position: {
      // top | bottom | left | right
      type: String,
      default: 'bottom'
    },
    name: {
      type: String,
      default: 'no-result'
    },
    imgWidth: Number,
    imgHeight: Number,
    fontPosition: {
      // center | left | right
      type: String,
      default: 'center'
    },
    text: String,
    tip: String
  },
  data() {
    return {
      noRecords,
      noResult,
      noContent,
      noOrder,
      noMess,
      noAddress,
      noCart,
      noComment,
      noCoupon
    };
  },
  computed: {
    imgStyle() {
      let style = {};
      if (this.imgWidth && this.imgHeight) {
        style.width = this.imgWidth + 'px';
        style.height = this.imgHeight + 'px';
      } else if (this.imgWidth) {
        style.width = this.imgWidth + 'px';
        style.height = this.imgWidth + 'px';
      } else if (this.imgHeight) {
        style.width = this.imgHeight + 'px';
        style.height = this.imgHeight + 'px';
      } else {
        style.width = '450px';
        style.height = '450px';
      }
      return style;
    }
  }
};
</script>

<style lang="less" scoped>
.sk-svg-page {
  display: flex;
  align-items: center;
  &.top {
    flex-direction: column-reverse;
  }
  &.bottom {
    flex-direction: column;
  }
  &.left {
    flex-direction: row-reverse;
  }
  &.right {
    flex-direction: row;
  }
  .sk-svg-box {
    img {
      width: 100%;
      height: 100%;
    }
  }
  .sk-svg-mess {
    flex-grow: 1;
    display: flex;
    align-items: center;
    &.center {
      justify-content: center;
    }
    &.left {
      justify-content: flex-start;
    }
    &.right {
      justify-content: flex-end;
    }
    .sk-svg-text {
      font-size: 18px;
      color: #666666;
    }
    .sk-svg-tip {
      font-size: 14px;
      color: #999999;
    }
  }
}
</style>

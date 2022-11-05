<template>
  <div class="sk-shop-card" @click="hanleClick">
    <div class="sk-shop-card-cover">
      <img :src="cover ? cover : $noCoverUrl" />
    </div>
    <div class="sk-shop-card-content">
      <div class="sk-shop-card__title">{{ name }}</div>
      <div class="sk-shop-card__score">
        <i class="icon ic-score"></i>
        <span class="text">{{ score ? score : '暂无' }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    cover: {
      type: String,
      default: null
    },
    name: {
      type: String,
      default: null
    },
    score: {
      type: Number,
      default: null
    },
    hanleClick: {
      type: Function,
      default: () => {}
    }
  }
};
</script>

<style lang="less" scoped>
.sk-shop-card {
  cursor: pointer;
  width: 300px;
  height: 217px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: space-between;
  .sk-shop-card-cover {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 186px;
    border-radius: 5px;
    overflow: hidden;
    box-shadow: 0px 4px 16px 0px rgba(0, 0, 0, 0.16);
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  .sk-shop-card-content {
    user-select: none;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 0 5px;
    margin-top: 5px;
    .sk-shop-card__title {
      font-size: 18px;
      color: #3d3d3d;
    }
    .sk-shop-card__score {
      display: flex;
      align-items: center;
      font-size: 14px;
      color: #83ccd2;
      .icon {
        margin-right: 3px;
      }
    }
  }
}
</style>

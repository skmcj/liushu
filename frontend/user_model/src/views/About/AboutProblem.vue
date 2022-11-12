<template>
  <div class="about-problem-box">
    <div class="about-problem-group">
      <div class="about-problem-title">
        <span class="icon"></span>
        <span class="text">使用指南</span>
      </div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">如何进行借阅？</div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">如何进行还书？</div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">如何联系商家？</div>
    </div>
    <div class="about-problem-group">
      <div class="about-problem-title">
        <span class="icon"></span>
        <span class="text">订单指南</span>
      </div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">配送方式具体是怎样的？</div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">取消订单后怎么退款给我？</div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">退款怎么查询？</div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">怎么进行催单？</div>
    </div>
    <div class="about-problem-group">
      <div class="about-problem-title">
        <span class="icon"></span>
        <span class="text">账户指南</span>
      </div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">如何修改密码？</div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">忘记密码怎么办？</div>
      <div class="about-problem-item" @click.stop="handleProblem(1)">忘记账户怎么办？</div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    handleProblem(id) {
      this.$router.push(`/about/problem/q=${id}`);
    }
  }
};
</script>

<style lang="less" scoped>
.about-problem-box {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  box-sizing: border-box;
  padding: 24px 36px 180px;
  .about-problem-group {
    margin-top: 12px;
    width: 50%;
    display: flex;
    flex-direction: column;
    .about-problem-title {
      display: flex;
      align-items: center;
      .icon {
        width: 5px;
        height: 18px;
        border-radius: 2.5px;
        background-color: var(--primary);
      }
      .text {
        margin-left: 5px;
        font-size: 18px;
        color: var(--primary-text);
      }
    }
    .about-problem-item {
      cursor: pointer;
      user-select: none;
      font-size: 16px;
      color: var(--primary);
      margin-top: 8px;
      margin-left: 18px;
      &:hover {
        opacity: 0.8;
      }
    }
  }
}
</style>

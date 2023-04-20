<template>
  <div class="about-problem-result-box">
    <div class="about-problem-q">
      <span class="icon"></span>
      <span class="text">{{ qContent }}</span>
    </div>
    <div class="about-problem-r">
      <div class="about-problem-r__item" v-for="(item, index) in qResult" :key="'about-q-' + index">{{ item }}</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      qId: '',
      qContent: '',
      qResult: []
    };
  },
  created() {
    console.log('q =>', this.$route);
    this.qId = this.$route.params.id;
    if (this.qId) {
      this.getQuestionResult(this.qId);
    }
  },
  methods: {
    /**
     * 获取问题答案
     */
    getQuestionResult(id) {
      // 发送请求
      // 测试模拟
      this.qContent = '如何进行借阅？';
      let data = '首先浏览网站，找寻自己的目标书籍。\n接着点击借阅，填写必要的信息，等待图书配送上门即可。';
      this.qResult = data.split('\n');
    }
  }
};
</script>

<style lang="less" scoped>
.about-problem-result-box {
  padding-bottom: 120px;
  margin-top: 12px;
  width: 100%;
  display: flex;
  flex-direction: column;
  .about-problem-q {
    width: 100%;
    display: flex;
    align-items: center;
    padding: 0 36px;
    box-sizing: border-box;
    .icon {
      width: 5px;
      height: 21px;
      border-radius: 2.5px;
      background-color: var(--primary);
    }
    .text {
      margin-left: 12px;
      font-size: 16px;
      color: var(--primary-text);
    }
  }
  .about-problem-r {
    margin-top: 42px;
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 0 36px;
    box-sizing: border-box;
    .about-problem-r__item {
      width: 100%;
      font-size: 14px;
      color: #666666;
      text-indent: 2em;
      & + .about-problem-r__item {
        margin-top: 24px;
      }
    }
  }
}
</style>

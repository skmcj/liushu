<template>
  <div class="about-our-box">
    <div
      class="about-our-item"
      v-for="(item, index) in csData"
      :key="'about-our-' + index"
      @click.stop="handleCopy(item.contact)">
      <div class="about-our-title">{{ item.title }}</div>
      <div class="about-our-main">
        <div class="about-our-text copy">{{ item.contact }}</div>
        <div class="about-our-text">工作时间：{{ item.time }}</div>
        <div class="about-our-text">{{ item.tip }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      csData: [
        {
          title: '电话客服',
          contact: '0234-7254321',
          time: '8:00-21:00',
          tip: '节假日不上班'
        },
        {
          title: '邮箱客服',
          contact: '2022@liushu.com',
          time: '8:00-21:00',
          tip: '节假日不上班'
        }
      ]
    };
  },
  methods: {
    /**
     * 将内容复制待剪切板
     */
    handleCopy(text) {
      let oInput = document.createElement('input');
      oInput.value = text;
      document.body.appendChild(oInput);
      oInput.select(); // 选择对象;
      console.log(oInput.value);
      document.execCommand('Copy'); // 执行浏览器复制命令
      oInput.remove();
      this.$showMsg('联系方式已复制到剪切板');
    }
  }
};
</script>

<style lang="less" scoped>
.about-our-box {
  display: flex;
  flex-wrap: wrap;
  gap: 24px 36px;
  box-sizing: border-box;
  padding: 36px;
  padding-bottom: 48px;
  .about-our-item {
    cursor: pointer;
    width: 240px;
    height: 132px;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-sizing: border-box;
    padding: 12px;
    border-radius: 5px;
    background-color: #fff;
    box-shadow: 0px 4px 16px 0px rgba(0, 0, 0, 0.16);
    .about-our-title {
      user-select: none;
      font-size: 18px;
      font-weight: bold;
      color: var(--primary-text);
    }
    .about-our-main {
      margin-top: 10px;
      display: flex;
      flex-direction: column;
      align-items: center;
      .about-our-text {
        user-select: none;
        font-size: 14px;
        color: var(--secondary-text);
        & + .about-our-text {
          margin-top: 5px;
        }
        &.copy {
          user-select: auto;
        }
      }
    }
    &:active {
      box-shadow: inset 2px 3px 8px 0px rgba(0, 0, 0, 0.16);
    }
  }
}
</style>

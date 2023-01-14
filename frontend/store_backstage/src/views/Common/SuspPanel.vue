<template>
  <div class="susp-panel">
    <!-- 悬浮球 -->
    <div class="susp-ball">
      <label title="关闭" @click="handleItem('close')">
        <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
          <path class="line--1" d="M0 40h62c13 0 6 28-4 18L35 35"></path>
          <path class="line--2" d="M0 50h70"></path>
          <path class="line--3" d="M0 60h62c13 0 6-28-4-18L35 65"></path>
        </svg>
      </label>
    </div>
    <!-- <div class="susp-menu">
      <div class="menu-item" title="首页" @click="handleItem('home')">
        <i class="ic-home"></i>
      </div>
      <div class="menu-item" title="分类" @click="handleItem('cate')">
        <i class="ic-cate-1"></i>
      </div>
      <div class="menu-item" title="关于" @click="handleItem('about')">
        <i class="ic-about"></i>
      </div>
      <div class="menu-item" title="我的" @click="handleItem('mine')">
        <i class="ic-mine-2"></i>
      </div>
    </div> -->
  </div>
</template>

<script>
export default {
  methods: {
    handleItem(text) {
      this.$emit('handleItem', text);
    }
  }
};
</script>

<style lang="less" scoped>
.susp-panel {
  width: 64px;
  height: 64px;
  display: flex;
  flex-direction: column-reverse;
  align-items: center;
  box-sizing: border-box;
  background-color: #83ccd2;
  transition: all 0.6s ease-in-out;
  border-radius: 32px;
  box-shadow: 0px 4px 16px 0px rgba(0, 0, 0, 0.16);
  .susp-menu {
    display: none;
    flex-direction: column;
    align-items: center;
    width: 64px;
    box-sizing: border-box;
    padding: 24px 0 64px 0;
    // transition: opacity 0.5s ease-in-out 0.6s;
    .menu-item {
      cursor: pointer;
      width: 48px;
      height: 48px;
      border-radius: 8px;
      transition: background-color 0.5s ease-in-out;
      i {
        display: inline-block;
        width: 48px;
        height: 48px;
        text-align: center;
        line-height: 48px;
        font-size: 25px;
        color: #fff;
      }
      &:hover {
        background-color: rgba(245, 243, 242, 0.3);
      }
      & + .menu-item {
        margin-top: 8px;
      }
    }
  }
  .susp-ball {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    label {
      cursor: pointer;
      width: 48px;
      height: 48px;
      display: block;
      border-radius: 8px;
      cursor: pointer;
      position: relative;
      transition: background-color 0.5s ease-in-out;
      // width: 100%;
      // height: 100%;
      &:hover {
        background-color: rgba(245, 243, 242, 0.3);
      }
    }
    svg {
      scale: 1.5;
    }
    .line--1,
    .line--3 {
      --total-length: 126.64183044433594;
    }
    .line--2 {
      --total-length: 70;
    }
    path {
      --length: 24;
      --offset: -38;
      fill: none;
      stroke: #ffffff;
      stroke-width: 3;
      stroke-linecap: round;
      stroke-linejoin: round;
      stroke-dasharray: var(--length) var(--total-length);
      stroke-dashoffset: var(--offset);
      transition: all 0.8s cubic-bezier(0.645, 0.045, 0.355, 1);
    }
  }
  &:hover {
    border-radius: 12px;
    // height: 368px;
    .susp-menu {
      display: flex;
      animation: fade-in 1.2s ease-in-out;
    }
    .susp-ball {
      svg {
        .line--1,
        .line--3 {
          --offset: -94.1149185097;
        }
        .line--2 {
          --offset: -50;
        }
      }
      label {
        svg {
          .line--1,
          .line--3 {
            --length: 22.627416998;
          }
          .line--2 {
            --length: 0;
          }
        }
      }
    }
  }
}
</style>

<template>
  <div class="sk-tools" :style="setStyle">
    <ReturnTopTool v-if="returnTop" :handle-click="handleReturnTop" />
    <CateListTool
      v-if="cate"
      :is-active="activeCate"
      :cate-list="cateList"
      :handle-click="handleCate"
      @listenActiveHash="listenCateHash" />
    <slot></slot>
  </div>
</template>

<script>
import ReturnTopTool from '@/components/Tool/ReturnTopTool';
import CateListTool from '@/components/Tool/CateListTool';

export default {
  components: {
    ReturnTopTool,
    CateListTool
  },
  props: {
    width: {
      type: Number,
      default: 45
    },
    returnTop: {
      type: Boolean,
      default: true
    },
    handleReturnTop: {
      type: Function,
      default: null
    },
    topVisibleHeight: {
      type: Number,
      default: 300
    },
    cate: {
      type: Boolean,
      default: false
    },
    handleCate: {
      type: Function,
      default: null
    },
    cateList: {
      type: Array,
      default: () => {
        return [];
      }
    },
    activeCate: {
      type: String,
      default: null
    },
    listenCateHash: {
      type: Function,
      default: hash => {}
    }
  },
  computed: {
    setStyle() {
      let style = {};
      style['--sk-tool-width'] = this.width + 'px';
      return style;
    }
  }
};
</script>

<style lang="less" scoped>
.sk-tools {
  display: flex;
  flex-direction: column-reverse;
  align-items: center;
  div + div {
    margin-bottom: 8px;
  }
}
</style>

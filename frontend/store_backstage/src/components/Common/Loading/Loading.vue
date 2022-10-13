<template>
  <div class="sk-loading">
    <BookLoading :color="color" :tip="tip" />
  </div>
</template>

<script>
import BookLoading from '@/components/Common/Loading/BookLoading';

export default {
  components: {
    BookLoading
  },
  props: {
    theme: {
      type: String,
      default: 'book'
    },
    tip: {
      type: String,
      default: ''
    },
    color: {
      type: String,
      default: '#666666'
    }
  }
};
</script>

<style lang="less" scoped>
.sk-loading {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>

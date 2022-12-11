<template>
  <div class="ic-icon" :class="messageIconClass" @click="handleIconClick"></div>
</template>

<script>
export default {
  name: 'MessageStatusIcon',
  props: {
    message: {
      type: Object,
      required: true
    }
  },
  computed: {
    messageIconClass() {
      switch (this.message.status) {
        case 'unSend':
          return 'is-loading';
        case 'fail':
          return 'is-fail';
        default:
          return '';
      }
    }
  },
  methods: {
    handleIconClick() {
      if (this.messageIconClass === 'is-fail') {
        this.tim.resendMessage(this.message).catch(imError => {
          this.$store.commit('showMessage', {
            message: imError.message,
            type: 'error'
          });
        });
      }
    }
  }
};
</script>

<style lang="less" scoped>
.is-loading,
.is-fail {
  width: 16px;
  height: 16px;
  font-size: 16px;
  text-align: center;
  line-height: 16px;
  padding: 8px;
}
.is-loading {
  animation: 2s linear 0s infinite normal none running rotating;
  &::before {
    content: '\e6de';
  }
}
.is-fail {
  color: #f35f5f;
  // background-color: #f35f5f;
  // color: #fff;
  border-radius: 50%;
  text-align: center;
  line-height: 16px;
  cursor: pointer;
  &::before {
    content: '\e64f';
  }
}
@keyframes rotating {
  0% {
    transform: rotateZ(0deg);
  }
  100% {
    transform: rotateZ(360deg);
  }
}
</style>

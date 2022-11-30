<template>
  <div
    class="sk-input-pay"
    :class="{
      'is-focus': focused
    }">
    <div class="sk-input-pay-box">
      <div
        class="sk-input-pay-item"
        v-for="index in size"
        :key="'sk-input-pay-item-' + index"
        :class="{
          'is-active': index - 1 === nativeInputValue.length
        }">
        <span class="sk-input-item__text" v-if="showPassword">{{ nativeInputValue.substr(index - 1, 1) }}</span>
        <span class="sk-input-pay__dot" v-if="!showPassword && nativeInputValue.length >= index"></span>
      </div>
      <input
        class="sk-input__inner"
        ref="input"
        type="text"
        @compositionstart="handleCompositionStart"
        @compositionupdate="handleCompositionUpdate"
        @compositionend="handleCompositionEnd"
        @input="handleInput"
        @focus="handleFocus"
        @blur="handleBlur"
        @change="handleChange" />
    </div>
    <div class="sk-input-pay-tip">{{ tip }}</div>
  </div>
</template>

<script>
import { isKorean } from '@/utils/shared';

export default {
  props: {
    value: [String, Number],
    size: {
      type: Number,
      default: 6
    },
    disabled: {
      type: Boolean,
      default: false
    },
    showPassword: {
      type: Boolean,
      default: false
    },
    tip: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      focused: false,
      isComposing: false
    };
  },
  methods: {
    setNativeInputValue() {
      const input = this.getInput();
      if (!input) return;
      if (input.value === this.nativeInputValue) return;
      input.value = this.nativeInputValue;
    },
    handleFocus(event) {
      this.focused = true;
      this.$emit('focus', event);
    },
    handleBlur(event) {
      this.focused = false;
      this.$emit('blur', event);
    },
    handleCompositionStart() {
      this.isComposing = true;
    },
    handleCompositionUpdate(event) {
      const text = event.target.value;
      const lastCharacter = text[text.length - 1] || '';
      this.isComposing = !isKorean(lastCharacter);
    },
    handleCompositionEnd(event) {
      if (this.isComposing) {
        console.log('end');
        this.isComposing = false;
        this.handleInput(event);
      }
    },
    handleInput(event) {
      // 避免在合成过程中发出输入
      if (this.isComposing) return;

      // 当不支持IE时删除
      if (event.target.value === this.nativeInputValue) return;

      this.$emit('input', event.target.value);

      // 确保本机输入值受到控制
      this.$nextTick(this.setNativeInputValue);
    },
    handleChange(event) {
      this.$emit('change', event.target.value);
    },
    getInput() {
      return this.$refs.input;
    }
  },
  computed: {
    nativeInputValue() {
      if (this.value === null || this.value === undefined) return '';
      if (String(this.value).length >= 6) {
        // 父组件通过 @complete 监听，支付密码输入完成
        this.$emit('complete', String(this.value).slice(0, 6));
        return String(this.value).slice(0, 6);
      }
      return String(this.value);
    }
  },
  watch: {
    nativeInputValue() {
      this.setNativeInputValue();
    }
  }
};
</script>

<style lang="less" scoped>
.sk-input-pay {
  display: flex;
  align-items: center;
  flex-direction: column;
  .sk-input-pay-box {
    position: relative;
    display: flex;
    align-items: center;
    .sk-input-pay-item {
      position: relative;
      width: 36px;
      height: 42px;
      border-radius: 5px;
      box-sizing: border-box;
      border: 1px solid #dcdfe6;
      transition: all 0.5s ease-in-out;
      display: flex;
      align-items: center;
      justify-content: center;
      .sk-input-item__text {
        font-size: 16px;
        font-weight: bold;
        color: #595857;
      }
      .sk-input-pay__dot {
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background-color: #595857;
      }
      & + .sk-input-pay-item {
        margin-left: 5px;
      }
    }
    .sk-input__inner {
      position: absolute;
      width: 100%;
      height: 100%;
      opacity: 0;
      outline: none;
    }
  }
  .sk-input-pay-tip {
    user-select: none;
    width: 100%;
    text-align: left;
    margin-top: 5px;
    font-size: 12px;
    color: #a4a4a4;
  }
  &.is-focus {
    .sk-input-pay-box {
      .sk-input-pay-item {
        border-color: #83ccd2;
        &.is-active {
          &::before {
            content: '';
            display: inline-block;
            position: absolute;
            left: 5px;
            top: 50%;
            transform: translateY(-50%);
            width: 2px;
            height: 64%;
            border-radius: 1px;
            background-color: #95949a;
            animation: flash 1.2s infinite;
          }
        }
      }
    }
  }
  &.is-error {
    .sk-input-pay-box {
      .sk-input-pay-item {
        border-color: #cd5e3c;
      }
    }
    .sk-input-pay-tip {
      color: #cd5e3c;
    }
  }
  @keyframes flash {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
}
</style>

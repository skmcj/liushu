<template>
  <div
    class="avatar-box"
    :class="{
      circle: shape === 'circle',
      'mi-logo': shape === 'mi'
    }">
    <img :src="avatarSrc" alt="头像" />
  </div>
</template>

<script>
import { genAvatarByText, getColorByText } from '@/utils/im/imHandle';
import systemAvatar from '@/assets/images/system.png';

export default {
  props: {
    src: String,
    type: {
      type: String,
      default: 'C2C'
    },
    shape: {
      type: String,
      default: 'mi'
    },
    text: String
  },
  methods: {
    handleUserAvatar(avatarUrl) {
      let name = avatarUrl.substr(avatarUrl.lastIndexOf('/') + 1);
      return this.$ossPath + name;
    }
  },
  computed: {
    avatarSrc() {
      let src = this.src;
      if (/^https:\/\/liushu.com\/img\/(bkc-|sta-|ua-|com-|ot-)/.test(src)) {
        return this.handleUserAvatar(src);
      } else if (/^(https:|http:|\/\/)/.test(src)) {
        return src;
      } else {
        return this.defaultSrc;
      }
    },
    defaultSrc() {
      let list = ['临', '兵', '斗', '者', '皆', '阵', '列', '前', '行'];
      let textList = this.text ? this.text[0] : list;
      let text = textList[Math.floor(Math.random() * textList.length)];
      let color = getColorByText(text);
      switch (this.type) {
        case 'C2C':
          // 个人头像
          return genAvatarByText(text, [300, 300], color);
        case 'GROUP':
          // 群默认头像
          return genAvatarByText(text, [300, 300], color);
        case this.TIM.TYPES.CONV_SYSTEM:
          return systemAvatar;
        default:
          // 默认头像
          return genAvatarByText(text, [300, 300], color);
      }
    }
  }
};
</script>

<style lang="less" scoped>
.avatar-box {
  width: 56px;
  height: 56px;
  overflow: hidden;
  &.circle {
    border-radius: 50%;
  }
  &.mi-logo {
    box-shadow: rgba(0, 0, 0, 0.1) 0px 5px 10px 0px;
    border-radius: 19px;
  }
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}
</style>

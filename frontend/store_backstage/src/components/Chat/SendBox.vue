<template>
  <div class="send-box">
    <div class="send-tools">
      <div class="send-tool">
        <span class="ic-smile" tabindex="11" title="表情" @click="handleEmojiClick"></span>
        <main class="face-main" v-show="emojiFlag" ref="emojiDialog">
          <ul class="face-list" v-for="(item, index) in list" :key="index" v-show="currentIndex === index">
            <li
              class="face-list-item"
              v-for="(childrenItem, childrenIndex) in item"
              :key="childrenIndex"
              @click="select(childrenItem, childrenIndex)">
              <img v-if="index === 0" :src="emojiUrl + emojiMap[childrenItem]" />
              <img class="face-img" v-else :src="faceUrl + childrenItem + '@2x.png'" />
            </li>
          </ul>
          <ul class="face-tab">
            <li class="face-tab-item" @click="selectFace(0)">
              <i class="ic-smile"></i>
            </li>
            <li class="face-tab-item" v-for="(item, index) in bigEmojiList" :key="index" @click="selectFace(index + 1)">
              <img :src="faceUrl + item.list[0] + '@2x.png'" />
            </li>
          </ul>
        </main>
      </div>
      <div class="send-tool"><span class="ic-picture" title="相册" @click="handleClickAlbum"></span></div>
      <div class="send-tool"><span class="ic-file" title="文件" @click="handleClickFile"></span></div>
    </div>
    <div class="send-content">
      <!-- enter - 发送，ctrl enter - 回车 -->
      <textarea
        ref="text-input"
        rows="4"
        resize="false"
        v-model="messageContent"
        placeholder="请输入消息"
        class="send-input"
        @focus="focus = true"
        @blur="focus = false"
        @keydown.enter.exact.prevent="handleEnter"
        @keyup.ctrl.enter.prevent.exact="handleLine"></textarea>
      <div class="send-btn" :class="{ disabled: !messageContent }" title="发送" @click="sendTextMessage">
        <i class="ic-send"></i>
      </div>
    </div>
    <!-- 图片视频 -->
    <input
      type="file"
      id="albumPicker"
      ref="albumPicker"
      accept=".jpg, .jpeg, .png, .gif, .bmp, .mp4"
      @change="sendAlbum"
      style="display: none" />
    <!-- 文件 -->
    <input type="file" id="filePicker" ref="filePicker" @change="sendFile" style="display: none" />
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import { emojiUrl, emojiMap, emojiName, faceUrl, bigEmojiList } from '@/utils/im/emojiMap';

export default {
  data() {
    return {
      emojiUrl,
      emojiMap,
      emojiName,
      faceUrl,
      bigEmojiList,
      // 控制表情列表的显隐
      emojiFlag: false,
      // 发送内容
      messageContent: '',
      show: true,
      currentIndex: 0
    };
  },
  methods: {
    selectFace(index) {
      this.currentIndex = index;
    },
    chooseBigEmoji(item, index) {
      this.closeEmojiPanel();
      let message = this.tim.createFaceMessage({
        to: this.toAccount,
        conversationType: this.currentConversationType,
        payload: {
          index: index + 1,
          data: item
        }
      });
      this.$store.commit('pushCurrentMessageList', message);
      this.$bus.$emit('scroll-bottom');
      this.tim.sendMessage(message).catch(error => {
        this.$store.commit('showMessage', {
          type: 'error',
          message: error.message
        });
      });
    },
    select(item, index) {
      // console.log('img => ', item, index);
      const options = {
        name: item
      };
      if (this.currentIndex === 0) {
        // 选择emoji表情
        // options.type = 'emo';
        // options.url = emojiUrl + emojiMap[item];
        // options.template = `<img src="${emojiUrl + emojiMap[item]}">`;
        this.messageContent += item;
        return '';
      } else {
        this.chooseBigEmoji(item, index);
      }
      // 选择其它图片表情
      // try {
      //   await Face.TUIServer.sendFaceMessage({
      //     // Change large expression display field
      //     index: this.bigEmojiList[this.currentIndex - 1].icon,
      //     data: this.bigEmojiList[this.currentIndex - 1].list[index]
      //   });
      //   TUIAegis.getInstance().reportEvent({
      //     name: 'messageType',
      //     ext1: 'typeFace'
      //   });
      // } catch (error) {
      //   handleErrorPrompts(error, props);
      // }
      // toggleShow();
    },
    /**
     * 点击相册
     */
    handleClickAlbum() {
      this.$refs.albumPicker.click();
    },
    /**
     * 点击文件
     */
    handleClickFile() {
      this.$refs.filePicker.click();
    },
    /**
     * 发送相册资源
     */
    sendAlbum(file) {
      let picker = document.getElementById('albumPicker');
      if (picker.files[0]) {
        let type = picker.files[0].type;
        let sType = type.substr(0, type.indexOf('/'));
        // console.log(sType);
        switch (sType) {
          case 'image':
            this.sendImage(picker);
            break;
          case 'video':
            this.sendVideo(picker);
            break;
          default:
            break;
        }
      }
    },
    /**
     * 处理表情按钮的点击事件
     */
    handleEmojiClick() {
      if (this.emojiFlag) {
        this.closeEmojiPanel();
      } else {
        this.emojiFlag = true;
        document.addEventListener('click', this.listenEmojiBlur);
      }
    },
    /**
     * 关闭表情面板
     */
    closeEmojiPanel() {
      this.emojiFlag = false;
      document.removeEventListener('click', this.listenEmojiBlur);
    },
    // 监听表情列表焦点
    listenEmojiBlur(e) {
      let x = window.event.clientX;
      let y = window.event.clientY;
      const emojiDom = this.$refs.emojiDialog;
      const emojiRect = emojiDom.getBoundingClientRect();
      let divLeft = emojiRect.left;
      let divRight = divLeft + emojiDom.clientWidth;
      let divTop = emojiRect.top;
      let divBottom = divTop + emojiDom.clientHeight;
      if (x < divLeft || x > divRight || y < divTop || y > divBottom) {
        // 表情列表失焦
        this.closeEmojiPanel();
      }
    },
    handleLine() {
      this.messageContent += '\n';
    },
    handlePaste(e) {
      console.log('paste =>', e.clipboardData);
      let clipboardData = e.clipboardData;
      let file;
      if (clipboardData && clipboardData.files && clipboardData.files.length > 0) {
        file = clipboardData.files[0];
      }

      if (typeof file === 'undefined') {
        return;
      }
      let a;
      // 1. 创建消息实例，接口返回的实例可以上屏
      // let message = this.tim.createImageMessage({
      //   to: this.toAccount,
      //   conversationType: this.currentConversationType,
      //   payload: {
      //     file: file
      //   },
      //   onProgress: percent => {
      //     this.$set(message, 'progress', percent); // 手动给message 实例加个响应式属性: progress
      //   }
      // });
      // this.$store.commit('pushCurrentMessageList', message);

      // // 2. 发送消息
      // let promise = this.tim.sendMessage(message);
      // promise.catch(error => {
      //   this.$store.commit('showMessage', {
      //     type: 'error',
      //     message: error.message
      //   });
      // });
    },
    reEditMessage(payload) {
      this.messageContent = payload;
    },
    /**
    handleUp() {
      const index = this.memberList.findIndex(member => member.userID === this.atUserID);
      if (index - 1 < 0) {
        return;
      }
      this.atUserID = this.memberList[index - 1].userID;
    },
    handleDown() {
      const index = this.memberList.findIndex(member => member.userID === this.atUserID);
      if (index + 1 >= this.memberList.length) {
        return;
      }
      this.atUserID = this.memberList[index + 1].userID;
    },
    */
    /**
     * 在输入框回车
     */
    handleEnter() {
      this.sendTextMessage();
    },
    /**
     * 发送文本消息
     */
    sendTextMessage() {
      if (!this.messageContent) return;
      if (this.messageContent.trim().length === 0) {
        this.messageContent = '';
        this.$store.commit('showMessage', {
          type: 'info',
          message: '不能发送空消息'
        });
        return;
      }
      const message = this.tim.createTextMessage({
        to: this.toAccount,
        conversationType: this.currentConversationType,
        payload: { text: this.messageContent }
      });
      this.$store.commit('pushCurrentMessageList', message);
      this.$bus.$emit('scroll-bottom');
      this.tim.sendMessage(message).catch(error => {
        this.$store.commit('showMessage', {
          type: 'error',
          message: error.message
        });
      });
      this.messageContent = '';
    },
    /**
     * 发送图片
     */
    sendImage(picker) {
      const message = this.tim.createImageMessage({
        to: this.toAccount,
        conversationType: this.currentConversationType,
        payload: {
          file: picker // 或者用event.target
        },
        onProgress: percent => {
          this.$set(message, 'progress', percent); // 手动给message 实例加个响应式属性: progress
        }
      });
      this.$store.commit('pushCurrentMessageList', message);
      this.tim
        .sendMessage(message)
        .then(() => {
          this.$refs.albumPicker.value = null;
        })
        .catch(imError => {
          this.$store.commit('showMessage', {
            message: imError.message,
            type: 'error'
          });
        });
    },
    /**
     * 发送视频资源
     */
    sendVideo(picker) {
      const message = this.tim.createVideoMessage({
        to: this.toAccount,
        conversationType: this.currentConversationType,
        payload: {
          file: picker // 或者用event.target
        },
        onProgress: percent => {
          this.$set(message, 'progress', percent); // 手动给message 实例加个响应式属性: progress
        }
      });
      this.$store.commit('pushCurrentMessageList', message);
      this.tim
        .sendMessage(message)
        .then(() => {
          this.$refs.albumPicker.value = null;
        })
        .catch(imError => {
          this.$store.commit('showMessage', {
            message: imError.message,
            type: 'error'
          });
        });
    },
    /**
     * 发送文件
     */
    sendFile() {
      const message = this.tim.createFileMessage({
        to: this.toAccount,
        conversationType: this.currentConversationType,
        payload: {
          file: document.getElementById('filePicker') // 或者用event.target
        },
        onProgress: percent => {
          this.$set(message, 'progress', percent); // 手动给message 实例加个响应式属性: progress
        }
      });
      this.$store.commit('pushCurrentMessageList', message);
      this.tim
        .sendMessage(message)
        .then(() => {
          this.$refs.filePicker.value = null;
        })
        .catch(imError => {
          this.$store.commit('showMessage', {
            message: imError.message,
            type: 'error'
          });
        });
    }
  },
  computed: {
    ...mapGetters(['toAccount', 'currentConversationType']),
    ...mapState({
      userID: state => state.user.userID
    }),
    list() {
      const emjiList = [this.emojiName];
      for (let i = 0; i < this.bigEmojiList.length; i++) {
        emjiList.push(this.bigEmojiList[i].list);
      }
      return emjiList;
    }
  }
};
</script>

<style lang="less" scoped>
.send-box {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.send-tools {
  display: flex;
  align-items: center;
  .send-tool {
    position: relative;
    cursor: pointer;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    &:hover {
      span {
        color: #83ccd2;
      }
    }
    span {
      font-size: 24px;
      color: #808080;
      transition: color 0.5s ease-in-out;
      &.ic-smile {
        font-size: 21px;
      }
      &.ic-picture {
        font-size: 18px;
      }
      &.ic-file {
        font-size: 26px;
      }
    }
    & + .send-tool {
      margin-left: 18px;
    }
  }
  .face-main {
    position: absolute;
    z-index: 5;
    width: 435px;
    height: 250px;
    top: -270px;
    left: -27px;
    padding: 10px;
    display: flex;
    flex-direction: column;
    background: #ffffff;
    border-radius: 5px;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
    animation: slide-in 0.5s ease-in-out;
    .face-list {
      flex: 1;
      display: flex;
      flex-wrap: wrap;
      overflow-y: auto;
      list-style: none;
      .face-list-item {
        padding: 5px;
        img {
          width: 30px;
        }
        .face-img {
          width: 60px;
        }
      }
    }
    .face-tab {
      display: flex;
      align-items: center;
      list-style: none;
      .face-tab-item {
        padding: 0 10px;
        img {
          width: 30px;
        }
        i {
          font-size: 24px;
        }
      }
    }
  }
}
.send-content {
  margin-top: 8px;
  width: 100%;
  flex-grow: 1;
  position: relative;
  .send-input {
    height: 100%;
    outline: none;
    font-size: 14px;
    width: 100%;
    box-sizing: box-sizing;
    border: none;
    resize: none;
    background-color: transparent;
  }
  .send-btn {
    cursor: pointer;
    position: absolute;
    bottom: 5px;
    right: 8px;
    opacity: 1;
    transition: opacity 0.5s ease-in-out;
    i {
      font-size: 32px;
      color: #83ccd2;
    }
    &.disabled {
      cursor: not-allowed;
      // pointer-events: none;
      opacity: 0.5;
    }
    &:hover:not(.disabled) {
      animation: send-shake 3s ease-in-out;
      animation-iteration-count: infinite;
      animation-direction: alternate;
    }
  }
}
// 滑入
@keyframes slide-in {
  0% {
    transform: translateY(24px);
    opacity: 0;
  }
  100% {
    transform: translateY(0);
    opacity: 1;
  }
}
// 对角抖动
@keyframes send-shake {
  0% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(-3px, 3px);
  }
  100% {
    transform: translate(3px, -3px);
  }
}
</style>

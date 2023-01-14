<template>
  <MessageBubble :isMine="isMine" :message="message">
    <div class="file-element-wrapper" title="单击下载" @click="downloadFile">
      <div class="header">
        <i class="file-icon" :class="fileType"></i>
        <div class="file-element">
          <span class="file-name">{{ fileName }}</span>
          <span class="file-size">{{ size }}</span>
        </div>
      </div>
      <el-progress v-if="showProgressBar" :percentage="percentage" color="#83ccd2" />
    </div>
  </MessageBubble>
</template>

<script>
import MessageBubble from '@/components/Chat/Message/MessageBubble';

export default {
  name: 'FileElement',
  props: {
    payload: {
      type: Object,
      required: true
    },
    message: {
      type: Object,
      required: true
    },
    isMine: {
      type: Boolean
    }
  },
  components: {
    MessageBubble
  },
  computed: {
    fileType() {
      /** @type {String} */
      let fileName = this.payload.fileName;
      let type = fileName.substr(fileName.lastIndexOf('.') + 1);
      if (type === 'jpeg' || type === 'png' || type === 'jpg' || type === 'gif') {
        return 'ic-file-img';
      }
      if (type === 'zip' || type === 'rar') {
        return 'ic-file-zip';
      }
      if (type === 'txt') {
        return 'ic-file-txt';
      }
      return 'ic-files';
    },
    fileName() {
      return this.payload.fileName;
    },
    fileUrl() {
      return this.payload.fileUrl;
    },
    size() {
      const size = this.payload.fileSize;
      if (size > 1024) {
        if (size / 1024 > 1024) {
          return `${this.toFixed(size / 1024 / 1024)} Mb`;
        }
        return `${this.toFixed(size / 1024)} Kb`;
      }
      return `${this.toFixed(size)}B`;
    },
    showProgressBar() {
      return this.$parent.message.status === 'unSend';
    },
    percentage() {
      return Math.floor((this.$parent.message.progress || 0) * 100);
    }
  },
  methods: {
    toFixed(number, precision = 2) {
      return number.toFixed(precision);
    },
    downloadFile() {
      // 浏览器支持fetch则用blob下载，避免浏览器点击a标签，跳转到新页面预览的行为
      if (window.fetch) {
        fetch(this.fileUrl)
          .then(res => res.blob())
          .then(blob => {
            let a = document.createElement('a');
            let url = window.URL.createObjectURL(blob);
            a.href = url;
            a.download = this.fileName;
            a.click();
          });
      } else {
        let a = document.createElement('a');
        a.href = this.fileUrl;
        a.target = '_blank';
        a.download = this.filename;
        a.click();
      }
    }
  }
};
</script>

<style lang="less" scoped>
.file-element-wrapper {
  cursor: pointer;
}
.header {
  display: flex;
}
.file-icon {
  font-size: 40px !important;
  &.ic-files {
    color: rgba(255, 122, 69, 0.5);
  }
  &.ic-file-img {
    color: rgba(255, 102, 104, 0.5);
  }
  &.ic-file-txt {
    color: rgba(145, 167, 185, 0.5);
  }
  &.ic-file-zip {
    color: rgba(253, 141, 89, 0.5);
  }
}
.file-element {
  display: flex;
  flex-direction: column;
  margin-left: 12px;
  text-align: left;
}
.file-size {
  font-size: 12px;
  padding-top: 5px;
}
</style>

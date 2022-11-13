<template>
  <div class="about-link-box">
    <div class="about-link-image">
      <div class="title">
        <span class="icon"></span>
        <span class="text">图片链接</span>
      </div>
      <!-- 瀑布流布局 -->
      <div class="about-link-content" id="aboutLinks">
        <div
          class="about-link-item"
          v-for="(item, index) in links.image"
          :key="'about-img-link-' + index"
          @click.stop="openLink(item.url)">
          <img :src="item.logo" :alt="item.name" />
        </div>
      </div>
    </div>
    <div class="about-link-text">
      <div class="title">
        <span class="icon"></span>
        <span class="text">文本链接</span>
      </div>
      <div class="about-link-content">
        <div
          class="about-link-item"
          v-for="(item, index) in links.text"
          :key="'about-text-link-' + index"
          @click.stop="openLink(item.url)">
          <div class="text-link">
            <div class="img">
              <img v-if="item.logo" :src="item.logo" :alt="item.name" />
              <i class="ic-link"></i>
            </div>
            <div class="text">{{ item.name }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Masonry from 'masonry-layout';

export default {
  data() {
    return {
      links: {
        image: [
          {
            // 图片链接
            id: '1',
            name: '美团外卖',
            url: 'https://waimai.meituan.com/',
            logo: 'https://p0.meituan.net/openhfiveimages/073cf4ab03d6bc6fa117b53157f3622e4898.png',
            type: 'image'
          },
          {
            // 图片链接
            id: '1',
            name: '中图网',
            url: 'http://www.bookschina.com/',
            logo: 'http://www.bookschina.com/Images/Head/images/images/logo.gif',
            type: 'image'
          }
        ],
        text: [
          {
            // 文本链接
            id: '2',
            name: '美团外卖',
            url: 'https://waimai.meituan.com/',
            logo: '',
            type: 'text'
          }
        ]
      }
    };
  },
  mounted() {
    const linkDom = document.getElementById('aboutLinks');
    const mLink = new Masonry(linkDom, {
      // options...
      itemSelector: '.about-link-item',
      columnWidth: 156,
      gutter: 10
    });
  },
  methods: {
    /**
     * 新标签页打开链接
     */
    openLink(link) {
      window.open(link, '_blank');
    }
  }
};
</script>

<style lang="less" scoped>
.about-link-box {
  box-sizing: border-box;
  padding: 36px;
  padding-bottom: 48px;
  display: flex;
  flex-direction: column;
  .about-link-image,
  .about-link-text {
    display: flex;
    flex-direction: column;
    .title {
      display: flex;
      align-items: center;
      width: 100%;
      height: 36px;
      margin-bottom: 18px;
      .icon {
        width: 5px;
        height: 24px;
        border-radius: 2.5px;
        background-color: var(--primary);
      }
      .text {
        margin-left: 5px;
        font-size: 18px;
        color: var(--primary-text);
      }
    }
    .about-link-content {
      position: relative;
      width: 100%;
    }
    .about-link-item {
      cursor: pointer;
      user-select: none;
      width: 156px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      padding: 5px;
      border-radius: 12px;
      background-color: #fff;
      box-shadow: 0px 3px 8px 0px rgba(0, 0, 0, 0.16);
      img {
        width: 100%;
        object-fit: cover;
        border-radius: 5px;
        overflow: hidden;
      }
      .text-link {
        height: 56px;
        display: flex;
        align-items: center;
        justify-content: center;
        .img {
          width: 42px;
          height: 42px;
          border-radius: 5px;
          overflow: hidden;
          & + .text {
            margin-left: 8px;
          }
          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
          i {
            width: 42px;
            height: 42px;
            display: block;
            border-radius: 50%;
            line-height: 42px;
            text-align: center;
            font-size: 25px;
            color: #fff;
            background-color: var(--primary);
          }
        }
        .text {
          font-size: 18px;
          font-weight: bold;
          color: var(--primary-text);
        }
      }
      &:active {
        box-shadow: 0px 1px 3px 0px rgba(0, 0, 0, 0.16);
      }
    }
  }
  .about-link-image {
    .about-link-content {
      .about-link-item {
        margin-bottom: 16px;
      }
    }
  }
  .about-link-text {
    margin-top: 24px;
    .about-link-content {
      display: flex;
      flex-wrap: wrap;
      row-gap: 12px;
      .about-link-item {
        padding: 0 12px;
        width: auto;
      }
    }
  }
}
</style>

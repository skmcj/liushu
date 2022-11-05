<template>
  <div class="header">
    <div class="left">
      <div class="logo ic-logo"></div>
      <div class="hr"></div>
      <div class="title">
        <span class="name">流书网</span>
        <span class="tip">图书外卖</span>
      </div>
    </div>
    <div class="right">
      <div class="search" :class="{ 'is-show': isShow, 'is-focus': inFocus }">
        <i v-if="isShow" class="close ic-next" @click="closeSearch"></i>
        <input
          v-if="isShow"
          v-model="searchContent"
          type="text"
          placeholder="请输入搜索内容"
          class="search-in"
          @focus="handleFocus"
          @blur="handleBlur" />
        <i class="icon ic-search" @click="handleSearch"></i>
      </div>
      <div class="nav">
        <div class="nav-item" :class="{ 'is-active': activeLink === '/home' }" @click="handleClick('/home')">
          <i class="icon ic-home"></i>
          <span class="link-tit">首页</span>
        </div>
        <div class="nav-item" :class="{ 'is-active': activeLink === '/cate' }" @click="handleClick('/cate')">
          <i class="icon ic-cate-1"></i>
          <span class="link-tit">分类</span>
        </div>
        <div class="nav-item" :class="{ 'is-active': activeLink === '/about' }" @click="handleClick('/about')">
          <i class="icon ic-about"></i>
          <span class="link-tit">关于</span>
        </div>
        <div v-if="!isLogin" class="nav-login">
          <i class="icon ic-mine-2"></i>
          <span class="link-tit" @click="handleClick('/login')">登录</span>
          <span class="hr"></span>
          <span class="link-tit" @click="handleClick('/logon')">注册</span>
        </div>
        <div v-if="isLogin" class="nav-mine" :class="{ 'is-active': activeLink === '/mine' }">
          <div class="avatar" @click="handleClick('/mine')">
            <img class="img" src="@/assets/images/avarat.jpeg" />
          </div>
          <i class="icon ic-bell" @click="handleClick('/mine/mess')"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isLogin: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      home: '/home',
      cate: '/cate',
      about: '/about',
      mine: '/mine',
      activeLink: '/home',
      inFocus: false,
      isShow: false,
      searchContent: ''
    };
  },
  methods: {
    handleClick(to) {
      this.$router.push(to);
      this.activeLink = to;
    },
    handleFocus() {
      this.inFocus = true;
    },
    handleBlur() {
      this.inFocus = false;
    },
    handleSearch() {
      if (this.isShow) {
        console.log(this.searchContent);
      } else {
        this.isShow = true;
      }
    },
    closeSearch() {
      this.isShow = false;
    }
  }
};
</script>

<style lang="less" scoped>
.header {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 96%;
  height: 96px;
  border-radius: 0 48px 48px 0;
  background-image: linear-gradient(to bottom right, var(--primary-g-1), var(--primary-g-2));
  .left {
    display: flex;
    align-items: center;
    margin-left: 32px;
    margin-right: 48px;
    .logo {
      font-size: 72px;
    }
    .hr {
      width: 3px;
      height: 72px;
      background-color: #000;
      border-radius: 1.5px;
      margin: 0 10px;
    }
    .title {
      width: 92px;
      height: 72px;
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      justify-content: center;
      .name {
        font-size: 30px;
      }
      .tip {
        font-size: 14px;
      }
    }
  }
  .right {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    margin-right: 48px;
    flex-grow: 1;
    .search {
      position: relative;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      height: 52px;
      border-radius: 26px;
      margin-right: 48px;
      transition: all 0.8s ease-in;
      &.is-show {
        flex-grow: 1;
        max-width: 560px;
        // max-width: 640px;
        background-color: rgba(241, 242, 243, 0.9);
        &:hover {
          background-color: rgba(241, 242, 243, 1);
          .is-focus {
            .search-in {
              background-color: rgba(210, 212, 214, 0.9);
            }
          }
        }
        .icon {
          color: #7d7d7e;
        }
        .close {
          position: absolute;
          height: 52px;
          line-height: 52px;
          top: 0;
          left: 6px;
          font-size: 18px;
          color: #7d7d7e;
          opacity: 0.1;
          transition: opacity 0.5s ease-in;
          &:hover {
            opacity: 1;
          }
        }
      }
      &.is-focus {
        .search-in {
          background-color: rgba(210, 212, 214, 0.9);
        }
      }
      .search-in {
        width: 100%;
        text-indent: 12px;
        flex-grow: 1;
        margin-left: 30px;
        height: 70%;
        box-sizing: border-box;
        background: none;
        border: none;
        outline: none;
        border-radius: 6px;
        font-size: 16px;
        color: var(--regular-text);
        transition: background-color 0.3s;
      }
      .icon {
        margin: 0 24px;
        font-size: 27px;
        color: var(--white);
      }
    }
    .nav {
      display: flex;
      align-items: center;
      .nav-item {
        display: flex;
        align-items: center;
        margin: 0 12px;
        cursor: pointer;
        transition: all 0.5s ease;
        opacity: 0.75;
        &.is-active {
          opacity: 1;
        }
        .icon {
          font-size: 27px;
          color: var(--white);
          margin-right: 6px;
          opacity: 0.75;
        }
        .link-tit {
          user-select: none;
          font-size: 18px;
          color: var(--white);
        }
        &:hover {
          opacity: 1;
        }
        .hr {
          margin: 0 5px;
          width: 3px;
          height: 21px;
          border-radius: 1.5px;
          background-color: var(--white);
        }
      }
      .nav-mine {
        margin: 0 36px;
        display: flex;
        align-items: center;
        .avatar {
          cursor: pointer;
          width: 45px;
          height: 45px;
          margin-right: 6px;
          border-radius: 50%;
          overflow: hidden;
          .img {
            width: 100%;
            height: 100%;
          }
        }
        .icon {
          cursor: pointer;
          font-size: 21px;
          color: var(--white);
          opacity: 0.75;
          &:hover {
            opacity: 1;
          }
        }
      }
      .nav-login {
        display: flex;
        align-items: center;
        margin: 0 12px;
        cursor: pointer;
        transition: all 0.5s ease;
        &:hover {
          .icon {
            opacity: 1;
          }
        }
        .icon {
          font-size: 27px;
          color: var(--white);
          margin-right: 6px;
          opacity: 0.75;
          transition: opacity 0.5s ease;
        }
        .link-tit {
          user-select: none;
          font-size: 18px;
          color: var(--white);
          opacity: 0.75;
          transition: opacity 0.5s ease;
          &:hover {
            opacity: 1;
          }
        }
        .hr {
          margin: 0 5px;
          width: 3px;
          height: 21px;
          border-radius: 1.5px;
          background-color: var(--white);
        }
      }
    }
  }
}
</style>

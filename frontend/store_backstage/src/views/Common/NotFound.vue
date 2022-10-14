<template>
  <div class="notfound">
    <div class="content">
      <div class="shelf">
        <router-link class="book shop-page" to="/shop">店铺管理</router-link>
        <router-link class="book book-page" to="/books">图书管理</router-link>
        <router-link class="book login" :to="{ name: 'login' }">登 录</router-link>
        <router-link class="book home-page" to="/">首 页</router-link>
        <span class="book not-found"></span>
        <span class="door left"></span>
        <span class="door right"></span>
      </div>
      <h1 class="main-text">Error 404</h1>
      <p class="tip-text">抱歉，您访问的页面丢失了···</p>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="less" scoped>
.notfound {
  width: 100vw;
  height: 100vh;
  background-color: #333;
  box-sizing: border-box;
  padding: 18px 24px;
  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    border-radius: 8px;
    // background-color: #60a3a8;
    .main-text {
      margin: 0;
      margin-top: 32px;
      color: #ccc;
      letter-spacing: 3px;
      font-size: 32px;
    }
    .tip-text {
      margin: 0;
      font-size: 16px;
      color: #ccc;
      margin-top: 12px;
    }
  }
}
.shelf {
  font-size: 16px;
  position: relative;
  border: 0.5em solid #83ccd2;
  border-radius: 0.5em;
  background-color: rgba(255, 255, 255, 0.3);
  perspective: 130em;
  box-shadow: inset 0 0 2em rgba(0, 0, 0, 0.5);
  .book {
    position: absolute;
    box-sizing: border-box;
    padding: 0.8em 4em 0.8em 2em;
    border-radius: 0.25em;
    background-color: rgba(255, 255, 255, 0.2);
    color: white;
    font-size: 16px;
    letter-spacing: 0.5em;
    text-decoration: none;
    box-shadow: inset 0 0 0.75em rgba(255, 255, 255, 0.2);
    &:hover {
      &:not(.not-found) {
        background-color: rgba(255, 255, 255, 0.3);
      }
    }
    &.shop-page {
      left: 3.2em;
      bottom: -1.1em;
      transform: rotate(-90deg);
      transform-origin: 0;
    }
    &.book-page {
      left: 8.5em;
      bottom: -0.8em;
      transform: rotate(-100deg);
      transform-origin: 0;
    }
    &.not-found {
      bottom: -1.1em;
      left: 50%;
      width: 12em;
      height: 3.5em;
      border: 1px dashed rgba(255, 255, 255, 0.4);
      background-color: transparent;
      transform: rotate(-90deg) translateY(-45%) scale(1);
      transform-origin: 0;
      cursor: default;
      -webkit-animation: bookFadeOut 2s 3s infinite forwards;
      animation: bookFadeOut 2s 3s infinite forwards;
      &::after {
        position: absolute;
        top: 50%;
        left: 85%;
        font-family: 'fbookfont' !important;
        font-size: 16px;
        font-style: normal;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        display: inline-block;
        content: '\e639';
        box-sizing: border-box;
        font-size: 64px;
        padding-left: 5rem;
        background-image: url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='80' height='30'><path fill='rgb(255, 255, 255)' d='M7.688,3.737C6.1,10.409,4.624,16.982,2.475,23.517c-0.424,1.29,0.724,2.338,1.957,1.916 c5.879-2.021,11.743-4.107,17.409-6.696c1.246-0.572,0.443-2.366-0.815-1.932c-3.856,1.333-7.695,2.69-11.565,3.959 c2.879-2.526,5.485-5.215,9.013-7.17c4.441-2.459,9.299-4.109,14.281-4.915c10.903-1.772,22.052,0.562,31.979,5.04 c9.241,4.162,12.895,8.725,13.164,4.942c0.121-1.69-5.57-4.953-14.125-8.714C53.19,5.292,41.094,3.539,29.636,6.058 c-7.122,1.567-16.708,5.374-22.064,11.544c1.359-4.455,2.461-9.028,3.032-13.563C10.852,2.091,8.12,1.927,7.688,3.737z'/></svg>");
        background-repeat: no-repeat;
        background-size: 4rem;
        background-position: left center;
        transform: translateY(-50%) rotate(90deg);
        transform-origin: 0;
        opacity: 0.3;
      }
    }
    &.login {
      padding: 0.6em 4em 0.6em 2em;
      bottom: 0.5em;
      right: 2.4em;
      border-radius: 0.25em 0 0 0.25em;
    }
    &.home-page {
      padding: 0.6em 4em 0.6em 2em;
      bottom: 3.2em;
      right: 0.8em;
      border-radius: 0.25em 0 0 0.25em;
    }
  }
  .door {
    position: absolute;
    display: flex;
    box-sizing: border-box;
    padding: 1em;
    background-color: #83ccd2;
    align-items: center;
    box-shadow: 0 1px 1px rgb(0 0 0 / 30%);
    outline: 1px solid transparent;
    &.left {
      left: 0;
      border-radius: 0 0.75em 0.75em 0;
      justify-content: flex-end;
      -webkit-animation: leftDoorOpen 3.5s ease-out forwards 1s, leftDoorFlap 15s linear infinite forwards 9s;
      animation: leftDoorOpen 3.5s ease-out forwards 1s, leftDoorFlap 15s linear infinite forwards 9s;
      transform-origin: 0 0 0;
    }
    &.right {
      right: 0;
      border-radius: 0.75em 0 0 0.75em;
      -webkit-animation: rightDoorOpen 3s ease-out forwards 1.5s, rightDoorFlap 10s linear infinite forwards 8s;
      animation: rightDoorOpen 3s ease-out forwards 1.5s, rightDoorFlap 10s linear infinite forwards 8s;
      transform-origin: 100% 0 0;
    }
    &::before {
      content: '';
      width: 1.5em;
      height: 1.8em;
      border-radius: 50%;
      background-color: rgba(0, 0, 0, 0.1);
    }
  }
}
// 适配不同屏幕
@media (min-width: 1400px) {
  .shelf {
    width: 560px;
    height: 270px;
    .door {
      width: 280px;
      height: 270px;
    }
  }
}
@media (min-width: 1200px) and (max-width: 1399.9px) {
  .shelf {
    width: 465px;
    height: 225px;
    .door {
      width: 232.5px;
      height: 225px;
    }
  }
}
@media (min-width: 768px) and (max-width: 1199.9px) {
  .shelf {
    width: 375px;
    height: 182px;
    .book {
      font-size: 14px;
      &.not-found {
        &::after {
          font-size: 45px;
        }
      }
    }
    .door {
      width: 187.5px;
      height: 182px;
    }
  }
}
@media (min-width: 480px) and (max-width: 767.9px) {
  .shelf {
    width: 320px;
    height: 155px;
    .book {
      font-size: 10px;
      &.not-found {
        &::after {
          font-size: 32px;
        }
      }
    }
    .door {
      width: 160px;
      height: 155px;
    }
  }
}
@media (max-width: 479.9px) {
  .shelf {
    width: 186px;
    height: 270px;
    display: flex;
    align-items: flex-end;
    flex-direction: column-reverse;
    .book {
      position: static;
      font-size: 10px;
      margin-bottom: 3px;
      &.shop-page {
        transform: none;
        border-radius: 0.25em 0 0 0.25em;
        margin-right: 3em;
      }
      &.book-page {
        transform: none;
        border-radius: 0.25em 0 0 0.25em;
        margin-right: 1.8em;
      }
      &.home-page {
        margin-right: 6em;
        margin-bottom: 2px;
      }
      &.login {
        margin-right: 4em;
      }
      &.not-found {
        position: relative;
        bottom: auto;
        left: auto;
        transform: none;
        border-radius: 0.25em 0 0 0.25em;
        margin-right: 6em;
        &::after {
          font-size: 32px;
          left: 15%;
          transform: translateY(-50%) rotate(-30deg);
        }
      }
    }
    .door {
      width: 93px;
      height: 270px;
    }
  }
}
@keyframes leftDoorOpen {
  60% {
    transform: rotateY(-115deg);
  }
  100% {
    transform: rotateY(-110deg);
  }
}
@keyframes rightDoorOpen {
  60% {
    transform: rotateY(125deg);
  }
  100% {
    transform: rotateY(120deg);
  }
}
@keyframes rightDoorFlap {
  0% {
    transform: rotateY(120deg);
  }
  5% {
    transform: rotateY(125deg);
  }
  15% {
    transform: rotateY(117deg);
  }
  25% {
    transform: rotateY(123deg);
  }
  30% {
    transform: rotateY(120deg);
  }
  100% {
    transform: rotateY(120deg);
  }
}
@keyframes leftDoorFlap {
  0% {
    transform: rotateY(-110deg);
  }
  5% {
    transform: rotateY(-115deg);
  }
  15% {
    transform: rotateY(-107deg);
  }
  25% {
    transform: rotateY(-113deg);
  }
  30% {
    transform: rotateY(-110deg);
  }
  100% {
    transform: rotateY(-110deg);
  }
}
@keyframes bookFadeOut {
  50% {
    border: 1px dashed rgba(255, 255, 255, 0.1);
  }
}
</style>

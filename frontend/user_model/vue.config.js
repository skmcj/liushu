const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
// 部署到springboot项目的resource下的store
  publicPath: process.env.NODE_ENV === 'development' ? '/' : '/front/',
  transpileDependencies: true,
  configureWebpack: {
    externals: {
      'vue': 'Vue',
      'element-ui': 'ELEMENT',
      'masonry-layout': 'Masonry'
    }
  },
  devServer: {
    port: 8081
    // 设置跨域代理
    // proxy: 'http://localhost:8080/api'
  }
})

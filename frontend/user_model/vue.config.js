const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    externals: {
      'vue': 'Vue',
      'element-ui': 'ELEMENT'
    }
  },
  devServer: {
    port: 8081
    // 设置跨域代理
    // proxy: 'http://localhost:8080/api'
  }
})

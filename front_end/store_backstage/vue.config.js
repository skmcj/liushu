const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    externals: {
      'vue': 'Vue',
      'element-ui': 'ELEMENT'
    }
  }
})

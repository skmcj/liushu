const { defineConfig } = require('@vue/cli-service');
const path = require('path');

const stylePath = path.resolve(__dirname, './src/assets/style/common.less');

module.exports = defineConfig({
  publicPath: './',
  transpileDependencies: true,
  configureWebpack: {
    externals: {
      'vue': 'Vue',
      'echarts': 'echarts',
      'element-ui': 'ELEMENT'
    }
  },
  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'less',
      patterns: [
        // 这个是加上自己的路径,不能使用(如下:alias)中配置的别名路径
        stylePath
      ]
    }
  }
})

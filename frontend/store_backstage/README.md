# 商家后台

基于`vue2`构建的单页面应用，主要服务于商家，用于管理后台数据，如员工、分类、图书、订单等

## 项目结构

### 大体框架

- ![大体框架](https://pic1.imgdb.cn/item/63610ea116f2c2beb140aab6.png)
- `Aside` — 网站的导航菜单，点击相应的模块，会在右侧`Main`显示相应的内容
- `Container` — 网站内容的主要容器，主要包含`Header`(标题栏)和`Main`(主要内容)
  - `Header` — 网站的标题栏，用于显示登录用户信息及当前页面标题
  - `Main` — 网站的主体内容区域，用于显示用户导航的主体内容

### 代码结构

- `public` — 公用静态资源
- `src` — 源码文件
  - `api` 项目接口
  - `assets` 项目内静态资源
  - `components` 组件
  - `router` 路由
  - `store` 共享数据
  - `utils` 工具
  - `views` 视图
  - `App.vue` 根组件
  - `main.js` 入口文件
- `vue.config.js` — 项目配置
- `package.json` — 项目依赖

## 构建运行

- 安装依赖

  ```sh
  npm install
  ```

- 运行

  ```sh
  npm run serve
  ```

- 打包

  ```sh
  npm run build
  ```

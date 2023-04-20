# 用户模块

用户主要交互的模块，也是**流书网-图书外卖**的主要功能模块。

## 项目结构

### 代码结构

- `public` — 静态文件夹，打包时会原封不动打包
  - `favicon.ico` — 项目图标
  - `index.html` — 单页面应用的主要页面文件
- `src` — 源代码文件夹
  - `api` — 项目`Api`文件夹
  - `assets` — 项目静态文件夹
  - `components` — 项目组件文件夹
  - `router` — 项目路由模块
  - `store` — 项目`VueX`模块
  - `utils` — 项目工具文件夹
  - `views` — 项目视图组件文件夹
  - `App.vue` — 项目根组件
  - `main.js` — 项目入口文件
- `vue.config.js` — 项目配置文件
- `package.json` — 项目依赖配置文件

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

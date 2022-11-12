import Vue from 'vue';
import VueRouter from 'vue-router';
// 组件
import Login from '@/views/Login/Login';
import Main from '@/components/Main/Main';
import Home from '@/views/Home/Home';
import Cate from '@/views/Cate/Cate';
import CateDetail from '@/views/Cate/CateDetail';
import BookDetail from '@/views/Detail/BookDetail';
import ShopDetail from '@/views/Detail/ShopDetail';
import About from '@/views/About/About';
import Mine from '@/views/Mine/Mine';

Vue.use(VueRouter)

const routes = [
  // 登录页
  {
    path: '/login',
    name: 'login',
    components: {
      index: Login
    },
    meta: {
      title: '登录'
    }
  },
  // 主要页面
  {
    path: '/',
    name: 'index',
    components: {
      index: Main
    },
    meta: {
      title: '流书网'
    },
    children: [
      {
        path: '/home',
        name: 'home',
        alias: ['/', '/index'],
        component: Home,
        meta: {
          title: '首页',
          rootLink: '/home'
        }
      },
      {
        path: '/cate',
        name: 'cate',
        component: Cate,
        meta: {
          title: '分类',
          rootLink: '/cate'
        }
      },
      {
        path: '/cate/detail',
        name: 'cateDetail',
        component: CateDetail,
        meta: {
          title: '分类-更多',
          rootLink: '/cate'
        }
      },
      {
        path: '/book',
        name: 'bookDetail',
        component: BookDetail,
        meta: {
          title: '图书详情',
          rootLink: '/book'
        }
      },
      {
        path: '/shop',
        name: 'shopDetail',
        component: ShopDetail,
        meta: {
          title: '商家详情',
          rootLink: '/shop'
        }
      },
      {
        path: '/about',
        name: 'about',
        component: About,
        meta: {
          title: '关于',
          rootLink: '/about'
        }
      },
      {
        path: '/mine',
        name: 'mine',
        component: Mine,
        meta: {
          title: '我的',
          rootLink: '/mine'
        }
      }
    ]
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

// 路由拦截器
router.beforeEach((to, from, next) => {
  // 处理标题
  if (to.meta.title) {
    document.title = to.meta.title;
  } else {
    document.title = '流书网'; // 此处写默认的title
  }
  // 登录验证
  next();
});

export default router;

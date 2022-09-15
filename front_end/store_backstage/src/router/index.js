import Vue from 'vue';
import VueRouter from 'vue-router';

import Employee from '@/views/Employee/Employee.vue';
import Cate from '@/views/Cate/Cate.vue';
import Books from '@/views/Books/Books.vue';
import Orders from '@/views/Orders/Orders.vue';
import Shop from '@/views/Shop/Shop.vue';

Vue.use(VueRouter)

const routes = [
  {
    path: '/employee',
    name: 'employee',
    component: Employee,
    alias: ['/', '/home'],
    meta: {
      title: '商家后台-员工管理'
    }
  },
  {
    path: '/cate',
    name: 'cate',
    component: Cate,
    meta: {
      title: '商家后台-分类管理'
    }
  },
  {
    path: '/books',
    name: 'books',
    component: Books,
    meta: {
      title: '商家后台-图书管理'
    }
  },
  {
    path: '/orders',
    name: 'orders',
    component: Orders,
    meta: {
      title: '商家后台-订单管理'
    }
  },
  {
    path: '/shop',
    name: 'shop',
    component: Shop,
    meta: {
      title: '商家后台-店铺管理'
    }
  }
  // {
  //   path: '/',
  //   name: 'home',
  //   component: HomeView
  // },
  // {
  //   path: '/about',
  //   name: 'about',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  // }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  } else {
    document.title = '商家后台';   // 此处写默认的title
  }
  next();
});

export default router

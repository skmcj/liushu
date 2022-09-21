import Vue from 'vue';
import VueRouter from 'vue-router';

import Employee from '@/views/Employee/Employee.vue';
import Cate from '@/views/Cate/Cate.vue';
import Books from '@/views/Books/Books.vue';
import Orders from '@/views/Orders/Orders.vue';
import Shop from '@/views/Shop/Shop.vue';
import AddEmployee from '@/views/Employee/AddEmployee';
import AddBook from '@/views/Books/AddBook';

Vue.use(VueRouter)

const routes = [
  {
    path: '/employee',
    name: 'employee',
    component: Employee,
    alias: ['/', '/home'],
    meta: {
      title: '商家后台-员工管理',
      head: '员工管理',
      asideItemIndex: '/employee',
      returnFlag: false
    }
  },
  {
    path: '/employee/add',
    name: 'addEmployee',
    component: AddEmployee,
    meta: {
      title: '商家后台-添加员工',
      head: '添加员工',
      asideItemIndex: '/employee',
      returnFlag: true
    }
  },
  {
    path: '/cate',
    name: 'cate',
    component: Cate,
    meta: {
      title: '商家后台-分类管理',
      head: '分类管理',
      asideItemIndex: '/cate',
      returnFlag: false
    }
  },
  {
    path: '/books',
    name: 'books',
    component: Books,
    meta: {
      title: '商家后台-图书管理',
      head: '图书管理',
      asideItemIndex: '/books',
      returnFlag: false
    }
  },
  {
    path: '/books/add',
    name: 'addName',
    component: AddBook,
    meta: {
      title: '商家后台-添加图书',
      head: '添加图书',
      asideItemIndex: '/books',
      returnFlag: true
    }
  },
  {
    path: '/orders',
    name: 'orders',
    component: Orders,
    meta: {
      title: '商家后台-订单管理',
      head: '订单管理',
      asideItemIndex: '/orders',
      returnFlag: false
    }
  },
  {
    path: '/shop',
    name: 'shop',
    component: Shop,
    meta: {
      title: '商家后台-店铺管理',
      head: '店铺管理',
      asideItemIndex: '/shop',
      returnFlag: false
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

import Vue from 'vue';
import VueRouter from 'vue-router';

import Login from '@/views/Login/Login';
import Logon from '@/views/Login/Logon';
import Container from '@/components/Container/Container';
import Employee from '@/views/Employee/Employee.vue';
import Cate from '@/views/Cate/Cate.vue';
import Books from '@/views/Books/Books.vue';
import Orders from '@/views/Orders/Orders.vue';
import Shop from '@/views/Shop/Shop.vue';
import AddEmployee from '@/views/Employee/AddEmployee';
import AddBook from '@/views/Books/AddBook';
import EditBaseInfo from '@/views/Shop/EditBaseInfo';
import EditAuthInfo from '@/views/Shop/EditAuthInfo';
import Agreement from '@/views/Agreement/Agreement';
import PrivacyPolicy from '@/views/Agreement/PrivacyPolicy';
import NotFound from '@/views/Common/NotFound';

Vue.use(VueRouter);

const routes = [
  {
    path: '/login',
    name: 'login',
    components: {
      index: Login
    },
    meta: {
      title: '商家登录',
      head: '登录',
      asideItemIndex: '/login',
      returnFlag: false
    }
  },
  {
    path: '/logon',
    name: 'logon',
    components: {
      index: Logon
    },
    meta: {
      title: '商家注册',
      head: '注册',
      asideItemIndex: '/logon',
      returnFlag: false
    }
  },
  {
    path: '/agreement',
    name: 'agreement',
    components: {
      index: Agreement
    },
    meta: {
      title: '用户服务协议',
      head: '用户服务协议',
      asideItemIndex: '/agreement',
      returnFlag: false,
      noVerify: true
    }
  },
  {
    path: '/privacy_policy',
    name: 'privacyPolicy',
    components: {
      index: PrivacyPolicy
    },
    meta: {
      title: '隐私政策',
      head: '隐私政策',
      asideItemIndex: '/privacy_policy',
      returnFlag: false,
      noVerify: true
    }
  },
  {
    path: '*',
    name: 'notFound',
    components: {
      index: NotFound
    },
    meta: {
      title: '404 NotFound',
      head: 'NotFound',
      asideItemIndex: '/404',
      returnFlag: false,
      noVerify: true
    }
  },
  {
    path: '/',
    name: 'home',
    components: {
      index: Container
    },
    meta: {
      title: '商家后台',
      head: '商家后台',
      asideItemIndex: '/logon',
      returnFlag: false
    },
    children: [
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
        path: '/employee/edit',
        name: 'editEmployee',
        component: AddEmployee,
        meta: {
          title: '商家后台-编辑员工',
          head: '编辑员工',
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
        name: 'addBook',
        component: AddBook,
        meta: {
          title: '商家后台-添加图书',
          head: '添加图书',
          asideItemIndex: '/books',
          returnFlag: true
        }
      },
      {
        path: '/books/edit',
        name: 'editBook',
        component: AddBook,
        meta: {
          title: '商家后台-编辑图书',
          head: '编辑图书',
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
      },
      {
        path: '/shop/edit/baseinfo',
        name: 'editBaseInfo',
        component: EditBaseInfo,
        meta: {
          title: '商家后台-编辑店铺信息',
          head: '编辑店铺信息',
          asideItemIndex: '/shop',
          returnFlag: true
        }
      },
      {
        path: '/shop/edit/authinfo',
        name: 'editAuthInfo',
        component: EditAuthInfo,
        meta: {
          title: '商家后台-编辑认证信息',
          head: '编辑认证信息',
          asideItemIndex: '/shop',
          returnFlag: true
        }
      }
    ]
  }
];

const router = new VueRouter({
  mode: 'history',
  // base: process.env.BASE_URL,
  base: process.env.NODE_ENV === 'development' ? process.env.BASE_URL : '/store/',
  routes
});

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  } else {
    document.title = '商家后台'; // 此处写默认的title
  }
  // 登录验证
  if(to.name === 'logon' || to.meta.noVerify) {
    // 如果访问注册页，直接放行
    next();
  } else if(to.name === 'login') {
    // 如果访问登录页，判断是否已登录，登录则不出来，未登录就放行
    const isLogin = window.localStorage.getItem('lsBusinessInfo');
    if(!isLogin) {
      next();
    } else if(isLogin && from.meta.title === '404 NotFound') {
      next('/');
    } else if(isLogin && JSON.stringify(from.meta) === '{}') {
      next('/');
    } else {
      document.title = from.meta.title;
    }
  } else {
    // 如果访问其它页面，判断是否登录，登录放行，未登录则跳转到登录页
    const isLogin = window.localStorage.getItem('lsBusinessInfo');
    if(isLogin) {
      next();
    } else {
      next('/login');
    }
  }
});

export default router;

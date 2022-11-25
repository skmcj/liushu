import Vue from 'vue';
import VueRouter from 'vue-router';
// 组件
import Login from '@/views/Login/Login';
import Logon from '@/views/Login/Logon';
import Main from '@/components/Main/Main';
import Home from '@/views/Home/Home';
import Cate from '@/views/Cate/Cate';
import CateDetail from '@/views/Cate/CateDetail';
import BookDetail from '@/views/Detail/BookDetail';
import ShopDetail from '@/views/Detail/ShopDetail';
import About from '@/views/About/About';
import AboutProfile from '@/views/About/AboutProfile';
import AboutProblem from '@/views/About/AboutProblem';
import AboutProblemResult from '@/views/About/AboutProblemResult';
import AboutOur from '@/views/About/AboutOur';
import AboutLink from '@/views/About/AboutLink';
import Mine from '@/views/Mine/Mine';
import MineMain from '@/views/Mine/MineMain';
import MineCenter from '@/views/Mine/MineCenter';
import MineCenterPer from '@/views/Mine/MineCenterPer';
import MineCenterAcc from '@/views/Mine/MineCenterAcc';
import MineCenterPay from '@/views/Mine/MineCenterPay';
import MineCart from '@/views/Mine/MineCart';
import MineOrder from '@/views/Mine/MineOrder';
import MineMess from '@/views/Mine/MineMess';
import MineMessSys from '@/views/Mine/MineMessSys';
import MineMessPer from '@/views/Mine/MineMessPer';
import MineAddress from '@/views/Mine/MineAddress';
import MineColl from '@/views/Mine/MineColl';
import MineComment from '@/views/Mine/MineComment';
import Settlement from '@/views/Common/Settlement';

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
      title: '流书网-登录'
    }
  },
  // 注册页
  {
    path: '/logon',
    name: 'logon',
    components: {
      index: Logon
    },
    meta: {
      title: '流书网-注册'
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
        },
        children: [
          {
            path: '/about/profile',
            name: 'aboutProfile',
            component: AboutProfile,
            alias: ['/'],
            meta: {
              title: '关于-本站简介',
              rootLink: '/about',
              aside: 'profile'
            }
          },
          {
            path: '/about/problem',
            name: 'aboutProblem',
            component: AboutProblem,
            meta: {
              title: '关于-常见问题',
              rootLink: '/about',
              aside: 'problem'
            }
          },
          {
            path: '/about/problem/q=:id',
            name: 'aboutProblemResult',
            component: AboutProblemResult,
            meta: {
              title: '关于-常见问题',
              rootLink: '/about',
              aside: 'problem'
            }
          },
          {
            path: '/about/our',
            name: 'aboutOur',
            component: AboutOur,
            meta: {
              title: '关于-联系我们',
              rootLink: '/about',
              aside: 'our'
            }
          },
          {
            path: '/about/link',
            name: 'aboutLink',
            component: AboutLink,
            meta: {
              title: '关于-友情链接',
              rootLink: '/about',
              aside: 'link'
            }
          }
        ]
      },
      {
        path: '/mine',
        name: 'mine',
        component: Mine,
        meta: {
          title: '我的',
          rootLink: '/mine'
        },
        children: [
          {
            path: '/mine',
            name: 'mineMain',
            component: MineMain,
            alias: ['/', '/index'],
            meta: {
              title: '我的',
              rootLink: '/mine',
              aside: 'main'
            }
          },
          {
            path: 'center',
            name: 'mineCenter',
            component: MineCenter,
            meta: {
              title: '我的-个人中心',
              rootLink: '/mine',
              aside: 'center'
            },
            children: [
              {
                path: 'personal',
                name: 'mineCenterPer',
                component: MineCenterPer,
                alias: ['/'],
                meta: {
                  title: '我的-个人信息',
                  rootLink: '/mine',
                  aside: 'center'
                }
              },
              {
                path: 'account',
                name: 'mineCenterAcc',
                component: MineCenterAcc,
                meta: {
                  title: '我的-账号安全',
                  rootLink: '/mine',
                  aside: 'center'
                }
              },
              {
                path: 'pay',
                name: 'mineCenterPay',
                component: MineCenterPay,
                meta: {
                  title: '我的-支付设置',
                  rootLink: '/mine',
                  aside: 'center'
                }
              }
            ]
          },
          {
            path: 'cart',
            name: 'mineCart',
            component: MineCart,
            meta: {
              title: '我的-购物车',
              rootLink: '/mine',
              aside: 'cart'
            }
          },
          {
            path: 'order',
            name: 'mineOrder',
            component: MineOrder,
            meta: {
              title: '我的-订单',
              rootLink: '/mine',
              aside: 'order'
            }
          },
          {
            path: 'mess',
            name: 'mineMess',
            component: MineMess,
            meta: {
              title: '我的-消息',
              rootLink: '/mine',
              aside: 'mess'
            },
            children: [
              {
                path: 'sys',
                name: 'mineMessSys',
                component: MineMessSys,
                alias: ['/'],
                meta: {
                  title: '我的-系统消息',
                  rootLink: '/mine',
                  aside: 'mess'
                }
              },
              {
                path: 'personal',
                name: 'mineMessPer',
                component: MineMessPer,
                meta: {
                  title: '我的-私信',
                  rootLink: '/mine',
                  aside: 'mess'
                }
              }
            ]
          },
          {
            path: '/mine/address',
            name: 'mineAddress',
            component: MineAddress,
            meta: {
              title: '我的-地址',
              rootLink: '/mine',
              aside: 'address'
            }
          },
          {
            path: 'coll',
            name: 'mineColl',
            component: MineColl,
            meta: {
              title: '我的-收藏',
              rootLink: '/mine',
              aside: 'coll'
            }
          },
          {
            path: 'comment',
            name: 'mineComment',
            component: MineComment,
            meta: {
              title: '我的-评价',
              rootLink: '/mine',
              aside: 'comment'
            }
          }
        ]
      },
      {
        path: '/settlement',
        name: 'settlement',
        component: Settlement,
        meta: {
          title: '结算页面',
          rootLink: '/settlement'
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

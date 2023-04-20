import Vue from 'vue';
import VueRouter from 'vue-router';
// 组件
// import Login from '@/views/Login/Login';
// import Logon from '@/views/Login/Logon';
// import Main from '@/components/Main/Main';
// import Home from '@/views/Home/Home';
// import Cate from '@/views/Cate/Cate';
// import CateDetail from '@/views/Cate/CateDetail';
// import BookDetail from '@/views/Detail/BookDetail';
// import ShopDetail from '@/views/Detail/ShopDetail';
// import About from '@/views/About/About';
// import AboutProfile from '@/views/About/AboutProfile';
// import AboutProblem from '@/views/About/AboutProblem';
// import AboutProblemResult from '@/views/About/AboutProblemResult';
// import AboutOur from '@/views/About/AboutOur';
// import AboutLink from '@/views/About/AboutLink';
// import Mine from '@/views/Mine/Mine';
// import MineMain from '@/views/Mine/MineMain';
// import MineCenter from '@/views/Mine/MineCenter';
// import MineCenterPer from '@/views/Mine/MineCenterPer';
// import MineCenterAcc from '@/views/Mine/MineCenterAcc';
// import MineCenterPay from '@/views/Mine/MineCenterPay';
// import MineCart from '@/views/Mine/MineCart';
// import MineOrder from '@/views/Mine/MineOrder';
// import MineMess from '@/views/Mine/MineMess';
// import MineMessSys from '@/views/Mine/MineMessSys';
// import MineMessPer from '@/views/Mine/MineMessPer';
// import MineAddress from '@/views/Mine/MineAddress';
// import MineColl from '@/views/Mine/MineColl';
// import MineComment from '@/views/Mine/MineComment';
// import Settlement from '@/views/Common/Settlement';
// import Agreement from '@/views/Common/Agreement';
// import PrivacyPolicy from '@/views/Common/PrivacyPolicy';
// import NotFound from '@/views/NotFound/NotFound';
// import Search from '@/views/Search/Search';
// import SearchBook from '@/views/Search/SearchBook';
// import SearchShop from '@/views/Search/SearchShop';

const Login = () => import(/* webpackChunkName: 'login' */ '@/views/Login/Login');
const Logon = () => import(/* webpackChunkName: 'login' */ '@/views/Login/Logon');
const Main = () => import('@/components/Main/Main');
const Home = () => import(/* webpackChunkName: 'home' */ '@/views/Home/Home');
const Cate = () => import(/* webpackChunkName: 'cate' */ '@/views/Cate/Cate');
const CateDetail = () => import(/* webpackChunkName: 'cate' */ '@/views/Cate/CateDetail');
const BookDetail = () => import(/* webpackChunkName: 'detail' */ '@/views/Detail/BookDetail');
const ShopDetail = () => import(/* webpackChunkName: 'detail' */ '@/views/Detail/ShopDetail');
const About = () => import(/* webpackChunkName: 'about' */ '@/views/About/About');
const AboutProfile = () => import(/* webpackChunkName: 'about' */ '@/views/About/AboutProfile');
const AboutProblem = () => import(/* webpackChunkName: 'about' */ '@/views/About/AboutProblem');
const AboutProblemResult = () => import(/* webpackChunkName: 'about' */ '@/views/About/AboutProblemResult');
const AboutOur = () => import(/* webpackChunkName: 'about' */ '@/views/About/AboutOur');
const AboutLink = () => import(/* webpackChunkName: 'about' */ '@/views/About/AboutLink');
const Mine = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/Mine');
const MineMain = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineMain');
const MineCenter = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineCenter');
const MineCenterPer = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineCenterPer');
const MineCenterAcc = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineCenterAcc');
const MineCenterPay = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineCenterPay');
const MineCart = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineCart');
const MineOrder = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineOrder');
const MineMess = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineMess');
const MineMessSys = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineMessSys');
const MineMessPer = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineMessPer');
const MineAddress = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineAddress');
const MineColl = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineColl');
const MineComment = () => import(/* webpackChunkName: 'mine' */ '@/views/Mine/MineComment');
const Settlement = () => import(/* webpackChunkName: 'settlement' */ '@/views/Common/Settlement');
const Agreement = () => import(/* webpackChunkName: 'agreement' */ '@/views/Common/Agreement');
const PrivacyPolicy = () => import(/* webpackChunkName: 'agreement' */ '@/views/Common/PrivacyPolicy');
const NotFound = () => import(/* webpackChunkName: 'nofound' */ '@/views/NotFound/NotFound');
const Search = () => import(/* webpackChunkName: 'search' */ '@/views/Search/Search');
const SearchBook = () => import(/* webpackChunkName: 'search' */ '@/views/Search/SearchBook');
const SearchShop = () => import(/* webpackChunkName: 'search' */ '@/views/Search/SearchShop');

Vue.use(VueRouter);

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
        path: '/agreement',
        name: 'agreement',
        component: Agreement,
        meta: {
          title: '流书网-用户服务协议'
        }
      },
      {
        path: '/privacy_policy',
        name: 'privacyPolicy',
        component: PrivacyPolicy,
        meta: {
          title: '流书网-隐私政策'
        }
      },
      {
        path: '/home',
        name: 'home',
        alias: ['/', '/index'],
        component: Home,
        meta: {
          title: '流书网-首页',
          rootLink: '/home'
        }
      },
      {
        path: '/cate',
        name: 'cate',
        component: Cate,
        meta: {
          title: '流书网-分类',
          rootLink: '/cate'
        }
      },
      {
        path: '/cate/detail',
        name: 'cateDetail',
        component: CateDetail,
        meta: {
          title: '流书网-分类-更多',
          rootLink: '/cate'
        }
      },
      {
        path: '/book',
        name: 'bookDetail',
        component: BookDetail,
        meta: {
          title: '流书网-图书详情',
          rootLink: '/book'
        }
      },
      {
        path: '/shop',
        name: 'shopDetail',
        component: ShopDetail,
        meta: {
          title: '流书网-商家详情',
          rootLink: '/shop'
        }
      },
      {
        path: '/about',
        name: 'about',
        component: About,
        meta: {
          title: '流书网-关于',
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
          title: '流书网-我的',
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
                  aside: 'center',
                  nav: 'personal'
                }
              },
              {
                path: 'account',
                name: 'mineCenterAcc',
                component: MineCenterAcc,
                meta: {
                  title: '我的-账号安全',
                  rootLink: '/mine',
                  aside: 'center',
                  nav: 'account'
                }
              },
              {
                path: 'pay',
                name: 'mineCenterPay',
                component: MineCenterPay,
                meta: {
                  title: '我的-支付设置',
                  rootLink: '/mine',
                  aside: 'center',
                  nav: 'pay'
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
                  aside: 'mess',
                  nav: 'sys'
                }
              },
              {
                path: 'personal',
                name: 'mineMessPer',
                component: MineMessPer,
                meta: {
                  title: '我的-私信',
                  rootLink: '/mine',
                  aside: 'mess',
                  nav: 'personal'
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
      },
      {
        path: '/search',
        name: 'search',
        component: Search,
        meta: {
          title: '流书网',
          rootLink: '/search'
        },
        children: [
          {
            path: 'book',
            name: 'searchBook',
            component: SearchBook,
            alias: ['/', '/book'],
            meta: {
              title: '搜索图书',
              rootLink: '/search',
              nav: 'book'
            }
          },
          {
            path: 'shop',
            name: 'searchShop',
            component: SearchShop,
            meta: {
              title: '搜索商家',
              rootLink: '/search',
              nav: 'shop'
            }
          }
        ]
      },
      {
        path: '*',
        name: 'notFound',
        component: NotFound,
        meta: {
          title: '404 NotFound',
          head: 'NotFound',
          asideItemIndex: '/404',
          returnFlag: false,
          noVerify: true
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
    document.title = '流书网'; // 此处是默认的title
  }
  // 登录验证
  if (to.meta.rootLink === '/mine' || to.name === 'settlement') {
    // 前往个人页面或结算界面
    const isLogin = JSON.parse(sessionStorage.getItem('loginFlag')) || false;
    if (isLogin) {
      next();
    } else {
      next('/login');
    }
  } else {
    next();
  }
});

export default router;

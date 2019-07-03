import VueRouter from 'vue-router'

const router=new VueRouter({
  routes: [
    //注册页面及子页面
    {
      path: '/register',
      name: 'register',
      component: () => import('./views/register.vue'),
    },
    //重置密码
    {
      path: '/reset',
      name: 'reset',
      component: () => import('./views/reset.vue'),
    },
    {
      path: '/loginIndex/:id',
      name: 'loginIndex', 
      component: () => import('./components/loginIndex.vue'),  
    },
    //首页
    {
      path: '/',
      name: 'index',
      redirect: '/home',
      component: () => import('./views/About.vue'),
      children: [{
          path: '/home',
          name: 'home',
          component: () => import('./views/Home.vue'),
        },
        {
          path: '/home/:id/order/:productId',
          name: 'order',
          component: () => import('./buy/snetOrder.vue'),
          meta: {
            requireAuth: true
          },
        },
        {
          path: '/home/pay/:id/:productId',
          name: 'pay',
          component: () => import('./buy/payOrder.vue'),
        },
        //用户页面,仍然是about的子组件
        {
          path: '/user/:id',
          name: 'userindex',
          redirect:'/user/:id/1',
          component: () => import('./user/userindex.vue'),
          meta: {
            requireAuth: true
          },
          children:[{
            path: '/user/:id/1',
            name: 'userpower',
            component: () => import('./user/userpower.vue'),
          },
          {
            path: '/user/:id/2',
            name: 'userBill',
            component: () => import('./user/userOrderBill.vue'),
          },
          {
            path: '/user/:orderId/3',
            name: 'orderHistory',
            component: () => import('./user/userOrderHistory.vue'),
          },
          {
            path: '/user/:id/4',
            name: 'userSetting',
            component: () => import('./user/userSetting.vue'),
          },
          {
            path: '/user/:id/detail',
            name: 'userDetail',
            component: () => import('./user/userCheckDetail.vue'),
          },
          {
            path: '/user/:id/outputDetail/:Outid',
            name: 'outputDetail',
            component: () => import('./user/outputDetail.vue'),
          }
        ]
        },
      ]
    },

  ]
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(res => res.meta.requireAuth)) {// 判断是否需要登录权限
    if (sessionStorage.getItem('targetId')) {// 判断是否登录
      next()
    } else {// 没登录则跳转到登录界面
      next({
        path: '/home',
        // query: {redirect: to.fullPath}
      })
    }
  } else 
    next()
  }
)
export default  router
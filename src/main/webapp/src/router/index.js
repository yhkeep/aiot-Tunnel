import Vue from 'vue'
import Router from 'vue-router'
//避免重复点击菜单跳转同一路由
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

const createRouter = () => new Router({
  routes: [
    {
      path:'/login_web',
      name:'login_web',
      component: () =>import('@/views/login/web/index'),
    },
    {
      path: '/login_mobile',
      name: 'login_mobile',
      meta: {
        title: '登录',
        showLeft: false,
        roles: ['admin', 'operator']
      },
      component: () => import('@/views/login/mobile/index')
    },
    {
      path: '*',
      name: 'fault',
      meta: {
        title: '404错误',
        roles: ['admin', 'operator']
      },
      component: () => import('@/views/404/index')
    },
   
  ],
 
})

const router = createRouter()

router.afterEach((to, from, next) => {
  if(to.name==="mobile_assets_manage"&&from.name==="mobile_assets_detail"){
    if (window.sessionStorage.getItem('mobileScroll')) {
      document.getElementById('mobile_wrap').scrollTop = window.sessionStorage.getItem('mobileScroll')
    }
  }else{
    if (to.name.split('_')[0] === 'mobile' && from.name&&from.name!=='login_mobile'){
      document.getElementById('mobile_wrap').scrollTop=0
    }
  }
  
});

//重新实例化一个新的路由表，替换之前的路由表，然后将这个方法导出
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // the relevant part
}

export default router;

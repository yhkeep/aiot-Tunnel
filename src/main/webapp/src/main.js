import Vue from 'vue'
import App from './App'
import router from './router'
import store from "./store"
import { getToken } from './utils/auth'
import { exitFn } from "./api/logout"
import { getInfo } from './api/login'
import checkBrowserTypeFn from "./utils/browserType"
import setFontSizeFn from "./utils/px2Rem"

import "iview/dist/styles/iview.css"
import 'vant/lib/icon/local.css';

import { LoadingBar, Message, Modal } from 'iview';
import moment from 'moment'
Vue.prototype.$Moment = moment;
Vue.prototype.$Loading = LoadingBar;
Vue.prototype.$Message = Message;
Vue.prototype.$Modal = Modal;

import { Dialog } from 'vant';
import 'vant/lib/dialog/style';

import axios from 'axios'


Vue.config.productionTip = true;
//在开发环境下开启devtools工具
if (process.env.NODE_ENV == 'development') {
  Vue.config.devtools = true;
} else {
  Vue.config.devtools = false;
}

//拿到浏览器类型
var browserType = checkBrowserTypeFn();

//如果是在手机端；执行rem适配的方法
if (browserType === 'mobile') {
  setFontSizeFn();
}

function dialogFn(msg){
  switch (browserType) {
    case 'mobile':

      Dialog.alert({
        title: '提示！',
        message: '<span style="color:red;">' + msg + '</span>',
        confirmButtonText: '重新登录',
      }).then(() => {
        exitFn();
      });

      break;
    default: 
      Modal.warning({
        title: '提示！',
        content: msg,
        okText: '重新登录',
        onOk: () => {
          exitFn();
        }
      })

  }
}
function messageFn(msg){
  switch (browserType) {
    case 'mobile':

      Dialog.alert({
        title: '提示！',
        message: '<span style="color:red;">' + msg + '</span>',
        confirmButtonText: '确定',
      }).then(() => {

      });

      break;
    default: 
      Message.error({
        content: msg,
        duration: 1,
      })

  }
}


// 创建axios实例
const service = axios.create({
  timeout: 1000 * 30,
  // 允许跨域带token
  withCredentials: true,
  headers: {
    // 'Content-Type': 'application/json; charset=utf-8'
  }
})

// request拦截器
service.interceptors.request.use(
  (config) => {
    config.headers['token'] = getToken() ? getToken() : ''
    return config
  },
  (error) => {
    return Promise.reject(error)
  })

// response拦截器
service.interceptors.response.use(response => {
  if (response.headers.message === 'Forced offline') {
    dialogFn('该账号在其它地方登陆，您被强制下线！')
  } else if (response.headers.message === 'authorized failed') {
    messageFn('您无此权限！')
  } else {
    return response
  }
}, error => {
  if (error.response.status === 401) {
    dialogFn('您的登录信息已失效，需要重新登录！')
  } else if (error.response.headers.message === 'user not exist') {
    dialogFn('当前用户不存在！')
  } else if (error.response.headers.message === 'user is not authorized') {
    dialogFn('用户未认证！')
  } else if (error.response.headers.message === 'authorized failed') {
    dialogFn('其它异常认证！')
  }else{
    return Promise.reject(error)
  }
})
Vue.prototype.$axios = service

router.beforeEach((to, from, next) => {
  if (getToken()) {
    if (to.path === '/login_web' || to.path === '/login_mobile') {
      next(
        {
          path: from.path
        }
      )

      document.title = from.meta.title

    } else {

      //如果vuex里面还存在当前用户的权限信息；那么直接去到要去的页面
      if (store.state.role) {
        next();
        document.title = to.meta.title
      } else {
        //否则执行getInfo方法，传入cookie值去向服务器获取cookie对应的用户名称和信息
        getInfo(getToken())

        //动态的添加路由组件，以控制权限
        router.addRoutes(store.state.routes)
        next({
          path: to.fullPath,
          replace: true,
        });

      }

    }

  } else {
    if (to.path == '/login_web' || to.path === '/login_mobile') {
      next();
    } else {
      
      if (browserType==='mobile'){
        next('/login_mobile');
      }else{
        next('/login_web');
      }
      
    }
    document.title = '登录'
  }

})

new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})

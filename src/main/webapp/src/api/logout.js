import router from '../router'
import { resetRouter } from "../router"
import { removeToken } from '../utils/auth'
import checkBrowserTypeFn from "../utils/browserType"

export function exitFn() {
    removeToken();
    // window.sessionStorage.removeItem('token')
    // window.sessionStorage.removeItem('username')
    // window.sessionStorage.removeItem('role')
    // window.sessionStorage.removeItem('project')
    // window.sessionStorage.removeItem('address')
    // window.sessionStorage.removeItem('queryData')
    // window.sessionStorage.removeItem('mobileScroll')
    // window.sessionStorage.removeItem('locDept')
    // window.sessionStorage.removeItem('chooseData')
    // window.sessionStorage.removeItem('departmentroom')
    // window.sessionStorage.removeItem('detailData')
    // window.sessionStorage.removeItem('configChooseData')
    // window.sessionStorage.removeItem('beforeRoute')
    // window.sessionStorage.removeItem('openRouteArr')
    // window.sessionStorage.removeItem('authority')
    window.sessionStorage.clear()
    // 重置路由/避免不同账号权限不同路由重复
    resetRouter();
    router.push({
        path: checkBrowserTypeFn()==='mobile' ? '/login_mobile' : '/login_web'
    })
}
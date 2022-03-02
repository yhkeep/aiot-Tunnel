import store from "../store"
import { getToken } from '../utils/auth'

export function getInfo(token) {

    // if (getToken() && window.sessionStorage.getItem('username') && window.sessionStorage.getItem('role') && window.sessionStorage.getItem('project') && window.sessionStorage.getItem('address') && window.sessionStorage.getItem('queryData') && window.sessionStorage.getItem('chooseData')) {
    //     store.commit('setUser', window.sessionStorage.getItem('username'));
    //     store.commit('setRole', window.sessionStorage.getItem('role'));
    //     store.commit('setDepartmentroom', window.sessionStorage.getItem('departmentroom'));
    //     store.commit('setLocDept', window.sessionStorage.getItem('locDept'));
    //     store.commit('setAddress', window.sessionStorage.getItem('address'));
    //     store.commit('setRoutes', window.sessionStorage.getItem('role'), window.sessionStorage.getItem('adress'))
    //     store.commit('setProject', window.sessionStorage.getItem('project'))
    //     store.commit('setQueryData', JSON.parse(window.sessionStorage.getItem('queryData')));
    //     store.commit('setChooseData', JSON.parse(window.sessionStorage.getItem('chooseData')));
    // }

    if (getToken() && window.sessionStorage.getItem('username') && window.sessionStorage.getItem('role') && window.sessionStorage.getItem('authority') && window.sessionStorage.getItem('project') && window.sessionStorage.getItem('address') && window.sessionStorage.getItem('queryData') && window.sessionStorage.getItem('chooseData')) {
        store.commit('setUser', window.sessionStorage.getItem('username'));
        store.commit('setRole', window.sessionStorage.getItem('role'));
        store.commit('setDepartmentroom', window.sessionStorage.getItem('departmentroom'));
        // store.commit('setLocDept', window.sessionStorage.getItem('locDept'));
        store.commit('setAddress', window.sessionStorage.getItem('address'));
        store.commit('setAuthority', JSON.parse(window.sessionStorage.getItem('authority')))
        store.commit('setRoutes', window.sessionStorage.getItem('role'))
        
        store.commit('setProject', window.sessionStorage.getItem('project'))
        store.commit('setQueryData', JSON.parse(window.sessionStorage.getItem('queryData')));
        store.commit('setChooseData', JSON.parse(window.sessionStorage.getItem('chooseData')));
    }

}
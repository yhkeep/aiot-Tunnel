import Vue from "vue";
import Vuex from "vuex"

Vue.use(Vuex)

const state = {
    routes: [],
    user: '',
    role: '',
    authority:[],
    departmentroom:'',
    // locDept:'',
    queryData:{},
    chooseData:{},
    project: '',
    address: '',
    isCollapsed: false,
    showFooter:true,
    mobileScroll:0,
}

const mutations = {

    setRoutes(state, role) {
        var routes1 =
            [
                {
                    path:'/',
                    redirect:'/web'
                },
                {
                    path: '/web',
                    redirect:'/web/home',
                    name: 'layout',
                    component: () => import('@/layout/web/index'),
                    children: [
                        {
                            path: '/web/home',
                            name: 'home',
                            meta: {
                                title: '资产首页',
                                icon: 'md-home',
                                showLeft: true,
                                roles: ['admin', 'operator'],
                                authority:1
                                
                            },
                            component: () => import('@/views/web/home/index')
                        },

                        {
                            path: '/web/assets_manage',
                            name: 'assets_manage',
                            meta: {
                                title: '资产管理',
                                icon: 'md-cube',
                                showLeft: true,
                                roles: ['admin', 'operator'],
                                authority: 2,
                                keepAlive: true, 
                                isBack: false,
                               
                            },
                            component: () => import('@/views/web/assets_manage/index'),

                        },

                        {
                            path: '/web/assets_create',
                            name: 'assets_create',
                            meta: {
                                title: '新增资产',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 2,
                                parent:{
                                    path: '/web/assets_manage',
                                    name: '资产管理'
                                }
                                
                            },
                            component: () => import('@/views/web/assets_create/index'),
                        },

                        {
                            path: '/web/assets_detail/:id',
                            name: 'assets_detail',
                            meta: {
                                title: '资产明细',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 2,
                                needFresh:false,
                                parent: {
                                    path: '/web/assets_manage',
                                    name: '资产管理'
                                }
                            },
                            component: () => import('@/views/web/assets_detail/index'),
                        },

                        

                        {
                            path: '/web/device_manage',
                            name: 'device_manage',
                            meta: {
                                title: '设备管理',
                                icon: 'md-desktop',
                                showLeft: true,
                                roles: ['admin', 'operator'],
                                authority: 3,
                            },
                            component: () => import('@/views/web/device_manage/index')
                        },
                        {
                            path: '/web/track_detail/:id',
                            name: 'track_detail',
                            meta: {
                                title: '轨迹明细',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 3,
                                needFresh: false,
                                parent: {
                                    path: '/web/assets_manage',
                                    name: '资产管理'
                                }
                            },
                            component: () => import('@/views/web/track_detail/index'),
                        },


                        {
                            path: '/web/temp_manage',
                            name: 'temp_manage',
                            meta: {
                                title: '温湿度管理',
                                icon: 'md-thermometer',
                                showLeft: true,
                                roles: ['admin', 'operator'],
                                authority: 4,
                                keepAlive: true,
                                isBack: false,
                            },
                            component: () => import('@/views/web/temp_manage/index')
                        },



                        {
                            path: '/web/temp_create',
                            name: 'temp_create',
                            meta: {
                                title: '温湿度仪器新增',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 4,
                                keepAlive: false,
                                parent: {
                                    path: '/web/temp_manage',
                                    name: '温湿度管理'
                                }
                            },
                            component: () => import('@/views/web/temp_create/index')
                        },

                        {
                            path: '/web/temp_detail/:mac',
                            name: 'temp_detail',
                            meta: {
                                title: '温湿度详情',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 4,
                                keepAlive: false,
                                parent: {
                                    path: '/web/temp_manage',
                                    name: '温湿度管理'
                                }
                            },
                            component: () => import('@/views/web/temp_detail/index')
                        },

                       
                        {
                            path: '/web/temp_edit/:mac',
                            name: 'temp_edit',
                            meta: {
                                title: '温湿度编辑',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 4,
                                parent: {
                                    path: '/web/temp_manage',
                                    name: '温湿度管理'
                                }
                            },
                            component: () => import('@/views/web/temp_edit/index')
                        },
                        {
                            path: '/web/rail',
                            name: 'rail',
                            meta: {
                                title: '电子围栏',
                                icon: 'md-radio-button-on',
                                showLeft: true,
                                roles: ['admin', 'operator'],
                                authority: 5,
                            },
                            component: () => import('@/views/web/rail/index')
                        },
                        {
                            path: '/web/damage_manage',
                            name: 'damage_manage',
                            meta: {
                                title: '维修/报废/调用',
                                icon: 'ios-build',
                                showLeft: true,
                                roles: ['admin', 'operator'],
                                authority: 6,
                            },
                            component: () => import('@/views/web/damage_manage/index')
                        },
                        {
                            path: '/web/damage_history/:id',
                            name: 'damage_history',
                            meta: {
                                title: '维修/报废/调用历史',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 6,
                            },
                            component: () => import('@/views/web/damage_history/index')
                        },
                       
                        {
                            path: '/web/check_manage',
                            name: 'check_manage',
                            meta: {
                                title: '盘点管理',
                                icon: 'md-contrast',
                                showLeft: true,
                                roles: ['admin','operator'],
                                authority: 7,
                                
                            },
                            component: () => import('@/views/web/check_manage/index')
                        },

                        {
                            path: '/web/check_history',
                            name: 'check_history',
                            meta: {
                                title: '盘点历史',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 7,
                                parent: {
                                    path: '/web/check_manage',
                                    name: '盘点管理'
                                }
                            },
                            component: () => import('@/views/web/check_history/index')
                        },
                        {
                            path: '/web/check_history_detail/:id',
                            name: 'check_history_detail',
                            meta: {
                                title: '盘点历史明细',
                                showLeft: false,
                                roles: ['admin', 'operator'],
                                authority: 7,
                                parent: {
                                    path: '/web/check_history',
                                    name: '盘点历史'
                                }
                            },
                            component: () => import('@/views/web/check_history_detail/index')
                        },

                        {
                            path: '/web/gateway_manage',
                            name: 'gateway_manage',
                            meta: {
                                title: '网关管理',
                                icon: 'md-compass',
                                showLeft: true,
                                roles: ['admin',],
                                authority: 8,
                            },
                            component: () => import('@/views/web/gateway_manage/index')
                        },
                        
                        {
                            path: '/web/gateway_create',
                            name: 'gateway_create',
                            meta: {
                                title: '网关新增',
                                showLeft: false,
                                roles: ['admin',],
                                authority: 8,
                                parent: {
                                    path: '/web/gateway_manage',
                                    name: '网关管理'
                                }
                            },
                            component: () => import('@/views/web/gateway_create/index')
                        },

                        {
                            path: '/web/gateway_detail',
                            name: 'gateway_detail',
                            meta: {
                                title: '网关明细',
                                showLeft: false,
                                roles: ['admin',],
                                authority: 8,
                                parent: {
                                    path: '/web/gateway_manage',
                                    name: '网关管理'
                                }
                            },
                            component: () => import('@/views/web/gateway_detail/index')
                        },

                        {
                            path: '/web/gateway_setting',
                            name: 'gateway_setting',
                            meta: {
                                title: '网关配置',
                                showLeft: false,
                                roles: ['admin',],
                                authority: 8,
                                parent: {
                                    path: '/web/gateway_manage',
                                    name: '网关管理'
                                }
                            },
                            component: () => import('@/views/web/gateway_setting/index')
                        },

                        {
                            path: '/web/account_manage',
                            name: 'account_manage',
                            meta: {
                                title: '账户管理',
                                icon: 'md-contact',
                                showLeft: true,
                                roles: ['admin',],
                                authority: 9,
                            },
                            component: () => import('@/views/web/account_manage/index')
                        },
                        {
                            path: '/web/account_create',
                            name: 'account_create',
                            meta: {
                                title: '账户新增',
                                showLeft: false,
                                roles: ['admin',],
                                authority: 9,
                                parent: {
                                    path: '/web/account_manage',
                                    name: '账户管理'
                                }
                            },
                            component: () => import('@/views/web/account_create/index')
                        },
                        // {
                        //     path: '/web/departmentroom_manage',
                        //     name: 'departmentroom_manage',
                        //     meta: {
                        //         title: '部门配置',
                        //         showLeft: false,
                        //         roles: ['admin',],
                        //         authority: 9,
                        //         parent: {
                        //             path: '/web/account_manage',
                        //             name: '账户管理'
                        //         }
                        //     },
                        //     component: () => import('@/views/web/departmentroom_manage/index')
                        // },
                        {
                            path: '/web/account_edit/:id',
                            name: 'account_edit',
                            meta: {
                                title: '账户编辑',
                                showLeft: false,
                                roles: ['admin',],
                                authority: 9,
                                parent: {
                                    path: '/web/account_manage',
                                    name: '账户管理'
                                }
                            },
                            component: () => import('@/views/web/account_edit/index')
                        },

                        {
                            path: '/web/password_manage',
                            name: 'password_manage',
                            meta: {
                                title: '密码管理',
                                showLeft: false,
                                roles: ['admin', 'operator']
                            },
                            component: () => import('@/views/web/password_manage/index')
                        },

                    ]
                },

                {
                    path: '/mobile',
                    redirect:'/mobile/pane',
                    name: 'mobile_layout',
                    component: () => import('@/layout/mobile/index'),
                    children:[
                        {
                            path: '/mobile/pane',
                            redirect: '/mobile/home',
                            name: 'mobile_pane_wrap',
                            keepAlive: true, 
                            component: () => import('@/layout/mobile/pane_wrap/index'),
                            children: [
                                {
                                    path: '/mobile/home',
                                    name: 'mobile_home',
                                    meta: {
                                        title: '资产首页',
                                        icon:'home-o',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 1,
                                        keepAlive: true, 
                                    },
                                    component: () => import('@/views/mobile/home/index')
                                },
                                {
                                    path: '/mobile/assets_manage',
                                    name: 'mobile_assets_manage',
                                    meta: {
                                        title: '资产管理',
                                        icon: 'apps-o',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 2,
                                        keepAlive: true, 
                                        isBack:false,
                                    },
                                    component: () => import('@/views/mobile/assets_manage/index')
                                },
                                {
                                    path: '/mobile/check_manage',
                                    name: 'mobile_check_manage',
                                    meta: {
                                        title: '资产盘点',
                                        icon: 'eye-o',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 7,
                                        keepAlive: true, 
                                    },
                                    component: () => import('@/views/mobile/check_manage/index')
                                },

                                {
                                    path: '/mobile/device_message',
                                    name: 'mobile_device_message',
                                    meta: {
                                        title: '设备动态',
                                        icon: 'location-o',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 5,
                                        keepAlive: true,
                                    },
                                    component: () => import('@/views/mobile/device_message/index')
                                },
                                {
                                    path: '/mobile/damage_manage',
                                    name: 'mobile_damage_manage',
                                    meta: {
                                        title: '维修/报废/调用',
                                        icon: 'records',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 6,
                                        keepAlive: true,
                                    },
                                    component: () => import('@/views/mobile/damage_manage/index')
                                },
                                {
                                    path: '/mobile/temp_manage',
                                    name: 'mobile_temp_manage',
                                    meta: {
                                        title: '温湿度管理',
                                        icon: 'bar-chart-o',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 4,
                                        keepAlive: true,
                                    },
                                    component: () => import('@/views/mobile/temp_manage/index')
                                },
                            ]
                        },

                        {
                            path: '/mobile/content',
                            redirect: '/mobile/assets_detail',
                            name: 'mobile_content_wrap',
                            // keepAlive: true, 
                            component: () => import('@/layout/mobile/content_wrap/index'),
                            children: [
                                {
                                    path: '/mobile/assets_detail/:id?',
                                    name: 'mobile_assets_detail',
                                    meta: {
                                        title: '资产明细',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 2,
                                        needFresh: false,

                                    },
                                    component: () => import('@/views/mobile/assets_detail/index')
                                },
                                {
                                    path: '/mobile/assets_modify/:id',
                                    name: 'mobile_assets_modify',
                                    meta: {
                                        title: '资产修改',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 2,
                                    },
                                    component: () => import('@/views/mobile/assets_modify/index')
                                },
                                {
                                    path: '/mobile/assets_create',
                                    name: 'mobile_assets_create',
                                    meta: {
                                        title: '资产新增',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 2,
                                    },
                                    component: () => import('@/views/mobile/assets_create/index')
                                },
                                {
                                    path: '/mobile/check_history',
                                    name: 'mobile_check_history',
                                    meta: {
                                        title: '盘点历史',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 7,
                                    },
                                    component: () => import('@/views/mobile/check_history/index')
                                },
                                {
                                    path: '/mobile/check_history_detail/:id',
                                    name: 'mobile_check_history_detail',
                                    meta: {
                                        title: '盘点历史明细',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 7,
                                    },
                                    component: () => import('@/views/mobile/check_history_detail/index')
                                },
                                {
                                    path: '/mobile/damage_history/:id',
                                    name: 'mobile_damage_history',
                                    meta: {
                                        title: '维修/报废/调用历史',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 6,
                                    },
                                    component: () => import('@/views/mobile/damage_history/index')
                                },
                                {
                                    path: '/mobile/temp_create',
                                    name: 'mobile_temp_create',
                                    meta: {
                                        title: '温湿度仪器新增',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 4,
                                    },
                                    component: () => import('@/views/mobile/temp_create/index')
                                },
                                {
                                    path: '/mobile/temp_detail/:mac',
                                    name: 'mobile_temp_detail',
                                    meta: {
                                        title: '温湿度明细',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 4,
                                    },
                                    component: () => import('@/views/mobile/temp_detail/index')
                                },
                                {
                                    path: '/mobile/temp_edit/:id',
                                    name: 'mobile_temp_edit',
                                    meta: {
                                        title: '温湿度仪器编辑',
                                        showLeft: false,
                                        roles: ['admin', 'operator'],
                                        authority: 4,
                                    },
                                    component: () => import('@/views/mobile/temp_edit/index')
                                },
                            ]
                        },
                      
                    ]
                },

            ]


        // let needRoutes = null;
        // switch (state.address) {
        //     case '1':
        //         needRoutes = routes1;
        //         break;
        //     default:
        //         return 
        // }

        // needRoutes[1].children = needRoutes[1].children.filter((item, index) => {
        //     return item.meta.roles.indexOf(role) > -1
        // })

        // state.routes = needRoutes;


        let needRoutes = null;
        switch (state.address) {
            case '1':
                needRoutes = routes1;
                break;
            default:
                return 
        }


        needRoutes[1].children = needRoutes[1].children.filter((item, index) => {
            return !item.meta.authority||state.authority.indexOf(item.meta.authority)>-1
        })

        needRoutes[2].children[0].children = needRoutes[2].children[0].children.filter((item, index) => {
            return !item.meta.authority||state.authority.indexOf(item.meta.authority)>-1
        })
        needRoutes[2].children[1].children = needRoutes[2].children[1].children.filter((item, index) => {
            return !item.meta.authority||state.authority.indexOf(item.meta.authority)>-1
        })

        state.routes = needRoutes;
    },


    setUser(state, newUser) {
        state.user = newUser;
        window.sessionStorage.setItem('username', newUser)
    },

    setRole(state, newRole) {
        state.role = newRole;
        window.sessionStorage.setItem('role', newRole)
    },
    
    setAuthority(state, newAuthority) {
        state.authority = newAuthority;
        window.sessionStorage.setItem('authority', JSON.stringify(newAuthority))
    },

    setQueryData(state, newQueryData) {
        state.queryData = newQueryData;
        window.sessionStorage.setItem('queryData', JSON.stringify(newQueryData))
    },

    setChooseData(state, newChooseData) {
        state.chooseData = newChooseData;
        window.sessionStorage.setItem('chooseData', JSON.stringify(newChooseData))
    },

    setProject(state, newProject) {
        state.project = newProject;
        window.sessionStorage.setItem('project', newProject)
    },
    setDepartmentroom(state, newDepartmentroom) {
        state.departmentroom = newDepartmentroom;
        window.sessionStorage.setItem('departmentroom', newDepartmentroom)
    },
    setLocDept(state, newLocDept) {
        state.locDept = newLocDept;
        window.sessionStorage.setItem('locDept', newLocDept)
    },

    setAddress(state, newAddress) {
        state.address = newAddress;
        window.sessionStorage.setItem('address', newAddress)
    },


    setCollapse(state, newCollapse) {
        state.isCollapsed = newCollapse;
    },

    setFooter(state, newFooter) {
        state.showFooter = newFooter;
    },

    setMobileScroll(state, newMobileScroll) {
        state.mobileScroll = newMobileScroll;
    },
  
}

const store = new Vuex.Store({
    state,
    mutations,
})



export default store
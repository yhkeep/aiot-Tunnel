 <template>
  <div class='layout_wrap'>
        <Layout style="height:100%">
            <Sider ref="side" hide-trigger collapsible :collapsed-width="78" v-model="$store.state.isCollapsed">
                <div class="side_menu_wrap">
                    <div class="logo_wrap">
                        <!-- <a href="javascript:;">
                            <img src="../../assets/img/logo.png" alt="">
                        </a> -->
                        
                    </div>
                    <Menu ref="menu" :active-name="defaultName"  theme="light" width="auto" :class="menuitemClasses">
                        <template v-for="item in routesData[1].children" v-if="item.meta.showLeft">
                            <Tooltip placement="right" :content="item.meta.title" :disabled="!$store.state.isCollapsed" transfer>
                                <MenuItem  :name='item.path' :to='item.path'>
                                    <Icon :type='item.meta.icon' />
                                    <span>{{item.meta.title}}</span>
                                </MenuItem>
                            </Tooltip>
                        </template>
                    </Menu>
                </div>
                
            </Sider>
            <Layout>
                <Header :style="{padding: 0,zIndex:2}" class="layout-header-bar">
                    <Icon @click.native="collapsedSider" :class="rotateIcon" :style="{margin: '0 10px 0 18px'}" type="md-menu" size="24"></Icon>
                    <div class="bread_crumb_wrap">
                        <Breadcrumb>
                            <BreadcrumbItem v-for="(item,index) in breadCrumbList" :to="item.path" :key="index">{{item.name}}</BreadcrumbItem>
                        </Breadcrumb>
                    </div>
                    <div class="user_wrap">
                        <!-- <div class="device_notice_wrap" @click="showDeviceModal=true;">
                            <Badge class="notice_icon" :count="deviceNoticeArr.length"  :offset='[5,0]'>
                                <Icon type="ios-notifications-outline" size="26" ></Icon>
                            </Badge>
                        </div> -->
                        <!-- <Tag color="geekblue">{{departmentroom? departmentroom : '--'}}</Tag> -->
                        <Dropdown style="margin-right: 20px" trigger="hover">
                            <Button size="small" type="primary">
                                授权部门
                                <Icon type="ios-arrow-down"></Icon>
                            </Button>
                            <div slot="list">
                                <div style="width:250px;padding:20px;line-height:25px;">
                                    <Tag v-for="(item,index) in departmentroom.split(',')" :key="index" color="default">{{item}}</Tag>
                                </div>
                            </div>
                        </Dropdown>
                        <Tag  :color="role==='admin' ? 'success' : 'warning'">{{role==='admin'? '管理员' :'操作员'}}</Tag>
                       
                       
                       <div class="user_box">
                            <Dropdown placement="bottom-end" @on-click="avatarDropdownFn" transfer>
                                <Badge >
                                    <Avatar :src="userAvator" class="avatar"/>
                                </Badge>
                                <Icon :size="18" type="md-arrow-dropdown"></Icon>
                                <DropdownMenu slot="list" >
                                    <div class="user_info">
                                        <!-- <h3>{{user}}</h3> -->
                                        <p>
                                            <Icon style="font-size:16px;vertical-align:middle" type="md-person" />
                                            <span style="vertical-align:middle;margin-left:5px;">{{user}}</span>
                                        </p>
                                    </div>
                                    <DropdownItem name="passwordManage">
                                        
                                        <p>
                                            <Icon style="font-size:16px;vertical-align:middle" type="md-key" />
                                            <span style="vertical-align:middle;margin-left:5px;">密码管理</span>
                                        </p>
                                    </DropdownItem>
                                    <DropdownItem name="logout">
                                        <p>
                                            <Icon style="font-size:16px;vertical-align:middle;font-weight:600" type="md-log-out" />
                                            <span style="vertical-align:middle;margin-left:5px;">退出登录</span>
                                        </p>
                                    </DropdownItem>
                                </DropdownMenu>
                            </Dropdown>
                       </div>
                    </div>

                </Header>

                <div class="route_nav_wrap">
                    <div class="route_scroll_arrow_left">
                        <Button type="default"  icon="ios-arrow-back" @click="routeScrollLeftFn"></Button>
                    </div>
                    <div class="route_scroll_wrap" ref="routeScrollWrap">
                        <div class="route_scroll_box" ref="routeScrollBox">
                            <Tag :color="item.name===currentRouteName? 'primary':'default'" type="dot" checkable   v-for="(item,index) in openRouteArr" :key="index" :closable="index!==0 ?true :false" @on-change="clickRouteTagFn(item)" @on-close="closeRouteTagFn($event,item.name)">{{item.title}}</Tag>
                        </div>
                    </div>
                    <div class="route_scroll_arrow_right" >
                        <Button type="default"  icon="ios-arrow-forward" @click="routeScrollRightFn"></Button>
                    </div>
                </div>
              
                <Content id="content_wrap" ref="contentWrap">
                        <keep-alive>
                            <router-view v-if="$route.meta.keepAlive"></router-view>
                        </keep-alive>
                        <router-view v-if="!$route.meta.keepAlive"></router-view>
                    
                        <div id="return_top_wrap">
                            <div class="return_top_box">
                                <Icon type="ios-arrow-up" />
                            </div>
                        </div>
                </Content>
            </Layout>
        </Layout>

        <Modal
            title="设备动态"
            width='600'
            v-model="showDeviceModal"
            :mask-closable="false"
            :closable="true">

            <div style="text-align:center;margin:50px;" v-if="!deviceNoticeArr.length">
                    暂无动态...
            </div>
            <div v-else  class="device_modal_wrap">
                
                <div class="device_message_item" v-for="(item,index) in deviceNoticeArr" :key="index">
                    <div class="device_message_item_left">
                        <div :class="item.locDept!==item.department&&item.department?'device_message_icon_box device_message_bg_color_2' :'device_message_icon_box device_message_bg_color_1'">
                            <Icon type="ios-warning" />
                        </div>
                    </div>
                    <div class="device_message_item_right">
                        <div class="device_message_item_right_top">
                            <span>{{index+1}}</span>. <b :class="item.locDept!==item.department&&item.department?'device_message_color_2' :'device_message_color_1'">{{item.assetName==='null' ? '' : item.assetName}}</b> - <span>{{item.assetID==='null' ? '' : item.assetID}}</span><span :class="item.locDept!==item.department&&item.department? 'beyond' : 'lose'">{{item.locDept!==item.department&&item.department?'超出电子围栏范围':'丢失信号'}}</span>
                        </div>
                        <div class="device_message_item_right_bottom">
                            <p>品牌：<b>{{item.brand==='null' ? '' : item.brand}}</b> & 型号：<b>{{item.type==='null' ? '' : item.type}}</b></p>
                            <p>最近更新时间：<b>{{item.updatetime==='null' ? '' : item.updatetime}}</b> & 房间名：<b>{{item.cadMapRoomName==='null' ? '' : item.cadMapRoomName}}</b></p>
                            <p>所属科室：<b>{{item.locDept==='null' ? '' : item.locDept}}</b> & 实时所在科室：<b>{{item.department==='null' ? '' : item.department}}</b></p>
                        </div>
                    </div>
                </div>
                
            </div>

            <div slot="footer">
                <div class="custom_flex_wrap">
                    <div class="device_message_num_item">
                        <span>{{deviceMessageNum.lose}}件</span>
                    </div>
                    <div class="device_message_num_item">
                        <span>{{deviceMessageNum.beyond}}件</span>
                    </div>
                </div>
            </div>

        </Modal>
  </div>
</template>

<script>
import Vue from "vue"
import { Layout, Header , Sider , Content ,Menu ,MenuItem ,Dropdown ,DropdownMenu, DropdownItem, Badge ,Avatar,Tag,Icon,Tooltip,Breadcrumb,BreadcrumbItem,Modal,Button} from 'iview';
Vue.component('Layout', Layout);
Vue.component('Header', Header);
Vue.component('Sider', Sider);
Vue.component('Content', Content);
Vue.component('Menu', Menu);
Vue.component('MenuItem', MenuItem);
Vue.component('Dropdown', Dropdown);
Vue.component('DropdownMenu', DropdownMenu);
Vue.component('DropdownItem', DropdownItem);
Vue.component('Badge', Badge);
Vue.component('Avatar', Avatar);
Vue.component('Tag', Tag);
Vue.component('Icon', Icon);
Vue.component('Tooltip', Tooltip);
Vue.component('Breadcrumb', Breadcrumb);
Vue.component('BreadcrumbItem', BreadcrumbItem);
Vue.component('Modal', Modal);
Vue.component('Button', Button);

//引入退出登录返回到登录页面方法
import { exitFn } from "../../api/logout"

import NET_PORT from "../../api/port.js"
import { getToken } from '../../utils/auth.js';

export default {
  name:'layout',
  components:{},
  props:{},
  data(){
    return {
        showDeviceModal:false,
        userAvator:require('../../assets/img/users.jpg'),
        user:this.$store.state.user,
        role:this.$store.state.role,
        departmentroom:this.$store.state.departmentroom,
        routesData:this.$store.state.routes,
        defaultName:'/web/home',
        currentRouteName:'home',
        //创建socket
        websock: null,
        deviceNoticeArr:[],
        deviceMessageNum:{
            lose:0,
            beyond:0,
        },
        breadCrumbList:[],
        beforeRoute:{},
        openRouteArr:[
            // {
            //     name:'home',
            //     path:'/web/home',
            //     title:'资产首页'
            // },
        ],
    }
  },
  watch:{
        '$route'(after,before){
            this.beforeRoute=before;
            let obj={
                name:before.name,
                title:before.meta.title,
                path:before.path,
            }
            window.sessionStorage.setItem('beforeRoute',JSON.stringify(obj))
            this.defaultName=this.$route.path;
            this.currentRouteName=this.$route.name
            this.setBreadCrumbFn();
            this.checkRouteFn(this.$route)
        },
        // "$store.state.queryData.department"(){
        //     this.department=this.$store.state.queryData.department;
        // }
       
  },
  computed:{
            rotateIcon () {
                return [
                    'menu-icon',
                    'collapse_icon',
                    this.$store.state.isCollapsed ? 'rotate-icon' : ''
                ];
            },
            menuitemClasses () {
                return [
                    'menu-item',
                    'menu_scroll',
                    this.$store.state.isCollapsed ? 'collapsed-menu' : ''
                ]
            }
  },
  methods:{
        checkRouteFn(currentRoute){
            let isHave=this.openRouteArr.find((item,index)=>{
                return item.name===currentRoute.name;
            })
            if(!isHave){
                this.openRouteArr.push({
                    name:currentRoute.name,
                    title:currentRoute.meta.title,
                    path:currentRoute.path,
                })

                window.sessionStorage.setItem('openRouteArr',JSON.stringify(this.openRouteArr))

               
                this.$nextTick(()=>{
                    let wrapWidth=this.$refs.routeScrollWrap.offsetWidth
                    let boxWidth=this.$refs.routeScrollBox.offsetWidth
                    let currentLeft=this.$refs.routeScrollBox.offsetLeft;
                    if(wrapWidth<boxWidth){
                        this.$refs.routeScrollBox.style.left=(wrapWidth-boxWidth)+"px";
                    }
                })
            }
        },
        closeRouteTagFn(event,name){
            this.openRouteArr.forEach((item,index)=>{
                if(item.name===name){
                    this.openRouteArr.splice(index,1)
                }
            })

            window.sessionStorage.setItem('openRouteArr',JSON.stringify(this.openRouteArr))

            if(this.currentRouteName===name){
                this.$router.push({
                    path:this.openRouteArr[this.openRouteArr.length-1].path
                })
            }

            let wrapWidth=this.$refs.routeScrollWrap.offsetWidth
            let boxWidth=this.$refs.routeScrollBox.offsetWidth
            let currentLeft=this.$refs.routeScrollBox.offsetLeft;
            if(wrapWidth>boxWidth){
                this.$refs.routeScrollBox.style.left=0+"px";
            }
        },

        clickRouteTagFn(item){
            if(this.currentRouteName!==item.name){
                this.$router.push({
                    path:item.path
                })
            }
        },
        routeScrollLeftFn(){
            let wrapWidth=this.$refs.routeScrollWrap.offsetWidth
            let boxWidth=this.$refs.routeScrollBox.offsetWidth
            let currentLeft=this.$refs.routeScrollBox.offsetLeft;
            
            if(currentLeft>=-200){
                this.$refs.routeScrollBox.style.left=0+"px";
            }else{
                this.$refs.routeScrollBox.style.left=(currentLeft+200)+"px";
            }
           
        },
        routeScrollRightFn(){
            
            let wrapWidth=this.$refs.routeScrollWrap.offsetWidth
            let boxWidth=this.$refs.routeScrollBox.offsetWidth
            let currentLeft=this.$refs.routeScrollBox.offsetLeft;
            if(wrapWidth<boxWidth){
                if((boxWidth+currentLeft)-wrapWidth<=200){
                    this.$refs.routeScrollBox.style.left=(wrapWidth-boxWidth)+"px";
                }else{
                    this.$refs.routeScrollBox.style.left=(currentLeft-200)+"px";
                }
            }
            
            
        },
       
        setBreadCrumbFn(){
            if(this.$route.name!=='home'){
                if(this.$route.meta.parent){
                    
                    if(this.$route.name==='assets_detail'||this.$route.name==='track_detail'){
                        let beforeRoute=JSON.parse(window.sessionStorage.getItem('beforeRoute'));
                        // console.log(beforeRoute)
                         this.breadCrumbList=[
                            // {
                            //     path:'/web/home',
                            //     name:'资产首页' 
                            // },
                            {
                                path:beforeRoute.path,
                                name:beforeRoute.title,
                            },
                            {
                                path:'',
                                name:this.$route.meta.title,
                            }
                           
                        ];
                    }else{
                         this.breadCrumbList=[
                            // {
                            //     path:'/web/home',
                            //     name:'资产首页' 
                            // },
                            {
                                path:this.$route.meta.parent.path,
                                name:this.$route.meta.parent.name,
                            },
                            {
                                path:'',
                                name:this.$route.meta.title,
                            }
                        ];
                    }
                 
                   
                }else{
                    this.breadCrumbList=[
                        // {
                        //     path:'/web/home',
                        //     name:'资产首页' 
                        // },
                        {
                            path:'',
                            name:this.$route.meta.title,
                        }
                    ];
                }
                 
            }else{
                this.breadCrumbList=[
                    {
                        path:'',
                        name:'资产首页' 
                    },
                   
                ];
            }
        },
        autoSiderFn(){
            if(window.innerWidth<=1600){
                this.$store.commit('setCollapse',true)
            }else{
                this.$store.commit('setCollapse',false)
            }

            this.$nextTick(()=>{
                    this.hideMenuFn();
            })
        },

        collapsedSider () {
            this.$store.commit('setCollapse',!this.$store.state.isCollapsed)
            //执行折叠菜单的方法
            this.hideMenuFn();
        },

        hideMenuFn(){
            //拿到所有菜单项
            var ivuMenuItems=document.getElementsByClassName('ivu-menu-item');
            if(this.$store.state.isCollapsed){
                //让所有菜单项下面的文字马上隐藏；
                for(var i=0;i<ivuMenuItems.length;i++){
                    ivuMenuItems[i].getElementsByTagName('span')[0].classList.add('hide')
                }
               
            }else{
                //隔0.2秒后让菜单项下面的文字显示
                setTimeout(()=>{
                        for(var k=0;k<ivuMenuItems.length;k++){
                            ivuMenuItems[k].getElementsByTagName('span')[0].classList.remove('hide')
                        }
                },200)

            }
        },

        avatarDropdownFn(name){
            switch(name){
                case 'message':
                    return
                    break;
                case 'logout':
                    //清除本地存储并退出页面
                    exitFn();
                    break;
                case 'passwordManage':
                    this.$router.push({
                        path:'/web/password_manage'
                    })
            }
        },

        transWebsocketFn(data){
            let dataArr=[];
            let toArr=data.split('{')
            toArr.splice(0,1);
            toArr.forEach((item,index)=>{
                let k=item.split('}')
                k.splice(k.length-1,1);
                let m=k[0].split(',')
                let itemObj={};
                m.forEach((g,h)=>{
                    let x=g.split('=');
                    itemObj[x[0].trim()]=x[1];
                })
                
                dataArr.push(itemObj)
            })

            return dataArr;
        },

        initWebSocket(){
            this.websock = new WebSocket(NET_PORT.deviceMsgSocket+'/'+getToken());
            this.websock.onmessage = this.websocketonmessage;
        },

        websocketonmessage(e){
            // let dataArr=this.transWebsocketFn(e.data)
            // this.deviceNoticeArr=dataArr;
            // this.deviceNoticeNum=dataArr.length;

            this.deviceNoticeArr=this.transWebsocketFn(e.data);

            let lose=0;
            let beyond=0;
            this.deviceNoticeArr.forEach((item,index)=>{
                if(item.locDept!==item.department&&item.department){
                    beyond++
                }
            })
            
            lose=this.deviceNoticeArr.length-beyond;
            this.deviceMessageNum.lose=lose;
            this.deviceMessageNum.beyond=beyond;
        },

        returnTopFn(){
            let contentWrap=document.getElementById('content_wrap');
            let returnButton=document.getElementById('return_top_wrap');
            let timer=null;
            let scrollValue=0;
            contentWrap.addEventListener('scroll', ()=>{
                if (contentWrap.scrollTop>100) {
                    returnButton.classList.add('return_top_active');
                } else {
                    returnButton.classList.remove('return_top_active');
                }
            })
            returnButton.addEventListener('click',()=>{
                clearInterval(timer)
                let scrollTop=contentWrap.scrollTop
                scrollValue=scrollTop
                if(scrollTop>100){
                    timer=setInterval(()=>{
                    scrollValue -= Math.ceil(scrollValue / 10);
                    if(scrollValue<=0){
                        scrollValue=0;
                        clearInterval(timer)
                    }
                    contentWrap.scrollTop=scrollValue;

                    },10)
                }
            })
        },
        afterResizeFn(){
            let timer=null;
            clearTimeout(timer)
            timer=setTimeout(()=>{
                this.autoSiderFn();
            },500)
        }
      
  },
  created(){
        this.initWebSocket()

        this.autoSiderFn();
        
        window.addEventListener('resize',this.afterResizeFn,true)
  },
  mounted(){
        this.setBreadCrumbFn();
        this.defaultName=this.$route.path
        this.currentRouteName=this.$route.name
        if(window.sessionStorage.getItem('openRouteArr')){
            this.openRouteArr=JSON.parse(window.sessionStorage.getItem('openRouteArr'))
        }

        // this.openRouteArr.push({
        //     name:this.routesData[1].children[0].name,
        //     path:this.routesData[1].children[0].path,
        //     title:this.routesData[1].children[0].meta.title,
        // })

        this.checkRouteFn(this.routesData[1].children[0])
        this.checkRouteFn(this.$route)
        this.$nextTick(()=>{
            this.$refs.menu.updateActiveName()
        });
        this.returnTopFn();

       

  },
    beforeDestroy(){
        this.websock.close();
        window.removeEventListener('resize',this.afterResizeFn,true)
    }
}
</script>
<style lang='scss'>
    @import "../../assets/scss/web/common/default.scss";
</style>
<style lang='scss' scoped>
    @import "../../assets/scss/web/layout.scss";
</style>
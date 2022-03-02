<template>
  <div class='layout_wrap'>
        <Layout style="height:100%">
            <Sider ref="side" hide-trigger collapsible :collapsed-width="78" v-model="$store.state.isCollapsed">
                 <div class="logo_wrap">
                    <a href="http://www.yikecha.net/" target='_blank'>
                        <img src="../assets/img/logo.png" alt="">
                    </a>
                    
                </div>
                <Menu ref="menu" :active-name="defaultName"  theme="light" width="auto" :class="menuitemClasses">

                     <template v-for="item in routesData[0].children" v-if="item.meta.showLeft">
                        <Tooltip placement="right" :content="item.meta.title" :disabled="!$store.state.isCollapsed">
                            <MenuItem  :name='item.path' :to='`/${item.path}`'>
                                <Icon :type='item.meta.icon' />
                                <span>{{item.meta.title}}</span>
                            </MenuItem>
                        </Tooltip>
                    </template>
                    
                </Menu>
            </Sider>
            <Layout>
                <Header :style="{padding: 0,zIndex:2}" class="layout-header-bar">
                    <Icon @click.native="collapsedSider" :class="rotateIcon" :style="{margin: '0 20px'}" type="md-menu" size="24"></Icon>
                    <span style="vertical-align:middle">{{project}}</span>
                    <div class="user_box">
                        
                        <Dropdown placement="bottom-end" @on-click="avatarDropdownFn" transfer>
                            <Badge >
                            <Avatar :src="userAvator" class="avatar"/>
                            </Badge>
                            <Icon :size="18" type="md-arrow-dropdown"></Icon>
                            <DropdownMenu slot="list" >
                                <div class="user_info">
                                    <h3>{{user}}</h3>
                                </div>
                                <DropdownItem name="passwordManage">密码管理</DropdownItem>
                                <DropdownItem name="logout">退出登录</DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                        <Tag color="geekblue">{{department}}</Tag>
                        <Tag  :color="role==='admin' ? 'success' : 'warning'">{{role==='admin'? '管理员' :'操作员'}}</Tag>
                        
                    </div>

                </Header>
              
                <Content id="content_wrap">
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
  </div>
</template>

<script>
import Vue from "vue"
import { Layout, Header , Sider , Content ,Menu ,MenuItem ,Dropdown ,DropdownMenu, DropdownItem, Badge ,Avatar,Tag,Icon,Tooltip} from 'iview';
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

//引入退出登录返回到登录页面方法
import { exitFn } from "../api/logout"

export default {
  name:'layout',
  components:{},
  props:{},
  data(){
    return {
        userAvator:require('../assets/img/users.jpg'),
        user:this.$store.state.user,
        role:this.$store.state.role,
        department:this.$store.state.queryData.department,
        project:this.$store.state.project,
        routesData:this.$store.state.routes,
        defaultName:'home',
    }
  },
  watch:{
        '$route'(){
            this.defaultName=this.$route.path.slice(1,this.$route.path.length);
        }
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
                    this.$store.state.isCollapsed ? 'collapsed-menu' : ''
                ]
            }
  },
  methods:{
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
                        path:'/password_manage'
                    })
            }
        },

        returnTopFn(){
            var contentWrap=document.getElementById('content_wrap');
            var returnButton=document.getElementById('return_top_wrap');
            var timer=null;
            var scrollValue=0;
            contentWrap.addEventListener('scroll', function () {
                if (contentWrap.scrollTop>100) {
                    returnButton.classList.add('return_top_active');
                } else {
                    returnButton.classList.remove('return_top_active');
                }
            })
            returnButton.addEventListener('click',()=>{
                clearInterval(timer)
                var scrollTop=contentWrap.scrollTop
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
        }
  },
  created(){
        this.autoSiderFn();
        var timer=null;
        window.addEventListener('resize',()=>{
            clearTimeout(timer)
            timer=setTimeout(()=>{
                this.autoSiderFn();
            },500)
        })
  },
  mounted(){
        this.defaultName=this.$route.path.slice(1,this.$route.path.length);
        this.$nextTick(()=>{
            this.$refs.menu.updateActiveName()
        });
        this.returnTopFn();
  }
}
</script>
<style scoped>
    @import "../assets/css/layout.css"
</style>
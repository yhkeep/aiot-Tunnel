<template>
  <div class='mobile_pane_wrap'>
      <div class="mobile_pane_box">
            <keep-alive>
                <router-view @getMessage="showMsg"  v-if="$route.meta.keepAlive"></router-view>
            </keep-alive>
            <router-view @getMessage="showMsg" v-if="!$route.meta.keepAlive"></router-view>
      </div>
      <!-- <Footer :deviceInfoNum="deviceInfoNum"></Footer> -->
      <Popup position="left" :style="{ width: '70%',height:'100%' }" v-model="moreMenuShow">
        <div class="mobile_side_wrap">
          <div class="mobile_side_avatar">
            <div class="mobile_side_avatar_left">
               <img src="../../../assets/img/users.jpg" alt="">
            </div>
            <div class="mobile_side_avatar_center">
                <h2>{{user}}</h2>
                <Tag  :type="role==='admin' ? 'success' : 'warning'">{{role==='admin'? '管理员' :'操作员'}}</Tag>
                
            </div>
            <div class="mobile_side_avatar_right">
                <Button round size="mini" type="info" @click="showDepartment=true;">
                  <span>授权部门</span>
                  <Icon name="arrow-down" />
                </Button>
            </div>
          </div>
         
          
          <div>
              <Cell v-for="(item,index) in routesData[2].children[0].children" class="mobile_side_menu_cell" :icon="item.meta.icon" :title="item.meta.title" is-link @click="toRouterFn(item.path)" :key="index" >
                <template #title v-if="item.name==='mobile_device_message'">
                  <span class="custom-title">设备动态</span>
                  <Tag style="transform:translateY(-1px);margin-left:10px;" :type="deviceInfoNum=='0'?'success' :'danger'" round>{{deviceInfoNum}}</Tag>
                </template>
              </Cell>

              <!-- <Cell class="mobile_side_menu_cell logout_btn" icon="close" is-link title="退出" @click="logoutFn"/> -->
          </div>
          <div class="mobile_side_logout">
            <!-- <Cell class="mobile_side_menu_cell" icon="close" is-link title="退出" @click="logoutFn"/> -->
            <div @click="logoutFn"><h3>退出</h3></div>
          </div>
        </div>
      </Popup>

      
      <Popup v-model="showDepartment" round position="bottom" :style="{ height: '30%' }" >
        <div class="department_show_wrap">
          <span  v-for="(item,index) in departmentroom.split(',')" :key="index">
            <Tag size="medium" type="primary">{{item}}</Tag>
          </span>
            
        </div>
      </Popup>

  </div>
</template>

<script>
// import Footer from "../../../components/mobile/Footer"
import {Button,Popup,Tag,CellGroup,Cell,Icon} from 'vant';
import 'vant/lib/button/style';
import 'vant/lib/popup/style';
import 'vant/lib/tag/style';
import 'vant/lib/cell-group/style';
import 'vant/lib/cell/style';
import 'vant/lib/icon/style';
import {exitFn} from '../../../api/logout.js'
import NET_PORT from "../../../api/port.js"
import {getToken} from "../../../utils/auth.js"
export default {
  name:'mobile_pane',
    components:{
    // Footer,
    Popup,
    Button,
    Tag,
    CellGroup,
    Cell,
    Icon,
  },
  props:{},
  data(){
    return {
        showDepartment:false,
        role:this.$store.state.role,
        user:this.$store.state.user,
        departmentroom:this.$store.state.departmentroom,
        routesData:this.$store.state.routes,
        moreMenuShow:false,
        deviceMessage:[],
        deviceInfoNum:0,
        //创建socket
        websock: null,
    }
  },
  watch:{},
  computed:{},
  methods:{
    
    showMsg (val) {  
      this.moreMenuShow=val
    },

  
    logoutFn(){
      
      this.$dialog.confirm({
        message: '你确定要退出吗？',
        confirmButtonColor:'red'
      })
        .then(() => {
          exitFn();
        })
        .catch(() => {
          // on cancel
        });
    },

    toRouterFn(route){
      this.$router.push(route)
      this.moreMenuShow=false;
    },

    //初始化weosocket
    initWebSocket(){ 
        this.websock = new WebSocket(NET_PORT.deviceMsgSocket+'/'+getToken());
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onclose = this.websocketclose;
    },

    websocketonmessage(e){
        let dataArr=[];
        let toArr=e.data.split('{')
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
        this.deviceInfoNum=dataArr.length;
    },


  },
  created(){
    this.initWebSocket()
  },
  mounted(){
  },
  beforeDestroy(){
    this.websock.close()
  }
}
</script>
<style lang="scss" scoped>
   @import "../../../assets/scss/mobile/pane_wrap.scss";
</style>
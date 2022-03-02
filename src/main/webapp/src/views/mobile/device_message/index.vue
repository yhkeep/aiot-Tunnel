<template>
  <div id='mobile_device_message_wrap'>
    <div class="mobile_search_wrap">
      <div class="mobile_device_message_header" ref="mobileSearchBoxFixed">
        <div class="mobile_device_message_title">
          <p>设备动态</p>
        </div>
        <div class="mobile_device_header_tools">
          <Icon name="bars" @click="showMoreMenuFn"/>
        </div>
      </div>
    </div>
    
    <div class="device_message_box">
      <div v-show="!deviceMessageArr.length" class="device_message_notice">
        <span>暂无动态...</span>
      </div>
      <ul v-show="deviceMessageArr.length" class="device_message_ul">
        <li v-for="(item,index) in deviceMessageArr" class="device_message_item">
            <div><p><span>{{index+1}}</span>. <span :class="item.locDept!==item.department&&item.department? 'device_message_color_2': 'device_message_color_1'">{{item.assetName==='null' ? '--' : item.assetName}}</span> - <span>{{item.assetID==='null' ? '--' : item.assetID}}</span> <span :class="item.locDept!==item.department&&item.department? 'beyond' : 'lose'">{{item.locDept!==item.department&&item.department?'超出电子围栏范围':'丢失信号'}}</span></p></div>
            <div>
                <p>品牌：<b>{{item.brand==='null' ? '--' : item.brand}}</b> & 型号：<b>{{item.type==='null' ? '--' : item.type}}</b></p>
                <p>最近更新时间：<b>{{item.updatetime==='null' ? '--' : item.updatetime}}</b> & 房间名：<b>{{item.cadMapRoomName==='null' ? '--' : item.cadMapRoomName}}</b></p>
                <p>所属科室：<b>{{item.locDept==='null' ? '--' : item.locDept}}</b> & 实时所在科室：<b>{{item.department==='null' ? '--' : item.department}}</b></p>
            </div>
        </li>
      </ul>
    </div>
    <div class="device_message_num_box custom_flex_row_wrap" v-show="deviceMessageArr.length">
      <div class="device_message_num_item">
        <span>{{deviceMessageNum.lose}}件</span>
      </div>
      <div class="device_message_num_item">
        <span>{{deviceMessageNum.beyond}}件</span>
      </div>
    </div>
  </div>
</template>

<script>
import {Icon } from 'vant';
import 'vant/lib/icon/style';
import NET_PORT from "../../../api/port.js"
import {getToken} from "../../../utils/auth.js"
export default {
  name:'mobile_device_message',
  components:{Icon},
  data(){
    return {
        deviceMessageArr:[],
        deviceMessageNum:{
            lose:0,
            beyond:0,
        },
        //创建socket
        websock: null,
        
    }
  },
 watch:{
    mobileScroll(){
        let scrollTop=this.mobileScroll;
        if(scrollTop>54){
          this.$refs.mobileSearchBoxFixed.classList.add('show');
        }else if(scrollTop===0){
          this.$refs.mobileSearchBoxFixed.classList.remove('show');
        }
    }
  },
  computed:{
    mobileScroll(){
      return this.$store.state.mobileScroll
    }
  },
  methods:{ 

    showMoreMenuFn(){
      this.$emit('getMessage', true);
    },

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

        this.deviceMessageArr=dataArr;
        let lose=0;
        let beyond=0;
        this.deviceMessageArr.forEach((item,index)=>{
            if(item.locDept!==item.department&&item.department){
                beyond++
            }
        })
        
        lose=this.deviceMessageArr.length-beyond;
        this.deviceMessageNum.lose=lose;
        this.deviceMessageNum.beyond=beyond;
    },

  },

  activated(){
    this.initWebSocket()
  },

  deactivated(){
    this.websock.close()
  },
 
}
</script>
<style lang="scss" scoped>
  @import "../../../assets/scss/mobile/device_message.scss";
  
</style>
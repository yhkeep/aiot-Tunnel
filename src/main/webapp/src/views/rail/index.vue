<template>
  <div class='rail_wrap'>
        <div class="custom_flex_wrap">
            <div class="custom_flex_item_2 hover_animat custom_bg_color_white row_box">
                    <h2 class="row_title">围栏地图</h2>
                    <div class="data_block" id="map_box">
                        
                    </div>
            </div>
            
            <div class="custom_flex_item_2 hover_animat custom_bg_color_white row_box">
                    <h2 class="row_title">设备动态</h2>
                    <div class="data_block" id="news_box">
                            <h3 v-if="deviceMessage.length<=0">暂无动态...</h3>
                            <div ref="deviceMessageWrap" v-else class="device_message_scroll_wrap">
                                <div ref="deviceMessageBox" class="device_message_scroll_box">
                                    <div class="device_message_item" v-for="(item,index) in deviceMessage" :key="index">
                                        <div class="device_message_item_left">
                                            <div class="device_message_icon_box">
                                                <!-- <i class="iconfont icon-tishi"></i> -->
                                                <Icon type="ios-warning" />
                                            </div>
                                        </div>
                                        <div class="device_message_item_right">
                                            <div class="device_message_item_right_top">
                                              <span>{{index+1}}-</span><span>{{item.brand==='null' ? '--' : item.brand}}</span> <b style="color:#e46cbb">{{item.assetName==='null' ? '--' : item.assetName}}</b> <span>{{item.type==='null' ? '--' : item.type}}</span><span>超出合法范围</span>
                                            </div>
                                            <div class="device_message_item_right_bottom">
                                                {{item.updatetime==='null' ? '--' : item.updatetime}}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                            </div>

                      </div>
            </div>
        </div>
  </div>
</template>

<script>
import Vue from "vue"
import {Icon,Tag ,Button} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Tag', Tag);
Vue.component('Button',Button);

export default {
  name:'rail',
  components:{},
  props:{},
  data(){
    return {
        deviceMessage:[],
        //创建socket
        websock: null,
    }
  },
  watch:{},
  computed:{},
  methods:{
       //初始化weosocket
        initWebSocket(){ 

            // this.websock = new WebSocket("ws://192.168.0.54:8009/websocket");
            this.websock = new WebSocket("ws://47.104.99.230:8080/websocket");
            this.websock.onopen = this.websocketonopen;
            this.websock.onerror = this.websocketonerror;
            this.websock.onmessage = this.websocketonmessage;
            this.websock.onclose = this.websocketclose;
        },

        websocketonopen() {
            console.log("WebSocket连接成功");
        },
        websocketonerror(e) { //错误
            console.log("WebSocket连接发生错误");
        },
        websocketonmessage(e){ //数据接收
            // console.log(e.data)
            let dataArr=[];
            //注意：长连接我们是后台直接1秒推送一条数据
            let toArr=e.data.split('{')
            toArr.splice(0,1);
            // console.log(toArr)
           
            toArr.forEach((item,index)=>{
                let k=item.split('}')
                k.splice(k.length-1,1);
                let m=k[0].split(',')
                let itemObj={};
                m.forEach((g,h)=>{
                    let x=g.split('=');
                    // console.log(x)
                    itemObj[x[0].trim()]=x[1];
                })
                
                dataArr.push(itemObj)

                
            })

            this.deviceMessage=dataArr;
        },


        websocketclose(e){ //关闭
            // console.log("connection closed (" + e.code + ")");
            // console.log(e)
        },
  },
  created(){
        //页面刚进入时开启长连接
        this.initWebSocket()
  },
  mounted(){},
    beforeDestroy(){
        //页面销毁时关闭长连接
        this.websocketclose();
    }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/device_message.css"></style>
<style scoped>
</style>
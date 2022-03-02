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
         initMapFn(){
            var map = new fengmap.FMMap({
                container: document.getElementById("map_box"),
                appName: "yikecha",
                key: "e2d15e7f84f0800239483a257eb6f08d",
                defaultMapScaleLevel: 15,
                // mapThemeURL: 'assets/map/theme',
                defaultThemeName: 'sc028-0002-20171212-01',
                // mapServerURL: 'assets/map/sc028-0002-20171212-01',
                focusAnimateMode: false, //开启聚焦层切换的动画显示
                viewModeAnimateMode: false, //开启2维，3维切换的动画显示
                moveToAnimateMode: false, //地图定位跳转动画设置
                naviLineAnimation: false,
                defaultViewMode: fengmap.FMViewMode.MODE_2D
            });

                map.openMapById('sc028-0002-20171212-01');
                map.showCompass = true;
                map.on('mapClickNode', (event) => {
                // console.log(event);
                });
                map.on('loadComplete', () => {
                const coords = [
                    { x: 11557063.95392135, y: 3622120.503429484 },
                    { x: 11557399.171417015, y: 3622214.006342391 },
                    { x: 11557475.033335855, y: 3621931.9093855997 },
                    { x: 11557088.391035853, y: 3621811.6267520385 }
                ]
                var groupLayer = map.getFMGroup(1);
                //创建PolygonMarkerLayer
                var layer = new fengmap.FMPolygonMarkerLayer();
                groupLayer.addLayer(layer);

                var polygonMarker = new fengmap.FMPolygonMarker({
                    alpha: .5,             //设置透明度
                    lineWidth: 1,      //设置边框线的宽度
                    height: 6,    //设置高度*/
                    points: coords //多边形坐标点
                });

                layer.addMarker(polygonMarker);
            })

        }
  },
  created(){
        //页面刚进入时开启长连接
        this.initWebSocket()
  },
  mounted(){
       this.$nextTick(()=>{
          this.initMapFn();
      })
  },
  
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
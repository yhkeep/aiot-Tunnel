<template>
  <div class='rail_wrap'>
        <div class="custom_flex_wrap">
            <div class="custom_flex_item_2 hover_animat custom_bg_color_white row_box">
                    <h2 class="row_title">围栏地图</h2>
                    <div class="data_block" id="map_box">
                        <div v-show="!showMap" class="map_loading_notice">
                          <h3>地图加载中...</h3>
                        </div>
                        <div v-show="showMap" id="mapID">
                            <div id="moniter">
                            <ul>
                                <li class="title">楼层选择</li>
                            </ul>
                            <ul id="moniterItem">
                                <li data-value="11"  class="active">十一层</li>
                                <li data-value="12">十二层</li>
                                <li data-value="13">十三层</li>
                                <li data-value="b1">医工科</li>
                                <li data-value="b2">报废库</li>
                            </ul>
                            </div>
                        
                        </div>
                    </div>
            </div>
            
            <div class="custom_flex_item_2 hover_animat custom_bg_color_white row_box">
                    <h2 class="row_title">设备动态</h2>
                    <div class="device_message_wrap">
                        <h3 v-if="deviceMessageArr.length<=0">暂无动态...</h3>
                        <div  v-else class="device_message_scroll_wrap">
                            <div class="device_message_scroll_box">
                                <div class="device_message_item" v-for="(item,index) in deviceMessageArr" :key="index">
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

import {baseData1,baseData2,baseData3,homeData11,homeData12,homeData13,homeDataB1,homeDataB2} from "../../../assets/js/leaflet/homeData.js"
import {leafLetScript,proj4ComScript,proj4LeafLetScript} from "../../../utils/needScript.js"

import NET_PORT from "../../../api/port.js"
import { getToken } from '../../../utils/auth.js';

export default {
  name:'rail',
  components:{},
  props:{},
  data(){
    return {
        showMap:false,
        deviceMessageArr:[],
        websock: null,

        homeLayers:null,
        baseLayers:null,
        deviceMarkers:[],
        devicePopups:[],
        map:null,

        currentFloor:'11',
        zoomKey:null,
    }
  },
  watch:{
     
  },
  computed:{
     
  },
  methods:{
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
        // this.deviceMessageArr=dataArr;
        this.deviceMessageArr=this.transWebsocketFn(e.data)
    },

    initMapFn(){
        this.homeLayers = new L.featureGroup();
        this.baseLayers = new L.featureGroup();
        this.markers=[]
        const resolutions = [
          156367.7919628329, 78183.89598141646, 39091.94799070823, 19545.973995354114, 9772.986997677057, 4886.4934988385285, 2443.2467494192642, 1221.6233747096321, 610.8116873548161, 305.40584367740803, 152.70292183870401, 76.35146091935201, 38.175730459676004, 19.087865229838002, 9.543932614919001, 4.7719663074595005, 2.3859831537297502, 1.1929915768648751, 0.5964957884324376, 0.2982478942162188
        ];
        const CRS_4549 = new L.Proj.CRS('EPSG:4549',
          '+proj=tmerc +lat_0=0 +lon_0=120 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs', {
            resolutions: resolutions
          }
        );

        let center = [-4.9002, 139.2102];
        const mybounds = L.latLngBounds(L.latLng(-4.279435, 140.075537), L.latLng(-5.438369, 138.29733));
        this.map = L.map('mapID', {
          center: center,
          layers: [this.homeLayers, this.baseLayers],
          zoom: 10,
          maxZoom: 14,
          minZoom: 10,
          attributionControl: false,
          doubleClickZoom: false,
          maxBounds: mybounds,
          crs: CRS_4549 // 定义的坐标系
        });


        this.initBoundaryFn(baseData1);
        this.initHomeLayerFn(homeData11);
        this.switchFloorFn();
        this.initZoomFn();
    },

    initZoomFn(){
        this.changeZoomFn();
        this.map.addEventListener("zoomend", e => {
            this.changeZoomFn();
        })
    },

    changeZoomFn(){
        let fonts=document.getElementsByClassName('showNAME')
        let mapZoom=this.map.getZoom()
        if(mapZoom<11){
            for(let i=0;i<fonts.length;i++){
              fonts[i].style.display="none";
              fonts[i].parentNode.style.display="none";
            }
        }else{
            for(let i=0;i<fonts.length;i++){
                fonts[i].style.display="block";
                fonts[i].parentNode.style.display="block";
                fonts[i].style.fontSize=mapZoom+2+'px';
            }
        }
    },


    initBoundaryFn(data){
        L.Proj.geoJson(data, {
          style: function(feature) {
            return {
              color: "#e46cbb",
              weight: 6,
              opacity: 1,
              fillColor: '#ddd',
              fillOpacity: 1,
            };
          }
        }).addTo(this.baseLayers);
    },

    createRoomFn(feature,color){
       var $geojson = L.Proj.geoJson(feature, {
          style: function(feature) {
            return {
              color: "#fff",
              opacity: 1,
              weight: 2,
              fillColor: color,
              fillOpacity: 1,
            };
          }
        }).addTo(this.homeLayers)

        var getBounds = $geojson.getBounds();
        var lat = (getBounds._northEast.lat - getBounds._southWest.lat) / 2 + getBounds._southWest.lat;
        var lng = (getBounds._northEast.lng - getBounds._southWest.lng) / 2 + getBounds._southWest.lng;
        var htmlTEXT = '<div class="showNAME">' + feature.properties.NAME + '</div>';
        var htmlMarker2 = L.marker([lat, lng], {
          icon: L.divIcon({
            className: 'leaflet-echart-icon',
            html: htmlTEXT
          }),
          pro: feature.properties
        }).addTo(this.homeLayers);

    },

    initHomeLayerFn(homeData){
          
        L.Proj.geoJson(homeData, {
            onEachFeature: (feature)=>{
                feature["crs"] = {
                  "type": "name",
                  "properties": {
                    "name": "EPSG:4549"
                  }
                };
                var color = this.initColorFn(feature.properties.TYPE)
                this.createRoomFn(feature,color)
            
            }
        })
    },

    initColorFn(TYPE){
          let colorArr=["#f7f0c5","#f7bbdf","#aeccfd","#88c7f9","#c3efc0","#c9b8ff","#ccc"]

          if(TYPE){
            return colorArr[TYPE-1]
          }else{
            return '#ddd'
          }
    },



     switchFloorFn(){
        document.getElementById('moniterItem').addEventListener('click',(e)=>{
          if(e.target.tagName.toLowerCase() === "li"){
              let currentFloor=e.target.getAttribute("data-value")
              if(currentFloor!==this.currentFloor){
                  let lis= document.getElementById('moniterItem').getElementsByTagName('li');
                  for(let i=0;i<lis.length;i++){
                    lis[i].classList.remove('active')
                  }
                  e.target.classList.add('active')
                  let baseData=null;
                  let homeData=null;
                  switch(currentFloor){
                    case '11' :
                      baseData=baseData1;
                      homeData=homeData11;
                      break;
                    case '12' :
                      baseData=baseData1;
                      homeData=homeData12;
                      break;
                    case '13' :
                      baseData=baseData1;
                      homeData=homeData13;
                      break;
                    case 'b1' :
                      baseData=baseData2;
                      homeData=homeDataB1;
                      break;
                    case 'b2' :
                      baseData=baseData3;
                      homeData=homeDataB2;
                      break;
                    
                  }
                  this.currentFloor=currentFloor;
                  this.clearAllDataFn();
                  this.initBoundaryFn(baseData);
                  this.initHomeLayerFn(homeData);
                  this.changeZoomFn()
              }
          }
            
        })
    },
    
    removeDeviceMarkerFn(){
      for(var i=0;i<this.deviceMarkers.length;i++){
        this.map.removeLayer(this.deviceMarkers[i])
      }
      for(var k=0;k<this.devicePopups.length;k++){
        this.map.removeLayer(this.devicePopups[k])
      }
      this.devicePopups=[]
      this.deviceMarkers=[]
    },

    removeRoomFn(){
      this.homeLayers.clearLayers()
    },

    removeBoundaryFn(){
      this.baseLayers.clearLayers()
    },

    clearAllDataFn(){
      this.removeBoundaryFn();
      this.removeRoomFn();
      this.removeDeviceMarkerFn();
    },
  },
  
  created(){
      this.initWebSocket()
  },
  mounted(){
      this.$nextTick(()=>{
        leafLetScript()
        .then(()=>{
            proj4ComScript()
            .then(()=>{
              proj4LeafLetScript()
              .then(()=>{
                this.showMap=true;
                this.$nextTick(()=>{
                  this.initMapFn();
                })
              })
            })
        })

    })
  },
  beforeDestroy(){
      this.websock.close();
  }
}
</script>


<style lang='scss' scoped>
@import "../../../assets/scss/web/common/device_message.scss";
</style>
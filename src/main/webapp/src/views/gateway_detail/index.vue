<template>
  <div class='gateway_detail_wrap'>
      <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>
       <div class="row_box hover_animat custom_bg_color_white">
            <h2 class="row_title">地图</h2>
            <div id="mapID" style="height:400px;">
                <div id="moniter">
                  <ul>
                      <li style="background:#fff;color: #558eea">楼层选择</li>
                  </ul>
                  <ul id="moniterItem">
                      <li data-value="11" class="active">十一层</li>
                      <li data-value="12">十二层</li>
                      <li data-value="13">十三层</li>
                  </ul>
                </div>
               
            </div>
        </div>

         <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">网关信息<span style="font-size:14px">（在地图上选择点位后会自动填充位置信息到下方输入框中）</span></h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="8">
                            <h3>网关mac地址</h3>
                            <Input v-model="gatewayData.mac" placeholder="mac地址" />
                            </Col>
                            <Col span="8">
                                <h3>所在楼层</h3>
                                <Input disabled v-model="gatewayData.floor" placeholder="所在楼层" />
                            </Col>
                            <Col span="8">
                                <h3>坐标位置</h3>
                                <Input disabled v-model="gatewayData.posi.toString()" placeholder="坐标位置" />
                            </Col>
                        </Row>
                    </div>

                  

                </div>
                <div class="submit_btn_wrap">
                       <Button type="primary" @click="saveFn">保存</Button>
                </div>
            </div>
        </div>

         <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
           
            <div style="text-align:center;padding:20px 0">
                <Spin fix v-if="!achieveUpload&&!errorUpload">
                    <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                    <div>正在更新</div>
                </Spin>
                <Spin fix v-if="achieveUpload" style="color:#00ad19">
                    <Icon type="ios-checkmark-circle" size=18 />
                    <div>更新成功</div>
                </Spin>
                <Spin fix v-if="errorUpload" style="color:#f72b2b">
                    <Icon type="ios-close-circle" size=18 />
                    <div>更新失败</div>
                </Spin>

            </div>
            
        </Modal>


  </div>
</template>

<script>

import Vue from "vue"

import {leafLetScript,proj4ComScript,proj4LeafLetScript} from "../../utils/needScript.js"
import { Row,Col,Icon,Table ,Tag ,Select,Option,Page,Input,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Table', Table);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
export default {
  name:'gateway_detail',
  components:{},
  props:{},
  data(){
    return {

        gatewayData:{
            mac:'',
            floor:'',
            posi:'',
        },

        homeLayers:null,
        pointLayers:null,
        deviceMarkers:[],
        devicePopups:[],
        map:null,

        currentFloor:'',
        zoomKey:null,

        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

    }
  },
  watch:{

       currentFloor(){
          this.gatewayData.floor=this.currentFloor;
      }
  },
  computed:{},
  methods:{

       returnFn(){
            this.$router.go(-1)
        },

        initMapFn(data){
      
          this.homeLayers = new L.featureGroup();
          this.pointLayers = new L.featureGroup();
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
            layers: [this.homeLayers, this.pointLayers],
            zoom: 10,
            maxZoom: 13,
            minZoom: 10,
            attributionControl: false,
            doubleClickZoom: false,
            maxBounds: mybounds,
            crs: CRS_4549
          });


          this.initBoundaryFn();
          this.initHomeLayerFn();
          switch(data.floor){
            case "11" :
              this.initHomeLayerFn(homeData11);
              break;
            case "12":
              this.initHomeLayerFn(homeData12);
              break;
            case "13":
              this.initHomeLayerFn(homeData13);
              break;
          }
          this.createMarkerFn(data)
          this.initFloorFn();
          this.initMarkerFn();
          this.initZoomFn();
    },

    createMarkerFn(data){

            var marker=L.marker(data.posi, {
                icon: L.icon({
                iconUrl: require('../../assets/img/location.png'),
                iconSize: [20],
                })
            }).addTo(this.map)
            let popInfo = "X:" + data.posi[0] + "</br>Y:" + data.posi[1];
            var popup=L.popup({
                minWidth: 120,
                minHeight: 100,
                closeButton:false,
            }).setLatLng(data.posi).setContent(popInfo).openOn(this.map);
                        
            this.deviceMarkers.push(marker);
            this.devicePopups.push(popup);

    },

    initMarkerFn(){

      
        this.map.addEventListener('click',e=>{
            this.removeDeviceMarkerFn();
            this.gatewayData.posi='';
            var marker=L.marker(e.latlng, {
                icon: L.icon({
                iconUrl: require('../../assets/img/location.png'),
                iconSize: [20],
                })
            }).addTo(this.map)
            let popInfo = "X:" + e.latlng.lat.toFixed(6) + "</br>Y:" + e.latlng.lng.toFixed(6);
            var popup=L.popup({
                minWidth: 120,
                minHeight: 100,
                closeButton:false,
            }).setLatLng(e.latlng).setContent(popInfo).openOn(this.map);
                        
            this.deviceMarkers.push(marker);
            this.devicePopups.push(popup);
            this.gatewayData.posi=[e.latlng.lat.toFixed(6),e.latlng.lng.toFixed(6)];
        })
    },

    initZoomFn(){
        this.changeZoomFn();
        this.map.addEventListener("zoomend", e => {
            this.changeZoomFn();
        })
    },

    changeZoomFn(){
          var fonts=document.getElementsByClassName('showNAME')
            if(this.map.getZoom()<11){
               for(var i=0;i<fonts.length;i++){
                  fonts[i].style.display="none";
                  
                }
            }
            else{
                for(var i=0;i<fonts.length;i++){
                    fonts[i].style.display="block";
                }

                if(this.map.getZoom()===13){
                  for(var i=0;i<fonts.length;i++){
                      fonts[i].style.fontSize='16px';
                    }
                }
                else if(this.map.getZoom()===12){
                   for(var i=0;i<fonts.length;i++){
                      fonts[i].style.fontSize='14px';
                    }
                }
                else{
                  for(var i=0;i<fonts.length;i++){
                      fonts[i].style.fontSize='12px';
                    }
                }
              
            }
    },

    initBoundaryFn(){
        L.Proj.geoJson(baseHomeData, {
          style: function(feature) {
            return {
              color: "#ddd",
              weight: 5,
              opacity: 1,
              fillOpacity: 1,
            };
          }
        }).addTo(this.map);
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
              var $geojson = L.Proj.geoJson(feature, {
                style: function(feature) {
                  return {
                    color: "#FFFFFF",
                    opacity: 1,
                    weight: 2,
                    fillColor: color,
                    fillOpacity: 1,
                  };
                }
              }).addTo(this.homeLayers);
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
            }
          })
    },

    initColorFn(TYPE){

                    switch(TYPE) {
                      case "1":
                        var color = "#f7f0c5";
                        break;
                      case "2":
                        var color = "#f7bbdf";
                        break;
                      case "3":
                        var color = "#aeccfd";
                        break;
                      case "4":
                        var color = "#88c7f9";
                        break;
                      case "5":
                        var color = "#c3efc0";
                        break;
                      case "6":
                        var color = "#c9b8ff";
                        break;
                      default:
                        var color = "#ddd";
                        break;
                    }
                    return color;
    },



    initFloorFn(){
        document.getElementById('moniterItem').addEventListener('click',(e)=>{
            e.stopPropagation();
            var lis= document.getElementById('moniterItem').getElementsByTagName('li');
            for(var i=0;i<lis.length;i++){
              lis[i].classList.remove('active')
            }
            e.target.classList.add('active')
              switch(e.target.getAttribute("data-value")) {
                case '11':
                  this.clearAllDataFn();
                  this.initHomeLayerFn(homeData11);
                  this.currentFloor="11";
                  this.changeZoomFn()
                  
                  break;
                case '12':
                  this.clearAllDataFn();
                  this.initHomeLayerFn(homeData12);
                  this.currentFloor="12";
                  this.changeZoomFn()
                  
                  break;
                case '13':
                  this.clearAllDataFn();
                  this.initHomeLayerFn(homeData13);
                  this.currentFloor="13";
                  this.changeZoomFn()
                  
                  break;
                default:
                  break;
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

    clearAllDataFn(){
      this.homeLayers.clearLayers()
      this.removeDeviceMarkerFn();
      this.gatewayData.posi='';
    },

    successFn(){
        this.achieveUpload=true;
        setTimeout(()=>{
            this.uploadModal=false;
            setTimeout(()=>{
                    this.achieveUpload=false;
                    this.errorUpload=false;
                    //回到上一页
                    this.$router.go(-1)
            },500)

        },1000)
    },

    errorFn(){
          this.errorUpload=true;
          setTimeout(()=>{
                  this.uploadModal=false;
                  setTimeout(()=>{
                      this.achieveUpload=false;
                      this.errorUpload=false;
                  },500)
              
          },1000)
    },

    saveFn(){
        
        if(this.gatewayData.mac.trim()===''||this.gatewayData.floor===''||this.gatewayData.posi===''){
            this.$Modal.error({
                title:'错误',
                content:'信息不完整！',
            })
        }else{

            this.$Modal.confirm({
                title: '提示',
                content: '您是否要更新网关位置？',
                onOk: () => {
                   
                    //这里发送请求更新网关位置
                    console.log(this.gatewayData)
                    //显示出正在更新
                    this.uploadModal=true;
                    //更新成功回调
                    // this.successFn()
                    //更新失败回调
                    // this.errorFn()
                },
               
            });
            
        }
    },

  },
  created(){},
  mounted(){

       this.$nextTick(()=>{
        
          leafLetScript()
          .then(()=>{
             proj4ComScript()
             .then(()=>{
                proj4LeafLetScript()
               .then(()=>{

                  if(window.sessionStorage.getItem('detailData')){
                    var data=JSON.parse(window.sessionStorage.getItem('detailData'));

                    // 楼层选择的样式切换到当前网关所在楼层
                    var lis= document.getElementById('moniterItem').getElementsByTagName('li');
                    console.log(lis)
                    lis[0].classList.remove('active')
                    var index=Number(data.floor)-11
                    lis[index].classList.add('active')


                    this.currentFloor=data.floor;
                    this.gatewayData.mac=data.mac;
                    this.gatewayData.posi=data.posi;

                    this.initMapFn(data);
                    
                  }else{
                      this.$Modal.error({
                          title:'错误',
                          content:'未查找到网关信息，请重试！',
                          onOk:this.returnFn,
                      })
                  }
                  
               })
             })
          })

      })
  },

   beforeDestroy(){
        window.sessionStorage.removeItem('detailData');
  }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped>
</style>
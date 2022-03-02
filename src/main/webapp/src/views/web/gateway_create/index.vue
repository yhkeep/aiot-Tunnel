<template>
  <div class='gateway_create_wrap'>
        <div class="row_box hover_animat custom_bg_color_white">
            <h2 class="row_title">地图</h2>
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
                        <!-- 这里是加入的测试办公室数据 -->
                        <li data-value="t1">办公室</li>
                        <!-- 这里是加入的测试办公室数据 -->
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
                            <Col span="6">
                              <h3>网关mac地址</h3>
                              <Input v-model="createData.gatewaymac" placeholder="mac地址（格式:ac233fa031fa）" />
                            </Col>
                            <Col span="6">
                              <h3>IP地址（可不填）</h3>
                              <Input v-model="createData.ipaddress" placeholder="输入IP地址" />
                            </Col>
                           
                            <Col span="6">
                                <h3>所属科室</h3>
                                <Input v-model="createData.department" placeholder="输入所属科室" />
                            </Col>
                            <Col span="6">
                                <h3>所处位置</h3>
                                <Input v-model="createData.location" placeholder="输入所处位置" />
                            </Col>
                           
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>房间名</h3>
                                <Input disabled v-model="createData.cadMapRoomName"/>
                            </Col>
                            <Col span="6">
                                <h3>所在楼层</h3>
                                <Input disabled v-model="createData.floor"  />
                            </Col>
                            <Col span="6">
                                <h3>坐标位置x</h3>
                                <Input disabled v-model="createData.mapx"  />
                            </Col>
                            <Col span="6">
                                <h3>坐标位置y</h3>
                                <Input disabled v-model="createData.mapy"  />
                            </Col>
                           
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>房间编号</h3>
                                <Input disabled v-model="createData.roomnumber"/>
                            </Col>
                        </Row>
                    </div>

                </div>
                <div class="submit_btn_wrap">
                       <Button type="primary" shape="circle" @click="createFn">确定新增</Button>
                </div>
            </div>
        </div>

        <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
           
            <div style="text-align:center;padding:20px 0">
                <Spin fix v-if="!achieveUpload&&!errorUpload">
                    <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                    <div>正在保存</div>
                </Spin>
                <Spin fix v-if="achieveUpload" style="color:#00ad19">
                    <Icon type="ios-checkmark-circle" size=18 />
                    <div>保存成功</div>
                </Spin>
                <Spin fix v-if="errorUpload" style="color:#f72b2b">
                    <Icon type="ios-close-circle" size=18 />
                    <div>保存失败</div>
                </Spin>

            </div>
           
        </Modal>

  </div>
</template>

<script>

import Vue from "vue"
import { Row,Col,Icon,Table ,Tag ,Page,Input,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
// 这里加入了测试办公室数据 baseData4和homeDataT1
import {baseData1,baseData2,baseData3,homeData11,homeData12,homeData13,homeDataB1,homeDataB2,baseData4,homeDataT1} from "../../../assets/js/leaflet/homeData.js"
import {leafLetScript,proj4ComScript,proj4LeafLetScript} from "../../../utils/needScript.js"
import NET_PORT from "../../../api/port.js"
import formatScanCodeFn from "../../../utils/formatScanCode.js"
export default {
  name:'gateway_create',
  components:{},
  props:{},
  data(){
    return {
      showMap:false,
      createData:{
        gatewaymac:'',
        ipaddress:'',
        floor:'11',
        department:'',
        location:'',
        gatewayfree:'1',
        gatewayload:'',
        mapx:'',
        mapy:'',
        cadMapRoomName:'',
        roomnumber:'',
      },

      baseLayers:null,
      homeLayers:null,
      deviceMarkers:[],
      devicePopups:[],
      map:null,

      currentFloor:'11',
      zoomKey:null,

      uploadModal:false,
      achieveUpload:false,
      errorUpload:false,

      currentRoom:{
        geoJson:null,
        layer:null,
      },

      selectRoom:{},
    }
  },
  watch:{
      currentFloor(){
          this.createData.floor=this.currentFloor;
      }
  },
  computed:{},
  methods:{
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
            crs: CRS_4549 
          });
          this.initBoundaryFn(baseData1);
          this.initHomeLayerFn(homeData11);
          this.switchFloorFn();
          this.initZoomFn();
          this.changeZoomFn();
    },

    initMarkerFn(e){
        this.removeDeviceMarkerFn();
        this.createData.mapx='';
        this.createData.mapy='';
        var marker=L.marker(e.latlng, {
            icon: L.icon({
            iconUrl: require('../../../assets/img/location.png'),
            iconSize: [20],
            })
        }).addTo(this.map)
        
                    
        this.deviceMarkers.push(marker);
        this.createData.mapx=e.latlng.lat.toFixed(6);
        this.createData.mapy=e.latlng.lng.toFixed(6);
    },

    initZoomFn(){
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
              color: "#ddd",
              weight: 5,
              opacity: 1,
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

        $geojson.addEventListener('click',(e)=>{
          //在地图上打出定位点
          this.initMarkerFn(e)
          
          //设置房间高亮
          if(this.currentRoom.layer&&this.currentRoom.geoJson){
             this.currentRoom.geoJson.resetStyle(this.currentRoom.layer);
          }
          this.currentRoom.layer=e.layer;
          this.currentRoom.geoJson=$geojson;
          this.selectRoom=e.layer.feature.properties;
          this.createData.cadMapRoomName=this.selectRoom.NAME;
          this.createData.roomnumber=this.selectRoom.NO;
          $geojson.setStyle({
              color: "#FFFFFF",
              opacity: 1,
              weight: 2,
              fillColor: '#ffd100',
              fillOpacity: 1,
          })

        })
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
                    //这里是加入的测试办公室数据
                    case 't1' :
                      baseData=baseData4;
                      homeData=homeDataT1;
                      break;
                    //这里是加入的测试办公室数据
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
      this.createData.mapx='';
      this.createData.mapy='';
      this.createData.cadMapRoomName='';
      this.createData.roomnumber='';
    },

    initData(){
      this.clearAllDataFn();
      switch(this.currentFloor) {
        case '11':
          this.initBoundaryFn(baseData1);
          this.initHomeLayerFn(homeData11);
          break;
        case '12':
          this.initBoundaryFn(baseData1);
          this.initHomeLayerFn(homeData12);
          
          break;
        case '13':
          this.initBoundaryFn(baseData1);
          this.initHomeLayerFn(homeData13);
          
          break;
        case 'b1':
          this.initBoundaryFn(baseData2);
          this.initHomeLayerFn(homeDataB1);
          
          break;
        case 'b2':
          this.initBoundaryFn(baseData3);
          this.initHomeLayerFn(homeDataB2);
          
          break;
          //这里是加入的测试办公室数据
        case 't1':
          this.initBoundaryFn(baseData4);
          this.initHomeLayerFn(homeDataT1);
          
          break;
          //这里是加入的测试办公室数据
        default:
          break;
      }

      this.changeZoomFn()
      this.createData={
          gatewaymac:'',
          ipaddress:'',
          floor:this.currentFloor,
          department:'',
          location:'',
          gatewayfree:'1',
          gatewayload:'',
          mapx:'',
          mapy:'',
          cadMapRoomName:'',
          roomnumber:'',
      }
    },

    successFn(){
          this.achieveUpload=true;
          setTimeout(()=>{
              this.uploadModal=false;
              //成功后清除掉填写的数据
              this.initData();
              setTimeout(()=>{
                      this.achieveUpload=false;
                      this.errorUpload=false;
                      //回到上一页
                      // this.$router.go(-1)
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

    createFn(){
        
        if(this.createData.gatewaymac.trim()===''||this.createData.floor===''||this.createData.mapx===''||this.createData.mapy===''||this.createData.location.trim()===''||this.createData.cadMapRoomName===''||this.createData.roomnumber===''){
            this.$Modal.error({
                title:'错误',
                content:'请输入完整信息！',
            })
        }else{

          //显示出正在保存
          this.uploadModal=true;

          let datas={
            gatewaymac:formatScanCodeFn(this.createData.gatewaymac.trim()),
            ipaddress:this.createData.ipaddress.trim(),
            floor:this.createData.floor,
            department:this.createData.department.trim(),
            location:this.createData.location.trim(),
            gatewayfree:this.createData.gatewayfree,
            gatewayload:this.createData.gatewayload,
            mapx:this.createData.mapx,
            mapy:this.createData.mapy,
            cadMapRoomName:this.createData.cadMapRoomName.trim(),
            roomnumber:this.createData.roomnumber,
          }

           this.$axios.post(NET_PORT.gatewayCreate,datas)
            .then((res)=>{

                if(res.data.code===0){
                    // 保存成功回调
                    this.successFn()
                }else{
                    //保存失败回调
                    this.errorFn()
                    this.$Message.error({
                      content:res.data.msg,
                      duration:2,
                    })
                }

            })
            .catch((err)=>{
                //保存失败回调
                this.errorFn()
            })

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
                    this.showMap=true;
                    this.$nextTick(()=>{
                      this.initMapFn();
                    })
               })
             })
          })

      })
  }
}
</script>


<style lang='scss' scoped>
  @import '../../../assets/scss/web/common/form_data.scss';
</style>
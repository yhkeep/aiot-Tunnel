<template>
  <div class='device_manage_wrap'>
      <div class="row_box hover_animat custom_bg_color_white">
            <h2 class="row_title">地图</h2>
            <div id="mapID" style="height:700px;">
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
                <div id="device_area">
                  <div class="device_area_header">
                      
                  </div>
                  <div class="device_area_content">
                      
                  </div>
                </div>
            </div>
      </div>

       <div class="row_wrap hover_animat custom_bg_color_white">
      <div class="row_box">
          <h2 class="row_title">设备明细表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">
                <Button type="primary" icon="md-add" @click="toCreateFn">新增设备</Button>
                <Button v-if="$store.state.role==='admin'" type="error" icon="md-trash" @click="deleteFn">删除设备</Button>
                <Button type="success" icon="md-archive" @click=exportExcelFn>导出设备总表</Button>
              </div>
              <div class="data_table_top_right">
                  <div class="pagination_wrap">
                    <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                  </div>
              </div>
            </div>

            <div class="table_scroll_wrap">
                 <Table style="min-width:1500px" stripe :columns="deviceDetailColumn" :data="deviceNowData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                    <template slot-scope="{row}" slot="assetID">
                      <a @click="toDetailFn(row.assetID)">{{row.assetID}}</a>
                    </template>
                    <template slot-scope="{row}" slot="applyDept">
                        <span>{{row.applyDept ? row.applyDept : '--'}}</span>
                    </template>
                    <!-- <template slot-scope="{row}" slot="locDept">
                        <span>{{row.locDept ? row.locDept : '--'}}</span>
                    </template> -->
                    <template slot-scope="{row}" slot="location">
                        <span>{{row.location ? row.location : '--'}}</span>
                    </template>
                    <!-- <template slot-scope="{row}" slot="operatingRoom">
                        <span>{{row.operatingRoom ? row.operatingRoom : '--'}}</span>
                    </template> -->
                    <template slot-scope="{row}" slot="check">
                        <span>{{row.check ? row.check : '--'}}</span>
                    </template>

                    <template slot-scope="{row}" slot="amount">
                        <span>{{Number(row.amount)? Number(row.amount) : '--'}}</span>
                    </template>
                    
                    <template slot-scope="{row}" slot="buyDate">
                      <span>2019-01-01</span>
                    </template>
                    
                    <template slot-scope="{row}" slot="money">
                      <span>10000</span>
                    </template>
                    <template slot-scope="{row}" slot="electric">
                        <span>{{row.electric}}%</span>
                    </template>

                    <template slot-scope="{ row }" slot="status">
                        <Tag v-if="row.status==='正常'" color="success"><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='在修'" color="warning"><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='待报废'" color="primary"><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='已报废'" color="error"><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='--'"  color="default"><span>{{row.status}}</span></Tag>
                        <Tag v-else  color="default"><span>{{row.status}}</span></Tag>
                    </template>
                  
                </Table>
            </div>

            
      </div>
    </div>

     

     

      <Modal v-model="deleteModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
          <div style="text-align:center;padding:20px 0">
              <Spin fix v-if="!achieveDelete&&!errorDelete">
                  <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                  <div>正在删除</div>
              </Spin>
              <Spin fix v-if="achieveDelete" style="color:#00ad19">
                  <Icon type="ios-checkmark-circle" size=18 />
                  <div>删除成功</div>
              </Spin>
              <Spin fix v-if="errorDelete" style="color:#f72b2b">
                  <Icon type="ios-close-circle" size=18 />
                  <div>删除失败</div>
              </Spin>
          </div>
      </Modal>

  </div>
</template>

<script>
import Vue from "vue"

import {leafLetScript,proj4ComScript,proj4LeafLetScript} from "../../utils/needScript.js"
import {Icon,Table ,Tag ,Page,Input,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);

import gatewayData from '../../utils/gatewayData.js';

export default {
  name:'device_manage',
  components:{},
  props:{},
  data(){
    return {
      totalCount: 0,
      pageSize: 10,
      currentPage: 1,
      tableLoading:true,

      deleteModal:false,
      achieveDelete:false,
      errorDelete:false,

      selection:[],

      gatewayData:gatewayData,
      gatewayFloorData:[],

      deviceDetailColumn:[
            {
                title: '资产id',
                key: 'assetID',
                slot:'assetID',
            },
            {
                title: '通用名称',
                key: 'generalName'
            },
            {
                title: '资产名称',
                key: 'assetName'
            },
            {
                title: '部门',
                key: 'department'
            },
            {
                title: '规格型号',
                key: 'type'
            },
            {
                title: '品牌',
                key: 'brand'
            },
            {
                title: '申请科室',
                key: 'applyDept',
                slot: 'applyDept',
            },
            // {
            //     title: '放置科室',
            //     key: 'locDept',
            //     slot: 'locDept',
            // },
            {
                title: '坐落位置',
                key: 'location',
                slot: 'location',
            },
            // {
            //     title: '手术室',
            //     key: 'operatingRoom',
            //     slot: 'operatingRoom',
            // },
           
            {
                title: '数量',
                key: 'amount',
                slot:'amount'
            },
            {
                title: '单位',
                key: 'unit'
            },
            
            {
                title: '盘点日期',
                key: 'check',
                slot:'check'
            },
           
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:100
            },
            // {
            //     title: 'mac地址',
            //     key: 'mac'
            // },
            // {
            //     title: '购买日期',
            //     key: 'buyDate',
            //     slot:'buyDate'
            // },
            // {
            //     title: '金额',
            //     key: 'money',
            //     slot:'money'
            // },
            // {
            //     title: '电量',
            //     key: 'electric',
            //     slot:'electric'
            // },
        
      ],

      deviceDetailData:[],
      deviceNowData:[],
      homeLayers:null,
      pointLayers:null,
      markers:[],
      trackMarkers:[],
      deviceMarkers:[],
      map:null,

      mapData:[],
      data11:[],
      data12:[],
      data13:[],

      currentFloor:'11',

      zoomKey:null,
      trackTimerArr:[],
      trackObj:{},

      deviceAreaOnOff:false,

    }
  },
  watch:{},
  computed:{},
  
  methods:{

    changePageFn(page){
      this.tableLoading=true;
      //改变页面的时候apollo会同步更新
      this.currentPage=page;
      
      this.getDataFn();
    },

    //导出excel
    exportExcelFn(){
        let href = process.env.API_HOST+'huaxi/poi/export'
        let a = document.createElement('a')
        a.setAttribute('download', '')
        a.setAttribute('href', href)
        a.click()
    },

    //勾选选项时
    checkAllGroupChange(selection){
      this.selection=selection;
    },

    //去到设备新增页面
    toCreateFn(){
        this.$router.push({
          path:'/assets_create'
        })
    },

    //去到设备明细页面
    toDetailFn(id){
      //将当前数据存入vuex
      for(var i=0;i<this.deviceNowData.length;i++){
        if(this.deviceNowData[i].assetID===id){
          window.sessionStorage.setItem('detailData',JSON.stringify(this.deviceNowData[i]))
        }
      }

       this.$router.push({
        path:'/assets_detail'
      })

    },

    //点击删除按钮
    deleteFn(){
        if(this.selection.length){
              this.$Modal.confirm({
                    title:'删除提示',
                    okText:'确定删除',
                    cancelText:'取消',
                    content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+'个资产吗？</h2>',
                    onOk:()=>{
                          this.deleteCertainFn();
                    },
              })
        }else{
          return this.$Message.error({
                    content:"请选择至少一项!",
                    duration:2,
          })
        }
    },

    successFn(){
       this.achieveDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                //删除成功后隔1秒隐藏模态框并更新数据
                this.currentPage=1;
                this.getDataFn('all');
                
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
    },

    errorFn(){
       this.errorDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
    },

    //点击确认删除
    deleteCertainFn(){
      this.deleteModal=true;
      var list=[];
      for(var i=0;i<this.selection.length;i++){
        list.push(this.selection[i].assetID)
      }
      
      //要删除的资产的id数组
      // console.log(list)

      this.$axios.get(process.env.API_HOST+'huaxi/del?assetID='+list)
      .then((res)=>{
          // console.log(res.data)
          if(res.data.msg==='ok'){
            this.successFn();
            
          }else if(res.data.msg==='failed'){
            this.errorFn();
          }

      })
      .catch((err)=>{
            this.errorFn();
      })

    },

  getDataFn(){
          this.$Loading.start();
          this.$axios.get(process.env.API_HOST+'huaxi/assetManagement/param?currentPage='+this.currentPage)
          .then((res)=>{
              var initData=res.data;
              var total=Number(initData.pop().total);
              this.deviceNowData=[];
                for(var i=0;i<initData.length;i++){
                    for(var k in initData[i]){
                        if(initData[i][k]===''||initData[i][k]==='null'){
                          initData[i][k]='--'; 
                        };

                    }
                    this.deviceNowData.push(initData[i])
                }
                this.totalCount=total;
                this.tableLoading=false;
                this.$Loading.finish()
          })
          .catch((err)=>{
            console.log(err)
            this.$Loading.error();
          })
    },

    initMapFn(){
      
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
            zoom: 11,
            maxZoom: 13,
            minZoom: 10,
            attributionControl: false,
            doubleClickZoom: false,
            maxBounds: mybounds,
            crs: CRS_4549 // 定义的坐标系
          });

          this.initBoundaryFn();
          this.initHomeLayerFn(homeData11);
          this.initGatewayFn("11");
          this.initFloorFn();
          this.initZoomFn();
 

    },

    initZoomFn(){
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

    initGatewayFn(floor){
                   
            for(var i=0;i<this.gatewayFloorData.length;i++){
                if(this.gatewayFloorData[i].floor===floor){
                      for(var k=0;k<this.gatewayFloorData[i].gateway.length;k++){
                          var posiData={
                            lat:this.gatewayFloorData[i].gateway[k].posi[0],
                            lng:this.gatewayFloorData[i].gateway[k].posi[1],
                          }

                          var marker=L.marker(posiData, {
                            icon: L.icon({
                              iconUrl: require('../../assets/img/location.png'),
                              iconSize: [30],
                            })
                          }).addTo(this.map)
                          marker._mac=this.gatewayFloorData[i].gateway[k].mac
                          this.markers.push(marker)
                          
                          this.gateWayMarkerFn(marker,posiData,marker._mac)
                            
                      }
                }
            };

    },
    setTrackFn(startPoint,endPoint,num){
      
            //算出起点到终点直线长度
            var lineKey=Math.sqrt(Math.abs(Math.abs(endPoint.y)-Math.abs(startPoint.y))+Math.abs(Math.abs(endPoint.x)-Math.abs(startPoint.x)));
            // console.log(lineKey.toFixed(1))
            //定义每一个轨迹点在斜线上的偏移量
            var lineStep=0.1;
            var testArr=[];

            for(var k=1;k<lineKey/lineStep;k++){
              var xStep=Math.abs(Math.abs(endPoint.x)-Math.abs(startPoint.x))/(lineKey/(lineStep*k));
              var yStep=Math.abs(Math.abs(endPoint.y)-Math.abs(startPoint.y))/(lineKey/(lineStep*k))
              var posiDataDemo={
                lat:endPoint.x>startPoint.x? xStep+startPoint.x : -xStep+startPoint.x,
                lng:endPoint.y>startPoint.y? yStep+startPoint.y : -yStep+startPoint.y,
              } 
              testArr.push(posiDataDemo)
            }


            //算夹角
            function getAngle(x1, y1, x2, y2) {
                // 直角的边长
                var x = Math.abs(x1 - x2);
                var y = Math.abs(y1 - y2);
                // 斜边长
                var z = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                // 余弦
                var cos = y / z;
                // 弧度
                var radina = Math.acos(cos);
                // 角度
                var angle =  180 / (Math.PI / radina);
                return angle;
            }

            var angle=getAngle(startPoint.x,startPoint.y,endPoint.x,endPoint.y);

            // console.log(parseInt(angle))



            //隔一段时间渲染出下一个轨迹
            for(var j=0;j<testArr.length;j++){
             (function(testArr,posiData,j,that,num){
                var timer=setTimeout(()=>{
                  // console.log(num)
                      var markerDemo=L.marker(posiData, {
                            icon: that.gatewayIcon(endPoint.y>startPoint.y,endPoint.x>startPoint.x,parseInt(angle))
                              // icon: L.icon({
                              //   iconUrl: require('../../assets/img/arrow_track.png'),
                              //   iconSize: [30],
                              // })
                        }).addTo(that.map)
                        that.trackMarkers.push(markerDemo)
                  var trackArr = Object.keys(that.trackObj);
                  // console.log(trackArr.length)
                  if(trackArr.length===0){
                    var dataArr=[];
                    dataArr.push(markerDemo)
                    that.trackObj['num'+num]=dataArr;
                  }else{
                    if(that.trackObj.hasOwnProperty('num'+num)){
                      // console.log('已经有了')
                      that.trackObj['num'+num].push(markerDemo)
                    }else{
                        var dataArr=[];
                        dataArr.push(markerDemo)
                        that.trackObj['num'+num]=dataArr;
                    }
                  }
                        
                        if(j===testArr.length-1){
                          
                            setTimeout(()=>{

                                that.trackObj['num'+num].forEach((item,index)=>{
                                    that.map.removeLayer(item)
                                    var key=that.trackMarkers.indexOf(item)
                                    that.trackMarkers.splice(key,1)

                                })
                              
                            },200)
                        }

                },j*500)

                that.trackTimerArr.push(timer)
             })(testArr,testArr[j],j,this,num)
                  
            }



    },
    gatewayIcon(xDir,yDir,angle){
      var toAngle;
      // console.log(xDir,yDir)
      if(xDir&&yDir){
        toAngle=0-angle;
      }else if(!xDir&&yDir){
        toAngle=180+angle;
      }else if(xDir&&!yDir){
        toAngle=angle;
      }
      else if(!xDir&&!yDir){
        toAngle=-180-angle;
      }

       const markerHtmlStyles = `
              transform: rotate(${toAngle}deg);         
            `
          const icon = L.divIcon({
            className: "my-custom-pin",
            iconSize:[24,24],
            iconAnchor: [5, 5],
            html: `<div style="${markerHtmlStyles}"><img style="width:100%;height:100%" src="${require('../../assets/img/arrow_track.png')}"></img></div>`
          })

          return icon
    },

    mouseMarkerFn(obj,posi,info){
       obj.addEventListener('mouseover',(e)=>{
          e.target._icon.src=require('../../assets/img/marker-active.png')
         
          obj.bindPopup(info,{
            closeButton:false,
          }).openPopup();
        })

       obj.addEventListener('mouseout',(e)=>{
          e.target._icon.src=require('../../assets/img/marker.png')
          obj.closePopup();
         
        })
    },
      gateWayMarkerFn(obj,posi,info){
      //  obj.addEventListener('mouseover',(e)=>{
      //     e.target._icon.src=require('../../assets/img/location-active.png')
         
      //     obj.bindPopup(info,{
      //       closeButton:false,
      //     }).openPopup();
           
      //   })

      //  obj.addEventListener('mouseout',(e)=>{
      //     e.target._icon.src=require('../../assets/img/location.png')
      //     obj.closePopup();

      //   })

        obj.addEventListener('click',(e)=>{


            if(obj.deviceAreaOnOff){
                obj.deviceAreaOnOff=false;
                e.target._icon.src=require('../../assets/img/location.png')
                document.getElementById('device_area').getElementsByClassName('device_area_header')[0].innerText='';
                document.getElementById('device_area').getElementsByClassName('device_area_content')[0].innerHTML='';
                document.getElementById('device_area').classList.remove('show');
            }else{
                
                this.markers.forEach((item,index)=>{
                  if(item.deviceAreaOnOff=true){
                      item.deviceAreaOnOff=false;
                      item._icon.src=require('../../assets/img/location.png')
                      document.getElementById('device_area').getElementsByClassName('device_area_header')[0].innerText='';
                      document.getElementById('device_area').getElementsByClassName('device_area_content')[0].innerHTML='';
                      document.getElementById('device_area').classList.remove('show');
                  }
                })
                obj.deviceAreaOnOff=true;
                e.target._icon.src=require('../../assets/img/location-active.png')

                var floorName="data"+this.currentFloor;
                var data=this[floorName]
                // console.log(data)
                var arr=[];
                data.forEach((item,index)=>{
                  if(item.gatewaymac===obj._mac){
                    arr.push(item)
                  }
                })

                arr.forEach((item,index)=>{
                  var deviceAreaItem=document.createElement('div');
                  deviceAreaItem.style.cssText="margin:5px 0;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;"
                  deviceAreaItem.innerText=(index+1)+' - '+item.Brand+' - '+item.AssetName+' - '+item.Mac;
                  document.getElementById('device_area').getElementsByClassName('device_area_content')[0].appendChild(deviceAreaItem)
                })

                document.getElementById('device_area').getElementsByClassName('device_area_header')[0].innerText=info+"附近设备"
                
                document.getElementById('device_area').classList.add('show');
            }
         
        })
    },



    initFloorFn(){

        document.getElementById('moniterItem').addEventListener('click',(e)=>{
            var lis= document.getElementById('moniterItem').getElementsByTagName('li');
            for(var i=0;i<lis.length;i++){
              lis[i].classList.remove('active')
            }
            // this.removeMarkerFn();
            e.target.classList.add('active')
              switch(e.target.getAttribute("data-value")) {
                case '11':
                  this.clearAllDataFn();
                  this.initHomeLayerFn(homeData11);
                  this.initGatewayFn("11");
                  this.currentFloor="11";
                  this.changeZoomFn()
                  
                  break;
                case '12':
                  this.clearAllDataFn();
                  this.initHomeLayerFn(homeData12);
                  this.initGatewayFn("12");
                  this.currentFloor="12";
                  this.changeZoomFn()
                  
                  break;
                case '13':
                  this.clearAllDataFn();
                  this.initHomeLayerFn(homeData13);
                  this.initGatewayFn("13");
                  this.currentFloor="13";
                  this.changeZoomFn()
                  
                  break;
                default:
                  break;
              }
        })
    },

    removeMarkerFn(){
      for(var i=0;i<this.markers.length;i++){
        this.map.removeLayer(this.markers[i])
      }
    },
    
    removeDeviceMarkerFn(){
      for(var i=0;i<this.deviceMarkers.length;i++){
        this.map.removeLayer(this.deviceMarkers[i])
      }
      this.deviceMarkers=[]
    },

    removeTrackMarkerFn(){
      for(var i=0;i<this.trackMarkers.length;i++){
        this.map.removeLayer(this.trackMarkers[i])
      }
      this.trackMarkers=[]
    },

    clearAllDataFn(){
      this.homeLayers.clearLayers()
      this.removeMarkerFn();
      this.removeDeviceMarkerFn();
      this.removeTrackMarkerFn();
      this.trackTimerArr.forEach((item,index)=>{
        clearTimeout(item)
      })
      this.mapData=[]
      this.data11=[]
      this.data12=[]
      this.data13=[]
      this.markers=[];

      document.getElementById('device_area').getElementsByClassName('device_area_header')[0].innerText='';
      document.getElementById('device_area').getElementsByClassName('device_area_content')[0].innerHTML='';
      document.getElementById('device_area').classList.remove('show');
    },

    removeFloorData(data){
      for(var k=0;k<this.gatewayFloorData.length;k++){

        for(var j=0;j<this.gatewayFloorData[k].gateway.length;j++){
          if(data._mac===this.gatewayFloorData[k].gateway[j].mac){

              switch(this.gatewayFloorData[k].floor){
                case "11":
                  this.data11.forEach((item,index)=>{
                    if(item.Mac===data._deviceMac){
                      this.data11.splice(index,1);
                    }
                  })
                  break;

                case "12":
                  this.data12.forEach((item,index)=>{
                    if(item.Mac===data._deviceMac){
                      this.data12.splice(index,1);
                    }
                  })
                  break;
                case "13":
                  this.data13.forEach((item,index)=>{
                    if(item.Mac===data._deviceMac){
                      this.data13.splice(index,1);
                    }
                  })
                  break;
                
                default:
                  break;
              }
          }
        }
      };

    },

    showAllMarkerFn(){
      var floorName="data"+this.currentFloor;
      var data=this[floorName]

          if(data.length>0){
                  
             this.showMarkerFn(data)

          }
    },

    showMarkerFn(data){
      // console.log(data)
      for(var j=0;j<data.length;j++){
              for(var i=0;i<this.gatewayData.length;i++){
                    if(this.gatewayData[i].mac===data[j].gatewaymac){
                        var xo=this.gatewayData[i].posi[0]
                        var yo=this.gatewayData[i].posi[1];
                        var R=0.01;
                        var xp=Math.random()
                        var yp=Math.random()

                        var x=xo-Math.cos(xp*Math.PI)*R

                        if(yp*10>5){
                          var y=yo+Math.sin(yp*Math.PI)*R
                        }else{
                          var y=yo-Math.sin(yp*Math.PI)*R
                        }

                        var posiData={
                          lat:x,
                          lng:y,
                        }

                      
                        var marker=L.marker(posiData, {
                          icon: L.icon({
                            iconUrl: require('../../assets/img/marker.png'),
                            iconSize: [16],
                          })
                        }).addTo(this.map)
                        marker._mac=this.gatewayData[i].mac
                        marker._deviceMac=data[j].Mac
                        marker._name=data[j].Brand+"-"+data[j].AssetName+"-"+data[j].Mac
                        this.deviceMarkers.push(marker);
                        this.mouseMarkerFn(marker,posiData,marker._name)
                        
                    }
              }
          
        }
    },

    addArrFn(beforeArr,afterArr){
      var arr=[]
        for(var i=0;i<afterArr.length;i++){
          
          var k=beforeArr.find((item)=>{
            return item.Mac===afterArr[i].Mac
          })
            
            if(!k){
              arr.push(afterArr[i])
            }
            
        }
        //拿到新增数据
        return arr;
    },

    reduceArrFn(beforeArr,afterArr){
       var arr=[]
        for(var i=0;i<beforeArr.length;i++){
          
          var k=afterArr.find((item)=>{
            return item.Mac===beforeArr[i].Mac
          })
            
            if(!k){
              arr.push(beforeArr[i])
            }

        }
        //拿到新增数据
        return arr;
    },

    commonArrFn(beforeArr,afterArr){
        var commonArr=[];
        for(var i=0;i<afterArr.length;i++){
          for(var k=0;k<beforeArr.length;k++){
            if(afterArr[i].Mac===beforeArr[k].Mac){
              commonArr.push(afterArr[i])
            }
          }
        }
        return commonArr;
    },

    refreshArrFn(commonArr,initArr){
      var refreshArr=[];
      for(var i=0;i<commonArr.length;i++){
        for(var k=0;k<initArr.length;k++){
          if(commonArr[i].Mac===initArr[k].Mac&&commonArr[i].gatewaymac!==initArr[k].gatewaymac){
            refreshArr.push(commonArr[i])
          }
        }
      }
      return refreshArr
    },

    filterFloorFn(data){
      for(var k=0;k<this.gatewayFloorData.length;k++){

        for(var j=0;j<this.gatewayFloorData[k].gateway.length;j++){

          if(data.gatewaymac===this.gatewayFloorData[k].gateway[j].mac){
              
              // this['data'+this.gatewayFloorData[k].floor].push(data)

              switch(this.gatewayFloorData[k].floor){
                case "11":
                  this.data11.push(data);
                  break;

                case "12":
                  this.data12.push(data);
                  break;
                case "13":
                  this.data13.push(data);
                  break;
                
                default:
                  break;
              }
          }else{
            // console.log('都不对')
            // return;
          }
        }
      };

    },

    currentFloorFn(data){
        for(var k=0;k<this.gatewayFloorData.length;k++){
          for(var j=0;j<this.gatewayFloorData[k].gateway.length;j++){
            if(data.gatewaymac===this.gatewayFloorData[k].gateway[j].mac){
                if(this.gatewayFloorData[k].floor===this.currentFloor){
                  var arr=[];
                  arr.push(data)
                  this.showMarkerFn(arr)
                }
            }
          }
        };
    },

    transWebsocketFn(data){
          let dataArr=[];
          //注意：长连接我们是后台直接1秒推送一条数据
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
    

     //初始化weosocket
        initWebSocket(){ 

            // this.websock = new WebSocket("ws://192.168.0.54:8009/map/websocket");
            this.websock = new WebSocket("ws://47.104.99.230:8080/map/websocket");
            this.websock.onopen = this.websocketonopen;
            this.websock.onerror = this.websocketonerror;
            this.websock.onmessage = this.websocketonmessage;
            this.websock.onclose = this.websocketclose;

        },

        initDeviceDataFn(dataArr){
          // console.log(dataArr)
           if(dataArr.length){
             //如果地图已经加载过一次已经拿到过数据后
                if(this.mapData.length){
                      var addDevices=this.addArrFn(this.mapData,dataArr);
                      var reduceDevices=this.reduceArrFn(this.mapData,dataArr);
                      var commonDevices=this.commonArrFn(this.mapData,dataArr);
                      var refreshDevices=this.refreshArrFn(commonDevices,this.mapData)

                      //需要更新的数据也需要先删除后添加，所以也合并进需要增加和删除的数据统一操作
                      var reduceConcat=reduceDevices.concat(refreshDevices);
                      var addConcat=addDevices.concat(refreshDevices);

                      //如果有需要更新的数据，先判断变化的网关是否在同一层，是的话在地图上画出轨迹
                       if(refreshDevices.length){
                        refreshDevices.forEach((item,index)=>{
                          this.mapData.forEach((m,n)=>{
                            if(item.Mac===m.Mac){
                              var startMac=m.gatewaymac;
                              var endMac=item.gatewaymac;
                              var startFloor=''
                              var endFloor=''
                              var startPosi=[]
                              var endPosi=[]
                              this.gatewayData.forEach((a,b)=>{
                                if(a.mac===startMac){
                                    startFloor=a.floor;
                                    startPosi=a.posi;
                                }else if(a.mac===endMac){
                                    endFloor=a.floor;
                                    endPosi=a.posi;
                                }
                              })
                              // console.log(index,item.Mac,'从'+startMac+'到'+endMac,new Date().getMinutes(),new Date().getSeconds())
                              if(startFloor===endFloor&&endFloor===this.currentFloor){
                                  // console.log(this.data11)
                                  this.setTrackFn({
                                    x:startPosi[0],
                                    y:startPosi[1],
                                  },{
                                    x:endPosi[0],
                                    y:endPosi[1],
                                  },index)
                              }

                            }
                          })
                        })
                      }
                                
                      //有需要移除掉的标记就从这里移除
                      if(reduceConcat.length){
                        for(var i=0;i<reduceConcat.length;i++){
                              for(var j=0;j<this.deviceMarkers.length;j++){
                                if(this.deviceMarkers[j]._deviceMac===reduceConcat[i].Mac){
                                  this.map.removeLayer(this.deviceMarkers[j])
                                  this.removeFloorData(this.deviceMarkers[j]);
                                  this.deviceMarkers.splice(j,1)
                                }
                              }

                              this.mapData.forEach((item,index)=>{
                                if(item.Mac===reduceConcat[i].Mac){
                                  this.mapData.splice(index,1)
                                }
                              })
                        }
                      }

                      

                      //有需要增加的标记从这里新增
                      if(addConcat.length){
                        for(var k=0;k<addConcat.length;k++){
                              this.filterFloorFn(addConcat[k])
                              this.currentFloorFn(addConcat[k])
                        }
                        
                        this.mapData=this.mapData.concat(addConcat)
                      }

                     

                }else{
                  this.mapData=dataArr;
                    for(var i=0;i<this.mapData.length;i++){
                      this.filterFloorFn(this.mapData[i])
                    }
                    this.showAllMarkerFn();
                }
           }else{
             return
           }
        },

        authorityFn(){
            //判断角色类型，为表格添加选择项
            if(this.$store.state.role==="admin"){
                this.deviceDetailColumn.unshift(
                  {
                    title:'选择',
                    slot:'selection',
                    type: 'selection',
                  },
                )
            }else{

            }
        },

        websocketonopen() {
            console.log("WebSocket连接成功");
        },
        websocketonerror(e) {
            console.log("WebSocket连接发生错误");
        },
        websocketonmessage(e){
            // console.log(e.data)
            var dataArr=this.transWebsocketFn(e.data)

            // console.log(dataArr)
            // dataArr.forEach((item)=>{
            //   if(item.Mac==="C2021A00015F"){
            //     console.log(item,new Date().getMinutes(),new Date().getSeconds())
            //   }
            // })

            if(L){
                    this.initDeviceDataFn(dataArr)
            }else{
              return
            }
              
        },


        websocketclose(e){
          console.log("WebSocket关闭");
        },

        getTestFn(){
          var dataArr=[
                      {
                        "Brand": "A牌",
                        "Type": "大",
                        "gatewaymac": "AC233FC038FF",
                        // "gatewaymac": "AC233FC038D3",
                        
                        "AssetName": "戊类",
                        "Mac": "C2021A0000EA"
                      },
                      {
                        "Brand": "A牌",
                        "Type": "大",
                        "gatewaymac": "AC233FC038D3",
                        
                        "AssetName": "戊类",
                        "Mac": "C2021A0000EB"
                      },

                    ]
                  var dataArr1=[
                      {
                        "Brand": "A牌",
                        "Type": "大",
                        
                        // "gatewaymac": "AC233FC037A3",
                        // "gatewaymac": "AC233FC038D3",
                         "gatewaymac": "AC233FC03789",
                        "AssetName": "戊类",
                        "Mac": "C2021A0000EA"
                      },
                      {
                        "Brand": "A牌",
                        "Type": "大",
                        // "gatewaymac": "AC233FC03789",
                        "gatewaymac": "AC233FC038D3",
                        "AssetName": "戊类",
                        "Mac": "C2021A0000EB"
                      },

                    ]
                    setTimeout(()=>{
                      this.initDeviceDataFn(dataArr1)
                    },1000)
                  this.initDeviceDataFn(dataArr)
        }
    
    
  },
  created(){

   this.authorityFn();

  },

  mounted(){

     this.$nextTick(()=>{
          leafLetScript()
          .then(()=>{
             proj4ComScript()
             .then(()=>{
                proj4LeafLetScript()
               .then(()=>{
                 var gatewayFloorData=[]

                //将没按楼层分组的数据分成一个按楼层分组的数组
                 gatewayData.forEach((m,n)=>{
                  if(gatewayFloorData.length===0){
                      var data1={
                        floor:m.floor,
                        gateway:[],
                      };
                      data1.gateway.push(m);
                      gatewayFloorData.push(data1);
                  }else{
                      var isHave=gatewayFloorData.find((item,index)=>{
                        return item.floor===m.floor
                      })
                      if(isHave){
                        isHave.gateway.push(m);
                      }else{
                            var data2={
                              floor:m.floor,
                              gateway:[],
                            };
                            data2.gateway.push(m);
                            gatewayFloorData.push(data2);
                      }
                  }
                 

                 })

                 this.gatewayFloorData=gatewayFloorData;

                  this.initMapFn();
                  this.initWebSocket()
                  // this.getTestFn()
                  
               })
                
             })
             
          })

          
      })
    
    //发请求获取数据
    this.getDataFn()
  },

  beforeDestroy(){
      this.$Loading.finish();
      //页面销毁时关闭长连接
      this.websocketclose();
  }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/table.css"></style>
<style scoped>
   @import "../../assets/css/device_manage.css"

</style>

<template>
  <div class='device_manage_wrap'>
      <div :class="foldArr[1] ? 'hover_animat custom_bg_color_white fold_wrap fold' : 'hover_animat custom_bg_color_white fold_wrap'" >
        <div class="row_box">
          <h2 class="row_title">地图</h2>
          <div class="fold_box">
              <div class="split_wrap clearfix">
                    <div  class="split_wrap_left">
                        <div class="map_area_wrap">
                            <div v-show="!isShowMap" class="map_loading_notice">
                              <h3>地图加载中...</h3>
                            </div>
                            <div v-show="isShowMap" id="mapID">
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
                                <div class="map_tools_wrap">
                                  <div class="map_tools_item">
                                      <span class="map_tools_text">是否显示网关</span>
                                      <i-switch size="large" true-color="#19be6b" v-model="isShowGateway" @on-change="showGatewayChangeFn">
                                          <span slot="open">显示</span>
                                          <span slot="close">隐藏</span>
                                      </i-switch>
                                  </div>
                                </div>
                                <div id="device_area" v-show=deviceModalData.show>
                                  <div class="device_area_header">
                                      {{deviceModalData.gatewaymac}} 附近设备
                                  </div>
                                  <div class="device_area_body">
                                      <div class="device_area_content" v-if="deviceModalData.deviceList.length">
                                        <p v-for="(item,index) in deviceModalData.deviceList">
                                          {{index+1}} - {{item.generalName}} - {{item.type}} - {{item.brand}} - {{item.assetName}}
                                        </p>
                                      </div>
                                      <div v-else style="text-align:center" class="device_area_notice"> 
                                        <p>暂无数据！</p>
                                      </div>
                                  </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div  class="split_wrap_right">
                        <div class="device_manage_tools_wrap">
                           <div class="row_box">
                              <div class="device_manage_tools_item">
                                  <h3 >资产筛选模式</h3>
                                  <i-switch v-model="isQueryMode" @on-change="querySwitchChange">
                                      <span slot="open">开</span>
                                      <span slot="close">关</span>
                                  </i-switch>
                                  <div class="device_manage_tools_item_box" v-show="isQueryMode">
                                      <div class="form_wrap">
                                        <div class="form_item">
                                            <Row :gutter="20">
                                                <Col span="12">
                                                  <h4>资产编号</h4>
                                                  <Input v-model="queryData.assetID" placeholder="资产编号" />
                                                  
                                                </Col>
                                                <Col span="12">
                                                  <h4>资产名称</h4>
                                                  <Input v-model="queryData.assetName" placeholder="资产名称" />
                                                </Col>
                                            </Row>
                                        </div>
                                        <div class="form_item">
                                            <Row :gutter="20">
                                                <Col span="12">
                                                  <h4>通用名称</h4>
                                                  <Input v-model="queryData.generalName" placeholder="通用名称" />
                                                  
                                                </Col>
                                                <Col span="12">
                                                  <h4>状态</h4>
                                                  <Select v-model="queryData.status">
                                                      <Option v-for="item in statusData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                                  </Select>
                                                </Col>
                                            </Row>
                                        </div>
                                        <div class="form_item">
                                            <Row :gutter="20">
                                                <Col span="12">
                                                  <h4>标签mac</h4>
                                                  <Input v-model="queryData.mac" placeholder="标签mac" />
                                                  
                                                </Col>
                                               
                                            </Row>
                                        </div>
                                      </div>
                                  </div>
                                  <div v-show="isQueryMode" class="submit_btn_wrap">
                                        <Button type="primary" shape="circle" @click="searchQueryModeFn">搜索</Button>
                                        <Button type="warning" shape="circle" @click="checkQueryModeFn" :disabled="queryCheckBtn.disabled">盘点</Button>
                                        <Button type="error" shape="circle" @click="clearQueryModeAndSearchFn">清空</Button>
                                  </div>
                              </div>
                               <Divider />
                              <div class="device_manage_tools_item">
                                  <h3 >房间筛选模式（开启后点击地图上房间可筛选对应房间的数据）</h3>
                                  <i-switch v-model="isSelectMode" @on-change="selectSwitchChange">
                                      <span slot="open">开</span>
                                      <span slot="close">关</span>
                                  </i-switch>
                                  <div  class="device_manage_tools_item_box" v-show="isSelectMode">
                                    <p style="margin-bottom:10px">已选择房间：</p>
                                    <Tag v-for="(item,index) in selectRoomArr" :key="index"  closable @on-close="closeSelectRoomFn(item.$properties.NO)">{{item.$properties.NAME}}</Tag>
                                  </div>
                                  <div v-show="isSelectMode"  class="submit_btn_wrap">
                                        <Button type="primary" shape="circle" @click="searchSelectModeFn">搜索</Button>
                                        <Button type="warning" shape="circle" @click="checkSelectModeFn">盘点</Button>
                                        <Button type="error" shape="circle" @click="clearSelectModeAndSearchFn">清空</Button>
                                  </div>
                              </div>
                             
                           </div>
                        </div>
                      
                    </div>
              </div>

           </div>
        </div>
        <div class="fold_icon_wrap ">
            <div class="fold_icon_box" @click="foldFn(1)">
              <Icon v-if="!foldArr[1]" type="ios-arrow-up"/>
              <Icon v-else type="ios-arrow-down" />
            </div>
        </div>
      </div>


      <div class="row_wrap hover_animat custom_bg_color_white">
        <div class="row_box">
          <h2 class="row_title">设备列表</h2>
        
          <div class="table_scroll_wrap">
              <Table style="min-width:1600px" stripe :columns="deviceDetailColumn" :data="currentPageData" size='large' :loading="tableLoading">
                  <template slot-scope="{row}" slot="assetID">
                    <a @click="toDetailFn(row.assetID)">{{row.assetID}}</a>
                  </template>

                  <template slot-scope="{ row }" slot="status">
                      <Tag v-if="row.status==='正常'" :color="statusColor[0]" ><span>{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='待维修'" :color="statusColor[1]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='维修接单'" :color="statusColor[2]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='待报废'" :color="statusColor[3]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='报废'" :color="statusColor[4]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='外借'" :color="statusColor[5]" ><span>{{row.status}}</span></Tag>
                      <Tag v-else  color="default"><span>{{row.status}}</span></Tag>
                  </template>

                  <!-- <template slot-scope="{ row }" slot="operation">
                      <Button shape="circle" icon="md-link" @click="toTrackFn(row.assetID)">轨迹</Button>
                  </template> -->
                
              </Table>
          </div>
          <div class="data_table_pagination_wrap">
              <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
          </div>
      </div>
    </div>

    <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
        
        <div style="text-align:center;padding:20px 0">
            <Spin fix v-if="!achieveUpload&&!errorUpload">
                <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                <div>正在盘点</div>
            </Spin>
            <Spin fix v-if="achieveUpload" style="color:#00ad19">
                <Icon type="ios-checkmark-circle" size=18 />
                <div>盘点成功</div>
            </Spin>
            <Spin fix v-if="errorUpload" style="color:#f72b2b">
                <Icon type="ios-close-circle" size=18 />
                <div>盘点失败</div>
            </Spin>

        </div>
        
    </Modal>

  </div>
</template>

<script>

import Vue from "vue"
import {Row,Col,Icon,CheckboxGroup,Checkbox,Select,Option,Table ,Tag ,Page,Input,Button,Modal,Spin,Switch,Divider} from 'iview';
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('CheckboxGroup', CheckboxGroup);
Vue.component('Checkbox', Checkbox);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
Vue.component('i-switch',Switch);
Vue.component('Divider',Divider);
// 这里加入了测试办公室数据 baseData4和homeDataT1
import {baseData1,baseData2,baseData3,homeData11,homeData12,homeData13,homeDataB1,homeDataB2,baseData4,homeDataT1} from "../../../assets/js/leaflet/homeData.js"
import {leafLetScript,proj4ComScript,proj4LeafLetScript,leafletAntPath,leafletFullScreen} from "../../../utils/needScript.js"

import NET_PORT from "../../../api/port.js"

import { getToken } from '../../../utils/auth.js';
import initTimeFn from "../../../utils/initTime.js"
import formatScanCodeFn from "../../../utils/formatScanCode.js"
import statusData from "../../../assets/data/status.js"
import gatewayTest from "../../../test/gatewayTestNo.js"
import {strStart,strEnd} from "../../../test/deviceTest.js"
import deviceData400 from "../../../test/deviceTest400.js"
import statusColor from "../../../assets/data/statusColor.js"
export default {
  name:'device_manage',
  components:{},
  props:{},
  data(){
    return {
      statusColor:statusColor,
      user:this.$store.state.user,
      foldArr:[false,false],
      isShowMap:false,
      isShowGateway:false,
      isSelectMode:false,
      isQueryMode:false,

      changePageMode:false,

      totalCount: 0,
      pageSize: 10,
      currentPage: 1,

      queryCheckBtn:{
        disabled:true,
        isLoading:false,
      },

      queryData:{
        assetID:'',
        assetName:'',
        generalName:'',
        status:'',
        mac:'',
      },

      queryDataNow:{
        assetID:'',
        assetName:'',
        generalName:'',
        status:'',
        mac:'',
      },

      statusData:statusData,

      deviceModalData:{
        show:false,
        gatewaymac:'',
        deviceList:[],
      },

      gatewayData:[],
      gatewayFloorData:[],

      uploadModal:false,
      achieveUpload:false,
      errorUpload:false,

      websock:null,

      deviceDetailColumn:[
          
            {
                title: '资产编号',
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
                title: '坐落位置',
                key: 'location',
            },
            {
                title: '数量',
                key: 'amount',
            },
            {
                title: '单位',
                key: 'unit'
            },
            {
                title: '标签mac',
                key: 'mac'
            },
            {
                title: '所属网关',
                key: 'gatewaymac'
            },
            {
                title: '入库时间',
                key: 'buyDate',
            },
            {
                title: '原值',
                key: 'money',
            },
            {
                title: 'SN',
                key: 'sn',
            },
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:120
            },
            // {
            //     title: '操作',
            //     key: 'operation',
            //     slot:'operation',
            //     width:120
            // },
        
      ],

      map:null,
      CRS_4549:null,
      baseLayers:null,
      homeLayers:null,
      zoomKey:null,
      currentFloor:'11',
      gatewayMarkerArr:[],
      deviceMarkerArr:[],

      allFloorDeviceData:[],
      currentFloorDeviceData:[],
      currentPageData:[],
      currentRoomArr:[],
      
      trackGatewayArr:[],
      trackLineArr:[],


      selectRoom:{},
      selectRoomArr:[],
      selectRoomArrNow:[],

      tableLoading:true,
    }
  },
  watch:{

  },
  computed:{
 
  },
  
  methods:{

    setCurrentPageFn(){
        let arr=[];
        if(this.currentFloorDeviceData.length){
          // 如果当前停留在超过数据条数得显示页码上，回到第一页
            if(((this.currentPage-1)*this.pageSize)>this.currentFloorDeviceData.length){
              this.currentPage=1;
            }
            let startIndex=this.pageSize*(this.currentPage-1)
            let endIndex=((this.pageSize*this.currentPage)-1) >this.currentFloorDeviceData.length-1 ? this.currentFloorDeviceData.length-1 : ((this.pageSize*this.currentPage)-1)
            for(let i=startIndex;i<=endIndex;i++){
              arr.push(this.currentFloorDeviceData[i])
            }
            this.totalCount=this.currentFloorDeviceData.length;
            this.currentPageData=arr;
            if(this.tableLoading){
              this.tableLoading=false;
            }
        }else{
          this.totalCount=0;
          this.currentPage=1;
          this.currentPageData=[]
          if(this.tableLoading){
            this.tableLoading=false;
          }
        }
    },

    querySwitchChange(){
      if(this.isQueryMode){
          this.isSelectMode=false;
          // 清除房间筛选的数据
          this.clearSelectModeFn();
      }else{
         this.clearQueryModeFn();
      }

    },


    searchQueryModeFn(){
        this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
        this.queryCheckBtn.isLoading=true;
        this.queryCheckBtn.disabled=true;
        this.tableLoading=true;

        setTimeout(()=>{
          this.setDeviceDataFn(this.currentFloorDeviceData)
        },300)

    },

    clearQueryModeAndSearchFn(){
      this.clearQueryModeFn();
      this.tableLoading=true;
    },

    clearQueryModeFn(){
        // this.isQueryMode=false;
        this.queryData={
          assetID:'',
          assetName:'',
          generalName:'',
          status:'',
          mac:'',
        }
        this.queryDataNow={
          assetID:'',
          assetName:'',
          generalName:'',
          status:'',
          mac:'',
        }
        this.queryCheckBtn={
          disabled:true,
          isLoading:false,
        }
        // this.tableLoading=true;
    },

    selectSwitchChange(){
      if(this.isSelectMode){
        this.isQueryMode=false;
        this.clearQueryModeFn();
      }else{
        this.clearSelectModeFn();
      }
    },

     //点击搜索房间数据
    searchSelectModeFn(){
      //拿到选择得房间得数据
      let arr=[];
      this.selectRoomArr.forEach((item,index)=>{
        arr.push(item.$properties);
      })
      this.selectRoomArrNow=JSON.parse(JSON.stringify(arr))
      this.queryCheckBtn.isLoading=true;
      this.queryCheckBtn.disabled=true;
      this.tableLoading=true;
      setTimeout(()=>{
        this.setDeviceDataFn(this.currentFloorDeviceData)
      },300)
    },

    clearSelectModeAndSearchFn(){
      this.clearSelectModeFn();
      this.tableLoading=true;
    },

    clearSelectModeFn(){
       this.selectRoomArr.forEach((item,index)=>{
         item.$geojson.setStyle(item.$style)
       })
       this.selectRoomArr=[];
       this.selectRoomArrNow=[]
    },

    closeSelectRoomFn(NO){
       this.selectRoomArr.forEach((item,index)=>{
         if(item.$properties.NO===NO){
           item.$geojson.setStyle(item.$style)
           this.selectRoomArr.splice(index,1)
         }
       })
       if(this.selectRoomArr.length===0){
         this.selectRoomArrNow=[]
       }
       
    },

    showGatewayChangeFn(key){
      if(key){
          this.initGatewayFn();
      }else{
          for(let i=0;i<this.gatewayMarkerArr.length;i++){
            this.map.removeLayer(this.gatewayMarkerArr[i])
          }
          this.gatewayMarkerArr=[];
          this.deviceModalData.show=false;
      }
    },

    foldFn(index){
      this.$set( this.foldArr,index, !this.foldArr[index])
    },

    checkQueryModeFn(){
      if(this.currentFloorDeviceData.length){
          this.$Modal.confirm({
                title:'盘点提示！',
                okText:'确定',
                cancelText:'取消',
                content:'<h2 style="color:#f94040">你确定要盘点这'+ this.currentFloorDeviceData.length+'个设备吗？</h2>',
                onOk:()=>{
                      this.checkQueryCertainFn();
                },
          })
      }else{
          this.$Message.error({
              content:"没有符合条件的设备数据！",
              duration:2,
          })
      }
    },

    checkQueryCertainFn(){
        let datas=[]
        this.currentFloorDeviceData.forEach((item,index)=>{
          let obj={
            "assetID":item.assetID,
          };

          datas.push(obj);
        })

        this.uploadModal=true;
        this.$axios.post(NET_PORT.assetsCheck+"checkpep="+this.user,datas)
        .then((res)=>{
          if(res.data.code===0){
            this.successFn();
          }else{
            this.errorFn();
          }
        })
        .catch((error)=>{
            Modal.error({
                title: '错误提示！',
                content: error,
                okText: '确定',
                
            })
        })
    },

    checkSelectModeFn(){
        if(this.isSelectMode&&this.selectRoomArr.length){
            this.$Modal.confirm({
                title:'盘点提示！',
                okText:'确定',
                cancelText:'取消',
                content:'<h2 style="color:#f94040">你确定要盘点这'+this.selectRoomArr.length+'个房间的设备吗？</h2>',
                onOk:()=>{
                    this.checkSelectCertainFn();
                },
            })
        }else{
          return this.$Message.error({
              content:"请选择至少一个房间",
              duration:2,
          })
        }
    },

    checkSelectCertainFn(){
          let datas=[];
          this.selectRoomArr.forEach((item,index)=>{
            datas.push({
              cadMapRoomName:item.$properties.NAME,
            });
          })

          this.uploadModal=true;
          this.$axios.post(NET_PORT.assetsCheck+"checkpep="+this.user,datas)
          .then((res)=>{
            if(res.data.code===0){
              this.successFn();
            }else{
              this.errorFn();
            }
          })
          .catch((error)=>{
             Modal.error({
                  title: '错误提示！',
                  content: error,
                  okText: '确定',
                  
              })
          })

    },


    successFn(){
        this.achieveUpload=true;
        setTimeout(()=>{
            this.uploadModal=false;
            setTimeout(()=>{
                this.achieveUpload=false;
                this.errorUpload=false;
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


     //点击状态去到不同页面
    statusFn(status){
      if(status==='正常'){
        this.$router.push('/web/assets_manage');
      }else {
        this.$router.push('/web/damage_manage');
      }
    },


    //去到设备明细页面
    toDetailFn(id){
      this.$router.push({
        path:'/web/assets_detail/'+id
      })

    },

    toTrackFn(id){
      this.$router.push({
        path:'/web/track_detail/'+id
      })
    },


    initMapFn(){
          this.homeLayers = new L.featureGroup();
          this.baseLayers = new L.featureGroup();
          this.gatewayMarkerArr=[]
          const resolutions = [
            156367.7919628329, 78183.89598141646, 39091.94799070823, 19545.973995354114, 9772.986997677057, 4886.4934988385285, 2443.2467494192642, 1221.6233747096321, 610.8116873548161, 305.40584367740803, 152.70292183870401, 76.35146091935201, 38.175730459676004, 19.087865229838002, 9.543932614919001, 4.7719663074595005, 2.3859831537297502, 1.1929915768648751, 0.5964957884324376, 0.2982478942162188
          ];
          this.CRS_4549 = new L.Proj.CRS('EPSG:4549',
            '+proj=tmerc +lat_0=0 +lon_0=120 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs', {
              resolutions: resolutions
            }
          );

          let center = [-4.9002, 139.2102];
          const mybounds = L.latLngBounds(L.latLng(-4.279435, 140.075537), L.latLng(-5.438369, 138.29733));
          this.map = L.map('mapID', {
            fullscreenControl: true,
            fullscreenControlOptions: {
              position: 'topleft'
            },
            center: center,
            layers: [this.baseLayers,this.homeLayers],
            zoom: 10,
            maxZoom: 14,
            minZoom: 10,
            attributionControl: false,
            doubleClickZoom: false,
            maxBounds: mybounds,
            crs: this.CRS_4549 // 定义的坐标系
          });
         

          this.initBoundaryFn(baseData1);
          this.initHomeLayerFn(homeData11);
          this.initGatewayFn();
          this.switchFloorFn();
          this.initZoomFn();
          this.changeZoomFn();
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



  isInPolygon(checkPoint, polygonPoints) {
      let counter = 0;
      let i;
      let xinters;
      let p1, p2;
      let pointCount = polygonPoints.length;
      p1 = polygonPoints[0];

      for (i = 1; i <= pointCount; i++) {
          p2 = polygonPoints[i % pointCount];
          if (
              checkPoint[0] > Math.min(p1[0], p2[0]) &&
              checkPoint[0] <= Math.max(p1[0], p2[0])
          ) {
              if (checkPoint[1] <= Math.max(p1[1], p2[1])) {
                  if (p1[0] != p2[0]) {
                      xinters =
                          (checkPoint[0] - p1[0]) *
                              (p2[1] - p1[1]) /
                              (p2[0] - p1[0]) +
                          p1[1];
                      if (p1[1] == p2[1] || checkPoint[1] <= xinters) {
                          counter++;
                      }
                  }
              }
          }
          p1 = p2;
      }
      if (counter % 2 == 0) {
          return false;
      } else {
          return true;
      }
  },

    createRoomFn(feature,color){
      //渲染房间
       let $geojson = L.Proj.geoJson(feature, {
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

       

        //渲染房间名
        let getBounds = $geojson.getBounds();
        let lat = (getBounds._northEast.lat - getBounds._southWest.lat) / 2 + getBounds._southWest.lat;
        let lng = (getBounds._northEast.lng - getBounds._southWest.lng) / 2 + getBounds._southWest.lng;


        let htmlTEXT = '<div class="showNAME" style="z-index:600">' + feature.properties.NAME + '</div>';
        let htmlMarker2 = L.marker([lat, lng], {
          icon: L.divIcon({
            className: 'leaflet-echart-icon',
            html: htmlTEXT
          }),
          pro: feature.properties
        }).addTo(this.homeLayers);

        //点击房间高亮
        $geojson.addEventListener('click',(e)=>{
          
          // 用来打点定坐标
          // let popInfo = "X:" + e.latlng.lat.toFixed(6) + "</br>Y:" + e.latlng.lng.toFixed(6);
          // console.log("X:                 " + e.latlng.lat.toFixed(6))
          // console.log("Y:                 " + e.latlng.lng.toFixed(6))
          // var popup=L.popup({
          //     minWidth: 120,
          //     minHeight: 100, 
          //     closeButton:true,
          // }).setLatLng(e.latlng).setContent(popInfo).openOn(this.map);

          if(this.isSelectMode){
              //拿到房间信息
              this.selectRoom=e.layer.feature.properties;
              let isHave=this.selectRoomArr.find((item,index)=>{
                return item.$properties.NO===this.selectRoom.NO;
              })
              if(!isHave){
                //先将高亮房间存起来
                this.selectRoomArr.push({
                  $geojson:$geojson,
                  $properties:this.selectRoom,
                  $style:{
                      color: "#fff",
                      opacity: 1,
                      weight: 2,
                      fillColor: color,
                      fillOpacity: 1,
                  }
                });

                //设置高亮样式
                $geojson.setStyle({
                    color: "#FFFFFF",
                    opacity: 1,
                    weight: 2,
                    fillColor: '#ffd100',
                    fillOpacity: 1,
                })
              }else{
                //将房间恢复原本样式
                $geojson.resetStyle(e.layer)
                //从高亮房间数组中移除
                this.selectRoomArr.forEach((item,index)=>{
                  if(item.$properties.NO===this.selectRoom.NO){
                    this.selectRoomArr.splice(index,1)
                  }
                })
              }
          }

        })
      
    },

    initHomeLayerFn(homeData){
        this.currentRoomArr=[];
        L.Proj.geoJson(homeData, {
          onEachFeature: (feature)=>{
            feature["crs"] = {
              "type": "name",
              "properties": {
                "name": "EPSG:4549"
              }
            };
            let color = this.initColorFn(feature.properties.TYPE)
            this.createRoomFn(feature,color)
            this.currentRoomArr.push(feature);
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

    //显示网关标记
    initGatewayFn(){
      //开启显示网关按钮才能渲染网关图标
      if(this.isShowGateway){
         for(let i=0;i<this.gatewayFloorData.length;i++){
            if(this.gatewayFloorData[i].floor===this.currentFloor){
                for(let k=0;k<this.gatewayFloorData[i].gateway.length;k++){
                    let posiData={
                      lat:Number(this.gatewayFloorData[i].gateway[k].posi[0]),
                      lng:Number(this.gatewayFloorData[i].gateway[k].posi[1]),
                    }
                    let marker;
                    if(this.gatewayFloorData[i].gateway[k].status==='在线'){
                      marker=L.marker(posiData, {
                        icon: L.icon({
                          iconUrl: require('../../../assets/img/location.png'),
                          iconSize: [30],
                        })
                      }).addTo(this.map)
                    }else{
                      marker=L.marker(posiData, {
                      icon: L.icon({
                          iconUrl: require('../../../assets/img/location-offline.png'),
                          iconSize: [30],
                        })
                      }).addTo(this.map)
                    }
                    marker.setZIndexOffset(10000);
                    marker.customAttribute={
                      gatewaymac:this.gatewayFloorData[i].gateway[k].gatewaymac,
                      status:this.gatewayFloorData[i].gateway[k].status,
                    }
                    this.gatewayMarkerArr.push(marker)
                    this.clickGateWayMarkerFn(marker)
                }
            }
        };
      }
    },

    clickGateWayMarkerFn(obj){
        obj.addEventListener('click',(e)=>{
          if(!this.deviceModalData.show){
            this.deviceModalData.show=true;
            e.target._icon.src=require('../../../assets/img/location-active.png')
            this.deviceModalData.gatewaymac=obj.customAttribute.gatewaymac;
            let data=this.allFloorDeviceData;
            let arr=[];
            data.forEach((item,index)=>{
              if(item.gatewaymac===obj.customAttribute.gatewaymac){
                arr.push(item)
              }
            })

             this.deviceModalData.deviceList=arr;
          }else{
            if(this.deviceModalData.gatewaymac===obj.customAttribute.gatewaymac){
              this.deviceModalData.show=false;
              if(obj.customAttribute.status==='在线'){
                e.target._icon.src=require('../../../assets/img/location.png')
              }else{
                e.target._icon.src=require('../../../assets/img/location-offline.png')
              }
              
              this.deviceModalData.gatewaymac='';
              this.deviceModalData.deviceList=[]
            }else{
              this.gatewayMarkerArr.forEach((item,index)=>{
                if(item.customAttribute.gatewaymac===this.deviceModalData.gatewaymac){
                    if(item.customAttribute.status==='在线'){
                       item._icon.src=require('../../../assets/img/location.png')
                    }else{
                       item._icon.src=require('../../../assets/img/location-offline.png')
                    }
                }
              })
              e.target._icon.src=require('../../../assets/img/location-active.png')
              this.deviceModalData.gatewaymac=obj.customAttribute.gatewaymac;
              let data=this.allFloorDeviceData;
              let arr=[];
              data.forEach((item,index)=>{
                if(item.gatewaymac===obj.customAttribute.gatewaymac){
                  arr.push(item)
                }
              })

              this.deviceModalData.deviceList=arr;
            }
          }

        })
    },


    gatewayIcon(xDir,yDir,angle){
        let toAngle;
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
          iconSize:[20,20],
          iconAnchor: [5, 5],
          html: `<div style="${markerHtmlStyles}"><img style="width:100%;height:100%" src="${require('../../../assets/img/arrow_track.png')}"></img></div>`
        })

        return icon
    },

    deviceIcon(status){
        let iconUrl;
        if(status==='正常'){
          iconUrl=require('../../../assets/img/marker-1.png');
        }else if(status==='待维修'){
          iconUrl=require('../../../assets/img/marker-2.png');
        }else if(status==='维修接单'){
          iconUrl=require('../../../assets/img/marker-3.png');
        }else if(status==='待报废'){
          iconUrl=require('../../../assets/img/marker-4.png');
        }else if(status==='报废'){
          iconUrl=require('../../../assets/img/marker-5.png');
        }else if(status==='外借'){
          iconUrl=require('../../../assets/img/marker-6.png');
        }else{
          iconUrl=require('../../../assets/img/marker-1.png');
        }

        return iconUrl;
    },

    deviceActiveIcon(status){
        let iconUrl;
        // if(status==='正常'){
        //   iconUrl=require('../../../assets/img/marker-1-active.png');
        // }else if(status==='待维修'){
        //   iconUrl=require('../../../assets/img/marker-2-active.png');
        // }else if(status==='维修接单'){
        //   iconUrl=require('../../../assets/img/marker-3-active.png');
        // }else if(status==='待报废'){
        //   iconUrl=require('../../../assets/img/marker-4-active.png');
        // }else if(status==='报废'){
        //   iconUrl=require('../../../assets/img/marker-5-active.png');
        // }else if(status==='外借'){
        //   iconUrl=require('../../../assets/img/marker-6-active.png');
        // }else{
        //   iconUrl=require('../../../assets/img/marker-1-active.png');
        // }

        iconUrl=require('../../../assets/img/marker-active.png');

        return iconUrl;
    },

    mouseDeviceMarkerFn(obj){
        obj.addEventListener('mouseover',(e)=>{
          let info=obj.customAttribute.brand+' - '+obj.customAttribute.assetName+' - '+obj.customAttribute.mac
          e.target._icon.src=this.deviceActiveIcon(e.target.customAttribute.status);
          obj.bindPopup(info,{
            closeButton:false,
          }).openPopup();
        })

        obj.addEventListener('mouseout',(e)=>{
          e.target._icon.src=this.deviceIcon(e.target.customAttribute.status);
          obj.closePopup();
        })
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
                  this.tableLoading=true;
                  
                  this.clearAllDataFn();
                  this.initBoundaryFn(baseData);
                  this.initHomeLayerFn(homeData);
                  this.initGatewayFn();
                  this.changeZoomFn()

              }
          }
            
        })
    },

    removeMarkerFn(){
      for(let i=0;i<this.gatewayMarkerArr.length;i++){
        this.map.removeLayer(this.gatewayMarkerArr[i])
      }
      for(let i=0;i<this.deviceMarkerArr.length;i++){
        this.map.removeLayer(this.deviceMarkerArr[i])
      }
     
      for(let i=0;i<this.trackLineArr.length;i++){
        this.map.removeLayer(this.trackLineArr[i])
      }

    
      this.gatewayMarkerArr=[];
      this.deviceMarkerArr=[]
      this.trackLineArr=[]
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
      this.removeMarkerFn();

      this.allFloorDeviceData=[];
      this.currentFloorDeviceData=[]
      this.currentPageData=[]
      this.currentPage=1;
      this.totalCount=0;

      this.selectRoomArr=[];
      this.selectRoomArrNow=[];

      this.deviceModalData.show=false;
      this.deviceModalData.gatewaymac='';
        
    },



    addArrFn(beforeArr,afterArr){
      let arr=[]
        for(let i=0;i<afterArr.length;i++){
          
          let k=beforeArr.find((item)=>{
            return item.mac===afterArr[i].mac
          })
            
            if(!k){
              arr.push(afterArr[i])
            }
            
        }
        //拿到新增数据
        return arr;
    },

    reduceArrFn(beforeArr,afterArr){
       let arr=[]
        for(let i=0;i<beforeArr.length;i++){
          
          let k=afterArr.find((item)=>{
            return item.mac===beforeArr[i].mac
          })
            
            if(!k){
              arr.push(beforeArr[i])
            }

        }
        //拿到新增数据
        return arr;
    },

    commonArrFn(beforeArr,afterArr){
        let commonArr=[];
        for(let i=0;i<afterArr.length;i++){
          for(let k=0;k<beforeArr.length;k++){
            if(afterArr[i].mac===beforeArr[k].mac){
                let obj=commonArr.find((item,index)=>{
                  return item.mac===afterArr[i].mac
                })
                if(!obj){
                  commonArr.push(afterArr[i])
                }
            }
          }
        }
        return commonArr;
    },

    refreshArrFn(commonArr,initArr){
      let refreshArr=[];
      for(let i=0;i<commonArr.length;i++){
        for(let k=0;k<initArr.length;k++){
          if(commonArr[i].mac===initArr[k].mac&&commonArr[i].gatewaymac!==initArr[k].gatewaymac){
            let cadRoom1='';
            let cadRoom2='';
            this.gatewayData.forEach((item,index)=>{
              if(item.gatewaymac===commonArr[i].gatewaymac){
                cadRoom1=item.cadMapRoomName;
              }
              if(item.gatewaymac===initArr[k].gatewaymac){
                cadRoom2=item.cadMapRoomName;
              }

            })
            //如果还是在同一个房间
            if(cadRoom1!==cadRoom2){
              refreshArr.push(commonArr[i])
            }else{
              //网关变了，但是房间没变，让网关等于新网关
              initArr[k].gatewaymac=commonArr[i].gatewaymac
            }

          }
        }
      }
      return refreshArr
    },


    currentFloorFn(){
        let currentGatewayArr=[];
        let currentDeviceArr=[];
        this.gatewayFloorData.forEach((item,index)=>{
          if(item.floor===this.currentFloor){
            currentGatewayArr=item.gateway;
          }
        })
        currentGatewayArr.forEach((item,index)=>{
          this.allFloorDeviceData.forEach((m,n)=>{
            if(m.gatewaymac===item.gatewaymac){
              currentDeviceArr.push(m)
            }
          })
        })
        this.currentFloorDeviceData=currentDeviceArr

        this.setCurrentPageFn();

        if(this.queryCheckBtn.isLoading===true){
          this.queryCheckBtn.disabled=false;
          this.queryCheckBtn.isLoading=false;
        }

    },

    changePageFn(page){
      this.tableLoading=true;
      this.changePageMode=true;
      setTimeout(()=>{
        let arr=[];
        if(this.currentFloorDeviceData.length){
            let startIndex=this.pageSize*(page-1)
            let endIndex=((this.pageSize*page)-1) >this.currentFloorDeviceData.length-1 ? this.currentFloorDeviceData.length-1 : ((this.pageSize*page)-1)
            for(let i=startIndex;i<=endIndex;i++){
              arr.push(this.currentFloorDeviceData[i])
            }
            this.currentPageData=arr;
        }
        this.currentPage=page;
        this.tableLoading=false;
        this.changePageMode=false;
      },300)
      
    },

    transWebsocketFn(data){
        let dataArr=[];
        let dataArrResult=[];
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

        dataArr.forEach((item,index)=>{
          let obj={
              amount: item.Amount==='null'? '--' :(item.Amount? item.Amount : '--'),
              applyDept:item.ApplyDept==='null'? '--' : (item.ApplyDept ? item.ApplyDept : '--') ,
              assetID: item.AssetID? item.AssetID : '--',
              assetName: item.AssetName==='null'? '--' :(item.AssetName? item.AssetName : '--'),
              brand: item.Brand==='null'? '--' :(item.Brand? item.Brand : '--'),
              buyDate:item.BuyDate==='null'? '--' :(item.BuyDate? item.BuyDate : '--'),
              check:item.Check==='null'? '--' : (item.Check ? item.Check : '--') ,
              generalName: item.GeneralName==='null'? '--' : (item.GeneralName? item.GeneralName : '--'),
              location:item.Location==='null'? '--' : (item.Location? item.Location : '--'),
              mac: item.Mac? formatScanCodeFn(item.Mac) : '--',
              money: item.Money==='null'? '--' : (item.Money? item.Money : '--'),
              status: item.Status==='null'? '--' : (item.Status? item.Status : '--'),
              type: item.Type==='null'? '--' : (item.Type? item.Type : '--'),
              unit:  item.Unit==='null'? '--' : (item.Unit? item.Unit : '--'),
              department:item.department==='null'? '--' : (item.department ? item.department : '--') ,
              gatewaymac:item.gatewaymac? formatScanCodeFn(item.gatewaymac) : '--',
              updatetime:item.updatetime==='null'? '--' : (item.updatetime? item.updatetime : '--'),
              sn:item.sn==='null'? '--' : (item.sn? item.sn : '--'),
          }
          dataArrResult.push(obj)
        })
        
        return dataArrResult;
    },

    showAllDeviceMarkerFn(){
      let data=this.currentFloorDeviceData;
      if(data.length>0){
          this.showDeviceMarkerFn(data)
      }

    },

    showDeviceMarkerFn(data){
      for(let j=0;j<data.length;j++){
        for(let i=0;i<this.gatewayData.length;i++){
            if(this.gatewayData[i].gatewaymac===data[j].gatewaymac){
              this.currentRoomArr.forEach((item,index)=>{
                if(item.properties.NO===this.gatewayData[i].roomnumber){
                  let $geojson = L.Proj.geoJson(item, {
                    style: function(item) {
                      return {
                        color: "#fff",
                        opacity: 1,
                        weight: 2,
                        fillColor: '#fff',
                        fillOpacity: 1,
                      };
                    }
                  })

                  //渲染房间名
                  let getBounds = $geojson.getBounds();
                  let lat = (getBounds._northEast.lat - getBounds._southWest.lat) / 2 + getBounds._southWest.lat;
                  let lng = (getBounds._northEast.lng - getBounds._southWest.lng) / 2 + getBounds._southWest.lng;

                  let polygonArr=[];

                  polygonArr=item.geometry.coordinates[0];

                  for(;;){
                      let indexX = (Math.random() * (getBounds._northEast.lat - getBounds._southWest.lat)+getBounds._southWest.lat).toFixed(6);
                      let indexY = (Math.random() * (getBounds._northEast.lng - getBounds._southWest.lng)+getBounds._southWest.lng).toFixed(6);
                      let point=[indexX,indexY]
                      
                      let pointResult=this.CRS_4549.project(L.latLng(point));
                      let p=[pointResult.x,pointResult.y]
                      let flag = this.isInPolygon(p, polygonArr);
                      if(flag){
                          let posiData={
                            lat:point[0],
                            lng:point[1],
                          }
                          let marker=L.marker(posiData, {
                            icon: L.icon({
                              iconUrl: this.deviceIcon(data[j].status),
                              iconSize: [16],
                            })
                          }).addTo(this.map)
                          marker.setZIndexOffset(9999);
                          marker.customAttribute={
                            gatewaymac:data[j].gatewaymac,
                            mac:data[j].mac,
                            brand:data[j].brand,
                            assetName:data[j].assetName,
                            status:data[j].status,
                          }
                          this.mouseDeviceMarkerFn(marker)
                          this.deviceMarkerArr.push(marker);

                        break;
                      }
                  }

                }
              })
             
            }
        }
      }
    },


    addDeviceMarkerFn(data){
       for(let k=0;k<this.gatewayFloorData.length;k++){
          for(let j=0;j<this.gatewayFloorData[k].gateway.length;j++){
            if(data.gatewaymac===this.gatewayFloorData[k].gateway[j].gatewaymac){
                if(this.gatewayFloorData[k].floor===this.currentFloor){
                  let arr=[];
                  arr.push(data)
                  this.showDeviceMarkerFn(arr)
                }
            }
          }
        };
    },

    
    //设置轨迹
    setTrackFn(startPoint,endPoint,num){
          let arrs=[
            startPoint,
            endPoint,
          ]
          let antPath = L.polyline.antPath;

          let path = antPath(arrs, {
              "paused": false,   　　//暂停  初始化状态
              "reverse": false,　　//方向反转
              "delay": 500,　　　　//延迟，数值越大效果越缓慢
              "dashArray": [30, 20],　//间隔样式
              "weight": 5,　　　　//线宽
              "opacity": 0.5,　　//透明度
              "color": "#0000FF",　//颜色
              "pulseColor": "#FFFFFF"　　//块颜色
          });
          path.addTo(this.map);

          this.trackLineArr.push(path)
    },

    setAllTrackFn(){
      //有需要显示轨迹的时候先清除掉上一次的轨迹数据
      this.trackLineArr.forEach((m,n)=>{
        this.map.removeLayer(m)
      })
      this.trackLineArr=[]

      let startPosi=[]
      let endPosi=[]
      this.trackGatewayArr.forEach((m,n)=>{
          this.gatewayData.forEach((a,b)=>{
            if(a.gatewaymac===m[0]){
                startPosi=a.posi;
            }else if(a.gatewaymac===m[1]){
                endPosi=a.posi;
            }
          })

          this.setTrackFn(startPosi,endPosi,n)
      })
      // 1.5秒后清除掉轨迹
      setTimeout(()=>{
        this.trackLineArr.forEach((m,n)=>{
          this.map.removeLayer(m)
        })
        this.trackLineArr=[]
      },1500)
     
    },
    
    initDeviceDataFn(dataArr){
      if(this.allFloorDeviceData.length){
          let addDevices=this.addArrFn(this.allFloorDeviceData,dataArr);
          let reduceDevices=this.reduceArrFn(this.allFloorDeviceData,dataArr);
          let commonDevices=this.commonArrFn(this.allFloorDeviceData,dataArr);
          let refreshDevices=this.refreshArrFn(commonDevices,this.allFloorDeviceData)
          //需要更新的数据也需要先删除后添加，所以也合并进需要增加和删除的数据统一操作
          let reduceConcat=reduceDevices.concat(refreshDevices);
          let addConcat=addDevices.concat(refreshDevices);
          //如果有需要更新的数据，先判断变化的网关是否在同一层，是的话在地图上画出轨迹
          if(refreshDevices.length){
            this.trackGatewayArr=[]
            refreshDevices.forEach((item,index)=>{
              let startMac='';
              let endMac='';
              let startFloor='';
              let endFloor='';
              this.allFloorDeviceData.forEach((m,n)=>{
                if(item.mac===m.mac){
                  startMac=m.gatewaymac;
                  endMac=item.gatewaymac;
                  this.gatewayData.forEach((a,b)=>{
                    if(a.gatewaymac===startMac){
                        startFloor=a.floor;
                    }else if(a.gatewaymac===endMac){
                        endFloor=a.floor;
                    }
                  })
                }
               
              })

              if(startFloor===endFloor&&endFloor===this.currentFloor){
                
                  if(this.trackGatewayArr.length){

                    let obj=this.trackGatewayArr.find((c,d)=>{
                      return c[0]===startMac&&c[1]===endMac
                    })
                    //如果不存在开始和结束都是同样的网关就将这个轨迹追加进数组
                    if(!obj){
                      this.trackGatewayArr.push([startMac,endMac])
                    }
                    
                }else{
                  this.trackGatewayArr.push([startMac,endMac])
                }

              }

            })

            //算出开始和结束的网关坐标后执行轨迹
            this.setAllTrackFn();
          }


          //有需要移除掉的标记就从这里移除
          if(reduceConcat.length){
            for(let i=0;i<reduceConcat.length;i++){
                for(let j=0;j<this.deviceMarkerArr.length;j++){
                  if(this.deviceMarkerArr[j].customAttribute.mac===reduceConcat[i].mac){
                    this.map.removeLayer(this.deviceMarkerArr[j])
                    this.deviceMarkerArr.splice(j,1)
                  }
                }

                this.allFloorDeviceData.forEach((item,index)=>{
                  if(item.mac===reduceConcat[i].mac){
                    
                    this.allFloorDeviceData.splice(index,1)
                  }
                })
               
            }
          }

          //有需要增加的标记从这里新增
          if(addConcat.length){
            for(let k=0;k<addConcat.length;k++){
                this.addDeviceMarkerFn(addConcat[k])
            }
            
            this.allFloorDeviceData=this.allFloorDeviceData.concat(addConcat)
          }

          this.currentFloorFn();

      }else{
        this.allFloorDeviceData=dataArr;
        this.currentFloorFn()
        this.showAllDeviceMarkerFn();
      }

    },

    initWebSocket(){
        this.websock = new WebSocket(NET_PORT.mapSocket+'/'+getToken());
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onclose = this.websocketclose;
    },

    testSocketFn(){
        let str={
          data:''
        }

        setTimeout(()=>{
          str.data=strStart;
          this.websocketonmessage(str);
        },2000)

        setInterval(()=>{
          str.data=strEnd;
          this.websocketonmessage(str);
        },3000)

        // setInterval(()=>{
        //   this.websocketonmessage();
        // },2000)
    },

    createDeviceDataFn(){
      // let arr=[];

      // for(var i=0;i<400;i++){
      //       let gatewayData=this.gatewayFloorData[0].gateway;
      //       let key=parseInt(Math.random()*gatewayData.length);
      //       let gatewaymac=gatewayData[key].gatewaymac;
      //       let obj={
      //         assetID:'ZCKP'+i,
      //         generalName: "通用名"+i,
      //         gatewaymac:gatewaymac,
      //         assetName: "模拟资产名",
      //         location:"模拟位置",
      //         department:"模拟部门",
      //         type: "模拟类型",
      //         brand: "模拟品牌",
      //         money: 2000,
      //         buyDate:"2012-08-29",
      //         status: "在线",
      //         mac: "ac23256f"+i,
      //         amount: 1,
      //         unit: "台",
      //         updatetime:'2020-08-29 08:32:24',
      //         applyDept:'模拟申请科室',
      //         check:'盘点日期',
      //     }

      //     arr.push(obj)
      // }

      let arr=deviceData400;

      return arr
    },


    websocketonmessage(e){
        let dataArr=this.transWebsocketFn(e.data)
        // let dataArr=deviceData400
        // let dataArr=this.createDeviceDataFn();
        let dataArrNow=this.filterCommonFn(dataArr)

        if(!this.changePageMode){
          this.setDeviceDataFn(dataArrNow)
        }
    },

    setDeviceDataFn(data){
        let dataArrResult=[];
        if(this.isQueryMode){
          dataArrResult=this.filterQueryFn(data)
        }else if(this.isSelectMode){
          dataArrResult=this.filterSelectFn(data)
        }else{
          dataArrResult=data;
        }
        
        if(L){
          this.initDeviceDataFn(dataArrResult);
          // this.tableLoading=false;
        }else{
          return
        }
    },

    filterQueryFn(dataArr){
      //资产筛选模式下过滤查询数据
      let dataArrResult=[];
      dataArr.forEach((item,index)=>{
          let fillKey=0;
          let commonKey=0;
          for(let i in this.queryDataNow){
            if(this.queryDataNow[i].trim()!==''){
              fillKey++;
              if(i==='assetID'||i==='mac'){
                if(item[i].trim().toLowerCase()===this.queryDataNow[i].trim().toLowerCase()){
                  commonKey++
                }
              }else if(i==='assetName'||i==='generalName'){
                let reg = new RegExp(this.queryDataNow[i].trim());
                if(item[i].trim().match(reg)){
                    commonKey++
                }
              }else{
                if(item[i].trim()===this.queryDataNow[i].trim()){
                  commonKey++
                }
              }
            }
          }
          if(fillKey===commonKey){
            dataArrResult.push(item)
          }
      })

      return dataArrResult
    },

    filterSelectFn(dataArr){
      let gatewayArr=[];
      let dataArrResult=[];

      if(!this.selectRoomArrNow.length){
        dataArrResult=dataArr
      }else{
          this.selectRoomArrNow.forEach((item,index)=>{
            this.gatewayData.forEach((m,n)=>{
              if(item.NO===m.roomnumber){
                gatewayArr.push(m.gatewaymac)
              }
            })
          })

          if(gatewayArr.length){
            //筛选出所选择房间得数据
              dataArr.forEach((item,index)=>{
                gatewayArr.forEach((m,n)=>{
                  if(item.gatewaymac===m){
                    dataArrResult.push(item);
                  }
                })
              })
          }else{
            dataArrResult=[]
          }
      }

        return dataArrResult;
    },


    filterCommonFn(dataArr){
        let dataArrNow=[];
          //如果有资产id相同的资产去除掉后面的
        dataArr.forEach((item,index)=>{
          if(dataArrNow.length){
            let commonDataIndex=null;
            let obj=dataArrNow.find((m,n)=>{
              if(m.assetID===item.assetID){
                commonDataIndex=n;
                return m.assetID===item.assetID;
              }
              
            })
            //如果已经有相同资产了
            if(obj){
              //如果两个资产的更新日期都存在
              if(obj.updatetime&&item.updatetime){
                  //如果已经存在的这个资产更新日期更新
                  if(Date.parse(obj.updatetime)>Date.parse(item.updatetime)){

                  }else if(Date.parse(obj.updatetime)===Date.parse(item.updatetime)){

                  }else{
                    dataArrNow[commonDataIndex]=item;
                  }
              }
            
            }else{
              dataArrNow.push(item);
            }
          }else{
            dataArrNow.push(item);
          }
        
        })

        return dataArrNow
    },

    getDataFn(){
        this.$Loading.start()
        let gatewayData=[]
        this.$axios.get(NET_PORT.gatewayQuery+this.$store.state.address)
        .then((res)=>{
            let arr=res.data;
            // let arr=gatewayTest;
            arr.forEach((item,index)=>{
                if(JSON.stringify(item)!=='{}'){
                  if(item.gatewaymac!==''){
                      let obj={
                          gatewaymac:formatScanCodeFn(item.gatewaymac),
                          floor:item.floor?item.floor : '',
                          mapx:item.mapx ? item.mapx : '',
                          mapy:item.mapy ? item. mapy : '',
                          department:item.department ? item.department :'',
                          location:item.location ? item.location :'',
                          status:item.online==='online'&&item.updatetime ?'在线' : '离线',
                          updatetime:item.updatetime ? item.updatetime :'',
                          cadMapRoomName:item.cadMapRoomName ? item.cadMapRoomName : '',
                          roomnumber:item.roomnumber? item.roomnumber :'',
                          posi:[item.mapx,item.mapy]
                      }

                      if(obj.floor){
                        gatewayData.push(obj)
                      }
                  }
                }
            })
            this.gatewayData=gatewayData;
            //格式化楼层网关数据
            this.formatGatewayFn()
            this.$Loading.finish();
        })
        .catch((err)=>{
            this.$Loading.error();
            // console.log(err)
        })
    },

    formatGatewayFn(){
        let gatewayData=this.gatewayData;
        let gatewayFloorData=[]
        //将没按楼层分组的数据分成一个按楼层分组的数组
        gatewayData.forEach((m,n)=>{
          if(gatewayFloorData.length===0){
              let data1={
                floor:m.floor,
                gateway:[],
              };
              data1.gateway.push(m);
              gatewayFloorData.push(data1);
          }else{
              let isHave=gatewayFloorData.find((item,index)=>{
                return item.floor===m.floor
              })
              if(isHave){
                isHave.gateway.push(m);
              }else{
                let data2={
                  floor:m.floor,
                  gateway:[],
                };
                data2.gateway.push(m);
                gatewayFloorData.push(data2);
              }
          }

        })

        this.gatewayFloorData=gatewayFloorData;

        this.$nextTick(()=>{
            leafLetScript()
            .then(()=>{
              proj4ComScript()
              .then(()=>{
                proj4LeafLetScript()
                .then(()=>{
                  leafletAntPath()
                  .then(()=>{
                    leafletFullScreen()
                    .then(()=>{
                        this.isShowMap=true;
                        this.$nextTick(()=>{
                          this.initMapFn();
                          this.initWebSocket()
                          // this.testSocketFn()
                        })
                    })
                     
                  })

                })
                  
              })
              
            })
        })
    }
      
  },
  created(){

  },

  mounted(){
    this.getDataFn();
  },

}
</script>


<style lang='scss' scoped>
  @import "../../../assets/scss/web/device_manage.scss";
  @import '../../../assets/scss/web/common/table.scss';
  @import '../../../assets/scss/web/common/form_data.scss';

</style>

<template>
  <div class='device_manage_wrap'>
      <div class="row_box hover_animat custom_bg_color_white">
            <h2 class="row_title">地图</h2>
            <div id="map_box" style="height:700px;">
                    
            </div>
      </div>

       <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">设备明细表</h2>
                    <div class="data_table_top clearfix">
                        <div class="data_table_top_left">
                            <Button type="primary" icon="md-add" @click="toCreateFn">新增设备</Button>
                            <Button v-if="$store.state.role==='admin'" type="error" icon="md-trash" @click="deleteFn">删除设备</Button>
                            <Button type="success" icon="md-archive" @click=exportExcelFn>导出设备明细表</Button>
                        </div>
                        <div class="data_table_top_right">
                            <!-- <div class="pagination_wrap">
                                <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                            </div> -->
                        </div>
                    </div>

                    <div class="table_scroll_wrap">
                        <Table style="min-width:1500px" stripe :columns="deviceDetailColumn" :data="deviceNowData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                            <template slot-scope="{row}" slot="assetID">
                              <a @click="toDetailFn(row.assetID)">{{row.assetID}}</a>
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

                            <template slot-scope="{row}" slot="check">
                                <span>{{row.check ? row.check : '--'}}</span>
                            </template>

                            <template slot-scope="{row}" slot="oldRatio">
                              {{row.oldRatio ? row.oldRatio+"%" :'--'}}
                            </template>

                            <template slot-scope="{ row }" slot="status">
                                <Tag v-if="row.status==='正常'" color="success">正常</Tag>
                                <Tag v-else-if="row.status==='在修'" color="warning">在修</Tag>
                                <Tag v-else-if="row.status==='待报废'" color="primary">待报废</Tag>
                                <Tag v-else-if="row.status==='已报废'" color="error">已报废</Tag>
                                <Tag v-else-if="row.status==='--'"  color="default">未知</Tag>
                                <Tag v-else  color="default">{{row.status}}</Tag>
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
import {Icon,Table ,Tag ,Page,Input,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
var fengmap = require('fengmap');

export default {
  name:'device_manage',
  components:{},
  props:{},
  data(){
    return {
       groupId:1,
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
                minWidth:30,
            },
            {
                title: '资产名称',
                key: 'assetName',
            },
            {
                title: 'mac地址',
                key: 'mac'
            },
            {
                title: '数量',
                key: 'amount',
                slot:'amount'
            },
            {
                title: '单位',
                key: 'unit',
            },
            {
                title: '购买日期',
                key: 'buyDate',
                slot: 'buyDate'
            },
            {
                title: '金额',
                key: 'money',
                slot: 'money'
            },
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:100
            },
            {
                title: '部门',
                key: 'department',
            },
            {
                title:'一级分类',
                key: 'fstCat', 
            },
            {
                title:'二级分类',
                key: 'secCat',
            },
            {
                title:'三级分类',
                key: 'thdCat',
            },
            {
                title: '折旧率',
                key: 'oldRatio', 
                slot:'oldRatio'
            },
            {
                title: '每月折旧',
                key: 'monthOldValue', 
                
            },
            {
                title: '盘点日期',
                key: 'check', 
                slot:'check'
            },
        
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

    }
  },
  watch:{},
  computed:{},
  
  methods:{

    //根据楼层过滤数据
      filterData(){
          this.deviceNowData=this.deviceDetailData.filter((k)=>{
            // if(k.level===this.groupId){
              return k
            // }
          })
      },


    paintDevices(){

            var group = this.fengMap.getFMGroup(this.groupId);

            //返回当前层中第一个imageMarkerLayer,如果没有，则自动创建
            var layer = group.getOrCreateLayer('imageMarker');
            layer.removeAll();
            
            this.deviceNowData.forEach((d,i) => {
              

                  let rdX = Math.random()+10;
                  let rdY = Math.random()+2;

                   //图标标注对象，默认位置为该楼层中心点
                  let im = new fengmap.FMImageMarker({
                    //设置图片路径
                    url: require('../../../assets/img/location.png'),
                    //设置图片显示尺寸
                    size: 32,
                    x: parseFloat(d.x)+rdX,
                    y: parseFloat(d.y)+rdY,
                   
                    callback: function() {
                      // 在图片载入完成后，设置 "一直可见"
                      im.alwaysShow();
                    }
                  });
                 
                  layer.addMarker(im);


                   this.fengMap.on('mapClickNode',(event)=>{
                      // var popMarker=null;
                      if(event.nodeType===31){
                            if(event.x===(parseFloat(d.x)+rdX)){
                                //点击的时候如果有存在的气泡先清除气泡
                                if(this.popMarker){
                                  this.popMarker.close()
                                }
                                im.url=require('../../../assets/img/location-active.png')
                                var opts = new fengmap.controlOptions({
                                    mapCoord: {
                                        x: parseFloat(d.x)+rdX, //设置弹框的x轴
                                        y: parseFloat(d.y)+rdY, //设置弹框的y轴
                                        height: 10,
                                        groupID: d.level //设置弹框位于的楼层
                                    },
                                    
                                    width: 200, //设置弹框的宽度
                                    height: 80, //设置弹框的高度
                                    marginTop: 0, //距离地图的高度
                                    content: '<div style="font-size:30px;">'+d.name ? d.name : '无名称'+'</div>', //设置弹框的内容,
                                    closeCallBack: function() {
                                      im.url=require('../../../assets/img/location.png')
                                      if(this.popMarker){
                                        this.popMarker.close()
                                      }else{
                                        return
                                      }
                                    }
                                });
                                this.popMarker = new fengmap.FMPopInfoWindow(this.fengMap, opts); 
                          }
                      }
                  })
                
              })

      },



    //  changePageFn(page){
    //   this.tableLoading=true;
    //   this.currentPage=page;
    // },

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
          path:'/assets_create_2'
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
        path:'/assets_detail_2'
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
      console.log(list)

      this.$axios.get(process.env.API_HOST+'huaxi/del?assetID='+list)
      .then((res)=>{
          console.log(res.data)
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
      
          var fmapID='sc028-0002-20171212-01'
          const map = new fengmap.FMMap({
            container: document.getElementById("map_box"),
            appName: "yikecha",
            key: "e2d15e7f84f0800239483a257eb6f08d",
            defaultMapScaleLevel: 18,
            // mapThemeURL: 'assets/map/theme',
            // mapServerURL: 'assets/map/sc028-0002-20171212-01',
            defaultThemeName: 'sc028-0002-20171212-01',
            
            focusAnimateMode: true,            //开启聚焦层切换的动画显示
            viewModeAnimateMode:true,          //开启2维，3维切换的动画显示
            moveToAnimateMode:true,           //地图定位跳转动画设置
            naviLineAnimation:true,           //路径线流动动画开关。false关闭
            defaultViewMode: fengmap.FMViewMode.MODE_2D,
            defaultVisibleGroups:[1],
            defaultFocusGroup: 1,
            // defaultGroupSpace:40,           //楼层间距空间
            // compassOffset:[20,20],          //初始指北针的偏移量，[左，上]
            compassSize: 48,       
          });

          map.openMapById(fmapID);

          //显示指北针
          map.showCompass = true;

          map.on("loadComplete", () => {

            if (!this.fengMap) {
              this.fengMap = map;
            }

            const ctlOpt = new fengmap.controlOptions({
              position: fengmap.controlPositon.RIGHT_TOP,
              showBtnCount: 7,
              // allLayer: false,
              offset: {
                x: 20,
                y: 50
              }
            });

            const groupControl = new fengmap.scrollGroupsControl(map, ctlOpt);

            groupControl.onChange(groups => {
              if(this.popMarker){
                this.popMarker.close()
              }
              this.groupId = groups[0];

              this.filterData()

              //标注点
              this.paintDevices();
              // console.log(this.deviceNowData)
            });



            //标注点
            this.paintDevices();

            
          });
 

    },
    
    
  },
  created(){

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

  mounted(){
    //发请求获取数据
    // this.getDataFn()
    this.$nextTick(()=>{
      this.initMapFn();
    })
  },

  beforeDestroy(){
      this.$Loading.finish();
  }
}
</script>


<style lang='scss' scoped>
  @import "../../../assets/scss/web/device_manage.scss";
  @import '../../../assets/scss/web/common/table.scss';
</style>

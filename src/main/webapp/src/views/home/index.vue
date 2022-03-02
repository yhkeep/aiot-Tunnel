<template>
  <div class='home_wrap'>
      <div class="custom_flex_wrap total_data_wrap">
          <div class="total_data_item custom_flex_item_4 custom_bg_color_white hover_animat">
                <div class="total_icon_box">
                    <Icon type="md-cube" />
                </div>
                <div class="total_item_box">
                    <div class="total_item_box_top">
                        {{summary.assetTotalFee ? assetTotalFeeCom : '--'}}万元
                    </div>
                    <div class="total_item_box_bottom">
                        资产总额
                    </div>
                </div>
          </div>
          <div class="total_data_item custom_flex_item_4 custom_bg_color_white hover_animat">
                <div class="total_icon_box">
                    <Icon type="ios-pie" />
                </div>
                <div class="total_item_box">
                    <div class="total_item_box_top">
                        {{summary.assetTotal ?assetTotalCom : '--'}}件
                    </div>
                    <div class="total_item_box_bottom">
                        资产总量
                    </div>
                </div>
          </div>
          <div class="total_data_item custom_flex_item_4 custom_bg_color_white hover_animat">
                <div class="total_icon_box">
                    <Icon type="md-desktop" />
                </div>
                <div class="total_item_box">
                    <div class="total_item_box_top">
                        {{summary.deviceTotalFee ? deviceTotalFeeCom : '--'}}万元
                    </div>
                    <div class="total_item_box_bottom">
                        设备总额
                    </div>
                </div>
          </div>
          <div class="total_data_item custom_flex_item_4 custom_bg_color_white hover_animat">
                <div class="total_icon_box">
                    <Icon type="md-battery-charging" />
                </div>
                <div class="total_item_box">
                    <div class="total_item_box_top">
                        {{summary.deviceUtilization ? deviceUtilizationCom : '--'}}%
                    </div>
                    <div class="total_item_box_bottom">
                        设备动用率
                    </div>
                </div>
          </div>
      </div>

      <div class="row_wrap hover_animat custom_bg_color_white utilization_wrap">
            <div class="row_box">
                <h2 class="row_title">设备最近一月动用率</h2>
                <div id="utilization_line"></div>
            </div>
      </div>

      <div class="custom_flex_wrap row_wrap">
          <div class="custom_flex_item_3 hover_animat custom_bg_color_white row_box">
                <h2 class="row_title">重点关注设备动用率</h2>
                <div class="data_block">
                    <div id="importDevice_01_line">
                        
                    </div>
                    <div id="importDevice_02_line">
                        
                    </div>
                </div>
          </div>
          <div class="custom_flex_item_3 hover_animat custom_bg_color_white row_box">
              <h2 class="row_title">设备动态</h2>
                        <div class="data_block" id="news_box">
                            <h3 v-if="deviceMessage.length<=0">暂无动态...</h3>
                            
                            <div ref="deviceMessageWrap" v-else class="device_message_scroll_wrap">
                                <div ref="deviceMessageBox" class="device_message_scroll_box">
                                    <div class="device_message_item clearfix" v-for="(item,index) in deviceMessage" :key="index">
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
          <div class="custom_flex_item_3 hover_animat custom_bg_color_white row_box">
                <h2 class="row_title">全院资产折旧率</h2>
                <div class="data_block" id="old_ratio_wrap">

                </div>
          </div>
      </div>

      <div class="row_wrap hover_animat custom_bg_color_white">
           <div class="row_box">
                <h2 class="row_title">新增资产列表</h2>
                <div class="table_scroll_wrap">
                    <Table style="min-width:1100px" stripe :columns="this.$store.state.address==='3'?newAssetsColumn2 : newAssetsColumn" :data="newAssetsData" size="large" :loading="tableLoading">
                        <template slot-scope="{row}" slot="applyDept">
                            <span>{{row.applyDept ? row.applyDept : '--'}}</span>
                        </template>
                        <template slot-scope="{row}" slot="locDept">
                            <span>{{row.locDept ? row.locDept : '--'}}</span>
                        </template>
                        <template slot-scope="{row}" slot="location">
                            <span>{{row.location ? row.location : '--'}}</span>
                        </template>
                        
                        <template slot-scope="{row}" slot="check">
                            <span>{{row.check ? row.check : '--'}}</span>
                        </template>
                        
                        <template slot-scope="{row}" slot="buyDate">
                            <span>2019-01-01</span>
                        </template>
                        
                        <template slot-scope="{row}" slot="amount">
                            <span>{{Number(row.amount)? Number(row.amount) : '--'}}</span>
                        </template>
                        <template slot-scope="{row}" slot="money">
                            <span>10000</span>
                        </template>
                        <template slot-scope="{row}" slot="electric">
                            <span>{{row.electric}}%</span>
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
  </div>
</template>

<script>
import Vue from "vue"
import {echartScript} from "../../utils/needScript.js"
import { Table ,Tag ,Icon} from 'iview';
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Icon', Icon);


import {allUtilizationOption} from "../../assets/js/chart/echarts/utilization_line.js"
import importDeviceOption from "../../assets/js/chart/echarts/importDevice_line.js"
import allOldRatioOption from "../../assets/js/chart/echarts/allOldRatio_pie.js"

export default {
  name:'home',
  components:{},
  props:{},
  data(){
    return {
        summary:{},
        assetTotalCom:null,
        assetTotalFeeCom:null,
        deviceTotalFeeCom:null,
        deviceUtilizationCom:null,
        deviceMessage:[],
        tableLoading:true,
        newAssetsColumn: [
            {
                title: '资产id',
                key: 'assetID',
                minWidth:30,
            },
            {
                title: '通用名称',
                key: 'generalName',
            },
            {
                title: '资产名称',
                key: 'assetName',
            },
            {
                title: '部门',
                key: 'department',
            },
            {
                title: '规格型号',
                key: 'type',
            },
            {
                title: '品牌',
                key: 'brand',
            },
            {
                title: '申请科室',
                key: 'applyDept',
                slot: 'applyDept',
            },
           
            {
                title: '坐落位置',
                key: 'location',
                slot: 'location',
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
                title: '盘点日期',
                key: 'check',
                slot: 'check',
            },
          
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:100
            },
        ],
         newAssetsColumn2: [
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
                title: '数量',
                key: 'amount',
                slot:'amount'
            },
            {
                title: '单位',
                key: 'unit',
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
                slot: 'check',
            },
        ],
        newAssetsData: [],
        //创建socket
        websock: null,

        utilizationChart:null,
        impDevOneChart:null,
        impDevTwoChart:null,
        allOldRatioChart:null,
    }
  },
  watch:{
      assetTotalCom(){
          var key=parseInt(this.summary.assetTotal/1)
          var timer=setInterval(()=>{
                if(this.assetTotalCom>=this.summary.assetTotal){
                    this.assetTotalCom=this.summary.assetTotal;
                }else{
                    this.assetTotalCom+=key
                }
                clearInterval(timer)
          },10)
      },
      assetTotalFeeCom(){
          var assetTotalFeeFix=(this.summary.assetTotalFee/10000).toFixed(2)
          var key=parseInt(assetTotalFeeFix/1)
          var timer=setInterval(()=>{
                if(this.assetTotalFeeCom>=assetTotalFeeFix){
                    this.assetTotalFeeCom=assetTotalFeeFix;
                }else{
                    this.assetTotalFeeCom+=key
                }
                clearInterval(timer)
          },10)
      },
      deviceTotalFeeCom(){
          var deviceTotalFeeFix=(this.summary.deviceTotalFee/10000).toFixed(2)
          var key=parseInt(deviceTotalFeeFix/1)
          var timer=setInterval(()=>{
                if(this.deviceTotalFeeCom>=deviceTotalFeeFix){
                    this.deviceTotalFeeCom=deviceTotalFeeFix;
                }else{
                    this.deviceTotalFeeCom+=key
                }
                clearInterval(timer)
          },10)
      },
      deviceUtilizationCom(){
          var key=parseInt(this.summary.deviceUtilization/1)
          var timer=setInterval(()=>{
                if(this.deviceUtilizationCom>=this.summary.deviceUtilization){
                    this.deviceUtilizationCom=this.summary.deviceUtilization;
                }else{
                    this.deviceUtilizationCom+=key
                }
                clearInterval(timer)
          },10)
      },

      isCollapsed(){
            setTimeout(()=>{
                    this.utilizationChart.resize();
                    this.impDevOneChart.resize();
                    this.impDevTwoChart.resize();
                    this.allOldRatioChart.resize();
            },500)
      },
  },
  computed:{
        isCollapsed(){
          return this.$store.state.isCollapsed
        }
  },
  methods:{
        setUtilizationFn(){
                this.utilizationChart = echarts.init(document.getElementById('utilization_line'));
                // 使用刚指定的配置项和数据显示图表。
                this.utilizationChart.setOption(allUtilizationOption);
        },

        setImportDeviceFn(){
            // 基于准备好的dom，初始化echarts实例
            this.impDevOneChart = echarts.init(document.getElementById('importDevice_01_line'));
            this.impDevTwoChart = echarts.init(document.getElementById('importDevice_02_line'));

            // 重点设备1最近一月动用率
            var impDevOneOption=JSON.parse(JSON.stringify(importDeviceOption));
            var impDevTwoOption=JSON.parse(JSON.stringify(importDeviceOption));
            // 重点设备1最近一月动用率
            impDevOneOption.title.text='1#核磁共振'
            impDevOneOption.series[0].data=[0, 23, 43, 35, 44, 45, 56, 37, 40, 45, 56, 7, 10, 44, 45, 56,];

            // 重点设备2最近一月动用率
            impDevTwoOption.title.text='1#CT'
            impDevTwoOption.series[0].data=[33, 23, 43, 35, 24, 45, 16, 17, 40, 45, 56, 34, 50, 14, 41, 26, ];
            impDevTwoOption.color="#4cb668"
            impDevTwoOption.title.textStyle.color="#4cb668"

                this.impDevOneChart.setOption(impDevOneOption)
                this.impDevTwoChart.setOption(impDevTwoOption)
               
       },

        setAllOldFn(){
            this.allOldRatioChart = echarts.init(document.getElementById('old_ratio_wrap'));
            this.allOldRatioChart.setOption(allOldRatioOption)
           
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

        //初始化websocket
        initWebSocket(){ 
            // this.websock = new WebSocket("ws://192.168.0.54:8009/websocket");
            this.websock = new WebSocket("ws://47.104.99.230:8080/websocket");
            this.websock.onopen = this.websocketonopen;
            this.websock.onerror = this.websocketonerror;
            this.websock.onmessage = this.websocketonmessage;
            this.websock.onclose = this.websocketclose;
        },

        websocketonopen() {
            // console.log("WebSocket连接成功");
        },
        websocketonerror(e) {
            // console.log("WebSocket连接发生错误");
        },
        websocketonmessage(e){
            var dataArr=this.transWebsocketFn(e.data)
            this.deviceMessage=dataArr;
        },


        websocketclose(e){ 
            //关闭
        },


        getDataFn(){
            this.$Loading.start()
            this.$axios.get(process.env.API_HOST+'huaxi/assetManagement/param')
            .then((res)=>{
                var initData=res.data;
                var total=Number(initData.pop().total);
                
                for(var i=0;i<initData.length;i++){
                    for(var k in initData[i]){
                        if(initData[i][k]===''||initData[i][k]==='null'){
                           initData[i][k]='--'; 
                        };

                    }
                    this.newAssetsData.push(initData[i])
                }

               
                
                this.summary.assetTotal=total
                this.summary.assetTotalFee=total*10000;
                
                this.assetTotalCom=0;
                this.assetTotalFeeCom=0;
                this.deviceTotalFeeCom=0;
                this.deviceUtilizationCom=0;

                this.$Loading.finish();
                this.tableLoading=false;

            })
            .catch((err)=>{
                console.log(err)
                this.$Loading.error();
            })
        },

        windowResizeFn(){
            window.addEventListener('resize',()=>{
                this.utilizationChart.resize();
                this.impDevOneChart.resize();
                this.impDevTwoChart.resize();
                this.allOldRatioChart.resize();     
            })
        }
  },
  created(){
        //页面刚进入时开启长连接
        this.initWebSocket()
       
  },
  mounted(){
        //获取数据
        this.getDataFn();
        this.$nextTick(()=>{
          echartScript()
          .then(()=>{
                this.setUtilizationFn();
                this.setImportDeviceFn();
                this.setAllOldFn();
          })
        })
        //窗口缩放图表也跟着缩放
        this.windowResizeFn();
  },

  beforeDestroy(){
        this.$Loading.finish();
        //页面销毁时关闭长连接
        this.websocketclose();
  }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/device_message.css"></style>
<style scoped>
    @import "../../assets/css/home.css"
</style>

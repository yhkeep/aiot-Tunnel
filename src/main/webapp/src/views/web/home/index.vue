<template>
  <div class='home_wrap'>
      <div class="custom_flex_wrap total_data_wrap">
          <div v-for="(item,index) in summaryList" :key="index" class="total_data_item custom_flex_item_4 custom_bg_color_white hover_animat">
                <div :style="'background-color:'+item.color" class="total_icon_box">
                    <Icon :type="item.icon" />
                </div>
                <div class="total_item_box">
                    <div class="total_item_box_top">
                        <!-- {{item.value}}{{item.unit}} -->
                        <countTo :startVal='0' :endVal='Number(item.value)' separator='' :duration='1000' :decimals="item.value.toString().indexOf('.')>-1 ?item.value.toString().length-(item.value.toString().indexOf('.')+1) :0" :suffix="item.unit" ></countTo>
                    </div>
                    <div class="total_item_box_bottom">
                        {{item.text}}
                    </div>
                </div>
          </div>
      </div>
      <div class="custom_flex_wrap row_wrap">
          <div class="custom_flex_item_3_no hover_animat custom_bg_color_white row_box">
                <h2 class="row_title">设备动态</h2>
                <div class="device_message_box" style="margin-bottom:10px">
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

                <div class="custom_flex_wrap">
                    <div class="device_message_num_item">
                        <span>{{deviceMessageNum.lose}}件</span>
                    </div>
                    <div class="device_message_num_item">
                        <span>{{deviceMessageNum.beyond}}件</span>
                    </div>
                  
                </div>
          </div>
          <div class="custom_flex_item_3_no hover_animat custom_bg_color_white row_box">
                <h2 class="row_title">维修报废</h2>
                <div class="data_block" id="damage_ratio_wrap" style="margin-bottom:10px"></div>

                <div class="custom_flex_wrap">
                    <div class="damage_num_item">
                        <span>{{damageData[0]}}件</span>
                    </div>
                    <div class="damage_num_item">
                        <span>{{damageData[1]}}件</span>
                    </div>
                    <div class="damage_num_item">
                        <span>{{damageData[2]}}件</span>
                    </div>
                    <div class="damage_num_item">
                        <span>{{damageData[3]}}件</span>
                    </div>
                    <div class="damage_num_item">
                        <span>{{damageData[4]}}件</span>
                    </div>
                    <div class="damage_num_item">
                        <span>{{damageData[5]}}件</span>
                    </div>
                </div>
          </div>
          <div class="custom_flex_item_3_no hover_animat custom_bg_color_white row_box">
                <h2 class="row_title">今日盘点</h2>
                <div class="data_block" id="check_pie_wrap" style="margin-bottom:10px"></div>
                <div class="custom_flex_wrap">
                    <div class="check_num_item">
                        <span>{{checkData[0]}}件</span>
                    </div>
                    <div class="check_num_item">
                        <span>{{checkData[1]}}件</span>
                    </div>
                </div>
          </div>
      </div>

      <div class="row_wrap hover_animat custom_bg_color_white">
           <div class="row_box">
                <h2 class="row_title">最近新增资产</h2>
                <div class="table_scroll_wrap">
                    <Table style="min-width:1500px" stripe :columns="newAssetsColumn" :data="newAssetsData" size="large" :loading="tableLoading">
                        <template slot-scope="{ row }" slot="status">
                            <Tag v-if="row.status==='正常'" :color="statusColor[0]"><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='待维修'" :color="statusColor[1]"><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='维修接单'" :color="statusColor[2]"><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='待报废'" :color="statusColor[3]"><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='报废'" :color="statusColor[4]"><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='外借'" :color="statusColor[5]"><span>{{row.status}}</span></Tag>
                            <Tag v-else  color="default"><span>{{row.status}}</span></Tag>
                        </template>
                    </Table>
                </div>
           </div>
      </div>
  </div>
</template>

<script>
import Vue from "vue"
import {echartScript} from "../../../utils/needScript.js"
import { Table ,Tag ,Icon} from 'iview';
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Icon', Icon);

import countTo from 'vue-count-to';

import damageOption from "../../../assets/js/chart/echarts/damage_pie.js"
import checkOption from "../../../assets/js/chart/echarts/check_pie.js"
import statusColor from "../../../assets/data/statusColor.js"
import assetsConfigArr from "../../../assets/data/assetsConfigArr.js"

import NET_PORT from "../../../api/port.js"
import { getToken } from '../../../utils/auth.js';
export default {
  name:'home',
  components:{countTo},
  props:{},
  data(){
    return {
        statusColor:statusColor,
        user:this.$store.state.user,
        deviceMessageArr:[],
        deviceMessageNum:{
            lose:0,
            beyond:0,
        },
         //创建socket
        websock: null,
        
        tableLoading:true,

        summaryList:[
            {
                value:0,
                text:'资产总额',
                unit:'元',
                color:'#2d8cf0',
                icon:'md-cube',
            },
            {
                value:0,
                text:'资产总量',
                unit:'件',
                color:'#19be6b',
                icon:'ios-pie',
            },
            {
                value:0,
                text:'维修接单',
                unit:'件',
                color:'#f3a32a',
                icon:'md-hammer',
            },
            {
                value:0,
                text:'总盘点数',
                unit:'件',
                color:'#9a66e4',
                icon:'ios-contrast',
            },
           
        ],
      
       
        newAssetsColumn: [
           
            {
                title: '资产编号',
                key: 'assetID',
                
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
              title:'规格',
              key:'specification',
            },
            {
                title: '型号',
                key: 'type'
            },
           
            {
                title: '存放地点',
                key: 'location',
            },
            {
                title: '产地',
                key: 'placeoforigin',
            },
            {
                title: '品牌',
                key: 'brand'
            },
          
            {
                title: '部门编码',
                key: 'departmentcode',
            },
            {
                title: '部门名称',
                key: 'departmentroom'
            },
            {
                title: '大科室编码',
                key: 'homeofficenumber'
            },
            {
                title: '大科室名称',
                key: 'homeofficename'
            },
           
          
            {
                title: '所在科室名称',
                key: 'locDept'
            },
           
            {
                title: 'SN',
                key: 'sn'
            },
           
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:120
            },
        ],

        assetsConfigArr:assetsConfigArr,
        
        newAssetsData: [],

        damageChart:null,
        checkChart:null,

        damageData:[0,0,0,0,0,0],
        checkData:[0,0,0],
    }
  },
  watch:{
      isCollapsed(){
            setTimeout(()=>{
                    this.damageChart.resize();
                    this.checkChart.resize();
            },500)
      },

   
  },
  computed:{
        isCollapsed(){
          return this.$store.state.isCollapsed
        },
      
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
            this.deviceMessageArr=this.transWebsocketFn(e.data)

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
        setAllOldFn(){
            this.damageChart = echarts.init(document.getElementById('damage_ratio_wrap'));
            let totalNum=this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5];
            let normalRatio=parseFloat(Number((this.damageData[0]/totalNum)*100).toFixed(1))
            let tobescrapRatio=parseFloat(Number((this.damageData[1]/totalNum)*100).toFixed(1))
            let maintainRatio=parseFloat(Number((this.damageData[2]/totalNum)*100).toFixed(1))
            let scrapRatio=parseFloat(Number((this.damageData[3]/totalNum)*100).toFixed(1))
            let hasScrapRatio=parseFloat(Number((this.damageData[4]/totalNum)*100).toFixed(1))
            let lendRatio=parseFloat((100-(normalRatio+tobescrapRatio+maintainRatio+scrapRatio+hasScrapRatio)).toFixed(1))
            damageOption.series[0].data=[
              {
                value:normalRatio,
                name:'正常',
              },
              {
                value:tobescrapRatio,
                name:'待维修',
              },
              {
                value:maintainRatio,
                name:'维修接单',
              },
              {
                value:scrapRatio,
                name:'待报废',
              },
              {
                value:hasScrapRatio,
                name:'报废',
              },
              {
                value:lendRatio,
                name:'外借',
              },
            ]
            this.damageChart.setOption(damageOption)
        },

        setCheckFn(){
            this.checkChart = echarts.init(document.getElementById('check_pie_wrap'));
            let totalNum=this.checkData[0]+this.checkData[1];
            let hasCheckRatio=parseFloat(Number((this.checkData[0]/totalNum)*100).toFixed(1))
            let noCheckRatio=parseFloat((100-hasCheckRatio).toFixed(1))
            checkOption.series[0].data=[
              {
                value:hasCheckRatio,
                name:'今日已盘',
              },
              {
                value:noCheckRatio,
                name:'今日未盘',
              },
            
            ]
            this.checkChart.setOption(checkOption)
        },

        getDataFn(){
            this.$Loading.start()
            this.$axios.post(NET_PORT.assetsQuery)
            .then((res)=>{
                let initData=res.data;
                let total=Number(initData.pop().total);
                
                for(let i=0;i<initData.length;i++){
                    let configArr=[];
                    for(let k in initData[i]){
                        if(initData[i][k]===''||initData[i][k]==='null'){
                           initData[i][k]='--'; 
                        };
                        configArr.push(k);
                    }

                    this.assetsConfigArr.forEach((m,n)=>{
                      let obj=configArr.find((item,index)=>{
                        return item===m;
                      })

                      if(!obj){
                        initData[i][m]='--'
                      }
                    })
                }
                this.newAssetsData=initData
                this.$Loading.finish();
                this.tableLoading=false;

            })
            .catch((err)=>{
                this.$Loading.error();
            })
            
            let datas=[
                {username:this.user}
            ]

            this.$axios.post(NET_PORT.assetsTotalNum)
            .then((res)=>{
                let obj=res.data[0]
                if(obj.msg==='failed'){

                }else{

                    this.summaryList.forEach((item,index)=>{
                        switch(item.text){
                            case "资产总额" :
                                item.value=obj.aggregate;
                            break;
                            case '资产总量' :
                                item.value=obj.examined+obj.unexamined;
                            break;
                            case '维修接单' :
                                item.value=obj.maintain;
                            break;
                            case '总盘点数' :
                                item.value=obj.totalCheck ? obj.totalCheck : 0;
                            break;
                            default:
                                return
                        }
                    })

                    this.damageData=[
                        obj.normal ? obj.normal : 0, //正常
                        obj.tobescrap ? obj.tobescrap : 0, //待维修
                        obj.maintain ? obj.maintain : 0, //维修接单
                        obj.scrap ? obj.scrap : 0, //待报废
                        obj.hasScrap ? obj.hasScrap : 0, //报废
                        obj.lend ? obj.lend : 0, //外借
                        // 0,
                        // 0,
                    ]
                    
                    this.checkData=[
                        obj.examined,
                        obj.unexamined,
                        obj.totalCheck,
                    ]

                    this.setAllOldFn();
                    this.setCheckFn();
                }
       
               
            })
            .catch((err)=>{
                // console.log(err)
            })
            
        },

        afterResizeFn(){
            this.damageChart.resize();
            this.checkChart.resize(); 
        },

      
  },
  created(){
     this.initWebSocket()
  },
  mounted(){
        //获取数据
        this.$nextTick(()=>{
            echartScript()
            .then(()=>{
                this.getDataFn();
                //窗口缩放图表也跟着缩放
                window.addEventListener('resize',this.afterResizeFn,true)
            })
        })
  },
  beforeDestroy(){
      this.websock.close();
      window.removeEventListener('resize',this.afterResizeFn,true)
  }
    
}
</script>


<style lang='scss' scoped>
    @import "../../../assets/scss/web/common/device_message.scss";
    @import "../../../assets/scss/web/home.scss";
    
</style>
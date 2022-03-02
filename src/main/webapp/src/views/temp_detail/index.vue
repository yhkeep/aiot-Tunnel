<template>
  <div id='temp_detail_wrap'>
        <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>
        <h1 class="assets_title">{{tempData.name}} - {{mac}}</h1>
        <div class="custom_flex_wrap">
                <div class="custom_flex_item_2 hover_animat custom_bg_color_white row_box">
                        <h2 class="col_title">温度监测</h2>
                        <div id="temp_box">
                            <h2>{{tempData.temp ? tempData.temp : '--'}}℃</h2>
                            <Progress :percent="100" status="success" :stroke-width="8" hide-info/>
                        </div>
                </div>
                
                <div class="custom_flex_item_2 hover_animat custom_bg_color_white row_box">
                        <h2 class="col_title">湿度监测</h2>
                        <div id="humi_box">
                            <h2>{{tempData.humi ? tempData.humi : '--'}}%</h2>
                            <Progress :percent="parseInt(tempData.humi)"  :stroke-width="8" hide-info/>
                        </div>
                </div>
        </div>

        <div class="row_wrap hover_animat custom_bg_color_white utilization_wrap">
                <div class="row_box">
                    <h2 class="row_title">今日温度</h2>
                    <div id="today_temp_box"></div>
                </div>
        </div>
        <div class="row_wrap hover_animat custom_bg_color_white utilization_wrap">
                <div class="row_box">
                    <h2 class="row_title">今日湿度</h2>
                    <div id="today_humi_box"></div>
                </div>
        </div>
        <div class="row_wrap hover_animat custom_bg_color_white utilization_wrap">
                <div class="row_box">
                    <h2 class="row_title">一周温度</h2>
                    <div id="week_temp_box"></div>
                </div>
        </div>

        <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                    <h2 class="row_title">历史明细数据</h2>
                    <div class="data_table_top clearfix">
                    <div class="data_table_top_left">
                       <DatePicker type="datetimerange" placeholder="选择日期范围" style="width: 500px" @on-clear="dateClearFn" @on-ok="dateOkFn" @on-change="dateRangeFn"></DatePicker>
                    </div>
                    <div class="data_table_top_right">
                        <div class="pagination_wrap">
                            <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                        </div>
                    </div>
                    </div>

                    <Table style="min-width:1500px"  stripe :columns="tempDetailColumn" :data="tempDetailData" size='large' ref="table" :loading="tableLoading">
                        <template slot-scope="{ row }" slot="name">
                            <span>{{row.name? row.name : '--'}}</span>
                        </template>
                        <template slot-scope="{ row }" slot="temp">
                            <span>{{row.temp}}℃</span>
                        </template>
                        <template slot-scope="{ row }" slot="humi">
                            <span>{{row.humi}}%</span>
                        </template>
                    </Table>
            </div>
        </div>
  </div>
</template>

<script>

import Vue from "vue"
import {echartScript} from "../../utils/needScript.js"
import {Icon,Table ,Tag ,DatePicker,Select,Option,Page,Input,Button,Progress} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('DatePicker', DatePicker);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Progress',Progress);


import { temperatureOption, humidityOption, weekTemOption} from "../../assets/js/chart/echarts/temp_bar.js"
export default {
  name:'temp_detail',
  components:{},
  props:{},
  data(){
    return {
        mac:'',
        queryData:{
            mac:'',
            timeStart:'',
            timeEnd:'',
        },

        tableLoading:false,
        totalCount: 0,
        pageSize: 10,
        currentPage: 1,

        dateValue:'',
        tempData:{
            name:'',
            temp:'',
            humi:'',
        },

         tempDetailColumn:[

          {
            key: 'createdTime',
            title: '记录时间'
          },
					{
            key: 'name', 
            title: '设备名称',
            slot:'name'
          },
					{
            key: 'temp', 
            title: '温度',
            slot:'temp'
          },
					{
            key: 'humi', 
            title: '湿度',
            slot:'humi'
          }
        ],

        tempDetailData:[],
        todayTempChart:null,
        todayHumiChart:null,
        weekTempChart:null,


    }
  },
 watch:{
        isCollapsed(){
                setTimeout(()=>{
                    this.todayTempChart.resize()
                    this.todayHumiChart.resize()
                    this.weekTempChart.resize()
                },500)
        },
  },
  computed:{
        isCollapsed(){
            return this.$store.state.isCollapsed
        }
  },
  methods:{
    returnFn(){
        this.$router.go(-1)
    },

     dateRangeFn(value){
      //设置日期范围
      this.queryData.mac=this.mac
      this.queryData.timeStart=value[0]
      this.queryData.timeEnd=value[1]
    },

    dateOkFn(){
      
      this.getDetailFn();

    },

    dateClearFn(){
      this.tempDetailData=[];
      this.currentPage=1;
      this.totalCount=0;

    },

    getDetailFn(){
        this.tableLoading=true,
        this.$axios.get(process.env.API_HOST+'luzhou/assetManagement/param?currentPage='+this.currentPage+'&startime='+this.queryData.timeStart+"&endtime="+this.queryData.timeEnd+'&mac='+this.mac)
        .then((res)=>{
          // console.log(res.data)
          var arr=[]

          var totalObj=res.data[res.data.length-1];

          res.data.splice(res.data.length-1,1);
          // console.log(totalObj)
          if(res.data.length){
            res.data.forEach((item,index)=>{
              // console.log(item)
              var obj={
                createdTime:item.currentTime,
                name:this.tempData.name,
                temp:item.temperature,
                humi:item.humidity,
              }
              arr.push(obj)
            })
          }
          // console.log(arr)
          this.tableLoading=false;
          this.totalCount=Number(totalObj.total)
          this.tempDetailData=arr;
        })
        .catch((err)=>{
          console.log(err)
        })
    },
 

     changePageFn(page){
      this.currentPage=page;
      this.getDetailFn();
    },

    setTempDetailFn(tempData){
      // console.log(tempData)
       // 基于准备好的dom，初始化echarts实例
            this.todayTempChart = echarts.init(document.getElementById('today_temp_box'));
            this.todayHumiChart = echarts.init(document.getElementById('today_humi_box'));
            this.weekTempChart = echarts.init(document.getElementById('week_temp_box'));
            
            //今日温度
            var todayTempOption=temperatureOption
            todayTempOption.color=["#f6724e",'#3cc59a']
            todayTempOption.xAxis[0].data=tempData.hourSummary.map(s => `${s.hour}:00`);
            todayTempOption.series[0].data=tempData.hourSummary.map(s => s.topTemp);
            todayTempOption.series[1].data=tempData.hourSummary.map(s => s.bottomTemp);
            
            //今日湿度
            var todayHumiOption=humidityOption
            todayHumiOption.color=["#2d8cf0",'#35c96e']
            todayHumiOption.xAxis[0].data=tempData.hourSummary.map(s => `${s.hour}:00`);
            todayHumiOption.series[0].data=tempData.hourSummary.map(s => s.topHum);
            todayHumiOption.series[1].data=tempData.hourSummary.map(s => s.bottomHum);

            //一周温度
            var weekTempOption=weekTemOption
            weekTempOption.color=["#f6724e",'#3cc59a']
            weekTempOption.xAxis.data=tempData.daySummary.map(d => d.date);
            weekTempOption.series[0].data=tempData.daySummary.map(d => d.topTemp);
            weekTempOption.series[1].data=tempData.daySummary.map(d => d.bottomTemp);
            

               
                this.todayTempChart.setOption(todayTempOption)
                this.todayHumiChart.setOption(todayHumiOption)
                this.weekTempChart.setOption(weekTempOption)
    },

    getDataFn(){
      this.$Loading.start()
      this.$axios.get(process.env.API_HOST+'luzhou/daySummary?mac='+this.mac)
      .then((res)=>{
          console.log(res)
        var result=res.data
        this.tempData.daySummary=result.daySummary;
        this.tempData.hourSummary=result.hourSummary;
        this.$Loading.finish();
         this.$nextTick(()=>{
        
              echartScript()
              .then(()=>{
                var datas={
                  daySummary:[],
                  hourSummary:[],
                };
              


                result[0].daySummary.forEach((item,index)=>{
                  if(JSON.stringify(item)==="{}"){

                  }else{
                    var obj={
                      date:item.date.split(' ')[0],
                      mac:item.mac,
                      bottomTemp:Number(item.mintemperature).toFixed(2),
                      topTemp:Number(item.toptemperature).toFixed(2),
                      bottomHum:Number(item.minhumidity).toFixed(2),
                      topHum:Number(item.tophumidity).toFixed(2)
                    }
                    datas.daySummary.push(obj)
                  }
                })

                result[0].hourSummary.forEach((item,index)=>{
                  if(JSON.stringify(item)==="{}"){

                  }else{

                    var hourData=(item.date.split(' ')[1]).split(':')[0];
                    if(hourData.charAt(0)==='0'){
                      hourData=hourData.charAt(1)
                    }
                    // console.log(typeof hourData.charAt(0))
                    // console.log(hourData)
                    var obj={
                      hour:Number(hourData),
                      mac:item.mac,
                      bottomTemp:Number(item.mintemperature).toFixed(2),
                      topTemp:Number(item.toptemperature).toFixed(2),
                      bottomHum:Number(item.minhumidity).toFixed(2),
                      topHum:Number(item.tophumidity).toFixed(2)
                    }
                    datas.hourSummary.push(obj)
                  }
                })

                datas.daySummary.reverse();
                datas.hourSummary.reverse();
                this.setTempDetailFn(datas)
                
              })
              
          })
      })
      .catch((err)=>{
        console.log(err)
      })
    },

    windowResizeFn(){
        window.addEventListener('resize',()=>{
                    this.todayTempChart.resize()
                    this.todayHumiChart.resize()
                    this.weekTempChart.resize()
        })
    }

  },
  created(){
        this.mac=this.$route.params.mac
        var result=window.sessionStorage.getItem('tempObj')
        if(result){
            var obj=JSON.parse(result)
            this.tempData.name=obj.name;
            this.tempData.temp=obj.temp;
            this.tempData.humi=obj.humi;

            //获取数据
            this.getDataFn();
            
        }else{
            this.$Message.error({
                content:"未找到网关信息，将返回上一页!",
                duration:1,
            })

            setTimeout(()=>{
                this.$router.go(-1)
            },1000)
        }

        
  },
  mounted(){
    this.windowResizeFn();
  },

  beforeDestroy(){
      this.$Loading.finish();
      window.sessionStorage.removeItem('tempObj')
  }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/table.css"></style>
<style scoped>
    @import "../../assets/css/temp_detail.css"
</style>
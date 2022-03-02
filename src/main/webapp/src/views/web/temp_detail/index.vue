<template>
  <div id='temp_detail_wrap'>
        <h1 class="assets_title">{{tempData.freezername}} - {{mac}}</h1>
        <!-- <div class="custom_flex_wrap">
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
        </div> -->

        <div class="hover_animat custom_bg_color_white">
                <div class="row_box">
                    <h2 class="row_title">今日温度</h2>
                   <!-- <div style="overflow:auto"> -->
                      <!-- <div style="min-width:1200px;"> -->
                        <div id="today_temp_box"></div>
                      <!-- </div> -->
                   <!-- </div> -->
                </div>
        </div>
        <div class="row_wrap hover_animat custom_bg_color_white">
                <div class="row_box">
                    <h2 class="row_title">今日湿度</h2>
                    <div id="today_humi_box"></div>
                </div>
        </div>
        <div class="row_wrap hover_animat custom_bg_color_white">
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
                          <!-- <div class="pagination_wrap">
                              <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                          </div> -->
                          <Button shape="circle" type="success" icon="md-archive" @click="exportExcelFn">导出excel</Button>
                      </div>
                    </div>

                    
                    <div class="table_scroll_wrap">
                      <Table style="min-width:1000px"  stripe :columns="tempDetailColumn" :data="tempDetailData" size='large' ref="table" :loading="tableLoading">
                         
                          <template slot-scope="{ row }" slot="temp">
                              <span>{{row.temp? row.temp+'℃' : '--'}}</span>
                          </template>
                          <template slot-scope="{ row }" slot="humi">
                              <span>{{row.humi? row.humi+'%' : '--'}}</span>
                          </template>
                          <template slot-scope="{ row }" slot="electric">
                              <span>{{row.electric? row.electric+'%' : '--'}}</span>
                          </template>
                      </Table>
                    </div>
                    <div class="data_table_pagination_wrap">
                        <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                    </div>
            </div>
        </div>
  </div>
</template>

<script>

import Vue from "vue"
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

// import {echartScript} from "../../../utils/needScript.js"
// import * as echarts from 'echarts';

import * as echarts from 'echarts/core';
import {
    LineChart,
} from 'echarts/charts';
import {
    TitleComponent,
    TooltipComponent,
    ToolboxComponent,
    MarkLineComponent,
    LegendComponent,
    GridComponent,
} from 'echarts/components';

import {
    CanvasRenderer
} from 'echarts/renderers';

echarts.use(
    [TitleComponent,TooltipComponent,ToolboxComponent,MarkLineComponent,LegendComponent,GridComponent,LineChart, CanvasRenderer]
);

import { temperatureOption, humidityOption, weekTemOption} from "../../../assets/js/chart/echarts/temp_bar.js"

import NET_PORT from "../../../api/port.js"

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

        tempData:{
            freezername:'',
            temp:'',
            humi:'',
        },

        tempDetailColumn:[

          {
            key: 'createdTime',
            title: '记录时间'
          },
				
					{
            key: 'temp', 
            title: '温度（℃）',
            slot:'temp'
          },
					{
            key: 'humi', 
            title: '湿度（%）',
            slot:'humi'
          },
					{
            key: 'electric', 
            title: '电量（%）',
            slot:'electric'
          },
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
        this.$axios.get(NET_PORT.tempHistory+'?currentPage='+this.currentPage+'&startime='+this.queryData.timeStart+"&endtime="+this.queryData.timeEnd+'&mac='+this.mac)
        .then((res)=>{
          let arr=[]
          let totalObj=res.data[res.data.length-1];
          res.data.splice(res.data.length-1,1);
          if(res.data.length){
            res.data.forEach((item,index)=>{
              let obj={
                createdTime:item.currentTime,
                name:this.tempData.name,
                temp:item.temperature,
                humi:item.humidity,
                electric:item.electric,
              }
              arr.push(obj)
            })
          }
          this.tableLoading=false;
          this.totalCount=Number(totalObj.total)
          this.tempDetailData=arr;
        })
        .catch((err)=>{
          // console.log(err)
        })
    },
 

    changePageFn(page){
      this.currentPage=page;
      this.getDetailFn();
    },

    setTempDetailFn(tempData){
      // 基于准备好的dom，初始化echarts实例
      this.todayTempChart = echarts.init(document.getElementById('today_temp_box'));
      this.todayHumiChart = echarts.init(document.getElementById('today_humi_box'));
      this.weekTempChart = echarts.init(document.getElementById('week_temp_box'));

      let todayDate=this.$Moment().format('YYYY-MM-DD');
      let todayData=tempData.find((item,index)=>{
        return item.currentTime===todayDate
      })

      let weekData=[];

      tempData.forEach((item,index)=>{
        item.data.forEach((m,n)=>{
          weekData.push(m);
        })
      })

      //今日温度
      let todayTempOption=temperatureOption
      todayTempOption.color="#5c7bd9"
      todayTempOption.title.text=this.tempData.freezername+" - 今日温度"
      todayTempOption.xAxis.data=todayData.data.map(s => (s.currentTime.split(' ')[1]).split(':')[0]+':'+(s.currentTime.split(' ')[1]).split(':')[1]);
      todayTempOption.series[0].data=todayData.data.map(s => s.temperature);

      let todayTempArr=[];
      for(let i=0;i<todayData.data.length;i++){
        todayTempArr.push(todayData.data[i].temperature)
      }
      todayTempArr.push(this.tempData.tempLimitTop)
      todayTempArr.push(this.tempData.tempLimitBottom);
      todayTempOption.yAxis.max=Math.max(...todayTempArr);
      todayTempOption.yAxis.min=Math.min(...todayTempArr);
      
      todayTempOption.series[0].markLine.data[0].yAxis=Number(this.tempData.tempLimitTop);
      todayTempOption.series[0].markLine.data[1].yAxis=Number(this.tempData.tempLimitBottom);

     
      
      //今日湿度
      let todayHumiOption=humidityOption
      todayHumiOption.color="#327e2b"
      todayHumiOption.title.text=this.tempData.freezername+" - 今日湿度"
      todayHumiOption.xAxis.data=todayData.data.map(s => (s.currentTime.split(' ')[1]).split(':')[0]+':'+(s.currentTime.split(' ')[1]).split(':')[1]);
      todayHumiOption.series[0].data=todayData.data.map(s => s.humidity);

      let todayHumiArr=[];
      for(let i=0;i<todayData.data.length;i++){
        todayHumiArr.push(todayData.data[i].humidity)
      }
      todayHumiArr.push(this.tempData.humiLimitTop)
      todayHumiArr.push(this.tempData.humiLimitBottom);
      todayHumiOption.yAxis.max=Math.max(...todayHumiArr);
      todayHumiOption.yAxis.min=Math.min(...todayHumiArr);

      todayHumiOption.series[0].markLine.data[0].yAxis=Number(this.tempData.humiLimitTop);
      todayHumiOption.series[0].markLine.data[1].yAxis=Number(this.tempData.humiLimitBottom);

      //一周温度
      let weekTempOption=weekTemOption
      weekTempOption.color="#930c89"
      weekTempOption.title.text=this.tempData.freezername+" - 一周温度"
      weekTempOption.xAxis.data=weekData.map(s => s.currentTime);
      weekTempOption.series[0].data=weekData.map(s => s.temperature);

      let weekTempArr=[];
      for(let i=0;i<todayData.data.length;i++){
        weekTempArr.push(todayData.data[i].temperature)
      }
      weekTempArr.push(this.tempData.tempLimitTop)
      weekTempArr.push(this.tempData.tempLimitBottom);
      weekTempOption.yAxis.max=Math.max(...weekTempArr);
      weekTempOption.yAxis.min=Math.min(...weekTempArr);

      weekTempOption.series[0].markLine.data[0].yAxis=Number(this.tempData.tempLimitTop);
      weekTempOption.series[0].markLine.data[1].yAxis=Number(this.tempData.tempLimitBottom);
          
      this.todayTempChart.setOption(todayTempOption)
      this.todayHumiChart.setOption(todayHumiOption)
      this.weekTempChart.setOption(weekTempOption)
    },

    getChartFn(){
      this.$axios.get(NET_PORT.tempSummary+this.mac)
      .then((res)=>{
        let arr=[];
        res.data.forEach((item,index)=>{
          let time=this.$Moment(item.currentTime).format('YYYY-MM-DD')
          let currentObj=arr.find((m,n)=>{
            return m.currentTime===time
          })
          if(!currentObj){
            let obj={
              currentTime:time,
              data:[],
            }

            obj.data.push(item)
            arr.push(obj)
          }else{
            currentObj.data.push(item);
          }
        })

        arr=arr.sort(sortFn)
        function sortFn(a,b){
          return new Date(a.currentTime).getTime()-new Date(b.currentTime).getTime()
        }

        arr.forEach((item,index)=>{
          item.data=item.data.sort(sortFn)
        })
        this.setTempDetailFn(arr)
       
      })
      .catch((err)=>{

      })
    },

    getDataFn(){
  
      this.$Loading.start()
      this.$axios.get(NET_PORT.tempEditDetail+'?mac='+this.mac)
      .then((res)=>{
          let result=res.data[0]
          this.tempData={
              freezernumber:result.freezernumber ? result.freezernumber : '',
              freezername:result.freezername,
              department:result.department,
              location:result.location ? result.location : '',
              mac:result.mac ? result.mac : '',
              type:result.type ? result.type : '',
              tempLimitTop:result.temperaturefitted.split('~')[1],
              tempLimitBottom:result.temperaturefitted.split('~')[0],
              humiLimitTop:result.humidityfitted.split('~')[1],
              humiLimitBottom:result.humidityfitted.split('~')[0],
          }

          this.$Loading.finish();

          this.getChartFn();
      })
      .catch((err)=>{
          this.$Loading.error();
      })

    },

    afterResizeFn(){
        this.todayTempChart.resize()
        this.todayHumiChart.resize()
        this.weekTempChart.resize()
    },

    exportExcelFn(){
      
      if(this.queryData.timeStart===''||this.queryData.timeEnd===''){
        this.$Message.error({
            content:"请选择时间范围！",
            duration:2,
        })
      }else {
        let start=this.$Moment(this.queryData.timeStart).unix()*1000
        let end=this.$Moment(this.queryData.timeEnd).unix()*1000
        let differ=end-start
        var days=Math.floor(differ/(24*3600*1000))
        if(days>180){
          this.$Message.error({
              content:"时间范围不能超过6个月！",
              duration:2,
          })
        }else{
              let data={
                mac:this.mac,
                startime:this.queryData.timeStart,
                endtime:this.queryData.timeEnd,
              }
                this.$axios.get(NET_PORT.tempExport+'?startime='+this.queryData.timeStart+"&endtime="+this.queryData.timeEnd+'&mac='+this.mac,{responseType: 'blob'})
                .then((res)=>{
                  let that=this;
                      let blob = res.data;
                      let reader = new FileReader();
                      reader.readAsDataURL(blob);
                      reader.onload = function (e) {
                          let a = document.createElement("a");
                          a.download = that.tempData.freezername + "历史数据.xls";
                          a.href = e.target.result;
                          document.documentElement.appendChild(a);
                          document.documentElement.removeChild(a)
                          a.click();
                      };
                })
                .catch((err)=>{
                  console.log(err)
                })
        }
      }
      
    },

  },
  created(){
        this.mac=this.$route.params.mac
        this.getDataFn();
  },
  mounted(){
    window.addEventListener('resize',this.afterResizeFn,true)
    document.getElementById('content_wrap').scrollTop=0;
  },

  beforeDestroy(){
      window.sessionStorage.removeItem('tempObj')
      window.removeEventListener('resize',this.afterResizeFn,true)
  }
}
</script>


<style lang='scss' scoped>
  @import "../../../assets/scss/web/temp_detail.scss";
  @import '../../../assets/scss/web/common/table.scss';
</style>
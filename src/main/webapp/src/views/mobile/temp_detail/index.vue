<template>
  <div class='mobile_temp_detail_wrap manage_list_wrap'>
      <div class="assets_detail_title">
          <h3>{{tempData.freezername}} - {{mac}}</h3>
      </div>

      <div class="temp_detail_box">
        <div class="temp_detail_chart_box">
          <Collapse v-model="activeNames">
            <CollapseItem title="今日温度" name="1">
                <div class="mobile_chart_box" id="today_temp_box"></div>
            </CollapseItem>
            <CollapseItem title="今日湿度" name="2">
                <div class="mobile_chart_box" id="today_humi_box"></div>
            </CollapseItem>
            <CollapseItem title="一周温度" name="3">
                <div class="mobile_chart_box" id="week_temp_box"></div>
            </CollapseItem>
          </Collapse>
        </div>
        <div class="temp_detail_history_box">
          <div class="van-cell">
            <div class="van-cell__title">
              <span>温湿度历史</span>
            </div>
          </div>

          <div class="query_data_wrap_open">
              <div class="query_data_box">
                    <Field @click="timeModalShow=true;currentMode='timeStart'" label="开始时间" readonly  v-model="queryData.timeStart" placeholder="请输入要查询的开始时间"/>
                    <Field @click="timeModalShow=true;currentMode='timeEnd'" label="结束时间" readonly  v-model="queryData.timeEnd" placeholder="请输入要查询的结束时间"/>
              </div>
          
              <div class="submit_button_wrap">
                  <Button class="submit_button" type="info" round  @click="getDetailFn">搜索</Button>
              </div>
          </div>

          <div class="assets_table_wrap">
            <div class="assets_table_wrap_content  clearfix">
              <div class="assets_table_wrap_left">
                  <ul class="assets_table_ul" >
                      <li class="assets_table_li assets_table_li_name">
                        <div class="assets_table_item"><p>序号</p></div>
                      </li>
                      <li class="assets_table_li" v-for="(item,index) in tempDetailData" :key="index">
                        <div class="assets_table_item"><p>{{index+1}}</p></div>
                      </li>
                  </ul>
              </div>
              <div class="assets_table_wrap_right">
                  <ul class="assets_table_ul" >
                      <li class="assets_table_li assets_table_li_name clearfix" >
                            <div class="assets_table_item"><p>记录时间</p></div>
                            <div class="assets_table_item"><p>温度</p></div>
                            <div class="assets_table_item"><p>湿度</p></div>
                            <div class="assets_table_item"><p>电量</p></div>
                      </li>
                      <li class="assets_table_li clearfix" v-for="(item,index) in tempDetailData" :key="index" >
                          <div class="assets_table_item">
                            <p>{{item.createdTime? item.createdTime : '--'}}</p>
                          </div>
                         
                          <div class="assets_table_item">
                            <p>{{item.temp? item.temp+'℃' : '--'}}</p>
                          </div>
                          <div class="assets_table_item">
                            <p>{{item.humi? item.humi+'%' : '--'}}</p>
                          </div>
                          <div class="assets_table_item">
                            <p>{{item.electric? item.electric+'%' : '--'}}</p>
                          </div>
                        
                      </li>
                  </ul>
              </div>
            </div>

            <div class="loading_info_wrap">
                <Pagination 
                v-model="currentPage" 
                :total-items="totalCount" 
                :show-page-size="2"
                prev-text="上一页"
                next-text="下一页"
                force-ellipses
                mode="simple"
                @change="changePageFn"
                v-show="showPagination&&tempDetailData.length"
              />

              <Loading v-show="loadingShow" size="24px">加载中...</Loading>
              <p v-show="!loadingShow&&!tempDetailData.length">暂无数据</p>
            </div>
          </div>
        </div>
      </div>

      <Popup v-model="timeModalShow" position="bottom">
          <DatetimePicker
          v-model="currentDate"
          type="datetime"
          show-toolbar
          title="请选择时间" 
          :min-date="minDate"
          :max-date="maxDate"
          @cancel="timeModalShow = false" 
          @confirm="getTimeFn"
          />
      </Popup>

     

  </div>
</template>

<script>
import {Button,Notify,Tag,Icon,Dialog,Toast,Popup,Loading,Collapse,CollapseItem,Field,DatetimePicker,Pagination} from 'vant';
import 'vant/lib/button/style';
import 'vant/lib/notify/style';
import 'vant/lib/tag/style';
import 'vant/lib/icon/style';
import 'vant/lib/dialog/style';
import 'vant/lib/popup/style';
import 'vant/lib/toast/style';
import 'vant/lib/loading/style';
import 'vant/lib/collapse/style';
import 'vant/lib/collapse-item/style';
import 'vant/lib/field/style';
import 'vant/lib/datetime-picker/style';
import 'vant/lib/pagination/style';
import NET_PORT from "../../../api/port.js"
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
export default {
  name:'mobile_temp_detail',
  components:{
      Button,
      Tag,
      Icon,
      Dialog,
      Popup,
      Toast,
      Loading,
      Collapse,
      CollapseItem,
      Field,
      DatetimePicker,
      Pagination
  },
  data(){
    return {
      role:this.$store.state.role,
      loadingShow:false,
      showPagination:false,
       currentPage:1,
        totalCount:0,
        hasMore:true,

      minDate: new Date(2020, 0, 1),
      maxDate: new Date(2050, 12, 0),
      currentDate: new Date(),
      timeModalShow:false,
      currentMode:"",
      mac:'',
      queryData:{
          mac:'',
          timeStart:'',
          timeEnd:'',
      },

       tempData:{
            freezername:'',
            temp:'',
            humi:'',
            electric:'',
        },

        tempDetailData:[],
        todayTempChart:null,
        todayHumiChart:null,
        weekTempChart:null,
        activeNames:['1','2','3']
    }
  },
  watch:{},
  computed:{},
  methods:{
   
    changePageFn(){
      this.getDetailFn();
    },

    getDetailFn(){
        this.loadingShow=true,
        this.$axios.get(NET_PORT.tempHistory+'?currentPage='+this.currentPage+'&startime='+this.queryData.timeStart+"&endtime="+this.queryData.timeEnd+'&mac='+this.mac)
        .then((res)=>{
          let arr=[]
          let totalObj=res.data[res.data.length-1];
          res.data.splice(res.data.length-1,1);
          if(res.data.length){
            res.data.forEach((item,index)=>{
              let obj={
                createdTime:item.currentTime,
                temp:item.temperature,
                humi:item.humidity,
                electric:item.electric,
              }
              arr.push(obj)
            })
          }

          this.totalCount=Number(totalObj.total)
          this.tempDetailData=arr;
          this.loadingShow=false;
          this.showPagination=true;
         
        })
        .catch((err)=>{
          // console.log(err)
        })
    },

    getTimeFn(value){
        let date=value
        let currentYear=date.getFullYear();
        let currentMonth=date.getMonth()+1;
        let currentDate=date.getDate();
        let currentHours=date.getHours();
        let currentMinutes=date.getMinutes();
        currentMonth= currentMonth<10 ? '0'+currentMonth : currentMonth
        currentDate= currentDate<10 ? '0'+currentDate : currentDate
        currentHours= currentHours<10 ? '0'+currentHours : currentHours
        currentMinutes= currentMinutes<10 ? '0'+currentMinutes : currentMinutes
            
        this.queryData[this.currentMode]=currentYear+'-'+currentMonth+'-'+currentDate+' '+currentHours+':'+currentMinutes
        this.timeModalShow=false;

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
           
            // todayTempOption.xAxis[0].show=false;
            // todayTempOption.yAxis[0].show=false;
            // todayTempOption.yAxis[0].axisLabel.inside=true;
            
            todayTempOption.color="#5c7bd9"
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

            todayTempOption.grid.left='15';
            todayTempOption.grid.right='15';

            
            
            //今日湿度
            let todayHumiOption=humidityOption
           
            // todayHumiOption.xAxis[0].show=false;
            // todayHumiOption.yAxis[0].show=false;
            // todayHumiOption.yAxis[0].axisLabel.inside=true;

            todayHumiOption.color="#327e2b"
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

            todayHumiOption.grid.left='15';
            todayHumiOption.grid.right='15';

            

            //一周温度
            let weekTempOption=weekTemOption
            // weekTempOption.xAxis.show=false;
            // weekTempOption.yAxis.show=false;
            // weekTempOption.yAxis.axisLabel.inside=true;

            weekTempOption.color="#930c89"
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

            weekTempOption.grid.left='15';
            weekTempOption.grid.right='15';

            // weekTempOption.axisLabel={
            //     interval: 1,
            //     formatter: function (value) {
            //       return value.split('-')[2]
            //     }  
            // } 
               
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

          this.getChartFn();
      })
      .catch((err)=>{

      })

    },

  },
  created(){
    this.$emit('setTitle','温湿度明细');
    this.mac=this.$route.params.mac
    this.getDataFn();
   
  },
  mounted(){}
}
</script>
<style lang="scss" scoped>
  @import "../../../assets/scss/mobile/common/manage_list.scss";
  @import "../../../assets/scss/mobile/temp_detail.scss";
</style>
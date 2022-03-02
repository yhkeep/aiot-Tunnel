<template>
  <div class='dust_detail_wrap'>
        <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>
        <h1 class="assets_title">{{dustData.name}} - {{dustData.mac}}</h1>
        <div class="custom_bg_color_white hover_animat ">
            <!-- <div class="custom_flex_wrap custom_bg_color_white hover_animat"> -->
               <div class="row_box custom_flex_wrap  dust_total_data_wrap">
                    <div class="circle_data_wrap">
                        <div class="circle_data_box">
                        <div class="circle_data_box_center">
                                <h1>45</h1>
                                <p>PM1</p>
                        </div>
                        </div>
                    </div>
                    <div class="circle_data_wrap">
                        <div class="circle_data_box">
                            <div class="circle_data_box_center">
                                <h1>178</h1>
                                <p>PM2.5</p>
                        </div>
                        </div>
                    </div>
                    <div class="circle_data_wrap">
                        <div class="circle_data_box">
                            <div class="circle_data_box_center">
                                <h1>372</h1>
                                <p>PM10</p>
                        </div>
                        </div>
                    </div>
               </div>
            <!-- </div> -->
        </div>

        <div class="row_wrap custom_flex_wrap">
           <div class="row_box custom_bg_color_white hover_animat custom_flex_item_3">
                <h2 class="row_title">PM1最近12小时情况</h2>
                <div id="pm1Dust12Chart"></div>
           </div>
           <div class="row_box custom_bg_color_white hover_animat custom_flex_item_3">
                <h2 class="row_title">PM2.5最近12小时情况</h2>
                <div id="pm25Dust12Chart"></div>
           </div>
           <div class="row_box custom_bg_color_white hover_animat custom_flex_item_3">
                <h2 class="row_title">PM10最近12小时情况</h2>
                <div id="pm10Dust12Chart"></div>
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

                    <Table style="min-width:1500px"  stripe :columns="dustDetailColumn" :data="dustDetailData" size='large' ref="table" :loading="tableLoading">
                        <!-- <template slot-scope="{ row }" slot="name">
                            <span>{{row.name? row.name : '--'}}</span>
                        </template>
                        <template slot-scope="{ row }" slot="temp">
                            <span>{{row.temp}}℃</span>
                        </template>
                        <template slot-scope="{ row }" slot="humi">
                            <span>{{row.humi}}%</span>
                        </template> -->
                    </Table>
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
import {echartScript} from "../../../utils/needScript.js"
export default {
  name:'dust_detail',
  components:{},
  props:{},
  data(){
    return {
        dustData:{
            no:'111111',
            name:'pm测试仪器1',
            mac:'ac111111',
            position:'车间1',
            pm1Dust12Chart:null,
            pm25Dust12Chart:null,
            pm10Dust12Chart:null,
            
        },

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


        dustDetailColumn:[
            {
                key: 'createdTime',
                title: '记录时间'
            },
            {
                key: 'name', 
                title: '名称',
            },
            {
                key: 'pm1', 
                title: 'PM1',
            },
            {
                key: 'pm25', 
                title: 'PM2.5',
            },
            {
                key: 'pm10', 
                title: 'PM10',
            },
           
        ],
        dustDetailData:[],
    }
  },
    watch:{
        isCollapsed(){
                setTimeout(()=>{
                    this.pm1Dust12Chart.resize()
                    this.pm25Dust12Chart.resize()
                    this.pm10Dust12Chart.resize()
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

        setDustFn(){
            this.pm1Dust12Chart = echarts.init(document.getElementById('pm1Dust12Chart'));
            this.pm25Dust12Chart = echarts.init(document.getElementById('pm25Dust12Chart'));
            this.pm10Dust12Chart = echarts.init(document.getElementById('pm10Dust12Chart'));
            let option1 = {
                color:'#19be6b',
                 tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        
                    }
                },
                grid: {
                    show: true,
                    left: '10',
                    right: '10',
                    bottom: '10',
                    top:'10',
                    containLabel: true,
                },
                calculable: true,
                xAxis: {
                    type: 'category',
                    axisTick: { show: false },
                    data: ['21','22','23','24','1','2','3','4','5','6','7','8'],
                    axisLine: {
                        lineStyle: {
                            color: '#878787',
                        }
                    },
                },
                yAxis: {
                    type: 'value',
                    axisLine: {
                        lineStyle: {
                            color: '#878787',
                        }
                    },
                },
                series: [{
                    data: [],
                    type: 'line'
                }]
            };
            let option2=JSON.parse(JSON.stringify(option1));
            let option3=JSON.parse(JSON.stringify(option1));
            option1.series[0].data=[32, 56, 123, 112, 78, 56, 87,94,63,54,23,95];
            option2.color='#ff9900';
            option2.series[0].data=[665,282,623,634,867,535,634,645,732,631,643,734];
            option3.color='#ed4014';
            option3.series[0].data=[890,1230,932,345,895,932,679,732,932,836,698,832];

            this.pm1Dust12Chart.setOption(option1)
            this.pm25Dust12Chart.setOption(option2)
            this.pm10Dust12Chart.setOption(option3)
        },

        changePageFn(page){
            this.currentPage=page;
            // this.getDetailFn();
        },


        dateRangeFn(value){
            //设置日期范围
            this.queryData.mac=this.dustData.mac
            this.queryData.timeStart=value[0]
            this.queryData.timeEnd=value[1]
        },

        dateOkFn(){
            
            // this.getDetailFn();

        },

        dateClearFn(){
            this.tempDetailData=[];
            this.currentPage=1;
            this.totalCount=0;

        },

        getDataFn(){
            this.$nextTick(()=>{
                echartScript()
                .then(()=>{
                    this.setDustFn();
                })
            })
        },

        windowResizeFn(){
            window.addEventListener('resize',()=>{
                        this.pm1Dust12Chart.resize()
                        this.pm25Dust12Chart.resize()
                        this.pm10Dust12Chart.resize()
            })
        },


  },
  created(){
      this.getDataFn();
  },
  mounted(){
      this.windowResizeFn();
  }
}
</script>
<style lang="scss" scoped>
    @import '../../../assets/scss/web/common/table.scss';
    .dust_total_data_wrap{
        .circle_data_wrap{
            flex:1;
            text-align: center;
            &:nth-of-type(1){
                .circle_data_box{
                    border:5px solid #19be6b;
                }
            }
            &:nth-of-type(2){
                .circle_data_box{
                    
                    border:5px solid #ff9900;
                }
            }
            &:nth-of-type(3){
                .circle_data_box{
                    border:5px solid #ed4014;
                }
            }
            .circle_data_box{
                position: relative;
                display: inline-block;
                width:140px;
                height:140px;
                border-radius: 50%;
                
                .circle_data_box_center{
                    position: absolute;
                    top:50%;
                    left:50%;
                    transform: translate(-50%,-50%);
                    h1{
                        font-size:30px;
                    }
                    p{
                        font-size:16px;
                    }
                }
            }
        }
    }

    #pm1Dust12Chart,#pm25Dust12Chart,#pm10Dust12Chart{
        height:200px;
    }
   
</style>
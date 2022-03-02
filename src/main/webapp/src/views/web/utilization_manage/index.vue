<template>
  <div class='utilization_manage_wrap'>
        <div class="custom_flex_wrap">
            <div class="custom_flex_item_2 hover_animat custom_bg_color_white row_box">
                    <h2 class="row_title">重点关注设备动用率</h2>
                    <div class="data_block">
                        <div id="importDevice_01_line">
                            
                        </div>
                        <div id="importDevice_02_line">
                            
                        </div>
                    </div>
            </div>
            
            <div class="custom_flex_item_2 hover_animat custom_bg_color_white row_box">
                    <h2 class="row_title">全院资产折旧率</h2>
                    <div class="data_block" id="all_utili_wrap">

                    </div>
            </div>
        </div>

        <div class="row_wrap hover_animat custom_bg_color_white utilization_wrap">
                <div class="row_box">
                    <h2 class="row_title">设备最近一月动用率</h2>
                    <div id="utilization_line"></div>
                </div>
        </div>

        <div class="hover_animat custom_bg_color_white row_wrap">
            <div class="row_box">
                <h2 class="row_title">按条件筛选</h2>
                <div class="form_wrap">
                    <div class="form_item">
                            <Row :gutter="20">
                                <Col span="12">
                                    <h3>关键字</h3>
                                    <Input v-model="queryData.name" placeholder="关键字" />
                                </Col>
                                <Col span="12">
                                    <h3>所属部门</h3>
                                    <Select v-model="queryData.depart">
                                        <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </Col>
                            
                            </Row>
                    </div>

                    
                </div>
                <div class="submit_btn_wrap">
                        <Button type="primary" @click="searchUtiliFn">搜索</Button>
                        <Button type="error"  @click="clearUtiliFn">清空</Button>
                </div>
            </div>
        </div>

        <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                    <h2 class="row_title">动用率明细表</h2>
                    <div class="data_table_top clearfix">
                    <div class="data_table_top_left">
                       <Button type="success" icon="md-archive" @click=exportExcelFn>导出动用率总表</Button>
                    </div>
                    <div class="data_table_top_right">
                        <div class="pagination_wrap">
                            <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                        </div>
                    </div>
                    </div>

                    <div class="table_scroll_wrap">
                        <Table style="min-width:1500px" stripe :columns="utiliDetailColumn" :data="utiliDetailData" size='large' :loading="tableLoading" >
                            <template slot-scope="{row}" slot="important">
                            <Icon  type="md-heart" size="20" :color="row.important ? '#f16846' : '#b5b5b5'"/>
                            </template>
                        
                            <template slot-scope="{ row }" slot="status">
                                <Tag v-if="row.status==='在用'" color="success">在用</Tag>
                                <Tag v-else-if="row.status==='待机'" color="warning">待机</Tag>
                                <Tag v-else  color="error">{{row.status}}</Tag>
                            </template>

                            <template slot-scope="{row}" slot="operation">
                                <Button @click="toUtiliDetailFn(row.id)">查看</Button>
                                <Button>编辑</Button>
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
import { Row,Col,Icon,Table ,Tag ,Select,Option,Page,Input,Button} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);

import {allUtilizationOption} from "../../../assets/js/chart/echarts/utilization_line.js"
import importDeviceOption from "../../../assets/js/chart/echarts/importDevice_line.js"
import allUtiliOption from "../../../assets/js/chart/echarts/allUtili_pie.js"

import departData from "../../../assets/data/depart.js"

export default {
  name:'utilization_manage',
  components:{},
  props:{},
  data(){
    return {
         queryData:{
        name:'',
        depart:'',
      },

      departData:departData,

      tableLoading:false,

      dateValue:'',


      queryDataNow:{},

      totalCount: 0,
      pageSize: 10,
      currentPage: 1,

       utiliDetailColumn:[
            {key: 'important',title: '重点关注' ,slot:'important'},
            {key:'no',title:'编号'},
            {key:'name', title:'名称'},
            {key: 'depart', title: '科室'},
            {key:'instrumentNum',title:'检测仪编号'},
            {key: 'electricity', title: '电流(A)'},
            {key: 'voltage',title:'电压(V)'},
            {key: 'power', title: '功率(W)'},
            {key: 'utilization', title:'当前动用率(%)'},
            {key: 'carbon', title: '碳排放(KG)'},
            {key: 'status', title:'状态',slot:'status',width:80},
            {key: 'operation', title:'编辑',slot:'operation',width:160},
      ],

      utiliDetailData:[
        {
            id: 13238,
            important:true,
            no: "190009990404", 
            name: "格兰仕空调", 
            depart: "某科室", 
            instrumentNum: 174249, 
            electricity: 1000,
            voltage: 220,
            power:200,
            utilization:33,
            carbon: "23",
            status: "在用",
        },
        {
            id: 13478,
            important:false,
            no: "190009990404", 
            name: "格兰仕空调", 
            depart: "某科室", 
            instrumentNum: 174249, 
            electricity: 1000,
            voltage: 220,
            power:200,
            utilization:33,
            carbon: "23",
            status: "待机",
        },
        {
            id: 32445,
            important:true,
            no: "190009990404", 
            name: "格兰仕空调", 
            depart: "某科室", 
            instrumentNum: 174249, 
            electricity: 1000,
            voltage: 220,
            power:200,
            utilization:33,
            carbon: "23",
            status: "关机",
        },
      ],

      utilizationChart:null,
      impDevOneChart:null,
      impDevTwoChart:null,
      allUtiliChart:null,

    }
  },
  watch:{
     isCollapsed(){
            setTimeout(()=>{
                    this.utilizationChart.resize();
                    this.impDevOneChart.resize();
                    this.impDevTwoChart.resize();
                    this.allUtiliChart.resize();
            },500)
      },
  },
  computed:{
      isCollapsed(){
          return this.$store.state.isCollapsed
      }
  },
  methods:{

    

    //导出总excel
    exportExcelFn(){
     

    },
    

    changePageFn(page){
      this.currentPage=page;
      this.tableLoading=true;
    },

    searchUtiliFn(){
      //点击搜索数据
          var obj={}
          for( var key in this.queryData){
              if(this.queryData[key]){
                obj[key]=this.queryData[key]
              }
          }
          
          //点击搜索时，改变要查询的数据并且让页码去到第一页
          this.queryDataNow=obj;
          this.currentPage=1;
          
    },

    //点击清空数据
    clearUtiliFn(){
      for(var i in this.queryData){
        this.queryData[i]="";
      }
    },
    
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

    setAllUtiliFn(){
           this.allUtiliChart = echarts.init(document.getElementById('all_utili_wrap'));
           this.allUtiliChart.setOption(allUtiliOption)
    },

    toUtiliDetailFn(id){
        this.$router.push({
            path:`/web/utili_detail/${id}`
        })
    },

    windowResizeFn(){
         window.addEventListener('resize',()=>{
            this.utilizationChart.resize();
            this.impDevOneChart.resize();
            this.impDevTwoChart.resize();
            this.allUtiliChart.resize(); 
        })
    }

  },
  created(){
    
  },
  mounted(){
     this.$nextTick(()=>{
          echartScript()
          .then(()=>{
                this.setUtilizationFn();
                this.setImportDeviceFn();
                this.setAllUtiliFn();
          })
          
      })

      this.windowResizeFn();
  }
}
</script>


<style lang='scss' scoped>
  @import "../../../assets/scss/web/utilization_manage.scss";
  @import '../../../assets/scss/web/common/table.scss';
  @import '../../../assets/scss/web/common/form_data.scss';
</style>
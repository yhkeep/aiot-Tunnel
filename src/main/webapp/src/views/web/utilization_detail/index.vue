<template>
  <div class='utilization_detail_wrap'>
      <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>
      <div class="custom_flex_wrap">
        <div class="custom_flex_item_2 row_box hover_animat custom_bg_color_white utilization_detail_box">
            <h2 class="row_title">电流(A)</h2>
            <div id="electricityChart"></div>
        </div>
        <div class="custom_flex_item_2 row_box hover_animat custom_bg_color_white utilization_detail_box">
            <h2 class="row_title">电压(V)</h2>
            <div id="voltageChart"></div>
        </div>
        <div class="custom_flex_item_2 row_box hover_animat custom_bg_color_white utilization_detail_box">
            <h2 class="row_title">功率(W)</h2>
            <div id="powerChart"></div>
        </div>
        <div class="custom_flex_item_2 row_box hover_animat custom_bg_color_white utilization_detail_box">
            <h2 class="row_title">碳排放(KG)</h2>
            <div id="carbonChart"></div>
        </div>
       
       
      </div>

       
      <div class="row_box hover_animat custom_bg_color_white utilization_detail_box">
          <h2 class="row_title">动用率(%)</h2>
          <div id="utilizationChart"></div>
      </div>
       
     
    
   
  </div>
</template>

<script>
import Vue from "vue"
import {echartScript} from "../../../utils/needScript.js"
import {Button,Icon} from 'iview';
Vue.component('Button',Button);
Vue.component('Icon',Icon);

import {utiliDetailOption} from "../../../assets/js/chart/echarts/utilization_line.js"
export default {
  name:'utilization_detail',
  components:{},
  props:{},
  data(){
    return {
        electricityChart:null,
        voltageChart:null,
        powerChart:null,
        carbonChart:null,
        utilizationChart:null,
    }
  },
  watch:{
    
      isCollapsed(){
                setTimeout(()=>{
                  this.electricityChart.resize()
                  this.voltageChart.resize()
                  this.powerChart.resize()
                  this.carbonChart.resize()
                  this.utilizationChart.resize()
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

        setUtiliDetailFn(){
            // 基于准备好的dom，初始化echarts实例
            this.electricityChart = echarts.init(document.getElementById('electricityChart'));
            this.voltageChart = echarts.init(document.getElementById('voltageChart'));
            this.powerChart = echarts.init(document.getElementById('powerChart'));
            this.carbonChart = echarts.init(document.getElementById('carbonChart'));
            this.utilizationChart = echarts.init(document.getElementById('utilizationChart'));
            
            //电流
            var elecOption=JSON.parse(JSON.stringify(utiliDetailOption));
            elecOption.color="#4cb4e6"
            elecOption.series[0].name="电流"
            elecOption.series[0].data=[800, 552, 1000, 562, 921, 82, 524, 321, 123, 521, 1211, 424,];

            //电压
            var voltageOption=JSON.parse(JSON.stringify(utiliDetailOption));
            voltageOption.color="#febf9f"
            voltageOption.series[0].name="电压"
            voltageOption.series[0].data=[220, 201, 212, 182, 0, 220, 142, 123, 212, 132, 123, 220, ];

            //功率
            var powerOption=JSON.parse(JSON.stringify(utiliDetailOption));
            powerOption.color="#b1b195"
            powerOption.series[0].name="功率"
            powerOption.series[0].data=[42, 75, 211,424 ,21, 92, 23, 42, 242, 132, 241, 522, ];

            //碳排放
            var carbonOption=JSON.parse(JSON.stringify(utiliDetailOption));
            carbonOption.color="#36c5bf"
            carbonOption.series[0].name="碳排放"
            carbonOption.series[0].data=[53, 32, 252, 221, 231, 221, 421, 123, 22, 132, 424, 31, ];

            //动用率
            var utilizationOption=JSON.parse(JSON.stringify(utiliDetailOption));
            utilizationOption.color="#8f1e77"
            utilizationOption.series[0].name="动用率"
            utilizationOption.series[0].data=[52, 24, 29, 98, 60, 98, 42, 86, 62, 32, 73, 22, ];
           
           

                this.electricityChart.setOption(elecOption)
                this.voltageChart.setOption(voltageOption)
                this.powerChart.setOption(powerOption)
                this.carbonChart.setOption(carbonOption)
                this.utilizationChart.setOption(utilizationOption)
        },

        windowResizeFn(){
            window.addEventListener('resize',()=>{
                this.electricityChart.resize()
                this.voltageChart.resize()
                this.powerChart.resize()
                this.carbonChart.resize()
                this.utilizationChart.resize()
            })
        }
  },
  created(){},
  mounted(){
      this.$nextTick(()=>{
          echartScript()
          .then(()=>{

               this.setUtiliDetailFn();
          })
      });

      this.windowResizeFn();
  }
}
</script>

<style lang='scss' scoped>
    @import "../../../assets/scss/web/utilization_detail.scss";
</style>
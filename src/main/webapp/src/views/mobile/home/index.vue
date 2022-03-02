<template>
  <div class='mobile_home_wrap'>
    <PullRefresh v-model="isLoading" @refresh="onRefresh">
      <div class="home_box">
          <div class="home_title_wrap custom_row_wrap">
            <div class="home_title_box custom_flex_row_wrap">
                <div class="hello_box">
                      <h2>Hello,{{user}}</h2>
                      <h3>欢迎回来！</h3>
                </div>
                <div class="user_box">
                      <img src="../../../assets/img/users.jpg" alt="">
                      <div class="more_menu_box" @click="showMoreMenuFn">
                        <Icon name="bars" />
                      </div>
                </div>
            </div>
          </div>
          <div class="home_total_wrap custom_row_wrap">
            <div class="home_total_box custom_flex_row_wrap" >
              <div class="total_box_item" v-for="(item,index) in summaryList" :key="index">
                  <span>{{item.text}}</span>
                  <h3>
                    <countTo :startVal='0' :endVal='Number(item.value)' separator='' :duration='1000' :decimals="item.value.toString().indexOf('.')>-1 ?item.value.toString().length-(item.value.toString().indexOf('.')+1) :0" :suffix="item.unit" ></countTo>
                  </h3>
              </div>
            
            </div>
          </div>
          <div class="home_chart_wrap custom_row_wrap">
            <div class="home_chart_box">
              <div class="home_chart_item">
                <div class="home_chart_item_title">
                    <h3>维修报废</h3>
                </div>
                <div class="home_chart_item_box">
                  <div class="home_chart_item_block" id="damage_ratio_wrap"></div>
                  <div class="custom_flex_row_wrap">
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
              </div>

              <div class="home_chart_item">
                <div class="home_chart_item_title">
                    <h3>今日盘点</h3>
                </div>
                <div class="home_chart_item_box">
                  <div class="home_chart_item_block" id="check_pie_wrap"></div>
                  <div class="custom_flex_row_wrap">
                    <div class="check_num_item">
                      <span>{{checkData[0]}}件</span>
                    </div>
                    <div class="check_num_item">
                      <span>{{checkData[1]}}件</span>
                    </div>
                  </div>
                </div>
              </div>
            
            </div>
          </div>
      </div>
    </PullRefresh>

  </div>
</template>

<script>
import {echartScript} from "../../../utils/needScript.js"

import damageOption from "../../../assets/js/chart/echarts/damage_pie.js"
import checkOption from "../../../assets/js/chart/echarts/check_pie.js"
import countTo from 'vue-count-to';
import {Button,Icon,ActionSheet,Popup,PullRefresh } from 'vant';
import 'vant/lib/button/style';
import 'vant/lib/icon/style';
import 'vant/lib/action-sheet/style';
import 'vant/lib/popup/style';
import 'vant/lib/pull-refresh/style';
import NET_PORT from "../../../api/port.js"

export default {
  name:'mobile_home',
  components:{
    Icon,
    ActionSheet,
    Popup,
    Button,
    PullRefresh,
    countTo
  },
  props:{
  
  },
  data(){
    return {
        user:this.$store.state.user,
        role:this.$store.state.role,

        // assetTotalFee:'--',
        // assetTotal:'--',
        // maintain:'--',
        // unexamined:'--',
        // examined:'--',
        // totalCheck:'--',

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
                color:'#e46cbb',
                icon:'md-hammer',
            },
            {
                value:0,
                text:'总盘点数',
                unit:'件',
                color:'#9a66e4',
                icon:'ios-contrast',
            },
            // {
            //     value:'--',
            //     text:'今日已盘',
            //     unit:'件',
            //     color:'#9a66e4',
            //     icon:'ios-contrast',
            // },
        ],

        damageChart:null,
        // departChart:null,
        checkChart:null,

        damageData:[0,0,0,0,0,0],
        checkData:[0,0,0],
        isLoading: false,
    }
  },
  watch:{},
  computed:{},
  methods:{
    showMoreMenuFn(){
      this.$emit('getMessage', true);
    },

    onRefresh() {
      this.getData();
      setTimeout(() => {
        this.isLoading = false;
      }, 500);
    },
    

    getData(){
        
        this.$axios.post(NET_PORT.assetsTotalNum)
        .then((res)=>{
            var obj=res.data[0]

            if(obj.msg==='failed'){

            }else{
                // this.assetTotal=obj.examined+obj.unexamined;
                // this.assetTotalFee=obj.aggregate;
                // this.maintain=obj.maintain;
                // this.examined=obj.examined;
                // this.unexamined=obj.unexamined;
                // this.totalCheck=obj.totalCheck ? obj.totalCheck :0;

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

                this.setDamageFn();
                this.setCheckFn();
            }

        })
        .catch((err)=>{
            // console.log(err)
        })
    },

     setDamageFn(){
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
                value:hasCheckRatio,name:'今日已盘',
              },
              {
                value:noCheckRatio,name:'今日未盘',
              },
            
            ]
            this.checkChart.setOption(checkOption)
        },

    
  },
  created(){
    
    
  },
  mounted(){
      
      this.$nextTick(()=>{
        echartScript()
        .then(()=>{
            this.getData();
        })
      })
  },

  activated(){
    window.scrollTo(0,0);
  }
}
</script>
<style lang="scss" scoped>
  @import "../../../assets/scss/mobile/home.scss";
  
</style>
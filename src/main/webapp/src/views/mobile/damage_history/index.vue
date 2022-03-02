<template>
  <div class='mobile_damage_history_wrap manage_list_wrap custom_flex_column_wrap' >
        
        <div class="assets_detail_title">
            <h3>{{assetID}}</h3>
        </div>

         <!-- <Search
          v-model="value"
          disabled 
          placeholder="点击展开搜索面板"
          shape="round"
          @click="openSearchPaneFn"
        >
        
        </Search>

        <Popup v-model="showSearchPane" position="top" round >
            <div class="query_data_wrap">
                <div class="query_data_box">
                    <div>
                        <Field @click="timeModalShow=true;currentMode='timeStart'" label="开始时间" disabled  v-model="queryData.timeStart" placeholder="请输入要查询的开始时间"/>
                    </div>
                    <div>
                        <Field @click="timeModalShow=true;currentMode='timeEnd'" label="结束时间" disabled  v-model="queryData.timeEnd" placeholder="请输入要查询的结束时间"/>
                    </div>
                </div>
            
                <div class="tools_button_wrap">
                    <Button class="tools_button"   type="primary" round  @click="searchFn">搜索</Button>
                </div>
            </div>
        </Popup> -->

        <div class="query_data_wrap_open">
            <div class="query_data_box">
                    <Field @click="timeModalShow=true;currentMode='timeStart'" label="开始时间" readonly  v-model="queryData.timeStart" placeholder="请输入要查询的开始时间"/>
                    <Field @click="timeModalShow=true;currentMode='timeEnd'" label="结束时间" readonly  v-model="queryData.timeEnd" placeholder="请输入要查询的结束时间"/>
            </div>
        
            <div class="submit_button_wrap">
                <Button class="submit_button"   type="info" round  @click="searchFn">搜索</Button>
            </div>
        </div>



        

       <div class="assets_table_wrap">
          <div class="assets_table_wrap_content  clearfix">
              <div class="assets_table_wrap_left">
                  <ul class="assets_table_ul" >
                      <li class="assets_table_li assets_table_li_name">
                         <div class="assets_table_item"><p>序号</p></div>
                      </li>
                      <li class="assets_table_li" v-for="(item,index) in historyDetailData" :key="index">
                        <div class="assets_table_item"><p>{{index+1}}</p></div>
                      </li>
                  </ul>
              </div>
              <div class="assets_table_wrap_right">
                  <ul class="assets_table_ul" >
                      <li class="assets_table_li assets_table_li_name clearfix" >
                            <div class="assets_table_item"><p>记录时间</p></div>
                            <div class="assets_table_item"><p>负责人签名</p></div>
                            <div class="assets_table_item"><p>交接人签名</p></div>
                            <div class="assets_table_item"><p>交接人部门</p></div>
                            <div class="assets_table_item"><p>预计归还时间(外借)</p></div>
                            <div class="assets_table_item"><p>备注</p></div>
                            <div class="assets_table_item"><p>类型</p></div>
                      </li>
                      <li class="assets_table_li clearfix" v-for="(item,index) in historyDetailData" :key="index" >
                          <div class="assets_table_item">{{item.handovertime ? item.handovertime : '--'}}</div>
                          <div class="assets_table_item">
                              <div class="damage_history_img">
                                  <img :src="item.handlingPeople" alt="" @click="showImgFn(item.handlingPeople)">
                              </div>
                          </div>
                          <div class="assets_table_item damage_history_img">
                               <div class="damage_history_img">
                                  <img :src="item.buttingPeople" alt="" @click="showImgFn(item.buttingPeople)">
                              </div>
                          </div>
                          <div class="assets_table_item"><p>{{item.handoverdepartment ? item.handoverdepartment : '--'}}</p></div>
                          <div class="assets_table_item"><p>{{item.estimatedtime ? item.estimatedtime : '--'}}</p></div>
                          <div class="assets_table_item"><p>{{item.remark ? item.remark : '--'}}</p></div>
                          <div class="assets_table_item">
                                <Tag v-if="item.assetstatustype==='1'" color="#ff9900">维修登记</Tag>
                                <Tag v-if="item.assetstatustype==='2'" color="#19be6b">维修完成</Tag>
                                <Tag v-if="item.assetstatustype==='3'" color="#9a66e4">外借登记</Tag>
                                <Tag v-if="item.assetstatustype==='4'" color="#2d8cf0">外借归还</Tag>
                          </div>
                      </li>
                  </ul>
              </div>
          </div>

            <div class="loading_info_wrap">
                <Loading v-show="loadingShow" size="24px">加载中...</Loading>
                <p v-show="!loadingShow&&!historyDetailData.length">暂无数据</p>
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

        <Popup v-model="showImg" class="scale_img_wrap">
            <img  :src="showImgData" alt="">
        </Popup>
  </div>
</template>

<script>
import {Button,Tag,Loading,Search,Icon,Field,Popup,DatetimePicker} from 'vant';
import 'vant/lib/button/style';
import 'vant/lib/tag/style';
import 'vant/lib/loading/style';
import 'vant/lib/search/style';
import 'vant/lib/icon/style';
import 'vant/lib/field/style';
import 'vant/lib/popup/style';
import 'vant/lib/datetime-picker/style';
import NET_PORT from "../../../api/port.js"
export default {
  name:'mobile_damage_history',
  components:{
        Button,
        Tag,
        Loading,
        Search,
        Icon,
        Field,
        Popup,
        DatetimePicker
  },
  props:{},
  data(){
    return {
        value:'',
        showSearchPane:false,
        showImg:false,
        showImgData:'',

        assetID:'',
        historyDetailData:[],
        loadingShow:true,
        queryData:{
            timeStart:'',
            timeEnd:'',
        },

        minDate: new Date(2020, 0, 1),
        maxDate: new Date(2050, 12, 0),
        currentDate: new Date(),
        timeModalShow:false,
        currentMode:"",
    }
  },
  watch:{},
  computed:{
    
  },
  methods:{
    getTimeFn(value){
        var date=value
        var currentYear=date.getFullYear();
        var currentMonth=date.getMonth()+1;
        var currentDate=date.getDate();
        var currentHours=date.getHours();
        var currentMinutes=date.getMinutes();
        currentMonth= currentMonth<10 ? '0'+currentMonth : currentMonth
        currentDate= currentDate<10 ? '0'+currentDate : currentDate
        currentHours= currentHours<10 ? '0'+currentHours : currentHours
        currentMinutes= currentMinutes<10 ? '0'+currentMinutes : currentMinutes
            
        this.queryData[this.currentMode]=currentYear+'-'+currentMonth+'-'+currentDate+' '+currentHours+':'+currentMinutes
        this.timeModalShow=false;

    },

    showImgFn(img){
        this.showImgData=img
        this.showImg=true;
    },

    openSearchPaneFn(){
        this.showSearchPane=true;
    },
     
    searchFn(){
        this.getDataFn();
        this.showSearchPane=false;
    },
    getDataFn(){
    
        this.loadingShow=true;
        this.$axios.get(NET_PORT.damageRegisterHistory+'?assetID='+this.assetID+'&timeStart='+ this.queryData.timeStart+'&timeEnd='+this.queryData.timeEnd)
        .then((res)=>{
        if(res.data.code===0){
            this.historyDetailData=res.data.data
            this.loadingShow=false;
        }

        })
        .catch((err)=>{
        console.log(err)
        })
        
    },
    
  },
  

  created(){
     this.$emit('setTitle','维修/报废/调用历史');
     this.assetID=this.$route.params.id;
  },
  mounted(){
      this.getDataFn();
  },

  
}
</script>
<style lang="scss" scoped>
    @import "../../../assets/scss/mobile/common/manage_list.scss";
    @import "../../../assets/scss/mobile/damage_history.scss";
</style>
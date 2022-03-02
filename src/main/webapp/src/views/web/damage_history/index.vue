<template>
  <div class='damage_history_wrap'>
    <h1 class="assets_title">{{assetID}}</h1>
    <div :class="foldArr[0] ? 'hover_animat custom_bg_color_white fold_wrap fold' : 'hover_animat custom_bg_color_white fold_wrap'">
        <div class="row_box">
            <h2 class="row_title">按条件筛选</h2>
            <div class="fold_box">
                <div class="form_wrap">
                  <div class="form_item">
                      <Row :gutter="20">
                          <Col span="6">
                            <h3>日期范围</h3>
                            <DatePicker v-model="dateRangeArr" :open="dateOpen" @on-open-change="dateOpenChangeFn" @on-ok="dateOkFn"  type="datetimerange" placeholder="选择日期范围" style="width: 100%"   @on-change="dateRangeFn"></DatePicker>
                          </Col>
                      </Row>
                  </div>

                
                
              </div>
              <div class="submit_btn_wrap">
                    <Button type="primary" shape="circle" @click="searchAssetsFn">搜索</Button>
                    <Button type="error" shape="circle" @click="clearAssetsFn">清空</Button>
              </div>
          </div>
        </div>
        <div class="fold_icon_wrap ">
          <div class="fold_icon_box" @click="foldFn(0)">
            <Icon v-if="!foldArr[0]" type="ios-arrow-up"/>
            <Icon v-else type="ios-arrow-down" />
          </div>
        </div>
      </div>
      <div class="row_wrap hover_animat custom_bg_color_white">
          <div class="row_box">
                  <h2 class="row_title">维修/外借历史数据</h2>
                  <div class="data_table_top clearfix">
                    <div class="data_table_top_left">
                      
                    </div>
                    <div class="data_table_top_right">
                      
                    </div>
                  </div>

                  
                  <div class="table_scroll_wrap">
                    <Table style="min-width:1000px"  stripe :columns="historyDetailColumn" :data="historyDetailData" size='large' ref="table" :loading="tableLoading">
                        <template slot-scope="{ row }" slot="handlingPeople">
                          <div class="damage_history_img">
                            <img :src="row.handlingPeople" alt=""  @click="showImgFn(row.handlingPeople)">
                          </div>
                        </template>
                        <template slot-scope="{ row }" slot="buttingPeople">
                          <div class="damage_history_img">
                            <img :src="row.buttingPeople" alt=""  @click="showImgFn(row.buttingPeople)">
                          </div>
                        </template>
                        <template slot-scope="{ row }" slot="assetstatustype">
                          <Tag v-if="row.assetstatustype==='1'" color="warning">维修登记</Tag>
                          <Tag v-if="row.assetstatustype==='2'" color="success">维修完成</Tag>
                          <Tag v-if="row.assetstatustype==='3'" color="#9a66e4">外借登记</Tag>
                          <Tag v-if="row.assetstatustype==='4'" color="primary">外借归还</Tag>
                          
                
                        </template>
                        
                    </Table>
                  </div>
                  <div class="data_table_pagination_wrap">
                      <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                  </div>
          </div>
      </div>

      <div v-show="showImg" class="scale_img_wrap"  @click="hideImgFn">
        <img  :src="showImgData" alt="">
      </div>
  </div>
</template>

<script>
import Vue from "vue"
import {Row,Col,Icon,Table ,Tag ,DatePicker,Select,Option,Page,Input,Button} from 'iview';
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('DatePicker', DatePicker);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);

import NET_PORT from "../../../api/port.js"
export default {
  name:'damage_history',
  components:{},
  props:{},
  data(){
    return {
        assetID:'',
        tableLoading:false,
        totalCount: 0,
        pageSize: 10,
        currentPage: 1,
        foldArr:[false,],
        queryData:{
            timeStart:'',
            timeEnd:'',
        },
        dateRangeArr:[],
        dateOpen:false,

        showImg:false,
        showImgData:'',

        historyDetailColumn:[
          {
            key: 'handovertime',
            title: '记录时间'
          },
		      {
            key: 'handlingPeople', 
            title: '负责人签名',
            slot:'handlingPeople'
          },
		      {
            key: 'buttingPeople', 
            title: '交接人签名',
            slot:'buttingPeople'
          },
		      {
            key: 'handoverdepartment', 
            title: '交接人部门',
          },
		      {
            key: 'estimatedtime', 
            title: '预计归还时间（外借）',
          },
		      {
            key: 'remark', 
            title: '备注',
          },
		      {
            key: 'assetstatustype', 
            title: '类型',
            slot:'assetstatustype'
          },
        ],

        historyDetailData:[],
    }
  },
  watch:{},
  computed:{},
  methods:{
    dateRangeFn(value){
      this.queryData.timeStart=value[0]
      this.queryData.timeEnd=value[1]
    },

    showImgFn(img){
        this.showImgData=img
        this.showImg=true;
    },

    hideImgFn(){
        this.showImg=false;
        this.showImgData='';
    },

    dateOpenChangeFn(key){
       if(key){
           this.dateOpen=true;
       }
    },

    dateOkFn(){
      this.dateOpen=false;
    },

    foldFn(index){
      this.$set( this.foldArr,index, !this.foldArr[index])
    },

    //点击搜索
    searchAssetsFn(){
       this.getDataFn();
    },

    //清除搜索数据
    clearAssetsFn(){
        this.dateOpen=false;
        this.queryData.timeStart=''
        this.queryData.timeEnd=''
        this.dateRangeArr=[];
    },

    changePageFn(page){
      this.currentPage=page;
    },

    getDataFn(){
        this.tableLoading=true;
        this.$axios.get(NET_PORT.damageRegisterHistory+'?assetID='+this.assetID+'&timeStart='+ this.queryData.timeStart+'&timeEnd='+this.queryData.timeEnd)
        .then((res)=>{
          if(res.data.code===0){
            this.historyDetailData=res.data.data
            this.tableLoading=false;
          }

        })
        .catch((err)=>{
          console.log(err)
        })
    }
  },
  created(){
    this.assetID=this.$route.params.id;
    if(!this.assetID){
        this.$Message.error({
            content:"未找到资产信息!",
            duration:2,
        })
        setTimeout(()=>{
            this.$router.go(-1)
        },2000)
    }
  },
  mounted(){
      this.getDataFn();
  }
}
</script>
<style lang="scss" scoped>
   @import '../../../assets/scss/web/common/table.scss';
   @import '../../../assets/scss/web/common/form_data.scss';
   @import '../../../assets/scss/web/damage_history.scss';
</style>
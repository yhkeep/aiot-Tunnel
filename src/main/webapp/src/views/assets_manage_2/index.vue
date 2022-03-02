<template>
  <div class='assets_manage_wrap'>
    <div class="hover_animat custom_bg_color_white">
      <div class="row_box">
          <h2 class="row_title">按条件筛选</h2>
          <div class="form_wrap">
              <div class="form_item">
                  <Row :gutter="20">
                          <Col span="8">
                            <h3>资产名称</h3>
                            <Input v-model="queryData.name" placeholder="资产名称" />
                          </Col>
                          <Col span="8">
                            <h3>所属部门</h3>
                            <Select v-model="queryData.depart">
                                <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                          </Col>
                          <Col span="8">
                            <h3>日期范围</h3>
                            <DatePicker v-model="dateValue" @on-change="dateRangeFn" type="daterange" split-panels placeholder="选择日期范围" style="width:100%"></DatePicker>
                          </Col>
                        </Row>
              </div>

              <div class="form_item">
                  <Row :gutter="20">
                          <Col span="8">
                          <h3>一级分类</h3>
                            <Select v-model="queryData.fstCat">
                                <Option v-for="item in fstCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                          </Col>
                          <Col span="8">
                          <h3>二级分类</h3>
                            <Select v-model="queryData.secCat">
                                <Option v-for="item in secCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                          </Col>
                          <Col span="8">
                          <h3>三级分类</h3>
                            <Select v-model="queryData.thdCat">
                                <Option v-for="item in thdCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                          </Col>
                        </Row>
              </div>
              <div class="form_item">
                  <Row :gutter="20">
                        <Col span="8">
                          <h3>账面值(最少)</h3>
                          <InputNumber  :min="0" v-model="queryData.feeStart" placeholder="账面值(最少)" style="width:100%;"></InputNumber>
                        </Col>
                        <Col  span="8">
                          <h3>账面值(最多)</h3>
                          <InputNumber  :min="0" v-model="queryData.feeEnd" placeholder="账面值(最多)" style="width:100%;"></InputNumber>
                        </Col>
                      </Row>
              </div>
          </div>
          <div class="submit_btn_wrap">
                <Button type="primary" @click="searchAssetsFn">搜索</Button>
                <Button type="error" @click="clearAssetsFn">清空</Button>
          </div>
      </div>
    </div>
    <div class="row_wrap hover_animat custom_bg_color_white">
      <div class="row_box">
          <h2 class="row_title">资产明细表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">
                <Button type="primary" icon="md-add" @click="toCreateFn">新增资产</Button>
                    <Button v-if="$store.state.role==='admin'" type="error" icon="md-trash" @click="deleteFn">删除资产</Button>
                    <Button type="info" icon="ios-print" @click="printFn">打印标签</Button>
                    <Button type="success" icon="md-archive" @click=exportExcelFn>导出资产总表</Button>
              </div>
              <div class="data_table_top_right">
                    <div class="pagination_wrap">
                        <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                    </div>
              </div>
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1500px" stripe :columns="assetsDetailColumn" :data="assetsDetailData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                    <template slot-scope="{row}" slot="assetID">
                      <a @click="toDetailFn(row.assetID)">{{row.assetID}}</a>
                    </template>

                    <template slot-scope="{row}" slot="amount">
                        <span>{{Number(row.amount)? Number(row.amount) : '--'}}</span>
                    </template>
                    
                    <template slot-scope="{row}" slot="buyDate">
                      <span>2019-01-01</span>
                    </template>
                    
                    <template slot-scope="{row}" slot="money">
                      <span>10000</span>
                    </template>

                    <template slot-scope="{row}" slot="check">
                        <span>{{row.check ? row.check : '--'}}</span>
                    </template>

                    <template slot-scope="{row}" slot="oldRatio">
                      {{row.oldRatio ? row.oldRatio+"%" :'--'}}
                    </template>

                    <template slot-scope="{ row }" slot="status">
                        <Tag v-if="row.status==='正常'" color="success">正常</Tag>
                        <Tag v-else-if="row.status==='在修'" color="warning">在修</Tag>
                        <Tag v-else-if="row.status==='待报废'" color="primary">待报废</Tag>
                        <Tag v-else-if="row.status==='已报废'" color="error">已报废</Tag>
                        <Tag v-else-if="row.status==='--'"  color="default">未知</Tag>
                        <Tag v-else  color="default">{{row.status}}</Tag>
                    </template>
                  
                </Table>
            </div>

           
      </div>
    </div>

      <iframe id="download" style="display:none"></iframe>

      <div id="print_wrap" v-show="false">
       
      </div>


      <Modal v-model="deleteModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
          <div style="text-align:center;padding:20px 0">
              <Spin fix v-if="!achieveDelete&&!errorDelete">
                  <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                  <div>正在删除</div>
              </Spin>
              <Spin fix v-if="achieveDelete" style="color:#00ad19">
                  <Icon type="ios-checkmark-circle" size=18 />
                  <div>删除成功</div>
              </Spin>
              <Spin fix v-if="errorDelete" style="color:#f72b2b">
                  <Icon type="ios-close-circle" size=18 />
                  <div>删除失败</div>
              </Spin>
          </div>
      </Modal>


  </div>
</template>

<script>
import Vue from "vue"
import { Row,Col,Icon,Table ,Tag ,DatePicker,Select,Option,Page,Input,InputNumber,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('DatePicker', DatePicker);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('InputNumber',InputNumber);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);

import QRCode from 'qrcodejs2'

import departData from "../../assets/data/depart2.js"
import {fstCatData,secCatAllData,thdCatAllData} from "../../assets/data/category.js"
export default {
  name:'assets_manage',
  components:{},
  props:{},
  data(){
    return {
      isFirstEnter:false,
      departData:departData,
      fstCatData:fstCatData,
      secCatAllData:secCatAllData,
      thdCatAllData:thdCatAllData,
      queryData:{
        name:'',
        depart:'',
        buyTimeStart:'',
        buyTimeEnd:'',
        fstCat:'',
        secCat:'',
        thdCat:'',
        feeStart:null,
        feeEnd:null,
      },
      dateValue:'',
      queryDataNow:{},

      tableLoading:true,
      isSearch:false,

      selection:[],
      
      totalCount: 0,
      pageSize: 10,
      currentPage: 1,
        
      deleteModal:false,
      achieveDelete:false,
      errorDelete:false,

      assetsDetailColumn:[
            
          {
                title: '资产id',
                key: 'assetID',
                minWidth:30,
            },
            {
                title: '资产名称',
                key: 'assetName',
            },
            {
                title: 'mac地址',
                key: 'mac'
            },
            {
                title: '数量',
                key: 'amount',
                slot:'amount'
            },
            {
                title: '单位',
                key: 'unit',
            },
            {
                title: '购买日期',
                key: 'buyDate',
                slot: 'buyDate'
            },
            {
                title: '金额',
                key: 'money',
                slot: 'money'
            },
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:100
            },
            {
                title: '部门',
                key: 'department',
            },
            {
                title:'一级分类',
                key: 'fstCat', 
            },
            {
                title:'二级分类',
                key: 'secCat',
            },
            {
                title:'三级分类',
                key: 'thdCat',
            },
            {
                title: '折旧率',
                key: 'oldRatio', 
                slot:'oldRatio'
            },
            {
                title: '每月折旧',
                key: 'monthOldValue', 
                
            },
            {
                title: '盘点日期',
                key: 'check', 
                slot:'check'
            },
        ],

      assetsDetailData:[],

      
      secCatData:[],
      thdCatData:[],
     

    }
  },
  watch:{
       //监视一级分类和二级分类的变化动态改变数据
      "queryData.fstCat"(){
        this.secCatData=this.secCatAllData.filter((item,index)=>{
          return item.parent===this.queryData.fstCat;
        })
        this.thdCatData=[];
        this.queryData.thdCat='';
      },
      "queryData.secCat"(){
        this.thdCatData=this.thdCatAllData.filter((item,index)=>{
          return item.parent===this.queryData.secCat;
        })

        this.queryData.thdCat='';
      },
  },
  computed:{},
  methods:{
 dateRangeFn(value){
      //设置日期范围
      this.queryData.buyTimeStart=value[0]
      this.queryData.buyTimeEnd=value[1]
    },


    //导出总excel
    exportExcelFn(){

        let href = process.env.API_HOST+'huaxi/poi/export'
        let a = document.createElement('a')
        a.setAttribute('download', '')
        a.setAttribute('href', href)
        a.click()

    },

    checkAllGroupChange(selection){
      this.selection=selection;
    },

    printFn(){

          if(this.selection.length===0){

                this.$Message.error({
                          content:"请选择至少一项!",
                          duration:2,
                })

          }else{

            

          //每次打印之前先清空打印容器的内容
            document.getElementById('print_wrap').innerHTML='';

            for(var i=0;i<this.selection.length;i++){
                  var printBox=document.createElement('div');
                    printBox.classList.add('print_box');
                    printBox.style.overflow="hidden";
                    printBox.style.transform="scale(0.9)";
                    printBox.style.transformOrigin="20% 20%";
                    printBox.style.pageBreakBefore="auto";
                    printBox.style.pageBreakAfter="always"


                    var printBoxHTML=['<div class="print_left" style="float:left;max-width:50%;font-size:12px;">',
                    '              <p style="margin:1px;">NO:'+this.selection[i].assetID+'</p>',
                    '              <p style="margin:1px;">部门:'+this.selection[i].department+'</p>',
                    '              <p style="margin:1px;">资产名称:'+this.selection[i].assetName+'</p>',
                    '              <p style="margin:1px;">购入日期:'+this.selection[i].buyDate+'</p>',
                    '              <p style="margin:1px;">某市第一人民医院</p>',
                    '            </div>',
                    '            <div class="print_right" style="float:left;margin-left:5px;max-width:50%;">',
                    // '              <img src='+this.printArr[i].img+' alt="">',
                    '              <div id="qrcode'+i+'" ref="qrcode"></div>',
                    '            </div>'].join("");

                    printBox.innerHTML=printBoxHTML;
                    
                    //生成二维码
                    (function(i){
                      this.$nextTick (function () {
                        this.qrcode('qrcode'+i+'',this.selection[i].assetId);
                      })
                    }).bind(this)(i)

                    document.getElementById('print_wrap').appendChild(printBox);
              }




                //当打印容器已经添加了内容后开始新建窗口执行打印程序
                setTimeout(function(){
                  if( document.getElementById('print_wrap').getElementsByClassName('print_box').length===this.selection.length){
                    var printHTML = document.getElementById('print_wrap').innerHTML;
                    var wind = window.open("",'newwindow', );
                    wind.document.body.innerHTML = printHTML;
                    wind.print();
                  }
                }.bind(this),100)
              
          }
      
    },

    

    searchAssetsFn(){
      // //点击搜索数据
          var obj={}
          var num=0;
          for( var key in this.queryData){
              if(this.queryData[key]){
                obj[key]=this.queryData[key]
                num++;
              }else{
                 obj[key]=''
              }
          }
          //如果填了至少有一个搜索数据才会进行搜索
          if(num>0){
            this.isSearch=true;
            this.queryDataNow=obj;
            this.currentPage=1;
            this.getDataFn('search')
          }
          else{
            this.currentPage=1;
            this.getDataFn('all');
          }
          
          
    },

    changePageFn(page){
      this.tableLoading=true;

      this.currentPage=page;
      
      if(this.isSearch){
        this.getDataFn('search');
      }else{
        this.getDataFn('all');
      }
    },

    //清除搜索数据
    clearAssetsFn(){
      for(var i in this.queryData){
        this.queryData[i]="";
      }

      this.dateValue='';

    },



    deleteFn(){
        if(this.selection.length){
              this.$Modal.confirm({
                    title:'删除提示',
                    okText:'确定删除',
                    cancelText:'取消',
                    content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+'个资产吗？</h2>',
                    onOk:()=>{
                          this.deleteCertainFn();
                    },
                    
              })
        }else{
          return this.$Message.error({
                    content:"请选择至少一项!",
                    duration:2,
          })
        }
    },

    //点击确认删除
    deleteCertainFn(){
      this.deleteModal=true;

      var list=[];
      for(var i=0;i<this.selection.length;i++){
        list.push(this.selection[i].assetID)
      }
      
      this.$axios.get(process.env.API_HOST+'huaxi/del?assetID='+list)
      .then((res)=>{

          if(res.data.msg==='ok'){
            this.achieveDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                //删除成功后隔1秒隐藏模态框并更新数据
                this.currentPage=1;
                this.getDataFn('all');
                
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
            
           
            
          }else if(res.data.msg==='failed'){
            this.errorDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
          }

      })
      .catch((err)=>{
            this.errorDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
      })

    },

    toCreateFn(){
      this.$router.push({
        path:'/assets_create_2'
      })
    },

    toDetailFn(id){
      //将当前数据存入vuex
      for(var i=0;i<this.assetsDetailData.length;i++){
        if(this.assetsDetailData[i].assetID===id){
          window.sessionStorage.setItem('detailData',JSON.stringify(this.assetsDetailData[i]))
        }
      }

       this.$router.push({
        path:'/assets_detail_2'
      })

    },

    getDataFn(type){
          this.$Loading.start();
          if(type==='all'){
              this.isSearch=false;
              this.$axios.get(process.env.API_HOST+'huaxi/assetManagement/param?currentPage='+this.currentPage)
              .then((res)=>{
                  var initData=res.data;
                  var total=Number(initData.pop().total);
                  this.assetsDetailData=[];
                    for(var i=0;i<initData.length;i++){
                        for(var k in initData[i]){
                            if(initData[i][k]===''||initData[i][k]==='null'){
                              initData[i][k]='--'; 
                            };

                        }
                        this.assetsDetailData.push(initData[i])
                    }
                    this.totalCount=total;
                    this.tableLoading=false;
                    this.$Loading.finish()
              })
              .catch((err)=>{
                console.log(err)
                this.$Loading.error();
              })
          }else if(type==='search'){
            
              var configData='';
              for (var m in this.queryDataNow){
                      //将参数拼接到一起，裁掉左右空格
                      configData+=m+'='+this.queryDataNow[m].trim()+'&'
              }

              //截掉最后一个&符号
              var configDataSlice=configData.slice(0,configData.length-1)
              //这里拿到搜索的数据
              console.log(configDataSlice)

              this.$axios.get(process.env.API_HOST+'huaxi/assetManagement/param?currentPage='+this.currentPage+'&'+configDataSlice)
              .then((res)=>{
                  var initData=res.data;
                  var total=Number(initData.pop().total);
                  this.assetsDetailData=[];
                     for(var i=0;i<initData.length;i++){
                          for(var k in initData[i]){
                              if(initData[i][k]===''||initData[i][k]==='null'){
                                initData[i][k]='--'; 
                              };

                          }
                          this.assetsDetailData.push(initData[i])
                      }
                    this.totalCount=total;
                    this.tableLoading=false;
                    this.$Loading.finish()
              })
              .catch((err)=>{
                console.log(err)
                this.$Loading.error();
              })
          }else{
            return
          }
    },

    initAllDataFn(){
     this.queryData={
        name:'',
        depart:'',
        buyTimeStart:'',
        buyTimeEnd:'',
        fstCat:'',
        secCat:'',
        thdCat:'',
        feeStart:null,
        feeEnd:null,
      },
      this.dateValue=''
      this.queryDataNow={}

      this.tableLoading=true
      this.isSearch=false

      this.selection=[]
      
      this.totalCount= 0
      this.pageSize= 10
      this.currentPage= 1
        
      this.deleteModal=false
      this.achieveDelete=false
      this.errorDelete=false

      this.assetsDetailData=[]

      this.secCatData=[]
      this.thdCatData=[]

       //判断角色类型，为表格添加选择项
      if(this.$store.state.role==="admin"){
          this.assetsDetailColumn.unshift(
            {
              title:'选择',
              slot:'selection',
              type: 'selection',
              
            },
          )
      }else{
        return
      }

    }


  },

  beforeRouteEnter(to, from, next) {
    console.log("我是资产管理 beforeRouteEnter 方法");
   
    if(from.name=='assets_detail_2'){
        to.meta.isBack=true;
    }

    next();
  },


  created(){
      console.log('这是资产管理 created 方法')
      this.isFirstEnter=true;
  },
  mounted(){
    console.log('这是资产管理 mounted 方法')
  },

  activated() {
    console.log("这是资产管理 activated 方法");
    if(!this.$route.meta.isBack || this.isFirstEnter){
        //先清空数据，避免让用户看到之前缓存的数据
        this.initAllDataFn();
        //获取数据
        this.getDataFn('all');

    }
    // 恢复成默认的false，避免isBack一直是true，导致下次无法获取数据
    this.$route.meta.isBack=false
    // 恢复成默认的false，避免isBack一直是true，导致每次都获取新数据
    this.isFirstEnter=false;

  },

  deactivated(){
    console.log("这是资产管理 deactivated 方法");
      this.$Loading.finish();
      
  },
  




}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped src="../../assets/css/common/table.css"></style>
<style scoped>
</style>
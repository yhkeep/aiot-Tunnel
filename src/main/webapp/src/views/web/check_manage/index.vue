<template>
  <div class='check_manage_wrap'>

       <div class="hover_animat custom_bg_color_white">
        <div class="row_box">
            <h2 class="row_title">按条件筛选</h2>
            <div class="form_wrap">
                <div class="form_item">
                    <Row :gutter="20">
                        <Col span="6">
                          <h3>资产编号</h3>
                          <Input v-model="queryData.assetID" placeholder="资产编号" />
                        </Col>
                        <Col span="6">
                        <h3>资产名称</h3>
                          <Input v-model="queryData.assetName" placeholder="资产名称" />
                        </Col>
                        <Col span="6">
                          <h3>部门名称</h3>
                          <AutoComplete
                              v-model="queryData.memorydepart"
                              @on-focus="inputFocusFn('departmentroom')"
                              @on-blur="inputBlurFn"
                              @on-search="handleSearchFn"
                              placeholder="部门名称"
                              style="width:100%">
                              <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                          </AutoComplete>
                        </Col>
                        <Col span="6">
                          <h3>所在科室名称</h3>
                          <AutoComplete
                              v-model="queryData.memorylocDept"
                              @on-focus="inputFocusFn('locDept')"
                              @on-blur="inputBlurFn"
                              @on-search="handleSearchFn"
                              placeholder="所在科室名称"
                              style="width:100%">
                              <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                          </AutoComplete>
                        </Col>
                    </Row>
                </div>
                <div class="form_item">
                    <Row :gutter="20">
                        <Col span="6">
                          <h3>标签mac</h3>
                          <Input v-model="queryData.mac" placeholder="标签mac" />
                        </Col>
                        <Col span="6">
                            <h3>状态</h3>
                            <Select v-model="queryData.status">
                                <Option v-for="item in statusData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                            </Select>
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

     <div class="row_wrap">
        <div class="row_box hover_animat custom_bg_color_white">
            <h2 class="row_title">盘点明细表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">
                <Button type="primary" shape="circle" icon="md-contrast" @click="checkFn">立即盘点</Button>
                <Button type="warning" shape="circle" icon="ios-paper" to='/web/check_history'>盘点历史</Button>
              </div>
              <div class="data_table_top_right">
                  <!-- <div class="pagination_wrap">
                    <Page show-elevator show-total show-sizer :total="totalCount" :page-size-opts="pageSizeOptions" @on-change="changePageFn" @on-page-size-change="changePageSizeFn" :current="currentPage" :page-size="pageSize"/>
                  </div> -->
              </div>
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1500px" stripe :columns="assetsDetailColumn" :data="assetsDetailData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                    <template slot-scope="{row}" slot="check">
                        <span style="color:#2d8cf0;">{{row.check ? row.check : '--'}}</span>
                    </template>
                
                    <template slot-scope="{ row }" slot="status">
                        <Tag v-if="row.status==='正常'" :color="statusColor[0]" ><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='待维修'" :color="statusColor[1]" ><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='维修接单'" :color="statusColor[2]" ><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='待报废'" :color="statusColor[3]" ><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='报废'" :color="statusColor[4]" ><span>{{row.status}}</span></Tag>
                        <Tag v-else-if="row.status==='外借'" :color="statusColor[5]" ><span>{{row.status}}</span></Tag>
                        <Tag v-else  color="default"><span>{{row.status}}</span></Tag>
                    </template>
                  
                </Table>
            </div>
            <div class="data_table_pagination_wrap">
                <Page show-elevator show-total show-sizer :total="totalCount" :page-size-opts="pageSizeOptions" @on-change="changePageFn" @on-page-size-change="changePageSizeFn" :current="currentPage" :page-size="pageSize"/>
            </div>
        </div>
     </div>

      <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
          
          <div style="text-align:center;padding:20px 0">
              <Spin fix v-if="!achieveUpload&&!errorUpload">
                  <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                  <div>正在盘点</div>
              </Spin>
              <Spin fix v-if="achieveUpload" style="color:#00ad19">
                  <Icon type="ios-checkmark-circle" size=18 />
                  <div>盘点成功</div>
              </Spin>
              <Spin fix v-if="errorUpload" style="color:#f72b2b">
                  <Icon type="ios-close-circle" size=18 />
                  <div>盘点失败</div>
              </Spin>

          </div>
          
      </Modal>
  </div>
</template>

<script>

import Vue from "vue"
import {Row,Col,Icon,Table ,Tag ,Page,Input,Button,Modal,Spin,Select,Option,AutoComplete} from 'iview';
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('AutoComplete',AutoComplete);

import NET_PORT from "../../../api/port.js"
import { getToken } from '../../../utils/auth.js';
import initTimeFn from "../../../utils/initTime.js"
import statusColor from "../../../assets/data/statusColor.js"
import statusData from "../../../assets/data/status.js"
import assetsConfigArr from "../../../assets/data/assetsConfigArr.js"
export default {
  name:'check_manage',
  components:{},
  props:{},
  data(){
    return {
      // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
      statusData:statusData,
      statusColor:statusColor,
      user:this.$store.state.user,
      totalCount: 0,
      pageSize: 10,
      currentPage: 1,
      pageSizeOptions:[10,20,30,40,50,60,70,80,90,100],
      tableLoading:true,

      uploadModal:false,
      achieveUpload:false,
      errorUpload:false,

      selection:[],

      deviceDetailData:[],

      inputData:[],
      inputDataAll:[],

      queryData:{
        assetName:'',
        assetID:'',
        memorydepart:'',
        memorylocDept:'',
        mac:'',
        status:'',
      },

      assetsDetailColumn:[
            {
                title:'选择',
                slot:'selection',
                type: 'selection',
                width:80,
            },
           

             {
                title: '资产编号',
                key: 'assetID',
                
            },
            {
                title: '通用名称',
                key: 'generalName'
            },
            {
                title: '资产名称',
                key: 'assetName'
            },
           
            {
              title:'规格',
              key:'specification',
            },
            {
                title: '型号',
                key: 'type'
            },
           
            {
                title: '存放地点',
                key: 'location',
            },
           
            {
                title: '品牌',
                key: 'brand'
            },
            {
                title: '标签mac',
                key: 'mac'
            },
          
         
            {
                title: '部门名称',
                key: 'departmentroom'
            },
           
            {
                title: '所在科室名称',
                key: 'locDept'
            },
            {
                title: 'SN',
                key: 'sn'
            },
         
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:120
            },
      ],

      assetsConfigArr:assetsConfigArr,

      assetsDetailData:[],

    }
  },
  watch:{

    deviceDetailData(){
      this.deviceDetailData.forEach((item,index)=>{
        this.assetsDetailData.forEach((m,n)=>{
          if(item.Mac===m.mac){
            m.location=item.Location;
          }
        })
      })
    }
  },
  computed:{},
  methods:{

    handleSearchFn(data){
        this.inputData = this.inputDataAll.filter(item => item.toLowerCase().indexOf(data.toLowerCase()) > -1);
    },
    inputBlurFn(){
        this.inputDataAll=[];
        this.inputData=[];
    },
    inputFocusFn(key){
        this.inputDataAll=[];
        this.inputData=[];
        let queryArr=[];
        let obj={};
        obj[key]="1"
        queryArr.push(obj)
        this.$axios.post(NET_PORT.inputQuery,queryArr)
        .then((res)=>{
            res.data.data.forEach((item,index)=>{
                if(this.inputDataAll.find((result)=>{return result===item[key]})){

                }else{
                    this.inputDataAll.push(item[key])
                }
            })

            setTimeout(()=>{
                this.inputData=JSON.parse(JSON.stringify(this.inputDataAll))
            },300)
        })
        .catch((err)=>{
            console.log(err)
        })
            
    },

    searchAssetsFn(){
        this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
        this.currentPage=1;
        //获取数据
        this.getDataFn();
    },

    //清除搜索数据
    clearAssetsFn(){
        for(let i in this.queryData){
          this.queryData[i]="";
        }
    },

    changePageFn(page){
      this.tableLoading=true;
      this.currentPage=page;
      this.getDataFn();
    },

    changePageSizeFn(pageSize){
      this.pageSize=pageSize;
      this.currentPage=1;
      this.getDataFn();
    },

    //勾选选项时
    checkAllGroupChange(selection){
      this.selection=selection;
    },

    checkFn(){
      if(this.selection.length){
             if(this.selection.length>1){
                this.$Modal.confirm({
                      title:'盘点提示！',
                      okText:'确定',
                      cancelText:'取消',
                      content:'<h2 style="color:#f94040">你确定要同时盘点这'+this.selection.length+'个资产吗？</h2>',
                      onOk:()=>{
                            this.checkCertainFn();
                      },
                })
             }else{
               this.checkCertainFn();
             }
      }else{
         return this.$Message.error({
              content:"请选择至少一项!",
              duration:2,
          })
      }
    },

    checkCertainFn(){

        var datas=[]
        this.selection.forEach((item,index)=>{
          var obj={
            "assetID":item.assetID,
          };

          datas.push(obj);
        })

        //显示出正在上传
        this.uploadModal=true;

        this.$axios.post(NET_PORT.assetsCheck+"checkpep="+this.user,datas)
        .then((res)=>{
          if(res.data.code===0){
            this.successFn();
          }else{
            this.errorFn();
          }
        })
        .catch((error)=>{
            Modal.error({
                title: '错误提示！',
                content: error,
                okText: '确定',
                
            })
        })
    },

    successFn(){
        this.achieveUpload=true;
        setTimeout(()=>{
            this.uploadModal=false;
            setTimeout(()=>{
                    this.achieveUpload=false;
                    this.errorUpload=false;
                    this.getDataFn();
            },500)

        },1000)
    },

    errorFn(){
        this.errorUpload=true;
        setTimeout(()=>{
                this.uploadModal=false;
                setTimeout(()=>{
                    this.achieveUpload=false;
                    this.errorUpload=false;
                },500)
            
        },1000)
    },

    getDataFn(){
        this.tableLoading=true;
        this.$Loading.start();
        let configData='';
        for (let m in this.queryDataNow){
          if(!this.queryDataNow[m]){
            this.queryDataNow[m]='';
          }
          configData+=m+'='+this.queryDataNow[m].trim()+'&'
        }

        //裁掉最后一个&
        configData=configData.substring(0,configData.length-1)
          this.$axios.post(NET_PORT.assetsQuery+'?currentPage='+this.currentPage+'&pageSize='+this.pageSize+'&'+configData)
          .then((res)=>{
              let initData=res.data;
              let total=Number(initData.pop().total);
              this.assetsDetailData=[];
                for(let i=0;i<initData.length;i++){
                  let configArr=[];
                    for(let k in initData[i]){
                        if(initData[i][k]===''||initData[i][k]==='null'){
                          initData[i][k]='--'; 
                        };
                        configArr.push(k);
                    }
                    this.assetsConfigArr.forEach((m,n)=>{
                    let obj=configArr.find((item,index)=>{
                      return item===m;
                    })

                    if(!obj){
                        initData[i][m]='--'
                      }
                    })
                    
                }
                this.assetsDetailData=initData
                this.totalCount=total;
                this.tableLoading=false;
                this.$Loading.finish()
          })
          .catch((err)=>{
            this.$Loading.error();
          })
    },

    transWebsocketFn(data){
          let dataArr=[];
          let toArr=data.split('{')
          toArr.splice(0,1);
        
          toArr.forEach((item,index)=>{
              let k=item.split('}')
              k.splice(k.length-1,1);
              let m=k[0].split(',')
              let itemObj={};
              m.forEach((g,h)=>{
                  let x=g.split('=');
                  itemObj[x[0].trim()]=x[1];
              })
              
              dataArr.push(itemObj)
          })

          return dataArr;
    },

    initWebSocket(){ 
        this.websock = new WebSocket(NET_PORT.mapSocket+'/'+getToken());
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onclose = this.websocketclose;
    },


    websocketonmessage(e){
        var dataArr=this.transWebsocketFn(e.data)
        this.deviceDetailData=dataArr;
    },

    getSearchConfigFn(){
      // this.queryData.memorylocDept=this.locDept;
      this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
      this.getDataFn()
    }

  },
  created(){
  },
  mounted(){
    this.getSearchConfigFn();
    this.initWebSocket()
  },
  beforeDestroy(){
      //页面销毁时关闭长连接
      this.websock.close()
  }
}
</script>


<style lang='scss' scoped>
    @import '../../../assets/scss/web/common/table.scss';
    @import '../../../assets/scss/web/common/form_data.scss';
</style>
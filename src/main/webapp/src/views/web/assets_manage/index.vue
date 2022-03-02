<template>
  <div class='assets_manage_wrap'>
    <div :class="foldArr[0] ? 'hover_animat custom_bg_color_white fold_wrap fold' : 'hover_animat custom_bg_color_white fold_wrap'">
      <div class="row_box">
          <h2 class="row_title">按条件筛选</h2>
          <div class="fold_box">
              <div class="form_wrap">
                <div class="form_item">
                    <Row :gutter="20">
                        <Col span="6">
                          <h3>资产编号</h3>
                          <Input v-model="queryData.assetID" placeholder="资产编号" />
                        </Col>
                        <Col span="6">
                          <h3>资产名称</h3>
                          <AutoComplete
                              v-model="queryData.assetName"
                              @on-focus="inputFocusFn('assetName')"
                              @on-blur="inputBlurFn"
                              @on-search="handleSearchFn"
                              placeholder="资产名称"
                              style="width:100%">
                              <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                          </AutoComplete>
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
                            <h3>一级分类</h3>
                            <Select v-model="queryData.oneclassify" @on-change="fstCatChangeFn" clearable>
                                <Option v-for="item in fstCatData" :value="item.text" :key="item.text">{{ item.key }}</Option>
                            </Select>
                        </Col>
                        <Col span="6">
                            <h3>二级分类</h3>
                            <Select v-model="queryData.secondclassify" @on-change="secCatChangeFn" clearable>
                                <Option v-for="item in secCatList" :value="item.text" :key="item.text">{{ item.key }}</Option>
                            </Select>
                        </Col>
                        <Col span="6">
                            <h3>三级分类</h3>
                            <Select v-model="queryData.threeclassify" @on-change="thdCatChangeFn" clearable>
                                <Option v-for="item in thdCatList" :value="item.text" :key="item.text">{{ item.key }}</Option>
                            </Select>
                        </Col>
                        <Col span="6">
                            <h3>四级分类</h3>
                            <Select v-model="queryData.fourclassify" clearable>
                                <Option v-for="item in fouCatList" :value="item.text" :key="item.text">{{ item.key }}</Option>
                            </Select>
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
                         <Col span="12">
                            <!-- <h3>是否填充上次搜索数据</h3> -->
                            <div style="margin-top:38px">
                              <i-switch  v-model="fillSearch" @on-change="fillSearchFn"/><span style="margin-left:10px">是否填充上次搜索数据</span>
                            </div>
                        </Col>
                        
                    </Row>
                </div>

                <div class="form_item">
                    <Row :gutter="20">
                        <Col span="24">
                        <h3>可选择显示项目</h3>
                              <CheckboxGroup v-model="chooseArr">
                                
                                  <Checkbox v-for="(item,index) in chooseInit" :label="item.label" :key="index"></Checkbox>
                                
                              </CheckboxGroup>
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
          <h2 class="row_title">资产明细表</h2>
          <div class="data_table_top clearfix">
            <div class="data_table_top_left">
              <Button to="/web/assets_create" shape="circle" type="primary" icon="md-add">新增资产</Button>
              
              <Button v-if="$store.state.role==='admin'" shape="circle" type="error" icon="md-trash" @click="deleteFn">删除资产</Button>
              <!-- <Upload
                :before-upload="handleUpload"
                action=""
                accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
              >
                  <Button  shape="circle" type="warning" icon="md-cloud-upload">导入资产数据</Button>
              </Upload> -->
              <Button shape="circle" type="success" icon="md-archive" @click=exportExcelFn>导出资产总表</Button>
              
            </div>
            <div class="data_table_top_right">
                <!-- <div class="pagination_wrap">
                  <Page show-elevator show-total show-sizer :total="totalCount" :page-size-opts="pageSizeOptions" @on-change="changePageFn" @on-page-size-change="changePageSizeFn" :current="currentPage"  :page-size="pageSize"/>
                </div> -->
            </div>
          </div>

          <div class="table_scroll_wrap">
            <Table id="assetsTable"  stripe :columns="assetsDetailColumn" :data="assetsDetailData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                <template slot-scope="{row}" slot="assetID">
                  <a @click="toDetailFn(row.assetID)">{{row.assetID}}</a>
                </template>

                <template slot-scope="{row}" slot="electric">
                    <span>{{row.electric}}%</span>
                </template>

                <template slot-scope="{ row }" slot="status">
                    <Tag v-if="row.status==='正常'" :color="statusColor[0]" ><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='待维修'" :color="statusColor[1]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='维修接单'" :color="statusColor[2]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='待报废'" :color="statusColor[3]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='报废'" :color="statusColor[4]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='外借'" :color="statusColor[5]" ><span>{{row.status}}</span></Tag>
                    <Tag v-else  color="default"><span>{{row.status}}</span></Tag>
                </template>
              
            </Table>
          </div>
          <div class="data_table_pagination_wrap">
            <Page show-elevator show-total show-sizer :total="totalCount" :page-size-opts="pageSizeOptions" @on-change="changePageFn" @on-page-size-change="changePageSizeFn" :current="currentPage"  :page-size="pageSize"/>
          </div>
      </div>
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
import { Row,Col,Icon,Table ,Tag ,DatePicker,Select,Option,Page,Input,CheckboxGroup,Checkbox,Button,Modal,Spin,AutoComplete,Upload,Switch} from 'iview';
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
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
Vue.component('CheckboxGroup',CheckboxGroup);
Vue.component('Checkbox',Checkbox);
Vue.component('AutoComplete',AutoComplete);
Vue.component('Upload',Upload);
Vue.component('i-switch',Switch);

import {fstCatData,secCatData,thdCatData,fouCatData} from "../../../assets/data/category.js"
import { getToken } from '../../../utils/auth.js'
import NET_PORT from "../../../api/port.js"
import statusColor from "../../../assets/data/statusColor.js"
import statusData from "../../../assets/data/status.js"
import assetsConfigArr from "../../../assets/data/assetsConfigArr.js"
export default {
  name:'assets_manage',
  components:{},
  props:{},
  data(){
    return {
      statusData:statusData,
      statusColor:statusColor,
      isFirstEnter:false,
      inputData:[],
      inputDataAll:[],
      fstCatData:fstCatData,
      secCatData:secCatData,
      thdCatData:thdCatData,
      fouCatData:fouCatData,
      secCatList:[],
      thdCatList:[],
      fouCatList:[],
      
      chooseArr: [],
      chooseInit:[
        {
          value:'isShowMac',
          label:'标签mac',
          key:'mac',
        },
        {
          value:'isShowBuyDate',
          label:'入库时间',
          key:'buyDate',
        },
        {
          value:'isShowMoney',
          label:'原值',
          key:'money',
        },
        {
          value:'isShowElectric',
          label:'电量',
          key:'electric',
        },
        {
          value:'isShowSpecification',
          label:'规格',
          key:'specification',
        },
        {
          value:'isShowType',
          label:'型号',
          key:'type',
        },
        {
          value:'isShowLocation',
          label:'存放地点',
          key:'location',
        },
        {
          value:'isShowPlaceoforigin',
          label:'产地',
          key:'placeoforigin',
        },
        {
          value:'isShowBrand',
          label:'品牌',
          key:'brand',
        },
        {
          value:'isShowDepartmentcode',
          label:'部门编码',
          key:'departmentcode',
        },
        {
          value:'isShowDepartmentroom',
          label:'部门名称',
          key:'departmentroom',
        },
        {
          value:'isShowHomeofficenumber',
          label:'大科室编码',
          key:'homeofficenumber',
        },
        {
          value:'isShowHomeofficename',
          label:'大科室名称',
          key:'homeofficename',
        },
        {
          value:'isShowLocDept',
          label:'所在科室名称',
          key:'locDept',
        },
        {
          value:'isShowIsentrance',
          label:'是否进口',
          key:'isentrance',
        },
        {
          value:'isShowSuppliername',
          label:'供应商名称',
          key:'suppliername',
        },
        {
          value:'isShowGeneratebusinessname',
          label:'生产厂商名称',
          key:'generatebusinessname',
        },
        {
          value:'isShowApplyoddnumbers',
          label:'处置申请单号',
          key:'applyoddnumbers',
        },
        
        {
          value:'isShowStatus',
          label:'状态',
          key:'status',
        },
        {
          value:'isShowSn',
          label:'SN',
          key:'sn',
        },
        {
          value:'isShowArchitecture',
          label:'建筑',
          key:'architecture',
        },
        {
          value:'isShowAcademy',
          label:'院区',
          key:'academy',
        },
      ],

      assetsConfigArr:assetsConfigArr,
      
      foldArr:[false,],
      queryData:{
        assetID:'',
        assetName:'',
        memorydepart:'',
        memorylocDept:"",
        oneclassify:"",
        secondclassify:"",
        threeclassify:"",
        fourclassify:"",
        mac:"",
        status:"",
      },

      queryDataNow:{},
      fillSearch:false,
      
      chooseArrNow:[],

      tableLoading:true,

      selection:[],
      
      totalCount: 0,
      pageSize: 10,
      currentPage: 1,
      pageSizeOptions:[10,20,30,40,50,60,70,80,90,100],
        
      deleteModal:false,
      achieveDelete:false,
      errorDelete:false,

      assetsDetailColumnInit:[
        
        {
            title: '资产编号',
            key: 'assetID',
            slot:'assetID',
            
        },
        {
            title: '通用名称',
            key: 'generalName'
        },
        {
            title: '资产名称',
            key: 'assetName'
        },

      ],

      assetsDetailColumn:[],
      assetsDetailData:[],

    }
  },
  watch:{
    chooseArrNow(){
      let assetsTable=document.getElementById('assetsTable');
      if(this.chooseArrNow.length<=4){
        assetsTable.style.minWidth="1000px";
      }else if(this.chooseArrNow.length<=8){
        assetsTable.style.minWidth="1500px";
      }else if(this.chooseArrNow.length>8&&this.chooseArrNow.length<=14){
        assetsTable.style.minWidth="2000px";
      }else if(this.chooseArrNow.length>14){
        assetsTable.style.minWidth="2500px";
      }
    }
  },
  computed:{
  
  },
  methods:{
    fillSearchFn(){
      if(this.fillSearch){
          let queryData=JSON.parse(window.sessionStorage.getItem('queryData'));
      
          this.queryData={
            assetID:'',
            assetName:'',
            memorydepart:queryData.memorydepart ? queryData.memorydepart : '',
            memorylocDept:queryData.memorylocDept? queryData.memorylocDept : '',
            oneclassify:queryData.memoryoneclassify ? queryData.memoryoneclassify : '',
            secondclassify:queryData.memorysecondclassify ? queryData.memorysecondclassify : '',
            threeclassify:queryData.memorythreeclassify ? queryData.memorythreeclassify : '',
            fourclassify:queryData.memoryfourclassify ? queryData.memoryfourclassify : '',
            mac:'',
            status:'',
          }
          //初始化分类数据
          this.initCatFn();
          // this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
      }else{
        for(let i in this.queryData){
          this.queryData[i]="";
        }
      }
    },
    handleUpload(file){
        return false;
    },
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
            // console.log(err)
        })
    },
    fstCatChangeFn(){
        this.secCatList=this.secCatData.filter((item,index)=>{
            return item.parent===this.queryData.oneclassify;
        })
        this.queryData.secondclassify='';
        this.queryData.threeclassify='';
        this.queryData.fourclassify='';
    },
    secCatChangeFn(){
        this.thdCatList=this.thdCatData.filter((item,index)=>{
            return item.parent===this.queryData.secondclassify;
        })
        this.queryData.threeclassify='';
        this.queryData.fourclassify='';
    },
    thdCatChangeFn(){
        this.fouCatList=this.fouCatData.filter((item,index)=>{
            return item.parent===this.queryData.threeclassify;
        })
        this.queryData.fourclassify='';
    },

    initCatFn(){
        this.secCatList=this.secCatData.filter((item,index)=>{
            return item.parent===this.queryData.oneclassify;
        })
        this.thdCatList=this.thdCatData.filter((item,index)=>{
            return item.parent===this.queryData.secondclassify;
        })

        this.fouCatList=this.fouCatData.filter((item,index)=>{
            return item.parent===this.queryData.threeclassify;
        })
    },

    foldFn(index){
      this.$set( this.foldArr,index, !this.foldArr[index])
    },
  
     //点击状态去到不同页面
    statusFn(status){
      if(status==='正常'){
        this.$router.push('/web/device_manage');
      }else {
        this.$router.push('/web/damage_manage');
      }
    },

    //导出总excel
    exportExcelFn(){
        this.$axios.get(NET_PORT.assetsExport,{responseType: 'blob'})
        .then((res)=>{
              let blob = res.data;
              let reader = new FileReader();
              reader.readAsDataURL(blob);
              reader.onload = function (e) {
                  let a = document.createElement("a");
                  a.download = name + ".xls";
                  a.href = e.target.result;
                  document.documentElement.appendChild(a);
                  document.documentElement.removeChild(a)
                  a.click();
              };
        })
        .catch((err)=>{
          // console.log(err)
        })
    },

     //点击多选按钮
    checkAllGroupChange(selection){
      this.selection=selection;
    },

    //添加和删除指定的显示选项
    filterChooseFn(){
      //先让属性数组等于必须显示的那些元素；
      this.assetsDetailColumn=JSON.parse(JSON.stringify(this.assetsDetailColumnInit))

      if(this.chooseArrNow.length){

        this.chooseArrNow.forEach((item,index)=>{
          for(let i=0;i<this.chooseInit.length;i++){
            if(this.chooseInit[i].label===item){
              if(item==='状态'){
                let obj={};
                obj.title='状态';
                obj.key='status';
                obj.slot='status';
                obj.width='120';
                this.assetsDetailColumn.push(obj)
              }else{
                let obj={};
                obj.title=this.chooseInit[i].label;
                obj.key=this.chooseInit[i].key;
                this.assetsDetailColumn.push(obj)
              }
                
            }
          }
          
        })
      }
     
    },


    //点击搜索
    searchAssetsFn(){
        this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
        this.chooseArrNow=JSON.parse(JSON.stringify(this.chooseArr))

        let chooseData={}
        this.chooseInit.forEach((item,index)=>{
          for(let i=0;i<this.chooseArrNow.length;i++){
            if(item.label===this.chooseArrNow[i]){
              chooseData[item.value]='1'
              return
            }else{
              chooseData[item.value]='0'
            }
          }
        })

        // let queryData={
        //     memorydepart:this.queryData.memorydepart,
        //     memorylocDept:this.departmentroom,
        //     memoryoneclassify:this.queryData.oneclassify,
        //     memorysecondclassify:this.queryData.secondclassify,
        //     memorythreeclassify:this.queryData.threeclassify,
        //     memoryfourclassify:this.queryData.fourclassify,
        // }

        //先在本地保存
        // this.$store.commit('setQueryData',queryData)
        this.$store.commit('setChooseData',chooseData)

        this.currentPage=1;
        //过滤多选显示属性
        this.filterChooseFn();
        //获取数据
        this.getDataFn();
    },

    //清除搜索数据
    clearAssetsFn(){
        for(let i in this.queryData){
          this.queryData[i]="";
        }
        this.chooseArr=[];
        this.fillSearch=false;
    },

    //点击多选按钮
    checkAllGroupChange(selection){
      this.selection=selection;
    },

    //点击改变页码
    changePageFn(page){
      this.currentPage=page;
      //获取数据
      this.getDataFn();
    },

    changePageSizeFn(pageSize){
      this.pageSize=pageSize;
      this.currentPage=1;
      this.getDataFn();
    },

      //点击删除资产按钮
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
      let list=[];
      for(let i=0;i<this.selection.length;i++){
        list.push(this.selection[i].assetID)
      }
      
      this.$axios.get(NET_PORT.assetsDelete+list)
      .then((res)=>{

          if(res.data.msg==='ok'){
            this.achieveDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                //删除成功后隔1秒隐藏模态框并更新数据
                this.currentPage=1;
                this.getDataFn();
                
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

    //点击资产编号去到资产详情页面
    toDetailFn(id){

        this.$router.push({
          path:'/web/assets_detail/'+id
        })

    },

    //向服务器获取资产数据
    getDataFn(){
        this.tableLoading=true;
        this.$Loading.start();
        let configData='';
        for (let m in this.queryDataNow){
          if(!this.queryDataNow[m]){
            this.queryDataNow[m]='';
          }
          //将参数拼接到一起，裁掉左右空格
          configData+=m+'='+this.queryDataNow[m].trim()+'&'
        }

        let chooseData={}
        this.chooseInit.forEach((item,index)=>{
          for(let i=0;i<this.chooseArrNow.length;i++){
            if(item.label===this.chooseArrNow[i]){
              chooseData[item.value]='1'
              return
            }else{
              chooseData[item.value]='0'
            }
          }
        })

        let chooseConfigData='';

        for (let n in chooseData){
          if(!chooseData[n]){
            chooseData[n]='';
          }
          chooseConfigData+=n+'='+chooseData[n].trim()+'&'
        }
        let allConfigData=configData+chooseConfigData
        //裁掉最后一个&
        allConfigData=allConfigData.substring(0,allConfigData.length-1)
        this.$axios.post(NET_PORT.assetsQuery+'?currentPage='+this.currentPage+'&pageSize='+this.pageSize+'&'+allConfigData)
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
              this.assetsDetailData=initData;
              this.totalCount=total;
              this.tableLoading=false;
              this.$Loading.finish()
        })
        .catch((err)=>{
          this.$Loading.error();
        })
    },
    

    //获取搜索配置
    getSearchConfigFn(){
      let chooseData=JSON.parse(window.sessionStorage.getItem('chooseData'));

      this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));

      //过滤可选项目
      let chooseArr=[]
      this.chooseInit.forEach((item,index)=>{
          for(let i in chooseData){
            if(chooseData[i]==='1'){
              if(item.value===i){
                chooseArr.push(item.label);
                return
              }
            }
          }
      })

      this.chooseArr=JSON.parse(JSON.stringify(chooseArr))
      this.chooseArrNow=JSON.parse(JSON.stringify(this.chooseArr))

      this.filterChooseFn();
      this.getDataFn()
    },

    authorityFn(){
      //判断角色类型，为表格添加选择项
      if(this.$store.state.role==="admin"){
          let obj=this.assetsDetailColumnInit.find((item,index)=>{
            return item.title==='选择'
          })
          //如果不存在多选这个属性就添加进去
          if(!obj){
            this.assetsDetailColumnInit.unshift(
              {
                title:'选择',
                slot:'selection',
                type: 'selection',
                width:80,
              },
            )
          }
      }

    },

    initAllDataFn(){
      this.chooseArr=[]
      this.queryData={
          assetID:'',
          assetName:'',
          memorydepart:'',
          memorylocDept:'',
          oneclassify:"",
          secondclassify:"",
          threeclassify:"",
          fourclassify:"",
          mac:"",
          status:"",
      },
      this.queryDataNow={}
      this.tableLoading=true
      this.selection=[]
      this.totalCount= 0
      this.pageSize= 10
      this.currentPage= 1
      this.deleteModal=false
      this.achieveDelete=false
      this.errorDelete=false
      this.assetsDetailColumn=[]
      this.assetsDetailData=[]
      this.fillSearch=false

      //判断角色类型，是否添加多选属性
      this.authorityFn();
      this.assetsDetailColumn=JSON.parse(JSON.stringify(this.assetsDetailColumnInit))

    }

  },

  beforeRouteEnter(to, from, next) {
   
    if(from.name=='assets_detail'){
      
      if(from.meta.needFresh){
        to.meta.isBack=false;
      }else{
        to.meta.isBack=true;
      }
        
    }

    next();
  },

  created(){
      this.isFirstEnter=true;
  },
  mounted(){

  },

  activated() {
    if(!this.$route.meta.isBack || this.isFirstEnter){
        //先清空数据，避免让用户看到之前缓存的数据
        this.initAllDataFn();
        //执行获取参数的方法
        this.getSearchConfigFn();
    }
    // 恢复成默认的false，避免isBack一直是true，导致下次无法获取数据
    this.$route.meta.isBack=false
    // 恢复成默认的false，避免isBack一直是true，导致每次都获取新数据
    this.isFirstEnter=false;

  },

}
</script>

<style lang='scss' scoped>
    @import '../../../assets/scss/web/common/table.scss';
    @import '../../../assets/scss/web/common/form_data.scss';
</style>
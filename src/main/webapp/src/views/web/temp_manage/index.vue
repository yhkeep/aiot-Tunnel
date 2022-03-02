<template>
  <div id='temp_manage_wrap'>
        <div :class="foldArr[0] ? 'hover_animat custom_bg_color_white fold_wrap fold' : 'hover_animat custom_bg_color_white fold_wrap'">
            <div class="row_box">
                <h2 class="row_title">按条件筛选</h2>
                <div class="fold_box">
                    <div class="form_wrap">
                        <div class="form_item">
                            <Row :gutter="20">
                                <Col span="6">
                                    <h3>仪器编号</h3>
                                    <Input v-model="queryData.freezernumber" placeholder="仪器编号" />
                                </Col>
                                <Col span="6">
                                    <h3>仪器名称</h3>
                                    <Input v-model="queryData.freezername" placeholder="仪器名称" />
                                </Col>
                                <Col span="6">
                                    <h3>部门名称</h3>
                                    <!-- <Input v-model="queryData.department" placeholder="部门名称" /> -->
                                    <!-- <AutoComplete
                                        v-model="queryData.department"
                                        @on-focus="inputFocusFn('locDept')"
                                        @on-blur="inputBlurFn"
                                        @on-search="handleSearchFn"
                                        placeholder="科室名称"
                                        :disabled="locDept!==''"
                                        >
                                        <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                    </AutoComplete> -->
                                    <Select v-model="queryData.department">
                                        <Option v-for="item in departmentroomArr" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                    </Select>
                                </Col>
                                <Col span="6">
                                    <h3>仪器类型</h3>
                                    <Select v-model="queryData.type">
                                        <Option v-for="item in instrumentType" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                    </Select>
                                </Col>
                            </Row>
                        </div>
                        <div class="form_item">
                            <Row :gutter="20">
                                <Col span="6">
                                    <h3>mac地址</h3>
                                    <Input v-model="queryData.mac" placeholder="mac地址" />
                                </Col>
                              
                            </Row>
                        </div>
                    </div>
                    <div class="submit_btn_wrap">
                            <Button type="primary" shape="circle" @click="searchTempFn">搜索</Button>
                            <Button type="error" shape="circle"  @click="clearTempFn">清空</Button>
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
              <h2 class="row_title">温湿度明细表</h2>
              <div class="data_table_top clearfix">
                  <div class="data_table_top_left">
                    <Button to="/web/temp_create" type="primary" shape="circle" icon="md-add">新增仪器</Button>
                    <Button v-if="$store.state.role==='admin'" type="error" shape="circle" icon="md-trash" @click="deleteFn">删除仪器</Button>
                    <Button shape="circle" type="success" icon="md-archive" @click="showExport=true;">导出历史数据excel</Button>
                  </div>
                  <div class="data_table_top_right">
                        <Badge class="notice_icon" :count="this.tempWarningArr.length" >
                            <Icon type="ios-notifications-outline" size="26" @click="noticeFn"></Icon>
                        </Badge>
                  </div>
              </div>

              <div class="table_scroll_wrap">
                  <Table style="min-width:1800px" stripe :columns="tempDetailColumn" :data="tempDetailDataNow" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                      <template slot-scope="{ row }" slot="temp">
                          <span>{{row.temp? row.temp+'℃' : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="humi">
                          <span>{{row.humi? row.humi+'%' : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="topTemp">
                          <span>{{row.topTemp ? row.topTemp+'℃' : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="bottomTemp">
                          <span>{{row.bottomTemp? row.bottomTemp+'℃' : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="topHum">
                          <span>{{row.topHum? row.topHum+'%' : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="bottomHum">
                          <span>{{row.bottomHum? row.bottomHum+'%' : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="electric">
                          <span>{{row.electric? row.electric+'%' : '--'}}</span>
                      </template>
                   
                      <template slot-scope="{ row }" slot="operation">
                          <Button shape="circle" type="default" icon="ios-book" @click="toTempDetailFn(row.mac)">查看</Button>
                          <Button shape="circle" type="default" icon="md-create" @click="toTempEditFn(row.mac)">编辑</Button>
                      </template>
                  </Table>
              </div>

              <div class="data_table_pagination_wrap">
                <Page show-elevator show-total  :total="totalCount"  @on-change="changePageFn"  :current="currentPage"  :page-size="pageSize"/>
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

      <Modal v-model="showExport" title="选择导出时间范围（时间跨度不超过1个月！）" :mask-closable="false" :closable="false" width="400">
        <DatePicker  type="datetimerange" placeholder="选择日期范围" style="width:100%" @on-change="dateRangeFn"></DatePicker>
        <div slot="footer"> 
          <Button @click="showExport=false;">取消</Button>
          <Button @click="exportExcelFn" type="success">确定导出</Button>
        </div>
      </Modal>


    <Drawer  :closable="false" v-model="showNotice" class="warning_wrap" width="15">
    
        <div slot="header" class="warning_title">
            <h2 >温湿度异常一览</h2>
        </div>

        <ul v-if="tempWarningArr.length">
          <li class="warning_li" v-for="(item, index) in tempWarningArr">
              <p><b>{{index+1}} - {{ item.freezername }} - {{item.name}}</b></p>
              <p>告警内容：<b class="warning_imp">{{item.desc}}</b></p>
              <p v-if="item.electric" >电量：<b class="warning_imp">{{item.electric}}</b></p>
              <p v-if="item.temperature" >温度：<b class="warning_imp">{{item.temperature}}</b></p>
              <p v-if="item.temperaturefitted">温度范围：<b>{{item.temperaturefitted}}</b></p>
              <p v-if="item.humidity" >湿度：<b class="warning_imp">{{item.humidity}}</b></p>
              <p v-if="item.humidityfitted">湿度范围：<b>{{item.humidityfitted}}</b></p>
              <p v-if="item.equipID">mac：<b>{{item.equipID}}</b></p>
              <p v-if="item.location">位置：<b>{{item.location}}</b></p>
              <p v-if="item.locDept">所在科室：<b>{{item.locDept}}</b></p>
              <p v-if="item.date">日期：<b>{{item.date}}</b></p>
          </li>
        </ul>
        <p v-else class="warning_notice">暂无异常告警!</p>

    </Drawer>

  </div>
</template>

<script>
import Vue from "vue"
import { Row,Col,Icon,Table ,Tag ,Select,Option,Page,Input,Button,Drawer,Modal,Spin,AutoComplete,DatePicker} from 'iview';
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
Vue.component('Drawer',Drawer);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
Vue.component('AutoComplete',AutoComplete);
Vue.component('DatePicker',DatePicker);
import instrumentType from "../../../assets/data/instrumentType.js"

import NET_PORT from "../../../api/port.js"
import { getToken } from '../../../utils/auth.js';
export default {
  name:'temp_manage',
  components:{},
  props:{},
  data(){
    return {
      showExport:false,
       exportTimeStart:'',
      exportTimeEnd:'',
      // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
      departmentroomArr:[],
      queryData:{
          freezernumber:'',
          mac:'',
          freezername:'',
          department:'',
          type:'',
      },

      queryDataNow:{
          freezernumber:'',
          mac:'',
          freezername:'',
          department:'',
          type:'',
      },

      searchMode:false,
      changePageMode:false,
      selectMode:false,
      instrumentType:instrumentType,

      selection:[],

      tableLoading:true,
      showNotice:false,

      deleteModal:false,
      achieveDelete:false,
      errorDelete:false,

      foldArr:[false,],

     

      tempDetailColumnInit:[
          {
            key: 'freezernumber', 
            title: '编号',
          },
          {
            key: 'freezername',
            title: '名称',
          },
          {
              key: 'mac',
              title: 'MAC',
          },
          {
            key: 'type', 
            title: '类型',
          },
          {
            key: 'department',
            title: '部门',
          },
          {
              key: 'location',
              title: '位置',
          },
          {
              key: 'electric',
              title: '电量（%）',
              slot:'electric'
          },
          {
            key: 'temp', 
            title: '温度（℃）',
            slot:'temp'
          },
          {
            key: 'humi',
              title: '湿度（%）',
            slot:'humi'
          },
          {
            key: 'topTemp',
            title: '今日最高温度（℃）',
            slot:'topTemp'
          },
          {
            key: 'bottomTemp',
            title: '今日最低温度（℃）',
            slot:'bottomTemp'
          },
          {
            key: 'topHum', 
            title: '今日最高湿度（%）',
            slot:'topHum'
          }, 
          {
            key: 'bottomHum',
            title: '今日最低湿度（%）',
            slot:'bottomHum'
          },
          {
            key: 'currenttime',
            title: '数据接收时间',
          },
          {
            key: 'operation',
            title: '操作',
            slot:'operation',
            width:200,
          }
      ],
      tempDetailColumn:[],

      tempDetailData:[],
      tempDetailDataNow:[],
      tempDetailDataAll:[],

      websock: null,
      warningSocket: null,
      // inputData:[],
      // inputDataAll:[],

      totalCount: 0,
      pageSize: 10,
      currentPage: 1,
      tempWarningArr:[],
    }
  },
  watch:{

  },
  computed:{},
  methods:{

      // handleSearchFn(data){
      //     this.inputData = this.inputDataAll.filter(item => item.toLowerCase().indexOf(data.toLowerCase()) > -1);
      // },

      // inputBlurFn(){
      //     this.inputDataAll=[];
      //     this.inputData=[];
      // },

      // inputFocusFn(key){
      //     this.inputDataAll=[];
      //     this.inputData=[];
      //     let queryArr=[];
      //     let obj={};
      //     obj[key]="1"
      //     queryArr.push(obj)
      //     this.$axios.post(NET_PORT.inputQuery,queryArr)
      //     .then((res)=>{
      //         res.data.data.forEach((item,index)=>{
              
      //             if(this.inputDataAll.find((result)=>{return result===item[key]})){

      //             }else{
      //                 this.inputDataAll.push(item[key])
      //             }
      //         })

      //         setTimeout(()=>{
      //             this.inputData=JSON.parse(JSON.stringify(this.inputDataAll))
      //         },300)
      //     })
      //     .catch((err)=>{
      //         console.log(err)
      //     })
      // },

      searchTempFn(){
        // this.tempDetailDataNow=[]
        this.searchMode=true;
        this.selection=[];
        this.selectMode=false;
        this.tableLoading=true;
        setTimeout(()=>{
            this.queryDataNow=JSON.parse(JSON.stringify(this.queryData))

            this.currentPage=1;
            this.filterSearchFn(this.tempDetailDataAll)

        },300)
       
      },

      filterSearchFn(arr){
         // 算出搜索属性个数
        let length=0;
        //算出有多少属性为空
        let key=0;
        for(let i in this.queryDataNow){
          if(this.queryDataNow[i].trim()===''){
            key++;
          }
          length++;
        };

        if(key<length){
          let dataResult=[];
          arr.forEach((item,index)=>{
              let fillKey=0;
              let commonKey=0;
              for(let i in this.queryDataNow){
                if(item[i]===undefined){
                  item[i]='';
                }
                if(this.queryDataNow[i].trim()!==''){
                  fillKey++;
                  if(i==='freezernumber'||i==='mac'){
                    if(item[i].trim().toLowerCase()===this.queryDataNow[i].trim().toLowerCase()){
                      commonKey++
                    }
                  }else if(i==='freezername'){
                    var reg = new RegExp(this.queryDataNow[i].trim());
                    if(item[i].trim().match(reg)){
                        commonKey++
                    }
                  }else if(i==='department'){
                    var reg = new RegExp(this.queryDataNow[i].trim());
                    if(item[i].trim().match(reg)){
                        commonKey++
                    }
                  }else{
                    if(item[i].trim()===this.queryDataNow[i].trim()){
                      commonKey++
                    }
                  }
                }
              }
              if(fillKey===commonKey){
                dataResult.push(item)
              }
          })
          this.setPageFn(dataResult)
          this.tempDetailData=JSON.parse(JSON.stringify(dataResult))
        }else{
          this.setPageFn(arr)
          this.tempDetailData=JSON.parse(JSON.stringify(arr))
        }


      },

      clearTempFn(){
        for(let i in this.queryData){
          this.queryData[i]="";
        }

      },

      foldFn(index){
        this.$set( this.foldArr,index, !this.foldArr[index])
      },

      initWebSocket(){ 
          this.websock = new WebSocket(NET_PORT.tempDetailSocket+'/'+getToken());
          this.websock.onmessage = this.websocketonmessage;
      },
    
      websocketonmessage(e){
          if(e.data){
            let arr=JSON.parse(e.data)
            let dataResult=this.formatTempFn(arr)
            this.tempDetailDataAll=JSON.parse(JSON.stringify(dataResult))

            //不在多选模式下可以更新数据
            if(!this.selectMode){
              //不在点击切换页码时可以更新数据
               if(!this.changePageMode){
                if(this.searchMode){
                  this.filterSearchFn(dataResult);
                }else{
                  this.setPageFn(dataResult)
                  this.tempDetailData=JSON.parse(JSON.stringify(dataResult))
                }
                // this.tableLoading=false;
              }
            }
            
          }
      },

      initWarningSocket(){
          this.warningSocket = new WebSocket(NET_PORT.tempWarningSocket+'/'+getToken());
          this.warningSocket.onmessage = this.warningSocketOnMessage;
      },

      warningSocketOnMessage(e){
          if(e.data){
            let arr=JSON.parse(e.data)
            this.tempDetailDataAll.forEach((item,index)=>{
              for(let i=0;i<arr.length;i++){
                if(arr[i].equipID===item.mac){
                  arr[i].locDept=item.department;
                  arr[i].freezername=item.freezername;
                  arr[i].location=item.location;
                }
              }
            })

            // if(this.locDept!==''){
            //  this.tempWarningArr=arr.filter((item,index)=>{
            //    return item.locDept===this.locDept;
            //  })
            // }else{
            //     this.tempWarningArr=arr;
            // }
            this.tempWarningArr=arr;

          }
      },

      setPageFn(arr){
        if(arr.length){
          let dataResult=[];
            let startIndex=this.pageSize*(this.currentPage-1)
            let endIndex=((this.pageSize*this.currentPage)-1) >arr.length-1 ? arr.length-1 : ((this.pageSize*this.currentPage)-1)
            for(let i=startIndex;i<=endIndex;i++){
              dataResult.push(arr[i])
            }
            this.tempDetailDataNow=dataResult;
            this.totalCount=arr.length;
        }else{
            this.tempDetailDataNow=[];
            this.currentPage=1;
            this.totalCount=0;
        }

        if(this.tableLoading){
          this.tableLoading=false;
        }
      },

       //点击改变页码
      changePageFn(page){
        this.changePageMode=true;
        this.selection=[];
        this.selectMode=false;
        // this.tempDetailDataNow=[]
        this.tableLoading=true;
        setTimeout(()=>{
            let arr=[];
            if(this.tempDetailData.length){
                let startIndex=this.pageSize*(page-1)
                let endIndex=((this.pageSize*page)-1) >this.tempDetailData.length-1 ? this.tempDetailData.length-1 : ((this.pageSize*page)-1)
                for(let i=startIndex;i<=endIndex;i++){
                  arr.push(this.tempDetailData[i])
                }
                this.tempDetailDataNow=arr;
            }

            this.currentPage=page;
            this.tableLoading=false;
            this.changePageMode=false;
        },300)
       
      },

      formatTempFn(data){
        let arr=[];
        data.forEach((item,index)=>{
          let obj={
            freezername: item.freezername ? item.freezername : '--',
            freezernumber: item.freezernumber ? item.freezernumber : '--',
            location: item.location ? item.location : '--',
            department: item.department ? item.department : '--',
            type: item.type ? item.type : '--',
            mac: item.mac ? item.mac : '--',
            temp:item.temperature? Number(Number(item.temperature).toFixed(2)) :'',
            humi:item.humidity ? Number(Number(item.humidity).toFixed(2)) : '',
            topTemp:item.toptemperature ? Number(Number(item.toptemperature).toFixed(2)) :'',
            topHum:item.tophumidity ? Number(Number(item.tophumidity).toFixed(2)) :'',
            bottomTemp:item.mintemperature ? Number(Number(item.mintemperature).toFixed(2)) : '',
            bottomHum:item.minhumidity ? Number(Number(item.minhumidity).toFixed(2)) : '',
            electric:item.electric ? item.electric: '',
            currenttime:item.currenttime ? item.currenttime: '',

          }
          arr.push(obj);
        })

        return arr;
      },

      noticeFn() {
          this.showNotice=true;
      },

      toTempDetailFn(mac){
          this.$router.push({
              path:`/web/temp_detail/${mac}`
          })
      },

      toTempEditFn(mac){
          this.$router.push({
              path:`/web/temp_edit/${mac}`
          })
      },

      //点击多选按钮
      checkAllGroupChange(selection){
        if(selection.length){
          this.selectMode=true;
        }else{
          this.selectMode=false;
        }
        this.selection=selection;
      },

      deleteFn(){
          if(this.selection.length){
                this.$Modal.confirm({
                      title:'删除提示',
                      okText:'确定删除',
                      cancelText:'取消',
                      content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+'个仪器吗？</h2>',
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
          list.push(this.selection[i].mac)
        }
        
        this.$axios.get(NET_PORT.tempDelete+list)
        .then((res)=>{

            if(res.data.code===0){
              this.achieveDelete=true;
              setTimeout(()=>{
                  this.deleteModal=false;
                  this.selectMode=false;
                  this.selection=[];
                  this.searchTempFn();
                  
                  setTimeout(()=>{
                      this.achieveDelete=false;
                      this.errorDelete=false;
                  },500)
                  
              },1000)
              
            }else{
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

     

      authorityFn(){
        //判断角色类型，为表格添加选择项
        if(this.$store.state.role==="admin"){
            let obj=this.tempDetailColumnInit.find((item,index)=>{
              return item.title==='选择'
            })
            //如果不存在多选这个属性就添加进去
            if(!obj){
              this.tempDetailColumnInit.unshift(
                {
                  title:'选择',
                  slot:'selection',
                  type: 'selection',
                  width:80,
                },
              )
            }
          
        }else{
          
        }
      },

      initAllDataFn(){
        this.queryData={
              freezernumber:'',
              mac:'',
              freezername:'',
              department:'',
              type:'',
          };

          this.queryDataNow={
              freezernumber:'',
              mac:'',
              freezername:'',
              department:'',
              type:'',
          },

          this.searchMode=false;
          this.changePageMode=false;
          this.selectMode=false;

          this.selection=[];

          this.tableLoading=true;
          this.showNotice=false;

          this.deleteModal=false;
          this.achieveDelete=false;
          this.errorDelete=false;
          this.tempDetailColumn=[];

          this.tempDetailData=[];
          this.tempDetailDataNow=[];
          this.tempDetailDataAll=[];

          this.websock= null;
          this.warningSocket= null;
          // inputData:[],
          // inputDataAll:[],

          this.totalCount= 0;
          this.pageSize= 10;
          this.currentPage= 1;
          this.tempWarningArr=[];

          let arr=[];
          this.$store.state.departmentroom.split(',').forEach((item,index)=>{
            arr.push(
              {
                key:item,
                text:item,
              }
            )
          })

          this.departmentroomArr=arr;

          this.authorityFn();
          
          this.tempDetailColumn=JSON.parse(JSON.stringify(this.tempDetailColumnInit))
          this.initWebSocket();
          this.initWarningSocket();
      },

      dateRangeFn(value){
        //设置日期范围
        this.exportTimeStart=value[0]
        this.exportTimeEnd=value[1]
      },

    exportExcelFn(){
        if(this.exportTimeStart===''||this.exportTimeEnd===''){
          this.$Message.error({
              content:"请选择时间范围！",
              duration:2,
          })
        }else {
          let start=this.$Moment(this.exportTimeStart).unix()*1000
          let end=this.$Moment(this.exportTimeEnd).unix()*1000
          let differ=end-start
          var days=Math.floor(differ/(24*3600*1000))
          if(days>31){
            this.$Message.error({
                content:"时间范围不能超过1个月！",
                duration:2,
            })
          }else{
                let data={
                  startime:this.exportTimeStart,
                  endtime:this.exportTimeEnd,
                }
                  this.$axios.get(NET_PORT.tempExport+'?startime='+this.exportTimeStart+"&endtime="+this.exportTimeEnd,{responseType: 'blob'})
                  .then((res)=>{
                        let blob = res.data;
                        let reader = new FileReader();
                        reader.readAsDataURL(blob);
                        reader.onload = function (e) {
                            let a = document.createElement("a");
                            a.download ="温湿度历史数据.xls";
                            a.href = e.target.result;
                            document.documentElement.appendChild(a);
                            document.documentElement.removeChild(a)
                            a.click();
                        };
                        this.showExport=false;
                  })
                  .catch((err)=>{
                    console.log(err)
                  })
          }
        }
    },

  },

  

  

  beforeRouteEnter(to, from, next) {
   
    if(from.name=='temp_detail'||from.name=='temp_edit'){
      to.meta.isBack=true;
     
    }else{
      to.meta.isBack=false;
    }

    next();
  },
 
  created(){
    // this.authorityFn();
    // this.tempDetailColumn=JSON.parse(JSON.stringify(this.tempDetailColumnInit))

    // this.initWebSocket();
    // this.initWarningSocket();
  },

  activated() {
    if(!this.$route.meta.isBack){
        this.initAllDataFn();
    }
    this.initWebSocket();
    this.initWarningSocket();

    // 恢复成默认的false，避免isBack一直是true，导致下次无法获取数据
    this.$route.meta.isBack=false
  

  },

  deactivated() {
      this.websock.close()
      this.warningSocket.close()
  },

  // beforeDestroy(){
      
  // }

}
</script>


<style lang='scss' scoped>
  @import '../../../assets/scss/web/common/table.scss';
  @import '../../../assets/scss/web/common/form_data.scss';
  @import "../../../assets/scss/web/temp_manage.scss";

</style>
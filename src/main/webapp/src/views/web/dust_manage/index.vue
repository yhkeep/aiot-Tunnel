<template>
  <div class='dust_manage_wrap'>
      <div class="hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">按条件筛选</h2>
                <div class="form_wrap">
                    <div class="form_item">
                            <Row :gutter="20">
                                <Col span="12">
                                    <h3>名称</h3>
                                    <Input v-model="queryData.name" placeholder="名称" />
                                </Col>
                                <Col span="12">
                                    <h3>mac地址</h3>
                                    <Input v-model="queryData.mac" placeholder="mac地址" />
                                </Col>
                            </Row>
                    </div>

                    
                </div>
                <div class="submit_btn_wrap">
                        <Button type="primary" @click="searchTempFn">搜索</Button>
                        <Button type="error"  @click="clearTempFn">清空</Button>
                </div>
            </div>
        </div>

        <div class="row_wrap hover_animat custom_bg_color_white">
          <div class="row_box">
              <h2 class="row_title">粉尘监控列表</h2>
              <div class="data_table_top clearfix">
                  <div class="data_table_top_left">
                    <Button to="/web/dust_create" type="primary" icon="md-add">新增仪器</Button>
                    <Button v-if="$store.state.role==='admin'" type="error" icon="md-trash" @click="deleteFn">删除仪器</Button>
                  </div>
                  <div class="data_table_top_right">
                        <Badge class="notice_icon" :count="this.warning.length" >
                            <Icon type="ios-notifications-outline" size="26" @click="noticeFn"></Icon>
                        </Badge>
                  </div>
              </div>

              <div class="table_scroll_wrap">
                  <Table style="min-width:1500px" stripe :columns="dustDetailColumn" :data="dustDetailDataNow" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                      <template slot-scope="{ row }" slot="no">
                          <span>{{row.no? row.no : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="name">
                          <span>{{row.name? row.name : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="mac">
                          <span>{{row.mac? row.mac : '--'}}</span>
                      </template>
                     
                      <template slot-scope="{ row }" slot="position">
                          <span>{{row.position? row.position : '--'}}</span>
                      </template>
                     
                      <template slot-scope="{ row }" slot="temp">
                          <span>{{row.temp? row.temp+'℃' : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="humi">
                          <span>{{row.humi? row.humi+'%' : '--'}}</span>
                      </template>
                    
                      <template slot-scope="{ row }" slot="pm1">
                          <span>{{row.pm1? row.pm1 : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="pm25">
                          <span>{{row.pm25? row.pm25 : '--'}}</span>
                      </template>
                      <template slot-scope="{ row }" slot="pm10">
                          <span>{{row.pm10? row.pm10 : '--'}}</span>
                      </template>
                    
                      <template slot-scope="{ row }" slot="operation">
                          <Button   @click="toDustDetailFn(row.no)">查看</Button>
                          <Button   type="info" @click="toDustEditFn(row.no)">编辑</Button>
                      </template>
                  </Table>
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


    <Drawer  :closable="false" v-model="showNotice" class="warning_wrap" width="15">
    
        <div slot="header" class="warning_title">
            <h2 >粉尘异常一览</h2>
        </div>

        <ul v-if="warning" v-for="(item, index) in warning">
          <li class="warning_li">
              <!-- <p>{{index+1}} - {{ item.depart }} - {{ item.name }}</p>
              <p><Icon type="md-thermometer" />：<span :class="item.warningType.find((item)=>item==='temp') ? 'warning_imp' : ''">{{item.temp}}℃</span>，<Icon type="md-snow" />：<span :class="item.warningType.find((item)=>item==='humi') ? 'warning_imp' : ''">{{item.humi}}%</span></p>
              <p>温度范围：{{item.tempRange[0]}} ~ {{item.tempRange[1]}}，湿度范围：{{item.humRange[0]}} ~ {{item.humRange[1]}}</p>
              <p style="color:#979797"></p> -->
          </li>
        </ul>
        <p v-if="warning&&warning.length===0" class="warning_notice">暂未发现异常数据!</p>

    </Drawer>
  </div>
</template>

<script>
import Vue from "vue"
import { Row,Col,Icon,Table ,Tag ,Select,Option,Page,Input,Button,Drawer,Modal,Spin} from 'iview';
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

import NET_PORT from "../../../api/port.js"
import { getToken } from '../../../utils/auth.js';
export default {
  name:'dust_manage',
  components:{},
  props:{},
  data(){
    return {
        isFirstEnter:false,
        queryData:{
            name:'',
            mac:'',
        },

        queryDataNow:{
            name:'',
            mac:'',
        },

        selection:[],

        tableLoading:true,
        showNotice:false,

        deleteModal:false,
        achieveDelete:false,
        errorDelete:false,

        dustDetailColumnInit:[
            {
              key: 'no', 
              title: '编号',
              // width:100,
              // slot:'no'
            },
            {
              key: 'name',
              title: '名称',
              // slot:'name',
              // width:200,
            },
            {
                key: 'mac',
               title: 'MAC',
              //  width:150,
            },
          
           
            {
                key: 'position',
                title: '位置',
                // slot:'position'
            },
            {
              key: 'pm1', 
              title: 'PM1',
              // width:100,
              // slot:'PM1'
            },
            
            {
              key: 'pm25',
               title: 'PM2.5',
              //  width:100,
              //  slot:'PM2.5'
            },
            {
              key: 'pm10',
               title: 'PM10',
              //  width:100,
              //  slot:'PM10'
            },
           
            {
              key: 'operation',
              title: '操作',
              slot:'operation',
              width:160,
            }
      ],
      dustDetailColumn:[],

      dustDetailData:[],
      dustDetailDataNow:[],
      warning:[],
      //创建socket
      websock: null,
    }
  },
  watch:{},
  computed:{},
  methods:{ 
    searchTempFn(){
            
        //点击搜索数据
            var obj={}
            var num=0;
            for( var key in this.queryData){
              if(this.queryData[key]&&this.queryData[key].trim()){
                  obj[key]=this.queryData[key].trim();
                  num++;
              }
            }
            
            this.dustDetailDataNow=[];
            this.warning=[];
            this.tableLoading=true;

            if(num===0){
              this.getDataFn();
            }else{
                if(num===1){
                  for( var key in obj){
                      this.dustDetailData.forEach((item,index)=>{
                        if(item[key]===obj[key]){
                          this.dustDetailDataNow.push(item);
                        }
                      })
                  }
                }
                else if(num===2){

                      this.dustDetailData.forEach((item,index)=>{
                        if(item.name===obj.name&&item.mac===obj.mac){
                          this.dustDetailDataNow.push(item);
                        }
                      })
                }

                this.tableLoading=false;
            }
    
      },

      clearTempFn(){
        for(var i in this.queryData){
          this.queryData[i]='';
        }
      },

       initWebSocket(){ 

            this.websock = new WebSocket(NET_PORT.tempDetailSocket+'/'+getToken());
            this.websock.onopen = this.websocketonopen;
            this.websock.onerror = this.websocketonerror;
            this.websock.onmessage = this.websocketonmessage;
            this.websock.onclose = this.websocketclose;
        },

        websocketonopen() {
          // console.log('连接成功')
        },
        websocketonerror(e) {

        },
        websocketonmessage(e){
          
            if(e.data){
              var arr=JSON.parse(e.data)
              this.filterTempFn(arr)
            }
            
        },


        websocketclose(e){ 

        },

      filterTempFn(arr){
        // console.log(arr)
            this.warning=[]
            this.dustDetailDataNow.forEach((item,index)=>{
              arr.forEach((m,n)=>{

                if(item.mac===m.mac){
                  item.warningType=[];
                  item.temp=m.temperature? Number(Number(m.temperature).toFixed(2)) :''
                  item.humi=m.humidity ? Number(Number(m.humidity).toFixed(2)) : ''
                  item.topTemp=m.toptemperature ? Number(Number(m.toptemperature).toFixed(2)) :''
                  item.topHum=m.tophumidity ? Number(Number(m.tophumidity).toFixed(2)) :''
                  item.bottomTemp=m.mintemperature ? Number(Number(m.mintemperature).toFixed(2)) : ''
                  item.bottomHum=m.minhumidity ? Number(Number(m.minhumidity).toFixed(2)) : ''

                  //有温度的情况下判断温度是否超标
                  if(item.temp){
                    if(item.tempRange[0]){
                        if(item.temp < item.tempRange[0]){
                            item.warningType.push('temp');
                        }
                    }

                    if(item.tempRange[1]){
                        if(item.temp > item.tempRange[1]){
                              item.warningType.push('temp');
                          }
                    }
                  }

                  //有湿度的情况下判断湿度是否超标
                  if(item.humi){
                      if(item.humRange[0]){
                        if(item.humi < item.humRange[0]){
                            item.warningType.push('humi');
                        }
                    }

                    if(item.humRange[1]){
                        if(item.humi > item.humRange[1]){
                          item.warningType.push('humi');
                        }
                    }
                  }

                  if(item.warningType.length>0){
                    this.warning.push(item);
                  }


                }
              })
            })
      },

        noticeFn() {
            this.showNotice=true;
        },

        toDustDetailFn(mac){
            var obj={};
            this.dustDetailDataNow.forEach((item,index)=>{
                if(item.mac===mac){
                  obj={
                      
                      mac:mac,
                      name:item.name,
                      temp:item.temp,
                      humi:item.humi,
                  }
                }
            })

            window.sessionStorage.setItem('dustObj',JSON.stringify(obj));


            this.$router.push({
                path:`/web/dust_detail/${mac}`
            })
        },

        toDustEditFn(mac){
            this.$router.push({
                path:`/web/dust_edit/${mac}`
            })
        },

        //点击多选按钮
        checkAllGroupChange(selection){
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

      var list=[];
      for(var i=0;i<this.selection.length;i++){
        list.push(this.selection[i].no)
      }

      console.log(list)
      
      this.$axios.get('')
      .then((res)=>{

          if(res.data.msg==='ok'){
            this.achieveDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                //删除成功后隔1秒隐藏模态框并更新数据
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

        getDataFn(datas){
            this.$Loading.start();
            this.dustDetailData=[]
               
            setTimeout(()=>{
              this.dustDetailData=[
                {
                  no:'111111',
                  name:'pm测试仪器1',
                  mac:'ac111111',
                  position:'车间1',
                  pm1:'8',
                  pm25:'57',
                  pm10:'83',
                },
                {
                  no:'222222',
                  name:'pm测试仪器2',
                  mac:'ac222222',
                  position:'车间2',
                  pm1:'15',
                  pm25:'89',
                  pm10:'183',
                },
              ]
              this.dustDetailDataNow=this.dustDetailData;
              this.$Loading.finish();
              this.tableLoading=false;
            },1000)

        },

        initAllDataFn(){
            this.queryData={
              name:'',
              mac:'',
            }

            this.queryDataNow={
              name:'',
              mac:'',
            }

            this.tableLoading=true
            this.showNotice=false
            this.dustDetailData=[]
            this.dustDetailDataNow=[]
            this.warning=[]

            //判断角色类型，是否添加多选属性
            this.authorityFn();
            this.dustDetailColumn=JSON.parse(JSON.stringify(this.dustDetailColumnInit))

        },

      authorityFn(){
        //判断角色类型，为表格添加选择项
        if(this.$store.state.role==="admin"){
            var obj=this.dustDetailColumnInit.find((item,index)=>{
              return item.title==='选择'
            })
            // console.log(obj)
            //如果不存在多选这个属性就添加进去
            if(!obj){
              this.dustDetailColumnInit.unshift(
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

  },
  beforeRouteEnter(to, from, next) {
    if(from.name=='dust_detail'){
        to.meta.isBack=true;
    }

    next();
  },

  beforeRouteLeave(to, from, next) {
   if(to.name==='dust_detail'){
        window.sessionStorage.setItem('webScroll',document.getElementById('content_wrap').scrollTop);
        document.getElementById('content_wrap').scrollTop=0;
   }else{
      if(window.sessionStorage.getItem('webScroll')){
        window.sessionStorage.removeItem('webScroll');
        document.getElementById('content_wrap').scrollTop=0;
      }
   }
    next();
  },
 
  created(){
    this.isFirstEnter=true;
  },



  activated() {
    if(window.sessionStorage.getItem('webScroll')){
      document.getElementById('content_wrap').scrollTop=window.sessionStorage.getItem('webScroll');
    }
   
    if(!this.$route.meta.isBack || this.isFirstEnter){
        //先清空数据，避免让用户看到之前缓存的数据
        this.initAllDataFn();
        //获取数据
        this.getDataFn();
        //页面刚进入时开启长连接
        this.initWebSocket()
    }
    // 恢复成默认的false，避免isBack一直是true，导致下次无法获取数据
    this.$route.meta.isBack=false
    // 恢复成默认的false，避免isBack一直是true，导致每次都获取新数据
    this.isFirstEnter=false;

  },

  deactivated(){
      this.$Loading.finish();
      this.websock.close()
  },
}
</script>
<style lang='scss' scoped>
  @import '../../../assets/scss/web/common/table.scss';
  @import '../../../assets/scss/web/common/form_data.scss';
  @import "../../../assets/scss/web/temp_manage.scss";

</style>
<template>
  <div id='temp_manage_wrap'>
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
                                    <h3>所属部门</h3>
                                    <Select v-model="queryData.depart">
                                        <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
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
          <h2 class="row_title">温湿度明细表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">

              </div>
              <div class="data_table_top_right">
                    <Badge class="notice_icon" :count="this.warning.length" >
                        <Icon type="ios-notifications-outline" size="26" @click="noticeFn"></Icon>
                    </Badge>
              </div>
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1500px" stripe :columns="tempDetailColumn" :data="tempDetailDataNow" size='large' :loading="tableLoading">
                    <template slot-scope="{ row }" slot="name">
                        <span>{{row.name? row.name : '室内温度计'}}</span>
                    </template>
                    <template slot-scope="{ row }" slot="no">
                        <span>{{row.no? row.no : '--'}}</span>
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
                    
                    <template slot-scope="{ row }" slot="operation">
                        <Button @click="toTempDetailFn(row.mac)">查看</Button>
                        <Button @click="toTempEditFn(row.mac)">编辑</Button>
                    </template>
                </Table>
            </div>

          
      </div>
    </div>

    <Drawer  :closable="false" v-model="showNotice" class="warning_wrap" width="15">
    
        <div slot="header" class="warning_title">
            <h2 >温湿度异常一览</h2>
        </div>

        <ul v-if="warning" v-for="(item, index) in warning">
        
        <li class="warning_li">
            <p>{{index+1}} - {{ item.depart }} - {{ item.name }}</p>
            <p><Icon type="md-thermometer" />：<span :class="item.warningType.find((item)=>item==='temp') ? 'warning_imp' : ''">{{item.temp}}℃</span>，<Icon type="md-snow" />：<span :class="item.warningType.find((item)=>item==='humi') ? 'warning_imp' : ''">{{item.humi}}%</span></p>
            <p>温度范围：{{item.tempRange[0]}} ~ {{item.tempRange[1]}}，湿度范围：{{item.humRange[0]}} ~ {{item.humRange[0]}}</p>
            <p style="color:#979797"></p>
        </li>
        </ul>
        <p v-if="warning&&warning.length===0" class="warning_right">暂未发现异常数据!</p>

    </Drawer>

  </div>
</template>

<script>
import Vue from "vue"
import { Row,Col,Icon,Table ,Tag ,Select,Option,Page,Input,Button,Drawer} from 'iview';
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

import departData from "../../assets/data/depart.js"
export default {
  name:'temp_manage',
  components:{},
  props:{},
  data(){
    return {
        isFirstEnter:false,
        queryData:{
            name:'',
            depart:'',
        },

        queryDataNow:{
            name:'',
            depart:'',
        },


        tableLoading:true,
        showNotice:false,

         tempDetailColumn:[
            {
              key: 'name',
              title: '名称',
              slot:'name',
              // width:200,
            },
            {
              key: 'no', 
              title: '编号',
              // width:100,
              slot:'no'
            },
            {
                key: 'depart',
                title: '科室',
                // width:100
            },
            {
                key: 'position',
                title: '位置',
                slot:'position'
            },
            {
                key: 'mac',
               title: 'MAC',
              //  width:150,
            },
            {
              key: 'type', 
              title: '网关类型',
              // width:100,
            },
            {
              key: 'temp', 
              title: '温度',
              // width:100,
              slot:'temp'
            },
            {
              key: 'humi',
               title: '湿度',
              //  width:100,
               slot:'humi'
            },
            {
              key: 'topTemp',
              title: '今日最高温度',
              // width:100,
              slot:'topTemp'
            },
            {
              key: 'bottomTemp',
              title: '今日最低温度',
              // width:100,
              slot:'bottomTemp'
            },
            {
              key: 'topHum', 
              title: '今日最高湿度',
              // width:100,
              slot:'topHum'
            }, 
            {
              key: 'bottomHum',
              title: '今日最低湿度',
              // width:100,
              slot:'bottomHum'
            },
            {
              key: 'operation',
              title: '操作',
              slot:'operation',
              width:160,
            }
      ],

      tempDetailData:[],
      tempDetailDataNow:[],
      warning:[],
      departData:departData,

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
            
            this.tempDetailDataNow=[];
            this.warning=[];
            this.tableLoading=true;

            if(num===0){
              this.getDataFn();
            }else{
                if(num===1){
                  for( var key in obj){
                      this.tempDetailData.forEach((item,index)=>{
                        if(item[key]===obj[key]){
                          this.tempDetailDataNow.push(item);
                        }
                      })
                  }
                }else if(num===2){

                      this.tempDetailData.forEach((item,index)=>{
                        if(item.name===obj.name&&item.depart===obj.depart){
                          this.tempDetailDataNow.push(item);
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

            // this.websock = new WebSocket("ws://192.168.0.54:8009/websocket");
            // this.websock = new WebSocket("ws://47.104.99.230:8080/websocket");
            this.websock = new WebSocket("ws://120.79.1.166:8009/hum/websocket");
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
          this.warning=[]
            this.tempDetailDataNow.forEach((item,index)=>{
              arr.forEach((m,n)=>{

                if(item.mac===m.mac){
                  item.temp=m.temperature? Number(m.temperature).toFixed(2) :''
                  item.humi=m.humidity ? Number(m.humidity).toFixed(2) : ''
                  item.topTemp=m.toptemperature ? Number(m.toptemperature).toFixed(2) :''
                  item.topHum=m.tophumidity ? Number(m.tophumidity).toFixed(2) :''
                  item.bottomTemp=m.mintemperature ? Number(m.mintemperature).toFixed(2) : ''
                  item.bottomHum=m.minhumidity ? Number(m.minhumidity).toFixed(2) : ''

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

                    if(item.humRange[0]){
                        if(item.hum < item.humRange[0]){
                            item.warningType.push('humi');
                        }
                    }

                    if(item.humRange[1]){
                        if(item.hum > item.humRange[1]){
                          item.warningType.push('humi');
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

        toTempDetailFn(mac){
            var obj={};
            this.tempDetailDataNow.forEach((item,index)=>{
                if(item.mac===mac){
                obj={
                    
                    mac:mac,
                    name:item.name,
                    temp:item.temp,
                    humi:item.humi,
                }
                }
            })

            window.sessionStorage.setItem('tempObj',JSON.stringify(obj));


            this.$router.push({
                path:`temp_detail/${mac}`
            })
        },

        toTempEditFn(mac){
            var obj={};
            this.tempDetailDataNow.forEach((item,index)=>{
                if(item.mac===mac){
                obj={
                    
                    mac:mac,
                    name:item.name,
                    temp:item.temp,
                    humi:item.hum,
                }
                }
            })

            window.sessionStorage.setItem('tempObj',JSON.stringify(obj));
            this.$router.push({
                path:`temp_edit/${mac}`
            })
        },

        getDataFn(datas){
            this.$Loading.start();
            this.tempDetailData=[]
            this.$axios.get(process.env.API_HOST+'luzhou/hum')
            .then((res)=>{
            
                // console.log(res.data)
                var arr=res.data;
                arr.forEach((item,index)=>{
                    var obj={
                    name: item.freezername,
                    no: item.freezernumber,
                    position: item.location,
                    depart: item.department,
                    type: item.type,
                    mac: item.mac,
                    temp:'',
                    humi:'',
                    topTemp:'',
                    bottomTemp:'',
                    topHum:'',
                    bottomHum:'',
                    warningType:[],
                    tempRange:[
                        item.temperaturefitted?item.temperaturefitted.split('~')[0]:'',
                        item.temperaturefitted?item.temperaturefitted.split('~')[1] :'',
                    ],

                    humRange:[
                        item.humidityfitted?item.humidityfitted.split('~')[0]:'',
                        item.humidityfitted?item.humidityfitted.split('~')[1] :'',
                    ]

                    }

                    this.tempDetailData.push(obj);
                    
                })


                this.tempDetailDataNow=this.tempDetailData;
                this.$Loading.finish();
                this.tableLoading=false;
            })
            .catch((err)=>{
            console.log(err)
            })

        },

        initAllDataFn(){
            this.queryData={
            name:'',
            depart:'',
            }

            this.queryDataNow={
            name:'',
            depart:'',
            }

            this.tableLoading=true
            this.showNotice=false
            this.tempDetailData=[]
            this.tempDetailDataNow=[]
            this.warning=[]

        }

  },
  beforeRouteEnter(to, from, next) {
    console.log("我是温湿度管理 beforeRouteEnter 方法");
    if(from.name=='temp_detail'){
        to.meta.isBack=true;
    }

    next();
  },

  
  
 
  created(){
    console.log('这是温湿度管理 created 方法')
    this.isFirstEnter=true;

  },
  mounted(){
    console.log('这是温湿度管理 mounted 方法')
    
  },

  activated() {
    console.log("这是温湿度管理 activated 方法");
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
    console.log("这是温湿度管理 deactivated 方法");
      this.websocketclose();
      this.$Loading.finish();
  },
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped src="../../assets/css/common/table.css"></style>
<style scoped>
    @import "../../assets/css/temp_manage.css"
</style>
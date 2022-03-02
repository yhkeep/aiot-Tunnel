<template>
  <div class='gateway_manage_wrap'>
    <div class="hover_animat custom_bg_color_white">
        <div class="row_box">
          <h2 class="row_title">按条件筛选</h2>
          <div class="form_wrap">
              <div class="form_item">
                    <Row :gutter="20">
                        <Col span="24">
                            <h3>网关mac地址</h3>
                            <Input v-model="queryData.mac" placeholder="网关mac地址" />
                        </Col>
                     
                    </Row>
              </div>
          </div>
          <div class="submit_btn_wrap">
                <Button type="primary" @click="searchGatewayFn">搜索</Button>
                <Button type="error" @click="clearGatewayFn">清空</Button>
          </div>
        </div>
    </div>


     <div class="row_wrap hover_animat custom_bg_color_white">
      <div class="row_box">
          <h2 class="row_title">网关明细表</h2>
            <div class="data_table_top clearfix">
                <div class="data_table_top_left">
                        <!-- <Button type="primary" icon="md-add" @click="toCreateFn">新增网关</Button> -->
                        <!-- <Button type="error" icon="md-trash" @click="deleteFn">删除网关</Button> -->
                        <Button type="warning" icon="md-power" @click="restartGatewayFn">重启指定网关</Button>
                        <Button type="primary" icon="md-refresh-circle" @click="restartAllGatewayFn">重启所有网关</Button>
                        <!-- <Button type="error" icon="md-arrow-round-up" @click="updateAllGatewayFn">升级所有网关</Button> -->
                        <!-- <Button type="warning" icon="md-cog" @click="settingGatewayFn">配置网关</Button> -->
                      
                </div>
                <div class="data_table_top_right">
                    <div class="pagination_wrap">
                    <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                    </div>
                </div>
              
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1000px" stripe :columns="gatewayDetailColumn" :data="gatewayDetailDataNow" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                    
                    <template slot-scope="{row}" slot="mac">
                        <!-- <a @click="toDetailFn(row.mac)">{{row.mac}}</a> -->
                        {{row.mac}}
                    </template>
                 
                    <template slot-scope="{row}" slot="status">
                        <Tag v-if="row.status==='在线'" color="success">在线</Tag>
                        <Tag v-else-if="row.status==='离线'" color="error">离线</Tag>
                        <span v-else>{{row.status}}</span>
                    </template>
                 
                </Table>
            </div>
            
      </div>
    </div>

      

  </div>
</template>

<script>

import Vue from "vue"
import { Row,Col,Icon,Table ,Tag ,DatePicker,Select,Option,Page,Input,Button,Modal,Spin} from 'iview';
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
// import floorData from "../../../assets/data/floor.js"
import gatewayDetailData from '../../../utils/gatewayDetailData.js';
import gatewayStatusData from '../../../utils/gatewayStatusData.js';
import gatewayVersionData from '../../../utils/gatewayVersionData.js';

import NET_PORT from "../../../api/port.js"

export default {
  name:'gateway_manage',
  components:{},
  props:{},
  data(){
    return {
         queryData:{
            mac:'',
        },
        tableLoading:true,
        // floorData:floorData,
        gatewayDetailColumn:[
            {
              title:'选择',
              slot:'selection',
              type: 'selection',
              width:100,
            },

            {
                title: '网关mac地址',
                key: 'mac',
                slot:'mac'
            },
            {
                title: '部门',
                key: 'depart',
            },

            {
                title: '坐落位置',
                key: 'location',
            },
            // {
            //     title: '楼层',
            //     key: 'floor',
            // },
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:100,
            },
            {
                title: '更新时间',
                key: 'updatetime',
            },
         
           
        ],
        gatewayDetailData:[],
        gatewayDetailDataNow:[],

        selection:[],
        deleteCertainModal:false,
        deleteModal:false,
        achieveDelete:false,
        errorDelete:false,

        totalCount:0,
        currentPage:1,
        pageSize:10,
    
    }
  },
  watch:{},
  computed:{},
  methods:{
       searchGatewayFn(){
          this.gatewayDetailDataNow=[];
          if(this.queryData.mac.trim()){
              this.gatewayDetailDataNow=[];
              this.gatewayDetailData.forEach((item,index)=>{
                  if(item.mac===this.queryData.mac.trim()){
                      this.gatewayDetailDataNow.push(item)
                  }
              })
          }else{
              this.getDataFn();
          }

      },

      clearGatewayFn(){
          for(var i in this.queryData){
            this.queryData[i]="";
          }
      },


      checkAllGroupChange(selection){
          this.selection=selection;
      },


      restartGatewayFn(mac){
            //多选重启所有接口
            if(this.selection.length){
                    this.$Modal.confirm({
                          title:'重启提示',
                          okText:'确定',
                          cancelText:'取消',
                          content:'<span style="color:#f94040">你确定要重启这'+this.selection.length+'个网关吗？</span>',
                          onOk:()=>{
                                var configData='';
                                for(var i=0;i<this.selection.length;i++){
                                    configData+='gatewaymac='+this.selection[i].mac+'&'
                                }

                                this.$axios.post(NET_PORT.gatewayRestart+configData+'address='+this.$store.state.address)
                                .then((res)=>{

                                    if(res.data.msg==='ok'){
                                        this.$Message.success({
                                            content:"设备重启成功！",
                                            duration:1,
                                        });
                                    }else{
                                        this.$Message.error({
                                            content:"设备重启失败！",
                                            duration:1,
                                        });
                                    }

                                })
                                .catch((err)=>{
                                    this.$Loading.error();
                                })
                          },
                         
                    })
            }else{
                return this.$Message.error({
                    content:"请选择至少一项!",
                    duration:2,
                })
            }



      },

      restartAllGatewayFn(){
        //   console.log(this.$store.state.address)
           this.$Modal.confirm({
                    title:'重启提示',
                    okText:'确定',
                    cancelText:'取消',
                    content:'<span style="color:#f94040">你确定要重启所有的网关吗？</span>',
                    onOk:()=>{

                            this.$axios.post(NET_PORT.gatewayRestartAll+'address='+this.$store.state.address)
                            .then((res)=>{
                               
                            //    console.log(res)
                               if(res.data.msg==='ok'){
                                    this.$Message.success({
                                        content:"设备重启成功！",
                                        duration:1,
                                    });
                               }else{
                                    this.$Message.error({
                                        content:"设备重启失败！",
                                        duration:1,
                                    });
                               }
                            })
                            .catch((err)=>{
                                this.$Message.error({
                                    content:"设备重启失败！",
                                    duration:1,
                                });
                            })

                    },
                    
            })
      },

    

      successFn(){

         this.achieveDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                //删除成功后隔1秒隐藏模态框并更新数据
                this.currentPage=1;
                
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
      },

      errorFn(){
          this.errorDelete=true;
          setTimeout(()=>{
              this.deleteModal=false;
              setTimeout(()=>{
                  this.achieveDelete=false;
                  this.errorDelete=false;
              },500)
              
          },1000)
      },

    

   

    getDataFn(){
       
        this.$axios.get(NET_PORT.gatewayQuery+this.$store.state.address)
        .then((res)=>{
            var arr=res.data;
            arr.forEach((item,index)=>{
                if(JSON.stringify(item)!=='{}'){
                   if(item.gatewaymac!==''){
                        var obj={
                            mac:item.gatewaymac,
                            depart:item.department ? item.department :'',
                            location:item.location ? item.location :'',
                            status:item.online==='online'?'在线' : '离线',
                            updatetime:item.updatetime ? item.updatetime :'',
                            _disabled:item.gatewaymac==='AC233FC011F2'? true : false
                        }
                        this.gatewayDetailData.push(obj)
                   }
                }
            })
            
            this.gatewayDetailDataNow=this.gatewayDetailData;
            // this.totalCount=this.gatewayDetailDataNow.length;
            this.tableLoading=false;

        })
        .catch((err)=>{
            // console.log(err)
        })
       
    },

    changePageFn(page){
          this.tableLoading=true;
          this.currentPage=page;
        
    },

    updateGatewayFn(mac){
        //这里拿到要升级的网关mac
        console.log(mac)
    },

    updateAllGatewayFn(){
        this.$Modal.confirm({
            title:'升级提示',
            okText:'确定',
            cancelText:'取消',
            content:'<span style="color:#f94040">你确定要升级所有的网关吗？</span>',
            onOk:()=>{

            },
        })
    },

    settingGatewayFn(){
        this.$router.push({
            path:'/web/gateway_setting'
        })
    },
    
    toCreateFn(){
        this.$router.push({
            path:'/web/gateway_create'
        })
    },

    toDetailFn(mac){
        this.gatewayDetailData.forEach((item,index)=>{
            if(item.mac===mac){
                // console.log(item)
                var obj={
                    mac:item.mac,
                    depart:item.depart,
                    location:item.location,
                    status:item.status,
                    updatetime:item.updatetime,
                    floor:'11',
                    posi: [
                        -4.780317,
                        139.336613
                    ]
                }
                window.sessionStorage.setItem('detailData',JSON.stringify(obj))
            }
        })
        this.$router.push({
            path:'/web/gateway_detail'
        })
    },

    deleteFn(){
        if(this.selection.length){
            this.deleteCertainModal=true;
        }else{
            return this.$Message.error({
                content:"请选择至少一项!",
                duration:2,
            })
        }
    },

    deleteCertainFn(){
        this.deleteCertainModal=false;
        this.deleteModal=true;
        console.log(this.selection)
        //删除成功回调
        // this.successFn()
        //删除失败回调
        // this.errorFn()
    },

  },
  created(){},
  mounted(){
      this.getDataFn();
    
  }
}
</script>



<style lang='scss' scoped>
  @import '../../../assets/scss/web/common/table.scss';
  @import '../../../assets/scss/web/common/form_data.scss';
</style>
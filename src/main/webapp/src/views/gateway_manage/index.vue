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
                        <!-- <Col span="12">
                            <h3>所在楼层</h3>
                            <Select v-model="queryData.floor">
                                <Option v-for="item in floorData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                        </Col> -->
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
                <!-- <div class="data_table_top_right">
                    <div class="pagination_wrap">
                    <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                    </div>
                </div> -->
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1000px" stripe :columns="gatewayDetailColumn" :data="gatewayDetailDataNow" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                    
                    <template slot-scope="{row}" slot="mac">
                        <!-- <a @click="toDetailFn(row.mac)">{{row.mac}}</a> -->
                        {{row.mac}}
                    </template>
                    <!-- <template slot-scope="{row}" slot="posi">
                        {{row.posi.toString()}}
                    </template> -->
                    <template slot-scope="{row}" slot="status">
                        <Tag v-if="row.status==='在线'" color="success">在线</Tag>
                        <Tag v-else-if="row.status==='离线'" color="error">离线</Tag>
                        <span v-else>{{row.status}}</span>
                    </template>
                    <!-- <template slot-scope="{row}" slot="isUpdate">
                        <Button size="small" shape="circle" type="success" v-if="row.isUpdate==='yes'" icon="md-arrow-round-up" @click="updateGatewayFn(row.mac)"></Button>
                        <Button size="small" shape="circle" disabled v-else-if="row.isUpdate==='no'" icon="ios-checkmark-circle-outline"></Button>
                        <span v-else>{{row.isUpdate}}</span>
                    </template> -->
                    <!-- <template slot-scope="{row}" slot="operation">
                        <Button  type="success"   @click="restartGatewayFn(row.mac)">重启</Button>
                    </template> -->
                    
                </Table>
            </div>
            
      </div>
    </div>

        <!-- <Modal title='' v-model="deleteCertainModal" width="360">
         
            <div style="text-align:center">
                <h2>你确定要删除这{{selection.length}}个网关吗?</h2>
            </div>
            <div slot="footer">
                <Button type="error" size="large" long  @click="deleteCertainFn">确定删除</Button>
            </div>
        </Modal>

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
        </Modal> -->

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
// import floorData from "../../assets/data/floor.js"
import gatewayDetailData from '../../utils/gatewayDetailData.js';
import gatewayStatusData from '../../utils/gatewayStatusData.js';
import gatewayVersionData from '../../utils/gatewayVersionData.js';

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

            // {
            //     title: '重启',
            //     key: 'operation',
            //     slot:'operation'
            // },
            // {
            //     title: '所在楼层',
            //     key: 'floor',
            // },
            {
                title: '坐落位置',
                key: 'location',
            },
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
            // {
            //     title: '版本',
            //     key: 'version',
            // },
            // {
            //     title: '可否升级',
            //     key: 'isUpdate',
            //     slot:'isUpdate'
            // },
           
        ],
        gatewayDetailData:[],
        gatewayDetailDataNow:[],

        selection:[],
        deleteCertainModal:false,
        deleteModal:false,
        achieveDelete:false,
        errorDelete:false,
        
        // totalCount:0,
        // currentPage:1,
        // pageSize:10,
    }
  },
  watch:{},
  computed:{},
  methods:{
       searchGatewayFn(){
          this.gatewayDetailDataNow=[];
          if(this.queryData.mac.trim()){
              this.gatewayDetailDataNow=[];
            //   console.log(this.queryData.mac.trim())

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

    //   changePageFn(page){
    //       this.tableLoading=true;
    //       this.currentPage=page;
        
    //   },

      checkAllGroupChange(selection){
          this.selection=selection;
      },

    //   updateGatewayFn(mac){
    //       //这里拿到要升级的网关mac
    //       console.log(mac)
    //   },

    //   updateAllGatewayFn(){
    //        this.$Modal.confirm({
    //                 title:'升级提示',
    //                 okText:'确定',
    //                 cancelText:'取消',
    //                 content:'<span style="color:#f94040">你确定要升级所有的网关吗？</span>',
    //                 onOk:()=>{

    //                 },
                    
    //         })
    //   },

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
                                // var list=[];
                                for(var i=0;i<this.selection.length;i++){
                                    // list.push(this.selection[i].mac)
                                    configData+='gatewaymac='+this.selection[i].mac+'&'
                                }

                                // //截掉最后一个&符号
                                // var configDataSlice=configData.slice(0,configData.length-1)
                                
                                //要删除的资产的id数组
                                // console.log(list)

                                // console.log(this.selection)

                                // console.log(configDataSlice)

                              

                                this.$axios.post(process.env.API_HOST+'huaxi/gateway?'+configData+'address='+this.$store.state.address)
                                .then((res)=>{
                                    // console.log('已发送指定')
                                    console.log(res)

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
                                    console.log(err)
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


            //每个单独重启按钮

                    // this.$Modal.confirm({
                    //       title:'重启提示',
                    //       okText:'确定',
                    //       cancelText:'取消',
                    //       content:'<span style="color:#f94040">你确定要重启mac地址为'+mac+'的网关吗？</span>',
                    //       onOk:()=>{

                    //             console.log(mac)
                    //             // this.$axios.get('api/huaxi/gateway?gatewaymac='+mac)
                    //             // .then((res)=>{
                    //             // console.log('已发送指定重启请求'+mac)
                    //             // console.log(res)

                    //             // })
                    //             // .catch((err)=>{
                    //             //     console.log(err)
                    //             //     this.$Loading.error();
                    //             // })
                    //       },
                         
                    // })

      },

      restartAllGatewayFn(){
          console.log(this.$store.state.address)
           this.$Modal.confirm({
                    title:'重启提示',
                    okText:'确定',
                    cancelText:'取消',
                    content:'<span style="color:#f94040">你确定要重启所有的网关吗？</span>',
                    onOk:()=>{

                            this.$axios.post(process.env.API_HOST+'huaxi/allGateway?address='+this.$store.state.address)
                            .then((res)=>{
                               
                               console.log(res)
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

    //   settingGatewayFn(){
    //         this.$router.push({
    //             path:'/gateway_setting'
    //         })
    //   },
      
    //   toCreateFn(){
    //        this.$router.push({
    //             path:'/gateway_create'
    //         })
    //   },

    //   toDetailFn(mac){
    //         this.gatewayDetailData.forEach((item,index)=>{
    //           if(item.mac===mac){
    //             window.sessionStorage.setItem('detailData',JSON.stringify(item))
    //           }
    //         })
    //         this.$router.push({
    //             path:'/gateway_detail'
    //         })
    //   },

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

    //   deleteFn(){
    //       if(this.selection.length){
    //         this.deleteCertainModal=true;
    //       }else{
    //         return this.$Message.error({
    //             content:"请选择至少一项!",
    //             duration:2,
    //         })
    //       }
    //   },

    //   deleteCertainFn(){
    //       this.deleteCertainModal=false;
    //       this.deleteModal=true;
    //       console.log(this.selection)
    //       //删除成功回调
    //       // this.successFn()
    //       //删除失败回调
    //       // this.errorFn()
    //   },

   

    getDataFn(){
        // this.gatewayDetailData=[];
        // this.gatewayDetailDataNow=[]
        // var arr=[
        //     {
        //         "mac": "AC233FC037A3",
        //     },
        //     {
        //         "mac": "AC233FC038D3",
        //     },
            
        //     {
        //         "mac": "AC233FC037A0",
        //     },
        //     {
        //         "mac": "AC233FC037A1",
        //     },
            
        //     {
        //         "mac": "AC233FC038A9",
        //     },
        //     {
        //         "mac": "AC233FC038BD",
        //     },
        // ]
        this.$axios.get(process.env.API_HOST+'huaxi/getGateway?address='+this.$store.state.address)
        .then((res)=>{
            // console.log(res.data)
            var arr=res.data;
            arr.forEach((item,index)=>{
                // console.log(JSON.stringify(item))
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
            this.tableLoading=false;

        })
        .catch((err)=>{
            console.log(err)
        })
        // arr.forEach((item,index)=>{
        //     var obj={
        //             // floor:item.floor,
        //             mac: item.mac,
        //             // posi:item.posi,
        //             status:'--',
        //             version:'--',
        //             isUpdate:'--',
        //     }
        //     this.gatewayDetailData.push(obj)
        // })

        // this.gatewayDetailDataNow=this.gatewayDetailData;
        // this.tableLoading=false;
    }

  },
  created(){},
  mounted(){
      this.getDataFn();

       // setTimeout(()=>{
        //         gatewayStatusData.forEach((item,index)=>{
        //             this.gatewayDetailData.forEach((m,n)=>{
        //                 if(m.mac===item.mac){
        //                     m.status=item.status
        //                 }
        //             })
        //         })

        //         gatewayVersionData.forEach((item,index)=>{
        //             this.gatewayDetailData.forEach((m,n)=>{
        //                 if(m.mac===item.mac){
        //                     m.version=item.version,
        //                     m.isUpdate=item.isUpdate
        //                 }
        //             })
        //         })


        // },1000)

     
    //   this.gatewayDetailData=gatewayDetailData;

    //   this.totalCount=gatewayDetailData.length
    
  }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped src="../../assets/css/common/table.css"></style>
<style scoped>
</style>
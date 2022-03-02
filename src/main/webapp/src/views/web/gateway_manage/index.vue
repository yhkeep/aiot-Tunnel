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
                            <Input v-model="queryData.gatewaymac" placeholder="网关mac地址" />
                        </Col>
                      
                    </Row>
              </div>
          </div>
          <div class="submit_btn_wrap">
                <Button type="primary" shape="circle" @click="searchGatewayFn">搜索</Button>
                <Button type="error" shape="circle" @click="clearGatewayFn">清空</Button>
          </div>
        </div>
    </div>


    <div class="row_wrap hover_animat custom_bg_color_white">
        <div class="row_box">
            <h2 class="row_title">网关明细表</h2>
            <div class="data_table_top clearfix">
                <div class="data_table_top_left">
                        <Button type="primary" shape="circle" icon="md-add" @click="toCreateFn">新增网关</Button>
                        <Button type="error" shape="circle" icon="md-trash" @click="deleteFn">批量删除网关</Button>
                        <Button type="warning" shape="circle" icon="md-power" @click="restartGatewayFn">批量重启网关</Button>
                        <Button type="primary" shape="circle" icon="md-refresh-circle" @click="restartAllGatewayFn">重启所有网关</Button>
                        <!-- <Button type="error" shape="circle" icon="md-arrow-round-up" @click="updateAllGatewayFn">升级所有网关</Button> -->
                        <Button type="warning" shape="circle" icon="md-cog" @click="settingGatewayFn">配置网关</Button>
                        <!-- <Button type="success" shape="circle" icon="md-refresh-circle" @click="refreshFn">刷新</Button> -->
                </div>
                
                <div class="data_table_top_right">
                    <!-- <div class="pagination_wrap">
                        <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                    </div> -->
                </div>
                <div class="data_table_top_center">
                    
                    <div style="display:inline-block;margin-right:10px">
                        <Select v-model="floor" @on-change="floorChangeFn">
                            <Option v-for="item in floorData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                        </Select>
                    </div>
                    <Button type="success" shape="circle" icon="md-refresh-circle" @click="refreshFn">刷新</Button>
                    
                </div>
                
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1000px" stripe :columns="gatewayDetailColumn" :data="gatewayDetailDataNow" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                    
                    <template slot-scope="{row}" slot="gatewaymac">
                        <a @click="toDetailFn(row.gatewaymac)">{{row.gatewaymac}}</a>
                    </template>
                    <template slot-scope="{row}" slot="operation">
                        <Button type="default" shape="circle" icon="md-power" @click="restartThisGatewayFn(row.gatewaymac)">重启</Button>
                        <Button type="default" shape="circle" icon="md-trash" @click="deleteThisGatewayFn(row.gatewaymac)">删除</Button>
                    </template>
                    
                    <template slot-scope="{row}" slot="status">
                        <Tag v-if="row.status==='在线'" color="success">在线</Tag>
                        <Tag v-else-if="row.status==='离线'" color="error">离线</Tag>
                        <span v-else>{{row.status}}</span>
                    </template>
                    
                </Table>
            </div>
            <!-- <div class="data_table_pagination_wrap">
                <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
            </div> -->
                
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
import floorData from "../../../assets/data/floor.js"

import NET_PORT from "../../../api/port.js"

export default {
  name:'gateway_manage',
  components:{},
  props:{},
  data(){
    return {
        queryData:{
            gatewaymac:'',
        },

        floor:'13',
        tableLoading:true,
        floorData:floorData,
        gatewayDetailColumn:[
            {
              title:'选择',
              slot:'selection',
              type: 'selection',
              width:100,
            },

            {
                title: '网关mac地址',
                key: 'gatewaymac',
                slot:'gatewaymac'
            },
          
            {
                title: '坐落位置',
                key: 'location',
            },
           
            {
                title: '楼层',
                key: 'floor',
            },
            {
                title: '房间名',
                key: 'cadMapRoomName',
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
            {
                title: '操作',
                key: 'operation',
                slot:'operation',
                width:200
            },
         
           
        ],
        gatewayDetailData:[],
        gatewayDetailDataNow:[],

        selection:[],
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
          if(this.queryData.gatewaymac.trim()){
              this.gatewayDetailDataNow=[];
              this.gatewayDetailData.forEach((item,index)=>{
                  if(item.gatewaymac===this.queryData.gatewaymac.trim()){
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


      restartGatewayFn(){
            //多选重启所有接口
            if(this.selection.length){
                let offlineArr=[]
                let onlineArr=[]
                this.selection.forEach((item,index)=>{
                    if(item.status==='离线'){
                        offlineArr.push(item);
                    }else{
                        onlineArr.push(item);
                    }
                })

                if(offlineArr.length&&onlineArr.length){
                     this.$Modal.confirm({
                          title:'重启提示',
                          okText:'确定',
                          cancelText:'取消',
                          content:'<h2 style="color:#f94040">'+offlineArr.length+'个网关为离线状态无法重启！你确定要继续重启其它'+onlineArr.length+'个在线网关吗？</h2>',
                          onOk:()=>{
                             
                                var configData='';
                                for(var i=0;i<onlineArr.length;i++){
                                    configData+='gatewaymac='+onlineArr[i].gatewaymac+'&'
                                }

                                this.$axios.post(NET_PORT.gatewayRestart+configData+'address='+this.$store.state.address)
                                .then((res)=>{

                                    if(res.data.msg==='ok'){
                                        this.$Message.success({
                                            content:"成功发送重启请求！",
                                            duration:2,
                                        });
                                    }else{
                                        this.$Message.error({
                                            content:"发送重启请求失败！",
                                            duration:2,
                                        });
                                    }

                                })
                                .catch((err)=>{
                                    this.$Message.error({
                                        content:"发送重启请求失败！",
                                        duration:2,
                                    });
                                })
                          },
                         
                    })
                }else if(offlineArr.length&&!onlineArr.length){
                    this.$Message.error({
                        content:"发送重启请求失败！所选网关均为离线状态",
                        duration:2,
                    }); 
                }else if(!offlineArr.length&&onlineArr.length){
                     this.$Modal.confirm({
                          title:'重启提示',
                          okText:'确定',
                          cancelText:'取消',
                          content:'<h2 style="color:#f94040">你确定要重启这'+onlineArr.length+'个网关吗？</h2>',
                          onOk:()=>{
                             
                                var configData='';
                                for(var i=0;i<onlineArr.length;i++){
                                    configData+='gatewaymac='+onlineArr[i].gatewaymac+'&'
                                }

                                this.$axios.post(NET_PORT.gatewayRestart+configData+'address='+this.$store.state.address)
                                .then((res)=>{

                                    if(res.data.msg==='ok'){
                                        this.$Message.success({
                                            content:"成功发送重启请求！",
                                            duration:2,
                                        });
                                    }else{
                                        this.$Message.error({
                                            content:"发送重启请求失败！",
                                            duration:2,
                                        });
                                    }

                                })
                                .catch((err)=>{
                                    this.$Message.error({
                                        content:"发送重启请求失败！",
                                        duration:2,
                                    });
                                })
                          },
                         
                    })
                }
                   
            }else{
                return this.$Message.error({
                    content:"请选择至少一项!",
                    duration:2,
                })
            }



      },

      restartAllGatewayFn(){
           this.$Modal.confirm({
                    title:'重启提示',
                    okText:'确定',
                    cancelText:'取消',
                    content:'<h2 style="color:#f94040">你确定要重启所有的网关吗？离线状态的网关将无法发送重启请求</h2>',
                    onOk:()=>{

                            this.$axios.post(NET_PORT.gatewayRestartAll+'address='+this.$store.state.address)
                            .then((res)=>{
                               
                               if(res.data.msg==='ok'){
                                    this.$Message.success({
                                        content:"成功发送重启请求！",
                                        duration:2,
                                    });
                               }else{
                                    this.$Message.error({
                                        content:"发送重启请求失败！",
                                        duration:2,
                                    });
                               }
                            })
                            .catch((err)=>{
                                this.$Message.error({
                                    content:"发送重启请求失败！",
                                    duration:2,
                                });
                            })

                    },
                    
            })
      },

      restartThisGatewayFn(gatewaymac){
            let obj=this.gatewayDetailData.find((item,index)=>{
                return item.gatewaymac===gatewaymac
            })
            
            if(obj.status==='离线'){
                this.$Message.error({
                    content:"发送重启请求失败！该网关为离线状态",
                    duration:2,
                }); 
            }else{
                this.$Modal.confirm({
                    title:'重启提示',
                    okText:'确定',
                    cancelText:'取消',
                    content:'<h2 style="color:#f94040">你确定要重启 '+gatewaymac+' 这个网关吗？</h2>',
                    onOk:()=>{
                    
                        this.$axios.post(NET_PORT.gatewayRestart+'gatewaymac='+gatewaymac+'&address='+this.$store.state.address)
                        .then((res)=>{

                            if(res.data.msg==='ok'){
                                this.$Message.success({
                                    content:"成功发送重启请求！",
                                    duration:2,
                                });
                            }else{
                                this.$Message.error({
                                    content:"发送重启请求失败！",
                                    duration:2,
                                });
                            }

                        })
                        .catch((err)=>{
                            this.$Message.error({
                                content:"发送重启请求失败！",
                                duration:2,
                            });
                        })
                        
                    },
                    
                })
            }
           
      },

      successFn(){

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

      filterFloorFn(data){
          let arr=[];

          arr=data.filter((item,index)=>{
              return item.floor===this.floor;
          })
          return arr
      },


    getDataFn(){
        this.$Loading.start()
        this.tableLoading=true;
        this.gatewayDetailData=[];
        this.gatewayDetailDataNow=[];
        this.$axios.get(NET_PORT.gatewayQuery+this.$store.state.address)
        .then((res)=>{
            var arr=res.data;
            arr.forEach((item,index)=>{
                if(JSON.stringify(item)!=='{}'){
                   if(item.gatewaymac!==''){
                      
                        var obj={
                            gatewaymac:item.gatewaymac,
                            floor:item.floor?item.floor : '',
                            ipaddress:item.ipaddress ? item.ipaddress : '',
                            mapx:item.mapx ? item.mapx : '',
                            mapy:item.mapy ? item. mapy : '',
                            department:item.department ? item.department :'',
                            location:item.location ? item.location :'',
                            status:item.online==='online'&&item.updatetime ?'在线' : '离线',
                            updatetime:item.updatetime ? item.updatetime :'',
                            cadMapRoomName:item.cadMapRoomName? item.cadMapRoomName : '',
                            roomnumber:item.roomnumber? item.roomnumber : '',
                            // _disabled:item.gatewaymac==='AC233FC011F2'? true : false
                        }
                        this.gatewayDetailData.push(obj)
                   }
                }
            })

            // this.gatewayDetailData.push({
            //     gatewaymac:'AC85EA3267BE',
            //     floor:'',
            //     ipaddress:'',
            //     mapx:'',
            //     mapy:'',
            //     department:'',
            //     location:'',
            //     status:'在线',
            //     updatetime:'2020-07-21 12:32:33',
            // })

            
            
            // this.gatewayDetailDataNow=this.gatewayDetailData
            this.gatewayDetailDataNow=this.filterFloorFn(this.gatewayDetailData)
            // console.log(this.gatewayDetailData)
            this.totalCount=this.gatewayDetailDataNow.length;
            this.tableLoading=false;
            this.$Loading.finish();
        })
        .catch((err)=>{
            this.$Loading.error();
            // console.log(err)
        })
       
    },

    changePageFn(page){
        this.tableLoading=true;
        this.currentPage=page;
        this.selection=[];
        this.getDataFn()
    },

    floorChangeFn(){
        this.selection=[];
        this.currentPage=1;
        this.getDataFn();
    },

    refreshFn(){
        this.selection=[];
        this.getDataFn();
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

    toDetailFn(gatewaymac){
        this.gatewayDetailData.forEach((item,index)=>{
            if(item.gatewaymac===gatewaymac){
                let obj={
                    gatewaymac:item.gatewaymac,
                    floor:item.floor,
                    ipaddress:item.ipaddress,
                    mapx:item.mapx,
                    mapy:item.mapy,
                    location:item.location ? item.location :'',
                    department:item.department ? item.department :'',
                    cadMapRoomName:item.cadMapRoomName,
                    roomnumber:item.roomnumber,
                }
                // let obj={ "gatewaymac": "AC:23:3F:C0:72:BE", "location": "DR-8","cadMapRoomName":"OR-8", "ipaddress": "172.20.29.109", "mapx": "-4.75845", "mapy": "139.326554", "floor": "11", "roomnumber": "11-1" }
                window.sessionStorage.setItem('detailData',JSON.stringify(obj))
            }
        })
        this.$router.push({
            path:'/web/gateway_detail'
        })
    },

    deleteFn(){
        // console.log(this.selection)
        if(this.selection.length){
            this.$Modal.confirm({
                title:'删除提示',
                okText:'确定删除',
                cancelText:'取消',
                content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+'个网关吗？</h2>',
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

    deleteCertainFn(){
        this.deleteModal=true;
        let arr=[];
        this.selection.forEach((item,index)=>{
            let obj={
                gatewaymac:item.gatewaymac,
            }

            arr.push(obj)
        })

        this.deletePortFn(arr)

    },

    deleteThisGatewayFn(gatewaymac){
        this.$Modal.confirm({
            title:'删除提示',
            okText:'确定删除',
            cancelText:'取消',
            content:'<h2 style="color:#f94040">你确定要删除 '+gatewaymac+' 这个网关吗？</h2>',
            onOk:()=>{
                    this.deleteModal=true;
                    let arr=[
                        {
                            gatewaymac:gatewaymac,
                        }
                    ]

                    this.deletePortFn(arr)
            },
            
        })
    },

    deletePortFn(arr){
        this.$axios.post(NET_PORT.gatewayDelete,arr)
        .then((res)=>{
            if(res.data.msg==='success'){
                this.successFn()
            }else{
                this.errorFn()
                this.$Message.error({
                    content:res.data.msg,
                    duration:2,
                })
            }

        })
        .catch((err)=>{
            this.errorFn()
        })
    }

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
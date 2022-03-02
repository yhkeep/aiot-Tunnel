<template>
  <div class='mobile_temp_manage_wrap manage_list_wrap custom_flex_column_wrap'>
     
      <div class="mobile_search_wrap">
           <div v-if="!selectMode" class="mobile_search_box" ref="mobileSearchBoxFixed">
                <div class="mobile_search_box_left">
                    <Search
                        v-model="value"
                        disabled 
                        placeholder="点击展开搜索面板"
                        shape="round"
                        @click="openSearchPaneFn"
                    />
                </div>
                <div class="mobile_search_box_right" >
                    <div class="mobile_search_box_right_tools temp_notice_icon" style="width:60px">
                        <Icon name="volume-o"  @click="warningDialogShow=true;"/>
                        <span v-if="this.tempWarningArr.length" class="right_dot_notice error">{{this.tempWarningArr.length > 99 ? '99+' : this.tempWarningArr.length}}</span>
                    </div>
                    <div class="mobile_search_box_right_tools" style="width:40px">
                        <!-- <Icon name="add-o" @click="toCreateFn"/> -->

                        <Popover
                          v-model="showPopover"
                            trigger="click"
                            :actions="actions"
                            @select="onSelect"
                            placement="bottom-end"
                            theme="dark"
                            >
                            <template #reference>
                                <Icon name="add-o" />
                            </template>
                        </Popover>
                    </div>
                    <div class="mobile_search_box_right_tools" style="width:40px">
                        <Icon name="bars"  @click="showMoreMenuFn"/>
                    </div>
                
                </div>
            </div>
      </div>

        <div class="assets_table_wrap" ref="assetsTableWrap">
            <div class="assets_table_wrap_content  clearfix">
                <div class="assets_table_wrap_left">
                    <ul class="assets_table_ul" >
                        <li class="assets_table_li assets_table_li_name">
                            <div class="assets_table_li_checkbox" v-if="selectMode">
                                <Checkbox  v-model="checkAll" @click="checkAllFn" checked-color="#07c160" ></Checkbox>
                            </div>
                            <div v-else class="assets_table_item"><p>序号</p></div>
                        </li>
                        <CheckboxGroup v-model="checkedArr" ref="checkboxGroup"  v-if="selectMode">
                            <li class="assets_table_li" v-for="(item,index) in tempDetailDataNow" :key="index">
                                <div class="assets_table_li_checkbox">
                                    <Checkbox :name='item.freezernumber'  checked-color="#07c160" @click="(e)=>{e.stopPropagation();}"></Checkbox>
                                </div>
                            </li>
                        </CheckboxGroup>
                        <li v-else class="assets_table_li" v-for="(item,index) in tempDetailDataNow" :key="index">
                           
                            <div class="assets_table_item"><p>{{index+1}}</p></div>
                        </li>
                       
                    </ul>
                </div>
                <div class="assets_table_wrap_right">
                    <ul class="assets_table_ul" >
                        <li class="assets_table_li assets_table_li_name clearfix">
                            <div class="assets_table_item"><p>编号</p></div>
                            <div class="assets_table_item"><p>名称</p></div>
                            <div class="assets_table_item"><p>温度（℃）</p></div>
                            <div class="assets_table_item"><p>湿度（%）</p></div>
                             <div class="assets_table_item"><p>今日最高温度（℃）</p></div>
                            <div class="assets_table_item"><p>今日最低温度（℃）</p></div>
                            <div class="assets_table_item"><p>今日最高湿度（%）</p></div>
                            <div class="assets_table_item"><p>今日最低湿度（%）</p></div>
                            <div class="assets_table_item"><p>电量（%）</p></div>
                            <div class="assets_table_item"><p>mac</p></div>
                            <div class="assets_table_item"><p>类型</p></div>
                            <div class="assets_table_item"><p>部门名称</p></div>
                            <div class="assets_table_item"><p>位置</p></div>
                            <div class="assets_table_item"><p>数据接收时间</p></div>
                            <div class="assets_table_item"><p>操作</p></div>
                            
                           
                        </li>
                        <li class="assets_table_li clearfix" v-for="(item,index) in tempDetailDataNow" :key="index" >
                           <div class="assets_table_item">
                               <p>
                                   {{item.freezernumber? item.freezernumber : '--'}}
                               </p>
                           </div>
                           <div class="assets_table_item">
                               <p>
                                    {{item.freezername? item.freezername : '--'}} 
                               </p>
                           </div>
                           <div class="assets_table_item">
                               <p>
                                    {{item.temp? item.temp+'℃' : '--'}}
                               </p>
                           </div>
                           <div class="assets_table_item">
                               <p>
                                    {{item.humi? item.humi+'%' : '--'}}
                               </p>
                           </div>
                            <div class="assets_table_item">
                                <p>
                                    {{item.topTemp? item.topTemp+'℃' : '--'}}
                                </p>
                           </div>
                           <div class="assets_table_item">
                                <p>
                                    {{item.bottomTemp? item.bottomTemp+'℃' : '--'}}
                                </p>
                           </div>
                           <div class="assets_table_item">
                                <p>
                                    {{item.topHum? item.topHum+'%' : '--'}}
                                </p>
                           </div>
                           <div class="assets_table_item">
                                <p>
                                    {{item.bottomHum? item.bottomHum+'%' : '--'}}
                                </p>
                           </div>
                            <div class="assets_table_item">
                                <p>
                                    {{item.electric? item.electric+'%' : '--'}}
                                </p>
                           </div>
                           <div class="assets_table_item">
                               <p>
                                    {{item.mac? item.mac : '--'}}
                               </p>

                           </div>
                           <div class="assets_table_item">
                               <p>
                                    {{item.type? item.type : '--'}}
                               </p>

                           </div>
                           <div class="assets_table_item">
                               <p>
                                    {{item.department? item.department : '--'}}
                               </p>

                           </div>
                           <div class="assets_table_item">
                               <p>
                                    {{item.location? item.location : '--'}}
                               </p>

                           </div>
                           <div class="assets_table_item">
                               <p>
                                    {{item.currenttime? item.currenttime : '--'}}
                               </p>

                           </div>
                           <div class="assets_table_item">
                               <Button  round size="small"  @click="toTempDetailFn(item.mac)">查看</Button>
                               <Button  round size="small"  @click="toTempEditFn(item.mac)">编辑</Button>
                           </div>
                          
                         
                        </li>
                    </ul>
                </div>
            </div>
            <div class="loading_info_wrap">
                <Pagination 
                v-model="currentPage" 
                :total-items="totalCount" 
                :show-page-size="2"
                :items-per-page="pageSize"
                prev-text="上一页"
                next-text="下一页"
                force-ellipses
                mode="simple"
                @change="changePageFn"
                v-show="!loadingShow&&tempDetailDataNow.length"
                />
                <Loading v-show="loadingShow" size="24px">加载中...</Loading>
                <p v-show="!loadingShow&&!tempDetailDataNow.length">暂无数据</p>
            </div>
        </div>

        <Popup v-model="showSearchPane" position="top" round >
           <div class="query_data_wrap">
              <div class="query_data_box">
                  <Field clearable label="仪器编号"  v-model="queryData.freezernumber" placeholder="请输入仪器编号"/>
                  <Field clearable label="仪器名称"  v-model="queryData.freezername" placeholder="请输入仪器名称"/>
                  <Field clearable label="mac地址"  v-model="queryData.mac" placeholder="请输入mac地址"/>
                  <Field  clearable readonly label="部门名称"  v-model="queryData.department" placeholder="请选择部门" @click="departmentDialogShow=true"/>
                  <Field clearable readonly label="仪器类型"  v-model="queryData.type" placeholder="请选择仪器类型" @click="typeDialogShow=true"/>
              </div>
              <div class="tools_button_wrap">
                  <Button class="tools_button"   color="linear-gradient(to right, #ff6034, #ee0a24)" round @click="clearTempFn">清空</Button>
                  <Button class="tools_button"   color="linear-gradient(to right, #69aff5, #1989fa)" round  @click="searchTempFn">搜索</Button>
              </div>
           </div>
        </Popup>

        <Popup v-model="typeDialogShow" position="bottom">
            <Picker  :columns="instrumentType" show-toolbar title="请选择类型" @cancel="typeDialogShow = false" @confirm="getTypeFn" :visible-item-count=5 />
        </Popup>
        <Popup v-model="departmentDialogShow" position="bottom">
            <Picker  :columns="departmentroomArr" show-toolbar title="请选择部门" @cancel="departmentDialogShow = false" @confirm="getDepartmentFn" :visible-item-count=5 />
        </Popup>
        <Popup v-model="warningDialogShow" position="right" :style="{ width: '70%',height:'100%' }" class="warning_wrap">
            <div  class="temp_warning_title">
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
        </Popup>

        <div :class="selectMode ? 'header_tools_wrap show':'header_tools_wrap'">
            <!-- <span class="tools_wrap_operation" @click="exitSelectFn">完成</span> -->
            <Button  round size="small" type="info"  @click="exitSelectFn">完成</Button>
        </div>
        <div :class="selectMode ? 'footer_tools_wrap show' : 'footer_tools_wrap'">
            <!-- <span v-if="role==='admin'" class="tools_wrap_operation warning" @click="deleteFn">删除</span> -->
            <Button :disabled="role!=='admin'"  round size="small" type="danger"  @click="deleteFn">删除</Button>
        </div>
  </div>
</template>

<script>
import {Row,Col,Button,Tag,Loading,Search,Icon,Field,Popup,Picker,Pagination,CheckboxGroup,Checkbox,Popover,Dialog,Toast,Notify} from 'vant';
import 'vant/lib/row/style';
import 'vant/lib/col/style';
import 'vant/lib/button/style';
import 'vant/lib/tag/style';
import 'vant/lib/loading/style';
import 'vant/lib/search/style';
import 'vant/lib/icon/style';
import 'vant/lib/field/style';
import 'vant/lib/popup/style';
import 'vant/lib/picker/style';
import 'vant/lib/pagination/style';
import 'vant/lib/checkbox/style';
import 'vant/lib/checkbox-group/style';
import 'vant/lib/popover/style';
import 'vant/lib/dialog/style';
import 'vant/lib/toast/style';
import 'vant/lib/notify/style';
import NET_PORT from "../../../api/port.js"
import { getToken } from '../../../utils/auth.js';
import instrumentType from "../../../assets/data/instrumentType.js"
export default {
  name:'mobile_temp_manage',
  components:{
        Button,
        Row,
        Col,
        Tag,
        Loading,
        Search,
        Icon,
        Field,
        Popup,
        Picker,
        Pagination,
        CheckboxGroup,
        Checkbox,
        Popover,
        Dialog,
        Toast,
        Notify
  },
  props:{},
  data(){
    return {
        // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
        role:this.$store.state.role,
        warningDialogShow:false,
        typeDialogShow:false,
        departmentDialogShow:false,
        tempDetailData:[],
        tempDetailDataNow:[],
        tempDetailDataAll:[],
      
        tempWarningArr:[],

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

        instrumentType:instrumentType,
        departmentroomArr:[],

        value:'',
        showSearchPane:false,
        loadingShow:true,
        websock: null,
        warningSocket: null,
        showPagination:false,
        currentPage:1,
        totalCount:0,
        pageSize:10,

        searchMode:false,
        changePageMode:false,
        selectMode:false,

        checkedArr:[],
        checkAll:false,
        selectMode:false,
        showPopover:false,
        actions: [{ text: '仪器新增',icon:'plus'}, { text: '选择',icon:'certificate'}],
       
    }
  },
  watch:{
    mobileScroll(){
        if(!this.selectMode){
            let scrollTop=this.mobileScroll;
            if(scrollTop>54){
                this.$refs.mobileSearchBoxFixed.classList.add('show');
            }else if(scrollTop===0){
                this.$refs.mobileSearchBoxFixed.classList.remove('show');
            }
        }
       
    },

    checkedArr(){
      if(this.checkedArr.length&&this.checkedArr.length===this.tempDetailDataNow.length){
        this.checkAll=true;
      }else{
        this.checkAll=false;
      }
    },
  },
  computed:{
    mobileScroll(){
      return this.$store.state.mobileScroll
    }
  },
  methods:{
    onSelect(action) {
        switch(action.text){
                case '仪器新增' :
                    this.toCreateFn();
                break;
                case '选择':
                   this.enterSelectFn();
                break;
        }
        
    },

    enterSelectFn(){
      this.selectMode=true;
      this.$refs.assetsTableWrap.style.paddingBottom="60px"
    },

    exitSelectFn(){
        this.showPopover=false;
        this.selectMode=false;
        this.checkedArr=[]
        this.$refs.assetsTableWrap.style.paddingBottom="0"
    },

    checkAllFn(){
        if(this.checkAll===false){
                this.$refs.checkboxGroup.toggleAll();
            
            }else{
                this.$refs.checkboxGroup.toggleAll(true);
            }
    },

    deleteFn(){
      let arr=[];
      this.checkedArr.forEach((item,index)=>{
        this.tempDetailDataNow.forEach((m,n)=>{
          if(item===m.freezernumber){
            arr.push(m.mac)
          }
        })
      })
      if(arr.length){
        Dialog.confirm({
            title: '提示！',
            message: '<span style="color:#ee0a24;">你确定要删除这'+arr.length+'个仪器吗？</span>',
            confirmButtonColor:'#ee0a24',
        }).then(() => {
          this.deleteCertainFn(arr);
        }).catch(() => {

        });
      }else{
          this.$notify({
              type:'warning',
              message: '请至少选择一个仪器！',
              duration: 2000
          });

      }
    },

    deleteCertainFn(list){
        var deleteLoading=Toast.loading({
              message: '正在删除...',
              forbidClick: true,
              duration:0
          });
          this.$axios.get(NET_PORT.tempDelete+list)
          .then((res)=>{

              if(res.data.code===0){
                deleteLoading.clear();
                Toast.success('删除成功！');
                this.exitSelectFn();
                this.checkedArr=[]
              }else{
                  deleteLoading.clear();
                  Toast.fail('删除失败！');
              }

          })
          .catch((err)=>{
              deleteLoading.clear();
              Toast.fail('删除失败！');
          })

    },

    getTypeFn(value){
      this.queryData.type=value.key;
      this.typeDialogShow=false;
    },
    getDepartmentFn(value){
      this.queryData.department=value.key;
      this.departmentDialogShow=false;
    },

    showMoreMenuFn(){
        this.$emit('getMessage', true);
    },
    openSearchPaneFn(){
        this.showSearchPane=true;
    },
    toTempDetailFn(mac){
        this.$router.push({
            path:`/mobile/temp_detail/${mac}`
        })
    },

    toTempEditFn(mac){
        this.$router.push({
            path:`/mobile/temp_edit/${mac}`
        })
    },

    toCreateFn(){
        this.$router.push({
            path:'/mobile/temp_create'
        })
    },

    searchTempFn(){
        this.tempDetailDataNow=[]
        this.searchMode=true;
        this.checkedArr=[];
        this.selectMode=false;
        this.loadingShow=true;
        this.showSearchPane=false;
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
                this.loadingShow=false;
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

            // if(!==''){
            //     this.tempWarningArr=arr.filter((item,index)=>{
            //         return item.locDept===;
            //     })
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

        if(this.loadingShow){
          this.loadingShow=false;
        }
      },

       //点击改变页码
      changePageFn(page){
        this.changePageMode=true;
        this.checkedArr=[];
        // this.selectMode=false;
        this.tempDetailDataNow=[]
        this.loadingShow=true;
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
            this.loadingShow=false;
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

  
  },
  created(){
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
  },
  mounted(){

  },
   activated(){
       
        this.exitSelectFn();
        // if(!==''){
        //     this.queryData.department=;
        //     this.queryDataNow.department=;
        //     this.searchMode=true;
        // }
        this.initWebSocket()
        this.initWarningSocket();
  },
  
  deactivated(){
        this.exitSelectFn();
        this.websock.close()
        this.warningSocket.close()
  },
  beforeDestroy(){

  }
}
</script>
<style lang="scss" scoped>
    @import "../../../assets/scss/mobile/common/manage_list.scss";
    @import "../../../assets/scss/mobile/temp_manage.scss";
</style>
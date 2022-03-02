<template>
  <div class='mobile_damage_manage_wrap manage_list_wrap custom_flex_column_wrap'>
    <div class="mobile_search_wrap">
         <div class="mobile_search_box" ref="mobileSearchBoxFixed">
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
            
                <div class="mobile_search_box_right_tools" style="width:40px">
                    <Icon name="bars"  @click="showMoreMenuFn"/>
                </div>
            </div>
        </div>
    </div>

        <div class="assets_table_wrap">
            <div class="assets_table_wrap_content  clearfix">
                <div class="assets_table_wrap_left">
                    <ul class="assets_table_ul" >
                        <li class="assets_table_li assets_table_li_name">
                            <div class="assets_table_item"><p>序号</p></div>
                        </li>
                        <li class="assets_table_li" v-for="(item,index) in assetsDetailData" :key="index">
                            <div class="assets_table_item"><p>{{index+1}}</p></div>
                        </li>
                    </ul>
                </div>
                <div class="assets_table_wrap_right">
                    <ul class="assets_table_ul" >
                        <li class="assets_table_li assets_table_li_name clearfix">
                            <div class="assets_table_item"><p>资产编号</p></div>
                            <div class="assets_table_item"><p>资产名称</p></div>
                            <div class="assets_table_item"><p>科室名称</p></div>
                            <div class="assets_table_item"><p>存放地点</p></div>
                            <div class="assets_table_item"><p>标签mac</p></div>
                            <!-- <div class="assets_table_item"><p>SN</p></div> -->
                            <div class="assets_table_item"><p>状态</p></div>
                            <div class="assets_table_item"><p>操作</p></div>
                        </li>
                        <li class="assets_table_li clearfix" v-for="(item,index) in assetsDetailData" :key="index" >
                            <div class="assets_table_item">
                                <p>{{item.assetID ? item.assetID : '--'}}</p>
                            </div>
                            <div class="assets_table_item">
                                <p>{{item.assetName ? item.assetName : '--'}}</p>
                            </div>
                            <div class="assets_table_item">
                                <p>{{item.locDept ? item.locDept : '--'}}</p>
                            </div>
                            <div class="assets_table_item">
                                <p>{{item.location ? item.location : '--'}}</p>
                            </div>
                            <div class="assets_table_item">
                                <p>{{item.mac ? item.mac : '--'}}</p>
                            </div>
                            <!-- <div class="assets_table_item">
                                <p>{{item.sn ? item.sn : '--'}}</p>
                            </div> -->
                            <div class="assets_table_item">
                                <Tag  size="medium" v-if="item.status==='正常'" :color="statusColor[0]">{{item.status}}</Tag>
                                <Tag  size="medium" v-else-if="item.status==='待维修'" :color="statusColor[1]">{{item.status}}</Tag>
                                <Tag  size="medium" v-else-if="item.status==='维修接单'" :color="statusColor[2]">{{item.status}}</Tag>
                                <Tag  size="medium" v-else-if="item.status==='待报废'" :color="statusColor[3]">{{item.status}}</Tag>
                                <Tag  size="medium" v-else-if="item.status==='报废'" :color="statusColor[4]">{{item.status}}</Tag>
                                <Tag  size="medium" v-else-if="item.status==='外借'" :color="statusColor[5]">{{item.status}}</Tag>
                                <Tag  size="medium" v-else color="#f7f7f7" text-color="#515a6e">{{item.status}}</Tag>
                            </div>
                            <div class="assets_table_item">
                                <Button v-if="item.status==='正常'||item.status==='待维修'" round size="small" style="border-color:#ff9900;color:#ff9900;"    @click="openRegisterModalFn('repairRegisterData',item)">维修登记</Button>
                                <Button v-if="item.status==='维修接单'"  round size="small"   style="border-color:#07c160;color:#07c160;"  @click="openRegisterModalFn('repairAchieveRegisterData',item)">维修完成</Button>
                                <Button v-if="item.status==='正常'" round size="small"   style="border-color:#9a66e4;color:#9a66e4;" @click="openRegisterModalFn('deliveryRegisterData',item)">外借登记</Button>
                                <Button v-if="item.status==='外借'" round size="small" style="border-color:#2d8cf0;color:#2d8cf0;"   @click="openRegisterModalFn('deliveryReturnRegisterData',item)">外借归还</Button>
                                <Button  round size="small"  @click="toDetailFn(item.assetID)">查看记录</Button>
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
                prev-text="上一页"
                next-text="下一页"
                force-ellipses
                mode="simple"
                @change="changePageFn"
                v-show="showPagination&&assetsDetailData.length"
                />

                <Loading v-show="loadingShow" size="24px">加载中...</Loading>
                <p v-show="!loadingShow&&!assetsDetailData.length">暂无数据</p>
            </div>
        </div>

        <Popup v-model="showSearchPane" position="top" round >
            <div class="query_data_wrap">
                <div class="query_data_box">
                    <Field clearable label="资产编号"  v-model="queryData.assetID" placeholder="请输入资产编号"/>
                    <Field clearable label="资产名称" center  v-model="queryData.assetName" placeholder="请输入资产名称">
                            <template #button>
                            <Button size="small" type="primary"  @click="showInputSearchFn('assetName')">云数据</Button>
                            </template>
                    </Field>
                    <Field clearable label="部门名称" center  v-model="queryData.memorydepart" placeholder="请输入部门名称">
                            <template #button>
                                <Button size="small" type="primary"  @click="showInputSearchFn('departmentroom')">云数据</Button>
                            </template>
                    </Field>
                    <Field  clearable label="所在科室" center  v-model="queryData.memorylocDept" placeholder="请输入所在科室">
                            <template #button>
                                <Button  size="small" type="primary"  @click="showInputSearchFn('locDept')">云数据</Button>
                            </template>
                    </Field>
                    <Field clearable label="标签mac"  v-model="queryData.mac" placeholder="请输入标签mac"/>
                    <Field readonly label="状态"  v-model="queryData.status" placeholder="请选择状态" @click="statusDialogShow=true"/>
                </div>
            
                <div class="tools_button_wrap">
                    <Button class="tools_button"   color="linear-gradient(to right, #ff6034, #ee0a24)" round @click="clearAssetsFn">清空</Button>
                    <Button class="tools_button"   color="linear-gradient(to right, #69aff5, #1989fa)" round  @click="searchFn">搜索</Button>
                </div>
            </div>
        </Popup>
        <Popup class="popup_wrap" v-model="showSignature" position="top"  round @closed="signatureCancelFn">
            <sign-canvas class="sign-canvas" ref="SignCanvas" :options="options" v-model="currentSignature"/>
            <div class="tools_button_wrap">
                <Button class="tools_button"   round color="linear-gradient(to right, #ff6034, #ee0a24)" @click="canvasClear()">清空</Button>
                <Button class="tools_button"   round color="linear-gradient(to right, #69aff5, #1989fa)" @click="saveAsImg()" ref="saveButton">保存</Button>
            </div>
        </Popup>

        <Popup class="popup_wrap" v-model="repairRegisterData.show" position="bottom"  round @closed="registerCancelFn">
            <div class="form_fill_item">
                   <Field disabled label="资产编号" v-model="registerObj.assetID" type="text"/>
            </div>
            <div class="form_fill_item">
                <Cell title="负责人签名">
                    <template #label>
                        <Button size="small" round :type="repairRegisterData.data.handlingPeople ?'danger' :'info'" @click="showSignature=true;currentStep='handlingPeople'">{{repairRegisterData.data.handlingPeople ? '重签' : '签名'}}</Button>
                    </template>
                    <template #default>
                        <img style="width:50px; cursor:pointer" v-show="repairRegisterData.data.handlingPeople" :src="repairRegisterData.data.handlingPeople" alt="" @click="showImgFn(repairRegisterData.data.handlingPeople)">
                    </template>
                </Cell>
            </div>
            <div class="form_fill_item">
                <Cell title="交接人签名">
                    <template #label>
                        <Button size="small" round :type="repairRegisterData.data.buttingPeople ?'danger' :'info'" @click="showSignature=true;currentStep='buttingPeople'">{{repairRegisterData.data.buttingPeople ? '重签' : '签名'}}</Button>
                    </template>
                    <template #default>
                        <img style="width:50px; cursor:pointer" v-show="repairRegisterData.data.buttingPeople" :src="repairRegisterData.data.buttingPeople" alt="" @click="showImgFn(repairRegisterData.data.buttingPeople)">
                    </template>
                </Cell>
            </div>
            <div class="form_fill_item">
                <div @click="openDeliveryModalFn">
                    <Field label="交接人科室" v-model="repairRegisterData.data.locDept" readonly type="text" placeholder="请选择交接人科室"/>
                </div>
                
            </div>
            <div class="form_fill_item">
                <Field label="备注信息" v-model="repairRegisterData.data.remark" type="textarea" placeholder="请输入备注信息"/>
            </div>
            <div class="submit_button_wrap">
                <Button class="submit_button"  type="warning" round  @click="registerSubmitFn">提交维修登记</Button>
            </div>
        </Popup>

        <Popup class="popup_wrap" v-model="repairAchieveRegisterData.show" position="bottom"  round @closed="registerCancelFn">
            <div class="form_fill_item">
                   <Field disabled label="资产编号" v-model="registerObj.assetID" type="text"/>
            </div>
            <div class="form_fill_item">
                <Cell title="负责人签名">
                    <template #label>
                        <Button size="small" round :type="repairAchieveRegisterData.data.handlingPeople ?'danger' :'info'" @click="showSignature=true;currentStep='handlingPeople'">{{repairAchieveRegisterData.data.handlingPeople ? '重签' : '签名'}}</Button>
                    </template>
                    <template #default>
                        <img style="width:50px; cursor:pointer" v-show="repairAchieveRegisterData.data.handlingPeople" :src="repairAchieveRegisterData.data.handlingPeople" alt="" @click="showImgFn(repairAchieveRegisterData.data.handlingPeople)">
                    </template>
                </Cell>
            </div>
            <div class="form_fill_item">
                <Cell title="交接人签名">
                    <template #label>
                        <Button size="small" round :type="repairAchieveRegisterData.data.buttingPeople ?'danger' :'info'" @click="showSignature=true;currentStep='buttingPeople'">{{repairAchieveRegisterData.data.buttingPeople ? '重签' : '签名'}}</Button>
                    </template>
                    <template #default>
                        <img style="width:50px; cursor:pointer" v-show="repairAchieveRegisterData.data.buttingPeople" :src="repairAchieveRegisterData.data.buttingPeople" alt="" @click="showImgFn(repairAchieveRegisterData.data.buttingPeople)">
                    </template>
                </Cell>
            </div>
            <div class="form_fill_item">
                <div @click="openDeliveryModalFn">
                    <Field label="交接人科室" v-model="repairAchieveRegisterData.data.locDept" readonly type="text" placeholder="请选择交接人科室"/>
                </div>
            </div>
            <div class="form_fill_item">
                <Field label="备注信息" v-model="repairAchieveRegisterData.data.remark" type="textarea" placeholder="请输入备注信息"/>
            </div>
            <div class="submit_button_wrap">
                <Button class="submit_button"  type="primary" round  @click="registerSubmitFn">提交维修完成登记</Button>
            </div>
        </Popup>

        <Popup class="popup_wrap" v-model="deliveryRegisterData.show" position="bottom"  round @closed="registerCancelFn">
            <div class="form_fill_item">
                   <Field disabled label="资产编号" v-model="registerObj.assetID" type="text"/>
            </div>
            <div class="form_fill_item">
                <Cell title="负责人签名">
                    <template #label>
                        <Button size="small" round :type="deliveryRegisterData.data.handlingPeople ?'danger' :'info'" @click="showSignature=true;currentStep='handlingPeople'">{{deliveryRegisterData.data.handlingPeople ? '重签' : '签名'}}</Button>
                    </template>
                    <template #default>
                        <img style="width:50px; cursor:pointer" v-show="deliveryRegisterData.data.handlingPeople" :src="deliveryRegisterData.data.handlingPeople" alt="" @click="showImgFn(deliveryRegisterData.data.handlingPeople)">
                    </template>
                </Cell>
            </div>
            <div class="form_fill_item">
                <Cell title="交接人签名">
                    <template #label>
                        <Button size="small" round :type="deliveryRegisterData.data.buttingPeople ?'danger' :'info'" @click="showSignature=true;currentStep='buttingPeople'">{{deliveryRegisterData.data.buttingPeople ? '重签' : '签名'}}</Button>
                    </template>
                    <template #default>
                        <img style="width:50px; cursor:pointer" v-show="deliveryRegisterData.data.buttingPeople" :src="deliveryRegisterData.data.buttingPeople" alt="" @click="showImgFn(deliveryRegisterData.data.buttingPeople)">
                    </template>
                </Cell>
            </div>
            <div class="form_fill_item">
                <div @click="openDeliveryModalFn">
                    <Field label="交接人科室" v-model="deliveryRegisterData.data.locDept" readonly type="text" placeholder="请选择交接人科室"/>
                </div>
            </div>
            <div class="form_fill_item">
                <div @click="returnTimeShow=true">
                    <Field label="预计归还时间" v-model="deliveryRegisterData.data.returnTime" type="text" readonly placeholder="请选择预计归还时间"/>
                </div>
            </div>
            <div class="form_fill_item">
                <Field label="备注信息" v-model="deliveryRegisterData.data.remark" type="textarea" placeholder="请输入备注信息"/>
            </div>
            <div class="submit_button_wrap">
                <Button class="submit_button"  style="background-color:#9a66e4;color:#fff" round  @click="registerSubmitFn">提交外借登记</Button>
            </div>
        </Popup>

       
        <Popup class="popup_wrap" v-model="deliveryReturnRegisterData.show" position="bottom"  round @closed="registerCancelFn">
            <div class="form_fill_item">
                   <Field disabled label="资产编号" v-model="registerObj.assetID" type="text"/>
            </div>
            <div class="form_fill_item">
                <Cell title="负责人签名">
                    <template #label>
                        <Button size="small" round :type="deliveryReturnRegisterData.data.handlingPeople ?'danger' :'info'" @click="showSignature=true;currentStep='handlingPeople'">{{deliveryReturnRegisterData.data.handlingPeople ? '重签' : '签名'}}</Button>
                    </template>
                    <template #default>
                        <img style="width:50px; cursor:pointer" v-show="deliveryReturnRegisterData.data.handlingPeople" :src="deliveryReturnRegisterData.data.handlingPeople" alt="" @click="showImgFn(deliveryReturnRegisterData.data.handlingPeople)">
                    </template>
                </Cell>
            </div>
            <div class="form_fill_item">
                <Cell title="交接人签名">
                    <template #label>
                        <Button size="small" round :type="deliveryReturnRegisterData.data.buttingPeople ?'danger' :'info'" @click="showSignature=true;currentStep='buttingPeople'">{{deliveryReturnRegisterData.data.buttingPeople ? '重签' : '签名'}}</Button>
                    </template>
                    <template #default>
                        <img style="width:50px; cursor:pointer" v-show="deliveryReturnRegisterData.data.buttingPeople" :src="deliveryReturnRegisterData.data.buttingPeople" alt="" @click="showImgFn(deliveryReturnRegisterData.data.buttingPeople)">
                    </template>
                </Cell>
            </div>
            <div class="form_fill_item">
                <div @click="openDeliveryModalFn">
                    <Field label="交接人科室" v-model="deliveryReturnRegisterData.data.locDept" readonly type="text" placeholder="请选择交接人科室"/>
                </div>
            </div>
           
            <div class="form_fill_item">
                <Field label="备注信息" v-model="deliveryReturnRegisterData.data.remark" type="textarea" placeholder="请输入备注信息"/>
            </div>
            <div class="submit_button_wrap">
                <Button class="submit_button"  type="info" round  @click="registerSubmitFn">提交外借归还登记</Button>
            </div>
        </Popup>

        <Popup v-model="returnTimeShow" position="bottom">
            <DatetimePicker
            v-model="returnTime"
            type="datetime"
            show-toolbar
            title="请选择预计归还时间" 
            :min-date="minDate"
            :max-date="maxDate"
            @cancel="returnTimeShow = false" 
            @confirm="getReturnTimeFn"
            />
        </Popup>

        <Popup v-model="showImg" class="scale_img_wrap">
            <img  :src="showImgData" alt="">
        </Popup>

        <Popup v-model="statusDialogShow" position="bottom">
            <Picker  :columns="statusData" show-toolbar title="请选择状态" @cancel="statusDialogShow=false" @confirm="getStatusFn" :visible-item-count=5 />
        </Popup>

        <Popup v-model="showButtingLocDeptInputSearch" position="bottom">
            <Search
                v-model="buttingLocDeptInputSearchValue"
                placeholder="请输入搜索关键词"
                input-align="center"
                @input="handleButtingLocDeptSearchFn"
            />
            <Picker  show-toolbar toolbar-position="bottom" @cancel="showButtingLocDeptInputSearch = false" @confirm="buttingLocDeptCertainSelectFn"  :columns="buttingLocDeptAll"  title=""  :visible-item-count=5 />
        </Popup>

        <Popup v-model="showInputSearch" position="bottom">
          <Search
                v-model="inputSearchValue"
                placeholder="请输入搜索关键词"
                input-align="center"
                @input="handleSearchFn"
            />
            <Picker  show-toolbar toolbar-position="bottom" @cancel="showInputSearch = false" @confirm="certainSelectFn"  :columns="inputData"  title=""  :visible-item-count=5 />
        </Popup>

  </div>
</template>

<script>

import {Button,Tag ,Icon,Dialog,Toast,Loading,Pagination,Field,Search,Popup,Notify,Cell,Picker,DatetimePicker} from 'vant';
import 'vant/lib/button/style';
import 'vant/lib/tag/style';
import 'vant/lib/dialog/style';
import 'vant/lib/toast/style';
import 'vant/lib/loading/style';
import 'vant/lib/pagination/style';
import 'vant/lib/field/style';
import 'vant/lib/popup/style';
import 'vant/lib/search/style';
import 'vant/lib/icon/style';
import 'vant/lib/notify/style';
import 'vant/lib/cell/style';
import 'vant/lib/picker/style';
import 'vant/lib/datetime-picker/style';
import NET_PORT from "../../../api/port.js"
import {getToken} from "../../../utils/auth.js"
import statusData from "../../../assets/data/status.js"
import statusColor from "../../../assets/data/statusColor.js"
import buttingLocDept from "../../../assets/data/buttingLocDept.js"
import SignCanvas from 'sign-canvas';
export default {
  name:'mobile_damage_manage',
  components:{
        Button,
        Tag,
        Dialog,
        Toast,
        Loading,
        Pagination,
        Field,
        Popup,
        Search,
        Icon,
        Notify,
        Cell,
        Picker,
        DatetimePicker,
        SignCanvas,
  },
  props:{},
  data(){
    return {
        // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
        inputData:[],
        inputDataAll:[],
        inputSearchValue:'',
        activeKey:'',
        showInputSearch:false,

        registerObj:{},
        options:{
            isDpr: true,       //是否使用dpr兼容高倍屏 [Boolean] 可选
            lastWriteSpeed: 1,  //书写速度 [Number] 可选
            lastWriteWidth: 2,  //下笔的宽度 [Number] 可选
            lineCap: 'round',   //线条的边缘类型 [butt]平直的边缘 [round]圆形线帽 [square]	正方形线帽
            lineJoin: 'bevel',  //线条交汇时边角的类型  [bevel]创建斜角 [round]创建圆角 [miter]创建尖角。
            canvasWidth: 300, //canvas宽高 [Number] 可选
            canvasHeight: 400,  //高度  [Number] 可选
            isShowBorder: true, //是否显示边框 [可选]
            bgColor: '#fff', //背景色 [String] 可选
            borderWidth: 1, // 网格线宽度  [Number] 可选
            borderColor: "#2d8cf0", //网格颜色  [String] 可选
            writeWidth: 5, //基础轨迹宽度  [Number] 可选
            maxWriteWidth: 30, // 写字模式最大线宽  [Number] 可选
            minWriteWidth: 5, // 写字模式最小线宽  [Number] 可选
            writeColor: '#101010', // 轨迹颜色  [String] 可选
            isSign: true, //签名模式 [Boolean] 默认为非签名模式,有线框, 当设置为true的时候没有任何线框
            imgType:'png'   //下载的图片格式  [String] 可选为 jpeg  canvas本是透明背景的
        },

        showButtingLocDeptInputSearch:false,
        buttingLocDeptInputSearchValue:'',
        buttingLocDeptAll:[],
        statusColor:statusColor,
        currentPage:1,
        totalCount:0,
        assetsDetailData:[],
        loadingShow:true,
        showPagination:false,
        queryData:{
          assetID:'',
          assetName:'',
          memorydepart:'',
          memorylocDept:'',
          mac:'',
          status:'',
        },
        queryDataNow:{},
        value:'',
        showSearchPane:false,

        showImg:false,
        showImgData:'',

        showSignature:false,
        currentSignature:'',

        currentMode:'',
        currentStep:'',

        repairRegisterData:{
            show:false,
            data:{
            handlingPeople:'',
            buttingPeople:'',
            locDept:'',
            remark:'',
            }
        
        },

        repairAchieveRegisterData:{
            show:false,
            data:{
                handlingPeople:'',
                buttingPeople:'',
                locDept:'',
                remark:'',
            }
        },

        deliveryRegisterData:{
            show:false,
            data:{
                handlingPeople:'',
                buttingPeople:'',
                locDept:'',
                returnTime:'',
                remark:'',
            }
        },

        deliveryReturnRegisterData:{
            show:false,
            data:{
                handlingPeople:'',
                buttingPeople:'',
                locDept:'',
                remark:'',
            }
        },

        returnTimeShow:false,
        returnTime:'',
        minDate: new Date(2000, 0, 1),
        maxDate: new Date(2050, 12, 0),

        showSearchPane:false,
        statusDialogShow:false,
        statusData:statusData,

    }
  },
 watch:{
    mobileScroll(){
        let scrollTop=this.mobileScroll;
        if(scrollTop>54){
          this.$refs.mobileSearchBoxFixed.classList.add('show');
        }else if(scrollTop===0){
          this.$refs.mobileSearchBoxFixed.classList.remove('show');
        }
    }
  },
  computed:{
    mobileScroll(){
      return this.$store.state.mobileScroll
    }
  },
  methods:{

    certainSelectFn(val){
      if(this.activeKey==='departmentroom'){
        this.queryData.memorydepart=val
      }else if(this.activeKey==='locDept'){
        this.queryData.memorylocDept=val
      }else{
        this.queryData[this.activeKey]=val;
      }
        
        this.showInputSearch=false;
        this.inputSearchValue='';
        this.activeKey='';
    },
    showInputSearchFn(key){
        this.showInputSearch=true;
        this.activeKey=key;
        this.inputFocusFn(key);
    },

    handleSearchFn(data){
        this.inputData = this.inputDataAll.filter(item => item.toLowerCase().indexOf(data.toLowerCase()) > -1);
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

            this.inputData=JSON.parse(JSON.stringify(this.inputDataAll))
        })
        .catch((err)=>{
            console.log(err)
        })
            
    },

    getStatusFn(value) {
        this.queryData.status=value.text;
        this.statusDialogShow=false;
    },

    clearAssetsFn(){
        for(let i in this.queryData){
            // if(this.locDept!==''){
            //     if(i!=='memorylocDept'){
            //     this.queryData[i]="";
            //     }
            // }else{
            //     this.queryData[i]="";
            // }
            this.queryData[i]="";
        }
    },

    buttingLocDeptCertainSelectFn(val){
        this[this.currentMode].data.locDept=val.key;
        this.showButtingLocDeptInputSearch=false;
        this.buttingLocDeptInputSearchValue='';
    },

    openDeliveryModalFn(key){
        this.buttingLocDeptAll=buttingLocDept;
        this.showButtingLocDeptInputSearch=true;
    },

    handleButtingLocDeptSearchFn(data){
        this.buttingLocDeptAll = buttingLocDept.filter(item => item.key.toLowerCase().indexOf(data.toLowerCase()) > -1);
    },

    openRegisterModalFn(key,obj){
        this.registerObj=obj;
        this.currentMode=key;
        this[key].show=true;
    },

    signatureCancelFn(){
        this.currentSignature=''
        this.canvasClear();
    },

    registerCancelFn(){
        let arr=['repairRegisterData','repairAchieveRegisterData','deliveryRegisterData','deliveryReturnRegisterData']
        for(let i=0;i<arr.length;i++){
            this[arr[i]].show=false;
            this[arr[i]].data.handlingPeople='';
            this[arr[i]].data.buttingPeople='';
            this[arr[i]].data.locDept='';
            this[arr[i]].data.remark='';
        }
        this.deliveryReturnRegisterData.data.returnTime='';
        this.returnTime='';
        this.currentMode='';
        this.currentStep='';
        this.registerObj={};
    },

    registerSubmitFn(){
       if(this[this.currentMode].data.handlingPeople.trim()&&this[this.currentMode].data.buttingPeople.trim()){
        //显示出正在上传
        var uploadLoading=Toast.loading({
            message: '正在提交...',
            forbidClick: true,
            duration:0
        });

        let PORT='';
        let datas={};
        if(this.currentMode==='repairRegisterData'||this.currentMode==='repairAchieveRegisterData'){
            datas={
              maintainhistoryonlyCode:this.registerObj.assetID,
              assetstatustype:this.currentMode==='repairRegisterData' ? '1' : '2',
              status:this.currentMode==='repairRegisterData'? '维修接单' : '正常',
              handlingPeople:this[this.currentMode].data.handlingPeople,
              buttingPeople:this[this.currentMode].data.buttingPeople,
              handoverdepartment:this[this.currentMode].data.locDept.trim(),
              remark:this[this.currentMode].data.remark.trim(),
              handoverimage:'',
              applaydepartment:'',
              applaypeople:'',
              successorpeople:'',
              estimatedtime:'',
            }

            PORT=NET_PORT.repairRegister
            
        }else{
            datas={
              maintainhistoryonlyCode:this.registerObj.assetID,
              assetstatustype:this.currentMode==='deliveryRegisterData' ? '3' : '4',
              status:this.currentMode==='deliveryRegisterData' ? '外借' : '正常',
              handlingPeople:this[this.currentMode].data.handlingPeople,
              buttingPeople:this[this.currentMode].data.buttingPeople,
              handoverdepartment:this[this.currentMode].data.locDept.trim(),
              remark:this[this.currentMode].data.remark.trim(),
              handoverimage:'',
              applaydepartment:'',
              applaypeople:'',
              successorpeople:'',
              estimatedtime:this.currentMode==='deliveryRegisterData' ? this[this.currentMode].data.returnTime.trim() : '',
            }

            PORT=NET_PORT.deliveryRegister
        }
        this.$axios.post(PORT,datas)
        .then((res)=>{
          if(res.data.code===0){
            uploadLoading.clear();
            Toast.success('提交成功！');
            this.registerCancelFn();
            this.getDataFn();
            
          }else{
             uploadLoading.clear();
             Toast.fail('提交失败！');
          }
        })
        .catch((err)=>{
            uploadLoading.clear();
            Toast.fail('提交失败！');
        })
          
          
        }else{
            this.$notify({
                type:'warning',
                message: '请签名完成!',
                duration: 2000
            });
        }
    },

    canvasClear(){
        this.$refs.SignCanvas.canvasClear();
    },
  
    saveAsImg(){
        const img = this.$refs.SignCanvas.saveAsImg();
        this[this.currentMode].data[this.currentStep]=img
        this.showSignature=false;
        this.canvasClear();
    },

    hideImgFn(){
        this.showImg=false;
        this.showImgData='';
    },

    toDetailFn(id){
        this.$router.push('/mobile/damage_history/'+id)
    },

    getReturnTimeFn(value){
        var date=value
        var currentYear=date.getFullYear();
        var currentMonth=date.getMonth()+1;
        var currentDate=date.getDate();
        var currentHours=date.getHours();
        var currentMinutes=date.getMinutes();
        currentMonth= currentMonth<10 ? '0'+currentMonth : currentMonth
        currentDate= currentDate<10 ? '0'+currentDate : currentDate
        currentHours= currentHours<10 ? '0'+currentHours : currentHours
        currentMinutes= currentMinutes<10 ? '0'+currentMinutes : currentMinutes
            
        this.deliveryRegisterData.data.returnTime=currentYear+'-'+currentMonth+'-'+currentDate+' '+currentHours+':'+currentMinutes
        this.returnTimeShow=false;
    },

    showImgFn(img){
        this.showImgData=img
        this.showImg=true;
    },

    showMoreMenuFn(){
      this.$emit('getMessage', true);
    },

    openSearchPaneFn(){
      this.showSearchPane=true;
    },

    searchFn(){
        this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
        this.currentPage=1;
        this.assetsDetailData=[]
        this.totalCount=0;
        this.getDataFn();
        this.showSearchPane=false;
    },

    getDataFn(){
        
        this.loadingShow=true;
        this.assetsDetailData=[];

        let configData='';
        for (let m in this.queryDataNow){
            if(!this.queryDataNow[m]){
            this.queryDataNow[m]='';
            }
            //将参数拼接到一起，裁掉左右空格
            configData+=m+'='+this.queryDataNow[m].trim()+'&'
        }

        //裁掉最后一个&
        configData=configData.substring(0,configData.length-1)

        this.$axios.post(NET_PORT.assetsQuery+'?currentPage='+this.currentPage+'&'+configData)
        .then((res)=>{
            var initData=res.data;
            var total=Number(initData.pop().total);
                for(var i=0;i<initData.length;i++){
                    for(var k in initData[i]){
                        if(initData[i][k]===''||initData[i][k]==='null'){
                            initData[i][k]='--'; 
                        };
                    }
                    
                    this.assetsDetailData.push(initData[i])
                }
                this.totalCount=total;
                this.loadingShow=false;
                this.showPagination=true;
        })
        .catch((err)=>{

        })
    },

    changePageFn(){
        this.getDataFn();
    },
    
    getSearchConfigFn(){
    //   this.queryData.memorylocDept=this.locDept;
      this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
      this.getDataFn()
    }

  },
  created(){
     
  },
  mounted(){
    this.getSearchConfigFn();
  },


}
</script>
<style lang="scss" scoped>
    @import "../../../assets/scss/mobile/common/manage_list.scss";
    @import "../../../assets/scss/mobile/damage_manage.scss";
    
</style>
<template>
  <div class='damage_manage_wrap'>
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
      <div class="fold_icon_wrap ">
        <div class="fold_icon_box" @click="foldFn(0)">
          <Icon v-if="!foldArr[0]" type="ios-arrow-up"/>
          <Icon v-else type="ios-arrow-down" />
        </div>
      </div>
    </div>

     <div class="row_wrap hover_animat custom_bg_color_white">
      <div class="row_box">
          <h2 class="row_title">维修/报废/调用列表</h2>
          <div class="data_table_top clearfix">
            <div class="data_table_top_left">

            </div>
            <div class="data_table_top_right">
               
            </div>
          </div>

          <div class="table_scroll_wrap">
            <Table style="min-width:1600px"  stripe :columns="assetsDetailColumn" :data="assetsDetailData" size='large' :loading="tableLoading">

                <template slot-scope="{ row }" slot="status">
                    <Tag v-if="row.status==='正常'" :color="statusColor[0]" ><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='待维修'" :color="statusColor[1]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='维修接单'" :color="statusColor[2]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='待报废'" :color="statusColor[3]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='报废'" :color="statusColor[4]"  @click="statusFn(row.status)"><span>{{row.status}}</span></Tag>
                    <Tag v-else-if="row.status==='外借'" :color="statusColor[5]" ><span>{{row.status}}</span></Tag>
                    <Tag v-else  color="default"><span>{{row.status}}</span></Tag>
                </template>
                <template slot-scope="{ row }" slot="operation">
                    <!-- <Button v-if="row.status==='正常'||row.status==='待维修'" icon='ios-build'  shape="circle" >维修登记</Button> -->
                    <!-- <Button v-if="row.status==='维修接单'"  shape="circle" icon='md-checkmark' >维修完成</Button> -->
                    <!-- <Button v-if="row.status==='正常'" shape="circle" icon='md-checkmark' >外借登记</Button> -->
                    <!-- <Button v-if="row.status==='外借'" shape="circle" icon='md-arrow-back' >外借归还</Button> -->

                    <Button v-if="row.status==='正常'||row.status==='待维修'" icon='ios-build' ghost type="warning" shape="circle" @click="openRegisterModalFn('repairRegisterData',row)">维修登记</Button>
                    <Button v-if="row.status==='维修接单'"  shape="circle" icon='md-checkmark' ghost type="success" @click="openRegisterModalFn('repairAchieveRegisterData',row)">维修完成</Button>
                    <Button v-if="row.status==='正常'" shape="circle" icon='md-arrow-forward' ghost style="border-color:#9a66e4;color:#9a66e4;" @click="openRegisterModalFn('deliveryRegisterData',row)">外借登记</Button>
                    <Button v-if="row.status==='外借'" shape="circle" icon='md-arrow-back' ghost type="primary" @click="openRegisterModalFn('deliveryReturnRegisterData',row)">外借归还</Button>

                    <Button  shape="circle" icon="ios-book" @click="toDetailFn(row.assetID)">查看记录</Button>
                </template>
              
            </Table>
          </div>
          <div class="data_table_pagination_wrap">
            <Page show-elevator show-total show-sizer :total="totalCount" :page-size-opts="pageSizeOptions" @on-change="changePageFn" @on-page-size-change="changePageSizeFn" :current="currentPage"  :page-size="pageSize"/>
          </div>
      </div>
    </div>

    <Modal
        title=""
        width='700'
        v-model="showSignature"
        @on-cancel="signatureCancelFn"
        :mask-closable="false"
        footer-hide
        :closable="true">

        <sign-canvas class="sign-canvas" ref="SignCanvas" :options="options" v-model="currentSignature"/>
        <div style="text-align:center">
          <Button shape="circle" type="error" @click="canvasClear()">清空</Button>
          <Button shape="circle" type="primary" @click="saveAsImg()" ref="saveButton">保存</Button>
        </div>
    </Modal>

    <Modal
        title="维修登记"
        width='500'
        v-model="repairRegisterData.show"
        @on-cancel="registerCancelFn"
        :mask-closable="false"
        footer-hide
        :closable="true">
        <div class="form_fill_item">
          <h3>资产编号</h3>
          <Input disabled v-model="registerObj.assetID" />
        </div>
        <div class="form_fill_item">
          <h3>负责人签名</h3>
          <div class="form_fill_bottom clearfix">
            <div class="form_fill_left">
              <Button shape="circle" :type="repairRegisterData.data.handlingPeople ?'error' :'primary'" @click="showSignature=true;currentStep='handlingPeople'">{{repairRegisterData.data.handlingPeople ? '重签' : '签名'}}</Button>
            </div>
            <div class="form_fill_right">
              <img style="width:50px; cursor:pointer" :src="repairRegisterData.data.handlingPeople" alt="" @click="showImgFn(repairRegisterData.data.handlingPeople)">
            </div>
          </div>
        </div>
        <div class="form_fill_item">
          <h3>交接人签名</h3>
            <div class="form_fill_bottom clearfix">
              <div class="form_fill_left">
                <Button shape="circle" :type="repairRegisterData.data.buttingPeople ?'error' :'primary'" @click="showSignature=true;currentStep='buttingPeople'">{{repairRegisterData.data.buttingPeople ? '重签' : '签名'}}</Button>
              </div>
              <div class="form_fill_right">
                <img style="width:50px; cursor:pointer" :src="repairRegisterData.data.buttingPeople" alt="" @click="showImgFn(repairRegisterData.data.buttingPeople)">
              </div>
            </div>
        </div>
        <div class="form_fill_item">
          <h3>交接人科室</h3>
          <!-- <Input v-model="repairRegisterData.data.locDept"  placeholder="请输入交接人科室" /> -->
            <Select v-model="repairRegisterData.data.locDept" filterable>
                <Option v-for="item in buttingLocDept" :value="item.key" :key="item.text">{{ item.text }}</Option>
            </Select>
        </div>
        <div class="form_fill_item">
          <h3>备注</h3>
          <Input v-model="repairRegisterData.data.remark" type="textarea" placeholder="输入备注信息..." />
        </div>

        <div style="text-align:right">
          <Button type="warning" shape="circle"  @click="registerSubmitFn">提交维修登记</Button>
        </div>
       
    </Modal>

    <Modal
        title="维修完成登记"
        width='500'
        v-model="repairAchieveRegisterData.show"
        @on-cancel="registerCancelFn"
        :mask-closable="false"
        footer-hide
        :closable="true">
        <div class="form_fill_item">
          <h3>资产编号</h3>
          <Input disabled v-model="registerObj.assetID" />
        </div>
        <div class="form_fill_item">
          <h3>负责人签名</h3>
          <div class="form_fill_bottom clearfix">
            <div class="form_fill_left">
              <Button shape="circle"  :type="repairAchieveRegisterData.data.handlingPeople ?'error' :'primary'" @click="showSignature=true;currentStep='handlingPeople'">{{repairAchieveRegisterData.data.handlingPeople ? '重签' : '签名'}}</Button>
            </div>
            <div class="form_fill_right">
              <img style="width:50px; cursor:pointer" :src="repairAchieveRegisterData.data.handlingPeople" alt="" @click="showImgFn(repairAchieveRegisterData.data.handlingPeople)">
            </div>
          </div>
        </div>
        <div class="form_fill_item">
          <h3>交接人签名</h3>
            <div class="form_fill_bottom clearfix">
              <div class="form_fill_left">
                <Button shape="circle"  :type="repairAchieveRegisterData.data.buttingPeople ?'error' :'primary'" @click="showSignature=true;currentStep='buttingPeople'">{{repairAchieveRegisterData.data.buttingPeople ? '重签' : '签名'}}</Button>
              </div>
              <div class="form_fill_right">
                <img style="width:50px; cursor:pointer" :src="repairAchieveRegisterData.data.buttingPeople" alt="" @click="showImgFn(repairAchieveRegisterData.data.buttingPeople)">
              </div>
            </div>
        </div>
        <div class="form_fill_item">
          <h3>交接人科室</h3>
          <!-- <Input v-model="repairAchieveRegisterData.data.locDept"  placeholder="请输入交接人科室" /> -->
            <Select v-model="repairAchieveRegisterData.data.locDept" filterable>
                <Option v-for="item in buttingLocDept" :value="item.key" :key="item.text">{{ item.text }}</Option>
            </Select>
        </div>
        <div class="form_fill_item">
          <h3>备注</h3>
          <Input v-model="repairAchieveRegisterData.data.remark" type="textarea" placeholder="输入备注信息..." />
        </div>

        <div style="text-align:right">
          <Button type="success" shape="circle"  @click="registerSubmitFn">提交维修完成登记</Button>
        </div>
       
    </Modal>

    <Modal
        title="外借登记"
        width='500'
        v-model="deliveryRegisterData.show"
        @on-cancel="registerCancelFn"
        :mask-closable="false"
        footer-hide
        :closable="true">
        <div class="form_fill_item">
          <h3>资产编号</h3>
          <Input disabled v-model="registerObj.assetID" />
        </div>
        <div class="form_fill_item">
          <h3>负责人签名</h3>
          <div class="form_fill_bottom clearfix">
            <div class="form_fill_left">
              <Button shape="circle"  :type="deliveryRegisterData.data.handlingPeople ?'error' :'primary'" @click="showSignature=true;currentStep='handlingPeople'">{{deliveryRegisterData.data.handlingPeople ? '重签' : '签名'}}</Button>
            </div>
            <div class="form_fill_right">
              <img style="width:50px; cursor:pointer" :src="deliveryRegisterData.data.handlingPeople" alt="" @click="showImgFn(deliveryRegisterData.data.handlingPeople)">
            </div>
          </div>
        </div>
        <div class="form_fill_item">
          <h3>交接人签名</h3>
            <div class="form_fill_bottom clearfix">
              <div class="form_fill_left">
                <Button shape="circle"  :type="deliveryRegisterData.data.buttingPeople ?'error' :'primary'" @click="showSignature=true;currentStep='buttingPeople'">{{deliveryRegisterData.data.buttingPeople ? '重签' : '签名'}}</Button>
              </div>
              <div class="form_fill_right">
                <img style="width:50px; cursor:pointer" :src="deliveryRegisterData.data.buttingPeople" alt="" @click="showImgFn(deliveryRegisterData.data.buttingPeople)">
              </div>
            </div>
        </div>
        <div class="form_fill_item">
          <h3>交接人科室</h3>
          <!-- <Input v-model="deliveryRegisterData.data.locDept"  placeholder="请输入交接人科室" /> -->
            <Select v-model="deliveryRegisterData.data.locDept" filterable>
                <Option v-for="item in buttingLocDept" :value="item.key" :key="item.text">{{ item.text }}</Option>
            </Select>
        </div>
        <div class="form_fill_item">
          <h3>预计归还时间</h3>
          <DatePicker v-model="returnTime" type="datetime" @on-change="returnTimeFn"  placeholder="归还时间时间" style="width:100%"></DatePicker>
        </div>
        <div class="form_fill_item">
          <h3>备注</h3>
          <Input v-model="deliveryRegisterData.data.remark" type="textarea" placeholder="输入备注信息..." />
        </div>

        <div style="text-align:right">
          <Button style="background-color:#9a66e4;color:#fff" shape="circle"  @click="registerSubmitFn">提交外借登记</Button>
        </div>
    </Modal>
    <Modal
        title="外借归还登记"
        width='500'
        v-model="deliveryReturnRegisterData.show"
        @on-cancel="registerCancelFn"
        :mask-closable="false"
        footer-hide
        :closable="true">
        <div class="form_fill_item">
          <h3>资产编号</h3>
          <Input disabled v-model="registerObj.assetID" />
        </div>
        <div class="form_fill_item">
          <h3>负责人签名</h3>
          <div class="form_fill_bottom clearfix">
            <div class="form_fill_left">
              <Button shape="circle"  :type="deliveryReturnRegisterData.data.handlingPeople ?'error' :'primary'" @click="showSignature=true;currentStep='handlingPeople'">{{deliveryReturnRegisterData.data.handlingPeople ? '重签' : '签名'}}</Button>
            </div>
            <div class="form_fill_right">
              <img style="width:50px; cursor:pointer" :src="deliveryReturnRegisterData.data.handlingPeople" alt="" @click="showImgFn(deliveryReturnRegisterData.data.handlingPeople)">
            </div>
          </div>
        </div>
        <div class="form_fill_item">
          <h3>交接人签名</h3>
            <div class="form_fill_bottom clearfix">
              <div class="form_fill_left">
                <Button shape="circle"  :type="deliveryReturnRegisterData.data.buttingPeople ?'error' :'primary'" @click="showSignature=true;currentStep='buttingPeople'">{{deliveryReturnRegisterData.data.buttingPeople ? '重签' : '签名'}}</Button>
              </div>
              <div class="form_fill_right">
                <img style="width:50px; cursor:pointer" :src="deliveryReturnRegisterData.data.buttingPeople" alt="" @click="showImgFn(deliveryReturnRegisterData.data.buttingPeople)">
              </div>
            </div>
        </div>
        <div class="form_fill_item">
          <h3>交接人科室</h3>
          <!-- <Input v-model="deliveryReturnRegisterData.data.locDept"  placeholder="请输入交接人科室" /> -->
            <Select v-model="deliveryReturnRegisterData.data.locDept" filterable>
                <Option v-for="item in buttingLocDept" :value="item.key" :key="item.text">{{ item.text }}</Option>
            </Select>
        </div>
       
        <div class="form_fill_item">
          <h3>备注</h3>
          <Input v-model="deliveryReturnRegisterData.data.remark" type="textarea" placeholder="输入备注信息..." />
        </div>

        <div style="text-align:right">
          <Button type="primary" shape="circle"  @click="registerSubmitFn">提交外借归还登记</Button>
        </div>
    </Modal>

  <div v-show="showImg" class="scale_img_wrap"  @click="hideImgFn">
      <img  :src="showImgData" alt="">
  </div>

  <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360" :styles="{top: '20px'}">
      <div style="text-align:center;padding:20px 0">
          <Spin fix v-if="!achieveUpload&&!errorUpload">
              <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
              <div>正在提交</div>
          </Spin>
          <Spin fix v-if="achieveUpload" style="color:#00ad19">
              <Icon type="ios-checkmark-circle" size=18 />
              <div>提交成功</div>
          </Spin>
          <Spin fix v-if="errorUpload" style="color:#f72b2b">
              <Icon type="ios-close-circle" size=18 />
              <div>提交失败</div>
          </Spin>
      </div>
  </Modal>
   
</div>
</template>

<script>
import Vue from "vue"
import { Row,Col,Icon,Table ,Tag ,Page,Button,Modal,Input,Select,Option,RadioGroup,Radio,Tabs,TabPane,AutoComplete,DatePicker,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Select',Select);
Vue.component('Option',Option);
Vue.component('RadioGroup',RadioGroup);
Vue.component('Radio',Radio);
Vue.component('Tabs',Tabs);
Vue.component('TabPane',TabPane);
Vue.component('AutoComplete',AutoComplete);
Vue.component('DatePicker',DatePicker);
Vue.component('Spin',Spin);
import { getToken } from '../../../utils/auth.js'
import NET_PORT from "../../../api/port.js"
import statusColor from "../../../assets/data/statusColor.js"
import statusData from "../../../assets/data/status.js"
import assetsConfigArr from "../../../assets/data/assetsConfigArr.js"
import buttingLocDept from "../../../assets/data/buttingLocDept.js"
import SignCanvas from 'sign-canvas';
export default {
  name:'damage_manage',
  components:{SignCanvas},
  props:{},
  data(){
    return {
      // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
      registerObj:{},
      options:{
          isDpr: true,       //是否使用dpr兼容高倍屏 [Boolean] 可选
          lastWriteSpeed: 1,  //书写速度 [Number] 可选
          lastWriteWidth: 2,  //下笔的宽度 [Number] 可选
          lineCap: 'round',   //线条的边缘类型 [butt]平直的边缘 [round]圆形线帽 [square]	正方形线帽
          lineJoin: 'bevel',  //线条交汇时边角的类型  [bevel]创建斜角 [round]创建圆角 [miter]创建尖角。
          canvasWidth: 600, //canvas宽高 [Number] 可选
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

      returnTime:'',

      uploadModal:false,
      achieveUpload:false,
      errorUpload:false,
      buttingLocDept:buttingLocDept,
      statusColor:statusColor,
      statusData:statusData,
      foldArr:[false,],
      inputData:[],
      inputDataAll:[],
      queryData:{
        assetID:'',
        assetName:'',
        memorydepart:'',
        memorylocDept:'',
        mac:"",
        status:"",
      },

      queryDataNow:{},

      tableLoading:true,
      totalCount: 0,
      pageSize: 10,
      currentPage: 1,
      pageSizeOptions:[10,20,30,40,50,60,70,80,90,100],

      assetsDetailColumn:[
          
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
                title: '存放地点',
                key: 'location',
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
            {
                title: '操作',
                key: 'operation',
                slot:'operation',
                width:350
            },
      ],

      assetsConfigArr:assetsConfigArr,

      assetsDetailData:[],

    }
  },
  watch:{
   
  },
  computed:{
   
  },
  methods:{
    
    showImgFn(img){
        this.showImgData=img
        this.showImg=true;
    },

    hideImgFn(){
        this.showImg=false;
        this.showImgData='';
    },

    toDetailFn(id){
      this.$router.push('/web/damage_history/'+id)
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
        this.uploadModal=true;
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
            this.successFn();
            
          }else{
            this.errorFn();
          }
        })
        .catch((err)=>{
          this.errorFn();
        })
          
          
        }else{
          this.$Message.error({
              content:"请签名完成!",
              duration:2,
          })
        }
    },

    returnTimeFn(value){
        this.deliveryRegisterData.data.returnTime=value
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

    foldFn(index){
      this.$set( this.foldArr,index, !this.foldArr[index])
    },


    //点击搜索
    searchAssetsFn(){
        this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));

        // let queryData={
        //     memorydepart:this.queryData.memorydepart,
        //     memorylocDept:this.queryData.memorylocDept,
        //     memoryoneclassify:this.queryData.oneclassify,
        //     memorysecondclassify:this.queryData.secondclassify,
        //     memorythreeclassify:this.queryData.threeclassify,
        //     memoryfourclassify:this.queryData.fourclassify,
        // }

        this.currentPage=1;
      
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

    successFn(){
        this.achieveUpload=true;
        setTimeout(()=>{
            this.uploadModal=false;
            setTimeout(()=>{
                this.achieveUpload=false;
                this.errorUpload=false;
                this.registerCancelFn();
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

    getSearchConfigFn(){
      // this.queryData.memorylocDept=this.locDept;
      this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
      this.getDataFn()
    }

  },
  created(){
    this.getSearchConfigFn();
  },
  mounted(){
    
  },
 
}
</script>


<style lang='scss' scoped>
    @import '../../../assets/scss/web/damage_manage.scss';
    @import '../../../assets/scss/web/common/table.scss';
    @import '../../../assets/scss/web/common/form_data.scss';
</style>
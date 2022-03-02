<template>
  <div class='assets_create_wrap'>
        <div class="hover_animat custom_bg_color_white" >
            <div class="row_box">
                <h2 class="row_title">图像</h2>
                <div class="clearfix">
                    <div class="img_block_wrap" v-for="(item,index) in imgDataArr" :key="index">
                        <div class="img_block add_img_wrap" v-show="!imgData[item.key]">
                            <div class="add_img_box">
                                <Icon  type="md-add" />
                                <input class="add_img_input" type="file" accept="image/*" @change="addImgFn($event,item.key)">
                            </div>
                        </div>
                        <div  class="img_block" v-show="imgData[item.key]">
                            <img :src="imgData[item.key]" alt="">
                            <div  class="img_block_tools clearfix">
                                <div class="img_block_tools_left">
                                    <Icon type="md-qr-scanner" @click="showImgFn(item.key)"/>
                                </div>
                                <div class="img_block_tools_right">
                                    <Icon type="md-trash"  @click="removeImgFn(item.key)"/>
                                </div>
                            </div>
                        </div>
                        <p>{{item.text}}</p>
                    </div>
                </div>
                <!-- <p class="img_notice"><Icon type="ios-alert" />最多可上传{{imgLimit}}张图片</p> -->
            </div>
        </div>
        <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">新增</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>资产编号<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="createData.assetID" placeholder="资产编号"/>
                                </Col>
                            <Col span="6">
                                <h3>通用名称</h3>
                                <AutoComplete
                                    v-model="createData.generalName"
                                    @on-focus="inputFocusFn('generalName')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="通用名称"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>资产名称</h3>
                                <AutoComplete
                                    v-model="createData.assetName"
                                    @on-focus="inputFocusFn('assetName')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="资产名称"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>存放地点</h3>
                                 <AutoComplete
                                    v-model="createData.location"
                                    @on-focus="inputFocusFn('location')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="存放地点"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                        </Row>
                    </div>

                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>规格</h3>
                                 <AutoComplete
                                    v-model="createData.specification"
                                    @on-focus="inputFocusFn('specification')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="规格"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>型号</h3>
                                 <AutoComplete
                                    v-model="createData.type"
                                    @on-focus="inputFocusFn('type')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="型号"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            
                            <Col span="6">
                                <h3>产地</h3>
                                 <AutoComplete
                                    v-model="createData.placeoforigin"
                                    @on-focus="inputFocusFn('placeoforigin')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="产地"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>品牌</h3>
                                 <AutoComplete
                                    v-model="createData.brand"
                                    @on-focus="inputFocusFn('brand')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="品牌"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                           
                            <Col span="6">
                                <h3>部门名称</h3>
                                <Select v-model="createData.departmentroom" @on-change="departRoomChangeFn">
                                    <Option v-for="item in departCode" :value="item.text" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>部门编码</h3>
                                <Input v-model="createData.departmentcode" placeholder="部门编码" disabled/>
                            </Col>
                            <Col span="6">
                                <h3>大科室名称</h3>
                                <Select v-model="createData.homeofficename" @on-change="homeOfficeNameChangeFn">
                                    <Option v-for="item in homeOfficeNumber" :value="item.text" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>大科室编号</h3>
                                <Input v-model="createData.homeofficenumber" placeholder="大科室编号" disabled/>
                            </Col>
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>所在科室名称</h3>
                               
                                <AutoComplete
                                    v-model="createData.locDept"
                                    @on-focus="inputFocusFn('locDept')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="所在科室名称"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                           
                            <Col span="6">
                                <h3>供应商名称</h3>
                                <AutoComplete
                                    v-model="createData.suppliername"
                                    @on-focus="inputFocusFn('suppliername')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="供应商名称"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>生产厂商名称</h3>
                                <AutoComplete
                                    v-model="createData.generatebusinessname"
                                    @on-focus="inputFocusFn('generatebusinessname')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="生产厂商名称"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>是否进口</h3>
                                <Select v-model="createData.isentrance" >
                                    <Option v-for="item in importData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </col>
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>原值</h3>
                                <InputNumber  :min="0" v-model="createData.money" placeholder="原值" style="width:100%;"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>入库时间</h3>
                                <DatePicker v-model="buyTime" type="date" @on-change="dateBuyFn"  placeholder="入库时间" style="width:100%;"></DatePicker>
                            </Col>

                            <Col span="6">
                                <h3>状态<span class="must_fill_info">（必填）</span></h3>
                                <Select v-model="createData.status">
                                    <Option v-for="item in statusData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </col>
                           
                            <Col span="6">
                                <h3>录入人员<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="createData.recorder" placeholder="录入人员" />
                            </Col>
                           
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                           
                            <Col span="6">
                                <h3>一级分类</h3>
                                <Select v-model="createData.oneclassify" @on-change="fstCatChangeFn">
                                    <Option v-for="item in fstCatData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>二级分类</h3>
                                <Select v-model="createData.secondclassify" @on-change="secCatChangeFn">
                                    <Option v-for="item in secCatList" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>三级分类</h3>
                                <Select v-model="createData.threeclassify" @on-change="thdCatChangeFn">
                                    <Option v-for="item in thdCatList" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>四级分类</h3>
                                <Select v-model="createData.fourclassify">
                                    <Option v-for="item in fouCatList" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                          
                           
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                           
                            <Col span="6">
                                <h3>标签mac</h3>
                                <Input v-model="createData.mac" placeholder="格式:ac233fa031fa" />
                            </Col>
                          
                            <Col span="6">
                                <h3>数量</h3>
                                <InputNumber  :min="1" v-model="createData.amount" placeholder="数量" style="width:100%;"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>单位</h3>
                                <AutoComplete
                                    v-model="createData.unit"
                                    @on-focus="inputFocusFn('unit')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="单位"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                               
                            </Col>
                            <Col span="6">
                                <h3>处置申请单号</h3>
                                <Input v-model="createData.applyoddnumbers" placeholder="请输入处置申请单号" />
                            </Col>
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                           
                            <Col span="6">
                                <h3>SN</h3>
                                <Input v-model="createData.sn" placeholder="请输入SN号" />
                            </Col>
                            <Col span="6">
                                <h3>建筑<span class="must_fill_info">（若mac存在则必填）</span></h3>
                                <!-- <Input v-model="createData.architecture" placeholder="请输入建筑楼名" /> -->
                                <Select v-model="createData.architecture">
                                    <Option v-for="item in architectureData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>院区<span class="must_fill_info">（若mac存在则必填）</span></h3>
                                <!-- <Input v-model="createData.academy" placeholder="请输入院区" /> -->
                                <Select v-model="createData.academy">
                                    <Option v-for="item in academyData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                        </Row>
                    </div>
                    
                    <div class="form_item">
                        <Row>
                            <Col>
                                <h3>备注</h3>
                                <Input v-model="createData.remark" type="textarea" placeholder="输入备注信息..." />
                            </Col>
                        </Row>
                    </div>
                   
                </div>
                <div class="submit_btn_wrap">
                       <Button shape="circle" type="primary" @click="createFn">保存</Button>
                </div>
            </div>
        </div>

        <div v-show="showImg" class="scale_img_wrap"  @click="hideImgFn">
            <img  :src="showImgData" alt="">
        </div>

        <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
           
            <div style="text-align:center;padding:20px 0">
                <Spin fix v-if="!achieveUpload&&!errorUpload">
                    <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                    <div>正在保存</div>
                </Spin>
                <Spin fix v-if="achieveUpload" style="color:#00ad19">
                    <Icon type="ios-checkmark-circle" size=18 />
                    <div>保存成功</div>
                </Spin>
                <Spin fix v-if="errorUpload" style="color:#f72b2b">
                    <Icon type="ios-close-circle" size=18 />
                    <div>保存失败</div>
                </Spin>

            </div>
           
        </Modal>
        
  </div>
</template>

<script>

import Vue from "vue"
import { Row,Col,Icon,DatePicker,Select,Option,Input,Button,Modal,InputNumber,Spin,AutoComplete} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('DatePicker', DatePicker);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Input',Input);
Vue.component('InputNumber',InputNumber);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
Vue.component('AutoComplete',AutoComplete);

import statusData from "../../../assets/data/status.js"
import importData from "../../../assets/data/importData.js"
import architectureData from "../../../assets/data/architectureData.js"
import academyData from "../../../assets/data/academyData.js"
import {fstCatData,secCatData,thdCatData,fouCatData} from "../../../assets/data/category.js"
import departCode from "../../../assets/data/departCode.js"
import homeOfficeNumber from "../../../assets/data/homeOfficeNumber.js"
import imgDataArr from "../../../assets/data/imgDataArr.js"

import NET_PORT from "../../../api/port.js"
import compressImgFn from "../../../utils/compressImg.js"
import formatScanCodeFn from "../../../utils/formatScanCode.js"

export default {
  name:'assets_create',
  components:{},
  props:{},
  data(){
    return {
        inputData:[],
        inputDataAll:[],

        address:this.$store.state.address,
        showImg:false,
        showImgData:'',
        buyTime:'',
        imgData:{
            image:'',
            leftImage: '',
            allroundImage:'',
            aboveImage:'',
            rightImage:'',
            paperlabelImage:'',
            onecodelableImage:'',
        },
        imgDataArr:imgDataArr,
        createData:{
            assetID: "ZCKP2000000000",
            generalName: "",
            assetName: "",
            location:"",
            specification:"",
            type: "",
            placeoforigin:"",
            brand: "",
            departmentcode:"",
            departmentroom:"",
            homeofficenumber:"",
            homeofficename:"",
            isentrance:"",
            suppliername:"",
            generatebusinessname:"",
            money: 0,
            buyDate:"",
            locDept:"",
            status: "",
            recorder:this.$store.state.user,
            mac: "",
            amount: 1,
            unit: "",
            applyoddnumbers:"",
            oneclassify:"",
            secondclassify:"",
            threeclassify:"",
            fourclassify:"",
            sn:"",
            architecture:"",
            academy:"",
            remark:"",
        },
      
        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,
        
        fstCatData:fstCatData,
        secCatData:secCatData,
        thdCatData:thdCatData,
        fouCatData:fouCatData,
        secCatList:[],
        thdCatList:[],
        fouCatList:[],
        departCode:departCode,
        homeOfficeNumber:homeOfficeNumber,
        statusData:statusData,
        importData:importData,
        architectureData:architectureData,
        academyData:academyData,
    }
  },
  watch:{},
  computed:{},
  methods:{
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

        fstCatChangeFn(){
            this.secCatList=this.secCatData.filter((item,index)=>{
                return item.parent===this.createData.oneclassify;
            })
            this.createData.secondclassify='';
            this.createData.threeclassify='';
            this.createData.fourclassify='';
        },
        secCatChangeFn(){
            this.thdCatList=this.thdCatData.filter((item,index)=>{
                return item.parent===this.createData.secondclassify;
            })
            this.createData.threeclassify='';
            this.createData.fourclassify='';
        },
        thdCatChangeFn(){
            this.fouCatList=this.fouCatData.filter((item,index)=>{
                return item.parent===this.createData.threeclassify;
            })
            this.createData.fourclassify='';
        },

        departRoomChangeFn(value){
            this.departCode.forEach((item,index)=>{
                if(item.text===value){
                    this.createData.departmentcode=item.key;
                }
            })
        },
        homeOfficeNameChangeFn(value){
            this.homeOfficeNumber.forEach((item,index)=>{
                if(item.text===value){
                    this.createData.homeofficenumber=item.key;
                }
            })
        },

        addImgFn(e,type){
            if(e.target.files.length){
                let imgObj=e.target.files[0];
                let reader = new FileReader();
                reader.readAsDataURL(imgObj);
                reader.onload = (e)=>{
                    compressImgFn(e.target.result,600,.9).then((val)=>{
                        this.imgData[type]=val;
                    });
                }
            }
            
        },

        showImgFn(type){
            this.showImgData=this.imgData[type];
            this.showImg=true;
        },

        hideImgFn(){
            this.showImg=false;
            this.showImgData='';
        },

        removeImgFn(type){
            this.imgData[type]=''
        },

        dateBuyFn(value){
            this.createData.buyDate=value
        },


        initData(){
             this.createData={
                assetID: "ZCKP2000000000",
                generalName: "",
                assetName: "",
                location:"",
                specification:"",
                type: "",
                placeoforigin:"",
                brand: "",
                departmentcode:"",
                departmentroom:"",
                homeofficenumber:"",
                homeofficename:"",
                isentrance:"",
                suppliername:"",
                generatebusinessname:"",
                money: 0,
                buyDate:"",
                locDept:"",
                status: "",
                recorder:this.$store.state.user,
                mac: "",
                amount: 1,
                unit: "",
                applyoddnumbers:"",
                oneclassify:"",
                secondclassify:"",
                threeclassify:"",
                fourclassify:"",
                sn:"",
                architecture:"",
                academy:"",

                remark:"",
            },

            this.buyTime='';

            this.showImgData=''

            this.imgData={
                image:'',
                leftImage: '',
                allroundImage:'',
                aboveImage:'',
                rightImage:'',
                paperlabelImage:'',
                onecodelableImage:'',
            }
            
        },

        successFn(){
            this.achieveUpload=true;
            setTimeout(()=>{
                this.uploadModal=false;
                //成功后清除掉填写的数据
                this.initData();
                setTimeout(()=>{
                    this.achieveUpload=false;
                    this.errorUpload=false;
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


        createFn(){
            if(!this.createData.status){
                this.createData.status=''
            }

            let assetIDReg=/[\u4E00-\u9FA5]/g;
            if(assetIDReg.test(this.createData.assetID.trim())){
                this.$Message.error({
                    content:"资产编号不能含有汉字!",
                    duration:2,
                })
                return
            }

            if(this.createData.assetID.trim()&&this.createData.status.trim()&&this.createData.recorder.trim()){
                if(this.createData.mac.trim()&&!this.createData.architecture.trim()){
                    this.$Message.error({
                        content:"若mac存在则建筑和院区必填!",
                        duration:2,
                    })
                }else if(this.createData.mac.trim()&&!this.createData.academy.trim()){
                    this.$Message.error({
                        content:"若mac存在则建筑和院区必填!",
                        duration:2,
                    })
                }else{
                        //显示出正在上传
                        this.uploadModal=true;

                        //拿到图片数据
                        let imgDatas = {
                            "assetID":this.createData.assetID.trim(),
                            "image":this.imgData.image? this.imgData.image : '',
                            "leftImage":this.imgData.leftImage? this.imgData.leftImage : '',
                            "allroundImage":this.imgData.allroundImage? this.imgData.allroundImage : '',
                            "aboveImage":this.imgData.aboveImage? this.imgData.aboveImage : '',
                            "rightImage":this.imgData.rightImage? this.imgData.rightImage : '',
                            "paperlabelImage":this.imgData.paperlabelImage? this.imgData.paperlabelImage : '',
                            "onecodelableImage":this.imgData.onecodelableImage? this.imgData.onecodelableImage : '',
                        }
                       

                        let normalData={
                            
                                assetID: this.createData.assetID.trim(),
                                generalName: this.createData.generalName.trim(),
                                assetName: this.createData.assetName.trim(),
                                location:this.createData.location.trim(),
                                specification:this.createData.specification.trim(),
                                type: this.createData.type.trim(),
                                placeoforigin:this.createData.placeoforigin.trim(),
                                brand: this.createData.brand.trim(),
                                departmentcode:this.createData.departmentcode,
                                departmentroom:this.createData.departmentroom,
                                homeofficenumber:this.createData.homeofficenumber,
                                homeofficename:this.createData.homeofficename,
                                isentrance:this.createData.isentrance.trim(),
                                suppliername:this.createData.suppliername.trim(),
                                generatebusinessname:this.createData.generatebusinessname.trim(),
                                money: Math.abs(this.createData.money),
                                buyDate:this.createData.buyDate,
                                locDept:this.createData.locDept.trim(),
                                status: this.createData.status.trim(),
                                recorder:this.createData.recorder.trim(),
                                mac:formatScanCodeFn(this.createData.mac.trim()),
                                amount: Math.abs(this.createData.amount),
                                unit: this.createData.unit,
                                applyoddnumbers:this.createData.applyoddnumbers.trim(),
                                oneclassify:this.createData.oneclassify,
                                secondclassify:this.createData.secondclassify,
                                threeclassify:this.createData.threeclassify,
                                fourclassify:this.createData.fourclassify,
                                sn:this.createData.sn.trim(),
                                architecture:this.createData.architecture.trim(),
                                academy:this.createData.academy.trim(),
                                remark:this.createData.remark.trim(),
                                address:this.address.trim(),
                        }

                     
                        let datas = [normalData];

                        this.$axios.post(NET_PORT.imgUpload,imgDatas)
                        .then((res)=>{

                            if(res.data.msg==='ok'){
                                
                                this.$axios.post(NET_PORT.assetsCreate,datas)
                                .then((response)=>{
                                    if(response.data.msg==='ok'){
                                    this.successFn();
                                    }else if(response.data.message){
                                        this.errorFn();
                                        Modal.error({
                                            title: '错误提示！',
                                            content: response.data.message,
                                            okText: '确定',
                                        })
                                    }else if(response.data.msg==='failed'){
                                        this.errorFn();
                                        Modal.error({
                                            title: '错误提示！',
                                            content: response.data.message,
                                            okText: '确定',
                                        })
                                    }
                                })
                                .catch((error)=>{
                                })

                            }else if(res.data.msg==='failed'){

                            this.errorFn();
                            }
                        })
                        .catch((err)=>{
                            this.$Message.error({
                                content:"图片保存失败!",
                                duration:2,
                            })
                        })
                }

            }else{
                this.$Message.error({
                    content:"请补充完整信息!",
                    duration:2,
                })
            }

        }
  },
  created(){},
  mounted(){
     
  }
}
</script>



<style lang='scss' scoped>
@import '../../../assets/scss/web/common/add_img.scss';
@import '../../../assets/scss/web/common/form_data.scss';

</style>
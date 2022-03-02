<template>
  <div class='assets_detail_wrap'>
        <div class="row_box hover_animat custom_bg_color_white">
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

        <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">明细</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>资产编号<span class="must_fill_info">（必填）</span></h3>
                                <Input disabled v-model="editData.assetID" placeholder="资产编号"/>
                            </Col>
                            <Col span="6">
                                <h3>通用名称</h3>
                                 <AutoComplete
                                    v-model="editData.generalName"
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
                                    v-model="editData.assetName"
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
                                    v-model="editData.location"
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
                                    v-model="editData.specification"
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
                                    v-model="editData.type"
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
                                    v-model="editData.placeoforigin"
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
                                    v-model="editData.brand"
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
                                <Select v-model="editData.departmentroom" @on-change="departRoomChangeFn">
                                    <Option v-for="item in departCode" :value="item.text" :key="item.text">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>部门编码</h3>
                                <Input v-model="editData.departmentcode" placeholder="部门编码" disabled/>
                            </Col>
                            <Col span="6">
                                <h3>大科室名称</h3>
                                <Select v-model="editData.homeofficename" @on-change="homeOfficeNameChangeFn">
                                    <Option v-for="item in homeOfficeNumber" :value="item.text" :key="item.text">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>大科室编号</h3>
                                <Input v-model="editData.homeofficenumber" placeholder="大科室编号" disabled/>
                            </Col>
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>所在科室名称</h3>
                               
                                 <AutoComplete
                                    v-model="editData.locDept"
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
                                    v-model="editData.suppliername"
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
                                    v-model="editData.generatebusinessname"
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
                                <Select v-model="editData.isentrance" >
                                    <Option v-for="item in importData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </col>
                           
                           
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>原值</h3>
                                <InputNumber  :min="0" v-model="editData.money" placeholder="原值" style="width:100%;"></InputNumber>
                            </Col>
                           
                            <Col span="6">
                                <h3>入库时间</h3>
                                <DatePicker v-model="buyTime" type="date" @on-change="dateBuyFn"  placeholder="入库时间" style="width:100%;"></DatePicker>
                            </Col>
                           
                            
                            <Col span="6">
                                <h3>状态<span class="must_fill_info">（必填）</span></h3>
                                <Select v-model="editData.status"  :disabled="editData.status!=='正常'&&editData.status!=='待维修'&&editData.status!=='待报废'&&editData.status!=='报废'">
                                    <Option v-for="item in statusData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </col>
                           
                            <Col span="6">
                                <h3>录入人员<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="editData.recorder" placeholder="录入人员" />
                            </Col>
                           
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                           
                            <Col span="6">
                                <h3>一级分类</h3>
                                <Select v-model="editData.oneclassify" @on-change="fstCatChangeFn">
                                    <Option v-for="item in fstCatData" :value="item.text" :key="item.key">{{ item.key }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>二级分类</h3>
                                <Select v-model="editData.secondclassify" @on-change="secCatChangeFn">
                                    <Option v-for="item in secCatList" :value="item.text" :key="item.key">{{ item.key }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>三级分类</h3>
                                <Select v-model="editData.threeclassify" @on-change="thdCatChangeFn">
                                    <Option v-for="item in thdCatList" :value="item.text" :key="item.key">{{ item.key }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>四级分类</h3>
                                <Select v-model="editData.fourclassify">
                                    <Option v-for="item in fouCatList" :value="item.text" :key="item.key">{{ item.key }}</Option>
                                </Select>
                            </Col>
                          
                           
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                           
                            <Col span="6">
                                <h3>标签mac</h3>
                                <Input v-model="editData.mac" placeholder="格式:ac233fa031fa" />
                            </Col>
                          
                            <Col span="6">
                                <h3>数量</h3>
                                <InputNumber  :min="1" v-model="editData.amount" placeholder="数量" style="width:100%;"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>单位</h3>
                               
                                 <AutoComplete
                                    v-model="editData.unit"
                                    @on-focus="inputFocusFn('unit')"
                                    @on-blur="inputBlurFn"
                                    @on-search="handleSearchFn"
                                    placeholder="单位"
                                    style="width:100%">
                                    <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>处置单号</h3>
                                <Input v-model="editData.applyoddnumbers" placeholder="请输入处置单号" />
                            </Col>
                        </Row>
                    </div>

                    <div class="form_item">
                         <Row :gutter="20">
                            <Col span="6">
                                <h3>SN</h3>
                                <Input v-model="editData.sn" placeholder="请输入SN号" />
                            </Col>
                            <Col span="6">
                                <h3>建筑<span class="must_fill_info">（若mac存在则必填）</span></h3>
                                <!-- <Input v-model="editData.architecture" placeholder="请输入建筑楼名" /> -->
                                <Select v-model="editData.architecture">
                                    <Option v-for="item in architectureData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>院区<span class="must_fill_info">（若mac存在则必填）</span></h3>
                                <!-- <Input v-model="editData.academy" placeholder="请输入院区" /> -->
                                <Select v-model="editData.academy">
                                    <Option v-for="item in academyData" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                        </Row>
                    </div>

                    <div class="form_item">
                        <Row>
                            <Col>
                                <h3>备注</h3>
                                <Input v-model="editData.remark" type="textarea" placeholder="输入备注信息..." />
                            </Col>
                        </Row>
                    </div>

                </div>
                <div class="submit_btn_wrap">
                       <Button type="primary" shape="circle" @click="saveFn">保存</Button>
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
                    <div>正在更新</div>
                </Spin>
                <Spin fix v-if="achieveUpload" style="color:#00ad19">
                    <Icon type="ios-checkmark-circle" size=18 />
                    <div>更新成功</div>
                </Spin>
                <Spin fix v-if="errorUpload" style="color:#f72b2b">
                    <Icon type="ios-close-circle" size=18 />
                    <div>更新失败</div>
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
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('InputNumber',InputNumber);
Vue.component('Spin',Spin);
Vue.component('AutoComplete',AutoComplete);

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
  name:'assets_detail',
  components:{},
  props:{},
  data(){
    return {
        assetID:'',
        inputData:[],
        inputDataAll:[],
        isFresh:false,
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

        editData:{
            assetID: "",
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

        statusData:[],
        fstCatData:fstCatData,
        secCatData:secCatData,
        thdCatData:thdCatData,
        fouCatData:fouCatData,
        secCatList:[],
        thdCatList:[],
        fouCatList:[],
        departCode:departCode,
        homeOfficeNumber:homeOfficeNumber,
        importData:importData,
        architectureData:architectureData,
        academyData:academyData,
    }
  },
  watch:{
  
  },
  computed:{},
  methods:{
        checkStatusFn(){
            if(this.editData.status!=='维修接单'&&this.editData.status!=='外借'){
                this.statusData=[
                    { text: '正常', key: '正常' },
                    { text: '待维修', key: '待维修' },
                    { text: '待报废', key: '待报废' },
                    { text: '报废', key: '报废' },
                ];
            }else{
                this.statusData=[
                    { text: '维修接单', key: '维修接单' },
                    { text: '外借', key: '外借' },
                ];
            }
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

        fstCatChangeFn(){
            this.secCatList=this.secCatData.filter((item,index)=>{
                return item.parent===this.editData.oneclassify;
            })
            this.editData.secondclassify='';
            this.editData.threeclassify='';
            this.editData.fourclassify='';
        },
        secCatChangeFn(){
            this.thdCatList=this.thdCatData.filter((item,index)=>{
                return item.parent===this.editData.secondclassify;
            })
            this.editData.threeclassify='';
            this.editData.fourclassify='';
        },
        thdCatChangeFn(){
            this.fouCatList=this.fouCatData.filter((item,index)=>{
                return item.parent===this.editData.threeclassify;
            })
            this.editData.fourclassify='';
        },

        initCatFn(){
            this.secCatList=this.secCatData.filter((item,index)=>{
                return item.parent===this.editData.oneclassify;
            })
            this.thdCatList=this.thdCatData.filter((item,index)=>{
                return item.parent===this.editData.secondclassify;
            })

            this.fouCatList=this.fouCatData.filter((item,index)=>{
                return item.parent===this.editData.threeclassify;
            })
        },

        departRoomChangeFn(value){
            this.departCode.forEach((item,index)=>{
                if(item.text===value){
                    this.editData.departmentcode=item.key;
                }
            })
        },
        homeOfficeNameChangeFn(value){
            this.homeOfficeNumber.forEach((item,index)=>{
                if(item.text===value){
                    this.editData.homeofficenumber=item.key;
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
            this.editData.buyDate=value
        },


        successFn(){
            this.achieveUpload=true;
            setTimeout(()=>{
                this.uploadModal=false;
                setTimeout(()=>{
                    this.achieveUpload=false;
                    this.errorUpload=false;
                    this.isFresh=true;
                    //回到上一页
                    this.$router.go(-1)
                    // this.$router.push('/web/assets_manage')
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


        saveFn(){
            
            if(this.editData.assetID.trim()&&this.editData.status.trim()&&this.editData.recorder.trim()){
                if(this.editData.mac.trim()&&!this.editData.architecture.trim()){
                    this.$Message.error({
                        content:"若mac存在则建筑和院区必填!",
                        duration:2,
                    })
                }else if(this.editData.mac.trim()&&!this.editData.academy.trim()){
                    this.$Message.error({
                        content:"若mac存在则建筑和院区必填!",
                        duration:2,
                    })
                }else{
                        //显示出正在上传
                        this.uploadModal=true;
                        
                        let imgDatas = {
                            "assetID":this.editData.assetID.trim(),
                            "image":this.imgData.image? this.imgData.image : '',
                            "leftImage":this.imgData.leftImage? this.imgData.leftImage : '',
                            "allroundImage":this.imgData.allroundImage? this.imgData.allroundImage : '',
                            "aboveImage":this.imgData.aboveImage? this.imgData.aboveImage : '',
                            "rightImage":this.imgData.rightImage? this.imgData.rightImage : '',
                            "paperlabelImage":this.imgData.paperlabelImage? this.imgData.paperlabelImage : '',
                            "onecodelableImage":this.imgData.onecodelableImage? this.imgData.onecodelableImage : '',
                        };


                       

                        let normalData={
                                assetID: this.editData.assetID.trim(),
                                generalName: this.editData.generalName.trim(),
                                assetName: this.editData.assetName.trim(),
                                location:this.editData.location.trim(),
                                specification:this.editData.specification.trim(),
                                type: this.editData.type.trim(),
                                placeoforigin:this.editData.placeoforigin.trim(),
                                brand: this.editData.brand.trim(),
                                departmentcode:this.editData.departmentcode,
                                departmentroom:this.editData.departmentroom,
                                homeofficenumber:this.editData.homeofficenumber,
                                homeofficename:this.editData.homeofficename,
                                isentrance:this.editData.isentrance.trim(),
                                suppliername:this.editData.suppliername.trim(),
                                generatebusinessname:this.editData.generatebusinessname.trim(),
                                money: Math.abs(this.editData.money),
                                buyDate:this.editData.buyDate,
                                locDept:this.editData.locDept.trim(),
                                status: this.editData.status.trim(),
                                recorder:this.editData.recorder.trim(),
                                mac: formatScanCodeFn(this.editData.mac.trim()),
                                amount: Math.abs(this.editData.amount),
                                unit: this.editData.unit,
                                applyoddnumbers:this.editData.applyoddnumbers.trim(),
                                oneclassify:this.editData.oneclassify,
                                secondclassify:this.editData.secondclassify,
                                threeclassify:this.editData.threeclassify,
                                fourclassify:this.editData.fourclassify,
                                sn:this.editData.sn.trim(),
                                architecture:this.editData.architecture.trim(),
                                academy:this.editData.academy.trim(),
                                remark:this.editData.remark.trim(),
                        }


                        let datas = [normalData];

                        this.$axios.post(NET_PORT.imgUpload,imgDatas) 
                        .then((res)=>{
                            if(res.data.msg==='ok'){
                                this.$axios.post(NET_PORT.assetsUpdate,datas) 
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
                                content:"图片更新失败!",
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

        },

        getDataFn(){

            this.$axios.post(NET_PORT.assetsQuery+'?currentPage=1'+'&assetID='+this.assetID)
            .then((res)=>{
                if(res.data[0].assetID){
                    let data=res.data[0];
                    let normalData={
                        assetID: data.assetID,
                        generalName: data.generalName? data.generalName : '',
                        assetName: data.assetName ? data.assetName : '',
                        location:data.location ? data.location : '',
                        specification:data.specification ? data.specification : '',
                        type: data.type ? data.type : '',
                        placeoforigin:data.placeoforigin ? data.placeoforigin : '',
                        brand: data.brand ? data.brand : '',
                        departmentcode:data.departmentcode ? data.departmentcode :'',
                        departmentroom:data.departmentroom ? data.departmentroom : '',
                        homeofficenumber:data.homeofficenumber ? data.homeofficenumber : '',
                        homeofficename:data.homeofficename ? data.homeofficename : '',
                        isentrance:data.isentrance ? data.isentrance : '',
                        suppliername:data.suppliername ? data.suppliername : '',
                        generatebusinessname:data.generatebusinessname ? data.generatebusinessname : '',
                        money: data.money ? Number(data.money) :0,
                        buyDate:data.buyDate ? data.buyDate : '',
                        locDept:data.locDept ? data.locDept : '',
                        status: data.status ? data.status : '',
                        recorder:data.recorder?data.recorder : this.$store.state.user,
                        mac: data.mac ? data.mac : '',
                        amount: data.amount ? Number(data.amount) : 1,
                        unit: data.unit ? data.unit : '',
                        applyoddnumbers:data.applyoddnumbers? data.applyoddnumbers : '',
                        oneclassify:data.oneclassify ? data.oneclassify : '',
                        secondclassify:data.secondclassify ? data.secondclassify : '',
                        threeclassify:data.threeclassify ? data.threeclassify : '',
                        fourclassify:data.fourclassify ? data.fourclassify : '',
                        sn:data.sn? data.sn : '',
                        architecture:data.architecture? data.architecture : '',
                        academy:data.academy? data.academy : '',
                        remark:data.remark ? data.remark : '',
                    }

                    this.buyTime=normalData.buyDate;

                    this.editData=normalData;

                    //根据状态设置状态下拉选项
                    this.checkStatusFn();

                    //初始化分类数据
                    this.initCatFn();

                    this.getImgFn('imgShow')
                    this.getImgFn('imgShowLeft')
                    this.getImgFn('imgShowAllround')
                    this.getImgFn('imgShowAbove')
                    this.getImgFn('imgShowRight')
                    this.getImgFn('imgShowPaperlabel')
                    this.getImgFn('imgShowOnecodelable')

                }else{
                    this.$Modal.error({
                        title: '提示！',
                        content: '未找到资产数据！',
                    })
                }
            })

        },

        getImgFn(key){
                this.$axios.get(NET_PORT[key]+this.editData.assetID,{responseType: 'arraybuffer'})
                .then((res)=>{
                    let result='';
                    result= 'data:image/png;base64,' + btoa(
                        new Uint8Array(res.data).reduce((data, byte) => data + String.fromCharCode(byte), '')
                    )

                    if(result==='data:image/png;base64,'){


                    }else{
                        // this.imgList.push(result)

                        switch(key){
                            case "imgShow" :
                                this.imgData.image=result;
                            break;
                            case "imgShowLeft" :
                                this.imgData.leftImage=result;
                            break;
                            case "imgShowAllround" :
                                this.imgData.allroundImage=result;
                            break;
                            case "imgShowAbove" :
                                this.imgData.aboveImage=result;
                            break;
                            case "imgShowRight" :
                                this.imgData.rightImage=result;
                            break;
                            case "imgShowPaperlabel" :
                                this.imgData.paperlabelImage=result;
                            break;
                            case "imgShowOnecodelable" :
                                this.imgData.onecodelableImage=result;
                            break;
                        }

                    }
                    
                })
                .catch((err)=>{
                // console.log(err)
                })
        }


    },

    beforeRouteEnter(to, from, next) {
        //每次进来先重置needFresh为false
        to.meta.needFresh=false,
        next();
    },

    beforeRouteLeave(to, from, next) {

        //判断是否是更新了内容，如果是则返回需要刷新资产管理页面
        if(this.isFresh){
            from.meta.needFresh=true;
        }
        
        next();
    },

    created(){
        this.assetID=this.$route.params.id;
        if(!this.assetID){
            this.$Message.error({
                content:"未找到资产信息!",
                duration:2,
            })

            setTimeout(()=>{
                this.$router.go(-1)
            },2000)
        }

    },
    mounted(){
        this.getDataFn();
    },
   
}
</script>



<style lang='scss' scoped>
    @import '../../../assets/scss/web/common/add_img.scss';
    @import '../../../assets/scss/web/common/form_data.scss';
</style>
<template>
  <div class='mobile_assets_modify_wrap'>
       <div class="assets_modify_box assets_upload_box">

            <div class="cell_item cell_item_img_box">
                <h5>图片</h5>
                <div class="cell_item_box">
                    <div class="upload_img_wrap">
                        <div class="upload_img_box" v-for="(item,index) in imgDataArr" :key="index">
                            <div class="img_item_box" v-show="!imgData[item.key]">
                                <div class="upload_button_wrap">
                                    <div class="add_img_icon">
                                        <Icon name="plus" />
                                    </div>
                                    <input class="add_img_input" type="file" accept="image/*" multiple="multiple" @change="addImgFn($event,item.key)">
                                </div>
                            </div>

                            <div class="img_item_box" v-show="imgData[item.key]"  :style="imgCom(imgData[item.key])"  @click="imgPreviewFn(item.key)">
                                <Icon class="img_delete_icon" name="clear" @click="imgDeleteFn($event,item.key)"/>
                            </div>
                            <p>{{item.text}}</p>
                        </div>
                    </div>
                </div>
            </div>

           <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable label="资产编号" disabled  required v-model="createData.assetID" placeholder="请输入资产编号"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field label="资产名称" center v-model="createData.assetName" placeholder="请输入资产名称">
                        <template #button>
                            <Button size="small" type="primary" @click="showInputSearchFn('assetName')">云数据</Button>
                        </template>
                    </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field label="通用名称" center v-model="createData.generalName" placeholder="请输入通用名称">
                        <template #button>
                            <Button size="small" type="primary" @click="showInputSearchFn('generalName')">云数据</Button>
                        </template>
                    </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field label='存放地点' center v-model="createData.location" placeholder="请输入存放地点">
                       <template #button>
                            <Button size="small" type="primary" @click="showInputSearchFn('location')">云数据</Button>
                        </template>
                    </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field label="规格" center v-model="createData.specification" placeholder="请输入规格">
                            <template #button>
                            <Button size="small" type="primary" @click="showInputSearchFn('specification')">云数据</Button>
                        </template>
                    </Field>
                </div>
            </div>
            
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field label="型号" center v-model="createData.type" placeholder="请输入型号">
                        <template #button>
                            <Button size="small" type="primary" @click="showInputSearchFn('type')">云数据</Button>
                        </template>
                    </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field label="产地" center v-model="createData.placeoforigin" placeholder="请输入产地">
                        <template #button>
                            <Button size="small" type="primary" @click="showInputSearchFn('placeoforigin')">云数据</Button>
                        </template>
                    </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field label="品牌" center v-model="createData.brand" placeholder="请输入品牌">
                        <template #button>
                            <Button size="small" type="primary" @click="showInputSearchFn('brand')">云数据</Button>
                        </template>
                    </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <div @click="departCodeShow=true;">
                        <Field label="部门名称" readonly v-model="createData.departmentroom" placeholder="请选择部门名称"/>
                    </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field disabled label="部门编码" readonly  v-model="createData.departmentcode" placeholder="请选择部门编码"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <div @click="homeOfficeNumberShow=true">
                        <Field label="大科室名称" readonly v-model="createData.homeofficename" placeholder="请选择大科室名称"/>
                    </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field disabled label="大科室编码" readonly  v-model="createData.homeofficenumber" placeholder="请选择大科室编码"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                        <Field label="所在科室名称" center v-model="createData.locDept" placeholder="请输入所在科室名称">
                                <template #button>
                                <Button size="small" type="primary" @click="showInputSearchFn('locDept')">云数据</Button>
                            </template>
                        </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                        <Field label="供应商名称" center v-model="createData.suppliername" placeholder="请输入供应商名称">
                            <template #button>
                                <Button size="small" type="primary" @click="showInputSearchFn('suppliername')">云数据</Button>
                            </template>
                        </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                        <Field label="生产厂商名称" center v-model="createData.generatebusinessname" placeholder="请输入生产厂商名称">
                            <template #button>
                                <Button size="small" type="primary" @click="showInputSearchFn('generatebusinessname')">云数据</Button>
                            </template>
                        </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                        <div @click="isEntranceShow=true">
                            <Field label="是否进口" v-model="createData.isentrance" readonly placeholder="请选择是否进口"/>
                        </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <Field label='原值' v-model="createData.money" type="number"  placeholder="请输入原值"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="buyTimeDialogShow=true">
                      <Field label='入库时间'  v-model="createData.buyDate" readonly placeholder="请选择入库时间"></Field>
                  </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <div @click="statusDialogFn">
                        <Field label="状态" required v-model="createData.status" readonly placeholder="请选择状态"></Field>
                    </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <Field label="录入人员" required v-model="createData.recorder" placeholder="请输入录入人员"/>
                </div>
            </div>
        
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="fstCatShow=true">
                      <Field label='一级分类'  v-model="createData.oneclassify" readonly placeholder="请选择一级分类"></Field>
                  </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="secCatShow=true">
                      <Field label='二级分类'  v-model="createData.secondclassify" readonly placeholder="请选择二级分类"></Field>
                  </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="thdCatShow=true">
                      <Field label='三级分类'  v-model="createData.threeclassify" readonly placeholder="请选择三级分类"></Field>
                  </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="fouCatShow=true">
                      <Field label='四级分类'  v-model="createData.fourclassify" readonly placeholder="请选择四级分类"></Field>
                  </div>
                </div>
            </div>
            
            <div class="cell_item">
                <div class="cell_item_box">
                  <Field label='数量' v-model="createData.amount" type="number" placeholder="请输入数量"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                        <Field label='单位' center v-model="createData.unit"  placeholder="请选择单位">
                            <template #button>
                                <Button size="small" type="primary" @click="showInputSearchFn('unit')">云数据</Button>
                            </template>
                        </Field>
                </div>
            </div>

            <div class="cell_item">
                <div class="cell_item_box">
                  <Field label="处置申请单号"  v-model="createData.applyoddnumbers" placeholder="请输入处置申请单号"/>
                </div>
            </div>

            <div class="cell_item">
                <div class="cell_item_box">
                        <Field label="标签mac" v-model="createData.mac"  placeholder="格式:ac233fa031fa"/>
                </div>
            </div>

            <div class="cell_item">
                <div class="cell_item_box">
                        <Field label="SN" v-model="createData.sn"  placeholder="请输入SN号"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                        <!-- <Field label="建筑（若mac存在则必填）" required v-model="createData.architecture"  placeholder="请输入建筑名"/> -->
                        <div @click="architectureDialogShow=true">
                            <Field label="建筑（若mac存在则必填）" required readonly v-model="createData.architecture"  placeholder="请选择建筑"/>
                        </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                        <!-- <Field label="院区（若mac存在则必填）" required v-model="createData.academy"  placeholder="请输入院区"/> -->
                        <div @click="academyDialogShow=true">
                            <Field label="院区（若mac存在则必填）" required readonly v-model="createData.academy"  placeholder="请选择院区"/>
                        </div>
                </div>
            </div>

             <div class="cell_item">
                <div class="cell_item_box">
                  <Field label="备注信息" v-model="createData.remark" type="textarea" placeholder="请输入备注信息"/>
                </div>
            </div>

            <Popup v-model="showInputSearch" position="bottom">
                <Search
                    v-model="inputSearchValue"
                    placeholder="请输入搜索关键词"
                    input-align="center"
                    @input="handleSearchFn"
                />
               
                <Picker ref="inputSelect"  show-toolbar toolbar-position="bottom" @cancel="showInputSearch = false" @confirm="certainSelectFn"  :columns="inputData"  title=""  :visible-item-count=5 />
            </Popup>

            <Popup v-model="departCodeShow" position="bottom">
               <Picker  :columns="departCode" show-toolbar title="请选择部门名称" @cancel="departCodeShow = false" @confirm="getDepartCodeFn" :visible-item-count=5 />
            </Popup>
            <Popup v-model="homeOfficeNumberShow" position="bottom">
               <Picker  :columns="homeOfficeNumber" show-toolbar title="请选择大科室名称" @cancel="homeOfficeNumberShow = false" @confirm="getHomeOfficeNumberFn" :visible-item-count=5 />
            </Popup>
           
            <Popup v-model="isEntranceShow" position="bottom">
               <Picker  :columns="importData" show-toolbar title="请选择是否进口" @cancel="isEntranceShow = false" @confirm="getIsEntranceFn" :visible-item-count=5 />
            </Popup>
            <Popup v-model="fstCatShow" position="bottom">
               <Picker  :columns="fstCatData" show-toolbar title="请选择一级分类" @cancel="fstCatShow = false" @confirm="getFstCatFn" :visible-item-count=5 />
            </Popup>
            <Popup v-model="secCatShow" position="bottom">
               <Picker  :columns="secCatList" show-toolbar title="请选择二级分类" @cancel="secCatShow = false" @confirm="getSecCatFn" :visible-item-count=5 />
            </Popup>
            <Popup v-model="thdCatShow" position="bottom">
               <Picker  :columns="thdCatList" show-toolbar title="请选择三级分类" @cancel="thdCatShow = false" @confirm="getThdCatFn" :visible-item-count=5 />
            </Popup>
            <Popup v-model="fouCatShow" position="bottom">
               <Picker  :columns="fouCatList" show-toolbar title="请选择四级分类" @cancel="fouCatShow = false" @confirm="getFouCatFn" :visible-item-count=5 />
            </Popup>
           
            <Popup v-model="statusDialogShow" position="bottom">
               <Picker  :columns="statusData" show-toolbar title="请选择状态" @cancel="statusDialogShow = false" @confirm="getStatusFn" :visible-item-count=5 />
            </Popup>
            <Popup v-model="architectureDialogShow" position="bottom">
               <Picker  :columns="architectureData" show-toolbar title="请选择建筑" @cancel="architectureDialogShow = false" @confirm="getArchitectureFn" :visible-item-count=5 />
            </Popup>
            <Popup v-model="academyDialogShow" position="bottom">
               <Picker  :columns="academyData" show-toolbar title="请选择院区" @cancel="academyDialogShow = false" @confirm="getAcademyFn" :visible-item-count=5 />
            </Popup>

            <Popup v-model="buyTimeDialogShow" position="bottom">
                <DatetimePicker
                v-model="currentDate"
                type="date"
                show-toolbar
                title="请选择入库时间" 
                :min-date="minDate"
                :max-date="maxDate"
                @cancel="buyTimeDialogShow = false" 
                @confirm="getDateFn"
                />
            </Popup>

            <div class="tools_wrap">
              <Button round class="tools_button" type="primary" @click="saveFn">保存</Button>
            </div>
        </div>
  </div>
</template>

<script>

import {Button,Notify,Tag,Icon,Field,Dialog,Toast,Picker,DatetimePicker,Popup,Loading,Search,ImagePreview} from 'vant';
import 'vant/lib/button/style';
import 'vant/lib/notify/style';
import 'vant/lib/tag/style';
import 'vant/lib/icon/style';
import 'vant/lib/field/style';
import 'vant/lib/dialog/style';
import 'vant/lib/picker/style';
import 'vant/lib/datetime-picker/style';
import 'vant/lib/popup/style';
import 'vant/lib/toast/style';
import 'vant/lib/loading/style';
import 'vant/lib/search/style';
import 'vant/lib/image-preview/style';
import NET_PORT from "../../../api/port.js"
import importData from "../../../assets/data/importData.js"
import architectureData from "../../../assets/data/architectureData.js"
import academyData from "../../../assets/data/academyData.js"
import departCode from "../../../assets/data/departCode.js"
import homeOfficeNumber from "../../../assets/data/homeOfficeNumber.js"
import imgDataArr from "../../../assets/data/imgDataArr.js"
import {fstCatData,secCatData,thdCatData,fouCatData} from "../../../assets/data/category.js"

import formatScanCodeFn from "../../../utils/formatScanCode.js"
import compressImgFn from "../../../utils/compressImg.js"
export default {
  name:'mobile_assets_modify',
  components:{
    Button,
    Tag,
    Icon,
    Field,
    Dialog,
    Picker,
    DatetimePicker,
    Popup,
    Toast,
    Loading,
    Search
  },
  props:{},
  data(){
    return {
        assetID:'',
        inputData:[],
        inputDataAll:[],
        inputSearchValue:'',
        activeKey:'',
        showInputSearch:false,

        departCodeShow:false,
        homeOfficeNumberShow:false,
        isEntranceShow:false,
        fstCatShow:false,
        secCatShow:false,
        thdCatShow:false,
        fouCatShow:false,
        fstCatData:fstCatData,
        secCatData:secCatData,
        thdCatData:thdCatData,
        fouCatData:fouCatData,
        secCatList:[],
        thdCatList:[],
        fouCatList:[],
        departCode:departCode,
        homeOfficeNumber:homeOfficeNumber,

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

        minDate: new Date(2000, 0, 1),
        maxDate: new Date(2050, 12, 0),
        "address":this.$store.state.address,
        buyTimeDialogShow:false,
        statusDialogShow:false,
        architectureDialogShow:false,
        academyDialogShow:false,


      currentDate: new Date(),
      createData:{
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

        statusData:[],

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
        if(this.createData.status!=='维修接单'&&this.createData.status!=='外借'){
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

    statusDialogFn(){
        if(this.createData.status!=='正常'&&this.createData.status!=='待维修'&&this.createData.status!=='待报废'&&this.createData.status!=='报废'){

        }else{
            this.statusDialogShow=true
        }
        
    },

    imgDeleteFn(e,type){
        e.stopPropagation();
        this.imgData[type]=''
    },
    imgPreviewFn(type){
        ImagePreview({
            images:[this.imgData[type]],
        });
    },
    imgCom(src){
        return 'background-image:url('+src+')';
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

    certainSelectFn(val){
        this.createData[this.activeKey]=val;
        this.showInputSearch=false;
        this.inputSearchValue='';
        this.activeKey='';
    },

    inputSelectChangeFn(picker, val, index){
        this.createData[this.activeKey]=val;
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
       
      

    getDepartCodeFn(obj) {
        this.createData.departmentcode=obj.key;
        this.createData.departmentroom=obj.text;
        this.departCodeShow=false;
    },
    getHomeOfficeNumberFn(obj) {
        this.createData.homeofficenumber=obj.key;
        this.createData.homeofficename=obj.text;
        this.homeOfficeNumberShow=false;
    },

    getIsEntranceFn(obj) {
        this.createData.isentrance=obj.text;
        this.isEntranceShow=false;
    },

    getFstCatFn(obj) {
        this.createData.oneclassify=obj.text;
        this.secCatList=this.secCatData.filter((item,index)=>{
            return item.parent===this.createData.oneclassify;
        })
        this.createData.secondclassify='';
        this.createData.threeclassify='';
        this.createData.fourclassify='';
        this.fstCatShow=false;
    },
    getSecCatFn(obj) {
        this.createData.secondclassify=obj.text;
        this.thdCatList=this.thdCatData.filter((item,index)=>{
            return item.parent===this.createData.secondclassify;
        })
        this.createData.threeclassify='';
        this.createData.fourclassify='';
        this.secCatShow=false;
    },
    getThdCatFn(obj) {
        this.createData.threeclassify=obj.text;
        this.fouCatList=this.fouCatData.filter((item,index)=>{
            return item.parent===this.createData.threeclassify;
        })
        this.createData.fourclassify='';
        this.thdCatShow=false;
    },
    getFouCatFn(obj) {
        this.createData.fourclassify=obj.text;
        this.fouCatShow=false;
    },

    getDateFn(value){
        var date=value
        var currentYear=date.getFullYear();
        var currentMonth=date.getMonth()+1;
        var currentDate=date.getDate();
        currentMonth= currentMonth<10 ? '0'+currentMonth : currentMonth
        currentDate= currentDate<10 ? '0'+currentDate : currentDate
        
        this.createData.buyDate=currentYear+'-'+currentMonth+'-'+currentDate
        this.buyTimeDialogShow=false;
    },


    getStatusFn(value) {
        this.createData.status=value.text;
        this.statusDialogShow=false;
    },
    getArchitectureFn(value) {
        this.createData.architecture=value.text;
        this.architectureDialogShow=false;
    },
    getAcademyFn(value) {
        this.createData.academy=value.text;
        this.academyDialogShow=false;
    },


    saveFn(){
    
        //保证必填项都已经填好后
        if(this.createData.assetID.trim()&&this.createData.status.trim()&&this.createData.recorder.trim()){
            if(this.createData.mac.trim()&&!this.createData.architecture.trim()){
                this.$notify({
                    type:'warning',
                    message: '若mac存在则建筑和院区必填',
                    duration: 2000
                });
            }else if(this.createData.mac.trim()&&!this.createData.academy.trim()){
                this.$notify({
                    type:'warning',
                    message: '若mac存在则建筑和院区必填',
                    duration: 2000
                });
            }else{
                    //显示出正在更新
                var uploadLoading=Toast.loading({
                    message: '正在更新...',
                    forbidClick: true,
                    duration:0
                });

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
                        mac: formatScanCodeFn(this.createData.mac.trim()),
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
                        
                        this.$axios.post(NET_PORT.assetsUpdate,
                        datas
                        )
                        .then((response)=>{
                            if(response.data.msg==='ok'){
                            uploadLoading.clear();
                            Toast.success('更新成功！');
                            setTimeout(()=>{
                                this.$router.go(-1)
                            },1000)

                            } else if(response.data.message){
                                uploadLoading.clear();
                                Toast.fail(response.data.message);
                            }else if(response.data.msg==='failed'){

                                uploadLoading.clear();
                                Toast.fail('更新失败！');
                            }
                        })
                        .catch((error)=>{
                            uploadLoading.clear();
                            Toast.fail('更新失败！');
                        })

                    }else if(res.data.msg==='failed'){

                        uploadLoading.clear();
                        Toast.fail('图片更新失败！');
                    }
                })
                .catch((err)=>{
                    
                })
            }
            


        }else{
            this.$notify({
                type:'warning',
                message: '请补充完整必填信息！',
                duration: 2000
            });
        }

    },

    initCatFn(){
        this.secCatList=this.secCatData.filter((item,index)=>{
            return item.parent===this.createData.oneclassify;
        })
        this.thdCatList=this.thdCatData.filter((item,index)=>{
            return item.parent===this.createData.secondclassify;
        })

        this.fouCatList=this.fouCatData.filter((item,index)=>{
            return item.parent===this.createData.threeclassify;
        })
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
                    applyoddnumbers:data.applyoddnumbers ? data.applyoddnumbers : '',
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

                this.createData=normalData;
                
                //根据状态设置状态下拉选项
                this.checkStatusFn();

                this.initCatFn();
                
                this.getImgFn('imgShow')
                this.getImgFn('imgShowLeft')
                this.getImgFn('imgShowAllround')
                this.getImgFn('imgShowAbove')
                this.getImgFn('imgShowRight')
                this.getImgFn('imgShowPaperlabel')
                this.getImgFn('imgShowOnecodelable')
            }
            
        })
        .catch((err)=>{

        })
    },


    getImgFn(key){
        this.$axios.get(NET_PORT[key]+this.assetID,{responseType: 'arraybuffer'})
        .then((res)=>{
            let result='';
            result= 'data:image/png;base64,' + btoa(
                new Uint8Array(res.data).reduce((data, byte) => data + String.fromCharCode(byte), '')
            )

            if(result==='data:image/png;base64,'){

            }else{
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

  created(){
    this.$emit('setTitle','资产修改');
    this.assetID=this.$route.params.id
  },
  mounted(){
    this.getDataFn();
  },

}
</script>
<style lang="scss" scoped>
 @import "../../../assets/scss/mobile/common/assets_upload.scss";
  
</style>
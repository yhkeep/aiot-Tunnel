<template>
  <div class='assets_create_wrap'>
      <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>

      <div class="hover_animat custom_bg_color_white" >
          <div class="row_box">
                <h2 class="row_title">图像</h2>
                <div id="img_box">
                    <img :src="createData.image" alt="">
                    <div class="upload_wrap">
                        <div class="upload_box">
                            <span class="upload_span" ref="upload_span">点击上传</span>
                            <input class="upload_input" type="file" accept="image/jpg, image/jpeg,image/png" @change="handleImgFn($event)">
                        </div>
                    </div>
                </div>
          </div>
      </div>

        <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">资产新增</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>资产编号</h3>
                                <Input v-model="createData.assetID" placeholder="资产id"/>
                                </Col>
                            <Col span="6">
                                <h3>通用名称</h3>
                                <Input v-model="createData.generalName" placeholder="通用名称" />
                            </Col>
                            <Col span="6">
                                <h3>资产名称</h3>
                                <Input v-model="createData.assetName" placeholder="资产名称" />
                            </Col>
                            <Col span="6">
                                <h3>所属部门</h3>
                                <Select v-model="createData.department">
                                    <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                        </Row>
                    </div>

                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>规格型号</h3>
                                <Input v-model="createData.type" placeholder="规格型号" />
                            </Col>
                            
                            <Col span="6">
                                <h3>品牌</h3>
                                <Input v-model="createData.brand" placeholder="品牌" />
                            </Col>

                            <Col span="6">
                                <h3>申请科室</h3>
                                <Input v-model="createData.applyDept" placeholder="申请科室" />
                            </Col>
                            <!-- <Col span="6">
                                <h3>放置科室</h3>
                                <Input v-model="createData.locDept" placeholder="放置科室" />
                            </Col> -->
                            <Col span="6">
                                <h3>mac地址</h3>
                                <Input v-model="createData.mac" placeholder="mac地址" />
                            </Col>
                            
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                           
                            
                            <!-- <Col span="6">
                                <h3>手术室</h3>
                                <Input v-model="createData.operatingRoom" placeholder="手术室" />
                            </Col> -->

                            <Col span="6">
                                <h3>数量</h3>
                                <InputNumber  :min="1" v-model="createData.amount" placeholder="数量" style="width:100%;"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>单位</h3>
                                <Select v-model="createData.unit">
                                    <Option v-for="item in unitData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>购入日期</h3>
                                <DatePicker v-model="buyTime" type="date" @on-change="dateBuyFn"  placeholder="购入日期" style="width:100%;"></DatePicker>
                            </Col>
                            <Col span="6">
                                <h3>质保到期日</h3>
                                <DatePicker v-model="qualityTime" type="date" @on-change="dateQualityFn"  placeholder="质保到期日" style="width:100%;"></DatePicker>
                            </Col>

                            
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                           
                            <Col span="6">
                                <h3>金额</h3>
                                <InputNumber  :min="0" v-model="createData.money" placeholder="金额" style="width:100%;"></InputNumber>
                            </Col>
                            
                            <Col span="6">
                                <h3>状态</h3>
                                <Select v-model="createData.status">
                                    <Option v-for="item in statusData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </col>
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
                       <Button type="primary" @click="createFn">增加</Button>
                </div>
            </div>
        </div>

        <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
           
            <div style="text-align:center;padding:20px 0">
                <Spin fix v-if="!achieveUpload&&!errorUpload">
                    <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                    <div>正在上传</div>
                </Spin>
                <Spin fix v-if="achieveUpload" style="color:#00ad19">
                    <Icon type="ios-checkmark-circle" size=18 />
                    <div>上传成功</div>
                </Spin>
                <Spin fix v-if="errorUpload" style="color:#f72b2b">
                    <Icon type="ios-close-circle" size=18 />
                    <div>上传失败</div>
                </Spin>

            </div>
           
        </Modal>
  </div>
</template>

<script>

import Vue from "vue"
import { Row,Col,Icon,DatePicker,Select,Option,Input,Button,Modal,InputNumber,Spin} from 'iview';
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

import departData from "../../assets/data/depart.js"
import unitData from "../../assets/data/unit.js"
import statusData from "../../assets/data/status.js"


export default {
  name:'assets_create',
  components:{},
  props:{},
  data(){
    return {
        buyTime:'2019-01-01',
        qualityTime:'2023-01-01',
        createData:{
            assetID: "",
            image:"",
            generalName: "",
            assetName: "",
            type: "",
            brand: "",
            applyDept: "",
            // locDept: "",
            // operatingRoom: "",
            department:"",
            mac: "",
            amount: null,
            unit: "",
            buyDate: "2019-01-01",
            qualityDate:"2023-01-01",
            money: 10000,
            status: "",
            remark:"",
        },
      
        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,
        

        departData:departData,
        unitData:unitData,

        statusData:statusData,

    }
  },
  watch:{},
  computed:{},
  methods:{

       returnFn(){
            this.$router.go(-1)
        },
        handleImgFn(e){
            if(e.target.files[0]){
                this.$refs.upload_span.innerText="重新上传"
                var reader = new FileReader();
                reader.readAsDataURL(e.target.files[0]);
                reader.onload = (e)=>{
                    //读取完成后，数据保存在对象的result属性中
                    this.createData.image=e.target.result
                }
            }
        },

        dateBuyFn(value){
            this.createData.buyDate=value
        },

        dateQualityFn(value){
            this.createData.qualityDate=value
        },

        initData(){
            this.createData={
                assetID: "",
                image:"",
                generalName: "",
                assetName: "",
                type: "",
                brand: "",
                applyDept: "",
                // locDept: "",
                // operatingRoom: "",
                department:"",
                mac: "",
                amount: null,
                unit: "",
                buyDate: "2019-01-01",
                money: 10000,
                status: "",
                remark:"",
            };
            this.buyTime='2019-01-01';
            this.$refs.upload_span.innerText="立即上传"
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
                        //回到上一页
                        // this.$router.go(-1)
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

            // 判断有几个必需填的没填
            var k=0;
            for( var i in this.createData){
                if(!this.createData[i]){
                    if(i==='remark'||i==='image'||this.createData.money===0){
                    }
                    else{
                        k++;
                    }
                }
            }


            //保证必填项都已经填好后
            if(k===0){

                //显示出正在上传
                this.uploadModal=true;

                //拿到图片数据
                let imgDatas = {
                    "image":this.createData.image,
                }

                var configData='';
                for (var m in this.createData){
                    //目前接口不存在这几个参数，在这里先过滤掉
                    if(m==='image'){

                    }
                    else if(m==='amount'||m==='money'){
                        configData+=m+'='+this.createData[m]+'&'
                    }
                    else{
                        //将参数拼接到一起，裁掉左右空格
                        configData+=m+'='+this.createData[m].trim()+'&'
                    }
                }


                //截掉最后一个&符号
                var configDataSlice=configData.slice(0,configData.length-1)

                this.$axios.post(process.env.API_HOST+"uploadimage",imgDatas)
                .then((res)=>{

                    if(res.data.msg==='ok'){

                        this.$axios.post(process.env.API_HOST+'huaxi/upload?'+configDataSlice)
                        .then((response)=>{

                            if(response.data.msg==='ok'){

                               this.successFn();

                            }else if(response.data.msg==='failed'){

                                this.errorFn();
                            }
                        })
                        .catch((error)=>{
                            console.log(error)
                        })

                    }else if(res.data.msg==='failed'){

                       this.errorFn();
                    }
                })
                .catch((err)=>{
                    console.log(err)
                })


            }else{
                this.$Message.error({
                            content:"请补充完整信息!",
                            duration:2,
                })
            }

        }
  },
  created(){},
  mounted(){}
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/data_create.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped>
   
</style>
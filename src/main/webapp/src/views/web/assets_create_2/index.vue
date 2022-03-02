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
                                    <Col span="8">
                                        <h3>资产编号</h3>
                                        <Input v-model="createData.assetID" placeholder="资产id" disabled/>
                                        </Col>
                                   
                                    <Col span="8">
                                        <h3>资产名称</h3>
                                        <Input v-model="createData.assetName" placeholder="资产名称" />
                                    </Col>
                                    <Col span="8">
                                        <h3>所属部门</h3>
                                        <Select v-model="createData.department">
                                            <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                        </Select>
                                    </Col>
                                    
                                </Row>
                    </div>

                    <div class="form_item">
                        <Row :gutter="20">
                                    <Col span="8">
                                        <h3>一级分类</h3>
                                        <Select v-model="createData.fstCat">
                                            <Option v-for="item in fstCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                        </Select>
                                    </Col>
                                    <Col span="8">
                                        <h3>二级分类</h3>
                                        <Select v-model="createData.secCat">
                                            <Option v-for="item in secCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                        </Select>
                                    </Col>
                                    <Col span="8">
                                        <h3>三级分类</h3>
                                        <Select v-model="createData.thdCat">
                                            <Option v-for="item in thdCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                        </Select>
                                    </Col>
                                </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                                    <Col span="8">
                                        <h3>mac地址</h3>
                                        <Input v-model="createData.mac" placeholder="mac地址" />
                                    </Col>

                                    <Col span="8">
                                        <h3>数量</h3>
                                        <InputNumber  :min="1" v-model="createData.amount" placeholder="数量" style="width:100%;"></InputNumber>
                                    </Col>
                                    <Col span="8">
                                        <h3>单位</h3>
                                        <Select v-model="createData.unit">
                                            <Option v-for="item in unitData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                        </Select>
                                    </Col>
                                    
                                </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                                    <Col span="8">
                                        <h3>购入日期</h3>
                                        <DatePicker v-model="buyTime" type="date" @on-change="dateBuyFn"  placeholder="购入日期" style="width:100%;"></DatePicker>
                                    </Col>
                                    <Col span="8">
                                        <h3>金额</h3>
                                        <InputNumber  :min="0" v-model="createData.money" placeholder="金额" style="width:100%;"></InputNumber>
                                    </Col>
                                   
                                    <Col span="8">
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

import departData from "../../../assets/data/depart2.js"
import unitData from "../../../assets/data/unit.js"
import statusData from "../../../assets/data/status.js"
import {fstCatData,secCatAllData,thdCatAllData} from "../../../assets/data/category.js"


export default {
  name:'assets_create',
  components:{},
  props:{},
  data(){
    return {
        buyTime:'2019-01-01',
        createData:{
            assetID: "",
            image:"",
            assetName: "",
            department:"",
            mac: "",
            amount: null,
            unit: "",
            buyDate: "2019-01-01",
            money: 10000,
            status: "",
            remark:"",
            fstCat:'',
            secCat:'',
            thdCat:'',
        },
      
        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,
        

        departData:departData,
        unitData:unitData,
        secCatData:[],
        thdCatData:[],
        fstCatData:fstCatData,
        secCatAllData:secCatAllData,
        thdCatAllData:thdCatAllData,

        statusData:statusData,

    }
  },
  watch:{

       //监视一级分类和二级分类的变化动态改变数据
        "createData.fstCat"(){
            this.secCatData=this.secCatAllData.filter((item,index)=>{
            return item.parent===this.createData.fstCat;
            })
            this.thdCatData=[];
            this.createData.thdCat='';
        },
        "createData.secCat"(){
            this.thdCatData=this.thdCatAllData.filter((item,index)=>{
            return item.parent===this.createData.secCat;
            })

            this.createData.thdCat='';
        },
        "createData.thdCat"(){
            console.log(this.createData.thdCat)
            if(this.createData.thdCat){
                var code='';
                this.thdCatData.forEach((item,index)=>{
                if(item.value===this.createData.thdCat){
                    code=item.code;
                }
                })
                //这里拿到所选择分类的代码
                // console.log(code)
                this.createData.assetID=code.toString();
            }else{
                this.createData.assetID='';
            }
        }
  },
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

        initData(){
            this.createData={
                assetID: "",
                image:"",
                assetName: "",
                department:"",
                mac: "",
                amount: null,
                unit: "",
                buyDate: "2019-01-01",
                money: 10000,
                status: "",
                remark:"",
                fstCat:'',
                secCat:'',
                thdCat:'',
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
                    console.log(m)
                    console.log(typeof this.createData[m])
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

                console.log(configDataSlice)

                // this.$axios.post(process.env.API_HOST+"uploadimage",imgDatas)
                // .then((res)=>{

                //     if(res.data.msg==='ok'){

                //         this.$axios.post(process.env.API_HOST+'huaxi/upload?'+configDataSlice)
                //         .then((response)=>{

                //             if(response.data.msg==='ok'){

                //                this.successFn();

                //             }else if(response.data.msg==='failed'){

                //                 this.errorFn();
                //             }
                //         })
                //         .catch((error)=>{
                //             console.log(error)
                //         })

                //     }else if(res.data.msg==='failed'){

                //        this.errorFn();
                //     }
                // })
                // .catch((err)=>{
                //     console.log(err)
                // })


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



<style lang='scss' scoped>
    @import '../../../assets/scss/web/common/data_create.scss';
    @import '../../../assets/scss/web/common/form_data.scss';
</style>
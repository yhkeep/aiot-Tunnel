<template>
  <div class='temp_create_wrap'>
        <div class="hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">温湿度仪器新增</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>编号<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="createData.freezernumber" placeholder="仪器编号"/>
                                </Col>
                            <Col span="6">
                                <h3>名称<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="createData.freezername" placeholder="仪器名称" />
                            </Col>
                           
                            <Col span="6">
                                <h3>部门名称<span class="must_fill_info">（必填）</span></h3>
                                <!-- <Select v-model="createData.depart">
                                    <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select> -->
                                <!-- <Input v-model="createData.department" placeholder="部门名称" /> -->
                                <AutoComplete
                                    v-model="createData.department"
                                   
                                    placeholder="部门名称"
                                    >
                                    <Option v-for="item in departmentroomArr" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>所在位置</h3>
                                <Input v-model="createData.location" placeholder="所在位置" />
                            </Col>
                            
                        </Row>
                        
                    </div>

                    <div class="form_item">
                        <Row :gutter="20">
                           <Col span="6">
                                <h3>mac地址<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="createData.mac" placeholder="mac地址" />
                            </Col>
                            <Col span="6">
                                <h3>仪器类型<span class="must_fill_info">（必填）</span></h3>
                                <Select v-model="createData.type" @on-change="instrumentChangeFn">
                                    <Option v-for="item in instrumentType" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>仪器记录时间间隔（分钟）<span class="must_fill_info">（必填）</span></h3>
                                <Select v-model="createData.saveinterval">
                                    <Option v-for="item in tempInterval" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                            <Col span="6">
                                <h3>温度上限（℃）</h3>
                                <InputNumber v-model="createData.tempTop"  style="width:100%"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>温度下限（℃）</h3>
                                <InputNumber v-model="createData.tempBottom"  style="width:100%"></InputNumber>
                            </Col>
                            <Col span="6" v-show="createData.type!=='GS1W'">
                                <h3>湿度上限（%）</h3>
                                <InputNumber v-model="createData.humTop"  style="width:100%" :max="100" :min="0"></InputNumber>
                            </Col>
                            <Col span="6" v-show="createData.type!=='GS1W'">
                                <h3>湿度下限（%）</h3>
                                <InputNumber v-model="createData.humBottom"  style="width:100%" :max="100" :min="0"></InputNumber>
                            </Col>
                            
                        </Row>
                    </div>
                </div>
                <div class="submit_btn_wrap">
                       <Button type="primary" shape="circle" @click="createFn">保存</Button>
                </div>
            </div>
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
import NET_PORT from "../../../api/port.js"
import instrumentType from "../../../assets/data/instrumentType.js"
import tempInterval from "../../../assets/data/tempInterval.js"
import formatScanCodeFn from "../../../utils/formatScanCode.js"
export default {
  name:'temp_create',
  components:{},
  props:{},
  data(){
    return {
        // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

        createData:{
            freezernumber:'',
            freezername:'',
            department:'',
            location:'',
            mac:'',
            type:'',
            tempTop: 0,
            tempBottom: 0,
            humTop: 0,
            humBottom: 0,
            saveinterval:'10',
        },
        instrumentType:instrumentType,
        tempInterval:tempInterval,
        inputData:[],
        inputDataAll:[],
        departmentroomArr:[],
    }
  },
  watch:{},
  computed:{},
  methods:{

        // handleSearchFn(data){
        //     this.inputData = this.inputDataAll.filter(item => item.toLowerCase().indexOf(data.toLowerCase()) > -1);
        // },

        // inputBlurFn(){
        //     this.inputDataAll=[];
        //     this.inputData=[];
        // },

        // inputFocusFn(key){
        //     this.inputDataAll=[];
        //     this.inputData=[];
        //     let queryArr=[];
        //     let obj={};
        //     obj[key]="1"
        //     queryArr.push(obj)
        //     this.$axios.post(NET_PORT.inputQuery,queryArr)
        //     .then((res)=>{
        //         res.data.data.forEach((item,index)=>{
                
        //             if(this.inputDataAll.find((result)=>{return result===item[key]})){

        //             }else{
        //                 this.inputDataAll.push(item[key])
        //             }
        //         })

        //         setTimeout(()=>{
        //             this.inputData=JSON.parse(JSON.stringify(this.inputDataAll))
        //         },300)
        //     })
        //     .catch((err)=>{
        //         console.log(err)
        //     })
        // },

        successFn(){
            this.achieveUpload=true;
            setTimeout(()=>{
                this.uploadModal=false;
                setTimeout(()=>{
                        this.achieveUpload=false;
                        this.errorUpload=false;
                        //回到上一页
                        this.$router.go(-1)
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

        instrumentChangeFn(value){
            this.instrumentType.forEach((item,index)=>{
                if(item.key===value){
                    for(let i in item.range){
                        this.createData[i]=item.range[i];
                    }
                }
            })
        },

        createFn(){
            if(this.createData.freezernumber.trim()&&this.createData.department.trim()&&this.createData.freezername.trim()&&this.createData.mac.trim()&&this.createData.type.trim()){
                
                if(this.createData.tempBottom!==null&&this.createData.tempTop!==null){
                    if(this.createData.tempBottom===this.createData.tempTop){
                        this.$Modal.warning({
                            title: '提示！',
                            content: '温度上下限不能相等！',
                        })
                        return
                    }
                    if(this.createData.tempBottom>this.createData.tempTop){
                        this.$Modal.warning({
                            title: '提示！',
                            content: '温度上限不能小于下限！',
                        })
                        return
                    }
                }

                if(this.createData.humBottom!==null&&this.createData.humTop!==null){
                    if(this.createData.humBottom===this.createData.humTop){
                        this.$Modal.warning({
                            title: '提示！',
                            content: '湿度上下限不能相等！',
                        })
                        return 
                    }
                    if(this.createData.humBottom>this.createData.humTop){
                        this.$Modal.warning({
                            title: '提示！',
                            content: '湿度上限不能小于下限！',
                        })
                        return
                    }
                }


                this.uploadModal=true;
                let datas={
                    freezernumber :this.createData.freezernumber.trim(),
                    freezername :this.createData.freezername.trim(),
                    department :this.createData.department.trim(),
                    location :this.createData.location.trim(),
                    mac:formatScanCodeFn(this.createData.mac.trim()),
                    type:this.createData.type,
                    temperaturefitted:this.createData.tempBottom+'~'+this.createData.tempTop,
                    humidityfitted:this.createData.humBottom+'~'+this.createData.humTop,
                    saveinterval:this.createData.saveinterval,
                    address:1,
                }

                this.$axios.post(NET_PORT.tempCreate,datas)
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
                    content:"请补充完整信息!",
                    duration:2,
                })
            }
        }
  },
  created(){
        let arr=[];
        this.$store.state.departmentroom.split(',').forEach((item,index)=>{
            arr.push(
                item
            )
        })
        this.departmentroomArr=arr;
    //   if(this.locDept!==''){
    //       this.createData.department=this.locDept;
    //   }
  },
  mounted(){}
}
</script>
<style lang='scss' scoped>
    @import '../../../assets/scss/web/common/form_data.scss';
</style>
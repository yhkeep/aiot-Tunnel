<template>
  <div id='temp_edit_wrap'>
        <div class="hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">温湿度编辑</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                             <Col span="6">
                                <h3>编号<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="tempData.freezernumber"  placeholder="仪器编号"/>
                                </Col>
                           
                            <Col span="6">
                                <h3>名称<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="tempData.freezername" placeholder="请输入名称" />
                            </Col>
                            <Col span="6">
                                <h3>部门名称<span class="must_fill_info">（必填）</span></h3>
                                <!-- <Input v-model="tempData.department" placeholder="部门名称" /> -->
                               
                                <AutoComplete
                                    v-model="tempData.department"
                                   
                                    placeholder="部门名称"
                                    >
                                    <Option v-for="item in departmentroomArr" :value="item" :key="item">{{ item }}</Option>
                                </AutoComplete>
                            </Col>
                            <Col span="6">
                                <h3>所在位置</h3>
                                <Input v-model="tempData.location" placeholder="所在位置" />
                            </Col>
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                           <Col span="6">
                                <h3>mac地址<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="tempData.mac" disabled placeholder="mac地址" />
                            </Col>
                            <Col span="6">
                                <h3>仪器类型<span class="must_fill_info">（必填）</span></h3>
                                <Select v-model="tempData.type">
                                    <Option v-for="item in instrumentType" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            <Col span="6">
                                <h3>仪器记录时间间隔（分钟）<span class="must_fill_info">（必填）</span></h3>
                                <Select v-model="tempData.saveinterval">
                                    <Option v-for="item in tempInterval" :value="item.key" :key="item.key">{{ item.text }}</Option>
                                </Select>
                            </Col>
                            
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                            <Col span="6">
                                <h3>温度上限（℃）</h3>
                                <InputNumber v-model="tempData.tempTop"  style="width:100%"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>温度下限（℃）</h3>
                                <InputNumber v-model="tempData.tempBottom"  style="width:100%"></InputNumber>
                            </Col>
                            <Col span="6" v-show="tempData.type!=='GS1W'">
                                <h3>湿度上限（%）</h3>
                                <InputNumber v-model="tempData.humTop"  style="width:100%" :max="100" :min="0"></InputNumber>
                            </Col>
                            <Col span="6" v-show="tempData.type!=='GS1W'">
                                <h3>湿度下限（%）</h3>
                                <InputNumber v-model="tempData.humBottom"  style="width:100%" :max="100" :min="0"></InputNumber>
                            </Col>
                            
                        </Row>
                    </div>

                  
                </div>
                <div class="submit_btn_wrap">
                       <Button type="primary" shape="circle" @click="saveTempFn">保存</Button>
                </div>
            </div>
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
import { Row,Col,Icon,Select,Option,Input,Button,Modal,InputNumber,Spin,AutoComplete} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('InputNumber',InputNumber);
Vue.component('Spin',Spin);
Vue.component('Modal',Modal);
Vue.component('AutoComplete',AutoComplete);

import instrumentType from "../../../assets/data/instrumentType.js"
import tempInterval from "../../../assets/data/tempInterval.js"
import NET_PORT from "../../../api/port.js"

export default {
  name:'temp_edit',
  components:{},
  props:{},
  data(){
    return {
        // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
        mac:'',
        tempData:{
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

        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,


        instrumentType:instrumentType,
        tempInterval:tempInterval,
        // inputData:[],
        // inputDataAll:[],
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

        saveTempFn(){
            if(this.tempData.freezernumber.trim()&&this.tempData.department.trim()&&this.tempData.freezername.trim()&&this.tempData.mac.trim()&&this.tempData.type.trim()){
                    if(this.tempData.tempBottom!==null&&this.tempData.tempTop!==null){
                        if(this.tempData.tempBottom===this.tempData.tempTop){
                            this.$Modal.warning({
                                title: '提示！',
                                content: '温度上下限不能相等！',
                            })
                            return
                        }
                        if(this.tempData.tempBottom>this.tempData.tempTop){
                            this.$Modal.warning({
                                title: '提示！',
                                content: '温度上限不能小于下限！',
                            })
                            return
                        }
                    }

                    if(this.tempData.humBottom!==null&&this.tempData.humTop!==null){
                        if(this.tempData.humBottom===this.tempData.humTop){
                            this.$Modal.warning({
                                title: '提示！',
                                content: '湿度上下限不能相等！',
                            })
                            return 
                        }
                        if(this.tempData.humBottom>this.tempData.humTop){
                            this.$Modal.warning({
                                title: '提示！',
                                content: '湿度上限不能小于下限！',
                            })
                            return
                        }
                    }

                    let datas={
                        freezernumber:this.tempData.freezernumber.trim(),
                        freezername:this.tempData.freezername.trim(),
                        department:this.tempData.department.trim(),
                        type:this.tempData.type,
                        location:this.tempData.location.trim(),
                        temperaturefitted:this.tempData.tempBottom+'~'+this.tempData.tempTop,
                        humidityfitted:this.tempData.humBottom+'~'+this.tempData.humTop,
                        saveinterval:this.tempData.saveinterval,
                    }

                    let configData='';
                    for (let m in datas){
                        if(!datas[m]){
                            datas[m]='';
                        }
                        //将参数拼接到一起，裁掉左右空格
                        configData+=m+'='+datas[m].trim()+'&'
                    }

                    this.uploadModal=true;

                    this.$axios.get(NET_PORT.tempEditUpdate+'?mac='+this.mac+'&'+configData)
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
           
        },

        tempRangeComFn(data,index){
            if(data){
                return data.split('~')[index] ==='null' ? null : Number(data.split('~')[index])
            }else{
                return null
            }
        },

        getDataFn(){
            this.$Loading.start()
            this.$axios.get(NET_PORT.tempEditDetail+'?mac='+this.mac)
            .then((res)=>{
                let result=res.data[0]

                this.tempData={
                    freezernumber:result.freezernumber ? result.freezernumber : '',
                    freezername:result.freezername,
                    department:result.department? result.department : '',
                    location:result.location ? result.location : '',
                    mac:result.mac ? result.mac : '',
                    type:result.type ? result.type : '',
                    tempTop:this.tempRangeComFn(result.temperaturefitted,1),
                    tempBottom: this.tempRangeComFn(result.temperaturefitted,0),
                    humTop: this.tempRangeComFn(result.humidityfitted,1),
                    humBottom: this.tempRangeComFn(result.humidityfitted,0),
                    saveinterval:result.saveinterval ? result.saveinterval : '10',
                }

                this.$Loading.finish();
            })
            .catch((err)=>{
                this.$Loading.error();
            })

        }
  },
    created(){
        this.mac=this.$route.params.mac
        this.getDataFn();
        let arr=[];
        this.$store.state.departmentroom.split(',').forEach((item,index)=>{
            arr.push(
                item
            )
        })
        this.departmentroomArr=arr;
    },
    mounted(){},

}
</script>



<style lang='scss' scoped>
  @import '../../../assets/scss/web/common/form_data.scss';
</style>
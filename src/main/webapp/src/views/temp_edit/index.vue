<template>
  <div id='temp_edit_wrap'>
        <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>
        <h1 class="assets_title">{{tempData.name}} - {{mac}}</h1>

        <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">温湿度编辑</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="12">
                                <h3>名称</h3>
                                <Input v-model="tempData.name" placeholder="请输入名称" />
                            </Col>
                            <Col span="12">
                                <h3>使用部门</h3>
                                <Select v-model="tempData.depart">
                                    <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                            <Col span="6">
                                <h3>温度上限</h3>
                                <InputNumber v-model="tempData.tempTop"  style="width:100%"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>温度下限</h3>
                                <InputNumber v-model="tempData.tempBottom"  style="width:100%"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>湿度上限</h3>
                                <InputNumber v-model="tempData.humTop"  style="width:100%" :max="100" :min="0"></InputNumber>
                            </Col>
                            <Col span="6">
                                <h3>湿度下限</h3>
                                <InputNumber v-model="tempData.humBottom"  style="width:100%" :max="100" :min="0"></InputNumber>
                            </Col>
                            
                        </Row>
                    </div>

                  
                </div>
                <div class="submit_btn_wrap">
                       <Button type="primary" @click="saveTempFn">保存</Button>
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
import { Row,Col,Icon,Select,Option,Input,Button,Modal,InputNumber,Spin} from 'iview';
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

import departData from "../../assets/data/depart.js"

export default {
  name:'temp_edit',
  components:{},
  props:{},
  data(){
    return {
        mac:'',
        tempData:{
            name:'',
            depart:'',
            tempTop: null,
            tempBottom: null,
            humTop: null,
            humBottom: null,
            // tempAdjust: null,
            // humAdjust: null,
        },

        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

        departData:departData,
    }
  },
  watch:{},
  computed:{},
  methods:{
        returnFn(){
            this.$router.go(-1)
        },

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
            // console.log(this.tempData)
            if(this.tempData.name.trim()){
                var obj={
                    name:this.tempData.name.trim(),
                    depart:this.tempData.depart,
                    temperaturefitted:this.tempData.tempBottom+'~'+this.tempData.tempTop,
                    humidityfitted:this.tempData.humBottom+'~'+this.tempData.humTop,
                }

                //显示出正在上传
                this.uploadModal=true;

                this.$axios.get(process.env.API_HOST+'luzhou/edit?mac='+this.mac+'&freezername='+obj.name+'&department='+obj.depart+'&temperaturefitted='+obj.temperaturefitted+'&humidityfitted='+obj.humidityfitted)
                .then((res)=>{
                    // console.log(res)
                    if(res.data.msg==='ok'){
                        this.successFn();
                    }else if(res.data.msg==='failed'){
                        this.errorFn();
                    }
                })
                .catch((err)=>{
                    console.log(err)
                })
           
            }
           
        },

        getDataFn(){
            this.$Loading.start()

            this.$axios.get(process.env.API_HOST+'luzhou/section?mac='+this.mac)
            .then((res)=>{
                // console.log(res)
                var result=res.data[0]

                // console.log(result)

                this.tempData={
                    name:result.freezername,
                    depart:result.department,
                    tempTop: Number(result.temperaturefitted.split('~')[1]),
                    tempBottom: Number(result.temperaturefitted.split('~')[0]),
                    humTop: Number(result.humidityfitted.split('~')[1]),
                    humBottom: Number(result.humidityfitted.split('~')[0]),
                }

                this.$Loading.finish();
            })
            .catch((err)=>{
                console.log(err)
            })


        }
  },
    created(){
            this.mac=this.$route.params.mac
            var result=window.sessionStorage.getItem('tempObj')
            if(result){
                var obj=JSON.parse(result)
                this.tempData.name=obj.name;

                this.getDataFn();
            }else{
                this.$Message.error({
                    content:"未找到网关信息，将返回上一页!",
                    duration:2,
                })

                setTimeout(()=>{
                this.$router.go(-1)
                },2000)
            }
    },
    mounted(){},
    beforeDestroy(){
      this.$Loading.finish();
      window.sessionStorage.removeItem('tempObj')
    }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped>
</style>
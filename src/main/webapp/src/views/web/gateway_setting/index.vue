<template>
  <div class='gateway_setting_wrap'>
        <div class="row_box hover_animat custom_bg_color_white">
                <h2 class="row_title">是否应用于所有网关</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            
                            <Col span="8">
                                <i-switch v-model="setAllKey" @on-change="switchChangeFn">
                                    <span slot="open">是</span>
                                    <span slot="close">否</span>
                                </i-switch>
                            </Col>
                            
                        </Row>
                    </div>
                    <div class="form_item" v-if="setAllKey===false">
                        <Row :gutter="20">
                            <Col span="24">
                                <h3>选择要应用的网关mac地址</h3>

                                <Select v-model="macArr" multiple filterable  >
                                    <Option v-for="item in gatewayArr" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            
                        </Row>
                    </div>

                </div>
        </div>

        <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                 <h2 class="row_title">公共配置</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                         
                            <Col span="8">
                                <h3>是否永远关闭彩灯（默认为是）</h3>
                                    <Select v-model="configData.disableLED">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                             <Col span="8">
                                <h3>顶部彩灯是否长亮（默认为否）</h3>
                                <Select v-model="configData.isLongBright">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>是否上传 Unknown类型的数据（默认为是）</h3>
                                <Select v-model="configData.isUploadUnkown">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>

                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                            <Col span="8">
                                <h3>是否上传ibeacon类型的数据（默认为是）</h3>
                                <Select v-model="configData.isUploadIBeacon">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                           
                            <Col span="8">
                                <h3>是否上传S1类型的数据（默认为是）</h3>
                                <Select v-model="configData.isUploadS1">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>是否上传网关数据（默认为是）</h3>
                                <Select v-model="configData.isUploadGateway">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            
                           
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                           
                            <Col span="8">
                                <h3>断线自动重启（默认为否）</h3>
                                <Select v-model="configData.isauto">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>重启监听时间（5 ~ 65 单位秒）</h3>
                                <InputNumber style="width:100%" :max="65" :min="5" :step="1" v-model="configData.timeout" placeholder="5 ~ 65 "></InputNumber>
                            </Col>
                           
                            <Col span="8">
                                <h3>过滤mac（如：AC23|C202）</h3>
                                <Input  v-model="configData.macReg" placeholder="过滤mac" />
                            </Col>
                            
                            
                        </Row>
                       
                    </div>
                    <div class="form_item">
                            <Row :gutter="20">
                               
                                <Col span="8">
                                    <h3>上传服务器（如：172.20.29.2）</h3>
                                    <Input  v-model="configData.mqttUrl" placeholder="上传服务器url值" />
                                </Col>
                                <Col span="8">
                                    <h3>端口</h3>
                                    <Input  v-model="configData.port" placeholder="端口号" />
                                </Col>
                               

                                
                               
                            </Row>
                    </div>
                    <div class="form_item">
                            <Row :gutter="20">
                                <Col span="8">
                                    <h3>QoS 等级（默认值1）</h3>
                                    <Select v-model="configData.qos">
                                        <Option v-for="item in qosData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </Col>
                                <Col span="8">
                                    <h3>提示</h3>
                                    <p class="notice_text">QoS=0：最多一次，有可能重复或丢失。</p>
                                    <p class="notice_text">QoS=1：至少一次，有可能重复。</p>
                                </Col>
                            </Row>
                    </div>
                   
                    <!-- <div class="form_item">
                         <Row :gutter="20">
                            <Col span="8">
                                <h3>发布号（mac地址）</h3>
                                <Input type="textarea"  v-model="configData.publishTopic" placeholder="发布号" />
                            </Col>
                            <Col span="8">
                                <h3>订阅号（mac地址）</h3>
                                <Input type="textarea"  v-model="configData.subscribeTopic" placeholder="订阅号" />
                            </Col>
                            <Col span="8">
                                <h3>订阅响应（mac地址）</h3>
                                <Input type="textarea"  v-model="configData.responseTopic" placeholder="订阅响应" />
                            </Col>
                            
                        </Row>
                       
                    </div> -->

                </div>
            </div>
               
        </div>

        <div class="row_wrap">
            <Button type="primary" shape="circle" @click="saveConfigFn">保存配置</Button>
        </div>
  </div>
</template>

<script>

import Vue from "vue"

import { Row,Col,Icon,Table ,Tag ,Select,Option,Page,Input,Button,Modal,InputNumber,Spin,Switch} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Table', Table);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('InputNumber',InputNumber);
Vue.component('Spin',Spin);
Vue.component('i-switch',Switch);
import NET_PORT from "../../../api/port.js"
export default {
  name:'gateway_setting',
  components:{},
  props:{},
  data(){
    return {

        whetherData:[
                { value: 'YES', label: '是' },
                { value: 'NO', label: '否' },
        ],
       
        qosData:[
                { value: 0, label: '0' },
                { value: 1, label: '1' },
        ],
        setAllKey:false,
        configData:{
            // takeEffectImmediately:'NO',
            // uploadInterval:1000,
            disableLED:'YES',
            isLongBright:'NO',
            isUploadUnkown:'YES',
            isUploadIBeacon:'YES',
            isUploadS1:'YES',
            isUploadGateway:'YES',
            isauto:'NO',
            mqttUrl:'172.20.29.2',
            port:'',
            qos:1,
            timeout:30,
            macReg:'',
            publishTopic:'',
            subscribeTopic:'',
            responseTopic:'',
            
            // userName:'',
            // passWord:'',
            // hUrl:'',
            // httpUser:'',
            // httpPass:'',
        },
        gatewayArr:[],
        macArr:[],
        // selectMacArr:[],
    }
  },
  watch:{
      
  },
  computed:{},
  methods:{
      switchChangeFn(status){
          this.macArr=[];
      },

      macDeleteFn(e,name){
          this.selectMacArr.forEach((item,index)=>{
              if(item===name){
                  this.selectMacArr.splice(index,1)
              }
          })
      },


        saveConfigFn(){

            if(!this.setAllKey&&!this.macArr.length){
                this.$Message.error({
                    title:'提示！',
                    content:'请选择您要设置的网关mac地址'
                })
            }else{
                this.$Modal.confirm(
                {
                    title:'保存提示',
                    okText:'确定',
                    cancelText:'取消',
                    content:'<h2 style="color:#f94040">你确定要保存当前配置吗？离线状态的网关将无法保存配置</h2>',
                    onOk:()=>{
                        let arr=[];
                        let obj={
                            gatewaymac:'',
                            disableLED:this.configData.disableLED,
                            isLongBright:this.configData.isLongBright,
                            isUploadUnkown:this.configData.isUploadUnkown,
                            isUploadIBeacon:this.configData.isUploadIBeacon,
                            isUploadS1:this.configData.isUploadS1,
                            isUploadGateway:this.configData.isUploadGateway,
                            isauto:this.configData.isauto,
                            // mUrl:this.configData.mUrl.trim(),
                            mqttUrl:this.configData.mqttUrl.trim(),
                            port:this.configData.port.trim(),
                            qos:this.configData.qos.toString(),
                            timeout:this.configData.timeout.toString(),
                            macReg:this.filterMacRegFn(this.configData.macReg.trim()),
                            // publishTopic:this.configData.publishTopic.trim(),
                            // subscribeTopic:this.configData.subscribeTopic.trim(),
                            // responseTopic:this.configData.responseTopic.trim(),
                        }

                        if(this.setAllKey){
                            this.gatewayArr.forEach((item,index)=>{
                                let filterObj=JSON.parse(JSON.stringify(obj));
                                filterObj.gatewaymac=item.value.trim();
                                filterObj.publishTopic=item.value.trim();
                                filterObj.subscribeTopic=item.value.trim();
                                filterObj.responseTopic=item.value.trim();
                                arr.push(filterObj)
                            })
                        }else{
                            this.macArr.forEach((item,index)=>{
                                let filterObj=JSON.parse(JSON.stringify(obj));
                                filterObj.gatewaymac=item.trim();
                                filterObj.publishTopic=item.trim();
                                filterObj.subscribeTopic=item.trim();
                                filterObj.responseTopic=item.trim();
                                arr.push(filterObj)
                            })
                        }

                        this.$axios.post(NET_PORT.gatewayEdit,arr)
                        .then((res)=>{
                            if(res.data.msg==='success'){
                               setTimeout(()=>{
                                    this.$Modal.success({
                                        title: '提示！',
                                        content: res.data.data,
                                    });
                               },500)
                            }else{
                               setTimeout(()=>{
                                    this.$Modal.error({
                                        title: '提示！',
                                        content: res.data.data,
                                    });
                               },500)
                            }
                        })
                        .catch((err)=>{
                            console.log(err)
                        })
                    },
                })
            }
            
           
        },

        filterMacRegFn(str){
            if(str===''){
                return ''
            }else{
                let arr=str.split('|')
                let result='';
                for(let i=0;i<arr.length;i++){
                    result=result+arr[i];
                }
                return result
            }
            
        },

        getDataFn(){
            this.$Loading.start()
            this.gatewayArr=[]
            this.$axios.get(NET_PORT.gatewayQuery+this.$store.state.address)
            .then((res)=>{
                var arr=res.data;
                arr.forEach((item,index)=>{
                    if(JSON.stringify(item)!=='{}'){
                        if(item.gatewaymac!==''){
                            var obj={
                                value:item.gatewaymac,
                                label:item.gatewaymac,
                            }
                            this.gatewayArr.push(obj)
                        }
                    }
                })

                this.$Loading.finish();

            })
            .catch((err)=>{
                this.$Loading.error();
                // console.log(err)
            })
        },
  },
  created(){
      this.getDataFn();
  },
  mounted(){}
}
</script>



<style lang='scss' scoped>
  @import '../../../assets/scss/web/common/form_data.scss';
</style>
<template>
  <div class='gateway_setting_wrap'>
      <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>

        <div class="row_box hover_animat custom_bg_color_white">
                <h2 class="row_title">是否应用于所有网关</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            
                            <Col span="8">
                                <i-switch v-model="setAllKey">
                                    <span slot="open">是</span>
                                    <span slot="close">否</span>
                                </i-switch>
                            </Col>
                            
                        </Row>
                    </div>
                    <div class="form_item" v-if="setAllKey===false">
                        <Row :gutter="20">
                            <Col span="4">
                                <h3>输入要应用的网关mac地址</h3>
                                <Input  v-model="configData.mac" placeholder="mac地址" />
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
                            <!-- <Col span="8">
                                <h3>网关mac地址<span style="color:red">（不填则默认设置所有网关）</span></h3>
                                <Input  v-model="configData.mac" placeholder="mac地址" />
                            </Col> -->
                            <Col span="8">
                                <h3>是否立刻生效（默认为否）</h3>
                                <Select v-model="configData.takeEffectImmediately">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>上传间隔（单位：毫秒，默认1000）</h3>
                                    <InputNumber  :min="1000" :step="1000" v-model="configData.common.uploadInterval" placeholder="上传间隔" style="width:100%;"></InputNumber>
                            </Col>
                            <Col span="8">
                                <h3>协议选择（默认为MQTT）</h3>
                                <Select v-model="configData.common.proto" @on-change="changeProtoFn">
                                    <Option v-for="item in protoData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>

                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                            <Col span="8">
                                <h3>是否永远关闭彩灯（默认为否）</h3>
                                    <Select v-model="configData.common.disableLED">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>顶部彩灯是否长亮（默认为否）</h3>
                                    <Select v-model="configData.common.isLongBright">
                                    <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                        </Row>
                    </div>
                    <div class="form_item">
                            <Row :gutter="20">
                                <Col span="8">
                                <h3>是否上传ibeacon类型的数据（默认为是）</h3>
                                    <Select v-model="configData.common.isUploadIBeacon">
                                        <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </Col>
                                <Col span="8">
                                    <h3>是否上传S1类型的数据（默认为否）</h3>
                                    <Select v-model="configData.common.isUploadS1">
                                        <Option v-for="item in whetherData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </Col>
                                
                            </Row>
                    </div>

                </div>
            </div>
               
        </div>

        <div class="row_wrap hover_animat custom_bg_color_white" v-if="configData.common.proto==='MQTT'">
            <div class="row_box">
                 <h2 class="row_title">MQTT配置</h2>
                <div class="form_wrap">
                    <div class="form_item">
                       <Row :gutter="20">
                            <Col span="8">
                            <h3>MQTT的url值</h3>
                                <Input  v-model="configData.mqtt.mac" placeholder="MQTT url值" />
                            </Col>
                            <Col span="8">
                                <h3>MQTT QoS 等级（默认值0）</h3>
                                <Select v-model="configData.mqtt.qos">
                                    <Option v-for="item in qosData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>MQTT服务器的用户名</h3>
                                <Input  v-model="configData.mqtt.userName" placeholder="MQTT用户名" />
                            </Col>
                            
                        </Row>
                    </div>
                    <div class="form_item">
                         <Row :gutter="20">
                            <Col span="8">
                            <h3>MQTT服务器的密码</h3>
                                <Input  v-model="configData.mqtt.passWord" placeholder="MQTT 密码" />
                            </Col>
                            
                        </Row>
                    </div>

                </div>
            </div>
               
        </div>

        <div class="row_wrap hover_animat custom_bg_color_white" v-else>
            <div class="row_box">
                 <h2 class="row_title">HTTP配置</h2>
                <div class="form_wrap">
                    <div class="form_item">
                       <Row :gutter="20">
                            <Col span="8">
                                <h3>http的url值</h3>
                                <Input  v-model="configData.http.hUrl" placeholder="http url值" />
                            </Col>
                            <Col span="8">
                                <h3>http用户名</h3>
                                <Input  v-model="configData.http.httpUser" placeholder="http用户名" />
                            </Col>
                            <Col span="8">
                                <h3>http密码</h3>
                                <Input  v-model="configData.http.httpPass" placeholder="http 密码" />
                            </Col>
                            
                        </Row>
                    </div>
                   

                </div>
            </div>
               
        </div>

        <div class="row_wrap">
            <Button type="primary" @click="saveConfigFn">保存配置</Button>
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
        protoData:[
                { value: 'MQTT', label: 'MQTT' },
                { value: 'HTTP', label: 'HTTP' },
        ],
        qosData:[
                { value: 0, label: '0' },
                { value: 1, label: '1' },
        ],
        setAllKey:true,
        configData:{
            mac:'',
            action:'config',
            takeEffectImmediately:'NO',
            common:{
                proto:'MQTT',
                uploadInterval:1000,
                disableLED:'NO',
                isLongBright:'NO',
                isUploadIBeacon:'YES',
                isUploadS1:'NO',
            },
            mqtt:{
                mUrl:'',
                qos:0,
                userName:'',
                passWord:'',
            },
            http:{
                hUrl:'',
                httpUser:'',
                httpPass:'',
            }
        }
    }
  },
  watch:{
        setAllKey(){
          if(this.setAllKey===true){
              this.configData.mac='';
          }
        }
  },
  computed:{},
  methods:{
       returnFn(){
            this.$router.go(-1)
        },
        changeProtoFn(){
            console.log(this.configData.common.proto)
            if(this.configData.common.proto==="MQTT"){
                this.configData.http={
                    hUrl:'',
                    httpUser:'',
                    httpPass:'',
                }
            }else if(this.configData.common.proto==="HTTP"){
                 this.configData.mqtt={
                    mUrl:'',
                    qos:0,
                    userName:'',
                    passWord:'',
                }
            }
        },

        saveConfigFn(){
            this.$Modal.confirm(
                {
                    title:'保存提示',
                    okText:'确定',
                    cancelText:'取消',
                    content:'<span style="color:#f94040">你确定要保存当前配置吗？</span>',
                    onOk:()=>{
                        console.log(this.configData)
                    },
                })
        }
  },
  created(){},
  mounted(){}
}
</script>

<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped>
</style>
<template>
  <div class='dust_create_wrap'>
      <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>
       <div class="hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">粉尘仪器新增</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>编号<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="createData.no" placeholder="仪器编号"/>
                            </Col>
                            <Col span="6">
                                <h3>名称</h3>
                                <Input v-model="createData.name" placeholder="仪器名称" />
                            </Col>
                           
                            <Col span="6">
                                <h3>mac地址<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="createData.mac" placeholder="mac地址" />
                            </Col>
                            <Col span="6">
                                <h3>所在位置</h3>
                                <Input v-model="createData.position" placeholder="所在位置" />
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
export default {
  name:'dust_create',
  components:{},
  props:{},
  data(){
    return {
        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

        createData:{
            no:'',
            name:'',
            position:'',
            mac:'',
          
        },
    }
  },
  watch:{},
  computed:{},
  methods:{
        returnFn(){
            this.$router.go(-1)
        },

        createFn(){
             if(this.createData.no.trim()&&this.createData.mac.trim()){

                //显示出正在上传
                this.uploadModal=true;
                // console.log(this.createData)
                var datas={
                    no:this.createData.no.trim(),
                    name:this.createData.name.trim(),
                    position:this.createData.position.trim(),
                    mac:this.createData.mac.trim(),
                }

                console.log(datas)
              
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
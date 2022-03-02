<template>
  <div class='dust_edit_wrap'>
      <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>
       <div class="hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">粉尘仪器编辑</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="6">
                                <h3>编号<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="editData.no" disabled placeholder="仪器编号"/>
                            </Col>
                            <Col span="6">
                                <h3>名称</h3>
                                <Input v-model="editData.name" placeholder="仪器名称" />
                            </Col>
                           
                            <Col span="6">
                                <h3>mac地址<span class="must_fill_info">（必填）</span></h3>
                                <Input v-model="editData.mac" placeholder="mac地址" />
                            </Col>
                            <Col span="6">
                                <h3>所在位置</h3>
                                <Input v-model="editData.position" placeholder="所在位置" />
                            </Col>
                            
                        </Row>
                        
                    </div>

                  

                  
                </div>
                <div class="submit_btn_wrap">
                       <Button type="primary" @click="saveFn">保存</Button>
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
  name:'dust_edit',
  components:{},
  props:{},
  data(){
    return {
        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

        editData:{
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

        saveFn(){
             if(this.editData.no.trim()&&this.editData.mac.trim()){

                //显示出正在上传
                this.uploadModal=true;
                // console.log(this.editData)
                var datas={
                    no:this.editData.no.trim(),
                    name:this.editData.name.trim(),
                    position:this.editData.position.trim(),
                    mac:this.editData.mac.trim(),
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
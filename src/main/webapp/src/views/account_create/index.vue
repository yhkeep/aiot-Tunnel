<template>
  <div class='account_create_wrap'>
      <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>

       <div class="hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">账户新增</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="8">
                                <h3>账户名</h3>
                                <Input readonly @on-focus="inputFocus" @on-blur="inputBlur"  v-model="createData.username" placeholder="账户名" />
                            </Col>
                             

                            <Col span="8">
                                <h3>密码</h3>
                                <Input readonly @on-focus="inputFocus" @on-blur="inputBlur"  type="password" v-model="createData.password" placeholder="密码"/>
                            </col>
                            <Col span="8">
                                <h3>手机号</h3>
                                <Input  v-model="createData.phone" placeholder="手机号" />
                            </Col>
                           
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="8">
                                <h3>所属部门</h3>
                                <Select v-model="createData.department">
                                    <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>申请科室</h3>
                                <Input v-model="createData.applyDept" placeholder="申请科室"/>
                            </col>
                           
                            <Col span="8">
                                <h3>账号级别</h3>
                                <Select v-model="createData.role">
                                    <Option v-for="item in roleData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </col>
                            
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            
                            <Col span="24">
                                <p ref="pass_error" class="error_msg hide">请输入6-20位密码</p>
                                <p ref="phone_error" class="error_msg hide">请输入正确的手机号</p>
                            </col>
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
import { Row,Col,Select,Option,Input,Button,Modal,Spin,Icon} from 'iview';
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Icon',Icon);
Vue.component('Spin',Spin);

import departData from "../../assets/data/depart.js"

export default {
  name:'account_create',
  components:{},
  props:{},
  data(){
    return {
         createData:{
            username:'',
            password:'',
            phone:'',
            department:'',
            role:'',
            applyDept:'',
        },

        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

        verifyPass:false,
        verifyPhone:false,

        departData:departData,

        roleData:[
            {
                value:'admin',label:'管理员',
                
            },
            {
                value:'operator',label:'操作员',
                
            },
        ]
    }
  },
  watch:{
      "createData.password"(){
          var reg=/^(\w){6,20}$/
          if(reg.test(this.createData.password)){
              this.$refs.pass_error.classList.add('hide')
              this.verifyPass=true;
          }else{
              this.$refs.pass_error.classList.remove('hide')
              this.verifyPass=false;
          }
      },
      "createData.phone"(){
          var reg=/^1[3456789]\d{9}$/
          if(reg.test(this.createData.phone)){
              this.$refs.phone_error.classList.add('hide')
              this.verifyPhone=true;
          }else{
              this.$refs.phone_error.classList.remove('hide')
              this.verifyPhone=false;
          }
      },
  },
  computed:{},
  methods:{
        returnFn(){
            this.$router.go(-1)
        },

        inputFocus(e){
            e.target.removeAttribute('readonly')
        },
        
        inputBlur(e){
            e.target.setAttribute('readonly',true)
        },
       

        initData(){
            this.createData={
                username:'',
                password:'',
                phone:'',
                department:'',
                role:'',
                applyDept:'',
            }
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

        createFn(){
            var k=0;
            for(var i in this.createData){
                if(this.createData[i].trim()===''){
                    k++
                }
            }

            if(k===0&&this.verifyPass===true&&this.verifyPhone===true){

                let datas={
                        "username":this.createData.username.trim(),
                        "password":this.createData.password.trim(),
                        "phone":this.createData.phone.trim(),
                        "department":this.createData.department.trim(),
                        "role":this.createData.role.trim(),
                        "applyDept":this.createData.applyDept.trim(),
                }
                this.uploadModal=true;

                this.$axios.post(process.env.API_HOST+'huaxi/user/add',datas)
                .then((res)=>{
                    if(res.data.msg==='ok'){
                        this.successFn();
                    }else if(res.data.msg==='failed'){
                        this.errorFn();
                    }
                })
                .catch((err)=>{
                    console.log(err)
                })
                
            }else{
                this.$Message.error({
                    content:'请填写完整账号信息!',
                    duration:1,
                })
            }
        },
  },
  created(){
      
  },
  mounted(){}
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped>
    @import '../../assets/css/account_create.css'
</style>
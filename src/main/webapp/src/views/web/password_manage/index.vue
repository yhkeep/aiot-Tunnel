<template>
  <div class='password_manage_wrap'>
      <div class="password_manage_box hover_animat">
          <div class="input_title">
              <h2>密码修改</h2>
          </div>
          <div class="input_item">
              <Input type="password" v-model="submitData.currentPassword" size="large" placeholder="请输入原密码" clearable class="password_input"/>
              <p ref="currPass_error" class="error_msg hide">请输入正确的密码格式(6-20位字符)</p>
          </div>
          <div class="input_item">
              <Input type="password" v-model="submitData.setPassword" size="large" placeholder="请输入更改后密码" clearable class="password_input"/>
              <p ref="setPass_error" class="error_msg hide">请输入正确的密码格式(6-20位字符)</p>
          </div>
          <div class="input_item">
              <Input type="password" v-model="certainPassword" size="large" placeholder="请确认更改后密码" clearable class="password_input" @on-enter="handleModifyFn"/>
              <p ref="cerPass_error" class="error_msg hide">两次输入密码不一致!</p>
          </div>
          <div >
              <Button  shape="circle" id="modify_btn" @click="handleModifyFn">确认修改</Button>
          </div>
          
      </div>

       <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
           
        <div style="text-align:center;padding:20px 0">
            <Spin fix v-if="!achieveUpload&&!errorUpload">
                <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                <div>正在修改</div>
            </Spin>
            <Spin fix v-if="achieveUpload" style="color:#00ad19">
                <Icon type="ios-checkmark-circle" size=18 />
                <div>修改成功</div>
            </Spin>
            <Spin fix v-if="errorUpload" style="color:#f72b2b">
                <Icon type="ios-close-circle" size=18 />
                <div>修改失败</div>
            </Spin>

        </div>
        
    </Modal>
  </div>
</template>

<script>
import Vue from "vue"
import {Input,Button,Modal,Icon,Spin} from 'iview';
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Icon',Icon);
Vue.component('Spin',Spin);

import { exitFn } from "../../../api/logout.js"
import NET_PORT from "../../../api/port.js"

export default {
  name:'password_manage',
  components:{},
  props:{},
  data(){
    return {
        username:this.$store.state.user,
        certainPassword:'',
        submitData:{
            currentPassword:'',
            setPassword:'',
        },

        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

        verifyCurrPass:false,
        verifySetPass:false,
        verifyCerPass:false,
    }
  },
  watch:{

       "submitData.currentPassword"(){
          var reg=/^(\w){6,20}$/
         if(reg.test(this.submitData.currentPassword)){
              this.$refs.currPass_error.classList.add('hide')
              this.verifyCurrPass=true;
          }else{
              this.$refs.currPass_error.classList.remove('hide')
              this.verifyCurrPass=false;
          }
      },
      "submitData.setPassword"(){
          var reg=/^(\w){6,20}$/
         if(reg.test(this.submitData.setPassword)){
              this.$refs.setPass_error.classList.add('hide')
              this.verifySetPass=true;
          }else{
              this.$refs.setPass_error.classList.remove('hide')
              this.verifySetPass=false;
          }
      },
      certainPassword(){
          if(this.submitData.setPassword.trim()===this.certainPassword.trim()){
              this.$refs.cerPass_error.classList.add('hide')
              this.verifyCerPass=true;
          }else{
              this.$refs.cerPass_error.classList.remove('hide')
              this.verifyCerPass=false;
          }
      }
  },
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

    modifyMySelfFn(){
        this.achieveUpload=true;
            setTimeout(()=>{
                this.uploadModal=false;
                setTimeout(()=>{
                        this.achieveUpload=false;
                        this.errorUpload=false;
                        this.$Modal.warning({
                            title: '提示！',
                            content: '当前登录用户密码已修改，需要重新登录！',
                            okText: '立即登录',
                            onOk: () => {
                                exitFn()
                            }
                        })
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

    handleModifyFn(){
        
        if(this.verifyCurrPass&&this.verifySetPass&&this.verifyCerPass){
                //走到这里表示密码格式填写都正确了，该发请求给服务器验证密码是否正确
                //密码修改后退出账号

                let datas={
                    "username":this.username,
                    "password":this.submitData.currentPassword.trim(),
                    "newpassword":this.submitData.setPassword.trim(),
                }
                this.uploadModal=true;
                this.$axios.post(NET_PORT.passwordUpdate,datas)
                .then((res)=>{
                    if(res.data.code===0){
                        this.successFn();
                        if(res.data.msg==='remove_myself'){
                            this.modifyMySelfFn();
                        }
                    }else{
                        this.errorFn();
                    }
                })
                .catch((err)=>{
                    // console.log(err)
                    this.errorFn();
                })
        }else{
            this.$Message.error({
                content:"密码填写有误",
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
    @import "../../../assets/scss/web/password_manage.scss";
</style>
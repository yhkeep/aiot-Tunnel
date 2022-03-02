<template>
  <div class='login_wrap'>
      <div class="header">
           <div class="header_box">
               <!-- <img src="../../../assets/img/logo.png" alt=""> -->
           </div>
       </div>
       <div class="content">
            <div class="content_box">
                <div class="img_box" :style="loginImg"></div>
                <div class="login_box">
                    <div class="col_wrap title_wrap">
                        <h1>医疗资产追踪管理系统</h1>
                    </div>
                    <div class="col_wrap account_wrap">
                        <Input v-model="account" size="large" placeholder="请输入帐户名" clearable class="account_input" @on-blur="inputBlurFn('account')"/>
                        <span ref="accountError" class="error_msg"></span>
                    </div>
                    <div class="col_wrap password_wrap">
                        <Input type="password" v-model="password" size="large" placeholder="请输入密码" clearable class="password_input" @on-blur="inputBlurFn('password')"/>
                        <span ref="passwordError" class="error_msg"></span>
                    </div>
                    <div class="col_wrap verification_wrap">
                        <div class="verification_wrap_top">
                            <div class="left_box">
                                <Input v-model="verification" size="large" placeholder="验证码" clearable class="verfication_code_input" @on-enter="handleLoginFn" @on-blur="inputBlurFn('verification')"/>
                            </div>
                            <div class="right_box" >
                                <h3 @click="updateCodeFn" onselectstart="return false"  ref="verfication_view_code"></h3>
                            </div>
                        </div>
                        <span ref="verificationError" class="error_msg"></span>
                    </div>
                    <div class="col_wrap">
                        <Button  shape="circle" :disabled="loginDisable" type="primary" id="login_btn" @click="handleLoginFn">登录</Button>
                    </div>
                </div>
            </div>
       </div>
       <div class="footer">

       </div>
  </div>
</template>

<script>
import Vue from "vue"
import { Input,Button,} from 'iview';
Vue.component('Input', Input);
Vue.component('Button', Button);
import { setToken } from '../../../utils/auth';
import NET_PORT from "../../../api/port.js"
export default {
  name:'login',
  components:{},
  props:{},
  data(){
    return {
        account:'',
        password:'',
        verification:'',
        loginDisable:false,
        loginBg:require('../../../assets/img/login_bg.jpg')
    }
  },
  watch:{},
  computed:{
      loginImg(){
          return "background-image:url('"+this.loginBg+"')"
      }
  },
  methods:{
      inputBlurFn(type){
            let msgObj={
                accountMsg:'账号不能为空',
                passwordMsg:'密码不能为空',
                verificationMsg:'验证码不正确',
            }

            if(type==='verification'){

                if(this[type].trim().toUpperCase() !== this.$refs.verfication_view_code.innerText.toUpperCase()){
                    this.$refs[type+'Error'].classList.add('show');
                    this.$refs[type+'Error'].innerText=msgObj[type+'Msg']

                }else{
                    this.$refs[type+'Error'].classList.remove('show');
                    this.$refs[type+'Error'].innerText=""
                }

            }else{
                if(this[type].trim()===''){
                    this.$refs[type+'Error'].classList.add('show');
                    this.$refs[type+'Error'].innerText=msgObj[type+'Msg']
                }else{
                    this.$refs[type+'Error'].classList.remove('show');
                    this.$refs[type+'Error'].innerText=""
                }
            }
      },

      updateCodeFn(){
        const allStr='0123456789abcdefghijklmnopqrstuvwxyz';
        let str='';
        for(let i=0;i<4;i++){
            str+=allStr[parseInt(Math.random()*allStr.length)]
        }
        this.$refs.verfication_view_code.innerText=str;
      },

      handleLoginFn(){
        this.inputBlurFn('account')
        this.inputBlurFn('password')
        this.inputBlurFn('verification')

        if(!this.account.trim()||!this.password.trim()||!this.verification.trim()||!this.verification.trim()||this.verification.trim().toUpperCase() !== this.$refs.verfication_view_code.innerText.toUpperCase()){
                this.$Message.error({
                        content:"请正确填写完必要信息！",
                        duration:2,
                })
        }else{

            //将按钮禁用，避免重复点击发送请求
            this.loginDisable=true;

            let data={
                username:this.account.trim(),
                password:this.password.trim()
            }

            this.$axios.post(NET_PORT.login,data)
            .then((res)=>{
                        if(res.data.token){
                            let token=res.data.token;
                            let user=res.data.user;
                            let project='';
                            let page='';
                            if(!user.authority){
                                this.$Message.error({
                                        content:"该账号没有权限！",
                                        duration:2,
                                })
                                this.updateCodeFn();
                                this.loginDisable=false;
                                return 
                            }

                            setToken(token)
                            
                            this.$store.commit('setUser',user.username);
                            this.$store.commit('setRole',user.role);
                            this.$store.commit('setDepartmentroom',user.departmentroom ? user.departmentroom : '');
                            // this.$store.commit('setLocDept',user.locDept ? user.locDept : '');
                            this.$store.commit('setAddress',user.address);
                            // console.log(user.authority,JSON.parse(user.authority))
                            // this.$store.commit('setAuthority',[1,2,3,4,5,6,7,8,9,])
                            this.$store.commit('setAuthority',JSON.parse(user.authority))
                            this.$store.commit('setRoutes',user.role)
                            
                           
                            let queryData={};
                            let chooseData={};
                            for(let i in user){
                                if(i.substring(0,6)==='memory'){
                                    queryData[i]=user[i];
                                }else if(i.substring(0,6)==='isShow'){
                                    chooseData[i]=user[i];
                                }
                            }
                           
                            this.$store.commit('setQueryData',queryData)
                            this.$store.commit('setChooseData',chooseData)

                            switch(user.address){
                                case '1' : 
                                    project='华西医院';
                                    page=this.$store.state.routes[1].children[0].path
                                break;
                                default:
                                    return;
                            }
                            this.$store.commit('setProject',project)
                            
                            this.$router.addRoutes(this.$store.state.routes)

                            this.$Message.success({
                                content:"登录成功!",
                                duration:0.5,
                            });

                            

                            setTimeout(()=>{
                                this.$router.push(
                                    {
                                        path:page
                                    }
                                )
                            },500)

                        }else if(res.data.message){
                            this.$Message.error({
                                    content:res.data.message,
                                    duration:2,
                            })
                            
                            this.updateCodeFn();
                            this.loginDisable=false;
                        }
            })
            .catch((err)=>{
                this.$Message.error({
                        content:"登录错误！",
                        duration:2,
                })
                this.updateCodeFn();
                this.loginDisable=false;
            })

        }

      },
  },
  created(){},
  mounted(){
    
    this.updateCodeFn();
    
  }
}
</script>
<style lang="scss" scoped>
    @import "../../../assets/scss/web/login.scss"
</style>
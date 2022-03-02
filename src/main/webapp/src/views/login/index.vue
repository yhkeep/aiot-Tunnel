<template>
  <div class='login_wrap'>
      <div class="header">
           <div class="header_box">
               <img src="../../assets/img/logo.png" alt="">
           </div>
       </div>
       <div class="content">
            <div class="content_box">
                    <div class="img_box">
                    </div>
                <div class="login_box">
                            <div class="col_wrap title_wrap">
                                <h1>医疗资产追踪管理系统</h1>
                            </div>
                                <div class="col_wrap account_wrap">
                                    <Input v-model="account" size="large" placeholder="请输入帐户名" clearable class="account_input" @on-blur="accountCheckFn"/>
                                    <span class="error_msg"></span>
                                </div>
                                <div class="col_wrap password_wrap">
                                    <Input type="password" v-model="password" size="large" placeholder="请输入密码" clearable class="password_input" @on-blur="passwordCheckFn"/>
                                    <span class="error_msg"></span>
                                </div>
                                
                                <div class="col_wrap verification_wrap">
                                   <div class="verification_wrap_top">
                                        <div class="left_box">
                                            <Input v-model="verification_code" size="large" placeholder="验证码" clearable class="verfication_code_input" @on-enter="handleLoginFn" @on-blur="verificationCheckFn"/>
                                        </div>
                                        <div class="right_box" >
                                            <h3 @click="changeCodeFn" onselectstart="return false"  ref="verfication_view_code"></h3>
                                        </div>
                                   </div>
                                    <span class="error_msg"></span>
                                </div>

                                <div class="col_wrap">
                                    <Button type="primary" id="login_btn" @click="handleLoginFn">登录</Button>
                                </div>
                        </div>
                </div>
       </div>
       <div class="footer">
           Copyright © 2017-2018 伊可查科技有限公司 | 地址:成都市高新区天益街38号理想中心3栋520室
       </div>
  </div>
</template>

<script>
import Vue from "vue"
import { Input,Button,} from 'iview';
Vue.component('Input', Input);
Vue.component('Button', Button);
import { setToken } from '../../utils/auth'
export default {
  name:'login',
  components:{},
  props:{},
  data(){
    return {
        account:'',
        password:'',
        verification_code:'',
    }
  },
  watch:{},
  computed:{},
  methods:{

      accountCheckFn(){
            if(this.account.trim()===''){
                document.getElementsByClassName('account_wrap')[0].getElementsByClassName('error_msg')[0].classList.add('show');
                document.getElementsByClassName('account_wrap')[0].getElementsByClassName('error_msg')[0].innerText="账号不能为空"
            }else{
                document.getElementsByClassName('account_wrap')[0].getElementsByClassName('error_msg')[0].classList.remove('show');
                document.getElementsByClassName('account_wrap')[0].getElementsByClassName('error_msg')[0].innerText=""
            }
      },
      passwordCheckFn(){
            if(this.password.trim()===''){
                document.getElementsByClassName('password_wrap')[0].getElementsByClassName('error_msg')[0].classList.add('show');
                document.getElementsByClassName('password_wrap')[0].getElementsByClassName('error_msg')[0].innerText="密码不能为空"
            }else{
                document.getElementsByClassName('password_wrap')[0].getElementsByClassName('error_msg')[0].classList.remove('show');
                document.getElementsByClassName('password_wrap')[0].getElementsByClassName('error_msg')[0].innerText=""
            }
      },
      verificationCheckFn(){
            if(this.verification_code.trim().toUpperCase() !== this.$refs.verfication_view_code.innerText.toUpperCase()){
                document.getElementsByClassName('verification_wrap')[0].getElementsByClassName('error_msg')[0].classList.add('show');
                document.getElementsByClassName('verification_wrap')[0].getElementsByClassName('error_msg')[0].innerText="验证码不正确"
            }else{
                document.getElementsByClassName('verification_wrap')[0].getElementsByClassName('error_msg')[0].classList.remove('show');
                document.getElementsByClassName('verification_wrap')[0].getElementsByClassName('error_msg')[0].innerText=""
            }
      },

      changeCodeFn(){
          this.updateCodeFn();
      },

      updateCodeFn(){
        var codeLength=4;
        var arr=[1, 2, 3, 4, 5, 6, 7, 8, 9, 0,'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
        var str='';
        for(var i=0;i<codeLength;i++){
            str+=arr[parseInt(Math.random()*arr.length)]
        }
        this.$refs.verfication_view_code.innerText=str;
      },

      handleLoginFn(){

        if (!this.account) {
            return this.$Message.error({
                      content:"请输入账号！",
                      duration:2,
            })
        }
        else if (!this.password) {
            return this.$Message.error({
                      content:"请输入密码！",
                      duration:2,
            })
        }
        else if (!this.verification_code) {
            return this.$Message.error({
                      content:"请输入验证码！",
                      duration:2,
            })
        }
        else if (this.verification_code.trim().toUpperCase() !== this.$refs.verfication_view_code.innerText.toUpperCase()) {
            return this.$Message.error({
                      content:"验证码错误！",
                      duration:2,
            })
        }



        //使用网络账号登录
        var data={
            username:this.account,
            password:this.password
        }
        this.$axios.post(process.env.API_HOST+'login',data)
        .then((res)=>{
                   
            console.log(res)
                    if(res.data.token){
                        var token=res.data.token;
                        var user=res.data.user;
                        var project='';
                        var page='';
                        setToken(token)
                        // window.sessionStorage.setItem('token',token);
                        this.$store.commit('setUser',user.username);
                        this.$store.commit('setRole',user.role);
                        this.$store.commit('setAddress',user.address);
                        this.$store.commit('setRoutes',user.role,user.address)
                        var queryData={
                            department:user.department,
                            applyDept:user.applyDept,
                            isShowMac:user.isShowMac,
                            isShowBuyDate:user.isShowBuyDate,
                            isShowMoney:user.isShowMoney,
                            isShowElectric:user.isShowElectric,
                        }
                        this.$store.commit('setQueryData',queryData)
                        switch(user.address){
                            case '1' : 
                                project='华西医院';
                                page='home' 
                            break;
                            case '2' : 
                                project='泸县第二人民医院';
                                page='temp_manage'
                            break;
                            case '3' : 
                                project='郫都区第二人民医院';
                                page='home'
                        }
                        this.$store.commit('setProject',project)
                        
                        // console.log(this.$store.state.routes)
                        this.$router.addRoutes(this.$store.state.routes)

                        

                        this.$Message.success({
                            content:"登录成功!",
                            duration:0.5,
                        });
                        // console.log(page)
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
                    }
        })
        .catch((err)=>{
            console.log(err)
            this.$Message.error({
                      content:"登录错误！",
                      duration:2,
            })
            this.updateCodeFn();
        })




      },
  },
  created(){},
  mounted(){
       this.$nextTick(()=>{
          this.updateCodeFn();
      })
  }
}
</script>
<style scoped>
    @import "../../assets/css/login.css"
</style>

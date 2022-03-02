<template>
  <div id='mobile_login_wrap'>
        <div class="login_logo_box">
            <Cell >
                <!-- <img src="../../../assets/img/logo.png" alt=""> -->
                <h2>医疗资产追踪管理系统</h2>
            </Cell>
        </div>
    
        <div class="login_form_box">
            <div class="login_form_item">
                <CellGroup :border=false>
                    <Field :border=false clearable  v-model="username" placeholder="请输入用户名" @focus="inputFocusFn('userName')" @blur="inputBlurFn('userName')"></Field>
                    <div class="input_border_box">
                        <div class="input_border_line"  ref="userNameBorder"></div>
                    </div>
                </CellGroup>
            </div>
            <div class="login_form_item">
                <CellGroup :border=false>
                    <Field :border=false clearable type="password"  v-model="password" placeholder="请输入密码" @focus="inputFocusFn('password')" @blur="inputBlurFn('password')"></Field>
                    <div class="input_border_box">
                        <div class="input_border_line"  ref="passwordBorder"></div>
                    </div>
                </CellGroup>
            </div>
            <div class="login_form_item">
                <CellGroup :border=false>
                    <Row>
                        <Col span="16">
                            <Field :border=false clearable   v-model="verificationCode" placeholder="请输入验证码" @focus="inputFocusFn('verificationCode')" @blur="inputBlurFn('verificationCode')"></Field>
                            <div class="input_border_box">
                                <div class="input_border_line"  ref="verificationCodeBorder"></div>
                            </div>
                        </Col>
                        <Col span="8">
                            <div class="verification_code_box">
                                <h3 @click="updateCodeFn" onselectstart="return false"  ref="verfication_view_code"></h3>
                            </div>
                        </Col>
                    </Row>
                </CellGroup>
            </div>
            <div class="login_form_item">
                <CellGroup :border=false>
                    <Cell :border=false>
                        <Button round :disabled="loginDisable"  class="submit_button" color="#2153a3" @click="handleLoginFn">登录</Button>
                    </Cell>
                </CellGroup>
            </div>
        </div>
        
  </div>
</template>

<script>
import {Row,Col,CellGroup,Cell,Field,Button,Toast,Notify} from 'vant';
import 'vant/lib/field/style';
import 'vant/lib/cell-group/style';
import 'vant/lib/cell/style';
import 'vant/lib/button/style';
import 'vant/lib/row/style';
import 'vant/lib/col/style';
import 'vant/lib/toast/style';
import 'vant/lib/notify/style';
import { setToken } from '../../../utils/auth';
import NET_PORT from "../../../api/port.js"
export default {
  name:'mobile_login',
  components:{
      Row,
      Col,
      CellGroup,
      Cell,
      Field,
      Button,
      Toast,
      Notify
  },
  props:{},
  data(){
    return {
        username:'',
        password:'',
        verificationCode:'',
        loginDisable:false,
    }
  },
  watch:{},
  computed:{},
  methods:{
  
    inputFocusFn(type){
        this.$refs[type+'Border'].classList.add('focus')
    },

    inputBlurFn(type){
        this.$refs[type+'Border'].classList.remove('focus')
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

        if (!this.username.trim()) {
          this.$toast.fail('请输入账号！');
        }
        else if (!this.password.trim()) {
           this.$toast.fail('请输入密码！');
        }
        else if (!this.verificationCode.trim()) {
           this.$toast.fail('请输入验证码！');
        }
        else if (this.verificationCode.trim().toUpperCase() !== this.$refs.verfication_view_code.innerText.toUpperCase()) {
           this.$toast.fail('验证码错误！');
        }else{

                //将按钮禁用，避免重复点击发送请求
                this.loginDisable=true;

                //使用网络账号登录
                let data={
                    username:this.username.trim(),
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
                                    this.$notify({
                                        type:'danger',
                                        message: '该账号没有权限！',
                                        duration: 2000
                                    });
                                    
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
                                this.$store.commit('setAuthority',JSON.parse(user.authority))
                                this.$store.commit('setRoutes',user.role,user.address)
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
                                        page=this.$store.state.routes[2].children[0].children[0].path
                                    break;
                                    default:
                                        return;
                                }
                                this.$store.commit('setProject',project)
                                
                                this.$router.addRoutes(this.$store.state.routes)

                                this.$notify({
                                    type:'success',
                                    message: '登录成功！',
                                    duration: 500
                                });

                                setTimeout(()=>{
                                    this.$router.push(
                                        {
                                            path:page
                                        }
                                    )
                                },500)

                            }else if(res.data.message){
                                this.$notify({
                                    type:'danger',
                                    message: res.data.message,
                                    duration: 2000
                                });
                                this.updateCodeFn();
                                this.loginDisable=false;
                            }
                })
                .catch((err)=>{
                    this.$notify({
                        type:'danger',
                        message: '登录错误！',
                        duration: 2000
                    });
                    
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
<style lang="scss">
  @import "../../../assets/scss/mobile/login.scss"
</style>
<template>
  <div class='account_detail_wrap'>
       <div class="hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">账户明细</h2>
                <div class="form_wrap_veritical">
                    <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>账户名</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                            <Input readonly disabled placeholder="账户名" style="width:200px"/>
                        </div>
                        <div class="input_item_right">
                            <span ref="notice_username"></span>
                            <span :class="createData.warning.username ? 'error_msg' : ''">{{createData.text.username}}</span>
                        </div>
                    </div>
                   
                    <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>手机号</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                            <Input  v-model="createData.data.phone"  @on-blur="inputBlurCheckFn('createData','phone')" placeholder="手机号" style="width:200px"/>
                        </div>
                        <div class="input_item_right">
                            <span :class="createData.warning.phone ? 'error_msg' : ''">{{createData.text.phone}}</span>
                        </div>
                    </div>
                    <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>部门名称</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                           
                             <AutoComplete
                                v-model="createData.data.departmentroom"
                                @on-focus="inputFocusFn('departmentroom')"
                                @on-blur="inputBlurFn"
                                @on-search="handleSearchFn"
                                placeholder="部门名称"
                                style="width:200px">
                                <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                            </AutoComplete>
                        </div>
                        <div class="input_item_right">
                            <span :class="createData.warning.departmentroom ? 'error_msg' : ''">{{createData.text.departmentroom}}</span>
                        </div>
                    </div>
                    <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>所在科室名称</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                          
                            <AutoComplete
                                v-model="createData.data.locDept"
                                @on-focus="inputFocusFn('locDept')"
                                @on-blur="inputBlurFn"
                                @on-search="handleSearchFn"
                                placeholder="所在科室名称"
                                style="width:200px">
                                <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                            </AutoComplete>
                        </div>
                        <div class="input_item_right">
                           <span :class="createData.warning.locDept ? 'error_msg' : ''">{{createData.text.locDept}}</span>
                        </div>
                    </div>
                    <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>账号级别</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                            <Select v-model="createData.data.role" @on-change="inputBlurCheckFn('createData','role')" style="width:200px">
                                <Option v-for="item in roleData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                        </div>
                        <div class="input_item_right">
                            <span :class="createData.warning.role ? 'error_msg' : ''">{{createData.text.role}}</span>
                        </div>
                    </div>
                    <!-- <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>模块权限</h5>
                        </div>
                        <div class="input_item_center">
                            <CheckboxGroup v-model="createData.data.authority">
                                <Checkbox v-for="(item,index) in authorityArr"  :label="item.code" :key="index" :disabled="item.code==='1'">
                                    <span>{{item.text}}</span>
                                </Checkbox>
                            </CheckboxGroup>
                        </div>
                        <div class="input_item_right">
                            
                        </div>
                    </div> -->

                </div>
                <div class="submit_btn_wrap left">
                       <Button type="primary" shape="circle" @click="createFn">保存修改</Button>
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
import { Row,Col,Select,Option,Input,Button,Modal,Spin,Icon,AutoComplete,CheckboxGroup,Checkbox} from 'iview';
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Icon',Icon);
Vue.component('Spin',Spin);
Vue.component('AutoComplete',AutoComplete);
Vue.component('CheckboxGroup',CheckboxGroup);
Vue.component('Checkbox',Checkbox);

import NET_PORT from "../../../api/port.js"
export default {
  name:'account_detail',
  components:{},
  props:{},
  data(){
    return {
        username:'',
        authorityArr:[
            {code:'1',text:'资产首页'},
            {code:'2',text:'资产管理'},
            {code:'3',text:'设备管理'},
            {code:'4',text:'温湿度管理'},
            {code:'5',text:'电子围栏'},
            {code:'6',text:'维修/报废/调用'},
            {code:'7',text:'盘点管理'},
            {code:'8',text:'网关管理'},
            {code:'9',text:'账户管理'},
        ],
        inputData:[],
        inputDataAll:[],
      
         createData:{
            data:{
                username:'',
                phone:'',
                departmentroom:'',
                role:'',
                locDept:'',
                // authority:['1',],
            },
            verify:{
                username:false,
                phone:false,
                departmentroom:false,
                role:false,
                locDept:false,
            },
            text:{
                username:'',
                phone:'',
                departmentroom:'',
                role:'管理员拥有资产删除权限，操作员没有',
                locDept:'',
            },
            warning:{
                username:false,
                phone:false,
                departmentroom:false,
                role:false,
                locDept:false,
            },

        },

        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

        passwordVerify:false,
        phoneVerify:false,


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
     
  },
  computed:{},
  methods:{

       handleSearchFn(data){
            this.inputData = this.inputDataAll.filter(item => item.toLowerCase().indexOf(data.toLowerCase()) > -1);
        },

        inputBlurFn(){
            this.inputDataAll=[];
            this.inputData=[];
        },

        inputFocusFn(key){
            this.inputDataAll=[];
            this.inputData=[];
            let queryArr=[];
            let obj={};
            obj[key]="1"
            queryArr.push(obj)
            this.$axios.post(NET_PORT.inputQuery,queryArr)
            .then((res)=>{
                res.data.data.forEach((item,index)=>{
                
                    if(this.inputDataAll.find((result)=>{return result===item[key]})){

                    }else{
                        this.inputDataAll.push(item[key])
                    }
                })

                setTimeout(()=>{
                    this.inputData=JSON.parse(JSON.stringify(this.inputDataAll))
                },300)
            })
            .catch((err)=>{
                console.log(err)
            })
                
        },
       

        inputFocus(e){
            e.target.removeAttribute('readonly')
        },
        
        inputBlur(e){
            e.target.setAttribute('readonly',true)
        },

        inputErrorFn(reg,type){
            if(reg.test(this.createData[type])){
                this.$refs[type+"_error"].classList.add('hide')
                this[type+'Verify']=true;
            }else{
                this.$refs[type+"_error"].classList.remove('hide')
                this[type+'Verify']=false;
            }
        },

        inputBlurCheckFn(obj,key){
                var obj=obj
                var name=key;
                var text="";
                var warning=false;
               if(key==='username'){
                    if(this[obj].data.username.trim()===''){
                        text='账户名不能为空！'
                        warning=true;
                        this[obj].verify[name]=false
                    }else{
                        let usernameReg=/^[(a-zA-Z0-9\u4e00-\u9fa5){1}_#]{4,8}$/;
                        if(usernameReg.test(this[obj].data.username.trim())){
                            text=''
                            warning=false;
                            this[obj].verify[name]=true
                        }else{
                            text='请输入正确格式的账户名！'
                            warning=true;
                            this[obj].verify[name]=false
                        }
                       
                    }

                }else if(key==="phone"){
                    
                    if(this[obj].data.phone.trim()===''){
                        text='手机号不能为空！'
                        warning=true;
                        this[obj].verify[name]=false
                    
                    }else {
                        
                        var phoneReg=/^1[3456789]\d{9}$/;
                        if(phoneReg.test(this[obj].data.phone.trim())){
                        
                            text=''
                            warning=false;
                            this[obj].verify[name]=true
                        }else{
                        
                            text='请输入正确格式的手机号！'
                            warning=true;
                            this[obj].verify[name]=false
                        }
                    }
                }else if(key==='departmentroom'){
                    if(this[obj].data.departmentroom.trim()===''){
                    
                        text='部门名称不能为空！'
                        warning=true;
                        this[obj].verify[name]=false
                    }else{
                        text=''
                        warning=false;
                        this[obj].verify[name]=true
                    }
                
                }else if(key==='locDept'){
                    if(this[obj].data.locDept.trim()===''){
                    
                        text='所在科室名称不能为空！'
                        warning=true;
                        this[obj].verify[name]=false
                    }else{
                        text=''
                        warning=false;
                        this[obj].verify[name]=true
                    }
                
                }else if(key==='role'){
                    if(this[obj].data.role.trim()===''){
                    
                        text='账户级别不能为空！'
                        warning=true;
                        this[obj].verify[name]=false
                    }else{
                        text=''
                        warning=false;
                        this[obj].verify[name]=true
                    }
                }
                this[obj].text[name]=text;
                if(warning){
                    this[obj].warning[name]=true
                }else{
                    this[obj].warning[name]=false
                }
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

        createFn(){

                this.inputBlurCheckFn('createData','username')
                this.inputBlurCheckFn('createData','phone')
                this.inputBlurCheckFn('createData','departmentroom')
                this.inputBlurCheckFn('createData','locDept')
                this.inputBlurCheckFn('createData','role')

                var verifyErrorNum=0;
                for(var k in this.createData.verify){
                    if(!this.createData.verify[k]){
                        verifyErrorNum++;
                    }
                }

            if(!verifyErrorNum){

                let datas={
                        "username":this.createData.data.username.trim(),
                        "phone":this.createData.data.phone.trim(),
                        "departmentroom":this.createData.data.departmentroom.trim(),
                        "role":this.createData.data.role.trim(),
                        "locDept":this.createData.data.locDept.trim(),
                }
                this.uploadModal=true;

               
                
            }else{
                this.$Message.error({
                    content:'请填写完整账户信息！',
                    duration:1,
                })
            }
        },

        getDataFn(){

        }
  },
  created(){
        this.username=this.$route.params.id;
        if(!this.username){
            this.$Message.error({
                content:"未找到账户信息!",
                duration:2,
            })

            setTimeout(()=>{
                this.$router.go(-1)
            },2000)
        }

  },
  mounted(){
      this.getDataFn();
  }
}
</script>
<style lang='scss' scoped>
    @import '../../../assets/scss/web/common/form_data.scss';
    @import '../../../assets/scss/web/account_create.scss';
</style>
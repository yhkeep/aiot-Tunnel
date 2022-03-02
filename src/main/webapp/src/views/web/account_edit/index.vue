<template>
  <div class='account_edit_wrap'>
        <div class="hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">账户编辑</h2>
                <div class="form_wrap_veritical">
                    <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>账户名</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                            <Input disabled readonly @on-focus="inputFocus" @on-blur="inputBlurCheckFn('editData','username')"  v-model="editData.data.username" placeholder="账户名" style="width:200px"/>
                        </div>
                        <div class="input_item_right">
                            <span ref="notice_username"></span>
                            <span :class="editData.warning.username ? 'error_msg' : ''">{{editData.text.username}}</span>
                        </div>
                    </div>
                   
                    <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>手机号</h5>
                            <!-- <span class="must_fill_info">*</span> -->
                        </div>
                        <div class="input_item_center">
                            <Input  v-model="editData.data.phone"   placeholder="手机号" style="width:200px"/>
                        </div>
                        <div class="input_item_right">
                            <!-- <span :class="editData.warning.phone ? 'error_msg' : ''">{{editData.text.phone}}</span> -->
                        </div>
                    </div>
                    <div class="input_item clearfix" style="height:auto;">
                        <div class="input_item_left">
                            <h5>可授权部门</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                           <!-- <Input  v-model="editData.data.departmentroom"   placeholder="手动填写部门名称" style="width:500px">
                                <Button slot="append">已存在部门列表</Button>
                           </Input> -->
                            <Select v-model="editData.data.departmentroom" filterable multiple style="min-width:200px;" placeholder="可多选">
                                <Option v-for="item in departmentroomArr" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                             <!-- <AutoComplete
                                v-model="editData.data.departmentroom"
                                @on-focus="inputFocusFn"
                                @on-blur="inputBlurFn"
                                @on-search="handleSearchFn"
                                placeholder="管辖部门"
                                multiple
                                style="width:200px">
                                <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                            </AutoComplete> -->
                        </div>
                        <div class="input_item_right">
                            <span :class="editData.warning.departmentroom ? 'error_msg' : ''">{{editData.text.departmentroom}}</span>
                        </div>
                    </div>
                    <!-- <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>所在科室名称</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                          
                            <AutoComplete
                                v-model="editData.data.locDept"
                                @on-focus="inputFocusFn('locDept')"
                                @on-blur="inputBlurFn"
                                @on-search="handleSearchFn"
                                placeholder="所在科室名称"
                                :disabled="locDept!==''"
                                style="width:200px">
                                <Option v-for="item in inputData" :value="item" :key="item">{{ item }}</Option>
                            </AutoComplete>
                        </div>
                        <div class="input_item_right">
                           <span :class="editData.warning.locDept ? 'error_msg' : ''">{{editData.text.locDept}}</span>
                        </div>
                    </div> -->
                    <div class="input_item clearfix">
                        <div class="input_item_left">
                            <h5>账户级别</h5>
                            <span class="must_fill_info">*</span>
                        </div>
                        <div class="input_item_center">
                            <Select v-model="editData.data.role" @on-change="inputBlurCheckFn('editData','role')" style="width:200px">
                                <Option v-for="item in roleData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                            </Select>
                        </div>
                        <div class="input_item_right">
                            <span :class="editData.warning.role ? 'error_msg' : ''">{{editData.text.role}}</span>
                        </div>
                    </div>
                    <div class="check_item">
                        <div class="check_item_left">
                            <h5>模块权限</h5>
                        </div>
                        <div class="check_item_right">
                            <CheckboxGroup v-model="editData.data.authority">
                                <Checkbox v-for="(item,index) in authorityArr"  :label="item.code" :key="index" >
                                    <span>{{item.text}}</span>
                                </Checkbox>
                            </CheckboxGroup>
                        </div>
                       
                    </div>
                    <!-- <div class="check_item">
                        <div class="check_item_left">
                            <h5>接口权限</h5>
                        </div>
                        <div class="check_item_right">
                            <CheckboxGroup v-model="editData.data.authority">
                                <Checkbox v-for="(item,index) in rolePathArr"  :label="item.code" :key="index" >
                                    <span>{{item.text}}</span>
                                </Checkbox>
                            </CheckboxGroup>
                        </div>
                    </div> -->
                </div>
                <div class="submit_btn_wrap left">
                       <Button type="primary" shape="circle" @click="editFn">保存修改</Button>
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
  name:'account_edit',
  components:{},
  props:{},
  data(){
    return {
        username:'',
        authorityArr:[
            {
                code:1,
                text:'资产首页',
                children:[
                    {code:'/huaxi/assetManagement/param',text:'资产查询'},
                    {code:'/websocket',text:'设备动态数据'},
                    {code:'/huaxi/statement',text:'资产统计数据'},
                    {code:'/user/update',text:'账号密码修改'},
                ]
            },
            {
                code:2,
                text:'资产管理',
                children:[
                    {code:'/huaxi/assetManagement/param',text:'资产查询'},
                    {code:'/huaxi/upload',text:'资产新增'},
                    {code:'/huaxi/del',text:'资产删除'},
                    {code:'/huaxi/update',text:'资产更新'},
                    {code:'/huaxi/poi/export',text:'导出资产表'},
                    {code:'/uploadimage',text:'资产图片上传'},
                    {code:'/showImage',text:'显示设备正面图片'},
                    {code:'/showImageLeft',text:'显示设备左面图片'},
                    {code:'/showImageRight',text:'显示设备右面图片'},
                    {code:'/showImageAbove',text:'显示设备顶面图片'},
                    {code:'/showImageAllround',text:'显示设备背面图片'},
                    {code:'/showImagePaperlabel',text:'显示设备铭牌图片'},
                    {code:'/showImageOnecodelable',text:'显示资产标签图片'},
                    {code:'/huaxi/assetmarked',text:'资产数据模糊查询'},
                    {code:'/user/update',text:'账号密码修改'},
                ]
            },
            {
                code:3,
                text:'设备管理',
                children:[
                    {code:'/huaxi/assertChecked',text:'资产盘点'},
                    {code:'/map/websocket',text:'地图定位数据'},
                    {code:'/huaxi/getGateway',text:'网关查询'},
                    {code:'/user/update',text:'账号密码修改'},
                ],
            },
            {
                code:4,
                text:'温湿度管理',
                children:[
                    {code:'/luzhou/hum',text:'温湿度查询'},
                    {code:'/luzhou/daySummary',text:'温湿度图表数据'},
                    {code:'/luzhou/assetManagement/param',text:'温湿度历史数据'},
                    {code:'/luzhou/section',text:'温湿度编辑数据'},
                    {code:'/luzhou/edit',text:'温湿度编辑更新数据'},
                    {code:'/huaxi/addHumiture',text:'温湿度仪器新增'},
                    {code:'/huaxi/delHumiture',text:'温湿度仪器删除'},
                    {code:'/hum/websocket',text:'温湿度明细数据'},
                    {code:'/hum/warn/websocket',text:'温湿度告警数据'},
                    {code:'/user/update',text:'账号密码修改'},
                    {code:'/huaxi/hygrothermograph/export',text:'温湿度导出'},
                ]
            },
            {
                code:5,
                text:'电子围栏',
                children:[
                    {code:'/websocket',text:'设备动态数据'},
                    {code:'/user/update',text:'账号密码修改'},
                ]
            },
            {
                code:6,
                text:'维修/报废/调用',
                children:[
                    {code:'/huaxi/assetmarked',text:'资产数据模糊查询'},
                    {code:'/huaxi/assetManagement/param',text:'资产查询'},
                    {code:'/maintainhistory/uploadimage',text:'维修登记'},
                    {code:'/lendouthistory/uploadimage',text:'外借登记'},
                    {code:'/maintainhistory/query',text:'维修/外借历史'},
                    {code:'/user/update',text:'账号密码修改'},
                ]
            },
            {
                code:7,
                text:'盘点管理',
                children:[
                    {code:'/huaxi/assetmarked',text:'资产数据模糊查询'},
                    {code:'/huaxi/assetManagement/param',text:'资产查询'},
                    {code:'/map/websocket',text:'地图定位数据'},
                    {code:'/huaxi/assertChecked',text:'资产盘点'},
                    {code:'/huaxi/assertChecked/History',text:'资产盘点历史'},
                    {code:'/huaxi/assertCheckedDetail',text:'资产盘点明细'},
                    {code:'/user/update',text:'账号密码修改'},
                ]
            },
            {
                code:8,
                text:'网关管理',
                children:[
                    {code:'/huaxi/editGateway',text:'保存网关配置'},
                    {code:'/huaxi/updateGateway',text:'网关参数更新'},
                    {code:'/huaxi/deleteGateway',text:'网关删除'},
                    {code:'/huaxi/addGateway',text:'网关新增'},
                    {code:'/huaxi/gateway',text:'重启指定网关'},
                    {code:'/huaxi/allGateway',text:'重启所有网关'},
                    {code:'/huaxi/getGateway',text:'网关查询'},
                    {code:'/user/update',text:'账号密码修改'},
                ]
            },
            {
                code:9,
                text:'账户管理',
                children:[
                    {code:'/huaxi/user/query',text:'账号查询'},
                    {code:'/huaxi/user/del',text:'账号删除'},
                    {code:'/huaxi/user/add',text:'账号创建'},
                    {code:'/huaxi/departmentType',text:'授权部门权限'},
                    {code:'/user/update',text:'账号密码修改'},
                ]
            },
        ],
        rolePathArr:[
            {code:'/huaxi/assetmarked',text:'资产数据模糊查询'},
            {code:'/huaxi/assetManagement/param',text:'资产查询'},
            {code:'/huaxi/statement',text:'资产统计数据'},
            {code:'/huaxi/upload',text:'资产新增'},
            {code:'/huaxi/del',text:'资产删除'},
            {code:'/huaxi/update',text:'资产更新'},
            {code:'/huaxi/poi/export',text:'导出资产表'},
            {code:'/uploadimage',text:'资产图片上传'},
            {code:'/showImage',text:'显示设备正面图片'},
            {code:'/showImageLeft',text:'显示设备左面图片'},
            {code:'/showImageRight',text:'显示设备右面图片'},
            {code:'/showImageAbove',text:'显示设备顶面图片'},
            {code:'/showImageAllround',text:'显示设备背面图片'},
            {code:'/showImagePaperlabel',text:'显示设备铭牌图片'},
            {code:'/showImageOnecodelable',text:'显示资产标签图片'},

            {code:'/websocket',text:'设备动态数据'},
            
            {code:'/huaxi/editGateway',text:'保存网关配置'},
            {code:'/huaxi/updateGateway',text:'网关参数更新'},
            {code:'/huaxi/deleteGateway',text:'网关删除'},
            {code:'/huaxi/addGateway',text:'网关新增'},
            {code:'/huaxi/gateway',text:'重启指定网关'},
            {code:'/huaxi/allGateway',text:'重启所有网关'},
            {code:'/huaxi/getGateway',text:'网关查询'},

            {code:'/huaxi/user/query',text:'账号查询'},
            {code:'/huaxi/user/del',text:'账号删除'},
            {code:'/huaxi/user/add',text:'账号创建'},

            {code:'/map/websocket',text:'地图定位数据'},

            {code:'/user/update',text:'账号密码修改'},
            

            {code:'/luzhou/hum',text:'温湿度查询'},
            {code:'/luzhou/daySummary',text:'温湿度图表数据'},
            {code:'/luzhou/assetManagement/param',text:'温湿度历史数据'},
            {code:'/luzhou/section',text:'温湿度编辑数据'},
            {code:'/luzhou/edit',text:'温湿度编辑更新数据'},
            {code:'/huaxi/addHumiture',text:'温湿度仪器新增'},
            {code:'/huaxi/delHumiture',text:'温湿度仪器删除'},
            {code:'/hum/websocket',text:'温湿度明细数据'},
            {code:'/hum/warn/websocket',text:'温湿度告警数据'},
           
            
            {code:'/huaxi/assertChecked',text:'资产盘点'},
            {code:'/huaxi/assertChecked/History',text:'资产盘点历史'},
            {code:'/huaxi/assertCheckedDetail',text:'资产盘点明细'},
            {code:'/maintainhistory/uploadimage',text:'维修登记'},
            {code:'/lendouthistory/uploadimage',text:'外借登记'},
            {code:'/maintainhistory/query',text:'维修/外借历史'},
           
            {code:'/huaxi/departmentType',text:'授权部门权限'},
        ],

        departmentroomArr:[],
      
         editData:{
            data:{
                username:'',
                password:'',
                phone:'',
                departmentroom:[],
                role:'',
                // locDept:'',
                authority:[],
                rolepath:[],
            },
            verify:{
                username:false,
                // password:false,
                // phone:false,
                departmentroom:false,
                role:false,
                // locDept:false,
            },
            text:{
                username:'请输入4-8位字符，可以包含中文/字母/数字',
                // password:'请输入6-20位密码',
                // phone:'',
                departmentroom:'',
                role:'管理员拥有资产删除权限，操作员没有',
                // locDept:'',
            },
            warning:{
                username:false,
                // password:false,
                // phone:false,
                departmentroom:false,
                role:false,
                // locDept:false,
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

      inputFocus(e){
            e.target.removeAttribute('readonly')
        },
        
        inputBlur(e){
            e.target.setAttribute('readonly',true)
        },

        inputErrorFn(reg,type){
            if(reg.test(this.editData[type])){
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

                }
                // else if(key==="phone"){
                    
                //     if(this[obj].data.phone.trim()===''){
                //         text='手机号不能为空！'
                //         warning=true;
                //         this[obj].verify[name]=false
                    
                //     }else {
                        
                //         var phoneReg=/^1[3456789]\d{9}$/;
                //         if(phoneReg.test(this[obj].data.phone.trim())){
                        
                //             text=''
                //             warning=false;
                //             this[obj].verify[name]=true
                //         }else{
                        
                //             text='请输入正确格式的手机号！'
                //             warning=true;
                //             this[obj].verify[name]=false
                //         }
                //     }
                // }
                else if(key==="password"){
                    if(this[obj].data.password.trim()===''){
                    
                        text='密码不能为空！'
                        warning=true;
                        this[obj].verify[name]=false
                    }else{
                        var passwordReg=/^(\w){6,20}$/;
                        if(passwordReg.test(this[obj].data.password.trim())){
                        
                            text=''
                            warning=false;
                            this[obj].verify[name]=true
                        }else{
                        
                            text='请输入6-20位密码'
                            warning=true;
                            this[obj].verify[name]=false
                        }
                    }
                }else if(key==='departmentroom'){
                    if(this[obj].data.departmentroom.length<=0){
                    
                        text='可授权部门至少需要一个'
                        warning=true;
                        this[obj].verify[name]=false
                    }else{
                        text=''
                        warning=false;
                        this[obj].verify[name]=true
                    }
                
                }
                // else if(key==='locDept'){
                //     if(this[obj].data.locDept.trim()===''){
                    
                //         text='所在科室名称不能为空！'
                //         warning=true;
                //         this[obj].verify[name]=false
                //     }else{
                //         text=''
                //         warning=false;
                //         this[obj].verify[name]=true
                //     }
                
                // }
                else if(key==='role'){
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

        editFn(){

            this.inputBlurCheckFn('editData','username')
            // this.inputBlurCheckFn('editData','phone')
            this.inputBlurCheckFn('editData','departmentroom')
            // this.inputBlurCheckFn('editData','locDept')
            this.inputBlurCheckFn('editData','role')

            var verifyErrorNum=0;
            for(var k in this.editData.verify){
                if(!this.editData.verify[k]){
                    verifyErrorNum++;
                }
            }

            if(!verifyErrorNum){
                if(!this.editData.data.authority.length){
                    this.$Message.error({
                        content:'模块权限至少需要一个',
                        duration:1,
                    })
                }else{
                    let rolePath=[];
                    for(let i=0;i<this.editData.data.authority.length;i++){
                        this.authorityArr.forEach((item,index)=>{
                            if(this.editData.data.authority[i]===item.code){
                                item.children.forEach((m,n)=>{
                                    if(!rolePath.find(path=>path===m.code)){
                                        rolePath.push(m.code)
                                    }
                                })
                            }
                        })
                    }

                    let datas={
                            "username":this.editData.data.username.trim(),
                            // "password":'',
                            // "newpassword":'',
                            "phone":this.editData.data.phone.trim(),
                            "departmentroom":this.editData.data.departmentroom.join(','),
                            "role":this.editData.data.role.trim(),
                            "authority":JSON.stringify(this.editData.data.authority),
                            "rolepath":rolePath.join(','),
                    }
                    this.uploadModal=true;

                    this.$axios.post(NET_PORT.accountEdit,datas)
                    .then((res)=>{
                        if(res.data.code===0){
                            this.successFn();
                        }else{
                            this.errorFn();
                            this.$Message.error({
                                content:res.data.msg,
                                duration:2,
                            })
                        }
                    })
                    .catch((err)=>{
                        this.$Message.error({
                            content:'创建用户失败！',
                            duration:1,
                        })
                    })
                }
            }else{
                this.$Message.error({
                    content:'请填写完整账户信息！',
                    duration:1,
                })
            }
        },

        getDepartmentRoomFn(){
            let obj={
                address:'1',
            };
            this.$axios.post(NET_PORT.departmentList,obj)
            .then((res)=>{
                let arr=[];
                res.data.data.forEach((item,index)=>{
                   arr.push(
                       {
                           value:item.departmentroom,
                           label:item.departmentroom,
                       }
                   )
                })
                this.departmentroomArr=arr
            })
            .catch((err)=>{
                console.log(err)
            })
        },

        getDataFn(){
            this.$Loading.start();
            this.$axios.get(NET_PORT.accountQuery)
            .then((res)=>{
                // console.log(res)
                let obj=res.data.find((item,index)=>{
                    return item.username===this.username
                })

                if(obj){
                    this.editData.data.username=obj.username;
                    this.editData.data.phone=obj.phone;
                    this.editData.data.role=obj.role;
                    this.editData.data.authority=JSON.parse(obj.authority)                                            
                    this.editData.data.departmentroom=obj.departmentroom.split(',')                                           
                    this.$Loading.finish();
                }else{
                    this.$Loading.error();
                    this.$Message.error({
                        content:"未找到账户信息!",
                        duration:2,
                    })

                    setTimeout(()=>{
                        this.$router.go(-1)
                    },2000) 
                }
            })
            .catch((err)=>{
                this.$Loading.error();
            })
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
      this.getDepartmentRoomFn();
      this.getDataFn();
  }
}
</script>
<style lang='scss' scoped>
    @import '../../../assets/scss/web/common/form_data.scss';
    @import '../../../assets/scss/web/account_create.scss';
</style>
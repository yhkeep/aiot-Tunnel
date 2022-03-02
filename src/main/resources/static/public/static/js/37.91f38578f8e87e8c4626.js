webpackJsonp([37],{DOR9:function(s,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=e("wdqF"),r=e("EMb8"),i=e("8sRp"),o=e("KxKW"),n=e("JtJ0"),c=e("7+uW"),d=e("xWT1"),l=e("wDsp");c.a.component("Input",n.a),c.a.component("Button",o.a),c.a.component("Modal",i.a),c.a.component("Icon",r.a),c.a.component("Spin",a.a);var u={name:"password_manage",components:{},props:{},data:function(){return{username:this.$store.state.user,certainPassword:"",submitData:{currentPassword:"",setPassword:""},uploadModal:!1,achieveUpload:!1,errorUpload:!1,verifyCurrPass:!1,verifySetPass:!1,verifyCerPass:!1}},watch:{"submitData.currentPassword":function(){/^(\w){6,20}$/.test(this.submitData.currentPassword)?(this.$refs.currPass_error.classList.add("hide"),this.verifyCurrPass=!0):(this.$refs.currPass_error.classList.remove("hide"),this.verifyCurrPass=!1)},"submitData.setPassword":function(){/^(\w){6,20}$/.test(this.submitData.setPassword)?(this.$refs.setPass_error.classList.add("hide"),this.verifySetPass=!0):(this.$refs.setPass_error.classList.remove("hide"),this.verifySetPass=!1)},certainPassword:function(){this.submitData.setPassword.trim()===this.certainPassword.trim()?(this.$refs.cerPass_error.classList.add("hide"),this.verifyCerPass=!0):(this.$refs.cerPass_error.classList.remove("hide"),this.verifyCerPass=!1)}},computed:{},methods:{returnFn:function(){this.$router.go(-1)},successFn:function(){var s=this;this.achieveUpload=!0,setTimeout(function(){s.uploadModal=!1,setTimeout(function(){s.achieveUpload=!1,s.errorUpload=!1,s.$router.go(-1)},500)},1e3)},modifyMySelfFn:function(){var s=this;this.achieveUpload=!0,setTimeout(function(){s.uploadModal=!1,setTimeout(function(){s.achieveUpload=!1,s.errorUpload=!1,s.$Modal.warning({title:"提示！",content:"当前登录用户密码已修改，需要重新登录！",okText:"立即登录",onOk:function(){Object(d.a)()}})},500)},1e3)},errorFn:function(){var s=this;this.errorUpload=!0,setTimeout(function(){s.uploadModal=!1,setTimeout(function(){s.achieveUpload=!1,s.errorUpload=!1},500)},1e3)},handleModifyFn:function(){var s=this;if(this.verifyCurrPass&&this.verifySetPass&&this.verifyCerPass){var t={username:this.username,password:this.submitData.currentPassword.trim(),newpassword:this.submitData.setPassword.trim()};this.uploadModal=!0,this.$axios.post(l.a.passwordUpdate,t).then(function(t){"ok"===t.data.msg?s.successFn():"remove_myself"===t.data.msg?s.modifyMySelfFn():"failed"===t.data.msg&&s.errorFn()}).catch(function(s){})}else this.$Message.error({content:"密码填写有误",duration:2})}},created:function(){},mounted:function(){}},p={render:function(){var s=this,t=s.$createElement,e=s._self._c||t;return e("div",{staticClass:"password_manage_wrap"},[e("div",{staticClass:"password_manage_box hover_animat"},[s._m(0),s._v(" "),e("div",{staticClass:"input_item"},[e("Input",{staticClass:"password_input",attrs:{type:"password",size:"large",placeholder:"请输入原密码",clearable:""},model:{value:s.submitData.currentPassword,callback:function(t){s.$set(s.submitData,"currentPassword",t)},expression:"submitData.currentPassword"}}),s._v(" "),e("p",{ref:"currPass_error",staticClass:"error_msg hide"},[s._v("请输入正确的密码格式(6-20位字符)")])],1),s._v(" "),e("div",{staticClass:"input_item"},[e("Input",{staticClass:"password_input",attrs:{type:"password",size:"large",placeholder:"请输入更改后密码",clearable:""},model:{value:s.submitData.setPassword,callback:function(t){s.$set(s.submitData,"setPassword",t)},expression:"submitData.setPassword"}}),s._v(" "),e("p",{ref:"setPass_error",staticClass:"error_msg hide"},[s._v("请输入正确的密码格式(6-20位字符)")])],1),s._v(" "),e("div",{staticClass:"input_item"},[e("Input",{staticClass:"password_input",attrs:{type:"password",size:"large",placeholder:"请确认更改后密码",clearable:""},on:{"on-enter":s.handleModifyFn},model:{value:s.certainPassword,callback:function(t){s.certainPassword=t},expression:"certainPassword"}}),s._v(" "),e("p",{ref:"cerPass_error",staticClass:"error_msg hide"},[s._v("两次输入密码不一致!")])],1),s._v(" "),e("div",{staticClass:"input_item"},[e("Button",{attrs:{type:"error",shape:"circle",id:"modify_btn"},on:{click:s.handleModifyFn}},[s._v("确认修改")])],1)]),s._v(" "),e("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:s.uploadModal,callback:function(t){s.uploadModal=t},expression:"uploadModal"}},[e("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[s.achieveUpload||s.errorUpload?s._e():e("Spin",{attrs:{fix:""}},[e("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),s._v(" "),e("div",[s._v("正在修改")])],1),s._v(" "),s.achieveUpload?e("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),s._v(" "),e("div",[s._v("修改成功")])],1):s._e(),s._v(" "),s.errorUpload?e("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),s._v(" "),e("div",[s._v("修改失败")])],1):s._e()],1)])],1)},staticRenderFns:[function(){var s=this.$createElement,t=this._self._c||s;return t("div",{staticClass:"input_title"},[t("h2",[this._v("密码修改")])])}]};var v=e("VU/8")(u,p,!1,function(s){e("yFjZ")},"data-v-3474c432",null);t.default=v.exports},yFjZ:function(s,t){}});
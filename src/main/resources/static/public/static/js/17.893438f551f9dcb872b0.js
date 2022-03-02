webpackJsonp([17],{qyUT:function(e,o,t){"use strict";Object.defineProperty(o,"__esModule",{value:!0});var s=t("Fd2+"),i=(t("wvM5"),t("QP/A"),t("cnGM"),t("FDxC"),t("jgNZ"),t("dKGA"),t("GKy3"),t("4T1P"),t("TIfe")),r=t("wDsp"),a={name:"mobile_login",components:{Row:s.t,Col:s.g,CellGroup:s.d,Cell:s.c,Field:s.j,Button:s.b,Toast:s.A,Notify:s.o},props:{},data:function(){return{username:"",password:"",verificationCode:"",loginDisable:!1}},watch:{},computed:{},methods:{inputFocusFn:function(e){this.$refs[e+"Border"].classList.add("focus")},inputBlurFn:function(e){this.$refs[e+"Border"].classList.remove("focus")},updateCodeFn:function(){for(var e="0123456789abcdefghijklmnopqrstuvwxyz",o="",t=0;t<4;t++)o+=e[parseInt(Math.random()*e.length)];this.$refs.verfication_view_code.innerText=o},handleLoginFn:function(){var e=this;if(this.username.trim())if(this.password.trim())if(this.verificationCode.trim())if(this.verificationCode.trim().toUpperCase()!==this.$refs.verfication_view_code.innerText.toUpperCase())this.$toast.fail("验证码错误！");else{var o={username:this.username.trim(),password:this.password.trim()};this.$axios.post(r.a.login,o).then(function(o){if(o.data.token){var t=o.data.token,s=o.data.user,r="",a="";Object(i.c)(t),e.$store.commit("setUser",s.username),e.$store.commit("setRole",s.role),e.$store.commit("setDepartmentroom",s.departmentroom?s.departmentroom:""),e.$store.commit("setLocDept",s.locDept?s.locDept:""),e.$store.commit("setAddress",s.address),e.$store.commit("setRoutes",s.role,s.address);var n={memorylocDept:s.memorylocDept,memorydepart:s.memorydepart,memoryoneclassify:s.memoryoneclassify,memorysecondclassify:s.memorysecondclassify,memorythreeclassify:s.memorythreeclassify,memoryfourclassify:s.memoryfourclassify},c={isShowMac:s.isShowMac,isShowBuyDate:s.isShowBuyDate,isShowMoney:s.isShowMoney,isShowElectric:s.isShowElectric,isShowSpecification:s.isShowSpecification,isShowType:s.isShowType,isShowLocation:s.isShowLocation,isShowPlaceoforigin:s.isShowPlaceoforigin,isShowBrand:s.isShowBrand,isShowDepartmentcode:s.isShowDepartmentcode,isShowDepartmentroom:s.isShowDepartmentroom,isShowHomeofficenumber:s.isShowHomeofficenumber,isShowHomeofficename:s.isShowHomeofficename,isShowIsentrance:s.isShowIsentrance,isShowSuppliername:s.isShowSuppliername,isShowGeneratebusinessname:s.isShowGeneratebusinessname,isShowApplyoddnumbers:s.isShowApplyoddnumbers,isShowLocDept:s.isShowLocDept,isShowStatus:s.isShowStatus};switch(e.$store.commit("setQueryData",n),e.$store.commit("setChooseData",c),s.address){case"1":r="华西医院",a="/mobile/pane";break;default:return}e.$store.commit("setProject",r),e.$router.addRoutes(e.$store.state.routes),e.$notify({type:"success",message:"登录成功！",duration:500}),e.loginDisable=!0,setTimeout(function(){e.$router.push({path:a})},500)}else o.data.message&&(e.$notify({type:"danger",message:o.data.message,duration:2e3}),e.updateCodeFn())}).catch(function(o){e.$notify({type:"danger",message:"登录错误！",duration:2e3}),e.updateCodeFn()})}else this.$toast.fail("请输入验证码！");else this.$toast.fail("请输入密码！");else this.$toast.fail("请输入账号！")}},created:function(){},mounted:function(){this.updateCodeFn()}},n={render:function(){var e=this,o=e.$createElement,s=e._self._c||o;return s("div",{attrs:{id:"mobile_login_wrap"}},[s("div",{staticClass:"login_logo_box"},[s("Cell",[s("img",{attrs:{src:t("dLd/"),alt:""}}),e._v(" "),s("h2",[e._v("华西医疗资产管理系统")])])],1),e._v(" "),s("div",{staticClass:"login_form_box"},[s("div",{staticClass:"login_form_item"},[s("CellGroup",{attrs:{border:!1}},[s("Field",{attrs:{border:!1,clearable:"",placeholder:"请输入用户名"},on:{focus:function(o){return e.inputFocusFn("userName")},blur:function(o){return e.inputBlurFn("userName")}},model:{value:e.username,callback:function(o){e.username=o},expression:"username"}}),e._v(" "),s("div",{staticClass:"input_border_box"},[s("div",{ref:"userNameBorder",staticClass:"input_border_line"})])],1)],1),e._v(" "),s("div",{staticClass:"login_form_item"},[s("CellGroup",{attrs:{border:!1}},[s("Field",{attrs:{border:!1,clearable:"",type:"password",placeholder:"请输入密码"},on:{focus:function(o){return e.inputFocusFn("password")},blur:function(o){return e.inputBlurFn("password")}},model:{value:e.password,callback:function(o){e.password=o},expression:"password"}}),e._v(" "),s("div",{staticClass:"input_border_box"},[s("div",{ref:"passwordBorder",staticClass:"input_border_line"})])],1)],1),e._v(" "),s("div",{staticClass:"login_form_item"},[s("CellGroup",{attrs:{border:!1}},[s("Row",[s("Col",{attrs:{span:"16"}},[s("Field",{attrs:{border:!1,clearable:"",placeholder:"请输入验证码"},on:{focus:function(o){return e.inputFocusFn("verificationCode")},blur:function(o){return e.inputBlurFn("verificationCode")}},model:{value:e.verificationCode,callback:function(o){e.verificationCode=o},expression:"verificationCode"}}),e._v(" "),s("div",{staticClass:"input_border_box"},[s("div",{ref:"verificationCodeBorder",staticClass:"input_border_line"})])],1),e._v(" "),s("Col",{attrs:{span:"8"}},[s("div",{staticClass:"verification_code_box"},[s("h3",{ref:"verfication_view_code",attrs:{onselectstart:"return false"},on:{click:e.updateCodeFn}})])])],1)],1)],1),e._v(" "),s("div",{staticClass:"login_form_item"},[s("CellGroup",{attrs:{border:!1}},[s("Cell",{attrs:{border:!1}},[s("Button",{staticClass:"submit_button",attrs:{disabled:e.loginDisable,round:"",color:"#1d50a2"},on:{click:e.handleLoginFn}},[e._v("登录")])],1)],1)],1)])])},staticRenderFns:[]};var c=t("VU/8")(a,n,!1,function(e){t("x62M")},null,null);o.default=c.exports},x62M:function(e,o){}});
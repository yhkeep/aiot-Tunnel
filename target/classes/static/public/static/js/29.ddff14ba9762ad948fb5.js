webpackJsonp([29],{IEe5:function(t,a){},URdx:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var i=e("mvHQ"),s=e.n(i),r=e("p8P9"),n=e("wdqF"),o=e("EMb8"),c=e("8sRp"),l=e("KxKW"),u=e("JtJ0"),p=e("X2sI"),d=e("LuSo"),h=e("NAz/"),_=e("a84E"),v=e("7+uW"),m=e("wDsp"),f=e("Uo+3"),D=e("kitu");v.a.component("Row",_.a),v.a.component("Col",h.a),v.a.component("Select",d.a),v.a.component("Option",p.a),v.a.component("Input",u.a),v.a.component("Button",l.a),v.a.component("Modal",c.a),v.a.component("Icon",o.a),v.a.component("Spin",n.a),v.a.component("AutoComplete",r.a);var C={name:"account_create",components:{},props:{},data:function(){return{inputData:[],inputDataAll:[],departCode:f.a,locDept:D.a,createData:{data:{username:"",password:"",phone:"",departmentroom:"",role:"",locDept:""},verify:{username:!1,password:!1,phone:!1,departmentroom:!1,role:!1,locDept:!1},text:{username:"",password:"请输入6-20位密码",phone:"",departmentroom:"",role:"",locDept:""},warning:{username:!1,password:!1,phone:!1,departmentroom:!1,role:!1,locDept:!1}},uploadModal:!1,achieveUpload:!1,errorUpload:!1,passwordVerify:!1,phoneVerify:!1,roleData:[{value:"admin",label:"管理员"},{value:"operator",label:"操作员"}]}},watch:{"createData.password":function(){this.inputErrorFn(/^(\w){6,20}$/,"password")},"createData.phone":function(){this.inputErrorFn(/^1[3456789]\d{9}$/,"phone")}},computed:{},methods:{handleSearchFn:function(t){this.inputData=this.inputDataAll.filter(function(a){return a.toLowerCase().indexOf(t.toLowerCase())>-1})},inputBlurFn:function(){this.inputDataAll=[],this.inputData=[]},inputFocusFn:function(t){var a=this;this.inputDataAll=[],this.inputData=[];var e=[],i={};i[t]="1",e.push(i),this.$axios.post(m.a.inputQuery,e).then(function(e){e.data.data.forEach(function(e,i){a.inputDataAll.find(function(a){return a===e[t]})||a.inputDataAll.push(e[t])}),a.inputData=JSON.parse(s()(a.inputDataAll))}).catch(function(t){console.log(t)})},inputFocus:function(t){t.target.removeAttribute("readonly")},inputBlur:function(t){t.target.setAttribute("readonly",!0)},inputErrorFn:function(t,a){t.test(this.createData[a])?(this.$refs[a+"_error"].classList.add("hide"),this[a+"Verify"]=!0):(this.$refs[a+"_error"].classList.remove("hide"),this[a+"Verify"]=!1)},inputBlurCheckFn:function(t,a){t=t;var e=a,i="",s=!1;if("username"===a)""===this[t].data.username.trim()?(i="用户名不能为空！",s=!0,this[t].verify[e]=!1):(i="",s=!1,this[t].verify[e]=!0);else if("phone"===a)if(""===this[t].data.phone.trim())i="手机号不能为空！",s=!0,this[t].verify[e]=!1;else{/^1[3456789]\d{9}$/.test(this[t].data.phone.trim())?(i="",s=!1,this[t].verify[e]=!0):(i="请输入正确格式的手机号！",s=!0,this[t].verify[e]=!1)}else if("password"===a)if(""===this[t].data.password.trim())i="密码不能为空！",s=!0,this[t].verify[e]=!1;else{/^(\w){6,20}$/.test(this[t].data.password.trim())?(i="",s=!1,this[t].verify[e]=!0):(i="请输入6-20位密码",s=!0,this[t].verify[e]=!1)}else"departmentroom"===a?""===this[t].data.departmentroom.trim()?(i="部门名称不能为空！",s=!0,this[t].verify[e]=!1):(i="",s=!1,this[t].verify[e]=!0):"locDept"===a?""===this[t].data.locDept.trim()?(i="所在科室名称不能为空！",s=!0,this[t].verify[e]=!1):(i="",s=!1,this[t].verify[e]=!0):"role"===a&&(""===this[t].data.role.trim()?(i="账户级别不能为空！",s=!0,this[t].verify[e]=!1):(i="",s=!1,this[t].verify[e]=!0));this[t].text[e]=i,this[t].warning[e]=!!s},successFn:function(){var t=this;this.achieveUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1,t.$router.go(-1)},500)},1e3)},errorFn:function(){var t=this;this.errorUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1},500)},1e3)},createFn:function(){var t=this;this.inputBlurCheckFn("createData","username"),this.inputBlurCheckFn("createData","phone"),this.inputBlurCheckFn("createData","password"),this.inputBlurCheckFn("createData","departmentroom"),this.inputBlurCheckFn("createData","locDept"),this.inputBlurCheckFn("createData","role");var a=0;for(var e in this.createData.verify)this.createData.verify[e]||a++;if(console.log(a),a)this.$Message.error({content:"请填写完整账号信息！",duration:1});else{var i={username:this.createData.data.username.trim(),password:this.createData.data.password.trim(),phone:this.createData.data.phone.trim(),departmentroom:this.createData.data.departmentroom.trim(),role:this.createData.data.role.trim(),locDept:this.createData.data.locDept.trim()};this.uploadModal=!0,this.$axios.post(m.a.accountCreate,i).then(function(a){"ok"===a.data.msg?t.successFn():"failed"===a.data.msg&&t.errorFn()}).catch(function(a){t.$Message.error({content:"创建请求失败！",duration:1})})}}},created:function(){},mounted:function(){}},w={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"account_create_wrap"},[e("div",{staticClass:"hover_animat custom_bg_color_white"},[e("div",{staticClass:"row_box"},[e("h2",{staticClass:"row_title"},[t._v("账户新增")]),t._v(" "),e("div",{staticClass:"form_wrap_veritical"},[e("div",{staticClass:"input_item clearfix"},[t._m(0),t._v(" "),e("div",{staticClass:"input_item_center"},[e("Input",{staticStyle:{width:"200px"},attrs:{readonly:"",placeholder:"账户名"},on:{"on-focus":t.inputFocus,"on-blur":function(a){return t.inputBlurCheckFn("createData","username")}},model:{value:t.createData.data.username,callback:function(a){t.$set(t.createData.data,"username",a)},expression:"createData.data.username"}})],1),t._v(" "),e("div",{staticClass:"input_item_right"},[e("span",{ref:"notice_username"}),t._v(" "),e("span",{class:t.createData.warning.username?"error_msg":""},[t._v(t._s(t.createData.text.username))])])]),t._v(" "),e("div",{staticClass:"input_item clearfix"},[t._m(1),t._v(" "),e("div",{staticClass:"input_item_center"},[e("Input",{staticStyle:{width:"200px"},attrs:{readonly:"",type:"password",placeholder:"密码"},on:{"on-focus":t.inputFocus,"on-blur":function(a){return t.inputBlurCheckFn("createData","password")}},model:{value:t.createData.data.password,callback:function(a){t.$set(t.createData.data,"password",a)},expression:"createData.data.password"}})],1),t._v(" "),e("div",{staticClass:"input_item_right"},[e("span",{class:t.createData.warning.password?"error_msg":""},[t._v(t._s(t.createData.text.password))])])]),t._v(" "),e("div",{staticClass:"input_item clearfix"},[t._m(2),t._v(" "),e("div",{staticClass:"input_item_center"},[e("Input",{staticStyle:{width:"200px"},attrs:{placeholder:"手机号"},on:{"on-blur":function(a){return t.inputBlurCheckFn("createData","phone")}},model:{value:t.createData.data.phone,callback:function(a){t.$set(t.createData.data,"phone",a)},expression:"createData.data.phone"}})],1),t._v(" "),e("div",{staticClass:"input_item_right"},[e("span",{class:t.createData.warning.phone?"error_msg":""},[t._v(t._s(t.createData.text.phone))])])]),t._v(" "),e("div",{staticClass:"input_item clearfix"},[t._m(3),t._v(" "),e("div",{staticClass:"input_item_center"},[e("AutoComplete",{staticStyle:{width:"200px"},attrs:{placeholder:"部门名称"},on:{"on-focus":function(a){return t.inputFocusFn("departmentroom")},"on-blur":t.inputBlurFn,"on-search":t.handleSearchFn},model:{value:t.createData.data.departmentroom,callback:function(a){t.$set(t.createData.data,"departmentroom",a)},expression:"createData.data.departmentroom"}},t._l(t.inputData,function(a){return e("Option",{key:a,attrs:{value:a}},[t._v(t._s(a))])}),1)],1),t._v(" "),e("div",{staticClass:"input_item_right"},[e("span",{class:t.createData.warning.departmentroom?"error_msg":""},[t._v(t._s(t.createData.text.departmentroom))])])]),t._v(" "),e("div",{staticClass:"input_item clearfix"},[t._m(4),t._v(" "),e("div",{staticClass:"input_item_center"},[e("AutoComplete",{staticStyle:{width:"200px"},attrs:{placeholder:"所在科室名称"},on:{"on-focus":function(a){return t.inputFocusFn("locDept")},"on-blur":t.inputBlurFn,"on-search":t.handleSearchFn},model:{value:t.createData.data.locDept,callback:function(a){t.$set(t.createData.data,"locDept",a)},expression:"createData.data.locDept"}},t._l(t.inputData,function(a){return e("Option",{key:a,attrs:{value:a}},[t._v(t._s(a))])}),1)],1),t._v(" "),e("div",{staticClass:"input_item_right"},[e("span",{class:t.createData.warning.locDept?"error_msg":""},[t._v(t._s(t.createData.text.locDept))])])]),t._v(" "),e("div",{staticClass:"input_item clearfix"},[t._m(5),t._v(" "),e("div",{staticClass:"input_item_center"},[e("Select",{staticStyle:{width:"200px"},on:{"on-change":function(a){return t.inputBlurCheckFn("createData","role")}},model:{value:t.createData.data.role,callback:function(a){t.$set(t.createData.data,"role",a)},expression:"createData.data.role"}},t._l(t.roleData,function(a){return e("Option",{key:a.value,attrs:{value:a.value}},[t._v(t._s(a.label))])}),1)],1),t._v(" "),e("div",{staticClass:"input_item_right"},[e("span",{class:t.createData.warning.role?"error_msg":""},[t._v(t._s(t.createData.text.role))])])])]),t._v(" "),e("div",{staticClass:"submit_btn_wrap left"},[e("Button",{attrs:{type:"primary",shape:"circle"},on:{click:t.createFn}},[t._v("确定新增")])],1)])]),t._v(" "),e("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:t.uploadModal,callback:function(a){t.uploadModal=a},expression:"uploadModal"}},[e("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[t.achieveUpload||t.errorUpload?t._e():e("Spin",{attrs:{fix:""}},[e("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),t._v(" "),e("div",[t._v("正在保存")])],1),t._v(" "),t.achieveUpload?e("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),t._v(" "),e("div",[t._v("保存成功")])],1):t._e(),t._v(" "),t.errorUpload?e("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),t._v(" "),e("div",[t._v("保存失败")])],1):t._e()],1)])],1)},staticRenderFns:[function(){var t=this.$createElement,a=this._self._c||t;return a("div",{staticClass:"input_item_left"},[a("h5",[this._v("账户名")]),this._v(" "),a("span",{staticClass:"must_fill_info"},[this._v("*")])])},function(){var t=this.$createElement,a=this._self._c||t;return a("div",{staticClass:"input_item_left"},[a("h5",[this._v("密码")]),this._v(" "),a("span",{staticClass:"must_fill_info"},[this._v("*")])])},function(){var t=this.$createElement,a=this._self._c||t;return a("div",{staticClass:"input_item_left"},[a("h5",[this._v("手机号")]),this._v(" "),a("span",{staticClass:"must_fill_info"},[this._v("*")])])},function(){var t=this.$createElement,a=this._self._c||t;return a("div",{staticClass:"input_item_left"},[a("h5",[this._v("部门名称")]),this._v(" "),a("span",{staticClass:"must_fill_info"},[this._v("*")])])},function(){var t=this.$createElement,a=this._self._c||t;return a("div",{staticClass:"input_item_left"},[a("h5",[this._v("所在科室名称")]),this._v(" "),a("span",{staticClass:"must_fill_info"},[this._v("*")])])},function(){var t=this.$createElement,a=this._self._c||t;return a("div",{staticClass:"input_item_left"},[a("h5",[this._v("账号级别")]),this._v(" "),a("span",{staticClass:"must_fill_info"},[this._v("*")])])}]};var y=e("VU/8")(C,w,!1,function(t){e("IEe5")},"data-v-32b733b6",null);a.default=y.exports}});
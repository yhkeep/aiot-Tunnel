webpackJsonp([19],{FanE:function(t,e){},gCML:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=a("mvHQ"),i=a.n(o),r=a("2nDa"),s=a("/qMM"),d=a("p8P9"),n=a("wdqF"),c=a("EMb8"),u=a("8sRp"),l=a("KxKW"),h=a("JtJ0"),m=a("X2sI"),x=a("LuSo"),p=a("NAz/"),v=a("a84E"),_=a("7+uW"),f=a("wDsp");_.a.component("Row",v.a),_.a.component("Col",p.a),_.a.component("Select",x.a),_.a.component("Option",m.a),_.a.component("Input",h.a),_.a.component("Button",l.a),_.a.component("Modal",u.a),_.a.component("Icon",c.a),_.a.component("Spin",n.a),_.a.component("AutoComplete",d.a),_.a.component("CheckboxGroup",s.a),_.a.component("Checkbox",r.a);var y={name:"account_edit",components:{},props:{},data:function(){return{username:"",authorityArr:[{code:1,text:"资产首页",children:[{code:"/huaxi/assetManagement/param",text:"资产查询"},{code:"/websocket",text:"设备动态数据"},{code:"/huaxi/statement",text:"资产统计数据"},{code:"/user/update",text:"账号密码修改"}]},{code:2,text:"资产管理",children:[{code:"/huaxi/assetManagement/param",text:"资产查询"},{code:"/huaxi/upload",text:"资产新增"},{code:"/huaxi/del",text:"资产删除"},{code:"/huaxi/update",text:"资产更新"},{code:"/huaxi/poi/export",text:"导出资产表"},{code:"/uploadimage",text:"资产图片上传"},{code:"/showImage",text:"显示设备正面图片"},{code:"/showImageLeft",text:"显示设备左面图片"},{code:"/showImageRight",text:"显示设备右面图片"},{code:"/showImageAbove",text:"显示设备顶面图片"},{code:"/showImageAllround",text:"显示设备背面图片"},{code:"/showImagePaperlabel",text:"显示设备铭牌图片"},{code:"/showImageOnecodelable",text:"显示资产标签图片"},{code:"/huaxi/assetmarked",text:"资产数据模糊查询"},{code:"/user/update",text:"账号密码修改"}]},{code:3,text:"设备管理",children:[{code:"/huaxi/assertChecked",text:"资产盘点"},{code:"/map/websocket",text:"地图定位数据"},{code:"/huaxi/getGateway",text:"网关查询"},{code:"/user/update",text:"账号密码修改"}]},{code:4,text:"温湿度管理",children:[{code:"/luzhou/hum",text:"温湿度查询"},{code:"/luzhou/daySummary",text:"温湿度图表数据"},{code:"/luzhou/assetManagement/param",text:"温湿度历史数据"},{code:"/luzhou/section",text:"温湿度编辑数据"},{code:"/luzhou/edit",text:"温湿度编辑更新数据"},{code:"/huaxi/addHumiture",text:"温湿度仪器新增"},{code:"/huaxi/delHumiture",text:"温湿度仪器删除"},{code:"/hum/websocket",text:"温湿度明细数据"},{code:"/hum/warn/websocket",text:"温湿度告警数据"},{code:"/user/update",text:"账号密码修改"},{code:"/huaxi/hygrothermograph/export",text:"温湿度导出"}]},{code:5,text:"电子围栏",children:[{code:"/websocket",text:"设备动态数据"},{code:"/user/update",text:"账号密码修改"}]},{code:6,text:"维修/报废/调用",children:[{code:"/huaxi/assetmarked",text:"资产数据模糊查询"},{code:"/huaxi/assetManagement/param",text:"资产查询"},{code:"/maintainhistory/uploadimage",text:"维修登记"},{code:"/lendouthistory/uploadimage",text:"外借登记"},{code:"/maintainhistory/query",text:"维修/外借历史"},{code:"/user/update",text:"账号密码修改"}]},{code:7,text:"盘点管理",children:[{code:"/huaxi/assetmarked",text:"资产数据模糊查询"},{code:"/huaxi/assetManagement/param",text:"资产查询"},{code:"/map/websocket",text:"地图定位数据"},{code:"/huaxi/assertChecked",text:"资产盘点"},{code:"/huaxi/assertChecked/History",text:"资产盘点历史"},{code:"/huaxi/assertCheckedDetail",text:"资产盘点明细"},{code:"/user/update",text:"账号密码修改"}]},{code:8,text:"网关管理",children:[{code:"/huaxi/editGateway",text:"保存网关配置"},{code:"/huaxi/updateGateway",text:"网关参数更新"},{code:"/huaxi/deleteGateway",text:"网关删除"},{code:"/huaxi/addGateway",text:"网关新增"},{code:"/huaxi/gateway",text:"重启指定网关"},{code:"/huaxi/allGateway",text:"重启所有网关"},{code:"/huaxi/getGateway",text:"网关查询"},{code:"/user/update",text:"账号密码修改"}]},{code:9,text:"账户管理",children:[{code:"/huaxi/user/query",text:"账号查询"},{code:"/huaxi/user/del",text:"账号删除"},{code:"/huaxi/user/add",text:"账号创建"},{code:"/huaxi/departmentType",text:"授权部门权限"},{code:"/user/update",text:"账号密码修改"}]}],rolePathArr:[{code:"/huaxi/assetmarked",text:"资产数据模糊查询"},{code:"/huaxi/assetManagement/param",text:"资产查询"},{code:"/huaxi/statement",text:"资产统计数据"},{code:"/huaxi/upload",text:"资产新增"},{code:"/huaxi/del",text:"资产删除"},{code:"/huaxi/update",text:"资产更新"},{code:"/huaxi/poi/export",text:"导出资产表"},{code:"/uploadimage",text:"资产图片上传"},{code:"/showImage",text:"显示设备正面图片"},{code:"/showImageLeft",text:"显示设备左面图片"},{code:"/showImageRight",text:"显示设备右面图片"},{code:"/showImageAbove",text:"显示设备顶面图片"},{code:"/showImageAllround",text:"显示设备背面图片"},{code:"/showImagePaperlabel",text:"显示设备铭牌图片"},{code:"/showImageOnecodelable",text:"显示资产标签图片"},{code:"/websocket",text:"设备动态数据"},{code:"/huaxi/editGateway",text:"保存网关配置"},{code:"/huaxi/updateGateway",text:"网关参数更新"},{code:"/huaxi/deleteGateway",text:"网关删除"},{code:"/huaxi/addGateway",text:"网关新增"},{code:"/huaxi/gateway",text:"重启指定网关"},{code:"/huaxi/allGateway",text:"重启所有网关"},{code:"/huaxi/getGateway",text:"网关查询"},{code:"/huaxi/user/query",text:"账号查询"},{code:"/huaxi/user/del",text:"账号删除"},{code:"/huaxi/user/add",text:"账号创建"},{code:"/map/websocket",text:"地图定位数据"},{code:"/user/update",text:"账号密码修改"},{code:"/luzhou/hum",text:"温湿度查询"},{code:"/luzhou/daySummary",text:"温湿度图表数据"},{code:"/luzhou/assetManagement/param",text:"温湿度历史数据"},{code:"/luzhou/section",text:"温湿度编辑数据"},{code:"/luzhou/edit",text:"温湿度编辑更新数据"},{code:"/huaxi/addHumiture",text:"温湿度仪器新增"},{code:"/huaxi/delHumiture",text:"温湿度仪器删除"},{code:"/hum/websocket",text:"温湿度明细数据"},{code:"/hum/warn/websocket",text:"温湿度告警数据"},{code:"/huaxi/assertChecked",text:"资产盘点"},{code:"/huaxi/assertChecked/History",text:"资产盘点历史"},{code:"/huaxi/assertCheckedDetail",text:"资产盘点明细"},{code:"/maintainhistory/uploadimage",text:"维修登记"},{code:"/lendouthistory/uploadimage",text:"外借登记"},{code:"/maintainhistory/query",text:"维修/外借历史"},{code:"/huaxi/departmentType",text:"授权部门权限"}],departmentroomArr:[],editData:{data:{username:"",password:"",phone:"",departmentroom:[],role:"",authority:[],rolepath:[]},verify:{username:!1,departmentroom:!1,role:!1},text:{username:"请输入4-8位字符，可以包含中文/字母/数字",departmentroom:"",role:"管理员拥有资产删除权限，操作员没有"},warning:{username:!1,departmentroom:!1,role:!1}},uploadModal:!1,achieveUpload:!1,errorUpload:!1,passwordVerify:!1,phoneVerify:!1,roleData:[{value:"admin",label:"管理员"},{value:"operator",label:"操作员"}]}},watch:{},computed:{},methods:{inputFocus:function(t){t.target.removeAttribute("readonly")},inputBlur:function(t){t.target.setAttribute("readonly",!0)},inputErrorFn:function(t,e){t.test(this.editData[e])?(this.$refs[e+"_error"].classList.add("hide"),this[e+"Verify"]=!0):(this.$refs[e+"_error"].classList.remove("hide"),this[e+"Verify"]=!1)},inputBlurCheckFn:function(t,e){t=t;var a=e,o="",i=!1;if("username"===e)if(""===this[t].data.username.trim())o="账户名不能为空！",i=!0,this[t].verify[a]=!1;else{/^[(a-zA-Z0-9\u4e00-\u9fa5){1}_#]{4,8}$/.test(this[t].data.username.trim())?(o="",i=!1,this[t].verify[a]=!0):(o="请输入正确格式的账户名！",i=!0,this[t].verify[a]=!1)}else if("password"===e)if(""===this[t].data.password.trim())o="密码不能为空！",i=!0,this[t].verify[a]=!1;else{/^(\w){6,20}$/.test(this[t].data.password.trim())?(o="",i=!1,this[t].verify[a]=!0):(o="请输入6-20位密码",i=!0,this[t].verify[a]=!1)}else"departmentroom"===e?this[t].data.departmentroom.length<=0?(o="可授权部门至少需要一个",i=!0,this[t].verify[a]=!1):(o="",i=!1,this[t].verify[a]=!0):"role"===e&&(""===this[t].data.role.trim()?(o="账户级别不能为空！",i=!0,this[t].verify[a]=!1):(o="",i=!1,this[t].verify[a]=!0));this[t].text[a]=o,this[t].warning[a]=!!i},successFn:function(){var t=this;this.achieveUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1,t.$router.go(-1)},500)},1e3)},errorFn:function(){var t=this;this.errorUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1},500)},1e3)},editFn:function(){var t=this;this.inputBlurCheckFn("editData","username"),this.inputBlurCheckFn("editData","departmentroom"),this.inputBlurCheckFn("editData","role");var e=0;for(var a in this.editData.verify)this.editData.verify[a]||e++;e?this.$Message.error({content:"请填写完整账户信息！",duration:1}):this.editData.data.authority.length?function(){for(var e=[],a=function(a){t.authorityArr.forEach(function(o,i){t.editData.data.authority[a]===o.code&&o.children.forEach(function(t,a){e.find(function(e){return e===t.code})||e.push(t.code)})})},o=0;o<t.editData.data.authority.length;o++)a(o);var r={username:t.editData.data.username.trim(),phone:t.editData.data.phone.trim(),departmentroom:t.editData.data.departmentroom.join(","),role:t.editData.data.role.trim(),authority:i()(t.editData.data.authority),rolepath:e.join(",")};t.uploadModal=!0,t.$axios.post(f.a.accountEdit,r).then(function(e){0===e.data.code?t.successFn():(t.errorFn(),t.$Message.error({content:e.data.msg,duration:2}))}).catch(function(e){t.$Message.error({content:"创建用户失败！",duration:1})})}():this.$Message.error({content:"模块权限至少需要一个",duration:1})},getDepartmentRoomFn:function(){var t=this;this.$axios.post(f.a.departmentList,{address:"1"}).then(function(e){var a=[];e.data.data.forEach(function(t,e){a.push({value:t.departmentroom,label:t.departmentroom})}),t.departmentroomArr=a}).catch(function(t){console.log(t)})},getDataFn:function(){var t=this;this.$Loading.start(),this.$axios.get(f.a.accountQuery).then(function(e){var a=e.data.find(function(e,a){return e.username===t.username});a?(t.editData.data.username=a.username,t.editData.data.phone=a.phone,t.editData.data.role=a.role,t.editData.data.authority=JSON.parse(a.authority),t.editData.data.departmentroom=a.departmentroom.split(","),t.$Loading.finish()):(t.$Loading.error(),t.$Message.error({content:"未找到账户信息!",duration:2}),setTimeout(function(){t.$router.go(-1)},2e3))}).catch(function(e){t.$Loading.error()})}},created:function(){var t=this;this.username=this.$route.params.id,this.username||(this.$Message.error({content:"未找到账户信息!",duration:2}),setTimeout(function(){t.$router.go(-1)},2e3))},mounted:function(){this.getDepartmentRoomFn(),this.getDataFn()}},g={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"account_edit_wrap"},[a("div",{staticClass:"hover_animat custom_bg_color_white"},[a("div",{staticClass:"row_box"},[a("h2",{staticClass:"row_title"},[t._v("账户编辑")]),t._v(" "),a("div",{staticClass:"form_wrap_veritical"},[a("div",{staticClass:"input_item clearfix"},[t._m(0),t._v(" "),a("div",{staticClass:"input_item_center"},[a("Input",{staticStyle:{width:"200px"},attrs:{disabled:"",readonly:"",placeholder:"账户名"},on:{"on-focus":t.inputFocus,"on-blur":function(e){return t.inputBlurCheckFn("editData","username")}},model:{value:t.editData.data.username,callback:function(e){t.$set(t.editData.data,"username",e)},expression:"editData.data.username"}})],1),t._v(" "),a("div",{staticClass:"input_item_right"},[a("span",{ref:"notice_username"}),t._v(" "),a("span",{class:t.editData.warning.username?"error_msg":""},[t._v(t._s(t.editData.text.username))])])]),t._v(" "),a("div",{staticClass:"input_item clearfix"},[t._m(1),t._v(" "),a("div",{staticClass:"input_item_center"},[a("Input",{staticStyle:{width:"200px"},attrs:{placeholder:"手机号"},model:{value:t.editData.data.phone,callback:function(e){t.$set(t.editData.data,"phone",e)},expression:"editData.data.phone"}})],1),t._v(" "),a("div",{staticClass:"input_item_right"})]),t._v(" "),a("div",{staticClass:"input_item clearfix",staticStyle:{height:"auto"}},[t._m(2),t._v(" "),a("div",{staticClass:"input_item_center"},[a("Select",{staticStyle:{"min-width":"200px"},attrs:{filterable:"",multiple:"",placeholder:"可多选"},model:{value:t.editData.data.departmentroom,callback:function(e){t.$set(t.editData.data,"departmentroom",e)},expression:"editData.data.departmentroom"}},t._l(t.departmentroomArr,function(e){return a("Option",{key:e.value,attrs:{value:e.value}},[t._v(t._s(e.label))])}),1)],1),t._v(" "),a("div",{staticClass:"input_item_right"},[a("span",{class:t.editData.warning.departmentroom?"error_msg":""},[t._v(t._s(t.editData.text.departmentroom))])])]),t._v(" "),a("div",{staticClass:"input_item clearfix"},[t._m(3),t._v(" "),a("div",{staticClass:"input_item_center"},[a("Select",{staticStyle:{width:"200px"},on:{"on-change":function(e){return t.inputBlurCheckFn("editData","role")}},model:{value:t.editData.data.role,callback:function(e){t.$set(t.editData.data,"role",e)},expression:"editData.data.role"}},t._l(t.roleData,function(e){return a("Option",{key:e.value,attrs:{value:e.value}},[t._v(t._s(e.label))])}),1)],1),t._v(" "),a("div",{staticClass:"input_item_right"},[a("span",{class:t.editData.warning.role?"error_msg":""},[t._v(t._s(t.editData.text.role))])])]),t._v(" "),a("div",{staticClass:"check_item"},[t._m(4),t._v(" "),a("div",{staticClass:"check_item_right"},[a("CheckboxGroup",{model:{value:t.editData.data.authority,callback:function(e){t.$set(t.editData.data,"authority",e)},expression:"editData.data.authority"}},t._l(t.authorityArr,function(e,o){return a("Checkbox",{key:o,attrs:{label:e.code}},[a("span",[t._v(t._s(e.text))])])}),1)],1)])]),t._v(" "),a("div",{staticClass:"submit_btn_wrap left"},[a("Button",{attrs:{type:"primary",shape:"circle"},on:{click:t.editFn}},[t._v("保存修改")])],1)])]),t._v(" "),a("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:t.uploadModal,callback:function(e){t.uploadModal=e},expression:"uploadModal"}},[a("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[t.achieveUpload||t.errorUpload?t._e():a("Spin",{attrs:{fix:""}},[a("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),t._v(" "),a("div",[t._v("正在保存")])],1),t._v(" "),t.achieveUpload?a("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[a("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),t._v(" "),a("div",[t._v("保存成功")])],1):t._e(),t._v(" "),t.errorUpload?a("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[a("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),t._v(" "),a("div",[t._v("保存失败")])],1):t._e()],1)])],1)},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"input_item_left"},[e("h5",[this._v("账户名")]),this._v(" "),e("span",{staticClass:"must_fill_info"},[this._v("*")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"input_item_left"},[e("h5",[this._v("手机号")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"input_item_left"},[e("h5",[this._v("可授权部门")]),this._v(" "),e("span",{staticClass:"must_fill_info"},[this._v("*")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"input_item_left"},[e("h5",[this._v("账户级别")]),this._v(" "),e("span",{staticClass:"must_fill_info"},[this._v("*")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"check_item_left"},[e("h5",[this._v("模块权限")])])}]};var w=a("VU/8")(y,g,!1,function(t){a("FanE")},"data-v-fdc9e5e8",null);e.default=w.exports}});
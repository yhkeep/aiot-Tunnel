webpackJsonp([35],{KEjP:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=a("p8P9"),o=a("wdqF"),n=a("8sRp"),s=a("KxKW"),i=a("bzvv"),c=a("JtJ0"),l=a("X2sI"),p=a("LuSo"),m=a("9N2q"),u=a("NAz/"),v=a("a84E"),d=a("EMb8"),h=a("7+uW"),_=a("wDsp"),f=a("/WQG"),D=a("VPn5"),b=a("zcOu");h.a.component("Icon",d.a),h.a.component("Row",v.a),h.a.component("Col",u.a),h.a.component("DatePicker",m.a),h.a.component("Select",p.a),h.a.component("Option",l.a),h.a.component("Input",c.a),h.a.component("InputNumber",i.a),h.a.component("Button",s.a),h.a.component("Modal",n.a),h.a.component("Spin",o.a),h.a.component("AutoComplete",r.a);var y={name:"temp_create",components:{},props:{},data:function(){return{uploadModal:!1,achieveUpload:!1,errorUpload:!1,createData:{freezernumber:"",freezername:"",department:"",location:"",mac:"",type:"",tempTop:0,tempBottom:0,humTop:0,humBottom:0,saveinterval:"10"},instrumentType:f.a,tempInterval:D.a,inputData:[],inputDataAll:[],departmentroomArr:[]}},watch:{},computed:{},methods:{successFn:function(){var t=this;this.achieveUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1,t.$router.go(-1)},500)},1e3)},errorFn:function(){var t=this;this.errorUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1},500)},1e3)},instrumentChangeFn:function(t){var e=this;this.instrumentType.forEach(function(a,r){if(a.key===t)for(var o in a.range)e.createData[o]=a.range[o]})},createFn:function(){var t=this;if(this.createData.freezernumber.trim()&&this.createData.department.trim()&&this.createData.freezername.trim()&&this.createData.mac.trim()&&this.createData.type.trim()){if(null!==this.createData.tempBottom&&null!==this.createData.tempTop){if(this.createData.tempBottom===this.createData.tempTop)return void this.$Modal.warning({title:"提示！",content:"温度上下限不能相等！"});if(this.createData.tempBottom>this.createData.tempTop)return void this.$Modal.warning({title:"提示！",content:"温度上限不能小于下限！"})}if(null!==this.createData.humBottom&&null!==this.createData.humTop){if(this.createData.humBottom===this.createData.humTop)return void this.$Modal.warning({title:"提示！",content:"湿度上下限不能相等！"});if(this.createData.humBottom>this.createData.humTop)return void this.$Modal.warning({title:"提示！",content:"湿度上限不能小于下限！"})}this.uploadModal=!0;var e={freezernumber:this.createData.freezernumber.trim(),freezername:this.createData.freezername.trim(),department:this.createData.department.trim(),location:this.createData.location.trim(),mac:Object(b.a)(this.createData.mac.trim()),type:this.createData.type,temperaturefitted:this.createData.tempBottom+"~"+this.createData.tempTop,humidityfitted:this.createData.humBottom+"~"+this.createData.humTop,saveinterval:this.createData.saveinterval,address:1};this.$axios.post(_.a.tempCreate,e).then(function(e){0===e.data.code?t.successFn():t.errorFn()}).catch(function(e){t.errorFn()})}else this.$Message.error({content:"请补充完整信息!",duration:2})}},created:function(){var t=[];this.$store.state.departmentroom.split(",").forEach(function(e,a){t.push(e)}),this.departmentroomArr=t},mounted:function(){}},C={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"temp_create_wrap"},[a("div",{staticClass:"hover_animat custom_bg_color_white"},[a("div",{staticClass:"row_box"},[a("h2",{staticClass:"row_title"},[t._v("温湿度仪器新增")]),t._v(" "),a("div",{staticClass:"form_wrap"},[a("div",{staticClass:"form_item"},[a("Row",{attrs:{gutter:20}},[a("Col",{attrs:{span:"6"}},[a("h3",[t._v("编号"),a("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),a("Input",{attrs:{placeholder:"仪器编号"},model:{value:t.createData.freezernumber,callback:function(e){t.$set(t.createData,"freezernumber",e)},expression:"createData.freezernumber"}})],1),t._v(" "),a("Col",{attrs:{span:"6"}},[a("h3",[t._v("名称"),a("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),a("Input",{attrs:{placeholder:"仪器名称"},model:{value:t.createData.freezername,callback:function(e){t.$set(t.createData,"freezername",e)},expression:"createData.freezername"}})],1),t._v(" "),a("Col",{attrs:{span:"6"}},[a("h3",[t._v("部门名称"),a("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),a("AutoComplete",{attrs:{placeholder:"部门名称"},model:{value:t.createData.department,callback:function(e){t.$set(t.createData,"department",e)},expression:"createData.department"}},t._l(t.departmentroomArr,function(e){return a("Option",{key:e,attrs:{value:e}},[t._v(t._s(e))])}),1)],1),t._v(" "),a("Col",{attrs:{span:"6"}},[a("h3",[t._v("所在位置")]),t._v(" "),a("Input",{attrs:{placeholder:"所在位置"},model:{value:t.createData.location,callback:function(e){t.$set(t.createData,"location",e)},expression:"createData.location"}})],1)],1)],1),t._v(" "),a("div",{staticClass:"form_item"},[a("Row",{attrs:{gutter:20}},[a("Col",{attrs:{span:"6"}},[a("h3",[t._v("mac地址"),a("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),a("Input",{attrs:{placeholder:"mac地址"},model:{value:t.createData.mac,callback:function(e){t.$set(t.createData,"mac",e)},expression:"createData.mac"}})],1),t._v(" "),a("Col",{attrs:{span:"6"}},[a("h3",[t._v("仪器类型"),a("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),a("Select",{on:{"on-change":t.instrumentChangeFn},model:{value:t.createData.type,callback:function(e){t.$set(t.createData,"type",e)},expression:"createData.type"}},t._l(t.instrumentType,function(e){return a("Option",{key:e.key,attrs:{value:e.key}},[t._v(t._s(e.text))])}),1)],1),t._v(" "),a("Col",{attrs:{span:"6"}},[a("h3",[t._v("仪器记录时间间隔（分钟）"),a("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),a("Select",{model:{value:t.createData.saveinterval,callback:function(e){t.$set(t.createData,"saveinterval",e)},expression:"createData.saveinterval"}},t._l(t.tempInterval,function(e){return a("Option",{key:e.key,attrs:{value:e.key}},[t._v(t._s(e.text))])}),1)],1)],1)],1),t._v(" "),a("div",{staticClass:"form_item"},[a("Row",{attrs:{gutter:20}},[a("Col",{attrs:{span:"6"}},[a("h3",[t._v("温度上限（℃）")]),t._v(" "),a("InputNumber",{staticStyle:{width:"100%"},model:{value:t.createData.tempTop,callback:function(e){t.$set(t.createData,"tempTop",e)},expression:"createData.tempTop"}})],1),t._v(" "),a("Col",{attrs:{span:"6"}},[a("h3",[t._v("温度下限（℃）")]),t._v(" "),a("InputNumber",{staticStyle:{width:"100%"},model:{value:t.createData.tempBottom,callback:function(e){t.$set(t.createData,"tempBottom",e)},expression:"createData.tempBottom"}})],1),t._v(" "),a("Col",{directives:[{name:"show",rawName:"v-show",value:"GS1W"!==t.createData.type,expression:"createData.type!=='GS1W'"}],attrs:{span:"6"}},[a("h3",[t._v("湿度上限（%）")]),t._v(" "),a("InputNumber",{staticStyle:{width:"100%"},attrs:{max:100,min:0},model:{value:t.createData.humTop,callback:function(e){t.$set(t.createData,"humTop",e)},expression:"createData.humTop"}})],1),t._v(" "),a("Col",{directives:[{name:"show",rawName:"v-show",value:"GS1W"!==t.createData.type,expression:"createData.type!=='GS1W'"}],attrs:{span:"6"}},[a("h3",[t._v("湿度下限（%）")]),t._v(" "),a("InputNumber",{staticStyle:{width:"100%"},attrs:{max:100,min:0},model:{value:t.createData.humBottom,callback:function(e){t.$set(t.createData,"humBottom",e)},expression:"createData.humBottom"}})],1)],1)],1)]),t._v(" "),a("div",{staticClass:"submit_btn_wrap"},[a("Button",{attrs:{type:"primary",shape:"circle"},on:{click:t.createFn}},[t._v("保存")])],1)])]),t._v(" "),a("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:t.uploadModal,callback:function(e){t.uploadModal=e},expression:"uploadModal"}},[a("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[t.achieveUpload||t.errorUpload?t._e():a("Spin",{attrs:{fix:""}},[a("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),t._v(" "),a("div",[t._v("正在保存")])],1),t._v(" "),t.achieveUpload?a("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[a("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),t._v(" "),a("div",[t._v("保存成功")])],1):t._e(),t._v(" "),t.errorUpload?a("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[a("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),t._v(" "),a("div",[t._v("保存失败")])],1):t._e()],1)])],1)},staticRenderFns:[]};var w=a("VU/8")(y,C,!1,function(t){a("Rrtf")},"data-v-4c971d6a",null);e.default=w.exports},Rrtf:function(t,e){}});
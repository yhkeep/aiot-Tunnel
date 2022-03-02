webpackJsonp([31],{KEjP:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var o=e("mvHQ"),r=e.n(o),n=e("p8P9"),i=e("wdqF"),s=e("8sRp"),c=e("KxKW"),l=e("bzvv"),p=e("JtJ0"),u=e("X2sI"),m=e("LuSo"),h=e("9N2q"),d=e("NAz/"),v=e("a84E"),f=e("EMb8"),_=e("7+uW"),D=e("wDsp"),b=e("/WQG"),y=e("zcOu");_.a.component("Icon",f.a),_.a.component("Row",v.a),_.a.component("Col",d.a),_.a.component("DatePicker",h.a),_.a.component("Select",m.a),_.a.component("Option",u.a),_.a.component("Input",p.a),_.a.component("InputNumber",l.a),_.a.component("Button",c.a),_.a.component("Modal",s.a),_.a.component("Spin",i.a),_.a.component("AutoComplete",n.a);var w={name:"temp_create",components:{},props:{},data:function(){return{uploadModal:!1,achieveUpload:!1,errorUpload:!1,createData:{freezernumber:"",freezername:"",department:"",location:"",mac:"",type:"",tempTop:null,tempBottom:null,humTop:null,humBottom:null},instrumentType:b.a,inputData:[],inputDataAll:[]}},watch:{},computed:{},methods:{handleSearchFn:function(t){this.inputData=this.inputDataAll.filter(function(a){return a.toLowerCase().indexOf(t.toLowerCase())>-1})},inputBlurFn:function(){this.inputDataAll=[],this.inputData=[]},inputFocusFn:function(t){var a=this;this.inputDataAll=[],this.inputData=[];var e=[],o={};o[t]="1",e.push(o),this.$axios.post(D.a.inputQuery,e).then(function(e){e.data.data.forEach(function(e,o){a.inputDataAll.find(function(a){return a===e[t]})||a.inputDataAll.push(e[t])}),setTimeout(function(){a.inputData=JSON.parse(r()(a.inputDataAll))},300)}).catch(function(t){console.log(t)})},successFn:function(){var t=this;this.achieveUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1,t.$router.go(-1)},500)},1e3)},errorFn:function(){var t=this;this.errorUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1},500)},1e3)},createFn:function(){var t=this;if(this.createData.freezernumber.trim()&&this.createData.department.trim()&&this.createData.freezername.trim()&&this.createData.mac.trim()&&this.createData.type.trim()){if(this.createData.tempBottom&&this.createData.tempTop){if(this.createData.tempBottom===this.createData.tempTop)return void this.$Modal.warning({title:"提示！",content:"温度上下限不能相等！"});if(this.createData.tempBottom>this.createData.tempTop)return void this.$Modal.warning({title:"提示！",content:"温度上限不能小于下限！"})}if(this.createData.humBottom&&this.createData.humTop){if(this.createData.humBottom===this.createData.humTop)return void this.$Modal.warning({title:"提示！",content:"湿度上下限不能相等！"});if(this.createData.humBottom>this.createData.humTop)return void this.$Modal.warning({title:"提示！",content:"湿度上限不能小于下限！"})}this.uploadModal=!0;var a={freezernumber:this.createData.freezernumber.trim(),freezername:this.createData.freezername.trim(),department:this.createData.department.trim(),location:this.createData.location.trim(),mac:Object(y.a)(this.createData.mac.trim()),type:this.createData.type.trim(),temperaturefitted:this.createData.tempBottom+"~"+this.createData.tempTop,humidityfitted:this.createData.humBottom+"~"+this.createData.humTop,address:1};this.$axios.post(D.a.tempCreate,a).then(function(a){0==a.data.code?t.successFn():t.errorFn()}).catch(function(a){t.errorFn()})}else this.$Message.error({content:"请补充完整信息!",duration:2})}},created:function(){},mounted:function(){}},C={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"temp_create_wrap"},[e("div",{staticClass:"hover_animat custom_bg_color_white"},[e("div",{staticClass:"row_box"},[e("h2",{staticClass:"row_title"},[t._v("温湿度仪器新增")]),t._v(" "),e("div",{staticClass:"form_wrap"},[e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[t._v("编号"),e("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),e("Input",{attrs:{placeholder:"仪器编号"},model:{value:t.createData.freezernumber,callback:function(a){t.$set(t.createData,"freezernumber",a)},expression:"createData.freezernumber"}})],1),t._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[t._v("名称"),e("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),e("Input",{attrs:{placeholder:"仪器名称"},model:{value:t.createData.freezername,callback:function(a){t.$set(t.createData,"freezername",a)},expression:"createData.freezername"}})],1),t._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[t._v("科室"),e("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),e("AutoComplete",{attrs:{placeholder:"所在科室名称"},on:{"on-focus":function(a){return t.inputFocusFn("locDept")},"on-blur":t.inputBlurFn,"on-search":t.handleSearchFn},model:{value:t.createData.department,callback:function(a){t.$set(t.createData,"department",a)},expression:"createData.department"}},t._l(t.inputData,function(a){return e("Option",{key:a,attrs:{value:a}},[t._v(t._s(a))])}),1)],1),t._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[t._v("所在位置")]),t._v(" "),e("Input",{attrs:{placeholder:"所在位置"},model:{value:t.createData.location,callback:function(a){t.$set(t.createData,"location",a)},expression:"createData.location"}})],1)],1)],1),t._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[t._v("mac地址"),e("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),e("Input",{attrs:{placeholder:"mac地址"},model:{value:t.createData.mac,callback:function(a){t.$set(t.createData,"mac",a)},expression:"createData.mac"}})],1),t._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[t._v("仪器类型"),e("span",{staticClass:"must_fill_info"},[t._v("（必填）")])]),t._v(" "),e("Select",{model:{value:t.createData.type,callback:function(a){t.$set(t.createData,"type",a)},expression:"createData.type"}},t._l(t.instrumentType,function(a){return e("Option",{key:a.key,attrs:{value:a.key}},[t._v(t._s(a.text))])}),1)],1)],1)],1),t._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[t._v("温度上限（℃）")]),t._v(" "),e("InputNumber",{staticStyle:{width:"100%"},model:{value:t.createData.tempTop,callback:function(a){t.$set(t.createData,"tempTop",a)},expression:"createData.tempTop"}})],1),t._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[t._v("温度下限（℃）")]),t._v(" "),e("InputNumber",{staticStyle:{width:"100%"},model:{value:t.createData.tempBottom,callback:function(a){t.$set(t.createData,"tempBottom",a)},expression:"createData.tempBottom"}})],1),t._v(" "),e("Col",{directives:[{name:"show",rawName:"v-show",value:"GS1W"!==t.createData.type,expression:"createData.type!=='GS1W'"}],attrs:{span:"6"}},[e("h3",[t._v("湿度上限（%）")]),t._v(" "),e("InputNumber",{staticStyle:{width:"100%"},attrs:{max:100,min:0},model:{value:t.createData.humTop,callback:function(a){t.$set(t.createData,"humTop",a)},expression:"createData.humTop"}})],1),t._v(" "),e("Col",{directives:[{name:"show",rawName:"v-show",value:"GS1W"!==t.createData.type,expression:"createData.type!=='GS1W'"}],attrs:{span:"6"}},[e("h3",[t._v("湿度下限（%）")]),t._v(" "),e("InputNumber",{staticStyle:{width:"100%"},attrs:{max:100,min:0},model:{value:t.createData.humBottom,callback:function(a){t.$set(t.createData,"humBottom",a)},expression:"createData.humBottom"}})],1)],1)],1)]),t._v(" "),e("div",{staticClass:"submit_btn_wrap"},[e("Button",{attrs:{type:"primary",shape:"circle"},on:{click:t.createFn}},[t._v("确定新增")])],1)])]),t._v(" "),e("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:t.uploadModal,callback:function(a){t.uploadModal=a},expression:"uploadModal"}},[e("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[t.achieveUpload||t.errorUpload?t._e():e("Spin",{attrs:{fix:""}},[e("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),t._v(" "),e("div",[t._v("正在上传")])],1),t._v(" "),t.achieveUpload?e("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),t._v(" "),e("div",[t._v("上传成功")])],1):t._e(),t._v(" "),t.errorUpload?e("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),t._v(" "),e("div",[t._v("上传失败")])],1):t._e()],1)])],1)},staticRenderFns:[]};var x=e("VU/8")(w,C,!1,function(t){e("LrDa")},"data-v-575379d4",null);a.default=x.exports},LrDa:function(t,a){}});
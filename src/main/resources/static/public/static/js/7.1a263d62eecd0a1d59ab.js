webpackJsonp([7],{FZNM:function(t,e){},kYRV:function(t,e){},poqm:function(t,e,a){a("XqYu"),a("kYRV")},q5bM:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("mvHQ"),n=a.n(i),l=a("Fd2+"),s=(a("FDxC"),a("4T1P"),a("2PSJ"),a("jydU"),a("wvM5"),a("MHRi"),a("Rv11"),a("tLo2"),a("GKy3"),a("uTM9"),a("poqm"),a("9ULi"),a("wDsp")),o=(a("zcOu"),a("/WQG")),m={name:"mobile_temp_edit",components:{Button:l.b,Tag:l.y,Icon:l.m,Field:l.l,Dialog:l.k,Picker:l.s,Popup:l.t,Toast:l.z,Loading:l.o,Stepper:l.x,Search:l.w},props:{},data:function(){return{mac:"",typeDialogShow:!1,tempData:{freezernumber:"",freezername:"",department:"",location:"",mac:"",type:"",tempTop:null,tempBottom:null,humTop:null,humBottom:null},instrumentType:o.a,inputData:[],inputDataAll:[],inputSearchValue:"",activeKey:"",showInputSearch:!1}},watch:{},computed:{},methods:{getTypeFn:function(t){this.tempData.type=t.key,this.typeDialogShow=!1},certainSelectFn:function(t){"locDept"===this.activeKey&&(this.tempData.department=t),this.showInputSearch=!1,this.inputSearchValue="",this.activeKey=""},showInputSearchFn:function(t){this.showInputSearch=!0,this.activeKey=t,this.inputFocusFn(t)},handleSearchFn:function(t){this.inputData=this.inputDataAll.filter(function(e){return e.toLowerCase().indexOf(t.toLowerCase())>-1})},inputFocusFn:function(t){var e=this;this.inputDataAll=[],this.inputData=[];var a=[],i={};i[t]="1",a.push(i),this.$axios.post(s.a.inputQuery,a).then(function(a){a.data.data.forEach(function(a,i){e.inputDataAll.find(function(e){return e===a[t]})||e.inputDataAll.push(a[t])}),e.inputData=JSON.parse(n()(e.inputDataAll))}).catch(function(t){console.log(t)})},saveFn:function(){var t=this;if(this.tempData.freezernumber.trim()&&this.tempData.department.trim()&&this.tempData.freezername.trim()&&this.tempData.mac.trim()&&this.tempData.type.trim()){if(this.tempData.tempBottom&&this.tempData.tempTop){if(this.tempData.tempBottom===this.tempData.tempTop)return void this.$notify({type:"warning",message:"温度上下限不能相等！",duration:2e3});if(this.tempData.tempBottom>this.tempData.tempTop)return void this.$notify({type:"warning",message:"温度上限不能小于下限！",duration:2e3})}if(this.tempData.humBottom&&this.tempData.humTop){if(this.tempData.humBottom===this.tempData.humTop)return void this.$notify({type:"warning",message:"湿度上下限不能相等！",duration:2e3});if(this.tempData.humBottom>this.tempData.humTop)return void this.$notify({type:"warning",message:"湿度上限不能小于下限！",duration:2e3})}var e=l.z.loading({message:"正在保存...",forbidClick:!0,duration:0}),a={freezernumber:this.tempData.freezernumber.trim(),freezername:this.tempData.freezername.trim(),department:this.tempData.department.trim(),type:this.tempData.type,location:this.tempData.location.trim(),temperaturefitted:this.tempData.tempBottom+"~"+this.tempData.tempTop,humidityfitted:this.tempData.humBottom+"~"+this.tempData.humTop},i="";for(var n in a)a[n]||(a[n]=""),i+=n+"="+a[n].trim()+"&";this.$axios.get(s.a.tempEditUpdate+"?mac="+this.mac+"&"+i).then(function(a){"ok"===a.data.msg?(e.clear(),l.z.success("保存成功！"),setTimeout(function(){t.$router.go(-1)},2e3)):"failed"===a.data.msg&&(e.clear(),l.z.fail("保存失败！"))}).catch(function(t){})}else this.$notify({type:"warning",message:"请补充完整必填信息！",duration:2e3})},tempRangeComFn:function(t,e){return t?"null"===t.split("~")[e]?null:Number(t.split("~")[e]):null},getDataFn:function(){var t=this;this.$axios.get(s.a.tempEditDetail+"?mac="+this.mac).then(function(e){var a=e.data[0];t.tempData={freezernumber:a.freezernumber?a.freezernumber:"",freezername:a.freezername,department:a.department,location:a.location?a.location:"",mac:a.mac?a.mac:"",type:a.type?a.type:"",tempTop:t.tempRangeComFn(a.temperaturefitted,1),tempBottom:t.tempRangeComFn(a.temperaturefitted,0),humTop:t.tempRangeComFn(a.humidityfitted,1),humBottom:t.tempRangeComFn(a.humidityfitted,0)}}).catch(function(t){})}},created:function(){this.$emit("setTitle","温湿度仪器编辑"),this.mac=this.$route.params.id},mounted:function(){this.getDataFn()}},c={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mobile_temp_edit_wrap"},[a("div",{staticClass:"temp_edit_box assets_upload_box"},[a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{clearable:"",label:"仪器编号",required:"",placeholder:"仪器编号"},model:{value:t.tempData.freezernumber,callback:function(e){t.$set(t.tempData,"freezernumber",e)},expression:"tempData.freezernumber"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{clearable:"",label:"仪器名称",required:"",placeholder:"请输入仪器名称"},model:{value:t.tempData.freezername,callback:function(e){t.$set(t.tempData,"freezername",e)},expression:"tempData.freezername"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"所在部门",clearable:"",required:"",center:"",placeholder:"请输入所在部门"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("locDept")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.tempData.department,callback:function(e){t.$set(t.tempData,"department",e)},expression:"tempData.department"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{clearable:"",label:"所在位置",placeholder:"请输入所在位置"},model:{value:t.tempData.location,callback:function(e){t.$set(t.tempData,"location",e)},expression:"tempData.location"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{clearable:"",disabled:"",label:"mac地址",required:"",placeholder:"请输入mac地址"},model:{value:t.tempData.mac,callback:function(e){t.$set(t.tempData,"mac",e)},expression:"tempData.mac"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.typeDialogShow=!0}}},[a("Field",{attrs:{label:"仪器类型",disabled:"",required:"",placeholder:"请输入仪器类型"},model:{value:t.tempData.type,callback:function(e){t.$set(t.tempData,"type",e)},expression:"tempData.type"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{staticClass:"van-cell van-field"},[t._m(0),t._v(" "),a("div",{staticClass:"van-cell__value"},[a("Stepper",{staticStyle:{"text-align":"left"},attrs:{integer:"",min:"-500",max:"500"},model:{value:t.tempData.tempTop,callback:function(e){t.$set(t.tempData,"tempTop",e)},expression:"tempData.tempTop"}})],1)])])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{staticClass:"van-cell van-field"},[t._m(1),t._v(" "),a("div",{staticClass:"van-cell__value"},[a("Stepper",{staticStyle:{"text-align":"left"},attrs:{integer:"",min:"-500",max:"500"},model:{value:t.tempData.tempBottom,callback:function(e){t.$set(t.tempData,"tempBottom",e)},expression:"tempData.tempBottom"}})],1)])])]),t._v(" "),a("div",{directives:[{name:"show",rawName:"v-show",value:"GS1W"!==t.tempData.type,expression:"tempData.type!=='GS1W'"}],staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{staticClass:"van-cell van-field"},[t._m(2),t._v(" "),a("div",{staticClass:"van-cell__value"},[a("Stepper",{staticStyle:{"text-align":"left"},attrs:{integer:"",min:"0",max:"100"},model:{value:t.tempData.humTop,callback:function(e){t.$set(t.tempData,"humTop",e)},expression:"tempData.humTop"}})],1)])])]),t._v(" "),a("div",{directives:[{name:"show",rawName:"v-show",value:"GS1W"!==t.tempData.type,expression:"tempData.type!=='GS1W'"}],staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{staticClass:"van-cell van-field"},[t._m(3),t._v(" "),a("div",{staticClass:"van-cell__value"},[a("Stepper",{staticStyle:{"text-align":"left"},attrs:{integer:"",min:"0",max:"100"},model:{value:t.tempData.humBottom,callback:function(e){t.$set(t.tempData,"humBottom",e)},expression:"tempData.humBottom"}})],1)])])]),t._v(" "),a("div",{staticClass:"tools_wrap"},[a("Button",{staticClass:"tools_button",attrs:{round:"",type:"primary"},on:{click:t.saveFn}},[t._v("保存")])],1)]),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.typeDialogShow,callback:function(e){t.typeDialogShow=e},expression:"typeDialogShow"}},[a("Picker",{attrs:{columns:t.instrumentType,"show-toolbar":"",title:"请选择类型","visible-item-count":5},on:{cancel:function(e){t.typeDialogShow=!1},confirm:t.getTypeFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.showInputSearch,callback:function(e){t.showInputSearch=e},expression:"showInputSearch"}},[a("Search",{attrs:{placeholder:"请输入搜索关键词","input-align":"center"},on:{input:t.handleSearchFn},model:{value:t.inputSearchValue,callback:function(e){t.inputSearchValue=e},expression:"inputSearchValue"}}),t._v(" "),a("Picker",{ref:"inputSelect",attrs:{"show-toolbar":"","toolbar-position":"bottom",columns:t.inputData,title:"","visible-item-count":5},on:{cancel:function(e){t.showInputSearch=!1},confirm:t.certainSelectFn}})],1)],1)},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"van-cell__title van-field__label"},[e("span",[this._v("温度上限(℃)")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"van-cell__title van-field__label"},[e("span",[this._v("温度下限(℃)")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"van-cell__title van-field__label"},[e("span",[this._v("湿度上限(%)")])])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"van-cell__title van-field__label"},[e("span",[this._v("湿度下限(%)")])])}]};var r=a("VU/8")(m,c,!1,function(t){a("FZNM")},"data-v-d93f2128",null);e.default=r.exports}});
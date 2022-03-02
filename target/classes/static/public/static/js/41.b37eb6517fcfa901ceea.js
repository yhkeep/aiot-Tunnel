webpackJsonp([41],{"6Ys1":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=e("Fd2+"),i=(e("FDxC"),e("2PSJ"),e("uTM9"),e("9ULi"),e("jydU"),e("wvM5"),e("tLo2"),e("D0HL"),e("wDsp")),l={name:"mobile_damage_history",components:{Button:s.b,Tag:s.y,Loading:s.o,Search:s.w,Icon:s.m,Field:s.l,Popup:s.t,DatetimePicker:s.j},props:{},data:function(){return{value:"",showSearchPane:!1,showImg:!1,showImgData:"",assetID:"",historyDetailData:[],loadingShow:!0,queryData:{timeStart:"",timeEnd:""},minDate:new Date(2020,0,1),maxDate:new Date(2050,12,0),currentDate:new Date,timeModalShow:!1,currentMode:""}},watch:{},computed:{},methods:{getTimeFn:function(t){var a=t,e=a.getFullYear(),s=a.getMonth()+1,i=a.getDate(),l=a.getHours(),o=a.getMinutes();s=s<10?"0"+s:s,i=i<10?"0"+i:i,l=l<10?"0"+l:l,o=o<10?"0"+o:o,this.queryData[this.currentMode]=e+"-"+s+"-"+i+" "+l+":"+o,this.timeModalShow=!1},showImgFn:function(t){this.showImgData=t,this.showImg=!0},openSearchPaneFn:function(){this.showSearchPane=!0},searchFn:function(){this.getDataFn(),this.showSearchPane=!1},getDataFn:function(){var t=this;this.loadingShow=!0,this.$axios.get(i.a.damageRegisterHistory+"?assetID="+this.assetID+"&timeStart="+this.queryData.timeStart+"&timeEnd="+this.queryData.timeEnd).then(function(a){"success"===a.data.msg&&(t.historyDetailData=a.data.data,t.loadingShow=!1)}).catch(function(t){console.log(t)})}},created:function(){this.$emit("setTitle","维修/报废/调用历史"),this.assetID=this.$route.params.id},mounted:function(){this.getDataFn()}},o={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"mobile_damage_history_wrap manage_list_wrap custom_flex_column_wrap"},[e("div",{staticClass:"assets_detail_title"},[e("h3",[t._v(t._s(t.assetID))])]),t._v(" "),e("div",{staticClass:"query_data_wrap_open"},[e("div",{staticClass:"query_data_box"},[e("Field",{attrs:{label:"开始时间",disabled:"",placeholder:"请输入要查询的开始时间"},on:{click:function(a){t.timeModalShow=!0,t.currentMode="timeStart"}},model:{value:t.queryData.timeStart,callback:function(a){t.$set(t.queryData,"timeStart",a)},expression:"queryData.timeStart"}}),t._v(" "),e("Field",{attrs:{label:"结束时间",disabled:"",placeholder:"请输入要查询的结束时间"},on:{click:function(a){t.timeModalShow=!0,t.currentMode="timeEnd"}},model:{value:t.queryData.timeEnd,callback:function(a){t.$set(t.queryData,"timeEnd",a)},expression:"queryData.timeEnd"}})],1),t._v(" "),e("div",{staticClass:"submit_button_wrap"},[e("Button",{staticClass:"submit_button",attrs:{type:"info",round:""},on:{click:t.searchFn}},[t._v("搜索")])],1)]),t._v(" "),e("div",{staticClass:"assets_table_wrap"},[e("div",{staticClass:"assets_table_wrap_content  clearfix"},[e("div",{staticClass:"assets_table_wrap_left"},[e("ul",{staticClass:"assets_table_ul"},[t._m(0),t._v(" "),t._l(t.historyDetailData,function(a,s){return e("li",{key:s,staticClass:"assets_table_li"},[e("div",{staticClass:"assets_table_item"},[e("p",[t._v(t._s(s+1))])])])})],2)]),t._v(" "),e("div",{staticClass:"assets_table_wrap_right"},[e("ul",{staticClass:"assets_table_ul"},[t._m(1),t._v(" "),t._l(t.historyDetailData,function(a,s){return e("li",{key:s,staticClass:"assets_table_li clearfix"},[e("div",{staticClass:"assets_table_item"},[t._v(t._s(a.handovertime?a.handovertime:"--"))]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("div",{staticClass:"damage_history_img"},[e("img",{attrs:{src:a.handlingPeople,alt:""},on:{click:function(e){return t.showImgFn(a.handlingPeople)}}})])]),t._v(" "),e("div",{staticClass:"assets_table_item damage_history_img"},[e("div",{staticClass:"damage_history_img"},[e("img",{attrs:{src:a.buttingPeople,alt:""},on:{click:function(e){return t.showImgFn(a.buttingPeople)}}})])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v(t._s(a.handoverdepartment?a.handoverdepartment:"--"))])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v(t._s(a.estimatedtime?a.estimatedtime:"--"))])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v(t._s(a.remark?a.remark:"--"))])]),t._v(" "),e("div",{staticClass:"assets_table_item"},["1"===a.assetstatustype?e("Tag",{attrs:{color:"#ff9900"}},[t._v("维修登记")]):t._e(),t._v(" "),"2"===a.assetstatustype?e("Tag",{attrs:{color:"#19be6b"}},[t._v("维修完成")]):t._e(),t._v(" "),"3"===a.assetstatustype?e("Tag",{attrs:{color:"#9a66e4"}},[t._v("外借登记")]):t._e(),t._v(" "),"4"===a.assetstatustype?e("Tag",{attrs:{color:"#2d8cf0"}},[t._v("外借归还")]):t._e()],1)])})],2)])]),t._v(" "),e("div",{staticClass:"loading_info_wrap"},[e("Loading",{directives:[{name:"show",rawName:"v-show",value:t.loadingShow,expression:"loadingShow"}],attrs:{size:"24px"}},[t._v("加载中...")]),t._v(" "),e("p",{directives:[{name:"show",rawName:"v-show",value:!t.loadingShow&&!t.historyDetailData.length,expression:"!loadingShow&&!historyDetailData.length"}]},[t._v("暂无数据")])],1)]),t._v(" "),e("Popup",{attrs:{position:"bottom"},model:{value:t.timeModalShow,callback:function(a){t.timeModalShow=a},expression:"timeModalShow"}},[e("DatetimePicker",{attrs:{type:"datetime","show-toolbar":"",title:"请选择时间","min-date":t.minDate,"max-date":t.maxDate},on:{cancel:function(a){t.timeModalShow=!1},confirm:t.getTimeFn},model:{value:t.currentDate,callback:function(a){t.currentDate=a},expression:"currentDate"}})],1),t._v(" "),e("Popup",{staticClass:"scale_img_wrap",model:{value:t.showImg,callback:function(a){t.showImg=a},expression:"showImg"}},[e("img",{attrs:{src:t.showImgData,alt:""}})])],1)},staticRenderFns:[function(){var t=this.$createElement,a=this._self._c||t;return a("li",{staticClass:"assets_table_li assets_table_li_name"},[a("div",{staticClass:"assets_table_item"},[a("p",[this._v("序号")])])])},function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("li",{staticClass:"assets_table_li assets_table_li_name clearfix"},[e("div",{staticClass:"assets_table_item"},[e("p",[t._v("记录时间")])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v("负责人签名")])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v("交接人签名")])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v("交接人部门")])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v("预计归还时间(外借)")])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v("备注")])]),t._v(" "),e("div",{staticClass:"assets_table_item"},[e("p",[t._v("类型")])])])}]};var n=e("VU/8")(l,o,!1,function(t){e("Hr28")},"data-v-0bcac8f6",null);a.default=n.exports},Hr28:function(t,a){}});
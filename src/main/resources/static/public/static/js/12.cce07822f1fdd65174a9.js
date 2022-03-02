webpackJsonp([12],{"2iNE":function(t,e){},"9Cmf":function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=a("EMb8"),i=a("NHTL"),n=a("yoLw"),o=a("7+uW"),c=a("Gaqz"),l=a("gUqX"),r=a("gyyF"),_=a("wDsp");a("TIfe");o.a.component("Table",n.a),o.a.component("Tag",i.a),o.a.component("Icon",s.a);var d={name:"home",components:{},props:{},data:function(){return{deviceMessage:[],tableLoading:!0,summaryList:[{value:"--",text:"资产总额",unit:"元",color:"#2d8cf0",icon:"md-cube"},{value:"--",text:"资产总量",unit:"件",color:"#19be6b",icon:"ios-pie"},{value:"--",text:"正在维修",unit:"件",color:"#e46cbb",icon:"md-hammer"},{value:"--",text:"尚未盘点",unit:"件",color:"#9a66e4",icon:"ios-contrast"}],newAssetsColumn:[{title:"资产id",key:"assetID"},{title:"通用名称",key:"generalName"},{title:"资产名称",key:"assetName"},{title:"规格",key:"specification"},{title:"型号",key:"type"},{title:"存放地点",key:"location"},{title:"产地",key:"placeoforigin"},{title:"品牌",key:"brand"},{title:"部门编码",key:"departmentcode"},{title:"部门名称",key:"departmentroom"},{title:"大科室编码",key:"homeofficenumber"},{title:"大科室名称",key:"homeofficename"},{title:"是否进口",key:"isentrance"},{title:"供应商名称",key:"suppliername"},{title:"生产厂商名称",key:"generatebusinessname"},{title:"所在科室名称",key:"locDept"},{title:"状态",key:"status",slot:"status",width:100}],newAssetsData:[],websock:null,damageChart:null,checkChart:null,damageData:[0,0,0,0],checkData:[0,0]}},watch:{isCollapsed:function(){var t=this;setTimeout(function(){t.damageChart.resize(),t.checkChart.resize()},500)},deviceMessageArr:function(){this.deviceMessage=this.deviceMessageArr}},computed:{isCollapsed:function(){return this.$store.state.isCollapsed},deviceMessageArr:function(){return this.$store.state.deviceMessageArr}},methods:{setAllOldFn:function(){this.damageChart=echarts.init(document.getElementById("damage_ratio_wrap"));var t=Math.round(this.damageData[0]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3])*100),e=Math.round(this.damageData[1]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3])*100),a=Math.round(this.damageData[2]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3])*100),s=100-(t+e+a);l.a.series[0].data=[{value:t,name:"正常"},{value:e,name:"在修"},{value:a,name:"待报废"},{value:s,name:"已报废"}],this.damageChart.setOption(l.a)},setCheckFn:function(){this.checkChart=echarts.init(document.getElementById("check_pie_wrap"));var t=Math.round(this.checkData[0]/(this.checkData[0]+this.checkData[1])*100),e=100-t;r.a.series[0].data=[{value:t,name:"已盘点"},{value:e,name:"未盘点"}],this.checkChart.setOption(r.a)},getDataFn:function(){var t=this;this.$Loading.start(),this.$axios.post(_.a.assetsQuery).then(function(e){for(var a=e.data,s=(Number(a.pop().total),0);s<a.length;s++){for(var i in a[s])""!==a[s][i]&&"null"!==a[s][i]||(a[s][i]="--");t.newAssetsData.push(a[s])}t.$Loading.finish(),t.tableLoading=!1}).catch(function(e){t.$Loading.error()}),this.$axios.post(_.a.assetsTotalNum).then(function(e){var a=e.data[0];"failed"===a.msg||(t.summaryList.forEach(function(t,e){switch(t.text){case"资产总额":t.value=a.aggregate;break;case"资产总量":t.value=a.examined+a.unexamined;break;case"正在维修":t.value=a.maintain;break;case"尚未盘点":t.value=a.unexamined;break;default:return}}),t.damageData=[a.normal,a.maintain,a.tobescrap,a.scrap],t.checkData=[a.examined,a.unexamined],t.setAllOldFn(),t.setCheckFn())}).catch(function(t){})},windowResizeFn:function(){var t=this;window.addEventListener("resize",function(){t.damageChart.resize(),t.checkChart.resize()})}},created:function(){},mounted:function(){var t=this;this.$nextTick(function(){Object(c.a)().then(function(){t.getDataFn(),t.windowResizeFn()})})},beforeDestroy:function(){this.$Loading.finish()}},m={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"home_wrap"},[a("div",{staticClass:"custom_flex_wrap total_data_wrap"},t._l(t.summaryList,function(e,s){return a("div",{key:s,staticClass:"total_data_item custom_flex_item_4 custom_bg_color_white hover_animat"},[a("div",{staticClass:"total_icon_box",style:"background-color:"+e.color},[a("Icon",{attrs:{type:e.icon}})],1),t._v(" "),a("div",{staticClass:"total_item_box"},[a("div",{staticClass:"total_item_box_top"},[t._v("\n                      "+t._s(e.value)+t._s(e.unit)+"\n                  ")]),t._v(" "),a("div",{staticClass:"total_item_box_bottom"},[t._v("\n                      "+t._s(e.text)+"\n                  ")])])])}),0),t._v(" "),a("div",{staticClass:"custom_flex_wrap row_wrap"},[a("div",{staticClass:"custom_flex_item_3_no hover_animat custom_bg_color_white row_box"},[a("h2",{staticClass:"row_title"},[t._v("设备动态")]),t._v(" "),a("div",{staticClass:"device_message_box"},[t.deviceMessage.length<=0?a("h3",[t._v("暂无动态...")]):a("div",{staticClass:"device_message_scroll_wrap"},[a("div",{staticClass:"device_message_scroll_box"},t._l(t.deviceMessage,function(e,s){return a("div",{key:s,staticClass:"device_message_item"},[a("div",{staticClass:"device_message_item_left"},[a("div",{staticClass:"device_message_icon_box"},[a("Icon",{attrs:{type:"ios-warning"}})],1)]),t._v(" "),a("div",{staticClass:"device_message_item_right"},[a("div",{staticClass:"device_message_item_right_top"},[a("span",[t._v(t._s(s+1)+" - ")]),a("span",[t._v(t._s("null"===e.brand?"--":e.brand))]),t._v(" "),a("b",{staticStyle:{color:"#e46cbb"}},[t._v(t._s("null"===e.assetName?"--":e.assetName))]),t._v(" "),a("span",[t._v(t._s("null"===e.type?"--":e.type))]),t._v(" - "),a("span",[t._v(t._s("null"===e.assetID?"--":e.assetID))]),t._v(" - "),a("span",[t._v("超出合法范围")])]),t._v(" "),a("div",{staticClass:"device_message_item_right_bottom"},[t._v("\n                                              "+t._s("null"===e.updatetime?"--":e.updatetime)+"\n                                          ")])])])}),0)])])]),t._v(" "),t._m(0),t._v(" "),t._m(1)]),t._v(" "),a("div",{staticClass:"row_wrap hover_animat custom_bg_color_white"},[a("div",{staticClass:"row_box"},[a("h2",{staticClass:"row_title"},[t._v("新增资产列表")]),t._v(" "),a("div",{staticClass:"table_scroll_wrap"},[a("Table",{staticStyle:{"min-width":"1100px"},attrs:{stripe:"",columns:t.newAssetsColumn,data:t.newAssetsData,size:"large",loading:t.tableLoading},scopedSlots:t._u([{key:"status",fn:function(e){var s=e.row;return["正常"===s.status?a("Tag",{attrs:{color:"success"}},[t._v("正常")]):"在修"===s.status?a("Tag",{attrs:{color:"warning"}},[t._v("在修")]):"已报废"===s.status?a("Tag",{attrs:{color:"error"}},[t._v("已报废")]):"--"===s.status?a("Tag",{attrs:{color:"default"}},[t._v("未知")]):a("Tag",{attrs:{color:"default"}},[t._v(t._s(s.status))])]}}])})],1)])])])},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"custom_flex_item_3_no hover_animat custom_bg_color_white row_box"},[e("h2",{staticClass:"row_title"},[this._v("全院维修报废情况")]),this._v(" "),e("div",{staticClass:"data_block",attrs:{id:"damage_ratio_wrap"}})])},function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"custom_flex_item_3_no hover_animat custom_bg_color_white row_box"},[e("h2",{staticClass:"row_title"},[this._v("盘点情况")]),this._v(" "),e("div",{staticClass:"data_block",attrs:{id:"check_pie_wrap"}})])}]};var u=a("VU/8")(d,m,!1,function(t){a("2iNE")},"data-v-08e6512b",null);e.default=u.exports},gUqX:function(t,e,a){"use strict";e.a={color:["#19be6b","#ddbd2b","#5060f0","#e46cbb"],legend:{show:!0,data:["正常","在修","待报废","已报废"]},series:[{name:"资产状态",type:"pie",radius:"55%",center:["50%","60%"],label:{normal:{formatter:"{c}%",textStyle:{fontSize:14}}},labelLine:{normal:{length:5,lineStyle:{}}},data:[{value:90,name:"正常"},{value:6,name:"在修"},{value:3,name:"待报废"},{value:1,name:"已报废"}]}]}},gyyF:function(t,e,a){"use strict";e.a={color:["#009688","#ddbd2b"],legend:{show:!0,data:["已盘点","未盘点"]},series:[{name:"资产盘点情况",type:"pie",radius:"55%",center:["50%","60%"],label:{normal:{formatter:"{c}%",textStyle:{fontSize:14}}},labelLine:{normal:{length:5,lineStyle:{}}},data:[{value:60,name:"已盘点"},{value:40,name:"未盘点"}]}]}}});
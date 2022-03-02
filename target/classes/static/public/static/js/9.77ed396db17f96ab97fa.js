webpackJsonp([9],{"0noK":function(t,a){},"9Cmf":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=e("EMb8"),i=e("NHTL"),n=e("yoLw"),o=e("7+uW"),r=e("Gaqz"),l=e("WBHA"),c=e.n(l),u=e("gUqX"),m=e("gyyF"),d=e("eJLk"),h=e("wDsp"),_=e("TIfe");o.a.component("Table",n.a),o.a.component("Tag",i.a),o.a.component("Icon",s.a);var p={name:"home",components:{countTo:c.a},props:{},data:function(){return{statusColor:d.a,user:this.$store.state.user,deviceMessageArr:[],deviceMessageNum:{lose:0,beyond:0},websock:null,tableLoading:!0,summaryList:[{value:0,text:"资产总额",unit:"元",color:"#2d8cf0",icon:"md-cube"},{value:0,text:"资产总量",unit:"件",color:"#19be6b",icon:"ios-pie"},{value:0,text:"维修接单",unit:"件",color:"#e46cbb",icon:"md-hammer"},{value:0,text:"总盘点数",unit:"件",color:"#9a66e4",icon:"ios-contrast"}],newAssetsColumn:[{title:"资产编号",key:"assetID"},{title:"通用名称",key:"generalName"},{title:"资产名称",key:"assetName"},{title:"规格",key:"specification"},{title:"型号",key:"type"},{title:"存放地点",key:"location"},{title:"产地",key:"placeoforigin"},{title:"品牌",key:"brand"},{title:"部门编码",key:"departmentcode"},{title:"部门名称",key:"departmentroom"},{title:"大科室编码",key:"homeofficenumber"},{title:"大科室名称",key:"homeofficename"},{title:"所在科室名称",key:"locDept"},{title:"SN",key:"sn"},{title:"状态",key:"status",slot:"status",width:120}],assetsConfigArr:["electric","generalName","assetName","location","specification","type","placeoforigin","brand","departmentcode","departmentroom","homeofficenumber","homeofficename","money","buyDate","locDept","status","recorder","mac","amount","unit","applyoddnumbers","oneclassify","secondclassify","threeclassify","fourclassify","repairPeople","repairDate","repairReason","scrapPeople","scrapDate","scrapReason","remark","sn"],newAssetsData:[],damageChart:null,checkChart:null,damageData:[0,0,0,0,0,0],checkData:[0,0,0]}},watch:{isCollapsed:function(){var t=this;setTimeout(function(){t.damageChart.resize(),t.checkChart.resize()},500)}},computed:{isCollapsed:function(){return this.$store.state.isCollapsed}},methods:{transWebsocketFn:function(t){var a=[],e=t.split("{");return e.splice(0,1),e.forEach(function(t,e){var s=t.split("}");s.splice(s.length-1,1);var i={};s[0].split(",").forEach(function(t,a){var e=t.split("=");i[e[0].trim()]=e[1]}),a.push(i)}),a},initWebSocket:function(){this.websock=new WebSocket(h.a.deviceMsgSocket+"/"+Object(_.a)()),this.websock.onmessage=this.websocketonmessage},websocketonmessage:function(t){var a=this.transWebsocketFn(t.data);this.deviceMessageArr=a;var e,s=0;this.deviceMessageArr.forEach(function(t,a){t.locDept!==t.department&&t.department&&s++}),e=this.deviceMessageArr.length-s,this.deviceMessageNum.lose=e,this.deviceMessageNum.beyond=s},setAllOldFn:function(){this.damageChart=echarts.init(document.getElementById("damage_ratio_wrap"));var t=Math.round(this.damageData[0]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),a=Math.round(this.damageData[1]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),e=Math.round(this.damageData[2]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),s=Math.round(this.damageData[3]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),i=Math.round(this.damageData[4]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),n=100-(t+e+a+s+i);u.a.series[0].data=[{value:t,name:"正常"},{value:a,name:"待维修"},{value:e,name:"维修接单"},{value:s,name:"待报废"},{value:i,name:"报废"},{value:n,name:"外借"}],this.damageChart.setOption(u.a)},setCheckFn:function(){this.checkChart=echarts.init(document.getElementById("check_pie_wrap"));var t=Math.round(this.checkData[0]/(this.checkData[0]+this.checkData[1])*100),a=100-t;m.a.series[0].data=[{value:t,name:"今日已盘"},{value:a,name:"今日未盘"}],this.checkChart.setOption(m.a)},getDataFn:function(){var t=this;this.$Loading.start(),this.$axios.post(h.a.assetsQuery).then(function(a){for(var e=a.data,s=(Number(e.pop().total),function(a){var s=[];for(var i in e[a])""!==e[a][i]&&"null"!==e[a][i]||(e[a][i]="--"),s.push(i);t.assetsConfigArr.forEach(function(t,i){s.find(function(a,e){return a===t})||(e[a][t]="--")})}),i=0;i<e.length;i++)s(i);t.newAssetsData=e,t.$Loading.finish(),t.tableLoading=!1}).catch(function(a){t.$Loading.error()});this.user;this.$axios.post(h.a.assetsTotalNum).then(function(a){var e=a.data[0];"failed"===e.msg||(t.summaryList.forEach(function(t,a){switch(t.text){case"资产总额":t.value=e.aggregate;break;case"资产总量":t.value=e.examined+e.unexamined;break;case"维修接单":t.value=e.maintain;break;case"总盘点数":t.value=e.totalCheck?e.totalCheck:0;break;default:return}}),t.damageData=[e.normal?e.normal:0,e.tobescrap?e.tobescrap:0,e.maintain?e.maintain:0,e.scrap?e.scrap:0,e.hasScrap?e.hasScrap:0,e.lend?e.lend:0],t.checkData=[e.examined,e.unexamined,e.totalCheck],t.setAllOldFn(),t.setCheckFn())}).catch(function(t){})},afterResizeFn:function(){this.damageChart.resize(),this.checkChart.resize()}},created:function(){this.initWebSocket()},mounted:function(){var t=this;this.$nextTick(function(){Object(r.a)().then(function(){t.getDataFn(),window.addEventListener("resize",t.afterResizeFn,!0)})})},beforeDestroy:function(){this.websock.close(),window.removeEventListener("resize",this.afterResizeFn,!0)}},v={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"home_wrap"},[e("div",{staticClass:"custom_flex_wrap total_data_wrap"},t._l(t.summaryList,function(a,s){return e("div",{key:s,staticClass:"total_data_item custom_flex_item_4 custom_bg_color_white hover_animat"},[e("div",{staticClass:"total_icon_box",style:"background-color:"+a.color},[e("Icon",{attrs:{type:a.icon}})],1),t._v(" "),e("div",{staticClass:"total_item_box"},[e("div",{staticClass:"total_item_box_top"},[e("countTo",{attrs:{startVal:0,endVal:Number(a.value),separator:"",duration:1e3,decimals:a.value.toString().indexOf(".")>-1?a.value.toString().length-(a.value.toString().indexOf(".")+1):0,suffix:a.unit}})],1),t._v(" "),e("div",{staticClass:"total_item_box_bottom"},[t._v("\n                      "+t._s(a.text)+"\n                  ")])])])}),0),t._v(" "),e("div",{staticClass:"custom_flex_wrap row_wrap"},[e("div",{staticClass:"custom_flex_item_3_no hover_animat custom_bg_color_white row_box"},[e("h2",{staticClass:"row_title"},[t._v("设备动态")]),t._v(" "),e("div",{staticClass:"device_message_box",staticStyle:{"margin-bottom":"10px"}},[t.deviceMessageArr.length<=0?e("h3",[t._v("暂无动态...")]):e("div",{staticClass:"device_message_scroll_wrap"},[e("div",{staticClass:"device_message_scroll_box"},t._l(t.deviceMessageArr,function(a,s){return e("div",{key:s,staticClass:"device_message_item"},[e("div",{staticClass:"device_message_item_left"},[e("div",{class:a.locDept!==a.department&&a.department?"device_message_icon_box device_message_bg_color_2":"device_message_icon_box device_message_bg_color_1"},[e("Icon",{attrs:{type:"ios-warning"}})],1)]),t._v(" "),e("div",{staticClass:"device_message_item_right"},[e("div",{staticClass:"device_message_item_right_top"},[e("span",[t._v(t._s(s+1)+" - ")]),e("span",[t._v(t._s("null"===a.brand?"":a.brand))]),t._v(" - "),e("b",{class:a.locDept!==a.department&&a.department?"device_message_color_2":"device_message_color_1"},[t._v(t._s("null"===a.assetName?"":a.assetName))]),t._v(" - "),e("span",[t._v(t._s("null"===a.type?"":a.type))]),t._v(" - "),e("span",[t._v(t._s("null"===a.assetID?"":a.assetID))]),t._v(" - "),e("span",[t._v(t._s(a.locDept!==a.department&&a.department?"超出电子围栏范围":"丢失信号"))])]),t._v(" "),e("div",{staticClass:"device_message_item_right_bottom"},[t._v("\n                                      最近更新时间："+t._s("null"===a.updatetime?"":a.updatetime)+" / 房间名："+t._s("null"===a.cadMapRoomName?"":a.cadMapRoomName)+"\n                                  ")]),t._v(" "),e("div",{staticClass:"device_message_item_right_bottom"},[t._v("\n                                      所属科室："+t._s("null"===a.locDept?"":a.locDept)+" / 实时所属科室："+t._s("null"===a.department?"":a.department)+"\n                                  ")])])])}),0)])]),t._v(" "),e("div",{staticClass:"custom_flex_wrap"},[e("div",{staticClass:"device_message_num_item"},[e("span",[t._v(t._s(t.deviceMessageNum.lose)+"件")])]),t._v(" "),e("div",{staticClass:"device_message_num_item"},[e("span",[t._v(t._s(t.deviceMessageNum.beyond)+"件")])])])]),t._v(" "),e("div",{staticClass:"custom_flex_item_3_no hover_animat custom_bg_color_white row_box"},[e("h2",{staticClass:"row_title"},[t._v("维修报废")]),t._v(" "),e("div",{staticClass:"data_block",staticStyle:{"margin-bottom":"10px"},attrs:{id:"damage_ratio_wrap"}}),t._v(" "),e("div",{staticClass:"custom_flex_wrap"},[e("div",{staticClass:"damage_num_item"},[e("span",[t._v(t._s(t.damageData[0])+"件")])]),t._v(" "),e("div",{staticClass:"damage_num_item"},[e("span",[t._v(t._s(t.damageData[1])+"件")])]),t._v(" "),e("div",{staticClass:"damage_num_item"},[e("span",[t._v(t._s(t.damageData[2])+"件")])]),t._v(" "),e("div",{staticClass:"damage_num_item"},[e("span",[t._v(t._s(t.damageData[3])+"件")])]),t._v(" "),e("div",{staticClass:"damage_num_item"},[e("span",[t._v(t._s(t.damageData[4])+"件")])]),t._v(" "),e("div",{staticClass:"damage_num_item"},[e("span",[t._v(t._s(t.damageData[5])+"件")])])])]),t._v(" "),e("div",{staticClass:"custom_flex_item_3_no hover_animat custom_bg_color_white row_box"},[e("h2",{staticClass:"row_title"},[t._v("今日盘点")]),t._v(" "),e("div",{staticClass:"data_block",staticStyle:{"margin-bottom":"10px"},attrs:{id:"check_pie_wrap"}}),t._v(" "),e("div",{staticClass:"custom_flex_wrap"},[e("div",{staticClass:"check_num_item"},[e("span",[t._v(t._s(t.checkData[0])+"件")])]),t._v(" "),e("div",{staticClass:"check_num_item"},[e("span",[t._v(t._s(t.checkData[1])+"件")])])])])]),t._v(" "),e("div",{staticClass:"row_wrap hover_animat custom_bg_color_white"},[e("div",{staticClass:"row_box"},[e("h2",{staticClass:"row_title"},[t._v("最近新增资产")]),t._v(" "),e("div",{staticClass:"table_scroll_wrap"},[e("Table",{staticStyle:{"min-width":"1500px"},attrs:{stripe:"",columns:t.newAssetsColumn,data:t.newAssetsData,size:"large",loading:t.tableLoading},scopedSlots:t._u([{key:"status",fn:function(a){var s=a.row;return["正常"===s.status?e("Tag",{attrs:{color:t.statusColor[0]}},[e("span",[t._v(t._s(s.status))])]):"待维修"===s.status?e("Tag",{attrs:{color:t.statusColor[1]}},[e("span",[t._v(t._s(s.status))])]):"维修接单"===s.status?e("Tag",{attrs:{color:t.statusColor[2]}},[e("span",[t._v(t._s(s.status))])]):"待报废"===s.status?e("Tag",{attrs:{color:t.statusColor[3]}},[e("span",[t._v(t._s(s.status))])]):"报废"===s.status?e("Tag",{attrs:{color:t.statusColor[4]}},[e("span",[t._v(t._s(s.status))])]):"外借"===s.status?e("Tag",{attrs:{color:t.statusColor[5]}},[e("span",[t._v(t._s(s.status))])]):e("Tag",{attrs:{color:"default"}},[e("span",[t._v(t._s(s.status))])])]}}])})],1)])])])},staticRenderFns:[]};var f=e("VU/8")(p,v,!1,function(t){e("0noK")},"data-v-82614c8e",null);a.default=f.exports},WBHA:function(t,a,e){var s;s=function(){return function(t){function a(s){if(e[s])return e[s].exports;var i=e[s]={i:s,l:!1,exports:{}};return t[s].call(i.exports,i,i.exports,a),i.l=!0,i.exports}var e={};return a.m=t,a.c=e,a.i=function(t){return t},a.d=function(t,e,s){a.o(t,e)||Object.defineProperty(t,e,{configurable:!1,enumerable:!0,get:s})},a.n=function(t){var e=t&&t.__esModule?function(){return t.default}:function(){return t};return a.d(e,"a",e),e},a.o=function(t,a){return Object.prototype.hasOwnProperty.call(t,a)},a.p="/dist/",a(a.s=2)}([function(t,a,e){var s=e(4)(e(1),e(5),null,null);t.exports=s.exports},function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=e(3);a.default={props:{startVal:{type:Number,required:!1,default:0},endVal:{type:Number,required:!1,default:2017},duration:{type:Number,required:!1,default:3e3},autoplay:{type:Boolean,required:!1,default:!0},decimals:{type:Number,required:!1,default:0,validator:function(t){return t>=0}},decimal:{type:String,required:!1,default:"."},separator:{type:String,required:!1,default:","},prefix:{type:String,required:!1,default:""},suffix:{type:String,required:!1,default:""},useEasing:{type:Boolean,required:!1,default:!0},easingFn:{type:Function,default:function(t,a,e,s){return e*(1-Math.pow(2,-10*t/s))*1024/1023+a}}},data:function(){return{localStartVal:this.startVal,displayValue:this.formatNumber(this.startVal),printVal:null,paused:!1,localDuration:this.duration,startTime:null,timestamp:null,remaining:null,rAF:null}},computed:{countDown:function(){return this.startVal>this.endVal}},watch:{startVal:function(){this.autoplay&&this.start()},endVal:function(){this.autoplay&&this.start()}},mounted:function(){this.autoplay&&this.start(),this.$emit("mountedCallback")},methods:{start:function(){this.localStartVal=this.startVal,this.startTime=null,this.localDuration=this.duration,this.paused=!1,this.rAF=(0,s.requestAnimationFrame)(this.count)},pauseResume:function(){this.paused?(this.resume(),this.paused=!1):(this.pause(),this.paused=!0)},pause:function(){(0,s.cancelAnimationFrame)(this.rAF)},resume:function(){this.startTime=null,this.localDuration=+this.remaining,this.localStartVal=+this.printVal,(0,s.requestAnimationFrame)(this.count)},reset:function(){this.startTime=null,(0,s.cancelAnimationFrame)(this.rAF),this.displayValue=this.formatNumber(this.startVal)},count:function(t){this.startTime||(this.startTime=t),this.timestamp=t;var a=t-this.startTime;this.remaining=this.localDuration-a,this.useEasing?this.countDown?this.printVal=this.localStartVal-this.easingFn(a,0,this.localStartVal-this.endVal,this.localDuration):this.printVal=this.easingFn(a,this.localStartVal,this.endVal-this.localStartVal,this.localDuration):this.countDown?this.printVal=this.localStartVal-(this.localStartVal-this.endVal)*(a/this.localDuration):this.printVal=this.localStartVal+(this.localStartVal-this.startVal)*(a/this.localDuration),this.countDown?this.printVal=this.printVal<this.endVal?this.endVal:this.printVal:this.printVal=this.printVal>this.endVal?this.endVal:this.printVal,this.displayValue=this.formatNumber(this.printVal),a<this.localDuration?this.rAF=(0,s.requestAnimationFrame)(this.count):this.$emit("callback")},isNumber:function(t){return!isNaN(parseFloat(t))},formatNumber:function(t){t=t.toFixed(this.decimals);var a=(t+="").split("."),e=a[0],s=a.length>1?this.decimal+a[1]:"",i=/(\d+)(\d{3})/;if(this.separator&&!this.isNumber(this.separator))for(;i.test(e);)e=e.replace(i,"$1"+this.separator+"$2");return this.prefix+e+s+this.suffix}},destroyed:function(){(0,s.cancelAnimationFrame)(this.rAF)}}},function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=function(t){return t&&t.__esModule?t:{default:t}}(e(0));a.default=s.default,"undefined"!=typeof window&&window.Vue&&window.Vue.component("count-to",s.default)},function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=0,i="webkit moz ms o".split(" "),n=void 0,o=void 0;if("undefined"==typeof window)a.requestAnimationFrame=n=function(){},a.cancelAnimationFrame=o=function(){};else{a.requestAnimationFrame=n=window.requestAnimationFrame,a.cancelAnimationFrame=o=window.cancelAnimationFrame;for(var r=void 0,l=0;l<i.length&&(!n||!o);l++)r=i[l],a.requestAnimationFrame=n=n||window[r+"RequestAnimationFrame"],a.cancelAnimationFrame=o=o||window[r+"CancelAnimationFrame"]||window[r+"CancelRequestAnimationFrame"];n&&o||(a.requestAnimationFrame=n=function(t){var a=(new Date).getTime(),e=Math.max(0,16-(a-s)),i=window.setTimeout(function(){t(a+e)},e);return s=a+e,i},a.cancelAnimationFrame=o=function(t){window.clearTimeout(t)})}a.requestAnimationFrame=n,a.cancelAnimationFrame=o},function(t,a){t.exports=function(t,a,e,s){var i,n=t=t||{},o=typeof t.default;"object"!==o&&"function"!==o||(i=t,n=t.default);var r="function"==typeof n?n.options:n;if(a&&(r.render=a.render,r.staticRenderFns=a.staticRenderFns),e&&(r._scopeId=e),s){var l=Object.create(r.computed||null);Object.keys(s).forEach(function(t){var a=s[t];l[t]=function(){return a}}),r.computed=l}return{esModule:i,exports:n,options:r}}},function(t,a){t.exports={render:function(){var t=this,a=t.$createElement;return(t._self._c||a)("span",[t._v("\n  "+t._s(t.displayValue)+"\n")])},staticRenderFns:[]}}])},t.exports=s()},gUqX:function(t,a,e){"use strict";a.a={color:["#19be6b","#ddbd2b","#ff9900","#e46cbb","#ed4014","#9a66e4"],legend:{show:!0,data:["正常","待维修","维修接单","待报废","报废","外借"]},series:[{name:"资产状态",type:"pie",radius:"55%",center:["50%","60%"],label:{normal:{formatter:"{c}%",textStyle:{fontSize:14}}},labelLine:{normal:{length:5,lineStyle:{}}},data:[{value:90,name:"正常"},{value:3,name:"待维修"},{value:1,name:"维修接单"},{value:2,name:"待报废"},{value:1,name:"报废"},{value:3,name:"外借"}]}]}},gyyF:function(t,a,e){"use strict";a.a={color:["#009688","#ddbd2b"],legend:{show:!0,data:["今日已盘","今日未盘"]},series:[{name:"资产盘点情况",type:"pie",radius:"55%",center:["50%","60%"],label:{normal:{formatter:"{c}%",textStyle:{fontSize:14}}},labelLine:{normal:{length:5,lineStyle:{}}},data:[{value:60,name:"今日已盘"},{value:40,name:"今日未盘"}]}]}}});
webpackJsonp([2],{"+V+a":function(t,a){},WBHA:function(t,a,e){var i;i=function(){return function(t){function a(i){if(e[i])return e[i].exports;var n=e[i]={i:i,l:!1,exports:{}};return t[i].call(n.exports,n,n.exports,a),n.l=!0,n.exports}var e={};return a.m=t,a.c=e,a.i=function(t){return t},a.d=function(t,e,i){a.o(t,e)||Object.defineProperty(t,e,{configurable:!1,enumerable:!0,get:i})},a.n=function(t){var e=t&&t.__esModule?function(){return t.default}:function(){return t};return a.d(e,"a",e),e},a.o=function(t,a){return Object.prototype.hasOwnProperty.call(t,a)},a.p="/dist/",a(a.s=2)}([function(t,a,e){var i=e(4)(e(1),e(5),null,null);t.exports=i.exports},function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var i=e(3);a.default={props:{startVal:{type:Number,required:!1,default:0},endVal:{type:Number,required:!1,default:2017},duration:{type:Number,required:!1,default:3e3},autoplay:{type:Boolean,required:!1,default:!0},decimals:{type:Number,required:!1,default:0,validator:function(t){return t>=0}},decimal:{type:String,required:!1,default:"."},separator:{type:String,required:!1,default:","},prefix:{type:String,required:!1,default:""},suffix:{type:String,required:!1,default:""},useEasing:{type:Boolean,required:!1,default:!0},easingFn:{type:Function,default:function(t,a,e,i){return e*(1-Math.pow(2,-10*t/i))*1024/1023+a}}},data:function(){return{localStartVal:this.startVal,displayValue:this.formatNumber(this.startVal),printVal:null,paused:!1,localDuration:this.duration,startTime:null,timestamp:null,remaining:null,rAF:null}},computed:{countDown:function(){return this.startVal>this.endVal}},watch:{startVal:function(){this.autoplay&&this.start()},endVal:function(){this.autoplay&&this.start()}},mounted:function(){this.autoplay&&this.start(),this.$emit("mountedCallback")},methods:{start:function(){this.localStartVal=this.startVal,this.startTime=null,this.localDuration=this.duration,this.paused=!1,this.rAF=(0,i.requestAnimationFrame)(this.count)},pauseResume:function(){this.paused?(this.resume(),this.paused=!1):(this.pause(),this.paused=!0)},pause:function(){(0,i.cancelAnimationFrame)(this.rAF)},resume:function(){this.startTime=null,this.localDuration=+this.remaining,this.localStartVal=+this.printVal,(0,i.requestAnimationFrame)(this.count)},reset:function(){this.startTime=null,(0,i.cancelAnimationFrame)(this.rAF),this.displayValue=this.formatNumber(this.startVal)},count:function(t){this.startTime||(this.startTime=t),this.timestamp=t;var a=t-this.startTime;this.remaining=this.localDuration-a,this.useEasing?this.countDown?this.printVal=this.localStartVal-this.easingFn(a,0,this.localStartVal-this.endVal,this.localDuration):this.printVal=this.easingFn(a,this.localStartVal,this.endVal-this.localStartVal,this.localDuration):this.countDown?this.printVal=this.localStartVal-(this.localStartVal-this.endVal)*(a/this.localDuration):this.printVal=this.localStartVal+(this.localStartVal-this.startVal)*(a/this.localDuration),this.countDown?this.printVal=this.printVal<this.endVal?this.endVal:this.printVal:this.printVal=this.printVal>this.endVal?this.endVal:this.printVal,this.displayValue=this.formatNumber(this.printVal),a<this.localDuration?this.rAF=(0,i.requestAnimationFrame)(this.count):this.$emit("callback")},isNumber:function(t){return!isNaN(parseFloat(t))},formatNumber:function(t){t=t.toFixed(this.decimals);var a=(t+="").split("."),e=a[0],i=a.length>1?this.decimal+a[1]:"",n=/(\d+)(\d{3})/;if(this.separator&&!this.isNumber(this.separator))for(;n.test(e);)e=e.replace(n,"$1"+this.separator+"$2");return this.prefix+e+i+this.suffix}},destroyed:function(){(0,i.cancelAnimationFrame)(this.rAF)}}},function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var i=function(t){return t&&t.__esModule?t:{default:t}}(e(0));a.default=i.default,"undefined"!=typeof window&&window.Vue&&window.Vue.component("count-to",i.default)},function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var i=0,n="webkit moz ms o".split(" "),s=void 0,r=void 0;if("undefined"==typeof window)a.requestAnimationFrame=s=function(){},a.cancelAnimationFrame=r=function(){};else{a.requestAnimationFrame=s=window.requestAnimationFrame,a.cancelAnimationFrame=r=window.cancelAnimationFrame;for(var o=void 0,l=0;l<n.length&&(!s||!r);l++)o=n[l],a.requestAnimationFrame=s=s||window[o+"RequestAnimationFrame"],a.cancelAnimationFrame=r=r||window[o+"CancelAnimationFrame"]||window[o+"CancelRequestAnimationFrame"];s&&r||(a.requestAnimationFrame=s=function(t){var a=(new Date).getTime(),e=Math.max(0,16-(a-i)),n=window.setTimeout(function(){t(a+e)},e);return i=a+e,n},a.cancelAnimationFrame=r=function(t){window.clearTimeout(t)})}a.requestAnimationFrame=s,a.cancelAnimationFrame=r},function(t,a){t.exports=function(t,a,e,i){var n,s=t=t||{},r=typeof t.default;"object"!==r&&"function"!==r||(n=t,s=t.default);var o="function"==typeof s?s.options:s;if(a&&(o.render=a.render,o.staticRenderFns=a.staticRenderFns),e&&(o._scopeId=e),i){var l=Object.create(o.computed||null);Object.keys(i).forEach(function(t){var a=i[t];l[t]=function(){return a}}),o.computed=l}return{esModule:n,exports:s,options:o}}},function(t,a){t.exports={render:function(){var t=this,a=t.$createElement;return(t._self._c||a)("span",[t._v("\n  "+t._s(t.displayValue)+"\n")])},staticRenderFns:[]}}])},t.exports=i()},ZV4y:function(t,a,e){e("XqYu"),e("+V+a")},ZYBX:function(t,a,e){e("XqYu"),e("+ed2"),e("s1Ps"),e("r9Lt")},gUqX:function(t,a,e){"use strict";a.a={color:["#19be6b","#ddbd2b","#ff9900","#e46cbb","#ed4014","#9a66e4"],legend:{show:!0,data:["正常","待维修","维修接单","待报废","报废","外借"]},series:[{name:"资产状态",type:"pie",radius:"55%",center:["50%","60%"],label:{normal:{formatter:"{c}%",textStyle:{fontSize:14}}},labelLine:{normal:{length:5,lineStyle:{}}},data:[{value:90,name:"正常"},{value:3,name:"待维修"},{value:1,name:"维修接单"},{value:2,name:"待报废"},{value:1,name:"报废"},{value:3,name:"外借"}]}]}},gyyF:function(t,a,e){"use strict";a.a={color:["#009688","#ddbd2b"],legend:{show:!0,data:["今日已盘","今日未盘"]},series:[{name:"资产盘点情况",type:"pie",radius:"55%",center:["50%","60%"],label:{normal:{formatter:"{c}%",textStyle:{fontSize:14}}},labelLine:{normal:{length:5,lineStyle:{}}},data:[{value:60,name:"今日已盘"},{value:40,name:"今日未盘"}]}]}},l4Va:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var i=e("Gaqz"),n=e("gUqX"),s=e("gyyF"),r=e("WBHA"),o=e.n(r),l=e("Fd2+"),u=(e("FDxC"),e("jydU"),e("ZYBX"),e("tLo2"),e("ZV4y"),e("wDsp")),c={name:"mobile_home",components:{Icon:l.m,ActionSheet:l.a,Popup:l.t,Button:l.b,PullRefresh:l.u,countTo:o.a},props:{},data:function(){return{user:this.$store.state.user,role:this.$store.state.role,summaryList:[{value:0,text:"资产总额",unit:"元",color:"#2d8cf0",icon:"md-cube"},{value:0,text:"资产总量",unit:"件",color:"#19be6b",icon:"ios-pie"},{value:0,text:"维修接单",unit:"件",color:"#e46cbb",icon:"md-hammer"},{value:0,text:"总盘点数",unit:"件",color:"#9a66e4",icon:"ios-contrast"}],damageChart:null,checkChart:null,damageData:[0,0,0,0,0,0],checkData:[0,0,0],isLoading:!1}},watch:{},computed:{},methods:{showMoreMenuFn:function(){this.$emit("getMessage",!0)},onRefresh:function(){var t=this;this.getData(),setTimeout(function(){t.isLoading=!1},500)},getData:function(){var t=this;this.$axios.post(u.a.assetsTotalNum).then(function(a){var e=a.data[0];"failed"===e.msg||(t.summaryList.forEach(function(t,a){switch(t.text){case"资产总额":t.value=e.aggregate;break;case"资产总量":t.value=e.examined+e.unexamined;break;case"维修接单":t.value=e.maintain;break;case"总盘点数":t.value=e.totalCheck?e.totalCheck:0;break;default:return}}),t.damageData=[e.normal?e.normal:0,e.tobescrap?e.tobescrap:0,e.maintain?e.maintain:0,e.scrap?e.scrap:0,e.hasScrap?e.hasScrap:0,e.lend?e.lend:0],t.checkData=[e.examined,e.unexamined,e.totalCheck],t.setDamageFn(),t.setCheckFn())}).catch(function(t){})},setDamageFn:function(){this.damageChart=echarts.init(document.getElementById("damage_ratio_wrap"));var t=Math.round(this.damageData[0]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),a=Math.round(this.damageData[1]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),e=Math.round(this.damageData[2]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),i=Math.round(this.damageData[3]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),s=Math.round(this.damageData[4]/(this.damageData[0]+this.damageData[1]+this.damageData[2]+this.damageData[3]+this.damageData[4]+this.damageData[5])*100),r=100-(t+e+a+i+s);n.a.series[0].data=[{value:t,name:"正常"},{value:a,name:"待维修"},{value:e,name:"维修接单"},{value:i,name:"待报废"},{value:s,name:"报废"},{value:r,name:"外借"}],this.damageChart.setOption(n.a)},setCheckFn:function(){this.checkChart=echarts.init(document.getElementById("check_pie_wrap"));var t=Math.round(this.checkData[0]/(this.checkData[0]+this.checkData[1])*100),a=100-t;s.a.series[0].data=[{value:t,name:"今日已盘"},{value:a,name:"今日未盘"}],this.checkChart.setOption(s.a)}},created:function(){},mounted:function(){var t=this;this.$nextTick(function(){Object(i.a)().then(function(){t.getData()})})},activated:function(){window.scrollTo(0,0)}},m={render:function(){var t=this,a=t.$createElement,i=t._self._c||a;return i("div",{staticClass:"mobile_home_wrap"},[i("PullRefresh",{on:{refresh:t.onRefresh},model:{value:t.isLoading,callback:function(a){t.isLoading=a},expression:"isLoading"}},[i("div",{staticClass:"home_box"},[i("div",{staticClass:"home_title_wrap custom_row_wrap"},[i("div",{staticClass:"home_title_box custom_flex_row_wrap"},[i("div",{staticClass:"hello_box"},[i("h2",[t._v("Hello,"+t._s(t.user))]),t._v(" "),i("h3",[t._v("欢迎回来！")])]),t._v(" "),i("div",{staticClass:"user_box"},[i("img",{attrs:{src:e("izX0"),alt:""}}),t._v(" "),i("div",{staticClass:"more_menu_box",on:{click:t.showMoreMenuFn}},[i("Icon",{attrs:{name:"bars"}})],1)])])]),t._v(" "),i("div",{staticClass:"home_total_wrap custom_row_wrap"},[i("div",{staticClass:"home_total_box custom_flex_row_wrap"},t._l(t.summaryList,function(a,e){return i("div",{key:e,staticClass:"total_box_item"},[i("span",[t._v(t._s(a.text))]),t._v(" "),i("h3",[i("countTo",{attrs:{startVal:0,endVal:Number(a.value),separator:"",duration:1e3,decimals:a.value.toString().indexOf(".")>-1?a.value.toString().length-(a.value.toString().indexOf(".")+1):0,suffix:a.unit}})],1)])}),0)]),t._v(" "),i("div",{staticClass:"home_chart_wrap custom_row_wrap"},[i("div",{staticClass:"home_chart_box"},[i("div",{staticClass:"home_chart_item"},[i("div",{staticClass:"home_chart_item_title"},[i("h3",[t._v("维修报废")])]),t._v(" "),i("div",{staticClass:"home_chart_item_box"},[i("div",{staticClass:"home_chart_item_block",attrs:{id:"damage_ratio_wrap"}}),t._v(" "),i("div",{staticClass:"custom_flex_row_wrap"},[i("div",{staticClass:"damage_num_item"},[i("span",[t._v(t._s(t.damageData[0])+"件")])]),t._v(" "),i("div",{staticClass:"damage_num_item"},[i("span",[t._v(t._s(t.damageData[1])+"件")])]),t._v(" "),i("div",{staticClass:"damage_num_item"},[i("span",[t._v(t._s(t.damageData[2])+"件")])]),t._v(" "),i("div",{staticClass:"damage_num_item"},[i("span",[t._v(t._s(t.damageData[3])+"件")])]),t._v(" "),i("div",{staticClass:"damage_num_item"},[i("span",[t._v(t._s(t.damageData[4])+"件")])]),t._v(" "),i("div",{staticClass:"damage_num_item"},[i("span",[t._v(t._s(t.damageData[5])+"件")])])])])]),t._v(" "),i("div",{staticClass:"home_chart_item"},[i("div",{staticClass:"home_chart_item_title"},[i("h3",[t._v("今日盘点")])]),t._v(" "),i("div",{staticClass:"home_chart_item_box"},[i("div",{staticClass:"home_chart_item_block",attrs:{id:"check_pie_wrap"}}),t._v(" "),i("div",{staticClass:"custom_flex_row_wrap"},[i("div",{staticClass:"check_num_item"},[i("span",[t._v(t._s(t.checkData[0])+"件")])]),t._v(" "),i("div",{staticClass:"check_num_item"},[i("span",[t._v(t._s(t.checkData[1])+"件")])])])])])])])])])],1)},staticRenderFns:[]};var d=e("VU/8")(c,m,!1,function(t){e("xM9Q")},"data-v-50508472",null);a.default=d.exports},r9Lt:function(t,a){},xM9Q:function(t,a){}});
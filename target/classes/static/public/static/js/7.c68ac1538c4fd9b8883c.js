webpackJsonp([7],{M8Zc:function(e,t){},SsyO:function(e,t,a){a("XqYu"),a("M8Zc")},eoO4:function(e,t,a){a("XqYu"),a("s1Ps"),a("h+VL")},"h+VL":function(e,t){},psXt:function(e,t){},qRWv:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=a("Fd2+"),n=(a("SsyO"),a("eoO4"),{name:"mobile2_wrap",components:{Tabbar:o.x,TabbarItem:o.y},props:["deviceInfoNum"],data:function(){return{active:"mobile_home"}},watch:{},computed:{},methods:{},created:function(){},mounted:function(){}}),c={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"mobile_wrap"},[a("router-view"),e._v(" "),a("Tabbar",{attrs:{route:""},model:{value:e.active,callback:function(t){e.active=t},expression:"active"}},[a("TabbarItem",{attrs:{to:"/mobile/home",name:"mobile_home",icon:"home-o"}},[e._v("首页")]),e._v(" "),a("TabbarItem",{attrs:{to:"/mobile/assets_manage",name:"mobile_assets_create",icon:"bar-chart-o"}},[e._v("资产管理")]),e._v(" "),a("TabbarItem",{attrs:{to:"/mobile/check_manage",name:"mobile_assets_check",icon:"certificate"}},[e._v("资产盘点")]),e._v(" "),a("TabbarItem",{attrs:{to:"/mobile/device_message",name:"mobile_my",icon:"more-o",info:0===e.deviceInfoNum?"":e.deviceInfoNum}},[e._v("设备动态")])],1)],1)},staticRenderFns:[]};var i=a("VU/8")(n,c,!1,function(e){a("psXt")},"data-v-51151592",null);t.default=i.exports}});
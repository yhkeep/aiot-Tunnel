webpackJsonp([22],{Odgr:function(t,s,a){"use strict";Object.defineProperty(s,"__esModule",{value:!0});var e=a("Fd2+"),i=(a("jgNZ"),a("dKGA"),a("FDxC"),a("2PSJ"),a("uTM9"),a("9ULi"),a("jydU"),a("wvM5"),a("tLo2"),a("wDsp")),l=(a("eJLk"),{name:"assets_manage",components:{Button:e.b,Row:e.t,Col:e.g,Tag:e.z,Loading:e.m,Search:e.u,Icon:e.k,Field:e.j,Popup:e.r},props:{},data:function(){return{user:this.$store.state.user,historyData:[],loadingShow:!0}},watch:{},computed:{},methods:{toDetailFn:function(t,s){this.$router.push({path:"/mobile/check_history_detail/"+t.checkOnlyCode})},getDataFn:function(){var t=this;this.loadingShow=!0;var s=[{username:this.user}];this.$axios.post(i.a.assetsCheckHistory,s).then(function(s){"success"===s.data.msg&&(t.historyData=s.data.data,t.loadingShow=!1)}).catch(function(t){})}},created:function(){this.$emit("setTitle","盘点历史")},mounted:function(){this.getDataFn()}}),_={render:function(){var t=this,s=t.$createElement,a=t._self._c||s;return a("div",{staticClass:"mobile_check_history_wrap manage_list_wrap custom_flex_column_wrap"},[a("div",{staticClass:"assets_table_wrap"},[a("div",{staticClass:"assets_table_wrap_content  clearfix"},[a("div",{staticClass:"assets_table_wrap_left"},[a("ul",{staticClass:"assets_table_ul"},[t._m(0),t._v(" "),t._l(t.historyData,function(s,e){return a("li",{key:e,staticClass:"assets_table_li"},[a("span",{staticClass:"assets_table_item"},[t._v(t._s(e+1))])])})],2)]),t._v(" "),a("div",{staticClass:"assets_table_wrap_right"},[a("ul",{staticClass:"assets_table_ul"},[t._m(1),t._v(" "),t._l(t.historyData,function(s,e){return a("li",{key:e,staticClass:"assets_table_li clearfix",on:{click:function(a){return t.toDetailFn(s,e)}}},[a("div",{staticClass:"assets_table_item"},[t._v(t._s(s.checkTime?s.checkTime:"--"))]),t._v(" "),a("div",{staticClass:"assets_table_item"},[t._v(t._s(s.checkNum?s.checkNum:"0"))]),t._v(" "),a("div",{staticClass:"assets_table_item"},[t._v(t._s(s.checkpep?s.checkpep:"--"))])])})],2)])]),t._v(" "),a("div",{staticClass:"loading_info_wrap"},[a("Loading",{directives:[{name:"show",rawName:"v-show",value:t.loadingShow,expression:"loadingShow"}],attrs:{size:"24px"}},[t._v("加载中...")])],1)])])},staticRenderFns:[function(){var t=this.$createElement,s=this._self._c||t;return s("li",{staticClass:"assets_table_li assets_table_li_name"},[s("span",{staticClass:"assets_table_item"},[this._v("序号")])])},function(){var t=this.$createElement,s=this._self._c||t;return s("li",{staticClass:"assets_table_li assets_table_li_name clearfix"},[s("div",{staticClass:"assets_table_item"},[this._v("盘点日期")]),this._v(" "),s("div",{staticClass:"assets_table_item"},[this._v("已盘点数量")]),this._v(" "),s("div",{staticClass:"assets_table_item"},[this._v("盘点人员")])])}]};var c=a("VU/8")(l,_,!1,function(t){a("aDsw")},"data-v-9ac1f5c8",null);s.default=c.exports},aDsw:function(t,s){}});
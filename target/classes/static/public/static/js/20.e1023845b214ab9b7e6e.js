webpackJsonp([20],{"6nJB":function(t,a,s){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var e=s("Fd2+"),i=(s("jgNZ"),s("dKGA"),s("FDxC"),s("4T1P"),s("2PSJ"),s("uTM9"),s("9ULi"),s("jydU"),s("wvM5"),s("wDsp")),n={name:"assets_manage",components:{Button:e.b,Row:e.u,Col:e.g,Tag:e.A,Loading:e.m,Search:e.v,Icon:e.k,Field:e.j},props:{},data:function(){return{isFirstEnter:!0,currentPage:1,totalCount:0,hasMore:!0,assetsDetailData:[],loadingShow:!0,updateId:null,needFresh:!0,queryData:{assetID:"",assetName:""}}},watch:{},computed:{assetsDetailDataNow:function(){var t=[];return this.assetsDetailData.forEach(function(a,s){var e={assetID:a.assetID,assetName:a.assetName};t.push(e)}),t}},methods:{toDetailFn:function(t,a){this.$router.push({path:"/mobile/assets_detail/"+t.assetID})},toCreateFn:function(){this.$router.push({path:"/mobile/assets_create"})},searchFn:function(){this.currentPage=1,this.assetsDetailData=[],this.getDataFn()},getDataFn:function(){var t=this;this.updateId?this.$axios.post(i.a.assetsQuery+"?currentPage="+this.currentPage+"&assetName="+this.queryData.assetName+"&assetID="+this.queryData.assetID).then(function(a){for(var s=a.data,e=(Number(s.pop().total),0);e<s.length;e++)s[e].assetID===t.updateId&&t.assetsDetailData.forEach(function(a,i){a.assetID===t.updateId&&(a=s[e])})}).catch(function(t){}):(this.loadingShow=!0,this.$axios.post(i.a.assetsQuery+"?currentPage="+this.currentPage+"&assetName="+this.queryData.assetName+"&assetID="+this.queryData.assetID).then(function(a){for(var s=a.data,e=Number(s.pop().total),i=0;i<s.length;i++){for(var n in s[i])""!==s[i][n]&&"null"!==s[i][n]||(s[i][n]="--");t.assetsDetailData.push(s[i])}e<=t.assetsDetailData.length?t.hasMore=!1:t.hasMore=!0,t.totalCount=e,t.loadingShow=!1}).catch(function(t){}))},getMoreFn:function(){this.currentPage+=1,this.getDataFn()},initAllDataFn:function(){this.loadingKey=!0,this.currentPage=1,this.totalCount=0,this.hasMore=!0,this.assetsDetailData=[],this.queryData={assetID:"",assetName:""}}},beforeRouteEnter:function(t,a,s){t.meta.isBack=!0,s()},beforeRouteLeave:function(t,a,s){var e=document.body.scrollTop||document.documentElement.scrollTop;window.sessionStorage.setItem("mobileScroll",e),s()},created:function(){this.isFirstEnter=!0},mounted:function(){},activated:function(){window.sessionStorage.getItem("mobileScroll")&&window.scrollTo(0,window.sessionStorage.getItem("mobileScroll")),this.$route.meta.isBack&&!this.isFirstEnter||(this.initAllDataFn(),this.getDataFn()),this.$route.meta.isBack=!1,this.isFirstEnter=!1},deactivated:function(){},beforeDestroy:function(){}},o={render:function(){var t=this,a=t.$createElement,s=t._self._c||a;return s("div",{staticClass:"mobile_assets_manage_wrap manage_list_wrap custom_flex_column_wrap"},[s("div",{staticClass:"query_data_wrap"},[s("div",{staticClass:"query_data_box"},[s("Field",{attrs:{label:"资产编号",placeholder:"请输入要查询的资产编号"},model:{value:t.queryData.assetID,callback:function(a){t.$set(t.queryData,"assetID",a)},expression:"queryData.assetID"}}),t._v(" "),s("Field",{attrs:{label:"资产名称",placeholder:"请输入要查询的资产名称"},model:{value:t.queryData.assetName,callback:function(a){t.$set(t.queryData,"assetName",a)},expression:"queryData.assetName"}}),t._v(" "),s("div",{staticClass:"tools_button_wrap"},[s("Button",{staticClass:"tools_button",attrs:{icon:"search",type:"primary",round:""},on:{click:t.searchFn}},[t._v("搜索")]),t._v(" "),s("Button",{staticClass:"tools_button",attrs:{icon:"plus",type:"info",round:""},on:{click:t.toCreateFn}},[t._v("新增")])],1)],1)]),t._v(" "),s("div",{staticClass:"assets_table_wrap"},[s("ul",{staticClass:"assets_table_ul"},[s("li",{staticClass:"assets_table_li assets_table_li_name"},[s("Row",[s("Col",{attrs:{span:"3"}},[s("h4",{staticClass:"assets_table_item assets_table_item_index"},[t._v("序号")])]),t._v(" "),s("Col",{attrs:{span:"10"}},[s("h4",{staticClass:"assets_table_item"},[t._v("资产id")])]),t._v(" "),s("Col",{attrs:{span:"11"}},[s("h4",{staticClass:"assets_table_item"},[t._v("资产名称")])])],1)],1),t._v(" "),t._l(t.assetsDetailData,function(a,e){return s("li",{key:e,staticClass:"assets_table_li",on:{click:function(s){return t.toDetailFn(a,e)}}},[s("Row",[s("Col",{attrs:{span:"3"}},[s("span",{staticClass:"assets_table_item assets_table_item_index"},[t._v(t._s(e+1))])]),t._v(" "),s("Col",{attrs:{span:"10"}},[s("span",{staticClass:"assets_table_item"},[t._v(t._s(a.assetID))])]),t._v(" "),s("Col",{attrs:{span:"11"}},[s("span",{staticClass:"assets_table_item"},[t._v(t._s(a.assetName?a.assetName:"--"))])])],1)],1)})],2),t._v(" "),s("div",{staticClass:"loading_info_wrap"},[s("Button",{directives:[{name:"show",rawName:"v-show",value:!t.loadingShow,expression:"!loadingShow"}],attrs:{disabled:!t.hasMore},on:{click:t.getMoreFn}},[t._v(t._s(!0===t.hasMore?"点击加载更多":"已无更多数据"))]),t._v(" "),s("Loading",{directives:[{name:"show",rawName:"v-show",value:t.loadingShow,expression:"loadingShow"}],attrs:{size:"24px"}},[t._v("加载中...")])],1)])])},staticRenderFns:[]};var r=s("VU/8")(n,o,!1,function(t){s("Y9Zs")},"data-v-72b4bebe",null);a.default=r.exports},Y9Zs:function(t,a){}});
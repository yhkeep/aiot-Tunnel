webpackJsonp([5],{"6NuU":function(t,a,e){e("XqYu")},"6d7H":function(t,a){},I40i:function(t,a){},Medb:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=e("Fd2+"),i=(e("jgNZ"),e("dKGA"),e("QP/A"),e("cnGM"),e("FDxC"),e("4T1P"),e("2PSJ"),e("d9cu"),e("OeKT"),e("6NuU"),e("MHRi"),e("GKy3"),e("uTM9"),e("UEU1"),e("wvM5"),e("wDsp")),n=e("TIfe"),o=(e("q29v"),{name:"mobile_check_manage",components:{Button:s.b,CellGroup:s.d,Cell:s.c,Row:s.u,Col:s.g,Tag:s.A,CheckboxGroup:s.f,Checkbox:s.e,Overlay:s.p,Dialog:s.i,Toast:s.B,Loading:s.m,Pagination:s.q,Field:s.j},props:{},data:function(){return{user:this.$store.state.user,currentPage:1,totalCount:0,hasMore:!0,checked:!0,assetsDetailData:[],deviceDetailData:[],checkedArr:[],showInfo:!1,editData:{},checkAll:!1,loadingShow:!0,showPagination:!1,queryData:{assetID:"",assetName:""},websock:null}},watch:{deviceDetailData:function(){var t=this;this.deviceDetailData.forEach(function(a,e){t.assetsDetailData.forEach(function(t,e){a.Mac===t.mac&&(t.location=a.Location)})})},checkedArr:function(){this.checkedArr.length&&this.checkedArr.length===this.assetsDetailData.length?this.checkAll=!0:this.checkAll=!1}},computed:{assetsDetailDataNow:function(){return this.assetsDetailData}},methods:{searchFn:function(){this.currentPage=1,this.assetsDetailData=[],this.checkedArr=[],this.checkAll=!1,this.totalCount=0,this.getDataFn()},checkAllFn:function(){!1===this.checkAll?this.$refs.checkboxGroup.toggleAll(!0):this.$refs.checkboxGroup.toggleAll(!1)},checkFn:function(){var t=this,a=[];this.checkedArr.forEach(function(e,s){t.assetsDetailData.forEach(function(t,s){e===t.assetID&&a.push(t)})}),a.length?a.length>1?s.i.confirm({title:"盘点提示！",message:"你确定要同时盘点这"+a.length+"个资产吗？"}).then(function(){t.checkCertainFn(a)}).catch(function(){}):this.checkCertainFn(a):this.$notify({type:"warning",message:"请至少选择一个资产！",duration:2e3})},checkCertainFn:function(t){var a=this,e=[];t.forEach(function(t,a){var s={assetID:t.assetID.trim()};e.push(s)});var s=this.$toast.loading({message:"盘点中...",forbidClick:!0,duration:0});this.$axios.post(i.a.assetsCheck+"checkpep="+this.user,e).then(function(t){"success"===t.data.msg?(s.clear(),a.$toast.success("盘点成功！"),a.assetsDetailData=[],a.checkedArr=[],a.checkAll=!1,a.totalCount=0,a.getDataFn()):(s.clear(),a.$toast.fail("盘点失败！"))}).catch(function(t){s.clear(),a.$notify({type:"danger",message:t,duration:2e3})})},showInfoFn:function(t){this.editData={},this.editData=t,this.showInfo=!0},getDataFn:function(){var t=this;this.loadingShow=!0,this.assetsDetailData=[],this.$axios.post(i.a.assetsQuery+"?currentPage="+this.currentPage+"&assetName="+this.queryData.assetName+"&assetID="+this.queryData.assetID).then(function(a){for(var e=a.data,s=Number(e.pop().total),i=0;i<e.length;i++){for(var n in e[i])""!==e[i][n]&&"null"!==e[i][n]||(e[i][n]="--");t.assetsDetailData.push(e[i])}t.totalCount=s,t.loadingShow=!1,t.showPagination=!0}).catch(function(t){})},getMoreFn:function(){this.currentPage+=1,this.getDataFn()},changePageFn:function(){this.checkedArr=[],this.checkAll=!1,this.getDataFn()},transWebsocketFn:function(t){var a=[],e=t.split("{");return e.splice(0,1),e.forEach(function(t,e){var s=t.split("}");s.splice(s.length-1,1);var i={};s[0].split(",").forEach(function(t,a){var e=t.split("=");i[e[0].trim()]=e[1]}),a.push(i)}),a},initWebSocket:function(){this.websock=new WebSocket(i.a.mapSocket+"/"+Object(n.a)()),this.websock.onopen=this.websocketonopen,this.websock.onerror=this.websocketonerror,this.websock.onmessage=this.websocketonmessage,this.websock.onclose=this.websocketclose},websocketonmessage:function(t){var a=this.transWebsocketFn(t.data);this.deviceDetailData=a}},created:function(){},mounted:function(){this.getDataFn(),this.initWebSocket()},beforeDestroy:function(){this.websock.close()}}),c={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"mobile_check_manage_wrap manage_list_wrap custom_flex_column_wrap"},[e("div",{staticClass:"query_data_wrap"},[e("div",{staticClass:"query_data_box"},[e("Field",{attrs:{label:"资产编号",placeholder:"请输入要查询的资产编号"},model:{value:t.queryData.assetID,callback:function(a){t.$set(t.queryData,"assetID",a)},expression:"queryData.assetID"}}),t._v(" "),e("Field",{attrs:{label:"资产名称",placeholder:"请输入要查询的资产名称"},model:{value:t.queryData.assetName,callback:function(a){t.$set(t.queryData,"assetName",a)},expression:"queryData.assetName"}}),t._v(" "),e("div",{staticClass:"tools_button_wrap"},[e("Button",{staticClass:"tools_button",attrs:{icon:"search",type:"primary",round:""},on:{click:t.searchFn}},[t._v("搜索")]),t._v(" "),e("Button",{staticClass:"tools_button",attrs:{icon:"points",type:"info",round:""},on:{click:t.checkFn}},[t._v("盘点")])],1)],1)]),t._v(" "),e("div",{staticClass:"assets_table_wrap"},[e("ul",{staticClass:"assets_table_ul"},[e("li",{staticClass:"assets_table_li assets_table_li_name"},[e("Row",[e("Col",{attrs:{span:"3"}},[e("h4",{staticClass:"assets_table_item assets_table_item_index"},[e("Checkbox",{attrs:{shape:"square","checked-color":"#07c160"},on:{click:t.checkAllFn},model:{value:t.checkAll,callback:function(a){t.checkAll=a},expression:"checkAll"}})],1)]),t._v(" "),e("Col",{attrs:{span:"3"}},[e("h4",{staticClass:"assets_table_item"},[t._v("序号")])]),t._v(" "),e("Col",{attrs:{span:"9"}},[e("h4",{staticClass:"assets_table_item"},[t._v("资产编号")])]),t._v(" "),e("Col",{attrs:{span:"9"}},[e("h4",{staticClass:"assets_table_item"},[t._v("资产名称")])])],1)],1),t._v(" "),e("CheckboxGroup",{ref:"checkboxGroup",model:{value:t.checkedArr,callback:function(a){t.checkedArr=a},expression:"checkedArr"}},t._l(t.assetsDetailData,function(a,s){return e("li",{key:s,staticClass:"assets_table_li",on:{click:function(e){return t.showInfoFn(a)}}},[e("Row",[e("Col",{attrs:{span:"3"}},[e("span",{staticClass:"assets_table_item assets_table_item_index"},[e("Checkbox",{attrs:{name:a.assetID,shape:"square","checked-color":"#07c160"},on:{click:function(t){t.stopPropagation()}}})],1)]),t._v(" "),e("Col",{attrs:{span:"3"}},[e("span",{staticClass:"assets_table_item assets_table_item_index"},[t._v("\n                        "+t._s(s+1)+"\n                        ")])]),t._v(" "),e("Col",{attrs:{span:"9"}},[e("span",{staticClass:"assets_table_item"},[t._v(t._s(a.assetID?a.assetID:"--"))])]),t._v(" "),e("Col",{attrs:{span:"9"}},[e("span",{staticClass:"assets_table_item"},[t._v(t._s(a.assetName?a.assetName:"--"))])])],1)],1)}),0)],1),t._v(" "),e("div",{staticClass:"loading_info_wrap"},[e("Pagination",{directives:[{name:"show",rawName:"v-show",value:t.showPagination&&t.assetsDetailData.length,expression:"showPagination&&assetsDetailData.length"}],attrs:{"total-items":t.totalCount,"show-page-size":2,"prev-text":"上一页","next-text":"下一页","force-ellipses":""},on:{change:t.changePageFn},model:{value:t.currentPage,callback:function(a){t.currentPage=a},expression:"currentPage"}}),t._v(" "),e("Loading",{directives:[{name:"show",rawName:"v-show",value:t.loadingShow,expression:"loadingShow"}],attrs:{size:"24px"}},[t._v("加载中...")])],1)]),t._v(" "),e("Overlay",{attrs:{show:t.showInfo},on:{click:function(a){t.showInfo=!1}}},[e("div",{staticClass:"show_info_wrap"},[e("div",{staticClass:"show_info_box"},[e("Cell",{attrs:{title:"资产id",value:""+(t.editData.assetID?t.editData.assetID:"--")}}),t._v(" "),e("Cell",{attrs:{title:"资产名称",value:""+(t.editData.assetName?t.editData.assetName:"--")}}),t._v(" "),e("Cell",{attrs:{title:"部门名称",value:""+(t.editData.departmentroom?t.editData.departmentroom:"--")}}),t._v(" "),e("Cell",{attrs:{title:"存放地点",value:""+(t.editData.location?t.editData.location:"--")}}),t._v(" "),e("Cell",{attrs:{title:"状态"}},["正常"===t.editData.status?e("Tag",{attrs:{size:"medium",type:"success"}},[t._v(t._s(t.editData.status))]):"待维修"===t.editData.status?e("Tag",{attrs:{size:"medium",type:"info"}},[t._v(t._s(t.editData.status))]):"维修接单"===t.editData.status?e("Tag",{attrs:{size:"medium",type:"warning"}},[t._v(t._s(t.editData.status))]):"待处置"===t.editData.status?e("Tag",{attrs:{size:"medium",type:"danger"}},[t._v(t._s(t.editData.status))]):e("Tag",{attrs:{size:"medium",type:"default"}},[t._v(t._s(t.editData.status))])],1)],1)])])],1)},staticRenderFns:[]};var l=e("VU/8")(o,c,!1,function(t){e("Q8rA")},"data-v-5215fdef",null);a.default=l.exports},OeKT:function(t,a,e){e("XqYu")},Q8rA:function(t,a){},UEU1:function(t,a,e){e("XqYu"),e("I40i")},d9cu:function(t,a,e){e("XqYu"),e("s1Ps"),e("6d7H")}});
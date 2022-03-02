webpackJsonp([34],{"+uwV":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=e("mvHQ"),o=e.n(s),n=e("p8P9"),i=e("X2sI"),r=e("LuSo"),c=e("wdqF"),l=e("8sRp"),u=e("KxKW"),p=e("JtJ0"),h=e("HYs4"),d=e("NHTL"),v=e("yoLw"),f=e("EMb8"),m=e("NAz/"),_=e("a84E"),g=e("7+uW"),D=e("wDsp"),y=e("TIfe"),k=(e("q29v"),e("eJLk"));g.a.component("Row",_.a),g.a.component("Col",m.a),g.a.component("Icon",f.a),g.a.component("Table",v.a),g.a.component("Tag",d.a),g.a.component("Page",h.a),g.a.component("Input",p.a),g.a.component("Button",u.a),g.a.component("Modal",l.a),g.a.component("Spin",c.a),g.a.component("Select",r.a),g.a.component("Option",i.a),g.a.component("AutoComplete",n.a);var b={name:"check_manage",components:{},props:{},data:function(){return{statusColor:k.a,user:this.$store.state.user,totalCount:0,pageSize:10,currentPage:1,pageSizeOptions:[10,20,30,40,50,60,70,80,90,100],tableLoading:!0,uploadModal:!1,achieveUpload:!1,errorUpload:!1,selection:[],deviceDetailData:[],inputData:[],inputDataAll:[],queryData:{assetName:"",assetID:"",memorydepart:"",memorylocDept:"",mac:""},assetsDetailColumn:[{title:"选择",slot:"selection",type:"selection",width:80},{title:"资产编号",key:"assetID"},{title:"通用名称",key:"generalName"},{title:"资产名称",key:"assetName"},{title:"规格",key:"specification"},{title:"型号",key:"type"},{title:"存放地点",key:"location"},{title:"品牌",key:"brand"},{title:"标签mac",key:"mac"},{title:"部门名称",key:"departmentroom"},{title:"所在科室名称",key:"locDept"},{title:"SN",key:"sn"},{title:"状态",key:"status",slot:"status",width:120}],assetsConfigArr:["electric","generalName","assetName","location","specification","type","placeoforigin","brand","departmentcode","departmentroom","homeofficenumber","homeofficename","isentrance","suppliername","generatebusinessname","money","buyDate","locDept","status","recorder","mac","amount","unit","applyoddnumbers","oneclassify","secondclassify","threeclassify","fourclassify","repairPeople","repairDate","repairReason","scrapPeople","scrapDate","scrapReason","remark","sn"],assetsDetailData:[]}},watch:{deviceDetailData:function(){var t=this;this.deviceDetailData.forEach(function(a,e){t.assetsDetailData.forEach(function(t,e){a.Mac===t.mac&&(t.location=a.Location)})})}},computed:{},methods:{handleSearchFn:function(t){this.inputData=this.inputDataAll.filter(function(a){return a.toLowerCase().indexOf(t.toLowerCase())>-1})},inputBlurFn:function(){this.inputDataAll=[],this.inputData=[]},inputFocusFn:function(t){var a=this;this.inputDataAll=[],this.inputData=[];var e=[],s={};s[t]="1",e.push(s),this.$axios.post(D.a.inputQuery,e).then(function(e){e.data.data.forEach(function(e,s){a.inputDataAll.find(function(a){return a===e[t]})||a.inputDataAll.push(e[t])}),setTimeout(function(){a.inputData=JSON.parse(o()(a.inputDataAll))},300)}).catch(function(t){console.log(t)})},searchAssetsFn:function(){this.currentPage=1,this.getDataFn()},clearAssetsFn:function(){for(var t in this.queryData)this.queryData[t]=""},changePageFn:function(t){this.tableLoading=!0,this.currentPage=t,this.getDataFn()},changePageSizeFn:function(t){this.pageSize=t,this.currentPage=1,this.getDataFn()},checkAllGroupChange:function(t){this.selection=t},checkFn:function(){var t=this;if(!this.selection.length)return this.$Message.error({content:"请选择至少一项!",duration:2});this.selection.length>1?this.$Modal.confirm({title:"盘点提示！",okText:"确定",cancelText:"取消",content:'<h2 style="color:#f94040">你确定要同时盘点这'+this.selection.length+"个资产吗？</h2>",onOk:function(){t.checkCertainFn()}}):this.checkCertainFn()},checkCertainFn:function(){var t=this,a=[];this.selection.forEach(function(t,e){var s={assetID:t.assetID};a.push(s)}),this.uploadModal=!0,this.$axios.post(D.a.assetsCheck+"checkpep="+this.user,a).then(function(a){"success"===a.data.msg?t.successFn():t.errorFn()}).catch(function(t){l.a.error({title:"错误提示！",content:t,okText:"确定"})})},successFn:function(){var t=this;this.achieveUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1,t.getDataFn()},500)},1e3)},errorFn:function(){var t=this;this.errorUpload=!0,setTimeout(function(){t.uploadModal=!1,setTimeout(function(){t.achieveUpload=!1,t.errorUpload=!1},500)},1e3)},getDataFn:function(){var t=this;this.tableLoading=!0,this.$Loading.start();var a="";for(var e in this.queryData)this.queryData[e]||(this.queryData[e]=""),a+=e+"="+this.queryData[e].trim()+"&";a=a.substring(0,a.length-1),this.$axios.post(D.a.assetsQuery+"?currentPage="+this.currentPage+"&pageSize="+this.pageSize+"&"+a).then(function(a){var e=a.data,s=Number(e.pop().total);t.assetsDetailData=[];for(var o=function(a){var s=[];for(var o in e[a])""!==e[a][o]&&"null"!==e[a][o]||(e[a][o]="--"),s.push(o);t.assetsConfigArr.forEach(function(t,o){s.find(function(a,e){return a===t})||(e[a][t]="--")})},n=0;n<e.length;n++)o(n);t.assetsDetailData=e,t.totalCount=s,t.tableLoading=!1,t.$Loading.finish()}).catch(function(a){t.$Loading.error()})},transWebsocketFn:function(t){var a=[],e=t.split("{");return e.splice(0,1),e.forEach(function(t,e){var s=t.split("}");s.splice(s.length-1,1);var o={};s[0].split(",").forEach(function(t,a){var e=t.split("=");o[e[0].trim()]=e[1]}),a.push(o)}),a},initWebSocket:function(){this.websock=new WebSocket(D.a.mapSocket+"/"+Object(y.a)()),this.websock.onopen=this.websocketonopen,this.websock.onerror=this.websocketonerror,this.websock.onmessage=this.websocketonmessage,this.websock.onclose=this.websocketclose},websocketonopen:function(){},websocketonerror:function(t){},websocketonmessage:function(t){var a=this.transWebsocketFn(t.data);this.deviceDetailData=a},websocketclose:function(t){}},created:function(){},mounted:function(){this.getDataFn(),this.initWebSocket()},beforeDestroy:function(){this.websock.close()}},w={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"check_manage_wrap"},[e("div",{staticClass:"hover_animat custom_bg_color_white"},[e("div",{staticClass:"row_box"},[e("h2",{staticClass:"row_title"},[t._v("按条件筛选")]),t._v(" "),e("div",{staticClass:"form_wrap"},[e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[t._v("资产编号")]),t._v(" "),e("Input",{attrs:{placeholder:"资产编号"},model:{value:t.queryData.assetID,callback:function(a){t.$set(t.queryData,"assetID",a)},expression:"queryData.assetID"}})],1),t._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[t._v("资产名称")]),t._v(" "),e("Input",{attrs:{placeholder:"资产名称"},model:{value:t.queryData.assetName,callback:function(a){t.$set(t.queryData,"assetName",a)},expression:"queryData.assetName"}})],1),t._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[t._v("部门名称")]),t._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"部门名称"},on:{"on-focus":function(a){return t.inputFocusFn("departmentroom")},"on-blur":t.inputBlurFn,"on-search":t.handleSearchFn},model:{value:t.queryData.memorydepart,callback:function(a){t.$set(t.queryData,"memorydepart",a)},expression:"queryData.memorydepart"}},t._l(t.inputData,function(a){return e("Option",{key:a,attrs:{value:a}},[t._v(t._s(a))])}),1)],1),t._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[t._v("所在科室名称")]),t._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"所在科室名称"},on:{"on-focus":function(a){return t.inputFocusFn("locDept")},"on-blur":t.inputBlurFn,"on-search":t.handleSearchFn},model:{value:t.queryData.memorylocDept,callback:function(a){t.$set(t.queryData,"memorylocDept",a)},expression:"queryData.memorylocDept"}},t._l(t.inputData,function(a){return e("Option",{key:a,attrs:{value:a}},[t._v(t._s(a))])}),1)],1)],1)],1),t._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[t._v("标签mac")]),t._v(" "),e("Input",{attrs:{placeholder:"标签mac"},model:{value:t.queryData.mac,callback:function(a){t.$set(t.queryData,"mac",a)},expression:"queryData.mac"}})],1)],1)],1)]),t._v(" "),e("div",{staticClass:"submit_btn_wrap"},[e("Button",{attrs:{type:"primary",shape:"circle"},on:{click:t.searchAssetsFn}},[t._v("搜索")]),t._v(" "),e("Button",{attrs:{type:"error",shape:"circle"},on:{click:t.clearAssetsFn}},[t._v("清空")])],1)])]),t._v(" "),e("div",{staticClass:"row_wrap"},[e("div",{staticClass:"row_box hover_animat custom_bg_color_white"},[e("h2",{staticClass:"row_title"},[t._v("盘点明细表")]),t._v(" "),e("div",{staticClass:"data_table_top clearfix"},[e("div",{staticClass:"data_table_top_left"},[e("Button",{attrs:{type:"primary",shape:"circle",icon:"md-contrast"},on:{click:t.checkFn}},[t._v("立即盘点")]),t._v(" "),e("Button",{attrs:{type:"warning",shape:"circle",icon:"ios-paper",to:"/web/check_history"}},[t._v("盘点历史")])],1),t._v(" "),e("div",{staticClass:"data_table_top_right"})]),t._v(" "),e("div",{staticClass:"table_scroll_wrap"},[e("Table",{staticStyle:{"min-width":"1500px"},attrs:{stripe:"",columns:t.assetsDetailColumn,data:t.assetsDetailData,size:"large",loading:t.tableLoading},on:{"on-selection-change":t.checkAllGroupChange},scopedSlots:t._u([{key:"check",fn:function(a){var s=a.row;return[e("span",{staticStyle:{color:"#2d8cf0"}},[t._v(t._s(s.check?s.check:"--"))])]}},{key:"status",fn:function(a){var s=a.row;return["正常"===s.status?e("Tag",{attrs:{color:t.statusColor[0]}},[e("span",[t._v(t._s(s.status))])]):"待维修"===s.status?e("Tag",{attrs:{color:t.statusColor[1]}},[e("span",[t._v(t._s(s.status))])]):"维修接单"===s.status?e("Tag",{attrs:{color:t.statusColor[2]}},[e("span",[t._v(t._s(s.status))])]):"待报废"===s.status?e("Tag",{attrs:{color:t.statusColor[3]}},[e("span",[t._v(t._s(s.status))])]):"报废"===s.status?e("Tag",{attrs:{color:t.statusColor[4]}},[e("span",[t._v(t._s(s.status))])]):"外借"===s.status?e("Tag",{attrs:{color:t.statusColor[5]}},[e("span",[t._v(t._s(s.status))])]):e("Tag",{attrs:{color:"default"}},[e("span",[t._v(t._s(s.status))])])]}}])})],1),t._v(" "),e("div",{staticClass:"data_table_pagination_wrap"},[e("Page",{attrs:{"show-elevator":"","show-total":"","show-sizer":"",total:t.totalCount,"page-size-opts":t.pageSizeOptions,current:t.currentPage,"page-size":t.pageSize},on:{"on-change":t.changePageFn,"on-page-size-change":t.changePageSizeFn}})],1)])]),t._v(" "),e("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:t.uploadModal,callback:function(a){t.uploadModal=a},expression:"uploadModal"}},[e("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[t.achieveUpload||t.errorUpload?t._e():e("Spin",{attrs:{fix:""}},[e("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),t._v(" "),e("div",[t._v("正在盘点")])],1),t._v(" "),t.achieveUpload?e("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),t._v(" "),e("div",[t._v("盘点成功")])],1):t._e(),t._v(" "),t.errorUpload?e("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),t._v(" "),e("div",[t._v("盘点失败")])],1):t._e()],1)])],1)},staticRenderFns:[]};var C=e("VU/8")(b,w,!1,function(t){e("rUP9")},"data-v-1d4cb4dc",null);a.default=C.exports},rUP9:function(t,a){}});
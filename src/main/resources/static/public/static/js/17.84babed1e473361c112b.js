webpackJsonp([17],{KtcL:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("mvHQ"),o=a.n(n),i=a("wdqF"),s=a("8sRp"),c=a("KxKW"),r=a("JtJ0"),l=a("HYs4"),u=a("X2sI"),d=a("LuSo"),h=a("9N2q"),p=a("NHTL"),g=a("yoLw"),y=a("NAz/"),m=a("a84E"),w=a("EMb8"),f=a("7+uW"),v=a("wDsp");f.a.component("Icon",w.a),f.a.component("Row",m.a),f.a.component("Col",y.a),f.a.component("Table",g.a),f.a.component("Tag",p.a),f.a.component("DatePicker",h.a),f.a.component("Select",d.a),f.a.component("Option",u.a),f.a.component("Page",l.a),f.a.component("Input",r.a),f.a.component("Button",c.a),f.a.component("Modal",s.a),f.a.component("Spin",i.a);var _={name:"gateway_manage",components:{},props:{},data:function(){return{queryData:{gatewaymac:""},tableLoading:!0,gatewayDetailColumn:[{title:"选择",slot:"selection",type:"selection",width:100},{title:"网关mac地址",key:"gatewaymac",slot:"gatewaymac"},{title:"坐落位置",key:"location"},{title:"楼层",key:"floor"},{title:"状态",key:"status",slot:"status",width:100},{title:"更新时间",key:"updatetime"},{title:"操作",key:"operation",slot:"operation"}],gatewayDetailData:[],gatewayDetailDataNow:[],selection:[],deleteModal:!1,achieveDelete:!1,errorDelete:!1,totalCount:0,currentPage:1,pageSize:10}},watch:{},computed:{},methods:{searchGatewayFn:function(){var t=this;this.gatewayDetailDataNow=[],this.queryData.gatewaymac.trim()?(this.gatewayDetailDataNow=[],this.gatewayDetailData.forEach(function(e,a){e.gatewaymac===t.queryData.gatewaymac.trim()&&t.gatewayDetailDataNow.push(e)})):this.getDataFn()},clearGatewayFn:function(){for(var t in this.queryData)this.queryData[t]=""},checkAllGroupChange:function(t){this.selection=t},restartGatewayFn:function(t){var e=this;if(!this.selection.length)return this.$Message.error({content:"请选择至少一项!",duration:2});this.$Modal.confirm({title:"重启提示",okText:"确定",cancelText:"取消",content:'<h2 style="color:#f94040">你确定要重启这'+this.selection.length+"个网关吗？</h2>",onOk:function(){for(var t="",a=0;a<e.selection.length;a++)t+="gatewaymac="+e.selection[a].gatewaymac+"&";e.$axios.post(v.a.gatewayRestart+t+"address="+e.$store.state.address).then(function(t){"ok"===t.data.msg?e.$Message.success({content:"成功发送重启请求！",duration:1}):e.$Message.error({content:"发送重启请求失败！",duration:1})}).catch(function(t){e.$Message.error({content:"发送重启请求失败！",duration:1})})}})},restartAllGatewayFn:function(){var t=this;this.$Modal.confirm({title:"重启提示",okText:"确定",cancelText:"取消",content:'<h2 style="color:#f94040">你确定要重启所有的网关吗？</h2>',onOk:function(){t.$axios.post(v.a.gatewayRestartAll+"address="+t.$store.state.address).then(function(e){console.log(e),"ok"===e.data.msg?t.$Message.success({content:"成功发送重启请求！",duration:1}):t.$Message.error({content:"发送重启请求失败！",duration:1})}).catch(function(e){t.$Message.error({content:"发送重启请求失败！",duration:1})})}})},restartThisGatewayFn:function(t){var e=this;this.$Modal.confirm({title:"重启提示",okText:"确定",cancelText:"取消",content:'<h2 style="color:#f94040">你确定要重启 '+t+" 这个网关吗？</h2>",onOk:function(){e.$axios.post(v.a.gatewayRestart+"gatewaymac="+t+"&address="+e.$store.state.address).then(function(t){"ok"===t.data.msg?e.$Message.success({content:"成功发送重启请求！",duration:1}):e.$Message.error({content:"发送重启请求失败！",duration:1})}).catch(function(t){e.$Message.error({content:"发送重启请求失败！",duration:1})})}})},successFn:function(){var t=this;this.achieveDelete=!0,setTimeout(function(){t.deleteModal=!1,t.currentPage=1,t.getDataFn(),setTimeout(function(){t.achieveDelete=!1,t.errorDelete=!1},500)},1e3)},errorFn:function(){var t=this;this.errorDelete=!0,setTimeout(function(){t.deleteModal=!1,setTimeout(function(){t.achieveDelete=!1,t.errorDelete=!1},500)},1e3)},getDataFn:function(){var t=this;this.tableLoading=!0,this.gatewayDetailData=[],this.gatewayDetailDataNow=[],this.$axios.get(v.a.gatewayQuery+this.$store.state.address).then(function(e){e.data.forEach(function(e,a){if("{}"!==o()(e)&&""!==e.gatewaymac){var n={gatewaymac:e.gatewaymac,floor:e.floor?e.floor:"",ipaddress:e.ipaddress?e.ipaddress:"",mapx:e.mapx?e.mapx:"",mapy:e.mapy?e.mapy:"",department:e.department?e.department:"",location:e.location?e.location:"",status:"online"===e.online&&e.updatetime?"在线":"离线",updatetime:e.updatetime?e.updatetime:""};t.gatewayDetailData.push(n)}}),t.gatewayDetailDataNow=t.gatewayDetailData,t.totalCount=t.gatewayDetailDataNow.length,t.tableLoading=!1}).catch(function(t){})},changePageFn:function(t){this.tableLoading=!0,this.currentPage=t,this.getDataFn()},updateGatewayFn:function(t){console.log(t)},updateAllGatewayFn:function(){this.$Modal.confirm({title:"升级提示",okText:"确定",cancelText:"取消",content:'<h2 style="color:#f94040">你确定要升级所有的网关吗？</h2>',onOk:function(){}})},settingGatewayFn:function(){this.$router.push({path:"/web/gateway_setting"})},toCreateFn:function(){this.$router.push({path:"/web/gateway_create"})},toDetailFn:function(t){this.gatewayDetailData.forEach(function(e,a){if(e.gatewaymac===t){var n={gatewaymac:e.gatewaymac,floor:e.floor,ipaddress:e.ipaddress,mapx:e.mapx,mapy:e.mapy,location:e.location?e.location:"",department:e.department?e.department:""};window.sessionStorage.setItem("detailData",o()(n))}}),this.$router.push({path:"/web/gateway_detail"})},deleteFn:function(){var t=this;if(!this.selection.length)return this.$Message.error({content:"请选择至少一项!",duration:2});this.$Modal.confirm({title:"删除提示",okText:"确定删除",cancelText:"取消",content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+"个网关吗？</h2>",onOk:function(){t.deleteCertainFn()}})},deleteCertainFn:function(){this.deleteModal=!0;var t=[];this.selection.forEach(function(e,a){var n={gatewaymac:e.gatewaymac};t.push(n)}),this.deletePortFn(t)},deleteThisGatewayFn:function(t){var e=this;this.$Modal.confirm({title:"删除提示",okText:"确定删除",cancelText:"取消",content:'<h2 style="color:#f94040">你确定要删除 '+t+" 这个网关吗？</h2>",onOk:function(){e.deleteModal=!0;var a=[{gatewaymac:t}];e.deletePortFn(a)}})},deletePortFn:function(t){var e=this;this.$axios.post(v.a.gatewayDelete,t).then(function(t){"success"===t.data.msg?e.successFn():(e.errorFn(),e.$Message.error({content:t.data.msg,duration:2}))}).catch(function(t){e.errorFn()})}},created:function(){},mounted:function(){this.getDataFn()}},D={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"gateway_manage_wrap"},[a("div",{staticClass:"hover_animat custom_bg_color_white"},[a("div",{staticClass:"row_box"},[a("h2",{staticClass:"row_title"},[t._v("按条件筛选")]),t._v(" "),a("div",{staticClass:"form_wrap"},[a("div",{staticClass:"form_item"},[a("Row",{attrs:{gutter:20}},[a("Col",{attrs:{span:"24"}},[a("h3",[t._v("网关mac地址")]),t._v(" "),a("Input",{attrs:{placeholder:"网关mac地址"},model:{value:t.queryData.gatewaymac,callback:function(e){t.$set(t.queryData,"gatewaymac",e)},expression:"queryData.gatewaymac"}})],1)],1)],1)]),t._v(" "),a("div",{staticClass:"submit_btn_wrap"},[a("Button",{attrs:{type:"primary",shape:"circle"},on:{click:t.searchGatewayFn}},[t._v("搜索")]),t._v(" "),a("Button",{attrs:{type:"error",shape:"circle"},on:{click:t.clearGatewayFn}},[t._v("清空")])],1)])]),t._v(" "),a("div",{staticClass:"row_wrap hover_animat custom_bg_color_white"},[a("div",{staticClass:"row_box"},[a("h2",{staticClass:"row_title"},[t._v("网关明细表")]),t._v(" "),a("div",{staticClass:"data_table_top clearfix"},[a("div",{staticClass:"data_table_top_left"},[a("Button",{attrs:{type:"primary",shape:"circle",icon:"md-add"},on:{click:t.toCreateFn}},[t._v("新增网关")]),t._v(" "),a("Button",{attrs:{type:"error",shape:"circle",icon:"md-trash"},on:{click:t.deleteFn}},[t._v("批量删除网关")]),t._v(" "),a("Button",{attrs:{type:"warning",shape:"circle",icon:"md-power"},on:{click:t.restartGatewayFn}},[t._v("批量重启网关")]),t._v(" "),a("Button",{attrs:{type:"primary",shape:"circle",icon:"md-refresh-circle"},on:{click:t.restartAllGatewayFn}},[t._v("重启所有网关")]),t._v(" "),a("Button",{attrs:{type:"warning",shape:"circle",icon:"md-cog"},on:{click:t.settingGatewayFn}},[t._v("配置网关")])],1),t._v(" "),t._m(0)]),t._v(" "),a("div",{staticClass:"table_scroll_wrap"},[a("Table",{staticStyle:{"min-width":"1000px"},attrs:{stripe:"",columns:t.gatewayDetailColumn,data:t.gatewayDetailDataNow,size:"large",loading:t.tableLoading},on:{"on-selection-change":t.checkAllGroupChange},scopedSlots:t._u([{key:"gatewaymac",fn:function(e){var n=e.row;return[a("a",{on:{click:function(e){return t.toDetailFn(n.gatewaymac)}}},[t._v(t._s(n.gatewaymac))])]}},{key:"operation",fn:function(e){var n=e.row;return[a("Button",{attrs:{type:"default",shape:"circle",icon:"md-power"},on:{click:function(e){return t.restartThisGatewayFn(n.gatewaymac)}}},[t._v("重启")]),t._v(" "),a("Button",{attrs:{type:"default",shape:"circle",icon:"md-trash"},on:{click:function(e){return t.deleteThisGatewayFn(n.gatewaymac)}}},[t._v("删除")])]}},{key:"status",fn:function(e){var n=e.row;return["在线"===n.status?a("Tag",{attrs:{color:"success"}},[t._v("在线")]):"离线"===n.status?a("Tag",{attrs:{color:"error"}},[t._v("离线")]):a("span",[t._v(t._s(n.status))])]}}])})],1)])]),t._v(" "),a("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:t.deleteModal,callback:function(e){t.deleteModal=e},expression:"deleteModal"}},[a("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[t.achieveDelete||t.errorDelete?t._e():a("Spin",{attrs:{fix:""}},[a("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),t._v(" "),a("div",[t._v("正在删除")])],1),t._v(" "),t.achieveDelete?a("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[a("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),t._v(" "),a("div",[t._v("删除成功")])],1):t._e(),t._v(" "),t.errorDelete?a("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[a("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),t._v(" "),a("div",[t._v("删除失败")])],1):t._e()],1)])],1)},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"data_table_top_right"},[e("div",{staticClass:"pagination_wrap"})])}]};var k=a("VU/8")(_,D,!1,function(t){a("cSdn")},"data-v-90887d56",null);e.default=k.exports},cSdn:function(t,e){}});
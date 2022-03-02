webpackJsonp([29],{EcwQ:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("wdqF"),o=a("8sRp"),i=a("KxKW"),r=a("HYs4"),c=a("NHTL"),s=a("yoLw"),l=a("EMb8"),d=a("7+uW"),u=a("xWT1"),h=a("wDsp");d.a.component("Icon",l.a),d.a.component("Table",s.a),d.a.component("Tag",c.a),d.a.component("Page",r.a),d.a.component("Button",i.a),d.a.component("Modal",o.a),d.a.component("Spin",n.a);var p={name:"account_manage",components:{},props:{},data:function(){return{tableLoading:!0,totalCount:0,pageSize:10,currentPage:1,selection:[],deleteModal:!1,achieveDelete:!1,errorDelete:!1,accountData:[],accountColumn:[{key:"account",title:"选择",type:"selection",width:100},{key:"username",title:"账户名",slot:"username"},{key:"phone",title:"手机号"},{key:"departmentroom",title:"授权部门",slot:"departmentroom",width:150},{key:"role",title:"账户级别",slot:"role",width:100},{key:"operation",title:"操作",slot:"operation",width:150}]}},watch:{},computed:{},methods:{checkAllGroupChange:function(t){this.selection=t},changePageFn:function(t){this.tableLoading=!0,this.currentPage=t},deleteFn:function(){var t=this;this.selection.length>0?this.$Modal.confirm({title:"删除提示",okText:"确定删除",cancelText:"取消",content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+"个账户吗？</h2>",onOk:function(){t.deleteCertainFn()}}):this.$Message.error({content:"请至少选择一项!",duration:1})},successFn:function(){var t=this;this.achieveDelete=!0,setTimeout(function(){t.deleteModal=!1,t.currentPage=1,t.getDataFn(),setTimeout(function(){t.achieveDelete=!1,t.errorDelete=!1},500)},1e3)},errorFn:function(){var t=this;this.errorDelete=!0,setTimeout(function(){t.deleteModal=!1,setTimeout(function(){t.achieveDelete=!1,t.errorDelete=!1},500)},1e3)},deleteMySelfFn:function(){var t=this;this.achieveDelete=!0,setTimeout(function(){t.deleteModal=!1,t.currentPage=1,setTimeout(function(){t.achieveDelete=!1,t.errorDelete=!1,t.$Modal.warning({title:"提示！",content:"当前登录用户已删除，需要重新登录！",okText:"立即登录",onOk:function(){Object(u.a)()}})},500)},1e3)},deleteCertainFn:function(){var t=this;this.deleteModal=!0;for(var e=[],a=0;a<this.selection.length;a++)e.push(this.selection[a].username);this.$axios.post(h.a.accountDelete+e).then(function(e){"ok"===e.data.msg?t.successFn():"remove_myself"===e.data.msg?t.deleteMySelfFn():"failed"===e.data.msg&&t.errorFn()}).catch(function(e){t.errorFn()})},getDataFn:function(){var t=this;this.$Loading.start(),this.$axios.get(h.a.accountQuery).then(function(e){t.accountData=e.data,t.totalCount=e.data.length,t.tableLoading=!1,t.$Loading.finish()}).catch(function(e){t.$Loading.error()})},toEditFn:function(t){this.$router.push("/web/account_edit/"+t)}},created:function(){},mounted:function(){this.getDataFn()}},v={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"account_manage_wrap"},[a("div",{staticClass:"hover_animat custom_bg_color_white"},[a("div",{staticClass:"row_box"},[a("h2",{staticClass:"row_title"},[t._v("账户明细表")]),t._v(" "),a("div",{staticClass:"data_table_top clearfix"},[a("div",{staticClass:"data_table_top_left"},[a("Button",{attrs:{to:"/web/account_create",type:"primary",shape:"circle",icon:"md-add"}},[t._v("新增账户")]),t._v(" "),a("Button",{attrs:{type:"error",shape:"circle",icon:"md-trash"},on:{click:t.deleteFn}},[t._v("删除账户")])],1),t._v(" "),a("div",{staticClass:"data_table_top_right"})]),t._v(" "),a("div",{staticClass:"table_scroll_wrap"},[a("Table",{staticStyle:{"min-width":"1000px"},attrs:{stripe:"",columns:t.accountColumn,data:t.accountData,size:"large",loading:t.tableLoading},on:{"on-selection-change":t.checkAllGroupChange},scopedSlots:t._u([{key:"username",fn:function(e){var a=e.row;return[t._v("\n                    "+t._s(a.username)+"\n                  ")]}},{key:"departmentroom",fn:function(e){var n=e.row;return[a("Dropdown",{staticStyle:{"margin-right":"20px"},attrs:{transfer:"",trigger:"hover"}},[a("Button",{attrs:{shape:"circle",type:"primary"}},[t._v("\n                              查看\n                              "),a("Icon",{attrs:{type:"ios-arrow-down"}})],1),t._v(" "),a("div",{attrs:{slot:"list"},slot:"list"},[a("div",{staticStyle:{width:"250px",padding:"20px"}},t._l(n.departmentroom.split(","),function(e,n){return a("Tag",{key:n,attrs:{color:"default"}},[t._v(t._s(e))])}),1)])],1)]}},{key:"role",fn:function(e){return["admin"===e.row.role?a("Tag",{attrs:{color:"success"}},[t._v("管理员")]):a("Tag",{attrs:{color:"warning"}},[t._v("操作员")])]}},{key:"operation",fn:function(e){var n=e.row;return[a("Button",{attrs:{shape:"circle",type:"default",icon:"md-create"},on:{click:function(e){return t.toEditFn(n.username)}}},[t._v("编辑")])]}}])})],1),t._v(" "),a("div",{staticClass:"data_table_pagination_wrap"})])]),t._v(" "),a("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:t.deleteModal,callback:function(e){t.deleteModal=e},expression:"deleteModal"}},[a("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[t.achieveDelete||t.errorDelete?t._e():a("Spin",{attrs:{fix:""}},[a("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),t._v(" "),a("div",[t._v("正在删除")])],1),t._v(" "),t.achieveDelete?a("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[a("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),t._v(" "),a("div",[t._v("删除成功")])],1):t._e(),t._v(" "),t.errorDelete?a("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[a("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),t._v(" "),a("div",[t._v("删除失败")])],1):t._e()],1)])],1)},staticRenderFns:[]};var _=a("VU/8")(p,v,!1,function(t){a("t5cX")},"data-v-6d4841cc",null);e.default=_.exports},t5cX:function(t,e){}});
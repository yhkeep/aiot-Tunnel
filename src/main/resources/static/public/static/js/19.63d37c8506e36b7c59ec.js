webpackJsonp([19],{ZBIn:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=a("Fd2+"),i=(a("cnGM"),a("FDxC"),a("4T1P"),a("2PSJ"),a("i9vB"),a("MHRi"),a("GKy3"),a("wDsp")),o=a("eJLk"),n={name:"mobile_assets_detail",components:{Button:s.b,Cell:s.c,Tag:s.y,ImagePreview:s.n,Dialog:s.k,Toast:s.z},props:{},data:function(){return{role:this.$store.state.role,statusColor:o.a,assetID:"",showRepair:!1,showScrap:!1,imgList:[],assetsDetailData:{assetID:"",generalName:"",assetName:"",location:"",specification:"",type:"",placeoforigin:"",brand:"",departmentcode:"",departmentroom:"",homeofficenumber:"",homeofficename:"",isentrance:"",suppliername:"",generatebusinessname:"",money:null,buyDate:"",locDept:"",status:"",recorder:"",mac:"",amount:null,unit:"",applyoddnumbers:"",oneclassify:"",secondclassify:"",threeclassify:"",fourclassify:"",sn:"",architecture:"",academy:"",remark:""},assetsDetailList:[{label:"资产编号",key:"assetID"},{label:"资产名称",key:"assetName"},{label:"通用名称",key:"generalName"},{label:"存放地点",key:"location"},{label:"规格",key:"specification"},{label:"型号",key:"type"},{label:"产地",key:"placeoforigin"},{label:"品牌",key:"brand"},{label:"部门编码",key:"departmentcode"},{label:"部门名称",key:"departmentroom"},{label:"大科室编码",key:"homeofficenumber"},{label:"大科室名称",key:"homeofficename"},{label:"所在科室名称",key:"locDept"},{label:"供应商名称",key:"suppliername"},{label:"生产厂商名称",key:"generatebusinessname"},{label:"是否进口",key:"isentrance"},{label:"原值",key:"money"},{label:"入库时间",key:"buyDate"},{label:"状态",key:"status"},{label:"录入人员",key:"recorder"},{label:"一级分类",key:"oneclassify"},{label:"二级分类",key:"secondclassify"},{label:"三级分类",key:"threeclassify"},{label:"四级分类",key:"fourclassify"},{label:"标签mac",key:"mac"},{label:"数量",key:"amount"},{label:"单位",key:"unit"},{label:"电量",key:"electric"},{label:"处置申请单号",key:"applyoddnumbers"},{label:"SN",key:"sn"},{label:"建筑",key:"architecture"},{label:"院区",key:"academy"},{label:"备注",key:"remark"}]}},watch:{},computed:{},methods:{imgPreviewFn:function(e){Object(s.n)({images:this.imgList,startPosition:e})},imgCom:function(e){return"background-image:url("+e+")"},toModifyFn:function(){this.$router.push("/mobile/assets_modify/"+this.assetID)},toDeleteFn:function(){var e=this;s.k.confirm({title:"提示！",message:'<span style="color:red;">你确定要删除此资产吗？</span>',confirmButtonColor:"red"}).then(function(){var t=s.z.loading({message:"正在删除...",forbidClick:!0,duration:0}),a=[];a.push(e.assetID),e.$axios.get(i.a.assetsDelete+a).then(function(a){"ok"===a.data.msg?(t.clear(),s.z.success("删除成功！"),setTimeout(function(){e.$router.go(-1)},1e3)):"failed"===a.data.msg&&(t.clear(),s.z.fail("删除失败！"))}).catch(function(e){t.clear(),s.z.fail("删除失败！")})}).catch(function(){})},getDataFn:function(){var e=this;this.$axios.post(i.a.assetsQuery+"?currentPage=1&assetID="+this.assetID).then(function(t){if(t.data[0].assetID){var a=t.data[0],i={assetID:a.assetID,generalName:a.generalName?a.generalName:"",assetName:a.assetName?a.assetName:"",location:a.location?a.location:"",specification:a.specification?a.specification:"",type:a.type?a.type:"",placeoforigin:a.placeoforigin?a.placeoforigin:"",brand:a.brand?a.brand:"",departmentcode:a.departmentcode?a.departmentcode:"",departmentroom:a.departmentroom?a.departmentroom:"",homeofficenumber:a.homeofficenumber?a.homeofficenumber:"",homeofficename:a.homeofficename?a.homeofficename:"",isentrance:a.isentrance?a.isentrance:"",suppliername:a.suppliername?a.suppliername:"",generatebusinessname:a.generatebusinessname?a.generatebusinessname:"",money:a.money?Number(a.money):0,buyDate:a.buyDate?a.buyDate:"",locDept:a.locDept?a.locDept:"",status:a.status?a.status:"",recorder:a.recorder?a.recorder:e.$store.state.user,mac:a.mac?a.mac:"",amount:a.amount?Number(a.amount):1,unit:a.unit?a.unit:"",applyoddnumbers:a.applyoddnumbers?a.applyoddnumbers:"",oneclassify:a.oneclassify?a.oneclassify:"",secondclassify:a.secondclassify?a.secondclassify:"",threeclassify:a.threeclassify?a.threeclassify:"",fourclassify:a.fourclassify?a.fourclassify:"",sn:a.sn?a.sn:"",architecture:a.architecture?a.architecture:"",academy:a.academy?a.academy:"",remark:a.remark?a.remark:""};e.buyTime=i.buyDate,e.assetsDetailData=i,e.getImgFn("imgShow"),e.getImgFn("imgShowLeft"),e.getImgFn("imgShowAllround"),e.getImgFn("imgShowAbove"),e.getImgFn("imgShowRight"),e.getImgFn("imgShowPaperlabel"),e.getImgFn("imgShowOnecodelable")}else Object(s.q)({type:"danger",message:"未找到资产数据！"}),setTimeout(function(){e.$router.go(-1)},1e3)}).catch(function(e){})},getImgFn:function(e){var t=this;this.$axios.get(i.a[e]+this.assetID,{responseType:"arraybuffer"}).then(function(e){var a;"data:image/png;base64,"===(a="data:image/png;base64,"+btoa(new Uint8Array(e.data).reduce(function(e,t){return e+String.fromCharCode(t)},"")))||t.imgList.push(a)}).catch(function(e){})}},beforeRouteEnter:function(e,t,a){e.meta.needFresh=!1,a()},beforeRouteLeave:function(e,t,a){t.meta.needFresh=!1,a()},created:function(){this.$emit("setTitle","资产明细"),this.assetID=this.$route.params.id},mounted:function(){this.getDataFn()}},l={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"mobile_assets_detail_wrap"},[a("div",{staticClass:"assets_detail_box"},[a("div",{staticClass:"assets_img_wrap"},[e.imgList.length?a("div",{staticClass:"assets_img_box"},e._l(e.imgList,function(t,s){return a("div",{key:s,staticClass:"img_item_box",style:e.imgCom(t),on:{click:function(t){return e.imgPreviewFn(s)}}})}),0):a("div",{staticClass:"assets_img_info"},[a("span",[e._v("暂无图片")])])]),e._v(" "),e._l(e.assetsDetailList,function(t,s){return["status"===t.key?a("Cell",{attrs:{title:"状态"}},["正常"===e.assetsDetailData.status?a("Tag",{attrs:{size:"medium",color:e.statusColor[0]}},[e._v(e._s(e.assetsDetailData.status))]):"待维修"===e.assetsDetailData.status?a("Tag",{attrs:{size:"medium",color:e.statusColor[1]}},[e._v(e._s(e.assetsDetailData.status))]):"维修接单"===e.assetsDetailData.status?a("Tag",{attrs:{size:"medium",color:e.statusColor[2]}},[e._v(e._s(e.assetsDetailData.status))]):"待报废"===e.assetsDetailData.status?a("Tag",{attrs:{size:"medium",color:e.statusColor[3]}},[e._v(e._s(e.assetsDetailData.status))]):"报废"===e.assetsDetailData.status?a("Tag",{attrs:{size:"medium",color:e.statusColor[4]}},[e._v(e._s(e.assetsDetailData.status))]):"外借"===e.assetsDetailData.status?a("Tag",{attrs:{size:"medium",color:e.statusColor[5]}},[e._v(e._s(e.assetsDetailData.status))]):a("Tag",{attrs:{size:"medium",color:"#f7f7f7","text-color":"#515a6e"}},[e._v(e._s(e.assetsDetailData.status))])],1):a("Cell",{attrs:{title:t.label,value:e.assetsDetailData[t.key]?e.assetsDetailData[t.key]:"--"}})]}),e._v(" "),a("div",{staticClass:"tools_wrap"},[a("Button",{staticClass:"tools_button",attrs:{round:"",type:"warning"},on:{click:e.toModifyFn}},[e._v("修改")]),e._v(" "),"admin"===e.role?a("Button",{staticClass:"tools_button",attrs:{round:"",type:"danger"},on:{click:e.toDeleteFn}},[e._v("删除")]):e._e()],1)],2)])},staticRenderFns:[]};var r=a("VU/8")(n,l,!1,function(e){a("mbRH")},"data-v-c9fdbd14",null);t.default=r.exports},mbRH:function(e,t){}});
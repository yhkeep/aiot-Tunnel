webpackJsonp([12],{"M+FE":function(a,t){},Qf3W:function(a,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=e("woOf"),r=e.n(s),n=e("mvHQ"),i=e.n(n),o=e("p8P9"),c=e("wdqF"),l=e("8sRp"),u=e("KxKW"),p=e("bzvv"),m=e("JtJ0"),h=e("X2sI"),v=e("LuSo"),d=e("9N2q"),f=e("NAz/"),_=e("a84E"),D=e("EMb8"),b=e("7+uW"),g=e("bkN4"),y=e("kD0S"),k=e("ao6w"),C=e("mtaw"),w=e("Uo+3"),F=e("m6KV"),x=e("kitu"),I=e("wDsp"),S=e("W9Um"),$=e("zcOu");b.a.component("Icon",D.a),b.a.component("Row",_.a),b.a.component("Col",f.a),b.a.component("DatePicker",d.a),b.a.component("Select",v.a),b.a.component("Option",h.a),b.a.component("Input",m.a),b.a.component("InputNumber",p.a),b.a.component("Button",u.a),b.a.component("Modal",l.a),b.a.component("Spin",c.a),b.a.component("AutoComplete",o.a);var L={name:"assets_create",components:{},props:{},data:function(){return{inputData:[],inputDataAll:[],address:this.$store.state.address,showRepair:!1,showScrap:!1,showImg:!1,showImgData:"",buyTime:"",qualityTime:"",repairTime:[],scrapTime:"",imgLimit:7,imgList:[],createData:{assetID:"ZCKP2000000000",generalName:"",assetName:"",location:"",specification:"",type:"",placeoforigin:"",brand:"",departmentcode:"",departmentroom:"",homeofficenumber:"",homeofficename:"",isentrance:"",suppliername:"",generatebusinessname:"",money:0,buyDate:"",locDept:"",status:"",recorder:this.$store.state.user,mac:"",amount:1,unit:"",applyoddnumbers:"",oneclassify:"",secondclassify:"",threeclassify:"",fourclassify:"",sn:"",architecture:"",academy:"",repairPeople:"",repairDate:"",repairReason:"",scrapPeople:"",scrapDate:"",scrapReason:"",remark:""},uploadModal:!1,achieveUpload:!1,errorUpload:!1,departData:g.a,unitData:y.a,statusData:k.a,fstCatData:C.b,secCatData:C.c,thdCatData:C.d,fouCatData:C.a,secCatList:[],thdCatList:[],fouCatList:[],departCode:w.a,homeOfficeNumber:F.a,locDept:x.a,importData:[{value:"是",label:"是"},{value:"否",label:"否"}]}},watch:{},computed:{},methods:{handleSearchFn:function(a){this.inputData=this.inputDataAll.filter(function(t){return t.toLowerCase().indexOf(a.toLowerCase())>-1})},inputBlurFn:function(){this.inputDataAll=[],this.inputData=[]},inputFocusFn:function(a){var t=this;this.inputDataAll=[],this.inputData=[];var e=[],s={};s[a]="1",e.push(s),this.$axios.post(I.a.inputQuery,e).then(function(e){e.data.data.forEach(function(e,s){t.inputDataAll.find(function(t){return t===e[a]})||t.inputDataAll.push(e[a])}),setTimeout(function(){t.inputData=JSON.parse(i()(t.inputDataAll))},300)}).catch(function(a){console.log(a)})},fstCatChangeFn:function(){var a=this;this.secCatList=this.secCatData.filter(function(t,e){return t.parent===a.createData.oneclassify}),this.createData.secondclassify="",this.createData.threeclassify="",this.createData.fourclassify=""},secCatChangeFn:function(){var a=this;this.thdCatList=this.thdCatData.filter(function(t,e){return t.parent===a.createData.secondclassify}),this.createData.threeclassify="",this.createData.fourclassify=""},thdCatChangeFn:function(){var a=this;this.fouCatList=this.fouCatData.filter(function(t,e){return t.parent===a.createData.threeclassify}),this.createData.fourclassify=""},departCodeChangeFn:function(a){var t=this;this.departCode.forEach(function(e,s){e.value===a&&(t.createData.departmentroom=e.name)})},homeOfficeNumberChangeFn:function(a){var t=this;this.homeOfficeNumber.forEach(function(e,s){e.value===a&&(t.createData.homeofficename=e.name)})},addImgFn:function(a){for(var t=this,e=a.target.files,s=[],r=e.length,n=0;n<r;n++)s.push(e[n]);s.forEach(function(a,e){var s=new FileReader;s.readAsDataURL(a),s.onload=function(a){Object(S.a)(a.target.result,600,.9).then(function(a){t.imgList.length<t.imgLimit&&t.imgList.push(a)})}})},showImgFn:function(a){var t=this;this.imgList.forEach(function(e,s){s===a&&(t.showImgData=e,t.showImg=!0)})},hideImgFn:function(){this.showImg=!1,this.showImgData=""},removeImgFn:function(a){this.imgList.splice(a,1)},dateBuyFn:function(a){this.createData.buyDate=a},dateQualityFn:function(a){this.createData.qualityDate=a},dateScrapFn:function(a){this.createData.scrapDate=a},dateRepairFn:function(a){this.createData.repairDate=a[0]+","+a[1]},statusFn:function(a){switch(a){case"正常":this.showRepair=!1,this.showScrap=!1;break;case"在修":this.showScrap=!1,this.showRepair=!0;break;case"已报废":this.showRepair=!1,this.showScrap=!0}},initData:function(){this.createData={assetID:"ZCKP2000000000",generalName:"",assetName:"",location:"",specification:"",type:"",placeoforigin:"",brand:"",departmentcode:"",departmentroom:"",homeofficenumber:"",homeofficename:"",isentrance:"",suppliername:"",generatebusinessname:"",money:0,buyDate:"",locDept:"",status:"",recorder:this.$store.state.user,mac:"",amount:1,unit:"",applyoddnumbers:"",oneclassify:"",secondclassify:"",threeclassify:"",fourclassify:"",sn:"",architecture:"",academy:"",repairPeople:"",repairDate:"",repairReason:"",scrapPeople:"",scrapDate:"",scrapReason:"",remark:""},this.buyTime="",this.repairTime=[],this.scrapTime="",this.imgList=[],this.showImgData=""},successFn:function(){var a=this;this.achieveUpload=!0,setTimeout(function(){a.uploadModal=!1,a.initData(),setTimeout(function(){a.achieveUpload=!1,a.errorUpload=!1},500)},1e3)},errorFn:function(){var a=this;this.errorUpload=!0,setTimeout(function(){a.uploadModal=!1,setTimeout(function(){a.achieveUpload=!1,a.errorUpload=!1},500)},1e3)},createFn:function(){var a=this;if(this.createData.status||(this.createData.status=""),this.createData.assetID.trim()&&this.createData.status.trim()&&this.createData.recorder.trim()){this.uploadModal=!0;var t={assetID:this.createData.assetID.trim(),image:this.imgList[0]?this.imgList[0]:"",leftImage:this.imgList[1]?this.imgList[1]:"",allroundImage:this.imgList[2]?this.imgList[2]:"",aboveImage:this.imgList[3]?this.imgList[3]:"",rightImage:this.imgList[4]?this.imgList[4]:"",paperlabelImage:this.imgList[5]?this.imgList[5]:"",onecodelableImage:this.imgList[6]?this.imgList[6]:""},e={};switch(this.createData.status.trim()){case"正常":break;case"在修":e={repairPeople:this.createData.repairPeople.trim(),repairDate:this.createData.repairDate.trim(),repairReason:this.createData.repairReason.trim()};break;case"已报废":e={scrapPeople:this.createData.scrapPeople.trim(),scrapDate:this.createData.scrapDate.trim(),scrapReason:this.createData.scrapReason.trim()}}var s={assetID:this.createData.assetID.trim(),generalName:this.createData.generalName.trim(),assetName:this.createData.assetName.trim(),location:this.createData.location.trim(),specification:this.createData.specification.trim(),type:this.createData.type.trim(),placeoforigin:this.createData.placeoforigin.trim(),brand:this.createData.brand.trim(),departmentcode:this.createData.departmentcode,departmentroom:this.createData.departmentroom,homeofficenumber:this.createData.homeofficenumber,homeofficename:this.createData.homeofficename,isentrance:this.createData.isentrance.trim(),suppliername:this.createData.suppliername.trim(),generatebusinessname:this.createData.generatebusinessname.trim(),money:Math.abs(this.createData.money),buyDate:this.createData.buyDate,locDept:this.createData.locDept.trim(),status:this.createData.status.trim(),recorder:this.createData.recorder.trim(),mac:Object($.a)(this.createData.mac.trim()),amount:Math.abs(this.createData.amount),unit:this.createData.unit,applyoddnumbers:this.createData.applyoddnumbers.trim(),oneclassify:this.createData.oneclassify,secondclassify:this.createData.secondclassify,threeclassify:this.createData.threeclassify,fourclassify:this.createData.fourclassify,sn:this.createData.sn.trim(),architecture:this.createData.architecture.trim(),academy:this.createData.academy.trim(),remark:this.createData.remark.trim(),address:this.address.trim()},n={};r()(n,s,e);var i=[n];this.$axios.post(I.a.imgUpload,t).then(function(t){"ok"===t.data.msg?a.$axios.post(I.a.assetsCreate,i).then(function(t){"ok"===t.data.msg?a.successFn():t.data.message?(a.errorFn(),l.a.error({title:"错误提示！",content:t.data.message,okText:"确定"})):"failed"===t.data.msg&&(a.errorFn(),l.a.error({title:"错误提示！",content:t.data.message,okText:"确定"}))}).catch(function(a){}):"failed"===t.data.msg&&a.errorFn()}).catch(function(t){a.$Message.error({content:"图片上传失败!",duration:2})})}else this.$Message.error({content:"请补充完整信息!",duration:2})}},created:function(){},mounted:function(){}},A={render:function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("div",{staticClass:"assets_create_wrap"},[e("div",{staticClass:"hover_animat custom_bg_color_white"},[e("div",{staticClass:"row_box"},[e("h2",{staticClass:"row_title"},[a._v("图像")]),a._v(" "),e("div",{staticClass:"clearfix"},[a._l(a.imgList,function(t,s){return e("div",{staticClass:"img_block",attrs:{index:s}},[e("img",{attrs:{src:t,alt:""}}),a._v(" "),e("div",{staticClass:"img_block_tools clearfix"},[e("div",{staticClass:"img_block_tools_left"},[e("Icon",{attrs:{type:"md-qr-scanner"},on:{click:function(t){return a.showImgFn(s)}}})],1),a._v(" "),e("div",{staticClass:"img_block_tools_right"},[e("Icon",{attrs:{type:"md-trash"},on:{click:function(t){return a.removeImgFn(s)}}})],1)])])}),a._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:!(a.imgList.length>=a.imgLimit),expression:"imgList.length>=imgLimit? false : true"}],staticClass:"img_block add_img_wrap"},[e("div",{staticClass:"add_img_box"},[e("Icon",{attrs:{type:"md-add"}}),a._v(" "),e("input",{staticClass:"add_img_input",attrs:{type:"file",accept:"image/*",multiple:"multiple"},on:{change:function(t){return a.addImgFn(t)}}})],1)])],2),a._v(" "),e("p",{staticClass:"img_notice"},[e("Icon",{attrs:{type:"ios-alert"}}),a._v("最多可上传"+a._s(a.imgLimit)+"张图片")],1)])]),a._v(" "),e("div",{staticClass:"row_wrap hover_animat custom_bg_color_white"},[e("div",{staticClass:"row_box"},[e("h2",{staticClass:"row_title"},[a._v("新增")]),a._v(" "),e("div",{staticClass:"form_wrap"},[e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("资产编号"),e("span",{staticClass:"must_fill_info"},[a._v("（必填）")])]),a._v(" "),e("Input",{attrs:{placeholder:"资产编号"},model:{value:a.createData.assetID,callback:function(t){a.$set(a.createData,"assetID",t)},expression:"createData.assetID"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("通用名称")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"通用名称"},on:{"on-focus":function(t){return a.inputFocusFn("generalName")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.generalName,callback:function(t){a.$set(a.createData,"generalName",t)},expression:"createData.generalName"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("资产名称")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"资产名称"},on:{"on-focus":function(t){return a.inputFocusFn("assetName")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.assetName,callback:function(t){a.$set(a.createData,"assetName",t)},expression:"createData.assetName"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("存放地点")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"存放地点"},on:{"on-focus":function(t){return a.inputFocusFn("location")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.location,callback:function(t){a.$set(a.createData,"location",t)},expression:"createData.location"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("规格")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"规格"},on:{"on-focus":function(t){return a.inputFocusFn("specification")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.specification,callback:function(t){a.$set(a.createData,"specification",t)},expression:"createData.specification"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("型号")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"型号"},on:{"on-focus":function(t){return a.inputFocusFn("type")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.type,callback:function(t){a.$set(a.createData,"type",t)},expression:"createData.type"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("产地")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"产地"},on:{"on-focus":function(t){return a.inputFocusFn("placeoforigin")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.placeoforigin,callback:function(t){a.$set(a.createData,"placeoforigin",t)},expression:"createData.placeoforigin"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("品牌")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"品牌"},on:{"on-focus":function(t){return a.inputFocusFn("brand")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.brand,callback:function(t){a.$set(a.createData,"brand",t)},expression:"createData.brand"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("部门编码")]),a._v(" "),e("Select",{on:{"on-change":a.departCodeChangeFn},model:{value:a.createData.departmentcode,callback:function(t){a.$set(a.createData,"departmentcode",t)},expression:"createData.departmentcode"}},a._l(a.departCode,function(t){return e("Option",{key:t.value,attrs:{value:t.value}},[a._v(a._s(t.label))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("部门名称")]),a._v(" "),e("Input",{attrs:{placeholder:"部门名称",disabled:""},model:{value:a.createData.departmentroom,callback:function(t){a.$set(a.createData,"departmentroom",t)},expression:"createData.departmentroom"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("大科室编码")]),a._v(" "),e("Select",{on:{"on-change":a.homeOfficeNumberChangeFn},model:{value:a.createData.homeofficenumber,callback:function(t){a.$set(a.createData,"homeofficenumber",t)},expression:"createData.homeofficenumber"}},a._l(a.homeOfficeNumber,function(t){return e("Option",{key:t.value,attrs:{value:t.value}},[a._v(a._s(t.label))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("大科室名称")]),a._v(" "),e("Input",{attrs:{placeholder:"大科室名称",disabled:""},model:{value:a.createData.homeofficename,callback:function(t){a.$set(a.createData,"homeofficename",t)},expression:"createData.homeofficename"}})],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("所在科室名称")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"所在科室名称"},on:{"on-focus":function(t){return a.inputFocusFn("locDept")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.locDept,callback:function(t){a.$set(a.createData,"locDept",t)},expression:"createData.locDept"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("供应商名称")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"供应商名称"},on:{"on-focus":function(t){return a.inputFocusFn("suppliername")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.suppliername,callback:function(t){a.$set(a.createData,"suppliername",t)},expression:"createData.suppliername"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("生产厂商名称")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"生产厂商名称"},on:{"on-focus":function(t){return a.inputFocusFn("generatebusinessname")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.generatebusinessname,callback:function(t){a.$set(a.createData,"generatebusinessname",t)},expression:"createData.generatebusinessname"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("是否进口")]),a._v(" "),e("Select",{model:{value:a.createData.isentrance,callback:function(t){a.$set(a.createData,"isentrance",t)},expression:"createData.isentrance"}},a._l(a.importData,function(t){return e("Option",{key:t.value,attrs:{value:t.value}},[a._v(a._s(t.label))])}),1)],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("原值")]),a._v(" "),e("InputNumber",{staticStyle:{width:"100%"},attrs:{min:0,placeholder:"原值"},model:{value:a.createData.money,callback:function(t){a.$set(a.createData,"money",t)},expression:"createData.money"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("入库时间")]),a._v(" "),e("DatePicker",{staticStyle:{width:"100%"},attrs:{type:"date",placeholder:"入库时间"},on:{"on-change":a.dateBuyFn},model:{value:a.buyTime,callback:function(t){a.buyTime=t},expression:"buyTime"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("状态"),e("span",{staticClass:"must_fill_info"},[a._v("（必填）")])]),a._v(" "),e("Select",{on:{"on-change":a.statusFn},model:{value:a.createData.status,callback:function(t){a.$set(a.createData,"status",t)},expression:"createData.status"}},a._l(a.statusData,function(t){return e("Option",{key:t.value,attrs:{value:t.value}},[a._v(a._s(t.label))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("录入人员"),e("span",{staticClass:"must_fill_info"},[a._v("（必填）")])]),a._v(" "),e("Input",{attrs:{placeholder:"录入人员"},model:{value:a.createData.recorder,callback:function(t){a.$set(a.createData,"recorder",t)},expression:"createData.recorder"}})],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("一级分类")]),a._v(" "),e("Select",{on:{"on-change":a.fstCatChangeFn},model:{value:a.createData.oneclassify,callback:function(t){a.$set(a.createData,"oneclassify",t)},expression:"createData.oneclassify"}},a._l(a.fstCatData,function(t){return e("Option",{key:t.value,attrs:{value:t.value}},[a._v(a._s(t.label))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("二级分类")]),a._v(" "),e("Select",{on:{"on-change":a.secCatChangeFn},model:{value:a.createData.secondclassify,callback:function(t){a.$set(a.createData,"secondclassify",t)},expression:"createData.secondclassify"}},a._l(a.secCatList,function(t){return e("Option",{key:t.value,attrs:{value:t.value}},[a._v(a._s(t.label))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("三级分类")]),a._v(" "),e("Select",{on:{"on-change":a.thdCatChangeFn},model:{value:a.createData.threeclassify,callback:function(t){a.$set(a.createData,"threeclassify",t)},expression:"createData.threeclassify"}},a._l(a.thdCatList,function(t){return e("Option",{key:t.value,attrs:{value:t.value}},[a._v(a._s(t.label))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("四级分类")]),a._v(" "),e("Select",{model:{value:a.createData.fourclassify,callback:function(t){a.$set(a.createData,"fourclassify",t)},expression:"createData.fourclassify"}},a._l(a.fouCatList,function(t){return e("Option",{key:t.value,attrs:{value:t.value}},[a._v(a._s(t.label))])}),1)],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("标签mac")]),a._v(" "),e("Input",{attrs:{placeholder:"格式:ac233fa031fa"},model:{value:a.createData.mac,callback:function(t){a.$set(a.createData,"mac",t)},expression:"createData.mac"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("数量")]),a._v(" "),e("InputNumber",{staticStyle:{width:"100%"},attrs:{min:1,placeholder:"数量"},model:{value:a.createData.amount,callback:function(t){a.$set(a.createData,"amount",t)},expression:"createData.amount"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("单位")]),a._v(" "),e("AutoComplete",{staticStyle:{width:"100%"},attrs:{placeholder:"单位"},on:{"on-focus":function(t){return a.inputFocusFn("unit")},"on-blur":a.inputBlurFn,"on-search":a.handleSearchFn},model:{value:a.createData.unit,callback:function(t){a.$set(a.createData,"unit",t)},expression:"createData.unit"}},a._l(a.inputData,function(t){return e("Option",{key:t,attrs:{value:t}},[a._v(a._s(t))])}),1)],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("处置申请单号")]),a._v(" "),e("Input",{attrs:{placeholder:"请输入处置申请单号"},model:{value:a.createData.applyoddnumbers,callback:function(t){a.$set(a.createData,"applyoddnumbers",t)},expression:"createData.applyoddnumbers"}})],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("SN")]),a._v(" "),e("Input",{attrs:{placeholder:"请输入SN号"},model:{value:a.createData.sn,callback:function(t){a.$set(a.createData,"sn",t)},expression:"createData.sn"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("建筑")]),a._v(" "),e("Input",{attrs:{placeholder:"请输入建筑楼名"},model:{value:a.createData.architecture,callback:function(t){a.$set(a.createData,"architecture",t)},expression:"createData.architecture"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("院区")]),a._v(" "),e("Input",{attrs:{placeholder:"请输入院区"},model:{value:a.createData.academy,callback:function(t){a.$set(a.createData,"academy",t)},expression:"createData.academy"}})],1)],1)],1),a._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:a.showRepair,expression:"showRepair"}],staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("维修人员")]),a._v(" "),e("Input",{attrs:{placeholder:"维修人员"},model:{value:a.createData.repairPeople,callback:function(t){a.$set(a.createData,"repairPeople",t)},expression:"createData.repairPeople"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("维修起止时间段")]),a._v(" "),e("DatePicker",{staticStyle:{width:"100%"},attrs:{type:"daterange",placement:"bottom-end",placeholder:"维修起止时间段"},on:{"on-change":a.dateRepairFn},model:{value:a.repairTime,callback:function(t){a.repairTime=t},expression:"repairTime"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("维修原因")]),a._v(" "),e("Input",{attrs:{type:"textarea",autosize:!0,placeholder:"维修原因"},model:{value:a.createData.repairReason,callback:function(t){a.$set(a.createData,"repairReason",t)},expression:"createData.repairReason"}})],1)],1)],1),a._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:a.showScrap,expression:"showScrap"}],staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("报废人员")]),a._v(" "),e("Input",{attrs:{placeholder:"报废人员"},model:{value:a.createData.scrapPeople,callback:function(t){a.$set(a.createData,"scrapPeople",t)},expression:"createData.scrapPeople"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("报废时间")]),a._v(" "),e("DatePicker",{staticStyle:{width:"100%"},attrs:{type:"date",placeholder:"报废时间"},on:{"on-change":a.dateScrapFn},model:{value:a.scrapTime,callback:function(t){a.scrapTime=t},expression:"scrapTime"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("报废原因")]),a._v(" "),e("Input",{attrs:{type:"textarea",autosize:!0,placeholder:"报废原因"},model:{value:a.createData.scrapReason,callback:function(t){a.$set(a.createData,"scrapReason",t)},expression:"createData.scrapReason"}})],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",[e("Col",[e("h3",[a._v("备注")]),a._v(" "),e("Input",{attrs:{type:"textarea",placeholder:"输入备注信息..."},model:{value:a.createData.remark,callback:function(t){a.$set(a.createData,"remark",t)},expression:"createData.remark"}})],1)],1)],1)]),a._v(" "),e("div",{staticClass:"submit_btn_wrap"},[e("Button",{attrs:{shape:"circle",type:"primary"},on:{click:a.createFn}},[a._v("确定新增")])],1)])]),a._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:a.showImg,expression:"showImg"}],staticClass:"scale_img_wrap",on:{click:a.hideImgFn}},[e("img",{attrs:{src:a.showImgData,alt:""}})]),a._v(" "),e("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:a.uploadModal,callback:function(t){a.uploadModal=t},expression:"uploadModal"}},[e("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[a.achieveUpload||a.errorUpload?a._e():e("Spin",{attrs:{fix:""}},[e("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),a._v(" "),e("div",[a._v("正在上传")])],1),a._v(" "),a.achieveUpload?e("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),a._v(" "),e("div",[a._v("上传成功")])],1):a._e(),a._v(" "),a.errorUpload?e("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),a._v(" "),e("div",[a._v("上传失败")])],1):a._e()],1)])],1)},staticRenderFns:[]};var R=e("VU/8")(L,A,!1,function(a){e("M+FE")},"data-v-6e79b7ac",null);t.default=R.exports},kD0S:function(a,t,e){"use strict";t.a=[{value:"把",label:"把"},{value:"本",label:"本"},{value:"个",label:"个"},{value:"根",label:"根"},{value:"间",label:"间"},{value:"件",label:"件"},{value:"节",label:"节"},{value:"辆",label:"辆"},{value:"米",label:"米"},{value:"平方米",label:"平方米"},{value:"台",label:"台"},{value:"套",label:"套"},{value:"张",label:"张"},{value:"帐",label:"帐"},{value:"组",label:"组"}]},m6KV:function(a,t,e){"use strict";t.a=[{value:"kssh0003",label:"kssh0003",name:"泌尿外科A"},{value:"kssh0005",label:"kssh0005",name:"小儿外科A"},{value:"kssh0043",label:"kssh0043",name:"肺癌中心A"},{value:"kssh0056",label:"kssh0056",name:"骨科A"},{value:"kssh0057",label:"kssh0057",name:"神经内科A"},{value:"kssh0058",label:"kssh0058",name:"胃肠外科A"},{value:"kssh0059",label:"kssh0059",name:"神经外科A"},{value:"kssh0060",label:"kssh0060",name:"烧伤整形科A"},{value:"kssh0062",label:"kssh0062",name:"胆道外科A"},{value:"kssh0063",label:"kssh0063",name:"甲状腺外科A"},{value:"kssh0064",label:"kssh0064",name:"眼科A"},{value:"kssh0066",label:"kssh0066",name:"肝脏外科A"},{value:"kssh0070",label:"kssh0070",name:"耳鼻咽喉--头颈外科医疗单元A"},{value:"kssh0073",label:"kssh0073",name:"心脏外科A"},{value:"kssh0077",label:"kssh0077",name:"重症医学科A"},{value:"kssh0089",label:"kssh0089",name:"手术室A"},{value:"kssh0091",label:"kssh0091",name:"外科办公室A"},{value:"kssh0113",label:"kssh0113",name:"乳腺外科B"}]}});
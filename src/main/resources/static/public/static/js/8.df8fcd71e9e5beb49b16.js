webpackJsonp([8],{GQVn:function(a,e,t){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var l=t("woOf"),s=t.n(l),n=t("bOdI"),i=t.n(n),r=t("wdqF"),o=t("bzvv"),u=t("8sRp"),p=t("KxKW"),c=t("JtJ0"),v=t("X2sI"),d=t("LuSo"),m=t("9N2q"),b=t("NAz/"),h=t("a84E"),f=t("EMb8"),D=t("7+uW"),_=t("bkN4"),g=t("kD0S"),k=t("ao6w"),y=t("mtaw"),C=t("Uo+3"),w=t("m6KV"),I=t("kitu"),x=t("wDsp"),F=t("W9Um");D.a.component("Icon",f.a),D.a.component("Row",h.a),D.a.component("Col",b.a),D.a.component("DatePicker",m.a),D.a.component("Select",d.a),D.a.component("Option",v.a),D.a.component("Input",c.a),D.a.component("Button",p.a),D.a.component("Modal",u.a),D.a.component("InputNumber",o.a),D.a.component("Spin",r.a);var R={name:"assets_detail",components:{},props:{},data:function(){var a;return a={isFresh:!1,showImgModal:!1,showRepair:!1,showScrap:!1,showImg:!1,showImgData:"",buyTime:"",repairTime:[],scrapTime:"",imgLimit:5,imgList:[],editData:{assetID:"",generalName:"",assetName:"",location:"",specification:"",type:"",placeoforigin:"",brand:"",departmentcode:"",departmentroom:"",homeofficenumber:"",homeofficename:"",isentrance:"",suppliername:"",generatebusinessname:"",money:0,buyDate:"",locDept:"",status:"",recordPeople:this.$store.state.user,mac:"",amount:1,unit:"",oneclassify:"",secondclassify:"",threeclassify:"",fourclassify:"",repairPeople:"",repairDate:"",repairReason:"",scrapPeople:"",scrapDate:"",scrapReason:"",remark:""},uploadModal:!1,achieveUpload:!1,errorUpload:!1,departData:_.a,unitData:g.a,statusData:k.a},i()(a,"statusData",k.a),i()(a,"fstCatData",y.b),i()(a,"secCatData",y.c),i()(a,"thdCatData",y.d),i()(a,"fouCatData",y.a),i()(a,"secCatList",[]),i()(a,"thdCatList",[]),i()(a,"fouCatList",[]),i()(a,"departCode",C.a),i()(a,"homeOfficeNumber",w.a),i()(a,"locDept",I.a),i()(a,"importData",[{value:"是",label:"是"},{value:"否",label:"否"}]),a},watch:{},computed:{},methods:{fstCatChangeFn:function(){var a=this;this.secCatList=this.secCatData.filter(function(e,t){return e.parent===a.editData.oneclassify}),this.editData.secondclassify="",this.editData.threeclassify="",this.editData.fourclassify=""},secCatChangeFn:function(){var a=this;this.thdCatList=this.thdCatData.filter(function(e,t){return e.parent===a.editData.secondclassify}),this.editData.threeclassify="",this.editData.fourclassify=""},thdCatChangeFn:function(){var a=this;this.fouCatList=this.fouCatData.filter(function(e,t){return e.parent===a.editData.threeclassify}),this.editData.fourclassify=""},initCatFn:function(){var a=this;this.secCatList=this.secCatData.filter(function(e,t){return e.parent===a.editData.oneclassify}),this.thdCatList=this.thdCatData.filter(function(e,t){return e.parent===a.editData.secondclassify}),this.fouCatList=this.fouCatData.filter(function(e,t){return e.parent===a.editData.threeclassify})},departCodeChangeFn:function(a){var e=this;this.departCode.forEach(function(t,l){t.value===a&&(e.editData.departmentroom=t.name)})},homeOfficeNumberChangeFn:function(a){var e=this;this.homeOfficeNumber.forEach(function(t,l){t.value===a&&(e.editData.homeofficename=t.name)})},addImgFn:function(a){for(var e=this,t=a.target.files,l=[],s=t.length,n=0;n<s;n++)l.push(t[n]);l.forEach(function(a,t){var l=new FileReader;l.readAsDataURL(a),l.onload=function(a){Object(F.a)(a.target.result,600,.9).then(function(a){e.imgList.length<e.imgLimit&&e.imgList.push(a)})}})},showImgFn:function(a){var e=this;this.imgList.forEach(function(t,l){l===a&&(e.showImgData=t,e.showImg=!0)})},hideImgFn:function(){this.showImg=!1,this.showImgData=""},removeImgFn:function(a){this.imgList.splice(a,1)},dateBuyFn:function(a){this.editData.buyDate=a},dateScrapFn:function(a){this.editData.scrapDate=a},dateRepairFn:function(a){this.editData.repairDate=a[0]+","+a[1]},statusFn:function(a){switch(a){case"正常":this.showRepair=!1,this.showScrap=!1;break;case"在修":this.showScrap=!1,this.showRepair=!0;break;case"已报废":this.showRepair=!1,this.showScrap=!0}},successFn:function(){var a=this;this.achieveUpload=!0,setTimeout(function(){a.uploadModal=!1,setTimeout(function(){a.achieveUpload=!1,a.errorUpload=!1,a.isFresh=!0,a.$router.go(-1)},500)},1e3)},errorFn:function(){var a=this;this.errorUpload=!0,setTimeout(function(){a.uploadModal=!1,setTimeout(function(){a.achieveUpload=!1,a.errorUpload=!1},500)},1e3)},saveFn:function(){var a=this;if(this.editData.assetID.trim()&&this.editData.status.trim()&&this.editData.recordPeople.trim()){this.uploadModal=!0;var e={assetID:this.editData.assetID.trim(),image:this.imgList[0]?this.imgList[0]:"",leftImage:this.imgList[1]?this.imgList[1]:"",allroundImage:this.imgList[2]?this.imgList[2]:"",aboveImage:this.imgList[3]?this.imgList[3]:"",rightImage:this.imgList[4]?this.imgList[4]:""},t={};switch(this.editData.status.trim()){case"正常":break;case"在修":t={repairPeople:this.editData.repairPeople.trim(),repairDate:this.editData.repairDate.trim(),repairReason:this.editData.repairReason.trim()};break;case"已报废":t={scrapPeople:this.editData.scrapPeople.trim(),scrapDate:this.editData.scrapDate.trim(),scrapReason:this.editData.scrapReason.trim()}}var l={assetID:this.editData.assetID.trim(),generalName:this.editData.generalName.trim(),assetName:this.editData.assetName.trim(),location:this.editData.location.trim(),specification:this.editData.specification.trim(),type:this.editData.type.trim(),placeoforigin:this.editData.placeoforigin.trim(),brand:this.editData.brand.trim(),departmentcode:this.editData.departmentcode,departmentroom:this.editData.departmentroom,homeofficenumber:this.editData.homeofficenumber,homeofficename:this.editData.homeofficename,isentrance:this.editData.isentrance.trim(),suppliername:this.editData.suppliername.trim(),generatebusinessname:this.editData.generatebusinessname.trim(),money:Math.abs(this.editData.money),buyDate:this.editData.buyDate,locDept:this.editData.locDept.trim(),status:this.editData.status.trim(),recordPeople:this.editData.recordPeople.trim(),mac:this.editData.mac.trim(),amount:Math.abs(this.editData.amount),unit:this.editData.unit,oneclassify:this.editData.oneclassify,secondclassify:this.editData.secondclassify,threeclassify:this.editData.threeclassify,fourclassify:this.editData.fourclassify,remark:this.editData.remark.trim()},n={};s()(n,l,t);var i=[n];console.log(i),this.$axios.post(x.a.imgUpload,e).then(function(e){"ok"===e.data.msg?a.$axios.post(x.a.assetsUpdate,i).then(function(e){"ok"===e.data.msg?a.successFn():e.data.message?(a.errorFn(),u.a.error({title:"错误提示！",content:e.data.message,okText:"确定"})):"failed"===e.data.msg&&(a.errorFn(),u.a.error({title:"错误提示！",content:e.data.message,okText:"确定"}))}).catch(function(a){}):"failed"===e.data.msg&&a.errorFn()}).catch(function(e){a.$Message.error({content:"图片上传失败!",duration:2})})}else this.$Message.error({content:"请补充完整信息!",duration:2})},getDataFn:function(){var a=this;if(window.sessionStorage.getItem("detailData")){var e=JSON.parse(window.sessionStorage.getItem("detailData")),t={assetID:e.assetID,generalName:e.generalName?e.generalName:"",assetName:e.assetName?e.assetName:"",location:e.location?e.location:"",specification:e.specification?e.specification:"",type:e.type?e.type:"",placeoforigin:e.placeoforigin?e.placeoforigin:"",brand:e.brand?e.brand:"",departmentcode:e.departmentcode?e.departmentcode:"",departmentroom:e.departmentroom?e.departmentroom:"",homeofficenumber:e.homeofficenumber?e.homeofficenumber:"",homeofficename:e.homeofficename?e.homeofficename:"",isentrance:e.isentrance?e.isentrance:"",suppliername:e.suppliername?e.suppliername:"",generatebusinessname:e.generatebusinessname?e.generatebusinessname:"",money:e.money?Number(e.money):0,buyDate:e.buyDate?e.buyDate:"",locDept:e.locDept?e.locDept:"",status:e.status?e.status:"",recordPeople:e.recordPeople?e.recordPeople:this.$store.state.user,mac:e.mac?e.mac:"",amount:e.amount?Number(e.amount):1,unit:e.unit?e.unit:"",oneclassify:e.oneclassify?e.oneclassify:"",secondclassify:e.secondclassify?e.secondclassify:"",threeclassify:e.threeclassify?e.threeclassify:"",fourclassify:e.fourclassify?e.fourclassify:"",remark:e.remark?e.remark:""};this.buyTime=t.buyDate;var l={};switch(t.status){case"正常":this.showRepair=!1,this.showScrap=!1,l={repairPeople:"",repairDate:"",repairReason:"",scrapPeople:"",scrapDate:"",scrapReason:""};break;case"在修":l={repairPeople:e.repairPeople?e.repairPeople:"",repairDate:e.repairDate?e.repairDate:"",repairReason:e.repairReason?e.repairReason:"",scrapPeople:"",scrapDate:"",scrapReason:""},this.repairTime=""===l.repairDate?[]:l.repairDate.split(","),this.showScrap=!1,this.showRepair=!0;break;case"已报废":l={repairPeople:"",repairDate:"",repairReason:"",scrapPeople:e.scrapPeople?e.scrapPeople:"",scrapDate:e.scrapDate?e.scrapDate:"",scrapReason:e.scrapReason?e.scrapReason:""},this.scrapTime=l.scrapTime,this.showRepair=!1,this.showScrap=!0}var n={};s()(n,t,l),this.editData=n,this.initCatFn(),this.getImgFn("imgShow"),this.getImgFn("imgShowLeft"),this.getImgFn("imgShowAllround"),this.getImgFn("imgShowAbove"),this.getImgFn("imgShowRight")}else this.$Message.error({content:"未找到资产信息，将返回上一页!",duration:2}),setTimeout(function(){a.$router.go(-1)},2e3)},getImgFn:function(a){var e=this;this.$axios.get(x.a[a]+this.editData.assetID,{responseType:"arraybuffer"}).then(function(a){var t;"data:image/png;base64,"===(t="data:image/png;base64,"+btoa(new Uint8Array(a.data).reduce(function(a,e){return a+String.fromCharCode(e)},"")))||e.imgList.push(t)}).catch(function(a){})}},beforeRouteEnter:function(a,e,t){a.meta.needFresh=!1,t()},beforeRouteLeave:function(a,e,t){this.isFresh&&(e.meta.needFresh=!0),t()},created:function(){},mounted:function(){this.getDataFn()},beforeDestroy:function(){this.$Loading.finish(),window.sessionStorage.removeItem("detailData")}},S={render:function(){var a=this,e=a.$createElement,t=a._self._c||e;return t("div",{staticClass:"assets_detail_wrap"},[t("div",{staticClass:"row_box hover_animat custom_bg_color_white"},[t("h2",{staticClass:"row_title"},[a._v("图像")]),a._v(" "),t("div",{staticClass:"clearfix"},[a._l(a.imgList,function(e,l){return t("div",{staticClass:"img_block",attrs:{index:l}},[t("img",{attrs:{src:e,alt:""}}),a._v(" "),t("div",{staticClass:"img_block_tools clearfix"},[t("div",{staticClass:"img_block_tools_left"},[t("Icon",{attrs:{type:"md-expand"},on:{click:function(e){return a.showImgFn(l)}}})],1),a._v(" "),t("div",{staticClass:"img_block_tools_right"},[t("Icon",{attrs:{type:"md-trash"},on:{click:function(e){return a.removeImgFn(l)}}})],1)])])}),a._v(" "),t("div",{directives:[{name:"show",rawName:"v-show",value:!(a.imgList.length>=a.imgLimit),expression:"imgList.length>=imgLimit? false : true"}],staticClass:"img_block add_img_wrap"},[t("div",{staticClass:"add_img_box"},[t("Icon",{attrs:{type:"md-add"}}),a._v(" "),t("input",{staticClass:"add_img_input",attrs:{type:"file",accept:"image/jpg, image/jpeg,image/png",multiple:"multiple"},on:{change:function(e){return a.addImgFn(e)}}})],1)])],2),a._v(" "),t("p",{staticClass:"img_notice"},[t("Icon",{attrs:{type:"ios-alert"}}),a._v("最多可上传"+a._s(a.imgLimit)+"张图片")],1)]),a._v(" "),t("div",{staticClass:"row_wrap hover_animat custom_bg_color_white"},[t("div",{staticClass:"row_box"},[t("h2",{staticClass:"row_title"},[a._v("明细")]),a._v(" "),t("div",{staticClass:"form_wrap"},[t("div",{staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("资产编号"),t("span",{staticClass:"must_fill_info"},[a._v("（必填）")])]),a._v(" "),t("Input",{attrs:{placeholder:"资产id"},model:{value:a.editData.assetID,callback:function(e){a.$set(a.editData,"assetID",e)},expression:"editData.assetID"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("通用名称")]),a._v(" "),t("Input",{attrs:{placeholder:"通用名称"},model:{value:a.editData.generalName,callback:function(e){a.$set(a.editData,"generalName",e)},expression:"editData.generalName"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("资产名称")]),a._v(" "),t("Input",{attrs:{placeholder:"资产名称"},model:{value:a.editData.assetName,callback:function(e){a.$set(a.editData,"assetName",e)},expression:"editData.assetName"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("存放地点")]),a._v(" "),t("Input",{attrs:{placeholder:"存放地点"},model:{value:a.editData.location,callback:function(e){a.$set(a.editData,"location",e)},expression:"editData.location"}})],1)],1)],1),a._v(" "),t("div",{staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("规格")]),a._v(" "),t("Input",{attrs:{placeholder:"规格"},model:{value:a.editData.specification,callback:function(e){a.$set(a.editData,"specification",e)},expression:"editData.specification"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("型号")]),a._v(" "),t("Input",{attrs:{placeholder:"型号"},model:{value:a.editData.type,callback:function(e){a.$set(a.editData,"type",e)},expression:"editData.type"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("产地")]),a._v(" "),t("Input",{attrs:{placeholder:"产地"},model:{value:a.editData.placeoforigin,callback:function(e){a.$set(a.editData,"placeoforigin",e)},expression:"editData.placeoforigin"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("品牌")]),a._v(" "),t("Input",{attrs:{placeholder:"品牌"},model:{value:a.editData.brand,callback:function(e){a.$set(a.editData,"brand",e)},expression:"editData.brand"}})],1)],1)],1),a._v(" "),t("div",{staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("部门编码")]),a._v(" "),t("Select",{on:{"on-change":a.departCodeChangeFn},model:{value:a.editData.departmentcode,callback:function(e){a.$set(a.editData,"departmentcode",e)},expression:"editData.departmentcode"}},a._l(a.departCode,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("部门名称")]),a._v(" "),t("Input",{attrs:{placeholder:"部门名称",disabled:""},model:{value:a.editData.departmentroom,callback:function(e){a.$set(a.editData,"departmentroom",e)},expression:"editData.departmentroom"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("大科室编码")]),a._v(" "),t("Select",{on:{"on-change":a.homeOfficeNumberChangeFn},model:{value:a.editData.homeofficenumber,callback:function(e){a.$set(a.editData,"homeofficenumber",e)},expression:"editData.homeofficenumber"}},a._l(a.homeOfficeNumber,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("大科室名称")]),a._v(" "),t("Input",{attrs:{placeholder:"大科室名称",disabled:""},model:{value:a.editData.homeofficename,callback:function(e){a.$set(a.editData,"homeofficename",e)},expression:"editData.homeofficename"}})],1)],1)],1),a._v(" "),t("div",{staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("所在科室名称")]),a._v(" "),t("Select",{model:{value:a.editData.locDept,callback:function(e){a.$set(a.editData,"locDept",e)},expression:"editData.locDept"}},a._l(a.locDept,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("供应商名称")]),a._v(" "),t("Input",{attrs:{placeholder:"供应商名称"},model:{value:a.editData.suppliername,callback:function(e){a.$set(a.editData,"suppliername",e)},expression:"editData.suppliername"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("生产厂商名称")]),a._v(" "),t("Input",{attrs:{placeholder:"生产厂商名称"},model:{value:a.editData.generatebusinessname,callback:function(e){a.$set(a.editData,"generatebusinessname",e)},expression:"editData.generatebusinessname"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("是否进口")]),a._v(" "),t("Select",{model:{value:a.editData.isentrance,callback:function(e){a.$set(a.editData,"isentrance",e)},expression:"editData.isentrance"}},a._l(a.importData,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1)],1)],1),a._v(" "),t("div",{staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("原值")]),a._v(" "),t("InputNumber",{staticStyle:{width:"100%"},attrs:{min:0,placeholder:"原值"},model:{value:a.editData.money,callback:function(e){a.$set(a.editData,"money",e)},expression:"editData.money"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("入库时间")]),a._v(" "),t("DatePicker",{staticStyle:{width:"100%"},attrs:{type:"date",placeholder:"入库时间"},on:{"on-change":a.dateBuyFn},model:{value:a.buyTime,callback:function(e){a.buyTime=e},expression:"buyTime"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("状态"),t("span",{staticClass:"must_fill_info"},[a._v("（必填）")])]),a._v(" "),t("Select",{on:{"on-change":a.statusFn},model:{value:a.editData.status,callback:function(e){a.$set(a.editData,"status",e)},expression:"editData.status"}},a._l(a.statusData,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("录入人员"),t("span",{staticClass:"must_fill_info"},[a._v("（必填）")])]),a._v(" "),t("Input",{attrs:{placeholder:"录入人员"},model:{value:a.editData.recordPeople,callback:function(e){a.$set(a.editData,"recordPeople",e)},expression:"editData.recordPeople"}})],1)],1)],1),a._v(" "),t("div",{staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("一级分类")]),a._v(" "),t("Select",{on:{"on-change":a.fstCatChangeFn},model:{value:a.editData.oneclassify,callback:function(e){a.$set(a.editData,"oneclassify",e)},expression:"editData.oneclassify"}},a._l(a.fstCatData,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("二级分类")]),a._v(" "),t("Select",{on:{"on-change":a.secCatChangeFn},model:{value:a.editData.secondclassify,callback:function(e){a.$set(a.editData,"secondclassify",e)},expression:"editData.secondclassify"}},a._l(a.secCatList,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("三级分类")]),a._v(" "),t("Select",{on:{"on-change":a.thdCatChangeFn},model:{value:a.editData.threeclassify,callback:function(e){a.$set(a.editData,"threeclassify",e)},expression:"editData.threeclassify"}},a._l(a.thdCatList,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("四级分类")]),a._v(" "),t("Select",{model:{value:a.editData.fourclassify,callback:function(e){a.$set(a.editData,"fourclassify",e)},expression:"editData.fourclassify"}},a._l(a.fouCatList,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1)],1)],1),a._v(" "),t("div",{staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("标签mac")]),a._v(" "),t("Input",{attrs:{placeholder:"格式:ac233fa031fa"},model:{value:a.editData.mac,callback:function(e){a.$set(a.editData,"mac",e)},expression:"editData.mac"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("数量")]),a._v(" "),t("InputNumber",{staticStyle:{width:"100%"},attrs:{min:1,placeholder:"数量"},model:{value:a.editData.amount,callback:function(e){a.$set(a.editData,"amount",e)},expression:"editData.amount"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("单位")]),a._v(" "),t("Select",{model:{value:a.editData.unit,callback:function(e){a.$set(a.editData,"unit",e)},expression:"editData.unit"}},a._l(a.unitData,function(e){return t("Option",{key:e.value,attrs:{value:e.value}},[a._v(a._s(e.label))])}),1)],1)],1)],1),a._v(" "),t("div",{directives:[{name:"show",rawName:"v-show",value:a.showRepair,expression:"showRepair"}],staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("维修人员")]),a._v(" "),t("Input",{attrs:{placeholder:"维修人员"},model:{value:a.editData.repairPeople,callback:function(e){a.$set(a.editData,"repairPeople",e)},expression:"editData.repairPeople"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("维修起止时间段")]),a._v(" "),t("DatePicker",{staticStyle:{width:"100%"},attrs:{type:"daterange",placement:"bottom-end",placeholder:"维修起止时间段"},on:{"on-change":a.dateRepairFn},model:{value:a.repairTime,callback:function(e){a.repairTime=e},expression:"repairTime"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("维修原因")]),a._v(" "),t("Input",{attrs:{type:"textarea",autosize:!0,placeholder:"维修原因"},model:{value:a.editData.repairReason,callback:function(e){a.$set(a.editData,"repairReason",e)},expression:"editData.repairReason"}})],1)],1)],1),a._v(" "),t("div",{directives:[{name:"show",rawName:"v-show",value:a.showScrap,expression:"showScrap"}],staticClass:"form_item"},[t("Row",{attrs:{gutter:20}},[t("Col",{attrs:{span:"6"}},[t("h3",[a._v("报废人员")]),a._v(" "),t("Input",{attrs:{placeholder:"报废人员"},model:{value:a.editData.scrapPeople,callback:function(e){a.$set(a.editData,"scrapPeople",e)},expression:"editData.scrapPeople"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("报废时间")]),a._v(" "),t("DatePicker",{staticStyle:{width:"100%"},attrs:{type:"date",placeholder:"报废时间"},on:{"on-change":a.dateScrapFn},model:{value:a.scrapTime,callback:function(e){a.scrapTime=e},expression:"scrapTime"}})],1),a._v(" "),t("Col",{attrs:{span:"6"}},[t("h3",[a._v("报废原因")]),a._v(" "),t("Input",{attrs:{type:"textarea",autosize:!0,placeholder:"报废原因"},model:{value:a.editData.scrapReason,callback:function(e){a.$set(a.editData,"scrapReason",e)},expression:"editData.scrapReason"}})],1)],1)],1),a._v(" "),t("div",{staticClass:"form_item"},[t("Row",[t("Col",[t("h3",[a._v("备注")]),a._v(" "),t("Input",{attrs:{type:"textarea",placeholder:"输入备注信息..."},model:{value:a.editData.remark,callback:function(e){a.$set(a.editData,"remark",e)},expression:"editData.remark"}})],1)],1)],1)]),a._v(" "),t("div",{staticClass:"submit_btn_wrap"},[t("Button",{attrs:{type:"primary",shape:"circle"},on:{click:a.saveFn}},[a._v("确定修改")])],1)])]),a._v(" "),t("div",{directives:[{name:"show",rawName:"v-show",value:a.showImg,expression:"showImg"}],staticClass:"scale_img_wrap",on:{click:a.hideImgFn}},[t("img",{attrs:{src:a.showImgData,alt:""}})]),a._v(" "),t("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:a.uploadModal,callback:function(e){a.uploadModal=e},expression:"uploadModal"}},[t("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[a.achieveUpload||a.errorUpload?a._e():t("Spin",{attrs:{fix:""}},[t("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),a._v(" "),t("div",[a._v("正在更新")])],1),a._v(" "),a.achieveUpload?t("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[t("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),a._v(" "),t("div",[a._v("更新成功")])],1):a._e(),a._v(" "),a.errorUpload?t("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[t("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),a._v(" "),t("div",[a._v("更新失败")])],1):a._e()],1)])],1)},staticRenderFns:[]};var $=t("VU/8")(R,S,!1,function(a){t("wkSW")},"data-v-ceb0d67e",null);e.default=$.exports},ao6w:function(a,e,t){"use strict";e.a=[{value:"正常",label:"正常"},{value:"在修",label:"在修"},{value:"已报废",label:"已报废"}]},kD0S:function(a,e,t){"use strict";e.a=[{value:"把",label:"把"},{value:"本",label:"本"},{value:"个",label:"个"},{value:"根",label:"根"},{value:"间",label:"间"},{value:"件",label:"件"},{value:"节",label:"节"},{value:"辆",label:"辆"},{value:"米",label:"米"},{value:"平方米",label:"平方米"},{value:"台",label:"台"},{value:"套",label:"套"},{value:"张",label:"张"},{value:"帐",label:"帐"},{value:"组",label:"组"}]},m6KV:function(a,e,t){"use strict";e.a=[{value:"kssh0003",label:"kssh0003",name:"泌尿外科A"},{value:"kssh0005",label:"kssh0005",name:"小儿外科A"},{value:"kssh0043",label:"kssh0043",name:"肺癌中心A"},{value:"kssh0056",label:"kssh0056",name:"骨科A"},{value:"kssh0057",label:"kssh0057",name:"神经内科A"},{value:"kssh0058",label:"kssh0058",name:"胃肠外科A"},{value:"kssh0059",label:"kssh0059",name:"神经外科A"},{value:"kssh0060",label:"kssh0060",name:"烧伤整形科A"},{value:"kssh0062",label:"kssh0062",name:"胆道外科A"},{value:"kssh0063",label:"kssh0063",name:"甲状腺外科A"},{value:"kssh0064",label:"kssh0064",name:"眼科A"},{value:"kssh0066",label:"kssh0066",name:"肝脏外科A"},{value:"kssh0070",label:"kssh0070",name:"耳鼻咽喉--头颈外科医疗单元A"},{value:"kssh0073",label:"kssh0073",name:"心脏外科A"},{value:"kssh0077",label:"kssh0077",name:"重症医学科A"},{value:"kssh0089",label:"kssh0089",name:"手术室A"},{value:"kssh0091",label:"kssh0091",name:"外科办公室A"},{value:"kssh0113",label:"kssh0113",name:"乳腺外科B"}]},mtaw:function(a,e,t){"use strict";t.d(e,"b",function(){return l}),t.d(e,"c",function(){return s}),t.d(e,"d",function(){return n}),t.d(e,"a",function(){return i});var l=[{value:"民用",label:"民用"},{value:"医用",label:"医用"}],s=[{value:"办公设备类",label:"办公设备类",parent:"民用"},{value:"电器设备类",label:"电器设备类",parent:"民用"},{value:"家具类",label:"家具类",parent:"民用"},{value:"运行保障设备类",label:"运行保障设备类",parent:"民用"},{value:"病理设备类",label:"病理设备类",parent:"医用"},{value:"病室护理类",label:"病室护理类",parent:"医用"},{value:"放射仪器设备类",label:"放射仪器设备类",parent:"医用"},{value:"检查设备类",label:"检查设备类",parent:"医用"},{value:"检验分析设备类",label:"检验分析设备类",parent:"医用"},{value:"手术设备类",label:"手术设备类",parent:"医用"},{value:"五官仪器设备类",label:"五官仪器设备类",parent:"医用"},{value:"药械设备类",label:"药械设备类",parent:"医用"},{value:"治疗仪器类",label:"治疗仪器类",parent:"医用"}],n=[{value:"X射线设备",label:"X射线设备",parent:"放射仪器设备类"},{value:"安保设备",label:"安保设备",parent:"运行保障设备类"},{value:"安全设备",label:"安全设备",parent:"运行保障设备类"},{value:"办公设备",label:"办公设备",parent:"办公设备类"},{value:"便携式超声诊断设备",label:"便携式超声诊断设备",parent:"检查设备类"},{value:"病理设备",label:"病理设备",parent:"病理设备类"},{value:"病室护理",label:"病室护理",parent:"病室护理类"},{value:"彩色超声成像设备",label:"彩色超声成像设备",parent:"检查设备类"},{value:"电器设备",label:"电器设备",parent:"电器设备类"},{value:"耳科仪器",label:"耳科仪器",parent:"五官仪器设备类"},{value:"光学内窥镜",label:"光学内窥镜",parent:"检查设备类"},{value:"柜类",label:"柜类",parent:"家具类"},{value:"肌电诊断",label:"肌电诊断",parent:"检查设备类"},{value:"基因生和命科学仪器",label:"基因生和命科学仪器",parent:"检验分析设备类"},{value:"激光手术和治疗设备",label:"激光手术和治疗设备",parent:"手术设备类"},{value:"架类",label:"架类",parent:"家具类"},{value:"监护设备",label:"监护设备",parent:"检查设备类"},{value:"检验分析设备",label:"检验分析设备",parent:"检验分析设备类"},{value:"理疗康复仪器",label:"理疗康复仪器",parent:"治疗仪器类"},{value:"脑电诊断、治疗",label:"脑电诊断、治疗",parent:"检查设备类"},{value:"其它治疗仪器",label:"其它治疗仪器",parent:"治疗仪器类"},{value:"器械台、柜",label:"器械台、柜",parent:"病室护理类"},{value:"腔内手术用内窥镜",label:"腔内手术用内窥镜",parent:"检查设备类"},{value:"沙发类",label:"沙发类",parent:"家具类"},{value:"射频治疗设备",label:"射频治疗设备",parent:"手术设备类"},{value:"手术及急救装置",label:"手术及急救装置",parent:"手术设备类"},{value:"手术设备",label:"手术设备",parent:"手术设备类"},{value:"手术显微镜及放大器",label:"手术显微镜及放大器",parent:"手术设备类"},{value:"输液辅助设备",label:"输液辅助设备",parent:"检查设备类"},{value:"台类",label:"台类",parent:"家具类"},{value:"通讯设备",label:"通讯设备",parent:"电气设备类"},{value:"消毒灭菌设备",label:"消毒灭菌设备",parent:"药械设备类"},{value:"信息设备",label:"信息设备",parent:"办公设备类"},{value:"眼科光学仪器",label:"眼科光学仪器",parent:"五官仪器设备类"},{value:"医用x射线附属设备",label:"医用x射线附属设备",parent:"放射仪器设备类"},{value:"医用凳、椅、台、床",label:"医用凳、椅、台、床",parent:"手术设备类"},{value:"医用防护类",label:"医用防护类",parent:"放射仪器设备类"},{value:"医用高能射线治疗设备",label:"医用高能射线治疗设备",parent:"放射仪器设备类"},{value:"医用高频设备",label:"医用高频设备",parent:"手术设备类"},{value:"医用剌激器",label:"医用剌激器",parent:"检查设备类"},{value:"医用冷温、冷藏设备",label:"医用冷温、冷藏设备",parent:"检验分析设备类"},{value:"医用培养箱",label:"医用培养箱",parent:"检验分析设备类"},{value:"医用推车",label:"医用推车",parent:"病室护理类"},{value:"医用推车",label:"医用推车",parent:"检验分析设备类"},{value:"音视频设备",label:"音视频设备",parent:"电器设备类"},{value:"有创式电生理仪器",label:"有创式电生理仪器",parent:"手术设备类"},{value:"运行保障设备",label:"运行保障设备",parent:"运行保障设备类"},{value:"专用手术台床",label:"专用手术台床",parent:"手术设备类"},{value:"桌类",label:"桌类",parent:"家具类"}],i=[{value:"B超",label:"B超",parent:"便携式超声诊断设备"},{value:"YAG激光机",label:"YAG激光机",parent:"眼科光学仪器"},{value:"办公桌",label:"办公桌",parent:"桌类"},{value:"鼻内窥镜",label:"鼻内窥镜",parent:"耳科仪器"},{value:"笔记本电脑",label:"笔记本电脑",parent:"信息设备"},{value:"标本车",label:"标本车",parent:"医用凳、椅、台、床"},{value:"标本柜",label:"标本柜",parent:"病理设备"},{value:"冰箱",label:"冰箱",parent:"电器设备"},{value:"玻切机",label:"玻切机",parent:"眼科光学仪器"},{value:"不锈钢存物柜",label:"不锈钢存物柜",parent:"器械台、柜"},{value:"彩色超声诊断仪",label:"彩色超声诊断仪",parent:"彩色超声成像设备"},{value:"操作台",label:"操作台",parent:"器械台、柜"},{value:"茶几",label:"茶几",parent:"桌类"},{value:"超声刀",label:"超声刀",parent:"医用高频设备"},{value:"超声气压弹道碎石机",label:"超声气压弹道碎石机",parent:"其它治疗仪器"},{value:"超声乳化仪",label:"超声乳化仪",parent:"眼科光学仪器"},{value:"除湿机",label:"除湿机",parent:"电器设备"},{value:"储物柜",label:"储物柜",parent:"柜类"},{value:"穿剌肾镜",label:"穿剌肾镜",parent:"腔内手术用内窥镜"},{value:"打印机",label:"打印机",parent:"信息设备"},{value:"担架",label:"担架",parent:"医用推车"},{value:"导航系统",label:"导航系统",parent:"手术及急救装置"},{value:"等离子体手术系统",label:"等离子体手术系统",parent:"耳科仪器"},{value:"等离子治疗仪",label:"等离子治疗仪",parent:"其它治疗仪器"},{value:"低温冰箱",label:"低温冰箱",parent:"医用冷温、冷藏设备"},{value:"电动手术刀",label:"电动手术刀",parent:"手术及急救装置"},{value:"电动手术台",label:"电动手术台",parent:"专用手术台床"},{value:"电动手术椅",label:"电动手术椅",parent:"医用凳、椅、台、床"},{value:"电动吸引器",label:"电动吸引器",parent:"手术设备"},{value:"电动胸骨锯",label:"电动胸骨锯",parent:"手术及急救装置"},{value:"电动止血仪",label:"电动止血仪",parent:"手术及急救装置"},{value:"电视机",label:"电视机",parent:"音视频设备"},{value:"电梯",label:"电梯",parent:"安全设备"},{value:"电外科工作站",label:"电外科工作站",parent:"手术及急救装置"},{value:"电子腹腔镜",label:"电子腹腔镜",parent:"光学内窥镜"},{value:"电子结肠镜",label:"电子结肠镜",parent:"光学内窥镜"},{value:"电子内窥镜系统",label:"电子内窥镜系统",parent:"光学内窥镜"},{value:"电子胃镜",label:"电子胃镜",parent:"光学内窥镜"},{value:"电钻",label:"电钻",parent:"手术及急救装置"},{value:"动力系统",label:"动力系统",parent:"手术及急救装置"},{value:"读片灯",label:"读片灯",parent:"医用x射线附属设备"},{value:"对讲机",label:"对讲机",parent:"通讯设备"},{value:"耳科治疗台",label:"耳科治疗台",parent:"耳科仪器"},{value:"二氧化氮激光治疗机",label:"二氧化氮激光治疗机",parent:"激光手术和治疗设备"},{value:"腹腔镜",label:"腹腔镜",parent:"腔内手术用内窥镜"},{value:"高频电刀",label:"高频电刀",parent:"医用高频设备"},{value:"搁物架",label:"搁物架",parent:"器械台、柜"},{value:"工作柜",label:"工作柜",parent:"器械台、柜"},{value:"工作台",label:"工作台",parent:"台类"},{value:"骨科动力系统",label:"骨科动力系统",parent:"其它治疗仪器"},{value:"光导纤维",label:"光导纤维",parent:"光学内窥镜"},{value:"恒温加热台座",label:"恒温加热台座",parent:"检验分析设备"},{value:"环氧乙烷灭菌器、低温灭菌器",label:"环氧乙烷灭菌器、低温灭菌器",parent:"消毒灭菌设备"},{value:"钬激光治疗机",label:"钬激光治疗机",parent:"激光手术和治疗设备"},{value:"货柜",label:"货柜",parent:"柜类"},{value:"货架",label:"货架",parent:"架类"},{value:"肌电图机",label:"肌电图机",parent:"肌电诊断"},{value:"激光扫描枪",label:"激光扫描枪",parent:"信息设备"},{value:"计算机",label:"计算机",parent:"信息设备"},{value:"间接检眼镜",label:"间接检眼镜",parent:"眼科光学仪器"},{value:"监护仪",label:"监护仪",parent:"监护设备"},{value:"经颅多普勒",label:"经颅多普勒",parent:"彩色超声成像设备"},{value:"考勤机",label:"考勤机",parent:"办公设备"},{value:"空气灭菌机",label:"空气灭菌机",parent:"病室护理"},{value:"空调",label:"空调",parent:"电器设备"},{value:"空压机",label:"空压机",parent:"运行保障设备"},{value:"冷藏箱",label:"冷藏箱",parent:"医用冷温、冷藏设备"},{value:"冷冻治疗机",label:"冷冻治疗机",parent:"其它治疗仪器"},{value:"冷光源",label:"冷光源",parent:"手术及急救装置"},{value:"冷柜",label:"冷柜",parent:"电器设备"},{value:"冷循环射频系统",label:"冷循环射频系统",parent:"射频治疗设备"},{value:"泌尿内窥镜",label:"泌尿内窥镜",parent:"腔内手术用内窥镜"},{value:"脑电图机",label:"脑电图机",parent:"脑电诊断、治疗"},{value:"脑内窥镜",label:"脑内窥镜",parent:"腔内手术用内窥镜"},{value:"膀胱镜",label:"膀胱镜",parent:"腔内手术用内窥镜"},{value:"气腹管",label:"气腹管",parent:"光学内窥镜"},{value:"气管镜",label:"气管镜",parent:"腔内手术用内窥镜"},{value:"器械柜",label:"器械柜",parent:"器械台、柜"},{value:"器械台",label:"器械台",parent:"器械台、柜"},{value:"铅屏风",label:"铅屏风",parent:"医用防护类"},{value:"铅围裙",label:"铅围裙",parent:"医用防护类"},{value:"铅衣",label:"铅衣",parent:"医用防护类"},{value:"铅衣架",label:"铅衣架",parent:"医用防护类"},{value:"前列腺电切镜",label:"前列腺电切镜",parent:"医用高频设备"},{value:"前哨淋巴结跟踪仪",label:"前哨淋巴结跟踪仪",parent:"检验分析设备"},{value:"切割机",label:"切割机",parent:"手术设备"},{value:"清创车",label:"清创车",parent:"医用推车"},{value:"清洗喷枪",label:"清洗喷枪",parent:"病室护理"},{value:"热水器",label:"热水器",parent:"电器设备"},{value:"人体模型",label:"人体模型",parent:"病室护理"},{value:"乳腺活检系统",label:"乳腺活检系统",parent:"检验分析设备"},{value:"沙发",label:"沙发",parent:"沙发类"},{value:"射频消融仪",label:"射频消融仪",parent:"射频治疗设备"},{value:"摄像机",label:"摄像机",parent:"音视频设备"},{value:"摄像机臂",label:"摄像机臂",parent:"手术及急救装置"},{value:"医用剌激器",label:"医用剌激器",parent:"神经剌激器"},{value:"生理记录仪",label:"生理记录仪",parent:"脑电诊断、治疗"},{value:"生物培养器",label:"生物培养器",parent:"医用培养箱"},{value:"食道镜",label:"食道镜",parent:"腔内手术用内窥镜"},{value:"视网膜凝结机",label:"视网膜凝结机",parent:"眼科光学仪器"},{value:"手术床配件",label:"手术床配件",parent:"专用手术台床"},{value:"手术放大镜",label:"手术放大镜",parent:"手术显微镜及放大器"},{value:"手术体位固定板",label:"手术体位固定板",parent:"医用凳、椅、台、床"},{value:"手术头灯",label:"手术头灯",parent:"手术及急救装置"},{value:"手术头架",label:"手术头架",parent:"手术及急救装置"},{value:"手术显微镜",label:"手术显微镜",parent:"手术显微镜及放大器"},{value:"数据管理系统",label:"数据管理系统",parent:"其它治疗仪器"},{value:"数字视频脑电分析系统",label:"数字视频脑电分析系统",parent:"脑电诊断、治疗"},{value:"双频射频机",label:"双频射频机",parent:"射频治疗设备"},{value:"送物车",label:"送物车",parent:"医用推车"},{value:"碎石机",label:"碎石机",parent:"有创式电生理仪器"},{value:"台式消毒器（手术室）",label:"台式消毒器（手术室）",parent:"消毒灭菌设备"},{value:"头部固定系统",label:"头部固定系统",parent:"手术设备"},{value:"投影仪",label:"投影仪",parent:"信息设备"},{value:"图像记录仪",label:"图像记录仪",parent:"手术设备"},{value:"推车",label:"推车",parent:"病室护理"},{value:"托架",label:"托架",parent:"医用凳、椅、台、床"},{value:"微波治疗仪",label:"微波治疗仪",parent:"理疗康复仪器"},{value:"文件柜",label:"文件柜",parent:"柜类"},{value:"污物车",label:"污物车",parent:"医用推车"},{value:"无影灯",label:"无影灯",parent:"手术及急救装置"},{value:"吸脂系统",label:"吸脂系统",parent:"手术设备"},{value:"洗手槽",label:"洗手槽",parent:"器械台、柜"},{value:"纤维导光关节镜",label:"纤维导光关节镜",parent:"光学内窥镜"},{value:"显微镜",label:"显微镜",parent:"检验分析设备"},{value:"消毒柜",label:"消毒柜",parent:"器械台、柜"},{value:"鞋柜",label:"鞋柜",parent:"柜类"},{value:"胸腔镜",label:"胸腔镜",parent:"腔内手术用内窥镜"},{value:"血管闭合系统",label:"血管闭合系统",parent:"手术设备"},{value:"压力蒸汽灭菌器（包括供应、中药制剂）",label:"压力蒸汽灭菌器（包括供应、中药制剂）",parent:"消毒灭菌设备"},{value:"眼科A/B超",label:"眼科A/B超",parent:"眼科光学仪器"},{value:"药品柜",label:"药品柜",parent:"器械台、柜"},{value:"液体存放柜",label:"液体存放柜",parent:"器械台、柜"},{value:"液压手术台",label:"液压手术台",parent:"专用手术台床"},{value:"衣帽柜",label:"衣帽柜",parent:"柜类"},{value:"医疗垃圾车",label:"医疗垃圾车",parent:"医用推车"},{value:"医用车",label:"医用车",parent:"医用推车"},{value:"医用高能射线设备(直线加速器)",label:"医用高能射线设备(直线加速器)",parent:"医用高能射线治疗设备"},{value:"医用柜",label:"医用柜",parent:"医用推车"},{value:"仪器台车",label:"仪器台车",parent:"医用推车"},{value:"移动X光机",label:"移动X光机",parent:"X射线设备"},{value:"移动电话",label:"移动电话",parent:"通讯设备"},{value:"营养泵",label:"营养泵",parent:"输液辅助设备"},{value:"阅读器",label:"阅读器",parent:"基因生和命科学仪器"},{value:"照相机",label:"照相机",parent:"音视频设备"},{value:"支撑喉镜",label:"支撑喉镜",parent:"耳科仪器"},{value:"植皮机",label:"植皮机",parent:"医用高频设备"},{value:"纸张柜",label:"纸张柜",parent:"柜类"},{value:"治疗车",label:"治疗车",parent:"医用推车"},{value:"治疗工作台",label:"治疗工作台",parent:"器械台、柜"},{value:"周界防护系统",label:"周界防护系统",parent:"安保设备"},{value:"椎间盘镜",label:"椎间盘镜",parent:"手术及急救装置"}]},wkSW:function(a,e){}});
webpackJsonp([20],{GJwU:function(t,e){},o8h3:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("mvHQ"),s=a.n(i),c=a("Fd2+"),o=(a("FDxC"),a("4T1P"),a("2PSJ"),a("jydU"),a("wvM5"),a("MHRi"),a("Rv11"),a("D0HL"),a("tLo2"),a("GKy3"),a("uTM9"),a("9ULi"),a("i9vB"),a("wDsp")),n=a("A4Jg"),r=a("pFyW"),l=a("rYqN"),m=a("Uo+3"),u=a("m6KV"),d=a("JFKG"),h=a("mtaw"),f=a("zcOu"),p=a("W9Um"),D={name:"mobile_assets_modify",components:{Button:c.b,Tag:c.z,Icon:c.m,Field:c.l,Dialog:c.k,Picker:c.s,DatetimePicker:c.j,Popup:c.u,Toast:c.A,Loading:c.o,Search:c.x},props:{},data:function(){return{assetID:"",inputData:[],inputDataAll:[],inputSearchValue:"",activeKey:"",showInputSearch:!1,departCodeShow:!1,homeOfficeNumberShow:!1,isEntranceShow:!1,fstCatShow:!1,secCatShow:!1,thdCatShow:!1,fouCatShow:!1,fstCatData:h.b,secCatData:h.c,thdCatData:h.d,fouCatData:h.a,secCatList:[],thdCatList:[],fouCatList:[],departCode:m.a,homeOfficeNumber:u.a,imgData:{image:"",leftImage:"",allroundImage:"",aboveImage:"",rightImage:"",paperlabelImage:"",onecodelableImage:""},imgDataArr:d.a,minDate:new Date(2e3,0,1),maxDate:new Date(2050,12,0),address:this.$store.state.address,buyTimeDialogShow:!1,statusDialogShow:!1,architectureDialogShow:!1,academyDialogShow:!1,currentDate:new Date,createData:{assetID:"",generalName:"",assetName:"",location:"",specification:"",type:"",placeoforigin:"",brand:"",departmentcode:"",departmentroom:"",homeofficenumber:"",homeofficename:"",isentrance:"",suppliername:"",generatebusinessname:"",money:0,buyDate:"",locDept:"",status:"",recorder:this.$store.state.user,mac:"",amount:1,unit:"",applyoddnumbers:"",oneclassify:"",secondclassify:"",threeclassify:"",fourclassify:"",sn:"",architecture:"",academy:"",remark:""},statusData:[],importData:n.a,architectureData:r.a,academyData:l.a}},watch:{},computed:{},methods:{checkStatusFn:function(){"维修接单"!==this.createData.status&&"外借"!==this.createData.status?this.statusData=[{text:"正常",key:"正常"},{text:"待维修",key:"待维修"},{text:"待报废",key:"待报废"},{text:"报废",key:"报废"}]:this.statusData=[{text:"维修接单",key:"维修接单"},{text:"外借",key:"外借"}]},statusDialogFn:function(){"正常"!==this.createData.status&&"待维修"!==this.createData.status&&"待报废"!==this.createData.status&&"报废"!==this.createData.status||(this.statusDialogShow=!0)},imgDeleteFn:function(t,e){t.stopPropagation(),this.imgData[e]=""},imgPreviewFn:function(t){Object(c.n)({images:[this.imgData[t]]})},imgCom:function(t){return"background-image:url("+t+")"},addImgFn:function(t,e){var a=this;if(t.target.files.length){var i=t.target.files[0],s=new FileReader;s.readAsDataURL(i),s.onload=function(t){Object(p.a)(t.target.result,600,.9).then(function(t){a.imgData[e]=t})}}},certainSelectFn:function(t){this.createData[this.activeKey]=t,this.showInputSearch=!1,this.inputSearchValue="",this.activeKey=""},inputSelectChangeFn:function(t,e,a){this.createData[this.activeKey]=e},showInputSearchFn:function(t){this.showInputSearch=!0,this.activeKey=t,this.inputFocusFn(t)},handleSearchFn:function(t){this.inputData=this.inputDataAll.filter(function(e){return e.toLowerCase().indexOf(t.toLowerCase())>-1})},inputFocusFn:function(t){var e=this;this.inputDataAll=[],this.inputData=[];var a=[],i={};i[t]="1",a.push(i),this.$axios.post(o.a.inputQuery,a).then(function(a){a.data.data.forEach(function(a,i){e.inputDataAll.find(function(e){return e===a[t]})||e.inputDataAll.push(a[t])}),e.inputData=JSON.parse(s()(e.inputDataAll))}).catch(function(t){console.log(t)})},getDepartCodeFn:function(t){this.createData.departmentcode=t.key,this.createData.departmentroom=t.text,this.departCodeShow=!1},getHomeOfficeNumberFn:function(t){this.createData.homeofficenumber=t.key,this.createData.homeofficename=t.text,this.homeOfficeNumberShow=!1},getIsEntranceFn:function(t){this.createData.isentrance=t.text,this.isEntranceShow=!1},getFstCatFn:function(t){var e=this;this.createData.oneclassify=t.text,this.secCatList=this.secCatData.filter(function(t,a){return t.parent===e.createData.oneclassify}),this.createData.secondclassify="",this.createData.threeclassify="",this.createData.fourclassify="",this.fstCatShow=!1},getSecCatFn:function(t){var e=this;this.createData.secondclassify=t.text,this.thdCatList=this.thdCatData.filter(function(t,a){return t.parent===e.createData.secondclassify}),this.createData.threeclassify="",this.createData.fourclassify="",this.secCatShow=!1},getThdCatFn:function(t){var e=this;this.createData.threeclassify=t.text,this.fouCatList=this.fouCatData.filter(function(t,a){return t.parent===e.createData.threeclassify}),this.createData.fourclassify="",this.thdCatShow=!1},getFouCatFn:function(t){this.createData.fourclassify=t.text,this.fouCatShow=!1},getDateFn:function(t){var e=t,a=e.getFullYear(),i=e.getMonth()+1,s=e.getDate();i=i<10?"0"+i:i,s=s<10?"0"+s:s,this.createData.buyDate=a+"-"+i+"-"+s,this.buyTimeDialogShow=!1},getStatusFn:function(t){this.createData.status=t.text,this.statusDialogShow=!1},getArchitectureFn:function(t){this.createData.architecture=t.text,this.architectureDialogShow=!1},getAcademyFn:function(t){this.createData.academy=t.text,this.academyDialogShow=!1},saveFn:function(){var t=this;if(this.createData.assetID.trim()&&this.createData.status.trim()&&this.createData.recorder.trim())if(this.createData.mac.trim()&&!this.createData.architecture.trim())this.$notify({type:"warning",message:"若mac存在则建筑和院区必填",duration:2e3});else if(this.createData.mac.trim()&&!this.createData.academy.trim())this.$notify({type:"warning",message:"若mac存在则建筑和院区必填",duration:2e3});else{var e=c.A.loading({message:"正在更新...",forbidClick:!0,duration:0}),a={assetID:this.createData.assetID.trim(),image:this.imgData.image?this.imgData.image:"",leftImage:this.imgData.leftImage?this.imgData.leftImage:"",allroundImage:this.imgData.allroundImage?this.imgData.allroundImage:"",aboveImage:this.imgData.aboveImage?this.imgData.aboveImage:"",rightImage:this.imgData.rightImage?this.imgData.rightImage:"",paperlabelImage:this.imgData.paperlabelImage?this.imgData.paperlabelImage:"",onecodelableImage:this.imgData.onecodelableImage?this.imgData.onecodelableImage:""},i=[{assetID:this.createData.assetID.trim(),generalName:this.createData.generalName.trim(),assetName:this.createData.assetName.trim(),location:this.createData.location.trim(),specification:this.createData.specification.trim(),type:this.createData.type.trim(),placeoforigin:this.createData.placeoforigin.trim(),brand:this.createData.brand.trim(),departmentcode:this.createData.departmentcode,departmentroom:this.createData.departmentroom,homeofficenumber:this.createData.homeofficenumber,homeofficename:this.createData.homeofficename,isentrance:this.createData.isentrance.trim(),suppliername:this.createData.suppliername.trim(),generatebusinessname:this.createData.generatebusinessname.trim(),money:Math.abs(this.createData.money),buyDate:this.createData.buyDate,locDept:this.createData.locDept.trim(),status:this.createData.status.trim(),recorder:this.createData.recorder.trim(),mac:Object(f.a)(this.createData.mac.trim()),amount:Math.abs(this.createData.amount),unit:this.createData.unit,applyoddnumbers:this.createData.applyoddnumbers.trim(),oneclassify:this.createData.oneclassify,secondclassify:this.createData.secondclassify,threeclassify:this.createData.threeclassify,fourclassify:this.createData.fourclassify,sn:this.createData.sn.trim(),architecture:this.createData.architecture.trim(),academy:this.createData.academy.trim(),remark:this.createData.remark.trim(),address:this.address.trim()}];this.$axios.post(o.a.imgUpload,a).then(function(a){"ok"===a.data.msg?t.$axios.post(o.a.assetsUpdate,i).then(function(a){"ok"===a.data.msg?(e.clear(),c.A.success("更新成功！"),setTimeout(function(){t.$router.go(-1)},1e3)):a.data.message?(e.clear(),c.A.fail(a.data.message)):"failed"===a.data.msg&&(e.clear(),c.A.fail("更新失败！"))}).catch(function(t){e.clear(),c.A.fail("更新失败！")}):"failed"===a.data.msg&&(e.clear(),c.A.fail("图片更新失败！"))}).catch(function(t){})}else this.$notify({type:"warning",message:"请补充完整必填信息！",duration:2e3})},initCatFn:function(){var t=this;this.secCatList=this.secCatData.filter(function(e,a){return e.parent===t.createData.oneclassify}),this.thdCatList=this.thdCatData.filter(function(e,a){return e.parent===t.createData.secondclassify}),this.fouCatList=this.fouCatData.filter(function(e,a){return e.parent===t.createData.threeclassify})},getDataFn:function(){var t=this;this.$axios.post(o.a.assetsQuery+"?currentPage=1&assetID="+this.assetID).then(function(e){if(e.data[0].assetID){var a=e.data[0],i={assetID:a.assetID,generalName:a.generalName?a.generalName:"",assetName:a.assetName?a.assetName:"",location:a.location?a.location:"",specification:a.specification?a.specification:"",type:a.type?a.type:"",placeoforigin:a.placeoforigin?a.placeoforigin:"",brand:a.brand?a.brand:"",departmentcode:a.departmentcode?a.departmentcode:"",departmentroom:a.departmentroom?a.departmentroom:"",homeofficenumber:a.homeofficenumber?a.homeofficenumber:"",homeofficename:a.homeofficename?a.homeofficename:"",isentrance:a.isentrance?a.isentrance:"",suppliername:a.suppliername?a.suppliername:"",generatebusinessname:a.generatebusinessname?a.generatebusinessname:"",money:a.money?Number(a.money):0,buyDate:a.buyDate?a.buyDate:"",locDept:a.locDept?a.locDept:"",status:a.status?a.status:"",recorder:a.recorder?a.recorder:t.$store.state.user,mac:a.mac?a.mac:"",amount:a.amount?Number(a.amount):1,unit:a.unit?a.unit:"",applyoddnumbers:a.applyoddnumbers?a.applyoddnumbers:"",oneclassify:a.oneclassify?a.oneclassify:"",secondclassify:a.secondclassify?a.secondclassify:"",threeclassify:a.threeclassify?a.threeclassify:"",fourclassify:a.fourclassify?a.fourclassify:"",sn:a.sn?a.sn:"",architecture:a.architecture?a.architecture:"",academy:a.academy?a.academy:"",remark:a.remark?a.remark:""};t.buyTime=i.buyDate,t.createData=i,t.checkStatusFn(),t.initCatFn(),t.getImgFn("imgShow"),t.getImgFn("imgShowLeft"),t.getImgFn("imgShowAllround"),t.getImgFn("imgShowAbove"),t.getImgFn("imgShowRight"),t.getImgFn("imgShowPaperlabel"),t.getImgFn("imgShowOnecodelable")}}).catch(function(t){})},getImgFn:function(t){var e=this;this.$axios.get(o.a[t]+this.assetID,{responseType:"arraybuffer"}).then(function(a){var i;if("data:image/png;base64,"===(i="data:image/png;base64,"+btoa(new Uint8Array(a.data).reduce(function(t,e){return t+String.fromCharCode(e)},""))));else switch(t){case"imgShow":e.imgData.image=i;break;case"imgShowLeft":e.imgData.leftImage=i;break;case"imgShowAllround":e.imgData.allroundImage=i;break;case"imgShowAbove":e.imgData.aboveImage=i;break;case"imgShowRight":e.imgData.rightImage=i;break;case"imgShowPaperlabel":e.imgData.paperlabelImage=i;break;case"imgShowOnecodelable":e.imgData.onecodelableImage=i}}).catch(function(t){})}},created:function(){this.$emit("setTitle","资产修改"),this.assetID=this.$route.params.id},mounted:function(){this.getDataFn()}},b={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mobile_assets_modify_wrap"},[a("div",{staticClass:"assets_modify_box assets_upload_box"},[a("div",{staticClass:"cell_item cell_item_img_box"},[a("h5",[t._v("图片")]),t._v(" "),a("div",{staticClass:"cell_item_box"},[a("div",{staticClass:"upload_img_wrap"},t._l(t.imgDataArr,function(e,i){return a("div",{key:i,staticClass:"upload_img_box"},[a("div",{directives:[{name:"show",rawName:"v-show",value:!t.imgData[e.key],expression:"!imgData[item.key]"}],staticClass:"img_item_box"},[a("div",{staticClass:"upload_button_wrap"},[a("div",{staticClass:"add_img_icon"},[a("Icon",{attrs:{name:"plus"}})],1),t._v(" "),a("input",{staticClass:"add_img_input",attrs:{type:"file",accept:"image/*",multiple:"multiple"},on:{change:function(a){return t.addImgFn(a,e.key)}}})])]),t._v(" "),a("div",{directives:[{name:"show",rawName:"v-show",value:t.imgData[e.key],expression:"imgData[item.key]"}],staticClass:"img_item_box",style:t.imgCom(t.imgData[e.key]),on:{click:function(a){return t.imgPreviewFn(e.key)}}},[a("Icon",{staticClass:"img_delete_icon",attrs:{name:"clear"},on:{click:function(a){return t.imgDeleteFn(a,e.key)}}})],1),t._v(" "),a("p",[t._v(t._s(e.text))])])}),0)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{clearable:"",label:"资产编号",disabled:"",required:"",placeholder:"请输入资产编号"},model:{value:t.createData.assetID,callback:function(e){t.$set(t.createData,"assetID",e)},expression:"createData.assetID"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"资产名称",center:"",placeholder:"请输入资产名称"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("assetName")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.assetName,callback:function(e){t.$set(t.createData,"assetName",e)},expression:"createData.assetName"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"通用名称",center:"",placeholder:"请输入通用名称"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("generalName")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.generalName,callback:function(e){t.$set(t.createData,"generalName",e)},expression:"createData.generalName"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"存放地点",center:"",placeholder:"请输入存放地点"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("location")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.location,callback:function(e){t.$set(t.createData,"location",e)},expression:"createData.location"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"规格",center:"",placeholder:"请输入规格"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("specification")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.specification,callback:function(e){t.$set(t.createData,"specification",e)},expression:"createData.specification"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"型号",center:"",placeholder:"请输入型号"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("type")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.type,callback:function(e){t.$set(t.createData,"type",e)},expression:"createData.type"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"产地",center:"",placeholder:"请输入产地"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("placeoforigin")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.placeoforigin,callback:function(e){t.$set(t.createData,"placeoforigin",e)},expression:"createData.placeoforigin"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"品牌",center:"",placeholder:"请输入品牌"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("brand")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.brand,callback:function(e){t.$set(t.createData,"brand",e)},expression:"createData.brand"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.departCodeShow=!0}}},[a("Field",{attrs:{label:"部门名称",readonly:"",placeholder:"请选择部门名称"},model:{value:t.createData.departmentroom,callback:function(e){t.$set(t.createData,"departmentroom",e)},expression:"createData.departmentroom"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{disabled:"",label:"部门编码",readonly:"",placeholder:"请选择部门编码"},model:{value:t.createData.departmentcode,callback:function(e){t.$set(t.createData,"departmentcode",e)},expression:"createData.departmentcode"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.homeOfficeNumberShow=!0}}},[a("Field",{attrs:{label:"大科室名称",readonly:"",placeholder:"请选择大科室名称"},model:{value:t.createData.homeofficename,callback:function(e){t.$set(t.createData,"homeofficename",e)},expression:"createData.homeofficename"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{disabled:"",label:"大科室编码",readonly:"",placeholder:"请选择大科室编码"},model:{value:t.createData.homeofficenumber,callback:function(e){t.$set(t.createData,"homeofficenumber",e)},expression:"createData.homeofficenumber"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"所在科室名称",center:"",placeholder:"请输入所在科室名称"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("locDept")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.locDept,callback:function(e){t.$set(t.createData,"locDept",e)},expression:"createData.locDept"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"供应商名称",center:"",placeholder:"请输入供应商名称"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("suppliername")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.suppliername,callback:function(e){t.$set(t.createData,"suppliername",e)},expression:"createData.suppliername"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"生产厂商名称",center:"",placeholder:"请输入生产厂商名称"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("generatebusinessname")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.generatebusinessname,callback:function(e){t.$set(t.createData,"generatebusinessname",e)},expression:"createData.generatebusinessname"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.isEntranceShow=!0}}},[a("Field",{attrs:{label:"是否进口",readonly:"",placeholder:"请选择是否进口"},model:{value:t.createData.isentrance,callback:function(e){t.$set(t.createData,"isentrance",e)},expression:"createData.isentrance"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"原值",type:"number",placeholder:"请输入原值"},model:{value:t.createData.money,callback:function(e){t.$set(t.createData,"money",e)},expression:"createData.money"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.buyTimeDialogShow=!0}}},[a("Field",{attrs:{label:"入库时间",readonly:"",placeholder:"请选择入库时间"},model:{value:t.createData.buyDate,callback:function(e){t.$set(t.createData,"buyDate",e)},expression:"createData.buyDate"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:t.statusDialogFn}},[a("Field",{attrs:{label:"状态",required:"",readonly:"",placeholder:"请选择状态"},model:{value:t.createData.status,callback:function(e){t.$set(t.createData,"status",e)},expression:"createData.status"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"录入人员",required:"",placeholder:"请输入录入人员"},model:{value:t.createData.recorder,callback:function(e){t.$set(t.createData,"recorder",e)},expression:"createData.recorder"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.fstCatShow=!0}}},[a("Field",{attrs:{label:"一级分类",readonly:"",placeholder:"请选择一级分类"},model:{value:t.createData.oneclassify,callback:function(e){t.$set(t.createData,"oneclassify",e)},expression:"createData.oneclassify"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.secCatShow=!0}}},[a("Field",{attrs:{label:"二级分类",readonly:"",placeholder:"请选择二级分类"},model:{value:t.createData.secondclassify,callback:function(e){t.$set(t.createData,"secondclassify",e)},expression:"createData.secondclassify"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.thdCatShow=!0}}},[a("Field",{attrs:{label:"三级分类",readonly:"",placeholder:"请选择三级分类"},model:{value:t.createData.threeclassify,callback:function(e){t.$set(t.createData,"threeclassify",e)},expression:"createData.threeclassify"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.fouCatShow=!0}}},[a("Field",{attrs:{label:"四级分类",readonly:"",placeholder:"请选择四级分类"},model:{value:t.createData.fourclassify,callback:function(e){t.$set(t.createData,"fourclassify",e)},expression:"createData.fourclassify"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"数量",type:"number",placeholder:"请输入数量"},model:{value:t.createData.amount,callback:function(e){t.$set(t.createData,"amount",e)},expression:"createData.amount"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"单位",center:"",placeholder:"请选择单位"},scopedSlots:t._u([{key:"button",fn:function(){return[a("Button",{attrs:{size:"small",type:"primary"},on:{click:function(e){return t.showInputSearchFn("unit")}}},[t._v("云数据")])]},proxy:!0}]),model:{value:t.createData.unit,callback:function(e){t.$set(t.createData,"unit",e)},expression:"createData.unit"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"处置申请单号",placeholder:"请输入处置申请单号"},model:{value:t.createData.applyoddnumbers,callback:function(e){t.$set(t.createData,"applyoddnumbers",e)},expression:"createData.applyoddnumbers"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"标签mac",placeholder:"格式:ac233fa031fa"},model:{value:t.createData.mac,callback:function(e){t.$set(t.createData,"mac",e)},expression:"createData.mac"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"SN",placeholder:"请输入SN号"},model:{value:t.createData.sn,callback:function(e){t.$set(t.createData,"sn",e)},expression:"createData.sn"}})],1)]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.architectureDialogShow=!0}}},[a("Field",{attrs:{label:"建筑（若mac存在则必填）",required:"",readonly:"",placeholder:"请选择建筑"},model:{value:t.createData.architecture,callback:function(e){t.$set(t.createData,"architecture",e)},expression:"createData.architecture"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("div",{on:{click:function(e){t.academyDialogShow=!0}}},[a("Field",{attrs:{label:"院区（若mac存在则必填）",required:"",readonly:"",placeholder:"请选择院区"},model:{value:t.createData.academy,callback:function(e){t.$set(t.createData,"academy",e)},expression:"createData.academy"}})],1)])]),t._v(" "),a("div",{staticClass:"cell_item"},[a("div",{staticClass:"cell_item_box"},[a("Field",{attrs:{label:"备注信息",type:"textarea",placeholder:"请输入备注信息"},model:{value:t.createData.remark,callback:function(e){t.$set(t.createData,"remark",e)},expression:"createData.remark"}})],1)]),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.showInputSearch,callback:function(e){t.showInputSearch=e},expression:"showInputSearch"}},[a("Search",{attrs:{placeholder:"请输入搜索关键词","input-align":"center"},on:{input:t.handleSearchFn},model:{value:t.inputSearchValue,callback:function(e){t.inputSearchValue=e},expression:"inputSearchValue"}}),t._v(" "),a("Picker",{ref:"inputSelect",attrs:{"show-toolbar":"","toolbar-position":"bottom",columns:t.inputData,title:"","visible-item-count":5},on:{cancel:function(e){t.showInputSearch=!1},confirm:t.certainSelectFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.departCodeShow,callback:function(e){t.departCodeShow=e},expression:"departCodeShow"}},[a("Picker",{attrs:{columns:t.departCode,"show-toolbar":"",title:"请选择部门名称","visible-item-count":5},on:{cancel:function(e){t.departCodeShow=!1},confirm:t.getDepartCodeFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.homeOfficeNumberShow,callback:function(e){t.homeOfficeNumberShow=e},expression:"homeOfficeNumberShow"}},[a("Picker",{attrs:{columns:t.homeOfficeNumber,"show-toolbar":"",title:"请选择大科室名称","visible-item-count":5},on:{cancel:function(e){t.homeOfficeNumberShow=!1},confirm:t.getHomeOfficeNumberFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.isEntranceShow,callback:function(e){t.isEntranceShow=e},expression:"isEntranceShow"}},[a("Picker",{attrs:{columns:t.importData,"show-toolbar":"",title:"请选择是否进口","visible-item-count":5},on:{cancel:function(e){t.isEntranceShow=!1},confirm:t.getIsEntranceFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.fstCatShow,callback:function(e){t.fstCatShow=e},expression:"fstCatShow"}},[a("Picker",{attrs:{columns:t.fstCatData,"show-toolbar":"",title:"请选择一级分类","visible-item-count":5},on:{cancel:function(e){t.fstCatShow=!1},confirm:t.getFstCatFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.secCatShow,callback:function(e){t.secCatShow=e},expression:"secCatShow"}},[a("Picker",{attrs:{columns:t.secCatList,"show-toolbar":"",title:"请选择二级分类","visible-item-count":5},on:{cancel:function(e){t.secCatShow=!1},confirm:t.getSecCatFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.thdCatShow,callback:function(e){t.thdCatShow=e},expression:"thdCatShow"}},[a("Picker",{attrs:{columns:t.thdCatList,"show-toolbar":"",title:"请选择三级分类","visible-item-count":5},on:{cancel:function(e){t.thdCatShow=!1},confirm:t.getThdCatFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.fouCatShow,callback:function(e){t.fouCatShow=e},expression:"fouCatShow"}},[a("Picker",{attrs:{columns:t.fouCatList,"show-toolbar":"",title:"请选择四级分类","visible-item-count":5},on:{cancel:function(e){t.fouCatShow=!1},confirm:t.getFouCatFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.statusDialogShow,callback:function(e){t.statusDialogShow=e},expression:"statusDialogShow"}},[a("Picker",{attrs:{columns:t.statusData,"show-toolbar":"",title:"请选择状态","visible-item-count":5},on:{cancel:function(e){t.statusDialogShow=!1},confirm:t.getStatusFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.architectureDialogShow,callback:function(e){t.architectureDialogShow=e},expression:"architectureDialogShow"}},[a("Picker",{attrs:{columns:t.architectureData,"show-toolbar":"",title:"请选择建筑","visible-item-count":5},on:{cancel:function(e){t.architectureDialogShow=!1},confirm:t.getArchitectureFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.academyDialogShow,callback:function(e){t.academyDialogShow=e},expression:"academyDialogShow"}},[a("Picker",{attrs:{columns:t.academyData,"show-toolbar":"",title:"请选择院区","visible-item-count":5},on:{cancel:function(e){t.academyDialogShow=!1},confirm:t.getAcademyFn}})],1),t._v(" "),a("Popup",{attrs:{position:"bottom"},model:{value:t.buyTimeDialogShow,callback:function(e){t.buyTimeDialogShow=e},expression:"buyTimeDialogShow"}},[a("DatetimePicker",{attrs:{type:"date","show-toolbar":"",title:"请选择入库时间","min-date":t.minDate,"max-date":t.maxDate},on:{cancel:function(e){t.buyTimeDialogShow=!1},confirm:t.getDateFn},model:{value:t.currentDate,callback:function(e){t.currentDate=e},expression:"currentDate"}})],1),t._v(" "),a("div",{staticClass:"tools_wrap"},[a("Button",{staticClass:"tools_button",attrs:{round:"",type:"primary"},on:{click:t.saveFn}},[t._v("保存")])],1)],1)])},staticRenderFns:[]};var v=a("VU/8")(D,b,!1,function(t){a("GJwU")},"data-v-f99c9bd4",null);e.default=v.exports}});
webpackJsonp([27],{"2udr":function(a,t){},QhYe:function(a,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=e("wdqF"),i=e("8sRp"),s=e("KxKW"),r=e("JtJ0"),n=e("HYs4"),l=e("NHTL"),c=e("yoLw"),d=e("NAz/"),m=e("a84E"),p=e("EMb8"),u=e("7+uW"),h=e("tpLV"),v=e("Gaqz"),f=e("wDsp"),y=e("zcOu");u.a.component("Icon",p.a),u.a.component("Row",m.a),u.a.component("Col",d.a),u.a.component("Table",c.a),u.a.component("Tag",l.a),u.a.component("Page",n.a),u.a.component("Input",r.a),u.a.component("Button",s.a),u.a.component("Modal",i.a),u.a.component("Spin",o.a);var _={name:"gateway_detail",components:{},props:{},data:function(){return{showMap:!1,editData:{gatewaymac:"",ipaddress:"",floor:"11",department:"",location:"",mapx:"",mapy:"",cadMapRoomName:"",roomnumber:""},baseLayers:null,homeLayers:null,deviceMarkers:[],devicePopups:[],map:null,currentFloor:"11",zoomKey:null,uploadModal:!1,achieveUpload:!1,errorUpload:!1,currentRoom:{geoJson:null,layer:null},selectRoom:{},geojsonArr:[]}},watch:{currentFloor:function(){this.editData.floor=this.currentFloor}},computed:{},methods:{initMapFn:function(){this.homeLayers=new L.featureGroup,this.baseLayers=new L.featureGroup,this.markers=[];var a=new L.Proj.CRS("EPSG:4549","+proj=tmerc +lat_0=0 +lon_0=120 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs",{resolutions:[156367.7919628329,78183.89598141646,39091.94799070823,19545.973995354114,9772.986997677057,4886.4934988385285,2443.2467494192642,1221.6233747096321,610.8116873548161,305.40584367740803,152.70292183870401,76.35146091935201,38.175730459676004,19.087865229838002,9.543932614919001,4.7719663074595005,2.3859831537297502,1.1929915768648751,.5964957884324376,.2982478942162188]}),t=L.latLngBounds(L.latLng(-4.279435,140.075537),L.latLng(-5.438369,138.29733));if(this.map=L.map("mapID",{center:[-4.9002,139.2102],layers:[this.homeLayers,this.baseLayers],zoom:10,maxZoom:14,minZoom:10,attributionControl:!1,doubleClickZoom:!1,maxBounds:t,crs:a}),this.gatewayData){switch(this.gatewayData.floor){case"11":this.initBoundaryFn(h.baseData1),this.initHomeLayerFn(h.homeData11);break;case"12":this.initBoundaryFn(h.baseData1),this.initHomeLayerFn(h.homeData12);break;case"13":this.initBoundaryFn(h.baseData1),this.initHomeLayerFn(h.homeData13);break;case"b1":this.initBoundaryFn(h.baseData2),this.initHomeLayerFn(h.homeDataB1);break;case"b2":this.initBoundaryFn(h.baseData3),this.initHomeLayerFn(h.homeDataB2);break;case"t1":this.initBoundaryFn(h.baseData4),this.initHomeLayerFn(h.homeDataT1)}this.detailMarkerFn(this.gatewayData)}else this.initBoundaryFn(h.baseData1),this.initHomeLayerFn(h.homeData11);this.switchFloorFn(),this.initZoomFn(),this.changeZoomFn()},initMarkerFn:function(a){this.removeDeviceMarkerFn(),this.editData.mapx="",this.editData.mapy="";var t=L.marker(a.latlng,{icon:L.icon({iconUrl:e("xnra"),iconSize:[20]})}).addTo(this.map);this.deviceMarkers.push(t),this.editData.mapx=a.latlng.lat.toFixed(6),this.editData.mapy=a.latlng.lng.toFixed(6)},initZoomFn:function(){var a=this;this.map.addEventListener("zoomend",function(t){a.changeZoomFn()})},changeZoomFn:function(){var a=document.getElementsByClassName("showNAME"),t=this.map.getZoom();if(t<11)for(var e=0;e<a.length;e++)a[e].style.display="none",a[e].parentNode.style.display="none";else for(var o=0;o<a.length;o++)a[o].style.display="block",a[o].parentNode.style.display="block",a[o].style.fontSize=t+2+"px"},initBoundaryFn:function(a){L.Proj.geoJson(a,{style:function(a){return{color:"#ddd",weight:5,opacity:1,fillOpacity:1}}}).addTo(this.baseLayers)},detailRoomFn:function(a,t){var e=this,o=L.Proj.geoJson(a,{style:function(a){return{color:"#fff",opacity:1,weight:2,fillColor:t,fillOpacity:1}}}).addTo(this.homeLayers),i=o.getBounds(),s=(i._northEast.lat-i._southWest.lat)/2+i._southWest.lat,r=(i._northEast.lng-i._southWest.lng)/2+i._southWest.lng,n='<div class="showNAME">'+a.properties.NAME+"</div>";L.marker([s,r],{icon:L.divIcon({className:"leaflet-echart-icon",html:n}),pro:a.properties}).addTo(this.homeLayers);this.geojsonArr.push({$geojson:o,NO:a.properties.NO}),o.addEventListener("click",function(a){e.initMarkerFn(a),e.currentRoom.layer&&e.currentRoom.geoJson&&e.currentRoom.geoJson.resetStyle(e.currentRoom.layer),e.currentRoom.layer=a.layer,e.currentRoom.geoJson=o,e.selectRoom=a.layer.feature.properties,e.editData.cadMapRoomName=e.selectRoom.NAME,e.editData.roomnumber=e.selectRoom.NO,o.setStyle({color:"#FFFFFF",opacity:1,weight:2,fillColor:"#ffd100",fillOpacity:1})})},initHomeLayerFn:function(a){var t=this;L.Proj.geoJson(a,{onEachFeature:function(a){a.crs={type:"name",properties:{name:"EPSG:4549"}};var e=t.initColorFn(a.properties.TYPE);t.detailRoomFn(a,e)}})},initColorFn:function(a){return a?["#f7f0c5","#f7bbdf","#aeccfd","#88c7f9","#c3efc0","#c9b8ff","#ccc"][a-1]:"#ddd"},switchFloorFn:function(){var a=this;document.getElementById("moniterItem").addEventListener("click",function(t){if("li"===t.target.tagName.toLowerCase()){var e=t.target.getAttribute("data-value");if(e!==a.currentFloor){for(var o=document.getElementById("moniterItem").getElementsByTagName("li"),i=0;i<o.length;i++)o[i].classList.remove("active");t.target.classList.add("active");var s=null,r=null;switch(e){case"11":s=h.baseData1,r=h.homeData11;break;case"12":s=h.baseData1,r=h.homeData12;break;case"13":s=h.baseData1,r=h.homeData13;break;case"b1":s=h.baseData2,r=h.homeDataB1;break;case"b2":s=h.baseData3,r=h.homeDataB2;break;case"t1":s=h.baseData4,r=h.homeDataT1}a.currentFloor=e,a.clearAllDataFn(),a.initBoundaryFn(s),a.initHomeLayerFn(r),a.changeZoomFn()}}})},detailMarkerFn:function(a){var t=L.marker(a.posi,{icon:L.icon({iconUrl:e("xnra"),iconSize:[20]})}).addTo(this.map);this.deviceMarkers.push(t)},removeDeviceMarkerFn:function(){for(var a=0;a<this.deviceMarkers.length;a++)this.map.removeLayer(this.deviceMarkers[a]);for(var t=0;t<this.devicePopups.length;t++)this.map.removeLayer(this.devicePopups[t]);this.devicePopups=[],this.deviceMarkers=[]},removeRoomFn:function(){this.homeLayers.clearLayers()},removeBoundaryFn:function(){this.baseLayers.clearLayers()},clearAllDataFn:function(){this.removeBoundaryFn(),this.removeRoomFn(),this.removeDeviceMarkerFn(),this.editData.mapx="",this.editData.mapy="",this.editData.cadMapRoomName="",this.editData.roomnumber=""},successFn:function(){var a=this;this.achieveUpload=!0,setTimeout(function(){a.uploadModal=!1,setTimeout(function(){a.achieveUpload=!1,a.errorUpload=!1,a.$router.go(-1)},500)},1e3)},errorFn:function(){var a=this;this.errorUpload=!0,setTimeout(function(){a.uploadModal=!1,setTimeout(function(){a.achieveUpload=!1,a.errorUpload=!1},500)},1e3)},editFn:function(){var a=this;if(""===this.editData.gatewaymac.trim()||""===this.editData.floor||""===this.editData.mapx||""===this.editData.mapy||""===this.editData.location.trim()||""===this.editData.cadMapRoomName||""===this.editData.roomnumber.trim())this.$Modal.error({title:"错误",content:"请输入完整信息！"});else{this.uploadModal=!0;var t={gatewaymac:Object(y.a)(this.editData.gatewaymac.trim()),ipaddress:this.editData.ipaddress.trim(),floor:this.editData.floor,department:this.editData.department.trim(),location:this.editData.location.trim(),mapx:this.editData.mapx,mapy:this.editData.mapy,cadMapRoomName:this.editData.cadMapRoomName.trim(),roomnumber:this.editData.roomnumber};this.$axios.post(f.a.gatewayUpdate,t).then(function(t){0===t.data.code?a.successFn():(a.errorFn(),a.$Message.error({content:t.data.msg,duration:2}))}).catch(function(t){a.errorFn()})}},initRoomHighLightFn:function(){var a=this;this.geojsonArr.forEach(function(t,e){if(t.NO===a.editData.roomnumber){t.$geojson.setStyle({color:"#FFFFFF",opacity:1,weight:2,fillColor:"#ffd100",fillOpacity:1});var o=[];for(var i in t.$geojson._layers)o.push(t.$geojson._layers[i]);a.currentRoom.layer=o[0],a.currentRoom.geoJson=t.$geojson}})},getDataFn:function(){var a=this;if(window.sessionStorage.getItem("detailData")){var t=JSON.parse(window.sessionStorage.getItem("detailData"));if(this.editData={gatewaymac:t.gatewaymac,ipaddress:t.ipaddress?t.ipaddress:"",floor:t.floor?t.floor:"11",department:t.department?t.department:"",location:t.location?t.location:"",mapx:t.mapx?t.mapx:"",mapy:t.mapy?t.mapy:"",cadMapRoomName:t.cadMapRoomName?t.cadMapRoomName:"",roomnumber:t.roomnumber?t.roomnumber:""},t.floor&&t.mapx&&t.mapy&&t.cadMapRoomName&&t.roomnumber){this.currentFloor=t.floor,this.gatewayData={floor:t.floor,posi:[t.mapx,t.mapy]};var e=document.getElementById("moniterItem").getElementsByTagName("li");e[0].classList.remove("active");var o=0;switch(t.floor){case"11":o=0;break;case"12":o=1;break;case"13":o=2;break;case"b1":o=3;break;case"b2":o=4;break;case"t1":o=5}e[o].classList.add("active")}else this.currentFloor="11"}else this.$Message.error({content:"未找到网关数据，将返回上一页！",duration:2}),setTimeout(function(){a.$router.go(-1)},2e3)}},detaild:function(){},mounted:function(){var a=this;this.$nextTick(function(){Object(v.b)().then(function(){Object(v.e)().then(function(){Object(v.f)().then(function(){a.getDataFn(),a.showMap=!0,a.$nextTick(function(){a.initMapFn(),a.initRoomHighLightFn()})})})})})},beforeDestroy:function(){window.sessionStorage.removeItem("detailData")}},D={render:function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("div",{staticClass:"gateway_detail_wrap"},[e("div",{staticClass:"row_box hover_animat custom_bg_color_white"},[e("h2",{staticClass:"row_title"},[a._v("地图")]),a._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:!a.showMap,expression:"!showMap"}],staticClass:"map_loading_notice"},[e("h3",[a._v("地图加载中...")])]),a._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:a.showMap,expression:"showMap"}],attrs:{id:"mapID"}},[a._m(0)])]),a._v(" "),e("div",{staticClass:"row_wrap hover_animat custom_bg_color_white"},[e("div",{staticClass:"row_box"},[a._m(1),a._v(" "),e("div",{staticClass:"form_wrap"},[e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("网关mac地址")]),a._v(" "),e("Input",{attrs:{disabled:"",placeholder:"mac地址（格式:AC233FA031FA）"},model:{value:a.editData.gatewaymac,callback:function(t){a.$set(a.editData,"gatewaymac",t)},expression:"editData.gatewaymac"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("IP地址（可不填）")]),a._v(" "),e("Input",{attrs:{placeholder:"输入IP地址"},model:{value:a.editData.ipaddress,callback:function(t){a.$set(a.editData,"ipaddress",t)},expression:"editData.ipaddress"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("所属科室")]),a._v(" "),e("Input",{attrs:{placeholder:"所属科室"},model:{value:a.editData.department,callback:function(t){a.$set(a.editData,"department",t)},expression:"editData.department"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("所处位置")]),a._v(" "),e("Input",{attrs:{placeholder:"输入所处位置"},model:{value:a.editData.location,callback:function(t){a.$set(a.editData,"location",t)},expression:"editData.location"}})],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("房间名")]),a._v(" "),e("Input",{attrs:{disabled:""},model:{value:a.editData.cadMapRoomName,callback:function(t){a.$set(a.editData,"cadMapRoomName",t)},expression:"editData.cadMapRoomName"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("所在楼层")]),a._v(" "),e("Input",{attrs:{disabled:""},model:{value:a.editData.floor,callback:function(t){a.$set(a.editData,"floor",t)},expression:"editData.floor"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("坐标位置x")]),a._v(" "),e("Input",{attrs:{disabled:""},model:{value:a.editData.mapx,callback:function(t){a.$set(a.editData,"mapx",t)},expression:"editData.mapx"}})],1),a._v(" "),e("Col",{attrs:{span:"6"}},[e("h3",[a._v("坐标位置y")]),a._v(" "),e("Input",{attrs:{disabled:""},model:{value:a.editData.mapy,callback:function(t){a.$set(a.editData,"mapy",t)},expression:"editData.mapy"}})],1)],1)],1),a._v(" "),e("div",{staticClass:"form_item"},[e("Row",{attrs:{gutter:20}},[e("Col",{attrs:{span:"6"}},[e("h3",[a._v("房间编号")]),a._v(" "),e("Input",{attrs:{disabled:""},model:{value:a.editData.roomnumber,callback:function(t){a.$set(a.editData,"roomnumber",t)},expression:"editData.roomnumber"}})],1)],1)],1)]),a._v(" "),e("div",{staticClass:"submit_btn_wrap"},[e("Button",{attrs:{type:"primary",shape:"circle"},on:{click:a.editFn}},[a._v("立即保存")])],1)])]),a._v(" "),e("Modal",{attrs:{title:"","footer-hide":!0,"mask-closable":!1,closable:!1,width:"360"},model:{value:a.uploadModal,callback:function(t){a.uploadModal=t},expression:"uploadModal"}},[e("div",{staticStyle:{"text-align":"center",padding:"20px 0"}},[a.achieveUpload||a.errorUpload?a._e():e("Spin",{attrs:{fix:""}},[e("Icon",{staticClass:"loding_icon",attrs:{type:"ios-loading",size:"18"}}),a._v(" "),e("div",[a._v("正在更新")])],1),a._v(" "),a.achieveUpload?e("Spin",{staticStyle:{color:"#00ad19"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-checkmark-circle",size:"18"}}),a._v(" "),e("div",[a._v("更新成功")])],1):a._e(),a._v(" "),a.errorUpload?e("Spin",{staticStyle:{color:"#f72b2b"},attrs:{fix:""}},[e("Icon",{attrs:{type:"ios-close-circle",size:"18"}}),a._v(" "),e("div",[a._v("更新失败")])],1):a._e()],1)])],1)},staticRenderFns:[function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("div",{attrs:{id:"moniter"}},[e("ul",[e("li",{staticClass:"title"},[a._v("楼层选择")])]),a._v(" "),e("ul",{attrs:{id:"moniterItem"}},[e("li",{staticClass:"active",attrs:{"data-value":"11"}},[a._v("十一层")]),a._v(" "),e("li",{attrs:{"data-value":"12"}},[a._v("十二层")]),a._v(" "),e("li",{attrs:{"data-value":"13"}},[a._v("十三层")]),a._v(" "),e("li",{attrs:{"data-value":"b1"}},[a._v("医工科")]),a._v(" "),e("li",{attrs:{"data-value":"b2"}},[a._v("报废库")]),a._v(" "),e("li",{attrs:{"data-value":"t1"}},[a._v("办公室")])])])},function(){var a=this.$createElement,t=this._self._c||a;return t("h2",{staticClass:"row_title"},[this._v("网关信息"),t("span",{staticStyle:{"font-size":"14px"}},[this._v("（在地图上选择点位后会自动填充位置信息到下方输入框中）")])])}]};var b=e("VU/8")(_,D,!1,function(a){e("2udr")},"data-v-7f9626b6",null);t.default=b.exports}});
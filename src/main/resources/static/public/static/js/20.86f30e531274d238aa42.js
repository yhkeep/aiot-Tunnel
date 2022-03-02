webpackJsonp([20],{"8jGS":function(e,t){},D6S8:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=a("bOdI"),i=a.n(s),o=a("KxKW"),n=a("NHTL"),r=a("EMb8"),c=a("7+uW"),l=a("tpLV"),v=a("Gaqz");a("wDsp"),a("TIfe");c.a.component("Icon",r.a),c.a.component("Tag",n.a),c.a.component("Button",o.a);var m={name:"rail",components:{},props:{},data:function(){return{showMap:!1,deviceMessage:[],websock:null,homeLayers:null,baseLayers:null,deviceMarkers:[],devicePopups:[],map:null,currentFloor:"11",zoomKey:null}},watch:{deviceMessageArr:function(){this.deviceMessage=this.deviceMessageArr}},computed:{deviceMessageArr:function(){return this.$store.state.deviceMessageArr}},methods:{initMapFn:function(){this.homeLayers=new L.featureGroup,this.baseLayers=new L.featureGroup,this.markers=[];var e=new L.Proj.CRS("EPSG:4549","+proj=tmerc +lat_0=0 +lon_0=120 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs",{resolutions:[156367.7919628329,78183.89598141646,39091.94799070823,19545.973995354114,9772.986997677057,4886.4934988385285,2443.2467494192642,1221.6233747096321,610.8116873548161,305.40584367740803,152.70292183870401,76.35146091935201,38.175730459676004,19.087865229838002,9.543932614919001,4.7719663074595005,2.3859831537297502,1.1929915768648751,.5964957884324376,.2982478942162188]}),t=L.latLngBounds(L.latLng(-4.279435,140.075537),L.latLng(-5.438369,138.29733));this.map=L.map("mapID",{center:[-4.9002,139.2102],layers:[this.homeLayers,this.baseLayers],zoom:10,maxZoom:14,minZoom:10,attributionControl:!1,doubleClickZoom:!1,maxBounds:t,crs:e}),this.initBoundaryFn(l.baseData1),this.initHomeLayerFn(l.homeData11),this.switchFloorFn(),this.initZoomFn()},initZoomFn:function(){var e=this;this.changeZoomFn(),this.map.addEventListener("zoomend",function(t){e.changeZoomFn()})},changeZoomFn:function(){var e=document.getElementsByClassName("showNAME"),t=this.map.getZoom();if(t<11)for(var a=0;a<e.length;a++)e[a].style.display="none",e[a].parentNode.style.display="none";else for(var s=0;s<e.length;s++)e[s].style.display="block",e[s].parentNode.style.display="block",e[s].style.fontSize=t+2+"px"},initBoundaryFn:function(e){L.Proj.geoJson(e,{style:function(e){return i()({color:"#e46cbb",weight:6,opacity:1,fillOpacity:1,fillColor:"#ddd"},"fillOpacity",1)}}).addTo(this.baseLayers)},createRoomFn:function(e,t){var a=L.Proj.geoJson(e,{style:function(e){return{color:"#fff",opacity:1,weight:2,fillColor:t,fillOpacity:1}}}).addTo(this.homeLayers).getBounds(),s=(a._northEast.lat-a._southWest.lat)/2+a._southWest.lat,i=(a._northEast.lng-a._southWest.lng)/2+a._southWest.lng,o='<div class="showNAME">'+e.properties.NAME+"</div>";L.marker([s,i],{icon:L.divIcon({className:"leaflet-echart-icon",html:o}),pro:e.properties}).addTo(this.homeLayers)},initHomeLayerFn:function(e){var t=this;L.Proj.geoJson(e,{onEachFeature:function(e){e.crs={type:"name",properties:{name:"EPSG:4549"}};var a=t.initColorFn(e.properties.TYPE);t.createRoomFn(e,a)}})},initColorFn:function(e){return e?["#f7f0c5","#f7bbdf","#aeccfd","#88c7f9","#c3efc0","#c9b8ff","#ccc"][e-1]:"#ddd"},switchFloorFn:function(){var e=this;document.getElementById("moniterItem").addEventListener("click",function(t){var a=t.target.getAttribute("data-value");if(a!==e.currentFloor){for(var s=document.getElementById("moniterItem").getElementsByTagName("li"),i=0;i<s.length;i++)s[i].classList.remove("active");t.target.classList.add("active");var o=null,n=null;switch(a){case"11":o=l.baseData1,n=l.homeData11;break;case"12":o=l.baseData1,n=l.homeData12;break;case"13":o=l.baseData1,n=l.homeData13;break;case"b1":o=l.baseData2,n=l.homeDataB1;break;case"b2":o=l.baseData3,n=l.homeDataB2}e.currentFloor=a,e.clearAllDataFn(),e.initBoundaryFn(o),e.initHomeLayerFn(n),e.changeZoomFn()}})},removeDeviceMarkerFn:function(){for(var e=0;e<this.deviceMarkers.length;e++)this.map.removeLayer(this.deviceMarkers[e]);for(var t=0;t<this.devicePopups.length;t++)this.map.removeLayer(this.devicePopups[t]);this.devicePopups=[],this.deviceMarkers=[]},removeRoomFn:function(){this.homeLayers.clearLayers()},removeBoundaryFn:function(){this.baseLayers.clearLayers()},clearAllDataFn:function(){this.removeBoundaryFn(),this.removeRoomFn(),this.removeDeviceMarkerFn()}},created:function(){},mounted:function(){var e=this;this.$nextTick(function(){Object(v.b)().then(function(){Object(v.c)().then(function(){Object(v.d)().then(function(){e.showMap=!0,e.$nextTick(function(){e.initMapFn()})})})})})},beforeDestroy:function(){}},d={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"rail_wrap"},[a("div",{staticClass:"custom_flex_wrap"},[a("div",{staticClass:"custom_flex_item_2 hover_animat custom_bg_color_white row_box"},[a("h2",{staticClass:"row_title"},[e._v("围栏地图")]),e._v(" "),a("div",{staticClass:"data_block",attrs:{id:"map_box"}},[a("div",{directives:[{name:"show",rawName:"v-show",value:!e.showMap,expression:"!showMap"}],staticClass:"map_loading_notice"},[a("h3",[e._v("地图加载中...")])]),e._v(" "),a("div",{directives:[{name:"show",rawName:"v-show",value:e.showMap,expression:"showMap"}],attrs:{id:"mapID"}},[e._m(0)])])]),e._v(" "),a("div",{staticClass:"custom_flex_item_2 hover_animat custom_bg_color_white row_box"},[a("h2",{staticClass:"row_title"},[e._v("设备动态")]),e._v(" "),a("div",{staticClass:"device_message_wrap"},[e.deviceMessage.length<=0?a("h3",[e._v("暂无动态...")]):a("div",{staticClass:"device_message_scroll_wrap"},[a("div",{staticClass:"device_message_scroll_box"},e._l(e.deviceMessage,function(t,s){return a("div",{key:s,staticClass:"device_message_item"},[a("div",{staticClass:"device_message_item_left"},[a("div",{staticClass:"device_message_icon_box"},[a("Icon",{attrs:{type:"ios-warning"}})],1)]),e._v(" "),a("div",{staticClass:"device_message_item_right"},[a("div",{staticClass:"device_message_item_right_top"},[a("span",[e._v(e._s(s+1)+" - ")]),a("span",[e._v(e._s("null"===t.brand?"--":t.brand))]),e._v(" "),a("b",{staticStyle:{color:"#e46cbb"}},[e._v(e._s("null"===t.assetName?"--":t.assetName))]),e._v(" "),a("span",[e._v(e._s("null"===t.type?"--":t.type))]),e._v(" - "),a("span",[e._v(e._s("null"===t.assetID?"--":t.assetID))]),e._v(" - "),a("span",[e._v("超出合法范围")])]),e._v(" "),a("div",{staticClass:"device_message_item_right_bottom"},[e._v("\n                                          最近更新时间："+e._s("null"===t.updatetime?"--":t.updatetime)+" / 房间名："+e._s("null"===t.cadMapRoomName?"--":t.cadMapRoomName)+"\n                                      ")])])])}),0)])])])])])},staticRenderFns:[function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{attrs:{id:"moniter"}},[a("ul",[a("li",{staticClass:"title"},[e._v("楼层选择")])]),e._v(" "),a("ul",{attrs:{id:"moniterItem"}},[a("li",{staticClass:"active",attrs:{"data-value":"11"}},[e._v("十一层")]),e._v(" "),a("li",{attrs:{"data-value":"12"}},[e._v("十二层")]),e._v(" "),a("li",{attrs:{"data-value":"13"}},[e._v("十三层")]),e._v(" "),a("li",{attrs:{"data-value":"b1"}},[e._v("负一层")]),e._v(" "),a("li",{attrs:{"data-value":"b2"}},[e._v("负二层")])])])}]};var u=a("VU/8")(m,d,!1,function(e){a("8jGS")},"data-v-9485f78c",null);t.default=u.exports}});
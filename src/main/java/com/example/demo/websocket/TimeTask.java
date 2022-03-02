package com.example.demo.websocket;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.ReduceTime;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.parameter.UserParameterField;
@Component
public class TimeTask {
	
	private static final JsonUtils js = new JsonUtils();
	
//	设备动态   （设备失去信号,或者超时180秒，设备状态出现在不该出现的部门和科室，以及脱离告警，以等级划分）
		@Scheduled(cron="0/2 * *  * * ? ") 
		public void timeSendMisMac() throws ParseException {
			
			CopyOnWriteArraySet<MyWebSocket> webSocketSet = MyWebSocket.getWebSocketSet();
//	        发送AssetName/Type/Brand 资产名称、类型、品牌、丢失时间   
//	        每隔两分钟检测一次(该时间必须大于asset中check切换ap的时间)
	        ReduceTime twominute = new ReduceTime();
	        String oldTime = twominute.localTime();
	        String minutes = twominute.timeReduceTwoMinute(oldTime);
	        WebSocketHandler handler = new WebSocketHandler();
//	        警告等级
			/*assetDatas.stream().filter(ad -> ad.size() != 0 && null != ad)
			.map(adatas -> {
				System.out.println(adatas+"=发送数据");
				return "";
			});*/
	        
//	        用户只允许查看被授权所在部门信息用户列表中departmentroom所对应的资产列表中locdept所在部门资产
//	        JsonUtils js = new JsonUtils();
			webSocketSet.forEach(c -> {
				try {
//					jwt解析该用户部门，进行过滤。
					String department_str = JWT.decode(c.getVerification()).getAudience().get(3); //用户只允许查看被授权所在部门信息用户列表中departmentroom所对应的资产列表中locdept所在部门资产
					
//					System.out.println(department_str+"==可授权的部门");医工科,住院手术室
					
					List<String> department = js.string2List(department_str);
			        List<Map> assetDatas = handler.getAssetDatas(minutes,department);
			        
			        
					if(assetDatas.size() != 0) {
//						System.err.println("*********   定时任务执行   **************");
		        		String str =  getDatas(assetDatas);
		        		c.sendMessage(str);
		        		
		        		
//		        		JSONObject parseObject = JSONObject.parseObject(str);
/*		        		try {
//		        			发送JSONobject类型数据
							c.sendObjectMessage(parseObject);
						} catch (EncodeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
//		        		System.err.println("。。。。。。。。。 定时任务完成 。。。。。。。。。。。。");
		        	}
				} catch (IOException e) {
					LogUtil.logger.error("2d设备动态数据发送异常:"+e);
				}
			});
		}
//		3D设备动态数据显示
		@Scheduled(cron="0/2 * *  * * ? ") 
		public void timeSend3DMisMac() throws ParseException {
			
			CopyOnWriteArraySet<My3DWebSocket> webSocketSet = My3DWebSocket.getWebSocketSet();
//	        发送AssetName/Type/Brand 资产名称、类型、品牌、丢失时间   
//	        每隔两分钟检测一次======每隔30秒检测一次
			ReduceTime twominute = new ReduceTime();
			String oldTime = twominute.localTime();
			String minutes = twominute.timeReduceTwoMinute(oldTime);
			WebSocket3DHandler handler = new WebSocket3DHandler();
			
			
//			分为等级警告
			List<Map> assetDatas = handler.getAssetDatas(minutes);
			
			/*assetDatas.stream().filter(ad -> ad.size() != 0 && null != ad)
			.map(adatas -> {
				System.out.println(adatas+"=发送数据");
				return "";
			});*/
			
			
			webSocketSet.forEach(c -> {
				try {
					if(assetDatas.size() != 0) {
//						System.err.println("*********   定时任务执行   **************");
//						String str =  getDatas(assetDatas);
//		        		JSONObject parseObject = JSONObject.parseObject(str);
						
						List<Map> new_mapDatas = assetDatas.stream()
								 .filter(obj -> StringUtils.isEmpty(obj.get("mac"))==false )
								 .collect(Collectors.toList());
						String str =  getDatas(assetDatas);
						
						c.sendMessage(str);
						/*		        		try {
//		        			发送JSONobject类型数据
							c.sendObjectMessage(parseObject);
						} catch (EncodeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
//		        		System.err.println("。。。。。。。。。 定时任务完成 。。。。。。。。。。。。");
					}
				} catch (IOException e) {
					LogUtil.logger.error("3d设备动态数据发送异常:"+e);
				}
			});
		}
//		数据转为string
		 public  String getDatas(List<Map> findAssetByRece){
//	        this.session.getBasicRemote().sendText(message);  发送文本数据
	    	List ls = new ArrayList<>();
	    	String str = "";
	    	
	    	for (Map map : findAssetByRece) {
//	    		如果不存在mac则为普通资产数据
	    		Object object = map.get("mac");
	    		
	    		if(StringUtils.isEmpty(object)) {
	    			continue;
	    		}
	    		
//	    		recevied类中属性获取
	    		Map received = (Map) map.get("received");
	    		String string = null;
	    		if(received != null) {
	    			string = (String) received.get("updatetime");
	    		}
	    		Map map1 = new HashMap<>();
	    		map1 = map;
//	    		map1.put("Type",map.get("Type"));单个元素获取
	    		map1.put("updatetime",string);
//	    		移除原有时间
	    		map1.remove("received");
	    		str += map1;
			}
	    	return str;
	    }
		 
//		 map地址点位数据获取，并且时刻显示,页面数据通过楼层进行划分
		 

//		 加强稳定性
		 @Scheduled(cron="0/2 * *  * * ? ") 
		 public void mapSendMac() throws ParseException {
			 
//			 获取所有资产设备mac数据,进行一级缓存，2.缓存中存在并且recevied存在的最强网关（rssi信号时大时小导致），则不进行网关变化，
			 
			 /*
			  * 1.数据库中存在，redis中不存在
			 2.数据库中不存在，redis中存在
			 3.数据库中存在，redis也存在，但不一致
			 写入getmapDatas()方法中，方便统一管理
			 */
			 
			 ReduceTime twominute = new ReduceTime();
		     String oldTime = twominute.localTime();
		     String minutes = twominute.timeReduceMinute(oldTime); //实时数据截止180秒
			 
			 CopyOnWriteArraySet<MapWebSocket> mapSocketSet = MapWebSocket.getWebSocketSet();
//	        发送AssetName/Type/Brand 资产名称、类型、品牌、丢失时间
			 MapSocketHandler handler = new MapSocketHandler();
			 
//			 long start = System.nanoTime();
			 
//			 获取redis缓存中数据
			 
//			 List<Map> mapDatas = handler.getMapDatas(minutes);//TODO210918管理员涉及部门数据
			 
			 
			 String department = "";
			 for (MapWebSocket mapWebSocket : mapSocketSet) {
				 department = JWT.decode(mapWebSocket.getVerification()).getAudience().get(3); //用户只允许查看被授权所在部门信息用户列表中departmentroom所对应的资产列表中locdept所在部门资产
			 }
			 List<Map> mapDatas = handler.getUserAssetDatas(minutes,department);//授权可查看的部门资产数据
			 
			 
//			 System.out.println("网关更新后Done in "+(System.nanoTime()-start)/1_000_000+" ms"); //Done in 14 ms ==》0ms
			 
			 mapSocketSet.forEach(c -> {
				 try {
					 if(mapDatas.size() != 0) {
//						 System.err.println("*********   定时任务执行   **************");
						 
						 
						//TODO1104	判断数据发送不全,直接返回，等待下一次发送
						 /*mapDatas.forEach(d -> {
							 if(StringUtils.isEmpty(d.get("gatewaymac").toString()) || StringUtils.isEmpty(d.get("mac").toString())) {
								 return ;
							 }
						 });*/
						 
//						String str =  getmapDatas(mapDatas);
						 
//						保证点位实时在线，数据完整传输(待定数据库表锁问题，或本循环中数据发送不完整)
						List<Map> new_mapDatas = mapDatas.stream()
								 .filter(obj -> StringUtils.isEmpty(obj.get("gatewaymac"))==false
								 || StringUtils.isEmpty(obj.get("mac"))==false)
								 .collect(Collectors.toList());
						String str =  getmapDatas(new_mapDatas);
//						发送jsonString 数据
						c.sendMessage(str);
//						System.err.println("。。。。。。。。。 定时任务完成 。。。。。。。。。。。。");
					 }
				 } catch (IOException e) {
					 LogUtil.logger.error("2d页面点位数据展示:"+e);
				 }
			 });
		 }
//		 3维地图数据
		 @Scheduled(cron="0/1 * *  * * ? ") 
		 public void mapSend3d() throws ParseException {
			 
			 ReduceTime twominute = new ReduceTime();
		     String oldTime = twominute.localTime();
		     String minutes = twominute.timeReduceMinute(oldTime);
			 
			 CopyOnWriteArraySet<Map3DWebSocket> mapSocketSet = Map3DWebSocket.getWebSocketSet();
			 Map3DSocketHandler handler = new Map3DSocketHandler();
			 List<Map> mapDatas = handler.getMapDatas(minutes);
			 mapSocketSet.forEach(c -> {
				 try {
					 if(mapDatas.size() != 0) {
//						 String str =  get3DDatas(mapDatas);
//						 实时消失处理
						 List<Map> new_mapDatas = mapDatas.stream()
								 .filter(obj -> StringUtils.isEmpty(obj.get("gatewaymac"))==false
								 || StringUtils.isEmpty(obj.get("mac"))==false )
								 .collect(Collectors.toList());
						 String str =  get3DDatas(new_mapDatas);
						 c.sendMessage(str);
					 }
				 } catch (IOException e) {
//					 LogUtil.logger.error("3d页面点位数据展示:"+e);//3D异常错误过多造成异常
				 }
			 });
		 }
		 
//	       温湿度首页
		 @Scheduled(cron="0/2 * *  * * ? ") 
		 public void humSend() throws ParseException {
			 CopyOnWriteArraySet<HumSocket> humSocketSet = HumSocket.getWebSocketSet();
			 List<Map> hum = getHum();
			 
			 humSocketSet.forEach(c -> {
				 try {
					 if(null != hum && hum.size()>0) {
//						 System.err.println("*********   定时任务执行   **************");
//						 TODO210914 发送授权过的温湿度数据
						 String department_str = JWT.decode(c.getVerification()).getAudience().get(3); //用户只允许查看被授权所在部门信息用户列表中departmentroom所对应的资产列表中locdept所在部门资产
						 List<Map> new_hum = new ArrayList<>();
						 for (Map map : hum) {
							 List<String> listByStr = js.string2List(department_str);
							 String department = map.get("department").toString();
							 boolean contains = listByStr.contains(department);
							 
							 if(!contains) {
								 continue;
							 }
//							 部门过滤后的map
							 new_hum.add(map);
						 }
						 
//						 String str = JSON.toJSONString(hum); //jsonstring  TODO210914
						 String str = JSON.toJSONString(new_hum);
						 c.sendMessage(str);
						 
//						 System.err.println("。。。。。。。。。 定时任务完成 。。。。。。。。。。。。");
					 }
				 } catch (IOException e) {
					 LogUtil.logger.error("温湿度页面数据展示:"+e);
				 }
			 });
		 }
		 
//		 3D温湿度告警
		 @Scheduled(cron="0/5 * *  * * ? ")
		 public void hum3DWarnSend() throws ParseException {
			 CopyOnWriteArraySet<Hum3DWebSocket> humSocketSet = Hum3DWebSocket.getWebSocketSet();
			 
			 List<Map> tempAndHumDatas = getTempAndHumDatas();
			 humSocketSet.forEach(c -> {
					 if(tempAndHumDatas.size()>0) {
//						 System.err.println("*********   定时任务执行   **************");
						 String str = JSON.toJSONString(tempAndHumDatas); //此行转换
						 try {
							c.sendMessage(str);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						 System.err.println("。。。。。。。。。 定时任务完成 。。。。。。。。。。。。");
					 }
			 });
		 }
//		 2d温湿度告警
		 @Scheduled(cron="0/5 * *  * * ? ")
		 public void humWarnSend() throws ParseException {
			 CopyOnWriteArraySet<HumWarnWebSocket> humSocketSet = HumWarnWebSocket.getWebSocketSet();
			 
			 List<Map> tempAndHumDatas = getTempAndHumDatas();
			 humSocketSet.forEach(c -> {
				 if(tempAndHumDatas.size()>0) {
//						 System.err.println("*********   定时任务执行   **************");
					 String department_str = JWT.decode(c.getVerification()).getAudience().get(3); //用户只允许查看被授权所在部门信息用户列表中departmentroom所对应的资产列表中locdept所在部门资产
					 List<Map> new_hum_warn = new ArrayList<>();
					 for (Map map : tempAndHumDatas) {
						String department = map.get("department").toString();
						List<String> listByStr = js.string2List(department_str);
						boolean contains = listByStr.contains(department);
						 
						if(!contains) {
							continue;
						}
//						 部门过滤后的map
						new_hum_warn.add(map);
					}
					 
					 
					 String str = JSON.toJSONString(new_hum_warn); //此行转换
//					 String str = JSON.toJSONString(tempAndHumDatas); //此行转换TODO210914
					 try {
						 c.sendMessage(str);
					 } catch (IOException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
//						 System.err.println("。。。。。。。。。 定时任务完成 。。。。。。。。。。。。");
				 }
			 });
		 }
		 
		 
		 
		 
		 
//		 转换数据
		 public  String getmapDatas(List<Map> findAssetByRece){
		    	String mapdatas = "";
		    	for (Map map : findAssetByRece) {
		    		Map map1 = new HashMap();
		    		map1.put("Status", map.get("Status"));
		    		map1.put("ApplyDept", map.get("ApplyDept"));
		    		map1.put("Amount", map.get("Amount"));
		    		map1.put("Check", map.get("Check")); //盘点
		    		map1.put("Unit", map.get("Unit")); //单位
		    		map1.put("Mac", map.get("Mac"));
		    		map1.put("AssetName", map.get("AssetName"));
		    		map1.put("Brand", map.get("Brand"));
		    		map1.put("Money", map.get("Money"));
		    		map1.put("Type", map.get("Type"));
		    		map1.put("gatewaymac", map.get("gatewaymac"));
		    		map1.put("GeneralName", map.get("GeneralName"));
		    		map1.put("updatetime", map.get("updatetime"));
//		    		map1.put("department", map.get("department"));TODO210918 授权部门后，所查看资产部门
		    		map1.put("department", map.get("departmentroom"));
		    		map1.put("AssetID", map.get("AssetID"));
		    		map1.put("Location", map.get("Location"));
		    		map1.put("BuyDate", map.get("BuyDate")); //购买日期
		    		mapdatas += map1;
				}
		    	
		    	return mapdatas;
		 }
//		 3D获取数据
		 private String get3DDatas(List<Map> findAssetByRece) {
			 String mapdatas = "";
		    	for (Map map : findAssetByRece) {
		    		Map map1 = new HashMap();
		    		map1.put("AssetID", map.get("AssetID"));
		    		map1.put("GeneralName", map.get("GeneralName"));
		    		map1.put("AssetName", map.get("AssetName"));
		    		map1.put("Specification",map.get("specification")); //规格
		    		map1.put("Brand", map.get("Brand"));
		    		map1.put("Type", map.get("Type")); //型号
		    		map1.put("Location", map.get("Location")); //放置位置
		    		map1.put("Status", map.get("Status"));
		    		map1.put("Mac",  map.get("gatewaymac")); //设备所在网关
		    		map1.put("Updatetime", map.get("updatetime"));
//		    		map1.put("department", map.get("department"));
		    		map1.put("Architecture", map.get("architecture"));
		    		map1.put("Academy", map.get("academy"));
		    		map1.put("Floor", map.get("floor"));
		    		mapdatas += map1;
				}
		    	
		    	return mapdatas;
		 }
		 
//获取温湿度
	    public List<Map> getHum() {
	    	
	    	HumSocketHandler handler = new HumSocketHandler();
	    	List<Humiture> queryhum = handler.getHumiture();
	    	
			List<Map> hy = new ArrayList<>();
			
			for (Humiture humiture : queryhum) {
				String mac = humiture.getMac();
//				冷藏物编号
				String freezernumber = humiture.getFreezernumber();
//				冷藏物名字
				String freezername = humiture.getFreezername();
//				湿度区间值
				String temperaturefitted = humiture.getTemperaturefitted();
//				湿度区间
				String humidityfitted = humiture.getHumidityfitted();
//				类型
				String type = humiture.getType();
//				部门
				String department = humiture.getDepartment();
				
				
				
//				位置
				String location = humiture.getLocation();
				
//				截止今日
				 timeUtiles timeUtile = new timeUtiles();
				 String get0Time = timeUtile.get0Time();
				 String currenttime = timeUtile.getCurrenttime();
				 List<Hygrothermograph> mapDatas = handler.getHumDatas(mac, get0Time, currenttime);	
				 
//				 获取今日的最高温度和最高湿度,以及最新温湿度
				 Map map = new HashMap();
				 if(null != mapDatas && mapDatas.size()>0) {
//					 先行排序，然后获取最大值，或者最小值
				        Collections.sort(mapDatas, new Comparator<Hygrothermograph>() {
				            @Override
				            public int compare(Hygrothermograph o1, Hygrothermograph o2) {
				            	if(StringUtils.isEmpty(o1.getHumidity()) || StringUtils.isEmpty(o2.getHumidity())) {
				            		return 1;
				            	}
				            	double d1 = Double.parseDouble(o1.getHumidity());
				            	double d2 = Double.parseDouble(o2.getHumidity());
				            	String result1 = String.format("%.2f",d1);
				            	String result2 = String.format("%.2f",d2);
				            	double f1 = Double.parseDouble(result1);
				            	double f2 = Double.parseDouble(result2);
				            	/*if(f1==f2) {
				            		return 0;
				            	}*/
				                return f1>f2? -1:(f1==f2? 0:1);
				                
				            }
				        });
				        for (int i = 0; i < mapDatas.size(); i++) {
				        	
//				        	最大湿度值
							if(i == 0) {
								map.put("tophumidity", mapDatas.get(0).getHumidity());
								
								
							}
//							最小湿度值
							if(i == mapDatas.size()-1) {
								map.put("minhumidity",mapDatas.get(mapDatas.size()-1).getHumidity());
							}
							
						}
				        Collections.sort(mapDatas, new Comparator<Hygrothermograph>() {
				            @Override
				            public int compare(Hygrothermograph o1, Hygrothermograph o2) {
				            	if(StringUtils.isEmpty(o1.getTemperature()) || StringUtils.isEmpty(o2.getTemperature())) {
				            		return 1;
				            	}
				            	
				            	double d1 = Double.parseDouble(o1.getTemperature());
				            	double d2 = Double.parseDouble(o2.getTemperature());
				            	String result1 = String.format("%.2f",d1);
				            	String result2 = String.format("%.2f",d2);
				            	double f1 = Double.parseDouble(result1);
				            	double f2 = Double.parseDouble(result2);
				            	/*if(f1==f2) {
				            		return 0;
				            	}*/
				                return f1>f2? -1:(f1==f2? 0:1);
				            }
				        });
				        for (int i = 0; i < mapDatas.size(); i++) {
				        	
							if(i == 0) {
								map.put("toptemperature",mapDatas.get(0).getTemperature());
								
							}
//								最小湿度值
							if(i == mapDatas.size()-1) {
								map.put("mintemperature", mapDatas.get(mapDatas.size()-1).getTemperature());
							}
				        		
//				        	
						}

				 }
//				 TODO0325 温湿度长时间数据查询优化
//			        获取最新一条温湿度数据，以及设备绑定数据
			        List<Hygrothermograph> currentimeDatas = handler.getcurrentimehy(mac);	
			        String humidity = "";
			        String temperature = "";
			        String electric = ""; //电量
			        String currentTime = ""; //记录时间
			        for (Hygrothermograph cu : currentimeDatas) {
						 humidity = cu.getHumidity();
						 temperature = cu.getTemperature();
						 electric = cu.getElectric();
						 currentTime = cu.getCurrentTime();
					}
//			      仪器温湿度
			        map.put("temperature", temperature);
			        map.put("humidity", humidity);
			        map.put("mac", mac);
			        map.put("electric", electric);
			        map.put("currenttime", currentTime);
//			        仪器设备信息
			        map.put("freezernumber", freezernumber);
					map.put("freezername", freezername);
					map.put("temperaturefitted", temperaturefitted);
					map.put("humidityfitted", humidityfitted);
					map.put("type", type);
					map.put("department", department);
					map.put("location", location);
			        hy.add(map);
			}
			
			
//			TODO数据分页处理
	    	/*JSONArray array1= JSONArray.parseArray(JSON.toJSONString(hy));
	    	String jsonString = array1.toString();
	    	return jsonString;*/
			
			return hy;
	    }
	    
	    private List<Map> getTempAndHumDatas(){
	    	
//	    	待用十分钟内告警
	    	timeUtiles ti = new timeUtiles();
	    	String startime = "";
	    	String currentTime = ti.getCurrenttime();
	    	currentTime = currentTime.substring(0, currentTime.length()-2);
	    	currentTime = currentTime+"00";
	    	try {
	    		startime = ti.timeReduce(currentTime, UserParameterField.Warn_duce_time*60);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	HumSocketHandler hs = new HumSocketHandler();
	    	List<Map> warnDatas = hs.getWarnDatas(startime,currentTime);
	    	List<Map> ls_warn = new ArrayList<>();
	    	
			if(null == warnDatas || warnDatas.size() == 0) {
				return ls_warn;
			}
	    	
	    	for (Map map : warnDatas) {
	    		
	    		String department = map.get("department").toString(); //部门过滤TODO210914
	    		
	        	String temperature = "";
	        	String humidity = "";
	        	String humidityfitted = "";
	        	String temperaturefitted = "";
	        	
	        	Map map_warn = new HashMap();
//	    		告警值不为空
    			String id = "";
    			Integer level = 0;
    			String name = "";
    			String desc = "";
    			
    			String warnnum = (String) map.get("warnnum");
    			
    			if(!StringUtils.isEmpty(warnnum)) {
    				int warn_value = Integer.parseInt(warnnum);
    				
    				if(warn_value == 1) {
    					id = "gw";
    					name = "高温";
    					desc="温度高于阀值";
    				}
    				if(warn_value == 2) {
    					id = "dw";
    					name = "低温";
    					desc="温度低于阀值";
    				}
    				if(warn_value == 3) {
    					id = "gs";
    					name = "高湿";
    					desc = "湿度高于阀值";
    				}
    				if(warn_value == 4 ) {
    					id = "ds";
    					name ="低湿";
    					desc ="湿度低于阀值";
    				}
    				if(warn_value == 12) {
    					id = "dd";
    					name = "掉电";
    					desc = "仪器掉电";
    				}
    				if(warn_value == 14) {
    					id = "ld";
    					name = "低电";
    					desc = "电量低于阀值";
    				}
    				
//    	    		温度阀值,以及温度不为空
    	    		if(null !=map.get("temperaturefitted") && !StringUtils.isEmpty(map.get("temperaturefitted").toString())
    	    				&& null != map.get("temperature") && !StringUtils.isEmpty(map.get("temperature").toString())) {
    	    			temperaturefitted = map.get("temperaturefitted").toString();
    	    			temperature = map.get("temperature").toString();
    	    			
    	    			Boolean tp = getWarnBoolean(temperaturefitted,temperature);
//    	    			记录温度告警
    	    			if(!tp) {
    	    				map_warn.put("temperature", temperature);
    	    				map_warn.put("temperaturefitted", temperaturefitted);
    	    			}/*
    	    			TODO210915,此处跳过可能影响3d的数据展示
    	    			else {
    	    				continue;//等待实时数据传递并且更新解除告警数据
    	    			}*/
    	    			
    	    		}
//    	    		湿度阀值,以及湿度不为空
    	    		if(null !=map.get("humidityfitted") && !StringUtils.isEmpty(map.get("humidityfitted").toString())
    	    				&& null != map.get("humidity") && !StringUtils.isEmpty(map.get("humidity").toString())) {
    	    			humidityfitted = map.get("humidityfitted").toString();
    	    			humidity = map.get("humidity").toString();
    	    			
    	    			Boolean hum = getWarnBoolean(humidityfitted,humidity);
//    	    			记录湿度告警
    	    			if(!hum) {
    	    				map_warn.put("humidity", humidity);
    	    				map_warn.put("humidityfitted", humidityfitted);
    	    			}/*
    	    			TODO210915,此处跳过可能影响3d的数据展示
    	    			else {
    	    				continue;//等待实时数据传递并且更新解除告警数据
    	    			}*/
    	    			
    	    		}
//    	    		电量告警
    				if(null !=map.get("electric") && !StringUtils.isEmpty(map.get("electric").toString())) {
    					String electric = map.get("electric").toString();
    					int ele = Integer.parseInt(electric);
    					if(ele < UserParameterField.GS1_electric) {
    						map_warn.put("electric", electric);
    					}
    				}
    				
    			}
    			
    			
    			map_warn.put("id", id);
//	    			告警设备识别号gw,dw,gs,ds,ld,dd
    			map_warn.put("equipID", map.get("mac").toString());
//	    			告警级别1-4，1高温，2低温，3，高湿，4，低湿，5，低电，6，掉电,7设备掉线
    			if(id.equals("gw")||id.equals("dw") || id.equals("gs")||id.equals("ds")
    					|| id.equals("gwgs") || id.equals("dwgs")||id.equals("gwds")||id.equals("dwds")) {
    				level=1;
    			}else if(id.equals("ld")||id.equals("dd")){
    				level=2;
    			}else{
    				level=3;
    			}
    			map_warn.put("level", level);
//	    			告警名称
    			map_warn.put("name", name);
//	    			告警明细
    			map_warn.put("desc", desc);
//	    			告警所属场景（环境）
    			map_warn.put("type", "环境");
//	    			告警状态(告警回执)
    			map_warn.put("state", "未处理");
//	    			告警时间
    			map_warn.put("date", map.get("currentTime").toString());
//    				告警部门
    			map_warn.put("department", department);
    			
    			ls_warn.add(map_warn);
	    	}
	    		
			return ls_warn;
	    }
	    
/*	    TODO1225告警3D修改
 * 		private List<Map> getTempAndHumDatas(){
	    	timeUtiles ti = new timeUtiles();
	    	String startime = "";
	    	String currentTime = ti.getCurrenttime();
	    	currentTime = currentTime.substring(0, currentTime.length()-2);
	    	currentTime = currentTime+"00";
	    	try {
	    		startime = ti.timeReduce(currentTime, UserParameterField.Warn_duce_time*60);
	    	} catch (ParseException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}
//	    	未有解除告警数值，进行告警
	    	
	    	
	    	
//	    	仪器设备数据保存时间一致用于告警
	    	HumSocketHandler hs = new HumSocketHandler();
//	    	sql必须分割，否则10分钟内有的仪器设备的数据如果没有接受到（掉线告警关联查询无法表示出来）
	    	List<Map> warnDatas = hs.getWarnDatas(startime,currentTime);
	    	List<Map> ls_warn = new ArrayList<>();
	    	
	    	if(null == warnDatas) {
	    		return ls_warn;
	    	}
	    	
	    	for (Map map : warnDatas) {
	    		
	    		String freezernumber = "";
	    		String freezername = "";
	    		String temperaturefitted = "";
	    		String humidityfitted = "";
	    		String temperature = "";
	    		String humidity = "";
	    		
	    		freezernumber = map.get("freezernumber").toString();
	    		freezername = map.get("freezername").toString();
	    		Map map_warn = new HashMap();
//	    		温度阀值,以及温度不为空
	    		if(null !=map.get("temperaturefitted") && !StringUtils.isEmpty(map.get("temperaturefitted").toString())
	    				&& null != map.get("temperature") && !StringUtils.isEmpty(map.get("temperature").toString())) {
	    			temperaturefitted = map.get("temperaturefitted").toString();
	    			temperature = map.get("temperature").toString();
	    			
	    			Boolean tp = getWarnBoolean(temperaturefitted,temperature);
//	    			记录温度告警
	    			if(!tp) {
	    				map_warn.put("temperature", temperature);
	    			}
	    		}
//	    		湿度阀值,以及湿度不为空
	    		if(null !=map.get("humidityfitted") && !StringUtils.isEmpty(map.get("humidityfitted").toString())
	    				&& null != map.get("humidity") && !StringUtils.isEmpty(map.get("humidity").toString())) {
	    			humidityfitted = map.get("humidityfitted").toString();
	    			humidity = map.get("humidity").toString();
	    			
	    			Boolean hum = getWarnBoolean(humidityfitted,humidity);
//	    			记录温度告警
	    			if(!hum) {
	    				map_warn.put("humidity", humidity);
	    			}
	    		}
	    		
	    		
//	    		告警值不为空
	    		if(map_warn.size()>0) {
	    			
	    			String id = "";
	    			Integer level = 0;
	    			String name = "";
	    			String desc = "";
	    			
//	    			高温或者低温(建议使用仪器设备数字编号)
	    			if(null != map_warn.get("temperature") && !StringUtils.isEmpty(map_warn.get("temperature").toString())) {
	    				Boolean warnType = getWarnType(temperaturefitted,temperature);
	    				if(warnType == true) {
	    					id = "gw";
	    					name = "高温";
	    					desc="温度高于阀值";
	    				}else {
	    					id = "dw";
	    					name = "低温";
	    					desc="温度低于阀值";
	    				}
	    				
//	    				解除温度告警
	    				
	    			}
//	    			高温低湿或低温高湿
	    			if(null != map_warn.get("humidity") && !StringUtils.isEmpty(map_warn.get("humidity").toString())
	    					&& !StringUtils.isEmpty(desc)) {
	    				Boolean warnType = getWarnType(humidityfitted,humidity);
	    				if(warnType == true) {
	    					id += "gs";
	    					name += "高湿";
	    					desc += "湿度高于阀值";
	    				}else {
	    					id += "ds";
	    					name +="低湿";
	    					desc +="湿度低于阀值";
	    				}
	    				
//	    				解除湿度告警
	    				
	    			}
	    			
	    			
	    			
	    			
	    			map_warn.put("id", id);
//	    			告警设备识别号gw,dw,gs,ds,ld,dd
	    			map_warn.put("equipID", map.get("mac").toString());
//	    			告警级别1-4，1高温，2低温，3，高湿，4，低湿，5，低电，6，掉电,7设备掉线
	    			if(id.equals("gw")||id.equals("dw") || id.equals("gs")||id.equals("ds")
	    					|| id.equals("gwgs") || id.equals("dwgs")||id.equals("gwds")||id.equals("dwds")) {
	    				level=1;
	    			}else if(id.equals("ld")||id.equals("dd")){
	    				level=2;
	    			}else{
	    				level=3;
	    			}
	    			map_warn.put("level", level);
//	    			告警名称
	    			map_warn.put("name", name);
//	    			告警明细
	    			map_warn.put("desc", desc);
//	    			告警所属场景（环境）
	    			map_warn.put("type", "环境");
//	    			告警状态(告警回执)
	    			map_warn.put("state", "未处理");
//	    			告警时间
	    			map_warn.put("date", map.get("currentTime").toString());
	    			
	    			ls_warn.add(map_warn);
	    		}
	    		
	    	}
	    	return ls_warn;
	    }
	    
*/	    
	    
	   
	    
//	    左低右高
	    public Boolean getWarnBoolean(String s,String temp_str) {
			String[] split = s.split("~");
			String s1 = "";
			String s2 = "";
			double temp = Double.parseDouble(temp_str);
			
//			阀值必须完整
	        for(String d : split){
	            if(StringUtils.isEmpty(s1)) {
	            	s1 = d; 
	            }else{
	            	s2 = d;
	            }
	        }
	        double w1 = Double.parseDouble(s1);
	        double w2 = Double.parseDouble(s2);
	        Boolean b = (temp>=w1 && temp<=w2);
	        return b;
	    }
	//  true 高温或高湿,false 低温或低湿
	    public Boolean getWarnType(String s,String temp_str) {
	    	String[] split = s.split("~");
	    	String s1 = "";
	    	String s2 = "";
	    	double temp = Double.parseDouble(temp_str);
	    	
//			阀值必须完整
	    	for(String d : split){
	    		if(StringUtils.isEmpty(s1)) {
	    			s1 = d; 
	    		}else{
	    			s2 = d;
	    		}
	    	}
	    	double w1 = Double.parseDouble(s1);
	    	double w2 = Double.parseDouble(s2);
	    	Boolean b = (temp>w2);
	    	return b;
	    }
}


 
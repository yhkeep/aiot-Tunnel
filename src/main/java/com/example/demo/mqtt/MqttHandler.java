package com.example.demo.mqtt;

//import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.cache.GatewayGuavaCache;
import com.example.demo.cache.GuavaTestCache;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.GatewayEdit;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.domain.HygrothermographWarn;
import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.Received;
import com.example.demo.mapper.IAssetMapper;
import com.example.demo.mapper.IReceivedMapper;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.GatewayService;
import com.example.demo.service.HygrothermographWarnService;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.service.hygrothermographService;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;
import com.example.demo.util.parameter.GatewayParameterField;
import com.example.demo.util.parameter.ParameterField;
import com.example.demo.util.parameter.UserParameterField;
import com.example.demo.util.sockettherd.HexConvert;

@Component
public class MqttHandler {
	
	
	@Autowired
	private IAssetMapper mapper;
	
	@Autowired
	private IReceivedMapper receivedMapper;
	
	@Autowired
    private IbeaconGatewayService ibeaconService;
	
	@Autowired
	private IAssetMapper assetMapper;
	
	@Autowired
    private GatewayService gatewayService;
	
	@Autowired
	private hygrothermographService hygrothermographService;
	
	@Autowired
	private HygrothermographWarnService hyWwarnService;
	
	@Resource
    private RedisUtils redisUtil;
	
	public static MqttHandler task;
	
	public static timeUtiles timeUtile = new timeUtiles();
	
//	public static List<Gateway> gatewayDatas = new ArrayList<Gateway>();
//	时间测试
//	public static long start;
	
	
	/*public static List<Gateway> getGatewayDatas() {
		return gatewayDatas;
	}
	public static void setGatewayDatas(List<Gateway> gatewayDatas) {
		MqttHandler.gatewayDatas = gatewayDatas;
	}*/
	
	private static final String sendRestart="{\"action\":\"reboot\", \"requestId\":\"b2c3d4e5-3424-4dca-32dc-12b73290cfed\"}";
//	注意彩灯配置如下，注意参数完整性
	private static final String sendLED="{ \"action\":\"config\", \"takeEffectImmediately\":\"YES\", \"common\":{ \"disableLED\":\"NO\",\"isLongBright\":\"YES\" } }";
//  升级网关外网在线升级配置
	private static final String sendUpgrade = "{ \"action\":\"upgrade\", \"type\":\"public\", \"isSave\":\"YES\", \"version\":\"v3.1.2\", \"requestId\":\"xxx\" }";

	
	private GuavaTestCache glfc = new GuavaTestCache<>();//缓存标签接近真实楼层,过滤下一秒时间
	
	//初始化
	
	@PostConstruct
    public void init() {    
		task = this;
    }
	
	/*
	 * mqtt中解析数据 
	   注意：解析过程中不能出现过大表数据查询sql，会造成不间断长时间订阅失败的情况
	   远程过程中，随着ap数量增加，订阅连接时间偏长
	 * 
	 * 
	 */
	
    
    public void getAssetDatas(JSONArray array) throws ParseException{
    	String gatewaymac= "";
    	
		/*
		 
		 * 
		 * [{"gatewayLoad":0.05,"gatewayFree":98,"type":"Gateway","mac":"AC233FC001EA","timestamp":"2019-08-13T06:06:20Z"},
		 {"rssi":-55,"bleName":"S1","temperature":27.1,"humidity":57.91,"type":"S1","battery":100,"mac":"AC233FA018E7","timestamp":"2019-08-13T06:06:21Z"},
		 {"rssi":-55,"bleName":"","rawData":"","type":"Unknown","mac":"AC233FA018E7","timestamp":"2019-08-13T06:06:21Z"},
		 {"rssi":-63,"bleName":"","rawData":"0201060303E1FF0D16E1FFA1026401FA31A03F23AC","type":"Unknown","mac":"AC233FA031FA","timestamp":"2019-08-13T06:06:21Z"
		 }]=====array
		 
		 *温湿度
		 *[
		 *{"timestamp":"2019-10-21T03:35:49Z","type":"Gateway","mac":"AC233FC001AA","gatewayFree":84,"gatewayLoad":0.21},
		 *{"timestamp":"2019-10-21T03:35:49Z","type":"S1","mac":"AC233FA05464","bleName":"","rssi":-54,"battery":100,"temperature":21.5,"humidity":83.37}
		 *]
		 *
		 *[{"timestamp":"2019-10-21T03:35:46Z","type":"Gateway","mac":"AC233FC001AA","gatewayFree":84,"gatewayLoad":0.21},
		 *{"timestamp":"2019-10-21T03:35:46Z","type":"S1","mac":"AC233FA05464","bleName":"","rssi":-56,"battery":100,"temperature":21.5,"humidity":83.37}]
		 *
		 */
    	
//    	数据排空
		if(null == array || array.size() == 0) {
			return;
		}
		
		for (int i = 0; i < array.size(); i++) {
			
			
			try {
				
				//注意：index中的内容带有中括号[]，所以要转化为JSONArray类型的对象
				String timestamp = array.getJSONObject(i).getString("timestamp");
				String type = array.getJSONObject(i).getString("type");
				String mac = array.getJSONObject(i).getString("mac");
				String rssi = array.getJSONObject(i).getString("rssi");
				
				
				String ele = array.getJSONObject(i).getString("battery");  //s1或ibeacon中电量获取
				
				
//				start = System.nanoTime();
				
				
//				网关数据量越多，时间延迟越大
//				String currentTime = timeUtile.UTC2local(timestamp);
				
				
				String currentTime = timeUtile.getCurrenttime();
				
//				每个标签对应的网关保存下来
				if ("Gateway".equals(type)) {
					if(StringUtils.isEmpty(mac)) {
						continue;
					}
					gatewaymac = mac;
					try {
						receivedGateWay(gatewaymac,currentTime);
						continue;
					} catch (Exception e) {
						// TODO: handle exception
						LogUtil.logger.error(e+":redis update gateway faile");
					}
				}
				
				
				if (!"Gateway".equals(type)) {
					
					
					
					if( mac.equals("AC233FA4333D")) {
//					if(mac.equals("AC233FA43094") || mac.equals("AC233FA431C9") || mac.equals("AC233FA42FD3") || mac.equals("AC233FB100EE")){
						LogUtil.logger.info(mac+"???????????????????????时间多久发送一次?????????????????????????????????????????====================mac"+rssi+"========rssi======gatewaymac===="+gatewaymac);
						
//						{"action":"config","takeEffectImmediately":"YES","common":{"proto":"default","uploadInterval":"default","isFilterDupData":"default","isOnlySpecialMac":"default","macList":"default","disableLED":"YES","isLongBright":"NO","isUploadUnkown":"YES","isUploadIBeacon":"YES","isUploadS1":"YES","isUploadGateway":"YES","isauto":"NO","timeout":"30","schedule":{"istiming":"default","min":"default","hour":"default","week":"default"},"rssi":"default","regex":"default","macReg":"^AC23.*\\|^C202.*","cacheTime":"default","isLongFormat":"default","isJsonFormat":"default"},"mqtt":{"mUrl":"tcp://172.27.121.184:8009","urlpath":"default","cafile":"default","certfile":"default","keyfile":"default","keypass":"default","publishTopic":"/gw/ac233fc071a5/status","subscribeTopic":"/gw/ac233fc071a5/action","responseTopic":"/gw/ac233fc071a5/action/response","qos":"1","userName":"default","passWord":"default","clientID":"default"},"http":{"hUrl":"default","auth":"default","httpUser":"default","httpPass":"default"},"requestId":"b2c3d4e5-3424-4dca-32dc-12b73290cfed"}=sendConfig172.27.121.184
					}
					
//					通过查询一分钟内程序信号接受到的最小值，判断，是否存在，如果存在则判断最小值，否则跳转报警接口，进行报警
					
					try {
						
//						温湿度数据处理
						if("S1".equals(type)) {
//						if("S3".equals(type)) {
							
							  
							/*
							array[{"gatewayLoad":1.35,"gatewayFree":88,"type":"Gateway","mac":"AC233FC08502","timestamp":"2020-11-09T02:59:07.335Z"},
							{"rssi":-66,"bleName":"S1","temperature":12.69,"humidity":60.39,"type":"S1","battery":100,"mac":"AC233FA018FB","timestamp":"2020-11-09T02:59:07.332Z"},
							{"rssi":-65,"bleName":"","rawData":"","type":"Unknown","mac":"AC233FA018FB","timestamp":"2020-11-09T02:59:07.335Z"},
							{"rssi":-50,"bleName":"S1","temperature":23.21,"humidity":64.28,"type":"S1","battery":100,"mac":"AC233FA018E7","timestamp":"2020-11-09T02:59:07.412Z"}]
							*/
							  
//							TODO1216取整分钟时间点
							/*
							 * currentTime = currentTime.substring(0, currentTime.length()-2);
						    currentTime = currentTime+"00";
						    */
							  
//							温度
							String temperature = array.getJSONObject(i).getString("temperature");
//							湿度
							String humidity = array.getJSONObject(i).getString("humidity");
//							电量
							String battery = array.getJSONObject(i).getString("battery");
							
//							四舍五入,保留一位小数
							double hum = Double.parseDouble(humidity);
							double temp = Double.parseDouble(temperature);
							
			            	String hum_f = String.format("%.1f",hum);
			            	String temp_f = String.format("%.1f",temp);
							
//							默认每10分钟保存一次温湿度数据(持久化)
							
//							获取温湿度仪器
							List<Humiture> humSection = task.hygrothermographService.getHumSection(mac);

							
							if(null != humSection && humSection.size()>0) {
								
								int temp_hum_savetim = ParameterField.tempHumTime;
								for (Humiture h : humSection) {
									if(!StringUtils.isEmpty(h.getSaveinterval())) {
										temp_hum_savetim = Integer.parseInt(h.getSaveinterval())*60;
									}
								}
								
//								单位时间内查询仪器，并且更新温湿度仪器数据
								String startime = timeUtile.timeReduce(currentTime, temp_hum_savetim);
								
//								线程阻塞严重(表中历史数据过多，创建索引)
								List<Map> queryhyDatas = updateTemphum(mac,startime,currentTime);
								
						    	Long l = 0L ;
						    	for (Map map : queryhyDatas) {
						    		l = (Long) map.get("count(*)"); 
						    		
//						    		已存在数据则跳过
						    		if(l>0) {
										continue;
//									否则插入最新数据
									}else {
										Hygrothermograph hy = new Hygrothermograph();
										hy.setCurrentTime(currentTime);
										hy.setGatewaymac(gatewaymac);
										hy.setHumidity(hum_f);
										hy.setMac(mac);
										hy.setTemperature(temp_f);
										hy.setElectric(battery);
										task.hygrothermographService.saveHy(hy);
										
									}
						    	}
						    	
//								（实时告警数据记录）告警历史记录,不受区间时间限制,超过阀值立即告警
								warnHistory(currentTime,battery,gatewaymac,hum_f,mac,temp_f);
								continue;
							}else {
//								未绑定设备过滤
									continue;
							}

						}
						
					} catch (Exception e) {
						// TODO: handle exception 处理不间断异常订阅现象
						LogUtil.logger.error(e+"温湿度标签捕获异常-解析过程中");
						continue;
					}
					
					
//					余下E6/S1/ibeacon等标签数据
					

					
					
//					缓存多个redis
					if(!StringUtils.isEmpty(gatewaymac)&&!StringUtils.isEmpty(mac)&&!StringUtils.isEmpty(rssi)){
						
//						通过缓存查询（添加/修改/删除redis缓存需要更新），只获取E6绑定标签数据
						Long result_num = getRedisE6(mac);
						
						
//						TODO去掉上下一秒左右同一时间多余ap，获取最大rssi值ap，降低内存消耗
//						====================TODO0310测试区
						int reNewRssi = 0;
						try {
							reNewRssi = Integer.parseInt(rssi);
							
						} catch (Exception e) {
							// TODO: handle exception
							LogUtil.logger.error("超强信号异常rssi="+e);
						}
//						排除掉非绑定资产和rssi小于90的数据
						if(result_num <= 0 || reNewRssi <= ParameterField.rssi_min) {
							continue;
						}
						
//						矫正过道上ap，进行rssi值干扰,通过校正值来断定（过道中无遮挡物品，信号干扰较小，此处统一矫正，前端页面定点过道加参数8-10）
						 try {
							 Received re = new Received();
							 re.setGatewaymac(gatewaymac);
							 re.setMac(mac);
							 re.setRssi(rssi);
							 Map<String,String> floorByRedis1 = getFloorByRedis(re);
							 if(!StringUtils.isEmpty(floorByRedis1.get("gatewayflag"))) {
								 if(re.getMac().equals("AC233FA430C4")) {
			    						LogUtil.logger.info(gatewaymac+"过道处理处理前ap波动="+mac + "==="+rssi);
			    				 }
								 
								 String string_new_rssi = floorByRedis1.get("gatewayflag");
								 Integer max_ap_Ele_gatewayfactor  = Integer.parseInt(string_new_rssi);//获取通道物理属性，增大该系列区域ap的rssi物理影响因子
								 int new_rssi = Integer.parseInt(rssi); 
								 rssi = (new_rssi - max_ap_Ele_gatewayfactor)+"";
								 
								 if(re.getMac().equals("AC233FA430C4")) {
			    						LogUtil.logger.info(gatewaymac+"过道处理后ap波动="+mac+"===="+rssi);
			    				 }
							 }
						} catch (Exception e) {
							// TODO: handle exception
							LogUtil.logger.error(e+"过道ap的rssi值矫正异常");
						}
						

						
						
//						过滤线程不安全单位时间数据,数据及时性相对较差
						Received re = new Received();
						re.setMac(mac);
						re.setRssi(rssi);
						re.setGatewaymac(gatewaymac);
						
						try {
//							暂时不做同秒内时间拦截处理
							Integer filterTime = filterTime(re,timeUtile);
							if(-1 == filterTime) {
								continue;
							}
						} catch (Exception e) {
							// TODO: handle exception
							LogUtil.logger.error(e+"==数据过滤");
						}
						
						
						
					   	/* 缓存中获取数据
					   	 * Map cache_fc_map1 = (Map)glfc.getCache(re.getMac());
						if(re.getMac().equals("AC233FB100EE")) {
							String fc_rssi1 = (String)cache_fc_map1.get("rssi");
							String fc_ap1 = (String)cache_fc_map1.get("ap");
							LogUtil.logger.info(fc_ap1+"==缓存中ap数据=="+fc_rssi1);
						}*/

						
						/**
						 * 当前时间以及下一秒内多个ap同时接受到的标签数据，返回最强（提前做好时间片最强ap，减少redis缓存压力）
						 */

						
//						====================
						
						
//						排除异常放电/如ap重启
						/*try {
							int reNewRssi = Integer.parseInt(rssi);
							reNewRssi = reNewRssi>=-39?reNewRssi=-45:reNewRssi;		 
							rssi = reNewRssi+"";
//							参考历史记录数据，rssi值小于70则抛弃，提高执行效率（厂房，开阔地需要排除）
							
						} catch (Exception e) {
							// TODO: handle exception
							LogUtil.logger.error("替代超强信号rssi="+e);
						}*/
						
						
						
//						获取UNKNOW电量
						String rawData = array.getJSONObject(i).getString("rawData"); //非常规电量数据
						if(!StringUtils.isEmpty(rawData) && StringUtils.isEmpty(ele)) {
//							0201060303E1FF0D16E1FFA102 64 00  EE 00 B1 3F 23 AC
							String battery_rawdata = rawData.substring(rawData.length()-16,rawData.length()-14);
							Integer battery_percentage = (int) (HexConvert.hexStringToDeci(battery_rawdata)*10);
							ele = battery_percentage+"";
							
							System.out.println(ele+"==电量显示"+rawData+"==mac"+mac);
							
//							TODO新建表存放以下两种告警数据
//							低于阀值15电量进行告警，存入数据表温湿度仪器告警中
							
//							脱离告警
							String breakAway = rawData.substring(rawData.length()-14,rawData.length()-12);
//							感光未遮蔽，标签脱落告警
							if("01".equals(breakAway)) {
//								System.out.println(breakAway+"=mac"+mac);
							}
						}
						/**
	0201 0603 03E1 FF0D 16E1 FFA1 0264 00EE 00B1 3F23 AC
	0201 0603 03E1 FF0D 16E1 FFA1 0264 01EE 00B1 3F23 AC
						 * */
						
						/*if((mac.equals("AC233FB100EE"))) {
							LogUtil.logger.info(gatewaymac+"=mac=切片时间段后="+rssi);
						}*/
						
						
						
						
//						前后定时长排除，异常放电情况（移动情况考虑冲突，数据不及时更新）
						
						Map<Object, Object> gatewaymacByMac = task.redisUtil.hmget(mac);
						
						if(null != gatewaymacByMac && !gatewaymacByMac.isEmpty()) {
							
							Object object = gatewaymacByMac.get(gatewaymac);
							Map gatewaymacMap = (Map)object;
							
							
//							更新过期时间
							String oldExpressTime = (String)gatewaymacByMac.get("expressTime");
							String expressTimeAdd = timeUtile.timeAdd(oldExpressTime,ParameterField.redis_express_disstime);
//							重新获取，清楚缓存，稳定ap定位--虽有时效但标签ap缓存实时更新
							if(currentTime.compareTo(expressTimeAdd)>=0) {
//								获取原有符合条件ap，并且清除，同时缓存
								getReadisDatas(gatewaymacByMac,currentTime,mac);
//								更新当前实时ap
								updateRedis(rssi, currentTime, gatewaymac, mac);
								updateReceived(rssi,currentTime,gatewaymac,mac,ele); //各参数传递为实时当前接受ap数据
								
								continue;
							}else {
								updateRedisMore( rssi, currentTime, gatewaymac, mac);
								updateReceived(rssi,currentTime,gatewaymac,mac,ele);
								continue;
							}
						}else if(null == gatewaymacByMac || gatewaymacByMac.isEmpty()){
							updateRedis( rssi, currentTime, gatewaymac, mac);
							updateReceived(rssi,currentTime,gatewaymac,mac,ele);
							continue;
						}
						continue;
					}
					continue;
					
					
//					redis更新截止
					
					
					
					
	/*
	 * 0827 TODO 
	 * 				2.1网关变化，以及rssi变化存入数据，就近网关显示备份使用
					获取最新数据的sql，并执行保存，向后衍生3秒是过滤掉程序和sql查询时间的影响
					String startTime = timeUtile.timeReduce(currentTime, 3);
					String endTime = timeUtile.timeAdd(currentTime, 3);

					redis缓存，单点位多网关匹配,信号强，持久化数据库,缓存时间2秒
					
					
//					re中查询
					List<Map> macdata = task.ibeaconService.findMacByMac(mac,endTime,startTime); //re中查询
//					2.更新数据，显示当前位置
					
					
					
					if(mac.equals("C2021A0000E1") || mac.equals("AC233FB100EE")||mac.equals("C2021A0000E3")) {
						
						LogUtil.logger.info("gatewaymac="+gatewaymac+"=mac"+mac+"currentTime="+currentTime+"rssi="+rssi+
								"=macdata"+macdata);
					}
					
					
					 
					
					
					
					if(macdata.size()>0) {
//						及时更新数据情况分为：
//						1.传递不同网关，相同mac
						
//						2.传递相同网关，相同mac
						JSONArray updateGateway= JSONArray.parseArray(JSON.toJSONString(macdata));
						String reoldGatewayMac = "";
						String reoldMac = "";
						String reoldRssi = "";
						String reoldupdatetime = "";
						for (int k = 0; k < macdata.size(); k++) {
							reoldGatewayMac = updateGateway.getJSONObject(k).getString("gatewaymac");
							reoldMac = updateGateway.getJSONObject(k).getString("mac");
							reoldRssi =  updateGateway.getJSONObject(k).getString("rssi"); //rssi此处最大需要替换
				    		reoldupdatetime = updateGateway.getJSONObject(k).getString("updatetime");
				    	}
						
						
						
						
						if (""!= rssi && ""!=reoldRssi) {
							int reNewRssi = Integer.parseInt(rssi);
							int oldRssi = Integer.parseInt(reoldRssi);
//								相同网关，mac则更新time
							if (reoldMac.equals(mac) && !gatewaymac.equals(reoldGatewayMac) && reNewRssi>oldRssi) {
								Received re = new Received();
								re.setMac(mac);
		    	    			re.setUpdatetime(currentTime);
		    	    			re.setGatewaymac(gatewaymac);
		    	    			re.setRssi(rssi);
		    	    			if(mac.equals("AC233FA018E3")) {
		    						
		    	    				System.out.println(mac+"=re更新网关mac="+reoldGatewayMac+"=oldgatewaymac"+gatewaymac+"=gatewaymac="+"rssi="+rssi+"=currenttime"+currentTime+"=oldrssi"+oldRssi+"=rssi="+rssi);
		    					}
		    	    			task.ibeaconService.updateRecevied(re);
							}else  {
//							}else if(gatewaymac.equals(reoldGatewayMac)) { //旧的网关等于新来的网关才进行更新
								Received re = new Received();
								re.setMac(mac);
		    	    			re.setUpdatetime(currentTime);
		    	    			
//		    	    			如果更新时间超过10秒，信号值很强的数据如-23，继续更新新来的网关
		    	    			
		    	    			
		    	    			if(mac.equals("AC233FA018E3")) {
		    						
		    						System.out.println(mac+"=re更新时间mac"+reoldGatewayMac+"=oldgatewaymac"+gatewaymac+"=gatewaymac="+"rssi="+rssi+"=currenttime"+currentTime+"=oldrssi"+oldRssi+"=rssi="+rssi);
		    					}
		    	    			task.ibeaconService.updateRecevied(re);
							}
//							更新,rssi信号值最强或者插入ib中数据，条件更据时间段进行操作
//							List datas = task.ibeaconService.findGatewayByMac(mac,ibStartTime,ibfutureTime);  //ib中查询
//							（在一直接受数据中新增sql如果该mac存在则更新，否则进行添加）
							IbeaconGateway ib = new IbeaconGateway(); 
							ib.setMac(mac);
							ib.setGatewaymac(gatewaymac);
							List<Map> ibDatas = task.ibeaconService.findIbGatewayByMac(ib);
							
							
							if(ibDatas.size()>0) {
								updateRssiAndBattery( currentTime, mac, battery, gatewaymac, rssi);
							}else if(ibDatas.size()==0){
								persistence(currentTime, type, mac, rssi, battery, gatewaymac);
							}
						}
					}else {
//						没有接收到信号，或者长时间没有接收到，又突然出现的情况
//					超过前后3秒没有改标签更新，那么就不更新
//						1.数据未进行初始化
						List<Map> receviedData = task.ibeaconService.findReceviedByMac(mac);
						if(receviedData.size() ==0 ) {
//							3.如果没有传递mac过来，同样该sql数据长度为0， 将re表中网关数据更新时间设置为空，其他time.mac.rssi。不变，则不显示该点位数据，并且报警
							Received re = new Received();
//							System.out.println("===执行首次保存");
				    		re.setCreatetime(currentTime);
				    		re.setGatewaymac(gatewaymac);
//				    		TODO测试时候注意更新时间是否更新对点位存在影响
//				    		re.setUpdatetime(currentTime);
				    		re.setMac(mac);
				    		re.setRssi(rssi);
				    		task.ibeaconService.saveMac(re);
				    		
//						2.如果长时间没有更新，但是移动之后又出现。则直接更新即可，前面下次循环逻辑会自动处理(有信号mac传递过来，才会执行该sql)
						}else if (receviedData.size()>0) {
							Received re = new Received();
							re.setMac(mac);
	    	    			re.setUpdatetime(currentTime);
	    	    			re.setRssi(rssi);
	    	    			re.setGatewaymac(gatewaymac);
//	    	    			System.out.println("===长时间移动更新"+"currnettime"+currentTime+"=mac="+mac+"gatewaymac="+gatewaymac);
	    	    			task.ibeaconService.updateRecevied(re);
							
						}

						
					}
					*/
					
					
					
					
					
//					2.2 根据具体单位时间获取数据，可用作显示一天路径，数据更为准确  =====TODO
					/*String startTime = timeUtile.timeReduce(currentTime, 24*60*60);
					List findByTime = task.ibeaconService.find(mac, startTime, currentTime);
					if (findByTime.size() != 0) {
					    	JSONArray gatewayDatas= JSONArray.parseArray(JSON.toJSONString(findByTime));
//					    	3.获取api中最大rssi，与数据库比较，并添加到数据库
//					    	默认初始值
					    	int maxRssi = -10000;
					    	String newGateway = "";
					    	String newMac = "";
					    	String maxRssiTime = "";
//					    	数据库中数据进行比较获取一分钟内最大rssi值================》》使用程序代码找出最大rssi值效率比查询使用快
					    	for (int j = 0; j < gatewayDatas.size(); j++) {
					    		String oldTime = gatewayDatas.getJSONObject(j).getString("timestamp");
					    		String oldGatewayMac = gatewayDatas.getJSONObject(j).getString("gatewaymac");
					    		String oldMac = gatewayDatas.getJSONObject(j).getString("mac");
					    		String oldRssi = gatewayDatas.getJSONObject(j).getString("rssi");
//					    		获取标签在一分钟内最大rssi值对应的网关
					    		int oldRssi1 = Integer.parseInt(oldRssi);
					    		if(oldRssi!="" && maxRssi<oldRssi1) {
					    			maxRssi = oldRssi1;
					    			newGateway = oldGatewayMac;
					    			newMac = oldMac;
					    			maxRssiTime = oldTime;
					    		}
					    	}
//					    	数据库循环结束返回最大rssi值,所对应的的网关
//					    	System.out.println(newGateway+"=newgateway=maxRssi"+maxRssi+"=mac"+newMac);
					    	int apiRssi = Integer.parseInt(rssi);
					    	if(mac.equals(newMac) && apiRssi > maxRssi) {
					    		System.out.println("数据库中最大rssi,与api拦截数据比较"+"apiRssi="+apiRssi+"maxRssi="+maxRssi+"需要添加新"
					    				+ "的最大ri的mac="+mac);
//					    		持久化到数据库 
//					    		进一步缩减，通过对rssi值判断以及当前网关和数据库已存网关判断，如果一致并且rssi最大只需要修改rssi值即可。如果网关不一致，重新添加。
					    		if(!newGateway.equals(gatewaymac)) {
					    			persistence(currentTime, type, newMac, rssi, battery, gatewaymac);
					    		}else {
//					    			更新rssi和电量
//					    			3分钟内的更新rssi时间>>>更新时间，、数据首次创建时间不变
					    			updateRssiAndBattery(currentTime,mac,battery,gatewaymac,rssi,maxRssiTime);
					    		}
					    	}
					  //初始或者超过单位时间更新数据
					}else {
//					初始设定一个固定电量
						System.out.println("初始数据库进行添加数据===============");
						persistence(currentTime, type, mac, rssi, battery, gatewaymac);
					}
					*/
					
					
					

				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception  //此处异常，会出现订阅失败情况，ap不间断掉线
				LogUtil.logger.error(e+"=mqtt获取数据解析过程中出现异常，此处进行捕获，避免长连接中断，无法再次订阅");
			}
	        
			
			
	        
				
			
			
		}
    }
    
//	根据ap获取楼层信息
	private Map<String,String> getFloorByRedis(Received re) {
	   
//	   ap更新缓存存在一分钟时间差
	   Map gateway_info_ib = new HashMap<>();
	   try {
		
		   Map<Object, Object> hmgate = task.redisUtil.hmget(re.getGatewaymac());
		   
		   if(null != hmgate && !hmgate.isEmpty()) {
			   Gateway gate_way = (Gateway)hmgate.get("gate");
			   String floor = gate_way.getFloor();
			   String mapx = gate_way.getMapx();
			   String mapy = gate_way.getMapy();
			   
			   String gatewayflag = gate_way.getGatewayflag();
			   
			   gateway_info_ib.put("rssi", re.getRssi());
			   gateway_info_ib.put("gatewaymac", re.getGatewaymac());
			   gateway_info_ib.put("mac", re.getMac());
			   gateway_info_ib.put("mapx", mapx);
			   gateway_info_ib.put("mapy", mapy);
			   gateway_info_ib.put("floor", floor);
			   gateway_info_ib.put("gatewayflag", gatewayflag);
		   }
		   
	   	} catch (Exception e) {
	   		// TODO: handle exception
	   	}
	  
				   
		return gateway_info_ib;
	}
    
    
//    告警历史记录
    private void warnHistory(String currentTime,String battery,String gatewaymac,String hum_f,String mac,String temp_f) {
//		告警数据实时记录
		Humiture humit = new Humiture();
		HygrothermographWarn hw = new HygrothermographWarn();
		hw.setCurrentTime(currentTime);
		hw.setElectric(battery);
		hw.setGatewaymac(gatewaymac);
		hw.setHumidity(hum_f);
		hw.setMac(mac);
		hw.setTemperature(temp_f);
//		仪器项目地址编号,数据默认地址收集，不可修改
		humit.setAddress(UserParameterField.address);
		List<Humiture> queryhum = task.hygrothermographService.queryhum(humit);
		
		queryhum.parallelStream().filter(q -> mac.equals(q.getMac())).map(qh->{
			String temperaturefitted = qh.getTemperaturefitted();
			String humidityfitted = qh.getHumidityfitted();
			
//			获取温度阀值
			String s1 = "";
	    	String s2 = "";
	    	double w1 = 0;
	    	double w2 = 0;
			if(!StringUtils.isEmpty(temperaturefitted)) {
				String[] split = temperaturefitted.split("~");
		    	
//					阀值必须完整
		    	for(String d : split){
		    		if(StringUtils.isEmpty(s1)) {
		    			s1 = d; 
		    		}else{
		    			s2 = d;
		    		}
		    	}
		    	 w1 = Double.parseDouble(s1);
		    	 w2 = Double.parseDouble(s2);
		    	if(w1>w2) {
		    		w1 = Double.parseDouble(s1);
		    		w2 = Double.parseDouble(s2);
		    	}else {
		    		w1 = Double.parseDouble(s2);
		    		w2 = Double.parseDouble(s1);
		    	}
			}
//			获取湿度阀值
			String s3 = "";
			String s4 = "";
			double w3 = 0;
			double w4 = 0;
			if(!StringUtils.isEmpty(humidityfitted)) {
				String[] split = humidityfitted.split("~");
		    	
//				阀值必须完整
		    	for(String d : split){
		    		if(StringUtils.isEmpty(s3)) {
		    			s3 = d; 
		    		}else{
		    			s4 = d;
		    		}
		    	}
		    	 w3 = Double.parseDouble(s3);
		    	 w4 = Double.parseDouble(s4);
		    	if(w3>w4) {
		    		w3 = Double.parseDouble(s3);
		    		w4 = Double.parseDouble(s4);
		    	}else {
		    		w3 = Double.parseDouble(s4);
		    		w4 = Double.parseDouble(s3);
		    	}
	    	
			}
//			添加各类告警，各类告警分别添加（不抽取公共代码）
			double temp_value = Double.parseDouble(temp_f);
			double hum_value = Double.parseDouble(hum_f);
//			高温告警
			if(temp_value>w1) {
				String warnnum = "1";
				hw.setWarnnum(warnnum);
				insertWarnValue(hw);
//			解除高温告警
			}else {
				String warnnum = "1";
				String warnnum_relieve = "101";
				hw.setWarnnum(warnnum);
				hw.setRelieveWarn(warnnum_relieve);
				updateWarnValue(hw);
			}
			
//			低温告警
			if(temp_value<w2) {
				String warnnum = "2";
				hw.setWarnnum(warnnum);
				insertWarnValue(hw);
//			解除低温告警
			}else{
				String warnnum = "2";
				String warnnum_relieve = "102";
				hw.setWarnnum(warnnum);
				hw.setRelieveWarn(warnnum_relieve);
				updateWarnValue(hw);
			}
			
//			高湿告警
			if(hum_value>w3) {
				String warnnum = "3";
				hw.setWarnnum(warnnum);
				insertWarnValue(hw);
//			解除高湿告警
			}else {
				String warnnum = "3";
				String warnnum_relieve = "103";
				hw.setWarnnum(warnnum);
				hw.setRelieveWarn(warnnum_relieve);
				updateWarnValue(hw);
			}
			
//			低湿告警
			if(hum_value<w4) {
				String warnnum = "4";
				hw.setWarnnum(warnnum);
				insertWarnValue(hw);
//			解除低湿告警
			}else {
				String warnnum = "4";
				String warnnum_relieve = "104";
				hw.setWarnnum(warnnum);
				hw.setRelieveWarn(warnnum_relieve);
				updateWarnValue(hw);
			}
			
//			低电告警
			if(!StringUtils.isEmpty(hw.getElectric())) {
				Integer ele = Integer.parseInt(hw.getElectric());
				if(ele<=UserParameterField.S1_electric) {
					String warnnum = "14";
					hw.setWarnnum(warnnum);
					insertWarnValue(hw);
//			低电告警解除
				}else {
					String warnnum = "14";
					String warnnum_relieve = "114";
					hw.setWarnnum(warnnum);
					hw.setRelieveWarn(warnnum_relieve);
					updateWarnValue(hw);
				}
			}
			return 1;
		}).collect(Collectors.toList());
    }
//   添加告警数值
    private void insertWarnValue(HygrothermographWarn hw) {
		List<Map> queryHwByTime = task.hyWwarnService.queryHwByTime(hw);
		queryHwByTime.parallelStream().map(a -> {
    		Long i = (Long)a.get("count(*)");
    		if(i==0) {
    			task.hyWwarnService.insertHyWarn(hw);
			}
    		return 1;
    	}).collect(Collectors.toList());
    }
//    修改解除告警数值
    private void updateWarnValue(HygrothermographWarn hw) {
    	List<Map> queryHwByTime = task.hyWwarnService.queryHwByTime(hw);
		queryHwByTime.parallelStream().map(a -> {
    		Long i = (Long)a.get("count(*)");
    		if(i==1) {
//    			解除告警值
    			hw.setRelieveTemp(hw.getTemperature());
    			hw.setRelieveHum(hw.getHumidity());
    			hw.setRelieveTime(hw.getCurrentTime());
    			
//    			清空原有对象缓存数据，赋值查询条件对象
    			hw.setHumidity("");
    			hw.setTemperature("");
    			hw.setCurrentTime("");
    			
    			task.hyWwarnService.updateHyWarn(hw);
			}
    		return 1;
    	}).collect(Collectors.toList());
    }
    
    
//  更新温湿度仪器
    private List<Map> updateTemphum(String mac,String startime,String currentTime) {
		List<Map> queryhyDatas =task.hygrothermographService.queryHyByTime(mac,startime,currentTime);
		return queryhyDatas;
    }
    
//    更新位置信息
    private void updateReceived(String rssi,String currentTime,String gatewaymac,String mac,String ele) {
    	Received re = new Received();
    	re.setMac(mac);
    	re.setUpdatetime(currentTime);
    	re.setGatewaymac(gatewaymac);
    	re.setRssi(rssi);
    	re.setBattery(ele);
    	task.ibeaconService.updateRecevied(re);
    }
    
    
    
    
//  获取缓存历史12秒内数据，并且更新
    private void getReadisDatas(Map<Object,Object> gatewaymacByMac,String currentTime,String mac) throws ParseException {
    	
    	Iterator<Map.Entry<Object, Object>> iterator = gatewaymacByMac.entrySet().iterator();
		 String expressTimeduce = timeUtile.timeReduce(currentTime,ParameterField.redis_express_disstime_ago);
		 Map<String,Object> mac2Gatewaymac = new HashMap<String,Object>();
        
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            
            if("expressTime".equals(key)) {
				continue;
			 }
		   Map gatewaymac_key_value = (Map)entry.getValue();
		   String currentTime_redis_gatewaymac = (String) gatewaymac_key_value.get("currentTime");
//			获取所有已经缓存redis并且符合条件10秒内的ap
		   if(currentTime_redis_gatewaymac.compareTo(expressTimeduce)>=0) {
			   mac2Gatewaymac.put(entry.getKey().toString(),gatewaymac_key_value);
			}
        }
        
      //定时清除缓存redis,保证ap数据实时性
        delap(mac);
//       更新
    	task.redisUtil.hmset(mac,mac2Gatewaymac,10);	
    	task.redisUtil.expire(mac,10);
    }
    
    
//    清楚原有缓存中ap
    private void delap(String mac) {
    	task.redisUtil.del(mac);
    }
    
    
    
    
    
    
    
    
    
//    时间锁待用区-缓存
    private void updateRedis(String rssi,String currentTime,String gatewaymac,String mac) {
    	Map map = new HashMap<>();
		map.put("rssi", rssi);
		map.put("currentTime", currentTime);
		Map<String,Object> mac2Gatewaymac = new HashMap<String,Object>();
		mac2Gatewaymac.put("expressTime",currentTime);//设定失效时间
		mac2Gatewaymac.put(gatewaymac,map);
		task.redisUtil.hmset(mac,mac2Gatewaymac,10);//有效期参数无效，多线程一直更新导致，只更新其中一个
		task.redisUtil.expire(mac,10);
    }
    
//    更新多个网关同时在线缓存
    private void updateRedisMore(String rssi,String currentTime,String gatewaymac,String mac) {
    	Map map = new HashMap<>();
    	map.put("rssi", rssi);
    	map.put("currentTime", currentTime);
    	Map<String,Object> mac2Gatewaymac = new HashMap<String,Object>();
    	mac2Gatewaymac.put(gatewaymac,map);
    	task.redisUtil.hmset(mac,mac2Gatewaymac,10);//有效期参数无效，多线程一直更新导致	
    	task.redisUtil.expire(mac,10);
    }
    
    
    
    
//  持久化到数据库
    public void persistence(String currentTime,String type,String mac,String rssi,String battery,String gatewaymac) {
//		持久化到数据库 
		IbeaconGateway ibeacon = new IbeaconGateway();
		ibeacon.setTimestamp(currentTime);
		ibeacon.setType(type);
	  	ibeacon.setMac(mac);
	//  	通过两个网关进行比较之间距离，距离越小并且网关发生变动，则表示改为可移动标签
	//  	三角定位第一步计算距离。
			/*d = 10^((abs(RSSI) - A) / (10 * n))
					d - 计算所得距离
					RSSI - 接收信号强度（负值）
					A - 发射端和接收端相隔1米时的信号强度(52)
					n - 环境衰减因子*/
	  	double rs = Double.parseDouble(rssi);
	  	double Rssi = Math.abs(rs);
	  	double power = (Rssi - 52) / (10.0 * 3.2);
	  	String location=String.valueOf(Math.pow(10, power));
	  	String distance = location.substring(0,3);
	//  	System.out.println("首次或者一分钟之后更新的数据");
			/*带入两个点的D和RSSI,计算就可以得出A和N了.A好像是代表1米处的衰减值,因为当RSSI=A时,D=1.
			N代表空间障碍物衰减因子.分开放空间,半开放空间和全封闭空间,跟材质也有关系*/
	  	ibeacon.setRssi(rssi);
	//  	写入当前数据组网关距离
	  	ibeacon.setDistance(distance);
	//	    电量通过特定数值一直按每月减少多少数值，写入数据库
	  	ibeacon.setBattery(battery);
	//		数据放入网关中，跟网关建立多对多关系
	  	ibeacon.setGatewaymac(gatewaymac);
	  	ibeacon.setMaxRssiUpdateTime(currentTime);
	  	task.ibeaconService.insert(ibeacon);
  }
//  更新rssi和电量以及maxrssi当前时间
    public void updateRssiAndBattery(String currentTime,String mac,String battery,String gatewaymac,String rssi){
  		IbeaconGateway ibeacon = new IbeaconGateway();
	//    	更新时间
		ibeacon.setMaxRssiUpdateTime(currentTime);
		
		ibeacon.setRssi(rssi);
		double rs = Double.parseDouble(rssi);
	  	double Rssi = Math.abs(rs);
	  	double power = (Rssi - 52) / (10.0 * 3.2);
	  	String location=String.valueOf(Math.pow(10, power));
	  	String distance = location.substring(0,3);
		ibeacon.setDistance(distance);
	//		    	电量通过特定数值一直按每月减少多少数值，写入数据库
//		ibeacon.setBattery(battery);
		ibeacon.setMac(mac);
		ibeacon.setGatewaymac(gatewaymac);
		
		task.ibeaconService.updateRssi(ibeacon);

  }
//  更新rssi和当前时间
    public void updateMaxRssiAndTime(String maxRssiUpdateTime,String mac,String gatewaymac,String rssi,String oldmaxRssiUpdateTime){
	  	double rs = Double.parseDouble(rssi);
	  	double Rssi = Math.abs(rs);
	  	double power = (Rssi - 52) / (10.0 * 3.2);
	  	String location=String.valueOf(Math.pow(10, power));
	  	String distance = location.substring(0,3);
	  	task.ibeaconService.updateRssiAndTime(mac,
				 gatewaymac, oldmaxRssiUpdateTime,
				 rssi, distance
				, maxRssiUpdateTime);
  	
  }
//有网关数据则更新，如果空间时间限定否则离线时间过长，则异常
    public void receivedGateWay(String gatewaymac,String currnettime) throws ParseException {
    	redisGateway(gatewaymac, currnettime);
	
  }     
//    mqtt实时更新网关数据
    public void redisGateway(String gatewaymac,String currnettime) {
    	
    	if(StringUtils.isEmpty(gatewaymac)==false) {
	    	Gateway gate = new Gateway(); 
    		gate.setGatewaymac(gatewaymac);
    		gate.setUpdatetime(currnettime);
    		
//    		缓存坐标
    		List<Gateway> allGatewayDatas = task.gatewayService.searchAllGateway();
			
			allGatewayDatas.parallelStream().filter(g -> gatewaymac.equals(g.getGatewaymac()))
			.map(gates -> {
				
				//    		TODO0921放入坐标,视图算法,避免跨楼层
				boolean gatewaymac_map = gates.getGatewaymac().equals(gatewaymac);
    			if(gatewaymac_map) {
    				gate.setMapx(gates.getMapx());
    				gate.setMapy(gates.getMapy());
    				gate.setFloor(gates.getFloor());
//    				放入过道信息
    				gate.setGatewayflag(gates.getGatewayflag());
    			}
				
				return -1;
			}).collect(Collectors.toList());
    		

    		
    		Map map = new HashMap();
//    		放入gate参数值
        	map.put("gate",gate);
        	
        	//    	通过hash获取值
	    	Map<Object, Object> object = task.redisUtil.hmget(gatewaymac);
	    	if(object == null || object.isEmpty()) {  
//	        	初始化缓存过程中-->未直接更新数据库-->因此网关会陆陆续续开始在线（非同时刻）
	    		map.put("expirationTime", currnettime);
	        	Object object1 = task.redisUtil.hmset(gatewaymac,map);
	    	}else{
	    		//异步持久化到数据库同时更新redis缓存
	    		
//				获取一级缓存数据 TODO一级缓存失效时间 
				GatewayGuavaCache gateCache = new GatewayGuavaCache();
	    		
	    		object.forEach((key,value) -> {
	    			

	    			if("expirationTime".equals(key)) {
	    				try {
	    					 //ap更新redis缓存  ,分时、错峰,一分钟以上更新ap缓存和数据库
	    					String futureTime = "";
							if(gatewaymac.compareTo(ParameterField.gatewaymac_comp)>0) {
								futureTime = timeUtile.timeReduce(currnettime,ParameterField.GATEWAYDELAYTIME);
							}else {
								futureTime = timeUtile.timeReduce(currnettime,ParameterField.GATEWAYDELAYTIME1);
							}
							Boolean date = timeUtile.compareDateByString(futureTime,value+"")>0?true:false;
							
							if(date) {
								
//								一级缓存网关
								Object cache = gateCache.getCache(gatewaymac);
								Gateway gatewayCache = (Gateway)cache;
								
								
								if(gatewayCache.getGatewaymac() == null) {
									
									allGatewayDatas.parallelStream().filter(g -> gatewaymac.equals(g.getGatewaymac()))
									.map(gates -> {
//										放入一级缓存
										gateCache.putCache(gatewaymac, gates);
										
										return -1;
									}).collect(Collectors.toList()); //加上集合线程安全
									
									
									
//									异步更新数据库网关数据
									GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
									cf.completFutureUpdateGateway(gatewaymac, currnettime);
									
//									再次获取缓存数据
									Object getCacheByPut = gateCache.getCache(gatewaymac);
								}
								
								
								
								/*更新一级缓存（避免HashMap重复查询数据库，减少空间和时间的消耗
									如一个网关查询并且缓存其他网关再次访问直接从缓存中获取即可）或数据库
									
									缓存优化，可取消ap缓存，减轻压力
								 */
								map.put("expirationTime", currnettime);
								
								Object object1 = task.redisUtil.hmset(gatewaymac,map);
								

								
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							LogUtil.logger.error(MqttPushCallback.class+"="+e);
						}
	    				
	    			}
	    		});

	    	}
	    	

    	}
    	
    }     
    
//    =======================================================================================TODO210708测试区  
    
//  休眠重连
    public void allGatewayAndClient(MqttPushClient instance,MqttClient client1){
    	int count = 0;
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
    	
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if(!StringUtils.isEmpty(ga.getFloor())) {
    			String new_gatewayfree = ga.getGatewayfree();
    			String new_gatewaymac = ga.getGatewaymac();
    			if(new_gatewayfree.equals("3")) {
    				new_gatewaymac = ga.getGatewaymac().toLowerCase();
    			}else if(new_gatewayfree.equals("2")){
    				new_gatewaymac = ga.getGatewaymac().toUpperCase();//泸州医院所有网关广播都是大写。注意区分
    			}else if(new_gatewayfree.equals("1")) {
//        			华西无订阅mqtt会导致长时间断线情况
//        			continue;
    				new_gatewaymac = ga.getGatewaymac().toLowerCase();  
    			}
    			
    			
    			try {
    				
//        			怀疑订阅时间过长导致
    				instance.sendClient("/gw/"+new_gatewaymac+"/action/response",client1);
    				instance.sendClient("/gw/"+new_gatewaymac+"/status", client1);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				LogUtil.logger.info("/gw/"+new_gatewaymac+"/status"+"订阅失败！");
    			}
    			
    		}
    		return -1;
    	}).collect(Collectors.toList());
    	
    	
    }
//  11楼
    public void allGatewayAndClient11(MqttPushClient instance,MqttClient client1){
    	int count = 0;
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
    	
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if(!StringUtils.isEmpty(ga.getFloor()) && ga.getFloor().equals("11")) {
    			String new_gatewayfree = ga.getGatewayfree();
    			String new_gatewaymac = ga.getGatewaymac();
    			if(new_gatewayfree.equals("3")) {
    				new_gatewaymac = ga.getGatewaymac().toLowerCase();
    			}else if(new_gatewayfree.equals("2")){
    				new_gatewaymac = ga.getGatewaymac().toUpperCase();//泸州医院所有网关广播都是大写。注意区分
    			}else if(new_gatewayfree.equals("1")) {
//        			华西无订阅mqtt会导致长时间断线情况
    				new_gatewaymac = ga.getGatewaymac().toLowerCase();  
    			}
    			
    			
    			try {
    				
//        			怀疑订阅时间过长导致
    				instance.sendClient("/gw/"+new_gatewaymac+"/action/response",client1);
    				instance.sendClient("/gw/"+new_gatewaymac+"/status", client1);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				LogUtil.logger.info("/gw/"+new_gatewaymac+"/status"+"订阅失败！");
    			}
    			
    		}
    		return -1;
    	}).collect(Collectors.toList());
    	
    	
    }
//  12楼
    public void allGatewayAndClient12(MqttPushClient instance,MqttClient client1){
    	int count = 0;
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
    	
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if(!StringUtils.isEmpty(ga.getFloor()) && ga.getFloor().equals("12")) {
    			String new_gatewayfree = ga.getGatewayfree();
    			String new_gatewaymac = ga.getGatewaymac();
    			if(new_gatewayfree.equals("3")) {
    				new_gatewaymac = ga.getGatewaymac().toLowerCase();
    			}else if(new_gatewayfree.equals("2")){
    				new_gatewaymac = ga.getGatewaymac().toUpperCase();//泸州医院所有网关广播都是大写。注意区分
    			}else if(new_gatewayfree.equals("1")) {
//        			华西无订阅mqtt会导致长时间断线情况
//        			continue;
    				new_gatewaymac = ga.getGatewaymac().toLowerCase();  
    			}
    			
    			
    			try {
    				
//        			怀疑订阅时间过长导致
    				instance.sendClient("/gw/"+new_gatewaymac+"/action/response",client1);
    				instance.sendClient("/gw/"+new_gatewaymac+"/status", client1);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				LogUtil.logger.info("/gw/"+new_gatewaymac+"/status"+"订阅失败！");
    			}
    			
    		}
    		return -1;
    	}).collect(Collectors.toList());
    	
    	
    }
//  13楼
    public void allGatewayAndClient13(MqttPushClient instance,MqttClient client1){
    	int count = 0;
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
    	
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if(!StringUtils.isEmpty(ga.getFloor()) && ga.getFloor().equals("13")) {
    			String new_gatewayfree = ga.getGatewayfree();
    			String new_gatewaymac = ga.getGatewaymac();
    			if(new_gatewayfree.equals("3")) {
    				new_gatewaymac = ga.getGatewaymac().toLowerCase();
    			}else if(new_gatewayfree.equals("2")){
    				new_gatewaymac = ga.getGatewaymac().toUpperCase();//泸州医院所有网关广播都是大写。注意区分
    			}else if(new_gatewayfree.equals("1")) {
//        			华西无订阅mqtt会导致长时间断线情况
//        			continue;
    				new_gatewaymac = ga.getGatewaymac().toLowerCase();  
    			}
    			
    			
    			try {
    				
//        			怀疑订阅时间过长导致
    				instance.sendClient("/gw/"+new_gatewaymac+"/action/response",client1);
    				instance.sendClient("/gw/"+new_gatewaymac+"/status", client1);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				LogUtil.logger.info("/gw/"+new_gatewaymac+"/status"+"订阅失败！");
    			}
    			
    		}
    		return -1;
    	}).collect(Collectors.toList());
    	
    	
    }
    
//=======================================================================================测试区
    
    
//   订阅掉线部分网关
   /* public void connectGatewayByClient(MqttPushClient instance,MqttClient client1,String gatewaymac) throws InterruptedException, MqttException {
//    	订阅字段统一小写
    	gatewaymac = gatewaymac.toLowerCase();
    	instance.sendClient("/gw/"+gatewaymac+"/action/response",client1);
		instance.sendClient("/gw/"+gatewaymac+"/status", client1);
    }*/
    
    
    
//    重启医院对应的所有网关（医院之间重启互相不干绕）
    public void restartAllGateway(MqttPushClient mqclient,int address) throws InterruptedException, MqttException {
    	
    	/*
//    	TimeUnit.SECONDS.sleep(10);//睡眠20秒执行
    	*/
    	String location = address+"";
    	List<Gateway> datas = task.gatewayService.searchGateway(location);
    	
    	String gatewaymac = "";
    	String gatewayfree = "";
    	for (Gateway gateway : datas) {
//    		排除地图数据影响
    		if(StringUtils.isEmpty(gateway.getGatewaymac())) {
    			continue;
    		}
    		
    		gatewayfree = gateway.getGatewayfree();
    		int parseInt = Integer.parseInt(gatewayfree);
    		if(address==parseInt && parseInt == 2 ) {
    			gatewaymac = gateway.getGatewaymac().toUpperCase();
    		}else if(address==parseInt && parseInt==1){
    			gatewaymac = gateway.getGatewaymac().toLowerCase();
    		}else if(address==parseInt && parseInt==3){
    			gatewaymac = gateway.getGatewaymac().toLowerCase();
    		}
    		
    		mqclient.sendClient("/gw/"+gatewaymac+"/action/response",mqclient.client);
    		mqclient.publish("/gw/"+gatewaymac+"/action", sendRestart);
		}
    }
//    重启单个/多个网关
    public void restartAloneGateway(MqttPushClient mqclient,List<String> gatewaymac,int address) throws InterruptedException, MqttException {
		
//		assert  gatewaymac instanceof List;
    	
    	if(gatewaymac.size()>0) {
    		
    		for (String gate : gatewaymac) {
    			String gateway = "";
    			if(address == 2) {
    				gateway = gate.toUpperCase();
    				
    			}else {
    				gateway = gate.toLowerCase();
    			}
    			
        		mqclient.publish("/gw/"+gateway+"/action", sendRestart);
    		}
    	}
    }
    
    
//    升级所有网关
    public void upgrade(MqttPushClient mqclient,String gatewaymac) throws InterruptedException, MqttException{
    	

//    	lan口局域网远程下载固件
//    	String urlpath = "http://172.20.29.2:8009/huaxi/upgradeVersion/";
    	
//    	String Upgrade = "{ \"action\":\"upgrade\", \"type\":\"self\", \"urlpath\":\""+urlpath+"\", \"filename\":\"thingoo-upgrade.bin\", \"isSave\":\"YES\", \"requestId\":\"xxx\" }";
    	
    	String Upgrade = "{ \"action\":\"upgrade\", \"type\":\"self\", \"urlpath\":\"\", \"filename\":\"thingoo-g1-c-v3.1.2.bin\", \"isSave\":\"YES\", \"requestId\":\"xxx\" }";
    	
    	LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    	map.put("action", "upgrade");
    	map.put("type", "self");
    	map.put("urlpath", "http://192.168.0.166:8009/huaxi/upgradeVersion/");
//    	map.put("filename", "thingoo-upgrade.bin");
    	map.put("filename", "thingoo-g1-c-v3.1.2.bin");
    	map.put("isSave", "YES");
    	map.put("requestId", "xxx");
    	
    	if("" != gatewaymac && "ac233fc03867".equals(gatewaymac)) {
    		mqclient.sendClient("/gw/"+gatewaymac+"/action/response",mqclient.client);
//    		mqclient.publish("/gw/"+gatewaymac+"/action", Upgrade);
    		mqclient.publish("/gw/"+gatewaymac+"/action", JSONObject.toJSONString(map));
    		
    		LogUtil.logger.info(JSONObject.toJSONString(map)+"==ac233fc03867");
    		
    	}
    	
    	

    }
    
//    修改网关配置
    public String upconfig(MqttPushClient mqclient,List<GatewayEdit> gateway){
    	
    	
    	
//    	默认配置
    	String disableLED = "YES"; //是否开启led灯
    	String isLongBright = "NO"; //闪烁开启
    	String isUploadUnkown = "YES"; 
    	String isUploadIBeacon = "default";
    	String isUploadS1 = "YES";
    	String isUploadGateway = "default";
    	String isauto = "YES"; //断线自动重启
    	String timeout = "30"; //自动重启时间,监听时间
    	
    	String publishTopic = "";
    	String subscribeTopic = "";
    	String responseTopic = "";
    	
    	String qos = GatewayParameterField.gateway_qos; //数据传输等级
    	String userName = "default"; //mqtt 服务器用户名
    	String passWord = "default"; //mqtt 服务器密码
    	
    	String hUrl = "default"; 
    	String httpUser = "default"; //http 用户名
    	String httpPass = "default"; //http 密码
    	
    	String macReg = GatewayParameterField.gateway_macReg; //mac过滤 ,非默认
    	
    	
    	String mqttUrl = GatewayParameterField.gateway_mUrl; //mqtt服务指向，非默认
    	
    	String port = GatewayParameterField.gateway_port; //mqtt服务指向，非默认
    	
    	

    	
    	for (GatewayEdit gatewayEdit : gateway) {
    		
    		String gatewaymac = gatewayEdit.getGatewaymac();
    		if(StringUtils.isEmpty(gatewaymac)) {
    			return "网关不正确";
    		}
    	 
   		 disableLED = StringUtils.isEmpty(gatewayEdit.getDisableLED())==true?"default":gatewayEdit.getDisableLED().toUpperCase();
   		 isLongBright = StringUtils.isEmpty(gatewayEdit.getIsLongBright())==true?"default":gatewayEdit.getIsLongBright().toUpperCase();
   		 isUploadUnkown = StringUtils.isEmpty(gatewayEdit.getIsUploadUnkown())==true?"default":gatewayEdit.getIsUploadUnkown().toUpperCase();
   		 isUploadIBeacon = StringUtils.isEmpty(gatewayEdit.getIsUploadIBeacon())==true?"default":gatewayEdit.getIsUploadIBeacon().toUpperCase();
   		 isUploadS1 = StringUtils.isEmpty(gatewayEdit.getIsUploadS1())==true?"default":gatewayEdit.getIsUploadS1().toUpperCase();
   		 isUploadGateway = StringUtils.isEmpty(gatewayEdit.getIsUploadGateway())==true?"default":gatewayEdit.getIsUploadGateway().toUpperCase();
   		 isauto = StringUtils.isEmpty(gatewayEdit.getIsauto())==true?"default":gatewayEdit.getIsauto().toUpperCase();
   		 timeout = StringUtils.isEmpty(gatewayEdit.getTimeout())==true?"30":gatewayEdit.getTimeout().toUpperCase();
   		 
   		 publishTopic = StringUtils.isEmpty(gatewayEdit.getPublishTopic())==true?"default":"/gw/"+gatewayEdit.getPublishTopic().toLowerCase()+"/status";
   		 subscribeTopic = StringUtils.isEmpty(gatewayEdit.getSubscribeTopic())==true?"default":"/gw/"+gatewayEdit.getSubscribeTopic().toLowerCase()+"/action";
   		 responseTopic = StringUtils.isEmpty(gatewayEdit.getResponseTopic())==true?"default":"/gw/"+gatewayEdit.getResponseTopic().toLowerCase()+"/action/response";
   		 
   		 qos = StringUtils.isEmpty(gatewayEdit.getQos())==true?"default":gatewayEdit.getQos().toUpperCase();
   		 userName = StringUtils.isEmpty(gatewayEdit.getUserName())==true?"default":gatewayEdit.getUserName().toUpperCase();
   		 passWord = StringUtils.isEmpty(gatewayEdit.getPassWord())==true?"default":gatewayEdit.getPassWord().toUpperCase();
   		 hUrl = StringUtils.isEmpty(gatewayEdit.getHUrl())==true?"default":gatewayEdit.getHUrl().toUpperCase();
   		 httpUser = StringUtils.isEmpty(gatewayEdit.getHttpUser())==true?"default":gatewayEdit.getHttpUser().toUpperCase();
   		 httpPass = StringUtils.isEmpty(gatewayEdit.getHttpPass())==true?"default":gatewayEdit.getHttpPass().toUpperCase();
   		 
//   	 服务器接受地址和端口
   		 port = StringUtils.isEmpty(gatewayEdit.getPort())==true?port:":"+gatewayEdit.getPort();
   		 mqttUrl = StringUtils.isEmpty(gatewayEdit.getMqttUrl())==true?mqttUrl:"tcp://"+gatewayEdit.getMqttUrl()+port;
   		 macReg = StringUtils.isEmpty(gatewayEdit.getMacReg())==true?macReg:gatewayEdit.getMacReg().toUpperCase();
   		 
   		 
   		 
   		 
   		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
   		LinkedHashMap<String, Object> commonMap = new LinkedHashMap<>();
   		LinkedHashMap<String, Object> scheduleMap = new LinkedHashMap<>();
   		LinkedHashMap<String, Object> mqttMap = new LinkedHashMap<>();
   		LinkedHashMap<String, Object> httpMap = new LinkedHashMap<>();
   		
   		map.put("action", "config");
   		map.put("takeEffectImmediately","YES");
   		
//   		参数map
   		commonMap.put("proto","default");
   		commonMap.put("uploadInterval","default");
   		commonMap.put("isFilterDupData","default");
   		commonMap.put("isOnlySpecialMac","default");
   		commonMap.put("macList","default");
   		commonMap.put("disableLED",disableLED);
   		commonMap.put("isLongBright",isLongBright);
   		commonMap.put("isUploadUnkown",isUploadUnkown);
   		commonMap.put("isUploadIBeacon",isUploadIBeacon);
   		commonMap.put("isUploadS1",isUploadS1);
   		commonMap.put("isUploadGateway",isUploadGateway);
   		commonMap.put("isauto",isauto);
   		commonMap.put("timeout",timeout);
//   	common参数	定时map
   		scheduleMap.put("istiming", "default");
   		scheduleMap.put("min", "default");
   		scheduleMap.put("hour", "default");
   		scheduleMap.put("week", "default");
   		
   		commonMap.put("schedule", scheduleMap);
   		commonMap.put("rssi","default");
   		commonMap.put("regex","default");
   		
//   	处理多个mac过滤
   		StringBuilder sb = new StringBuilder();
		if(macReg.length()>=8 && !macReg.equals("default")) {
   			for (int i = 0; i < macReg.length(); i++) {
				if(i%4==0 && i>0) {
					sb.append( ".*\\|^");
				}
				sb.append(macReg.charAt(i));
			}
   			macReg = "^"+sb+".*";
   		}
   		commonMap.put("macReg",macReg);
   		
   		commonMap.put("cacheTime","default");
   		commonMap.put("isLongFormat","default");
   		commonMap.put("isJsonFormat","default");
   		
   		map.put("common",commonMap);
//		mqttMap   		 
   		mqttMap.put("mUrl", mqttUrl);
   		mqttMap.put("urlpath", "default");
   		mqttMap.put("cafile", "default");
   		mqttMap.put("certfile", "default");
   		mqttMap.put("keyfile", "default");
   		mqttMap.put("keypass", "default");
   		mqttMap.put("publishTopic", publishTopic);
   		mqttMap.put("subscribeTopic", subscribeTopic);
   		mqttMap.put("responseTopic", responseTopic);
   		mqttMap.put("qos", qos);
   		mqttMap.put("userName", userName);
   		mqttMap.put("passWord", passWord);
   		mqttMap.put("clientID", "default");
   		
   		map.put("mqtt", mqttMap);
   		
//		httMap
   		httpMap.put("hUrl", hUrl);
   		httpMap.put("auth", "default");
   		httpMap.put("httpUser", httpUser);
   		httpMap.put("httpPass", httpPass);
   		map.put("http", httpMap);
   		
   	    map.put("requestId","b2c3d4e5-3424-4dca-32dc-12b73290cfed");
   	    mqclient.publish("/gw/"+gatewayEdit.getGatewaymac().toLowerCase()+"/action", JSONObject.toJSONString(map));
   	     
   	     
   	     
   	     LogUtil.logger.info(JSONObject.toJSONString(map)+"=sendConfig"+gatewayEdit.getMqttUrl());
   	     
    	}
    	
    	return "配置成功";
    		
    	
    	
    }
//    过滤下一秒时间，返回0向下继续执行，返回-1跳过执行。影响：多个ap同时接受数据，但只会获取最新首次传来ap和最强ap
    private Integer filterTime(Received re,timeUtiles times) {
		 
 	   Map cache_fc_map = (Map)glfc.getCache(re.getMac());
 	   Map map_lq = new HashMap();
 	   String floor_timeAdd_limit ="";
 		try {
 			floor_timeAdd_limit = times.timeAdd(times.getCurrenttime(), UserParameterField.TIME_EXP_CACHE);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			LogUtil.logger.error("IbeaconGatewayServiceImpl时间过滤异常"+e);
 		}
// 	    获取单位时间片最新ap及其rssi值数据,以及当前时间节点区间限制
   	   if(null !=cache_fc_map && !cache_fc_map.isEmpty() ) { 
   		   String fc_rssi = (String)cache_fc_map.get("rssi");
   		   String fc_tim = (String)cache_fc_map.get("time");
   		   String fc_ap = (String)cache_fc_map.get("ap");
   		   
   		   
   		   if(times.getCurrenttime().compareTo(fc_tim)<=0 && !StringUtils.isEmpty(fc_rssi) && !StringUtils.isEmpty(re.getRssi()) && Integer.parseInt(fc_rssi) < Integer.parseInt(re.getRssi())) {
   			   map_lq.put("ap", re.getGatewaymac());
    		   map_lq.put("rssi", re.getRssi());
    		   map_lq.put("time", fc_tim);//时间参数不变动
    		   glfc.putCache(re.getMac(), map_lq);
    		   return 0;
   		   }
   		   
//   	   超过时间节点重新获取最新ap数据（保证数据及时性），否则如未改变原有ap值，则时效性降低
   		   if(times.getCurrenttime().compareTo(fc_tim)>=0) {
   			   map_lq.put("ap", re.getGatewaymac());
    		   map_lq.put("rssi", re.getRssi());
    		   map_lq.put("time", floor_timeAdd_limit);//更改原有时间
   		   }else if(times.getCurrenttime().compareTo(fc_tim)<0){
   			   map_lq.put("time", fc_tim);//时间参数不变动
   			   map_lq.put("rssi", fc_rssi);
   			   map_lq.put("ap", fc_ap);
   			   glfc.putCache(re.getMac(), map_lq);
   			   return -1;
   		   }
/*   		TODO210616 提高多个ap同时数据传输
 * 			if(times.getCurrenttime().compareTo(fc_tim)>0) {
   			   map_lq.put("ap", re.getGatewaymac());
   			   map_lq.put("rssi", re.getRssi());
   			   map_lq.put("time", floor_timeAdd_limit);//更改原有时间
   		   }else if(times.getCurrenttime().compareTo(fc_tim)<=0){
   			   map_lq.put("time", fc_tim);//时间参数不变动
   			   map_lq.put("rssi", fc_rssi);
   			   map_lq.put("ap", fc_ap);
   			   glfc.putCache(re.getMac(), map_lq);
   			   return -1;
   		   }
*/   		   
   	   }else {
   		   map_lq.put("ap", re.getGatewaymac());
   		   map_lq.put("rssi", re.getRssi());
   		   map_lq.put("time", floor_timeAdd_limit);
   	   }
   	   glfc.putCache(re.getMac(), map_lq);
   	   return 0;
  
    }
    
    
//    redis缓存中获取e6标签
    private Long getRedisE6(String mac){
    	Map<Object, Object> e6 = task.redisUtil.hmget("E6");
    	long count = 0;
//    	缓存中获取e6标签
    	if(null != e6 && !e6.isEmpty()) {
    		List<Map> mac_map = (List<Map>) e6.get("e6_mac");
    		count = mac_map.stream().filter(asset -> mac.equals(asset.get("mac"))).count();
    	}else {
    		List<Map> findE6 = task.assetMapper.findE6();
    		count = findE6.stream().filter(asset -> mac.equals(asset.get("mac"))).count();
    		
//    		放入缓存中
    		Map map = new HashMap();
    		map.put("e6_mac", findE6);
    		Object object1 = task.redisUtil.hmset("E6",map);
    		
    	}
    	return count;
    }
    
    
}
package com.example.demo.mqtt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.aio.GatewayProgress;
import com.example.demo.cache.GatewayGuavaCache;
import com.example.demo.cache.GuavaCache;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.GatewayEdit;
import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.Received;
import com.example.demo.mapper.IAssetMapper;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.GatewayService;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.service.hygrothermographService;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;
import com.example.demo.util.parameter.GatewayParameterField;
import com.example.demo.util.parameter.ParameterField;
import com.example.demo.util.sockettherd.HexConvert;

@Component
public class MqttHandler0824gateway {
	
	
	@Autowired
	private IAssetMapper mapper;
	
	@Autowired
    private IbeaconGatewayService ibeaconService;
	
	@Autowired
    private GatewayService gatewayService;
	
	@Autowired
	private hygrothermographService hygrothermographService;
	
	@Resource
    private RedisUtils redisUtil;
	
	public static MqttHandler0824gateway task;
	
	public static timeUtiles timeUtile = new timeUtiles();
	
//	public static List<Gateway> gatewayDatas = new ArrayList<Gateway>();
//	时间测试
	public static long start;
	
	
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

private static final String String = null;
	
	//初始化
	
	@PostConstruct
    public void init() {    
		task = this;
    }
//    从mqtt中解析数据
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
		
		
		for (int i = 0; i < array.size(); i++) {
			
			
			
	        //注意：index中的内容带有中括号[]，所以要转化为JSONArray类型的对象
			String timestamp = array.getJSONObject(i).getString("timestamp");
			String type = array.getJSONObject(i).getString("type");
			String mac = array.getJSONObject(i).getString("mac");
			String rssi = array.getJSONObject(i).getString("rssi");
			
			
			String ele = array.getJSONObject(i).getString("battery");  //s1或ibeacon中电量获取
			
			
			start = System.nanoTime();
			
			
//			网关数据量越多，时间延迟越大
//			String currentTime = timeUtile.UTC2local(timestamp);
			
			
			String currentTime = timeUtile.getCurrenttime();
			
			
			
//			每个标签对应的网关保存下来
			if ("Gateway".equals(type)) {
				gatewaymac = mac;
				
				/*
				if("AC233FC07313".equals(gatewaymac)) {
					LogUtil.logger.info(gatewaymac+"=11上线时间="+currentTime);
				}
				if("AC233FC07257".equals(gatewaymac)) {
					LogUtil.logger.info(gatewaymac+"=12上线时间="+currentTime);
				}
				if("AC233FC03829".equals(gatewaymac)) {
					LogUtil.logger.info(gatewaymac+"=13上线时间="+currentTime);
				}
				*/
				
//				更新网关数据
//				long start1 = System.nanoTime();
				receivedGateWay(gatewaymac,currentTime);
//				System.out.println("网关更新后Done in "+(System.nanoTime()-MqttHandler.start)/1_000_000+" ms"); //Done in 14 ms ==》0ms
			}
	

			
			
			
			if (!"Gateway".equals(type)) {
				
				
				
//				通过查询一分钟内程序信号接受到的最小值，判断，是否存在，如果存在则判断最小值，否则跳转报警接口，进行报警
				
//				过滤掉非ac开头mac地址
				/*if(mac.indexOf(ParameterField.macCapitalizedfilter)!= 0 || mac.indexOf(ParameterField.macLowerfilter) != 0) {
					System.out.println(mac+"非正常mac不执行");
					
				}*/
				
//				区分温湿度与ibeacon定位
				
				/*温湿度数据处理
				 * 
				 * if("S1".equals(type)) {
					
					温度=23.739999999999997湿度=71.76=gatewaymacAC233FC00015=macAC233FA05462currenttime2019-10-21 15:28:55
							温度=22.91湿度=74.9=gatewaymacAC233FC00015=macAC233FA05466currenttime2019-10-21 15:28:55
							温度=2.92湿度=38.62=gatewaymacAC233FC01303=macAC233FA018EBcurrenttime2019-10-21 15:28:55
							温度=36.15湿度=32.24=gatewaymacAC233FC01303=macAC233FA018EEcurrenttime2019-10-21 15:28:55
							温度=2.94湿度=37.13=gatewaymacAC233FC01303=macAC233FA05334currenttime2019-10-21 15:28:56
							温度=21.54湿度=76.52=gatewaymacAC233FC01303=macAC233FA05463currenttime2019-10-21 15:28:55
							温度=20.14湿度=85.69=gatewaymacAC233FC012DD=macAC233FA05416currenttime2019-10-21 15:28:55
							温度=20.53湿度=86.01=gatewaymacAC233FC012DD=macAC233FA0546Acurrenttime2019-10-21 15:28:55
							温度=6.21湿度=79.94=gatewaymacAC233FC0128F=macAC233FA018EDcurrenttime2019-10-21 15:28:56
							温度=-21.58湿度=77.9=gatewaymacAC233FC0128F=macAC233FA0534Acurrenttime2019-10-21 15:28:56
					
					
					
//					温度
					String temperature = array.getJSONObject(i).getString("temperature");
//					湿度
					String humidity = array.getJSONObject(i).getString("humidity");
//					System.out.println("温度="+temperature+"湿度="+humidity+"=gatewaymac"+gatewaymac+"=mac"+mac+"currenttime"+currentTime);
//					每5分钟保存一次数据
					String startime = timeUtile.timeReduce(currentTime, ParameterField.tempHumTime);
					
					

					List<hygrothermograph> queryhyDatas = task.hygrothermographService.queryhy(mac,startime,currentTime);
					
//					System.out.println(queryhyDatas+"=queryhydatas");
					if(queryhyDatas.size()>0) {
//						System.out.println("已有数据需要等待5分鐘更新="+queryhyDatas);
						continue;
					}else {
						hygrothermograph hy = new hygrothermograph();
						hy.setCurrentTime(currentTime);
						hy.setGatewaymac(gatewaymac);
						hy.setHumidity(humidity);
						hy.setMac(mac);
						hy.setTemperature(temperature);
//						持久化数据
						task.hygrothermographService.saveHy(hy);
						
//						测试同时更新定位数据
						Received re = new Received();
			    		re.setCreatetime(currentTime);
			    		re.setGatewaymac(gatewaymac);
			    		re.setMac(mac);
			    		re.setRssi(rssi);
						task.ibeaconService.updateRecevied(re);
						
						continue; // 泸州测试
						
					}

				}*/
				
				
				/*if("AC233FB100EE".equals(mac)) {
					LogUtil.logger.info(mac+"="+array);
				}*/
				
				
				String rawData = array.getJSONObject(i).getString("rawData"); //非常规电量数据
				if(!StringUtils.isEmpty(rawData) && StringUtils.isEmpty(ele)) {
//					0201060303E1FF0D16E1FFA1026400EE00B13F23AC
					String battery_rawdata = rawData.substring(rawData.length()-16,rawData.length()-14);
					Integer battery_percentage = (int) (HexConvert.hexStringToDeci(battery_rawdata)*10);
					ele = battery_percentage+"";
				}

//				redis数据缓存 TODO 09-10内存溢出异常排查
				if(!StringUtils.isEmpty(gatewaymac)&&!StringUtils.isEmpty(mac)&&!StringUtils.isEmpty(rssi)){
//					0709 12:11 TODO		处理超强信号问题一直占用网关位置问题，redis缓存后可忽略该问题
					
//					  TODO0917移动效果测试，提升固定性，去掉最强
					  try {
						int reNewRssi = Integer.parseInt(rssi);
						reNewRssi = reNewRssi>=-35?reNewRssi=-45:reNewRssi;		 
						rssi = reNewRssi+"";
						
					} catch (Exception e) {
						// TODO: handle exception
						LogUtil.logger.error("替代超强信号rssi="+e);
					}
//					数据每次来临，便更新redis.因此缓存时间失效，故先获取，再更新mac所在网关，超过三个网关，精准三圆运算定位。
					
//					前后定时长排除，异常放电情况（移动情况考虑冲突，数据不及时更新）
					
					Map<Object, Object> gatewaymacByMac = task.redisUtil.hmget(mac);
					
					if(null != gatewaymacByMac && !gatewaymacByMac.isEmpty()) {
						
						Object object = gatewaymacByMac.get(gatewaymac);
						Map gatewaymacMap = (Map)object;
						
						
						/*加时间锁，30s秒后过期==》作用于3d
						  
						  TODO 0826 TEST
						  失效后更新缓存以及数据库
						  
								用于持久化，防止来回穿房间，ap信号增强导致
						  */
						String oldExpressTime = (String)gatewaymacByMac.get("expressTime");
						String expressTimeAdd = timeUtile.timeAdd(oldExpressTime,ParameterField.redis_gatewaymac_disstime);
//						更新过期时间
						
						if(currentTime.compareTo(expressTimeAdd)>=0) {
							/*if("AC233FB100EE".equals(mac)) {
								LogUtil.logger.info("20秒定时更新="+mac+"="+expressTimeAdd+"=time"+currentTime+"=hmget"+gatewaymacByMac);
							}*/
							
							/*TODO0919修改注释掉过期重新更新的情况
							 * 
							 * updateRedis( rssi, currentTime, gatewaymac, mac);
							updateReceived(rssi,currentTime,gatewaymac,mac,ele);*/
							
//							没有过期前添加3次最新rssi缓存
							
							continue;
						}else {
//							没过期的情况进行多个网关进行更新，10秒一次数据精准定位只能移动后，接受到新的网关才能计算
//							updateRedisMoreDevice缓存多个rssi值取其平均值最大
							
							updateRedisMore( rssi, currentTime, gatewaymac, mac);
							updateReceived(rssi,currentTime,gatewaymac,mac,ele);
							continue;
						}
					
//						首次接受到并且更新
					}else if(null == gatewaymacByMac || gatewaymacByMac.isEmpty()){
//						LogUtil.logger.info("==首次直接更新"+gatewaymacByMac+"=mac"+mac);
						updateRedis( rssi, currentTime, gatewaymac, mac);
						updateReceived(rssi,currentTime,gatewaymac,mac,ele);
						
//						缓存持久化到数据库==》作用于3d
						continue;
					}
//					后续不再执行
					continue;
				}
//				LogUtil.logger.info(currentTime+"===截至线");
				
				continue;
				
				
				
/*
 * 0827 TODO 
 * 				2.1网关变化，以及rssi变化存入数据，就近网关显示备份使用
				获取最新数据的sql，并执行保存，向后衍生3秒是过滤掉程序和sql查询时间的影响
				String startTime = timeUtile.timeReduce(currentTime, 3);
				String endTime = timeUtile.timeAdd(currentTime, 3);

				redis缓存，单点位多网关匹配,信号强，持久化数据库,缓存时间2秒
				
				
//				re中查询
				List<Map> macdata = task.ibeaconService.findMacByMac(mac,endTime,startTime); //re中查询
//				2.更新数据，显示当前位置
				
				
				
				if(mac.equals("C2021A0000E1") || mac.equals("AC233FB100EE")||mac.equals("C2021A0000E3")) {
					
					LogUtil.logger.info("gatewaymac="+gatewaymac+"=mac"+mac+"currentTime="+currentTime+"rssi="+rssi+
							"=macdata"+macdata);
				}
				
				
				 
				
				
				
				if(macdata.size()>0) {
//					及时更新数据情况分为：
//					1.传递不同网关，相同mac
					
//					2.传递相同网关，相同mac
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
//							相同网关，mac则更新time
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
//						}else if(gatewaymac.equals(reoldGatewayMac)) { //旧的网关等于新来的网关才进行更新
							Received re = new Received();
							re.setMac(mac);
	    	    			re.setUpdatetime(currentTime);
	    	    			
//	    	    			如果更新时间超过10秒，信号值很强的数据如-23，继续更新新来的网关
	    	    			
	    	    			
	    	    			if(mac.equals("AC233FA018E3")) {
	    						
	    						System.out.println(mac+"=re更新时间mac"+reoldGatewayMac+"=oldgatewaymac"+gatewaymac+"=gatewaymac="+"rssi="+rssi+"=currenttime"+currentTime+"=oldrssi"+oldRssi+"=rssi="+rssi);
	    					}
	    	    			task.ibeaconService.updateRecevied(re);
						}
//						更新,rssi信号值最强或者插入ib中数据，条件更据时间段进行操作
//						List datas = task.ibeaconService.findGatewayByMac(mac,ibStartTime,ibfutureTime);  //ib中查询
//						（在一直接受数据中新增sql如果该mac存在则更新，否则进行添加）
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
//					没有接收到信号，或者长时间没有接收到，又突然出现的情况
//				超过前后3秒没有改标签更新，那么就不更新
//					1.数据未进行初始化
					List<Map> receviedData = task.ibeaconService.findReceviedByMac(mac);
					if(receviedData.size() ==0 ) {
//						3.如果没有传递mac过来，同样该sql数据长度为0， 将re表中网关数据更新时间设置为空，其他time.mac.rssi。不变，则不显示该点位数据，并且报警
						Received re = new Received();
//						System.out.println("===执行首次保存");
			    		re.setCreatetime(currentTime);
			    		re.setGatewaymac(gatewaymac);
//			    		TODO测试时候注意更新时间是否更新对点位存在影响
//			    		re.setUpdatetime(currentTime);
			    		re.setMac(mac);
			    		re.setRssi(rssi);
			    		task.ibeaconService.saveMac(re);
			    		
//					2.如果长时间没有更新，但是移动之后又出现。则直接更新即可，前面下次循环逻辑会自动处理(有信号mac传递过来，才会执行该sql)
					}else if (receviedData.size()>0) {
						Received re = new Received();
						re.setMac(mac);
    	    			re.setUpdatetime(currentTime);
    	    			re.setRssi(rssi);
    	    			re.setGatewaymac(gatewaymac);
//    	    			System.out.println("===长时间移动更新"+"currnettime"+currentTime+"=mac="+mac+"gatewaymac="+gatewaymac);
    	    			task.ibeaconService.updateRecevied(re);
						
					}

					
				}
				*/
				
				
				
				
				
//				2.2 根据具体单位时间获取数据，可用作显示一天路径，数据更为准确  =====TODO
				/*String startTime = timeUtile.timeReduce(currentTime, 24*60*60);
				List findByTime = task.ibeaconService.find(mac, startTime, currentTime);
				if (findByTime.size() != 0) {
				    	JSONArray gatewayDatas= JSONArray.parseArray(JSON.toJSONString(findByTime));
//				    	3.获取api中最大rssi，与数据库比较，并添加到数据库
//				    	默认初始值
				    	int maxRssi = -10000;
				    	String newGateway = "";
				    	String newMac = "";
				    	String maxRssiTime = "";
//				    	数据库中数据进行比较获取一分钟内最大rssi值================》》使用程序代码找出最大rssi值效率比查询使用快
				    	for (int j = 0; j < gatewayDatas.size(); j++) {
				    		String oldTime = gatewayDatas.getJSONObject(j).getString("timestamp");
				    		String oldGatewayMac = gatewayDatas.getJSONObject(j).getString("gatewaymac");
				    		String oldMac = gatewayDatas.getJSONObject(j).getString("mac");
				    		String oldRssi = gatewayDatas.getJSONObject(j).getString("rssi");
//				    		获取标签在一分钟内最大rssi值对应的网关
				    		int oldRssi1 = Integer.parseInt(oldRssi);
				    		if(oldRssi!="" && maxRssi<oldRssi1) {
				    			maxRssi = oldRssi1;
				    			newGateway = oldGatewayMac;
				    			newMac = oldMac;
				    			maxRssiTime = oldTime;
				    		}
				    	}
//				    	数据库循环结束返回最大rssi值,所对应的的网关
//				    	System.out.println(newGateway+"=newgateway=maxRssi"+maxRssi+"=mac"+newMac);
				    	int apiRssi = Integer.parseInt(rssi);
				    	if(mac.equals(newMac) && apiRssi > maxRssi) {
				    		System.out.println("数据库中最大rssi,与api拦截数据比较"+"apiRssi="+apiRssi+"maxRssi="+maxRssi+"需要添加新"
				    				+ "的最大ri的mac="+mac);
//				    		持久化到数据库 
//				    		进一步缩减，通过对rssi值判断以及当前网关和数据库已存网关判断，如果一致并且rssi最大只需要修改rssi值即可。如果网关不一致，重新添加。
				    		if(!newGateway.equals(gatewaymac)) {
				    			persistence(currentTime, type, newMac, rssi, battery, gatewaymac);
				    		}else {
//				    			更新rssi和电量
//				    			3分钟内的更新rssi时间>>>更新时间，、数据首次创建时间不变
				    			updateRssiAndBattery(currentTime,mac,battery,gatewaymac,rssi,maxRssiTime);
				    		}
				    	}
				  //初始或者超过单位时间更新数据
				}else {
//				初始设定一个固定电量
					System.out.println("初始数据库进行添加数据===============");
					persistence(currentTime, type, mac, rssi, battery, gatewaymac);
				}
				*/
				
				
				

			}
			
			
	        
				
			
			
		}
    }
//    更新位置信息
    private void updateReceived(String rssi,String currentTime,String gatewaymac,String mac,String ele) {
    	Received re = new Received();
		re.setMac(mac);
		re.setUpdatetime(currentTime);
		re.setGatewaymac(gatewaymac);
		re.setRssi(rssi);
		
		re.setBattery(ele);
		
//		task静态变量进行处理，数据过多导致 --猜疑
		task.ibeaconService.updateRecevied(re);
    }
    
//    时间锁待用区-缓存
    private void updateRedis(String rssi,String currentTime,String gatewaymac,String mac) {
    	Map map = new HashMap<>();
		map.put("rssi", rssi);
		map.put("currentTime", currentTime);
		Map<String,Object> mac2Gatewaymac = new HashMap<String,Object>();
		mac2Gatewaymac.put("expressTime",currentTime);//设定失效时间
		mac2Gatewaymac.put(gatewaymac,map);
		task.redisUtil.hmset(mac,mac2Gatewaymac,ParameterField.redis_gatewaymac_disstime);//有效期参数无效，多线程一直更新导致	
		
    }
//    更新多个网关同时在线缓存
    private void updateRedisMore(String rssi,String currentTime,String gatewaymac,String mac) {
    	Map map = new HashMap<>();
    	map.put("rssi", rssi);
    	map.put("currentTime", currentTime);
    	
    	Map<String,Object> mac2Gatewaymac = new HashMap<String,Object>();
    	mac2Gatewaymac.put(gatewaymac,map);
    	task.redisUtil.hmset(mac,mac2Gatewaymac,ParameterField.redis_gatewaymac_disstime);//有效期参数无效，多线程一直更新导致	
    	
    }
//   TODO0919修改 3次更新标签（存在标签中途有几次未及时发送数据过来的可能）
    private void updateRedisMoreDevice(String rssi,String currentTime,String gatewaymac,String mac) {
    	Map<Object, Object> hmget = task.redisUtil.hmget(mac);//有效期参数无效，多线程一直更新导致
    	
    	if(MapUtils.isEmpty(hmget)) {
    		Map gateway_device = (Map)hmget.get(mac);
    		Map rssi_device = (Map)gateway_device.get(gatewaymac);
    		
    		Map<String,Object> mac2Gatewaymac = new HashMap<String,Object>();
//    		连续保存rssi值
    		if(rssi_device.containsKey("rssi") && !rssi_device.containsKey("rssi1")) {
    			Map map = new HashMap<>();
    	    	map.put("rssi1", rssi);
    	    	map.put("currentTime", currentTime);
    	    	mac2Gatewaymac.put(gatewaymac,map);
    	    	task.redisUtil.hmset(mac,mac2Gatewaymac,ParameterField.redis_gatewaymac_disstime);//20秒过期
    		}else if(rssi_device.containsKey("rssi1")) {
    			Map map = new HashMap<>();
    	    	map.put("rssi2", rssi);
    	    	map.put("currentTime", currentTime);
    	    	mac2Gatewaymac.put(gatewaymac,map);
    	    	task.redisUtil.hmset(mac,mac2Gatewaymac,ParameterField.redis_gatewaymac_disstime);//10秒过期
    		}
    		
    	}
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
    		

    		
    		Map map = new HashMap();
/*//    		放入gate参数值
        	map.put("gate",gate);*/
        	
        	//    	通过hash获取值
	    	Map<Object, Object> object = task.redisUtil.hmget(gatewaymac);
	    	if(object == null || object.isEmpty()) {  
//	        	初始化缓存过程中-->未直接更新数据库-->因此网关会陆陆续续开始在线（非同时刻）
	    		map.put("expirationTime", currnettime);
	        	map.put("gate",gate);
	        	Object object1 = task.redisUtil.hmset(gatewaymac,map);
	    	}else{
	    		//异步持久化到数据库同时更新redis缓存
	    		
//				获取一级缓存数据 TODO一级缓存失效时间 
				GatewayGuavaCache gateCache = new GatewayGuavaCache();
	    		
	    		object.forEach((key,value) -> {
	    			

	    			if("expirationTime".equals(key)) {
	    				try {
	    					 //ap更新redis缓存  ,分时、错峰
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
									List<Gateway> allGatewayDatas = task.gatewayService.searchAllGateway();
									
									List<String> mac = allGatewayDatas.parallelStream().filter(g -> gatewaymac.equals(g.getGatewaymac()))
									.map(gates -> {
//										放入一级缓存
										gateCache.putCache(gatewaymac, gates);
										
										//    		TODO0921放入坐标,视图算法
										boolean gatewaymac_map = gates.getGatewaymac().equals(gatewaymac);
						    			if(gatewaymac_map) {
						    				gate.setMapx(gates.getMapx());
						    				gate.setMapy(gates.getMapy());
						    			}
										
										return gates+"";
									}).collect(Collectors.toList());
									
									
									
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
								
//								缓存ap坐标
								map.put("gate",gate);
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
    
    
    public void allGatewayAndClient11(MqttPushClient instance,MqttClient client1){
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
//    	MqttHandler.setGatewayDatas(gatewayDatas); 
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if("11".equals(ga.getFloor())) {
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
    	
    	
    	
    	
    	
    	/*String gatewaymac = "";
    	String gatewayfree = "";
    	
    	for (Gateway gateway : gatewayDatas) {
    		gatewayfree = gateway.getGatewayfree();
    		
    		
//    		排除地图数据影响
    		
    		if("AC233FC03829".equals(gateway.getGatewaymac())) {
    			
    			LogUtil.logger.info("/gw/"+gatewaymac+"/status"+"数据排除前");
    		}
    		
    		
    		if(StringUtils.isEmpty(gateway.getGatewaymac())) {
    			continue;
    		}
    		
    		if(gatewayfree.equals("3")) {
    			gatewaymac = gateway.getGatewaymac().toLowerCase();  
    		}else if(gatewayfree.equals("2")){
    			gatewaymac = gateway.getGatewaymac().toUpperCase();//泸州医院所有网关广播都是大写。注意区分
    		}else if(gatewayfree.equals("1")) {
//    			华西无订阅mqtt会导致长时间断线情况
//    			continue;
    			gatewaymac = gateway.getGatewaymac().toLowerCase();  
    		}
    		
    		instance.sendClient("/gw/"+gatewaymac+"/action/response",client1);
    		instance.sendClient("/gw/"+gatewaymac+"/status", client1);
    		
    		if("AC233FC03829".toLowerCase().equals(gatewaymac)) {
    			
    			LogUtil.logger.info("/gw/"+gatewaymac+"/status"+"数据排除后");
    		}
    		if("AC233FC03839".toLowerCase().equals(gatewaymac)) {
    			
    			LogUtil.logger.info("/gw/"+gatewaymac+"/status");
    		}
    		
    		
    		count ++;
    		
    	}
    	 */
    	
    	
    }
//    订阅掉线重新订阅所有网关,13楼
    public void allGatewayAndClient13(MqttPushClient instance,MqttClient client1){
    	int count = 0;
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
//    	MqttHandler.setGatewayDatas(gatewayDatas); 
    	
    	/*for (Gateway gateway : gatewayDatas) {
    		if(StringUtils.isEmpty(gateway.getGatewaymac())) {
    			continue;
    		}
    		if(!gateway.getFloor().equals("12") && !gateway.getFloor().equals("13")){
    			System.out.println(gateway.getFloor());
    			count ++;
    		}
		}
    	System.out.println(count+"=其他楼层进行订阅数量");*/
    	
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if("13".equals(ga.getFloor())) {
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
//    12楼
    public void allGatewayAndClient12(MqttPushClient instance,MqttClient client1){
    	int count = 0;
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
    	
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if("12".equals(ga.getFloor())) {
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
//   b2楼
    public void allGatewayAndClientb2(MqttPushClient instance,MqttClient client1){
    	int count = 0;
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
    	
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if("b2".equals(ga.getFloor())) {
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
//   b1楼
    public void allGatewayAndClientb1(MqttPushClient instance,MqttClient client1){
    	int count = 0;
//    	从缓存中获取数据
    	List<Gateway> gatewayDatas = task.gatewayService.searchAllGateway();
    	
    	
    	gatewayDatas.stream().filter(g -> StringUtils.isEmpty(g.getGatewaymac()) == false)
    	.map(ga -> {
    		if("b1".equals(ga.getFloor())) {
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
//    			AC233FC011F2不执行重启，网关本身问题
    			/*if(gateway.getGatewaymac().toUpperCase().equals("AC233FC011F2")) {
    				continue;
    			}*/
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
    	
    	
    	String mUrl = GatewayParameterField.gateway_mUrl; //mqtt服务指向，非默认
    	
    	

    	
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
   		 
   		 mUrl = StringUtils.isEmpty(gatewayEdit.getMqttUrl())==true?mUrl:"tcp://"+gatewayEdit.getMqttUrl()+":8009";
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
   		mqttMap.put("mUrl", mUrl);
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
   	     
   	     
   	     
   	    LogUtil.logger.info(JSONObject.toJSONString(map)+"=sendConfig");
   	     
    	}
    	
    	return "配置成功";
    		
    	
    	
    }
    
    
    
}
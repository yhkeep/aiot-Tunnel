package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Interceptor.PassToken;
import com.example.demo.cache.GatewayGuavaCache;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.domain.HygrothermographWarn;
import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.Received;
import com.example.demo.mqtt.MqttPushCallback;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.GatewayService;
import com.example.demo.service.HygrothermographWarnService;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.service.hygrothermographService;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;
import com.example.demo.util.parameter.ParameterField;
import com.example.demo.util.parameter.UserParameterField;

@RestController
public class ApiServerController {
	
	@Autowired
    private IbeaconGatewayService ibeaconService;
	@Autowired
	private hygrothermographService hygrothermographService;
	@Autowired
	private HygrothermographWarnService hyWwarnService;
	
	@Resource
    private RedisUtils redisUtil;
	@Autowired
	private GatewayService gatewayService; 
	
	private timeUtiles timeUtile = new timeUtiles();
	
//	获取api数据，通过拦截获取request流数据
	@PassToken  
    @RequestMapping(value = "/apiservice")
    public String apiService(HttpServletRequest request) throws ParseException {
    	String body = "";
//    	不考虑线程安全加快速度
    	StringBuilder stringBuilder = new StringBuilder();
    	BufferedReader bufferedReader = null;
    	InputStream inputStream = null;
    	try {
    	    inputStream = request.getInputStream();
    	    if (inputStream != null) {
    	        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    	        char[] charBuffer = new char[128];
    	        int bytesRead = -1;
    	        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
    	            stringBuilder.append(charBuffer, 0, bytesRead);
    	        }
    	    } else {
    	        stringBuilder.append("");
    	    }
    	} catch (IOException ex) {
    	    ex.printStackTrace();
    	} finally {
    	    if (inputStream != null) {
    	        try {
    	            inputStream.close();
    	        }
    	        catch (IOException e) {
    	        	LogUtil.logger.error("apiS数据接受/inputStream异常:"+e);
    	        }
    	    }
    	    if (bufferedReader != null) {
    	        try {
    	            bufferedReader.close();
    	        }
    	        catch (IOException e) {
    	        	LogUtil.logger.error("apiS数据接受/bufferedReader异常:"+e);
    	        }
    	    }
    	}
    	
    	JSONArray array= new JSONArray();
    	
    	try {
    		body = stringBuilder.toString();
    		
        	String str = body.toString();
        	
//    		  转为普通数组
    		List list = JSONObject.parseArray(str);
//    		  转为jsonarray
    		array= JSONArray.parseArray(JSON.toJSONString(list));
    		
    		
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.info("com.alibaba.fastjson.JSONException: unclosed.str");
		}
    	
    	
//		防止空指针
		if (array == null || array.size() == 0){
			return "";
		}
		
		
		
		
		
		
		
		
//		解析数据
		String gatewaymac= "";
		
		
/*		
 * [{"gatewayLoad":0.07,"gatewayFree":97,"type":"Gateway","mac":"AC233FC0380F","timestamp":"2020-06-03T02:46:18Z"},
 * {"rssi":-67,"bleName":"S1","temperature":24.24,"humidity":53.55,"type":"S1","battery":100,"mac":"AC233FA018E7","timestamp":"2020-06-03T02:46:18Z"}]
 * */

//		LogUtil.logger.info("http=data"+array);
		
/*		
//		从http获取数据
		for (int i = 0; i < array.size(); i++) {
			

			
			
	        //注意：index中的内容带有中括号[]，所以要转化为JSONArray类型的对象
			String timestamp = array.getJSONObject(i).getString("timestamp");
			String type = array.getJSONObject(i).getString("type");
			String mac = array.getJSONObject(i).getString("mac");
			String rssi = array.getJSONObject(i).getString("rssi");
			String battery = array.getJSONObject(i).getString("battery");
			
			String currentTime = timeUtile.getCurrenttime();
			
//			每个标签对应的网关保存下来
			if ("Gateway".equals(type)) {
				gatewaymac = mac;
//				更新网关数据
				receivedGateWay(gatewaymac,currentTime);
			}
			

			if (!"Gateway".equals(type)) {
//				通过查询一分钟内程序信号接受到的最小值，判断，是否存在，如果存在则判断最小值，否则跳转报警接口，进行报警
				
//				区分温湿度与ibeacon定位
				
				if(type.equals("S1")) {

//					温度
					String temperature = array.getJSONObject(i).getString("temperature");
//					湿度
					String humidity = array.getJSONObject(i).getString("humidity");
					
//					每5分钟保存一次数据
					String startime = timeUtile.timeReduce(currentTime, 5*60);
					List<hygrothermograph> queryhyDatas = hygrothermographService.queryhy(mac,startime,currentTime);
					if(queryhyDatas.size()>0) {
						continue;
					}else {
						hygrothermograph hy = new hygrothermograph();
						hy.setCurrentTime(currentTime);
						hy.setGatewaymac(gatewaymac);
						hy.setHumidity(humidity);
						hy.setMac(mac);
						hy.setTemperature(temperature);
//						持久化数据
						hygrothermographService.saveHy(hy);
						
						continue;  //泸州温湿度数据
						
					}

				}
				
//				2.1网关变化，以及rssi变化存入数据，就近网关显示备份使用
//				获取最新数据的sql，并执行保存，向后衍生3秒是过滤掉程序和sql查询时间的影响
				String startTime = timeUtile.timeReduce(currentTime, 3);
				String endTime = timeUtile.timeAdd(currentTime, 3);

//				re中查询
				List<Map> macdata = ibeaconService.findMacByMac(mac,endTime,startTime); //re中查询
//				2.更新数据，显示当前位置
				
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
						if (mac.equals(reoldMac) && !gatewaymac.equals(reoldGatewayMac) && reNewRssi>oldRssi) {
							Received re = new Received();
							re.setMac(mac);
	    	    			re.setUpdatetime(currentTime);
	    	    			re.setGatewaymac(gatewaymac);
	    	    			re.setRssi(rssi);
	    	    			if(mac.equals("AC233FA018E3")) {
	    						
	    	    				System.out.println(mac+"=re更新网关mac="+reoldGatewayMac+"=oldgatewaymac"+gatewaymac+"=gatewaymac="+"rssi="+rssi+"=currenttime"+currentTime+"=oldrssi"+oldRssi+"=rssi="+rssi);
	    					}
	    	    			ibeaconService.updateRecevied(re);
						}else {
							Received re = new Received();
							re.setMac(mac);
	    	    			re.setUpdatetime(currentTime);
	    	    			if(mac.equals("AC233FA018E3")) {
	    						
	    						System.out.println(mac+"=re更新时间mac"+reoldGatewayMac+"=oldgatewaymac"+gatewaymac+"=gatewaymac="+"rssi="+rssi+"=currenttime"+currentTime+"=oldrssi"+oldRssi+"=rssi="+rssi);
	    					}
	    	    			ibeaconService.updateRecevied(re);
						}
//						更新,rssi信号值最强或者插入ib中数据，条件更据时间段进行操作
//						List datas = ibeaconService.findGatewayByMac(mac,ibStartTime,ibfutureTime);  //ib中查询
//						（在一直接受数据中新增sql如果该mac存在则更新，否则进行添加）
						IbeaconGateway ib = new IbeaconGateway(); 
						ib.setMac(mac);
						ib.setGatewaymac(gatewaymac);
						List<Map> ibDatas = ibeaconService.findIbGatewayByMac(ib);
						
						
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
					List<Map> receviedData = ibeaconService.findReceviedByMac(mac);
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
			    		ibeaconService.saveMac(re);
			    		
//					2.如果长时间没有更新，但是移动之后又出现。则直接更新即可，前面下次循环逻辑会自动处理(有信号mac传递过来，才会执行该sql)
					}else if (receviedData.size()>0) {
						Received re = new Received();
						re.setMac(mac);
    	    			re.setUpdatetime(currentTime);
    	    			re.setRssi(rssi);
    	    			re.setGatewaymac(gatewaymac);
//    	    			System.out.println("===长时间移动更新"+"currnettime"+currentTime+"=mac="+mac+"gatewaymac="+gatewaymac);
    	    			ibeaconService.updateRecevied(re);
						
					}

					
				}
				
				
				
				
				
				
//				2.2 根据具体单位时间获取数据，可用作显示一天路径，数据更为准确  =====TODO
				String startTime = timeUtile.timeReduce(currentTime, 24*60*60);
				List findByTime = ibeaconService.find(mac, startTime, currentTime);
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
				
				
				
				

			}
			
			
	        

				
			
			
		}*/

    	
		
		
for (int i = 0; i < array.size(); i++) {
			
			
	        //注意：index中的内容带有中括号[]，所以要转化为JSONArray类型的对象
			String timestamp = array.getJSONObject(i).getString("timestamp");
			String type = array.getJSONObject(i).getString("type");
			String mac = array.getJSONObject(i).getString("mac");
			String rssi = array.getJSONObject(i).getString("rssi");
			String battery = array.getJSONObject(i).getString("battery");
			
			

			
			
//			网关数据量越多，时间延迟越大
//			String currentTime = timeUtile.UTC2local(timestamp);
			
			
			String currentTime = timeUtile.getCurrenttime();
			
//			每个标签对应的网关保存下来
			if ("Gateway".equals(type)) {
//				LogUtil.logger.info(array+"接受到的数据");
				gatewaymac = mac;
				
//				更新网关数据
				receivedGateWay(gatewaymac,currentTime);
			}
			

			
			
			
			if (!"Gateway".equals(type)) {
//				通过查询一分钟内程序信号接受到的最小值，判断，是否存在，如果存在则判断最小值，否则跳转报警接口，进行报警
				
//				过滤掉非ac开头mac地址
				/*if(mac.indexOf(ParameterField.macCapitalizedfilter)!= 0 || mac.indexOf(ParameterField.macLowerfilter) != 0) {
					System.out.println(mac+"非正常mac不执行");
					
				}*/
				
//				区分温湿度与ibeacon定位
				
				/*if(type.equals("S1")) {
					
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
					
					

					List<hygrothermograph> queryhyDatas = hygrothermographService.queryhy(mac,startime,currentTime);
					
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
						hygrothermographService.saveHy(hy);
						
//						测试同时更新定位数据
						Received re = new Received();
			    		re.setCreatetime(currentTime);
			    		re.setGatewaymac(gatewaymac);
			    		re.setMac(mac);
			    		re.setRssi(rssi);
						ibeaconService.updateRecevied(re);
						
						continue; // 泸州测试
						
					}

				}*/
				
				if("S1".equals(type)) {
					
					  
//					温度
					String temperature = array.getJSONObject(i).getString("temperature");
//					湿度
					String humidity = array.getJSONObject(i).getString("humidity");
//					电量
//					String battery = array.getJSONObject(i).getString("battery");
					
//					四舍五入,保留一位小数
					double hum = Double.parseDouble(humidity);
					double temp = Double.parseDouble(temperature);
					
	            	String hum_f = String.format("%.1f",hum);
	            	String temp_f = String.format("%.1f",temp);
					
					
//					默认每10分钟保存一次温湿度数据(持久化)
					List<Humiture> humSection = hygrothermographService.getHumSection(mac);
					int temp_hum_savetim = 10*60;
					for (Humiture h : humSection) {
						String saveinterval = h.getSaveinterval();
						temp_hum_savetim = Integer.parseInt(saveinterval)*60;
					}
//					String startime = timeUtile.timeReduce(currentTime, ParameterField.tempHumTime);
					String startime = timeUtile.timeReduce(currentTime, temp_hum_savetim);
					
					List<Map> queryhyDatas =hygrothermographService.queryHyByTime(mac,startime,currentTime);
			    	Long l = 0L ;
			    	for (Map map : queryhyDatas) {
			    		l = (Long) map.get("count(*)"); 
			    	}
			    	if(l>0) {
						continue;
					}else {
						Hygrothermograph hy = new Hygrothermograph();
						
						hy.setCurrentTime(currentTime);
						hy.setGatewaymac(gatewaymac);
						hy.setHumidity(hum_f);
						hy.setMac(mac);
						hy.setTemperature(temp_f);
						hy.setElectric(battery);
//						持久化数据
						hygrothermographService.saveHy(hy);

//						告警历史记录
						warnHistory(currentTime,battery,gatewaymac,hum_f,mac,temp_f);
						
						continue;
						
					}

				}
				
				
				
				
//				redis数据缓存
				if(!StringUtils.isEmpty(gatewaymac)&&!StringUtils.isEmpty(mac)&&!StringUtils.isEmpty(rssi)){
//					最大rssi放在首位，方便后续读取
					Map map = new HashMap<>();
					map.put("rssi", rssi);
					map.put("currentTime", currentTime);
					
					Map<String,Object> mac2Gatewaymac = new HashMap<String,Object>();
					
					mac2Gatewaymac.put(gatewaymac,map);
					
					redisUtil.hmset(mac,mac2Gatewaymac,ParameterField.redis_gatewaymac_disstime);
					
					
				}
				
				
				
				
				
//				2.1网关变化，以及rssi变化存入数据，就近网关显示备份使用
//				获取最新数据的sql，并执行保存，向后衍生3秒是过滤掉程序和sql查询时间的影响
				String startTime = timeUtile.timeReduce(currentTime, 3);
				String endTime = timeUtile.timeAdd(currentTime, 3);

//				redis缓存，单点位多网关匹配,信号强，持久化数据库,缓存时间2秒
				
				
//				re中查询
				List<Map> macdata = ibeaconService.findMacByMac(mac,endTime,startTime); //re中查询
//				2.更新数据，显示当前位置
				
				
				
				/*if(mac.equals("C2021A0000E1") || mac.equals("AC233FB100EE")||mac.equals("C2021A0000E3")) {
					
					LogUtil.logger.info("gatewaymac="+gatewaymac+"=mac"+mac+"currentTime="+currentTime+"rssi="+rssi+
							"=macdata"+macdata);
				}
				*/
				
				/*if( mac.equals("AC233FB100EE") || mac.equals("C2021A0000E3") || mac.equals("C2021A0000E1")){
					
					LogUtil.logger.info("gatewaymac="+gatewaymac+"=mac"+mac+"currentTime="+currentTime+"rssi="+rssi+
							"=macdata"+macdata);
				}*/
				 
				
				
				
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
						if (mac.equals(reoldMac) && !gatewaymac.equals(reoldGatewayMac) && reNewRssi>oldRssi) {
							Received re = new Received();
							re.setMac(mac);
	    	    			re.setUpdatetime(currentTime);
	    	    			re.setGatewaymac(gatewaymac);
	    	    			re.setRssi(rssi);
	    	    			/*if(mac.equals("AC233FA018E3")) {
	    						
	    	    				System.out.println(mac+"=re更新网关mac="+reoldGatewayMac+"=oldgatewaymac"+gatewaymac+"=gatewaymac="+"rssi="+rssi+"=currenttime"+currentTime+"=oldrssi"+oldRssi+"=rssi="+rssi);
	    					}*/
	    	    			ibeaconService.updateRecevied(re);
						}else  {
//						}else if(gatewaymac.equals(reoldGatewayMac)) { //旧的网关等于新来的网关才进行更新
							Received re = new Received();
							re.setMac(mac);
	    	    			re.setUpdatetime(currentTime);
	    	    			
//	    	    			如果更新时间超过10秒，信号值很强的数据如-23，继续更新新来的网关
	    	    			
	    	    			
	    	    			/*if(mac.equals("AC233FA018E3")) {
	    						
	    						System.out.println(mac+"=re更新时间mac"+reoldGatewayMac+"=oldgatewaymac"+gatewaymac+"=gatewaymac="+"rssi="+rssi+"=currenttime"+currentTime+"=oldrssi"+oldRssi+"=rssi="+rssi);
	    					}*/
	    	    			ibeaconService.updateRecevied(re);
						}
//						更新,rssi信号值最强或者插入ib中数据，条件更据时间段进行操作
//						List datas = ibeaconService.findGatewayByMac(mac,ibStartTime,ibfutureTime);  //ib中查询
//						（在一直接受数据中新增sql如果该mac存在则更新，否则进行添加）
						IbeaconGateway ib = new IbeaconGateway(); 
						ib.setMac(mac);
						ib.setGatewaymac(gatewaymac);
						List<Map> ibDatas = ibeaconService.findIbGatewayByMac(ib);
						
						
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
					List<Map> receviedData = ibeaconService.findReceviedByMac(mac);
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
			    		ibeaconService.saveMac(re);
			    		
//					2.如果长时间没有更新，但是移动之后又出现。则直接更新即可，前面下次循环逻辑会自动处理(有信号mac传递过来，才会执行该sql)
					}else if (receviedData.size()>0) {
						Received re = new Received();
						re.setMac(mac);
    	    			re.setUpdatetime(currentTime);
    	    			re.setRssi(rssi);
    	    			re.setGatewaymac(gatewaymac);
//    	    			System.out.println("===长时间移动更新"+"currnettime"+currentTime+"=mac="+mac+"gatewaymac="+gatewaymac);
    	    			ibeaconService.updateRecevied(re);
						
					}

					
				}
				
				
				
				
				
				
//				2.2 根据具体单位时间获取数据，可用作显示一天路径，数据更为准确  =====TODO
				/*String startTime = timeUtile.timeReduce(currentTime, 24*60*60);
				List findByTime = ibeaconService.find(mac, startTime, currentTime);
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
		
    	return "request datas";
    }
//    持久化到数据库
    public void persistence(String currentTime,String type,String mac,String rssi,String battery,String gatewaymac) {
//		持久化到数据库 
		IbeaconGateway ibeacon = new IbeaconGateway();
		ibeacon.setTimestamp(currentTime);
		ibeacon.setType(type);
    	ibeacon.setMac(mac);
//    	通过两个网关进行比较之间距离，距离越小并且网关发生变动，则表示改为可移动标签
//    	三角定位第一步计算距离。
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
//    	System.out.println("首次或者一分钟之后更新的数据");
		/*带入两个点的D和RSSI,计算就可以得出A和N了.A好像是代表1米处的衰减值,因为当RSSI=A时,D=1.
		N代表空间障碍物衰减因子.分开放空间,半开放空间和全封闭空间,跟材质也有关系*/
    	ibeacon.setRssi(rssi);
//    	写入当前数据组网关距离
    	ibeacon.setDistance(distance);
//	    电量通过特定数值一直按每月减少多少数值，写入数据库
    	ibeacon.setBattery(battery);
//		数据放入网关中，跟网关建立多对多关系
    	ibeacon.setGatewaymac(gatewaymac);
    	ibeacon.setMaxRssiUpdateTime(currentTime);
        ibeaconService.insert(ibeacon);
    }
//    更新rssi和电量以及maxrssi当前时间
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
		
		ibeaconService.updateRssi(ibeacon);

    }
//    更新rssi和当前时间
    public void updateMaxRssiAndTime(String maxRssiUpdateTime,String mac,String gatewaymac,String rssi,String oldmaxRssiUpdateTime){
    	double rs = Double.parseDouble(rssi);
    	double Rssi = Math.abs(rs);
    	/*double power = (Rssi - 52) / (10.0 * 3.2);
    	String location=String.valueOf(Math.pow(10, power));
    	String distance = location.substring(0,3);*/
    	double power = (Rssi - 50) / (10.0 * 3.3);
    	String location=String.valueOf(Math.pow(10, power));
    	String distance = location.substring(0,3);
    	ibeaconService.updateRssiAndTime(mac,
				 gatewaymac, oldmaxRssiUpdateTime,
				 rssi, distance
				, maxRssiUpdateTime);
    	
    }
    
	    	    		
	    	    		
	    	    		
	    	    		
	    	    		
	    	    		
	    	    		
//    单位时间5分钟内更新网关
//    接收到信号的网关更新字段gateway，online，updatetime
    public void receivedGateWay(String gatewaymac,String currnettime) {
    	redisGateway( gatewaymac, currnettime);
	}     
    
    
    
    public void redisGateway(String gatewaymac,String currnettime) {
    	
    	if(StringUtils.isEmpty(gatewaymac)) {
    		LogUtil.logger.info(gatewaymac+"=gatewaymac为空");
    	}else {
    	
	//    	通过hash获取值
	    	Map<Object, Object> object = redisUtil.hmget(gatewaymac);
	    	Gateway gate = new Gateway(); 
    		gate.setGatewaymac(gatewaymac);
    		gate.setUpdatetime(currnettime);
    		Map map = new HashMap();
        	map.put("gate",gate);
        	
	    	
	    	if(object == null || object.isEmpty()) {  //map非空判断
	    		
	    		//        	map对应多个键值
	    		map.put("expirationTime", currnettime);
	        	Object object1 = redisUtil.hmset(gatewaymac,map);
	        	
//	        	LogUtil.logger.info(object1+"=====设置redis缓存");
	    	}else{
	    		GatewayGuavaCache gateCache = new GatewayGuavaCache();
	    		//    	异步持久化到数据库和更新网关，超时则掉线,同时更新redis缓存
	    		object.forEach((key,value) -> {
	    			

	    			if("expirationTime".equals(key)) {
	    				try {
	    					 //更新redis缓存  ,分时、错峰
	    					String futureTime = "";
							if(gatewaymac.compareTo(ParameterField.gatewaymac_comp)>0) {
								futureTime = timeUtile.timeReduce(currnettime,ParameterField.GATEWAYDELAYTIME);
							}else {
								futureTime = timeUtile.timeReduce(currnettime,ParameterField.GATEWAYDELAYTIME1);
							}
							Boolean date = timeUtile.compareDateByString(futureTime,value+"")>0?true:false;
							
							if(date) {
								/*更新一级缓存（避免HashMap重复查询数据库，减少空间和时间的消耗
									如一个网关查询并且缓存其他网关再次访问直接从缓存中获取即可）或数据库
								 */
								map.put("expirationTime", currnettime);
								Object object1 = redisUtil.hmset(gatewaymac,map);
								

								Object cache = gateCache.getCache(gatewaymac);
								Gateway gatewayCache = (Gateway)cache;
								
//								LogUtil.logger.info(gatewayCache+"=firestCache");
								
								
								
								if(gatewayCache.getGatewaymac() == null) {
									List<Gateway> allGatewayDatas = gatewayService.searchAllGateway();
									
									List<String> mac = allGatewayDatas.parallelStream().filter(g -> gatewaymac.equals(g.getGatewaymac()))
									.map(gates -> {
//										放入缓存
										gateCache.putCache(gatewaymac, gates);
										return gates+"";
									}).collect(Collectors.toList());
									
									
									
//									异步更新数据库网关数据
									GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
									cf.completFutureUpdateGateway(gatewaymac, currnettime);
									
//									再次获取缓存数据
									Object getCacheByPut = gateCache.getCache(gatewaymac);
//									LogUtil.logger.info(getCacheByPut+"=gput");
								}
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							LogUtil.logger.error(MqttPushCallback.class+"apiserver接受数据异常:"+e);
						}
	    				
	    			}
	    		});

	    	}
	    	

    	}
    	
    }     
//  告警历史记录
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
		List<Humiture> queryhum = hygrothermographService.queryhum(humit);
		
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
//			温湿度阀值过滤
			double temp_value = Double.parseDouble(temp_f);
			double hum_value = Double.parseDouble(hum_f);
//			高温/低温/高湿/低湿告警
			if(temp_value>w1 || temp_value<w2 || hum_value>w3 || hum_value<w4) {
				hyWwarnService.insertHyWarn(hw);
			}
			return 1;
		}).collect(Collectors.toList());
  }
    
    
    
    
}


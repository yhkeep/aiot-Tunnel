/*package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.GatewayEdit;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.hygrothermograph;
import com.example.demo.util.parameter.ParameterField;
import com.github.pagehelper.PageInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IGatewayTest {
	@Autowired
    private GatewayService gatewayService;
	@Autowired
	private IbeaconGatewayService ibeaconService;
	@Autowired
	private hygrothermographService hygrothermographService;
	@Test
    public void testgate() throws ParseException{
    	List<Gateway> datas = gatewayService.searchGateway("2");
    	String gatewaymac = "";
    	System.out.println(datas+"=datas");
    	
    	for (Gateway gateway : datas) {
    		gatewaymac = gateway.getGatewaymac().toLowerCase();
    		System.out.println("/gw/"+gatewaymac+"/status");
    		
		}
    	
    }
	@Test
	public void testgate11() throws Exception{
//		分页
		[{"currentTime":"2019-10-22 00:01:00","gatewaymac":"AC233FC011F2","temperature":"20","humidity":"24","mac":"AC233FA05366"}
		 * ,{"currentTime":"2019-10-22 00:01:09","gatewaymac":"AC233FC011F2","temperature":"21","humidity":"29","mac":"AC233FA05366"},
		 * {"currentTime":"2019-10-22 01:01:10","temperature":"12","humidity":"39","mac":"AC233FA05366"},
		 * {"currentTime":"2019-10-22 02:01:20","gatewaymac":"AC233FC011F3","temperature":"18","humidity":"10","mac":"AC233FA05366"},
		 * {"currentTime":"2019-10-22 03:01:38","temperature":"19","humidity":"79","mac":"AC233FA05366"},
		 * {"currentTime":"2019-10-22 00:01:39","temperature":"23","humidity":"89","mac":"AC233FA05366"},
		 * {"currentTime":"2019-10-21 19:05:58","gatewaymac":"AC233FC011F2","temperature":"89","humidity":"-24","mac":"AC233FA05366"},
		 * {"currentTime":"2019-10-20 19:05:58","temperature":"89","humidity":"45","mac":"AC233FA05366"},
		 * {"currentTime":"2019-10-20 19:15:58","temperature":"12","humidity":"36","mac":"AC233FA05366"},{"total":"9"}]=string
		PageInfo<hygrothermograph> datasPage = hygrothermographService.getDatasPage(1, 10, "2019-10-20 19:05:58","2019-10-22 03:01:38","AC233FA05366");
		String jsonString = JSONObject.toJSONString(datasPage);
		JSONObject json = JSONObject.parseObject(jsonString);	
		JSONArray string = json.getJSONArray("list");
//		向JSONArray添加总条数
		String total = json.getString("total");
		JSONObject obj = new JSONObject();  
        obj.put("total",total) ;  
        string.add(obj) ;  
        System.out.println(string+"=string");
		
	}
	@Test
	public void testgate1(){
		hygrothermograph hy = new hygrothermograph(); 
		hy.setCurrentTime("2019-10-18 19:05:58");
		hy.setGatewaymac("AC233FC011F2");
		hy.setHumidity("-24");
		hy.setMac("AC233FA018E3");
		hygrothermographService.saveHy(hy);
		
	}
	@Test
	public void testgatehum(){
		[Humiture [freezernumber=1, freezername=中药饮片, temperaturefitted=2~8, humidityfitted=35~75, type=s1-w, mac=AC233FA05366, department=药剂科, location=中药房（外）]]=se
		List<Humiture> se = hygrothermographService.getHumSection("AC233FA05366");
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(se));
		System.out.println(array+"=array");
		
	}
	@Test
	public void testgate2(){
		[hygrothermograph [gatewaymac=AC233FC011F2, temperature=null, humidity=-24, currentTime=2019-10-18 19:05:48, mac=AC233FA018E3], 
 * hygrothermograph 
		 [gatewaymac=AC233FC011F2, temperature=null, humidity=-24, currentTime=2019-10-18 19:05:58, mac=AC233FA018E3]]
		String startime = "2019-10-18 19:05:38";
		String endtime = "2019-10-21 18:19:22";
		List<hygrothermograph> queryhy = hygrothermographService.queryhy("AC233FA018EB",startime, endtime);
		for (hygrothermograph hq : queryhy) {
			hq.getCurrentTime();
		}
		System.out.println(queryhy);
		
//		 hygrothermographService.updateHum("AC233FA05366","中药饮片1","药剂科","2~6","35~75");
	}
	@Test
	public void testgate6() throws ParseException{

	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00", Locale.CHINA);// 输出北京时间
	      Date dt = new Date();
	      Calendar rightNow = Calendar.getInstance();
	      Calendar rightNow1 = Calendar.getInstance();
	      String oneHoursAgoTime="";
	      String oneHoursAgoTime1="";
	      List ls = new ArrayList<>();
	      for (int i = 0; i < dt.getHours(); i++) {
	          rightNow.setTime(dt);
	          rightNow.add(Calendar.HOUR, -i-1);
	          Date dt1=rightNow.getTime();
	          oneHoursAgoTime = sdf.format(dt1);
	          
	          rightNow1.setTime(dt);
	          rightNow1.add(Calendar.HOUR, -i);
	          Date dt2=rightNow1.getTime();
	          oneHoursAgoTime1 = sdf.format(dt2);
	          System.out.println(oneHoursAgoTime + "=one"+oneHoursAgoTime1);
	          Map toporMinHum = new HashMap();
	          toporMinHum =  getToporMinHum("AC233FA05366", oneHoursAgoTime, oneHoursAgoTime1);
	          ls.add(toporMinHum);
	      }
	      System.out.println(ls+"=");
	      
	      
	}
	public Map getToporMinHum(String mac,String get0Time,String currenttime) {
		 List<hygrothermograph> mapDatas = hygrothermographService.queryhy(mac, get0Time, currenttime);	
			
		 Map map = new HashMap();
//		 获取最高温度和最高湿度,以及最新温湿度
		 if(mapDatas.size()>0) {
//			 先行排序，然后获取最大值，或者最小值
		        Collections.sort(mapDatas, new Comparator<hygrothermograph>() {
		            @Override
		            public int compare(hygrothermograph o1, hygrothermograph o2) {
		            	Float f1 = Float.parseFloat(o1.getHumidity());
		            	Float f2 = Float.parseFloat(o2.getHumidity());
		                return f1>f2? -1:(f1==f2? 0:1);
		            }
		        });
		        for (int j = 0; j < mapDatas.size(); j++) {
//		        	最大湿度值
					if(j == 0) {
						map.put("tophumidity", mapDatas.get(0).getHumidity());
						
//						System.out.println(mapDatas.get(0)+"=tophumidity");
						
					}
//					最小湿度值
					if(j == mapDatas.size()-1) {
						map.put("minhumidity",mapDatas.get(mapDatas.size()-1).getHumidity());
//						System.out.println(mapDatas.get(mapDatas.size()-1)+"=minhumidity");
					}
					
				}
		        Collections.sort(mapDatas, new Comparator<hygrothermograph>() {
		            @Override
		            public int compare(hygrothermograph o1, hygrothermograph o2) {
		            	Float f1 = Float.parseFloat(o1.getTemperature());
		            	Float f2 = Float.parseFloat(o2.getTemperature());
		                return f1>f2? -1:(f1==f2? 0:1);
		            }
		        });
		        for (int k = 0; k < mapDatas.size(); k++) {
//		        	最大湿度值
					if(k == 0) {
						map.put("toptemperature",mapDatas.get(0).getTemperature());
//						System.out.println(mapDatas.get(0)+"=toptemperature");
						
					}
//					最小湿度值
					if(k == mapDatas.size()-1) {
						map.put("mintemperature", mapDatas.get(mapDatas.size()-1).getTemperature());
//						System.out.println(mapDatas.get(mapDatas.size()-1)+"=mintemperature");
					}
				}
		       
		        * 		        查询一条最新温湿度sql
		        *  List<hygrothermograph> currentimeDatas = handler.getcurrentimehy(mac);	
		        String humidity = "";
		        String temperature = "";
		        for (hygrothermograph cu : currentimeDatas) {
					 humidity = cu.getHumidity();
					 temperature = cu.getTemperature();
				}
		        

		        map.put("temperature", temperature);
		        map.put("humidity", humidity);
		        
		        
		        map.put("mac", mac);
		        map.put("date", get0Time);
//		        hy.add(map);
		 }
		 return map;
	 }
	@Test
	public void testgate4(){
		List<Map> hy = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			Calendar cal=Calendar.getInstance();
			Calendar cal1=Calendar.getInstance();
			cal.add(Calendar.DATE,-i);
			cal1.add(Calendar.DATE,-i+1);
			Date time=cal.getTime();
			Date time1=cal1.getTime();
			String get0Time = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(time);
			String currenttime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(time1);
			
			System.out.println(get0Time+"="+currenttime);
			List<hygrothermograph> mapDatas = hygrothermographService.queryhy("AC233FA05366", get0Time, currenttime);	
			
//			 获取最高温度和最高湿度,以及最新温湿度
			 if(mapDatas.size()>0) {
				 Map map = new HashMap();
//				 先行排序，然后获取最大值，或者最小值
			        Collections.sort(mapDatas, new Comparator<hygrothermograph>() {
			            @Override
			            public int compare(hygrothermograph o1, hygrothermograph o2) {
			            	Float f1 = Float.parseFloat(o1.getHumidity());
			            	Float f2 = Float.parseFloat(o2.getHumidity());
			                return f1>f2? -1:(f1==f2? 0:1);
			            }
			        });
			        for (int j = 0; j < mapDatas.size(); j++) {
//			        	最大湿度值
						if(j == 0) {
							map.put("tophumidity", mapDatas.get(0).getHumidity());
							
//							System.out.println(mapDatas.get(0)+"=tophumidity");
							
						}
//						最小湿度值
						if(j == mapDatas.size()-1) {
							map.put("minhumidity",mapDatas.get(mapDatas.size()-1).getHumidity());
//							System.out.println(mapDatas.get(mapDatas.size()-1)+"=minhumidity");
						}
						
					}
			        Collections.sort(mapDatas, new Comparator<hygrothermograph>() {
			            @Override
			            public int compare(hygrothermograph o1, hygrothermograph o2) {
			            	Float f1 = Float.parseFloat(o1.getTemperature());
			            	Float f2 = Float.parseFloat(o2.getTemperature());
			                return f1>f2? -1:(f1==f2? 0:1);
			            }
			        });
			        for (int k = 0; k < mapDatas.size(); k++) {
//			        	最大湿度值
						if(k == 0) {
							map.put("toptemperature",mapDatas.get(0).getTemperature());
//							System.out.println(mapDatas.get(0)+"=toptemperature");
							
						}
//						最小湿度值
						if(k == mapDatas.size()-1) {
							map.put("mintemperature", mapDatas.get(mapDatas.size()-1).getTemperature());
//							System.out.println(mapDatas.get(mapDatas.size()-1)+"=mintemperature");
						}
					}
			       
			        * 		        查询一条最新温湿度sql
			        *  List<hygrothermograph> currentimeDatas = handler.getcurrentimehy(mac);	
			        String humidity = "";
			        String temperature = "";
			        for (hygrothermograph cu : currentimeDatas) {
						 humidity = cu.getHumidity();
						 temperature = cu.getTemperature();
					}
			        

			        map.put("temperature", temperature);
			        map.put("humidity", humidity);
			        
			        
			        map.put("mac", "AC233FA05366");
			        map.put("date", get0Time);
			        hy.add(map);
			 }
		}
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(hy));
		System.out.println(array+"=array");
		
	}
	@Test
	public void testgate3(){
		Humiture humit = new Humiture();
		humit.setAddress("1");
		
		List<Humiture> queryhum = hygrothermographService.queryhum(humit);
		
		
    	System.out.println(queryhum+"=queryhum");
		
	}
	@Test
	public void testFilter(){
		// 查找的字符串
		String mac = "AC233FA018E8";
		if(mac.indexOf(ParameterField.macCapitalizedfilter)!= 0 ) {
			System.out.println(mac+"非正常mac不执行");
			
		}
		
	}
	@Test
	public void testString(){
		Gateway gate2 = new Gateway();
		gate2.setCadMapRoomName("房间名");
		gate2.setType("网关");
		gate2.setDepartment("yanghan1");
		gate2.setFloor("13");
		gate2.setGatewaymac("123456789");
		gate2.setIpaddress("12346");
		gate2.setLocation("ceisi");
		gate2.setMapx("yangha");
		gate2.setMapy("yanghan");
		
		
		try {
			gatewayService.addGateway(gate2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void testGateway(){
		
//    	默认配置
    	String disableLED = "YES"; //是否开启led灯
    	String isLongBright = "NO"; //闪烁开启
    	String isUploadUnkown = "YES"; 
    	String isUploadIBeacon = "default";
    	String isUploadS1 = "YES";
    	String isUploadGateway = "default";
    	String isauto = "YES"; //断线自动重启
    	String timeout = "default"; //自动重启时间
    	
    	String publishTopic = "";
    	String subscribeTopic = "";
    	String responseTopic = "";
    	
    	String qos = "1"; //数据传输等级
    	String userName = "default"; //mqtt 服务器用户名
    	String passWord = "default"; //mqtt 服务器密码
    	
    	String hUrl = "default"; 
    	String httpUser = "default"; //http 用户名
    	String httpPass = "default"; //http 密码
    	
    	String macReg = "^AC23.*"; //mac过滤 ,非默认
    	String mUrl = "tcp://192.168.0.117:1883"; //mqtt服务指向，非默认
    	
    	
    	List<GatewayEdit> gateway = new ArrayList<GatewayEdit>();
    	GatewayEdit ge = new GatewayEdit();
    	ge.setMacReg("DL");
    	ge.setPublishTopic("ac233fc0380f");
    	ge.setMUrl("192.168.0.115");
    	gateway.add(ge);
    			
    	
    	for (GatewayEdit gatewayEdit : gateway) {
    		 disableLED = StringUtils.isEmpty(gatewayEdit.getDisableLED())==true?"default":gatewayEdit.getDisableLED().toUpperCase();
    		 isLongBright = StringUtils.isEmpty(gatewayEdit.getIsLongBright())==true?"default":gatewayEdit.getIsLongBright().toUpperCase();
    		 isUploadUnkown = StringUtils.isEmpty(gatewayEdit.getIsUploadUnkown())==true?"default":gatewayEdit.getIsUploadUnkown().toUpperCase();
    		 isUploadIBeacon = StringUtils.isEmpty(gatewayEdit.getIsUploadIBeacon())==true?"default":gatewayEdit.getIsUploadIBeacon().toUpperCase();
    		 isUploadS1 = StringUtils.isEmpty(gatewayEdit.getIsUploadS1())==true?"default":gatewayEdit.getIsUploadS1().toUpperCase();
    		 isUploadGateway = StringUtils.isEmpty(gatewayEdit.getIsUploadGateway())==true?"default":gatewayEdit.getIsUploadGateway().toUpperCase();
    		 isauto = StringUtils.isEmpty(gatewayEdit.getIsauto())==true?"default":gatewayEdit.getIsauto().toUpperCase();
    		 timeout = StringUtils.isEmpty(gatewayEdit.getTimeout())==true?"default":gatewayEdit.getTimeout().toUpperCase();
    		 
    		 publishTopic = StringUtils.isEmpty(gatewayEdit.getPublishTopic())==true?"default":"/gw/"+gatewayEdit.getPublishTopic().toLowerCase()+"/status";
    		 subscribeTopic = StringUtils.isEmpty(gatewayEdit.getSubscribeTopic())==true?"default":"/gw/"+gatewayEdit.getSubscribeTopic().toLowerCase()+"/status";
    		 responseTopic = StringUtils.isEmpty(gatewayEdit.getResponseTopic())==true?"default":"/gw/"+gatewayEdit.getResponseTopic().toLowerCase()+"/status";
    		 
    		 qos = StringUtils.isEmpty(gatewayEdit.getQos())==true?"default":gatewayEdit.getQos().toUpperCase();
    		 userName = StringUtils.isEmpty(gatewayEdit.getUserName())==true?"default":gatewayEdit.getUserName().toUpperCase();
    		 passWord = StringUtils.isEmpty(gatewayEdit.getPassWord())==true?"default":gatewayEdit.getPassWord().toUpperCase();
    		 hUrl = StringUtils.isEmpty(gatewayEdit.getHUrl())==true?"default":gatewayEdit.getHUrl().toUpperCase();
    		 httpUser = StringUtils.isEmpty(gatewayEdit.getHttpUser())==true?"default":gatewayEdit.getHttpUser().toUpperCase();
    		 httpPass = StringUtils.isEmpty(gatewayEdit.getHttpPass())==true?"default":gatewayEdit.getHttpPass().toUpperCase();
    		 
    		 macReg = StringUtils.isEmpty(gatewayEdit.getMacReg())==true?macReg:"^"+gatewayEdit.getMacReg().toUpperCase()+".*";
    		 mUrl = StringUtils.isEmpty(gatewayEdit.getMUrl())==true?mUrl:"tcp://"+gatewayEdit.getMUrl()+":1883";
    		 
//    	    	网关配置参数为json格式
    	     String sendCONFIG="{ \r\n" + 
    	    			"\"action\":\"config\", \r\n" + 
    	    			"\"takeEffectImmediately\":\"YES\", \r\n" + 
    	    			"\"common\":{ \r\n" + 
    	    			"\"proto\":\"default\", \r\n" + 
    	    			"\"uploadInterval\":\"default\", \r\n" + 
    	    			"\"isFilterDupData\":\"default\", \r\n" + 
    	    			"\"isOnlySpecialMac\":\"default\", \r\n" + 
    	    			"\"macList\":\"default\",\r\n" + 
    	    			"\"disableLED\":\""+disableLED+"\", \r\n" + 
    	    			"\"isLongBright\":\""+isLongBright+"\", \r\n" + 
    	    			"\"isUploadUnkown\":\""+isUploadUnkown+"\", \r\n" + 
    	    			"\"isUploadIBeacon\" :\""+isUploadIBeacon+"\", \r\n" + 
    	    			"\"isUploadS1\":\""+isUploadS1+"\", \r\n" + 
    	    			"\"isUploadGateway\":\""+isUploadGateway+"\", \r\n" + 
    	    			"\"isauto\":\""+isauto+"\", \r\n" + 
    	    			"\"timeout\":\""+timeout+"\", \r\n" + 
    	    			"\"schedule\":{ \r\n" + 
    	    			"\"istiming\":\"default\", \r\n" + 
    	    			"\"min\":\"default\", \r\n" + 
    	    			"\"hour\":\"default\", \r\n" + 
    	    			"\"week\":\"default\" \r\n" + 
    	    			"},\r\n" + 
    	    			"\"rssi\":\"default\", \r\n" + 
    	    			"\"regex\":\"default\", \r\n" + 
    	    			"\"macReg\":\""+macReg+"\", \r\n" + 
    	    			"\"cacheTime\":\"default\", \r\n" + 
    	    			"\"isLongFormat\":\"default\", \r\n" + 
    	    			"\"isJsonFormat\":\"default\" \r\n" + 
    	    			"},\r\n" + 
    	    			"\"mqtt\":{ \r\n" + 
    	    			"\"mUrl\":\""+mUrl+"\", \r\n" + 
    	    			"\"urlpath\":\"default\", \r\n" + 
    	    			"\"cafile\" :\"default\", \r\n" + 
    	    			"\"certfile\":\"default\", \r\n" + 
    	    			"\"keyfile\" :\"default\", \r\n" + 
    	    			"\"keypass\":\"default\", \r\n" + 
    	    			"\"publishTopic\":\""+publishTopic+"\", \r\n" + 
    	    			"\"subscribeTopic\":\""+subscribeTopic+"\", \r\n" + 
    	    			"\"responseTopic\":\""+responseTopic+"\", \r\n" + 
    	    			"\"qos\":\""+qos+"\", \r\n" + 
    	    			"\"userName\":\""+userName+"\", \r\n" + 
    	    			"\"passWord\":\""+passWord+"\", \r\n" + 
    	    			"\"clientID\":\"default\" \r\n" + 
    	    			"},\r\n" + 
    	    			"\"http\":{ \r\n" + 
    	    			"\"hUrl\":\""+hUrl+"\", \r\n" + 
    	    			"\"auth\":\"default\", \r\n" + 
    	    			"\"httpUser\":\""+httpUser+"\", \r\n" + 
    	    			"\"httpPass\":\""+httpPass+"\" \r\n" + 
    	    			"},\r\n" + 
    	    			"\"requestId\": \"b2c3d4e5-3424-4dca-32dc-12b73290cfed\" \r\n" + 
    	    			"} ";
    	     
    	     System.out.println(sendCONFIG +"=sendconfig");
		}
    	

		
	}
}
*/
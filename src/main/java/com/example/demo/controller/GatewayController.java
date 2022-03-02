package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Interceptor.PassToken;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.GatewayEdit;
import com.example.demo.domain.Result;
import com.example.demo.mqtt.MqttHandler;
import com.example.demo.mqtt.MqttPushCallback;
import com.example.demo.mqtt.MqttPushClient;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.GatewayService;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;
import com.example.demo.util.parameter.GatewayParameterField;
import com.example.demo.util.parameter.ParameterField;
import com.example.demo.util.parameter.UserParameterField;
import com.example.demo.util.result.ResultUtil;

@RestController
public class GatewayController{
	@Autowired
    private GatewayService gatewayService;
	
	@Resource
    private RedisUtils redisUtil;
	
	@RequestMapping(value = "/huaxi/gateway", method = RequestMethod.POST)
    public String restartAloneGateway(@RequestParam(value="gatewaymac",required=true)List<String> gatewaymac,@RequestParam(value="address",required=false)int address) throws InterruptedException, MqttException {
//		后携带地址参数1为华西，2为泸州，3位郫院
		JSONObject result = new JSONObject();
		MqttHandler mqttHandler = new MqttHandler();
//		静态化对象后，重启,速度较快.但是不安全
		MqttPushCallback mq = new MqttPushCallback();
		MqttPushClient aloneclinet = initClient();
		try {
//			TODO 可进行切面编程进行程序执行跟踪，最后通过返回订阅匹配，操作是否成功
			LogUtil.logger.info("重启单个网关"+gatewaymac);
			mqttHandler.restartAloneGateway(aloneclinet,gatewaymac,address);
			
			result.put("msg", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("重启错误="+e);
			result.put("msg", "failed");
		}
		return result.toJSONString();	
	}
//	重启所有网关
	@RequestMapping(value = "/huaxi/allGateway", method = RequestMethod.POST)
	public String restartAllGateway(@RequestParam(value="address",required=false)int address) throws InterruptedException, MqttException {
		MqttHandler mqttHandler = new MqttHandler();
//		静态化对象后，重启,速度较快.但是不安全
		MqttPushCallback mq = new MqttPushCallback();
		MqttPushClient aloneclinet = initClient();
		
		mqttHandler.restartAllGateway(aloneclinet,address);
		
		
		JSONObject result = new JSONObject();
		try {
			
			result.put("msg", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "failed");
		}
//		返回删除成功与失败！
		return result.toJSONString();	
	}
	
	
//	升级配置所有网关
	@RequestMapping(value = "/huaxi/allGateway/upgrade", method = RequestMethod.GET)
	@PassToken
	public String upgradeAllGateway() throws InterruptedException, IOException {
		
		
		/*MqttHandler mqttHandler = new MqttHandler();
		JSONObject result = new JSONObject();
		MqttPushClient upgradeClient;
		try {
			List<Gateway> gatewayData = gatewayService.searchGateway("1");
			upgradeClient = initClient();
			for (Gateway gateway : gatewayData) {
				LogUtil.logger.info(gateway+"");
				if(!StringUtils.isEmpty(gateway.getType())) {
					
					mqttHandler.upgrade(upgradeClient,gateway.getGatewaymac().toLowerCase());
				}
				
			}
			
			result.put("msg", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "failed");
		}
//		返回删除成功与失败！
		return result.toJSONString();	
		*/

String Upgrade = "{ \"action\":\"upgrade\", \"type\":\"self\", \"urlpath\":\"http://172.20.29.2:8009/huaxi/upgradeVersion\", \"filename\":\"thingoo-g1-c-v3.1.2.bin\", \"isSave\":\"YES\", \"requestId\":\"xxx\" }";
    	
    	LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    	map.put("action", "upgrade");
    	map.put("type", "self");
//    	map.put("urlpath", "http://192.168.218.178:8009/huaxi/upgradeVersion/");  服务器
    	map.put("urlpath", "http://172.20.29.2:8009/huaxi/upgradeVersion/");
    	map.put("filename", "thingoo-g1-c-v3.1.2.bin");
    	map.put("isSave", "YES");
    	map.put("requestId", "b2c3d4e5-3424-4dca-32dc-12b73290cfed");
    	
    	
    	
    	/*if("" != gatewaymac && gatewaymac.equals("ac233fc03867")) {
    		mqclient.sendClient("/gw/"+gatewaymac+"/action/response",mqclient.client);
//    		mqclient.publish("/gw/"+gatewaymac+"/action", Upgrade);
    		mqclient.publish("/gw/"+gatewaymac+"/action", JSONObject.toJSONString(map));
    		
    		LogUtil.logger.info(JSONObject.toJSONString(map)+"==ac233fc03867");
    		
    	}*/
    	
    	
    	MqttPushClient mqclient;
		try {
			mqclient = initClient();
			String gatewaymac = "ac233fc072c0";
	    	mqclient.publish("/gw/"+gatewaymac+"/action", JSONObject.toJSONString(map));
	    	LogUtil.logger.info(JSONObject.toJSONString(map)+"=action订阅号="+"/gw/"+gatewaymac+"/action");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			LogUtil.logger.error("ap升级配置异常:"+e);
		}
    	
		JSONObject result = new JSONObject();
		return result.toJSONString();
		
		
		
	}
//	升级配置所有网关
	@RequestMapping(value = "/huaxi/upgradeVersion", method = RequestMethod.GET)
	@PassToken
	public void upgradeVersion(HttpServletResponse response) throws InterruptedException, IOException {
		
		
//		{ "action":"upgrade", "type":"self", "urlpath":"http://172.20.29.2:8009/huaxi/upgradeVersion", "filename":"thingoo-g1-c-v3.1.2.bin", "isSave":"YES", "requestId":"xxx" }
		
		
//    	本地资源版本下载
    	 ClassPathResource classPathResource = new ClassPathResource("upgradeVersion/thingoo-g1-c-v3.1.2.bin");
		    File file = classPathResource.getFile();
		    InputStream inputStream = classPathResource.getInputStream();
		    //输出文件
		    InputStream fis = new BufferedInputStream(inputStream);
		    byte[] buffer = new byte[fis.available()];
		    fis.read(buffer);
		    fis.close();
		    response.reset();

		
		
		    //获取文件的名字再浏览器下载页面
		    String name = file.getName();
		    response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes(), "iso-8859-1"));
		    response.addHeader("Content-Length", "" + file.length());
		    OutputStream out = new BufferedOutputStream(response.getOutputStream());
		    response.setContentType("application/octet-stream");
		    out.write(buffer);
		    out.flush();
		    out.close();
		
	}
	

	
	//	    启动所有网关   (注意谁启动的连接，那么就使用该client对象进行关闭，否则关闭失效)
	public void initAllGateway() throws InterruptedException, MqttException{
		 
		
//		 本地总订阅TODO210722
		 MqttPushClient  instance = initClient();
		 
		 
//		 TODO210726多线程8订阅启动
		 for (int i = 0; i < 8; i++) {
			 new Thread(new Runnable() {
		 			@Override
		 			public void run() {
		 				// TODO Auto-generated method stub
	 					GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture(instance);
	 					cf.completFutureClientSubAll(instance);
	 					System.out.println(Thread.currentThread().getName()+"==线程全部数据订阅====name");
		 			}
		 	}).start();	
		 }
		 
		 System.out.println(Thread.currentThread().getName()+"==主线程====name");
	}
	
	
	 
	 public  MqttPushClient initClient() throws MqttException {
//		 初次启动需要创建client对象
//		 MqttPushClient.MQTT_HOST = "tcp://192.168.218.179:8009"; //迁移前远程
//		 MqttPushClient.MQTT_HOST = "tcp://172.21.244.86:8009"; //迁移后远程
//		 MqttPushClient.MQTT_CLIENTID = "client2";  //远程服务端
		 
		 MqttPushClient.MQTT_HOST = "tcp://localhost:1883"; //本地
		 MqttPushClient.MQTT_CLIENTID = "client1";  //本地
		 MqttPushClient.MQTT_USERNAME = "username";
		 MqttPushClient.MQTT_PASSWORD = "password";
		 MqttPushClient mq = MqttPushClient.getInstance();
//         建立连接
		 mq.connect();
		 return mq;
	 }
	 public  MqttPushClient initClient11() throws MqttException {
//		 初次启动需要创建client对象
//		 MqttPushClient.MQTT_HOST = "tcp://192.168.218.179:8009"; //迁移前远程
//		 MqttPushClient.MQTT_HOST = "tcp://172.21.244.86:8009"; //迁移后远程
//		 MqttPushClient.MQTT_CLIENTID = "client11";  //远程服务端
		 
		 MqttPushClient.MQTT_HOST = "tcp://localhost:1883"; //本地
		 MqttPushClient.MQTT_CLIENTID = "client11";  //本地
		 MqttPushClient.MQTT_USERNAME = "username";
		 MqttPushClient.MQTT_PASSWORD = "password";
		 MqttPushClient mq = MqttPushClient.getInstance();
//         建立连接
		 mq.connect();
		 return mq;
	 }
	 public  MqttPushClient initClient12() throws MqttException {
//		 初次启动需要创建client对象
//		 MqttPushClient.MQTT_HOST = "tcp://192.168.218.179:8009"; //迁移前远程
//		 MqttPushClient.MQTT_HOST = "tcp://172.21.244.86:8009"; //迁移后远程
//		 MqttPushClient.MQTT_CLIENTID = "client12";  //远程服务端
		 
		 MqttPushClient.MQTT_HOST = "tcp://localhost:1883"; //本地
		 MqttPushClient.MQTT_CLIENTID = "client12";  //本地
		 MqttPushClient.MQTT_USERNAME = "username";
		 MqttPushClient.MQTT_PASSWORD = "password";
		 MqttPushClient mq = MqttPushClient.getInstance();
//         建立连接
		 mq.connect();
		 return mq;
	 }
	 public  MqttPushClient initClient13() throws MqttException {
//		 初次启动需要创建client对象
//		 MqttPushClient.MQTT_HOST = "tcp://192.168.218.179:8009"; //迁移前远程
//		 MqttPushClient.MQTT_HOST = "tcp://172.21.244.86:8009"; //迁移后远程
//		 MqttPushClient.MQTT_CLIENTID = "client13";  //远程服务端
		 
		 MqttPushClient.MQTT_HOST = "tcp://localhost:1883"; //本地
		 MqttPushClient.MQTT_CLIENTID = "client13";  //本地
		 MqttPushClient.MQTT_USERNAME = "username";
		 MqttPushClient.MQTT_PASSWORD = "password";
		 MqttPushClient mq = MqttPushClient.getInstance();
//         建立连接
		 mq.connect();
		 return mq;
	 }
	 
	 
	 
	 

/*	TODO 0723 
//	 页面展示网关数据
	  @RequestMapping(value = "/huaxi/getGateway", method = RequestMethod.GET)
	    public JSONArray getAllGateway(@RequestParam(value="address",required=false)int address) throws InterruptedException, ParseException {
		 timeUtiles time =  new timeUtiles();
		 String currenttime = time.getCurrenttime();
//		 网关1分钟在内则为在线(因为不存在数据丢失，造成时间延误一分钟左右)
		 String agoMinute = time.timeReduce(currenttime, ParameterField.GATEWAYDELAYTIME);
		 	List ls = new ArrayList();
//			后携带地址参数1为华西，2为泸州，3位郫院
		 	try {
	 			 List<Gateway> gateway = gatewayService.searchGateway(UserController.address);
				 String gatewaymac = "";
				 String online = "";
				 String updatetime = "";
				 String location = "";
				 String department = "";
				 String gatewayfree = "";
				 String floor = "";
				 String ipaddress = "";
				 String mapx = "";
				 String mapy = "";
				 
				for (Gateway gate : gateway) {
//		    		排除地图数据,和更新时间影响
		    		if(StringUtils.isEmpty(gate.getGatewaymac()) ) {
		    			continue;
		    		}
					
					Map map = new HashMap<>();
					gatewaymac = gate.getGatewaymac();
					online = gate.getGateway();
					updatetime = gate.getUpdatetime();
					location = gate.getLocation();
					department = gate.getDepartment();
					floor = gate.getFloor();
					ipaddress = gate.getIpaddress();
					mapx = gate.getMapx();
					mapy = gate.getMapy();
					
					map.put("gatewaymac", gatewaymac);
			
					
//					更新时间为空
					if(StringUtils.isEmpty(gate.getUpdatetime())) {
						map.put("online", "");
					}else {
//						1分钟没有更新网关则为不在线
						int timeduce = updatetime.compareTo(agoMinute);
						if(timeduce>=0) {
							map.put("online", online);
						}else {
							map.put("online", "");
						}
					}

					map.put("updatetime", updatetime);
					map.put("location", location);
					map.put("department", department);
					map.put("floor", floor);
					map.put("ipaddress", ipaddress);
					map.put("mapx", mapx);
					map.put("mapy", mapy);
					
					ls.add(map);
				}
				LogUtil.logger.info(ls+"=所有网关数据");

				
				
			} catch (Exception e) {
				// TODO: handle exception
				
				LogUtil.logger.error("网关获取错误："+e);
			}
		 	 
			JSONArray array =  JSONArray.parseArray(JSON.toJSONString(ls));
			return array;
		}*/
	  
//	 页面展示网关数据
//	 [ERROR] java.lang.ArithmeticException: / by zero自定义制造异常
	  @RequestMapping(value = "/huaxi/getGateway", method = RequestMethod.GET)
	  public JSONArray getAllGateway(@RequestParam(value="address",required=false)int address) throws InterruptedException, ParseException {
		 timeUtiles time =  new timeUtiles();
		 String currenttime = time.getCurrenttime();
//		 网关90分钟在内则为在线(因为不存在数据丢失，造成时间延误5分钟左右)
		 String agoMinute = time.timeReduce(currenttime, ParameterField.GATEWAYDELAYTIME2);
		 	List ls = new ArrayList();
//			后携带地址参数1为华西，2为泸州，3位郫院
		 	try {
	 			 List<Gateway> gateway = gatewayService.searchGateway(UserParameterField.address);
				 String gatewaymac = "";
				 String online = "";
				 String updatetime = "";
				 String location = "";
				 String department = "";
				 String gatewayfree = "";
				 String floor = "";
				 String ipaddress = "";
				 String mapx = "";
				 String mapy = "";
				 String cadMapRoomName = "";
				 String roomnumber = "";
				for (Gateway gate : gateway) {
//		    		排除地图数据,和更新时间影响
		    		if(StringUtils.isEmpty(gate.getGatewaymac()) ) {
		    			continue;
		    		}
					
					Map map = new HashMap<>();
					gatewaymac = gate.getGatewaymac();
					online = gate.getGateway();
					updatetime = gate.getUpdatetime();
					location = gate.getLocation();
					department = gate.getDepartment();
					floor = gate.getFloor();
					ipaddress = gate.getIpaddress();
					mapx = gate.getMapx();
					mapy = gate.getMapy();
					cadMapRoomName = gate.getCadMapRoomName();
					roomnumber = gate.getRoomnumber();
					
					
					map.put("gatewaymac", gatewaymac);
			
					
//					更新时间为空
					if(StringUtils.isEmpty(gate.getUpdatetime())) {
						map.put("online", "");
					}else {
//						1分钟没有更新网关则为不在线
						int timeduce = updatetime.compareTo(agoMinute);
						if(timeduce>=0) {
							map.put("online", online);
						}else {
							map.put("online", "");
						}
					}

					map.put("updatetime", updatetime);
					map.put("location", location);
					map.put("department", department);
					map.put("floor", floor);
					map.put("ipaddress", ipaddress);
					map.put("mapx", mapx);
					map.put("mapy", mapy);
					map.put("cadMapRoomName", cadMapRoomName);
					map.put("roomnumber", roomnumber);
					
					ls.add(map);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				
				LogUtil.logger.error("网关获取错误："+e);
			}
		 	 
			JSONArray array =  JSONArray.parseArray(JSON.toJSONString(ls));
			return array;
		}
	 
	 
	 
	  
	 
//	网关路由配置
	@RequestMapping(value = "/huaxi/editGateway", method = RequestMethod.POST)
	public Result editGateway(@RequestBody List<GatewayEdit> gatewayEdit){
		MqttHandler mqttHandler = new MqttHandler();
		MqttPushCallback mq = new MqttPushCallback();
		MqttPushClient editClient;
		String result = "";
		try {
			
			
			editClient = initClient();
			
			result = mqttHandler.upconfig(editClient,gatewayEdit);
			
			
		} catch (MqttException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			LogUtil.logger.error("editGateway配置异常："+e);
			return ResultUtil.error("编辑网关", result);
		}
		
		
		return ResultUtil.success(result);
		
	}
	
	
//	网关修改
	@RequestMapping(value = "/huaxi/updateGateway", method = RequestMethod.POST)
	public Result updateGateway(@RequestBody Gateway gateway){
		try {
			if(StringUtils.isEmpty(gateway.getGatewaymac())) {
				return ResultUtil.error("网关添加失败", "无效网关mac地址");
			}
			String gatewaymac_up = gateway.getGatewaymac().toUpperCase();
			gateway.setGatewaymac(gatewaymac_up);
			gatewayService.updateGateway(gateway);
//			重新订阅
			new MqttPushCallback().reline();
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("网关更新失败："+e);
			return ResultUtil.error("更新失败", "更新错误");
		}
		
		
		return ResultUtil.success("更新成功");
		
	}
//	网关删除
	@RequestMapping(value = "/huaxi/deleteGateway", method = RequestMethod.POST)
	public Result deleteGateway(@RequestBody List<Gateway> gateway){
		try {
			for (Gateway gateway2 : gateway) {
				if(StringUtils.isEmpty(gateway2.getGatewaymac())) {
					return ResultUtil.error("网关删除失败", "无网关mac地址");
				}
			}
			gatewayService.deleteGateway(gateway);
			
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("网关删除失败："+e);
			return ResultUtil.error("删除失败", "删除错误");
		}
		
		
		return ResultUtil.success("删除成功");
		
	}
//	网关增加
	@RequestMapping(value = "/huaxi/addGateway", method = RequestMethod.POST)
	public Result addGateway(@RequestBody Gateway gateway){
		try {
			gateway.setType(GatewayParameterField.gatewayType);
			if(StringUtils.isEmpty(gateway.getGatewaymac())) {
				return ResultUtil.error("网关添加失败", "无效网关mac地址");
			}
			String gatewaymac_up = gateway.getGatewaymac().toUpperCase();
			gateway.setGatewaymac(gatewaymac_up);
//			设置创建时间
			gateway.setUpdatetime(new timeUtiles().getCurrenttime());
			gatewayService.addGateway(gateway);
			
//			重新订阅
			new MqttPushCallback().reline();
			
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("网关添加失败："+e);
			return ResultUtil.error("网关添加失败", "添加错误");
		}
		
		
		return ResultUtil.success("添加成功");
		
	}
	
	
	
  }
	

 
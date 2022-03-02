package com.example.demo.mqtt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.StartUpApplication;
import com.example.demo.controller.GatewayController;
import com.example.demo.domain.Gateway;
import com.example.demo.util.LogUtil;
import com.example.demo.util.MusicThread;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;

/**
 * MQTT 推送回调
 * @author yanghan
 * @date 2018-08-22
 */
public class MqttPushCallback implements MqttCallback{
	
	
//    private static final Logger log = LoggerFactory.getLogger(MqttPushCallback.class);
	
	
    @Override
    public void connectionLost(Throwable cause) {
    	/*cause.getCause().getMessage()获取异常连接中断原因
    	如果是代码异常造成的中断，进行重新连接，此刻的instance是存在并且缓存中存在会话机制，所以直接进一步进行订阅，便再次重新再次获取到数据
    	*/
    	
//    	TODO 关机重启后,存在断线长时间断线重连失败。导致长时间订阅后出现自动关闭java服务
//    	或者关闭ssh窗口后，出现，断掉服务现象
    	
//    	LogUtil.logger.info("emq==connectdis=mqtt/sub==>start reline"+this);
        
    /*    取消所有订阅
        TODO 方式一：     断线重连需要开辟新线程
    	TimeUnit.SECONDS.sleep(15);//睡眠20秒执行
		
		System.out.println(instance.isConnected()+"判断是否建立连接");
		
		instance.disconnect();//关闭连接，需要开辟新线程开启才能进行连接
		
		instance.unsubscribe("/gw/"+gate.getGatewaymac()+"/status"); //取消订阅    	
    */
        try {
			reline();
//			LogUtil.logger.info("订阅掉线开始重新订阅"+cause.getCause().getMessage());
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    	
//        log.info(token.isComplete());
    }
    
    /**
     *订阅完成之后，mqtt自动解析数据，从而降低一边解析一边订阅，消耗的时间 
     *
     *
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
    	try {
    		String mqttMessage = message+"";
//        	排除断线重连情况
        	if(null != mqttMessage && "" != mqttMessage && !mqttMessage.equals("close")) {
        		
//        		{"code":200,"message":"success","requestId":"b2c3d4e5-3424-4dca-32dc-12b73290cfed"}=mqttMessage ===>>log.info(mqttMessage+"=mqttMessage");
//        		排除网关响应200成功的数据，close为排除为关闭的命令
        		boolean contains = mqttMessage.contains("\"code\":200");
        		MqttHandler mqttHandler = new MqttHandler();
        		if(contains) {
        			LogUtil.logger.info("操作成功Topic: " + mqttMessage);
        			
        		}else {
        			List list = JSONObject.parseArray(mqttMessage);
    				JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
    				
//    				开启多线程进行解析===========================测试区
//    				========================================测试区
    				
    		    	mqttHandler.getAssetDatas(array);// 订阅完成后进行解析
    		    	
//    		    	LogUtil.logger.info("Topic: " + topic);
//    		        LogUtil.logger.info("mqttMessage: " + mqttMessage);
        		}
    			
        	}else {
//        		LogUtil.logger.info(mqttMessage+"=mqttMessage");
        	}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.info(e+"mqtt数据解析异常捕获-解析过程结束");
			
		}
    	
    	

    	
    }
    public void reline() throws Exception {

    	
//    	通过单例获取已有地址实例，进行重启
    	MqttPushClient instance = MqttPushClient.getInstance();
    	
    	
//    	连接依旧存在，但是订阅号却不存在，需要重新订阅===如果直接使用原有单例地址则会抛出32104异常，所以需要重新建立连接开始订阅"+instance.MQTT_CLIENTID
//    		重新生成新的client
    		/*基本属性，
    		 * MqttConnectOptions 静态保存中，无需再次创建
    		 * MqttPushClient.MQTT_HOST = "tcp://localhost:1883";
   		    MqttPushClient.MQTT_CLIENTID = "client1";
   		    MqttPushClient.MQTT_USERNAME = "username";
   		    MqttPushClient.MQTT_PASSWORD = "password";*/
    		
    		
//    		服务器应用程序休眠以及设备断链重新建立连接
    		  
    		 if(null != instance.MQTT_CLIENTID && instance.isConnected()==true){
    			  
	    		GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
	    		cf.completFutureClientSubAll(instance);
	    		
	    		LogUtil.logger.info("在线重新订阅=====");
	    		
	    		
	    	}else if(instance.isConnected()==false && instance.MQTT_CLIENTID != null) {
	    		
	    		
	    		GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
	    		instance.connect();
	    		cf.completFutureClientSubAll(instance);
	    		LogUtil.logger.info("离线启动实例化重新订阅");
	    		
	    	}else if (StringUtils.isEmpty(instance.client.getClientId())) {
//    		部分client实例为空的情况
	    		GatewayController gateway = new GatewayController();
	            gateway.initAllGateway();
	            LogUtil.logger.info("完全重连=====");
	    	}
           
    }
    
    
    
}
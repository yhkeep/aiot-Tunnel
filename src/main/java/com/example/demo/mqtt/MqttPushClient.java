package com.example.demo.mqtt;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.util.LogUtil;
import com.example.demo.util.parameter.UserParameterField;

/**
 * 创建一个MQTT客户端
 * @author yanghan
 * @date 2018-08-22
 */
public class MqttPushClient {

//    private static final Logger log = LoggerFactory.getLogger(MqttPushClient.class); 占用内存
    public static String MQTT_HOST = "";
    public static String MQTT_CLIENTID = "";
    public static String MQTT_USERNAME = "";
    public static String MQTT_PASSWORD = "";
//    public static int MQTT_TIMEOUT = 30;
    public static int MQTT_TIMEOUT = 180;   //TODO0929参数修改
//    public static int MQTT_KEEPALIVE = 15; //设置网关启动花费时间，从而实现快速订阅 
    public static int MQTT_KEEPALIVE = 180; //TODO0929参数修改
    public  MqttClient client;
    
    
    
    private static MqttConnectOptions option = new MqttConnectOptions();
    
    
//  private static volatile MqttPushClient mqttClient = null; TODO0916编辑
    
//TODO1009    
	private static  MqttPushClient mqttClient = null;
//    单例创建
    public static MqttPushClient getInstance() {
    	if(mqttClient == null) {
    		synchronized (MqttPushClient.class) {
    			if(mqttClient == null) {
    				mqttClient = new MqttPushClient();
    			}
    		}
    	}
    	return mqttClient;
    }
    
    
    
    
    
    
    private MqttPushClient() {
        LogUtil.logger.info("Connect MQTT: " + this);
    }
    
    public void connect() {   //TODO0915参数修改，断线重新连接后，出现有时候在线有时候离线的情况，怀疑可能未用静态存储连接
        try {
            option.setCleanSession(true); //清除缓存已有连接
            option.setUserName(MQTT_USERNAME);
            option.setPassword(MQTT_PASSWORD.toCharArray());
            option.setConnectionTimeout(MQTT_TIMEOUT);
            option.setKeepAliveInterval(MQTT_KEEPALIVE);
    		client = new MqttClient(MQTT_HOST, MQTT_CLIENTID, new MemoryPersistence());//添加缓存
    		//回调方法
            try {
            	
//				订阅线程完成之后开启订阅解析数据线程,(由于定时器未关闭，会造成内存一直占用并且不断扩大)
                //方式一
//                Timer timer = new Timer();
//        		timer.schedule(new Tisk(client),UserParameterField.Tisk_SubAndExp_TIME);
//                回调函数再进行连接之前进行调用，内存积压bug试验
        		//方式二
        		new Thread(new Runnable() {
        			
        			@Override
        			public void run() {
        				// TODO Auto-generated method stub
        				try {
//        					延迟3秒加载，先订阅建立连接，后解析数据
        					Thread.sleep(UserParameterField.Tisk_SubAndExp_TIME);
        					client.setCallback(new MqttPushCallback());
        				} catch (InterruptedException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
//        				System.out.println("测试");
        			}
        		}).start();
//        		System.out.println("先打印");
                
                /*
            	 *client.setCallback(new MqttPushCallback()); 
            	 * 如在订阅连接过程中启动回调机制（数据解析在其中）将加大订阅连接所用时间，因此连接数量越大，时间消耗越大
            	 * 解决方案，可先定时完成订阅，再进行数据解析
            	 * */
            	
            	
//              连接是否存在
                if(!client.isConnected()) {
                	client.connect(option);
                }else if(client.isConnected()) {
                	LogUtil.logger.info("客户端已建立连接");
                }

                
                
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.logger.error("client创建失败");
            }
                
            
        } catch (Exception e) {
        	LogUtil.logger.error("client连接失败");
        }
    }
    /**
     * 发布主题，用于通知<br>
     * 默认qos为1 非持久化
     * @param topic
     * @param data
     */
    public void publish(String topic, String data) {
        publish(topic, data, UserParameterField.PUB, false);
    }
    /**
     * 发布
     * @param topic
     * @param data
     * @param qos
     * @param retained
     */
    public void publish(String topic, String data, int qos, boolean retained) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(data.getBytes());
        MqttTopic mqttTopic = client.getTopic(topic);
        if(null == mqttTopic) {
        	LogUtil.logger.error("Topic Not Exist");
        }
        MqttDeliveryToken token;
        try {
            token = mqttTopic.publish(message);
            token.waitForCompletion();
        } catch (Exception e) {
        	LogUtil.logger.error("客户端未连接重复发布导致异常32104"+e);
        }
    }
    /**
     * 订阅某个主题 qos默认为0
     * @param topic
     * @throws MqttException 
     */
    
    
    /**
     * 订阅某个主题
     * @param topic
     * @param qos
     * @throws MqttException 
     */
    public void resubscribe(String topic, int qos, MqttClient client1) throws MqttException {
//    	如果new 新的地址用来进行发布，但是原有的client依旧存在，导致32104客户端重复订阅异常
    	try {
			client1.subscribe(topic, qos);
        } catch (Exception e) {
//        	LogUtil.logger.error("mqttpushclient==>resubscribe==>订阅过程失败"+e); //取消掉线日志记录
        }
    }
//   重新获取订阅
    public void sendClient(String topic,MqttClient client1) throws MqttException {
//    	resubscribe(topic,0,client1);//最多一次 TODO0918测试不断线ap修改topic订阅等级制
    	resubscribe(topic,UserParameterField.QOS,client1);
    }
//   取消订阅
    public void cancelClient(String topic) throws MqttException {
    	unsubscribe(topic);
    }
    
//    取消订阅untopic
	public void unsubscribe(String topicFilters) throws MqttException {
		client.unsubscribe(topicFilters);
	}
	
//	返回是否断线
	public Boolean isConnected()throws MqttException {
		return client.isConnected();
	}
	
	public void disconnect() throws MqttException {
		client.disconnect();
	}

	
	@Override
	public String toString() {
		return "MqttPushClient [MQTT_HOST=" + MQTT_HOST + " MQTT_CLIENTID=" + MQTT_CLIENTID + " client=" + client + " option=" + option +"]";
	}
}
package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.StompConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.ReduceTime;
import com.example.demo.util.timeUtiles;

//public class ReportController implements MessageCreator {
@RestController
public class ReportController{
	@Autowired
    private IbeaconGatewayService ibeaconService;
//	标签
	private String missing;
//	网关
	private String gatemissing;
//	创建日期
	private String  date1;
	
	private timeUtiles timeUtile = new timeUtiles();
	/*public void run() {
		
    	System.out.println("子线程开始执行");
//		启动告警接口
		ActivemqBuild ac = new ActivemqBuild();
//      ac.testActive();	socket进行连接
		try {
//			该推送数据2分钟发送一次
//			Thread.sleep(1000*60*1);
			ac.testactivemq("");	//queue队列数据传输
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
    /*
     * 通过jms向客户端发送数据
     * @Override
    public Message createMessage(Session session) throws JMSException {
        return session.createTextMessage("测试消息是否发");
    }
    */
//一种方式，从流中读取数据，然后根据数据库存在标签对应网关的时间如超过3分钟没有接收到信号，则进行报警，这个过程必须是主动（可尝试定时器主动发送消息）
//    通过定时器方式不能一直获取数据，需要每次发送请求之后才能。此方式不适用此处
//    @Scheduled(cron = "*/4 * * * * ?")
//    @RequestMapping(value = "/api/report",method = RequestMethod.GET)
//    public List report(){
//    	
//    	List list = new ArrayList();
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	map.put("yanghan", "loveyuanyue");
//    	map.put("yuanyue", "loveyuanyue");
//    	
//    	Random ra =new Random();
//    	int num = ra.nextInt(10)+1;
//    	System.out.println(num+"=num");
//    	map.put("ra", num);
//    	list.add(map);
////    	转为json数组，并且返回
//    	JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
//    	System.out.println(array+"=array");
//    	return list;
//    }
    
    @RequestMapping(value = "/api/report",method = RequestMethod.GET)
    public void report() throws InterruptedException, ParseException {
    	/*获取数据库表中已有标签mac地址，与流中所有标签mac进行比较，如果没有则说明标签未接收到信号，则进行报警===》
    			1.信号丢失
    			2.不处在网关所能接受位置====单位时间内标签每1分钟监听一次，网关每5分钟检查一次
    			*
    			*
				*/
//      1分钟内循环查询
      misser();
    	
//    stomp协议发送
        try {
			testactivemq();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        
      
//      考虑主线程是否在子线程执行完成后再继续执行
    }
    
    
//    满足条件循环发送
    public void testactivemq() throws Exception {
		  StompConnection conn = new StompConnection(); 
//		  建立一次连接，然后一次发送数据，处理抛出异常
		    conn.open("47.104.99.230", 61613);  
		    conn.connect("admin", "admin");  
//		    conn.open("192.168.0.54", 61689);  
//		    conn.connect("admin", "admin");  
		    while (true) {
				try {
					// send text message  
				 /*   HashMap<String, String> txtHeaders = new HashMap<String,String>();  
//				    txtHeaders.put(Stomp.Headers.Send.PERSISTENT, "true");  
				    txtHeaders.put(Stomp.Headers.Send.DESTINATION, "true");  
				    sendMessage(conn, txtHeaders);  */
			        HashMap<String, String> mapHeaders = new HashMap<String,String>();
			        mapHeaders.put(Stomp.Headers.Send.DESTINATION, "true");  
			        sendMessage(conn, mapHeaders);
				}catch (Exception e) {
					// TODO: handle exception
					 // disconnect  
				    conn.disconnect();  
				}
				
			}
		    
		   
		}  
		  
		/** 
		 * 发送JMS消息 
		 * @param conn       jms connection 
		 * @param message    message content 
		 * @param headers    message headers 
		 * @throws Exception exception  
		 */  
//		睡眠10发送一次
	private  void sendMessage(  
	        StompConnection conn, HashMap<String, String> headers) throws Exception{  
		/*{'expires': '0', 
		 * 'destination': '/queue/ok', 'priority': '4', 
		 * 'message-id': 'ID:WIN-QQD29J1C40M-57324-1566198180240-3:10:-1:1:8657', 'timestamp': '1566200994986'} =head*/
	    String tx = UUID.randomUUID().toString().replaceAll("-", "");
	    Thread.sleep(1000*10);
	    conn.begin(tx);  
//      循环输出丢失标签或者网关
	    String json = map2String();
//	    System.out.println("send:"+json);
	    conn.send("ykc_activemq", json, tx, headers);  
	    conn.commit(tx);  
	}  
	
//	单位时间内重复数据查询
	@Scheduled(cron="0/60 * *  * * ? ") 
	public void misser() throws ParseException {
//		网关监听，添加判断更新时间大于5分钟，没有接收到信号的视为丢失网关信号
		String localTime = localTime();
		String fiveTime = timeUtile.timeReduce(localTime, 5*60);
//        System.out.println(fiveTime+"=fiveTime");
    	List<Map> gatewayMiss = ibeaconService.findGatewayOlineByTime(fiveTime);
    	
    	if(gatewayMiss.size() == 0) {
    		gatemissing = "";
    	}else {
//    		返回json数组
			JSONArray array= JSONArray.parseArray(JSON.toJSONString(gatewayMiss));
    		gatemissing = array.toString();
    	}
//    	两分钟检测
    	ReduceTime reduceTime = new ReduceTime();
    	String localTime2 = ReduceTime.localTime();
    	String twoMinuteTime = reduceTime.timeReduceTwoMinute(localTime2);
//    	标签监听
		List<Map> assetMiss = ibeaconService.findMissing(twoMinuteTime);
		if(assetMiss.size() == 0) {
			missing = "";
		}else {
//	    	返回json数组
			JSONArray array= JSONArray.parseArray(JSON.toJSONString(assetMiss));
			missing = array.toString();
		}

//		System.out.println(missing+"主线程测试=array");
		
		
//		发送时间
		/*Date date = new Date();// 转换为标准时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
        date1 = sdf.format(date);
		创建子线程，进行数据的发送
		ReportController myThread = new ReportController();
	    myThread.setMissinger("发送时间:"+date1+"标签:"+missing+"网关:"+gatemissing);
    当访问目标接口的时候再开始发送数据   启动警告子线程 ，此处主线程依旧执行原有程序
      Thread thread = new Thread(myThread);
      主线程必须等待子线程结束后方可进行继续查询操作
      thread.start();
        System.out.println("主线程单位时间查询！missinger"+"发送时间:"+date1+"标签:"+missing+"网关:"+gatemissing);*/
	}
	
	public String map2String() {
		Map<String, String> map = new HashMap<String,String>();
        map.put("createTime", date1);
        map.put("loseAssetID", missing);
        map.put("loseGatewayMac", gatemissing);
        String json = JsonUtils.map2Json(map);
        return json;
	}
    public  String localTime() {
    	Date date = new Date();// 转换为标准时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
        String date1 = sdf.format(date);
        return date1;
    }
}


 
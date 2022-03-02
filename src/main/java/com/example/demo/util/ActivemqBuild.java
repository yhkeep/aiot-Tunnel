package com.example.demo.util;

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

import com.alibaba.fastjson.JSONArray;
import com.example.demo.service.IbeaconGatewayService;

public class ActivemqBuild {
	
	
	/*
	 * 使用socket发送
	 * 
	 * 
	 * public void testActive() {
		System.out.println("hello");
		int count = 20;
//		存在数据就一直发送
		while (true) {
			try {
		        // 建立Stomp协议的连接
		        StompConnection con = new StompConnection();
//		        Socket so = new Socket("47.104.99.230", 61613);
		        Socket so = new Socket("192.168.0.54", 61689);
		        con.open(so);
		        // 注意，协议版本可以是1.2，也可以是1.1
		        con.setVersion("1.2");
		        // 用户名和密码，在循环内部重复建立连接
		        con.connect("admin", "admin");
		        Random ran = new Random();
		        int i = ran.nextInt(100);
		        // 以下发送一条信息（您也可以使用“事务”方式）
		        con.send("ok",i+"");
		        count --;
		    } catch (Exception e) {
		        e.printStackTrace(System.out);
		    }
		}
		
	}*/
	public void testactivemq(String array) throws Exception {
		  StompConnection conn = new StompConnection(); 
//		  建立一次连接，然后一次发送数据，处理抛出异常
//		    conn.open("47.104.99.230", 61613);  
//		    conn.connect("admin", "admin");  
		    conn.open("192.168.0.110", 61689);  
		    conn.connect("admin", "admin");  
		    while (true) {
				try {
					// send text message  
				    HashMap<String, String> txtHeaders = new HashMap<String,String>();  
//				    txtHeaders.put(Stomp.Headers.Send.PERSISTENT, "true");  
				    txtHeaders.put(Stomp.Headers.Send.DESTINATION, "true");  
				    sendMessage(conn, array, txtHeaders);  
//				    System.out.println("send: " + array);  
				    
				
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
		

	private void sendMessage(  
	        StompConnection conn, String message, HashMap<String, String> headers) throws Exception{  
		/*{'expires': '0', 
		 * 'destination': '/queue/ok', 'priority': '4', 
		 * 'message-id': 'ID:WIN-QQD29J1C40M-57324-1566198180240-3:10:-1:1:8657', 'timestamp': '1566200994986'} =head*/
	    String tx = UUID.randomUUID().toString().replaceAll("-", "");
	    Thread.sleep(1000*10);
	    conn.begin(tx);  
//	    System.out.println(message+"=message");
	    Date date = new Date();// 转换为标准时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
        String date1 = sdf.format(date);
        
        /*Received re = new Received();
        re.setCreatetime(date1);*/
       
//        输出丢失标签
	    conn.send("ykc_activemq", date1, tx, headers);  
	    // conn.send(JMSInfo.STOMP_TOPIC_NAME, message, tx, headers);  
	    conn.commit(tx);  
	}  
	
}

package com.example.demo.websocket;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.controller.UserController;
import com.example.demo.service.AssetService;
import com.example.demo.util.ReduceTime;
import com.example.demo.util.excetion.LogutilExcetion;
@ServerEndpoint(value = "/3Dwebsocket/{verification}")
@Component
public class My3DWebSocket {
	
	@Autowired
    private AssetService assetService;
	
    //concurrent包的线程安全Set，用来存放每个客户端对应的My3DWebSocket对象。
//	在外部可以获取此连接的所有websocket对象，并能对其触发消息发送功能，我们的定时发送核心功能的实现在与此变量 
    private static CopyOnWriteArraySet<My3DWebSocket> webSocketSet = new CopyOnWriteArraySet<My3DWebSocket>();

    
    
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
	private String verification;

    /**
     * 连接建立成功调用的方法
     * @throws ParseException 
     * @throws EncodeException */
    
    @OnOpen
    public void onOpen(Session session,@PathParam("verification") String verification) throws ParseException {
       
    	
    	this.session = session;
        this.verification = verification;
        
//       建立连接之前进行断链，注解测试动态参数获取
        
//      token验证 ,反射拦截验证
        String token = UserController.token; 
//      2020/07/21 TODO a2c3d4e5-3424-4dca-32dc-22b73290cfed 三维临时使用请求
        if(!token.equals(this.verification) && !"a2c3d4e5-3424-4dca-32dc-22b73290cfed".equals(this.verification)) {
        	int a = 1/0;
    	}else {
        
    		webSocketSet.add(this);     //加入set中
        
    	}
    	
        
        
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
    }


//     * 发生错误时调用
    @OnError
    public void onError(Session session, Throwable error) {
//    	LogutilExcetion.logger.error(error+"/3Dwebsocket/自定义制造异常");
    }

    
    public void sendMessage(String message) throws IOException {
//        this.session.getBasicRemote().sendText(message);  发送文本数据
    	this.session.getBasicRemote().sendText(message);
    }
    public void sendObjectMessage(Object data) throws IOException, EncodeException {
    	this.session.getBasicRemote().sendObject(data);
    }
    
//    转换数据格式
    public  String getDatas(List<Map> findAssetByRece){
//        this.session.getBasicRemote().sendText(message);  发送文本数据
    	List ls = new ArrayList<>();
    	String str = "";
    	
    	for (Map map : findAssetByRece) {
    		Map received = (Map) map.get("received");
    		String string = null;
    		if(received != null) {
    			string = (String) received.get("updatetime");
    		}
    		Map map1 = new HashMap<>();
    		map1 = map;
//    		map1.put("Type",map.get("Type"));单个元素获取
    		map1.put("updatetime",string);
//    		移除原有时间
    		map1.remove("received");
    		str += map1;
		}
    	return str;
    }

    
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
//定时任务核心代码
    public static CopyOnWriteArraySet<My3DWebSocket> getWebSocketSet() {
        return webSocketSet;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<My3DWebSocket> webSocketSet) {
        My3DWebSocket.webSocketSet = webSocketSet;
    }
    
}
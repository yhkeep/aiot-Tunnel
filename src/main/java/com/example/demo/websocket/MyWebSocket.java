package com.example.demo.websocket;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.example.demo.controller.UserController;
import com.example.demo.service.AssetService;
import com.example.demo.util.LogUtil;
import com.example.demo.util.excetion.LogutilExcetion;
@ServerEndpoint(value = "/websocket/{verification}")
@Component
public class MyWebSocket {
	
	@Autowired
    private AssetService assetService;
	
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//	在外部可以获取此连接的所有websocket对象，并能对其触发消息发送功能，我们的定时发送核心功能的实现在与此变量 
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

//  用户会话
    private Set<Session> user_session = new HashSet<Session>();
    
    
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
	private String verification;
	
	
//	提供url路径中token获取
    public String getVerification() {
		return verification;
	}


	/**
     * 连接建立成功调用的方法
     * @throws ParseException 
     * @throws EncodeException */
    
    @OnOpen
    public void onOpen(Session session,@PathParam("verification") String verification) throws ParseException {
        this.session = session;
        this.verification = verification;
        this.user_session = user_session;
        
        
//      token验证 ,反射拦截验证，但是线程不安全
        String token = UserController.token;
        
        
//      jwt解码缓存中用户名称,可与数据库中存在用户进行比较
        String username = JWT.decode(this.verification).getAudience().get(0);
        
//      TODO 0817多个用户存在同时登陆后，需要在不同会话客户端建立连接发送数据
//        LogUtil.logger.info(token+"=token/websocket/"+this+"=this="+this.verification+"=this.verification");
        
        if(this.verification.equals(token) || !StringUtils.isEmpty(username)) { 
    		 // 将session按照房间名来存储，将各个房间的用户隔离
    		user_session.add(this.session);
    		webSocketSet.add(this);     //加入set中
        
    	}
/*      if(!token.equals(this.verification)) {
        	int a = 1/0;
        }else {
        	
        	webSocketSet.add(this);     //加入set中
        	
        }
*/        
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
    	user_session.remove(this.session);
        webSocketSet.remove(this);  //从set中删除
    }


//     * 发生错误时调用
    @OnError
    public void onError(Session session, Throwable error) {
//    	LogutilExcetion.logger.error(error+"/websocket/自定义制造异常");
    }

//    多客户端发送
    public void sendMessage(String message) throws IOException {
    	for (Session usersession : user_session) {
    		usersession.getBasicRemote().sendText(message);
		}
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
    public static CopyOnWriteArraySet<MyWebSocket> getWebSocketSet() {
        return webSocketSet;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<MyWebSocket> webSocketSet) {
        MyWebSocket.webSocketSet = webSocketSet;
    }
    
}
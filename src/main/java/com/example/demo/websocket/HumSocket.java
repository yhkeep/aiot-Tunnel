package com.example.demo.websocket;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.example.demo.controller.UserController;
import com.example.demo.util.excetion.LogutilExcetion;
@ServerEndpoint(value = "/hum/websocket/{verification}")
@Component
public class HumSocket {
	
	
	
    private static CopyOnWriteArraySet<HumSocket> webSocketSet = new CopyOnWriteArraySet<HumSocket>();

    
    
    private Session session;
	private String verification;
	private Set<Session> user_session = new HashSet<Session>();
	

	
	
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

        
        String token = UserController.token;
        
        String username = JWT.decode(this.verification).getAudience().get(0);
        
        if(this.verification.equals(token) || !StringUtils.isEmpty(username)) {
    		user_session.add(this.session);
    		webSocketSet.add(this);    
        
    	}
        
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
//    	LogutilExcetion.logger.error(error+"/hum/websocket自定义制造异常");
    }

    
    public void sendMessage(String message) throws IOException {
    	for (Session usersession : user_session) {
    		usersession.getBasicRemote().sendText(message);
		}
    }

    
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
//定时任务核心代码
    public static CopyOnWriteArraySet<HumSocket> getWebSocketSet() {
        return webSocketSet;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<HumSocket> webSocketSet) {
    	HumSocket.webSocketSet = webSocketSet;
    }
    
}
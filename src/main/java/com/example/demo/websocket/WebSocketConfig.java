package com.example.demo.websocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
@Configuration
public class WebSocketConfig {  
	

	/**
	 * 开启WebSocket支持
	 * @author yanghan
	 * 进行service层测试注释掉注解即可
	 */
		
	    @Bean  
	    public ServerEndpointExporter serverEndpointExporter() {  
	        return new ServerEndpointExporter();  
	    }  
	  
}

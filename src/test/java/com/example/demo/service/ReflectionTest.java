/*package com.example.demo.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.ServerEndpoint;

import com.alibaba.druid.util.StringUtils;
import com.example.demo.controller.UserController;
import com.example.demo.domain.Gateway;

public class ReflectionTest {
	
	
	public static void main(String[] args) throws Exception {
         
      //获取Class对象
//      Class perClass = Asset.class;
    	
//      建立连接之前进行断链，注解测试动态参数获取
    	
    	Class wsClass = Class.forName("com.example.demo.websocket.HumWebSocket");
//    	获取注解接口，并且获取其中值
    	AnnotatedType[] annotatedInterfaces = wsClass.getAnnotatedInterfaces();
    	for (AnnotatedType annotatedType : annotatedInterfaces) {
    		System.out.println(annotatedType.getClass().getField("value"));
			
		}
    	
    	ServerEndpoint serpoint = (ServerEndpoint) wsClass.getAnnotation(ServerEndpoint.class);
    	
    	
    	System.out.println(serpoint.value());
    	
         
    }
	
	public static void main(String[] args) throws Exception {
         
		try {
			 assert 32<2;
			System.out.println("yanghan");
		} catch (Exception e) {
			// TODO: handle exception
		}
    	System.out.println(3);
         
    }
	
	
}*/
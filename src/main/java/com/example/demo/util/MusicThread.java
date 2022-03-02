package com.example.demo.util;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.example.demo.controller.GatewayController;

//1):定义一个类A继承于java.lang.Thread类.  
public class MusicThread extends Thread{  
    //2):在A类中覆盖Thread类中的run方法.  
    public void run() {  
        //3):在run方法中编写需要执行的操作  
    	GatewayController gateway = new GatewayController();
        try {
			gateway.initAllGateway();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
    }  
}  
  

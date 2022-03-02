package com.example.demo.mqtt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class Tisk extends TimerTask{
	public MqttClient client;

	public Tisk(MqttClient client) {
		super();
		this.client = client;
	}
// 订阅完成后，开启回调函数，并且进行数据解析
	@Override
	public void run() {
		// TODO Auto-generated method stub
		client.setCallback(new MqttPushCallback());
//		System.out.println("测试");
		
	}
}
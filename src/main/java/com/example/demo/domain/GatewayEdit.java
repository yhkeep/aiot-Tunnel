package com.example.demo.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayEdit implements Serializable{
	private String gatewaymac;
	private	String disableLED; //是否开启led灯
	private	String isLongBright; //闪烁开启
	private	String isUploadUnkown; 
	private	String isUploadIBeacon;
	private	String isUploadS1;
	private	String isUploadGateway;
	private	String isauto; //断线自动重启
	private	String timeout; //自动重启时间
	private	String macReg; //mac过滤
	private String mqttUrl; //mqtt服务指向
	private	String publishTopic;
	private	String subscribeTopic;
	private	String responseTopic;
	private	String qos; //数据传输等级
	private	String userName; //mqtt 服务器用户名
	private	String passWord; //mqtt 服务器密码
	private	String hUrl; 
	private	String httpUser; //http 用户名
	private	String httpPass; //http 密码
	
	
	private String port;//服务器指向端口号
}
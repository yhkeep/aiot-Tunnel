package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HygrothermographWarn {
//	网关
	private String gatewaymac;
//	温度
	private String temperature;
//	湿度
	private String humidity;
//	当前时间
	private String currentTime;
//	标签mac
	private String mac;
	
//	告警数值
	private String warnnum;
//	电量
	private String electric;
	
//	解除告警数值
	private String relieveWarn;
//	解除告警时间
	private String relieveTime;
//	解除告警温度
	private String relieveTemp;
//	解除告警湿度
	private String relieveHum;
	
}
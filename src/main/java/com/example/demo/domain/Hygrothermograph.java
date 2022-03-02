package com.example.demo.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hygrothermograph {
//	网关
	private String gatewaymac;
//	温度
	@Excel(name = "温度", orderNum = "2")
	private String temperature;
//	湿度
	@Excel(name = "湿度", orderNum = "3")
	private String humidity;
//	当前时间
	@Excel(name = "更新时间", orderNum = "1")
	private String currentTime;
//	标签mac
	@Excel(name = "MAC地址", orderNum = "0")
	private String mac;
	
//	告警数值
	private String warnnum;
//	电量
	@Excel(name = "仪器电量", orderNum = "4")
	private String electric;
	
	
	
	
}
/*package com.example.demo.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class hygrothermograph {
//	网关
	private String gatewaymac;
//	温度
	@Excel(name = "温度", orderNum = "0")
	private String temperature;
//	湿度
	@Excel(name = "湿度", orderNum = "1")
	private String humidity;
//	当前时间
	@Excel(name = "更新时间", orderNum = "2")
	private String currentTime;
//	标签mac
	@Excel(name = "仪器mac地址", orderNum = "3")
	private String mac;
	
//	告警数值
	private String warnnum;
//	电量
	@Excel(name = "仪器电量", orderNum = "4")
	private String electric;
	
	
	
	
	public String getElectric() {
		return electric;
	}
	public void setElectric(String electric) {
		this.electric = electric;
	}
	public String getWarnnum() {
		return warnnum;
	}
	public void setWarnnum(String warnnum) {
		this.warnnum = warnnum;
	}
	public String getGatewaymac() {
		return gatewaymac;
	}
	public void setGatewaymac(String gatewaymac) {
		this.gatewaymac = gatewaymac;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	
	public hygrothermograph() {
		super();
	}
	public hygrothermograph(String gatewaymac, String temperature, String humidity, String currentTime, String mac,
			String warnnum, String electric) {
		super();
		this.gatewaymac = gatewaymac;
		this.temperature = temperature;
		this.humidity = humidity;
		this.currentTime = currentTime;
		this.mac = mac;
		this.warnnum = warnnum;
		this.electric = electric;
	}
	@Override
	public String toString() {
		return "hygrothermograph [gatewaymac=" + gatewaymac + ", temperature=" + temperature + ", humidity=" + humidity
				+ ", currentTime=" + currentTime + ", mac=" + mac + ", warnnum=" + warnnum + ", electric=" + electric
				+ "]";
	}
	
	
}*/
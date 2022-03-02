package com.example.demo.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Received {
//	标签mac
	private String mac;
//	网关
	private String gatewaymac;
//	创建时间
	private String createtime;
//	更新时间
	private String updatetime;
//	更新rssi
	private String rssi;
//	电量
	private String battery;
	
//	楼层,临时属性
	private String floor;
	
	
	
	
	public Received(String mac, String gatewaymac, String createtime, String updatetime, String rssi) {
	super();
	this.mac = mac;
	this.gatewaymac = gatewaymac;
	this.createtime = createtime;
	this.updatetime = updatetime;
	this.rssi = rssi;
	}
	
	
	
	public Received() {
		super();
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public Received(String mac, String gatewaymac, String createtime, String updatetime, String rssi, String battery,
			String floor) {
		super();
		this.mac = mac;
		this.gatewaymac = gatewaymac;
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.rssi = rssi;
		this.battery = battery;
		this.floor = floor;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getGatewaymac() {
		return gatewaymac;
	}
	public void setGatewaymac(String gatewaymac) {
		this.gatewaymac = gatewaymac;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getRssi() {
		return rssi;
	}
	public void setRssi(String rssi) {
		this.rssi = rssi;
	}
	@Override
	public String toString() {
		return "Received [mac=" + mac + ", gatewaymac=" + gatewaymac + ", createtime=" + createtime + ", updatetime="
				+ updatetime + ", rssi=" + rssi + "]";
	}
	public String getBattery() {
		return battery;
	}
	public void setBattery(String battery) {
		this.battery = battery;
	}
	
	
}
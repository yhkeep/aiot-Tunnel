package com.example.demo.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class IbeaconGateway {
//	ibeacon标签
//	发送时间
//	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private String timestamp;
//	类型
	private String type;
//	mac
	private String mac;
//	设备的 BLE 名字
	private String bleName;
//	rssi值
	private String rssi;
//	电量
	private String battery;
//	网关
	private String gatewaymac;
//	距离
	private String distance;
	
//	最大rssi更新时间
	private String maxRssiUpdateTime;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getBleName() {
		return bleName;
	}

	public void setBleName(String bleName) {
		this.bleName = bleName;
	}

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public String getGatewaymac() {
		return gatewaymac;
	}

	public void setGatewaymac(String gatewaymac) {
		this.gatewaymac = gatewaymac;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getMaxRssiUpdateTime() {
		return maxRssiUpdateTime;
	}

	public void setMaxRssiUpdateTime(String maxRssiUpdateTime) {
		this.maxRssiUpdateTime = maxRssiUpdateTime;
	}

	public IbeaconGateway() {
		super();
	}

	public IbeaconGateway(String timestamp, String type, String mac, String bleName, String rssi, String battery,
			String gatewaymac, String distance, String maxRssiUpdateTime) {
		super();
		this.timestamp = timestamp;
		this.type = type;
		this.mac = mac;
		this.bleName = bleName;
		this.rssi = rssi;
		this.battery = battery;
		this.gatewaymac = gatewaymac;
		this.distance = distance;
		this.maxRssiUpdateTime = maxRssiUpdateTime;
	}

	@Override
	public String toString() {
		return "IbeaconGateway [timestamp=" + timestamp + ", type=" + type + ", mac=" + mac + ", bleName=" + bleName
				+ ", rssi=" + rssi + ", battery=" + battery + ", gatewaymac=" + gatewaymac + ", distance=" + distance
				+ ", maxRssiUpdateTime=" + maxRssiUpdateTime + "]";
	}
	
	
	





}

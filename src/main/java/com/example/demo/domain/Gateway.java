package com.example.demo.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Gateway implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	类型
	private String type;	
//	网关
	private String gatewaymac;
//	网关地址
	private String gatewayfree;
	private String gatewayload;
//	网关原始位置
	private String location;
//	更新时间
	private String updatetime;
	
	private String department;
	
	private String gateway;
//	网关位置信息
	private String	floor;
	private String	ipaddress;
	private String	mapx;
	private String	mapy;
	private String	cadMapRoomName;
	private String  roomnumber;
	private String	gatewayflag;
	
}
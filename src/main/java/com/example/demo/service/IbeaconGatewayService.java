package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.Received;

public interface IbeaconGatewayService {
	void insert(IbeaconGateway ibeacon);
	
	List<Map> find(String mac,String startTime,String endTime);
	
	List<Map> findmacByGatewayMac(String mac,String gatewaymac,String endTime,String currentTime);
	
	void updateRssi(IbeaconGateway ibeacon);
//	根据mac查询表中最后一次所在网关的位置
	List<Map> findGatewayByMac(String mac,String startTime,String currnetTime);
	
//	查询一天历史记录
	List<Map> searchOneDay(String startTime,String endTime);
//	1分中内进行报警
//	List<Map> findMacByMac(String gatewaymac,String mac,String currnetTime,String startTime);
	List<Map> findMacByMac(String mac,String currnetTime,String startTime);
//	保存监听的标签和网关
	void saveMac(Received re);
//	监听更新
	void updateRecevied(Received re);
	
	List<Map> findReceviedByMac(String mac);
//	更新网关字段
	void updateGatewayTime(String gatewaymac,String currenttime,String gateway);
//	标签
	List<Map> findMissing(String currentime);
	
	List<Map> findGatewayOline();
	
	List<Map> findGatewayOlineByTime(String date);
	
	
	List<Map> findmaxRssiByMac(String macmac,String startTime,String currentTime);
	
//	查询最后一条数据
	List<Map> searchLastDatas();
//	查询ib中mac所有有关gateway数据
	List<Map> findIbGatewayByMac(IbeaconGateway ibeacon);
	
	void updateRssiAndTime(String mac,
			 String gatewaymac,String oldmaxRssiUpdateTime,
			 String rssi,String distance
			,String maxRssiUpdateTime);

}

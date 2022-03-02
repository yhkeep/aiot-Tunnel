package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.IbeaconHistory;

public interface IbeaconHistoryService {
	
	
	void insert(IbeaconHistory ibeacon);
	
//	单位时间ap以及其楼层，rssi等添加和切换
	void updateRssiAndAp(IbeaconHistory ibeacon);
	
	
//	 查询ap创建记录，及时间切片中历史记录 
	List<Map> findApBytime(String mac,String timestamp);
	
	
}

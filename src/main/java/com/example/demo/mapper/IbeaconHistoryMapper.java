package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.IbeaconHistory;
import com.example.demo.domain.Received;

public interface IbeaconHistoryMapper {
//	插入数据
	void save(IbeaconHistory ibeacon);
	
	
//	更新数据
	void updateRssiAndTime(IbeaconHistory ibeacon);
	
	
//	查询单位时间内ap移动记录
	List findMacBytime(@Param("mac")String mac,@Param("startTime")String startTime,@Param("currnetTime")String currnetTime);
	
//	查询ibeacon存在历史记录
	List findApBytime(@Param("mac")String mac,@Param("timestamp")String timestamp);
	
}

package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Maintainhistory;

public interface IMaintainhistoryMapper { 
	void insertMaintainHistory(Maintainhistory mih);
	List<Map> queryUnionMaintainHistory(@Param("maintainhistoryonlyCode")String maintainhistoryonlyCode,
			@Param("timeStart")String timeStart,@Param("timeEnd")String timeEnd);
}

package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Check;

public interface ICheckMapper {
	void insertChecked(Check check);
	
	List<Check> queryChecked();
	
	List<Check> queryCheckByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);
}

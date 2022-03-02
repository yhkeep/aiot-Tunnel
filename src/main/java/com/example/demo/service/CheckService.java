package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Check;

public interface CheckService {
//	添加盘点
	void addChecked(Check check) throws Exception;
//	查询所有盘点历史
	List<Check> queryCheck() throws Exception;
//	查询今日盘点通过时间
	List<Check> queryCheckByTime(String startTime,String endTime) throws Exception;
	
	
}

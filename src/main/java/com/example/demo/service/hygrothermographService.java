package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.github.pagehelper.PageInfo;

public interface hygrothermographService {
//	温湿度详细
	List<Hygrothermograph> queryhy(String mac,String startime,String endtime);
//	开区间查询
	List<Map> queryHyByTime(String mac,String startime,String endtime);
//	闭区间查询
	List<Map> queryHyByEndTime(String mac,String startime,String endtime);
	
	List<Humiture> queryhum(Humiture humit);
//	区间限制
	List<Humiture> getHumSection(String mac);
	
	List<Hygrothermograph> queryCurrenthum(String mac);
	
	void saveHy(Hygrothermograph hy);
	
	void updateHum(Humiture humit);
	
	PageInfo<Hygrothermograph> getDatasPage(Integer pageIndex, Integer pageSize, String startime,String endtime,String mac) throws Exception;
	
//	更新温湿度表中数据
	void updateHy(Map<String, Object> map);
	
//	保存设备
	void saveHumiture(Humiture hum);
	
	List<Map> getWarnDatas(String startTime,String endTime);
	
	void delEquipment(List delEquipment);
}

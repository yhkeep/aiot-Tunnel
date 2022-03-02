package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;

public interface IhygrothermographMapper {
//	温湿度明细
	List<Hygrothermograph> search(@Param("mac")String mac,@Param("startime")String startime,@Param("endtime")String endtime);
//	时间区查询
	List<Map> searchByTime(@Param("mac")String mac,@Param("startime")String startime,@Param("endtime")String endtime);
//	闭时间区查询
	List<Map> searchByEndTime(@Param("mac")String mac,@Param("startime")String startime,@Param("endtime")String endtime);
	
	List<Humiture> queryhumDatas(Humiture humit);
	
	List<Humiture> queryLimit(@Param("mac")String mac);
	
	List<Hygrothermograph> getHumCurrentime(@Param("mac")String mac);
	
	void insertHy(Hygrothermograph hy);
	
	void updateHumDatas(Humiture humit);
	
//	分页查询
    List<Map> getDatasByParameter(Map<String, Object> map) throws Exception;
    
    void updateHyWarnnum(Map<String, Object> map);
    
//  设备添加
    void insertHumiture(Humiture hum);
    
//  设备阀值告警
    List<Map> getDatasWarnValue(@Param("startime")String startime,@Param("endtime")String endtime);
    
    void delEquipment(@Param("humitureEquipment") List humitureEquipment);
}

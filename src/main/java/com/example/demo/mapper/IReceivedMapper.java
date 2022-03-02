package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Asset;
import com.example.demo.domain.Received;
import com.github.pagehelper.Page;

public interface IReceivedMapper {
	List<Map> queryRG(String mac);
	
//	资产明细
	List<Map> querymapByUpdateTime(String timeEnd);
//	可授权资产明细
	List<Map> queryAssetByUpdateTime(@Param("timeEnd")String timeEnd,@Param("department") String department);
	
	List<Map> querymap();
	
	List<Asset> queryIbeacon();//只获取资产信息
	
	
}

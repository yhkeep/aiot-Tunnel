package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.Received;

public interface IbeaconGatewayMapper {
//	插入数据
	void save (IbeaconGateway ibeaconWay);
//	更新数据
	void update (IbeaconGateway ibeaconWay);
	
//	查询一周内变化数据
	List<IbeaconGateway> queryWay(@Param("AssetID")String AssetID,@Param("startTime")String startTime,@Param("endTime")String endTime);
//	一分钟内数据的获取，传递需要的参数进行匹配
	List findByTime(@Param("mac")String mac,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
//	最后一次网关出现的位置
	List findGatewayMacByMac(@Param("mac")String mac,@Param("startTime")String startTime,@Param("currnetTime")String currnetTime);
//	过滤数据
	List findMacBytime(@Param("mac")String mac,@Param("gatewaymac")String gatewaymac,@Param("endTime")String endTime,@Param("currentTime")String currentTime);
//	查询一天记录
	List findDataByOneDay(@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	List findMaxNewGateway(@Param("mac")String mac,@Param("startTime")String startTime,@Param("currentTime")String currentTime);
	
//	查询是否表中存在mac
	/*List searchMacByMac(@Param("gatewaymac")String gatewaymac,@Param("mac")String mac,
			@Param("currnetTime")String currnetTime,@Param("startTime")String startTime);*/
	List searchMacByMac(@Param("mac")String mac,
			@Param("currnetTime")String currnetTime,@Param("startTime")String startTime);
	
//监听持久化和更新
	void persistence(Received re);
	
	void updateRecevied(Received re);
//	一般数据查询
//	List findRecevied(@Param("gatewaymac")String gatewaymac,@Param("mac")String mac);
	List findRecevied(@Param("mac")String mac);
//	网关更新数据
	void updateGatewayMac(@Param("gatewaymac")String gatewaymac,
			@Param("currenttime")String currenttime,
			@Param("gateway")String gateway);
//	关联查询丢失的标签,超过一分钟
	List<Map> findmissing(@Param("currentime")String currentime);
	
//	网关
	List<Map> findGateway();
	
	List<Map> findGatewayByTime(@Param("dateTime")String dateTime);
	
	
	List<Map> searchLastData();
	
	List<Map> findGatewayByMac(IbeaconGateway ib);
	
	void updateRssiAndTime(
			@Param("mac")String mac,
			@Param("gatewaymac")String gatewaymac, @Param("oldmaxRssiUpdateTime")String oldmaxRssiUpdateTime,
			@Param("rssi")String rssi, @Param("distance")String distance
			, @Param("maxRssiUpdateTime")String maxRssiUpdateTime);
}

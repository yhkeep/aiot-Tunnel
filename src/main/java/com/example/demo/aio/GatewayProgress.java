package com.example.demo.aio;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.basecompoent.BaseCompoent;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.IbeaconHistory;
import com.example.demo.domain.Received;
import com.example.demo.mapper.IbeaconGatewayMapper;
import com.example.demo.mapper.IbeaconHistoryMapper;
import com.example.demo.mapper.IhygrothermographMapper;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.GatewayService;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.service.ReceivedService;
import com.example.demo.util.LogUtil;

@Component
public class GatewayProgress<K> extends BaseCompoent{
	
	@Autowired
    private IbeaconGatewayService ibeaconService;
	
	@Autowired
	private IbeaconGatewayMapper ibeaconMapper;
	
	@Autowired
	private IbeaconHistoryMapper ibeaconHistoryMapper;
	
	@Autowired
	private IhygrothermographMapper hygrothermographMapper;
	
	@Autowired
	private GatewayService gatewayService;
	
	@Autowired
	private ReceivedService reService;
	
	@Resource
    private RedisUtils redisUtil;
	
	public static GatewayProgress task;
	
	@PostConstruct
    public void init() {
		task = this;
    }
//	更新网关
	public String updateGateway(K gatewaymac,K currnettime) {
		task.ibeaconService.updateGatewayTime(gatewaymac+"",currnettime+"","online");
		return "事务更新成功";
	}
	
	public List<Gateway> searchGateway() {
		List<Gateway> allGateway = task.gatewayService.searchAllGateway();
		return allGateway;
	}
	
//	更新received
	public String updateReceviedDatas(Received re) {
		// TODO Auto-generated method stub
		task.ibeaconMapper.updateRecevied(re);
		return "事务更新成功";
	}
	
	public List<Map> queryRecevied(K mac) {
		// TODO Auto-generated method stub
		List<Map> searchRG = task.reService.searchRG((String)mac);
		return searchRG;
	}
	
//	插入ibeaconHistory历史数据
	public String insertHistoryDatas(IbeaconHistory ibeacon) {
		// TODO Auto-generated method stub
		task.ibeaconHistoryMapper.save(ibeacon);
		return "事务更新成功";
	}
//	更新ibeaconHistory历史数据
	public String updateHistoryDatas(IbeaconHistory ibeacon) {
		// TODO Auto-generated method stub
		task.ibeaconHistoryMapper.updateRssiAndTime(ibeacon);
		return "事务更新成功";
	}
//	查询ibeaconHistory历史存在数据
	public List<IbeaconHistory> queryHistoryDatas(String mac,String timestamp) {
		// TODO Auto-generated method stub
		List findMacBytime = task.ibeaconHistoryMapper.findApBytime(mac, timestamp);
		
		return findMacBytime;
	}
//	查询温湿度历史数据
	public List<Map> queryHyByTime(String mac,String startime,String currentTime) {
		// TODO Auto-generated method stub
		List<Map> hy = task.hygrothermographMapper.searchByTime(mac,startime,currentTime);
		return hy;
	}
	
    
}
	

 


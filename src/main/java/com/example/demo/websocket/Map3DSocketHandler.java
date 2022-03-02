package com.example.demo.websocket;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Received;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.service.ReceivedService;


@Component
public class Map3DSocketHandler {
	
	@Autowired
    private ReceivedService receivedService;
	@Autowired
	private IbeaconGatewayService ibeaconService;
	
	public static Map3DSocketHandler task;
	//初始化
	@PostConstruct
    public void init() {    
		task = this;
    }
//    返回数据
    public List<Map> getMapDatas(String timeEnd){
//    	获取标签即刻位置信息
    	List<Map> ls = task.receivedService.searchAssert(timeEnd);
    	return ls;
    }
//    返回ibeacon数据
    public List<Map> getReceviedDatas(String mac,String currnetTime,String startTime){
//    	获取标签即刻位置信息
    	List<Map> ls = task.ibeaconService.findMacByMac(mac,currnetTime,startTime);
    	return ls;
    }
    
}
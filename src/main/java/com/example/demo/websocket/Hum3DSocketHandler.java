package com.example.demo.websocket;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Received;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.service.ReceivedService;
import com.example.demo.service.hygrothermographService;


@Component
public class Hum3DSocketHandler {
	
	@Autowired
	private hygrothermographService hyService;
	
	public static Hum3DSocketHandler task;
	//初始化
	@PostConstruct
    public void init() {    
		task = this;
    }
//    返回数据
   /* public List<Map> getMapDatas(String timeEnd){
//    	获取标签即刻位置信息
    	List<Map> ls = task.receivedService.searchAssert(timeEnd);
    	return ls;
    }*/
	public List<Map> getWarnDatas(String startTime,String endTime){
//		task.hyService.getWarnData();
		return null;
	}
    
}
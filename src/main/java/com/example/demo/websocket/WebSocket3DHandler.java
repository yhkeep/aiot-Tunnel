package com.example.demo.websocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.example.demo.mapper.IAssetMapper;
import com.example.demo.service.AssetService;
import com.example.demo.util.JsonUtils;

@Component
public class WebSocket3DHandler {
	
	@Autowired
	private IAssetMapper mapper;
	@Autowired
    private AssetService assetService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	public static WebSocket3DHandler task;
	//初始化
	@PostConstruct
    public void init() {    
		task = this;
    }
//	测试
    /*public List<Map> text(){
    	String minutes = "2019-08-30 09:34:14";
    //调用方法
    	List<Map> ls = task.assetService.findAssetByRece(minutes);
    	return ls;
    }*/
//  3D返回所有部门数据
    public List<Map> getAssetDatas(String Time){
    	/*List ls = new ArrayList();
    	ls.add("住院手术室");*/
    	
    	List<Map> result = task.assetService.find3DAssetByRece(Time);
    	return result;
    }
    
}
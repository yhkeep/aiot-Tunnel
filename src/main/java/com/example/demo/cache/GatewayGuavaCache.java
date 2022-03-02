package com.example.demo.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.example.demo.aio.GatewayProgress;
import com.example.demo.domain.Gateway;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.parameter.GatewayParameterField;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GatewayGuavaCache extends GuavaCache<String,Gateway>{
	
	
//	  一级缓存
	public Cache<String,Gateway> cache= CacheBuilder.newBuilder().maximumSize(GatewayParameterField.cacheGateway_number)
	           .expireAfterAccess(GatewayParameterField.cacheGateway_destroytime,TimeUnit.SECONDS)
	           .build();
	
	public void putCache(String key,Gateway value) {
		cache.put(key, (Gateway) value);
	}
	
	public Gateway getCache(Object keyValue) {
		// TODO Auto-generated method stub
//		Object value = null;
		Gateway ga = null;
		try {
			// 从缓存获取数据
			ga = cache.get((String) (keyValue),  new Callable<Gateway>(){
				// 返回网关数据库数据 
				@SuppressWarnings("unchecked")
				public Gateway call() {
					Gateway gate = new Gateway();
					gate.setGatewaymac(null);
					return  gate;
				}
			});
			
		} catch (ExecutionException e) {
//			e.printStackTrace();
			LogUtil.logger.error("google cache"+e);
		}
		return ga;
	}
	
}

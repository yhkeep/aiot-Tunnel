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

public class GuavaCache<K, V>{
//	  一级缓存
	private Cache<K,V> cache= CacheBuilder.newBuilder().maximumSize(GatewayParameterField.cache_number)
	           .expireAfterAccess(GatewayParameterField.cache_destroytime,TimeUnit.SECONDS)
	           .build();
	
//	放入缓存中
	public void putCache(K key,V value) {
		cache.put(key, value);
	}

	
	public Object getCache(Object keyValue) {
		// TODO Auto-generated method stub
		Object value = null;
		try {
			// 从缓存获取数据
			value = cache.get((K) (keyValue), new Callable<V>() {
				// 返回网关数据库数据 
				@SuppressWarnings("unchecked")
				public V call() {
					List<V> ls = new ArrayList<>();
					ls.add((V) "google重新缓存加载"); 
					return (V) ls;
				}
			});
			
			
		} catch (ExecutionException e) {
			e.printStackTrace();
			LogUtil.logger.error("google cache"+e);
		}
		return value;
	}
	
}

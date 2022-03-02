package com.example.demo.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.example.demo.util.parameter.UserParameterField;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
public class GuavaAssetData<K,V>{
	
   @SuppressWarnings("unchecked")
   public static Cache cache = CacheBuilder.newBuilder().
			maximumSize(100000)//设置容量上限
			.expireAfterAccess(UserParameterField.assetCacheTime, TimeUnit.SECONDS)
			.removalListener(new RemovalListener<String, List<Object>>() {
				//移除监听器
				@Override
				public void onRemoval(RemovalNotification<String, List<Object>> notification) {
//					System.out.println(notification.getKey()+"=移除监听事件"+notification);
				}

			}).build();
  

           
	public Object getCache(K keyValue) {
		
		Object value = null;
		try {
			// 从缓存获取数据
			value = cache.get((K)(keyValue), new Callable<V>() {
				// 返回数据库数据
				@SuppressWarnings("unchecked")
				public V call() {
					 
					  /*TODO0927
					  MapSocketHandler handler = new MapSocketHandler();
					 List<Map> mapDatas = handler.getMapDatas((String)keyValue);*/
					
					 Map map = new HashMap();
					 return (V)map;
					
				}
			});
			
			
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return value;

	}
           
	public void putCache(K key,V value) {
		cache.put(key, value);
	}
}

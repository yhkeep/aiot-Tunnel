package com.example.demo.util.CompletableFutureStream;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.example.demo.aio.GatewayProgress;
import com.example.demo.domain.Gateway;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class BaseCompletableFuture<K,V>{
	private GatewayProgress<K> gatewayProgress = new GatewayProgress<K>();
	private timeUtiles timeUtile = new timeUtiles();
	private static int timeduce = 5*60;
//	一级缓存20秒
	private Cache<K, V> cache= CacheBuilder.newBuilder().maximumSize(2)
	           .expireAfterAccess(15,TimeUnit.SECONDS)
	           .build();
	
	
	public Object getCache(K keyValue) {
			
			Object value = null;
			try {
				// 从缓存获取数据
				value = cache.get((K) (keyValue), new Callable<V>() {
					// 返回数据库数据 ，子类给出定义类
					@SuppressWarnings("unchecked")
					public V call() {
						List<V> ls = new ArrayList<>();
						ls.add((V) "缓存加载"); 
						return (V) ls;
					}
				});
				
				
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			 
			
			return value;
	
		
	
	}
	
//	放入缓存中
	public void putCache(K key,V value) {
		cache.put(key, value);
	}
	  
	public void future(K gatewaymac,K currnettime) {
		
 		
// 		如果存在缓存则继续获取/获取缓存。
 		Object cache = getCache(gatewaymac);
 		
 		List<V> userCode = (List<V>) cache;
 		
 		
 		if(!userCode.isEmpty() && null != userCode) {
 			/*//        	线程池中单个对象异步执行
        	List<CompletableFuture<String>> list = userCode.stream()
        			.map(V -> CompletableFuture.supplyAsync(  //通过map遍历后的对象进行下一步异步 
//        					() -> String.format("异步获取一级缓存数据:%s",V)//可返回对应Map类型，并解析
        					
//        					一级缓存中，网关与更新时间进行比较,无则更新缓存，有则跳过。
        					() -> {
        						return gatewayProgress.updateGateway(gatewaymac, currnettime);
        					}
        					
        					,executorService)
        				)
        			.collect(Collectors.toList());
        	
//        	遍历CompletableFuture对象，返回list
        	List<String> collect = list.stream().map(CompletableFuture::join).collect(Collectors.toList());
        	
        	*/
 			
 			
//        	限定线程池中线程数量
        	ExecutorService executorService = Executors.newFixedThreadPool(Math.min(userCode.size(), 100),
        			
        			new ThreadFactory() {
    					
    					@Override
    					public Thread newThread(Runnable r) {
    						// TODO Auto-generated method stub
    						Thread tr = new Thread(r);
    						tr.setDaemon(true);
    						return tr;
    					}
    				}
        			
        			);
        	
        	LogUtil.logger.info(userCode+"=usercode");
        	
        	
//        	抽取父类，使用泛型
        	getStream(userCode,executorService, gatewaymac, currnettime);
        	
 		}
 		
 		
	}
	
	public void getStream(List<V> userCode,ExecutorService executorService,K gatewaymac,K currnettime) {
		List<CompletableFuture<String>> list = userCode.stream()
    			.map(V -> CompletableFuture.supplyAsync( 
    					
//    					一级缓存中，网关与更新时间进行比较,无则更新缓存，有则跳过。
    					() -> {
    						return gatewayProgress.updateGateway(gatewaymac, currnettime);
    					}
    					
    					,executorService)
    				)
    			.collect(Collectors.toList());
    	
//    	遍历CompletableFuture对象，返回list
    	List<String> collect = list.stream().map(CompletableFuture::join).collect(Collectors.toList());
    	
    	LogUtil.logger.info(collect+"=collect");
	}
	
	
	
	
}
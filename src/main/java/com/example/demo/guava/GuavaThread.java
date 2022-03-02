package com.example.demo.guava;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.aio.AioProgress;
import com.example.demo.domain.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
public class GuavaThread<K,V>{
	
//   private Cache<K, V> cache= CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(10, TimeUnit.MINUTES).build();
	
//	3秒私有划无访问则过期
/*	private Cache<K, V> cache= CacheBuilder.newBuilder().maximumSize(2)
           .expireAfterAccess(3,TimeUnit.SECONDS)
           .build();;
*/   
   public static Cache cache = CacheBuilder.newBuilder().
		maximumSize(128)//设置容量上限
		.expireAfterAccess(3, TimeUnit.SECONDS)//若3秒内没有读写请求则进行回收
		.removalListener(new RemovalListener() {
			//移除监听器
				@Override
				public void onRemoval(RemovalNotification notification) {
//					System.out.println(notification.getKey());
 
				}
 
			}).build();
  
/*   public Object getCache(K keyValue,final String ThreadName){
	   Object value=null;
	   try {
		   //从缓存获取数据
		   value = cache.get(keyValue, new Callable<V>() {
//			返回数据库数据
			   @SuppressWarnings("unchecked")
			   public V call() {
//				System.out.println("ThreadName 执行业务数据并返回处理结果的数据（访问数据库等）=============="+ThreadName);
//				return (V) (new HttpHandler().read().toString());
				   
				   return (V) (new AioProgress().findUser(keyValue+"").toString());
			   }
		   });
	   } catch (ExecutionException e) {
		   e.printStackTrace();
	   }
	   return value;
   }
*/   

           
	public Object getCache(K keyValue) {
		
		Object value = null;
		try {
			// 从缓存获取数据
			value = cache.get((K) ((User)keyValue), new Callable<V>() {
				// 返回数据库数据
				@SuppressWarnings("unchecked")
				public V call() {
					return (V) (new AioProgress().findUser((User) keyValue));
				}
			});
			
			
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return value;

	}
           
}

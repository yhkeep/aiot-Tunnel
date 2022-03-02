package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Interceptor.PassToken;
import com.example.demo.domain.User;
import com.example.demo.guava.GuavaThread;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.UserService;


@RestController
public class RedisController{
//	private static int ExpireTime = 10;   // redis中存储的过期时间60s

    @Resource
    private RedisUtils redisUtil;
    
    @Autowired
    private UserService userService;

    @PassToken
    @RequestMapping(value = "jmeter/get", method = RequestMethod.POST)
    public Object redisget(@RequestBody User user){
    	
    	
//    	redis一级缓存获取数据  ,通过key获取值
//    	Object object = redisUtil.get(user.getUsername());
    	
    	
    	Map map = new HashMap();
    	map.put("name","yanghan");
    	
//    	map对应多个键值
//    	Object object1 = redisUtil.hmset(user.getUsername(),map);
    	
//    	通过hash获取值
    	Object object = redisUtil.hmget(user.getUsername());
    	
    	
    	
    	if(object == null) {
//    		guava 本地二级缓存获取数据
    		
//    		第一种方式本地缓存直接获取
    		/*ExecutorService executorService = Executors.newFixedThreadPool(5);
    		for(int i=0;i<5;i++){
	    		Future<Object> future = executorService.submit(new Callable<Object>() {
	
	                 @Override
	                 public Object call() throws Exception {
	                	 
	                	final GuavaThread<String,String> guavaThread=new GuavaThread<String,String>();
	             		Object cache = guavaThread.getCache(user.getName());
	             		
	                    return cache;
	                 }
	             });
	    		try {
	    			*//** 
	                 * 获得future对象之前可以使用isDone()方法检测future是否完成，完成后可以调用get()方法获得future的值， 
	                 * 如果直接调用get()方法，get()方法将阻塞值线程结束 
	                 *//*  
    				Object data = future.get();
//                	System.out.println(Thread.currentThread().getName()+"--"); 
//                	return data;
                	
                	
	    			
				} catch (Exception e) {
					// TODO: handle exception
					return ResultUtil.success("abv"+"接口jmeter/get");
				} finally {
					
				}
    		}

//    		关闭线程池
    		executorService.shutdown();*/
    		
    		
    		
//    		第二种方式通过本地缓存后，异步获取数据
    		final GuavaThread<User,User> guavaThread=new GuavaThread<User,User>();
    		
     		Object cache = guavaThread.getCache(user);
     		
//     		第三种通过netty中获取数据/添加数据--》redis--》异步从本地中一级缓存==》持久化数据库
     		
     		List<User> userCode = (List<User>) cache;
     		
     		if(!userCode.isEmpty() && null != userCode) {
     		
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
	        	
	//        	线程池中单个对象异步执行
	        	List<CompletableFuture<String>> list = userCode.stream()
	        			.map(u -> CompletableFuture.supplyAsync(  //通过map遍历后的对象进行下一步异步 
	        					() -> u.getRole()//可返回对应Map类型，并解析
	        					,executorService)
	        				)
	        			.collect(Collectors.toList());
	        	
	//        	遍历CompletableFuture对象，返回list
	        	List<String> collect = list.stream().map(CompletableFuture::join).collect(Collectors.toList());
	        	
	        	return collect;
	        	
     		}
    		
    		
    		
    		
    	}
    	
        return object;
    }
}
	

 


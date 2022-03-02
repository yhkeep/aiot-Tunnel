/*package com.example.demo.ThreadPools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

import javax.validation.constraints.Max;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.domain.User;

public class ThreadPoolCompletableFuture {
	 
	List<User> userCode;
    
    
    
    @Before
    public void before() {
    	
    	userCode = Arrays.asList(new User("yanghan","123","admin"),
    			new User("苏琴","18","admin"),
    			new User("小琴琴","21","admin"));
    }
	
//    线程阻塞获取数据
    public List<String> findUserName() {
        List<String> list = userCode.stream()
                .map(s -> String.format("%s:%s", s.getUsername(),s.getPhone()))
                .collect(Collectors.toList());
 
        return list;
    }
    
    public List<String> findUserName1(int id) {
    	
//    	限定线程池中线程数量
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
    	
//    	线程池中单个对象异步执行
    	List<CompletableFuture<String>> list = userCode.stream()
    			.map(user -> CompletableFuture.supplyAsync(  //通过map遍历后的对象进行下一步异步 
    					() -> String.format("%s:%s", user.getUsername(),user.getPhone())//返回对应Map类型，并解析
    					,executorService)
    			)
    			.collect(Collectors.toList());
    	
//    	遍历CompletableFuture对象，返回list
    	List<String> collect = list.stream().map(CompletableFuture::join).collect(Collectors.toList());
    	
    	return collect;
    }
    
    
    
	 @Test
	 public void test() {
	        long start = System.nanoTime();
	       
//	        List<String> list = findUserName(); // 线程阻塞1000导致时间过长
	        
	        List<String> list = findUserName1(100);
	        
	        System.out.println(list);
	        
	        System.out.println("Done in "+(System.nanoTime()-start)/1_000_000+" ms");
	        
	 }
}
*/
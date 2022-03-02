/*package com.example.demo.guava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.druid.util.StringUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class DemoGuava extends Thread{
		public static Cache<String, List<Object>> caches = CacheBuilder.newBuilder().
				maximumSize(128)//设置容量上限
				.expireAfterAccess(8, TimeUnit.SECONDS)//若3秒内没有读写请求则进行回收
				.removalListener(new RemovalListener<String, List<Object>>() {
					//移除监听器
					@Override
					public void onRemoval(RemovalNotification<String, List<Object>> notification) {
//						System.out.println(notification.getKey()+"缓存监听移除");
						
	 
					}
	 
				}).build();
	 
		public static List<Object> get(final String key) throws Exception{
			List<Object> rr = caches.get(key, new Callable<List<Object>>() {
	 
				@Override
				public List<Object> call() throws Exception {
					if ("1".equalsIgnoreCase(key)) {
						return null;
					}
					throw new Exception("This is a test!");
				}
			});
			return rr;
		}
	 
		
		public void putCache(String key,List<Object> value) {
			caches.put(key, value);
		}



		public static void main(String[] args) throws Exception {
			System.out.println(DemoGuava.get("1")+DemoGuava.caches.asMap().toString());
			
			System.out.println("before expire: " + DemoGuava.caches.asMap().keySet());
	        System.out.println("after expire: " + DemoGuava.caches.asMap().keySet());
	      
	        [
	         {departmentroom=, threeclassify=, generatebusinessname=, secondclassify=, Check=2020-08-28 17:06:36, remark=, Unit=, mac=AC233FA018E7, Money=0.00, LocDept=住院手术室, onecodelableImageUrl=/D:/huaxi/springbootdemo02/target//images/onecodelable/1598342046105, placeoforigin=, aboveImageUrl=/D:/huaxi/springbootdemo02/target//images/above/1598342046105, oneclassify=, imageUrl=/D:/huaxi/springbootdemo02/target//images/1598342046102, departmentcode=, homeofficename=, id=2, department=住院手术室, floor=b2, AssetID=yikecha123, Status=待处置, createtime=2020-08-27 16:55:27, rssi=-46, recorder=0280001, homeofficenumber=, isentrance=, Amount=1, specification=, fourclassify=, compressImageUrl=/D:/huaxi/springbootdemo02/target//images/compress/1598342046170, AssetName=, Mac=AC233FA018E7, Brand=, Type=, suppliername=, allroundImageUrl=/D:/huaxi/springbootdemo02/target//images/allround/1598342046104, paperlabelImageUrl=/D:/huaxi/springbootdemo02/target//images/paperlabel/1598342046105, gatewaymac=AC233FC03867, leftImageUrl=/D:/huaxi/springbootdemo02/target//images/left/1598342046103, GeneralName=, updatetime=2020-08-28 17:07:24, cadMapRoomName=报废库, applyoddnumbers=, Location=住院手术室, BuyDate=, rightImageUrl=/D:/huaxi/springbootdemo02/target//images/right/1598342046105}, 
	         {Status=待处置, createtime=2020-08-27 16:55:33, rssi=-54, Check=2020-08-28 17:06:36, mac=C2021A0000E3, Mac=C2021A0000E3, LocDept=住院手术室, gatewaymac=AC233FC07217, id=3, updatetime=2020-08-28 17:07:24, department=住院手术室, floor=b2, cadMapRoomName=报废库, AssetID=yikecha123456, Location=住院手术室}
	         ]一级缓存数据		
	        
	        List<Object> ls = new ArrayList<Object>();
	        Map map = new HashMap();
	        map.put("gatewaymac", "AC233FC07217");
	        map.put("updatetime", "2020-08-28 17:07:24");
	        map.put("mac", "C2021A0000E3");
	        map.put("AssetID", "yanghan123");
	        ls.add(map);
	        Map map1 = new HashMap();
	        map1.put("gatewaymac", "AC233FC03867");
	        map1.put("updatetime", "2020-08-28 17:07:24");
	        map1.put("mac", "AC233FA018E7");
	        map.put("AssetID", "yanghan123456");
	        ls.add(map1);
	        
	        DemoGuava gu = new DemoGuava();
	        gu.start();
	        DemoGuava gu1 = new DemoGuava();
	        gu1.start();
	        
	        gu.caches.put("yanghan", ls);//放入缓存数据
	        

	        

	        
	      
	       * 获取所有key值
	       *   System.out.println(gu.getName()+"=gu="+gu.caches.asMap().keySet());
	        gu1.sleep(3000);
	        System.out.println(gu1.getName()+"=gu1="+gu1.caches.asMap().keySet());
	        
	        
	        
	        
//	        System.out.println(gu.getName()+"=gu="+gu.getabc("yanghan")); 直接通过方法获取缓存数据
	        
//	        更新前缓存数据
	        System.out.println(gu.getName()+"=更新前缓存数据="+gu.caches.get("yanghan", new Callable<List<Object>>() {
	        	
	        	@Override
	        	public List<Object> call() throws Exception {
	        		// TODO Auto-generated method stub
	        		return null;
	        	}
	        	
	        }));
	        
	        
	        
	        List<Object> list = gu.caches.get("yanghan", new Callable<List<Object>>() {
	        	
	        	@Override
	        	public List<Object> call() throws Exception {
	        		// TODO Auto-generated method stub
	        		return null;
	        	}
	        	
	        });
	        
	        
	        
	        
	        
	        List<Object> collect = list.stream().filter(f -> StringUtils.equals(((Map)f).get("mac")+"","C2021A0000E3")).map(l -> 
	        	{
	        		((Map)l).put("gatewaymac", "AC233FC03867");
	        		return l;
	        	}
	        ).collect(Collectors.toList());
	        
	        System.out.println(collect);
	        
	        
	        
	        gu.caches.put("yanghan", collect);//更新缓存数据
	        
	        
	        System.out.println(gu.getName()+"=gu="+gu.caches.get("yanghan", new Callable<List<Object>>() {
	        	
	        	@Override
	        	public List<Object> call() throws Exception {
	        		// TODO Auto-generated method stub
	        		return null;
	        	}
	        	
	        }));
	        
	        
	        System.out.println(gu1.getName()+"=gu1="+gu1.caches.get("yanghan", new Callable<List<Object>>() {
	        	
	        	@Override
	        	public List<Object> call() throws Exception {
	        		// TODO Auto-generated method stub
	        		return null;
	        	}
	        	
	        }));
	        
	        
	        
	        
	        
		}

		
		
		@Override
	    public void run() {
	        super.run();
	    }
}
*/
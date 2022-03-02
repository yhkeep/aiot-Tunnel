/*package com.example.demo.service.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.druid.util.StringUtils;
import com.example.demo.redis.RedisUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.parameter.ParameterField;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
	@Resource
    private RedisUtils redisUtil;
	
	@Test
	public void redisMacTest() {
		String mac = "AC233FA02008";
		Map<Object, Object> gatewaymacByMac = redisUtil.hmget(mac);
		if(null != gatewaymacByMac && !gatewaymacByMac.isEmpty()) {
			Map<String,Object> mac2Gatewaymac = new HashMap<String,Object>();
			
			
			String currentTime= "2021-01-11 18:18:18";
			String gatewaymac = "AC233FC07217";
			Map map = new HashMap<>();
			map.put("rssi", "-45");
			map.put("currentTime", currentTime);
			mac2Gatewaymac.put("expressTime",currentTime);//设定失效时间
			LogUtil.logger.info(currentTime+"=expressTime");
			mac2Gatewaymac.put(gatewaymac,map);
			
			if(mac2Gatewaymac.containsKey("expressTime")) {
				System.out.println(123454);
			}
			
			redisUtil.del(mac);
			
			redisUtil.hmset(mac,mac2Gatewaymac);
		}
		
		
	}
	
//	通过value - stream 获取map - key
	@Test
	public void stream() {
//	  Map<String, Integer> map = ImmutableMap.of("A", 1, "B", 2, "C", 3, "D", 2);
	  Map<String, Integer> map = new HashMap();
	  map.put("a", 1);
	  map.put("b", 1);
	  map.put("c", 2);
	  
	  
	  System.out.println(getKeysByStream(map, 1));
	  
	}

	private <K, V> Set<K> getKeysByStream(Map<K, V> map, V value) {
	  return map.entrySet()
	    .stream()
	    .filter(kvEntry -> Objects.equals(kvEntry.getValue(), value))
	    .map(Map.Entry::getKey)
	    .collect(Collectors.toSet());
	}
	
}
*/
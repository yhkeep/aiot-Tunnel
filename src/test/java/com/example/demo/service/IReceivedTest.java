/*package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Received;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.websocket.MapSocketHandler;
import com.example.demo.websocket.WebSocketHandler;
import com.github.pagehelper.PageInfo;



@RunWith(SpringRunner.class)
@SpringBootTest
public class IReceivedTest {
	@Autowired
    private IbeaconGatewayService ibeaconService;
	@Autowired
	private ReceivedService reService;
//	[ 
//Received [mac=AC233FA031A0, gatewaymac=AC233FC001EA, createtime=2019-08-20 14:18:12, updatetime=2019-09-03 15:21:33], 
//Received [mac=AC233FA031C7, gatewaymac=AC233FC001EA, createtime=2019-08-29 17:05:54, updatetime=2019-09-03 15:21:22],]
//Received [mac=AC233FA031C7, gatewaymac=AC233FC001E8, createtime=2019-08-29 17:05:54, updatetime=2019-09-03 15:21:22],]
	
    @Test
    public void testAsset(){
    	List searchUser = new ArrayList<>(); 
		JSONArray userjson = new JSONArray();
		Map<String,Object> result = new HashMap<>();
		try {
//	    	temp校验令牌
	    		List ls = new ArrayList<>();
//	    		查询19.20号标签所在位置
	    		List<String> lm = new ArrayList<>();
	    		lm.add("C2021A00015F");
	    		lm.add("C2021A000160");
	    		for (int i = 0; i < lm.size(); i++) {
	    			Map map = new HashMap<>();
	    			List<Map> searchRG = reService.searchRG(lm.get(i)); 
	    			for (Map iot : searchRG) {
						map.put("location", iot.get("location"));
						map.put("gatewaymac", iot.get("gatewaymac"));
						map.put("mac", lm.get(i));
						map.put("updatetime", iot.get("updatetime"));
					}
	    			List<Map> searchAssert = reService.searchAssert();
	    			for (Map ele : searchAssert) {
	    				if(ele.get("mac").equals(lm.get(i))) {
	    					map.put("electric", ele.get("electric"));
	    				}
					}
	    			
	    			 ls.add(map);
	    		}
	    		result.put("msg",ls);
	    		
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "参数错误");
		}
		searchUser.add(result);
		userjson = JsonUtils.list2jsonArray(searchUser);
		System.out.println(userjson+"=userjson");
    }
    @Test
    public void testAsset1(){
    	MapSocketHandler handler = new MapSocketHandler();
    	List<Map> mapDatas = handler.getUserAssetDatas("2021-07-16 15:17:21","住院手术室,易柯尔");//授权可查看的部门资产数据
    	System.out.println(mapDatas+"==map");
    }
//    随机数
    @Test
    public void testin(){
    	Random rand = new Random();
		int num = 0;
    	for(int i=0; i<1; i++) {
    		num = rand.nextInt(20)+80;
    	}
    	System.out.println(num+"");
    	
//    	抛出异常
    	String a = "";
    	try {
			a = 3/0+"";
		} catch (Exception e) {
			// TODO: handle exception
			a = "3";
		}
    	System.out.println(a);
    }
}
*/
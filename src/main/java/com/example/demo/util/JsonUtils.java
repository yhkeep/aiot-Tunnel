package com.example.demo.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.LimitQueue;

public class JsonUtils {
//	字符串转list
	public static List<String> string2List(String str){
		String[] split = str.split(",");
    	List<String> department = Arrays.asList(split);
		return department;
	}
	
	public static String map2Json(Map map) {
        JSONObject json = new JSONObject(map);
        String str = json.toJSONString();
        return str;
	}
	public static JSONArray list2jsonArray(List list) {
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
		return array;
	}
	public static JSONObject map2JsonObject(Map map) {
        JSONObject jsonobject = new JSONObject(map);
        return jsonobject;
	}
//	解析数据
	public static Map json2String(List datas) {
		Map map = new HashMap();
		String oldGatewayMac = "";
		String oldMac = "";
		String oldRssi = "";
		String updatetime = "";
		int oldRssi1 = -10000;
		JSONArray updateGateway= JSONArray.parseArray(JSON.toJSONString(datas));
		
		for (int k = 0; k < datas.size(); k++) {
    		oldGatewayMac = updateGateway.getJSONObject(k).getString("gatewaymac");
    		oldMac = updateGateway.getJSONObject(k).getString("mac");
    		oldRssi =  updateGateway.getJSONObject(k).getString("rssi"); //rssi此处最大需要替换
    		oldRssi1 =  Integer.parseInt(oldRssi);
    		updatetime = updateGateway.getJSONObject(k).getString("updatetime");
    	}
		map.put("oldGatewayMac", oldGatewayMac);
		map.put("oldMac", oldMac);
		map.put("oldRssi", oldRssi);
		map.put("updatetime", updatetime);
		map.put("oldRssi1", oldRssi1);
		return map;
	}
	
//	获取集合中出现次数最多的元素并且返回
	public String getMaxEleNumber(List<String> l) {
	   if(null == l || l.size() ==0) return "-1";
		
	   Map<String,Integer> map=new HashMap<String,Integer>();
		   
	   Integer max_num = 1;
	   String max_ele = "";
	   for(String s:l){
		   if(map.containsKey(s) && !StringUtils.isEmpty(s)){
			   int q = map.get(s);
			   map.put(s,q+1);
//				   统计出现次数最多个数，以及元素
			   if(max_num < q+1) {
				   max_num = q+1;
				   max_ele = s;
			   }
		   }
		   else{
			   map.put(s,1);
//			     统计出现一次的个数，以及元素
			   if(max_num == 1) {
				   max_ele = s;
			   }
		   }
	   }
	   return max_ele; 
	}
//	获取集合中出现次数最多的元素和次数
	public Map getMaxEleAndNumber(List<String> l) {
		Map map_max = new HashMap<>();
		if(null == l || l.size() ==0) return map_max;
		
		Map<String,Integer> map=new HashMap<String,Integer>();
		
		Integer max_num = 1;
		String max_ele = "";
		for(String s:l){
			if(map.containsKey(s) && !StringUtils.isEmpty(s)){
				int q = map.get(s);
				map.put(s,q+1);
//				   统计出现次数最多个数，以及元素
				if(max_num < q+1) {
					max_num = q+1;
					max_ele = s;
				}
			}
			else{
				map.put(s,1);
//			     统计出现一次的个数，以及元素
				if(max_num == 1) {
					max_ele = s;
				}
			}
		}
		map_max.put("max_num",max_num);
		map_max.put("max_ap_ele", max_ele);
   		return map_max;
	}
	
	
//	获取集合中各自元素和元素出现的次数
	public Map<String,Integer> getEleAndNumber(List<String> l) {
		
		Map<String,Integer> map=new HashMap<String,Integer>();
		
	   if(null == l || l.size() ==0) return map;
		
		   
	   for(String s:l){
		   if(map.containsKey(s) && !StringUtils.isEmpty(s)){
			   int q = map.get(s);
			   map.put(s,q+1);
		   }
		   else{
			   map.put(s,1);
		   }
	   }
	  
	   return map; 
	   
	}
//	获取集合中最大连续元素和次数(TODO210519异常数据待定追踪节点)
	/*public Map getMaxContinuousEle(List<String> ls){
		Map map = new HashMap<>();
		if(null == ls || ls.size() ==0) return map;
		
		String[] arr = new String[ls.size()];
		ls.toArray(arr); //toArray只能转化引用类型，基本类型不可用
		int n = ls.size();
		int run = 1;
		int max_num = 0;
		String max_ap_ele = "";
	    for (int i = 0; i < n; ++i) {   // n is the size of array
	        if (i + 1 < n && arr[i] == arr[i + 1]) {
	            run++;        // increment run if consecutive elements are equal
	        } else {
//	            获取最大连续次数及元素
	            if(max_num<=run) {
	            	max_num = run;
	            	max_ap_ele = arr[i];
	            	map.put("max_ap_ele", max_ap_ele);
	        	    map.put("max_num",max_num);
	            }
	            run = 1;      // reset run if they are not equal
	        }
	    }
	    return map;
	}*/
	
	
}

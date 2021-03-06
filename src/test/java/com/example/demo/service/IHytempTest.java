/*package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.domain.HygrothermographWarn;
import com.example.demo.domain.LimitQueue;
import com.example.demo.util.ComponentUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.parameter.ParameterField;
import com.example.demo.util.parameter.UserParameterField;
import com.example.demo.util.sockettherd.HexConvert;



@RunWith(SpringRunner.class)
@SpringBootTest
public class IHytempTest {
	@Autowired
	private hygrothermographService hyService;
	@Autowired
	private HygrothermographWarnService hyWwarnService;
	

    @Test
    public void testHyService(){
    	timeUtiles ti = new timeUtiles();
    	String currentTime = ti.getCurrenttime();
    	int temp_hum_savetim = ParameterField.tempHumTime;
    	String startime;
		try {
			
			startime = ti.timeReduce(currentTime, temp_hum_savetim);
			String mac = "8002201004A1";
	    	List<Map> queryhyDatas =hyService.queryHyByTime(mac,startime,currentTime);
	    	LogUtil.logger.info(queryhyDatas+"==queryhydatas");
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    @Test
    public void testHy1(){
    	String mac = "AC233FA57E2D";
			Calendar cal1=Calendar.getInstance();
			cal1.add(Calendar.DATE,-6);
			Date time1=cal1.getTime();
			String startime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(time1);
			
			timeUtiles time = new timeUtiles();
			String currenttime = time.getCurrenttime();
			
			List<Hygrothermograph> mapDatas = hyService.queryhy(mac,startime,currenttime);	
			
//			???10??????????????????
			Map map_new_key = new HashMap<>();
			for (Hygrothermograph hg : mapDatas) {
				String time_current = hg.getCurrentTime();
				StringBuilder sb = new StringBuilder();
//				??????????????????key
				String min_time_key = sb.append(time_current).replace(time_current.length()-4,time_current.length(),"0:00").toString();
				hg.setCurrentTime(min_time_key);//????????????
				map_new_key.put(min_time_key, hg);
			}
			Iterator<Map.Entry<Object, Object>> iterator = map_new_key.entrySet().iterator();
	        List ls = new ArrayList<>();
	        while (iterator.hasNext()) {
	             Map.Entry<Object, Object> entry = iterator.next();
				 ls.add(entry.getValue());
	        }
			
			JSONArray array= JSONArray.parseArray(JSON.toJSONString(ls));
			System.out.println(array);
//			??????????????????????????????????????????
    	
    }
    
    public Map getToporMinHum(String mac,String get0Time,String currenttime) {
		 
		 List<Hygrothermograph> mapDatas = hyService.queryhy(mac, get0Time, currenttime);	
			
		 Map map = new HashMap();
//		 ?????????????????????????????????,?????????????????????
		 if(mapDatas.size()>0) {
//			 ??????????????????????????????????????????????????????
		        Collections.sort(mapDatas, new Comparator<Hygrothermograph>() {
		            @Override
		            public int compare(Hygrothermograph o1, Hygrothermograph o2) {
		            	
		            	if(null == o1.getHumidity() || null == o2.getHumidity()) {
		            		return 1;
		            	}
		            	double d1 = Double.parseDouble(o1.getHumidity());
		            	double d2 = Double.parseDouble(o2.getHumidity());
		            	String result1 = String.format("%.2f",d1);
		            	String result2 = String.format("%.2f",d2);
		            	double f1 = Double.parseDouble(result1);
		            	double f2 = Double.parseDouble(result2);
		                return f1>f2? -1:(f1==f2? 0:1);
		            }
		        });
		        for (int j = 0; j < mapDatas.size(); j++) {
//		        	???????????????
					if(j == 0) {
						map.put("tophumidity", mapDatas.get(0).getHumidity());
						
					}
//					???????????????
					if(j == mapDatas.size()-1) {
						map.put("minhumidity",mapDatas.get(mapDatas.size()-1).getHumidity());
					}
					
				}
		        Collections.sort(mapDatas, new Comparator<Hygrothermograph>() {
		            @Override
		            public int compare(Hygrothermograph o1, Hygrothermograph o2) {
		            	if(null == o1.getTemperature() || null == o2.getTemperature()) {
		            		return 1;
		            	}
		            	double d1 = Double.parseDouble(o1.getTemperature());
		            	double d2 = Double.parseDouble(o2.getTemperature());
		            	String result1 = String.format("%.2f",d1);
		            	String result2 = String.format("%.2f",d2);
		            	double f1 = Double.parseDouble(result1);
		            	double f2 = Double.parseDouble(result2);
		                return f1>f2? -1:(f1==f2? 0:1);
		            }
		        });
		        for (int k = 0; k < mapDatas.size(); k++) {
//		        	???????????????
					if(k == 0) {
						map.put("toptemperature",mapDatas.get(0).getTemperature());
						
					}
//					???????????????
					if(k == mapDatas.size()-1) {
						map.put("mintemperature", mapDatas.get(mapDatas.size()-1).getTemperature());
					}
				}
		        map.put("mac", mac);
		        map.put("date", get0Time);
		 }
		 return map;
	 }
    	
    	@Test
    	public void testHy(){
    		Humiture humit = new Humiture();
    		HygrothermographWarn hw = new HygrothermographWarn();
    		hw.setCurrentTime("2020-01-14 11:17:29");
    		hw.setElectric("89");
    		hw.setGatewaymac("AC233FC08519");
    		hw.setHumidity("20");
    		hw.setMac("AC233FA018E7");
    		hw.setTemperature("11");
    		humit.setAddress(UserParameterField.address);
    		List<Humiture> queryhum = hyService.queryhum(humit);
    	queryhum.parallelStream().filter(a -> "AC233FA018E7".equals(a.getMac())).map(qh->{
			String temperaturefitted = qh.getTemperaturefitted();
			String humidityfitted = qh.getHumidityfitted();
//			??????????????????
			String s1 = "";
	    	String s2 = "";
	    	double w1 = 0;
	    	double w2 = 0;
			if(!StringUtils.isEmpty(temperaturefitted)) {
				String[] split = temperaturefitted.split("~");
		    	
//					??????????????????
		    	for(String d : split){
		    		if(StringUtils.isEmpty(s1)) {
		    			s1 = d; 
		    		}else{
		    			s2 = d;
		    		}
		    	}
		    	 w1 = Double.parseDouble(s1);
		    	 w2 = Double.parseDouble(s2);
		    	if(w1>w2) {
		    		w1 = Double.parseDouble(s1);
		    		w2 = Double.parseDouble(s2);
		    	}else {
		    		w1 = Double.parseDouble(s2);
		    		w2 = Double.parseDouble(s1);
		    	}
			}
//			??????????????????
			String s3 = "";
			String s4 = "";
			double w3 = 0;
			double w4 = 0;
			if(!StringUtils.isEmpty(humidityfitted)) {
				String[] split = humidityfitted.split("~");
		    	
//				??????????????????
		    	for(String d : split){
		    		if(StringUtils.isEmpty(s3)) {
		    			s3 = d; 
		    		}else{
		    			s4 = d;
		    		}
		    	}
		    	 w3 = Double.parseDouble(s3);
		    	 w4 = Double.parseDouble(s4);
		    	if(w3>w4) {
		    		w3 = Double.parseDouble(s3);
		    		w4 = Double.parseDouble(s4);
		    	}else {
		    		w3 = Double.parseDouble(s4);
		    		w4 = Double.parseDouble(s3);
		    	}
	    	
			}
//			?????????????????????
			double temp_value = Double.parseDouble("11");
			double hum_value = Double.parseDouble("20");
//			??????/??????/??????/????????????
			if(temp_value>w1 || temp_value<w2 || hum_value>w3 || hum_value<w4) {
				hyWwarnService.insertHyWarn(hw);
			}
			return 1;
		}).collect(Collectors.toList());
		
    }
    @Test
    public void testHumupdate(){
    	HygrothermographWarn hy = new HygrothermographWarn();
    	hy.setTemperature("23.7");
    	hy.setHumidity("34.2");
    	hy.setMac("C00220000DCC");
    	hy.setCurrentTime("2020-12-24 19:20:00");
    	hy.setWarnnum("13");
    	

    	List<Map> queryHwByTime = hyWwarnService.queryHwByTime(hy);
		queryHwByTime.parallelStream().map(a -> {
    		Long i = (Long)a.get("count(*)");
    		if(i==0) {
    			hyWwarnService.insertHyWarn(hy);
			}
    		return 1;
    	}).collect(Collectors.toList());
		
		
    }
//   ?????????????????????????????????????????????
    private static String getHexTemp(String t){
		Double f = Double.parseDouble(t);
		Double d;
		String HexString = "";
		
		if(f<0) {
			d = f+6553.5;
			int temp_hex = (int)(d*10);
			HexString = temp_hex+"";
		}else if(f>0) {
			int temp_hex = (int)(f*10);
			HexString = temp_hex+"";
		}
		return HexString;
	}
//    ?????????????????????s1?????????????????????gs1???????????????????????????????????????
    @Test
    public void testHyTime(){
    	
    	
    	timeUtiles ti = new timeUtiles();
    	String startime = "";
    	String currentTime = ti.getCurrenttime();
    	currentTime = currentTime.substring(0, currentTime.length()-2);
    	currentTime = currentTime+"00";
    	try {
    		startime = ti.timeReduce(currentTime, 10*60);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
//    	????????????????????????????????????????????????
    	List<Map> warnDatas = hyService.getWarnDatas(startime, currentTime);
    
    	
		if(null == warnDatas) {
			return;
		}
		
    	List<Map> ls_warn = new ArrayList<>();
    	for (Map map : warnDatas) {
    		
    		String freezernumber = "";
        	String freezername = "";
        	String temperaturefitted = "";
        	String humidityfitted = "";
        	String temperature = "";
        	String humidity = "";
    		
    		freezernumber = map.get("freezernumber").toString();
    		freezername = map.get("freezername").toString();
    		Map map_warn = new HashMap();
//    		????????????,?????????????????????
    		if(null !=map.get("temperaturefitted") && !StringUtils.isEmpty(map.get("temperaturefitted").toString())
    				&& null != map.get("temperature") && !StringUtils.isEmpty(map.get("temperature").toString())) {
    			temperaturefitted = map.get("temperaturefitted").toString();
    			temperature = map.get("temperature").toString();
    			
    			Boolean tp = get(temperaturefitted,temperature);
//    			??????????????????
        		if(!tp) {
        			map_warn.put("temperature", temperature);
        		}
    		}
//    		????????????,?????????????????????
    		if(null !=map.get("humidityfitted") && !StringUtils.isEmpty(map.get("humidityfitted").toString())
    				&& null != map.get("humidity") && !StringUtils.isEmpty(map.get("humidity").toString())) {
    			humidityfitted = map.get("humidityfitted").toString();
    			humidity = map.get("humidity").toString();
    			
    			Boolean hum = get(humidityfitted,humidity);
//    			??????????????????
    			if(!hum) {
    				map_warn.put("humidity", humidity);
    			}
    		}
//    		??????????????????
    		if(map_warn.size()>0) {
    			
    			String id = "";
    			Integer level = 0;
    			String name = "";
    			String desc = "";
    			
//    			??????????????????(????????????????????????????????????)
    			if(null != map_warn.get("temperature") && !StringUtils.isEmpty(map_warn.get("temperature").toString())) {
    				Boolean warnType = getWarnType(temperaturefitted,temperature);
    				if(warnType == true) {
    					id = "gw";
    					name = "??????";
    					desc="??????????????????";
    				}else {
    					id = "dw";
    					name = "??????";
    					desc="??????????????????";
    				}
    			}
//    			???????????????????????????
    			if(null != map_warn.get("humidity") && !StringUtils.isEmpty(map_warn.get("humidity").toString())
    					&& !StringUtils.isEmpty(desc)) {
    				Boolean warnType = getWarnType(humidityfitted,humidity);
    				if(warnType == true) {
    					id += "gs";
    					name += "??????";
    					desc += "??????????????????";
    				}else {
    					id += "ds";
    					name +="??????";
    					desc +="??????????????????";
    				}
    			}
    			map_warn.put("id", id);
//    			?????????????????????gw,dw,gs,ds,ld,dd
    			map_warn.put("equipID", map.get("mac").toString());
//    			????????????1-4???1?????????2?????????3????????????4????????????5????????????6?????????,7????????????
    			if(id.equals("gw")||id.equals("dw") || id.equals("gs")||id.equals("ds")
    					|| id.equals("gwgs") || id.equals("dwgs")||id.equals("gwds")||id.equals("dwds")) {
    				level=1;
    			}else if(id.equals("ld")||id.equals("dd")){
    				level=2;
    			}else{
    				level=3;
    			}
    			map_warn.put("level", level);
//    			????????????
    			map_warn.put("name", name);
//    			????????????
    			map_warn.put("desc", desc);
//    			??????????????????????????????
    			map_warn.put("type", "??????");
//    			????????????(????????????)
    			map_warn.put("state", "?????????");
//    			????????????
    			map_warn.put("date", map.get("currentTime").toString());
    			
    			ls_warn.add(map_warn);
    		}
    		
		}
    	LogUtil.logger.info(ls_warn.toString());
    }
    
    public Boolean get(String s,String temp_str) {
		String[] split = s.split("~");
		String s1 = "";
		String s2 = "";
		double temp = Double.parseDouble(temp_str);
		
//		??????????????????
        for(String d : split){
            if(StringUtils.isEmpty(s1)) {
            	s1 = d; 
            }else{
            	s2 = d;
            }
        }
        double w1 = Double.parseDouble(s1);
        double w2 = Double.parseDouble(s2);
        Boolean b = (temp>=w1 && temp<=w2);
        return b;
    }
//  true ???????????????,false ???????????????
    public Boolean getWarnType(String s,String temp_str) {
    	String[] split = s.split("~");
    	String s1 = "";
    	String s2 = "";
    	double temp = Double.parseDouble(temp_str);
    	
//		??????????????????
    	for(String d : split){
    		if(StringUtils.isEmpty(s1)) {
    			s1 = d; 
    		}else{
    			s2 = d;
    		}
    	}
    	double w1 = Double.parseDouble(s1);
    	double w2 = Double.parseDouble(s2);
    	Boolean b = (temp>w2);
    	return b;
    }
    
    @Test
    public void testHyDatas(){

//		??????		 
		String h = "7EC00220000DCC02"
				+ "001E"
				+ "00ED01B2140B120B3600"
				+ "00ED01B0140B120B3700"
				+ "00ED01A6140B120B3800"
				+ "0D";
		
		String datas = "7EC00220000DCC0D0009000170140B13092A340D";
		String equipment = datas.substring(6, 14);
//		 ??????
		 
//		 ????????????????????????
		 String Type = datas.substring(2, 6);
		 String binary = HexConvert.hexStringToByte(Type);
//		 16???????????????
		 String funcCode = datas.substring(14,16);
		 
		 Hygrothermograph hy = new Hygrothermograph();
		 ComponentUtils comp = new ComponentUtils();
		 
		 
		 
		 
		 
		 String h_len = datas.substring(16,20);
		 Integer h_datas = HexConvert.hexString2Deci(h_len);
		 System.out.println(h_datas);
		 String h_lu = datas.substring(20,h_datas*2+20);
		 System.out.println(h_lu);
//		 ??????????????????10??????
//		 00ED 01B2 140B 120B 3600 
//		 00ED 01B0 140B 120B 3700 
//		 00ED 01A6 140B 120B 3800
		 
//		???????????? 
		for (int j = 0; j < h_datas/10; j++) {
			
			String temp = "";
			String hum = "";
			String y = "";
			String m = "";
			String d = "";
			String hour = "";
			String mi = "";
			String s = "";
			
	        for (int i = 10*2*j; i < 10*2*(j+1); i++) {
				temp = h_lu.substring(10*2*j,10*2*j+4);
				hum = h_lu.substring(10*2*j+4,10*2*j+8);
				y = h_lu.substring(10*2*j+8,10*2*j+10);
				m = h_lu.substring(10*2*j+10,10*2*j+12);
				d = h_lu.substring(10*2*j+12,10*2*j+14);
				hour = h_lu.substring(10*2*j+14,10*2*j+16);
				mi = h_lu.substring(10*2*j+16,10*2*j+18);
				s = h_lu.substring(10*2*j+18,10*2*j+20);
	        }
	        
	        System.out.println(temp);
	        System.out.println(hum);
	        System.out.println(y);
	        System.out.println(m);
	        System.out.println(d);
	        System.out.println(hour);
	        System.out.println(mi);
	        System.out.println(s);
	        
	        
//	        ????????????
	        Integer year = HexConvert.hexString2Deci(y);
	        Integer month = HexConvert.hexString2Deci(m);
	        Integer day = HexConvert.hexString2Deci(d);
	        Integer hours = HexConvert.hexString2Deci(hour);
	        Integer minute = HexConvert.hexString2Deci(mi);
	        Integer second = HexConvert.hexString2Deci(s);
	        
	        List<String> ls = new ArrayList<>();
	        ls.add(year.toString()+"-");
	        ls.add(month.toString()+"-");
	        ls.add(day.toString()+" ");
	        ls.add(hours.toString()+":");
	        ls.add(minute.toString()+":");
	        ls.add(second.toString());
	        String timeJoint = timeJoint(ls);
	        System.out.println(timeJoint);
	        
//	        ????????????,??????????????????????????????,???????????????????????????????????????????????????????????????
	        
//			???????????????????????????
		updateTempHum(Type, equipment, funcCode, hy,
 		    		  temp, hum, comp,timeJoint);
		}
    }
    
    @Test
    public void testHyTempAndEleDatas(){

//		??????		 
		String datas = "7EC00220000DCC02"
				+ "0018"
				+ "00ED140B140B3600"
				+ "00ED140B140B3700"
				+ "00ED140B140B3800"
				+ "0D";
		
//		String datas = "7EC00220000DCC0D0009000170140B13092A340D";
		String equipment = datas.substring(6, 14);
//		 ??????
		 
//		 ????????????????????????
		 String Type = datas.substring(2, 6);
		 String binary = HexConvert.hexStringToByte(Type);
//		 16???????????????
		 String funcCode = datas.substring(14,16);
		 
		 Hygrothermograph hy = new Hygrothermograph();
		 ComponentUtils comp = new ComponentUtils();
		 
		 
		 
		 
		 
		 String h_len = datas.substring(16,20);
		 Integer h_datas = HexConvert.hexString2Deci(h_len);
		 System.out.println(h_datas);
		 String h_lu = datas.substring(20,h_datas*2+20);
		 System.out.println(h_lu);
//		 ??????????????????10??????
//		 00ED 01B2 140B 120B 3600 
//		 00ED 01B0 140B 120B 3700 
//		 00ED 01A6 140B 120B 3800
		 
//		 ???????????????8??????
//		 00ED 140B 120B 3600 
//		 00ED 140B 120B 3700 
//		 00ED 140B 120B 3800
		 
		 
//		??????????????????????????? 
		for (int j = 0; j < h_datas/10; j++) {
			
//			???????????????
			String temp = "";
			String hum = "";
			String y = "";
			String m = "";
			String d = "";
			String hour = "";
			String mi = "";
			String s = "";
			
			
	        for (int i = 10*2*j; i < 10*2*(j+1); i++) {
//				???????????????
				temp = h_lu.substring(10*2*j,10*2*j+4);
				hum = h_lu.substring(10*2*j+4,10*2*j+8);
				y = h_lu.substring(10*2*j+8,10*2*j+10);
				m = h_lu.substring(10*2*j+10,10*2*j+12);
				d = h_lu.substring(10*2*j+12,10*2*j+14);
				hour = h_lu.substring(10*2*j+14,10*2*j+16);
				mi = h_lu.substring(10*2*j+16,10*2*j+18);
				s = h_lu.substring(10*2*j+18,10*2*j+20);
	        }
	        
	        
//	        ????????????
	        Integer year = HexConvert.hexString2Deci(y);
	        Integer month = HexConvert.hexString2Deci(m);
	        Integer day = HexConvert.hexString2Deci(d);
	        Integer hours = HexConvert.hexString2Deci(hour);
	        Integer minute = HexConvert.hexString2Deci(mi);
	        Integer second = HexConvert.hexString2Deci(s);
	        
	        List<String> ls = new ArrayList<>();
	        ls.add(year.toString()+"-");
	        ls.add(month.toString()+"-");
	        ls.add(day.toString()+" ");
	        ls.add(hours.toString()+":");
	        ls.add(minute.toString()+":");
	        ls.add(second.toString());
	        String timeJoint = timeJoint(ls);
	        System.out.println(timeJoint);
//			???????????????????????????
		updateTempHum(Type, equipment, funcCode, hy,
 		    		  temp, hum, comp,timeJoint);
		}
		 
		 
//		????????????
		for (int j = 0; j < h_datas/8; j++) {
			
			
//			????????????
			String temp = "";
			String hum = "";
			String y = "";
			String m = "";
			String d = "";
			String hour = "";
			String mi = "";
			String s = "";
			
			for (int i = 8*2*j; i < 8*2*(j+1); i++) {
				temp = h_lu.substring(8*2*j,8*2*j+4);
				y = h_lu.substring(8*2*j+4,8*2*j+6);
				m = h_lu.substring(8*2*j+6,8*2*j+8);
				d = h_lu.substring(8*2*j+8,8*2*j+10);
				hour = h_lu.substring(8*2*j+10,8*2*j+12);
				mi = h_lu.substring(8*2*j+12,8*2*j+14);
				s = h_lu.substring(8*2*j+14,8*2*j+16);
			}
			
			
//	        ????????????
			Integer year = HexConvert.hexString2Deci(y);
			Integer month = HexConvert.hexString2Deci(m);
			Integer day = HexConvert.hexString2Deci(d);
			Integer hours = HexConvert.hexString2Deci(hour);
			Integer minute = HexConvert.hexString2Deci(mi);
			Integer second = HexConvert.hexString2Deci(s);
			
			List<String> ls = new ArrayList<>();
			ls.add(year.toString()+"-");
			ls.add(month.toString()+"-");
			ls.add(day.toString()+" ");
			ls.add(hours.toString()+":");
			ls.add(minute.toString()+":");
			ls.add(second.toString());
			String timeJoint = timeJoint(ls);
			System.out.println(timeJoint);
			
//	        ????????????,??????????????????????????????,???????????????????????????????????????????????????????????????
			
//			???????????????????????????
			updateTempHum(Type, equipment, funcCode, hy,
					temp, hum, comp,timeJoint);
		}
    }
    
//		?????????????????????????????????
	public String timeJoint(List<String> l) {
		String date = "20";
		 for (String str_date : l) {
			//	 ?????????????????????
			if(str_date.length() == 3) {
				date += str_date;
			}else if (str_date.length() == 2){
				date +="0"+str_date;
			}else if (str_date.length() == 1) {
				date += "00";
			}
		}
		return date;
	}
	public void updateTempHum(String Type,String equipment,String funcCode,Hygrothermograph hy,
		   		 String temp,String hum,ComponentUtils comp,String endtime) {
//			??????mac
			hy.setMac(Type+equipment); 
//			??????????????????
			hy.setCurrentTime(endtime);

			
		      	 if(funcCode.equals("0B") || funcCode.equals("01")) {
					 
//					 LogUtil.logger.info(hy.toString()+"======????????????");
			   		 comp.addData(hy);
		   		 
		      	 }else if(funcCode.equals("0C") || funcCode.equals("02")) {
//		      		 ??????????????????
		      		 
//		 			??????????????????
		 			hy.setElectric("100");
		      		 
		      		if(!StringUtils.isEmpty(temp)) {
			   			 
			   			String temperature = HexConvert.hexStringToDeci(temp)+"";
	//		   			?????????????????????
			   			String temp2 = getTemp(temperature);
						hy.setTemperature(temp2);
			   		 }
	//				 ????????????
					 if(!StringUtils.isEmpty(hum)) {
						 String humidity =  HexConvert.hexStringToDeci(hum)+"";
						 hy.setHumidity(humidity);
					 }
			   		 comp.addDataByTime(hy,endtime);
		      		 
		      	 }else if(funcCode.equals("0D") || funcCode.equals("03")) {
//		      		 ??????????????????
		      	 }
		}
	
	public  String getTemp(String t){
	   	 Double d = Double.parseDouble(t);
	   	 Double f;
	   	 String temp = "";
	   	 
	   	 if(3276.8<=d && d<=6553.5) {
	   		 f = (d-6553.5);
	   		 temp = String.format("%.1f", f);
	   	 }else if(d>=0.0 && d<=3276.7) {
	   		 temp = String.format("%.1f", d);
	   	 }
	   	 return temp;
	    }
	    
		public static String getDate() {
			//		 ????????????
				 String string = new SimpleDateFormat().format(new Date()).toString();
				 Calendar calendar = Calendar.getInstance(); 
				 String date_year = calendar.get(Calendar.YEAR)+"";
				 String year =  date_year.substring(2,4);
				 String month = calendar.get(Calendar.MONTH)+1+"";
				 String day = calendar.get(Calendar.DATE)+"";
				 String hour = calendar.get(Calendar.HOUR_OF_DAY)+ "";  //24?????????
				 String minute = calendar.get(Calendar.MINUTE)+"";
				 String sencond = calendar.get(Calendar.SECOND)+"";
				 
//				 LogUtil.logger.info(minute+"=minute"+calendar.get(Calendar.MINUTE)+1);
				 
				 List<String> ls=new ArrayList<>();
				 ls.add(year);
				 ls.add(month);
				 ls.add(day);
				 ls.add(hour);
				 ls.add(minute);
				 ls.add(sencond);
				 List<String> l = new ArrayList<>();
				 for (int i = 0; i < ls.size(); i++) {
					 int data = Integer.parseInt(ls.get(i));
					 String int2Hexstring = HexConvert.int2Hexstring(data);
					 l.add(int2Hexstring);
				}
				 String date = ""; 
				 for (String str_date : l) {
			//	 ?????????????????????
					if(str_date.length() == 2) {
						date += str_date;
					}else if (str_date.length() == 1){
						date +="0"+str_date;
					}else if (str_date.length() == 0) {
						date += "00";
					}
				}
				 
				 return date;
		     }
		
}
*/
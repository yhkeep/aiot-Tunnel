package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.util.ComponentUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.sockettherd.HexConvert;
public class JsonTest {
	
	
	public static void main(String[] args) throws ParseException {
		StringBuilder sb = new StringBuilder();
		String time_current = "2021-09-17 15:16:26";
		String a = sb.append(time_current).replace(time_current.length()-4,time_current.length(),"0:00").toString();
		
		System.out.println(a);
	}
	
	
	
	private static List<String> getListByStr(String str){
		List<String> list = Arrays.asList(str.split(","));
		List<String> newlist = new ArrayList<String>(list);
		return newlist;
	}
	
	public static String timeJoint1(List<String> l) {
		String date = "20";
		 for (String str_date : l) {
			//	 时间段拼接处理
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
	/*private static String getHexTemp(String t){
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
	}*/
	
	
			
	/*public static void main(String[] args) {
		  LinkedHashMap<String, Object> smap = new LinkedHashMap<>();
        smap.put("5", 5);
        smap.put("6","6");
        smap.put("7","7");
        
        Map map = new HashMap();
        map.put("a", "b");
        map.put("3", 2);
        map.put("ab", "\\");
        System.out.println(JSONObject.toJSONString(map));
		
		int reNewRssi = -20;
		reNewRssi = reNewRssi>=-35?reNewRssi=-45:reNewRssi;
		System.out.println(reNewRssi);
		
		String macReg = "AC23C202b203";
		StringBuilder sb = new StringBuilder();
		if(macReg.length()>=8 && !macReg.equals("default")) {
			
			for (int i = 0; i < macReg.length(); i++) {
				if(i%4==0 && i>0) {
					System.out.println(macReg.charAt(i)+"i="+i);
					sb.append( ".*\\|^");
				}
				sb.append(macReg.charAt(i));
				
			}
			
		}
		
		System.out.println("^"+sb+".*");
	}*/

//	设备只支持当前世纪时间
	public static String timeJoint(List<String> l) {
		String date = "20";
		 for (String str_date : l) {
			//	 时间段拼接处理
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
	private static void updateTempHum(String Type,String equipment,String funcCode,Hygrothermograph hy,
   		 String temp,String hum,ComponentUtils comp,String endtime) {
      	 if(funcCode.equals("0B") || funcCode.equals("01")) {
//	 		更新温度
   		 if(!StringUtils.isEmpty(temp)) {
   			 
   			String temperature = HexConvert.hexStringToDeci(temp)+"";
//   			处理零下摄氏度
   			String temp2 = getTemp(temperature);
			hy.setTemperature(temp2);
   		 }
//			 更新湿度
			 if(!StringUtils.isEmpty(hum)) {
				 String humidity =  HexConvert.hexStringToDeci(hum)+"";
				 hy.setHumidity(humidity);
			 }
			 
		 LogUtil.logger.info(hy.toString());
   		 comp.addDataByTime(hy,endtime);
   		 
      	 }else if(funcCode.equals("0C") || funcCode.equals("02")) {
//      		 获取历史记录
//      		 两种仪器设备存在差异
      		 
//      				2020-11-18 11:56:18 [INFO] C002=type=20000DCC=equipment=0C
//      				2020-11-18 11:56:24 [INFO] 7E
//      		
      		 
      	 }else if(funcCode.equals("0D") || funcCode.equals("03")) {
//      		 获取告警数据
      	 }
    }
	
	private static String getTemp(String t){
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
	
    
	public static String getDate() {
		//		 时间设置
			 String string = new SimpleDateFormat().format(new Date()).toString();
			 Calendar calendar = Calendar.getInstance(); 
			 String date_year = calendar.get(Calendar.YEAR)+"";
			 String year =  date_year.substring(2,4);
			 String month = calendar.get(Calendar.MONTH)+1+"";
			 String day = calendar.get(Calendar.DATE)+"";
			 String hour = calendar.get(Calendar.HOUR_OF_DAY)+ "";  //24小时制
			 String minute = calendar.get(Calendar.MINUTE)+"";
			 String sencond = calendar.get(Calendar.SECOND)+"";
			 
//			 LogUtil.logger.info(minute+"=minute"+calendar.get(Calendar.MINUTE)+1);
			 
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
		//	 时间段拼接处理
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


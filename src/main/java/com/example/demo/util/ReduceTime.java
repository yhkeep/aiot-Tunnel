package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.demo.util.parameter.UserParameterField;

public class ReduceTime {
	
//当前时间
  public static String localTime() {
	    	Date date = new Date();// 转换为标准时间对象
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
	        String date1 = sdf.format(date);
	        return date1;
	    }
//  单位分钟内
  public static String timeReduceTwoMinute(String endTime) throws ParseException {
  	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  	Date date =df.parse(endTime);
//		date.setTime(date.getTime() + 1000); 加1秒
//  	date.setTime(date.getTime() - 2*60*1000);  //减少2分钟 
  	date.setTime(date.getTime() - UserParameterField.equipmentDynamic*1000);  //减少30秒
  	String startTime = df.format(date);
  	return startTime;
  }
//  实时数据发送时间
  public static String timeReduceMinute(String endTime) throws ParseException {
	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  Date date =df.parse(endTime);
	  date.setTime(date.getTime() - UserParameterField.RealtimeMapPush*1000);  //减少60秒
	  String startTime = df.format(date);
	  return startTime;
  }
}

package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class timeUtiles {

//  时间转换
  public String UTC2local(String time) {
  	//此方法是将2017-11-18T07:12:06.615Z格式的时间转化为秒为单位的Long类型。
  	time = time.replace("Z", " UTC");//UTC是本地时间
  	SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
  	Date d = null;
  	try {
  		d = format.parse(time);
  	} catch (ParseException e) {
  	// TODO Auto-generated catch block
  	e.printStackTrace();
  	}
  	//此处是将date类型装换为字符串类型，比如：Sat Nov 18 15:12:06 CST 2017转换为2017-11-18 15:12:06
  	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  	String date = sf.format(d);
  	Long time1 = d.getTime()/1000;//这里输出的是以秒为单位的long类型的时间。如果需要一毫秒为单位，可以不用除1000.
  	String t = sf.format(d);//这里输出的是字符串类型的时间
  	return t;
  }
//时间向后移
  public String timeReduce(String endTime,int second) throws ParseException {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date =df.parse(endTime);
	date.setTime(date.getTime() - second*1000);  //减少60秒
//	date.setTime(date.getTime() - 7*24*60*60*1000);  //减少7天
	String startTime = df.format(date);
	return startTime;
  }
//  时间向前移
  public String timeAdd(String endTime,int second) throws ParseException {
	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  Date date =df.parse(endTime);
	  date.setTime(date.getTime() + second*1000);  
	  String startTime = df.format(date);
	  return startTime;
  }
//  获取当前标准时间
  public String getCurrenttime() {	
	  Date date = new Date();// 转换为标准时间对象
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
      String date1 = sdf.format(date);
	  return date1+"";
  }
//  获取0点时间点
  public String get0Time() {
	  java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      Calendar c = Calendar.getInstance();
      c.set(Calendar.HOUR_OF_DAY, 0); 
      c.set(Calendar.MINUTE, 0); 
      c.set(Calendar.SECOND, 0); 
      Date d = c.getTime();
      String time = f.format(d)+"";
      return time;
  }
  
//字符串比较大小
  public int compareDateByString(String d1,String d2){
	  int compareTo = d1.compareTo(d2);
	  return compareTo;
	//d1.compareTo(d2)jdk用那个毫秒数来比较 如下；
      /*if (dt1.getTime() > dt2.getTime()) {
          System.out.println("dt1 在dt2前");
          return 1;
      } else if (dt1.getTime() < dt2.getTime()) {
          System.out.println("dt1在dt2后");
          return -1;
      } else {//相等
          return 0;
      }*/
  }
  
//  截取字符串后两位并替换
  public String timePeak(String time) {
	 String timeSub = time.substring(time.length()-2);
	 Random random = new Random();
	 int l = random.nextInt(Integer.parseInt(timeSub)+1);//包含60，去掉0异常随机数
	 String timePeak = l<10?"0"+l:l+"";
	 String replace = StringUtils.replace(time,timeSub,timePeak);
	 return replace;
  }
//  获取当前毫秒数
  public String timeMeter() {
	  SimpleDateFormat sdf  = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒SSS毫秒") ;
      return sdf.format(new Date());// 将当前日期进行格式化操作

  }
//  补全拼接时间字符串
  public String timeJoint(List<String> l) {
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
  
//  告警值判断
  public Boolean getWarnBoolean(String s,String temp_str) {
		String[] split = s.split("~");
		String s1 = "";
		String s2 = "";
		double temp = Double.parseDouble(temp_str);
		
//		阀值必须完整
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
//  true 高温或高湿,false 低温或低湿
  public Boolean getWarnType(String s,String temp_str) {
  	String[] split = s.split("~");
  	String s1 = "";
  	String s2 = "";
  	double temp = Double.parseDouble(temp_str);
  	
//		阀值必须完整
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
}

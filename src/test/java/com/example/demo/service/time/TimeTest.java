/*package com.example.demo.service.time;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.example.demo.util.timeUtiles;

*//**
 * @author yanghan
 * @date 2019/7/30上午11:04
 *//*
public class TimeTest {
    
	@Before
    public void before() {
    	
    }
	 @Test
	 public void test() {
		 String name = "2020-06-10 11:36:01";
		 String timeSub = name.substring(name.length()-2);
		 Random random = new Random();
		 int l = random.nextInt(Integer.parseInt(timeSub)+1);
		 String timePeak = l<10?"0"+l:l+"";
		 String replace = StringUtils.replace(name,timeSub,timePeak);
		 System.out.println(replace);
	 }
	 @Test
	 public void testDuce() {
		 String time1= "2020-06-11 10:44:04";
		 String time2 = "2020-06-11 10:44:15";
		 int res = new timeUtiles().compareDateByString(time1,time2);
		 System.out.println(res);
		 if(res>0)
	            System.out.println("str1>str2");
	        else if(res==0)
	            System.out.println("str1=str2");
	        else
	            System.out.println("str1<str2");

		 
	 	}
	 @Test
	 public void testV() {
		 BigDecimal number = new BigDecimal(10);
			int value=200000000;
			number=BigDecimal.valueOf((int)value);
			System.out.println(number);
		 
	 }
	 
	 
	
}*/
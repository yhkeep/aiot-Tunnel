package com.example.demo.util.sockettherd;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.example.demo.domain.Hygrothermograph;
import com.example.demo.domain.HygrothermographWarn;
import com.example.demo.util.ComponentUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;

public class SocketServerTherd extends Thread{
	
	
	 Socket socket = null;
     
	 
     public SocketServerTherd(Socket socket){
         this.socket = socket;
     }
//     服务线程接收消息
     public void run(){
    	 
    	 InputStream inputStream = null;
    	 
    	 try {
    		 boolean connected = socket.isConnected();//是否处于连接状态
    		 
    		 boolean closed = socket.isClosed(); //socket是否关闭
    		 
    		 boolean bound = socket.isBound(); //
    		 
    		 /*
    		  * TODO0309
    		 if(!connected && !closed) {
        		 return ;
        	 }*/
    		 
    		 
    		 
    		 if(connected && !closed && bound) {
    		 }else {
    			 return ;
    		 }
    		 
    		 inputStream = socket.getInputStream();
    		 BufferedInputStream r = new BufferedInputStream(inputStream);  
        	 
        	 
		    // 每次接收的数据
		    byte[] data = new byte[1024];
		    int len = r.read(data);
		    
		    
//		    仪表传递过来数据类型为字节数组，并且不能为负长度，（long型转为int可能为负）
		    if(len<0) {
		    	return ;
		    }
		    
		    String byteArrayToHexString;
       	 	StringBuilder stringBuilder = new StringBuilder();
		    byteArrayToHexString = HexConvert.byteArrayToHexString(Arrays.copyOf(data, len));
		    stringBuilder.append(byteArrayToHexString);
		    
//		    LogUtil.logger.info(stringBuilder+"=stringbuilder");
//		    数据来时立即回复,否则无效连接
		    if(null != stringBuilder) {
		        pare2Byte(stringBuilder);
		    }
		    
		    
		} catch (Exception e) {
			// TODO: handle exception
//			LogutilExcetion.logger.error("异常错误："+LogutilExcetion.getExceptionInfo(e));
		}
           
     }

//     解析数据
     public void pare2Byte(StringBuilder stringBuilder) {
//		数据添加
		 ComponentUtils comp = new ComponentUtils();
    	 
    	 
    	 String datas = stringBuilder.toString(); 
//    	 起始结束判断
    	 if(datas.endsWith("0D") && datas.startsWith("7E")) {
    		 
// 			仪表型号（无，添加）温湿度值、对应状态	 持久化到数据库 
    		 Hygrothermograph hy = new Hygrothermograph();
    		 
 			 timeUtiles time_ = new timeUtiles();
 			 
// 			 一般接收时间
 			 hy.setCurrentTime(time_.getCurrenttime());
//    		 仪器型号
    		 String equipment = datas.substring(6, 14);
			 
//    		 通过型号判断类型
    		 String Type = datas.substring(2, 6);
    		 
    		 String binary = HexConvert.hexStringToByte(Type);
    		 
//			 16进制功能码
			 String funcCode = datas.substring(14,16);
			 
//			 设备编号
			 hy.setMac(Type+equipment);
			
			 
//			 LogUtil.logger.info(Type+"=type="+equipment+"=equipment="+funcCode);
//			 实时数据
//			 温湿度仪器
    		 if("1".equals(binary.substring(1,2)) && datas.length()==36 && (funcCode.equals("0B") || funcCode.equals("01"))) {
    			 
    			 String temp = "";
        		 String hum = "";
        		 String elec = "";
//    			 实时温湿度数据区
    			 temp = datas.substring(20,24);
    			 hum = datas.substring(24,28);
    			 elec = datas.substring(28,30);
    			 
    			 updateTempHum( Type, equipment, funcCode, hy,
    		    		  temp, elec, hum, comp);
    			 
//			温度仪器
    		 }else if (!"1".equals(binary.substring(1,2)) && datas.length()==32 && (funcCode.equals("0B") || funcCode.equals("01"))) {
    			 String temp = "";
        		 String hum = "";
        		 String elec = "";
        		 
//    			 温度
    			 temp = datas.substring(20,24);
//    			 电量
    			 elec = datas.substring(24,26);
    			
//    			 实时数据待更新
    			 updateTempHum( Type, equipment, funcCode, hy,
   		    		  temp, elec, hum, comp);
    			 
    		 }
//    		 00F2 01AC 140B 1410 1D00
//    		 00F2 0193 140B 1410 1E00
//    		 00F3 019A 140B 1410 1F00 
//    		 00F2 018F 140B 1410 2000 
//    		 00F2 018C 140B 1410 2100
    		 
//    		 历史记录 (报警数据自动过滤)
//    		 温湿度仪器
    		 if("1".equals(binary.substring(1,2)) && datas.length() > 36 && (funcCode.equals("0C") || funcCode.equals("02"))) {
    			 
    			 
    			 
    			 String h_len = datas.substring(16,20);
    			 Integer h_datas = HexConvert.hexString2Deci(h_len);
    			 String h_lu = datas.substring(20,h_datas*2+20);
    			 
//    			更新数据 ,温湿度仪器以10整除(小于10不执行，大于10非整除部分不执行)
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
    		        
    		        
//    		        时间组合
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
    		        ls.add(second.toString()+" ");
    		        String timeJoint = timeJoint(ls);
        			String history_time = timeJoint.substring(0, timeJoint.length()-1);
    		        
//    		   持久化，无电量更新
    			updateTempHumHistory(Type, equipment, funcCode, hy,
    	 		    		  temp, hum, comp,history_time);
    			}
//    		温度仪器
//    			TODO 温度仪器历史记录存在异常
    		 }else if(!"1".equals(binary.substring(1,2)) && datas.length() > 36 && (funcCode.equals("0C") || funcCode.equals("02"))) {
    			 String h_len = datas.substring(16,20);
    			 Integer h_datas = HexConvert.hexString2Deci(h_len);
    			 String h_lu = datas.substring(20,h_datas*2+20);
    			 
    			 
//    				温度仪器
				for (int j = 0; j < h_datas/8; j++) {
//    					温度仪器
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
					
					
//    			        时间组合
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
					ls.add(second.toString()+" ");
	    			String timeJoint = timeJoint(ls);
	    			String temp_time = timeJoint.substring(0, timeJoint.length()-1);
					
					updateTempHumHistory(Type, equipment, funcCode, hy,
							temp, hum, comp,temp_time);
				}
    		 }
//    		 告警数据
//    		 告警数据长度，高温高湿长度，和一般高温区别。
//    		 7E C0 02 20 00 0D CC 0D 00 09 03 29 03 14 0B 14 12 00 2A 0D  //length=40
//    		 7E C0 02 20 00 0D CC 0D 00 09 03 1F 67 14 0B 14 12 06 07 0D
//    		 7E C0 02 20 00 0D CC 0D 00 09 00 00 0C 14 0B 14 12 07 00 0D
//    		 7E 80 02 20 10 04 99 0D 00 09 01 61 01 14 0B 1A 11 12 0F 0D
    		 if(datas.length() == 40 && (funcCode.equals("0D") || funcCode.equals("03"))) {
    			 HygrothermographWarn hw = new HygrothermographWarn();
    			 
    			 String h_len = datas.substring(16,20);
    			 Integer h_datas = HexConvert.hexString2Deci(h_len);
    			 String h_lu = datas.substring(20,h_datas*2+20);
    			 
    			 String warning = h_lu.substring(0,4); //告警值
    			 String warningType = h_lu.substring(4,6);//告警类型
    			 
    			 String y = h_lu.substring(6,8);
    			 String m = h_lu.substring(8,10);
    			 String d = h_lu.substring(10,12);
    			 String hour = h_lu.substring(12,14);
    			 String mi = h_lu.substring(14,16);
    			 String s = h_lu.substring(16,18);
//    			        时间组合
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
    			ls.add(second.toString()+" ");
    			String timeJoint = timeJoint(ls);//告警时间
    			String warn_time = timeJoint.substring(0, timeJoint.length()-1);
//    			转为告警
//    			Integer warnvalue = HexConvert.hexString2Deci(warning);
    			
    			Integer warnType = HexConvert.hexString2Deci(warningType);
    			
    			String temp_warn = "";
    			String hum_warn = "";
//    			告警硬编码
    			if(1 == warnType || 2 == warnType || 101 == warnType || 102 == warnType){
    				temp_warn = warning;
    			}else if(3== warnType || 4 == warnType ||103 == warnType || 104 == warnType){
    				hum_warn = warning;
    			}
//    			设置告警值类型
    			hw.setWarnnum(warnType.toString());
    			
    			updateWarningHistory(Type, equipment,funcCode,comp,
    					temp_warn,hum_warn,warn_time,hw);
    		 }
    		 
//	    	平台回复
	    	 new sendMessThread(Type,equipment,funcCode,this.socket).start();
//				 控制数据
	    		 /*if(datas.length() == 24 && (funcCode.equals("0E") || funcCode.equals("04"))) {
	    			 LogUtil.logger.info("控制数据datas="+datas);
	    			 new sendMessThread(Type+equipment,funcCode,this.socket).start();
	    			 return ;
	    		 }
				 
				 
//	    		 告警写入并且返回
	    		 if (datas.length()==40 && (funcCode.equals("0D") || funcCode.equals("03"))) {
	    			
	    			
	    			String T_value = datas.substring(20,24); //触发值
	    			Double Ti_value = HexConvert.hexStringToDeci(T_value);
	    			
    				String w_value_type = datas.substring(24,26); //警告值类型
    				Integer warn_value_type = HexConvert.hexString2Deci(w_value_type);
    				
    				
    				String Date_year = datas.substring(26,28); //年
    				
    				String Date_month = datas.substring(28,30); //月
	    				
    				String Date_day = datas.substring(30,32); //日
    				
    				String hou = datas.substring(32,34); //小时
    				
    				String min = datas.substring(34,36); //分钟
    				
    				String sen = datas.substring(36,38); //秒
    				
	    			 
//	    			 告警日期解析
	    			 Integer year = HexConvert.hexString2Deci(Date_year);
	    			 Integer mouth = HexConvert.hexString2Deci(Date_month);
	    			 Integer day = HexConvert.hexString2Deci(Date_day);
	    			 Integer hour= HexConvert.hexString2Deci(hou);
	    			 Integer minute= HexConvert.hexString2Deci(min);
	    			 Integer sencond= HexConvert.hexString2Deci(sen);
	    			 
	    			 
	    			 
    				  判断日期格式，并且补全，温湿度截至日期2099年
    				 * 
    				 * 
    				 * 0 代表前面补充0      
    				 2 代表长度为2      
    				 d 代表参数为正数型      
	    			 
	    			 
	    			String mouth1 = String.format("%02d", mouth);   
	    			String day1 = String.format("%02d", day);   
	    			String hour1 = String.format("%02d", hour);   
	    			String minute1 = String.format("%02d", minute);   
	    			String sencond1 = String.format("%02d", sencond);   
	    			 
	    			

		             //		             结束当前线程回复
		             return ;
	    		 }

	    		*/
    	 }else {
    		 LogUtil.logger.info("其他数据格式");
    		 return ;
    	 }
    	 
		return ;
     } 
     
     
//		温湿度设备只支持21世纪时间
	public String timeJoint(List<String> l) {
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
//   更新历史记录
     public void updateTempHumHistory(String Type,String equipment,String funcCode,Hygrothermograph hy,
	   		 String temp,String hum,ComponentUtils comp,String endtime) {
//		设备mac
		hy.setMac(Type+equipment); 
//		取整时间
		String substring = endtime.substring(0, endtime.length()-2);
		endtime = substring+"00";
//		设置历史时间
		hy.setCurrentTime(endtime);

		
	      	 if(funcCode.equals("0C") || funcCode.equals("02")) {
//	 			默认设置电量
	 			hy.setElectric("100");
	      		 
	      		if(!StringUtils.isEmpty(temp)) {
		   			 
		   			String temperature = HexConvert.hexStringToDeci(temp)+"";
//		   			处理零下摄氏度
		   			String temp2 = getTemp(temperature);
					hy.setTemperature(temp2);
		   		 }
//				 更新湿度
				 if(!StringUtils.isEmpty(hum)) {
					 String humidity =  HexConvert.hexStringToDeci(hum)+"";
					 hy.setHumidity(humidity);
				 }
		   		 comp.addDataByTime(hy,endtime);
	      		 
	      	}
	}
//   更新告警历史记录
     public void updateWarningHistory(String Type,String equipment,String funcCode,ComponentUtils comp,
    		 String temp,String hum,String endtime,HygrothermographWarn hw) {
//		设备mac
    	 hw.setMac(Type+equipment);
 		
//		设置告警时间
    	/* 取整分钟时间
    	 * String substring = endtime.substring(0, endtime.length()-2);
  		 endtime = substring+"00";
  		 */
    	 
    	 hw.setCurrentTime(endtime);
    	 
    	 
    	 if(funcCode.equals("0D") || funcCode.equals("03")) {
//    		 设置告警温度
    		 if(!StringUtils.isEmpty(temp)) {
    			 
    			 String temperature = HexConvert.hexStringToDeci(temp)+"";
//		   			处理零下摄氏度
    			 String temp2 = getTemp(temperature);
    			 hw.setTemperature(temp2);
    		 }
//			设置告警湿度
    		 if(!StringUtils.isEmpty(hum)) {
    			 
    			 String humidity =  HexConvert.hexStringToDeci(hum)+"";
    			 hw.setHumidity(humidity);
    		 }
    		 
    		 comp.addWarnDataByTime(hw,endtime);
    		 
    	 }
     }

     
     
     
//     实时更新温湿度
     private void updateTempHum(String Type,String equipment,String funcCode,Hygrothermograph hy,
    		 String temp,String elec,String hum,ComponentUtils comp) {
//   	 实时数据(回复0B)/历史记录/告警记录
       	 if(funcCode.equals("0B") || funcCode.equals("01")) {
//	 		更新温度
    		 if(!StringUtils.isEmpty(temp)) {
    			 
    			String temperature = HexConvert.hexStringToDeci(temp)+"";
//    			处理零下摄氏度
    			String temp2 = getTemp(temperature);
 				hy.setTemperature(temp2);
    		 }
    		 
//    		 更新电量
			 if(!StringUtils.isEmpty(elec)) {
				 String electric = HexConvert.hexStringToDeci(elec)+"";
				 double parseInt = Double.parseDouble(electric);
				 parseInt = parseInt*10*25;
				 int b = (int)parseInt;					 
				 hy.setElectric(b+"");
	    	}
			 
//			 更新湿度
			 if(!StringUtils.isEmpty(hum)) {
				 String humidity =  HexConvert.hexStringToDeci(hum)+"";
				 hy.setHumidity(humidity);
			 }
			 hy.setMac(Type+equipment);
    		 comp.addData(hy);
//    		 LogUtil.logger.info(Type+equipment+hy);//数据测试
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
     
}
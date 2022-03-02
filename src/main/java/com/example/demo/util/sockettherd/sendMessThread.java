package com.example.demo.util.sockettherd;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.example.demo.domain.Humiture;
import com.example.demo.util.ComponentUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.parameter.ParameterField;
import com.example.demo.util.parameter.UserParameterField;
import com.example.demo.websocket.HumSocketHandler;

//     回复消息
class sendMessThread extends Thread{
     
	 public String Type; 
	 
	 public String equipment; 
	 
	 public String funcCode; 
	 
	 public Socket socket;
	 
     public sendMessThread(String Type,String equipment,String funcCode,Socket socket){
    	 this.Type = Type;
    	 this.equipment = equipment;
         this.funcCode = funcCode;
         this.socket = socket;
     }
     
     @Override
     public void run(){
         OutputStream out = null;
         try{
//            	 回复平台，返回上报才知道数据（温度或湿度）超阀
             if(this.socket != null){
            	 String str = "";  
            	//时间校准
        		 String date = getDate();
            	 
        		 /*告警
        		  * 7EC00220000DCC0D0009032903140B0A1114190D=stringbuilder高湿告警
        				 2020-11-10 17:29:24 [INFO] C00220000DCC0D=str平台请求历史数据，同时包含实时数据回复
        				 2020-11-10 17:29:28 [INFO] 7EC00220000DCC0D0009
        				 030E
        				 67140B0A1114210D=stringbuilder 解除高湿告警
        				 2020-11-10 17:29:28 [INFO] C00220000DCC0D=str平台请求历史数据，同时包含实时数据回复
        		 */
//        		 校准时间和设定阀值
            	 String binary = HexConvert.hexStringToByte(Type);
            	 if((this.funcCode.equals("0B") || this.funcCode.equals("01"))) {
            		 
            		if("1".equals(binary.substring(1,2))) {
//            			获取持久化温湿度阀值
            			HumSocketHandler humHandler = new HumSocketHandler();
            			List<Humiture> humSection = humHandler.getHumSection(this.Type+this.equipment);
//            			排除掉未绑定的仪器设备，并且回复仪器
            			if(null != humSection && humSection.size()>0 ) {
            				
            				String temperaturefitted = "";
                			String humidityfitted = "";
                			String saveinterval= ParameterField.interval_time;
                			
                			for (Humiture hu : humSection) {
                				temperaturefitted = hu.getTemperaturefitted();
                				humidityfitted = hu.getHumidityfitted();
    						}
                			
//                			获取温度阀值
                			String s1 = "";
            		    	String s2 = "";
            		    	double w1 = 0;
            		    	double w2 = 0;
                			if(!StringUtils.isEmpty(temperaturefitted)) {
                				String[] split = temperaturefitted.split("~");
                		    	
//                					阀值必须完整
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
//                			获取湿度阀值
                			String s3 = "";
                			String s4 = "";
                			double w3 = 0;
                			double w4 = 0;
                			if(!StringUtils.isEmpty(humidityfitted)) {
                				String[] split = humidityfitted.split("~");
                		    	
//            					阀值必须完整
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
                			
                			if(!StringUtils.isEmpty(w1+"") && !StringUtils.isEmpty(w2+"")
                					&& !StringUtils.isEmpty(w3+"") && !StringUtils.isEmpty(w4+"")
                					&& !StringUtils.isEmpty(saveinterval)) {
//                				低于0度的温度处理
                				String warn_higth_temp = getHexTemp(w1+"");
                				String warn_low_temp = getHexTemp(w2+"");
//                				湿度处理
                				int warn_higth_hum = (int)(w3*10);
                				int warn_low_hum = (int)(w4*10);
                				
                				String higth_temp = HexConvert.int2Hexstring(Integer.parseInt(warn_higth_temp));
                				String low_temp = HexConvert.int2Hexstring(Integer.parseInt(warn_low_temp));
                				String higth_hum = HexConvert.int2Hexstring(warn_higth_hum);
                				String low_hum = HexConvert.int2Hexstring(warn_low_hum);
                				String inter_record = HexConvert.int2Hexstring(Integer.parseInt(saveinterval));
//                				补全十六进制
                				String vt_ht = HexConvert.vt(higth_temp);
                				String vt_lt = HexConvert.vt(low_temp);
                				String vt_hh = HexConvert.vt(higth_hum);
                				String vt_lh = HexConvert.vt(low_hum);
                				String vt_ir = HexConvert.vt(inter_record);
                				
//                				LogUtil.logger.info(vt_ht+"=higth_temp"+vt_lt+"=low_temp"+vt_hh+"=higth_hum"+vt_lh+"=low_hum"+vt_ir+"=vt_ir");
                				
                				str =  "7E"+this.Type+this.equipment+this.funcCode+
                        				"0001000019"+date  //当前时间
                        				+vt_ir //实时数据上报间隔,后台用户设定
                        				+vt_ir //实时数据记录间隔，后台用户设定
                        				+"000A" //历史上报时刻
                    					+"00" //历史上报间隔
                    					+vt_ht //高温告警值
                    					+vt_lt //低温告警值
                    					+"0001" //温度桓冲值
                    					+vt_hh //高湿告警值
                    					+vt_lh //低湿告警值
                    					+"0001" //湿度桓冲值
                    					+"0D";
                			}
            				
            				
            			}
//            			仪器未绑定,数据继续记录，不回复
            				
            			
            		}else if(!"1".equals(binary.substring(1,2))){
            			
//            			获取持久化温湿度阀值
            			HumSocketHandler humHandler = new HumSocketHandler();
            			List<Humiture> humSection = humHandler.getHumSection(this.Type+this.equipment);
            			
//                		无设备绑定，并且回复常规回复数据
            			if(null != humSection && humSection.size()>0) {
            				
                			try {
                				String temperaturefitted = "";
                    			String saveinterval= ParameterField.interval_time;
                    			for (Humiture hu : humSection) {
                    				temperaturefitted = hu.getTemperaturefitted();
        						}
                    			
//                    			获取温度阀值
                    			String s1 = "";
                		    	String s2 = "";
                		    	double w1 = 0;
                		    	double w2 = 0;
                    			if(!StringUtils.isEmpty(temperaturefitted)) {
                    				String[] split = temperaturefitted.split("~");
                    		    	
//                    					阀值必须完整
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
                    			
                    			if(!StringUtils.isEmpty(w1+"") && !StringUtils.isEmpty(w2+"") && !StringUtils.isEmpty(saveinterval)) {
//                    				低于0度的温度处理
                    				String warn_higth_temp = getHexTemp(w1+"");
                    				String warn_low_temp = getHexTemp(w2+"");
                    				
                    				String higth_temp = HexConvert.int2Hexstring(Integer.parseInt(warn_higth_temp));
                    				String low_temp = HexConvert.int2Hexstring(Integer.parseInt(warn_low_temp));
                    				String inter_record = HexConvert.int2Hexstring(Integer.parseInt(saveinterval));
                    				
                    				String vt_ht = HexConvert.vt(higth_temp);
                    				String vt_lt = HexConvert.vt(low_temp);
                    				String vt_ir = HexConvert.vt(inter_record);
                    				
//                    				LogUtil.logger.info(vt_ht+"=higth_temp"+vt_lt+"=low_temp"+vt_ir+"=vt_ir");
                    				
                    				str =  "7E"+this.Type+this.equipment+this.funcCode+
                            				"0001000013"+date  //当前时间
                            				+vt_ir //实时数据上报间隔,后台用户设定
                            				+vt_ir //实时数据记录间隔，后台用户设定
                            				+"000A" //历史上报时刻	
                        					+"00" //历史上报间隔
                        					+vt_ht //高温告警值
                        					+vt_lt //低温告警值
                        					+"0001" //温度桓冲值
                        					+"0D";
                    			}
                    			
                    			
    							
    						} catch (Exception e) {
    							// TODO: handle exception
    							LogUtil.logger.error("未绑定设备信息："+this.Type+this.equipment);
    						}
            				
            			}
//            			仪器未绑定,数据继续记录，不回复
            			

            		}
            		
            	 }else {
//             		 统一回复 
             		str = "7E"+this.Type+this.equipment+this.funcCode+
                 			 "00010000000D";            		 
            	 }
            	 

            	
//         		 LogUtil.logger.info("发送数据"+str);
            	 byte[] bytes = HexConvert.hexStringToByteArray(str);//发送的16进制字符串
            	 
                 OutputStream os = this.socket.getOutputStream();
                 os.write(bytes);
                 os.close();
                 
                 this.socket.close();
            	 
             }
         }catch (IOException e) {
             e.printStackTrace();
         }
         

     }
     
// (温湿度低温解析)
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
    /* public static StringBuilder getTempHumFitted(String toolType) {
//		 告警阀值十六进制设置
		 String tempFitted = "";
		 String humFitted = "";
		 
//		 临时存放
		 String temp_Temp = "";
		 String temp_Hum = "";
//传递过来为空，默认使用持久化中数据
		 ComponentUtils comp = new ComponentUtils();
		 String mac =toolType.substring(4,12);
		 List<Humiture> fitted = comp.getFitted(mac);
		 
		 if(null != fitted && fitted.size()>0 ) {
			 for (Humiture humiture : fitted) {
				 
				temp_Temp = tempFitted = humiture.getTemperaturefitted();
				temp_Hum  = humFitted = humiture.getHumidityfitted();
				
//				 进行转换进制
				 if(!StringUtils.isEmpty(temp_Temp)) {
					 tempFitted = HexConvert.str2HexString(tempFitted);
				 }
				 if(!StringUtils.isEmpty(temp_Hum)) {
					 humFitted  = HexConvert.str2HexString(humFitted);
				 }
				 
				 
			}
		 }

		 

		// 持久化中为空，则进行默认设置 温度
		
		 if(StringUtils.isEmpty(tempFitted)) {
			 temp_Temp = "0~100";
		 }
//		  湿度
		 if(StringUtils.isEmpty(humFitted)) {
			 temp_Hum = "0~90";
		 }
		 
//		 更新阀值，并且持久化
		 Humiture humit = new Humiture();
		 humit.setTemperaturefitted(temp_Temp);
		 humit.setHumidityfitted(temp_Hum);
		 humit.setMac(mac);
		 comp.update(humit);
		 
		 
		 StringBuilder sb = new StringBuilder();
		 sb.append(tempFitted+";");
		 sb.append(humFitted);
		 
		 return sb;
     }*/

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
		 
//		 LogUtil.logger.info(minute+"=minute"+calendar.get(Calendar.MINUTE)+1);
		 
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
     
    /* public static String putStr(String l) {
    	 
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
     }*/
    
 }
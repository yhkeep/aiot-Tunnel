package com.example.demo.util.sockettherd;

import java.text.DecimalFormat;

import com.example.demo.domain.User;
import com.example.demo.util.token.TokenUtil;

/**
 * Created by wly on 2018/4/17.
 */
public class HexConvert {
	//字节数组转为十六进制字符串
	private static final String HEX_CODE = "0123456789ABCDEF";
	
	public static String byteArrayToHexString(byte[] bs) {
	    int _byteLen = bs.length;
	    StringBuilder _result = new StringBuilder(_byteLen * 2);
	    for (int i = 0; i < _byteLen; i++) {
	        int n = bs[i] & 0xFF;
	        _result.append(HEX_CODE.charAt(n >> 4));
	        _result.append(HEX_CODE.charAt(n & 0x0F));
	    }
	    return String.valueOf(_result);
	}
	
	/**
	 * 16进制表示的字符串转换为字节数组
	 *
	 * @param hexString 16进制表示的字符串
	 * @return byte[] 字节数组
	 */
	public static byte[] hexStringToByteArray(String hexString) {
	    hexString = hexString.replaceAll(" ", "");
	    int len = hexString.length();
	    byte[] bytes = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
	        bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
	                .digit(hexString.charAt(i + 1), 16));
	    }
	    return bytes;
	}
	
	//十六进制字符串转二进制
	public static String hexStringToByte(String s){
		Integer deci=Integer.parseInt(s,16);
	    //十进制转换成二进制!
	    String binary=Integer.toBinaryString(deci);
	    return binary;
	}
//	十六进制转十进制,浮点类型
	public static Double hexStringToDeci(String s){
		Integer deci=Integer.parseInt(s,16);
//		保留一位小数
		Double valueOf = Double.valueOf(deci*0.1);
		DecimalFormat df = new DecimalFormat("#.0");
		String deci_datas = df.format(valueOf);
		Double deci_one = Double.valueOf(deci_datas);
		return deci_one;
	}
	
//	十六进制转十进制 整数类型
	public static Integer hexString2Deci(String s) {
		Integer deci=Integer.parseInt(s,16);
		return deci;
	}
//	十进制转十六进制
	public static String int2Hexstring(int n) {
		StringBuilder sb = new StringBuilder(8);
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            sb = sb.append(b[n%16]);
            n = n/16;            
        }
        a = sb.reverse().toString();
        return a;
	}

	
	public static void main(String[] args) {
		
	/*	StringBuilder sb = new StringBuilder();
		sb.append("ab"+";");
		sb.append("abc");
		System.out.println(sb.toString().split(";")[0]);*/
		
		TokenUtil token = TokenUtil.getTokenInstall();
		User user = new User();
		user.setUsername("0280001");
		user.setPassword("123147");
		token.getToken(user);
//		System.out.println(token);
		
	}
//	含一位小数点，字符串转十六进制
	public static String str2HexString(String s) {

		String[] split = s.split("~");
		String stringTemp = split[0];
		String stringTemp1 = split[1];
		
		Integer mintemp = null;
		Integer maxtemp = null;
		
//		如果温度是负数，则加上32768 
		
//		最小值
		if(split[0].contains("-")) {
			String newTemp =  stringTemp.replace("-", "");
			mintemp =(int) (10*Double.valueOf(newTemp));
			mintemp = 65535 - mintemp;
		}else{
			mintemp =(int) (10*Double.valueOf(stringTemp));
		}
//		最大值
		if(split[1].contains("-")) {
			String newTemp =  stringTemp1.replace("-", "");
			maxtemp =(int) (10*Double.valueOf(newTemp));
			maxtemp = 65535 - maxtemp;
		}else {
			maxtemp =(int) (10*Double.valueOf(stringTemp1));
		}
		
		String mintempHex = int2Hexstring(mintemp);
		String maxtempHex = int2Hexstring(maxtemp);
		
		String vt = vt(mintempHex);
		String vt1 = vt(maxtempHex);
		
		
		return vt+vt1;
	}
	
//	补全数据进制
	public static String vt(String date) {
		if(date.length() == 4) {
			date = date;
		}else if (date.length() == 3){
			date ="0"+date;
		}else if (date.length() == 2) {
			date = "00"+date;
		}else if(date.length() == 1) {
			date = "000"+date;
		}else if(date.length() == 0) {
			date = "0000";
		}
		return date;
	}
	
	
	
}
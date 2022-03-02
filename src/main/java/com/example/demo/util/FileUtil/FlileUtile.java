package com.example.demo.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FlileUtile {
	
//	获取当前项目路径
	public String getPath(){
    	String path1 = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//    	 路径头，去除
		String replace = path1.replace("file:/", "");
//    		路径项目名，去除
		String realpath = replace.replace("huaxi.jar", "");
//    		jar包之后自动添加的路径，去除
		String realpath1 = realpath.replace("!/BOOT-INF/classes!", "");
		String path = realpath1.replace("\\", "/");
//    		E:/workspace32/springbootdemo02_userloginbak/target//realpath
		//    	去掉最后的target//属性
		
		String upload = path+"t";
  		
		GenerateFile(upload);
		
    	return upload+"/images/";  //加入绝对路径
     }
//	创建目录
	public static void GenerateFile(String upload) {
		
//		申请人签名照
		String handlingpeoplesignatureimage =upload+"/images/handlingPeople"; 
  		File hadimage = new File(handlingpeoplesignatureimage); 
  		if(!hadimage.exists()){
  			hadimage.mkdirs(); 
  		}
//  	交接人签名照
  		String buttingpeoplesignatureimage =upload+"/images/buttingPeople";
  		File blimage = new File(buttingpeoplesignatureimage); 
  		if(!blimage.exists()){
  			blimage.mkdirs(); 
  		}
//  	交接资产图片
  		String handoverimage =upload+"/images/handoverimage"; 
  		File hdimage = new File(handoverimage); 
  		if(!hdimage.exists()){
  			hdimage.mkdirs(); 
  		}
		
		
//		缩小图路径
		String compress =upload+"/images/compress"; 
  		File com = new File(compress); 
  		if(!com.exists()){
  			com.mkdirs(); 
  		}
  		
//  	左侧图
  		String left =upload+"/images/left"; 
  		File le = new File(left); 
  		if(!le.exists()){
  			le.mkdirs(); 
  		}
//  	右侧图
  		String right =upload+"/images/right"; 
  		File ri = new File(right); 
  		if(!ri.exists()){
  			ri.mkdirs(); 
  		}
//  	俯视图
  		String above =upload+"/images/above"; 
  		File ab = new File(above); 
  		if(!ab.exists()){
  			ab.mkdirs(); 
  		}
//  	全图
  		String allround =upload+"/images/allround"; 
  		File ar = new File(allround); 
  		if(!ar.exists()){
  			ar.mkdirs(); 
  		}
//  	纸质图
  		String paperlabel =upload+"/images/paperlabel"; 
  		File pl = new File(paperlabel); 
  		if(!pl.exists()){
  			pl.mkdirs(); 
  		}
//  	一维码图
  		String onecodelable =upload+"/images/onecodelable"; 
  		File ol = new File(onecodelable); 
  		if(!ol.exists()){
  			ol.mkdirs(); 
  		}
  		
//  	原始图上传路径
  		File f = new File(upload); 
  		if(!f.exists()){
  			f.mkdirs(); //创建目录
  		}
	}
	
	
//	生成图片
	public static boolean GenerateImage(String imgStr,String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
    	imgFilePath = "/"+imgFilePath; //绝对路径生成文件
    	
        if (imgStr==null){
            // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
 
            return true;
        } catch (Exception e) {
            return false;
        }
    }
//	图片文件转为base64
	 public static String fileToBase64(String path) {
	        String base64 = null;
	        InputStream in = null;
	        byte[] data = null;
	        try {
	            in = new FileInputStream(new File(path));
	            data = new byte[in.available()];
	            in.read(data);
	            in.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        //对字节数组Base64编码
	        BASE64Encoder encoder = new BASE64Encoder();
	        //返回Base64编码过的字节数组字符串
	        return encoder.encode(data);
	}

}
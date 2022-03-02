/*package com.example.demo.service;
*//**
 * @author yanghan
 * @date 2019/7/30上午11:04
 *//*
public class CoordinateTest {
 
    public static void main(String[] args) {
        // 多边形经度数组
        double[] lon = {113.314882,113.355845,113.370289,113.356779,113.338238,113.330979,113.313588,113.323865,113.314882};
        // 多边形纬度数组
        double[] lat = {23.163055,23.167042,23.149564,23.129758,23.13913,23.124706,23.140858,23.158204,23.163055};
        // 测试在多边形内的点
        boolean inAccurateArea = isInAccurateArea(113.330908, 23.125678, lon, lat);
        System.out.println(inAccurateArea);
        // 测试不在多边形内的点
        boolean outAccurateArea = isInAccurateArea(113.34831, 23.127335, lon, lat);
        System.out.println(outAccurateArea);
    	
    	// 多边形纬度数组
    	double[] lon = {-4.990611,-4.999590,-4.996872,-4.951313,-4.953017};
        double[] lat = {139.398133,139.413353,139.485543,139.484634,139.396987};
        
        // 测试在多边形内的点
        boolean inAccurateArea = isInAccurateArea(-4.987403,139.454723, lon, lat);
        System.out.println(inAccurateArea);
        boolean inAccurateArea_border = isInAccurateArea(-4.953635,139.483244, lon, lat);
        System.out.println(inAccurateArea_border);
        boolean inAccurateArea_border4 = isInAccurateArea(-4.982069,139.400471, lon, lat);
        System.out.println(inAccurateArea_border4);
        
        
//        边界线
        boolean inAccurateArea_border1 = isInAccurateArea(-4.970924, 139.397369, lon, lat);
        System.out.println(inAccurateArea_border1);
        boolean inAccurateArea_border2 = isInAccurateArea(-4.970028, 139.484881, lon, lat);
        System.out.println(inAccurateArea_border2);
        
        
        
        // 测试不在多边形内的点
        System.out.println("线外点");
        boolean outAccurateArea = isInAccurateArea(-5.000921,139.454651, lon, lat);
        System.out.println(outAccurateArea);
        boolean outAccurateArea1 = isInAccurateArea(-4.948807, 139.422849, lon, lat);
        System.out.println(outAccurateArea1);
        boolean outAccurateArea2 = isInAccurateArea(-4.973389, 139.486770, lon, lat);
        System.out.println(outAccurateArea2);
        
    	
    }
 
    *//**
     * 从已知坐标向多边形划线，如果为偶数说明点在多边形外，奇数说明在多边形内
     *
     * @param pointLon
     * @param pointLat
     * @param lon
     * @param lat
     * @return
     *//*
    private static boolean isInAccurateArea(double pointLon, double pointLat, double[] lon,
                                            double[] lat) {
        // 代表有几个交点
        int vertexNum = lon.length;
        boolean result = false;
 
        for (int i = 0, j = vertexNum - 1; i < vertexNum; j = i++) {
            // 满足条件，与多边形相交一次，result布尔值取反一次，奇数个则在区域内
            if ((lon[i] > pointLon) != (lon[j] > pointLon)
                    && (pointLat < (lat[j] - lat[i]) * (pointLon - lon[i]) / (lon[j] - lon[i])
                    + lat[i])) {
                result = !result;
            }
        }
        return result;
    }
    
    
}*/


package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;


public class CoordinateTest {

	public static void main(String[] args) {  
	       
	    
	    
	    boolean inCircle = isInCircle(0.0555,-4.801100,139.336066,-5.01409,139.336066);
	    System.out.println(inCircle);
	  }
	
	
	
	private static boolean isInCircle(double r,double centerX1,double centerY1,double x2,double y2){
	    double distance=Math.sqrt((y2-centerY1)*(y2-centerY1)+(x2-centerX1)*(x2-centerX1));
	    if(distance>r){
	        return false;
	    }
	      else
	    {return true;}
   }

}


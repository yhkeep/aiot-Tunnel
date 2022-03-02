package com.example.demo.util.parameter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.StompConnection;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.service.IbeaconGatewayService;

public class ParameterField{
//	页面分页大小
	public static Integer pageSize = 10;
	
/*//TODO0429网关更新时间-单位秒
	public static int GATEWAYDELAYTIME = 60;
//	避免同时更新
	public static int GATEWAYDELAYTIME1 = 55;*/
	
//	网关更新时间-单位秒(TODO0429时间过短，内存压力较大)
	public static int GATEWAYDELAYTIME = 1;
//	避免同时更新
	public static int GATEWAYDELAYTIME1 = 3;
	
	
	
//	页面判断离线时间
	public static int GATEWAYDELAYTIME2 = 90;
//	温湿度保存间隔时间
	public static int tempHumTime = 10*60;
//	超低温仪器设备记录间隔时间
	public static String interval_time = "1";
	
//	网关操作时间
	public static int actionTime = 30;
	
//	单线程redis缓存标签对应网关失效时间
	public static int redis_gatewaymac_disstime = 32;  
	
//	cation:exp时间必须大于多级缓存更新时间 ，并且该时间用于定位切换ap时间
//	该值与标签发送信号时间有关，如8-10秒每次===//实际redis缓存在10秒内
	public static int redis_express_disstime = 12;
	
//	redis前10秒内ap数据
	public static int redis_express_disstime_ago = 10; 
	
//  多线程更新时间
	
	public static int cache1 = 10;
//	public static int cache2 = 20;
//	public static int cache3 = 30;
	
//	限制队列长度  该值数值越大ap稳定性越强，ap数值越小ap位置速度返回越快（位置及时性越强）
//	public static int lqSize = 7;//TODO21-01-23 后台数据库设置，方便华西内网调试
	
	public static int lqSize_moreThanTwo = 7;//过滤后稳定队列，用于筛选其中所包含连续两次ap
	
	public static int lqSize_real_floor = 7;//过滤后稳定队列，用于筛选其中较大较多rssi所在ap的楼层
	
	public static int lqSize_real_circle = 7;//缓存当前焦点圆范围内7个ap队列最初信号接受值
	
	public static int lqSize_real_ap = 1;//刷选楼层后剩余的准ap
	
	public static int lqSize_newap_more = 5;//最新最强连续多次ap，华西本地测试（10）可降低次数（存在个别稳定性不强的情况，适当增加队列长度-原10，华西测试为TODO210720==10）
	
	public static int lqSize = 3;//TODO21-03-17 3次未发现ap，便切换。加快ap切换，从而通过数据库稳定
	
	public static int lqCacheSize = 1; //最大焦点ap缓存队列个数
	
	public static int floorRssi = -50; //楼层rssi临界值,现场因子决定,低于该值可能在其他楼层
	
	public static int rssi_min = -90; //楼层rssi临界值,现场因子决定,低于该值标签将不会接受到信号，适用ap环境较多的情况
	
	public static int floor_filter_rssi = -65;//楼层过滤因子，节省资源消耗，因环境而定
	
	public static int ap_change_rssi = -45;//ap切换rssi最小值
	
	public static int ap_continue_num = 4;//二次过滤中连续ap出现最低次数 （提高ap连续次数==4次以上，进行跳动频繁和切换次数较少进行两极分化）
	
	public static int floor_continue_num = 3;//三次连续楼层，为准楼层
	
//	public static int ap_unline_num = 1;//ap离线或不是最强的在队列中不出现的次数(TODO210531待修改,提高定位时效,建议修改为4-5)
	public static int ap_unline_num = 4;
	
	public static int floor_rssi_flot = 5;//不同楼层信号接受值，上下浮动跳动值
	
	public static int room_rssi_flot = 5;//相同楼层规定圆范围内不同房间信号接受值，上下浮动跳动值（5-8）
	
	
	public static int room_rssi_flot_unonline = 8;//相同楼层不同房间信号接受值，上下浮动跳动值（5-8个单位以上），加速圆内ap快速切换，单位越低，速度越块
	
	public static int room_rssi_flot_unonline_cache = 5;//缓存队列中已存在并且大于原有值固有单位以上（5-8）, 本地测试:建议取值8，稳定性强
	
//	public static int StrongSelf = 3;//并且连续3次强过自身,则切换ap(提高准确度)
	
	public static int old_ciry_rssi = 8;//超过圆外8个单位以上则进行切换。 华西测试使用5-8，本地测试使用3左右
	
	public static int old_low_ciry_rssi = 5;//超过圆外8个单位以上则进行切换。
	
//	队列中连续相同ap数
//	public static int numberContinuous = 3; //数字越小，精确度越高，但是稳定性越差，并且小于等于lqSize
	public static int numberContinuous = 6; //TODO21-01-08
	
	public static int serviceNum = 16;//线程池存储线程数量
	
//	错时更新缓存中网关
	public static String gatewaymac_comp = "AC233FC05555";
	
//	映射后路径端口
	public static String MQTT_PORT = ":8009";
}

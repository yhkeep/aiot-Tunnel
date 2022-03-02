package com.example.demo.util.parameter;

public class UserParameterField{
//	页面设备状态超时时间设定范围
	public static final Integer equipmentDynamic = 180; //单位秒(TODO210707-180)
//TODO1104	public static final Integer equipmentDynamic = 300; //定位设备时间延长，调整
	
//	实时数据时间截至范围
//	public static final Integer RealtimeMapPush = 60; //单位秒
	public static final Integer RealtimeMapPush = 180; //标签数据发送与接受时间间隔较长，可适当增加
	
	
	
//TODO1104	public static final Integer RealtimeMapPush = 240; //定位设备时间延长，调整
	
//	环境因子--rssi移动阈值
	public static final Integer RssiScope = -48;//TODO0924,长距离移动效果不错
	
	public static final Integer RSSI_tolerances = 5; // ap最强rssi值波动范围，环境波动因子，最高阀值5-7
	
	public static final Integer RSSI_tolerances_low = 10; // ap最强rssi值波动范围，环境波动因子，最低阀值8-10
	
	
//	public static final Integer change_RssiScope = -45;//TODO0923-13:59调整
	public static final Integer change_RssiScope = -70;//TODO0923-14:18
//	资产缓存时间
	public static final Integer assetCacheTime = 2;
	
//	单位时间准确楼层缓存更新-过期时间
	public static final Integer floor_exp_time_Cache = 3;
//	楼层缓存一分钟内时间更新-过期时间
	public static final Integer floor_exp_OneMinute_Cache = 15;
//	缓存楼层必须大于等于当前时间2秒之后
	public static final Integer TIME_EXP_CACHE = 0;
//	public static final Integer TIME_EXP_CACHE = 2; TODO210616 同秒内新发送数据也进行解析
	
	
	
//	ap_rssi多层失效时间,必须大于等于redis缓存失效时间
//	public static final Integer exp_Cache = 55;
//	public static final Integer exp_Cache = 35;
	public static final Integer exp_Cache = 15;
	
	
/*	public static final Integer One1Cache = 61;
	public static final Integer Two2Cache = 61;
	public static final Integer Three3Cache = 61;
	public static final Integer Three4Cache = 61;
	public static final Integer Three5Cache = 61;
*/	
//	华西地图圆心限制半径
//	public static final double cirle = 0.0555;
//	尽量小，保证稳定性即可。
	
	public static final double cirle = 0.888;//TODO210611,易柯尔本地测试 公司临界测试值为0.20，本地8502，7217测试两个ap之间距离半径0.0539，最大两者380F，8509与8502间距最大0.15
//	public static final double cirle = 0.0655;//TODO210715 ，华西半径0.0655~0.0555较为理想
//	public static final double cirle = 0.0655;//圆半径越小，准确度较高。待测试（过小超过楼层间辐射范围，将会导致穿楼层现象，即所在真实楼层覆盖的ap越多，楼层真实性越强）
	
	
	
	
//	超远距离不进行ap切换
//	public static final double long_distance_cirle = 1.0;
	public static final double long_distance_cirle = 0.0888;//TODO0923-14:27
//	QOS订阅数据访问等级
	public static final Integer QOS = 1;
//	PUB客户发布等级
	public static final Integer PUB = 1;
	
	
//	温湿度设置数据保存间隔时间
	public static final Integer TEMP_HUM_TIMELIMI = 10*60;
//	超低温间隔数据保存时间
	public static final Integer MINUTE_ = 10;
//	当前时间点（包含当前时间）10分钟内告警
	public static final Integer Warn_duce_time = 10;
//	华西ap以及温湿度地址识别号:(阿里云公共平台地址识别号同项目地址，泸二院：2，郫二院：3)。token未获取到使用
	public static final String address = "1";
// 	s1低电量告警阀值
	public static final Integer S1_electric = 25;
//	GS1低电量告警
	public static final Integer GS1_electric = 25;
//  订阅完成连接时间，超过则开始回调并解析数据
	public static final Integer Tisk_SubAndExp_TIME = 3000;
}

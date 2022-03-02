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

public class GatewayParameterField{
	
//	网关添加
	public static String gatewayType = "网关";
//	mqtt服务器路径
	public static String gateway_mUrl = "tcp://192.168.218.179:8009"; //tcp://172.21.244.86:8009
//	mac地址过滤
	public static String gateway_macReg = "^AC23.*";
//	端口
	public static String gateway_port = ":8009";
//	qos等级
	public static String gateway_qos = "1";
	
//	本地cache缓存数量阀值
	public static Integer cache_number = 1000;
//	本地cache缓存销毁时间
	public static Integer cache_destroytime = 15;
//	本地cache网关缓存数量阀值
	public static Integer cacheGateway_number = 100000;
//	本地cache网关缓存销毁时间3分钟
	public static Integer cacheGateway_destroytime = 3*60;
}

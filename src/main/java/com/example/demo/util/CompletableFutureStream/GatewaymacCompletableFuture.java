package com.example.demo.util.CompletableFutureStream;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.aio.GatewayProgress;
import com.example.demo.domain.IbeaconHistory;
import com.example.demo.domain.Received;
import com.example.demo.mqtt.MqttHandler;
import com.example.demo.mqtt.MqttPushClient;
import com.example.demo.service.impl.ReceivedServiceImpl;
import com.example.demo.util.parameter.ParameterField;

//public class GatewaymacCompletableFuture<String,Gateway> extends BaseCompletableFuture{
//TODO0928去掉泛型
//public class GatewaymacCompletableFuture<String,Gateway>{
//public class GatewaymacCompletableFuture{
public class GatewaymacCompletableFuture{
	private GatewayProgress<String> gatewayProgress = new GatewayProgress<String>();	
	
	
	private static final ExecutorService executorService = getExecutorService();
//	共用future对象，避免高重复生产，导致内存溢出
	private static final CompletableFuture<java.lang.String> future = CompletableFuture.supplyAsync(()->{
		return "";
	},executorService);
	
	/*
	 * 一级缓存
	 * private Cache<String,Gateway> cache= CacheBuilder.newBuilder().maximumSize(GatewayParameterField.gatewaymac_number)
	           .expireAfterAccess(GatewayParameterField.gatewaymac_destroytime,TimeUnit.SECONDS)
	           .build();
	
	public Object getCache(Object keyValue) {
		// TODO Auto-generated method stub
		Object value = null;
		try {
			// 从缓存获取数据
			value = cache.get((String) (keyValue), new Callable<Gateway>() {
				// 返回网关数据库数据 
				@SuppressWarnings("unchecked")
				public Gateway call() {
					List<Gateway> ls = new ArrayList<>();
					ls.add((Gateway) "缓存加载"); 
					return (Gateway) ls;
//					return null;
				}
			});
			
			
		} catch (ExecutionException e) {
			e.printStackTrace();
			LogUtil.logger.error("google cache"+e);
		}
		return value;
	}*/
	
//	覆写run方法,TODO210722=============================
	private MqttPushClient instance;
	
	public GatewaymacCompletableFuture(MqttPushClient instance) {
		super();
		this.instance = instance;
	}
	public GatewaymacCompletableFuture() {
		super();
	}
//	==================================================
	
	
//  限定线程池中线程数量
	public static ExecutorService getExecutorService() {
    	ExecutorService executorService = Executors.newFixedThreadPool(16,
    			new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						// TODO Auto-generated method stub
						Thread tr = new Thread(r);
						tr.setDaemon(true);
						return tr;
					}
				});
    	return executorService;
	}
	
//	集合大小划分线程池
	public static ExecutorService getExecutorService(List userCode) {
		int serviceNum = 16;
		
		if(!userCode.isEmpty() && null != userCode) {
			serviceNum = userCode.size();
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(Math.min(serviceNum, 100),
				new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				// TODO Auto-generated method stub
				Thread tr = new Thread(r);
				tr.setDaemon(true);
				return tr;
			}
		});
		return executorService;
	}
	
	
//	及时更新网关
	public void completFutureUpdateGateway(String gatewaymac,String currnettime) {
//		更新网关
		future.thenApply(s -> gatewayProgress.updateGateway(gatewaymac, currnettime));
    	
	}
	
//	更新received
	public void completFutureUpdateRecevied(Received re) {
		
		future.thenApply(s -> gatewayProgress.updateReceviedDatas(re));
    	
	}
	
//	插入ibeaconHistory数据
	public void completFutureInsertHistoryIbeacon(IbeaconHistory ibeacon) {
		
		future.thenApply(s -> gatewayProgress.insertHistoryDatas(ibeacon));
		
	}
//	更新ibeaconHistory数据
	public void completFutureUpdateHistoryIbeacon(IbeaconHistory ibeacon) {
		
		future.thenApply(s -> gatewayProgress.updateHistoryDatas(ibeacon));
		
	}
//	查询ibeaconHistory单个标签单位时间切片数据
	public List<IbeaconHistory> completFutureQueryHistoryIbeacon(String mac,String timestamp) throws InterruptedException, ExecutionException {
		CompletableFuture<List<IbeaconHistory>> thenApply = future.thenApply(s -> {
			List<IbeaconHistory> queryHistoryDatas = gatewayProgress.queryHistoryDatas(mac,timestamp);			
			return queryHistoryDatas;
		});
		List<IbeaconHistory> queryMap = thenApply.get();
		return queryMap;
	}
	
	
	
//	异步11楼层进行订阅
	public void completFutureClientSub11(MqttPushClient instance) {
		
		
		MqttHandler mqttHandler = new MqttHandler();
//		更新网关
		future.thenApply(s -> {
			mqttHandler.allGatewayAndClient11(instance, instance.client);
			return "";
		});
    	
	}
//	异步12楼层进行订阅
	public void completFutureClientSub12(MqttPushClient instance) {
		
		
		MqttHandler mqttHandler = new MqttHandler();
//		更新网关
		future.thenApply(s -> {
			mqttHandler.allGatewayAndClient12(instance, instance.client);
			return "";
		});
		
	}
//	异步13楼层进行订阅
	public void completFutureClientSub13(MqttPushClient instance) {
		
		
		MqttHandler mqttHandler = new MqttHandler();
//		更新网关
		future.thenApply(s -> {
			mqttHandler.allGatewayAndClient13(instance, instance.client);
			return "";
		});
		
	}
	
//	总订阅以及休眠重新订阅(TODO0430内存消耗持续增强)
	public void completFutureClientSubAll(MqttPushClient instance) {
		/*MqttHandler mqttHandler = new MqttHandler();
		mqttHandler.allGatewayAndClient(instance, instance.client);单线程缓慢加载，不断积压内存*/
		
		MqttHandler mqttHandler = new MqttHandler();
//		更新网关
		future.thenApply(s -> {
			mqttHandler.allGatewayAndClient(instance, instance.client);
			return "";
		});
		
	}
	
//	异步查询
	public List<Map> completQueryReceived(String timeEnd) throws InterruptedException, ExecutionException {
		
		ReceivedServiceImpl rsi = new ReceivedServiceImpl();
//		更新网关
		CompletableFuture<List<Map>> thenApply = future.thenApply(s -> {
			List<Map> queryReceiveData = rsi.queryReceivedByTime(timeEnd);
//			return "";
			return queryReceiveData;
		});
		List<Map> queryMap = thenApply.get();
		return queryMap;
	}
	
	
//	查询hygrothermograph温湿度历史记录
	public List<Map> completFutureQueryTempHum(String mac,String startime,String currentTime) throws InterruptedException, ExecutionException {
		CompletableFuture<List<Map>> thenApply = future.thenApply(s -> {
			List<Map> queryHistoryDatas = gatewayProgress.queryHyByTime(mac,startime,currentTime);			
			return queryHistoryDatas;
		});
		List<Map> queryMap = thenApply.get();
		return queryMap;
	}
	
}
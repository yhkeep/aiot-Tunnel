/*package com.example.demo.service.impl;

import java.text.ParseException;

//import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.cache.GuavaAssetData;
import com.example.demo.cache.GuavaLimitFloorCache;
//import com.example.demo.cache.GuavaLimitFloorOneMiuteCache;
import com.example.demo.cache.GuavaMaxApCache;
import com.example.demo.cache.GuavaMoreThanTwoCache;
import com.example.demo.cache.GuavaStatSliceCache;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.IbeaconHistory;
import com.example.demo.domain.LimitQueue;
import com.example.demo.domain.Received;
import com.example.demo.mapper.IbeaconGatewayMapper;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.AssetService;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.service.ReceivedService;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;
import com.example.demo.util.parameter.ParameterField;
import com.example.demo.util.parameter.UserParameterField;
@Service
public class IbeaconGatewayServiceImpl210521 implements IbeaconGatewayService{
	
	@Autowired
	private IbeaconGatewayMapper ibeaconMapper;
	
	@Autowired
    private RedisUtils redisUtil;	
	
	@Autowired
	private ReceivedService reService;
	
	@Autowired
    private AssetService assetService;
	
	private String  newi = "AC233FC08519";//次数异常统计
	
	private String  newitest = "AC233FC08519";//次数异常统计
	
	private String  floor_max = "1";//次数异常统计
	
	private Integer  count = 0;//次数异常统计
	
	private Integer  counttest = 0;//次数异常统计
	
	
	
//	private GuavaLimitCache glc = new GuavaLimitCache(); //缓存多次最强ap
	
	private GuavaStatSliceCache gssc = new GuavaStatSliceCache();//缓存多个队列切片
	
	private GuavaMoreThanTwoCache moreThanTwo = new GuavaMoreThanTwoCache();//刷选连续多个最强rssi值
	
	private GuavaLimitFloorCache glfc = new GuavaLimitFloorCache();//缓存标签接近真实楼层,过滤下一秒时间
	
//	private GuavaLimitFloorOneMiuteCache glfomc = new GuavaLimitFloorOneMiuteCache();//缓存标签一分钟数据存储
	
	private GuavaMaxApCache gmac = new GuavaMaxApCache<>();//缓存总节点ap与稳定值rssi
	
//	private GuavaMacReceived<String, Object> gr = new GuavaMacReceived();
//	private Guava3Cache g3c = new Guava3Cache ();
//	private Guava2Cache g2c = new Guava2Cache();
	
	@Override
	public void insert(IbeaconGateway ibeacon) {
		// TODO Auto-generated method stub
		ibeaconMapper.save(ibeacon);
	}




	@Override
	public List<Map> find(String mac, String startTime, String endTime) {
		// TODO Auto-generated method stub
		List findByTime = ibeaconMapper.findByTime(mac, startTime, endTime);
		return findByTime;
	}


	@Override
	public void updateRssi(IbeaconGateway ibeacon) {
		// TODO Auto-generated method stub
		ibeaconMapper.update(ibeacon);
		
	}


	@Override
	public List<Map> findGatewayByMac(String mac,String startTime,String currnetTime) {
		// TODO Auto-generated method stub
		List gatewayDatas = ibeaconMapper.findGatewayMacByMac(mac,startTime,currnetTime);
		return gatewayDatas;
	}


	@Override
	public List<Map> searchOneDay(String startTime, String endTime) {
		// TODO Auto-generated method stub
		List findByTime = ibeaconMapper.findDataByOneDay(startTime, endTime);
		return findByTime;
	}


	@Override
	public List<Map> findMacByMac(String mac, String currnetTime,String startTime) {
		// TODO Auto-generated method stub
		List macData = ibeaconMapper.searchMacByMac(mac,currnetTime,startTime);
		return macData;
	}
	@Override
	public List<Map> findMacByMac(String gatewaymac, String mac, String currnetTime,String startTime) {
		// TODO Auto-generated method stub
		List macData = ibeaconMapper.searchMacByMac(gatewaymac,mac,currnetTime,startTime);
		return macData;
	}


	@Override
	public void saveMac(Received re) {
		// TODO Auto-generated method stub
		ibeaconMapper.persistence(re);
	}

	
	
	

	@Override
//	@Transactional 抛出异常
	public void updateRecevied(Received re) {
		// TODO Auto-generated method stub
		timeUtiles times = new timeUtiles();
		
//	redis获取最强ap-->队列统计-->连续3次-->数据库持久化
		
		try {
			if(null != re && null != re.getMac() && null != re.getGatewaymac() && null != re.getRssi()
				 && !StringUtils.isEmpty(re.getRssi())&&!StringUtils.isEmpty(re.getMac())&&!StringUtils.isEmpty(re.getGatewaymac())) {
//				获取标签对应的所有ap
				Map<Object, Object> hmget = redisUtil.hmget(re.getMac());
				
				if(null == hmget || hmget.isEmpty()) {
					return;
				}
				
				try {
					JsonUtils js = new JsonUtils();
					   
//					   上一时间节点1秒内不进行楼层切换（线程安全）TODO0310华西待修改
					   	 Integer filterTime = filterTime(re,times);
					   	 if(-1 == filterTime) {
					   		 return;
					   	 }
					
					   
					   	 
					   	 
//					单个标签所处ap位置楼层（大部分归类）属于时间切片
				         Iterator<Map.Entry<Object, Object>> iterator = hmget.entrySet().iterator();
				         String real_ap_floor = "";
				         
				         LimitQueue<Map> lq_hmget_ap = new LimitQueue<>(hmget.size()-1);
				         
				         
				         
				         while (iterator.hasNext()) {
				             Map.Entry<Object, Object> entry = iterator.next();
				             Object key = entry.getKey();
				             if("expressTime".equals(key)) {
									continue;
							 }
							   Map value = (Map)entry.getValue();
							   String gatewaymac_key = key.toString();
//							   ap更新缓存存在一分钟时间差
							   Map<Object, Object> hmgate = redisUtil.hmget(gatewaymac_key);
							   if(null != hmgate && !hmgate.isEmpty()) {
								   Gateway gate_way = (Gateway)hmgate.get("gate");
								   String floor = gate_way.getFloor();
								   String mapx = gate_way.getMapx();
								   String mapy = gate_way.getMapy();
//								   单个ap属性统一放入队列
								   Map gateway_info_ib = new HashMap<>();
								   gateway_info_ib.put("currentTime",value.get("currentTime"));
								   gateway_info_ib.put("rssi", value.get("rssi"));
								   
								   gateway_info_ib.put("mapx", mapx);
								   gateway_info_ib.put("mapy", mapy);
								   gateway_info_ib.put("floor", floor);
								   gateway_info_ib.put("gatewaymac", gatewaymac_key);
								   gateway_info_ib.put("mac", re.getMac());
								   lq_hmget_ap.offer(gateway_info_ib);
								   
							   }
							   
				         }

					
							   
				       
				        *   方法说明：同楼层中ap通过最强rssi值不停进行最强更新，不同楼层情况，
				         根据rssi值以及队列中最强楼层所占比例进行移动（不考虑多楼层中只有一个特殊情况）
				         *
				         
						 
				         
				         
				         if(re.getMac().equals("AC233FB100EE")) {
				        	 statApTimeSlice(lq_hmget_ap,times,re);
				         }
				      
				         
				         statApTimeSlice(lq_hmget_ap,times,re);
				        
					     
					     Map<String,LimitQueue<Map>> ap_rssi_lqtest = (Map)gmac.getCache(re.getMac());
						 if(null == ap_rssi_lqtest || ap_rssi_lqtest.size()== 0) {
							 return;
						 }
						 LimitQueue<Map> limitQueue = ap_rssi_lqtest.get("muliteap");
//						 最大ap/rssi/楼层
						 String max_ap_Ele_Number = "";
						 String max_ap_Ele_rssi = "";
						 String max_ap_Ele_floor = "";
						 Queue<Map> api = limitQueue.getQueue();
						 for (Map map : api) {
								max_ap_Ele_Number  = map.get("mulite_ap_max").toString();
								max_ap_Ele_rssi  = map.get("mulite_ap_rssi").toString();
								max_ap_Ele_floor  = map.get("mulite_ap_floor").toString();
						 }
							  
				    	
//				    	获取该最强ap后，通过该ap楼层及坐标，与队列中同楼层和不同楼层ap及坐标，
				    	
						 
				    	

//				    	统计更新ap次数
						if("AC233FB100EE".equals(re.getMac())) {
							LogUtil.logger.info(rece_lq.getQueue()+"=更新后获取队列中数据"+max_ap_Ele_Number);
							if(!newi.equals(max_ap_Ele_Number) && !StringUtils.isEmpty(max_ap_Ele_Number)) {
								count++;
//								LogUtil.logger.info(count+"==count");
								newi = max_ap_Ele_Number;
							}
						}
				    	
						
						
						
//			    		rece表中更新最强ap
			    		IbeaconGateway ib = new IbeaconGateway(); 
						ib.setMac(re.getMac());
						ib.setGatewaymac(re.getGatewaymac());
						
						List<Map> ibdatas = ibeaconMapper.findGatewayByMac(ib);
						
						Long count= ibdatas.parallelStream().filter(mapdatas -> {
							return  (Long)mapdatas.get("count(*)") <= 0;
						}).count();
						
				         
						
						if(StringUtils.isEmpty(max_ap_Ele_rssi) || StringUtils.isEmpty(max_ap_Ele_floor)) {
							return;
						}
//						通过ibeaconHistory历史记录,获取上一天结果判断如：大于500-1000切换历史记录的ap，限定其以最强rssi作为最强ap，避免出现如网口出现异常导致ap切换
						
						*//**
						 * 
						 * 
						 * TODO0311获取ap位置及其楼层所在，作为均值圆心，该圆范围内（除楼梯过道处，上下ap可以切换）上下ap不可切换，
						 * 强制避免其他位置楼层相串
						 * 
						 *//*
						
				         
//				        TODO210510大于-45(大于-53都可以直接进行更新，（存在特殊情况偶尔强放射信号值（大于-45）干扰，偶尔近邻跳动一次ap）
//						近邻可允许移动 )切换ap,小于-65ap，过滤掉（针对于ap较密集的地区） 
						
						
//						首次直接插入rece (不需要限制接受的信号值强度)
						if(count == 1) {
							re.setCreatetime(re.getUpdatetime());
							ibeaconMapper.persistence(re);
						}else{
							if(StringUtils.isEmpty(max_ap_Ele_Number)) {
								 TODO0519 异常进行修改
								 * ibdatas.parallelStream().forEach(rece -> {
									String rece_gatewaymac = rece.get("gatewaymac").toString();
									if(StringUtils.isEmpty(rece_gatewaymac)) {
										re.setGatewaymac(re.getGatewaymac());
									}else {
										re.setGatewaymac(rece_gatewaymac);
									}
								});
								
//								ap不进行切换，只更新时间
								for (Map map : ibdatas) {
									String rece_gatewaymac = map.get("gatewaymac").toString();
									if(StringUtils.isEmpty(rece_gatewaymac)) {
										re.setGatewaymac(re.getGatewaymac());
									}else {
										re.setGatewaymac(rece_gatewaymac);
									}
								}
								
							}else{
								
								try {
									
									re.setGatewaymac(max_ap_Ele_Number);
									re.setRssi(max_ap_Ele_rssi);
									
//									存入缓存（包含-65在内的缓存以及时间存入）
									moreThanTwo(re);
//									TODO0519返回连续次数最多或者缓存中存在次数最大的ap,(下面re值必须通过该处获取)
//									Received moreThanTwoCache_re = getMoreThanTwoCache(re);
									re = getMoreThanTwoCache(re);
									
									
									
//									筛选时间差小于7次（6*30【一分钟内前稳定值ap切换间隔时间】=180）秒，ap切换（小于-65rssi值过滤掉）
//									过滤掉小于-65数据 ，TODO210519异常：数据库中存在一般稳定ap重复连续短时间缓存 cache中，（1）时间段大于180，（2）时间段小于180
									if(re.getMac().equals("AC233FA432DA")) {
										
										LogUtil.logger.info(re+"=moreThanTwoCache_re");
									}
									
									
									
//									小于-65，进行过滤，防止穿楼层（部分拦截）
									int floor_filter = Integer.parseInt(max_ap_Ele_rssi);
									if(floor_filter<=ParameterField.floor_filter_rssi) {
//									    更新原有ap时间，不切换ap，防止超长时间离线情况,rssi值不进行更新，相同ap不更新只更新rssi值和时间
										re.setGatewaymac("");
										re.setRssi("");
									}else if(floor_filter>=ParameterField.ap_change_rssi){

//										超强ap直接进行更新ap,并且7次中连续两次以上或者7次中超过2次以上存在（异常现象，较远距离出现切换ap情况）
										
										re.setGatewaymac(max_ap_Ele_Number);
										re.setRssi(max_ap_Ele_rssi);
									}else {
										
//										TODO0519再次缓存数据队列中存放7次（替代mysql数据库查询，降低数据保存量），缓存上一数据不同于新数据，连续两次以上大于-65以上rssi 
//										并且缓存中楼层前、后楼层不一致情况切换仅有楼梯口位置（非楼梯口位置楼层切换，必须连续5次以上最强ap，以及该标签接受的ap楼层附近的最多为准确）
//										思考：缓存中连续ap次数达到7次中连续4次或（次数最多情况）提高准确度
										//通过所得ap去匹配redis中网关楼层更准确
										
//										(-45~-65)一般情况下更新实时定位数据表（通过历史记录分析knn）
										re.setGatewaymac(max_ap_Ele_Number);
										re.setRssi(max_ap_Ele_rssi);
									}
									

									
									
									
								} catch (Exception e) {
									// TODO: handle exception
									LogUtil.logger.error(e+"=e");
								}
								
								
								

							}
							
							
//							System.out.println("sql更新后Done in "+(System.nanoTime()-MqttHandler.start)/1_000_000+" ms"); //Done in 14 ms ==》0ms
							
							
							GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
							cf.completFutureUpdateRecevied(re);
							
//							持久化3个月标签路径数据TODO21-01-12,(每天每一个标签保存一次),根据路径历史数据可视化查看
//							插入和更新（获取表中已有楼层及ap，追加在其后==>先查询已有变更结果后更新）
							*//**
							 *通过历史记录获取大数据集中最后即最新并且最强rssi值ap（因为已存在多节点ap缓存，已经排除单个ap异常放电情况）
							 *一直异常放电最强ap（可忽略）， 由此可得较准确楼层，最强ap。
							 *
							 *但是移动效果需要处理（方案一：通过以数据集中获取最新最强ap，作出范围圆（该范围圆，应包括位置固定不变的情况下，数据集中包含圆内附近跳动ap（数据跳动ap）），如最新移动ap存在该圆内，不做移动处理。
							 *（楼梯处上下移动可排除，货物不堆放在楼梯口处），一旦超出该圆范围（即最新最后一部份数据集中（10-20个数据集）未包含在上一个ap定位位置附近所拥有的ap（数据跳动ap）），做出移动效果）
							 * 
							 * 
							 *
							 *//*

							
							String get0Time = times.get0Time();
							try {
								
								List<IbeaconHistory> ibeacon_History = cf.completFutureQueryHistoryIbeacon(re.getMac(), get0Time);
								IbeaconHistory ibeacon = new IbeaconHistory();
//								首次数据插入
								if(null == ibeacon_History || ibeacon_History.size() == 0 ) {
									String distanceByRssi = getDistanceByRssi(max_ap_Ele_rssi);
									
									ibeacon.setMac(re.getMac());
							    	ibeacon.setTimestamp(get0Time);
							    	ibeacon.setMaxRssiUpdateTime(re.getUpdatetime());
							    	ibeacon.setApRssiQueue(max_ap_Ele_rssi);
							    	
							    	ibeacon.setApChangeNum(1);
							    	ibeacon.setApMoveFloorQueue(max_ap_Ele_floor);
							    	ibeacon.setApMoveQueue(max_ap_Ele_Number);
									cf.completFutureInsertHistoryIbeacon(ibeacon);
								}else {
									
//									除首次外，历史记录中超过-65不进行数据记录
									int floor_filter = Integer.parseInt(max_ap_Ele_rssi);
									if(floor_filter<=ParameterField.floor_filter_rssi) {
										return ;
									}
									
									
								
								 * 
								 * 
								 * 数据更新
								 * 
								 * 
								 * TODO（sql数据查询，不能以字符串的数字大小比较，其顺序是以首字母大小排序，查询结果是错误的，时间排序除外，本地项目rssi同样除外，未出现个位数rssi值或三位数值，否则异常）
								 * TODO0507楼层数据与ap存在异常，需进行排查
								 * 
									for (IbeaconHistory ibeaconHistory : ibeacon_History) {
//										Integer num = Integer.parseInt(ibeaconHistory.getApChangeNum());
										
										Integer num = ibeaconHistory.getApChangeNum();
										num++;
										String apMoveFloorQueue = ibeaconHistory.getApMoveFloorQueue();
										String apMoveQueue = ibeaconHistory.getApMoveQueue();
										String apRssiQueue = ibeaconHistory.getApRssiQueue();
										String apMaxRssiTime = ibeaconHistory.getMaxRssiUpdateTime();
										
//										ap切换次数
//										ibeacon.setApChangeNum(num.toString());
										ibeacon.setApChangeNum(num);
//										插入楼层集合 
										ibeacon.setApMoveFloorQueue(apMoveFloorQueue+","+max_ap_Ele_floor);//通过所得ap去匹配redis中网关楼层更准确
//										切换的ap
										ibeacon.setApMoveQueue(apMoveQueue +","+max_ap_Ele_Number);
//										rssi改变
										ibeacon.setApRssiQueue(apRssiQueue+","+max_ap_Ele_rssi);
//										多节点时间集
										ibeacon.setMaxRssiUpdateTime(apMaxRssiTime+","+re.getUpdatetime());
										
										ibeacon.setMac(re.getMac());
										ibeacon.setTimestamp(get0Time);//时间节点条件
										
										
										
										List<String> listByStr = getListByStr(apMoveQueue);
										String new_ap= listByStr.get(listByStr.size()-1);
										
//									     数据库ap与现有ap一致，不记录
										if(new_ap.equals(max_ap_Ele_Number)) {
											return;
										}
										
										
									}
									cf.completFutureUpdateHistoryIbeacon(ibeacon);
									
								}
								
								
							} catch (Exception e) {
								// TODO: handle exception
								LogUtil.logger.error(e.toString()+"添加数据异常");
							}
							
							
							
						}
						
	
					    
//						TODO1106 待做间隔保存3-5分钟保存一次路径数据
//						System.out.println("sql最终更新后Done in "+(System.nanoTime()-MqttHandler.start)/1_000_000+" ms"); //Done in 14 ms ==》0ms
						
						
				} catch (Exception e) {
					// TODO: handle exception
					LogUtil.logger.error("ap切换异常"+e);
				}
					
					
			
				
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("ibeaconGatewayserviceimpl插入异常="+e);
		}
		
		
//		====数据修改开始
//		更新网关数据后，同时更新资产所在房间位置 08-11 17:52   耗时0.03 
		
		 * 优化添加索引以及替代in 使用EXISTS
		
		一级缓存ap所有数据，访问缩短时间
		通过mac匹配ap相关数据
		
		
		
		
		
//		获取设备对应的ap相关信息
		List<Map> searchRG = reService.searchRG(re.getMac());
//		List<Map> lmap = rec.completFutureQueryRecevied(re.getMac()); 异步查询
		
		String location = "";
//		动态设备更新楼层
		String floor = "";
		String cadMapRoomName = "";
		String check = "";
		String department = "";
		try {
			if(searchRG != null && searchRG.size()>0) {
				for (Map map : searchRG) {
					location = (String) map.get("location");
					floor = (String)map.get("floor");
					cadMapRoomName = (String)map.get("cadMapRoomName");
//					更新房间时间，用于盘点,空值不进行更新
					check = (String)map.get("updatetime");
					
					department = (String)map.get("department");
					
					
					
					Asset asset = new Asset();
					
			    	asset.setMac(re.getMac());
			    	asset.setLocation(location);
			    	asset.setFloor(floor);
			    	asset.setCadMapRoomName(cadMapRoomName);
			    	//记录设备切换ap的时间（盘点离线设备2分钟必须大于此处ap更新1分钟的时间）
			    	asset.setCheck(check);
//			    	电子围栏,部门跨界报警(department实时所在位置与所在locdept规定所在位置进行比较)
			    	asset.setDepartment(department);
			    	
//			    	更新电量
			    	asset.setElectric(re.getBattery());
//			    	asset.setStatus();  设置维修接单状态，显示位于医工科
			    	
//			    	更新资产实时路径位置
			    	assetService.updateLocation(asset);
			    	
			    	
//			    	缓存资产定位数据，避免点位实现时消失
			    	GuavaAssetData gad = new GuavaAssetData();
			    	
			    	Map location_real_datas = new HashMap<>();
			    	location_real_datas.put("location", location);
			    	location_real_datas.put("floor", floor);
			    	location_real_datas.put("cadMapRoomName", cadMapRoomName);
			    	location_real_datas.put("department", department);
			    	gad.putCache(re.getMac(), location_real_datas);
			    	
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("IbeaconGatewayServiceImpl=>location==>error==>ap信息不完整导致一级缓存存放异常"+e);
		}
//		System.out.println("更新后Done in "+(System.nanoTime()-MqttHandler.start)/1_000_000+" ms"); //Done in 14 ms ==》0ms
		
		
	}
	
	
	
	
	
//	单位时间数据区获取楼层及其属性(map超过单位时间不保存)
	private void datasGetFloor(Received re,String time,List<String> ls) {
		timeUtiles times = new timeUtiles();
//		线程安全
    	Map cache_lq_map = (Map)glfomc.getCache(re.getMac());
    	
    	LimitQueue limit_cache = (LimitQueue)cache_lq_map.get("limit");
    	
//    	队列创建
    	LimitQueue<String> lq = new LimitQueue<>();
    	
//	    首次添加
    	if(null == limit_cache || limit_cache.size()==0) {
    		for (String string : ls) {
    			lq.offer(string);
			}
    	}else{
//    		单位时间内ap缓存队列，提高稳定性
    		String tim = (String)cache_lq_map.get("time");
    		
    		if(times.getCurrenttime().equals(tim)) {
    			return;
    		}
    		
//    		更新ap队列
    		Queue queue = limit_cache.getQueue();
    		for (Object object : queue) {
    			lq.offer(object.toString());
			}
    		
    		for (String string : ls) {
    			lq.offer(string);
			}
    	}
    	
    	
    	Map map_lq = new HashMap();
    	map_lq.put("limit", lq);
    	map_lq.put("time", times.getCurrenttime());
//    	更新缓存
    	glfomc.putCache(re.getMac(), map_lq);
	    
    	
    	
//    	获取缓存中队列数据
    	Map new_cache_lq_map = (Map)glfomc.getCache(re.getMac());	
    	LimitQueue rece_lq = (LimitQueue)new_cache_lq_map.get("limit");
    	
    	if("AC233FB100EE".equals(re.getMac())) {
//    		获取单个队列属性时间
    		LogUtil.logger.info(rece_lq.getQueue()+"=更新后获取队列中数据"+ls+"=更新前获取队列中数据");
    	}
	}
	
	
//	最终定位ap，进行圆内范围计算(三圆定位)
	private void circlePotion(Received re,String new_maxelenumber_gatewaymac,List l) {
		IbeaconGateway ib = new IbeaconGateway(); 
		ib.setMac(re.getMac());
		ib.setGatewaymac(re.getGatewaymac());
//		获取持久化数据
		List<Map> ibdatas = ibeaconMapper.findGatewayByMac(ib);
		
		Long collect= ibdatas.parallelStream().filter(mapdatas -> {
			return  (Long)mapdatas.get("count(*)") <= 0;
		}).count();
		GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
		
		
		if(collect == 1) {
			re.setGatewaymac(new_maxelenumber_gatewaymac);
			cf.completFutureUpdateRecevied(re);
		}else{
			ibdatas.stream().map(gateMap -> {
				
				String recevied_gatewaymac = (String) gateMap.get("gatewaymac"); //数据库中原有ap

//				如果re在l中不更新
				if(!StringUtils.isEmpty(recevied_gatewaymac) && (recevied_gatewaymac.equals(new_maxelenumber_gatewaymac)) 
						|| l.contains(recevied_gatewaymac)) {
					re.setGatewaymac(recevied_gatewaymac);
					cf.completFutureUpdateRecevied(re);
				}else {
//					存入数据库
//					如果该点位离ap较近不更新
					Map<Object, Object> rece_gate_map = redisUtil.hmget(recevied_gatewaymac);
					Gateway gate_map = (Gateway) rece_gate_map.get("gate");
					if(!StringUtils.isEmpty(new_maxelenumber_gatewaymac) && (null != gate_map || !StringUtils.isEmpty(gate_map.getMapx()))) {
						
						
	//				圆心基点ap
							double circle_center_mapx = Double.parseDouble(gate_map.getMapx());
							double circle_center_mapy = Double.parseDouble(gate_map.getMapy());
//							最后10秒左右在更新
							
							Map<Object, Object> newMax_gate_map = redisUtil.hmget(new_maxelenumber_gatewaymac); //新传入最大值ap
							Gateway new_gate_map = (Gateway) newMax_gate_map.get("gate");
							double circle__mapx = Double.parseDouble(new_gate_map.getMapx());
							double circle__mapy = Double.parseDouble(new_gate_map.getMapy());
									
//														如果在内，不随意进行ap切换
							boolean circle = getCircle(UserParameterField.cirle,circle_center_mapx,circle_center_mapy,circle__mapx,circle__mapy);
//							园内不更新
							if(circle) {
								re.setGatewaymac(recevied_gatewaymac);
								cf.completFutureUpdateRecevied(re);
								
//							存在超距离更新
							}else {
								re.setGatewaymac(new_maxelenumber_gatewaymac);
								cf.completFutureUpdateRecevied(re);
							}
							
					}
					
				}
				
				return -1;
			}).collect(Collectors.toList());

		}
	}
	

//	圆心画图
	private  boolean getCircle(double r,double centerX1,double centerY1,double x2,double y2){
	    double distance=Math.sqrt((y2-centerY1)*(y2-centerY1)+(x2-centerX1)*(x2-centerX1));
	    if(distance>r){
	        return false;
	    }else {
	    	return true;	    	
	    }
   }
//	只有三个缓存，ap间切换
	private void three_cache_Change(Map max_1cache_rssi,Map max_2cache_rssi,Map max_3cache_rssi,Received re) {
		GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
//		排除三个都为{}的情况
		if(max_1cache_rssi.size()>0) {
//			含有2个及两个以上相同最强ap及其rssi
//			通过一级指向快速切换点位以及多个相同
			if(max_3cache_rssi.containsValue(max_1cache_rssi.get("gatewaymac")) || max_2cache_rssi.containsValue(max_1cache_rssi.get("gatewaymac")) 
					|| max_3cache_rssi.size()==0){
				re.setGatewaymac(max_1cache_rssi.get("gatewaymac").toString());
				re.setRssi(max_1cache_rssi.get("maxRssi").toString());
//			三个都不相同或者2和1相同以及1,3不同，2为空，皆以最新为切换点
			}else{
				re.setGatewaymac(max_3cache_rssi.get("gatewaymac").toString());
				re.setRssi(max_3cache_rssi.get("maxRssi").toString());
			}
		}
		cf.completFutureUpdateRecevied(re);
	}

	@Override
	public List<Map> findReceviedByMac( String mac) {
		// TODO Auto-generated method stub
		List recevied = ibeaconMapper.findRecevied(mac);
		return recevied;
	}
	@Override
	public List<Map> findReceviedByMac(String gatewaymac, String mac) {
		// TODO Auto-generated method stub
		List recevied = ibeaconMapper.findRecevied(gatewaymac,mac);
		return recevied;
	}

	@Transactional
	@Override
	public void updateGatewayTime(String gatewaymac,String currenttime,String gateway) {
		// TODO Auto-generated method stub
		ibeaconMapper.updateGatewayMac(gatewaymac,currenttime,gateway);
	}


	@Override
	public List<Map> findMissing(String currentime) {
		// TODO Auto-generated method stub
		List<Map> findmissing = ibeaconMapper.findmissing(currentime);
		return findmissing;
	}

	
	@Override
	public List<Map> findGatewayOline() {
		// TODO Auto-generated method stub
		List<Map> gateway = ibeaconMapper.findGateway();
		return gateway;
	}


	@Override
	public List<Map> findGatewayOlineByTime(String date) {
		// TODO Auto-generated method stub
		List<Map> gateway = ibeaconMapper.findGatewayByTime(date);
		return gateway;
	}


	@Override
	public List<Map> searchLastDatas() {
		// TODO Auto-generated method stub
		List<Map> datas = ibeaconMapper.searchLastData();
		return datas;
	}


	@Override
	public void updateRssiAndTime(String mac,
			 String gatewaymac,String oldmaxRssiUpdateTime,
			 String rssi,String distance
			,String maxRssiUpdateTime) {
		// TODO Auto-generated method stub
		ibeaconMapper.updateRssiAndTime( mac,
				  gatewaymac, oldmaxRssiUpdateTime,
				  rssi, distance
				, maxRssiUpdateTime);
	}


	@Override
	public List<Map> findmacByGatewayMac(String mac, String gatewaymac, String endTime, String currentTime) {
		// TODO Auto-generated method stub
		List<Map> datas = ibeaconMapper.findMacBytime(mac,gatewaymac,endTime,currentTime);
		return datas;
	}


	@Override
	public List<Map> findmaxRssiByMac(String mac,String startTime,String currentTime) {
		// TODO Auto-generated method stub
		List<Map> datas = ibeaconMapper.findMaxNewGateway(mac,startTime,currentTime);
		return datas;
	}


	@Override
	public List<Map> findIbGatewayByMac(IbeaconGateway ibeacon) {
		// TODO Auto-generated method stub
		List<Map> ibdatas = ibeaconMapper.findGatewayByMac(ibeacon);
		return ibdatas;
	}
	
	
//	获取最大值rssi网关所在
	private Map getGatewaymacByRssiMax(Map hmget,String mac) {

		Map map1 = new HashMap();
		if(null != hmget && hmget.size()>0) {
			Set<Object> keySet=hmget.keySet();
//			设定无限最低值
			int rssi_max = -1000000;
//			Map map1 = new HashMap();
//			map遍历，获取最大rssi
			for(Object string:keySet){
				if("expressTime".equals(string)) {
					continue;
				}
				Object object = hmget.get(string);
				Map map = (Map)object;
				String redis_rssi = (String)map.get("rssi");
				
				
//				以获取最近的更新时间在10秒之内，并且rssi最大
				String redis_currentTime = (String)map.get("currentTime");
				timeUtiles time_ = new timeUtiles();
				String currenttime = time_.getCurrenttime();
				String time_10 ="";
				try {
					time_10 = time_.timeReduce(currenttime, 10);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(redis_currentTime.compareTo(time_10.toString())<0) {
					if("AC233FB100EE".equals(mac)) {
						
						LogUtil.logger.info(hmget+"=hmget");
					}
					continue;
				}
				
				
				try {
					int reNewRssi = Integer.parseInt(redis_rssi);
					if(reNewRssi>rssi_max) {
						rssi_max = reNewRssi;
						map1 = map;
					}
				} catch (Exception e) {
					// TODO: handle exception
					LogUtil.logger.info("redis_rssi大小比较异常:"+e);
				}
			}
			String maxRssi = rssi_max+"";
			String gatewaymac = "";
			Set<Object> keysByStream = getKeysByStream(hmget,map1);
			for (Object object : keysByStream) {
				 gatewaymac = (String)object;
			}
			Map gateway_map = new HashMap();
			gateway_map.put("gatewaymac", gatewaymac);
			gateway_map.put("maxRssi", maxRssi);
			
			return gateway_map;
		}
		
		return map1;
		
	}
	private <K, V> Set<K> getKeysByStream(Map<K, V> map, V value) {
		  return map.entrySet()
		    .stream()
		    .filter(kvEntry -> Objects.equals(kvEntry.getValue(), value))
		    .map(Map.Entry::getKey)
		    .collect(Collectors.toSet());
	}
	
//	TODO0310==华西待修改==过滤下一秒时间
	private void filterTime(Received re,timeUtiles times) {
		 
	   Map cache_fc_map = (Map)glfc.getCache(re.getMac());
	   Map map_lq = new HashMap();
	   String floor_timeAdd_limit ="";
		try {
			floor_timeAdd_limit = times.timeAdd(times.getCurrenttime(), UserParameterField.TIME_EXP_CACHE);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LogUtil.logger.error("IbeaconGatewayServiceImpl时间过滤异常"+e);
		}
  	   if(null !=cache_fc_map && !cache_fc_map.isEmpty() ) { 
  		   
  		   String tim = (String)cache_fc_map.get("time");
  		   if(times.getCurrenttime().compareTo(tim)>0) {
			       map_lq.put("time", floor_timeAdd_limit);
  		   }else {
  			   return;
  		   }
  		   
  	   }else {
		       map_lq.put("time", floor_timeAdd_limit);
  	   }
  	   glfc.putCache(re.getMac(), map_lq);
   }
	
	private Integer filterTime(Received re,timeUtiles times) {
		
		Map cache_fc_map = (Map)glfc.getCache(re.getMac());
		Map map_lq = new HashMap();
		String floor_timeAdd_limit ="";
		try {
			floor_timeAdd_limit = times.timeAdd(times.getCurrenttime(), UserParameterField.TIME_EXP_CACHE);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LogUtil.logger.error("IbeaconGatewayServiceImpl时间过滤异常"+e);
		}
		if(null !=cache_fc_map && !cache_fc_map.isEmpty() ) { 
			
			String tim = (String)cache_fc_map.get("time");
			if(times.getCurrenttime().compareTo(tim)>0) {
				map_lq.put("time", floor_timeAdd_limit);
			}else {
				return -1;
			}
			
		}else {
			map_lq.put("time", floor_timeAdd_limit);
		}
		glfc.putCache(re.getMac(), map_lq);
		return 0;
	}
	
//	获取队列中内连续两次最强
	private void moreThanTwo(Received re) {
		
		Map more_Than_Two = (Map)moreThanTwo.getCache(re.getMac());
		
		LimitQueue<Map> limit_cache = (LimitQueue)more_Than_Two.get("morenthantwo");
    	
//    	队列创建 //TODO21-01-14 后台数据库设置队列大小，方便华西内网调试
    	LimitQueue<Map> lq = new LimitQueue<>(ParameterField.lqSize_moreThanTwo);
    	
    	Map map = new HashMap<>();
    	map.put("ap", re.getGatewaymac());
    	map.put("time", re.getUpdatetime());
    	map.put("rssi", re.getRssi());
    	
    	
//	    首次添加
    	if(null == limit_cache || limit_cache.size()==0) {
    		lq.offer(map);
//    		鉴定缓存中容量超载自动清除数据，LogUtil.logger.info("map="+map+"=limit_cache.size="+limit_cache+"=RE.MAC="+re.getMac());
    		
    	}else{
//    		更新ap队列
    		Queue<Map> queue = limit_cache.getQueue();
    		for (Map object : queue) {
    			lq.offer(object);
			}
	    	lq.offer(map);
	    	
    	}
    	
    	Map map_lq_limit = new HashMap();
    	map_lq_limit.put("morenthantwo", lq);
    	
//    	更新缓存
    	moreThanTwo.putCache(re.getMac(), map_lq_limit);
    	
	}
//	楼层处理
	private Received getMoreThanTwoCache(Received re) {
    	
    	Map more_Than_Two = (Map)moreThanTwo.getCache(re.getMac());
    	LimitQueue<Map> limit_cache = (LimitQueue)more_Than_Two.get("morenthantwo");
    	
    	
    	Queue<Map> queue = limit_cache.getQueue();
//    	2021-05-19 14:03:32 [INFO] [{rssi=-32, time=2021-05-19 14:02:22, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:32, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:42, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:52, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:02, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:12, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:32, ap=AC233FC08502}]=queue
    	
    	String time_start;
    	String time_end;
    	List ls = new ArrayList<>();
    	for (Map map : queue) {
    		
    		String rssi_ = (String) map.get("rssi");
//    		String time_ = (String) map.get("time");
    		String ap_ = (String) map.get("ap");
    		ls.add(ap_);
		}
    	
    	
    	JsonUtils js = new JsonUtils();
    	Map map_max_ap = js.getMaxContinuousEle(ls);
//    	如果连续次数小于该值，则只返回更新时间
    	if((int)map_max_ap.get("max_num") >= ParameterField.ap_continue_num) {
    		
    		re.setGatewaymac((String)map_max_ap.get("max_ap_ele"));
    		
    		String ap_ = "";
    		String rssi_ = "";
    		String time_ = "";
    		for (Map map : queue) {
    			ap_ = (String) map.get("ap");
    			if(ap_.equals(re.getGatewaymac())) {
    				rssi_ = (String) map.get("rssi");
    				time_ = (String) map.get("time");
    				re.setRssi(rssi_);
    				return re;
    			}
    		}
    	}else{
//    		TODO0519异常：待处理短时间多次切换ap情况，此处包含处理首次队列定位ap
    		
    		String ap = js.getMaxEleNumber(ls);
    		re.setGatewaymac(ap);
    		
    		String ap_ = "";
    		String rssi_ = "";
    		String time_ = "";
    		for (Map map : queue) {
    			ap_ = (String) map.get("ap");
    			if(ap_.equals(re.getGatewaymac())) {
    				rssi_ = (String) map.get("rssi");
    				time_ = (String) map.get("time");
    				re.setRssi(rssi_);
    				return re;
    			}
    		}
    	}
    	
		return re;
	}
	
	
//  单位时间片获取最强，（自调节）多节点时间返回最稳定ap
	private void statApTimeSlice(LimitQueue<Map> lq_slice,timeUtiles times,Received re) {
		List list_slice = lq_slice.stream().filter(fap -> ((String)fap.get("mac")).equals(re.getMac())).map( ap -> {
			  return ap;
		 }).collect(Collectors.toList());
//		线程安全
    	Map cache_lq_map = (Map)gssc.getCache(re.getMac());
    	
    	LimitQueue<List<Map>> limit_cache = (LimitQueue)cache_lq_map.get("limit");
    	
//    	队列创建 //TODO21-01-14 后台数据库设置队列大小，方便华西内网调试
    	LimitQueue<List<Map>> lq = new LimitQueue<>(ParameterField.lqSize);
    	
//	    首次添加
    	if(null == limit_cache || limit_cache.size()==0) {
    		lq.offer(list_slice);
    	}else{
//    		同一秒不进行更新，提高稳定性
    		String tim = (String)cache_lq_map.get("time");
    		if(times.getCurrenttime().equals(tim)) {
    			return;
    		}
    		
//    		更新ap队列
    		Queue<List<Map>> queue = limit_cache.getQueue();
    		for (List<Map> object : queue) {
    			lq.offer(object);
			}
	    	lq.offer(list_slice);
    	}
    	
    	Map map_lq_limit = new HashMap();
    	map_lq_limit.put("limit", lq);
    	map_lq_limit.put("time", times.getCurrenttime());
//    	更新缓存
    	gssc.putCache(re.getMac(), map_lq_limit);
    	
//    	获取缓存中队列数据
    	Map new_cache_lq_map = (Map)gssc.getCache(re.getMac());
    	LimitQueue rece_lq = (LimitQueue)new_cache_lq_map.get("limit");
    	
    	
//    	2021-05-19 14:03:32 [INFO] [{rssi=-32, time=2021-05-19 14:02:22, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:32, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:42, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:52, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:02, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:12, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:32, ap=AC233FC08502}]=queue
    	if(re.getMac().equals("AC233FA432DA")) {
    		Queue queue = rece_lq.getQueue();
    		
    		LogUtil.logger.info(queue+"=queue-稳定值中队列");
    	}
    	
    	if((re.getMac().equals("AC233FB100EE"))) {
			LogUtil.logger.info("=queue===============================queue多节点重新开始"+rece_lq.getQueue());
		}
    		
    		
    		Queue<List<Map>> queue = rece_lq.getQueue();
    		
    		String mulite_ap_max = "";
    		Integer mulite_ap_rssi = -10000;
    		String mulite_ap_floor = "";
    		
    		
    		LimitQueue<Map> ap_rssi_queue = new LimitQueue<>(ParameterField.lqCacheSize);
    		
    		
    		
    		Map map_max = new LinkedHashMap();
    		
    		Map lq_map_max = new LinkedHashMap();//缓存队列map
    		
    		
			Integer ap_slice_num = 0;//统计切片ap出现次数
			List ls_floor = new ArrayList<>();
			
			
			Integer ap_slice_max_rssi = -10000;//多节点队列最强ap
    		String ap_slice_max = "";
    		String ap_slice_max_floor = "";//多节点队列最强ap楼层
    		
//    		连续出现4次及以上ap所在楼层即为所在楼层，如果单节点突然有放电变强的情况，必须满足楼层要求才可切换楼层
    		
    		
//    		List ls_rssi = new ArrayList<>();//提高时效性，以三个rssi平均值和原有ap进行比较（15-20单位）
    		
//    		多节点
    		for (List<Map> object : queue) {
    			
    			Map map = new LinkedHashMap();
    			Integer single_ap_max_rssi = -10000;
    			String single_ap_max = "";
    			String floor = "";
    			
//    			单时间切片节点
    			for (Map max_rssi_ap : object) {
    				
    				if(null == max_rssi_ap || max_rssi_ap.size() == 0 || StringUtils.isEmpty(max_rssi_ap.get("rssi").toString())) {
    					continue;
    				}
    				
    				Integer ap_rssi = Integer.parseInt(max_rssi_ap.get("rssi").toString());
    				
    				if(single_ap_max_rssi<ap_rssi) {
    					
    					single_ap_max_rssi = ap_rssi;
    					single_ap_max = max_rssi_ap.get("gatewaymac").toString();
    					floor = max_rssi_ap.get("floor").toString();
    					
    					
    					map.put("single_ap_max_rssi", single_ap_max_rssi);
    					map.put("single_ap_max", single_ap_max);
    					map.put("floor", floor);
    				}
    				
				}
    			
    			
    			
    			
//    			2021-01-23 15:06:58 [INFO] =========================={single_ap_max_rssi=-41, single_ap_max=AC233FC08509, floor=5}
//    			单个节点最强rssi
//    			LogUtil.logger.info("=========================="+map.toString()+"最大单片时间节点ap"+single_ap_max);
    			
    			
    			
    			Integer mu_ap_rssi = Integer.parseInt(map.get("single_ap_max_rssi").toString());
    			String mu_ap_floor = map.get("floor").toString();
    			ls_floor.add(mu_ap_floor);//楼层放入集合中

    			
//    			每次节点更新一次获取一次缓存，否则缓存数据不准确,不及时
    			Map<String,LimitQueue<Map>> ap_rssi_lq = (Map)gmac.getCache(re.getMac());
    			
//    			首次标签添加缓存
    			if(null == ap_rssi_lq || ap_rssi_lq.size() == 0) {
    				lq_map_max.put("mulite_ap_max",single_ap_max);
    				lq_map_max.put("mulite_ap_rssi",single_ap_max_rssi);
    				lq_map_max.put("mulite_ap_floor",floor);
    				
    				ap_rssi_queue.offer(lq_map_max);
    				
    				map_max.put("muliteap", ap_rssi_queue);
    				
    				
					gmac.putCache(re.getMac(), map_max);
    				
    			}
    			
    			
//    			已有该标签缓存，首次中的缓存还未存在，不向下执行
    			
    			if(null != ap_rssi_lq && ap_rssi_lq.size()>0) {
    				
    				
    				 LimitQueue<Map> limitQueue = ap_rssi_lq.get("muliteap");
    				 
//    				获取缓存中已有最强可浮动ap及rssi值及楼层
    				for (Map map_ap_rssi : limitQueue.getQueue()) {
						mulite_ap_rssi = Integer.parseInt(map_ap_rssi.get("mulite_ap_rssi").toString());
						mulite_ap_max =map_ap_rssi.get("mulite_ap_max").toString();
						mulite_ap_floor =map_ap_rssi.get("mulite_ap_floor").toString();
						
					}
//    				统计次数，排除掉不稳定ap。处理路径移动
    				if(single_ap_max.equals(mulite_ap_max)) {
    					ap_slice_num ++;
    				}
    				
//    				获取多节点中最强ap,作为移动后（或ap异常放电，不稳定）ap位置
    				if(ap_slice_max_rssi < single_ap_max_rssi) {
    					ap_slice_max_rssi = single_ap_max_rssi;
    					ap_slice_max = single_ap_max;
    					ap_slice_max_floor = floor;
    				}
    				
    				
    				
    				
    				 * 
    				 * 从缓存中获取ap以及对应的rssi值与每个单节点进行比较上下5个单位（容许队列长度队的空值，即无信号），超过则切换节点ap
    				 * rss值为理想标签所在位置的到各标签的距离，该值为平均距离值(各ap与标签之间距离稳定值)
    				 * 
        	
    				
//    				如果是相同ap,当衰减因子小于等8-10则必须切换ap（如果是自身ap，更新最新最小的rssi值即可）
    				if(single_ap_max.equals(mulite_ap_max) && mulite_ap_rssi >= single_ap_max_rssi+UserParameterField.RSSI_tolerances_low) {
    					
    					gmac.putCache(re.getMac(), map_max);//清空原有缓存，存放原ap的rssi最新小值
    					
//    					放入缓存中
//    					==========================
    					
        				lq_map_max.put("mulite_ap_max",single_ap_max);
        				lq_map_max.put("mulite_ap_rssi",single_ap_max_rssi.toString());
        				lq_map_max.put("mulite_ap_floor",floor);
        				
//        				获取原有队列中数据，并且更新
        				
        				ap_rssi_queue.offer(lq_map_max);
        				
    					map_max.put("muliteap", ap_rssi_queue);
//    					LogUtil.logger.info("更新同ap缓存数据"+lq_map_max);
    					
    					gmac.putCache(re.getMac(), map_max);
    				}

    				
    				
    				
//    				获取多节点中最强rssi值，作为焦点浮动ap（效果：切换ap后逐渐加强自身ap的rssi,上一条件逻辑顺序不可调）
//    				未移动的情况下。楼层与缓存中楼层必须是一致的。(防止穿楼层现象)，只允许同楼层间的交互
        			if(mulite_ap_rssi + UserParameterField.RSSI_tolerances < mu_ap_rssi && mulite_ap_floor.equals(mu_ap_floor)) {
        				
        				
        				
//        				如果是不同ap，清空原有缓存，更新ap和rssi值，并且该ap所处楼层占队列长度一半以上
        				if(!single_ap_max.equals(mulite_ap_max)) {
//        					清空原有缓存
        					gmac.putCache(re.getMac(), map_max);
        					
        				}
        				
//        				待做 ====》相同ap情况下，逐渐变强rssi值（注意楼层控制）
    					
        				lq_map_max.put("mulite_ap_max",single_ap_max);
        				lq_map_max.put("mulite_ap_rssi",single_ap_max_rssi.toString());
        				lq_map_max.put("mulite_ap_floor",floor);
        				
//        				获取原有队列中数据，并且更新
        				
        				ap_rssi_queue.offer(lq_map_max);
        				
    					map_max.put("muliteap", ap_rssi_queue);
    					
//    					LogUtil.logger.info("更新缓存更强ap数据"+lq_map_max);
    					
    					gmac.putCache(re.getMac(), map_max);
        				
    					
    					
    				}
        			
        			
        			
    			}
		
    			
			}
    		
			
    		
    		 * 如果缓存中存在的ap在多节点队列中存在数量小于1（连续队列长度以上没有收到该ap信号单切片最强数据，
    		 * 该ap可能不稳定或者标签进行移动）， 则切换ap
    		 * 
//    		缓存已经存在,非首次缓存,并且已有单切片最大值，则切换ap
//			单切点一半以上最强为楼层最终位置（11楼换衣处暂时不考虑特殊位置）,至少楼层集合中以大多数未准
    		
    		JsonUtils js = new JsonUtils();
    		String maxEleNumber = js.getMaxEleNumber(ls_floor);
    		
    		 *稳定情况下， 首次队列长度存满,位置放入缓存，以大多数楼层及rssi值为焦点，降低楼层来回穿梭，以及降低初始位置楼层偏差
    		（特殊情况，该楼层ap数量较少情况，只能以rssi强为基准，楼层可能出现偏差）
    		
    		
    		
    		
//    		校准楼层，首次队列长度减一满足缓存要求，以最强为准,如rssi<=45，后续波动较小，并且队列中包含两个及以上最强ap
			if(queue.size() == ParameterField.lqSize - 1 && ap_slice_max_rssi >= ParameterField.floorRssi && ap_slice_num>=2)  {
				
				lq_map_max.put("mulite_ap_max",ap_slice_max);
				lq_map_max.put("mulite_ap_rssi",ap_slice_max_rssi.toString());
				lq_map_max.put("mulite_ap_floor",ap_slice_max_floor);
				ap_rssi_queue.offer(lq_map_max);
				map_max.put("muliteap", ap_rssi_queue);
//				LogUtil.logger.info("首满更新"+lq_map_max);
				gmac.putCache(re.getMac(), map_max);
				
			}
    		
    		
    		
    		
//    		LogUtil.logger.info(maxEleNumber+"===最大楼层值");//以结果值为准，该值目的用来楼层间移动（已有缓存队列之后执行）以及及时更新最新移动
			
			TODO-03-17
			 * 2021-03-16 17:17:43 [INFO] AC233FB100EE=mulite_ap_max缓存中存在数据[{mulite_ap_max=AC233FC08502, mulite_ap_rssi=-52, mulite_ap_floor=b2}]
					2021-03-16 17:17:53 [INFO] =========================={single_ap_max_rssi=-48, single_ap_max=AC233FC07217, floor=b2}最大单片时间节点apAC233FC07217
					2021-03-16 17:17:53 [INFO] =========================={single_ap_max_rssi=-49, single_ap_max=AC233FC07217, floor=b2}最大单片时间节点apAC233FC07217
					2021-03-16 17:17:53 [INFO] =========================={single_ap_max_rssi=-51, single_ap_max=AC233FC07217, floor=b2}最大单片时间节点apAC233FC07217
					2021-03-16 17:17:53 [INFO] =========================={single_ap_max_rssi=-51, single_ap_max=AC233FC07217, floor=b2}最大单片时间节点apAC233FC07217
					2021-03-16 17:17:53 [INFO] =========================={single_ap_max_rssi=-49, single_ap_max=AC233FC07217, floor=b2}最大单片时间节点apAC233FC07217
					2021-03-16 17:17:53 [INFO] =========================={single_ap_max_rssi=-60, single_ap_max=AC233FC08509, floor=2}最大单片时间节点apAC233FC08509
					2021-03-16 17:17:53 [INFO] =========================={single_ap_max_rssi=-50, single_ap_max=AC233FC07217, floor=b2}最大单片时间节点apAC233FC07217
					2021-03-16 17:17:53 [INFO] 移动（取缔不稳地）时更新ap缓存数据{mulite_ap_max=AC233FC07217, mulite_ap_rssi=-48, mulite_ap_floor=b2}
					2021-03-16 17:17:53 [INFO] AC233FB100EE=mulite_ap_max缓存中存在数据[{mulite_ap_max=AC233FC07217, mulite_ap_rssi=-48, mulite_ap_floor=b2}]
					2021-03-16 17:17:53 [INFO] 6==count
			*
			*前提条件是ap在单位时间内规律性进行切换，通过数据库中已有数据切换ap（造成标签受ap接受信号时有时无（连续七次无该ap对应标签信号接受），因而不断来回跳动）
			
			if(ap_slice_num < 1 && !StringUtils.isEmpty(ap_slice_max) && ap_slice_max_floor.equals(maxEleNumber)) {
				gmac.putCache(re.getMac(), map_max);//清空原有缓存，存放多节点最强ap
//				放入缓存中
//				==========================
				lq_map_max.put("mulite_ap_max",ap_slice_max);
				lq_map_max.put("mulite_ap_rssi",ap_slice_max_rssi.toString());
				lq_map_max.put("mulite_ap_floor",ap_slice_max_floor);
				ap_rssi_queue.offer(lq_map_max);
				map_max.put("muliteap", ap_rssi_queue);
//				LogUtil.logger.info("移动（取缔不稳地）时更新ap缓存数据"+lq_map_max);
				
				gmac.putCache(re.getMac(), map_max);
				
			}
			
    		
    		
    		
    		
    		
//  待删除  		查看缓存中存在存在数据
//			统计变更次数
    		Map<String,LimitQueue<Map>> ap_rssi_lqtest = (Map)gmac.getCache(re.getMac());
    		
			 if(null == ap_rssi_lqtest || ap_rssi_lqtest.size()== 0) {
				 return;
			 }
					 
			 LimitQueue<Map> limitQueue = ap_rssi_lqtest.get("muliteap");
				
				LogUtil.logger.info(re.getMac()+"=mulite_ap_max"+"缓存中存在数据"+limitQueue.getQueue());
				

				String newap = "";
				Queue<Map> api = limitQueue.getQueue();
				for (Map map : api) {
					newap = map.get("mulite_ap_max").toString();
				}
				
				if(!newitest.equals(newap) && !StringUtils.isEmpty(newap)) {
					counttest++;
					LogUtil.logger.info(counttest+"==count");
					newitest = newap;
				}
    	
	}
//	通过rssi获取距离
	public String getDistanceByRssi(String rssi){
    	double rs = Double.parseDouble(rssi);
    	double Rssi = Math.abs(rs);
    	double power = (Rssi - 50) / (10.0 * 3.3);
    	String location=String.valueOf(Math.pow(10, power));
    	String distance = location.substring(0,3);
    	return distance;
    }
//	字符串转为集合
	private  List<String> getListByStr(String str){
		List<String> list = Arrays.asList(str.split(","));
		List<String> newlist = new ArrayList<String>(list);
		return newlist;
	}
}
*/
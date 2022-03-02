package com.example.demo.service.impl;

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

import com.example.demo.cache.GuavaApInCircleCache;
import com.example.demo.cache.GuavaApInCircleChangeCache;
import com.example.demo.cache.GuavaApRealCache;
import com.example.demo.cache.GuavaApRealFloorCache;
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
public class IbeaconGatewayServiceImpl implements IbeaconGatewayService{
	
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
	
	private GuavaApRealFloorCache grfl = new GuavaApRealFloorCache();//刷选真实楼层
	
	private GuavaApRealCache gaf = new GuavaApRealCache();//刷选真实ap
	
	private GuavaApInCircleCache gaicc = new GuavaApInCircleCache();//校准点范围内缓存的ap
	
	private GuavaApInCircleChangeCache newap = new GuavaApInCircleChangeCache();//最新最强连续ap缓存
	
	private GuavaStatSliceCache gssc = new GuavaStatSliceCache();//缓存多个队列切片
	
	private GuavaMoreThanTwoCache moreThanTwo = new GuavaMoreThanTwoCache();//刷选连续多个最强rssi值
	
	private GuavaLimitFloorCache glfc = new GuavaLimitFloorCache();//缓存标签接近真实楼层,过滤下一秒时间
	
//	private GuavaLimitFloorOneMiuteCache glfomc = new GuavaLimitFloorOneMiuteCache();//缓存标签一分钟数据存储
	
	private GuavaMaxApCache gmac = new GuavaMaxApCache<>();//缓存总节点ap与稳定值rssi
	
	
	private final timeUtiles times = new timeUtiles();//缓存时间工具
	
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
/*	@Override
	public List<Map> findMacByMac(String gatewaymac, String mac, String currnetTime,String startTime) {
		// TODO Auto-generated method stub
		List macData = ibeaconMapper.searchMacByMac(gatewaymac,mac,currnetTime,startTime);
		return macData;
	}
*/

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

					
							   
				       /*
				        *   方法说明：同楼层中ap通过最强rssi值不停进行最强更新，不同楼层情况，
				         根据rssi值以及队列中最强楼层所占比例进行移动（不考虑多楼层中只有一个特殊情况）
				         *
				         */
						 
				         
				         
				        /* if(re.getMac().equals("AC233FB100EE")) {
				        	 statApTimeSlice(lq_hmget_ap,times,re);
				         }*/
				      
				         
//				         ==================================================测试中TODO210603
				         
				        /* statApTimeSlice(lq_hmget_ap,times,re);
				        
					     
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
						 }*/
							  
				    	
//						 ==================================================================测试中
						 
						 
						 
//						 矫正过道上ap，进行rssi值干扰,通过校正值来断定（过道中无遮挡物品，信号干扰较小，此处统一矫正，前端页面定点过道加参数8-10）
						/* try {
							 Map<String,String> floorByRedis1 = getFloorByRedis(re);
							 if(!StringUtils.isEmpty(floorByRedis1.get("gatewayflag"))) {
								 if(re.getMac().equals("AC233FB100EE") || re.getMac().equals("AC233FA42FDB") || re.getMac().equals("AC233FA430DF") || re.getMac().equals("AC233FB100EE") || re.getMac().equals("AC233FA42FD4") || re.getMac().equals("AC233FA42FE3") || re.getMac().equals("AC233FA431F7")) {
			    						LogUtil.logger.info(re.getGatewaymac()+"过道处理处理前ap波动="+re.getMac() + "==="+max_ap_Ele_rssi);
			    				 }
								 
								 
								 String string_new_rssi = floorByRedis1.get("gatewayflag");
								 Integer max_ap_Ele_gatewayfactor  = Integer.parseInt(string_new_rssi);//获取通道物理属性，增大该系列区域ap的rssi物理影响因子
								 int new_rssi = Integer.parseInt(max_ap_Ele_rssi); 
								 max_ap_Ele_rssi = (new_rssi - max_ap_Ele_gatewayfactor)+"";
								 
								 if(re.getMac().equals("AC233FB100EE") || re.getMac().equals("AC233FA42FDB") || re.getMac().equals("AC233FA430DF") || re.getMac().equals("AC233FB100EE") || re.getMac().equals("AC233FA42FD4") || re.getMac().equals("AC233FA42FE3") || re.getMac().equals("AC233FA431F7")) {
			    						LogUtil.logger.info(re.getGatewaymac()+"过道处理后ap波动="+re.getMac()+"===="+max_ap_Ele_rssi);
			    				 }
							 }
						} catch (Exception e) {
							// TODO: handle exception
							LogUtil.logger.error(e+"过道ap矫正异常");
						}*/
						 
						 
						 
						 
				         String max_ap_Ele_Number = re.getGatewaymac();
						 String max_ap_Ele_rssi = re.getRssi();
						 Map<String, String> floorByRedis1 = getFloorByRedis(re);
						 String max_ap_Ele_floor  = floorByRedis1.get("floor");
						 
//						 ===================================================================节点测试
						 
						 
//				    	获取该最强ap后，通过该ap楼层及坐标，与队列中同楼层和不同楼层ap及坐标，
				    	
						 
				    	

//				    	统计更新ap次数
					/*	if("AC233FB100EE".equals(re.getMac())) {
							LogUtil.logger.info(rece_lq.getQueue()+"=更新后获取队列中数据"+max_ap_Ele_Number);
							if(!newi.equals(max_ap_Ele_Number) && !StringUtils.isEmpty(max_ap_Ele_Number)) {
								count++;
//								LogUtil.logger.info(count+"==count");
								newi = max_ap_Ele_Number;
							}
						}*/
				    	
						
						
						
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
						
						/**
						 * 
						 * 
						 * TODO0311获取ap位置及其楼层所在，作为均值圆心，该圆范围内（除楼梯过道处，上下ap可以切换）上下ap不可切换，
						 * 强制避免其他位置楼层相串
						 * 
						 */
						
				         
//				        TODO210510大于-45(大于-53都可以直接进行更新，（存在特殊情况偶尔强放射信号值（大于-45）干扰，偶尔近邻跳动一次ap）
//						近邻可允许移动 )切换ap,小于-65ap，过滤掉（针对于ap较密集的地区） 
						
						
//						首次直接插入rece (不需要限制接受的信号值强度)
						if(count == 1) {
							re.setCreatetime(re.getUpdatetime());
							ibeaconMapper.persistence(re);
						}else{
							if(StringUtils.isEmpty(max_ap_Ele_Number)) {
								/* TODO0519 异常进行修改
								 * ibdatas.parallelStream().forEach(rece -> {
									String rece_gatewaymac = rece.get("gatewaymac").toString();
									if(StringUtils.isEmpty(rece_gatewaymac)) {
										re.setGatewaymac(re.getGatewaymac());
									}else {
										re.setGatewaymac(rece_gatewaymac);
									}
								});*/
								
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
									
//									TODO210525不同楼层分为多类，以rssi值众多且较强 为准楼层
									re.setFloor(max_ap_Ele_floor);//TODO210525添加楼层属性
									
									
									
									re = getRealFloorByRssiAndAp(re);
									
									
									
								/*	
//									存入缓存（包含-65在内的缓存以及时间存入）
									moreThanTwo(re);
//									TODO0519返回连续次数最多或者缓存中存在次数最大的ap,(下面re值必须通过该处获取)
									re = getMoreThanTwoCache(re);
									
								*/
									
									
									
									
									/*
									 * 思路一：通过再次拦截添加缓存，步骤一：将所获取值存入队列中，队列中所得结果，队列中7次，
									 *步骤二： 获取校准点（同楼层以最强rssi值为准，存在不同楼层情况下，以最强rssi较多的楼层为准，楼层切换条件（rssi必须多个强于其他情况））
									 * ，该校准点（拟定范围半径R，包括上下楼层），如果超过该半径，进行移动
									 * 
									 * 问题：半径范围内的ap切换
									 * 
									 * */
									
									
									
//									除去上一次t与t+1次相同ap，以最后一次相同ap的时间t+n 与下一个ap B 间隔包含在7次内，所耗时时间长短在180秒内（如同样大于该值则该附近几个ap不稳定）
									
//									此处过滤有影响掉线ap（动态设备中离线）=============================================
									
									
//									筛选时间差小于7次（6*30【一分钟内前稳定值ap切换间隔时间】=180）秒，ap切换（小于-65rssi值过滤掉）
//									过滤掉小于-65数据 ，TODO210519异常：数据库中存在一般稳定ap重复连续短时间缓存 cache中，（1）时间段大于180，（2）时间段小于180
									/*if(re.getMac().equals("AC233FB100EE")) {
										
										LogUtil.logger.info(re+"=moreThanTwoCache_re");
									}*/
									//TODO210521通过所得ap去匹配redis中网关楼层更准确
//									getFloorByRedis(re);
									
									
//									小于-65，进行过滤，防止穿楼层（部分拦截）
									/*int floor_filter = Integer.parseInt(max_ap_Ele_rssi);
									if(floor_filter<=ParameterField.floor_filter_rssi) {
//									    更新原有ap时间，不切换ap，防止超长时间离线情况,rssi值不进行更新，相同ap不更新只更新rssi值和时间
//										小于-65的rssi值，除首次外记录，后续只更新时间即可，避免设备（因信号弱）断线情况出现
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
//										re.setGatewaymac(max_ap_Ele_Number);
//										re.setRssi(max_ap_Ele_rssi);
									}*/
									
//								==========================================================================
									
									
									
								} catch (Exception e) {
									// TODO: handle exception
									LogUtil.logger.error(e+"=e");
								}
								
								
								

							}
							
							
//							System.out.println("sql更新后Done in "+(System.nanoTime()-MqttHandler.start)/1_000_000+" ms"); //Done in 14 ms ==》0ms
							
//							TODO210524待处理问题：（1）掉线数据分析，因程序解析异常或者本身数据发送时间间隔较长。通过mqtt解析数据的mac查看离线时间较长的ap即可
							
							
							GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
							/* TODO210702 修改调试，待验证
							 * GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
							*/
							cf.completFutureUpdateRecevied(re);
							
							
//							插入和更新
							/**
							 *通过历史记录获取大数据集中最后即最新并且最强rssi值ap（因为已存在多节点ap缓存，已经排除单个ap异常放电情况）
							 *一直异常放电最强ap（可忽略）， 由此可得较准确楼层，最强ap。
							 *
							 *但是移动效果需要处理（方案一：通过以数据集中获取最新最强ap，作出范围圆（该范围圆，应包括位置固定不变的情况下，数据集中包含圆内附近跳动ap（数据跳动ap）），如最新移动ap存在该圆内，不做移动处理。
							 *（楼梯处上下移动可排除，货物不堆放在楼梯口处），一旦超出该圆范围（即最新最后一部份数据集中（10-20个数据集）未包含在上一个ap定位位置附近所拥有的ap（数据跳动ap）），做出移动效果）
							 * 
							 * 
							 *
							 */

							
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
									

									
									
									
//									将过滤好的数据进行保存,包含ap坐标，以及楼层
									Map<String, String> floorByRedis = getFloorByRedis(re);
									
									
//									除首次外，历史记录中超过-65不进行数据记录
									
									
									/*去掉过滤值TODO210722
									int floor_filter = Integer.parseInt(floorByRedis.get("rssi"));
									if(floor_filter<=ParameterField.floor_filter_rssi) {
										return ;
									}
									*/
									
									
									
								/*
								 * 
								 * 
								 * 数据更新
								 * 
								 * 
								 * TODO（sql数据查询，不能以字符串的数字大小比较，其顺序是以首字母大小排序，查询结果是错误的，时间排序除外，本地项目rssi同样除外，未出现个位数rssi值或三位数值，否则异常）
								 * TODO0507楼层数据与ap存在异常，需进行排查
								 * */
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
										ibeacon.setApMoveFloorQueue(apMoveFloorQueue+","+floorByRedis.get("floor"));
//										切换的ap
										ibeacon.setApMoveQueue(apMoveQueue +","+floorByRedis.get("gatewaymac"));
//										rssi改变
										ibeacon.setApRssiQueue(apRssiQueue+","+floorByRedis.get("rssi"));
//										多节点时间集
										ibeacon.setMaxRssiUpdateTime(apMaxRssiTime+","+re.getUpdatetime());
										
										ibeacon.setMac(floorByRedis.get("mac"));
										ibeacon.setTimestamp(get0Time);//时间节点条件
										
										
										
										
										
										List<String> listByStr = getListByStr(apMoveQueue);
										String new_ap= listByStr.get(listByStr.size()-1);
										
//									     数据库ap与现有ap一致，不记录
										if(new_ap.equals(floorByRedis.get("gatewaymac"))) {
											return;
										}
										
										
									}
//									存入历史数据库
									cf.completFutureUpdateHistoryIbeacon(ibeacon);
									
									
//									存入实时标签位置表中，TODO210702修改 (过滤掉65以下数据，会导致接受新的数据时间延长造成离线数量增加)
									re.setUpdatetime(times.getCurrenttime());
									re.setGatewaymac(floorByRedis.get("gatewaymac"));
									re.setMac(floorByRedis.get("mac"));
									re.setRssi(floorByRedis.get("rssi"));
//									cf.completFutureUpdateRecevied(re);
									
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
		/*
		 * 优化添加索引以及替代in 使用EXISTS
		
		一级缓存ap所有数据，访问缩短时间
		通过mac匹配ap相关数据
		
		*/
		
		
		
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
	
	
	
	
/*	
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
	}*/
	
	
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
/*	@Override
	public List<Map> findReceviedByMac(String gatewaymac, String mac) {
		// TODO Auto-generated method stub
		List recevied = ibeaconMapper.findRecevied(gatewaymac,mac);
		return recevied;
	}
*/
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
				/*String redis_currentTime = (String)map.get("currentTime");
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
				}*/
				
				
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
	/*private void filterTime(Received re,timeUtiles times) {
		 
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
   }*/
	
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
//			if(times.getCurrenttime().compareTo(tim)>0) { //TODO210616暂时不做同秒内时间处理
			if(times.getCurrenttime().compareTo(tim)>=0) {
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
	
	
//	获取到真实楼层，以及稳定ap
	private Received getRealFloorByRssiAndAp(Received re) {
		realFloor(re);
		Map grfl_map = (Map)grfl.getCache(re.getMac());
//		获取队列中数据
    	LimitQueue<Map> limit_cache = (LimitQueue)grfl_map.get("grfl");
    	Queue<Map> queue = limit_cache.getQueue();
    	
    	/*if( re.getMac().equals("AC233FB100EE")) {
    		LogUtil.logger.info(queue+"==queue起始位置=mac="+re.getMac());
    	}*/
    	
    	
//    	步骤一，获取最大rssi，并且最多楼层所处位置 即为准楼层。
//    	方案一：(归类，机器学习)
//    	方案二：（逻辑规定）
//    	1.获取楼层最大数或连续3次以上楼层为准楼层
    	Integer max_rssi = -10000;
    	Map map_maxrssi_ap = new HashMap<>();
    	List ls = new ArrayList<>();
    	String rssi_ = "";
		String floor_ =  "";
		String ap_ = "";
		String time_ = "";
    	for (Map map : queue) {
    		rssi_ = (String) map.get("rssi");
    		floor_ = (String) map.get("floor");
    		ap_ = (String) map.get("ap");
    		time_ = (String) map.get("time");
//    		ls.add(floor_);
    		ls.add(ap_);
    		
//    		获取最大rssi值对应的ap，以及楼层
    		if(Integer.parseInt(rssi_)>max_rssi) {
    			max_rssi = Integer.parseInt(rssi_);
    			map_maxrssi_ap.put("ap", ap_);
    			map_maxrssi_ap.put("rssi", rssi_);
    			map_maxrssi_ap.put("floor", floor_);
    			map_maxrssi_ap.put("time", time_);
    		}
    		
		}
    	
    	JsonUtils js = new JsonUtils();
    	
//    	Map map_max_ap = js.getMaxContinuousEle(ls);//连续ap出现次数以及ap
    	
//    	rssi最强，并且相同楼层出现次数最多。或者rssi最强，但是楼层出现次数较少情况
    	
    	String floor_real = js.getMaxEleNumber(ls);//机器学习:贝叶斯概率比较
    	
    	Map maxEleAndNumber = js.getMaxEleAndNumber(ls);//统计元素出现最多的元素以及个数
    	
    	if(floor_real.equals(map_maxrssi_ap.get("floor")) || map_maxrssi_ap.size()>0 ) {
//        	步骤1.5，固定楼层前，需要进行范围估计
//    		该队列中皆为result_cache 结果缓存校验上下楼层或者附近范围内ap，即可固定。（连续超过4次以上最强ap为其他ap则进行切换）
    		Map gaf_map = (Map)gaf.getCache(re.getMac());
        	
        	Map map = new HashMap<>();
        	map.put("ap", map_maxrssi_ap.get("ap"));
        	map.put("time", map_maxrssi_ap.get("time"));
        	map.put("rssi", map_maxrssi_ap.get("rssi"));
        	map.put("floor", map_maxrssi_ap.get("floor"));
        	
        	map.put("max_num", maxEleAndNumber.get("max_num"));//连续出现次数，不一定等同于最大rssi值ap
        	map.put("max_ap_ele", maxEleAndNumber.get("max_ap_ele"));
        	
//        	TODO210528 异常由于最大连续值以及连续次数值（存在偶然不与正确值最强值一致（
//        	长时间离线，导致连续次数较少）导致长时间未能提高精准度（从而一致出现在隔壁房间））导致房间间隔壁切换异常
        	/*if(re.getMac().equals("AC233FA43310")) {
	        	LogUtil.logger.info(map+"==新传入map中包含的值"); 
	    	}*/
        	
        	
        	
        	/*if( re.getMac().equals("AC233FB100EE")) {
        		LogUtil.logger.info(map+"=map中最新数据获取最大连续值或者最大出现次数值"+re.getMac()+"===="+ls+"===连续次数"+maxEleAndNumber);
        	}*/

        	
        	
        	
//        	与缓存中相同ap ，更新时间以及rssi值，在焦点半径范围内的ap（包含不同楼层）更新时间，不切换ap。否则切换ap
        	re = updateTimeOrAp(re,map);
        	
        	
    	}
    	
    	
    	/*机器学习:贝叶斯概率比较
    	各楼层ap，rssi值进行排序，以各个楼层最强rssi值相互比较（前一队列中基本排除异常放电情况，暂时不考虑），
    	得到最强rssi的最多楼层的ap。*/
    	
    	

//    	步骤二，存放该ap至缓存中（结果值result_cache）==（如以7个或多个为准点值，则如标签移动，则有偏差）
    	return re;
    	
	}
	
//	更新结果值时间或者ap
	private Received updateTimeOrAp(Received re,Map map) {
		Map more_Than_Two = (Map)gaf.getCache(re.getMac());
    	LimitQueue<Map> limit_cache_gaf = (LimitQueue)more_Than_Two.get("gaf");
    	
//    	Queue<Map> queue = limit_cache_gaf.getQueue();
//    	与缓存中相同ap ，更新时间以及rssi值，在焦点半径范围内的ap（包含不同楼层）更新时间，不切换ap。否则切换ap
    	
    	if( re.getMac().equals("AC233FB100EE")) {
    		LogUtil.logger.info(map+"=map中最新数据"+re.getMac());
    	}
    	
    	
//    	起始值队列中最新连续最强ap缓存(最强连续多次ap重复最新最强)
    	Map newap_more = (Map)newap.getCache(re.getMac());
    	LimitQueue<String> newap_more_queue = (LimitQueue)newap_more.get("newap");
    	
    	LimitQueue lq_newap = new LimitQueue<>(ParameterField.lqSize_newap_more);
    	Boolean new_more_ap = false;
    	Boolean move_ap = false; //双降情况下：相近房间进行移动，移动前的ap在移动后，其起始值中必须小于最强、最新8-10个单位以上，才算双降移动
    	Boolean more_move_ap = false;//移动后在连续最新、最强队列长度中，起始值中一直无上一结果值进行比较
    	String ele_more_than = ""; //最新,最强,连续多次元素
    	
//    	多个ap信号值较弱情况,以连续最多次且最强
    	if(null == newap_more_queue || newap_more_queue.size()==0) {
    		lq_newap.offer(map.get("ap").toString());
    	}else {
//    		连续最强多次ap
    		Queue<String> newap_more_= newap_more_queue.getQueue();
    		
    		if( re.getMac().equals("AC233FB100EE")) {
				LogUtil.logger.info(re.getMac()+"===mac==="+newap_more_+"==最新,最强缓存队列中数据======================");
			}
    		
    		int n = 0;
    		String ele = "";
//    		更新原有队列以及连续判定
    		for (String string : newap_more_) {
				if(string.equals(ele)) {
					n++;
				}else{
					n=1;
				}
				ele = string;
				lq_newap.offer(string);
			}
//    		快速进行双降，处理真实移动情况
//    		int num_ap = ParameterField.lqSize_newap_more/2;//TODO210923设置ap连续出现次数
    		int num_ap = ParameterField.lqSize_newap_more/2;
    		if(n == num_ap) {
    			new_more_ap = true;
    			ele_more_than = ele;
    		}
//    		特殊情况：处理移动后起始值中，连续多次没有上一个结果值ap
    		if(n == ParameterField.lqSize_newap_more) {
    			more_move_ap = true;
    			ele_more_than = ele;
    		}
    		lq_newap.offer(map.get("ap").toString());//添加最新、最强ap
    	}
    	Map map_lq_newap = new HashMap();
    	map_lq_newap.put("newap", lq_newap);
    	newap.putCache(re.getMac(), map_lq_newap);
    	
    	
//    	定位校准
    	LimitQueue<Map> lq = new LimitQueue<>(ParameterField.lqSize_real_ap);
//	    首次添加（首次校准点，预设）
    	if(null == limit_cache_gaf || limit_cache_gaf.size()==0) {
    		lq.offer(map);
    		
    		circleInAp(map,re);//一定范围内进行缓存（包含初始值）
    	}else{
//    		更新ap队列(重点,有且仅有一个值,否则异常)
    		Queue<Map> queue_gaf = limit_cache_gaf.getQueue();
    		
    		
//    		新值队列中与上一次结果值进行比较
    		Boolean result_old = false;
    		for (Map object : queue_gaf) {
    			
    			Map grfl_map = (Map)grfl.getCache(re.getMac());
//    			获取队列中数据
    	    	LimitQueue<Map> limit_cache = (LimitQueue)grfl_map.get("grfl");
    	    	Queue<Map> queue_start = limit_cache.getQueue();
//    	    	上一结果值
    	    	String ap_result = (String)object.get("ap");
				String rssi_result = (String)object.get("rssi");
//    	    	（1）获取起始值相关ap
    	    	for (Map map_start : queue_start) {
    	    		try {
    	    			String ap_new_result_rssi = map_start.get("rssi").toString();
        	    		Integer ap_new_ = Integer.parseInt(ap_new_result_rssi);	
        	    		int old_result = Integer.parseInt(rssi_result);
//    	    			新值相较于上一结果值降低5-8个单位
//						if(map_start.get("ap").equals(ap_result) && ap_new_ < old_result - 15 ) {
						if(map_start.get("ap").equals(ap_result) && ap_new_ < old_result - 15 && ap_new_ > old_result - 30 ) { //TODO210705加上一个自身ap异常弱电情况,移动过程中最高阀值=最低阀值
							result_old = true;		
						}
						
//						最新、最强ap与上一个结果值ap的实时数据（起始值中）进行比较（异常，起始值队列中不存在上一个ap值）
						if(map_start.get("ap").equals(ap_result)) {
//							最新、最强值与上一个ap值进行比较，最强最新值一定必须大于8-10个单位以上，便是移动
							Integer map_rssi_max = Integer.parseInt(map.get("rssi").toString());
//							if(ap_new_ < map_rssi_max -8) { //华西测试
							if(ap_new_ < map_rssi_max -5) { //办公室测试
								move_ap = true;
							}
						}
        	    		
					} catch (Exception e) {
						// TODO: handle exception
						LogUtil.logger.error(e+":房间级范围内ap切换异常");
					}
    	    	}
    	    	
    	    	
    		}
    		
    		
    		
    		for (Map object : queue_gaf) {
//    			不相同ap，必须rssi值大于原缓存中原有ap缓存
    			try {
    				
//    				圆内，圆外判断
    				Map<Object, Object> rece_gate_map = redisUtil.hmget(object.get("ap").toString());
					Gateway gate_map = (Gateway) rece_gate_map.get("gate");
    				
    				double circle_center_mapx = Double.parseDouble(gate_map.getMapx());
					double circle_center_mapy = Double.parseDouble(gate_map.getMapy());
//					最后10秒左右在更新
					
					Map<Object, Object> newMax_gate_map = redisUtil.hmget(map.get("ap").toString()); //新传入最大值ap
					Gateway new_gate_map = (Gateway) newMax_gate_map.get("gate");
					double circle__mapx = Double.parseDouble(new_gate_map.getMapx());
					double circle__mapy = Double.parseDouble(new_gate_map.getMapy());
							
//					如果在内，不随意进行ap切换
					boolean circle = getCircle(UserParameterField.cirle,circle_center_mapx,circle_center_mapy,circle__mapx,circle__mapy);
    				
//    				连续出现次数，不一定等同于最大rssi值ap，当连续次数大于等于4次以上则相同（队列为7）
    				int max_num = Integer.parseInt(map.get("max_num").toString());
    				String max_ap_ele = map.get("max_ap_ele").toString();
    				
    				
    				
    				Integer new_ap = Integer.parseInt(map.get("rssi").toString());
    				Integer old_ap = Integer.parseInt(object.get("rssi").toString());
    				//不同楼层的不同ap,rssi值强才切换楼层ap（待测：在同一个园内，新rssi值必须大于旧rssi值（5-8个单位），才能切换）
    				if(!object.get("ap").equals(map.get("ap")) && old_ap+ParameterField.floor_rssi_flot < new_ap && !object.get("floor").equals(map.get("floor"))
    					) {
    					lq.offer(map);
    					
    					if( re.getMac().equals("AC233FB100EE")) {
    						LogUtil.logger.info("不同楼层新值大于旧值5-8个单位更新ap===mac=="+re.getMac());
    					}
    					
        			}
//    				不同楼层，出现一次或者多次以上不同楼层ap,但是在圆范围内，并且rssi值小于原有rssi，不切换楼层
    				if(!object.get("ap").equals(map.get("ap")) && !object.get("floor").equals(map.get("floor")) && old_ap >= new_ap
    						&& circle 
    						) {
    					
    					String result_time = times.getCurrenttime();
    					
    					//更新最新时间，最大次数，最大ap元素
    					object.put("time", result_time); 
    					object.put("max_num", map.get("max_num")); 
    					object.put("max_ap_ele", map.get("max_ap_ele")); 
    					
    					
    					
    					lq.offer(object);
    					
    					if( re.getMac().equals("AC233FB100EE")) {
    						LogUtil.logger.info("不同楼层在圆范围内并且信号值大于新值只更新时间===mac="+re.getMac());
    					}
    					
    				}
//    				（无法解决,楼梯口不能及时切换）不同楼层进行移动的情况（如果楼层移动新值小于原先旧值，可能出现移动失败的情况）,所以不在同一楼层，并且不在同一圆内，并且新的楼层ap超过3次以上，则切换ap
    				if(!object.get("ap").equals(map.get("ap")) && !object.get("floor").equals(map.get("floor"))
        					&& !circle && max_num > 3) {
    					
    					if( re.getMac().equals("AC233FB100EE")) {
    						LogUtil.logger.info("不同楼层不在圆范围内连续3次以上更新为ap===mac="+re.getMac());
    					}
    					
        				lq.offer(map);
            		}
    				


//    				相同楼层，不同ap情况
    				
//    				（如果隔壁房间的rssi强于原来的rssi==5-8单位连续3-4次以上则认为是切换到隔壁），并且缩短前面第一阶段发送数据过来的时间
//    				该校准ap点位，圆范围内的ap进行缓存，如果其中隔壁一个ap，（非校准ap）连续3-4次以上超过自身5-8个单位以上，则视为移动到隔壁，否则视为（校准ap短暂离线）不切换ap
    				
//    				(此处圆范围，待测可以取消，圆内最多容纳7个ap)
    				if(object.get("floor").equals(map.get("floor")) && circle) {
    					
    					
    					
//    					不同ap在圆范围内（如自身连续增强至5-8个单位，则视为移动到该房间）
//    					重点异常关注:（不可以缓存中最强为基准，一旦移动并且缓存中不超过7个即包含移动前最大值rssi,将会出现实际移动但数据未改变的情况）
    					
//    					上一缓存结果值,如果小于新来参数值5-8个单位,(并且范围队列中无相关新属性值,如有则自身必须连续强于自身5-8个单位,才进行切换)
    					
//    					步骤一:获取上一次缓存结果值ap及相关值
    					String ap_result = (String)object.get("ap");
    					String rssi_result = (String)object.get("rssi");
						try {
							int old_cache_rssi = Integer.parseInt(rssi_result);//上一个结果值的rssi
							
							
	    					
//    	    				（范围内缓存队列）隔壁连续4次以上超过最初的5-8以上rssi值，则切换ap（隔壁移动情况）（原理：强于自身5-8以上给单位）
	    					Map gaicc_map = (Map)gaicc.getCache(re.getMac());
	    		        	LimitQueue<Map> limit_cache_map = (LimitQueue)gaicc_map.get("gaicc");
	    		        	Queue<Map> queue = limit_cache_map.getQueue();
	    		        	
	    		        	int ap_num_same = 0;//相同ap出现次数
	    		        	
	    		        	
	    		        	
	    		        	
	    		        	
	    		        	int ole_cir_rssi = -10000;
	    		        	int new_cir_rssi = Integer.parseInt(map.get("rssi").toString());
	    		        	
	    		        	Boolean move_flag = false; //自身属性减弱标签（范围缓存队列中减弱，满足其中条件之一）
	    		        	Boolean result_flag = false;//起始值中是否存在上一结果值ap
	    		        	int new_ap_num_same = 0;//新值是否出现在范围缓存队列中
//    		        	    	条件之一：结果值-新值-范围缓存值（优先获取作为后续判定基准条件之一）
	    		        	for (Map map_cir : queue) {
//    		        			作为条件二中，新值是否在范围队列存在的条件，且顺序不可修改
	    		        		if(map_cir.get("ap").equals(map.get("ap"))) {
	    		        			new_ap_num_same ++;
	    		        		}
	    		        		
	    		        		ole_cir_rssi = Integer.parseInt(map_cir.get("rssi").toString());
//    		        			TODO210616待处理（移动过程中，则基本证明在移动）原本结果result中rssi的值降低5-8个单位，同时map中新的ap的rssi值大于旧值5-8单位以上，进行切换
//    		        			原有缓存队列中（基本是最强ap的最强rssi值不断自我增强），如果新更新值超过缓存队列值或者低于最大限度对于该缓存队列值（-5-5）个单位，并且 原结果值降低10-15个单位以上（基本说明进行了移动）
//    		        			新传值(非自身ap)在范围缓存队列中波动范围+5--8单位移动，（初始起始值中可能包含该值，或者不包含（排除掉长时间多次ap未接受到该ap并且进行发送数据））如果低于自身ap10-15单位以下则（未接受到信号，新传入值，不进行比较，视为不动）视为切换
    		        			
//    		        			（1）获取起始值相关ap
    		        			Map grfl_map = (Map)grfl.getCache(re.getMac());
//    		        			获取队列中数据
    		        	    	LimitQueue<Map> limit_cache = (LimitQueue)grfl_map.get("grfl");
    		        	    	Queue<Map> queue_start = limit_cache.getQueue();
	    		        		
    		        	    	for (Map map_start : queue_start) {
//	    		        	    	1.上一结果缓存值ap_result,与起始接受值进行比较,(存在并且和范围缓存最强值进行比较)
//	    		        	    		非自身ap，与缓存中自身最强值进行比较
    		        	    		
	    		        	    		try {
	    		        	    			String ap_new_result_rssi = map_start.get("rssi").toString();
		    		        	    		Integer ap_new_ = Integer.parseInt(ap_new_result_rssi);	
//	    		        	    			新值等于缓存值并且等于上一个结果值时候
//		    		        	    		以上一次结果值与起始值，与缓存范围值进行比较（多次遍历，以最后一次最新起始值进行更新）
//	    		        	    			三值一致，情况下，参数值比较，（起始值队列中不存在上一次结果值ap，可能是暂时断链，必须出现该值方才能进行比较，因此圆的范围半径需要尽量大0.05以上）
											if(map_start.get("ap").equals(ap_result) && ap_result.equals(map_cir.get("ap"))) { 
//											新传值在范围缓存队列中波动范围+15以下单位则移动,或低于上一次结果值10个单位以下
												if ( ap_new_ < ole_cir_rssi - 15 || ap_new_ <= old_cache_rssi - 10) { //相距较近ap的间隙值可调整
//												if ( ap_new_ < ole_cir_rssi - 18 || ap_new_ <= old_cache_rssi - 15) {
//													结果缓存存放急速增强ap
													move_flag = true;
												}
											}
//											起始值中存在上一个结果值ap
											if(map_start.get("ap").equals(ap_result)) {
												result_flag = true;
											}
											
		    		        	    		
										} catch (Exception e) {
											// TODO: handle exception
											LogUtil.logger.error(e+":房间级范围内ap切换异常");
										}

								}	
	    		        		
	    		        	}
	    		        	
	    		        	for (Map map_cir : queue) {
	    		        		try {

	    		        			
//	    		        			移动属性标签
	    		        	    	Boolean new_move_flag = false;//移动时所处属性增强标签
//    	    		        			比较范围内新来缓存值(如已存在,如不存在)
	    		        			if(map_cir.get("ap").equals(map.get("ap"))) {
	    		        				ap_num_same ++;
	    		        			}
	    		        			
	    		        			ole_cir_rssi = Integer.parseInt(map_cir.get("rssi").toString());
//	    		        			TODO210616待处理（移动过程中，则基本证明在移动）原本结果result中rssi的值降低5-8个单位，同时map中新的ap的rssi值大于旧值5-8单位以上，进行切换
//	    		        			原有缓存队列中（基本是最强ap的最强rssi值不断自我增强），如果新更新值超过缓存队列值或者低于最大限度对于该缓存队列值（-5-5）个单位，并且 原结果值降低10-15个单位以上（基本说明进行了移动）
//	    		        			新传值(非自身ap)在范围缓存队列中波动范围+5--8单位移动，（初始起始值中可能包含该值，或者不包含（排除掉长时间多次ap未接受到该ap并且进行发送数据））如果低于自身ap10-15单位以下则（未接受到信号，新传入值，不进行比较，视为不动）视为切换
	    		        			
//	    		        			（1）获取起始值相关ap
	    		        			Map grfl_map = (Map)grfl.getCache(re.getMac());
//	    		        			获取队列中数据
	    		        	    	LimitQueue<Map> limit_cache = (LimitQueue)grfl_map.get("grfl");
	    		        	    	Queue<Map> queue_start = limit_cache.getQueue();
	    		        	    	
	    		        	    	

	    		        	    	
	    		        	    	
	    		        	    	
//	    		        	    	条件之二：范围圆内相近ap，属性值逐渐增强
	    		        	    	for (Map map_start : queue_start) {
//	    		        	    	1.上一结果缓存值ap_result,与起始接受值进行比较,(存在并且和范围缓存最强值进行比较)
//	    		        	    		非自身ap，与缓存中自身最强值进行比较
	    		        	    		try {
	    		        	    			String ap_new_result_rssi = map_start.get("rssi").toString();
		    		        	    		Integer ap_new_ = Integer.parseInt(ap_new_result_rssi);	
		    		        	    		
//		    		        	    		新值等同于缓存值的情况下
		    		        	    		if(map_start.get("ap").equals(map_cir.get("ap"))){
//		    		        	    			满足移动条件之一，自身减弱。满足移动条件之二，相近ap急速增强，或者初始值在范围队列中不存在
		    		        	    			
//		    		        	    			初始值大于上一个结果值5个单位以上或者其中一个新值ap比范围缓存值大5个单位以上(缓存值中基本都是最强rssi值，如已有最强缓存，则靠长时间连续10次以上ap的双降校准)，并且最新值比较值一定是起始值队列中最强ap才进行变动，并且连续4次以上才进行隔壁间数据切换
		    		        	    			
//		    		        	    			条件二中：新值ap增强，必须强于自身5个单位以上（TODO210720原max_num=3修改为,max_num>=2包含移动过程中(两次间隔发送间断时间)，具有最多最强的次数。问题一旦快速移动后，缓存中值已经更新但是次数未达到，造成异常），
//		    		        	    			if(!map_start.get("ap").equals(ap_result)&& map_start.get("ap").equals(map.get("ap")) && ap_new_ > ole_cir_rssi + 5 ) {  //及时性弱（高于原有范围队列5个单位，导致缓存过强情况不进行移动情况，剩余情况交由双降特殊处理），但是稳定的高，准确度高。map_start.get("ap").equals(map.get("ap"))最新最强ap移动阀值校验（提高稳定度），不影响队列中是否存在上一个ap结果值
//		    		        	    			if(!map_start.get("ap").equals(ap_result) && (ap_new_ > old_cache_rssi || ap_new_ > ole_cir_rssi) && max_num >= 3 && map.get("max_ap_ele").equals(map.get("ap")) ) {  //及时性很弱，但是稳定的高，准确度高，（但是max_ap_ele最大值在初始缓存队列中不一定是次数最大值，通过概率极小）
		    		        	    			if(!map_start.get("ap").equals(ap_result) && (ap_new_ > old_cache_rssi || ap_new_ > ole_cir_rssi)) {  // 及时性强，但是稳定度不高
		    		        	    				new_move_flag = true;
		    		        	    			}
		    		        	    			
												
//												比较两个条件情况
		    		        	    			if(re.getMac().equals("AC233FB100EE")) {
			    	    		        			LogUtil.logger.info(map_start.get("ap")+"起始的ap测试====="+ap_new_+"==ap_new_新值 =="+ old_cache_rssi +"=====上一个值==================="+  ole_cir_rssi+"==范围缓存值=========================="
			    	    		        				+"===ap_result=="+ap_result);
			    	    		        			
			    	    		        			LogUtil.logger.info("==new_move_flag=="+new_move_flag+"===="+move_flag+"===move_flag ===");
			    	    		        		}
												
												
		    		        	    		}
		    		        	    		
		    		        	    		
										} catch (Exception e) {
											// TODO: handle exception
											LogUtil.logger.error(e+":房间级范围内ap切换异常");
										}
	    		        	    		

	    		        	    		

									}
	    		        			
	    		        	    	
	    		        	    	
//	    		        	    	特征：（自身减小并且隔壁房间增强）或者 新传值ap在范围队列中的首次不存在并且上一结果值ap减小
//	    		        	    	（特殊情况）移动过程中速度较快- 新传入起始数据中未有范围队列已经缓存的ap，导致真移动后信号降低的值未及时传输过来，（一般圆内信号相互接受较强（时间一长也会收到该信号参数），暂时可以不考虑该特殊情况，一旦处理会导致较近距离房间来回跳动）
//	    		        	    	new_ap_num_same缓存队列中已经存在（ap不间断休眠以及对应标签数据发送时间较长导致标签数据发送不定时情况），依旧定位在该处位置
//	    		        	    	过滤范围队列中是否已存在新ap值，以及临近ap进行跳动(max_num >= 2 && map.get("max_ap_ele").equals(map.get("ap"))，条件不可删除，用于最终确认ap)
//	    		        	    	(move_flag && new_ap_num_same == 0)如圆半径过大，则会出现，圆内远距离来回跳动的情况但是仅有一次
	    		        	    	
//	    		        	    	单升单降
//	    		        			if( (new_move_flag && move_flag) || (move_flag && new_ap_num_same == 0)) { //速度快、稳定性差
//	    		        	    	if( (new_move_flag && move_flag)) //速度慢、稳定性强
	    		        			if( (new_move_flag && move_flag) || (new_move_flag && !result_flag)) { //TODO210721 ，其中（!result_flag）表示起始值队列中不包含上一结果值ap，并且新移动后的ap增强5个单位以上
	    		        				if( re.getMac().equals("AC233FB100EE")) {
	    	    		        			LogUtil.logger.info("范围队列中存在该ap（或不存在）或者（单降单升），并且进行圆内范围移动====="+re.getMac() + "===new_move_flag"+new_move_flag+"===new_ap_num_same===="+new_ap_num_same+"===move_flag====="+move_flag + "===map==="+map);
	    	    		        		}
	    		        				lq.offer(map);
	    		        			}
	    		        			
	    		        			
								} catch (Exception e) {
									// TODO: handle exception
								}
	    		        		
							}
//	    		        	连续最新、最强ap队列最大值长度相同情况下，同时无上一结果值在起始值中进行实时比较
	    		        	if(!result_flag && more_move_ap) {
	    		        		move_ap = true;	
	    		        	}
	    		        	
	    		        	
//	    		        	===========================================
//	    		        	初始过程中，进行均值漂移中心位置初始定位(非标签自身移动情况处理!move_flag)
	    		        	
//	    		        	无自降的情况下三种情况：
//	    		        	1.作为上一个条件的补充，圆内中所处隔壁房间内，如超过上一个信号值则进行房间级跳转，加快移动效果，但是同时降低了稳定度（仅仅对初始效果明显）
//	    		        	if(ap_num_same > 0 && old_cache_rssi < new_cir_rssi-ParameterField.room_rssi_flot_unonline_cache){ //TODO210713修改,此处rssi值提升较高，可去掉自身减弱属性move_flag
	    		        	if(ap_num_same > 0 && old_cache_rssi+ParameterField.room_rssi_flot_unonline_cache <= new_cir_rssi && !move_flag){
	    		        		if( re.getMac().equals("AC233FB100EE")) {
	    		        			LogUtil.logger.info("范围队列中存在该ap，并且隔壁房间或者自身信号强于上一个结果缓存值====mac=="+re.getMac());
	    		        		}
	    		        		lq.offer(map);
	    		        	}
	    		        	
//	    		        	2.TODO210706提高及时性,快速进行校准定位,不断提高rssi最强ap
//	    		        	if(map.get("max_ap_ele").equals(map.get("ap")) && max_num >= 3 && old_cache_rssi < new_cir_rssi) { //TODO210713修改
//	    		        	TODO210713修改 原有结果值自身降低同时，新值大于旧值，则进行更新
	    		        	if(map.get("max_ap_ele").equals(map.get("ap")) && max_num >= 2 && old_cache_rssi + 5 <= new_cir_rssi && !move_flag) {
	    		        		if( re.getMac().equals("AC233FB100EE")) {
	    		        			LogUtil.logger.info("附近ap连续3次以上超过自身ap则更新为该值最强ap"+map+"===object"+object+"===mac="+re.getMac());
	    		        		}
	    		        		lq.offer(map);	
	    		        	}
//	    		        	===========================================上述两者性质相似,但功能具有差异性
	    		        	
	    		        	
//    	    		     	3.范围队列中不存在该ap,并且新值大于旧缓存值5-8个单位,切换为新的ap （浮动值尽可能小，方便长时间未发送ap数据，使定位更为精准）
//	    		        	通过范围队列初始位置获取（快速获取初始位置，进行后续校准）。TODO待定义可作为参考，初始定位更快速，但同时稳定性较差
//	    		        	初始过程中，进行均值漂移中心位置初始定位
	    		        	if(ap_num_same==0 && old_cache_rssi + ParameterField.room_rssi_flot_unonline < new_cir_rssi && !move_flag){
	    		        		if( re.getMac().equals("AC233FB100EE")) {
	    		        			LogUtil.logger.info("同楼层范围队列中不存在ap，并且新值大于旧值0-8个单位更新为ap====mac=="+re.getMac());
	    		        		}
	    		        		
	    		        		lq.offer(map);
	    		        	}
	    		        	
//	    		        	==============================================初始无移动区间
//	    		        	快速移动新点位过程中，无该新点缓存 （特点：上一节点必须减弱，并且连续最强多次,或者上一个结果值ap在起始值队列中不存在）
	    		        	if(ap_num_same==0 && map.get("max_ap_ele").equals(map.get("ap")) && max_num >= 3 && (move_flag || !result_flag)){
	    		        		if( re.getMac().equals("AC233FB100EE")) {
	    		        			LogUtil.logger.info("移动到无缓存点ap的复杂区域"+re.getMac());
	    		        		}
	    		        		
	    		        		lq.offer(map);
	    		        	}
	    		        	
	    		        	
	    		        	
	    		        	
//	    		        	范围信号弱区域处理：
//	    		        	TODO210714 待测试AC233FA432EE
	    		        	
//	    		        	如果最新且最强次数超过连续ap队列长度7次以上,处理圆内移动到较弱信号区域,并且与最新ap一致才进行移动处理.(因信号值较弱,值之间差距可调制较大8-10单位以上)
	    		        	
	    		        	/*
	    		        	 * 2021-07-07 14:48:58,2021-07-07 14:50:10,2021-07-07 14:54:57,2021-07-07 15:02:58,2021-07-07 15:04:49
	    		        	 * AC233FC038BD,AC233FC072A6,AC233FC037E2,AC233FC038BD,AC233FC072A6
	    		        	 * 11,12,11,11,12
	    		        	 * -64,-58,-51,-63,-54
	    		        	 * 因偏差值导致同楼层偏移，从而引发楼层偏差（该标签可能在11楼信号-51处）
	    		        	 * */
//	    		        	if(new_more_ap && ele_more_than.equals(map.get("ap")) && (old_cache_rssi+8 < new_cir_rssi  || old_cache_rssi-8 > new_cir_rssi)) { 
	    		        	//TODO210712圆内固定ap下处在较弱区域，并且上一原有结果值降低不超过8-10以下单位（移动到信号值弱的区域，自身必须满足降低）
//	    		        	而且初始值中，该值依旧存在，且最强依旧是它，则不变（如新传入值中短时间内未出现，同样不改变ap位置）
//	    		        	比原有强则直接切换，但是如果新值比上一结果值值弱的情况，则必须满足小于5个单位以下，15个单位以上（5-8-15大致为该范围圆的信号覆盖区域）
//	    		        	TODO210720 队列中连续次数降低为5次，原华西测试为10次
//	    		        	if(new_more_ap && ele_more_than.equals(map.get("ap")) && (move_flag && new_cir_rssi > old_cache_rssi -15) && (new_cir_rssi>old_cache_rssi|| new_cir_rssi < old_cache_rssi -8)) { //TODO 210714 增加移动上限
	    		        	//TODO 210722，移动前处于较弱区域，移动后同样也处于较弱区域，并且前后信号值相差不大（特殊情况）====移动后起始值中新的ap的rssi一定会大于上一个8-10个单位以上（并且多次连续皆是如此）。未进行移动的ap而造成来回跳动的，起始值中两则相差不大（偶尔一两次突变）（异常，起始值队列中不存在上一个ap值）
	    		        	if(new_more_ap && ele_more_than.equals(map.get("ap")) && (move_flag && new_cir_rssi > old_cache_rssi -20) && (new_cir_rssi>old_cache_rssi+5 || (new_cir_rssi<=old_cache_rssi && move_ap)) ) { 
	    		        		if( re.getMac().equals("AC233FB100EE")) {
	    		        			LogUtil.logger.info("处理圆内移动到较弱信号区域====mac=="+re.getMac());
	    		        		}
	    		        		lq.offer(map);
	    		        	}
//	    		        	
	    		        	
						} catch (Exception e) {
							// TODO: handle exception
						}
						
    					
    					
    					circleInAp(map,re);//将新圆范围内ap存入缓存范围队列中，用作后续比较，（条件：仅仅支持更新一定圆范围内缓存的队列）
    					
    				}
    				
//    				超过半径范围外（之后，并且队列长时间次数内，如没有原该ap出现，则进行远距离跳动）
    				
//    				相同楼层不同ap,(并且连续3次以上出现该ap最强) 
    				try {
    					/*2021-06-28 17:22:36 [INFO] [{rssi=-48, max_ap_ele=AC233FC0380F, time=2021-06-28 17:22:36, floor=b2, max_num=2, ap=AC233FC08509}]=queue结束时===mac==AC233FB100EE
    							2021-06-28 17:22:37 [INFO] AC233FB100EE====================mac-54========rssi======gatewaymac====AC233FC08502
    							2021-06-28 17:22:37 [INFO] [{rssi=-63, time=2021-06-28 17:22:17, floor=b2, ap=AC233FC08502}, {rssi=-63, time=2021-06-28 17:22:26, floor=b2, ap=AC233FC07217}, {rssi=-53, time=2021-06-28 17:22:26, floor=b2, ap=AC233FC08509}, {rssi=-53, time=2021-06-28 17:22:26, floor=b2, ap=AC233FC0380F}, {rssi=-59, time=2021-06-28 17:22:27, floor=b2, ap=AC233FC08502}, {rssi=-51, time=2021-06-28 17:22:36, floor=b2, ap=AC233FC07217}, {rssi=-54, time=2021-06-28 17:22:37, floor=b2, ap=AC233FC08502}]==queue起始位置=mac=AC233FB100EE
    							2021-06-28 17:22:37 [INFO] {rssi=-51, max_ap_ele=AC233FC08502, time=2021-06-28 17:22:36, floor=b2, max_num=3, ap=AC233FC07217}=map中最新数据获取最大连续值或者最大出现次数值AC233FB100EE====[AC233FC08502, AC233FC07217, AC233FC08509, AC233FC0380F, AC233FC08502, AC233FC07217, AC233FC08502]===连续次数{max_ap_ele=AC233FC08502, max_num=3}
    							2021-06-28 17:22:37 [INFO] {rssi=-51, max_ap_ele=AC233FC08502, time=2021-06-28 17:22:36, floor=b2, max_num=3, ap=AC233FC07217}=map中最新数据AC233FB100EE
    							2021-06-28 17:22:37 [INFO] true==!circle==同楼层不在范围内进行连续3次以上更新为ap===mac==AC233FB100EE
    							2021-06-28 17:22:37 [INFO] [{rssi=-51, max_ap_ele=AC233FC08502, time=2021-06-28 17:22:36, floor=b2, max_num=3, ap=AC233FC07217}]=queue结束时===mac==AC233FB100EE
    							异常：远距离移动出现（偶然出现一次，跳动）间隔连续跳动
    					*/
    					
    					int new_ciry_rssi = Integer.parseInt(map.get("rssi").toString());
    					int old_ciry_rssi = Integer.parseInt(object.get("rssi").toString());
//    					最大次数ap与最新ap不同时，其最大出现次数ap在起始值中不能占超过总数的一半以上，或者最大最新并且连续两次以上则视为远距离移动，同时移动时自身ap降低5-8个单位以上
    					if(!object.get("ap").equals(map.get("ap")) && object.get("floor").equals(map.get("floor"))
//    						&& !circle && ( map.get("max_ap_ele").equals(map.get("ap")) && max_num >=2 ) && result_old  //华西测试210702，远程必须3次以上才能进行移动
    						&& !circle && ( map.get("max_ap_ele").equals(map.get("ap")) && max_num > 2 ) && result_old
    						) {
    					
//    						原有结果值ap，与新传入该相同ap（暂不考虑，一直未发送下一次的情况）相比小于5-8个单位
    						
	    					if( re.getMac().equals("AC233FB100EE")) {
	    						LogUtil.logger.info(!circle+"==!circle=="+"同楼层不在范围内进行连续3次以上更新为ap===mac=="+re.getMac());
	    					}
	    					lq.offer(map);
    					
    					}
//    				并且考虑发送时间间隔较长的ap,新值超过旧值8个上单位，同样切换 （其中原有的aprssi信号值必须下降8个单位以下或者起始位置队列中已经不存在）
    				
//    					新值不一定大于旧值，可能在其范围左右，浮动甚至更小。作为快速切换基准
    					if(!object.get("ap").equals(map.get("ap")) && object.get("floor").equals(map.get("floor"))
        						&& !circle && new_ciry_rssi - ParameterField.old_ciry_rssi > old_ciry_rssi  // TODO210713快速校准均值圆心点,稳定性也强
//        						&& !circle && new_ciry_rssi - ParameterField.old_ciry_rssi > old_ciry_rssi && ( map.get("max_ap_ele").equals(map.get("ap")) && max_num >= 2 ) //准确度差，但是稳定性强
        						) {
        					
        					if( re.getMac().equals("AC233FB100EE")) {
        						LogUtil.logger.info("同楼层不在范围内rssi值超过原有ap的8个以上单位更新为ap===mac=="+re.getMac());
        					}
        					lq.offer(map);
        					
        				}
    					
//    					非圆范围内，如果连续5-7次（新值缓存）以上最新值且最强，视为移动至信号较弱区域（其值可能小于上一ap结果值，移动到新ap信号位置接受较弱的情况）。
//    					16：10 起始时间
    					
					} catch (Exception e) {
						// TODO: handle exception
						
					}
    				
    				

    				
//    				默认更新原有缓存,避免传空值队列
    				if(null == lq.getQueue() || lq.getQueue().size() == 0) {
    					
    					if( re.getMac().equals("AC233FB100EE")) {
    						LogUtil.logger.info("空值造成最新数据更新为ap===mac=="+re.getMac());
    					}
    					
    					//更新最新时间，最大次数，最大ap元素
    					String result_time = times.getCurrenttime();
    					object.put("time", result_time); 
    					object.put("max_num", map.get("max_num")); 
    					object.put("max_ap_ele", map.get("max_ap_ele")); 
    					
    					lq.offer(object);	
    				}
    				
    				
    				
    				
				} catch (Exception e) {
					// TODO: handle exception
				}
    			
			}
	    	
    	}
    	Map map_lq_limit = new HashMap();
    	map_lq_limit.put("gaf", lq);
    	
//    	更新缓存
    	gaf.putCache(re.getMac(), map_lq_limit);
    	
    	
    	
    	Queue<Map> queue = lq.getQueue();
    	
    	if( re.getMac().equals("AC233FB100EE")) {
        	LogUtil.logger.info(queue+"=queue结束时===mac=="+re.getMac());	
    	}
    	
    	for (Map reiv : queue) {
    		re.setGatewaymac(reiv.get("ap").toString());
    		re.setFloor(reiv.get("floor").toString());
    		re.setRssi(reiv.get("rssi").toString());
		}
    	return re;
    	
	}
//	（计算从缓存第二次开始，首次直接跳过）更新校准点范围内ap（注意：该圆大小范围内最多存放7个不同ap）
	private void circleInAp(Map mapCir,Received re) {
		
		Map more_Than_Two = (Map)gaicc.getCache(re.getMac());
		
		LimitQueue<Map> limit_cache = (LimitQueue)more_Than_Two.get("gaicc");
    	
//    	队列创建 //TODO21-01-14 后台数据库设置队列大小，方便华西内网调试
    	LimitQueue<Map> lq = new LimitQueue<>(ParameterField.lqSize_real_circle);
    	
    	Map map = new HashMap<>();
    	map.put("ap", mapCir.get("ap"));
    	map.put("time", mapCir.get("time"));
    	map.put("rssi", mapCir.get("rssi"));
    	map.put("floor", mapCir.get("floor"));

    	
//	    首次添加
    	if(null == limit_cache || limit_cache.size()==0) {
    		lq.offer(map);
    		
    	}else{
//    		更新ap
    		Queue<Map> queue = limit_cache.getQueue();
    		
//        	====================测试
        	/*if( re.getMac().equals("AC233FB100EE")) {
            	LogUtil.logger.info(queue+"===queue范围内队列更新前===mac=="+re.getMac());
        	}*/
//        	====================测试
    		Boolean flag = true;
    		for (Map object : queue) {
//    			TODO210609测试中不添加相同ap，或者小于原有缓存ap的rssi值只更新时间
    			/*if(mapCir.get("ap").equals(object.get("ap"))) {
    				continue;
    			}*/
    			try {
    				Integer map_rssi = Integer.parseInt(mapCir.get("rssi").toString());
    				Integer object_rssi = Integer.parseInt(object.get("rssi").toString());
    				

//    				缓存中的值小于新值，则直接跳过已有数据保存，在新值中直接添加
    				if(mapCir.get("ap").equals(object.get("ap")) && object_rssi<=map_rssi) {
    					continue;
    				}
//    				缓存中旧值大于新值，不进行更新（时间不一定为准确时间）
    				if(mapCir.get("ap").equals(object.get("ap")) && object_rssi>map_rssi) {
    					flag = false;
    				}
//    				旧值更新后，flag新值不进行更新
    				lq.offer(object);
    				
				} catch (Exception e) {
					// TODO: handle exception
				}
    			
			}
    		if(flag) {
    			lq.offer(map);
    		}
	    	
    	}
    	
    	Map map_lq_limit = new HashMap();
    	map_lq_limit.put("gaicc", lq);
    	
//    	更新缓存
    	gaicc.putCache(re.getMac(), map_lq_limit);
    	
    	
    	
//    	=================测试
    	/*if( re.getMac().equals("AC233FB100EE")) {
    		Map gaicc_map = (Map)gaicc.getCache(re.getMac());
        	LimitQueue<Map> limit_cache_map = (LimitQueue)gaicc_map.get("gaicc");
        	Queue<Map> queue = limit_cache_map.getQueue();
        	
        	LogUtil.logger.info(queue+"===queue范围内队列更新后===mac=="+re.getMac());
    	}*/
    	
    	
//    	==================
	}
	
	
//	缓存均值过滤后数据到获取真实楼层队列中
	private void realFloor(Received re) {
		
		Map more_Than_Two = (Map)grfl.getCache(re.getMac());
		
		LimitQueue<Map> limit_cache = (LimitQueue)more_Than_Two.get("grfl");
    	
//    	队列创建 //TODO21-01-14 后台数据库设置队列大小，方便华西内网调试
    	LimitQueue<Map> lq = new LimitQueue<>(ParameterField.lqSize_real_floor);
    	
    	Map map = new HashMap<>();
    	map.put("ap", re.getGatewaymac());
    	map.put("time", re.getUpdatetime());
    	map.put("rssi", re.getRssi());
    	map.put("floor", re.getFloor());
    	
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
    	map_lq_limit.put("grfl", lq);
    	
//    	更新缓存
    	grfl.putCache(re.getMac(), map_lq_limit);
    	
	}
	
	
	
//	缓存均值过滤后数据到连续ap断定队列中
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
	
//	获取队列中内连续多次（连续多次中，算法存在量大之后，值不准确的问题）
	/*private Received getMoreThanTwoCache(Received re) {
    	
    	Map more_Than_Two = (Map)moreThanTwo.getCache(re.getMac());
    	LimitQueue<Map> limit_cache = (LimitQueue)more_Than_Two.get("morenthantwo");
    	
    	
    	Queue<Map> queue = limit_cache.getQueue();
    	
//    	LogUtil.logger.info(queue+"=queue");
//    	2021-05-19 14:03:32 [INFO] [{rssi=-32, time=2021-05-19 14:02:22, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:32, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:42, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:02:52, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:02, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:12, ap=AC233FC08502}, {rssi=-32, time=2021-05-19 14:03:32, ap=AC233FC08502}]=queue
    	
//    	String time_start;
//    	String time_end;
    	
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
//    				return re;
    			}
    		}
    		return re; //获取队列中最后一个相同ap的rssi值即最新rssi值以及最新时间
    	}else{
//    		TODO0519异常：待处理短时间多次切换ap情况，此处包含处理首次队列定位ap，以及一直未能满足上一条件情况
//    		（存在一种特殊情况，上一次满足连续几次后，后面队列中不存在连续情况，导致切换ap,无法避免，需要保证时效性（队列数据采集只能短）,可忽略）
    		
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
//    				return re;
    			}
    		}
    		return re; //获取队列中最后一个相同ap的rssi值即最新rssi值以及最新时间
    	}
//    	return re;
	}
	*/
	
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
    	/*if(re.getMac().equals("AC233FB100EE")) {
    		Queue queue = rece_lq.getQueue();
    		
    		LogUtil.logger.info(queue+"=queue-稳定值中队列");
    	}*/
    	
    	/*if((re.getMac().equals("AC233FB100EE"))) {
			LogUtil.logger.info("=queue===============================queue多节点重新开始"+rece_lq.getQueue());
		}*/
    		
    		
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
    				
    				
    				
    				/*
    				 * 
    				 * 从缓存中获取ap以及对应的rssi值与每个单节点进行比较上下5个单位（容许队列长度队的空值，即无信号），超过则切换节点ap
    				 * rss值为理想标签所在位置的到各标签的距离，该值为平均距离值(各ap与标签之间距离稳定值)
    				 * */
        	
    				
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
    		
			
    		/*
    		 * 如果缓存中存在的ap在多节点队列中存在数量小于1（连续队列长度以上没有收到该ap信号单切片最强数据，
    		 * 该ap可能不稳定或者标签进行移动）， 则切换ap
    		 * */
//    		缓存已经存在,非首次缓存,并且已有单切片最大值，则切换ap
//			单切点一半以上最强为楼层最终位置（11楼换衣处暂时不考虑特殊位置）,至少楼层集合中以大多数未准
    		
    		JsonUtils js = new JsonUtils();
    		String maxEleNumber = js.getMaxEleNumber(ls_floor);
    		/*
    		 *稳定情况下， 首次队列长度存满,位置放入缓存，以大多数楼层及rssi值为焦点，降低楼层来回穿梭，以及降低初始位置楼层偏差
    		（特殊情况，该楼层ap数量较少情况，只能以rssi强为基准，楼层可能出现偏差）
    		*/
    		
    		
    		
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
			
			/*TODO-03-17
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
			*
			*数值1可适当增大，提高ap切换效率
			*/
			if(ap_slice_num < ParameterField.ap_unline_num && !StringUtils.isEmpty(ap_slice_max) && ap_slice_max_floor.equals(maxEleNumber)) {
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
    		/*Map<String,LimitQueue<Map>> ap_rssi_lqtest = (Map)gmac.getCache(re.getMac());
    		
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
				}*/
    	
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
//	根据ap获取楼层信息
	private Map<String,String> getFloorByRedis(Received re) {
//	   ap更新缓存存在一分钟时间差
	   Map<Object, Object> hmgate = redisUtil.hmget(re.getGatewaymac());
	   Map gateway_info_ib = new HashMap<>();
	   if(null != hmgate && !hmgate.isEmpty()) {
		   Gateway gate_way = (Gateway)hmgate.get("gate");
		   String floor = gate_way.getFloor();
		   String mapx = gate_way.getMapx();
		   String mapy = gate_way.getMapy();
		   
		   String gatewayflag = gate_way.getGatewayflag();
		   
		   gateway_info_ib.put("rssi", re.getRssi());
		   gateway_info_ib.put("gatewaymac", re.getGatewaymac());
		   gateway_info_ib.put("mac", re.getMac());
		   gateway_info_ib.put("mapx", mapx);
		   gateway_info_ib.put("mapy", mapy);
		   gateway_info_ib.put("floor", floor);
		   gateway_info_ib.put("gatewayflag", gatewayflag);
	   }
				   
		return gateway_info_ib;
	}
}

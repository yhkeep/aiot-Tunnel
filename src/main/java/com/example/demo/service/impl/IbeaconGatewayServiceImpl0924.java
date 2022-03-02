/*package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.cache.Guava2Cache;
import com.example.demo.cache.Guava3Cache;
import com.example.demo.cache.GuavaMacReceived;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.Received;
import com.example.demo.mapper.IbeaconGatewayMapper;
import com.example.demo.mqtt.MqttHandler;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.AssetService;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.service.ReceivedService;
import com.example.demo.util.LogUtil;
import com.example.demo.util.ReduceTime;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;
import com.example.demo.util.parameter.UserParameterField;
import com.mysql.jdbc.TimeUtil;
@Service
public class IbeaconGatewayServiceImpl0924 implements IbeaconGatewayService{
	
	@Autowired
	private IbeaconGatewayMapper ibeaconMapper;
	
//	@Resource --内存溢出猜疑
	@Autowired
    private RedisUtils redisUtil;	
	
	@Autowired
	private ReceivedService reService;
	
	@Autowired
    private AssetService assetService;
	
	
//	private GuavaMacReceived<String, Object> gr = new GuavaMacReceived();
//	private Guava3Cache g3c = new Guava3Cache ();
//	private Guava2Cache g2c = new Guava2Cache();
	
	@Override
	public void insert(IbeaconGateway ibeacon) {
		// TODO Auto-generated method stub
		ibeaconMapper.save(ibeacon);
	}


	@Override
	public List search(String AssetID,String startTime, String endTime) {
		// TODO Auto-generated method stub
		List<IbeaconGateway> gateWay = ibeaconMapper.queryWay(AssetID,startTime,endTime);
		return gateWay;
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
//		ReceviedCompletableFuture rec = new ReceviedCompletableFuture();

		
		
//0723	15:37 TODO redis缓存中存在标签关联的其中一个网关则跳过，否则进行更新最强rssi
//0723 TODO	三圆定位算法 redis缓存中rssi值计算获取
		
		try {
			
			if(!StringUtils.isEmpty(re.getRssi())&&!StringUtils.isEmpty(re.getMac())&&!StringUtils.isEmpty(re.getGatewaymac())) {
//				不包含在内的进行更新。特殊情况，突然一个信号弱点接受到信号，需要排除 
//				在redis存在的时间内中进行更新数据库，占用很大资源
				
				Map<Object, Object> hmget = redisUtil.hmget(re.getMac());
				boolean containsKey = hmget.containsKey(re.getGatewaymac());
				
				
				
				
				
				if(containsKey) {
//				只更新最大rssi值所在网关和时间以及rssi值======>
//			TODO 0828获取最大网关值的同时还可以和recevied中进行比较,如果二级缓存中存在两种不同网关,但是最强变更,但是数据库中依旧存在原有最强不进行变化位置
					Map gate = getGatewaymacByRssiMax(hmget,re.getMac());
					String gatewaymac_new = (String)gate.get("gatewaymac");
					String maxRssi = (String)gate.get("maxRssi");
					re.setGatewaymac(gatewaymac_new);
					re.setRssi(maxRssi);
					     
					 * 间隔redis失效时间过去，重新生成，因此最新网关数据中没有原有强网关数据，重新加载，所以资产在网关来回跳动
					 *  2020-08-26 16:32:15 [INFO] 最强更新Received [mac=C2021A0000E3, gatewaymac=AC233FC03867, createtime=null, updatetime=2020-08-26 16:32:15, rssi=-60]
							2020-08-26 16:32:19 [INFO] 首次出现更新
							2020-08-26 16:32:21 [INFO] 首次出现更新
							2020-08-26 16:32:23 [INFO] {currentTime=2020-08-26 16:32:23, rssi=-66}
							2020-08-26 16:32:23 [INFO] {currentTime=2020-08-26 16:32:21, rssi=-74}
							2020-08-26 16:32:23 [INFO] 最强更新Received [mac=C2021A0000E3, gatewaymac=AC233FC07217, createtime=null, updatetime=2020-08-26 16:32:23, rssi=-66]
							
					IbeaconGateway ib = new IbeaconGateway(); 
					ib.setMac(re.getMac());
					ib.setGatewaymac(re.getGatewaymac());
					
					List<Map> ibdatas = ibeaconMapper.findGatewayByMac(ib);
					
					
					Long collect= ibdatas.parallelStream().filter(mapdatas -> {
						return  (Long)mapdatas.get("count(*)") <= 0;
					}).count();
					List<Map> ibdatas = ibeaconMapper.findGatewayByMac(ib);
					
					Long collect= ibdatas.parallelStream().filter(mapdatas -> {
						return  (Long)mapdatas.get("count(*)") <= 0;
					}).count();
					
					
					
					if(collect == 1) {
						re.setCreatetime(re.getUpdatetime());
						ibeaconMapper.persistence(re);
					}else{
						
						 * 
						 * 首次优化控制，后续可以延长至1-5分钟持久化到数据库ibeacon表，包含楼层,存储为历史记录
						 * 
						待处理，直接修改一级缓存中数据，websocket获取数据一级缓存中的数据,
						(但是可能存在线程安全问题,如在页面获取定时发送数据的同时,缓存中进行修改.但是目前数据存在移动性,可忽略
						后续可以通过monitor消费链表方式保证线程安全执行,达到并发情况下数据高精度准确)
						
						问题可能存在：设置一级缓存后，每5redis更新一次，后续可能存在缓存时间可能不失效
						
						执行流程
						一级缓存==》更新mac=>gatewaymac
						应用直接获取更新数据
						
						ReduceTime twominute = new ReduceTime();
					    String oldTime = twominute.localTime();
					    String minutes = twominute.timeReduceMinute(oldTime);
					    GuavaMacReceived<String, Object> gr = new GuavaMacReceived();
					    
					    
//						LogUtil.logger.info(gr.getCache(re.getMac())+"放入参数前" +"=hmget"+hmget);
//						一级缓存中存在redis已有的，不进行更新（ap信号不一定是最强）
						try {
//							先获取一二级缓存
//							缓存2,缓存22s，获取缓存1，拿去所有平均值
							
//							缓存1，缓存12s
							Object cache = gr.getCache(re.getMac());
							Received rece = (Received) cache;
							
//							Guava3Cache g3c = new Guava3Cache ();
							Object cache3 = g3c.getCache(re.getMac());
							Received rece3 = (Received) cache3;
							
//							Guava2Cache g2c = new Guava2Cache();
							Object cache2 = g2c.getCache(re.getMac());
							Received rece2 = (Received) cache2;
							
							
//							自身为null,防止同一秒多次发送,比较更新时间
//							一级缓存时间
							String one_cache_updatetime = rece.getUpdatetime();
							String two_cache_updatetime = rece2.getUpdatetime();
							String three_cache_udpatetime = rece3.getUpdatetime();
							
							timeUtiles ti = new timeUtiles();
							String one2two = "";
							String one2three = "";
							if(!StringUtils.isEmpty(one_cache_updatetime)) {
								one2two= ti.timeAdd(one_cache_updatetime, 10);
								one2three = ti.timeAdd(one_cache_updatetime, 20);
							}
							
							
							
							if(StringUtils.isEmpty(rece3.getGatewaymac()) && StringUtils.isEmpty(rece2.getGatewaymac()) && StringUtils.isEmpty(rece.getGatewaymac())) {
								gr.putCache(re.getMac(), re);  //存放第一次缓存
								
							}else if ( !StringUtils.isEmpty(rece.getGatewaymac()) && StringUtils.isEmpty(rece3.getGatewaymac()) && StringUtils.isEmpty(rece2.getGatewaymac() )
									&& one2two.compareTo(re.getUpdatetime()) == 0 ) {
//								存放第二级缓存
								g2c.putCache(re.getMac(), re);
							}else if(!StringUtils.isEmpty(rece.getGatewaymac()) && !StringUtils.isEmpty(rece2.getGatewaymac()) && StringUtils.isEmpty(rece3.getGatewaymac())
									&& one2three.compareTo(re.getUpdatetime()) == 0) {
//								存放第三级缓存
								g3c.putCache(re.getMac(), re);
								if("AC233FA430DC".equals(re.getMac())) {
									LogUtil.logger.info(gr.getCache(re.getMac())+"=一级"
											+g2c.getCache(re.getMac())+"=二级"+g3c.getCache(re.getMac())+"=三级" + re.getMac()+"=re中mac");	
								}
							}
							
							
							
							if("AC233FA430DC".equals(re.getMac())) {
								LogUtil.logger.info(gr.getCache(re.getMac())+"=一级"
										+g2c.getCache(re.getMac())+"=二级"+g3c.getCache(re.getMac())+"=三级" + re.getMac()+"=re中mac" +"=时间差值"+one2three.compareTo(re.getUpdatetime())
										+"one2three"+one2three+"=one_cache_updatetime"+one_cache_updatetime);	
							}
							
							
							
//							  TODO0923
							Object cache = gr.getCache(re.getMac());
							Received rece = (Received) cache;
							boolean rssiMaxGatewaymac = hmget.containsKey(rece.getGatewaymac()); //空指针自定义已经排除
//							距离大于3米的rssi 小于-68值，就进行更换，否则会导致移动长距离，但是有信号而无变化
							String rssi = re.getRssi();
							int parseIntRssi = Integer.parseInt(rssi);

							
							
							
//							定位在该点，并且以该点为核心的ap坐标，辐射周围的ap如果在该范围内并且标签3-5次内rssi平均值最大
							
							
							
							
//							  TODO0922
							
							  
							  ibdatas.stream().map(gateMap -> {
								String recevied_gatewaymac = (String) gateMap.get("gatewaymac");
								String recevied_rssi = (String) gateMap.get("rssi");  //数据库中原有rssi值
								
								Map<Object, Object> rece_gate_map = redisUtil.hmget(recevied_gatewaymac);
								
								
								
								Gateway gate_map = (Gateway) rece_gate_map.get("gate");
								double circle_center_mapx = Double.parseDouble(gate_map.getMapx());
								double circle_center_mapy = Double.parseDouble(gate_map.getMapy());
//								优先级执行二次运算后gatewaymac
								if(!StringUtils.isEmpty(re.getGatewaymac())) {
	//								需要判断是否在圆内范围的ap
									String ap = re.getGatewaymac();
									Map<Object, Object> newMax_gate_map = redisUtil.hmget(ap); //新传入最大值ap
									Gateway new_gate_map = (Gateway) newMax_gate_map.get("gate");
									double circle__mapx = Double.parseDouble(new_gate_map.getMapx());
									double circle__mapy = Double.parseDouble(new_gate_map.getMapy());
									
//									如果在内，不随意进行ap切换
									boolean circle = getCircle(UserParameterField.cirle,circle_center_mapx,circle_center_mapy,circle__mapx,circle__mapy);
									
//									不同楼层间切换
//									boolean long_distance_circle = getCircle(UserParameterField.long_distance_cirle,circle_center_mapx,circle_center_mapy,circle__mapx,circle__mapy);
									
									
//									if(circle  || (rssiMaxGatewaymac && parseIntRssi > UserParameterField.RssiScope)) {
									
									if((rssiMaxGatewaymac && parseIntRssi > UserParameterField.RssiScope) || circle) {
										re.setGatewaymac(recevied_gatewaymac);//使用数据库ap
										gr.putCache(re.getMac(), re);  //抽取前内容	
										
//										问题：1 此处如果3-5次，隔壁ap一直大于，进行ap切换
//										问题：2 7361ap异常远程争抢切换ap
										
//										极为近距离房间，多次临时切换到其他点位，便可进行正常定位
//										一级缓存和二级缓存的平均值都小于新来ap的rssi值则进行切换，如再添加对比值，再添加缓存cache2
										

										
//										近房间切换ap
										String rssi2 = rece.getRssi();
										try {
											int cache_rssi2 = Integer.parseInt(rssi2);
											int rece_rssi1 = Integer.parseInt(recevied_rssi);
											int avg_rssi = (cache_rssi2+rece_rssi1)/2;
											if(parseIntRssi > avg_rssi) {
												re.setGatewaymac(re.getGatewaymac()); //切换ap
												gr.putCache(re.getMac(), re);
											}
										} catch (Exception e) {
											// TODO: handle exception
											LogUtil.logger.error(e+"=e");;
										}
										
										
										
									}else {
										
//										rssi超值超强情况，并且，超远距离不超过一定距离r=0.0888,进行切换。（注意不同楼）
										if(long_distance_circle && parseIntRssi > UserParameterField.change_RssiScope) {
//											更新最强网关到一级缓存
											gr.putCache(re.getMac(), re);
//										物理单位距离太远，不切换ap
										}else {
											re.setGatewaymac(recevied_gatewaymac);//使用原有数据库ap,不切换
										}
										
										
										gr.putCache(re.getMac(), re); //0923-10:58
										
									}
									
								}
								return -1;
							}).collect(Collectors.toList());
							
							
//							RssiScope，需要更新缓存的rssi值距离值要小于设备两者间距一半以下
						    
						     * 
						     * if(rssiMaxGatewaymac && parseIntRssi > UserParameterField.RssiScope) {
//								更新时间,不更新网关,并且更新数据库
								re.setGatewaymac(rece.getGatewaymac());
								gr.putCache(re.getMac(), re);  //抽取前内容
								
								
							}else {
//								更新最强网关到一级缓存
								gr.putCache(re.getMac(), re);
								
//								耗时最长造成线程阻塞
//								ibeaconMapper.updateRecevied(re); //sql更新耗时 15ms
								
								
							}
							  
							  
//							异步数据库
						    GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
							cf.completFutureUpdateRecevied(re);
						    
						    
							
						} catch (Exception e) {
							// TODO: handle exception
							LogUtil.logger.error("更新recevied缓存获取异常"+e);
						}
						
						
						

						
					}
					
					 * 更新保存设备移动路径 
					 * 控制层代码==》服务层处理
					 * if(ibDatas.size()>0) {
						updateRssiAndBattery( currentTime, mac, battery, gatewaymac, rssi);
					}else if(ibDatas.size()==0){
						persistence(currentTime, type, mac, rssi, battery, gatewaymac);
					}
					
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
		
		
//		System.out.println("sql更新后Done in "+(System.nanoTime()-MqttHandler.start)/1_000_000+" ms"); //Done in 14 ms ==》0ms
		
		
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
//					电子围栏,部门跨界报警
					department = (String)map.get("department");
					
					
//					测试电脑休眠，服务不断线情况             
					if(re.getMac().equals("AC233FA430A2")) {
						LogUtil.logger.info(department+"===>department");
					}
					
					Asset asset = new Asset();
					
			    	asset.setMac(re.getMac());
			    	asset.setLocation(location);
			    	asset.setFloor(floor);
			    	asset.setCadMapRoomName(cadMapRoomName);
			    	
			    	asset.setCheck(check);//空值时间设置
			    	
			    	asset.setDepartment(department);
			    	
//			    	更新电量
			    	asset.setElectric(re.getBattery());
//			    	asset.setStatus();  设置维修接单状态，显示位于医工科
			    	
//			    	更新实时路径位置
			    	assetService.updateLocation(asset);
			    	
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("IbeaconGatewayServiceImpl=>location==>error"+e);
		}
//		System.out.println("更新后Done in "+(System.nanoTime()-MqttHandler.start)/1_000_000+" ms"); //Done in 14 ms ==》0ms
		
		
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
		Set<Object> keySet=hmget.keySet();
//		设定无限最低值
		int rssi_max = -1000000;
		
		Map map1 = new HashMap();
//		map遍历，获取最大rssi
		for(Object string:keySet){
			if("expressTime".equals(string)) {
				continue;
			}
			Object object = hmget.get(string);
			Map map = (Map)object;
			String redis_rssi = (String)map.get("rssi");
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
		if("AC233FB100EE".equals(mac)) {
			
			System.out.println("最强rssi网关所在"+map1+"==gatewaymac="+gatewaymac);
		}
		return gateway_map;
	}
	private <K, V> Set<K> getKeysByStream(Map<K, V> map, V value) {
		  return map.entrySet()
		    .stream()
		    .filter(kvEntry -> Objects.equals(kvEntry.getValue(), value))
		    .map(Map.Entry::getKey)
		    .collect(Collectors.toSet());
	}
}
*/
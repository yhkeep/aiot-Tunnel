package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.cache.GuavaAssetData;
import com.example.demo.domain.Asset;
import com.example.demo.mapper.IReceivedMapper;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.ReceivedService;
import com.example.demo.util.LogUtil;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ReceivedServiceImpl implements ReceivedService{

	@Autowired
	private IReceivedMapper receivedMapper;
	
	@Autowired
	private RedisUtils redisUtil;
	
	@Override
	public List<Map> searchAssert(String timeEnd) {
		//定时器不断查询获取数据，存放如map中，TODO210430可能会造成内存积压
//		Map<Object, Object> assetDatas = redisUtil.hmget("assetCacheDatas");
		
		
		GuavaAssetData gad = new GuavaAssetData();
		List<Map> querymap = null;
		/*首次资产数据更新，添加的时候，进行redis的flag修改，此时同步进行首次数据缓存，
		,数据如果再rece中继续增加，会造成资产缓存中不存在，如再添加资产后,不会有数据*/
		
		/*if(null == assetDatas || assetDatas.isEmpty()) {
			querymap = receivedMapper.querymapByUpdateTime(timeEnd);
			if(null != querymap && querymap.size()>0) {
				Map Total_Cache_Datas = new HashMap();
				querymap.stream().map(ad -> {
					Map cacheMap = (Map)gad.getCache(ad.get("mac"));					
//					将一级缓存中数据部分更新至二级缓存永久缓存中
					ad.put("location",cacheMap.get("location"));
					ad.put("floor",cacheMap.get("floor"));
					ad.put("cadMapRoomName",cacheMap.get("cadMapRoomName"));
					ad.put("department",cacheMap.get("department"));
					Total_Cache_Datas.put(ad.get("mac"), ad);
					return -1;
				}).collect(Collectors.toList());
//				LogUtil.logger.info(Total_Cache_Datas+"=total");
				redisUtil.hmset("assetCacheDatas",Total_Cache_Datas);
			}
//		缓存更新
		}else {
			Map<Object, Object> Total_Cache_Datas = redisUtil.hmget("assetCacheDatas");
			
			
			redisUtil.hmset("assetCacheDatas",Total_Cache_Datas);
		}
		*/
		
		
//		TODO0927再添加资产后,才会有实时数据
		querymap = receivedMapper.querymapByUpdateTime(timeEnd);
		
		/*GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
		try {
			List<Map> queryMap = cf.completQueryReceived(timeEnd);
			LogUtil.logger.info(querymap+"=querymap");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
//		redis非空判断不断更新，保证资产数据最新
		if(null != querymap && querymap.size()>0) {
			Map Total_Cache_Datas = new HashMap();
			querymap.stream().map(ad -> {
				
//				一级缓存不能为空--》转map
				Map cacheMap = (Map)gad.getCache(ad.get("mac"));

//				将一级缓存中数据部分更新至二级缓存永久缓存中
				ad.put("location",cacheMap.get("location"));
				ad.put("floor",cacheMap.get("floor"));
				ad.put("cadMapRoomName",cacheMap.get("cadMapRoomName"));
				ad.put("department",cacheMap.get("department"));
				Total_Cache_Datas.put(ad.get("mac"), ad);
				return -1;
			}).collect(Collectors.toList());
//			LogUtil.logger.info(Total_Cache_Datas+"=total");
			
			redisUtil.hmset("assetCacheDatas",new HashMap());//清空原有数据
			redisUtil.hmset("assetCacheDatas",Total_Cache_Datas);
		}
		
		
//		LogUtil.logger.info(redisUtil.hmget("assetCacheDatas")+"=hmget");
		// TODO Auto-generated method stub
		
		
		return querymap;
	}
//	用户所处部门数据
	@Override
	public List<Map> searchUserAssert(String timeEnd,String department) {
		
		GuavaAssetData gad = new GuavaAssetData();
		List<Map> querymap = null;
		
		querymap = receivedMapper.queryAssetByUpdateTime(timeEnd,department);
		
//		redis非空判断不断更新，保证资产数据最新
		if(null != querymap && querymap.size()>0) {
			Map Total_Cache_Datas = new HashMap();
			querymap.stream().map(ad -> {
				
//				一级缓存不能为空--》转map
				Map cacheMap = (Map)gad.getCache(ad.get("mac"));

//				将一级缓存中数据部分更新至二级缓存永久缓存中
				ad.put("location",cacheMap.get("location"));
				ad.put("floor",cacheMap.get("floor"));
				ad.put("cadMapRoomName",cacheMap.get("cadMapRoomName"));
				ad.put("department",cacheMap.get("department"));
				Total_Cache_Datas.put(ad.get("mac"), ad);
				return -1;
			}).collect(Collectors.toList());
//			LogUtil.logger.info(Total_Cache_Datas+"=total");
			
			redisUtil.hmset("assetCacheDatas",new HashMap());//清空原有数据
			redisUtil.hmset("assetCacheDatas",Total_Cache_Datas);
		}
		return querymap;
		
	}
	
	
	
//	异步查询
	public List<Map> queryReceivedByTime(String timeEnd){
		List<Map> querymap = receivedMapper.querymapByUpdateTime(timeEnd);
		return querymap;
	}
	
	@Override
	public List<Map> searchAssert() {
		// TODO Auto-generated method stub
		List<Map> querymap = receivedMapper.querymap();
		
		return querymap;
	}
	
	@Override
	public List<Map> searchRG(String mac) {
		// TODO Auto-generated method stub
		List<Map> querymap = receivedMapper.queryRG(mac);
		
		return querymap;
	}

	@Override
	public PageInfo<Asset> searchAssertIbeaconDatas(Integer pageIndex, Integer pageSize) throws Exception {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageIndex, pageSize);
		List<Asset> querymap = receivedMapper.queryIbeacon();
        PageInfo<Asset> pageInfo = new PageInfo(querymap);
        return pageInfo;
	}



	
}


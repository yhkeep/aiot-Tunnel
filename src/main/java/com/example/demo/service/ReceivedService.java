package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.Asset;
import com.github.pagehelper.PageInfo;

public interface ReceivedService {
	List<Map> searchRG(String mac);
	
//	具体位置
	List<Map> searchAssert(String timeEnd);
	
	List<Map> searchUserAssert(String timeEnd,String searchUserAssert);
	
	List<Map> searchAssert();
	
	
	PageInfo<Asset> searchAssertIbeaconDatas(Integer pageIndex, Integer pageSize) throws Exception;
}

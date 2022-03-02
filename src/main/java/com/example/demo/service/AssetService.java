package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.Asset;
import com.example.demo.domain.User;
import com.github.pagehelper.PageInfo;

public interface AssetService {
//	资产详细
	List<Asset> searchAssert(String address);
	
	List<Asset> searchAssertUser(User user);
	
	List<Map> find3DAssetByRece(String twominute);
	
	List<Map> findAssetByRece(String twominute,List department);
	
	PageInfo<Asset> getDatasPage(Integer pageIndex, Integer pageSize, Map map) throws Exception;
	
	List<Asset> getAssetmarked(Asset asset);
	
	void insertAssetDatas(Asset assetdata);
	
	void delAssetData(List<String> assetid);
	
	void updateAsset(Asset updateasset);

	List<Asset> queryImageByID(String assetID);
	
	void updateLocation(Asset asset);
	
	void updateCheck(Asset asset);
	
	void updatePIG(Asset asset);
	
	List<Map> selectID(List<Asset> asset) throws Exception;
	
	List<Map> searchAssertTotal(Map<String, Object> map) throws Exception;
	
	List<Asset> queryAssetByAssetID(List check) throws Exception;
	
	void updateState(Asset asset) throws Exception;
	
	List queryDepartmentByAsset(String address) throws Exception;
	
	List<Map> findE6() throws Exception;
}

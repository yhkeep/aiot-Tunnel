package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Asset;
import com.example.demo.domain.User;

public interface IAssetMapper {
//	资产明细
	List<Asset> queryAsset(String address);
	
	List<Asset> queryUserAsset(User user);
	
	List<Map> findAsset3DByReceived(@Param("twoMinuteAgo")String twoMinuteAgo);
	
	List<Map> findAssetByReceived(@Param("twoMinuteAgo")String twoMinuteAgo,@Param("department") List department);
	
	
//	分页查询
    List<Map> getDatasByParameter(Map<String, Object> map) throws Exception;
    
//  智能提示
    List<Asset> getAssetmarked(Asset assetdatas);
    
//  Page<Asset> getDatasByParameter(Map<String, Object> map) throws Exception;
    
    void saveAssetDatas(Asset assetdatas);
//    批量删除
    void delAsset(@Param("assetid") List assetid);
    
    void updateAssetDatas(Asset updateasset);
    
    List<Asset> queryImageByAssetID(@Param("assetid") String assetid);
    
    void updateAL(Asset asset);
    
    void updateCH(Asset asset);
    
    void updatePIG(Asset asset);
    
    List selectID(List<Asset> asset);
    
    List<Map> queryAssetTotal(Map<String, Object> map);
    
    List<Asset> queryAssetCheckDetail(@Param("check")List check);
    
    void updateStatus(Asset asset);
    
    List queryDepartment(String address);
    
    List<Map> findE6();
}

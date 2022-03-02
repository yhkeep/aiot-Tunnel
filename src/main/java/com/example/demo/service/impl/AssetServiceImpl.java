package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Asset;
import com.example.demo.domain.Check;
import com.example.demo.domain.User;
import com.example.demo.mapper.IAssetMapper;
import com.example.demo.service.AssetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AssetServiceImpl implements AssetService{
	@Autowired
	private IAssetMapper assetMapper;
	@Override
	public List<Asset> searchAssert(String address) {
		// TODO Auto-generated method stub
		List<Asset> queryAsset = assetMapper.queryAsset(address);
		return queryAsset;
	}
	@Override
	public List<Map> findAssetByRece(String twominute,List department) {
		// TODO Auto-generated method stub
		List<Map> queryMissAsset = assetMapper.findAssetByReceived(twominute,department);
		return queryMissAsset;
	}
	@Override
	public List<Map> find3DAssetByRece(String twominute) {
		// TODO Auto-generated method stub
		List<Map> queryMissAsset = assetMapper.findAsset3DByReceived(twominute);
		return queryMissAsset;
	}

	@Override
	public PageInfo<Asset> getDatasPage(Integer pageIndex, Integer pageSize,Map map) throws Exception {
//	public Page<Asset> getDatasPage(Integer pageIndex, Integer pageSize, String assetName) throws Exception {
		// TODO Auto-generated method stub
//		  创建分页工具类
        PageHelper.startPage(pageIndex, pageSize);
        List<Map> docs = assetMapper.getDatasByParameter(map);
        PageInfo<Asset> pageInfo = new PageInfo(docs);
        return pageInfo;
//      Page<Asset>  userList= assetMapper.getDatasByParameter(map);
//      return userList;
	}
	
	
//	添加资产
//	@Transactional
	@Override
	public void insertAssetDatas(Asset assetdata) {
		// TODO Auto-generated method stub
		assetMapper.saveAssetDatas(assetdata);
		
	}
	@Override
	public void delAssetData(List assetId) {
		// TODO Auto-generated method stub
		assetMapper.delAsset(assetId);
	}
	@Override
	public void updateAsset(Asset updateasset) {
		// TODO Auto-generated method stub
		assetMapper.updateAssetDatas(updateasset);
	}
	@Override
	public List<Asset> queryImageByID(String assetID) {
		// TODO Auto-generated method stub
		List <Asset> image = assetMapper.queryImageByAssetID(assetID);
		return image;
	}
	@Override
	public void updateLocation(Asset asset) {
		// TODO Auto-generated method stub
		assetMapper.updateAL(asset);
	}
	@Override
	public void updateCheck(Asset asset) {
		// TODO Auto-generated method stub
		assetMapper.updateCH(asset);
	}
	@Override
	public void updatePIG(Asset asset) {
		// TODO Auto-generated method stub
		assetMapper.updatePIG(asset);
	}
	@Override
	public List<Asset> getAssetmarked(Asset asset) {
		// TODO Auto-generated method stub
		List<Asset> assetmark = assetMapper.getAssetmarked(asset);
		return assetmark;
	}
	@Override
	public List<Map> selectID(List<Asset> asset)throws Exception {
		// TODO Auto-generated method stub
		List<Map> idMaps = assetMapper.selectID(asset);
		return idMaps;
	}
	@Override
	public List<Map> searchAssertTotal(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		List<Map> assetTotal = assetMapper.queryAssetTotal(map);
		return assetTotal;
	}
	@Override
	public List<Asset> queryAssetByAssetID(List check) throws Exception {
		// TODO Auto-generated method stub
		List<Asset> assetCheckDetail = assetMapper.queryAssetCheckDetail(check);
		return assetCheckDetail;
	}
	@Override
	public void updateState(Asset asset) throws Exception {
		// TODO Auto-generated method stub
		assetMapper.updateStatus(asset);
	}
	@Override
	public List queryDepartmentByAsset(String address) throws Exception {
		// TODO Auto-generated method stub
		List department = assetMapper.queryDepartment(address);
		return department;
	}
	@Override
	public List findE6() throws Exception {
		// TODO Auto-generated method stub
		List findE6 = assetMapper.findE6();
		return findE6;
	}
	
	@Override
	public List<Asset> searchAssertUser(User user) {
		// TODO Auto-generated method stub
		List<Asset> queryAsset = assetMapper.queryUserAsset(user);
		return queryAsset;
	}
}

package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Asset;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.mapper.IhygrothermographMapper;
import com.example.demo.service.hygrothermographService;
import com.example.demo.util.CompletableFutureStream.GatewaymacCompletableFuture;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class hygrothermographServiceImpl implements hygrothermographService{
	@Autowired
	private IhygrothermographMapper hygrothermographMapper;

	@Override
	public void saveHy(Hygrothermograph hy) {
		// TODO Auto-generated method stub
		hygrothermographMapper.insertHy(hy);
	}

	@Override
	public List<Hygrothermograph> queryhy(String mac,String startime, String endtime) {
		// TODO Auto-generated method stub
		List<Hygrothermograph> hy = hygrothermographMapper.search(mac,startime,endtime);
		return hy;
	}

	@Override
	public List<Humiture> queryhum(Humiture humit) {
		// TODO Auto-generated method stub
		List<Humiture> hum = hygrothermographMapper.queryhumDatas(humit);
		return hum;
	}

	@Override
	public List<Hygrothermograph> queryCurrenthum(String mac) {
		// TODO Auto-generated method stub
		List<Hygrothermograph> chum = hygrothermographMapper.getHumCurrentime(mac);
		return chum;
	}

	@Override
	public PageInfo<Hygrothermograph> getDatasPage(Integer pageIndex, Integer pageSize, String startime,String endtime,String mac) throws Exception {
//	public Page<Asset> getDatasPage(Integer pageIndex, Integer pageSize, String assetName) throws Exception {
		// TODO Auto-generated method stub
		  /*创建分页工具类*/
        PageHelper.startPage(pageIndex, pageSize);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("startime",startime);
        map.put("endtime",endtime);
        map.put("mac",mac);
        List<Map> docs = hygrothermographMapper.getDatasByParameter(map);
        PageInfo<Hygrothermograph> pageInfo = new PageInfo(docs);
        return pageInfo;
        
//      Page<Asset>  userList= assetMapper.getDatasByParameter(map);
//      return userList;
	}

	@Override
	public List<Humiture> getHumSection(String mac) {
		// TODO Auto-generated method stub
		List<Humiture> humLi = hygrothermographMapper.queryLimit(mac);
		return humLi;
	}

	@Override
	public void updateHum(Humiture humit) {
		// TODO Auto-generated method stub
		hygrothermographMapper.updateHumDatas(humit);
		
	}

	@Override
	public void updateHy(Map<String, Object> map) {
		// TODO Auto-generated method stub
		hygrothermographMapper.updateHyWarnnum(map);
	}

	@Override
	public List<Map> queryHyByTime(String mac, String startime, String endtime){
		// TODO Auto-generated method stub
		/*GatewaymacCompletableFuture cf = new GatewaymacCompletableFuture();
		List<Map> queryhyDatas = new ArrayList<Map>();
		try {
			queryhyDatas = cf.completFutureQueryTempHum(mac,startime,endtime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		List<Map> queryhyDatas = hygrothermographMapper.searchByTime(mac,startime,endtime);
		
		return queryhyDatas;
	}

	@Override
	@Transactional
	public void saveHumiture(Humiture hum) {
		// TODO Auto-generated method stub
		hygrothermographMapper.insertHumiture(hum);
	}

	@Override
	public List<Map> getWarnDatas(String startTime,String endTime) {
		// TODO Auto-generated method stub
		List<Map> datasWarnValue = hygrothermographMapper.getDatasWarnValue(startTime, endTime);
		return datasWarnValue;
	}

	@Override
	public void delEquipment(List humitureEquipment) {
		// TODO Auto-generated method stub
		hygrothermographMapper.delEquipment(humitureEquipment);
	}

	@Override
	public List<Map> queryHyByEndTime(String mac, String startime, String endtime) {
		// TODO Auto-generated method stub
		List<Map> hy = hygrothermographMapper.searchByEndTime(mac,startime,endtime);
		return hy;
	}

}

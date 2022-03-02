package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.IbeaconHistory;
import com.example.demo.mapper.IbeaconHistoryMapper;
import com.example.demo.service.IbeaconHistoryService;
@Service
public class IbeaconHistoryServiceImpl implements IbeaconHistoryService{
	
	@Autowired
	private IbeaconHistoryMapper ibeaconMapper;

	@Override
	@Transactional
	public void insert(IbeaconHistory ibeacon) {
		// TODO Auto-generated method stub
		ibeaconMapper.save(ibeacon);
	}

	@Override
	@Transactional
	public void updateRssiAndAp(IbeaconHistory ibeacon) {
		// TODO Auto-generated method stub
		ibeaconMapper.updateRssiAndTime(ibeacon);
	}


	@Override
	public List<Map> findApBytime(String mac, String timestamp) {
		// TODO Auto-generated method stub
		List ibeacon = ibeaconMapper.findApBytime(mac, timestamp);
		return ibeacon;
	}
	
	
	
}

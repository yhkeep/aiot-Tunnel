package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Maintainhistory;
import com.example.demo.mapper.IMaintainhistoryMapper;
import com.example.demo.service.MaintainHistoryService;


@Service
public class MaintainHistoryServiceImpl implements MaintainHistoryService{
	@Autowired
	private IMaintainhistoryMapper maintainHistroyMapper;


	@Override
	@Transactional
	public void insertMaintain(Maintainhistory mih) throws Exception {
		// TODO Auto-generated method stub
		maintainHistroyMapper.insertMaintainHistory(mih);
	}


	@Override
	public List<Map> queryUnionMaintainHistory(String maintainhistoryonlyCode,String starthandtime,String endhandtime) throws Exception {
		// TODO Auto-generated method stub
		List<Map> qmhistory = maintainHistroyMapper.queryUnionMaintainHistory(maintainhistoryonlyCode, starthandtime, endhandtime);
		return qmhistory;
	}

	
	
	
}
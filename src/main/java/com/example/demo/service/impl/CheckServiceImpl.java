package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Check;
import com.example.demo.mapper.ICheckMapper;
import com.example.demo.service.CheckService;

@Service
public class CheckServiceImpl implements CheckService{
	@Autowired
	private ICheckMapper checkMapper;

	@Override
	@Transactional
	public void addChecked(Check check) throws Exception{
		// TODO Auto-generated method stub
		checkMapper.insertChecked(check);
	}

	@Override
	public List<Check> queryCheck() throws Exception {
		// TODO Auto-generated method stub
		List<Check> checkdatas = checkMapper.queryChecked();
		return checkdatas;
	}

	@Override
	public List<Check> queryCheckByTime(String startTime, String endTime) throws Exception {
		// TODO Auto-generated method stub
		List<Check> checkdatas = checkMapper.queryCheckByTime(startTime,endTime);
		return checkdatas;
	}
	
	
	
}
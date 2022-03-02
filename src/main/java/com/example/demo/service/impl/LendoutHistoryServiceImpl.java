package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Lendouthistory;
import com.example.demo.mapper.ILendouthistoryMapper;
import com.example.demo.service.LendoutHistoryService;


@Service
public class LendoutHistoryServiceImpl implements LendoutHistoryService{
	@Autowired
	private ILendouthistoryMapper lendoutHistroyMapper;


	@Override
	@Transactional
	public void insertLendout(Lendouthistory loh) throws Exception {
		// TODO Auto-generated method stub
		lendoutHistroyMapper.insertLendoutHistory(loh);
	}

	
	
	
}
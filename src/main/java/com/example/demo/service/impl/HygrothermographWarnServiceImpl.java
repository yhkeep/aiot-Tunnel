package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.HygrothermographWarn;
import com.example.demo.mapper.IhygrothermographWarnMapper;
import com.example.demo.service.HygrothermographWarnService;


@Service
public class HygrothermographWarnServiceImpl implements HygrothermographWarnService{

	@Autowired
	private IhygrothermographWarnMapper hygrothermographWarnMapper;
	
	@Override
	public void insertHyWarn(HygrothermographWarn hw) {
		// TODO Auto-generated method stub
		hygrothermographWarnMapper.addHywarnValue(hw);
	}


	@Override
	public void updateHyWarn(HygrothermographWarn hy) {
		// TODO Auto-generated method stub
		hygrothermographWarnMapper.updateHywarnValue(hy);
	}


	@Override
	public List<Map> queryHwByTime(HygrothermographWarn hy) {
		// TODO Auto-generated method stub
		List<Map> hw = hygrothermographWarnMapper.selectHwHistory(hy);
		return hw;
	}

}

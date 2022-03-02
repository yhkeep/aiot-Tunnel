package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Asset;
import com.example.demo.domain.Gateway;
import com.example.demo.mapper.IAssetMapper;
import com.example.demo.mapper.IGatewayMapper;
import com.example.demo.service.AssetService;
import com.example.demo.service.GatewayService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class GatewayServiceImpl implements GatewayService{
	@Autowired
	private IGatewayMapper gatewayMapper;
	@Override
	public List<Gateway> searchGateway(String address) {
		// TODO Auto-generated method stub
		List<Gateway> gateway = gatewayMapper.queryGateway(address);
		return gateway;
	}
	@Override
	public List<Gateway> searchAllGateway() {
		// TODO Auto-generated method stub
		List<Gateway> gateway = gatewayMapper.initGateway();
		return gateway;
	}
	
	@Override
	@Transactional
	public void updateGateway(Gateway gateway) throws Exception{
		// TODO Auto-generated method stub
		gatewayMapper.updateGateway(gateway);
	}
	
	@Override
	@Transactional
	public void deleteGateway(List<Gateway> gateway) throws Exception {
		// TODO Auto-generated method stub
		gatewayMapper.deleteGateway(gateway);
	}
	@Override
	@Transactional
	public void addGateway(Gateway gateway) throws Exception {
		// TODO Auto-generated method stub
		gatewayMapper.insertGateway(gateway);
	}
}

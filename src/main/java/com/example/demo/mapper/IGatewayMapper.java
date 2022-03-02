package com.example.demo.mapper;

import java.util.List;

import com.example.demo.domain.Gateway;

public interface IGatewayMapper {
//	资产明细
	List<Gateway> queryGateway(String address);
	
	List<Gateway> initGateway();
	
	void updateGateway(Gateway gateway);
	
	void deleteGateway(List<Gateway> gateway);
	
	void insertGateway(Gateway gateway);
}

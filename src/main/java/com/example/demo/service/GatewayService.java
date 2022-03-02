package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.Gateway;

public interface GatewayService {
//	资产详细
	List<Gateway> searchGateway(String address);
	
	List<Gateway> searchAllGateway();
	
	void updateGateway(Gateway gateway) throws Exception;
	
	void deleteGateway(List<Gateway> gateway)throws Exception;
	
	void addGateway(Gateway gateway)throws Exception;
}

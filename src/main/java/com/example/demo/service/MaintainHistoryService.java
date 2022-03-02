package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.Maintainhistory;

public interface MaintainHistoryService {
	void insertMaintain(Maintainhistory mih) throws Exception;
	List<Map> queryUnionMaintainHistory(String maintainhistoryonlyCode,String starthandtime,String endhandtime) throws Exception;
}

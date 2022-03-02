package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.Asset;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.HygrothermographWarn;
import com.github.pagehelper.PageInfo;

public interface HygrothermographWarnService {
	
	void insertHyWarn(HygrothermographWarn hw);
	
	void updateHyWarn(HygrothermographWarn hy);
	
	List<Map> queryHwByTime(HygrothermographWarn hw);
	
}

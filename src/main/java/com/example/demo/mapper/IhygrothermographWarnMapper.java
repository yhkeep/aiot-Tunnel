package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Humiture;
import com.example.demo.domain.HygrothermographWarn;

public interface IhygrothermographWarnMapper {
	void addHywarnValue(HygrothermographWarn hw);
	
	void updateHywarnValue(HygrothermographWarn hw);
	
	List<Map> selectHwHistory(HygrothermographWarn hy);
	
}

package com.example.demo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Lendouthistory;
import com.example.demo.domain.Maintainhistory;

public interface ILendouthistoryMapper {
	void insertLendoutHistory(Lendouthistory mih);
}

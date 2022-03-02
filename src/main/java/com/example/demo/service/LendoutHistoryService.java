package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Check;
import com.example.demo.domain.Lendouthistory;
import com.example.demo.domain.Maintainhistory;

public interface LendoutHistoryService {
	void insertLendout(Lendouthistory loh) throws Exception;
}

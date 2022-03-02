package com.example.demo.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Check {
	private String id;
	private String checkpep; //盘点人
	private String checkTime; //盘点时间
	private String checkOnlyCode; //盘点资产表中唯一id
	private Integer checkNum;//盘点数量
	private String auditpep;//审核人

}
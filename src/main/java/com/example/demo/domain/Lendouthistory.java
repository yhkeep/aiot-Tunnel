package com.example.demo.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Lendouthistory {
	
	private String	id;
//	申请部门
	private String	applaydepartment;
//	交接部门
	private String	handoverdepartment;
//	申请人
	private String	applaypeople;
//	交接人
	private String	successorpeople;
//	申请人签名照
//	private String	handlingpeoplesignatureimage;
	private String	handlingPeople;
//	交接人签名照
//	private String	buttingpeoplesignatureimage;
	private String	buttingPeople;
//	交接资产图片
	private String	handoverimage;
//	交接时间
	private String	handovertime;
//	估计时间
	private String estimatedtime;
//	维修资产id确认码
	private String maintainhistoryonlyCode; 
//	所属资产状态类别
	private String assetstatustype;
//	备注
	private String	remark;
	
}
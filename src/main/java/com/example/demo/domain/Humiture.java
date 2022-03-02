package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Humiture {
//	冷藏物编号
	private String freezernumber;
//	冷藏物名字
	private String freezername;
//	温度区间值
	private String temperaturefitted;
//	湿度区间
	private String humidityfitted;
//	仪器类型
	private String type;
//	标签mac
	private String mac;
//	部门
	private String department;
//	位置
	private String location;
//	地址
	private String address;
//	历史记录间隔时间-分钟
//	private String interrecord;
//	s1告警和数据保存时间-分钟
	private String saveinterval;
}
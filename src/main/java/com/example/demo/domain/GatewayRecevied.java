package com.example.demo.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRecevied implements Serializable{
	private String gatewaymac; //分析后标签所在ap
	private String floor; //分析后标签所在楼层
	private String mapx; //分析后标签所在坐标x
	private String mapy;//分析后标签所在坐标y
}
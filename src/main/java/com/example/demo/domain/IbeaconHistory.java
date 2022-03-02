package com.example.demo.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class IbeaconHistory {

	
	//	发送时间
	private String timestamp;
//	mac
	private String mac;
	
//	单位时间内最大rssi或最接近ap的更新时间
	private String maxRssiUpdateTime;
//	单位时间更新ap次数记录(限定ap随机切换)
//	private String apChangeNum;
	private Integer apChangeNum;
//	单位时间移动ap记录(可根据ap==》常用科室及房间)
	private String apMoveQueue;
//	单位时间移动ap楼层记录
	private String apMoveFloorQueue;
//	
	private String apRssiQueue;
	
}


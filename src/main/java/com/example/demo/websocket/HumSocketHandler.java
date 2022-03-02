package com.example.demo.websocket;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.controller.UserController;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.domain.HygrothermographWarn;
import com.example.demo.service.HygrothermographWarnService;
import com.example.demo.service.hygrothermographService;
import com.example.demo.util.parameter.UserParameterField;


@Component
public class HumSocketHandler {
	
	@Autowired
    private hygrothermographService humService;
	
	@Autowired
	private HygrothermographWarnService hyWwarnService;
	
	
	public static HumSocketHandler task;
	//初始化
	@PostConstruct
    public void init() {    
		task = this;
    }
//    返回数据
    public List<Hygrothermograph> getHumDatas(String mac,String startime,String endtime){
//    	获取标签即刻位置信息
    	List<Hygrothermograph> ls = task.humService.queryhy(mac, startime, endtime);
    	return ls;
    }
    public List<Hygrothermograph> getcurrentimehy(String mac){
//    	获取温湿度最新一条数据
    	List<Hygrothermograph> ls = task.humService.queryCurrenthum(mac);
    	return ls;
    }
    public List<Humiture> getHumiture(){
    	String address = UserParameterField.address;
		
		Humiture humit = new Humiture();
		humit.setAddress(address);
//    	获取标签即刻位置信息
    	List<Humiture> queryhum = task.humService.queryhum(humit);
    	return queryhum;
    }
//  温湿度获取区间告警值
    public List<Map> getWarnDatas(String startime,String currentTime){
    	List<Map> warnDatas = task.humService.getWarnDatas(startime, currentTime);
    	return warnDatas;
    }
//  获取温湿度区间阀值
    public List<Humiture> getHumSection(String mac){
    	List<Humiture> humSection = task.humService.getHumSection(mac);
    	return humSection;
    }
//  更新解除告警
    public void updateGS1WarnValue(HygrothermographWarn hy) {
		
		task.hyWwarnService.updateHyWarn(hy);
    }
}
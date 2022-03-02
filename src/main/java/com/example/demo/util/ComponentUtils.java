package com.example.demo.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.domain.HygrothermographWarn;
import com.example.demo.service.HygrothermographWarnService;
import com.example.demo.service.hygrothermographService;
import com.example.demo.util.parameter.UserParameterField;
import com.example.demo.websocket.HumSocketHandler;

@Component
public class ComponentUtils {
	public static ComponentUtils task;
	//初始化
	@PostConstruct
    public void init() {    
		task = this;
    }
	
	
	@Autowired
	private hygrothermographService hyService;
	
	@Autowired
	private HygrothermographWarnService hyWwarnService;
	
	
	
//	默认十分钟保存一次，处理GS1未完全记录告警
	public void addData(Hygrothermograph hy){
		timeUtiles time = new timeUtiles();
		String cur_time = time.getCurrenttime();
		
		
		String substring = cur_time.substring(0, cur_time.length()-2);
		String endtime = substring+"00";
		
		String minute_ = cur_time.substring(cur_time.length()-5, cur_time.length()-3);
		int minute = Integer.parseInt(minute_);
		hy.setCurrentTime(endtime);
		String mac = hy.getMac();
//		List<Map> queryHyByTime = task.hyService.queryHyByTime(mac, endtime, endtime);
		List<Map> queryHyByTime = task.hyService.queryHyByEndTime(mac, endtime, endtime);
		
//		GS1解除告警，仪器告警过程中，阀值突然改变并且设置仪器
			String temperature = hy.getTemperature();
        	String humidity = hy.getHumidity();
        	String ele = hy.getElectric();
			
//			符合阀值范围直接更新
			List<Map> warnDatas = task.hyService.getWarnDatas(endtime, endtime);
			if(null != warnDatas && warnDatas.size()>0) {
				warnDatas.parallelStream().filter(w -> hy.getMac().equals(w.get("mac"))).map(wd -> {
					HumSocketHandler hs = new HumSocketHandler();
					HygrothermographWarn hgw = new HygrothermographWarn();
					hgw.setWarnnum(wd.get("warnnum").toString());
					hgw.setMac(mac);
					hgw.setRelieveTime(cur_time);
					
					String humidityfitted = "";
		        	String temperaturefitted = "";
//		    		记录解除温度告警
					if(null !=wd.get("temperaturefitted") && !StringUtils.isEmpty(wd.get("temperaturefitted").toString())
		    				&& null != wd.get("temperature") && !StringUtils.isEmpty(wd.get("temperature").toString())
		    				 && !StringUtils.isEmpty(temperature)) {
		    			temperaturefitted = wd.get("temperaturefitted").toString();
		    			Boolean tp = getWarnBoolean(temperaturefitted,temperature);
		    			
		    			if(tp) {
		    				hgw.setRelieveTemp(temperature);//解除温度值
		    				HygrothermographWarn hgy = updateWarnValue(hgw);
		    				hs.updateGS1WarnValue(hgy);
		    			}
		    			
		    		}
//		    		记录解除湿度告警
					if(null !=wd.get("humidityfitted") && !StringUtils.isEmpty(wd.get("humidityfitted").toString())
							&& null != wd.get("humidity") && !StringUtils.isEmpty(wd.get("humidity").toString())
		    				 && !StringUtils.isEmpty(humidity)) {
    	    			humidityfitted = wd.get("humidityfitted").toString();
    	    			Boolean hum = getWarnBoolean(humidityfitted,humidity);
    	    			
    	    			if(hum) {
    	    				hgw.setRelieveHum(humidity);//解除湿度值
		    				HygrothermographWarn hgy = updateWarnValue(hgw);
		    				hs.updateGS1WarnValue(hgy);
		    			}
    	    			
    	    		}
//					记录解除低电量告警
					if(	(null == wd.get("humidity") || StringUtils.isEmpty(wd.get("humidity").toString()))
							&& (null == wd.get("temperature") || StringUtils.isEmpty(wd.get("temperature").toString()))
							&& null != wd.get("electric") && !StringUtils.isEmpty(wd.get("electric").toString())
							&& null != ele && !StringUtils.isEmpty(ele)) {
    					int electric = Integer.parseInt(ele);
    	    			if(electric >= UserParameterField.GS1_electric && "14".equals(hgw.getWarnnum())) {
    	    				//解除电量告警值
		    				HygrothermographWarn hgy = updateWarnValue(hgw);
		    				hs.updateGS1WarnValue(hgy);
		    			}
    	    		}
					
					return -1;
				}).collect(Collectors.toList());	
			}
			
		
//		保存时间限制
		Integer saveinterval = UserParameterField.MINUTE_;
		List<Humiture> humSection = task.hyService.getHumSection(mac);
		for (Humiture humiture : humSection) {
			if(!StringUtils.isEmpty(humiture.getSaveinterval())) {
				saveinterval = Integer.parseInt(humiture.getSaveinterval());
			}
		}
		
		for (Map map : queryHyByTime) {
			Long i = (Long)map.get("count(*)");
			if(i==0 && minute%saveinterval == 0) {
				task.hyService.saveHy(hy);
			}
		}
   
    }
/*	public void addData(hygrothermograph hy){
		timeUtiles time = new timeUtiles();
		String cur_time = time.getCurrenttime();
		String substring = cur_time.substring(0, cur_time.length()-2);
		String endtime = substring+"00";
		hy.setCurrentTime(endtime);
		
		String startime = "";
		try {
			startime = time.timeReduce(endtime, 1*60);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String mac = hy.getMac();
		//五分钟更新一次
		List<hygrothermograph> queryhy = task.hyService.queryhy(mac, startime, endtime);
		
//		调用公共类启用方法
		if(null == queryhy || queryhy.size() == 0 ) {
			
			task.hyService.saveHy(hy);
//		设备信息已经存在，则更新
		}else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("temperature", hy.getTemperature());
			map.put("humidity", hy.getHumidity());
			map.put("currentTime", endtime);
			map.put("startime", startime);
			map.put("warnnum", hy.getWarnnum());
			map.put("mac", hy.getMac());
			
			task.hyService.updateHy(map);
		}
		
	}
*/
	
	//	历史记录数据
	public void addDataByTime(Hygrothermograph hy,String endtime){
		
		
		
//		历史记录时间能被10整除,并且数据库中无相同数据
		String minute_ = endtime.substring(endtime.length()-5, endtime.length()-3);
		int mintue = Integer.parseInt(minute_);
		
//		保存时间限制
		Integer saveinterval = UserParameterField.MINUTE_;
		List<Humiture> humSection = task.hyService.getHumSection(hy.getMac());
		for (Humiture humiture : humSection) {
			if(!StringUtils.isEmpty(humiture.getSaveinterval())) {
				saveinterval = Integer.parseInt(humiture.getSaveinterval());
			}
		}
		
		List<Map> queryHyByTime = task.hyService.queryHyByEndTime(hy.getMac(), endtime, endtime);

		
		for (Map map : queryHyByTime) {
			Long i = (Long)map.get("count(*)");
			if(i==0 && mintue%saveinterval == 0) {
				task.hyService.saveHy(hy);
			}
		}
		
		
	}
	//	告警记录数据更新或保存
	public void addWarnDataByTime(HygrothermographWarn hy,String endtime){
		
		/*
		1.小于20告警值重新添加，否则更新
		2.更新解除告警条件为%100之后告警数值的数据
		3.连续不插入相同告警值类型
		*/
//			默认设置低电电量
		if(Integer.parseInt(hy.getWarnnum()) == 14 || Integer.parseInt(hy.getWarnnum()) == 114) {
			hy.setElectric("25");
		}else {
			hy.setElectric("100");
		}
		
		if(Integer.parseInt(hy.getWarnnum())<20) {
			List<Map> queryHwByTime = task.hyWwarnService.queryHwByTime(hy);
			queryHwByTime.parallelStream().map(a -> {
	    		Long i = (Long)a.get("count(*)");
	    		if(i==0) {
	    			task.hyWwarnService.insertHyWarn(hy);
				}
	    		return 1;
	    	}).collect(Collectors.toList());
			
		}else if(Integer.parseInt(hy.getWarnnum())>100){
			String mac = hy.getMac();
//			解除告警值
			int warnnum_relieve = Integer.parseInt(hy.getWarnnum());
			int warnnum = warnnum_relieve%100;
			String warnnum_value = warnnum+"";
			
			hy.setRelieveHum(hy.getHumidity());
			hy.setRelieveTemp(hy.getTemperature());
			hy.setRelieveTime(endtime);
			hy.setRelieveWarn(warnnum_relieve+"");
//			清空原有对象缓存数据，赋值查询条件对象
			hy.setHumidity("");
			hy.setTemperature("");
			hy.setCurrentTime("");
			hy.setWarnnum(warnnum_value);
			
			task.hyWwarnService.updateHyWarn(hy);
		}
		
	}
	public List<Humiture> getFitted(String mac){
//		获取告警阀值
		List<Humiture> humSection = task.hyService.getHumSection(mac);
		return humSection;
		
	}
	
	
	public void update(Humiture humit) {
		 task.hyService.updateHum(humit);
	}
	
	 /*
     * 处理GS1仪器部分设置告警阀值后，仪器未记录到解除告警记录的情况
	温度已经符合修改后阀值条件,但未解除告警,此处修改阀值，电量阀值手动不进行修改，不存在此现象
	*/
    public HygrothermographWarn updateWarnValue(HygrothermographWarn hy) {
		String warnnum = hy.getWarnnum();
		String relieveWarn = "";
		if(warnnum.length() == 1) {
			relieveWarn = "10"+warnnum;
		}
		if(warnnum.length() == 2) {
			relieveWarn = "1"+warnnum;
		}
		hy.setRelieveWarn(relieveWarn);
		return hy;
    }
//	判断是否符合阀值
    public Boolean getWarnBoolean(String s,String temp_str) {
		String[] split = s.split("~");
		String s1 = "";
		String s2 = "";
		double temp = Double.parseDouble(temp_str);
		
//		阀值必须完整
        for(String d : split){
            if(StringUtils.isEmpty(s1)) {
            	s1 = d; 
            }else{
            	s2 = d;
            }
        }
        double w1 = Double.parseDouble(s1);
        double w2 = Double.parseDouble(s2);
        Boolean b = (temp>=w1 && temp<=w2);
        return b;
    }
	
}

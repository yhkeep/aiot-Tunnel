package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.example.demo.Interceptor.PassToken;
import com.example.demo.domain.Humiture;
import com.example.demo.domain.Hygrothermograph;
import com.example.demo.domain.Result;
import com.example.demo.service.hygrothermographService;
import com.example.demo.util.ExcelUtiles;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.parameter.UserParameterField;
import com.example.demo.util.result.ResultUtil;
import com.github.pagehelper.PageInfo;

@RestController
public class HygrothermographController{
	@Autowired
	private hygrothermographService hygrothermographService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	
	@RequestMapping(value = "/luzhou/hum", method = RequestMethod.GET)
    public JSONArray getHum() throws InterruptedException {
		String token = httpServletRequest.getHeader("token");  //抽取单例线程安全公共类
		String token_address = JWT.decode(token).getAudience().get(1); //项目地址
		
		Humiture humit = new Humiture();
		humit.setAddress(token_address);
//		查询mac对应的位置以及类型和科室
		List<Humiture> queryhum = hygrothermographService.queryhum(humit);
		
    	JSONArray array= JSONArray.parseArray(JSON.toJSONString(queryhum));
    	return array;
	}
	
//	查询一周最高和最低温度TODO210918
	/*@RequestMapping(value = "/luzhou/daySummary", method = RequestMethod.GET)
	public JSONArray getdaySummary(@RequestParam(value="mac",required=false)String mac) throws InterruptedException {
		List<Map> allhy = new ArrayList<>();
		Map mapall = new HashMap();
		
		List<Map> hy = new ArrayList<>();
//		获取一周的数据 （时间点查询，只需要时间起始截至时间段即可）
		for (int i = 0; i < 7; i++) {
			Calendar cal=Calendar.getInstance();
			Calendar cal1=Calendar.getInstance();
			cal.add(Calendar.DATE,-i);
			cal1.add(Calendar.DATE,-i+1);
			Date time=cal.getTime();
			Date time1=cal1.getTime();
			String get0Time = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(time);
			String currenttime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(time1);
			
			Map map = getToporMinHum(mac, get0Time, currenttime);
			hy.add(map);
		}
		mapall.put("daySummary", hy);
//		获取每小时的数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00", Locale.CHINA);// 输出北京时间
	      Date dt = new Date();
	      Calendar rightNow = Calendar.getInstance();
	      Calendar rightNow1 = Calendar.getInstance();
	      String oneHoursAgoTime="";
	      String oneHoursAgoTime1="";
	      List ls = new ArrayList<>();
//	      时间截至点需要提前到下一个时间点（i+1），才会展示本小时时间段数据，当循环dt.getHours时间之后，将获取0点信息
	      for (int i = 0; i <= dt.getHours(); i++) {
	          rightNow.setTime(dt);
	          rightNow.add(Calendar.HOUR, -i);
	          Date dt1=rightNow.getTime();
	          oneHoursAgoTime = sdf.format(dt1);
	          
	          rightNow1.setTime(dt);
	          rightNow1.add(Calendar.HOUR, -i+1);
	          Date dt2=rightNow1.getTime();
	          oneHoursAgoTime1 = sdf.format(dt2);
	          Map toporMinHum = new HashMap();
	          toporMinHum =  getToporMinHum(mac, oneHoursAgoTime, oneHoursAgoTime1);
	          ls.add(toporMinHum);
	      }
	      
	    mapall.put("hourSummary", ls);
	    allhy.add(mapall);
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(allhy));
		return array;
	}*/
	
	
	
	
	
//	查询一周最高和最低温度
	@RequestMapping(value = "/luzhou/daySummary", method = RequestMethod.GET)
	public JSONArray getdaySummary(@RequestParam(value="mac",required=false)String mac) throws InterruptedException {
		Calendar cal1=Calendar.getInstance();
		cal1.add(Calendar.DATE,-6);
		Date time1=cal1.getTime();
		String startime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(time1);
		
		timeUtiles time = new timeUtiles();
		String currenttime = time.getCurrenttime();
		
		List<Hygrothermograph> mapDatas = hygrothermographService.queryhy(mac,startime,currenttime);	
		
//		每10分钟一次数据
		Map map_new_key = new HashMap<>();
		for (Hygrothermograph hg : mapDatas) {
			String time_current = hg.getCurrentTime();
			StringBuilder sb = new StringBuilder();
//			以分钟数作为key
			String min_time_key = sb.append(time_current).replace(time_current.length()-4,time_current.length(),"0:00").toString();
			hg.setCurrentTime(min_time_key);//更新时间
			map_new_key.put(min_time_key, hg);
		}
		Iterator<Map.Entry<Object, Object>> iterator = map_new_key.entrySet().iterator();
        List ls = new ArrayList<>();
        while (iterator.hasNext()) {
             Map.Entry<Object, Object> entry = iterator.next();
			 ls.add(entry.getValue());
        }
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(ls));
		return array;
	}
    
//	 注意空指针异常处理
	 public Map getToporMinHum(String mac,String get0Time,String currenttime) {
		 
		 List<Hygrothermograph> mapDatas = hygrothermographService.queryhy(mac, get0Time, currenttime);	
			
		 Map map = new HashMap();
//		 获取最高温度和最高湿度,以及最新温湿度
		 if(mapDatas.size()>0) {
//			 先行排序，然后获取最大值，或者最小值
		        Collections.sort(mapDatas, new Comparator<Hygrothermograph>() {
		            @Override
		            public int compare(Hygrothermograph o1, Hygrothermograph o2) {
		            	
		            	if(null == o1.getHumidity() || null == o2.getHumidity()) {
		            		return 1;
		            	}
		            	double d1 = Double.parseDouble(o1.getHumidity());
		            	double d2 = Double.parseDouble(o2.getHumidity());
		            	String result1 = String.format("%.2f",d1);
		            	String result2 = String.format("%.2f",d2);
		            	double f1 = Double.parseDouble(result1);
		            	double f2 = Double.parseDouble(result2);
		                return f1>f2? -1:(f1==f2? 0:1);
		            }
		        });
		        for (int j = 0; j < mapDatas.size(); j++) {
//		        	最大湿度值
					if(j == 0) {
						map.put("tophumidity", mapDatas.get(0).getHumidity());
						
					}
//					最小湿度值
					if(j == mapDatas.size()-1) {
						map.put("minhumidity",mapDatas.get(mapDatas.size()-1).getHumidity());
					}
					
				}
		        Collections.sort(mapDatas, new Comparator<Hygrothermograph>() {
		            @Override
		            public int compare(Hygrothermograph o1, Hygrothermograph o2) {
		            	if(null == o1.getTemperature() || null == o2.getTemperature()) {
		            		return 1;
		            	}
		            	double d1 = Double.parseDouble(o1.getTemperature());
		            	double d2 = Double.parseDouble(o2.getTemperature());
		            	String result1 = String.format("%.2f",d1);
		            	String result2 = String.format("%.2f",d2);
		            	double f1 = Double.parseDouble(result1);
		            	double f2 = Double.parseDouble(result2);
		                return f1>f2? -1:(f1==f2? 0:1);
		            }
		        });
		        for (int k = 0; k < mapDatas.size(); k++) {
//		        	最大湿度值
					if(k == 0) {
						map.put("toptemperature",mapDatas.get(0).getTemperature());
						
					}
//					最小湿度值
					if(k == mapDatas.size()-1) {
						map.put("mintemperature", mapDatas.get(mapDatas.size()-1).getTemperature());
					}
				}
		        map.put("mac", mac);
		        map.put("date", get0Time);
//		        hy.add(map);
		 }
		 return map;
	 }
	 
	 
	 
//	 历史记录分页
	 @RequestMapping(value = "/luzhou/assetManagement/param", method = RequestMethod.GET)
	 @PassToken //TODO1128-3d地图临时温湿度数据获取
	public JSONArray findAssetDetail1(@RequestParam(value="currentPage",required=false,defaultValue="1")Integer currentPage,
				@RequestParam(value="startime",required=false)String startime,@RequestParam(value="endtime",required=false)String endtime,@RequestParam(value="mac",required=false)String mac) throws Exception {
//			默认首页查询
			PageInfo<Hygrothermograph> datasPage = hygrothermographService.getDatasPage(currentPage, 10, startime,endtime,mac);
			String jsonString = JSONObject.toJSONString(datasPage);
			JSONObject json = JSONObject.parseObject(jsonString);	
			JSONArray string = json.getJSONArray("list");
//			向JSONArray添加总条数
			String total = json.getString("total");
			JSONObject obj = new JSONObject();  
	        obj.put("total",total) ;  
	        string.add(obj) ;  
	        JSONArray array= JSONArray.parseArray(JSON.toJSONString(string));
			return array;	
		}
//	 温湿度区间限制
	 @RequestMapping(value = "/luzhou/section", method = RequestMethod.GET)
	public JSONArray getSection(@RequestParam(value="mac",required=false)String mac) throws InterruptedException {
		 List<Humiture> se = hygrothermographService.getHumSection(mac);
		 JSONArray array= JSONArray.parseArray(JSON.toJSONString(se));
		 return array;
	 }
	 
	 
//	 温湿度区间编辑限制, 地区超低温可控告警，持久化更方便
	 @RequestMapping(value = "/luzhou/edit", method = RequestMethod.GET)
	 public Result getSection(@RequestParam(value="mac",required=false)String mac,@RequestParam(value="freezername",required=false)String freezername, @RequestParam(value="department",required=false)String department
			 ,@RequestParam(value="temperaturefitted",required=false)String temperaturefitted,@RequestParam(value="humidityfitted",required=false)String humidityfitted,
			 @RequestParam(value="freezernumber",required=false)String freezernumber,
			 @RequestParam(value="location",required=false)String location,@RequestParam(value="type",required=false)String type,
			 @RequestParam(value="saveinterval",required=false)String saveinterval
			 ) throws InterruptedException {
		 
		 JSONArray addHumiture = new JSONArray();
		 
		 try {
			 Humiture humit = new Humiture();
			 humit.setMac(mac);
			 humit.setFreezername(freezername);
			 humit.setDepartment(department);
			 humit.setTemperaturefitted(temperaturefitted);
			 humit.setHumidityfitted(humidityfitted);
			 humit.setFreezernumber(freezernumber);
			 humit.setLocation(location);
			 humit.setType(type);
			 
//			 获取项目地址
			 String token = httpServletRequest.getHeader("token");  //抽取单例线程安全公共类
			 String token_address = JWT.decode(token).getAudience().get(1); //项目地址
//			 humit.setAddress(UserParameterField.address);
			 humit.setAddress(token_address);
			 
			 saveinterval = StringUtils.isEmpty(saveinterval)== true?UserParameterField.MINUTE_.toString():saveinterval;
			 
			 humit.setSaveinterval(saveinterval);
			 
			 hygrothermographService.updateHum(humit);
			 
			 
			} catch (Exception e) {
				// TODO: handle exception
				LogUtil.logger.error("温湿度编辑异常"+e);
				return ResultUtil.error(403, "修改信息错误");
			}
		 return ResultUtil.success(addHumiture);
	 }
	 
	 
/*	TODO1229
 *  @RequestMapping(value = "/luzhou/edit", method = RequestMethod.GET)
	 public JSONObject getSection(@RequestParam(value="mac",required=false)String mac,@RequestParam(value="freezername",required=false)String freezername, @RequestParam(value="department",required=false)String department
			 ,@RequestParam(value="temperaturefitted",required=false)String temperaturefitted,@RequestParam(value="humidityfitted",required=false)String humidityfitted,
			 @RequestParam(value="freezernumber",required=false)String freezernumber,
			 @RequestParam(value="location",required=false)String location,@RequestParam(value="type",required=false)String type,
			 @RequestParam(value="saveinterval",required=false)String saveinterval
//			 @RequestParam(value="interrecord",required=false)String interrecord
			 ) throws InterruptedException {
		 
		 JSONObject result = new JSONObject();
		 try {
			 Humiture humit = new Humiture();
			 humit.setMac(mac);
			 humit.setFreezername(freezername);
			 humit.setDepartment(department);
			 humit.setTemperaturefitted(temperaturefitted);
			 humit.setHumidityfitted(humidityfitted);
			 humit.setFreezernumber(freezernumber);
			 humit.setLocation(location);
			 humit.setType(type);
			 humit.setAddress(UserParameterField.address);
			 
//			 interrecord = StringUtils.isEmpty(interrecord)== true?UserParameterField.MINUTE_.toString():interrecord;
			 saveinterval = StringUtils.isEmpty(saveinterval)== true?UserParameterField.MINUTE_.toString():saveinterval;
			 
//			 humit.setInterrecord(interrecord);
			 humit.setSaveinterval(saveinterval);
			 
			 hygrothermographService.updateHum(humit);
			 result.put("msg", "ok");
		 } catch (Exception e) {
			 // TODO: handle exception
			 result.put("msg", "failed");
		 }
		 return result;
	 }
*/	 
//	 TODO1128设备添加
//	 @PassToken
	 @RequestMapping(value = "/huaxi/addHumiture", method = RequestMethod.POST)
	 public Result addHumiture(@RequestBody Humiture hum) throws InterruptedException {
		 
		 JSONArray addHumiture = new JSONArray();
		 try {
			 hum.setAddress("1");//默认华西
			 /*if(StringUtils.isEmpty(hum.getInterrecord())) {
				 hum.setInterrecord(UserParameterField.MINUTE_.toString());
			 }*/
//			 查询数据库确保mac地址唯一性
			 if(StringUtils.isEmpty(hum.getMac())) {
				 return ResultUtil.error(400, "添加信息不全");	
			 }
			 List<Humiture> humSection = hygrothermographService.getHumSection(hum.getMac());
			 if(humSection.size()>0) {
				 return ResultUtil.error(402, "添加信息已存在");
			 }
			 
			 if(StringUtils.isEmpty(hum.getSaveinterval())) {
				 hum.setSaveinterval(UserParameterField.MINUTE_.toString());
			 }
			 hygrothermographService.saveHumiture(hum);
			 
		 } catch (Exception e) {
			// TODO: handle exception
			return ResultUtil.error(403, "添加信息错误");
		 }
		return ResultUtil.success(addHumiture);
		 
	 }
	 
//	 删除仪器设备
	 @RequestMapping(value = "/huaxi/delHumiture", method = RequestMethod.GET)
	 public Result deleteHumiture(@RequestParam(value="delEquipment",required=true)List delEquipment) throws InterruptedException {
		 
		 JSONArray addHumiture = new JSONArray();
		 try {
//			 查询数据库确保mac地址唯一性
			 if(delEquipment.size() == 0) {
				 return ResultUtil.error(400, "信息不全");
			 }
			 hygrothermographService.delEquipment(delEquipment);
			 
			 
		 } catch (Exception e) {
			 // TODO: handle exception
			 return ResultUtil.error(403, "删除信息错误");
		 }
		 return ResultUtil.success(addHumiture);
		 
	 }
	 
//	 告警设置模块，（连续告警时间时长，告警通知方式【app,通知固定人员】，告警状态【未处理，已处理】），告警历史
	 
//	 手动解除部分超过阀值的告警，数据依旧超过阀值（告警处理方式【修改仪器告警阀值，现场温湿度调试解除告警】）
	 
	 
	@RequestMapping(value = "/huaxi/hygrothermograph/export", method = RequestMethod.GET)
//		导出公共工具类提取(华西同院登陆不区分医院地址，不同地址或不同权限登陆获取不同数据，多院平台共用user.address线程不安全)
	public void exportAsset(HttpServletResponse response,String startime,String endtime,String mac) {
//		处理资产下载范围部门控制
		String token = httpServletRequest.getHeader("token");
		String username = JWT.decode(token).getAudience().get(0); //用户名
		String address = JWT.decode(token).getAudience().get(1); //项目地址
		String department = JWT.decode(token).getAudience().get(3); //部门授权
		Humiture hum = new Humiture();
		hum.setAddress(address);
		hum.setDepartment(department); //一个或者多个部门
		List<Humiture> queryhum = hygrothermographService.queryhum(hum);
		
		
		List ls = new ArrayList<>();
//		单标签数据存档
		if(!StringUtils.isEmpty(mac)) {
			List<Hygrothermograph> queryhy = hygrothermographService.queryhy(mac,startime, endtime);
			for (Hygrothermograph hygrothermograph : queryhy) {
				if(null != queryhy && queryhy.size()>0) {
					ls.add(hygrothermograph);
				}
			}
//		该部门所有标签存档
		}else {
			for (Humiture humiture : queryhum) {
				List<Hygrothermograph> queryhy = hygrothermographService.queryhy(humiture.getMac(),startime, endtime);
				for (Hygrothermograph hygrothermograph : queryhy) {
					if(null != queryhy && queryhy.size()>0) {
						ls.add(hygrothermograph);
					}
				}			
			}	
		}
		
		
		//导出操作
		ExcelUtiles.exportExcel(ls,"温湿度明细表","Sheet0",Hygrothermograph.class,"温湿度明细.xls",response);
		
	}
	 
  }
	

 
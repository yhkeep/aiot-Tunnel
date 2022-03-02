package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.Interceptor.PassToken;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Result;
import com.example.demo.service.AssetService;
import com.example.demo.service.IbeaconGatewayService;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.result.ResultUtil;
import com.example.demo.websocket.MapSocketHandler;

@RestController
public class HistoryController {
	@Autowired
    private IbeaconGatewayService ibeaconService;
	@Autowired
	private AssetService assetService;
	
	private timeUtiles timeUtile = new timeUtiles();
	
	
	@RequestMapping(value = "/api/getAsset", method = RequestMethod.GET)
	@PassToken
	public Result findAsset() {
		JSONArray assetjson = new JSONArray();
		try {
			List<Asset> searchAssert = assetService.searchAssert("1");//3d地址
			
			/* 0814 设备数据
			 * 
			 * List<Map> collect = searchAssert.stream().map(asset -> {
				Map map = new HashMap();
//				资产id
				map.put("AssetID", asset.getAssetID());
//				一级分类名称
				map.put("Oneclassify", asset.getOneclassify());
//				二级分类名称
				map.put("Secondclassify", asset.getSecondclassify());
//				三级分类名称
				map.put("Threeclassify", asset.getThreeclassify());
//				四级分类名称
				map.put("Fourclassify",asset.getFourclassify());
//				通用名称
				map.put("GeneralName", asset.getGeneralName());
//	    		资产名称				
				map.put("AssetName", asset.getAssetName());
//				规格
				map.put("Specification", asset.getSpecification());
//				型号
				map.put("Type", asset.getType());
//				存放地点
				map.put("Location", asset.getLocation());
//				产地
				map.put("Placeoforigin", asset.getPlaceoforigin());
//				品牌
				map.put("Brand", asset.getBrand());
//				入库时间
				map.put("BuyDate",asset.getBuyDate());
//				部门编号
				map.put("Departmentcode", asset.getDepartmentcode());
//				部门名称
				map.put("Departmentroom", asset.getDepartmentroom());
//				大科室编码
				map.put("Homeofficenumber", asset.getHomeofficenumber());
//				大科室名称
				map.put("Homeofficename",asset.getHomeofficename());
//				备注
				map.put("Remark", asset.getRemark());
//				是否进口
				map.put("Isentrance", asset.getIsentrance());
//				供应商名称
				map.put("Suppliername", asset.getSuppliername());
//				生产商名称
				map.put("Generatebusinessname", asset.getGeneratebusinessname());
//				原值
				map.put("Money", asset.getMoney());
//				处置申请单号
				map.put("Applyoddnumbers", asset.getApplyoddnumbers());
//				所在科室名称  - 所属科室
				map.put("LocDept", asset.getLocDept());
//				状态
				map.put("Status", asset.getStatus());
//				建筑
				map.put("Architecture",asset.getArchitecture());
//				院区
				map.put("Academy", asset.getAcademy());
//				楼层
				map.put("Floor", asset.getFloor());
				
				return map;
			}).collect(Collectors.toList());
			assetjson = JsonUtils.list2jsonArray(collect);*/
			
//			mac不为空的设备信息
			List<Map> collect = searchAssert.stream().filter(asset -> StringUtils.isEmpty(asset.getMac()) == false).map(asset -> {
				Map map = new HashMap();
//				资产id
				map.put("AssetID", asset.getAssetID());
//				一级分类名称
				map.put("Oneclassify", asset.getOneclassify());
//				二级分类名称
				map.put("Secondclassify", asset.getSecondclassify());
//				三级分类名称
				map.put("Threeclassify", asset.getThreeclassify());
//				四级分类名称
				map.put("Fourclassify",asset.getFourclassify());
//				通用名称
				map.put("GeneralName", asset.getGeneralName());
//	    		资产名称				
				map.put("AssetName", asset.getAssetName());
//				规格
				map.put("Specification", asset.getSpecification());
//				型号
				map.put("Type", asset.getType());
//				存放地点
				map.put("Location", asset.getLocation());
//				产地
				map.put("Placeoforigin", asset.getPlaceoforigin());
//				品牌
				map.put("Brand", asset.getBrand());
//				入库时间
				map.put("BuyDate",asset.getBuyDate());
//				部门编号
				map.put("Departmentcode", asset.getDepartmentcode());
//				部门名称
				map.put("Departmentroom", asset.getDepartmentroom());
//				大科室编码
				map.put("Homeofficenumber", asset.getHomeofficenumber());
//				大科室名称
				map.put("Homeofficename",asset.getHomeofficename());
//				备注
				map.put("Remark", asset.getRemark());
//				是否进口
				map.put("Isentrance", asset.getIsentrance());
//				供应商名称
				map.put("Suppliername", asset.getSuppliername());
//				生产商名称
				map.put("Generatebusinessname", asset.getGeneratebusinessname());
//				原值
				map.put("Money", asset.getMoney());
//				处置申请单号
				map.put("Applyoddnumbers", asset.getApplyoddnumbers());
//				所在科室名称  - 所属科室
				map.put("LocDept", asset.getLocDept());
//				状态
				map.put("Status", asset.getStatus());
//				建筑
				map.put("Architecture",asset.getArchitecture());
//				院区
				map.put("Academy", asset.getAcademy());
//				楼层
				map.put("Floor", asset.getFloor());
				
				return map;
			}).collect(Collectors.toList());
			assetjson = JsonUtils.list2jsonArray(collect);
			
		} catch (Exception e) {
			LogUtil.logger.error(e+"3d地图资产数据发送失败");
			return ResultUtil.error(401, "数据获取异常");
		}
		return ResultUtil.success(assetjson);
	}
//	通过标签mac获取历史记录
//    @RequestMapping(value = "/api/history/{mac}", method = RequestMethod.GET)
//	通过资产id来获取一周内数据  (资产id可以通过正则限制格式以字母开头，以冒号：开始匹配)
	@PassToken  //放行token验证
	@RequestMapping(value = "/api/history/{AssetID}", method = RequestMethod.GET)
    public List findOneWeekHistory(@PathVariable("AssetID") String AssetID) throws ParseException {	
		
		String currentDate = getCurrenttime();
		String startTime = timeUtile.timeReduce(currentDate, 7*24*60*60);
		
		
		
		
		
//    	 限制安全人员进入访问，返回特定内容
    	
    	
    	 
		 
		 
    /*	
{Brand=威利, Status=在用, ApplyDept=住院手术室, Type=FORCE FX-8C, LocDept=住院手术室, gatewaymac=AC233FC038FF,
 GeneralName=电刀, AssetID=ZCKP2007070164, mac=C2021A0000EA, AssetName=高频电刀, timestamp=2019-08-16 16:34:35, 
=二住12楼}=datas多个网关数据

{Status=在用, ApplyDept=住院手术室, mac=C2021A0000EA,
 AssetName=高频电刀, maxRssiUpdateTime=2019-08-16 16:34:41, Brand=威利, Type=FORCE FX-8C,
  LocDept=住院手术室, gatewaymac=AC233FC038A2, GeneralName=电刀, AssetID=ZCKP2007070164, 
  timestamp=2019-08-16 16:34:40, Location=二住12楼}=datas多个网关数据
  
{Status=在用, ApplyDept=住院手术室, mac=C2021A0000EA, 
AssetName=高频电刀, maxRssiUpdateTime=2019-08-16 16:34:59,
 Brand=威利, Type=FORCE FX-8C, LocDept=住院手术室, gatewaymac=AC233FC038FF,
  GeneralName=电刀, AssetID=ZCKP2007070164, timestamp=2019-08-16 16:34:53, Location=二住12楼}=datas多*/
    	
//    	mac ====> gatewaymac   LastUpdatedTime ===>maxRssiUpdateTime  CreateTime ===》timestamp 
//    	接口对应映射关系  耗时查询0.4s，查询之后通过程序控制返回数据（减少时间）,只获取rssi最大且时间最后一条数据
    	MapSocketHandler handler = new MapSocketHandler();
		List<Map> mapDatas = handler.getMapDatas();
		
		
		List list = new ArrayList();
		Map map1 = new HashMap();

		for (Map map : mapDatas) {
    		
//    		资产id
    		map1.put("AssetID", map.get("AssetID"));
//    		通用名称
    		map1.put("GeneralName", map.get("GeneralName"));
//    		资产名称
    		map1.put("AssetName", map.get("AssetName"));
//    		规格型号
    		map1.put("Type", map.get("Type"));
//          品牌
    		map1.put("Brand", map.get("Brand"));
//          申请科室
    		map1.put("ApplyDept", map.get("ApplyDept"));
//    		放置科室    		
    		map1.put("Location", map.get("Location"));
//    		状态
    		map1.put("Status", map.get("Status"));
//    		最强信号网关
    		map1.put("MAC", map.get("gatewaymac"));
//    		创建者
    		map1.put("Createby","yanghan");
//    		创建时间
    		map1.put("CreateTime",map.get("createtime")); //sql修改
//    		更新者
    		map1.put("LastUpdatedby","yanghan"); 
//    		更新时间    		
    		map1.put("LastUpdatedTime",map.get("updatetime")); 
//    		电量
    		map1.put("electric",map.get("electric"));//电量sql修改 
    		
    		list.add(map1);
		}
		
		
    	
    	/*数据ibeacon持久化中获取，稳定，非实时
    	 * 
    	 * List list = new ArrayList();
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	for (Map datas : search) {
    		
    		
//    		资产id
    		map.put("AssetID", datas.get("AssetID"));
//    		通用名称
    		map.put("GeneralName", datas.get("GeneralName"));
//    		资产名称
    		map.put("AssetName", datas.get("AssetName"));
//    		创建时间
    		map.put("CreateTime", datas.get("timestamp"));
//    		规格型号
            map.put("Type", datas.get("Type"));
//          品牌
            map.put("Brand",datas.get("Brand"));
//          申请科室
    		map.put("ApplyDept",datas.get("ApplyDept"));
//    		放置科室
    		map.put("LocDept",datas.get("LocDept"));
//    		坐落位置
    		map.put("Location",datas.get("Location"));
//    		状态
    		map.put("Status",datas.get("Status"));
//    		最强信号网关
    		map.put("MAC",datas.get("gatewaymac"));
//    		创建者
    		map.put("Createby","yanghan");
//    		创建时间
    		map.put("CreateTime",datas.get("timestamp"));
//    		更新者
    		map.put("LastUpdatedby","yanghan");
//    		更新时间
    		map.put("LastUpdatedTime",datas.get("maxRssiUpdateTime"));
//    		电量
    		map.put("electric",datas.get("electric"));
    		list.add(map);
		}*/
    	
    	
    	
    	
    	
    	JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
    	if(list.size() == 0) {
    		return array;
    	}
    	List list2 = new ArrayList();
    	list2.add(list.get(0));
    	JSONArray array1= JSONArray.parseArray(JSON.toJSONString(list2));
//    	转为json数组，并且返回,rssi已经排序过，直接返回便是最强网关位置
    	return array1;
    }

//	不带参数(但是要加上/,如果不加上那么访问的url时候就不需要加上)
	@PassToken
	@RequestMapping(value = "/api/history/", method = RequestMethod.GET)
    public List findOneDayHistory() throws ParseException {	
		String currentDate = getCurrenttime();
		String startTime = timeUtile.timeReduce(currentDate, 24*60*60);
//    	 限制安全人员进入访问，返回特定内容
		List<Map> search = new ArrayList<>();
    	search = ibeaconService.searchOneDay(startTime,currentDate);
    	List list = new ArrayList();
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	如果没有变化数据，那么返回最后一条
    	if (search.size() == 0) {
    		search = ibeaconService.searchLastDatas();
    	}
    	for (Map datas : search) {
//    		多组数据重复录入
    		Map<String, Object> map = new HashMap<String, Object>();
//    		资产id
    		map.put("AssetID", datas.get("AssetID"));
//    		通用名称
    		map.put("GeneralName", datas.get("GeneralName"));
//    		资产名称
    		map.put("AssetName", datas.get("AssetName"));
//    		创建时间
    		map.put("CreateTime", datas.get("timestamp"));
//    		规格型号
            map.put("Type", datas.get("Type"));
//          品牌
            map.put("Brand",datas.get("Brand"));
//          申请科室
    		map.put("ApplyDept",datas.get("ApplyDept"));
//    		放置科室
    		map.put("LocDept",datas.get("LocDept"));
//    		坐落位置
    		map.put("Location",datas.get("Location"));
//    		状态
    		map.put("Status",datas.get("Status"));
//    		最强信号网关
    		map.put("MAC",datas.get("gatewaymac"));
//    		创建者
    		map.put("Createby","yanghan");
//    		创建时间
    		map.put("CreateTime",datas.get("timestamp"));
//    		更新者
    		map.put("LastUpdatedby","yanghan");
//    		更新时间
    		map.put("LastUpdatedTime",datas.get("maxRssiUpdateTime"));
    		list.add(map);
		}
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
    	return array;
	}
//	抽取一周时间变化
	public String getCurrenttime() {	
		Date date = new Date();// 转换为标准时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
        String date1 = sdf.format(date);
  	    return date1+"";
	}
}

/*
org.apache.catalina.connector.RequestFacade@1af86864request=
[{"timestamp":"2019-08-01T05:54:25Z","type":"Gateway","mac":"AC233FC001EA","gatewayFree":98,"gatewayLoad":0.05},
{"timestamp":"2019-08-01T05:54:25Z","type":"Unknown","mac":"AC233FA03208","bleName":"","rssi":-79,"rawData":"0201060303E1FF0D16E1FFA10264000832A03F23AC"},
{"timestamp":"2019-08-01T05:54:25Z","type":"S1","mac":"AC233FA018E3","bleName":"S1","rssi":-54,"battery":100,"temperature":26.69,"humidity":65.14},
{"timestamp":"2019-08-01T05:54:25Z","type":"Unknown","mac":"AC233FA018E3","bleName":"","rssi":-53,"rawData":""},
{"timestamp":"2019-08-01T05:54:26Z","type":"S1","mac":"AC233FA018E7","bleName":"S1","rssi":-40,"battery":100,"temperature":27.34,"humidity":63.04},
{"timestamp":"2019-08-01T05:54:26Z","type":"Unknown","mac":"AC233FA018E7","bleName":"","rssi":-39,"rawData":""},
{"timestamp":"2019-08-01T05:54:26Z","type":"Unknown","mac":"AC233FA031BF","bleName":"","rssi":-81,"rawData":"0201060303E1FF0D16E1FFA1026400BF31A03F23AC"},
{"timestamp":"2019-08-01T05:54:26Z","type":"S1","mac":"AC233FA018FB","bleName":"S1","rssi":-69,"battery":100,"temperature":14.97,"humidity":46.07},
{"timestamp":"2019-08-01T05:54:26Z","type":"Unknown","mac":"AC233FA018FB","bleName":"","rssi":-68,"rawData":""}]
*/

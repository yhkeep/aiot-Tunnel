package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.Interceptor.PassToken;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Check;
import com.example.demo.domain.Result;
import com.example.demo.domain.User;
import com.example.demo.service.AssetService;
import com.example.demo.service.CheckService;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.ReduceTime;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.result.ResultUtil;

@RestController
public class CheckController{
	@Autowired
	private CheckService checkService;
	@Autowired
	private AssetService assetService;
	
//	 根据房间号/资产id future科室/楼层等盘点
	@RequestMapping(value = "/huaxi/assertChecked",method = RequestMethod.POST)
    public Result assetChecked(@RequestBody List<Asset> assetls,@RequestParam(value = "checkpep", required = true) String checkpep) {
		if(StringUtils.isEmpty(assetls+"") || StringUtils.isEmpty(checkpep)) {
			return ResultUtil.error(400, "盘点信息不全");	
		}
		JSONArray userjson = new JSONArray();
    	try {
    		
    		//超过设备动态时间不进行盘点
    		ReduceTime twominute = new ReduceTime();
	        String oldTime = twominute.localTime();
	        String minutes = twominute.timeReduceTwoMinute(oldTime); 

	        for (Asset asset : assetls) {
				asset.setCheck(minutes);
			}
	        
//	        System.out.println(assetls+"=assetls资产详细");
    		
//    		查询要盘点的资产
    		List<Map> selectID = assetService.selectID(assetls);
    		
//    		减少id=，字段所占用字节
    		List<Object> collect = selectID.stream().map(obj -> obj.get("id")).collect(Collectors.toList());
    		
//    		历史盘点记录
    		Check check = new Check();
    		check.setCheckpep(checkpep);
    		check.setCheckOnlyCode(collect+"");
    		check.setCheckNum(collect.size());
    		check.setCheckTime(new timeUtiles().getCurrenttime());
    		
//    		审核人
    		checkService.addChecked(check);
    		
    		
    		userjson.add("盘点成功");    		
    		
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("资产盘点异常："+e);
			return ResultUtil.error("checked", e+"");
		}
    	return ResultUtil.success(userjson);
    }
	
//	盘点历史记录==>后期分页，分期查询处理
	@RequestMapping(value = "/huaxi/assertChecked/History",method = RequestMethod.POST)
    public Result assetCheckedHistory(@RequestBody List<User> user) {
		if(StringUtils.isEmpty(user+"")) {
			return ResultUtil.error(400, "用户信息不全");	
		}
		JSONArray checkjson = new JSONArray();
    	try {
    		List<Check> queryCheck = checkService.queryCheck();
    		checkjson = JsonUtils.list2jsonArray(queryCheck);
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("获取盘点历史记录异常："+e);
			return ResultUtil.error("checkedHistory", e+"");
		}
    	return ResultUtil.success(checkjson);
    }
	
	
//	盘点明细
	@RequestMapping(value = "/huaxi/assertCheckedDetail",method = RequestMethod.POST)
    public Result assetCheckedHistoryDetail(@RequestBody List<Check> check) {
		if(StringUtils.isEmpty(check+"")) {
			return ResultUtil.error(400, "盘点信息不全");	
		}
		JSONArray checkjson = new JSONArray();
    	try {
//    		string -> list
    		for (Check check1 : check) {
    			String checkOnlyCode = check1.getCheckOnlyCode();
        		List asList = Arrays.asList(checkOnlyCode.substring(1,checkOnlyCode.length()-1).split(","));
        		List<Asset> queryAssetCheck = assetService.queryAssetByAssetID(asList);
        		checkjson = JsonUtils.list2jsonArray(queryAssetCheck);
			}
    		
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("获取盘点历史明细异常："+e);
			return ResultUtil.error("checkedHistoryDetail", e+"");
		}
    	return ResultUtil.success(checkjson);
    }
	
}
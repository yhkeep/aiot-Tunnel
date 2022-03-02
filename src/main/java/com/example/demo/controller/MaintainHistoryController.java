package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Lendouthistory;
import com.example.demo.domain.Maintainhistory;
import com.example.demo.domain.Result;
import com.example.demo.service.AssetService;
import com.example.demo.service.LendoutHistoryService;
import com.example.demo.service.MaintainHistoryService;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.FileUtil.FlileUtile;
import com.example.demo.util.result.ResultUtil;

@RestController
public class MaintainHistoryController{
	@Autowired
	private MaintainHistoryService maintainHistoryService;
	@Autowired
    private AssetService assetService;
	@Autowired
	private LendoutHistoryService lendoutHistoryService;
	@Autowired
	private MaintainHistoryService maintainService;
	
	private final ResourceLoader resourceLoader;
	@Autowired
	public MaintainHistoryController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	/*
	assetstatustype = 1 维修登记
	assetstatustype = 2 维修完成
	assetstatustype = 3 外借登记
	assetstatustype = 4 外借归还
	
	*/
	
	@RequestMapping(value="/maintainhistory/uploadimage",method = RequestMethod.POST)
    public Result uploadImage(@RequestBody(required = false) Map paramMap){
    	
    	
		JSONArray userjson = new JSONArray();
		Map<String,Object> map = new HashMap<>();
		try {
			
			if(paramMap == null || StringUtils.isEmpty(paramMap.get("maintainhistoryonlyCode").toString())
					|| StringUtils.isEmpty(paramMap.get("status").toString()) || StringUtils.isEmpty(paramMap.get("assetstatustype").toString())) {
				return ResultUtil.error(400, "参数错误");
			}
			
			FlileUtile fi = new FlileUtile();
	        String path = fi.getPath();
	        
	        String handlingPeople_image = "";
	        String buttingPeople_image = "";
	        String handoverimage_image = "";
//	        申请人签名照
	        boolean handlingPeople = paramMap.containsKey("handlingPeople");
	        if(handlingPeople && !StringUtils.isEmpty(paramMap.get("handlingPeople").toString())) {
	        	String prop = paramMap.get("handlingPeople").toString();
		        String prop_re = prop.substring(prop.indexOf(",")+1);
		        handlingPeople_image = path+"handlingPeople/"+System.currentTimeMillis();
				String realleftImagesPath = handlingPeople_image+".png";
				fi.GenerateImage(prop_re,realleftImagesPath);
	        }
//	        交接人签名照
	        boolean buttingPeople = paramMap.containsKey("buttingPeople");
	        if(buttingPeople && !StringUtils.isEmpty(paramMap.get("buttingPeople").toString())) {
	        	String prop = paramMap.get("buttingPeople").toString();
		        String prop_re = prop.substring(prop.indexOf(",")+1);
		        buttingPeople_image = path+"buttingPeople/"+System.currentTimeMillis();
				String realleftImagesPath = buttingPeople_image+".png";
				fi.GenerateImage(prop_re,realleftImagesPath);
	        }
//	        交接资产图片
	        boolean handoverimage = paramMap.containsKey("handoverimage");
	        if(handoverimage && !StringUtils.isEmpty(paramMap.get("handoverimage").toString())) {
	        	String prop = paramMap.get("handoverimage").toString();
		        String prop_re = prop.substring(prop.indexOf(",")+1);
		        handoverimage_image = path+"handoverimage/"+System.currentTimeMillis();
				String realleftImagesPath = handoverimage_image+".png";
				fi.GenerateImage(prop_re,realleftImagesPath);
	        }
//	        持久化数据库
	        Maintainhistory mih = new Maintainhistory();
	        mih.setApplaydepartment(paramMap.get("applaydepartment").toString());
	        mih.setHandoverdepartment(paramMap.get("handoverdepartment").toString());
	        mih.setApplaypeople(paramMap.get("applaypeople").toString());
	        mih.setSuccessorpeople(paramMap.get("successorpeople").toString());
	        mih.setHandlingPeople(handlingPeople_image);
	        mih.setButtingPeople(buttingPeople_image);
	        mih.setHandovertime(new timeUtiles().getCurrenttime());
	        mih.setEstimatedtime(paramMap.get("estimatedtime").toString());
	        mih.setHandoverimage(handoverimage_image);
	        mih.setMaintainhistoryonlyCode(paramMap.get("maintainhistoryonlyCode").toString());
	        mih.setAssetstatustype(paramMap.get("assetstatustype").toString());
	        mih.setRemark(paramMap.get("remark").toString());
	        maintainHistoryService.insertMaintain(mih);
//	        修改资产状态
	        Asset asset = new Asset();
	        asset.setStatus(paramMap.get("status").toString());
	        asset.setAssetID(paramMap.get("maintainhistoryonlyCode").toString());
	        assetService.updateState(asset);
		} catch (Exception e) {
			LogUtil.logger.error("资产维修记录异常:"+e);
			return ResultUtil.error("maintainhistory维修记录失败", e+"");
		}
		return ResultUtil.success();

	}
	
//	外借，分表
	@RequestMapping(value="/lendouthistory/uploadimage",method = RequestMethod.POST)
    public Result lendout(@RequestBody(required = false) Map paramMap){
    	
    	
		JSONArray userjson = new JSONArray();
		Map<String,Object> map = new HashMap<>();
		try {
			
			if(paramMap == null || StringUtils.isEmpty(paramMap.get("maintainhistoryonlyCode").toString())
					|| StringUtils.isEmpty(paramMap.get("status").toString()) || StringUtils.isEmpty(paramMap.get("assetstatustype").toString())) {
				return ResultUtil.error(400, "参数错误");
			}
			
			FlileUtile fi = new FlileUtile();
	        String path = fi.getPath();
	        
	        String handlingPeople_image = "";
	        String buttingPeople_image = "";
	        String handoverimage_image = "";
//	        申请人签名照
	        boolean handlingPeople = paramMap.containsKey("handlingPeople");
	        if(handlingPeople && !StringUtils.isEmpty(paramMap.get("handlingPeople").toString())) {
	        	String prop = paramMap.get("handlingPeople").toString();
		        String prop_re = prop.substring(prop.indexOf(",")+1);
		        handlingPeople_image = path+"handlingPeople/"+System.currentTimeMillis();
				String realleftImagesPath = handlingPeople_image+".png";
				fi.GenerateImage(prop_re,realleftImagesPath);
	        }
//	        交接人签名照
	        boolean buttingPeople = paramMap.containsKey("buttingPeople");
	        if(buttingPeople && !StringUtils.isEmpty(paramMap.get("buttingPeople").toString())) {
	        	String prop = paramMap.get("buttingPeople").toString();
		        String prop_re = prop.substring(prop.indexOf(",")+1);
		        buttingPeople_image = path+"buttingPeople/"+System.currentTimeMillis();
				String realleftImagesPath = buttingPeople_image+".png";
				fi.GenerateImage(prop_re,realleftImagesPath);
	        }
//	        交接资产图片
	        boolean handoverimage = paramMap.containsKey("handoverimage");
	        if(handoverimage && !StringUtils.isEmpty(paramMap.get("handoverimage").toString())) {
	        	String prop = paramMap.get("handoverimage").toString();
		        String prop_re = prop.substring(prop.indexOf(",")+1);
		        handoverimage_image = path+"handoverimage/"+System.currentTimeMillis();
				String realleftImagesPath = handoverimage_image+".png";
				fi.GenerateImage(prop_re,realleftImagesPath);
	        }
//	        持久化数据库
	        Lendouthistory mih = new Lendouthistory();
	        mih.setApplaydepartment(paramMap.get("applaydepartment").toString());
	        mih.setHandoverdepartment(paramMap.get("handoverdepartment").toString());
	        mih.setApplaypeople(paramMap.get("applaypeople").toString());
	        mih.setSuccessorpeople(paramMap.get("successorpeople").toString());
	        mih.setHandlingPeople(handlingPeople_image);
	        mih.setButtingPeople(buttingPeople_image);
	        mih.setHandovertime(new timeUtiles().getCurrenttime());
	        mih.setEstimatedtime(paramMap.get("estimatedtime").toString());
	        mih.setHandoverimage(handoverimage_image);
	        mih.setMaintainhistoryonlyCode(paramMap.get("maintainhistoryonlyCode").toString());
	        mih.setAssetstatustype(paramMap.get("assetstatustype").toString());
	        mih.setRemark(paramMap.get("remark").toString());
	        
	        lendoutHistoryService.insertLendout(mih);
//	        修改资产状态
	        Asset asset = new Asset();
	        asset.setStatus(paramMap.get("status").toString());
	        asset.setAssetID(paramMap.get("maintainhistoryonlyCode").toString());
	        assetService.updateState(asset);
		} catch (Exception e) {
			LogUtil.logger.error("资产维修记录异常:"+e);
			return ResultUtil.error("maintainhistory维修记录失败", e+"");
		}
		return ResultUtil.success();

	}
	
	
	@RequestMapping(value="/maintainhistory/query",method = RequestMethod.GET)
	public Result queryMaintainHistory(@RequestParam(value="assetID",required=true)String assetID,
			@RequestParam(value="timeStart",required=true)String timeStart
			,@RequestParam(value="timeEnd",required=true)String timeEnd){
		JSONArray maintainHistoryJson = new JSONArray();
		List<Map> queryUnionMaintainHistory;
		try {
			queryUnionMaintainHistory = maintainService.queryUnionMaintainHistory(assetID, timeStart, timeEnd);
//			base64切换
			FlileUtile fi = new FlileUtile();
			List<Map> collect = queryUnionMaintainHistory.stream().map(qh -> {
				
				if(null != qh && null != qh.get("buttingPeople")  && !StringUtils.isEmpty(qh.get("buttingPeople").toString())) {
					String buttingPeople = qh.get("buttingPeople").toString();
					buttingPeople = buttingPeople+".png";
					String buttingPeople_base64 = fi.fileToBase64(buttingPeople);
					qh.put("buttingPeople", "data:image/png;base64,"+buttingPeople_base64);
				}
				if(null != qh && null != qh.get("handlingPeople") && !StringUtils.isEmpty(qh.get("handlingPeople").toString())) {
					String handlingPeople = qh.get("handlingPeople").toString();
					handlingPeople = handlingPeople+".png";
					String handlingPeople_base64 = fi.fileToBase64(handlingPeople);
					qh.put("handlingPeople", "data:image/png;base64,"+handlingPeople_base64);
				}
				if(null != qh && null != qh.get("handoverimage") && !StringUtils.isEmpty(qh.get("handoverimage").toString())) {
					String handoverimage = qh.get("handoverimage").toString();
					handoverimage = handoverimage+".png";
					String handoverimage_base64 = fi.fileToBase64(handoverimage);
					qh.put("handoverimage", "data:image/png;base64,"+handoverimage_base64);
				}
				return qh;
			}).collect(Collectors.toList());
			
			maintainHistoryJson = JsonUtils.list2jsonArray(collect);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.logger.error("历史记录查询异常:"+e);
			return ResultUtil.error("历史记录查询失败", e+"");
		}
		return ResultUtil.success(maintainHistoryJson);
	}
	
//	维修图片展示接口
	@RequestMapping(value = "/maintainhistory/showImage",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
  	@ResponseBody
  	public ResponseEntity<?> showImage(@RequestBody(required = false)Map paramMap){
	   
//     路径拼接图片
  		try {
  			String imageulr = "";
  			if(paramMap.containsKey("buttingPeople") && !StringUtils.isEmpty(paramMap.get("buttingPeople").toString())){
  				imageulr = paramMap.get("buttingPeople").toString()+".png";
  			}
  			if(paramMap.containsKey("handlingPeople") && !StringUtils.isEmpty(paramMap.get("handlingPeople").toString())){
  				imageulr = paramMap.get("handlingPeople").toString()+".png";
  			}
  			if(paramMap.containsKey("handoverimage") && !StringUtils.isEmpty(paramMap.get("handoverimage").toString())){
  				imageulr = paramMap.get("handoverimage").toString()+".png";
  			}
  			return ResponseEntity.ok(resourceLoader.getResource("file:/" + imageulr));	
  			
  		} catch (Exception e) {
  			return ResponseEntity.notFound().build();
  		}
  	}
	
	
}
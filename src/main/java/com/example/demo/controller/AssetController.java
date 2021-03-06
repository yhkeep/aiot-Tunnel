package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.example.demo.Interceptor.PassToken;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Check;
import com.example.demo.domain.Result;
import com.example.demo.domain.User;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.AssetService;
import com.example.demo.service.CheckService;
import com.example.demo.service.ReceivedService;
import com.example.demo.service.UserService;
import com.example.demo.util.ExcelUtiles;
import com.example.demo.util.ImageUtils;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.ReduceTime;
import com.example.demo.util.timeUtiles;
import com.example.demo.util.parameter.ParameterField;
import com.example.demo.util.parameter.UserParameterField;
import com.example.demo.util.result.ResultUtil;
import com.github.pagehelper.PageInfo;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;

//@Controller//??????jsp??????
@RestController
public class AssetController{
//??????????????????
	private final ResourceLoader resourceLoader;
	 
//	private static  UserController user = new UserController();
	
	@Autowired
	public AssetController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	@Autowired
	private ReceivedService reService;
	@Autowired
    private AssetService assetService;
	@Autowired
	private UserService userService;
	@Autowired
	private CheckService checkService;
	@Resource
    private RedisUtils redisUtil;
	
	
//	??????token
	@Autowired
	private HttpServletRequest httpServletRequest;

//	????????????????????????
	private  String imagesPath = "";
//	???????????????
	private  String comImagesPath = "";
	
	private  String leftImagesPath = "";
	
	private  String allroundImagesPath = "";
	
	private  String aboveImagesPath = "";
	
	private  String rightImagesPath = "";
	
	private  String paperlabelImagePath = "";
	
	private  String onecodelableImagePath = "";
	
//	?????????????????????????????????????????????????????????????????????
	
	@RequestMapping(value = "/huaxi/asset", method = RequestMethod.GET)
    public JSONArray findAsset() {
		JSONArray assetjson = new JSONArray();
		try {
			List<Asset> searchAssert = assetService.searchAssert(UserParameterField.address);
			assetjson = JsonUtils.list2jsonArray(searchAssert);
		} catch (Exception e) {
			LogUtil.logger.error(e+"");
		}
//		return ResultUtil.success(assetjson);
		return assetjson;	
	}
	
	
	/*
	 * ??????jsp??????
	 * @RequestMapping(value = "/websocket", method = RequestMethod.GET)
	 public String index(Model m){
        m.addAttribute("now",DateFormat.getDateTimeInstance().format(new Date()));
        return "index";  //????????????@Controller??????properties????????????????????????????????????????????????index.jsp
    }*/
	
//	???????????????isShowMac,isShowBuyDate,isShowMoney,isShowElectric
	@RequestMapping(value = "/huaxi/assetManagement/param", method = RequestMethod.POST)
	public JSONArray findAssetDetail1(
			@RequestParam(value="currentPage",required=false,defaultValue="1")Integer currentPage,
			@RequestParam(value="assetName",required=false)String assetName,
			@RequestParam(value="memorydepart",required=false)String memorydepart,
			@RequestParam(value="memorylocDept",required=false)String memorylocDept,
			@RequestParam(value="isShowMac",required=false)String isShowMac,
			@RequestParam(value="isShowBuyDate",required=false)String isShowBuyDate,
			@RequestParam(value="isShowMoney",required=false)String isShowMoney,
			@RequestParam(value="isShowElectric",required=false)String isShowElectric,
			@RequestParam(value="assetID",required=false)String assetID,
			@RequestParam(value="mac",required=false)String mac,
			@RequestParam(value="pageSize",required=false)String pageSize,
			@RequestParam(value="oneclassify",required=false)String oneclassify,
			@RequestParam(value="secondclassify",required=false)String secondclassify,
			@RequestParam(value="threeclassify",required=false)String threeclassify,
			@RequestParam(value="fourclassify",required=false)String fourclassify,
			@RequestParam(value="status",required=false)String status,//TODO1111
			
			@RequestParam(value="isShowSpecification",required=false)String isShowSpecification,
			@RequestParam(value="isShowType",required=false)String isShowType,
			@RequestParam(value="isShowLocation",required=false)String isShowLocation,
			@RequestParam(value="isShowPlaceoforigin",required=false)String isShowPlaceoforigin,
			@RequestParam(value="isShowBrand",required=false)String isShowBrand,
			@RequestParam(value="isShowDepartmentcode",required=false)String isShowDepartmentcode,
			@RequestParam(value="isShowDepartmentroom",required=false)String isShowDepartmentroom,
			@RequestParam(value="isShowHomeofficenumber",required=false)String isShowHomeofficenumber,
			@RequestParam(value="isShowHomeofficename",required=false)String isShowHomeofficename,
			@RequestParam(value="isShowIsentrance",required=false)String isShowIsentrance,
			@RequestParam(value="isShowSuppliername",required=false)String isShowSuppliername,
			@RequestParam(value="isShowGeneratebusinessname",required=false)String isShowGeneratebusinessname,
			@RequestParam(value="isShowApplyoddnumbers",required=false)String isShowApplyoddnumbers,
			@RequestParam(value="isShowLocDept",required=false)String isShowLocDept,
			@RequestParam(value="isShowStatus",required=false)String isShowStatus
			
			
			) throws Exception {
		String token = "";
		String username = "";
		String address = "";
		JSONArray string = new JSONArray();
		try {
			token = httpServletRequest.getHeader("token");
			username = JWT.decode(token).getAudience().get(0); //?????????
			address = JWT.decode(token).getAudience().get(1); //????????????
//			User???????????????????????????
			User userinfo = new User();
			userinfo.setUsername(username); //??????
			
//			??????????????????????????????????????????????????????????????????????????????
			userinfo.setMemorylocDept(memorylocDept);
			userinfo.setMemorydepart(memorydepart);
			userinfo.setMemoryoneclassify(oneclassify);
			userinfo.setMemorysecondclassify(secondclassify);
			userinfo.setMemorythreeclassify(threeclassify);
			userinfo.setMemoryfourclassify(fourclassify);
			

//			??????????????????
			userinfo.setIsShowMac(isShowMac);
			userinfo.setIsShowBuyDate(isShowBuyDate);
			userinfo.setIsShowMoney(isShowMoney);
			userinfo.setIsShowElectric(isShowElectric);
			userinfo.setIsShowSpecification(isShowSpecification);
			userinfo.setIsShowType(isShowType);
			userinfo.setIsShowLocation(isShowLocation);
			userinfo.setIsShowPlaceoforigin(isShowPlaceoforigin);
			userinfo.setIsShowBrand(isShowBrand);
			userinfo.setIsShowDepartmentcode(isShowDepartmentcode);
			userinfo.setIsShowDepartmentroom(isShowDepartmentroom);
			userinfo.setIsShowHomeofficenumber(isShowHomeofficenumber);
			userinfo.setIsShowHomeofficename(isShowHomeofficename);
			userinfo.setIsShowIsentrance(isShowIsentrance);
			userinfo.setIsShowSuppliername(isShowSuppliername);
			userinfo.setIsShowGeneratebusinessname(isShowGeneratebusinessname);
			userinfo.setIsShowApplyoddnumbers(isShowApplyoddnumbers);
			userinfo.setIsShowLocDept(isShowLocDept);
			userinfo.setIsShowStatus(isShowStatus);
			userService.updateUser(userinfo);
			
//			LogUtil.logger.info("userinfo"+userinfo); ??????????????????
//			??????????????????
			Map<String,Object> map=new HashMap<String,Object>();
	        map.put("assetName",assetName);
	        map.put("departmentroom",memorydepart);
	        map.put("locDept",memorylocDept);
	        map.put("address",address);
	        map.put("assetID",assetID);
	        map.put("mac",mac);
	        map.put("status", status);//TODO1111
	        map.put("oneclassify", oneclassify);
	        map.put("secondclassify", secondclassify);
	        map.put("threeclassify", threeclassify);
	        map.put("fourclassify", fourclassify);
//	        token?????????department????????????
	        String department = JWT.decode(token).getAudience().get(3);
//	         ????????????????????????
	        String[] split = department.split(",");
	        List<String> department_list = Arrays.asList(split);
	        map.put("department",department_list);
	        
	        
	        Integer pagesize = StringUtils.isEmpty(pageSize)?ParameterField.pageSize:Integer.parseInt(pageSize);
			PageInfo<Asset> datasPage = assetService.getDatasPage(currentPage,pagesize,map);
			
			String jsonString = JSONObject.toJSONString(datasPage);
			JSONObject json = JSONObject.parseObject(jsonString);	
			string = json.getJSONArray("list");
//			???JSONArray???????????????
			String total = json.getString("total");
			JSONObject obj = new JSONObject();  
	        obj.put("total",total) ;
	        obj.put("timeTest",new timeUtiles().timeMeter());
	        string.add(obj) ; 
		} catch (Exception e) {
			// TODO: handle exception
			
			LogUtil.logger.info("????????????????????????"+e);
		}
		return string;	
	}
//	????????????
//	??????????????????????????????
	@RequestMapping(value = "/huaxi/upload", method = RequestMethod.POST)
	public String addAsset(@RequestBody List<Asset> asset) throws Exception {
		JSONObject result = new JSONObject();
		if(asset != null) {
			for (Asset assetdatas : asset) {
//				????????????
				if("" == UserParameterField.address || !UserParameterField.address.equals(assetdatas.getAddress())) {
					result.put("message", "?????????????????????");
//					return ResultUtil.error("message",  "?????????????????????");
					return result.toJSONString();
				}
				
//				??????mac??????(???null),??????id??????
				List<Asset> searchAssert = assetService.searchAssert(assetdatas.getAddress());
				for (Asset asset_data : searchAssert) {
					String oldAssetID = asset_data.getAssetID();
					String oldmac = asset_data.getMac();
					try {
						if(oldAssetID.equals(assetdatas.getAssetID()) ||
								 !StringUtils.isEmpty(assetdatas.getMac()) && oldmac.equals(assetdatas.getMac()) ) {
							result.put("message", "??????id???mac???????????????");
							return result.toJSONString(); 
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
//					???????????????
				try {
//					TODO1023?????????????????????
					
					
//					????????????,??????????????????
					assetdatas.setImageUrl(imagesPath);
//					??????????????????
					assetdatas.setCompressImageUrl(comImagesPath);
//					????????????????????????????????????
					assetdatas.setAboveImageUrl(aboveImagesPath);
					
					assetdatas.setAllroundImageUrl(allroundImagesPath);
					
					assetdatas.setLeftImageUrl(leftImagesPath);
					
					assetdatas.setRightImageUrl(rightImagesPath);
					
					assetdatas.setPaperlabelImageUrl(paperlabelImagePath);
					
					assetdatas.setOnecodelableImageUrl(onecodelableImagePath);
//					????????????
					assetService.insertAssetDatas(assetdatas);
//					??????????????????????????????????????????????????????????????????????????????
					
					imagesPath = "";
					
					comImagesPath = "";
					
					leftImagesPath = "";
					
					allroundImagesPath = "";
					
					aboveImagesPath = "";
					
					rightImagesPath = "";
					
					paperlabelImagePath = "";
					
					onecodelableImagePath = "";
					
					
//					?????????????????????????????????????????????
					findAssetAndRedis();
					
					
					result.put("msg", "ok");
				} catch (Exception e) {
					// TODO: handle exception
					LogUtil.logger.error(e+"??????????????????");
					result.put("msg", "failed");
				}
			}
		}

//		????????????????????????
		return result.toJSONString();	
	}
//	????????????
	@RequestMapping(value = "/huaxi/del", method = RequestMethod.GET)
	public String deleteAsset(@RequestParam(value="assetID",required=true)List assetID) {
		
//		??????????????????id??????
		JSONObject result = new JSONObject();
		try {
			if(assetID instanceof List) {
				assetService.delAssetData(assetID);
				result.put("msg", "ok");	
			}else {
				result.put("msg", "failed");
			}
			
//			?????????????????????????????????
			findAssetAndRedis();
	    	
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "failed");
		}
//		??????????????????????????????
		return result.toJSONString();	
	}
	
	
//	??????pojo
	@RequestMapping(value = "/huaxi/update", method = RequestMethod.POST)
	public String updateAsset( @RequestBody List<Asset> asset) {
		JSONObject result = new JSONObject();
		for (Asset assetdatas : asset) {
//			????????????id,??????mac???????????????
			List<Asset> searchAssert = assetService.searchAssert(UserParameterField.address);
			for (Asset asset_data : searchAssert) {
				String oldAssetID = asset_data.getAssetID();
				String oldmac = asset_data.getMac();
				
//				????????????id??????mac??????????????????mac??????????????????
				if(!StringUtils.isEmpty(assetdatas.getMac()) && !StringUtils.isEmpty(oldmac) && oldmac.equals(assetdatas.getMac())  
						&& !assetdatas.getAssetID().equals(asset_data.getAssetID())
						) {
					result.put("message", "mac???????????????");
					return result.toJSONString(); 
				}
			}
			
			
			
//			???????????????
			if(StringUtils.isEmpty(assetdatas.getAmount()) || assetdatas.getAmount().equals("--")) {
				assetdatas.setAmount("1");
			}
			if(StringUtils.isEmpty(assetdatas.getMoney()) || assetdatas.getMoney().equals("--")){
				assetdatas.setMoney(new BigDecimal(0));
			}
			
			
			try {
//				??????????????????
				if(!StringUtils.isEmpty(assetdatas.getCheck())) {
//					??????????????????
					assetService.updateCheck(assetdatas);
					result.put("msg", "ok");
					continue; //????????????
					
				}
				
//				???????????????uploadimage
				/*if(StringUtils.isEmpty(imagesPath) && StringUtils.isEmpty(comImagesPath) 
						&& StringUtils.isEmpty(leftImagesPath) && StringUtils.isEmpty(rightImagesPath)
						&& StringUtils.isEmpty(aboveImagesPath) && StringUtils.isEmpty(allroundImagesPath)) {
					assetdatas.setImageUrl(imagesPath);
					assetdatas.setCompressImageUrl(comImagesPath);
					assetdatas.setLeftImageUrl(leftImagesPath);
					assetdatas.setRightImageUrl(rightImagesPath);
					assetdatas.setAboveImageUrl(aboveImagesPath);
					assetdatas.setAllroundImageUrl(allroundImagesPath);
					assetdatas.setPaperlabelImageUrl(paperlabelImagePath);
					assetdatas.setOnecodelableImageUrl(onecodelableImagePath);
					assetService.updatePIG(assetdatas); //????????????
					assetService.updateAsset(assetdatas);
				}else {
//					?????????????????????????????????????????????,??????????????????????????????????????? ???uploadimage????????????????????????
					assetdatas.setImageUrl(imagesPath);
					assetdatas.setCompressImageUrl(comImagesPath);
					assetdatas.setLeftImageUrl(leftImagesPath);
					assetdatas.setRightImageUrl(rightImagesPath);
					assetdatas.setAboveImageUrl(aboveImagesPath);
					assetdatas.setAllroundImageUrl(allroundImagesPath);
					assetdatas.setPaperlabelImageUrl(paperlabelImagePath);
					assetdatas.setOnecodelableImageUrl(onecodelableImagePath);
					assetService.updateAsset(assetdatas);
				}*/
				assetdatas.setImageUrl(imagesPath);
				assetdatas.setCompressImageUrl(comImagesPath);
				assetdatas.setLeftImageUrl(leftImagesPath);
				assetdatas.setRightImageUrl(rightImagesPath);
				assetdatas.setAboveImageUrl(aboveImagesPath);
				assetdatas.setAllroundImageUrl(allroundImagesPath);
				assetdatas.setPaperlabelImageUrl(paperlabelImagePath);
				assetdatas.setOnecodelableImageUrl(onecodelableImagePath);
				
/*TODO1027		assetdatas.setImageUrl("usr/local/share/huaxi/"+imagesPath);
				assetdatas.setCompressImageUrl("usr/local/share/huaxi/"+comImagesPath);
				assetdatas.setLeftImageUrl("usr/local/share/huaxi/"+leftImagesPath);
				assetdatas.setRightImageUrl("usr/local/share/huaxi/"+rightImagesPath);
				assetdatas.setAboveImageUrl("usr/local/share/huaxi/"+aboveImagesPath);
				assetdatas.setAllroundImageUrl("usr/local/share/huaxi/"+allroundImagesPath);
				assetdatas.setPaperlabelImageUrl("usr/local/share/huaxi/"+paperlabelImagePath);
				assetdatas.setOnecodelableImageUrl("usr/local/share/huaxi/"+onecodelableImagePath);
*/				
				/*LogUtil.logger.info(leftImagesPath+"=leftImagesPath"+imagesPath +"rightImagesPath"+rightImagesPath
						+"=aboveImagesPath" +aboveImagesPath +"=allroundImagesPath"+allroundImagesPath+"=paperlabelImagePath"+paperlabelImagePath
						+"=onecodelableImagePath"+onecodelableImagePath);*/
				
//				??????????????????????????????????????????
				assetService.updatePIG(assetdatas); //????????????
				
				assetService.updateAsset(assetdatas);

				
				imagesPath = "";
				
				comImagesPath = "";
				
				leftImagesPath = "";
				
				allroundImagesPath = "";
				
				aboveImagesPath = "";
				
				rightImagesPath = "";
				
				paperlabelImagePath = "";
				
				onecodelableImagePath = "";
				
				/*TODO1028
				 * 
				 * if(StringUtils.isEmpty(comImagesPath) || StringUtils.isEmpty(imagesPath) || StringUtils.isEmpty(leftImagesPath) || StringUtils.isEmpty(rightImagesPath) ||
						StringUtils.isEmpty(aboveImagesPath) || StringUtils.isEmpty(allroundImagesPath) || StringUtils.isEmpty(paperlabelImagePath) || 
						StringUtils.isEmpty(onecodelableImagePath)) {
					
					assetService.updatePIG(assetdatas); //????????????
					
					assetService.updateAsset(assetdatas);

//					????????????????????????????????????
					imagesPath = "";
					
					comImagesPath = "";
					
					leftImagesPath = "";
					
					allroundImagesPath = "";
					
					aboveImagesPath = "";
					
					rightImagesPath = "";
					
					paperlabelImagePath = "";
					
					onecodelableImagePath = "";
					
				}else {
//					?????????????????????????????????????????????,??????????????????????????????????????? ???uploadimage????????????????????????
					assetService.updateAsset(assetdatas);
				}*/

				result.put("msg", "ok");
				
			} catch (Exception e) {
				// TODO: handle exception
				LogUtil.logger.info(e+"update????????????");
				result.put("msg", "failed");
			}
		}
//		??????????????????????????????
		return result.toJSONString();	
	}
	
	
//	TODO210915????????? ???????????????????????????????????????
	@RequestMapping(value = "/huaxi/poi/export", method = RequestMethod.GET)
//	???????????????????????????(??????????????????????????????????????????????????????????????????????????????????????????????????????????????????user.address???????????????)
	public void exportAsset(HttpServletResponse response) {
		
		
		String token = httpServletRequest.getHeader("token");
		String username = JWT.decode(token).getAudience().get(0); //?????????
		String address = JWT.decode(token).getAudience().get(1); //????????????
		String department = JWT.decode(token).getAudience().get(3); //????????????
		
		
		User user = new User();
		user.setDepartment(department);
		user.setAddress(address);
		List<Asset> userAssert = assetService.searchAssertUser(user); //user.address??????1;
		
        //????????????
        ExcelUtiles.exportExcel(userAssert,"???????????????","asset",Asset.class,"????????????.xls",response);
    }
	
	
//	????????????
//	TODO 0817 ????????????????????????????????????????????????????????????????????????????????????
//	??????????????????????????????????????????????????????????????????????????????
    @RequestMapping(value="/uploadimage",method = RequestMethod.POST)
    public String uploadImage(@RequestBody(required = false) Map paramMap){
    	
    	
		JSONObject result = new JSONObject();
		Map<String,Object> map = new HashMap<>();
		try {
			
			if(paramMap == null) {
//				??????????????????
				result.put("msg", "failed");
				return result.toJSONString();
			}
			
//			LogUtil.logger.info(paramMap+"=paramMap=??????base64??????"); //???????????????????????????
			
		     
//	        ?????????????????????
	        String path = getPath();
	        
//	        String path = "t/images/";//TODO1027????????????????????????????????????
	        
	        imagesPath = path+System.currentTimeMillis();
	        String imagePath = imagesPath+".png";
	        
//	        LogUtil.logger.info("=???????????????getpath()"+path);
	        
//	  ?????????=====TODO 0817 ????????????????????????????????????????????????????????????????????????????????????
	        
	        
			boolean imageKey = paramMap.containsKey("image");
			
//			LogUtil.logger.info("????????????="+paramMap.get("image").toString()); //???????????????????????????
			if(imageKey && !StringUtils.isEmpty(paramMap.get("image").toString())) {
				String image = paramMap.get("image").toString();
				//??????base64 ??????,??????","????????????????????????
		        String str = image.substring(image.indexOf(",")+1);
		        
		/**     ???????????????base64?????????????????????base64?????????????????????????????????????????????????????????
		      ?????????Base64?????????  ???????????????GenerateImage????????????base64?????????????????????
		 */
		        GenerateImage(str, imagePath);
			}

//	        ?????????
	        boolean leftImageKey = paramMap.containsKey("leftImage");
	        if(leftImageKey && !StringUtils.isEmpty(paramMap.get("leftImage").toString())) {
	        	String leftImage = paramMap.get("leftImage").toString();
		        String leftstr = leftImage.substring(leftImage.indexOf(",")+1);
		        leftImagesPath = path+"left/"+System.currentTimeMillis();
				String realleftImagesPath = leftImagesPath+".png";
				GenerateImage(leftstr,realleftImagesPath);
	        }

//	        ????????? 
	        boolean allroundImageKey = paramMap.containsKey("allroundImage");
	        if(allroundImageKey && !StringUtils.isEmpty(paramMap.get("allroundImage").toString())) {
	        	String allroundImage = paramMap.get("allroundImage").toString();
		        String allroundImagestr = allroundImage.substring(allroundImage.indexOf(",")+1);
		        allroundImagesPath = path+"allround/"+System.currentTimeMillis();
				String realallroundImagesPath = allroundImagesPath+".png";
				GenerateImage(allroundImagestr,realallroundImagesPath);
	        }
//	        ?????????
	        boolean aboveImageKey = paramMap.containsKey("aboveImage");
	        if(aboveImageKey && !StringUtils.isEmpty(paramMap.get("aboveImage").toString())) {
	        	String aboveImage = paramMap.get("aboveImage").toString();
		        String aboveImagestr = aboveImage.substring(aboveImage.indexOf(",")+1);
		        aboveImagesPath = path+"above/"+System.currentTimeMillis();
				String realaboveImagesPath = aboveImagesPath+".png";
				GenerateImage(aboveImagestr,realaboveImagesPath);
	        }

//	        ?????????
	        boolean rightImageKey = paramMap.containsKey("rightImage");
	        if(rightImageKey && !StringUtils.isEmpty(paramMap.get("rightImage").toString())) {
	        	String rightImage = paramMap.get("rightImage").toString();
		        String rightImagestr = rightImage.substring(rightImage.indexOf(",")+1);
		        rightImagesPath = path+"right/"+System.currentTimeMillis();
				String realrightImagesPath = rightImagesPath+".png";
				GenerateImage(rightImagestr,realrightImagesPath);
	        }


//		      ?????????????????????
	        boolean paperlabelImageKey = paramMap.containsKey("paperlabelImage");
	        if(paperlabelImageKey && !StringUtils.isEmpty(paramMap.get("paperlabelImage").toString())) {
	        	String paperlabelImage = paramMap.get("paperlabelImage").toString();
		        String paperlabelImagestr = paperlabelImage.substring(paperlabelImage.indexOf(",")+1);
		        paperlabelImagePath = path+"paperlabel/"+System.currentTimeMillis();
				String paperlabelImagesPath = paperlabelImagePath+".png";
				GenerateImage(paperlabelImagestr,paperlabelImagesPath);
	        }

//	    	??????????????????
	        boolean onecodelableImageKey = paramMap.containsKey("onecodelableImage");
	        if(onecodelableImageKey && !StringUtils.isEmpty(paramMap.get("onecodelableImage").toString())) {
	        	String onecodelableImage = paramMap.get("onecodelableImage").toString();
		        String onecodelableImagestr = onecodelableImage.substring(onecodelableImage.indexOf(",")+1);
		        onecodelableImagePath = path+"onecodelable/"+System.currentTimeMillis();
				String onecodelableImagesPath = onecodelableImagePath+".png";
				GenerateImage(onecodelableImagestr,onecodelableImagesPath);
	        }
	        
	        
	        
//		        ???????????????
	        try {

	        	if(imageKey && !StringUtils.isEmpty(paramMap.get("image").toString())) {
	        		String filePath= imagePath;
//					??????????????????????????????????????????
					String imageStr = ImageUtils.resizeImage(filePath, 60, 600, 720);
					
					comImagesPath = path+"compress/"+System.currentTimeMillis();
					
					String realComImagesPath = comImagesPath+".png";
					
					GenerateImage(imageStr,realComImagesPath);
	        	}
		
			} catch (Exception e) {
				// TODO: handle exception
				LogUtil.logger.error(e+"??????????????????");
//				??????????????????
				result.put("msg", "failed");
				return result.toJSONString();
			}
	        
	        result.put("msg", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "failed");
		}
		return result.toJSONString();

	}
    
	//base64????????????????????????  
   /* public static boolean GenerateImage(String imgStr,String imageName){
        //??????????????????????????????Base64?????????????????????  
        if (imgStr == null) //??????????????????  
            return false;  
        try{     
            //Base64??????  
        	byte[] b = Base64.decodeBase64(imgStr.replace("data:image/png;base64,",""));
        	imgStr = imgStr.replace("base64,","");
            for(int i=0;i<b.length;++i) {  
                if(b[i]<0){  
                //??????????????????  
                    b[i]+=256;  
                }  
            }  
            //??????jpeg??????  
            String imgFilePath = imageName;//??????????????????  
            OutputStream out = new FileOutputStream(imgFilePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        } catch (Exception e) {   
            return false;  
        }  
    }*/

    
    public static boolean GenerateImage(String imgStr,String imgFilePath) { // ??????????????????????????????Base64?????????????????????
    	imgFilePath = "/"+imgFilePath; //????????????????????????
    	
        if (imgStr==null){
            // ??????????????????
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64??????
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// ??????????????????
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
 
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    public String getPath(){
    	String path1 = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//    	 ??????????????????
		String replace = path1.replace("file:/", "");
//    		????????????????????????
		String realpath = replace.replace("huaxi.jar", "");
//    		jar???????????????????????????????????????
		String realpath1 = realpath.replace("!/BOOT-INF/classes!", "");
		String path = realpath1.replace("\\", "/");
//    		E:/workspace32/springbootdemo02_userloginbak/target//realpath
		//    	???????????????target//??????
		
		
		
//TODO1029		String image_path= path.substring(0, path.length() - 8);
//		String upload =image_path+"/t";
		
		String upload = path+"t";
		
//		LogUtil.logger.info("?????????????????????"+path);
		
//		???????????????
		String compress =upload+"/images/compress"; 
  		File com = new File(compress); 
  		if(!com.exists()){
  			com.mkdirs(); 
  		}
  		
//  	?????????
  		String left =upload+"/images/left"; 
  		File le = new File(left); 
  		if(!le.exists()){
  			le.mkdirs(); 
  		}
//  	?????????
  		String right =upload+"/images/right"; 
  		File ri = new File(right); 
  		if(!ri.exists()){
  			ri.mkdirs(); 
  		}
//  	?????????
  		String above =upload+"/images/above"; 
  		File ab = new File(above); 
  		if(!ab.exists()){
  			ab.mkdirs(); 
  		}
//  	??????
  		String allround =upload+"/images/allround"; 
  		File ar = new File(allround); 
  		if(!ar.exists()){
  			ar.mkdirs(); 
  		}
//  	?????????
  		String paperlabel =upload+"/images/paperlabel"; 
  		File pl = new File(paperlabel); 
  		if(!pl.exists()){
  			pl.mkdirs(); 
  		}
//  	????????????
  		String onecodelable =upload+"/images/onecodelable"; 
  		File ol = new File(onecodelable); 
  		if(!ol.exists()){
  			ol.mkdirs(); 
  		}
  		
//  	?????????????????????
  		File f = new File(upload); 
  		if(!f.exists()){
  			f.mkdirs(); //????????????
  		}
    	return upload+"/images/";  //??????????????????
      }
      
//  ??????????????????,????????????id TODO????????????????????? ,?????????????????????result?????????
    @RequestMapping(value = "/showImage",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
  	@ResponseBody
  	public ResponseEntity<?> showImage(@RequestParam(value="assetID",required=true)String assetID){
//     	????????????ID??????????????????
      	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
      	
//     ????????????????????????
  		try {
  			String imageulr = "";
  	      	for (Asset asseturl : queryImageByID) {
//  	      		????????????
  	      		imageulr = asseturl.getImageUrl()+".png";
//  	      		????????????????????????
//  	      		imageulr = asseturl.getCompressImageUrl()+".png";
  	  		}
  			return ResponseEntity.ok(resourceLoader.getResource("file:/" + imageulr));
  		} catch (Exception e) {
  			return ResponseEntity.notFound().build();
//  			return ResultUtil.errorEntity();
  		}
  	}
//    ??????
    @RequestMapping(value = "/showImageLeft",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<?> showImageLeft(@RequestParam(value="assetID",required=true)String assetID){
    	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
    	try {
    		String leftImageUrl = "";
    		for (Asset asseturl : queryImageByID) {
    			leftImageUrl = asseturl.getLeftImageUrl()+".png";
    		}
    		return ResponseEntity.ok(resourceLoader.getResource("file:/" + leftImageUrl));
    	} catch (Exception e) {
    		return ResultUtil.errorEntity();
    	}
    }
//   ??????
    @RequestMapping(value = "/showImageRight",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<?> showImageRight(@RequestParam(value="assetID",required=true)String assetID){
    	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
    	try {
    		String rightImageUrl = "";
    		for (Asset asseturl : queryImageByID) {
    			
    			rightImageUrl =  asseturl.getRightImageUrl()+".png";
    		}
    		return ResponseEntity.ok(resourceLoader.getResource("file:/" + rightImageUrl));
    	} catch (Exception e) {
    		return ResultUtil.errorEntity();
    	}
    }
//  ?????????
    @RequestMapping(value = "/showImageAbove",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<?> showImageAbove(@RequestParam(value="assetID",required=true)String assetID){
    	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
    	try {
    		String aboveImageUrl = "";
    		for (Asset asseturl : queryImageByID) {
    			aboveImageUrl = asseturl.getAboveImageUrl()+".png";
    		}
    		return ResponseEntity.ok(resourceLoader.getResource("file:/" + aboveImageUrl));
    	} catch (Exception e) {
    		return ResultUtil.errorEntity();
    	}
    }
//  ?????????
    @RequestMapping(value = "/showImageAllround",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<?> showImageAllround(@RequestParam(value="assetID",required=true)String assetID){
    	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
    	try {
    		String allroundImageUrl = "";
    		for (Asset asseturl : queryImageByID) {
    			allroundImageUrl = asseturl.getAllroundImageUrl()+".png";
    		}
    		return ResponseEntity.ok(resourceLoader.getResource("file:/" + allroundImageUrl));
    	} catch (Exception e) {
    		return ResultUtil.errorEntity();
    	}
    }
    
//	?????????????????????
    @RequestMapping(value = "/showImagePaperlabel",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<?> showImagePaperlabel(@RequestParam(value="assetID",required=true)String assetID){
    	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
    	try {
    		String paperlabelImageUrl = "";
    		for (Asset asseturl : queryImageByID) {
    			paperlabelImageUrl = asseturl.getPaperlabelImageUrl()+".png";
    		}
    		return ResponseEntity.ok(resourceLoader.getResource("file:/" + paperlabelImageUrl));
    	} catch (Exception e) {
    		return ResultUtil.errorEntity();
    	}
    }
//	??????????????????
    @RequestMapping(value = "/showImageOnecodelable",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<?> showImageOnecodelable(@RequestParam(value="assetID",required=true)String assetID){
    	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
    	try {
    		String onecodelableImageUrl = "";
    		for (Asset asseturl : queryImageByID) {
    			onecodelableImageUrl = asseturl.getOnecodelableImageUrl()+".png";
    		}
    		return ResponseEntity.ok(resourceLoader.getResource("file:/" + onecodelableImageUrl));
    	} catch (Exception e) {
    		return ResultUtil.errorEntity();
    	}
    }
    
    
    
    
    
    
    
//  ????????????
    
    @RequestMapping(value = "/huaxi/statement", method = RequestMethod.POST)
	public JSONArray statement() {
    	JSONArray asset_num = new JSONArray();
    	int totalCheck = 0; //???????????????
		int unexamined = 0; //?????????????????????
		int examined = 0; //?????????????????????
		String get0Time = ""; //??????0?????????
		
		
//		??????
		int normal = 0; //??????
		int maintain = 0; //????????????
		int tobescrap  = 0;//?????????
		int scrap  = 0;//?????????(?????????)
		int hasScrap  = 0;//(??????)
		int lend  = 0;//??????
		int aggregate = 0;//??????
		Map map = new HashMap<>();
//		SELECT `Status`,Amount,Money FROM Asset
		
		/*
		 * Asset assetdata = new Asset();
		assetdata.setAddress(user.address);
		*/
		
		String token = httpServletRequest.getHeader("token");
		String address = JWT.decode(token).getAudience().get(1); //????????????
		String department = JWT.decode(token).getAudience().get(3); //????????????
//        ????????????????????????
        String[] split = department.split(",");
        List<String> department_list = Arrays.asList(split);
        
        Map<String, Object> asset_attr = new HashMap<>();
        asset_attr.put("address", address);
        asset_attr.put("department", department_list);
        
        
		try {
				List<Map> searchAssert =  assetService.searchAssertTotal(asset_attr);
//			????????????
				 BigDecimal total =  searchAssert.stream()
		    			.filter(asset -> StringUtils.isEmpty(asset.get("Money")) == false)
		    			.reduce(BigDecimal.ZERO,(x,y) -> { //????????????
		    				BigDecimal number = new BigDecimal(0);
		    				int value = 1;
		    				if(null == y.get("Amount") || StringUtils.isEmpty(y.get("Amount"))) {
		    					value = 1; 
		    				}else {
		    					value = Integer.parseInt(y.get("Amount").toString());
		    				}
		    				number=BigDecimal.valueOf((int)value);
		    				return x.add(((BigDecimal) y.get("Money")).multiply(number));
		    			}, BigDecimal::add);
		    	
//				????????????
				IntSummaryStatistics collect1 = searchAssert.parallelStream().filter(a -> null != a.get("assetID")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
								);
				totalCheck = Integer.parseInt(collect1.getSum()+"");
//				????????????,?????????????????????????????????
				timeUtiles time_ = new timeUtiles();
				get0Time = time_.get0Time();
				String currenttime = time_.getCurrenttime();
				
				List<Check> checkAsset = checkService.queryCheckByTime(get0Time,currenttime);
				examined = checkAsset.parallelStream().map(check -> check.getCheckNum()).reduce(0, Integer::sum);
				
				
//				???????????????????????????????????????????????????????????????
				
		
//			????????????????????????  TODO ??????????????????Collectors.groupingBy
				IntSummaryStatistics undetermined = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("??????")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
								);
				normal = Integer.parseInt(undetermined.getSum()+"");
				
				IntSummaryStatistics undetermined1 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("?????????")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				tobescrap = Integer.parseInt(undetermined1.getSum()+"");
				
				IntSummaryStatistics undetermined2 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("????????????")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				maintain = Integer.parseInt(undetermined2.getSum()+"");
//	TODO1016			IntSummaryStatistics undetermined3 = searchAssert.parallelStream().filter(asset -> asset.get("Status").equals("?????????")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
				IntSummaryStatistics undetermined3 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("?????????")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				scrap = Integer.parseInt(undetermined3.getSum()+"");
				IntSummaryStatistics undetermined4 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("??????")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				hasScrap = Integer.parseInt(undetermined4.getSum()+"");
				IntSummaryStatistics undetermined5 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("??????")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				lend = Integer.parseInt(undetermined5.getSum()+"");
				
//			??????
			unexamined = totalCheck-examined;
			map.put("examined", examined);
			map.put("unexamined", unexamined);
			map.put("aggregate", total+"");
			map.put("normal", normal);
			map.put("maintain", maintain);
			map.put("tobescrap", tobescrap);
			map.put("scrap", scrap);
			map.put("hasScrap", hasScrap);
			map.put("lend", lend);
			map.put("totalCheck",totalCheck);
			
		} catch (Exception e) {
			// TODO: handle exception
			map.put("msg", "failed");
			e.printStackTrace();
			LogUtil.logger.error("?????????????????????????????????"+e);
		}
		asset_num.add(map);
//		??????????????????????????????
		return asset_num;	
	}
	
    @RequestMapping(value = "/huaxi/assetmarked",method = RequestMethod.POST)
	public Result HospitalRoom(@RequestBody List<Asset> asset) {
    	List searchUser = new ArrayList<>(); 
		JSONArray userjson = new JSONArray();
		try {
			for (Asset assetdatas : asset) {
	    		List<Asset> assetmarked = assetService.getAssetmarked(assetdatas);
	    		userjson = JsonUtils.list2jsonArray(assetmarked);
	    	}
		} catch (Exception e) {
			// TODO: handle exception
			return ResultUtil.error("marked", "????????????");
		}
    	
    	
    	return ResultUtil.success(userjson);
	}
    
    
    
    
    
/*//    ??????????????????(????????????)
    @RequestMapping(value = "/api/huaxi/HospitalRoom",method = RequestMethod.POST)
	@PassToken
	public JSONArray HospitalRoom(@RequestBody Object object) {
    	List searchUser = new ArrayList<>(); 
		JSONArray userjson = new JSONArray();
		Map<String,Object> result = new HashMap<>();
//		??????19.20?????????????????????,??????
		List<String> lm = new ArrayList<>();
		lm.add("C2021A00015F");
		lm.add("C2021A000160");
    	try {
    		Map token = (Map) object;
    		if (token.get("tempAuthCode").equals("jHJKWRKt")) {
        		try {
    	    		List ls = new ArrayList<>();

    	    		for (int i = 0; i < lm.size(); i++) {
//    	    			??????????????????
    	    			Map map = new HashMap<>();
    	    			List<Map> searchRG = reService.searchRG(lm.get(i));
    	    			for (Map iot : searchRG) {
    						map.put("location", iot.get("location"));
    						map.put("gatewaymac", iot.get("gatewaymac"));
    						map.put("mac", lm.get(i));
    						map.put("updatetime", iot.get("updatetime"));
    					}
    	    			//??????????????????
    	    			List<Map> searchAssert = reService.searchAssert();
    	    			for (Map ele : searchAssert) {
    	    				if(ele.get("mac").equals(lm.get(i))) {
    	    					map.put("electric", ele.get("electric"));
    	    				}
    					}
    	    			ls.add(map);
    	    		}
    	    		result.put("msg",ls);
        	    		
        	    		
        		} catch (Exception e) {
        			// TODO: handle exception
        			result.put("msg", "????????????");
        		}
        		
        	}else {
        		result.put("msg", "????????????");
        	}
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "????????????");
		}
    	searchUser.add(result);
		userjson = JsonUtils.list2jsonArray(searchUser);
    	return userjson;
	}
    

    */
    
    
//  ??????????????????redis
    private void findAssetAndRedis() throws Exception {
    	List<Map> findE6 = assetService.findE6();
//		???????????????
		Map map = new HashMap();
		map.put("e6_mac", findE6);
		Object object1 = redisUtil.hmset("E6",map);
    }
    
    
    
    
    
//  token??????????????????
    public Map<String,String> getUserinfoByToken() {
    	Map map = new HashMap<>();
    	String token = "";
    	String username = "";
		String address = "";
    	token = httpServletRequest.getHeader("token");
    	username = JWT.decode(token).getAudience().get(0); //?????????
    	address = JWT.decode(token).getAudience().get(1); //????????????
    	map.put("username", username);
    	map.put("address", address);
		return map;
    }
    
    

    
//  TODO210526 ????????????????????????
    @RequestMapping(value = "/wanghai/datas",method = RequestMethod.GET)
  	@ResponseBody
  	@PassToken
    public Result getIbeaconDatas(@RequestParam(value="currentPage",required=false,defaultValue="1")Integer currentPage,
    		@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize) {
    	JSONArray ibeaconjson = new JSONArray();
    	try {
    		PageInfo<Asset> datasPage = reService.searchAssertIbeaconDatas(currentPage,pageSize);
    		
    		String jsonString = JSONObject.toJSONString(datasPage);
			JSONObject json = JSONObject.parseObject(jsonString);	
			ibeaconjson = json.getJSONArray("list");
//			???JSONArray???????????????
			String total = json.getString("total");
			JSONObject obj = new JSONObject();  
	        obj.put("total",total) ;
	        ibeaconjson.add(obj) ; 
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("wanghai?????????????????????"+e);
			return ResultUtil.error("datas", e+"");
		}
    	return ResultUtil.success(ibeaconjson);
    }
    
    
    
  }
	

 


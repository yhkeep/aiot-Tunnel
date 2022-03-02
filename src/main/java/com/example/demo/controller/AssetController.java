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

//@Controller//测试jsp使用
@RestController
public class AssetController{
//注入资源获取
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
	
	
//	获取token
	@Autowired
	private HttpServletRequest httpServletRequest;

//	存在线程安全问题
	private  String imagesPath = "";
//	缩小图路径
	private  String comImagesPath = "";
	
	private  String leftImagesPath = "";
	
	private  String allroundImagesPath = "";
	
	private  String aboveImagesPath = "";
	
	private  String rightImagesPath = "";
	
	private  String paperlabelImagePath = "";
	
	private  String onecodelableImagePath = "";
	
//	资产首页显示，匹配对应医院地址之后进行展示数据
	
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
	 * 访问jsp文件
	 * @RequestMapping(value = "/websocket", method = RequestMethod.GET)
	 public String index(Model m){
        m.addAttribute("now",DateFormat.getDateTimeInstance().format(new Date()));
        return "index";  //注解配置@Controller以及properties属性添加路径访问过后，视图重定向index.jsp
    }*/
	
//	记忆筛选，isShowMac,isShowBuyDate,isShowMoney,isShowElectric
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
			username = JWT.decode(token).getAudience().get(0); //用户名
			address = JWT.decode(token).getAudience().get(1); //项目地址
//			User数据持久化更新记录
			User userinfo = new User();
			userinfo.setUsername(username); //必有
			
//			分页过后，进行用户记忆，部门、所在科室、一到四级分类
			userinfo.setMemorylocDept(memorylocDept);
			userinfo.setMemorydepart(memorydepart);
			userinfo.setMemoryoneclassify(oneclassify);
			userinfo.setMemorysecondclassify(secondclassify);
			userinfo.setMemorythreeclassify(threeclassify);
			userinfo.setMemoryfourclassify(fourclassify);
			

//			是否展示参数
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
			
//			LogUtil.logger.info("userinfo"+userinfo); 用户记忆功能
//			默认首页查询
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
//	        token中获取department部门权限
	        String department = JWT.decode(token).getAudience().get(3);
//	         部门字符串转集合
	        String[] split = department.split(",");
	        List<String> department_list = Arrays.asList(split);
	        map.put("department",department_list);
	        
	        
	        Integer pagesize = StringUtils.isEmpty(pageSize)?ParameterField.pageSize:Integer.parseInt(pageSize);
			PageInfo<Asset> datasPage = assetService.getDatasPage(currentPage,pagesize,map);
			
			String jsonString = JSONObject.toJSONString(datasPage);
			JSONObject json = JSONObject.parseObject(jsonString);	
			string = json.getJSONArray("list");
//			向JSONArray添加总条数
			String total = json.getString("total");
			JSONObject obj = new JSONObject();  
	        obj.put("total",total) ;
	        obj.put("timeTest",new timeUtiles().timeMeter());
	        string.add(obj) ; 
		} catch (Exception e) {
			// TODO: handle exception
			
			LogUtil.logger.info("用户查询信息不全"+e);
		}
		return string;	
	}
//	资产管理
//	添加资产（医院地址）
	@RequestMapping(value = "/huaxi/upload", method = RequestMethod.POST)
	public String addAsset(@RequestBody List<Asset> asset) throws Exception {
		JSONObject result = new JSONObject();
		if(asset != null) {
			for (Asset assetdatas : asset) {
//				切换医院
				if("" == UserParameterField.address || !UserParameterField.address.equals(assetdatas.getAddress())) {
					result.put("message", "用户信息不正确");
//					return ResultUtil.error("message",  "用户信息不正确");
					return result.toJSONString();
				}
				
//				上传mac不同(非null),资产id不同
				List<Asset> searchAssert = assetService.searchAssert(assetdatas.getAddress());
				for (Asset asset_data : searchAssert) {
					String oldAssetID = asset_data.getAssetID();
					String oldmac = asset_data.getMac();
					try {
						if(oldAssetID.equals(assetdatas.getAssetID()) ||
								 !StringUtils.isEmpty(assetdatas.getMac()) && oldmac.equals(assetdatas.getMac()) ) {
							result.put("message", "资产id或mac地址已存在");
							return result.toJSONString(); 
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
//					事务处理等
				try {
//					TODO1023图片上传硬编码
					
					
//					图片上传,图片路径保存
					assetdatas.setImageUrl(imagesPath);
//					压缩图片上传
					assetdatas.setCompressImageUrl(comImagesPath);
//					资产加载多张图片共同上传
					assetdatas.setAboveImageUrl(aboveImagesPath);
					
					assetdatas.setAllroundImageUrl(allroundImagesPath);
					
					assetdatas.setLeftImageUrl(leftImagesPath);
					
					assetdatas.setRightImageUrl(rightImagesPath);
					
					assetdatas.setPaperlabelImageUrl(paperlabelImagePath);
					
					assetdatas.setOnecodelableImageUrl(onecodelableImagePath);
//					资产保存
					assetService.insertAssetDatas(assetdatas);
//					添加成功后，开始清空，图片缓存数据，避免线程安全异常
					
					imagesPath = "";
					
					comImagesPath = "";
					
					leftImagesPath = "";
					
					allroundImagesPath = "";
					
					aboveImagesPath = "";
					
					rightImagesPath = "";
					
					paperlabelImagePath = "";
					
					onecodelableImagePath = "";
					
					
//					添加成功后，同时缓存中添加资产
					findAssetAndRedis();
					
					
					result.put("msg", "ok");
				} catch (Exception e) {
					// TODO: handle exception
					LogUtil.logger.error(e+"资产上传异常");
					result.put("msg", "failed");
				}
			}
		}

//		返回成功与失败！
		return result.toJSONString();	
	}
//	删除资产
	@RequestMapping(value = "/huaxi/del", method = RequestMethod.GET)
	public String deleteAsset(@RequestParam(value="assetID",required=true)List assetID) {
		
//		删除多个资产id对象
		JSONObject result = new JSONObject();
		try {
			if(assetID instanceof List) {
				assetService.delAssetData(assetID);
				result.put("msg", "ok");	
			}else {
				result.put("msg", "failed");
			}
			
//			删除同时，缓存同时更新
			findAssetAndRedis();
	    	
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "failed");
		}
//		返回删除成功与失败！
		return result.toJSONString();	
	}
	
	
//	集合pojo
	@RequestMapping(value = "/huaxi/update", method = RequestMethod.POST)
	public String updateAsset( @RequestBody List<Asset> asset) {
		JSONObject result = new JSONObject();
		for (Asset assetdatas : asset) {
//			根据资产id,修改mac等资产信息
			List<Asset> searchAssert = assetService.searchAssert(UserParameterField.address);
			for (Asset asset_data : searchAssert) {
				String oldAssetID = asset_data.getAssetID();
				String oldmac = asset_data.getMac();
				
//				不同资产id下，mac不能相同，空mac同样需要更新
				if(!StringUtils.isEmpty(assetdatas.getMac()) && !StringUtils.isEmpty(oldmac) && oldmac.equals(assetdatas.getMac())  
						&& !assetdatas.getAssetID().equals(asset_data.getAssetID())
						) {
					result.put("message", "mac地址已存在");
					return result.toJSONString(); 
				}
			}
			
			
			
//			数字类限制
			if(StringUtils.isEmpty(assetdatas.getAmount()) || assetdatas.getAmount().equals("--")) {
				assetdatas.setAmount("1");
			}
			if(StringUtils.isEmpty(assetdatas.getMoney()) || assetdatas.getMoney().equals("--")){
				assetdatas.setMoney(new BigDecimal(0));
			}
			
			
			try {
//				更新盘点数据
				if(!StringUtils.isEmpty(assetdatas.getCheck())) {
//					更新盘点操作
					assetService.updateCheck(assetdatas);
					result.put("msg", "ok");
					continue; //重复执行
					
				}
				
//				图片先调用uploadimage
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
					assetService.updatePIG(assetdatas); //支持传空
					assetService.updateAsset(assetdatas);
				}else {
//					如果没有图片，普通资产一起上传,但是不会执行上传为空的图片 ，uploadimage接口传递数据过来
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
				
//				页面传来多少图片更新多少图片
				assetService.updatePIG(assetdatas); //支持传空
				
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
					
					assetService.updatePIG(assetdatas); //支持传空
					
					assetService.updateAsset(assetdatas);

//					页面更新后，清空图片缓存
					imagesPath = "";
					
					comImagesPath = "";
					
					leftImagesPath = "";
					
					allroundImagesPath = "";
					
					aboveImagesPath = "";
					
					rightImagesPath = "";
					
					paperlabelImagePath = "";
					
					onecodelableImagePath = "";
					
				}else {
//					如果没有图片，普通资产一起上传,但是不会执行上传为空的图片 ，uploadimage接口传递数据过来
					assetService.updateAsset(assetdatas);
				}*/

				result.put("msg", "ok");
				
			} catch (Exception e) {
				// TODO: handle exception
				LogUtil.logger.info(e+"update更新异常");
				result.put("msg", "failed");
			}
		}
//		返回修改成功与失败！
		return result.toJSONString();	
	}
	
	
//	TODO210915待修改 ，只能下载授权所在部门数据
	@RequestMapping(value = "/huaxi/poi/export", method = RequestMethod.GET)
//	导出公共工具类提取(华西同院登陆不区分医院地址，不同地址或不同权限登陆获取不同数据，多院平台共用user.address线程不安全)
	public void exportAsset(HttpServletResponse response) {
		
		
		String token = httpServletRequest.getHeader("token");
		String username = JWT.decode(token).getAudience().get(0); //用户名
		String address = JWT.decode(token).getAudience().get(1); //项目地址
		String department = JWT.decode(token).getAudience().get(3); //部门授权
		
		
		User user = new User();
		user.setDepartment(department);
		user.setAddress(address);
		List<Asset> userAssert = assetService.searchAssertUser(user); //user.address改为1;
		
        //导出操作
        ExcelUtiles.exportExcel(userAssert,"资产明细表","asset",Asset.class,"资产明细.xls",response);
    }
	
	
//	上传图片
//	TODO 0817 图片数据传递过程中如果属性有，但是值为空则不进行文件创建
//	先请求图片后再去请求资产上传后者更新，否则无保存路径
    @RequestMapping(value="/uploadimage",method = RequestMethod.POST)
    public String uploadImage(@RequestBody(required = false) Map paramMap){
    	
    	
		JSONObject result = new JSONObject();
		Map<String,Object> map = new HashMap<>();
		try {
			
			if(paramMap == null) {
//				上传图片失败
				result.put("msg", "failed");
				return result.toJSONString();
			}
			
//			LogUtil.logger.info(paramMap+"=paramMap=图片base64数据"); //获取所有图片属性值
			
		     
//	        创建文件夹路径
	        String path = getPath();
	        
//	        String path = "t/images/";//TODO1027固定，后期无文件自动创建
	        
	        imagesPath = path+System.currentTimeMillis();
	        String imagePath = imagesPath+".png";
	        
//	        LogUtil.logger.info("=分割后路径getpath()"+path);
	        
//	  原始图=====TODO 0817 图片数据传递过程中如果属性有，但是值为空则不进行文件创建
	        
	        
			boolean imageKey = paramMap.containsKey("image");
			
//			LogUtil.logger.info("图片数据="+paramMap.get("image").toString()); //获取所有图片属性值
			if(imageKey && !StringUtils.isEmpty(paramMap.get("image").toString())) {
				String image = paramMap.get("image").toString();
				//输出base64 数据,截取","之后的值进行转换
		        String str = image.substring(image.indexOf(",")+1);
		        
		/**     第一个参数base64转图片的必须的base64数据，第二个是转换后生成的图片存放路径
		      实例化Base64转换类  调用里面的GenerateImage方法（把base64数据转为图片）
		 */
		        GenerateImage(str, imagePath);
			}

//	        左侧图
	        boolean leftImageKey = paramMap.containsKey("leftImage");
	        if(leftImageKey && !StringUtils.isEmpty(paramMap.get("leftImage").toString())) {
	        	String leftImage = paramMap.get("leftImage").toString();
		        String leftstr = leftImage.substring(leftImage.indexOf(",")+1);
		        leftImagesPath = path+"left/"+System.currentTimeMillis();
				String realleftImagesPath = leftImagesPath+".png";
				GenerateImage(leftstr,realleftImagesPath);
	        }

//	        全局图 
	        boolean allroundImageKey = paramMap.containsKey("allroundImage");
	        if(allroundImageKey && !StringUtils.isEmpty(paramMap.get("allroundImage").toString())) {
	        	String allroundImage = paramMap.get("allroundImage").toString();
		        String allroundImagestr = allroundImage.substring(allroundImage.indexOf(",")+1);
		        allroundImagesPath = path+"allround/"+System.currentTimeMillis();
				String realallroundImagesPath = allroundImagesPath+".png";
				GenerateImage(allroundImagestr,realallroundImagesPath);
	        }
//	        俯视图
	        boolean aboveImageKey = paramMap.containsKey("aboveImage");
	        if(aboveImageKey && !StringUtils.isEmpty(paramMap.get("aboveImage").toString())) {
	        	String aboveImage = paramMap.get("aboveImage").toString();
		        String aboveImagestr = aboveImage.substring(aboveImage.indexOf(",")+1);
		        aboveImagesPath = path+"above/"+System.currentTimeMillis();
				String realaboveImagesPath = aboveImagesPath+".png";
				GenerateImage(aboveImagestr,realaboveImagesPath);
	        }

//	        右视图
	        boolean rightImageKey = paramMap.containsKey("rightImage");
	        if(rightImageKey && !StringUtils.isEmpty(paramMap.get("rightImage").toString())) {
	        	String rightImage = paramMap.get("rightImage").toString();
		        String rightImagestr = rightImage.substring(rightImage.indexOf(",")+1);
		        rightImagesPath = path+"right/"+System.currentTimeMillis();
				String realrightImagesPath = rightImagesPath+".png";
				GenerateImage(rightImagestr,realrightImagesPath);
	        }


//		      纸质标签图片照
	        boolean paperlabelImageKey = paramMap.containsKey("paperlabelImage");
	        if(paperlabelImageKey && !StringUtils.isEmpty(paramMap.get("paperlabelImage").toString())) {
	        	String paperlabelImage = paramMap.get("paperlabelImage").toString();
		        String paperlabelImagestr = paperlabelImage.substring(paperlabelImage.indexOf(",")+1);
		        paperlabelImagePath = path+"paperlabel/"+System.currentTimeMillis();
				String paperlabelImagesPath = paperlabelImagePath+".png";
				GenerateImage(paperlabelImagestr,paperlabelImagesPath);
	        }

//	    	一维码标签照
	        boolean onecodelableImageKey = paramMap.containsKey("onecodelableImage");
	        if(onecodelableImageKey && !StringUtils.isEmpty(paramMap.get("onecodelableImage").toString())) {
	        	String onecodelableImage = paramMap.get("onecodelableImage").toString();
		        String onecodelableImagestr = onecodelableImage.substring(onecodelableImage.indexOf(",")+1);
		        onecodelableImagePath = path+"onecodelable/"+System.currentTimeMillis();
				String onecodelableImagesPath = onecodelableImagePath+".png";
				GenerateImage(onecodelableImagestr,onecodelableImagesPath);
	        }
	        
	        
	        
//		        生成压缩图
	        try {

	        	if(imageKey && !StringUtils.isEmpty(paramMap.get("image").toString())) {
	        		String filePath= imagePath;
//					通过已生成的图片进行图片压缩
					String imageStr = ImageUtils.resizeImage(filePath, 60, 600, 720);
					
					comImagesPath = path+"compress/"+System.currentTimeMillis();
					
					String realComImagesPath = comImagesPath+".png";
					
					GenerateImage(imageStr,realComImagesPath);
	        	}
		
			} catch (Exception e) {
				// TODO: handle exception
				LogUtil.logger.error(e+"图片上传异常");
//				上传图片失败
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
    
	//base64字符串转化成图片  
   /* public static boolean GenerateImage(String imgStr,String imageName){
        //对字节数组字符串进行Base64解码并生成图片  
        if (imgStr == null) //图像数据为空  
            return false;  
        try{     
            //Base64解码  
        	byte[] b = Base64.decodeBase64(imgStr.replace("data:image/png;base64,",""));
        	imgStr = imgStr.replace("base64,","");
            for(int i=0;i<b.length;++i) {  
                if(b[i]<0){  
                //调整异常数据  
                    b[i]+=256;  
                }  
            }  
            //生成jpeg图片  
            String imgFilePath = imageName;//新生成的图片  
            OutputStream out = new FileOutputStream(imgFilePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        } catch (Exception e) {   
            return false;  
        }  
    }*/

    
    public static boolean GenerateImage(String imgStr,String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
    	imgFilePath = "/"+imgFilePath; //绝对路径生成文件
    	
        if (imgStr==null){
            // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
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
//    	 路径头，去除
		String replace = path1.replace("file:/", "");
//    		路径项目名，去除
		String realpath = replace.replace("huaxi.jar", "");
//    		jar包之后自动添加的路径，去除
		String realpath1 = realpath.replace("!/BOOT-INF/classes!", "");
		String path = realpath1.replace("\\", "/");
//    		E:/workspace32/springbootdemo02_userloginbak/target//realpath
		//    	去掉最后的target//属性
		
		
		
//TODO1029		String image_path= path.substring(0, path.length() - 8);
//		String upload =image_path+"/t";
		
		String upload = path+"t";
		
//		LogUtil.logger.info("创建文件路径前"+path);
		
//		缩小图路径
		String compress =upload+"/images/compress"; 
  		File com = new File(compress); 
  		if(!com.exists()){
  			com.mkdirs(); 
  		}
  		
//  	左侧图
  		String left =upload+"/images/left"; 
  		File le = new File(left); 
  		if(!le.exists()){
  			le.mkdirs(); 
  		}
//  	右侧图
  		String right =upload+"/images/right"; 
  		File ri = new File(right); 
  		if(!ri.exists()){
  			ri.mkdirs(); 
  		}
//  	俯视图
  		String above =upload+"/images/above"; 
  		File ab = new File(above); 
  		if(!ab.exists()){
  			ab.mkdirs(); 
  		}
//  	全图
  		String allround =upload+"/images/allround"; 
  		File ar = new File(allround); 
  		if(!ar.exists()){
  			ar.mkdirs(); 
  		}
//  	纸质图
  		String paperlabel =upload+"/images/paperlabel"; 
  		File pl = new File(paperlabel); 
  		if(!pl.exists()){
  			pl.mkdirs(); 
  		}
//  	一维码图
  		String onecodelable =upload+"/images/onecodelable"; 
  		File ol = new File(onecodelable); 
  		if(!ol.exists()){
  			ol.mkdirs(); 
  		}
  		
//  	原始图上传路径
  		File f = new File(upload); 
  		if(!f.exists()){
  			f.mkdirs(); //创建目录
  		}
    	return upload+"/images/";  //加入绝对路径
      }
      
//  展示图片接口,通过资产id TODO整合多图片显示 ,错误结果返回与result不一致
    @RequestMapping(value = "/showImage",method = RequestMethod.GET,produces=MediaType.IMAGE_PNG_VALUE)
  	@ResponseBody
  	public ResponseEntity<?> showImage(@RequestParam(value="assetID",required=true)String assetID){
//     	根据资产ID获取图片路径
      	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
      	
//     没有图片异常处理
  		try {
  			String imageulr = "";
  	      	for (Asset asseturl : queryImageByID) {
//  	      		原图数据
  	      		imageulr = asseturl.getImageUrl()+".png";
//  	      		展示压缩图片路径
//  	      		imageulr = asseturl.getCompressImageUrl()+".png";
  	  		}
  			return ResponseEntity.ok(resourceLoader.getResource("file:/" + imageulr));
  		} catch (Exception e) {
  			return ResponseEntity.notFound().build();
//  			return ResultUtil.errorEntity();
  		}
  	}
//    左图
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
//   右图
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
//  俯视图
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
//  全局图
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
    
//	纸质标签图片照
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
//	一维码标签照
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
    
    
    
    
    
    
    
//  图表数据
    
    @RequestMapping(value = "/huaxi/statement", method = RequestMethod.POST)
	public JSONArray statement() {
    	JSONArray asset_num = new JSONArray();
    	int totalCheck = 0; //总盘点数量
		int unexamined = 0; //今日未盘点数量
		int examined = 0; //今日已盘点数量
		String get0Time = ""; //今日0点时间
		
		
//		状态
		int normal = 0; //正常
		int maintain = 0; //维修接单
		int tobescrap  = 0;//待维修
		int scrap  = 0;//待处置(待报废)
		int hasScrap  = 0;//(报废)
		int lend  = 0;//外借
		int aggregate = 0;//合计
		Map map = new HashMap<>();
//		SELECT `Status`,Amount,Money FROM Asset
		
		/*
		 * Asset assetdata = new Asset();
		assetdata.setAddress(user.address);
		*/
		
		String token = httpServletRequest.getHeader("token");
		String address = JWT.decode(token).getAudience().get(1); //项目地址
		String department = JWT.decode(token).getAudience().get(3); //项目部门
//        部门字符串转集合
        String[] split = department.split(",");
        List<String> department_list = Arrays.asList(split);
        
        Map<String, Object> asset_attr = new HashMap<>();
        asset_attr.put("address", address);
        asset_attr.put("department", department_list);
        
        
		try {
				List<Map> searchAssert =  assetService.searchAssertTotal(asset_attr);
//			资产总额
				 BigDecimal total =  searchAssert.stream()
		    			.filter(asset -> StringUtils.isEmpty(asset.get("Money")) == false)
		    			.reduce(BigDecimal.ZERO,(x,y) -> { //异常处理
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
		    	
//				资产个数
				IntSummaryStatistics collect1 = searchAssert.parallelStream().filter(a -> null != a.get("assetID")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
								);
				totalCheck = Integer.parseInt(collect1.getSum()+"");
//				盘点数量,各科室人员盘点数量总和
				timeUtiles time_ = new timeUtiles();
				get0Time = time_.get0Time();
				String currenttime = time_.getCurrenttime();
				
				List<Check> checkAsset = checkService.queryCheckByTime(get0Time,currenttime);
				examined = checkAsset.parallelStream().map(check -> check.getCheckNum()).reduce(0, Integer::sum);
				
				
//				正常，待维修，维修接单，待报废，报废，外借
				
		
//			资产状态数量统计  TODO 后续分组统计Collectors.groupingBy
				IntSummaryStatistics undetermined = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("正常")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
								);
				normal = Integer.parseInt(undetermined.getSum()+"");
				
				IntSummaryStatistics undetermined1 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("待维修")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				tobescrap = Integer.parseInt(undetermined1.getSum()+"");
				
				IntSummaryStatistics undetermined2 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("维修接单")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				maintain = Integer.parseInt(undetermined2.getSum()+"");
//	TODO1016			IntSummaryStatistics undetermined3 = searchAssert.parallelStream().filter(asset -> asset.get("Status").equals("待处置")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
				IntSummaryStatistics undetermined3 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("待报废")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				scrap = Integer.parseInt(undetermined3.getSum()+"");
				IntSummaryStatistics undetermined4 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("报废")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				hasScrap = Integer.parseInt(undetermined4.getSum()+"");
				IntSummaryStatistics undetermined5 = searchAssert.parallelStream().filter(asset -> null != asset.get("Status") && asset.get("Status").equals("外借")).map(asset -> asset.get("Amount")).collect(Collectors.summarizingInt
						(value -> Integer.parseInt((StringUtils.isEmpty(value)==true?"1":value.toString())))
						);
				lend = Integer.parseInt(undetermined5.getSum()+"");
				
//			状态
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
			LogUtil.logger.error("资产统计数据获取异常："+e);
		}
		asset_num.add(map);
//		返回修改成功与失败！
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
			return ResultUtil.error("marked", "异常解析");
		}
    	
    	
    	return ResultUtil.success(userjson);
	}
    
    
    
    
    
/*//    医护室外接口(外放接口)
    @RequestMapping(value = "/api/huaxi/HospitalRoom",method = RequestMethod.POST)
	@PassToken
	public JSONArray HospitalRoom(@RequestBody Object object) {
    	List searchUser = new ArrayList<>(); 
		JSONArray userjson = new JSONArray();
		Map<String,Object> result = new HashMap<>();
//		查询19.20号标签所在位置,暂定
		List<String> lm = new ArrayList<>();
		lm.add("C2021A00015F");
		lm.add("C2021A000160");
    	try {
    		Map token = (Map) object;
    		if (token.get("tempAuthCode").equals("jHJKWRKt")) {
        		try {
    	    		List ls = new ArrayList<>();

    	    		for (int i = 0; i < lm.size(); i++) {
//    	    			获取资产信息
    	    			Map map = new HashMap<>();
    	    			List<Map> searchRG = reService.searchRG(lm.get(i));
    	    			for (Map iot : searchRG) {
    						map.put("location", iot.get("location"));
    						map.put("gatewaymac", iot.get("gatewaymac"));
    						map.put("mac", lm.get(i));
    						map.put("updatetime", iot.get("updatetime"));
    					}
    	    			//获取电量信息
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
        			result.put("msg", "参数错误");
        		}
        		
        	}else {
        		result.put("msg", "参数错误");
        	}
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "参数错误");
		}
    	searchUser.add(result);
		userjson = JsonUtils.list2jsonArray(searchUser);
    	return userjson;
	}
    

    */
    
    
//  查询资产更新redis
    private void findAssetAndRedis() throws Exception {
    	List<Map> findE6 = assetService.findE6();
//		放入缓存中
		Map map = new HashMap();
		map.put("e6_mac", findE6);
		Object object1 = redisUtil.hmset("E6",map);
    }
    
    
    
    
    
//  token获取用户信息
    public Map<String,String> getUserinfoByToken() {
    	Map map = new HashMap<>();
    	String token = "";
    	String username = "";
		String address = "";
    	token = httpServletRequest.getHeader("token");
    	username = JWT.decode(token).getAudience().get(0); //用户名
    	address = JWT.decode(token).getAudience().get(1); //项目地址
    	map.put("username", username);
    	map.put("address", address);
		return map;
    }
    
    

    
//  TODO210526 临时获取定位数据
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
//			向JSONArray添加总条数
			String total = json.getString("total");
			JSONObject obj = new JSONObject();  
	        obj.put("total",total) ;
	        ibeaconjson.add(obj) ; 
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.error("wanghai资产定位异常："+e);
			return ResultUtil.error("datas", e+"");
		}
    	return ResultUtil.success(ibeaconjson);
    }
    
    
    
  }
	

 


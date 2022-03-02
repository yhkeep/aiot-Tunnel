/*package com.example.demo.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Asset;
import com.example.demo.domain.User;
import com.example.demo.util.ImageUtils;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.github.pagehelper.PageInfo;

import sun.misc.BASE64Decoder;



@RunWith(SpringRunner.class)
@SpringBootTest
public class IAssetTest {
	@Autowired
    private AssetService assetService;
	
	@Autowired
	private ReceivedService reService;
	@Autowired
	private CheckService checkService;
	
	
//	查询所有资产
	
	
    @Test
    public void testQueryAllAsset(){
    	List<Asset> searchAssert = assetService.searchAssert("1");
		JSONArray assetjson = JsonUtils.list2jsonArray(searchAssert);
		LogUtil.logger.info(assetjson+"");
    }
    @Test
    public void testAsset(){
//    	List<Asset> searchAssert = assetService.searchAssert();
//    	System.out.println(searchAssert+"=searchassert");
    	if(4>3  ||  3>4 && 34>7 && 5>2) {
    		System.out.println("test");
    		
    	}
    	String str = "{\"code\":200,\"message\":\"success\",\"requestId\":\"b2c3d4e5-3424-4dca-32dc-12b73290cfed\"}";
    	String str1 = "";
    	String str2 = "null";
    	String str3 = null;
    	boolean contains = str.contains("\"code\":200");
    	
    	
    	System.out.println(contains);
    	boolean empty = StringUtils.isEmpty(str);
    	boolean empty1 = StringUtils.isEmpty(str1);
    	boolean empty2 = StringUtils.isEmpty(str2);
    	boolean empty3 = StringUtils.isEmpty(str3);
    	System.out.println(empty);
    	System.out.println(empty1);
    	System.out.println(empty2);
    	System.out.println(empty3);
    	if(false || true && false) {
    		System.out.println("dsf");
    	}
    }
//    获取资产明细分页后数据
    @Test
    public void testAssetpage() throws Exception{
    	Map map = new HashMap();
    	List ls = new ArrayList<>();
        ls.add("住院手术室");
        ls.add("医工科");
        map.put("department",ls);
    	
    	PageInfo<Asset> datasPage = assetService.getDatasPage(1,5, map);
    	String jsonString = JSONObject.toJSONString(datasPage);
    	JSONObject json = JSONObject.parseObject(jsonString);	
    	String string2 = json.getString("total");
    	JSONArray string = json.getJSONArray("list");
    	JSONObject obj = new JSONObject();  
        obj.put("total",string2) ;  
        string.add(obj) ;  
    	
    	LogUtil.logger.info(string.toJSONString());
    	
    }
    
    
    @Test
    public void testMissAsset(){
    	
//      
//{Brand=null, Type=TES, updatetime=2019-08-29 16:49:38, AssetID=ZCKP12345678911, AssetName=TEST1}
//{Brand=一棵草, Type=耟陈, updatetime=, AssetID=ZCKP12345678913, AssetName=TEST2}
//{Brand=异常, Type=CHANG, updatetime=, AssetID=ZCKP12345654645, AssetName=TEST3}=str
//      
    	List ls = new ArrayList<>();
    	ls.add("yikecha");
    	ls.add("住院手术室");
    	String minutes = "2019-08-30 09:34:14";
    	List<Map> findAssetByRece = assetService.findAssetByRece(minutes,ls);
    	System.out.println(findAssetByRece.size());
    	
//    	List ls = new ArrayList<>();
    	String str = "";
    	for (Map map : findAssetByRece) {
    		Map received = (Map) map.get("received");
    		String string = "";
    		if(received != null) {
    			string = (String) received.get("updatetime");
    		}
    		Map map1 = new HashMap<>();
    		map1.put("Type",map.get("Type"));
    		map1.put("updatetime",string);
    		map1.put("Brand",map.get("Brand"));
    		map1.put("AssetName",map.get("AssetName"));
    		map1.put("AssetID",map.get("AssetID"));
    		str += map1;
		}
//    	System.out.println(str+"=str");
    }
//    自定义类自动注入service层
    @Test
    public void testAutoWire(){
    	User user = new User();
    	user.setAddress("1");
    	user.setDepartment("住院手术室,易柯尔");
    	
    	List<Asset> userAssert = assetService.searchAssertUser(user);
    	
    	System.out.println(userAssert+"==userassert"+userAssert.size());
    }
    @Test
    public void testinsert(){
    	Asset assetdatas = new Asset();
    	String assetID = "123456";
		String assetName="123456";
		String department="sdf";
		String amount="123456";
		String unit="1";
		String status="在用";
		String remark="备注";
		assetdatas.setAssetID(assetID);
		assetdatas.setAssetName(assetName);
		assetdatas.setDepartment(department);
		assetdatas.setAmount(amount);
		assetdatas.setUnit(unit);
		assetdatas.setStatus(status);
		assetdatas.setRemark(remark);
    	assetService.insertAssetDatas(assetdatas);
    }
//    删除
    @Test
    public void testdel(){
    	List<String> list=new ArrayList<>();
    	list.add("cs111116");
    	list.add("cs752223331");
    	assert  list instanceof List;
    	System.out.println(list+"=list");
    	assetService.delAssetData(list);
    }
//    更新
    @Test
    public void testupdate(){
    	
    	
		
		
		
		
		JSONArray asset_num = new JSONArray();
    	int totalCheck = 0; //总盘点数量
		int unexamined = 0; //今日未盘点数量
		int examined = 0; //今日已盘点数量
		String get0Time = ""; //今日0点时间
		int aggregate = 0; //合计
		
//		状态
		int normal = 0;
		int maintain = 0;
		int tobescrap  = 0;
		int scrap  = 0;
		List<Asset> searchAssert = assetService.searchAssert("1");
			
			for (Asset asset : searchAssert) {
//				通用名 》资产名》品牌》型号》数量》金额
				
//				状态及其数量，各部门，各科室数量，总资产，总盘点数量
				String brand = asset.getBrand();
				String generalName = asset.getGeneralName();
				timeUtiles time_ = new timeUtiles();
				get0Time = time_.get0Time();
				
//				盘点
				if(!StringUtils.isEmpty(asset.getCheck())) {
					int compare_result = time_.compareDateByString(get0Time,asset.getCheck());
					if(compare_result < 0) {
						examined++;
					}
				}
//				金额
//				if(!StringUtils.isEmpty(asset.getMoney())) {
//					int money = asset.getMoney()
//					注意金额还需要*个数
//					int amount = Integer.parseInt(asset.getAmount());
//					
//					aggregate += money*amount;
//					
//				}
//				资产状态
				if(!StringUtils.isEmpty(asset.getStatus())) {
//					正常,维修,报废
					if(asset.getStatus().equals("正常")) {
						normal++;
					}else if (asset.getStatus().equals("在修")) {
						maintain++;
					}else if(asset.getStatus().equals("待报废")) {
						tobescrap++;
					}else if(asset.getStatus().equals("已报废")) {
						scrap++;
					}

				}
				totalCheck++;
			}
			unexamined = totalCheck-examined;
			
			
		LogUtil.logger.info(examined+"已盘点数量"+unexamined+"总计="+aggregate
				+"normal"+normal+"main="+maintain+"待报废"+tobescrap+"已报废"+scrap);
		Map map = new HashMap<>();
		map.put("已盘点数量", examined);
		map.put("未盘点数量", unexamined);
		map.put("合计", aggregate);
		map.put("正常", normal);
		map.put("在修", maintain);
		map.put("待报废", tobescrap);
		map.put("已报废", scrap);
		
		asset_num.add(map);
			
    	System.out.println(asset_num);
		
    }
    
    @Test
    public void testcheck(){
		Asset asset = new Asset();
    	asset.setMac("AC233FA018E7");
    	asset.setLocation("yanghan2");
    	
//    	更新实时路径位置
    	assetService.updateLocation(asset);
    }
    @Test
    public void testcheck2(){
    	
    }
    
    @Test
    public void testgetmark(){
    	Asset assetdatas = new Asset();
    	assetdatas.setAssetName("zckp00000000001");
    	List asset_department = new ArrayList<>();
		try {
			asset_department = assetService.queryDepartmentByAsset("1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    	System.out.println(asset_department);
    	
    }
    
    
    
    
    
    
    
    @Test
    public void testimage(){
//    	Asset assetdatas = new Asset();
    	String assetID= "cs111117";
//    	assetdatas.setAssetID(assetID);
    	List<Asset> queryImageByID = assetService.queryImageByID(assetID);
    	for (Asset asset : queryImageByID) {
			
    		System.out.println(asset.getCompressImageUrl());
		}
    	
    }
    @Test
    public void testQueryAsset() throws Exception{
    	
//    	计算表中数量和单价的合计
    	List<Map> list = assetService.findE6();
    	long count = list.stream().filter(a -> "AC233FA4313C".equals(a.get("mac"))).count();
    	System.out.println(count);
    	
    }
    @Test
    public void testList(){
    	
    	List<Asset> searchAssert = assetService.searchAssert("1");
		JSONArray assetjson = JsonUtils.list2jsonArray(searchAssert);
		String mac = "";
		try {
//			通过revied获取所在网关，通过网关在获取地理位置
			for (int i = 0; i < assetjson.size(); i++) {
				mac = (String) assetjson.getJSONObject(i).get("mac");
				List<Map> searchRG = reService.searchRG(mac);
				String location = "";
				for (Map map : searchRG) {
					try {
						location = (String) map.get("location");
						assetjson.getJSONObject(i).put("location", location);
					} catch (Exception e) {
//						// TODO: handle exception
						LogUtil.logger.info("位置获取异常"+location);
					}
				}
				
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
    	
    }
    @Test
    public void testTime() throws InterruptedException{
    	TimeUnit.SECONDS.sleep(10);//睡眠10秒执行
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
    	
    	double d1 = Double.parseDouble("3.1545");
    	double d2 = Double.parseDouble("5.1555");
    	String result1 = String.format("%.2f",d1);
    	String result2 = String.format("%.2f",d2);
    	double f1 = Double.parseDouble(result1);
    	double f2 = Double.parseDouble(result2);
    	System.out.println(f1+"="+f2);
    }
//    图片压缩测试
    @Test
	public void imageFileToBase64() throws Exception {
		String filePath="D:\\test\\1530508706282.jpg";
//		压缩文件大小
		String imageStr = ImageUtils.resizeImage(filePath, 60, 300, 420);
		
		String imagesPath = "D:\\test\\images\\"+System.currentTimeMillis()+".png";
		
		GenerateImage(imageStr,imagesPath);
		
	    Asset as = new Asset();
	    as.setCompressImageUrl("sdfsdf");
	    as.setAssetID("cs111116");
    	assetService.insertAssetDatas(as);
	}
//    base64转为图片
    public static boolean GenerateImage(String imgData, String imgFilePath) throws IOException { // 对字节数组字符串进行Base64解码并生成图片
        if (imgData == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            out = new FileOutputStream(imgFilePath);
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            out.write(b);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            return true;
        }
    }
    
}
*/
/*package com.example.demo.service;

import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.activemq.transport.stomp.StompConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.controller.ApiServerController;
import com.example.demo.domain.IbeaconGateway;
import com.example.demo.domain.IbeaconHistory;
import com.example.demo.domain.Received;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.timeUtiles;


@RunWith(SpringRunner.class)
@SpringBootTest
public class IbeaconServiceTest {
	@Autowired
    private IbeaconGatewayService ibeaconService;
	@Autowired
	private IbeaconHistoryService ibeaconHistoryService;

    @Test
    public void testString(){
    	
    	String s = "D:/workspace/cipbak/springbootdemo02/target//";
    	s = s.substring(0, s.length() - 8);
    	System.out.println(s+"=s");
    }
    @Test
    public void inserDatas(){
    	IbeaconGateway ibeacon = new IbeaconGateway();
    	ibeacon.setBattery("75");
    	ibeaconService.insert(ibeacon);
    }
//    插入监听
    @Test
    public void inserRecevied() throws ParseException{
//    	Received re = new Received ();
//    	re.setCreatetime("2019-08-16 12:26:48");
//    	re.setGatewaymac("AC233FC001EA");
//    	re.setMac("AC233FA018E7");
//    	ibeaconService.saveMac(re);
    	
    	IbeaconGateway ib = new IbeaconGateway(); 
		ib.setMac("AC233FA018E3");
		ib.setGatewaymac("AC233FC001OF");
    	List<Map> ibDatas = ibeaconService.findIbGatewayByMac(ib);
//		说明该数据为空，从新添加（起止时间2000，当前时间设置为未来3秒,减少添加数据）
//			获取最强网关，sql已经排序获取最后一条就是该数据
			JSONArray ibData= JSONArray.parseArray(JSON.toJSONString(ibDatas));
			String iboldGatewayMac = "";
			String iboldMac = "";
			String iboldRssi = "";
			String ibmaxRssiUpdateTime = "";
			
			System.out.println(ibDatas+"=ibdatas");
//			去重，获取相同网关最新时间，更据最大时间更新该网关所对应的数据，不同网关同时也要更新
			for (int k = 0; k < ibData.size(); k++) {
	    		iboldGatewayMac = ibData.getJSONObject(k).getString("gatewaymac");
	    		iboldMac = ibData.getJSONObject(k).getString("mac");
	    		iboldRssi =  ibData.getJSONObject(k).getString("rssi"); //rssi此处最大需要替换
	    		ibmaxRssiUpdateTime = ibData.getJSONObject(k).getString("maxRssiUpdateTime");
//	    		循环判断并且更新符合条件的数据
	    	}
			System.out.println("iboldGatewayMac"+iboldGatewayMac+", iboldMac"+ iboldMac+", iboldRssi, "+iboldRssi);
    }
    @Test
    public void updatetime(){
    	Received re = new Received ();
    	re.setUpdatetime("2019-08-17 12:26:48");
    	re.setGatewaymac("AC233FC001EA");
    	re.setMac("AC233FA018E7");
    	ibeaconService.updateRecevied(re);
    }
//    一般数据监听
    @Test
    public void findRecevied(){
//    	String gatewaymac = "AC233FC001EA"; 
    	String mac = "AC233FA031A7"; 
//    	List<Map> datas = ibeaconService.findReceviedByMac(gatewaymac,mac);
    	List<Map> receviedData = ibeaconService.findReceviedByMac(mac);
    	System.out.println(receviedData.size()+"data-size");
    	if(receviedData.size() > 0) {
			String oldrssi = "";
			String oldmac = "";
			String oldgatewaymac = "";
			for (Map map : receviedData) {
				oldrssi = (String) map.get("rssi");
				oldmac = (String) map.get("mac");
				oldgatewaymac = (String) map.get("gatewaymac");
			}
			System.out.println(oldgatewaymac+"=oldgatewaymac");
    	}
    }
    @Test
    public void queryWay(){
    
    }
    @Test
    public void queryTime() throws ParseException{
    	
    	String mac = "AC233FA018FB";
    	String startTime = "2019-08-14 16:05:51";
    	
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =df.parse(startTime);

//		date.setTime(date.getTime() + 1000); 加1秒
		date.setTime(date.getTime() - 1*60*1000);  //减少1分钟
		System.out.println("当前时间      ："+df.format(date));
        
    	String endTime = "2019-08-14 16:06:51";
    	List findByTime = ibeaconService.find(mac, startTime, endTime);
    	System.out.println(findByTime+"=findByTime");
    	JSONArray gatewayDatas= JSONArray.parseArray(JSON.toJSONString(findByTime));
//    	默认初始值
    	int maxRssi = -10000;
    	String newGateway = "";
//    	3.对不变数据进行比较，并添加到数据库
    	for (int j = 0; j < gatewayDatas.size(); j++) {
    		String oldTime = gatewayDatas.getJSONObject(j).getString("timestamp");
    		String oldGatewayMac = gatewayDatas.getJSONObject(j).getString("gatewaymac");
    		String oldMac = gatewayDatas.getJSONObject(j).getString("mac");
    		String oldRssi = gatewayDatas.getJSONObject(j).getString("rssi");
//    		获取标签在一分钟内最大rssi值对应的网关
    		int oldRssi1 = Integer.parseInt(oldRssi);
    		if(oldRssi!="" && maxRssi<oldRssi1) {
    			maxRssi = oldRssi1;
    			newGateway = oldGatewayMac;
    		}
    	}
//    	循环结束返回最大rssi值,所对应的的网关
    	System.out.println(newGateway+"=newgateway=maxRssi"+maxRssi);
    	
    }
    @Test
    public void time(){

    	//此方法是将2017-11-18T07:12:06.615Z格式的时间转化为秒为单位的Long类型。2019-08-14 09:46:56，2019-08-14 09:46:59
    	String time =  "2019-08-14T01:46:59Z";
    	time = time.replace("Z", " UTC");//UTC是本地时间
    	SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
    	Date d = null;
    	try {
    		d = format.parse(time);
    	} catch (ParseException e) {
    	// TODO Auto-generated catch block
    	e.printStackTrace();
    	}
    	//此处是将date类型装换为字符串类型，比如：Sat Nov 18 15:12:06 CST 2017转换为2017-11-18 15:12:06
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String date = sf.format(d);
    	System.out.println(d.getTime()/1000);//这里输出的是以秒为单位的long类型的时间。如果需要一毫秒为单位，可以不用除1000.
    	System.out.println(sf.format(d));//这里输出的是字符串类型的时间
    }
    @Test
    public void testAbs(){
//    	String str = "-90";
//    	int a = Integer.parseInt(str);
//    	int abs = Math.abs(a);
//    	System.out.println(abs);
    	int rssi1 = Integer.parseInt("-900");
    	int rssi2 = Integer.parseInt("-100");
    	System.out.println(rssi1<rssi2);
    	
    }
    @Test
    public void testUpdate(){
    	IbeaconGateway ibeacon = new IbeaconGateway();
//        	更新时间
		ibeacon.setMaxRssiUpdateTime("2019-19-15 16:22:53");
		
    	ibeacon.setRssi("20");
    	ibeacon.setDistance("2.0");
//    		    	电量通过特定数值一直按每月减少多少数值，写入数据库
    	ibeacon.setBattery("10");
    	ibeacon.setMac("AC233FA031F7");
    	ibeacon.setGatewaymac("AC233FC001EA");
    	ibeacon.setTimestamp("2019-08-15 14:22:53");
    	
    	ibeaconService.updateRssi(ibeacon);
    	
    }
    @Test
    public void testUpdatemax(){
    	String gatewaymac = "AC233FC001EA";
    	String mac = "AC233FA018E3";
    	String maxRssiUpdateTime = "2019-10-13 14:08:56";
    	String oldmaxRssiUpdateTime = "2019-10-13 14:08:28";
    	String rssi = "-36";
    	String distance = "2.1";
    	ibeaconService.updateRssiAndTime(mac,
				 gatewaymac, oldmaxRssiUpdateTime,
				 rssi, distance
				, maxRssiUpdateTime);
   	
    	
    }
    @Test
    public void testQueryDatas(){
    	IbeaconGateway ib = new IbeaconGateway();
    	ib.setMac("AC233FA42F98");
    	List<Map> findIbGatewayByMac = ibeaconService.findIbGatewayByMac(ib);
    	System.out.println(findIbGatewayByMac);
    }
    @Test
    public void testDate(){
    	Date date = new Date();// 转换为标准时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
        String date1 = sdf.format(date);
  	    System.out.println(date1+""+"===date");
    }
    @Test
    public void testOneDay1(){
    	 try {
             // 建立Stomp协议的连接
             StompConnection con = new StompConnection();
             Socket so = new Socket("192.168.0.54", 61689);
             con.open(so);
             // 注意，协议版本可以是1.2，也可以是1.1
             con.setVersion("1.2");
             // 用户名和密码，这个不必多说了
             con.connect("admin", "admin");

             // 以下发送一条信息（您也可以使用“事务”方式）
             con.send("/test", "生产者发送的消息");
         } catch (Exception e) {
             e.printStackTrace(System.out);
         }
    	
    }
    
    @Test
    public void testReceived(){
    	String gatewaymac = "AC233FC001EA";
    	String mac = "AC233FA018FB";
    	String startTime = "2019-08-16 12:26:47";
    	String currentTime = "2019-08-16 12:29:34";
//    	List<Map> macdata = ibeaconService.findMacByMac(gatewaymac,mac,currentTime,startTime);
    	List<Map> macdata = ibeaconService.findMacByMac(mac,currentTime,startTime);
    	System.out.println(macdata+"==macdata");
    }
    @Test
    public void testupdate(){
    	String gatewaymac = "AC233FC001EA";
    	String currnettime = "2019-08-20 12:22:47";
    	String startTime = "2019-08-20 12:17:47";
    	ibeaconService.updateGatewayTime(gatewaymac,currnettime,"online");
    }
    @Test
    public void testmiss(){
    	String oneMinuteTime = "2019-08-29 16:30:50";
    	List<Map> assetMiss = ibeaconService.findMissing(oneMinuteTime);
    	JSONArray array= JSONArray.parseArray(JSON.toJSONString(assetMiss));
    	System.out.println(array+"=array");
    }
    @Test
    public void testgatewaymiss(){
//    	网关监听
    	List<Map> gatewayMiss = ibeaconService.findGatewayOline();
    	JSONArray array= JSONArray.parseArray(JSON.toJSONString(gatewayMiss));
    	System.out.println(array+"=array"+gatewayMiss.size());
    }
    @Test
    public void testgatewaymissByTime() throws ParseException{
    	timeUtiles timeUtile = new timeUtiles();
    	Date date = new Date();// 转换为标准时间对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
        String date1 = sdf.format(date);
        ApiServerController apiserver = new ApiServerController();
        String fiveTime = timeUtile.timeReduce(date1, 5*60);
        System.out.println(fiveTime+"=fiveTime");
//    	网关监听
    	List<Map> gatewayMiss = ibeaconService.findGatewayOlineByTime(fiveTime);
    	JSONArray array= JSONArray.parseArray(JSON.toJSONString(gatewayMiss));
    	System.out.println(array+"=array"+gatewayMiss.size());
    }
//    查询未更新最后一条位置记录
    
    @Test
    public void testLastLocation() throws ParseException{
    	List<Map> searchLastDatas = ibeaconService.searchLastDatas();
    	List<Map> list = new ArrayList<>();
    	for (Map datas : searchLastDatas) {
    		Map<String, Object> map = new HashMap<String, Object>();
    		System.out.println(datas+"=datas");
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
    	System.out.println(list+"=list");
		JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));
    }
    
}
*/
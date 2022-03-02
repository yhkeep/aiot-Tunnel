/*package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Maintainhistory;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.FileUtil.FlileUtile;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IMaintainHistroyTest {
	@Autowired
	private MaintainHistoryService maintainService;
	
	@Test
    public void testQueryAllAsset() throws Exception{
		String maintainhistoryonlyCode = "zckp00000000001";
		String starthandtime = "2020-11-13 16:10:37";
		String endhandtime = "2020-11-16 14:52:18";
//		图片路径切换为base64
		List<Map> queryUnionMaintainHistory = maintainService.queryUnionMaintainHistory(maintainhistoryonlyCode, starthandtime, endhandtime);
		
		JSONArray assetjson = new JSONArray();
		assetjson = JsonUtils.list2jsonArray(queryUnionMaintainHistory);
//		System.out.println(assetjson);
    }
	
    
}
*/
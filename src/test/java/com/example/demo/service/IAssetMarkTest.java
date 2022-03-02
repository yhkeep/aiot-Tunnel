/*package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.StartUpApplication;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Check;
import com.example.demo.util.ImageUtils;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.ReduceTime;
import com.example.demo.util.timeUtiles;
import com.example.demo.websocket.WebSocketHandler;
import com.github.pagehelper.PageInfo;

import sun.misc.BASE64Decoder;



@RunWith(SpringRunner.class)
@SpringBootTest
public class IAssetMarkTest {
	@Autowired
    private AssetService assetService;
	@Autowired
	private ReceivedService receivedService;
	@Test
    public void testQueryAllAsset() throws Exception{
		List<Map> findAssetByRece = assetService.findAssetByRece("2020-08-14 19:09:20");
		System.out.println(findAssetByRece);
    }
	
    
}
*/
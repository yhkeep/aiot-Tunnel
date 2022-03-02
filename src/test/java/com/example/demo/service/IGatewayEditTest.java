/*package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
import com.example.demo.controller.UserController;
import com.example.demo.domain.Asset;
import com.example.demo.domain.Gateway;
import com.example.demo.domain.GatewayEdit;
import com.example.demo.util.ImageUtils;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.timeUtiles;
import com.example.demo.websocket.WebSocketHandler;
import com.github.pagehelper.PageInfo;

import sun.misc.BASE64Decoder;



@RunWith(SpringRunner.class)
@SpringBootTest
public class IGatewayEditTest {
	@Autowired
    private GatewayService gatewayService;
	
	@Test
	public void testEdit() {
		GatewayEdit ge = new GatewayEdit();
		ge.setGatewaymac("ab");
		System.out.println(ge);
	}
	@Test
	public void testGateway() {
		List<Gateway> gatewayData = gatewayService.searchGateway("1");
		
		for (Gateway gateway : gatewayData) {
			LogUtil.logger.info(gateway+"");
			if(!StringUtils.isEmpty(gateway.getType())) {
				
			}
			
		}
	}
    
}
*/
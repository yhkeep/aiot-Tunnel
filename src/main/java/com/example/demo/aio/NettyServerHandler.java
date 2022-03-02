package com.example.demo.aio;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.domain.User;
import com.example.demo.netty.HttpHandler;

public class NettyServerHandler extends HttpHandler{
	private List<User> aip = new ArrayList<>();
//	异步查询数据
	public void findHandler() {
//		aip = new AioProgress().findUser();
		
	}
//	添加数据
	public void addHandler(User user) throws Exception {
		new AioProgress().addUser(user);
	}
	
}
package com.example.demo.aio;

import com.example.demo.netty.HttpServer;

public class NettyServer extends HttpServer{

	public void startNetty(int port) throws Exception {
		super.start(port);
	}
	
}
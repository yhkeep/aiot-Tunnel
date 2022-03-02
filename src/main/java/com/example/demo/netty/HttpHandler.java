package com.example.demo.netty;

import com.example.demo.aio.AioProgress;
import com.example.demo.guava.GuavaThread;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;

/**
 * 
 * TODO single
 */
public class HttpHandler extends BaseHttpHandler { 
	
//  文本类型
	private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;
	
	@Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
//        System.out.println("class:" + msg.getClass().getName());
      //TODO 处理非nio 8633端口 null指针异常
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(read().toString().getBytes()));//缓存中获取数据
        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
        ctx.flush(); //刷流
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        System.out.println("exceptionCaught");
        if(null != cause) cause.printStackTrace();
        if(null != ctx) ctx.close();
        
    }
//TODO 从本地缓存中获取数据 
    public String cacheBuffer() {
    	final GuavaThread<String,String> guavaThread=new GuavaThread<String,String>();
 		Object cache = guavaThread.getCache("liping");
    	return cache + "";
    }
//	redis分布式缓存
    public StringBuilder read() {
    	

    	String loadRedis = new AioProgress().readRedis("liping");
    	
    	StringBuilder sb =  new StringBuilder(loadRedis+"[HttpHandler read Method]");
    	
    	return sb;
    }
}
package com.example.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer implements HttpServerInterface{
//	懒汉线程安全单例模式
    
    public void start(int port) throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline()
                                .addLast("decoder", new HttpRequestDecoder())   //解码request
                                .addLast("encoder", new HttpResponseEncoder())  //编码response
                                .addLast("aggregator", new HttpObjectAggregator(512 * 1024)) //聚合的消息长度不超过512
                                .addLast("handler", new HttpHandler());       // 添加自定义处理接口
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128) // 确定队列的连接数
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        b.bind(port).sync();
    }
}
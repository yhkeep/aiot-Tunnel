package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.demo.controller.GatewayController;
import com.example.demo.netty.HttpServer;
import com.example.demo.util.LogUtil;
import com.example.demo.util.MusicThread;
import com.example.demo.util.FileUtil.FlileUtile;
import com.example.demo.util.sockettherd.SocketService;

//优化启动
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan(basePackages = {"com.example.demo"})
//@MapperScan扫描mapper
//public class StartUpApplication implements CommandLineRunner{  实现之后通过application.properties配置openwire端口，默认该协议，实现run方法
@SpringBootApplication
@MapperScan("com.example.demo.mapper")
//开启定时
@EnableScheduling
@ImportResource("classpath:applicationContext.xml")
public class StartUpApplication{
	/*@Autowired
    JmsTemplate  jmsTemplate;*/
	
	
	
    public static void main(String[] args) throws Exception {
    	
    	
    	
        SpringApplication.run(StartUpApplication.class, args);
//      首次启动mqtt服务器连接,启动发布所有网关数据   ,开始定时启动订阅取消其他一切保护机制
        GatewayController gateway = new GatewayController();
        gateway.initAllGateway();
        

//     超低温服务 //TODO0430定位中资源消耗过大已造成内存溢出（可分布服务器减压）
        
      SocketService socketService = new SocketService();
      socketService.manyServer();
         /*
//      netty 读写服务器
        HttpServer httpServer = new HttpServer();
        try {
			httpServer.start(8633);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.logger.error("netty服务启动异常:"+e);
		}*/
        

        

        
    }
//    CommandLineRunner接口中的run方法，是在程序启动后就会执行的代码。JmsTemplate 是用来操作JMS消息的操作类。
    /*@Override
    public void run(String... args) throws Exception {
            jmsTemplate.send("my-destination1",new ReportController());
    }*/
    
}

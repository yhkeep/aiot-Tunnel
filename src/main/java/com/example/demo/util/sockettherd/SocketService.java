package com.example.demo.util.sockettherd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.demo.util.LogUtil;

public class SocketService {
  /*  public static void main(String[] args) throws IOException{
        SocketService socketService = new SocketService();
        socketService.manyServer();
    }	*/
    //    监听多个客户端
    public void manyServer() throws IOException{
        boolean flag = true;
         ServerSocket serverSocket = new ServerSocket(5300);
         while(flag){
        	 try {
        		 Socket accept = serverSocket.accept();
        		 //TODO210506疑是温湿度内存消耗过大，是否在于线程等待过多，过长（或是温湿度表中数据过多）
        		 SocketServerTherd socketServerTherd = new SocketServerTherd(accept);
        		 socketServerTherd.start();
//        		new SocketServerTherd(serverSocket.accept()).start();
			} catch (Exception e) {
				// TODO: handle exception
				serverSocket.close();
			}
         }
         serverSocket.close();
   }
    
}
package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FlileTest {
	
	
	
	public static void main(String[] args) throws IOException {
		 Resource resource = new ClassPathResource("RW.txt");
	        InputStream is = resource.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	        String data = null;
	        while((data = br.readLine()) != null) {
	            System.out.println(data);
	        }
	        
	        br.close();
	        isr.close();
	        is.close();
	        
	}
}
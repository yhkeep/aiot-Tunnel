package com.example.demo.util.excetion;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.StartUpApplication;

public class LogutilExcetion {
	public static Logger logger=LoggerFactory.getLogger(StartUpApplication.class);
	
	
	public static String getExceptionInfo(Exception e){
        ByteArrayOutputStream byteOutStrem = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(byteOutStrem));
	    return byteOutStrem.toString();
	}
}


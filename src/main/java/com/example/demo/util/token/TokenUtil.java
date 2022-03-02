package com.example.demo.util.token;


import java.util.Date;
import java.util.Random;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.StartUpApplication;
import com.example.demo.domain.User;

public class TokenUtil {
	private static volatile TokenUtil token = null;
//	重新登陆时限45分钟
	private static final long EXPIRE_TIME = 45* 60 * 1000;
	
	private TokenUtil() {}
	
	public static TokenUtil getTokenInstall() {
		if(token == null) {
			synchronized (TokenUtil.class) {
				if(token == null) {
					token = new TokenUtil();
				}
			}
		}
		return token; 
	}
//		生成token版本号
	/*public static String getToken(User user) {
		Random rand = new Random();
		int version = 0;
		for(int i=0; i<1; i++) {
			version = rand.nextInt(10)+1;
		}
		String vi = version+"";
		
//		放入用户名，设置token过期时间
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		String token="";
		token= JWT.create().withAudience(user.getUsername(),user.getAddress(),vi).withExpiresAt(date)
				.sign(Algorithm.HMAC256(user.getPassword()));
		return token;
	}*/
	
//	TODO0326生成token,存入用户名，地址，版本号,部门
	public static String getToken(User user) {
		Random rand = new Random();
		int version = 0;
		for(int i=0; i<1; i++) {
			version = rand.nextInt(10)+1;
		}
		String vi = version+"";
		
//		放入用户名，设置token过期时间
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		String token="";
		token= JWT.create().withAudience(user.getUsername(),user.getAddress(),vi,user.getDepartmentroom()).withExpiresAt(date)
				.sign(Algorithm.HMAC256(user.getPassword()));
		return token;
	}
	
}

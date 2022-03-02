package com.example.demo.Interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.util.LogUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
  
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        
        
        HandlerMethod handlerMethod=(HandlerMethod)object;
//      获取方法
        Method method = handlerMethod.getMethod();
        
//        LogUtil.logger.info("method="+method);
    	
        
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        
     // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
      //获取servlet路径
        String servletPath = httpServletRequest.getServletPath(); 
        

        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
            

        }else {
        	// 执行认证，必须返回是否通过认证，否则token失效后，依旧能够访问
            if (token == null) {
            	return false;
            }
         // 获取 token 中的 user Audience中的配置 ，存在失效过期token并判断
            User loginUser = new User();
            String username;
            String address;
            String vi;
            try {
            	username = JWT.decode(token).getAudience().get(0);
            	address = JWT.decode(token).getAudience().get(1);
            	vi = JWT.decode(token).getAudience().get(2);
            	loginUser = userService.findUserByUsername(username);
            } catch (JWTDecodeException j) {
            	httpServletResponse.setHeader("message", "user is not authorized");
            	httpServletResponse.setStatus(401);
            	return false;
            }
            if (loginUser == null) {
            	httpServletResponse.setHeader("message", "user not exist");
//                throw new RuntimeException("用户不存在，请重新登录");
                return false;
            }
            // 验证 token Algorithm中密码的配置
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(loginUser.getPassword())).build();
            try {
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
//            	没有开通其访问权限
            	httpServletResponse.setHeader("message", "authorized failed");
            	httpServletResponse.setStatus(401);
            	return false;
            }
//          通过登录传递过来的路徑信息，比较其是否与数据库授权路径一致，然后执行token放行。如跳过登录直接访问，则直接返回false
//            boolean flag = false;
            
            List<Map> searchUser = userService.findRole(loginUser);
        	for (Map map : searchUser) {
//            	同一时间账号同时登录,版本号不一致则强制下线
            	String newpassword = loginUser.getNewpassword();
            	if(!StringUtils.isEmpty(newpassword) && !vi.equals(newpassword)) {
            		httpServletResponse.setHeader("message", "Forced offline");
            		return false;
            	}
        		
        		/*
        		 * TODO-0319 (粗粒度权限拦截)
        		 * if(servletPath.equals(map.get("path"))) {
        			httpServletResponse.setHeader("message", "ok");
        			return true; 
        		}*/
            	
//            	权限路径
            	String rolepath = map.get("rolepath").toString();
            	if(StringUtils.isEmpty(rolepath)) {
            		continue;
            	}
    			List<String> list= Arrays.asList(rolepath.split(",")).stream().map(s -> {
        			String trim = s.trim();
        			return trim;
        		}).collect(Collectors.toList());
    			for (String dbpath: list) {
//    				不能使用包含关系，会造成访问权限异常
    				if(dbpath.equals(servletPath)) {
    					LogUtil.logger.info("servletPath="+servletPath);
    					httpServletResponse.setHeader("message", "ok");
    					return true;
    				}
    			}
//            	httpServletResponse.setHeader("message", "authorized failed");
//            	return false;
    		}
        	httpServletResponse.setHeader("message", "authorized failed");
        	return false;
        	
        }
        httpServletResponse.setHeader("message", "authorized failed");
      //最后拦截
        return false;
        
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, 
                                  HttpServletResponse httpServletResponse, 
                            Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, 
                                          HttpServletResponse httpServletResponse, 
                                          Object o, Exception e) throws Exception {
    }
    
}
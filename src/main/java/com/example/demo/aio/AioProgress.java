package com.example.demo.aio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.basecompoent.BaseCompoent;
import com.example.demo.domain.User;
import com.example.demo.redis.RedisUtils;
import com.example.demo.service.UserService;

@Component
public class AioProgress<K> extends BaseCompoent{
	
//	父类服务层
	@Autowired
    private UserService userService;
	
	@Resource
    private RedisUtils redisUtil;
	
	public static AioProgress task;
	
	@PostConstruct
    public void init() {
		task = this;
    }
	
	
	
    public List<User> findUser(User keyValue) {
    	User searchUser = task.userService.findUser(keyValue);
    	List<User> data = new ArrayList<User>();
    	data.add(searchUser);
		return data;	
	}
    
    public User addUser(User user) throws Exception {
    	task.userService.updateUser(user);
    	return user;
    }

//    存入缓存
    public boolean loadRedis() {
    	
    	String key = "liping";
    	String value = "123";
        return task.redisUtil.set(key,value);
    } 
    
    public String readRedis(String username) {
    	
    	Object object = task.redisUtil.get(username);
    	if(object == null) {
    		loadRedis();
    		object = task.redisUtil.get(username);
    	}
    	
    	return object+"";
    } 
    
//	gc回收
	
	
    
}
	

 


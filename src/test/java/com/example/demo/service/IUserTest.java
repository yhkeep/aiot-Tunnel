package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.User;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;



@RunWith(SpringRunner.class)
@SpringBootTest
public class IUserTest {
	@Autowired
    private UserService userService;
//查询所有用户
    @Test
    public void testUser(){
    	List<User> searchUser = userService.searchUser("2");
    	for (User user : searchUser) {
    		String username = user.getUsername();
		}
		JSONArray userjson = JsonUtils.list2jsonArray(searchUser);
    	List<String> ls = new ArrayList<>();
    	ls.add("test");
    	String a = "test1";
    	boolean contains = ls.contains(a);
    	
    	System.out.println(contains);
		
    	
    }
//登录
    @Test
    public void testLoginUser(){
    	User user = new User();
    	user.setUsername("huaxi");
    	user.setPassword("123456789");
    	User searchUser = userService.findUser(user);
    	System.out.println(searchUser);
    	
    }
    @Test
    public void testRole(){
    	User user = new User();
    	user.setUsername("02800016");
    	user.setPassword("123147");
    	user.setAddress("1");
    	List<Map> searchUser = userService.findRole(user);
    	boolean flag = false;
    	String path = "/huaxi/asset";
    	for (Map map : searchUser) {
			String rolepath = map.get("rolepath").toString();
			List<String> list= Arrays.asList(rolepath.split(",")).stream().map(s -> {
    			String trim = s.trim();
    			return trim;
    		}).collect(Collectors.toList());
			for (String dbpath: list) {
				if(dbpath.equals(path)) {
					LogUtil.logger.info("path="+path);
					flag = true;
				}
			}
		}
//    	return flag;
    	
    	
    	
    }
    @Test
    public void testAddUser(){
    	User  userdata = new User();
    	userdata.setUsername("yikecha");
    	userdata.setPassword("123456");
    	userdata.setDepartment("huaxi");
    	userdata.setPhone("123456");
    	userdata.setAddress("2");
    	userdata.setRole("admin");
    	userdata.setRolepath("/huaxi/asset,/huaxi/user/query");
    	userService.insertUser(userdata);
    	
    }
    @Test
    public void testUpdateUser(){
    	User userinfo = new User();
		userinfo.setUsername("0280001");
		userinfo.setPhone("13");
		userinfo.setRole("admin");
//		userinfo.setPassword("123456");
//		userinfo.setRolepath("/huaxi/user");
//		userinfo.setAuthority("[1,2]");
		
		LogUtil.logger.info(userinfo+"");
		userService.updateUser(userinfo);
    	
    }
    @Test
    public void testDelUser(){
    	List<String> user = new ArrayList<String>();
    	user.add("zhonghe");
    	user.add("yikecha");
    	userService.delUser(user);
    	
    }
    @Test
    public void testByPhoneUser(){
    	String username = "huaxi";
    	User findUserByPhone = userService.findUserByUsername(username);
    	System.out.println(findUserByPhone+"=finduserbyphtone");
    }
    @Test
    public void testiter(){
            HashSet<String> set = new HashSet<String>();
            set.add("zq");
            set.add("allen");
            set.add("alex");
            for(String s : set){
                System.out.println(s);
            }
            Iterator<String> iterator = set.iterator();
            while(iterator.hasNext()){
                String s = iterator.next();
                if(s.equals("alex")){
                    iterator.remove();
                }
            }
            System.out.println(set);

    	
    }
    
    
}

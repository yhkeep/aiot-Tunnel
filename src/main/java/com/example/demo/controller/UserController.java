package com.example.demo.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.domain.Check;
import com.example.demo.domain.Result;
import com.example.demo.domain.User;
import com.example.demo.service.AssetService;
import com.example.demo.service.UserService;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.LogUtil;
import com.example.demo.util.parameter.UserParameterField;
import com.example.demo.util.result.ResultUtil;
import com.example.demo.util.token.TokenUtil;

@RestController
public class UserController{
//    医院地址存放
//  public static String address = "1"; //  TODO 210918线程不安全，可使用token中包含地址（同院可忽略）

	public static String token = "";  //   TODO 21-08-19线程不安全,取消全局变量，影响websocket数据首次发送
    
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
    private AssetService assetService;
	
//	管理员用户查询
	@RequestMapping(value = "/huaxi/user/query", method = RequestMethod.GET)
    public JSONArray findUser() {
		List<User> searchUser = userService.searchUser(UserParameterField.address);
		List<User> adminUser = searchUser.parallelStream().map(user -> {
			user.setPassword("");
			return user;
		}).collect(Collectors.toList());
		JSONArray userjson = JsonUtils.list2jsonArray(adminUser);
		return userjson;	
	}
//	删除用户
	@RequestMapping(value = "/huaxi/user/del", method = RequestMethod.POST)
	public String deleteAsset(@RequestParam(value="username",required=true)List username) {
		
		JSONObject result = new JSONObject();
		try {
			userService.delUser(username);
			String token_ = httpServletRequest.getHeader("token");  //抽取单例线程安全公共类
			String token_username = JWT.decode(token_).getAudience().get(0); //用户名
			String token_address = JWT.decode(token_).getAudience().get(1); //项目地址
//			删除已登录账号，强制弹出
			if(username.contains(token_username)) {
				result.put("msg", "remove_myself");
			}else {
				result.put("msg", "ok");
			}
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "failed");
		}
//		返回删除成功与失败！
		return result.toJSONString();	
	}
	
/*	TODO210819 线程安全修改前 ，删除用户，并且强制退出
 * @RequestMapping(value = "/huaxi/user/del", method = RequestMethod.POST)
	public String deleteAsset(@RequestParam(value="username",required=true)List username) {
		
		JSONObject result = new JSONObject();
		try {
			userService.delUser(username);
			token = httpServletRequest.getHeader("token");  //抽取单例线程安全公共类
			String token_username = JWT.decode(token).getAudience().get(0); //用户名
			String token_address = JWT.decode(token).getAudience().get(1); //项目地址
//			删除已登录账号，强制弹出
			if(username.contains(token_username)) {
				result.put("msg", "remove_myself");
			}else {
				result.put("msg", "ok");
			}
		} catch (Exception e) {
			// TODO: handle exception
			result.put("msg", "failed");
		}
//		返回删除成功与失败！
		return result.toJSONString();	
	}
*/	
	
//	添加用户 ,密码需要加密保存，以及所拥有的权限，地址，申请部门
	@RequestMapping(value = "/huaxi/user/add", method = RequestMethod.POST)
	public Result addUser(@RequestBody User user) {
//		添加用户时注意事项。不能添加用户名已存在的用户
		List<User> searchUser = userService.searchUser(UserParameterField.address);
		String address = UserParameterField.address;
		try {
			if(!StringUtils.isEmpty(address)) {
				user.setAddress(address);
		    	for (User u : searchUser) {
		    		String username = u.getUsername();
		    		if(username.equals(user.getUsername())) {
//		    			用户已经存在或者添加权限路径为空
		    			return ResultUtil.error(400, "用户信息已存在");
		    		}
		    		if(StringUtils.isEmpty(user.getRolepath())) {
		    			return ResultUtil.error(301, "用户信息不正确");
		    		}
				}
//		    	管理员权限路径全部添加，操作员部分添加
				userService.insertUser(user);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return ResultUtil.error(500, "添加用户异常");
		}
		return ResultUtil.success();	
	}
	
//	获取所有资产中，部门种类,提供权限筛选
	@RequestMapping(value = "/huaxi/departmentType", method = RequestMethod.POST)
	public Result queryDepartmentType(@RequestBody User user) {
		
		String token = httpServletRequest.getHeader("token");  //抽取单例线程安全公共类
		String token_address = JWT.decode(token).getAudience().get(1); //项目地址
		JSONArray dempartment_json = new JSONArray();
		try {
			if(!StringUtils.isEmpty(token_address) && user.getAddress().equals(token_address)) {
				List asset_department = assetService.queryDepartmentByAsset(token_address);
				dempartment_json = JsonUtils.list2jsonArray(asset_department);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return ResultUtil.error(500, "部门查询异常");
		}
		return ResultUtil.success(dempartment_json);	
	}
	
	
	
//	登录时需要传入地址
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JSONObject loginUser(@RequestBody User user,HttpSession session) {
		
		JSONObject jsonObject=new JSONObject();
        User userInfo=userService.findUser(user);
//        token不为空并且与新传入用户进行比较，防止重复登录。
        if(userInfo==null){
            jsonObject.put("message","登录失败,账户或密码错误");
            return jsonObject;
        }else {
            if (!userInfo.getPassword().equals(user.getPassword())){
                jsonObject.put("message","登录失败,密码错误");
                return jsonObject;
            }else {
//                验证成功存放地址
//            	address = userInfo.getAddress();
//            	存放session中
//            	session.setAttribute("address", address);
//            	session.setAttribute("userinfo", user);
            	
//            	同一时间账号同时登录,线程安全
            	token = TokenUtil.getTokenInstall().getToken(userInfo);
            	
            	
                jsonObject.put("token", token);
                
                Map<String, String> userinfoByToken = getUserViByToken(token);
                
                String vi = userinfoByToken.get("vi");
                String username = userinfoByToken.get("username");
                User newuser = new User();
                newuser.setUsername(username);
                newuser.setNewpassword(vi);
//              持久化数据库,更新版本号newpassword
                userService.updateUser(newuser);
                userInfo.setPassword("");
                jsonObject.put("user", userInfo);
                return jsonObject;
            }
        }
	}
	
//	用户密码以及权限url更新（根据用户账户进行更新）
	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public Result updateUser(@RequestBody User user) {
		String token = "";
		String username = "";
		String address = "";
		String msg = "";
		try {
			token = httpServletRequest.getHeader("token");
			username = JWT.decode(token).getAudience().get(0); //用户名
			address = JWT.decode(token).getAudience().get(1); //项目地址
//			用户信息双重验证,或则密码token验证
			User userInfo=userService.findUser(user);
//			username,address符合则修改,密码修改，须有新密码，覆盖原有密码,修改自身密码
			if(user.getUsername().equals(username) && userInfo != null  && !StringUtils.isEmpty(user.getNewpassword())) {
				String newpassword = user.getNewpassword();
				user.setPassword(newpassword);
				msg = "remove_myself";
			}else if(StringUtils.isEmpty(user.getNewpassword())){
//				不修改密码
				msg = "success";
			}
			userService.updateUser(user);
			return ResultUtil.success(0,msg,null);
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.logger.info("用户密码或权限更新异常");
//			修改失败
			return ResultUtil.error(500, "用户信息更新异常");
		}
		
	}
	
//TODO 管理员用户权限管理 、增加、删除、修改。修改时间，修改人
	
	
//	获取token中用户信息
	public Map<String,String> getUserViByToken(String token) {
    	Map map = new HashMap<>();
    	String username = "";
		String address = "";
		String vi = "";
		String departmentroom = "";
    	username = JWT.decode(token).getAudience().get(0); //用户名
    	address = JWT.decode(token).getAudience().get(1); //项目地址
    	vi = JWT.decode(token).getAudience().get(2); //项目地址
    	departmentroom = JWT.decode(token).getAudience().get(3); //用户所在部门
    	map.put("username", username);
    	map.put("address", address);
    	map.put("vi", vi);
    	map.put("departmentroom", departmentroom);
		return map;
    }
	
//	普通/admin用户权限编辑
	/*@RequestMapping(value = "/huaxi/user/role/edit", method = RequestMethod.POST)
	public String User( @RequestBody User user) {
		
	}*/
	
  }
	

 
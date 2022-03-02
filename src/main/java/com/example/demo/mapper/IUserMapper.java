package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.User;

public interface IUserMapper {
//	用户添加
	void addUser(User user);
//    批量删除
    void delUser(@Param("username") List username);
//    用户查询
    List<User> queryUser(String address);
//    角色查询
    List<Map> searchRole(User user);
//    登录用户
    User loginUser(User user);
    
    User serachUserByPhone(String username);
    
    void update(User user);
}

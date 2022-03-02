package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.User;

public interface UserService {
	List<User> searchUser(String address);
	
	List<Map> findRole(User user);
	
	User findUserByUsername(String username);
	
	User findUser(User user);
	
	
	void insertUser(User userdata);
	
	void delUser(List<String> username);
	
	void updateUser(User user);
	
}

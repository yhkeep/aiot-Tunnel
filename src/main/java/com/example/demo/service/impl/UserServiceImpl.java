package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.mapper.IUserMapper;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private IUserMapper   userMapper;
	
	@Override
	public List<User> searchUser(String address) {
		// TODO Auto-generated method stub
		List<User> queryUser = userMapper.queryUser(address);
		return queryUser;
	}

	@Override
	public void insertUser(User userdata) {
		// TODO Auto-generated method stub
		userMapper.addUser(userdata);
	}

	@Override
	public void delUser(List<String> username) {
		// TODO Auto-generated method stub
		userMapper.delUser(username);
	}

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		User userinfo = userMapper.serachUserByPhone(username);
		return userinfo;
	}

	@Override
	public User findUser(User user) {
		// TODO Auto-generated method stub
		User loginUser = userMapper.loginUser(user);
		return loginUser;
	}

	@Override
	public List<Map> findRole(User user) {
		// TODO Auto-generated method stub
		List<Map> role = userMapper.searchRole(user);
		return role;
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		userMapper.update(user);
	}


}

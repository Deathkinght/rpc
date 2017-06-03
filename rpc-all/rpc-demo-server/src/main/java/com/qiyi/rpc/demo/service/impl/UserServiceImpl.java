package com.qiyi.rpc.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.qiyi.rpc.demo.dto.UserDto;
import com.qiyi.rpc.demo.service.UserService;


@Service("userService")
public class UserServiceImpl implements UserService {

	private static Map<Integer,UserDto> users = new HashMap<>();
	private static volatile int id = 101;
	
	private Object obj = new Object();
	
	static{
		for(int i=1;i<=100;i++)
		{
			UserDto u = new UserDto();
			u.setAddress("aaa"+i);
			u.setAge(i);
			u.setId(i);
			u.setName("name"+i);
			users.put(i,u);
		}
	}
	

	@Override
	public void addUser(UserDto user) {
		synchronized (obj) {
			users.put(id++, user);
		}
	}

	@Override
	public List<UserDto> getUsers() {
		return new ArrayList<>(users.values());
	}

	@Override
	public UserDto getUser(int userId) {
		return users.get(userId);
	}

	@Override
	public UserDto queryUsers(String name, int age, String address,boolean check) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

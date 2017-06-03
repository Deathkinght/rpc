package com.qiyi.rpc.demo.service;

import java.util.List;

import com.qiyi.rpc.demo.annotation.Param;
import com.qiyi.rpc.demo.dto.UserDto;

public interface UserService {

	
   void addUser(@Param("user")UserDto user);
   
   List<UserDto> getUsers();
   
   UserDto getUser(@Param("userId")int userId);
	
   public UserDto queryUsers(@Param("name")String name,@Param("age")int age,@Param("address")String address,@Param("boolean")boolean check);
}

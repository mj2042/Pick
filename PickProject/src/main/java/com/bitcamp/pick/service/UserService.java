package com.bitcamp.pick.service;

import java.util.List;
import java.util.Map;

import com.bitcamp.pick.domain.User;

public interface UserService {
	public int addUser(User user) throws Exception;
	public void updateUser(User user) throws Exception;
	public User getUserByUserEmail(String userEmail) throws Exception;
	public User getUserByUserNo(int userNo) throws Exception;
	public User getUserByUserName(String userName) throws Exception;
	public Map<String,Object> loginUser(User user) throws Exception;
	public List<User> getUserList() throws Exception;
	 
}

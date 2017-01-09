package com.bitcamp.pick.dao;

import java.util.List;

import com.bitcamp.pick.domain.User;

public interface UserDao {
	public int addUser(User user) throws Exception;
	public void updateUser(User user) throws Exception;
	public User getUserByUserEmail(String userEmail) throws Exception;
	public User getUserByUserName(String userName) throws Exception;
	public User getUserByUserNo(int userNo) throws Exception;
	public List<User> getUserList() throws Exception;
}

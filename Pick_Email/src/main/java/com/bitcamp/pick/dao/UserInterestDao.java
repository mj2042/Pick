package com.bitcamp.pick.dao;

public interface UserInterestDao {
	public int addUserInterest(int userNo,int interestNo) throws Exception;
	public int deleteUserInterest(int userNo) throws Exception;
}

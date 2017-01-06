package com.bitcamp.pick.dao;

import java.util.List;

import com.bitcamp.pick.domain.Interest;

public interface InterestDao {
	public int addInterest(Interest interest) throws Exception;
	public Interest getInterestByContent(String content) throws Exception;
	public Interest getInterestByInterestNo(int interestNo) throws Exception;
	public List<Interest> getInterestList() throws Exception;
}

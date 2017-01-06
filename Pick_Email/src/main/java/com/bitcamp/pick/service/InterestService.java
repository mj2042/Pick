package com.bitcamp.pick.service;

import java.util.List;

import com.bitcamp.pick.domain.Interest;

public interface InterestService {
	public int addInterest(Interest interest) throws Exception;
	public Interest getInterestByContent(String content) throws Exception;
	public Interest getInterestByInterestNo(int interestNo) throws Exception;
	public List<Interest> getInterestList() throws Exception;
} 

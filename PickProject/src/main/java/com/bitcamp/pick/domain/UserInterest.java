package com.bitcamp.pick.domain;

public class UserInterest {
	private int userNo;
	private int interestNo;
	

	public UserInterest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public int getInterestNo() {
		return interestNo;
	}
	public void setInterestNo(int interestNo) {
		this.interestNo = interestNo;
	}

	@Override
	public String toString() {
		return "UserInterest [userNo=" + userNo + ", interestNo=" + interestNo + "]";
	}
	
}

package com.bitcamp.pick.domain;

public class ChoiceUser {
	
	int choiceNo;
	int userNo;
	
	public ChoiceUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChoiceUser(int choiceNo, int userNo) {
		super();
		this.choiceNo = choiceNo;
		this.userNo = userNo;
	}
	public int getChoiceNo() {
		return choiceNo;
	}
	public void setChoiceNo(int choiceNo) {
		this.choiceNo = choiceNo;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	@Override
	public String toString() {
		return "ChoiceUser [choiceNo=" + choiceNo + ", userNo=" + userNo + "]";
	}
	
	
}

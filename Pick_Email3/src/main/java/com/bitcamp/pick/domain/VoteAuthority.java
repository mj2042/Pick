package com.bitcamp.pick.domain;

public class VoteAuthority {
	int voteAuthorityNo;
	int voteNo;
	boolean male;
	boolean female;
	boolean one;
	boolean two;
	boolean three;
	boolean four;
	boolean five;
	boolean six;
	public VoteAuthority() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getVoteAuthorityNo() {
		return voteAuthorityNo;
	}
	public void setVoteAuthorityNo(int voteAuthorityNo) {
		this.voteAuthorityNo = voteAuthorityNo;
	}
	public int getVoteNo() {
		return voteNo;
	}
	public void setVoteNo(int voteNo) {
		this.voteNo = voteNo;
	}
	public boolean isMale() {
		return male;
	}
	public void setMale(boolean male) {
		this.male = male;
	}
	public boolean isFemale() {
		return female;
	}
	public void setFemale(boolean female) {
		this.female = female;
	}
	public boolean isOne() {
		return one;
	}
	public void setOne(boolean one) {
		this.one = one;
	}
	public boolean isTwo() {
		return two;
	}
	public void setTwo(boolean two) {
		this.two = two;
	}
	public boolean isThree() {
		return three;
	}
	public void setThree(boolean three) {
		this.three = three;
	}
	public boolean isFour() {
		return four;
	}
	public void setFour(boolean four) {
		this.four = four;
	}
	public boolean isFive() {
		return five;
	}
	public void setFive(boolean five) {
		this.five = five;
	}
	public boolean isSix() {
		return six;
	}
	public void setSix(boolean six) {
		this.six = six;
	}
	@Override
	public String toString() {
		return "VoteAuthority [voteAuthorityNo=" + voteAuthorityNo + ", voteNo=" + voteNo + ", male=" + male
				+ ", female=" + female + ", one=" + one + ", two=" + two + ", three=" + three + ", four=" + four
				+ ", five=" + five + ", six=" + six + "]";
	}
	
}

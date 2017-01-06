package com.bitcamp.pick.domain;

public class Choice {

	private int choiceNo;
	private int voteNo;
	private String photo;
	private String content;
	private int choiceCount;

	public Choice() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getChoiceNo() {
		return choiceNo;
	}

	public void setChoiceNo(int choiceNo) {
		this.choiceNo = choiceNo;
	}

	public int getVoteNo() {
		return voteNo;
	}

	public void setVoteNo(int voteNo) {
		this.voteNo = voteNo;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getChoiceCount() {
		return choiceCount;
	}

	public void setChoiceCount(int choiceCount) {
		this.choiceCount = choiceCount;
	}

	@Override
	public String toString() {
		return "Choice [choiceNo=" + choiceNo + ", voteNo=" + voteNo + ", photo=" + photo + ", content=" + content
				+ ", choiceCount=" + choiceCount + "]";
	}

	
	
}
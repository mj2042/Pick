package com.bitcamp.pick.domain;

import java.sql.Timestamp;


public class Comment {
	
	private int commentNo;
	private int voteNo;
	private int userNo;
	private String commentContent;
	private Timestamp regDate;
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getCommentNo() {
		return commentNo;
	}
	public void setCommentNo(int commentNo) {
		this.commentNo = commentNo;
	}
	public int getVoteNo() {
		return voteNo;
	}
	public void setVoteNo(int voteNo) {
		this.voteNo = voteNo;
	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public Timestamp getRegDate() {
		return regDate;
	}
	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}
	@Override
	public String toString() {
		return "Comment [commentNo=" + commentNo + ", voteNo=" + voteNo + ", userNo=" + userNo + ", commentContent="
				+ commentContent + ", regDate=" + regDate + "]";
	}
	
	

	
}
	
	

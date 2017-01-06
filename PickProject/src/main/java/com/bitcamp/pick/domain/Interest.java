package com.bitcamp.pick.domain;

import java.util.List;

public class Interest {
	private int interestNo;
	private String content;
	private String interestPhoto;
	
	
	public Interest(int interestNo) {
		super();
		this.interestNo = interestNo;
	}
	public Interest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getInterestNo() {
		return interestNo;
	}
	public void setInterestNo(int interestNo) {
		this.interestNo = interestNo;
	}
	public Interest(String content, String interestPhoto) {
		super();
		this.content = content;
		this.interestPhoto = interestPhoto;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getInterestPhoto() {
		return interestPhoto;
	}
	
	public void setInterestPhoto(String interestPhoto) {
		this.interestPhoto = interestPhoto;
	}
	
	@Override
	public String toString() {
		return "Interest [interestNo=" + interestNo + ", content=" + content + ", interestPhoto=" + interestPhoto + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + interestNo;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interest other = (Interest) obj;
		if (interestNo != other.interestNo)
			return false;
		return true;
	}
	
	
}

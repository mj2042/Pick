package com.bitcamp.pick.service;

import java.util.List;

import com.bitcamp.pick.domain.Choice;


public interface ChoiceService {
	
	public void addChoice(Choice choice)throws Exception;
	public List<Choice> getChoiceListByVoteNo(int voteNo)throws Exception;
	public Choice getChoiceByChoiceNo(int choiceNo)throws Exception;
	public void updateChoiceCount(Choice choice,int userNo)throws Exception;
	public List<Integer> getUserNoListByChoiceNo(int choiceNo) throws Exception;

	
}
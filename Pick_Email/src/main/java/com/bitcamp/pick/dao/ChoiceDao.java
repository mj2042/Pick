package com.bitcamp.pick.dao;

import java.util.List;

import com.bitcamp.pick.domain.Choice;




public interface ChoiceDao {
	public void addChoice(Choice choice)throws Exception ;
	public List<Choice> getChoiceListByVoteNo(int voteNo)throws Exception; 
	public Choice getChoiceByChoiceNo(int choiceNo)throws Exception;
	public void updateChoiceCount(Choice choice)throws Exception;
	
}